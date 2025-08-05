package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.gui.GuiChemicalGenerator;
import com.denfop.gui.GuiCore;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TileEntityChemicalPlantGenerator extends TileEntityMultiBlockElement implements IGenerator {

    private final ComponentBaseEnergy energy;

    public TileEntityChemicalPlantGenerator(BlockPos pos, BlockState state) {
        super(BlockChemicalPlant.chemical_plant_generator, pos, state);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant.getBlock(getTeBlock().getId());
    }

    @Override
    public ComponentBaseEnergy getEnergy() {
        return energy;
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerDefaultMultiElement getGuiContainer(final Player var1) {
        return new ContainerDefaultMultiElement(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(final Player var1, final ContainerBase<?> var2) {
        return new GuiChemicalGenerator((ContainerDefaultMultiElement) var2);
    }

}
