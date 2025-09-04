package com.denfop.blockentity.mechanism.vending;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.widget.IType;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuVending;
import com.denfop.inventory.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenVending;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlockEntityBaseVending extends BlockEntityInventory implements IType {

    public final EnumTypeStyle style;
    public final Inventory inventoryInventoryInput;
    public final InventoryOutput output;
    public Inventory inventoryBuy;
    public Inventory inventorySell;
    public Inventory inventoryBuyPrivate;
    public Inventory inventorySellPrivate;

    public boolean update;
    public int timer = 0;
    Map<Item, Integer> mapValues = new HashMap<>();

    public BlockEntityBaseVending(EnumTypeStyle style, MultiBlockEntity block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.style = style;
        this.inventoryBuyPrivate = new Inventory(this, Inventory.TypeItemSlot.INPUT, style.ordinal() + 1);
        this.inventorySellPrivate = new Inventory(this, Inventory.TypeItemSlot.INPUT, style.ordinal() + 1);

        this.inventoryBuy = new Inventory(this, Inventory.TypeItemSlot.INPUT, style.ordinal() + 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return !inventoryBuyPrivate.get(index).isEmpty() && inventoryBuyPrivate.get(index).is(stack.getItem());
            }
        };
        this.inventorySell = new Inventory(this, Inventory.TypeItemSlot.OUTPUT, style.ordinal() + 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return false;
            }
        };
        this.inventoryInventoryInput = new Inventory(this, Inventory.TypeItemSlot.INPUT, 18) {
            @Override
            public void setChanged() {
                super.setChanged();
                update = true;
            }
        };
        this.output = new InventoryOutput(this, 18);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        timer += 5;
        return super.onActivated(player, hand, side, vec3);
    }


    public void updateItems() {
        mapValues.clear();
        for (ItemStack stack : inventoryInventoryInput) {
            if (!stack.isEmpty()) {
                final Integer map = mapValues.computeIfAbsent(stack.getItem(), k -> 0);
                int value = map + stack.getCount();
                mapValues.replace(stack.getItem(), value);
            }
        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            this.updateItems();
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            Inventory slot = (Inventory) DecoderHandler.decode(customPacketBuffer);
            Inventory slot1 = (Inventory) DecoderHandler.decode(customPacketBuffer);
            for (int i = 0; i < slot.size(); i++) {
                this.inventoryBuyPrivate.set(i, slot.get(i));
                this.inventorySellPrivate.set(i, slot1.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        try {
            EncoderHandler.encode(customPacketBuffer, this.inventoryBuyPrivate);
            EncoderHandler.encode(customPacketBuffer, this.inventorySellPrivate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenVending((ContainerMenuVending) menu, this.getComponentPrivate().getPlayers().contains(var1.getName().getString()));


    }

    @Override
    public ContainerMenuVending getGuiContainer(final Player var1) {
        return new ContainerMenuVending(this, var1, this.getComponentPrivate().getPlayers().contains(var1.getName().getString()));
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (update) {
            this.update = false;
            this.updateItems();
        }
        if (timer > 0) {
            timer--;
        }
        for (int i = 0; i < this.inventoryBuy.size(); i++) {
            ItemStack stack = this.inventoryBuy.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack privateStack = this.inventoryBuyPrivate.get(i);
            if (privateStack.isEmpty()) {
                continue;
            }
            if (!stack.is(privateStack.getItem())) {
                return;
            }
            ItemStack privateSell = this.inventorySellPrivate.get(i);
            if (privateSell.isEmpty()) {
                continue;
            }
            ItemStack output = this.inventorySell.get(i);

            final Integer map = mapValues.computeIfAbsent(privateSell.getItem(), k -> 0);
            if (map == 0) {
                continue;
            }
            int value = map;
            if (value > 0) {
                int countCan = output.isEmpty()
                        ? privateSell.getMaxStackSize() / privateSell.getCount()
                        : (privateSell.getMaxStackSize() - output.getCount()) / privateSell.getCount();

                countCan = Math.min(value / privateSell.getCount(), countCan);
                countCan = Math.min(countCan, stack.getCount() / privateStack.getCount());
                if (countCan == 0) {
                    continue;
                }
                int totalFreeSpace = 0;
                for (ItemStack outStack : this.output) {
                    if (outStack.isEmpty()) {
                        totalFreeSpace += privateStack.getMaxStackSize();
                    } else if (outStack.is(privateStack.getItem())) {
                        totalFreeSpace += privateStack.getMaxStackSize() - outStack.getCount();
                    }
                }


                int maxByOutput = totalFreeSpace / privateStack.getCount();


                final int countCan1 = Math.min(maxByOutput, countCan);
                if (countCan1 == 0) {
                    continue;
                }
                for (ItemStack stack1 : inventoryInventoryInput) {
                    if (countCan == 0) {
                        break;
                    }
                    if (stack1.is(privateSell.getItem())) {
                        int shrink = Math.min(countCan, stack1.getCount() / privateSell.getCount());
                        stack1.shrink(shrink * privateSell.getCount());
                        countCan -= shrink;
                    }
                }
                stack.shrink(countCan1 * privateStack.getCount());
                this.output.add(ModUtils.setSize(privateStack, countCan1 * privateStack.getCount()));
                this.inventorySell.add(ModUtils.setSize(privateSell, countCan1 * privateSell.getCount()));
                this.updateItems();
            }
        }


    }


    @Override
    public EnumTypeStyle getStyle() {
        return style;
    }

}




