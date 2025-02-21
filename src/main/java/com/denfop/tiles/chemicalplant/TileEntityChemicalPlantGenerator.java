package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.gui.GuiChemicalGenerator;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityChemicalPlantGenerator extends TileEntityMultiBlockElement implements IGenerator {

    private final ComponentBaseEnergy energy;

    public TileEntityChemicalPlantGenerator() {
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 1000));

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_generator;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant;
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
    public ContainerDefaultMultiElement getGuiContainer(final EntityPlayer var1) {
        return new ContainerDefaultMultiElement(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiChemicalGenerator(this.getGuiContainer(var1));
    }

}
