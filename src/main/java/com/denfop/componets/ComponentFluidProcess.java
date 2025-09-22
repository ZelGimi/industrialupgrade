package com.denfop.componets;

import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.invslot.InventoryFluidByList;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public class ComponentFluidProcess extends AbstractComponent {

    private final FluidHandlerRecipe fluid_handler;
    private final InventoryFluidByList[] outputsSlot;
    private final InventoryFluidByList[] inputSlot;
    private final InventoryRecipes inputSlotA;
    private final InventoryOutput output1;
    private final boolean needConsumeFluid;
    private final IUpdateTick updateTick;

    public ComponentFluidProcess(
            final TileEntityInventory parent, Fluids fluids, String recipe, boolean hasItem,
            IUpdateTick updateTick, boolean needOutputSlotForInput, boolean needOutputSlotForOutput,
            boolean needConsumeFluid
    ) {
        super(parent);
        this.fluid_handler = new FluidHandlerRecipe(recipe, fluids);
        final List<FluidTank> inputTanks = fluid_handler.getInputTank();
        final List<FluidTank> outputTanks = fluid_handler.getOutputTank();
        outputsSlot = new InventoryFluidByList[outputTanks.size()];
        inputSlot = new InventoryFluidByList[inputTanks.size()];
        for (int i = 0; i < inputTanks.size(); i++) {
            ((Fluids.InternalFluidTank) inputTanks.get(i)).setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(i)));
            inputSlot[i] = new InventoryFluidByList(parent, 1, this.fluid_handler.getFluids(i));
        }
        for (int i = 0; i < outputTanks.size(); i++) {
            ((Fluids.InternalFluidTank) outputTanks.get(i)).setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(
                    i)));
            outputsSlot[i] = new InventoryFluidByList(parent, 1, this.fluid_handler.getOutputFluids(i));
        }
        if (hasItem) {
            this.inputSlotA = new InventoryRecipes(parent, recipe, updateTick);
        } else {
            this.inputSlotA = null;
        }
        this.updateTick = updateTick;
        int size = 0;
        if (needOutputSlotForInput) {
            size += inputTanks.size();
        }
        if (needOutputSlotForOutput) {
            size += outputTanks.size();
        }
        if (size > 0) {
            this.output1 = new InventoryOutput(parent, size);
        } else {
            this.output1 = null;
        }
        this.needConsumeFluid = needConsumeFluid;
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.parent.getWorld().isRemote) {
            if (inputSlotA != null) {
                inputSlotA.load();
                this.fluid_handler.load(this.inputSlotA.get());
                this.updateTick.setRecipeOutput(getOutput());
            } else {
                this.fluid_handler.load();
            }
        }
    }

    public MachineRecipe getOutput() {

        return this.inputSlotA.process();
    }

    public InventoryFluidByList[] getInputSlot() {
        return inputSlot;
    }

    public InventoryFluidByList[] getOutputsSlot() {
        return outputsSlot;
    }

    public FluidHandlerRecipe getFluidHandler() {
        return fluid_handler;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();

    }

    @Override
    public boolean isServer() {
        return true;
    }

}
