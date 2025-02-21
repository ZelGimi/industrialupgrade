package com.denfop.componets;

import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;

public class ComponentFluidProcess extends AbstractComponent {

    private final FluidHandlerRecipe fluid_handler;
    private final InvSlotFluidByList[] outputsSlot;
    private final InvSlotFluidByList[] inputSlot;
    private final InvSlotRecipes inputSlotA;
    private final InvSlotOutput output1;
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
        outputsSlot = new InvSlotFluidByList[outputTanks.size()];
        inputSlot = new InvSlotFluidByList[inputTanks.size()];
        for (int i = 0; i < inputTanks.size(); i++) {
            ((Fluids.InternalFluidTank) inputTanks.get(i)).setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(i)));
            inputSlot[i] = new InvSlotFluidByList(parent, 1, this.fluid_handler.getFluids(i));
        }
        for (int i = 0; i < outputTanks.size(); i++) {
            ((Fluids.InternalFluidTank) outputTanks.get(i)).setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(
                    i)));
            outputsSlot[i] = new InvSlotFluidByList(parent, 1, this.fluid_handler.getOutputFluids(i));
        }
        if (hasItem) {
            this.inputSlotA = new InvSlotRecipes(parent, recipe, updateTick);
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
            this.output1 = new InvSlotOutput(parent, size);
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
            }else{
                this.fluid_handler.load();
            }
        }
    }

    public MachineRecipe getOutput() {

        return this.inputSlotA.process();
    }

    public InvSlotFluidByList[] getInputSlot() {
        return inputSlot;
    }

    public InvSlotFluidByList[] getOutputsSlot() {
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
