package com.denfop.blockentity.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotronEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuPositrons;
import com.denfop.screen.ScreenCyclotronPositrons;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityCyclotronPositrons extends BlockEntityMultiBlockElement implements IPositrons {


    private final ComponentBaseEnergy positrons;

    public BlockEntityCyclotronPositrons(BlockPos pos, BlockState state) {
        super(BlockCyclotronEntity.cyclotron_positrons, pos, state);
        this.positrons = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.POSITRONS, this, 10000));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerMenuPositrons getGuiContainer(final Player var1) {
        return new ContainerMenuPositrons(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCyclotronPositrons((ContainerMenuPositrons) menu);
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCyclotronEntity.cyclotron_positrons;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron.getBlock(getTeBlock());
    }


    @Override
    public ComponentBaseEnergy getPositrons() {
        return positrons;
    }

}
