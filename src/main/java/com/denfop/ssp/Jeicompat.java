package com.denfop.ssp;

import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import ic2.core.block.ITeBlock;
import ic2.core.init.Localization;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.jeiIntegration.recipe.machine.DynamicCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeWrapper;
import ic2.jeiIntegration.recipe.machine.IRecipeWrapperGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.denfop.ssp.tiles.SSPBlock;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

@JEIPlugin
public final class Jeicompat implements IModPlugin {
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
      super((ITeBlock)SSPBlock.molecular_transformer, IMolecularTransformerRecipeManager.RECIPES, guiHelper, 63);
    }
    
    protected int getProcessSpeed(String name) {
      if ("progress".equals(name))
        return 50; 
      return super.getProcessSpeed(name);
    }
  }
  
  protected static class MolecularTransformerRecipeWrapper extends IORecipeWrapper {
    public static final IRecipeWrapperGenerator<IMolecularTransformerRecipeManager> RECIPE_WRAPPER = new IRecipeWrapperGenerator<IMolecularTransformerRecipeManager>() {
        public List<IRecipeWrapper> getRecipeList(IORecipeCategory<IMolecularTransformerRecipeManager> category) {
          List<IRecipeWrapper> recipes = new ArrayList<>();
          for (MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> container : (Iterable<MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack>>)IMolecularTransformerRecipeManager.RECIPES.getRecipes())
            recipes.add(new Jeicompat.MolecularTransformerRecipeWrapper(container, category)); 
          return recipes;
        }
      };
    
    protected final String input;
    
    protected final String output;
    
    protected final String totalEU;
    
    MolecularTransformerRecipeWrapper(MachineRecipe<IMolecularTransformerRecipeManager.Input, ItemStack> container, IORecipeCategory<?> category) {
      super(((IMolecularTransformerRecipeManager.Input)container.getInput()).input, Collections.singletonList(container.getOutput()), category);
      String inputText;
      IRecipeInput input = ((IMolecularTransformerRecipeManager.Input)container.getInput()).input;
      if (!input.getInputs().isEmpty()) {
        inputText = ((ItemStack)input.getInputs().get(0)).getDisplayName();
      } else if (input instanceof RecipeInputOreDict) {
        inputText = ((RecipeInputOreDict)input).input;
      } else {
        SuperSolarPanels.log.warn("Unexpected empty recipe input: " + input + " (" + input.getClass() + ')');
        inputText = "Empty " + input.getClass().getSimpleName();
      } 
      this.input = Localization.translate("super_solar_panels.gui.input") + ' ' + inputText;
      this.output = Localization.translate("super_solar_panels.gui.output") + ' ' + ((ItemStack)container.getOutput()).getDisplayName();
      this.totalEU = String.format("%s %,d %s", new Object[] { Localization.translate("super_solar_panels.gui.energyPerOperation"), 
            Integer.valueOf(((IMolecularTransformerRecipeManager.Input)container.getInput()).totalEU), 
            Localization.translate("ic2.generic.text.EU") });
    }
    
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
      int space = 5;
      int x = 5;
      minecraft.fontRenderer.drawSplitString(this.input, 42, x, recipeWidth - 42, 16777215);
      x += minecraft.fontRenderer.getWordWrappedHeight(this.input, recipeWidth - 42) + 5;
      minecraft.fontRenderer.drawSplitString(this.output, 42, x, recipeWidth - 42, 16777215);
      x += minecraft.fontRenderer.getWordWrappedHeight(this.output, recipeWidth - 42) + 5;
      minecraft.fontRenderer.drawString(this.totalEU, 42, x, 16777215);
    }
  }
  
  public void register(IModRegistry registry) {
    registry.addRecipeClickArea(TransparentDynamicGUI.class, 23, 48, 10, 15, new String[] { SSPBlock.molecular_transformer.getName() });
    addMachineRecipes(registry, (IORecipeCategory<IMolecularTransformerRecipeManager>)new MolecularTransformerCategory(registry.getJeiHelpers().getGuiHelper()), MolecularTransformerRecipeWrapper.RECIPE_WRAPPER);
  }
  
  private <T> void addMachineRecipes(IModRegistry registry, IORecipeCategory<T> category, IRecipeWrapperGenerator<T> wrappergen) {
    registry.addRecipeCategories(new IRecipeCategory[] { (IRecipeCategory)category });
    registry.addRecipes(wrappergen.getRecipeList(category), category.getUid());
    registry.addRecipeCatalyst(category.getBlockStack(), new String[] { category.getUid() });
  }
}
