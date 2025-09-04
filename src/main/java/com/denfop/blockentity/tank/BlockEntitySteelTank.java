package com.denfop.blockentity.tank;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.base.BlockEntityLiquedTank;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuTank;
import com.denfop.inventory.Inventory;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntitySteelTank extends BlockEntityLiquedTank {

    public BlockEntitySteelTank(BlockPos pos, BlockState state) {
        super(10, BlockBaseMachine3Entity.steel_tank, pos, state);
        this.containerslot1.setTypeItemSlot(Inventory.TypeItemSlot.NONE);
    }

    @Override
    public ContainerMenuTank getGuiContainer(final Player entityPlayer) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return null;
    }

    public MultiBlockEntity getTeBlock() {
        return BlockBaseMachine3Entity.steel_tank;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

}
