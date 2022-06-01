package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerPlasticPlateCreator;
import com.denfop.gui.GuiPlasticPlateCreator;
import com.denfop.tiles.base.TileEntityBasePlasticPlateCreator;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.ContainerBase;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityPlasticPlateCreator extends TileEntityBasePlasticPlateCreator implements IUpdateTick {

    public TileEntityPlasticPlateCreator() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "plasticplate", this, this.fluidTank);
    }

    public static void init() {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plasticplate", new BaseMachineRecipe(new Input(
                new FluidStack(FluidName.fluidoxy.getInstance(), 1000),
                input.forStack(new ItemStack(IUItem.plast))
        ), new RecipeOutput(null, new ItemStack(IUItem.plastic_plate))));
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockPlasticPlateCreator.name");
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPlasticPlateCreator(new ContainerPlasticPlateCreator(entityPlayer, this));

    }

    public ContainerBase<? extends TileEntityPlasticPlateCreator> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerPlasticPlateCreator(entityPlayer, this);

    }

    public String getStartSoundFile() {
        return "Machines/plastic_plate.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
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
