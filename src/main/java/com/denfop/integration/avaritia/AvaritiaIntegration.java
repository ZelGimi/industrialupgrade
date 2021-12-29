package com.denfop.integration.avaritia;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.integration.de.SSPDEItem;
import ic2.api.recipe.Recipes;
import ic2.core.block.TeBlockRegistry;
import morph.avaritia.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AvaritiaIntegration {

    public static Block blockAvSolarPanel;
    public static Item neutroncore;
    public static Item infinitycore;
    public static Item singularity;

    public static void init() {
        blockAvSolarPanel = TeBlockRegistry.get(BlockAvaritiaSolarPanel.IDENTITY).setCreativeTab(IUCore.SSPTab);
        neutroncore = new SSPDEItem("neutroncore");
        infinitycore = new SSPDEItem("infinitycore");

    }

    public static void recipe() {
        Recipes.advRecipes.addRecipe(new ItemStack(blockAvSolarPanel, 1, 0), " B ", "BAB", " B ", 'B',
                new ItemStack(IUItem.blockpanel, 1, 8), 'A', neutroncore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(blockAvSolarPanel, 1, 1),
                " B ", "BAB", " B ", 'B', new ItemStack(blockAvSolarPanel, 1, 0), 'A', infinitycore
        );

        Recipes.advRecipes.addRecipe(new ItemStack(neutroncore, 1), " A ", "ABA", " A ", 'B',
                new ItemStack(IUItem.core, 1, 5), 'A', ModItems.neutronium_ingot
        );
        Recipes.advRecipes.addRecipe(new ItemStack(infinitycore, 1), "BAB", "ABA", "BAB", 'B',
                new ItemStack(neutroncore, 1), 'A', ModItems.infinity_ingot
        );


        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.UpgradePanelKit, 1, 20), "   ", "BAB", " B ", 'B',
                new ItemStack(IUItem.blockpanel, 1, 8), 'A', neutroncore
        );
        Recipes.advRecipes.addRecipe(new ItemStack(IUItem.UpgradePanelKit, 1, 21),
                "   ", "BAB", " B ", 'B', new ItemStack(blockAvSolarPanel, 1, 0), 'A', infinitycore
        );

    }

}
