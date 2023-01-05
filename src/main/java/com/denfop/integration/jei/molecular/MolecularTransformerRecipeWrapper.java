package com.denfop.integration.jei.molecular;

import com.denfop.IUCore;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MolecularTransformerRecipeWrapper implements IRecipeWrapper {


    protected final String input;

    protected final String output;

    protected final String totalEU;
    private final ItemStack inputstack;
    private final ItemStack outputstack;


    public MolecularTransformerRecipeWrapper(MolecularTransformerHandler container) {

        String inputText;

        if (!container.getInput().isEmpty()) {
            inputText = container.getInput().getDisplayName();
        } else {
            IUCore.log.warn("Unexpected empty recipe input: " + container.getInput() + " (" + container
                    .getInput()
                    .getClass() + ')');
            inputText = "Empty " + container.getInput().getClass().getSimpleName();
        }
        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();
        this.input = Localization.translate("gui.MolecularTransformer.input") + ' ' + inputText;
        this.output =
                Localization.translate("gui.MolecularTransformer.output") + ' ' + container.getOutput().getDisplayName();
        this.totalEU = String.format("%s %s %s", Localization.translate("gui.MolecularTransformer.energyPerOperation"),
                ModUtils.getString(container.getEnergy()),
                Localization.translate("ic2.generic.text.EU")
        );
    }

    public ItemStack getInput() {
        return inputstack;
    }

    public List<List<ItemStack>> getInputs() {
        ItemStack inputs = this.inputstack;
        List<ItemStack> stack = new ArrayList<>();
        if (OreDictionary.getOreIDs(inputs).length > 0) {
            int id = OreDictionary.getOreIDs(inputs)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(inputs);
        }
        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int y = 5;
        int x = 49;
        minecraft.fontRenderer.drawSplitString(this.input, x, y, recipeWidth - x, 16777215);
        y += minecraft.fontRenderer.getWordWrappedHeight(this.input, recipeWidth - x) + 5;
        minecraft.fontRenderer.drawSplitString(this.output, x, y, recipeWidth - x, 16777215);
        y += minecraft.fontRenderer.getWordWrappedHeight(this.output, recipeWidth - x) + 5;
        minecraft.fontRenderer.drawString(this.totalEU, x, y, 16777215);
    }

}
