package com.denfop.blockentity.reactors.water.levelfuel;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.water.ILevelFuel;
import com.denfop.blockentity.reactors.water.controller.BlockEntityMainController;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuLevelFuel;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenLevelFuel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class BlockEntityMainLevelFuel extends BlockEntityMultiBlockElement implements ILevelFuel {

    public BlockEntityMainLevelFuel(MultiBlockEntity tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);

    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenLevelFuel((ContainerMenuLevelFuel) menu);
    }

    @Override
    public ContainerMenuLevelFuel getGuiContainer(final Player var1) {
        return new ContainerMenuLevelFuel((BlockEntityMainController) this.getMain(), var1);
    }

}
