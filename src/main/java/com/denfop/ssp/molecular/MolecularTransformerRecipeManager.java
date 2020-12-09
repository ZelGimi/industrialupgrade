package com.denfop.ssp.molecular;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.init.MainConfig;
import ic2.core.recipe.MachineRecipeHelper;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MolecularTransformerRecipeManager extends MachineRecipeHelper<IMolecularTransformerRecipeManager.Input, ItemStack> implements IMolecularTransformerRecipeManager {
	public boolean addRecipe(IRecipeInput input, int totalEU, ItemStack output, boolean replace) {
		return addRecipe(new IMolecularTransformerRecipeManager.Input(input, totalEU), output, null, replace);
	}

	public boolean addRecipe(IMolecularTransformerRecipeManager.Input input, ItemStack output, NBTTagCompound metadata, boolean replace) {
		if (input == null) {
			showError("Invalid recipe input: null");
			return false;
		}
		if (StackUtil.isEmpty(output)) {
			showError("Invalid recipe output: " + StackUtil.toStringSafe(output));
			return false;
		}
		if (input.input.matches(output) && (
				metadata == null || !metadata.hasKey("ignoreSameInputOutput"))) {
			showError("The output ItemStack " + StackUtil.toStringSafe(output) + " is the same as the recipe input " + input + ".");
			return false;
		}
		label22:
		for (ItemStack is : input.input.getInputs()) {
			MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> machineRecipe = getRecipe(is);
			if (machineRecipe != null) {
				if (replace)
					while (true) {
						this.recipes.remove(input);
						removeCachedRecipes(input);
						machineRecipe = getRecipe(is);
						if (machineRecipe == null)
							continue label22;
					}
				return false;
			}
		}
		MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> recipe = new MachineRecipe(input, output.copy(), metadata);
		this.recipes.put(input, recipe);
		addToCache(recipe);
		return true;
	}

	public static void showError(String message) {
		if (MainConfig.ignoreInvalidRecipes) {
			com.denfop.ssp.SuperSolarPanels.log.warn(message);
		} else {
			throw new RuntimeException(message);
		}
	}

	public int getTotalEUNeeded(ItemStack input) {
		IMolecularTransformerRecipeManager.Input recipe = getRecipe(input).getInput();
		return (recipe == null) ? -1 : recipe.totalEU;
	}

	protected IRecipeInput getForInput(IMolecularTransformerRecipeManager.Input input) {
		return input.input;
	}


}
