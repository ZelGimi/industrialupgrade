package com.denfop.integration.de;

import com.brandon3055.draconicevolution.DEFeatures;
import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DraconicIntegration {


    public static Item chaosingot;
    public static Block blockDESolarPanel;
    public static SSPDEItem ChaosEnergyCore;


    public static void init() {
        if (Config.Draconic) {

            ChaosEnergyCore = new SSPDEItem("ChaosEnergyCore");

            chaosingot = new SSPDEItem("chaosingot");
            if (Config.registerDraconicPanels) {
                blockDESolarPanel = TeBlockRegistry.get(BlockDESolarPanel.IDENTITY).setCreativeTab(IUCore.SSPTab);
            }
        }
    }


    public static void Recipes() {
        final IRecipeInputFactory input = Recipes.inputFactory;
        Recipes.compressor.addRecipe(
                input.forStack(new ItemStack(DEFeatures.chaosShard, 1, 2), 1),
                null, false, new ItemStack(chaosingot, 1)
        );

        Recipes.advRecipes.addRecipe(new ItemStack(ChaosEnergyCore, 1), " B ", "BAB", " B ", 'B',
                new ItemStack(chaosingot, 1), 'A', DEFeatures.draconicEnergyCore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockDESolarPanel, 1, 0), " B ", "BAB", " B ", 'B',
                new ItemStack(IUItem.blockpanel, 1), 'A', DEFeatures.wyvernCore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockDESolarPanel, 1, 1), "AB ", "BAB", " BA", 'B',
                new ItemStack(blockDESolarPanel, 1, 0), 'A', DEFeatures.awakenedCore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockDESolarPanel, 1, 2), "ABC", "BAB", "CBA", 'B',
                new ItemStack(blockDESolarPanel, 1, 1), 'A', DEFeatures.chaoticCore, 'C', ChaosEnergyCore
        );

        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.UpgradePanelKit, 1, 14), "   ", "BAB", " B ", 'B',
                new ItemStack(IUItem.blockpanel, 1), 'A', DEFeatures.wyvernCore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.UpgradePanelKit, 1, 15), "A  ", "BAB", " BA", 'B',
                new ItemStack(blockDESolarPanel, 1, 0), 'A', DEFeatures.awakenedCore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.UpgradePanelKit, 1, 16), "A C", "BAB", "CBA", 'B',
                new ItemStack(blockDESolarPanel, 1, 1), 'A', DEFeatures.chaoticCore, 'C', ChaosEnergyCore
        );

    }

    public static void render() {


    }

}
