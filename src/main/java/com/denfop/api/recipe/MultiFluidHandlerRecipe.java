package com.denfop.api.recipe;

import com.denfop.api.Recipes;
import com.denfop.componets.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiFluidHandlerRecipe {

    private final Fluids component;
    List<FluidTank> inputTank = new ArrayList<>();
    List<FluidTank> outputTank = new ArrayList<>();
    private BaseFluidMachineRecipe[] output;
    private IBaseRecipe name;
    private List<BaseFluidMachineRecipe> list_recipe;


    public MultiFluidHandlerRecipe(String name, Fluids fluid_component, int col) {
        this.component = fluid_component;
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name);
        List<Fluids.InternalFluidTank> list = new ArrayList<>();
        fluid_component.getAllTanks().forEach(list::add);
        for (Fluids.InternalFluidTank internalFluidTank : list) {
            if (internalFluidTank.isInput()) {
                inputTank.add(internalFluidTank);
            } else {
                outputTank.add(internalFluidTank);
            }
        }
        this.output = new BaseFluidMachineRecipe[col];
    }

    public MultiFluidHandlerRecipe(String name, int col, Fluids.InternalFluidTank... fluid_tank) {
        this.component = null;
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name);
        for (Fluids.InternalFluidTank internalFluidTank : fluid_tank) {
            if (internalFluidTank.isInput()) {
                inputTank.add(internalFluidTank);
            } else {
                outputTank.add(internalFluidTank);
            }
        }
        this.output = new BaseFluidMachineRecipe[col];
    }

    public List<Fluid> getFluids(int i) {
        Set<Fluid> fluids = new HashSet<>();
        for (BaseFluidMachineRecipe machineRecipe : list_recipe) {
            fluids.add(machineRecipe.input.getInputs().get(i).getFluid());
        }
        return new ArrayList<>(fluids);
    }

    public List<Fluid> getOutputFluids(int i) {
        Set<Fluid> fluids = new HashSet<>();
        for (BaseFluidMachineRecipe machineRecipe : list_recipe) {
            fluids.add(machineRecipe.output_fluid.get(i).getFluid());
        }
        return new ArrayList<>(fluids);
    }

    public List<BaseFluidMachineRecipe> getList_recipe() {
        return list_recipe;
    }

    public void load(int col) {
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name.getName());
        this.getOutput(col);
    }

    public void load(ItemStack stack, int col) {
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name.getName());
        this.getOutput(stack, col);
    }

    public void setName(String name, int col) {
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        getOutput(col);
    }

    public Fluids getComponent() {
        return component;
    }

    public boolean canOperate(int col) {
        for (int i = 0; i < this.name.getSize(); i++) {
            if (this.inputTank.get(i).getFluidAmount() < this.output[col].input.getInputs().get(i).amount) {
                return false;
            }
        }
        return true;
    }

    public void consume(int col) {
        for (int i = 0; i < this.name.getSize(); i++) {
            this.inputTank.get(i).drain(this.output[col].input.getInputs().get(i), true);
        }
    }

    public BaseFluidMachineRecipe output(int col) {
        return this.output[col];
    }

    public BaseFluidMachineRecipe getOutput(int col) {
        if (this.output[col] != null) {
            return this.output[col];
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
        this.output[col] = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                fluidStackList
        );
        return this.output[col];

    }

    public BaseFluidMachineRecipe getOutput(ItemStack stack, int col) {
        if (this.output[col] != null) {
            return this.output[col];
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
        this.output[col] = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                stack
        );
        return this.output[col];

    }

    public void checkOutput(int col) {
        List<FluidStack> fluidStackList = new ArrayList<>();
        for (FluidTank tank : this.inputTank) {
            if (tank.getFluidAmount() == 0) {
                if (this.output[col] != null) {
                    this.output[col] = null;
                }
                return;
            } else {
                fluidStackList.add(tank.getFluid());
            }
            if (fluidStackList.size() == this.name.getSize()) {
                break;
            }
        }
        this.output[col] = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                fluidStackList
        );

    }


    public void setOutput(BaseFluidMachineRecipe baseFluidMachineRecipe, int col) {
        this.output[col] = baseFluidMachineRecipe;
    }

    public boolean canFillFluid(int col) {
        if (this.output[col] != null) {
            for (int i = 0; i < outputTank.size(); i++) {
                FluidTank fluidTank = outputTank.get(i);
                if (fluidTank.getFluidAmount() + output[col].output_fluid.get(i).amount > fluidTank.getCapacity() && (fluidTank.getFluid() == null || fluidTank
                        .getFluid()
                        .getFluid() == output[col].output_fluid.get(i).getFluid())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void fillFluid(int col) {
        if (this.output[col] != null) {
            for (int i = 0; i < outputTank.size(); i++) {
                outputTank.get(i).fill(output[col].output_fluid.get(i), true);
            }
        }
    }

    public boolean checkFluids(int col) {
        if (this.output[col] != null) {
            for (FluidTank fluidTank : inputTank) {
                if (fluidTank.getFluidAmount() == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
