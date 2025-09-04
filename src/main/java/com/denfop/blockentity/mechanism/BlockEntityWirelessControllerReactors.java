package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockBase;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuWirelessControllerReactors;
import com.denfop.inventory.Inventory;
import com.denfop.items.ItemReactorData;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenWirelessControllerReactors;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.LinkedList;
import java.util.List;

public class BlockEntityWirelessControllerReactors extends BlockEntityInventory implements IUpdatableTileEvent {


    public final Inventory invslot;
    public List<ItemStack> itemStacks = new LinkedList<>();

    public List<BlockEntityMultiBlockBase> tileMultiBlockBaseList = new LinkedList<>();

    public BlockEntityWirelessControllerReactors(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.wireless_controller_reactors, pos, state);
        this.invslot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 12) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                updateList();
                return content;
            }

            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                if (!(stack.getItem() instanceof ItemReactorData)) {
                    return false;
                }
                final CompoundTag nbt = ModUtils.nbt(stack);
                return !nbt.getString("name").isEmpty();
            }
        };
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ContainerMenuWirelessControllerReactors getGuiContainer(final Player var1) {
        return new ContainerMenuWirelessControllerReactors(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenWirelessControllerReactors((ContainerMenuWirelessControllerReactors) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.wireless_controller_reactors;
    }


    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {

        }

        super.addInformation(stack, tooltip);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);

        tileMultiBlockBaseList.clear();
        itemStacks.clear();
        for (int i = 0; i < 12; i++) {
            final CompoundTag nbt = ModUtils.nbt(this.invslot.get(i));
            BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            BlockEntity tileEntity = this.getWorld().getBlockEntity(pos);
            if (tileEntity instanceof BlockEntityMultiBlockBase && tileEntity instanceof IAdvReactor) {
                this.tileMultiBlockBaseList.add((BlockEntityMultiBlockBase) tileEntity);
                this.itemStacks.add(((BlockEntityMultiBlockBase) tileEntity).getPickBlock(null, null));
            } else {
                this.tileMultiBlockBaseList.add(null);
                this.itemStacks.add(ItemStack.EMPTY);
            }
        }
    }


    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide) {
            updateList();
        }
    }

    public void updateList() {
        itemStacks.clear();
        tileMultiBlockBaseList.clear();
        for (int i = 0; i < 12; i++) {
            final CompoundTag nbt = ModUtils.nbt(this.invslot.get(i));
            BlockPos pos = new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
            BlockEntity tileEntity = this.getWorld().getBlockEntity(pos);
            if (tileEntity instanceof BlockEntityMultiBlockBase && tileEntity instanceof IAdvReactor) {
                this.tileMultiBlockBaseList.add((BlockEntityMultiBlockBase) tileEntity);
                this.itemStacks.add(((BlockEntityMultiBlockBase) tileEntity).getPickBlock(null, null));
            } else {
                this.tileMultiBlockBaseList.add(null);
                this.itemStacks.add(ItemStack.EMPTY);
            }
        }
    }


    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (!this.itemStacks.get((int) var2).isEmpty()) {
            BlockEntityMultiBlockBase tileMultiBlockBase = this.tileMultiBlockBaseList.get((int) var2);
            if (tileMultiBlockBase != null && tileMultiBlockBase.isFull() && !tileMultiBlockBase.isRemoved()) {
                tileMultiBlockBase.onActivated(var1, var1.getUsedItemHand(), Direction.NORTH, new Vec3(0, 0, 0));

            }
        }
    }

}
