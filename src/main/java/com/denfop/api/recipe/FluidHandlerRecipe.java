package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.List;

public class FluidHandlerRecipe {

    private final Fluids component;
    List<FluidTank> inputTank = new ArrayList<>();
    List<FluidTank> outputTank = new ArrayList<>();
    private BaseFluidMachineRecipe output;
    private IBaseRecipe name;
    private List<BaseFluidMachineRecipe> list_recipe;


    public FluidHandlerRecipe(String name, Fluids fluid_component) {
        this.component = fluid_component;
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name);
        List<Fluids.InternalFluidTank> list = new ArrayList<>();
        fluid_component.getAllTanks().forEach(list::add);
        for (Fluids.InternalFluidTank internalFluidTank : list) {
            if (internalFluidTank.canFill()) {
                inputTank.add(internalFluidTank);
            } else {
                outputTank.add(internalFluidTank);
            }
        }
        this.output = null;
    }

    public void load() {
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name.getName());
        this.getOutput();
    }

    public void setName(String name) {
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        getOutput();
    }

    public Fluids getComponent() {
        return component;
    }

    public void consume() {
        for (int i = 0; i < this.name.getSize(); i++) {
            this.inputTank.get(i).drain(this.output.input.getInputs().get(i), true);
        }
    }

    public BaseFluidMachineRecipe output() {
        return this.output;
    }

    public BaseFluidMachineRecipe getOutput() {
        if (this.output != null) {
            return this.output;
        }
        List<FluidStack> fluidStackList = new ArrayList<>();
        for (FluidTank tank : this.inputTank) {
            if (tank.getFluidAmount() == 0) {
                return null;
            } else {
                fluidStackList.add(tank.getFluid());
            }
            if (fluidStackList.size() == this.name.getSize()) {
                break;
            }
        }
        this.output = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                fluidStackList
        );
        return this.output;

    }


    public void checkOutput() {
        List<FluidStack> fluidStackList = new ArrayList<>();
        for (FluidTank tank : this.inputTank) {
            if (tank.getFluidAmount() == 0) {
                if (this.output != null) {
                    this.output = null;
                }
                return;
            } else {
                fluidStackList.add(tank.getFluid());
            }
            if (fluidStackList.size() == this.name.getSize()) {
                break;
            }
        }
        this.output = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                fluidStackList
        );

    }


}
