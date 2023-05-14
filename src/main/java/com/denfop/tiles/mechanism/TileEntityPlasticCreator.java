package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerPlasticCreator;
import com.denfop.gui.GuiPlasticCreator;
import com.denfop.tiles.base.TileEntityBasePlasticCreator;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import ic2.core.init.Localization;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityPlasticCreator extends TileEntityBasePlasticCreator implements IHasRecipe {

    public TileEntityPlasticCreator() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "plastic", this, this.fluidTank);
        Recipes.recipes.addInitRecipes(this);
    }

    public void init() {
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidRegistry.WATER, 1000),
                        input.forStack(IUItem.PolyethCell),
                        input.forStack(IUItem.PolypropCell)
                ),
                new RecipeOutput(null, new ItemStack(IUItem.plast))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidRegistry.WATER, 1000),
                        input.forStack(ModUtils.getCellFromFluid("iufluidpolyeth")),
                        input.forStack(ModUtils.getCellFromFluid(
                                "iufluidpolyprop"))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.plast))
        ));
        Recipes.recipes.addRecipe("plastic", new BaseMachineRecipe(
                new Input(
                        new FluidStack(FluidName.fluidazot.getInstance(), 12000),
                        input.forOreDict("blockVitalium"),
                        input.forStack(new ItemStack(IUItem.crafting_elements,1,269))
                ),
                new RecipeOutput(null, new ItemStack(IUItem.crafting_elements,1,270))
        ));
    }

    public String getInventoryName() {

        return Localization.translate("iu.blockPlasticCreator.name");
    }

    public int gaugeLiquidScaled(int i) {
        return this.getFluidTank().getFluidAmount() <= 0
                ? 0
                : this.getFluidTank().getFluidAmount() * i / this.getFluidTank().getCapacity();
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPlasticCreator(new ContainerPlasticCreator(entityPlayer, this));

    }

    public String getStartSoundFile() {
        return "Machines/plastic.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
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

}
