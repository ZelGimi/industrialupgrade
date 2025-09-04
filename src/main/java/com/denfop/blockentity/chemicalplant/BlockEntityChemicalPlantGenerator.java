package com.denfop.blockentity.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.otherenergies.common.EnergyType;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChemicalPlantEntity;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuDefaultMultiElement;
import com.denfop.screen.ScreenChemicalGenerator;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityChemicalPlantGenerator extends BlockEntityMultiBlockElement implements IGenerator {

    private final ComponentBaseEnergy energy;

    public BlockEntityChemicalPlantGenerator(BlockPos pos, BlockState state) {
        super(BlockChemicalPlantEntity.chemical_plant_generator, pos, state);
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));

    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockChemicalPlantEntity.chemical_plant_generator;
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
    public ContainerMenuDefaultMultiElement getGuiContainer(final Player var1) {
        return new ContainerMenuDefaultMultiElement(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(final Player var1, final ContainerMenuBase<?> var2) {
        return new ScreenChemicalGenerator((ContainerMenuDefaultMultiElement) var2);
    }

}
