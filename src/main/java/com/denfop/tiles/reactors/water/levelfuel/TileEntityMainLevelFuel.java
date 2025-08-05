package com.denfop.tiles.reactors.water.levelfuel;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerLevelFuel;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiLevelFuel;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.water.ILevelFuel;
import com.denfop.tiles.reactors.water.controller.TileEntityMainController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityMainLevelFuel extends TileEntityMultiBlockElement implements ILevelFuel {

    public TileEntityMainLevelFuel(IMultiTileBlock tileBlock, BlockPos pos, BlockState state) {
        super(tileBlock, pos, state);

    }

    @Override
    public boolean hasOwnInventory() {
        return this.getMain() != null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiLevelFuel((ContainerLevelFuel) menu);
    }

    @Override
    public ContainerLevelFuel getGuiContainer(final Player var1) {
        return new ContainerLevelFuel((TileEntityMainController) this.getMain(), var1);
    }

}
