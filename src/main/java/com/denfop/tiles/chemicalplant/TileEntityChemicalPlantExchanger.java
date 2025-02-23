package com.denfop.tiles.chemicalplant;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.componets.Fluids;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.gui.GuiChemicalExchanger;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityChemicalPlantExchanger extends TileEntityMultiBlockElement implements IExchanger {

    private final Fluids fluids;
    private final Fluids.InternalFluidTank fluidTank;

    public TileEntityChemicalPlantExchanger() {
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank = this.fluids.addTank("fluids", 10000);
        this.fluidTank.setAcceptedFluids(Fluids.fluidPredicate(FluidName.fluidiodine.getInstance()));
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
        return new GuiChemicalExchanger(this.getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockChemicalPlant.chemical_plant_exchanger;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.chemicalPlant;
    }

    @Override
    public Fluids.InternalFluidTank getFluidTank() {
        return this.fluidTank;
    }

}
