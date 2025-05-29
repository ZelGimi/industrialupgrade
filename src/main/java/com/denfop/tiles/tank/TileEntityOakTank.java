package com.denfop.tiles.tank;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerTank;
import com.denfop.gui.GuiCore;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.base.TileEntityLiquedTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityOakTank extends TileEntityLiquedTank {

    public TileEntityOakTank(BlockPos pos, BlockState state) {
        super(4,BlockBaseMachine3.oak_tank,pos,state);
        this.containerslot1.setTypeItemSlot(InvSlot.TypeItemSlot.NONE);
    }

    @Override
    public ContainerTank getGuiContainer(final Player entityPlayer) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player entityPlayer, ContainerBase<? extends IAdvInventory> isAdmin) {
        return null;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.oak_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
