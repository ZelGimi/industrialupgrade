package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerFacadeBlock;
import com.denfop.gui.GuiFacadeBlock;
import com.denfop.invslot.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.tiles.base.TileEntityInventory;
import com.denfop.tiles.transport.tiles.TileEntityMultiCable;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class TileEntityFacadeBlock extends TileEntityInventory implements IUpdatableTileEvent {


    public final Inventory stackSlot;

    public TileEntityFacadeBlock() {
        this.stackSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return Block.getBlockFromItem(stack.getItem()) != Blocks.AIR;
            }
        };
        this.stackSlot.setInventoryStackLimit(1);
    }

    @Override
    public ContainerFacadeBlock getGuiContainer(final EntityPlayer var1) {
        return new ContainerFacadeBlock(var1, this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiFacadeBlock(getGuiContainer(var1));
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.facademechanism;
    }

    @Override
    public void updateTileServer(final EntityPlayer var1, final double var2) {
        if (var2 == 0) {
            if (!this.stackSlot.isEmpty()) {
                new PacketSoundPlayer(EnumSound.pen, var1);
                List<BlockPos> list = new ArrayList<>();
                List<TileEntityMultiCable> tileEntityMultiCables = new ArrayList<>();
                for (EnumFacing facing1 : EnumFacing.VALUES) {
                    TileEntity tile = world.getTileEntity(this.pos.offset(facing1));
                    if (tile instanceof TileEntityMultiCable) {
                        tileEntityMultiCables.add((TileEntityMultiCable) tile);
                        list.add(((TileEntityMultiCable) tile).getBlockPos());
                        ((TileEntityMultiCable) tile).rerenderCable(this.stackSlot.get());
                    }
                }
                while (!tileEntityMultiCables.isEmpty()) {
                    final TileEntityMultiCable componemt = tileEntityMultiCables.remove(0);
                    for (EnumFacing facing1 : EnumFacing.VALUES) {
                        TileEntity tile = world.getTileEntity(componemt.getBlockPos().offset(facing1));
                        if (tile instanceof TileEntityMultiCable && !list.contains(tile.getPos())) {
                            tileEntityMultiCables.add((TileEntityMultiCable) tile);
                            list.add(((TileEntityMultiCable) tile).getBlockPos());
                            ((TileEntityMultiCable) tile).rerenderCable(this.stackSlot.get());
                        }
                    }
                }
            }
        } else {
            new PacketSoundPlayer(EnumSound.pen, var1);
            List<BlockPos> list = new ArrayList<>();
            List<TileEntityMultiCable> tileEntityMultiCables = new ArrayList<>();
            for (EnumFacing facing1 : EnumFacing.VALUES) {
                TileEntity tile = world.getTileEntity(this.pos.offset(facing1));
                if (tile instanceof TileEntityMultiCable) {
                    tileEntityMultiCables.add((TileEntityMultiCable) tile);
                    list.add(((TileEntityMultiCable) tile).getBlockPos());
                    ((TileEntityMultiCable) tile).rerenderCable(ItemStack.EMPTY);
                }
            }
            while (!tileEntityMultiCables.isEmpty()) {
                final TileEntityMultiCable componemt = tileEntityMultiCables.remove(0);
                for (EnumFacing facing1 : EnumFacing.VALUES) {
                    TileEntity tile = world.getTileEntity(componemt.getBlockPos().offset(facing1));
                    if (tile instanceof TileEntityMultiCable && !list.contains(tile.getPos())) {
                        tileEntityMultiCables.add((TileEntityMultiCable) tile);
                        list.add(((TileEntityMultiCable) tile).getBlockPos());
                        ((TileEntityMultiCable) tile).rerenderCable(ItemStack.EMPTY);
                    }
                }
            }
        }
    }

}
