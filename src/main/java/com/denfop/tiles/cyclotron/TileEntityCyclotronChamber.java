package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.container.ContainerCyclotronChamber;
import com.denfop.gui.GuiCyclotronChamber;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCyclotronChamber extends TileEntityMultiBlockElement implements IBombardmentChamber, IUpdateTick {


    private final InvSlotRecipes inputSlotA;
    private MachineRecipe output;
    private int chance;
    private int cryogen;
    private int positrons;

    public TileEntityCyclotronChamber() {
        this.inputSlotA = new InvSlotRecipes(this, "cyclotron", this);
    }

    @Override
    public ContainerCyclotronChamber getGuiContainer(final EntityPlayer var1) {
        return new ContainerCyclotronChamber(this, var1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(final EntityPlayer var1, final boolean var2) {
        return new GuiCyclotronChamber(getGuiContainer(var1));
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockCyclotron.cyclotron_bombardment_chamber;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.cyclotron;
    }


    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();
        this.chance = 100;
        this.cryogen = 1;
        this.positrons = 1;
        if (this.output != null) {
            this.chance = output.getRecipe().getOutput().metadata.hasKey("chance") ? output
                    .getRecipe()
                    .getOutput().metadata.getInteger(
                            "chance") : 100;
            this.cryogen = output.getRecipe().getOutput().metadata.hasKey("cryogen") ?
                    output.getRecipe().getOutput().metadata.getInteger(
                            "cryogen") : 1;
            this.positrons = output.getRecipe().getOutput().metadata.hasKey("positrons") ?
                    output.getRecipe().getOutput().metadata.getInteger(
                            "positrons") : 1;

        }

        return this.output;
    }

    public int getChance() {
        return chance;
    }

    public int getCryogen() {
        return cryogen;
    }

    public int getPositrons() {
        return positrons;
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            inputSlotA.load();
            this.getOutput();
        }
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        this.chance = 100;
        this.cryogen = 1;
        this.positrons = 1;
        if (this.output != null) {
            this.chance = output.getRecipe().getOutput().metadata.hasKey("chance") ? output
                    .getRecipe()
                    .getOutput().metadata.getInteger(
                            "chance") : 100;
            this.cryogen = output.getRecipe().getOutput().metadata.hasKey("cryogen") ?
                    output.getRecipe().getOutput().metadata.getInteger(
                            "cryogen") : 1;
            this.positrons = output.getRecipe().getOutput().metadata.hasKey("positrons") ?
                    output.getRecipe().getOutput().metadata.getInteger(
                            "positrons") : 1;

        }
    }

    @Override
    public InvSlotRecipes getInputSlot() {
        return inputSlotA;
    }

}
