package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.container.ContainerCyclotronElectrostaticDeflector;
import com.denfop.gui.GuiCyclotronElectrostaticDeflector;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCyclotronElectrostaticDeflector extends TileEntityMultiBlockElement implements IElectrostaticDeflector {


    private final InventoryOutput outputSlot;

    public TileEntityCyclotronElectrostaticDeflector() {
        this.outputSlot = new InventoryOutput(this, 1);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public ContainerCyclotronElectrostaticDeflector getGuiContainer(final EntityPlayer var1) {
        return new ContainerCyclotronElectrostaticDeflector(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCyclotronElectrostaticDeflector(getGuiContainer(var1));
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_electrostatic_deflector;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }


    @Override
    public InventoryOutput getOutputSlot() {
        return outputSlot;
    }

}
