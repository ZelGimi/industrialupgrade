package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.reactors.IAdvReactor;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerWirelessControllerReactors;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiWirelessControllerReactors;
import com.denfop.invslot.InvSlot;
import com.denfop.items.ItemReactorData;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import com.denfop.utils.Keyboard;
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

public class TileEntityWirelessControllerReactors extends TileEntityInventory implements IUpdatableTileEvent {


    public final InvSlot invslot;
    public List<ItemStack> itemStacks = new LinkedList<>();

    public List<TileMultiBlockBase> tileMultiBlockBaseList = new LinkedList<>();

    public TileEntityWirelessControllerReactors(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.wireless_controller_reactors,pos,state);
        this.invslot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 12) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                updateList();
                return content;
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
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
    public ContainerWirelessControllerReactors getGuiContainer(final Player var1) {
        return new ContainerWirelessControllerReactors(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiWirelessControllerReactors((ContainerWirelessControllerReactors) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.wireless_controller_reactors;
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
            if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IAdvReactor) {
                this.tileMultiBlockBaseList.add((TileMultiBlockBase) tileEntity);
                this.itemStacks.add(((TileMultiBlockBase) tileEntity).getPickBlock(null, null));
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
            if (tileEntity instanceof TileMultiBlockBase && tileEntity instanceof IAdvReactor) {
                this.tileMultiBlockBaseList.add((TileMultiBlockBase) tileEntity);
                this.itemStacks.add(((TileMultiBlockBase) tileEntity).getPickBlock(null, null));
            } else {
                this.tileMultiBlockBaseList.add(null);
                this.itemStacks.add(ItemStack.EMPTY);
            }
        }
    }


    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (!this.itemStacks.get((int) var2).isEmpty()) {
            TileMultiBlockBase tileMultiBlockBase = this.tileMultiBlockBaseList.get((int) var2);
            if (tileMultiBlockBase != null && tileMultiBlockBase.isFull() && !tileMultiBlockBase.isRemoved()) {
                tileMultiBlockBase.onActivated(var1, var1.getUsedItemHand(), Direction.NORTH, new Vec3(0, 0, 0));

            }
        }
    }

}
