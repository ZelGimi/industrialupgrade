package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.container.ContainerPlasticPlateCreator;
import com.denfop.gui.GuiPlasticPlateCreator;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.TileBasePlasticPlateCreator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TilePlasticPlateCreator extends TileBasePlasticPlateCreator implements IUpdateTick, IHasRecipe {

    public TilePlasticPlateCreator() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "plasticplate", this, this.fluidTank);
        this.componentProcess.setInvSlotRecipes(inputSlotA);
        this.inputSlotA.setInvSlotConsumableLiquidByList(this.fluidSlot);
        Recipes.recipes.addInitRecipes(this);
    }

    public void init() {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidoxy.getInstance(), 1000),
                input.getInput(new ItemStack(IUItem.plast))
        ), new RecipeOutput(null, new ItemStack(IUItem.plastic_plate))));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine2.plastic_plate_creator;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine1;
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockPlasticPlateCreator.name");
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @Override
    public ContainerPlasticPlateCreator getGuiContainer(final EntityPlayer entityPlayer) {
        return new ContainerPlasticPlateCreator(entityPlayer, this);
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPlasticPlateCreator(new ContainerPlasticPlateCreator(entityPlayer, this));

    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.plastic_plate.getSoundEvent();
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(
                UpgradableProperty.Processing,
                UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage,
                UpgradableProperty.ItemConsuming,
                UpgradableProperty.ItemProducing,
                UpgradableProperty.FluidConsuming
        );
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
    }

}
