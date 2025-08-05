package com.denfop.tiles.reactors.heat.socket;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerHeatSocket;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiHeatSocket;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import com.denfop.tiles.reactors.heat.ISocket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityMainSocket extends TileEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public TileEntityMainSocket(int Capacity, IMultiTileBlock block, BlockPos pos, BlockState state) {
        super(block, pos, state);
        this.energy = this.addComponent(Energy.asBasicSource(this, Capacity, 14));
    }

    public Energy getEnergy() {
        return energy;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiHeatSocket((ContainerHeatSocket) menu);
    }

    @Override
    public ContainerHeatSocket getGuiContainer(final Player var1) {
        return new ContainerHeatSocket(this, var1);
    }

}
