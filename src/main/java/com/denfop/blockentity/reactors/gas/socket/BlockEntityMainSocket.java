package com.denfop.blockentity.reactors.gas.socket;

import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blockentity.reactors.gas.ISocket;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuGasSocket;
import com.denfop.screen.ScreenGasSocket;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityMainSocket extends BlockEntityMultiBlockElement implements ISocket {

    private final Energy energy;

    public BlockEntityMainSocket(int Capacity, MultiBlockEntity block, BlockPos pos, BlockState state) {
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
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {

        return new ScreenGasSocket((ContainerMenuGasSocket) menu);
    }

    @Override
    public ContainerMenuGasSocket getGuiContainer(final Player var1) {
        return new ContainerMenuGasSocket(this, var1);
    }

}
