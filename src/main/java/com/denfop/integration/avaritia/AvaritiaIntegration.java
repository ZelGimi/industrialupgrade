package com.denfop.integration.avaritia;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.integration.de.IUDEItem;
import morph.avaritia.init.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AvaritiaIntegration {

    public static BlockTileEntity blockAvSolarPanel;
    public static Item neutroncore;
    public static Item infinitycore;

    public static void init() {
        blockAvSolarPanel = TileBlockCreator.instance.create(BlockAvaritiaSolarPanel.class);
        neutroncore = new IUDEItem("neutroncore");
        infinitycore = new IUDEItem("infinitycore");

    }

    public static void recipe() {

        Recipes.recipe.addRecipe(new ItemStack(blockAvSolarPanel, 1, 0), " B ", "BAB", " B ", 'B',
                new ItemStack(IUItem.blockpanel, 1, 8), 'A', neutroncore
        );
        Recipes.recipe.addRecipe(new ItemStack(blockAvSolarPanel, 1, 1),
                " B ", "BAB", " B ", 'B', new ItemStack(blockAvSolarPanel, 1, 0), 'A', infinitycore
        );

        Recipes.recipe.addRecipe(new ItemStack(neutroncore, 1), " A ", "ABA", " A ", 'B',
                new ItemStack(IUItem.core, 1, 5), 'A', ModItems.neutronium_ingot
        );
        Recipes.recipe.addRecipe(new ItemStack(infinitycore, 1), "BAB", "ABA", "BAB", 'B',
                new ItemStack(neutroncore, 1), 'A', ModItems.infinity_ingot
        );


        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 20), "   ", "BAB", " B ", 'B',
                new ItemStack(IUItem.blockpanel, 1, 8), 'A', neutroncore
        );
        Recipes.recipe.addRecipe(new ItemStack(IUItem.upgradepanelkit, 1, 21),
                "   ", "BAB", " B ", 'B', new ItemStack(blockAvSolarPanel, 1, 0), 'A', infinitycore
        );
    }

}
