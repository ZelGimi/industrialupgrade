package com.denfop.integration.de;

import com.brandon3055.draconicevolution.DEFeatures;
import com.brandon3055.draconicevolution.lib.RecipeManager;
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
    public static IUDEItem ChaosEnergyCore;


    public static void init() {
        if (Config.Draconic) {

            ChaosEnergyCore = new IUDEItem("ChaosEnergyCore");

            chaosingot = new IUDEItem("chaosingot");

            blockDESolarPanel = TeBlockRegistry.get(BlockDESolarPanel.IDENTITY).setCreativeTab(IUCore.IUTab);

        }
    }


    public static void Recipes() {
        final IRecipeInputFactory input = Recipes.inputFactory;
        if (Config.Draconic) {
            Recipes.compressor.addRecipe(
                    input.forStack(new ItemStack(DEFeatures.chaosShard, 1, 2), 1),
                    null, false, new ItemStack(chaosingot, 1)
            );

            Recipes.advRecipes.addRecipe(new ItemStack(ChaosEnergyCore, 1), " B ", "BAB", " B ", 'B',
                    new ItemStack(chaosingot, 1), 'A', DEFeatures.draconicEnergyCore
            );


            RecipeManager.addFusion(RecipeManager.RecipeDifficulty.ALL, new ItemStack(blockDESolarPanel, 1),
                    new ItemStack(IUItem.blockpanel, 1), 400000000, 1, DEFeatures.wyvernCore, DEFeatures.wyvernCore,
                    DEFeatures.wyvernCore, DEFeatures.wyvernCore
            );
            RecipeManager.addFusion(RecipeManager.RecipeDifficulty.ALL,
                    new ItemStack(blockDESolarPanel, 1, 1),
                    new ItemStack(blockDESolarPanel, 1),
                    1000000000,
                    1,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore
            );
            RecipeManager.addFusion(RecipeManager.RecipeDifficulty.ALL,
                    new ItemStack(blockDESolarPanel, 1, 2),
                    new ItemStack(blockDESolarPanel, 1, 1),
                    2000000000,
                    1,
                    DEFeatures.chaoticCore,
                    DEFeatures.chaoticCore,
                    DEFeatures.chaoticCore,
                    DEFeatures.chaoticCore,
                    ChaosEnergyCore,
                    ChaosEnergyCore,
                    ChaosEnergyCore,
                    ChaosEnergyCore
            );

            RecipeManager.addFusion(RecipeManager.RecipeDifficulty.ALL, new ItemStack(IUItem.upgradepanelkit, 1, 14),
                    new ItemStack(IUItem.upgradepanelkit, 1), 1000000, 1, DEFeatures.wyvernCore, DEFeatures.wyvernCore,
                    DEFeatures.wyvernCore, DEFeatures.wyvernCore
            );
            RecipeManager.addFusion(RecipeManager.RecipeDifficulty.ALL,
                    new ItemStack(IUItem.upgradepanelkit, 1, 15),
                    new ItemStack(IUItem.upgradepanelkit, 1),
                    10000000,
                    1,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore,
                    DEFeatures.awakenedCore
            );
            RecipeManager.addFusion(RecipeManager.RecipeDifficulty.ALL,
                    new ItemStack(IUItem.upgradepanelkit, 1, 16),
                    new ItemStack(IUItem.upgradepanelkit, 1),
                    200000000,
                    1,
                    DEFeatures.chaoticCore,
                    DEFeatures.chaoticCore,
                    DEFeatures.chaoticCore,
                    DEFeatures.chaoticCore,
                    ChaosEnergyCore,
                    ChaosEnergyCore,
                    ChaosEnergyCore,
                    ChaosEnergyCore
            );
        }

    }

    public static void render() {


    }

}
