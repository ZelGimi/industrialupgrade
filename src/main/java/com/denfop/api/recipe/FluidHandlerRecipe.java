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

public class FluidHandlerRecipe {

    private final Fluids component;
    List<FluidTank> inputTank = new ArrayList<>();
    List<FluidTank> outputTank = new ArrayList<>();
    private BaseFluidMachineRecipe output;
    private IBaseRecipe name;
    private List<BaseFluidMachineRecipe> list_recipe;

    public FluidHandlerRecipe(String name) {
        this.component = null;
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name);
        this.output = null;
    }

    public FluidHandlerRecipe(String name, Fluids fluid_component) {
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
        this.output = null;
    }

    public List<FluidTank> getInputTank() {
        return inputTank;
    }

    public List<FluidTank> getOutputTank() {
        return outputTank;
    }

    public void setTanks(List<Fluids.InternalFluidTank> fluidTanks) {
        inputTank.clear();
        outputTank.clear();
        for (Fluids.InternalFluidTank internalFluidTank : fluidTanks) {
            if (internalFluidTank.isInput()) {
                inputTank.add(internalFluidTank);
            } else {
                outputTank.add(internalFluidTank);
            }
        }
        this.output = null;
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

    public void load() {
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name.getName());
        this.getOutput();
    }

    public void load(ItemStack stack) {
        this.list_recipe = Recipes.recipes.getRecipeFluid().getRecipeList(name.getName());
        this.getOutput(stack);
    }

    public Fluids getComponent() {
        return component;
    }

    public boolean canOperate() {
        for (int i = 0; i < this.name.getSize(); i++) {
            if (this.inputTank.get(i).getFluidAmount() < this.output.input.getInputs().get(i).amount) {
                return false;
            }
        }
        return true;
    }

    public boolean canOperate1(FluidTank fluidTank) {
        for (int i = 0; i < this.name.getSize(); i++) {
            if (fluidTank.getFluidAmount() < this.output.input.getInputs().get(i).amount) {
                return false;
            }
        }
        return true;
    }

    public void consume() {
        for (int i = 0; i < this.name.getSize(); i++) {
            this.inputTank.get(i).drain(this.output.input.getInputs().get(i), true);
        }
    }

    public void consume(FluidTank fluidTank) {
        for (int i = 0; i < this.name.getSize(); i++) {
            fluidTank.drain(this.output.input.getInputs().get(i), true);
        }
    }

    public BaseFluidMachineRecipe output() {
        return this.output;
    }

    public IBaseRecipe getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Recipes.recipes.getRecipeFluid().getRecipeFromName(name);
        getOutput();
    }

    public BaseFluidMachineRecipe getOutput(FluidTank tank) {
        if (this.output != null) {
            return this.output;
        }
        output = null;
        if (tank == null) {
            return output;
        }
        List<FluidStack> fluidStackList = new ArrayList<>();

        if (tank.getFluidAmount() == 0) {
            return output;
        } else {
            fluidStackList.add(tank.getFluid());
        }
        this.output = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                fluidStackList
        );
        return this.output;

    }

    public BaseFluidMachineRecipe getOutput() {
        if (this.output != null) {
            return this.output;
        }
        if (this.inputTank.isEmpty()) {
            return null;
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

    public void setOutput(BaseFluidMachineRecipe baseFluidMachineRecipe) {
        this.output = baseFluidMachineRecipe;
    }

    public BaseFluidMachineRecipe getOutput(ItemStack stack) {
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
                stack
        );
        return this.output;

    }

    public void checkOutput(FluidTank tank) {
        List<FluidStack> fluidStackList = new ArrayList<>();

        if (tank.getFluidAmount() == 0) {
            if (this.output != null) {
                this.output = null;
            }
            return;
        } else {
            fluidStackList.add(tank.getFluid());
        }


        this.output = Recipes.recipes.getRecipeFluid().getRecipeOutput(
                name,
                list_recipe,
                false,
                fluidStackList
        );

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

    public boolean canFillFluid() {
        if (this.output != null) {
            for (int i = 0; i < outputTank.size(); i++) {
                FluidTank fluidTank = outputTank.get(i);
                final FluidStack fluid = output.output_fluid.get(i);
                if (fluidTank.getFluid() != null && !fluid.isFluidEqual(fluidTank.getFluid())) {
                    return false;
                }
                if (fluidTank.getFluidAmount() + fluid.amount > fluidTank.getCapacity()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean canFillFluid1() {
        if (this.output != null) {
            for (FluidTank fluidTank : outputTank) {
                final FluidStack fluid = output.output_fluid.get(0);
                if ((fluidTank.getFluidAmount() + fluid.amount < fluidTank.getCapacity() && (fluidTank.getFluid() == null || fluidTank
                        .getFluid()
                        .getFluid() == fluid.getFluid()))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public boolean canFillFluid(FluidStack fluidStack) {

        for (FluidTank fluidTank : outputTank) {
            if (!(fluidTank.getFluidAmount() + fluidStack.amount > fluidTank.getCapacity() && (fluidTank.getFluid() == null || fluidTank
                    .getFluid()
                    .getFluid() == fluidStack.getFluid()))) {
                return true;
            }
        }
        return false;

    }

    public boolean hasFluid(FluidStack fluidStack, FluidTank fluidTank) {
        if (fluidStack == null) {
            return false;
        }

        return fluidTank.getFluidAmount() >= fluidStack.amount && fluidTank
                .getFluid() != null && fluidTank
                .getFluid()
                .getFluid() == fluidStack.getFluid();

    }

    public void drainFluid(FluidStack fluidStack, FluidTank fluidTank) {
        if (fluidStack == null) {
            return;
        }
        fluidTank.drain(fluidStack.amount, true);
    }

    public void fillFluid() {
        if (this.output != null) {
            for (int i = 0; i < outputTank.size(); i++) {
                outputTank.get(i).fill(output.output_fluid.get(i), true);
            }
        }
    }

    public void fillFluid(FluidStack fluidStack) {

        for (FluidTank tank : outputTank) {
            if (tank.getFluid() == null || (tank
                    .getFluid()
                    .isFluidEqual(fluidStack) && tank.getFluid().amount + fluidStack.amount <= tank.getCapacity())) {
                tank.fill(fluidStack, true);
                break;
            }
        }

    }

    public void fillFluid1() {
        if (this.output != null) {
            for (FluidTank tank : outputTank) {
                if (tank.getFluid() == null || (tank
                        .getFluid()
                        .isFluidEqual(output.output_fluid.get(0)) && tank.getFluid().amount + output.output_fluid.get(0).amount <= tank.getCapacity())) {
                    tank.fill(output.output_fluid.get(0), true);
                    break;
                }
            }
        }
    }

    public boolean checkFluids() {
        if (this.output != null) {
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
