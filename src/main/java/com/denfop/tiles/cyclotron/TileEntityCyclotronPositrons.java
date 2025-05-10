package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerPositrons;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiCyclotronPositrons;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityCyclotronPositrons extends TileEntityMultiBlockElement implements IPositrons {


    private final ComponentBaseEnergy positrons;

    public TileEntityCyclotronPositrons(BlockPos pos, BlockState state) {
        super( BlockCyclotron.cyclotron_positrons, pos, state);
        this.positrons = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.POSITRONS, this, 10000));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerPositrons getGuiContainer(final Player var1) {
        return new ContainerPositrons(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCyclotronPositrons((ContainerPositrons) menu);
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_positrons;
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
