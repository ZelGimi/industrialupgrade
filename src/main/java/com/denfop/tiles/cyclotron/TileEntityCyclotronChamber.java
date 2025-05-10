package com.denfop.tiles.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotron;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerCyclotronChamber;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiCyclotronChamber;
import com.denfop.tiles.mechanism.multiblocks.base.TileEntityMultiBlockElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityCyclotronChamber extends TileEntityMultiBlockElement implements IBombardmentChamber, IUpdateTick {


    private final InvSlotRecipes inputSlotA;
    private MachineRecipe output;
    private int chance;
    private int cryogen;
    private int positrons;

    public TileEntityCyclotronChamber(BlockPos pos, BlockState state) {
        super(BlockCyclotron.cyclotron_bombardment_chamber, pos, state);
        this.inputSlotA = new InvSlotRecipes(this, "cyclotron", this);
    }

    @Override
    public ContainerCyclotronChamber getGuiContainer(final Player var1) {
        return new ContainerCyclotronChamber(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiCyclotronChamber((ContainerCyclotronChamber) menu);
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
        return IUItem.cyclotron.getBlock(getTeBlock());
    }


    public MachineRecipe getOutput() {

        this.output = this.inputSlotA.process();
        this.chance = 100;
        this.cryogen = 1;
        this.positrons = 1;
        if (this.output != null) {
            this.chance = output.getRecipe().getOutput().metadata.contains("chance") ? output
                    .getRecipe()
                    .getOutput().metadata.getInt(
                            "chance") : 100;
            this.cryogen = output.getRecipe().getOutput().metadata.contains("cryogen") ?
                    output.getRecipe().getOutput().metadata.getInt(
                            "cryogen") : 1;
            this.positrons = output.getRecipe().getOutput().metadata.contains("positrons") ?
                    output.getRecipe().getOutput().metadata.getInt(
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
        if (!this.getWorld().isClientSide) {
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
            this.chance = output.getRecipe().getOutput().metadata.contains("chance") ? output
                    .getRecipe()
                    .getOutput().metadata.getInt(
                            "chance") : 100;
            this.cryogen = output.getRecipe().getOutput().metadata.contains("cryogen") ?
                    output.getRecipe().getOutput().metadata.getInt(
                            "cryogen") : 1;
            this.positrons = output.getRecipe().getOutput().metadata.contains("positrons") ?
                    output.getRecipe().getOutput().metadata.getInt(
                            "positrons") : 1;

        }
    }

    @Override
    public InvSlotRecipes getInputSlot() {
        return inputSlotA;
    }

}
