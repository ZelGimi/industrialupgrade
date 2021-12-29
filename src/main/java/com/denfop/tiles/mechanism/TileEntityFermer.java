package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.init.Localization;
import ic2.core.item.type.CraftingItemType;
import ic2.core.ref.BlockName;
import ic2.core.ref.ItemName;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileEntityFermer extends TileEntityMultiMachine {

    public TileEntityFermer() {
        super(EnumMultiMachine.Fermer.usagePerTick, EnumMultiMachine.Fermer.lenghtOperation, Recipes.fermer, 3);
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.fermer);
    }

    public static void init() {
         addrecipe(Items.WHEAT_SEEDS, Items.WHEAT, 2);
        addrecipe(Items.WHEAT, Items.WHEAT_SEEDS, 1);
        addrecipe(Items.CARROT, Items.CARROT, 2);
        addrecipe(Items.POTATO, Items.POTATO, 2);
        addrecipe(Item.getItemFromBlock(Blocks.PUMPKIN), Items.PUMPKIN_SEEDS, 1);

        addrecipe(Items.PUMPKIN_SEEDS, Item.getItemFromBlock(Blocks.PUMPKIN), 2);
        addrecipe(Items.MELON_SEEDS, Items.MELON, 2);
        addrecipe(Items.MELON, Items.MELON_SEEDS, 1);
        for (int i = 0; i < 4; i++) {
            addrecipe(new ItemStack(Blocks.SAPLING, 1, i), new ItemStack(Blocks.LOG, 2, i));
        }
        for (int i = 0; i < 2; i++) {
            addrecipe(new ItemStack(Blocks.SAPLING, 1, i + 4), new ItemStack(Blocks.LOG2, 2, i));
        }
        for (int i = 0; i < 4; i++) {
            addrecipe(new ItemStack(Blocks.LOG, 1, i), new ItemStack(Blocks.SAPLING, 1, i));
        }
        for (int i = 0; i < 2; i++) {
            addrecipe(new ItemStack(Blocks.LOG2, 1, i), new ItemStack(Blocks.SAPLING, 1, i + 4));
        }
        addrecipe(ItemName.crafting.getItemStack(CraftingItemType.rubber),
                BlockName.sapling.getItemStack().getItem(), 1
        );
        addrecipe(BlockName.sapling.getItemStack().getItem(), ItemName.crafting.getItemStack(CraftingItemType.rubber), 2);
    }

    public static void addrecipe(ItemStack input, Item output) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.fermer.addRecipe(input1.forStack(input), null, false, new ItemStack(output));
    }


    public static void addrecipe(ItemStack input, Item output, int n) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.fermer.addRecipe(input1.forStack(input), null, false, new ItemStack(output, n));
    }

    public static void addrecipe(Item input, ItemStack output, int n) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.fermer.addRecipe(input1.forStack(new ItemStack(input)), null, false, new ItemStack(output.getItem(), n,
                output.getItemDamage()
        ));
    }

    public static void addrecipe(Item input, Item output) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.fermer.addRecipe(input1.forStack(new ItemStack(input)), null, false, new ItemStack(output));
    }

    public static void addrecipe(ItemStack input, ItemStack output) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.fermer.addRecipe(input1.forStack(input), null, false, output);
    }

    public static void addrecipe(Item input, Item output, int n) {
        final IRecipeInputFactory input1 = ic2.api.recipe.Recipes.inputFactory;

        Recipes.fermer.addRecipe(input1.forStack(new ItemStack(input)), null, false, new ItemStack(output, n));
    }

    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.Fermer;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockFermer.name");
    }

    public String getStartSoundFile() {
        return "Machines/Fermer.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
