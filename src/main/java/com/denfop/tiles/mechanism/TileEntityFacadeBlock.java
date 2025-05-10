package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerFacadeBlock;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiFacadeBlock;
import com.denfop.invslot.InvSlot;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFacadeBlock extends TileEntityInventory implements IUpdatableTileEvent {


    public final InvSlot stackSlot;

    public TileEntityFacadeBlock(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3.facademechanism,pos,state);
        this.stackSlot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return  Block.byItem(stack.getItem()) != Blocks.AIR;
            }
        };
        this.stackSlot.setStackSizeLimit(1);
    }

    @Override
    public ContainerFacadeBlock getGuiContainer(final Player var1) {
        return new ContainerFacadeBlock(var1, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiFacadeBlock((ContainerFacadeBlock) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.facademechanism;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.stackSlot.isEmpty()) {
                new PacketSoundPlayer(EnumSound.pen, var1);
                List<BlockPos> list = new ArrayList<>();
                List<TileEntityMultiCable> tileEntityMultiCables = new ArrayList<>();
                for (Direction facing1 : Direction.values()) {
                    BlockEntity tile = level.getBlockEntity(this.pos.offset(facing1.getNormal()));
                    if (tile instanceof TileEntityMultiCable) {
                        tileEntityMultiCables.add((TileEntityMultiCable) tile);
                        list.add(((TileEntityMultiCable) tile).getBlockPos());
                        ((TileEntityMultiCable) tile).rerenderCable(this.stackSlot.get(0));
                    }
                }
                while (!tileEntityMultiCables.isEmpty()) {
                    final TileEntityMultiCable componemt = tileEntityMultiCables.remove(0);
                    for (Direction facing1 : Direction.values()) {
                        BlockEntity tile = level.getBlockEntity(componemt.getBlockPos().offset(facing1.getNormal()));
                        if (tile instanceof TileEntityMultiCable && !list.contains(tile.getBlockPos())) {
                            tileEntityMultiCables.add((TileEntityMultiCable) tile);
                            list.add(((TileEntityMultiCable) tile).getBlockPos());
                            ((TileEntityMultiCable) tile).rerenderCable(this.stackSlot.get(0));
                        }
                    }
                }
            }
        } else {
            new PacketSoundPlayer(EnumSound.pen, var1);
            List<BlockPos> list = new ArrayList<>();
            List<TileEntityMultiCable> tileEntityMultiCables = new ArrayList<>();
            for (Direction facing1 : Direction.values()) {
                BlockEntity tile = level.getBlockEntity(this.pos.offset(facing1.getNormal()));
                if (tile instanceof TileEntityMultiCable) {
                    tileEntityMultiCables.add((TileEntityMultiCable) tile);
                    list.add(((TileEntityMultiCable) tile).getBlockPos());
                    ((TileEntityMultiCable) tile).rerenderCable(ItemStack.EMPTY);
                }
            }
            while (!tileEntityMultiCables.isEmpty()) {
                final TileEntityMultiCable componemt = tileEntityMultiCables.remove(0);
                for (Direction facing1 : Direction.values()) {
                    BlockEntity tile = level.getBlockEntity(componemt.getBlockPos().offset(facing1.getNormal()));
                    if (tile instanceof TileEntityMultiCable && !list.contains(tile.getBlockPos())) {
                        tileEntityMultiCables.add((TileEntityMultiCable) tile);
                        list.add(((TileEntityMultiCable) tile).getBlockPos());
                        ((TileEntityMultiCable) tile).rerenderCable(ItemStack.EMPTY);
                    }
                }
            }
        }
    }

}
