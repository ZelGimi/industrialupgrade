package com.denfop.tiles.mechanism.vending;

import com.denfop.api.gui.IType;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerVending;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiVending;
import com.denfop.invslot.InvSlot;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TileEntityBaseVending extends TileEntityInventory implements IType {

    public final EnumTypeStyle style;
    public final InvSlot invSlotInventoryInput;
    public final InvSlotOutput output;
    public InvSlot invSlotBuy;
    public InvSlot invSlotSell;
    public InvSlot invSlotBuyPrivate;
    public InvSlot invSlotSellPrivate;

    public boolean update;
    public int timer = 0;
    Map<Item, Integer> mapValues = new HashMap<>();

    public TileEntityBaseVending(EnumTypeStyle style, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.style = style;
        this.invSlotBuyPrivate = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, style.ordinal() + 1);
        this.invSlotSellPrivate = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, style.ordinal() + 1);

        this.invSlotBuy = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, style.ordinal() + 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return !invSlotBuyPrivate.get(index).isEmpty() && invSlotBuyPrivate.get(index).is(stack.getItem());
            }
        };
        this.invSlotSell = new InvSlot(this, InvSlot.TypeItemSlot.OUTPUT, style.ordinal() + 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return false;
            }
        };
        this.invSlotInventoryInput = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 18) {
            @Override
            public void onChanged() {
                super.onChanged();
                update = true;
            }
        };
        this.output = new InvSlotOutput(this, 18);
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        timer += 5;
        return super.onActivated(player, hand, side, vec3);
    }


    public void updateItems() {
        mapValues.clear();
        for (ItemStack stack : invSlotInventoryInput) {
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
            InvSlot slot = (InvSlot) DecoderHandler.decode(customPacketBuffer);
            InvSlot slot1 = (InvSlot) DecoderHandler.decode(customPacketBuffer);
            for (int i = 0; i < slot.size(); i++) {
                this.invSlotBuyPrivate.set(i, slot.get(i));
                this.invSlotSellPrivate.set(i, slot1.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        try {
            EncoderHandler.encode(customPacketBuffer, this.invSlotBuyPrivate);
            EncoderHandler.encode(customPacketBuffer, this.invSlotSellPrivate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return customPacketBuffer;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiVending((ContainerVending) menu, this.getComponentPrivate().getPlayers().contains(var1.getName().getString()));


    }

    @Override
    public ContainerVending getGuiContainer(final Player var1) {
        return new ContainerVending(this, var1, this.getComponentPrivate().getPlayers().contains(var1.getName().getString()));
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
        for (int i = 0; i < this.invSlotBuy.size(); i++) {
            ItemStack stack = this.invSlotBuy.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack privateStack = this.invSlotBuyPrivate.get(i);
            if (privateStack.isEmpty()) {
                continue;
            }
            if (!stack.is(privateStack.getItem())) {
                return;
            }
            ItemStack privateSell = this.invSlotSellPrivate.get(i);
            if (privateSell.isEmpty()) {
                continue;
            }
            ItemStack output = this.invSlotSell.get(i);

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
                for (ItemStack stack1 : invSlotInventoryInput) {
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
                this.invSlotSell.add(ModUtils.setSize(privateSell, countCan1 * privateSell.getCount()));
                this.updateItems();
            }
        }


    }


    @Override
    public EnumTypeStyle getStyle() {
        return style;
    }

}




