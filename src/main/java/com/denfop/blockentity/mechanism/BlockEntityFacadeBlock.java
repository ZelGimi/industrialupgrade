package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blockentity.transport.tiles.BlockEntityMultiCable;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuFacadeBlock;
import com.denfop.inventory.Inventory;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.screen.ScreenFacadeBlock;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.sound.EnumSound;
import com.denfop.utils.Localization;
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

public class BlockEntityFacadeBlock extends BlockEntityInventory implements IUpdatableTileEvent {


    public final Inventory stackSlot;

    public BlockEntityFacadeBlock(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.facademechanism, pos, state);
        this.stackSlot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public boolean canPlaceItem(final int index, final ItemStack stack) {
                return Block.byItem(stack.getItem()) != Blocks.AIR;
            }
        };
        this.stackSlot.setStackSizeLimit(1);
    }

    @Override
    public void addInformation(ItemStack stack, List<String> tooltip) {
        super.addInformation(stack, tooltip);
        tooltip.add(Localization.translate("iu.facade_mechanism.info"));
    }

    @Override
    public ContainerMenuFacadeBlock getGuiContainer(final Player var1) {
        return new ContainerMenuFacadeBlock(var1, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenFacadeBlock((ContainerMenuFacadeBlock) menu);
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.facademechanism;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 == 0) {
            if (!this.stackSlot.isEmpty()) {
                new PacketSoundPlayer(EnumSound.pen, var1);
                List<BlockPos> list = new ArrayList<>();
                List<BlockEntityMultiCable> tileEntityMultiCables = new ArrayList<>();
                for (Direction facing1 : Direction.values()) {
                    BlockEntity tile = level.getBlockEntity(this.pos.offset(facing1.getNormal()));
                    if (tile instanceof BlockEntityMultiCable) {
                        tileEntityMultiCables.add((BlockEntityMultiCable) tile);
                        list.add(((BlockEntityMultiCable) tile).getBlockPos());
                        ((BlockEntityMultiCable) tile).rerenderCable(this.stackSlot.get(0));
                    }
                }
                while (!tileEntityMultiCables.isEmpty()) {
                    final BlockEntityMultiCable componemt = tileEntityMultiCables.remove(0);
                    for (Direction facing1 : Direction.values()) {
                        BlockEntity tile = level.getBlockEntity(componemt.getBlockPos().offset(facing1.getNormal()));
                        if (tile instanceof BlockEntityMultiCable && !list.contains(tile.getBlockPos())) {
                            tileEntityMultiCables.add((BlockEntityMultiCable) tile);
                            list.add(((BlockEntityMultiCable) tile).getBlockPos());
                            ((BlockEntityMultiCable) tile).rerenderCable(this.stackSlot.get(0));
                        }
                    }
                }
            }
        } else {
            new PacketSoundPlayer(EnumSound.pen, var1);
            List<BlockPos> list = new ArrayList<>();
            List<BlockEntityMultiCable> tileEntityMultiCables = new ArrayList<>();
            for (Direction facing1 : Direction.values()) {
                BlockEntity tile = level.getBlockEntity(this.pos.offset(facing1.getNormal()));
                if (tile instanceof BlockEntityMultiCable) {
                    tileEntityMultiCables.add((BlockEntityMultiCable) tile);
                    list.add(((BlockEntityMultiCable) tile).getBlockPos());
                    ((BlockEntityMultiCable) tile).rerenderCable(ItemStack.EMPTY);
                }
            }
            while (!tileEntityMultiCables.isEmpty()) {
                final BlockEntityMultiCable componemt = tileEntityMultiCables.remove(0);
                for (Direction facing1 : Direction.values()) {
                    BlockEntity tile = level.getBlockEntity(componemt.getBlockPos().offset(facing1.getNormal()));
                    if (tile instanceof BlockEntityMultiCable && !list.contains(tile.getBlockPos())) {
                        tileEntityMultiCables.add((BlockEntityMultiCable) tile);
                        list.add(((BlockEntityMultiCable) tile).getBlockPos());
                        ((BlockEntityMultiCable) tile).rerenderCable(ItemStack.EMPTY);
                    }
                }
            }
        }
    }

}
