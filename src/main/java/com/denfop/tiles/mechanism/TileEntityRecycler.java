package com.denfop.tiles.mechanism;

import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;

import java.util.Collections;


public class TileEntityRecycler extends TileEntityMultiMachine {

    public TileEntityRecycler() {
        super(
                EnumMultiMachine.RECYCLER.usagePerTick,
                EnumMultiMachine.RECYCLER.lenghtOperation,
                1,
                8,
                true,
                4
        );
    }

    public MachineRecipe getOutput(int slotId) {

        if (this.inputSlots.isEmpty(slotId)) {
            this.output[slotId] = null;
            return null;
        }
        this.output[slotId] = this.inputSlots.process(slotId);
        if (output[slotId] == null) {
            final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
            output[slotId] = new MachineRecipe(new BaseMachineRecipe(
                    new Input(input.forStack(this.inputSlots.get(slotId))),
                    new RecipeOutput(
                            null,
                            Ic2Items.scrap
                    )
            ), Collections.singletonList(1));

        }
        if (this.outputSlot.canAdd(output[slotId].getRecipe().output.items)) {
            return output[slotId];
        }

        return null;
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.RECYCLER;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockRecycler.name");
    }

    public String getStartSoundFile() {
        return "Machines/RecyclerOp.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
