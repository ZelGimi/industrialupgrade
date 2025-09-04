package com.denfop.blockentity.cyclotron;

import com.denfop.IUItem;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.blockentity.mechanism.multiblocks.base.BlockEntityMultiBlockElement;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockCyclotronEntity;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuCyclotronChamber;
import com.denfop.screen.ScreenCyclotronChamber;
import com.denfop.screen.ScreenIndustrialUpgrade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockEntityCyclotronChamber extends BlockEntityMultiBlockElement implements IBombardmentChamber, IUpdateTick {


    private final InventoryRecipes inputSlotA;
    private MachineRecipe output;
    private int chance;
    private int cryogen;
    private int positrons;

    public BlockEntityCyclotronChamber(BlockPos pos, BlockState state) {
        super(BlockCyclotronEntity.cyclotron_bombardment_chamber, pos, state);
        this.inputSlotA = new InventoryRecipes(this, "cyclotron", this);
    }

    @Override
    public ContainerMenuCyclotronChamber getGuiContainer(final Player var1) {
        return new ContainerMenuCyclotronChamber(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player var1, ContainerMenuBase<? extends CustomWorldContainer> menu) {
        return new ScreenCyclotronChamber((ContainerMenuCyclotronChamber) menu);
    }

    @Override
    public boolean hasOwnInventory() {
        return true;
    }

    @Override
    public MultiBlockEntity getTeBlock() {
        return BlockCyclotronEntity.cyclotron_bombardment_chamber;
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
    public InventoryRecipes getInputSlot() {
        return inputSlotA;
    }

}
