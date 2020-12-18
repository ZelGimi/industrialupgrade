package com.denfop.ssp;

import com.denfop.ssp.gui.TransparentDynamicGUI;
import com.denfop.ssp.molecular.IMolecularTransformerRecipeManager;
import com.denfop.ssp.tiles.SSPBlock;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.block.ITeBlock;
import ic2.core.init.Localization;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.jeiIntegration.recipe.machine.DynamicCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeWrapper;
import ic2.jeiIntegration.recipe.machine.IRecipeWrapperGenerator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JEIPlugin
public final class JEICompat implements IModPlugin {
	public void register(IModRegistry registry) {
		registry.addRecipeClickArea(TransparentDynamicGUI.class, 23, 48, 10, 15, SSPBlock.molecular_transformer.getName());
		addMachineRecipes(registry, new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	private <T> void addMachineRecipes(IModRegistry registry, IORecipeCategory<T> category) {
		registry.addRecipeCategories(category);
		registry.addRecipes(((IRecipeWrapperGenerator<T>) MolecularTransformerRecipeWrapper.RECIPE_WRAPPER).getRecipeList(category), category.getUid());
		registry.addRecipeCatalyst(category.getBlockStack(), category.getUid());
	}

	public static class CustomHeightDynamicCategory<T> extends DynamicCategory<T> {
		protected final int height;

		public CustomHeightDynamicCategory(ITeBlock block, T recipeManager, IGuiHelper guiHelper, int height) {
			super(block, recipeManager, guiHelper);
			this.height = height;
		}

		public int getHeight() {
			return this.height;
		}
	}

	protected static class MolecularTransformerCategory extends CustomHeightDynamicCategory<IMolecularTransformerRecipeManager> {
		public MolecularTransformerCategory(IGuiHelper guiHelper) {
			super(SSPBlock.molecular_transformer, IMolecularTransformerRecipeManager.RECIPES, guiHelper, 63);
		}

		protected int getProcessSpeed(String name) {
			if ("progress".equals(name))
				return 50;
			return super.getProcessSpeed(name);
		}
	}

	protected static class MolecularTransformerRecipeWrapper extends IORecipeWrapper {
		public static final IRecipeWrapperGenerator<IMolecularTransformerRecipeManager> RECIPE_WRAPPER = category -> {
			List<IRecipeWrapper> recipes = new ArrayList<>();
			for (MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> container : IMolecularTransformerRecipeManager.RECIPES.getRecipes())
				recipes.add(new MolecularTransformerRecipeWrapper(container, category));
			return recipes;
		};

		protected final String input;

		protected final String output;

		protected final String totalEU;

		MolecularTransformerRecipeWrapper(MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> container, IORecipeCategory<?> category) {
			super(container.getInput().input, Collections.singletonList(container.getOutput()), category);
			String inputText;
			IRecipeInput input = container.getInput().input;
			if (!input.getInputs().isEmpty()) {
				inputText = input.getInputs().get(0).getDisplayName();
			} else if (input instanceof RecipeInputOreDict) {
				inputText = ((RecipeInputOreDict) input).input;
			} else {
				SuperSolarPanels.log.warn("Unexpected empty recipe input: " + input + " (" + input.getClass() + ')');
				inputText = "Empty " + input.getClass().getSimpleName();
			}
			this.input = Localization.translate("super_solar_panels.gui.input") + ' ' + inputText;
			this.output = Localization.translate("super_solar_panels.gui.output") + ' ' + container.getOutput().getDisplayName();
			this.totalEU = String.format("%s %,d %s", Localization.translate("super_solar_panels.gui.energyPerOperation"),
					container.getInput().totalEU,
					Localization.translate("ic2.generic.text.EU"));
		}

		public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
			int y = 5;
			int x = 42;
			minecraft.fontRenderer.drawSplitString(this.input, x, y, recipeWidth - x, 16777215);
			y += minecraft.fontRenderer.getWordWrappedHeight(this.input, recipeWidth - x) + 5;
			minecraft.fontRenderer.drawSplitString(this.output, x, y, recipeWidth - x, 16777215);
			y += minecraft.fontRenderer.getWordWrappedHeight(this.output, recipeWidth - x) + 5;
			minecraft.fontRenderer.drawString(this.totalEU, x, y, 16777215);
		}
	}
}
