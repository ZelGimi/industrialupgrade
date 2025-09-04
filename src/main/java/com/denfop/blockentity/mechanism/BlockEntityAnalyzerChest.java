package com.denfop.blockentity.mechanism;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.containermenu.ContainerMenuAnalyzerChest;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.screen.ScreenAnalyzerChest;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityAnalyzerChest extends BlockEntityInventory {

    public InventoryOutput outputSlot = new InventoryOutput(this, 36);

    public BlockEntityAnalyzerChest(BlockPos pos, BlockState state) {
        super(BlockBaseMachine3Entity.analyzer_chest, pos, state);
    }

    @Override
    public ContainerMenuAnalyzerChest getGuiContainer(final Player entityPlayer) {
        return new ContainerMenuAnalyzerChest(entityPlayer, this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(final Player entityPlayer, final ContainerMenuBase<? extends CustomWorldContainer> b) {
        return new ScreenAnalyzerChest((ContainerMenuAnalyzerChest) b);
    }

    public MultiBlockEntity getTeBlock() {

        return BlockBaseMachine3Entity.analyzer_chest;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }


}
