package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class FluidRecipes {

    private final IFluidUpdateTick tile;
    private final IFluidMechanism fluidMechanism;
    private final List<BaseFluidMachineRecipe> recipes;

    public FluidRecipes(
            IBaseRecipe baseRecipe,
            IFluidUpdateTick tile,
            IFluidMechanism fluidMechanism
    ) {
        this.tile = tile;
        this.fluidMechanism = fluidMechanism;
        this.recipes = Recipes.recipes.getFluidRecipes(baseRecipe.getName());
    }

    public void put() {
        final BaseFluidMachineRecipe recipe1 = this.process();
        this.tile.setRecipeOutput(recipe1);
        this.tile.onUpdate();
    }

    public boolean can_continue() {
        for (FluidTank tank : this.fluidMechanism.getInputTank()) {
            if (tank.getFluid() == null || tank.getFluidAmount() <= 0) {
                return false;
            }
        }
        for (int i = 0; i < this.fluidMechanism.getOutputTank().size(); i++) {
            FluidTank tank = this.fluidMechanism.getOutputTank().get(i);
            FluidStack fluidStack = this.tile.getRecipeOutput().output.get(i);
            if (tank.getFluidAmount() + fluidStack.amount > tank.getCapacity()) {
                return false;
            }
        }
        return true;
    }

    public BaseFluidMachineRecipe process() {
        for (FluidTank tank : this.fluidMechanism.getInputTank()) {
            if (tank.getFluid() == null || tank.getFluidAmount() <= 0) {
                return null;
            }
        }
        List<FluidStack> fluidStackList = new ArrayList<>();
        for (FluidTank tank : this.fluidMechanism.getInputTank()) {
            fluidStackList.add(tank.getFluid());
        }
        return Recipes.recipes.getFluidRecipe(fluidStackList, this.recipes);
    }

    public void consume() {
        BaseFluidMachineRecipe baseFluidMachineRecipe = this.tile.getRecipeOutput();
        if (baseFluidMachineRecipe == null) {
            throw new NullPointerException();
        }
        List<FluidStack> fluidStackList = baseFluidMachineRecipe.input.getInputs();
        for (int i = 0; i < fluidStackList.size(); i++) {
            this.fluidMechanism.getInputTank().get(i).drain(fluidStackList.get(i).amount, true);
        }
    }

}
