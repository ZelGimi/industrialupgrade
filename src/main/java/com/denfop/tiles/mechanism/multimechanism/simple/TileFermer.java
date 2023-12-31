package com.denfop.tiles.mechanism.multimechanism.simple;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileMultiMachine;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TileFermer extends TileMultiMachine {

    public TileFermer() {
        super(EnumMultiMachine.Fermer.usagePerTick, EnumMultiMachine.Fermer.lenghtOperation, 3);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack input, Item output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, new ItemStack(output))
                )
        );
    }

    public static void addrecipe(ItemStack input, Item output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(input)
                        ),
                        new RecipeOutput(null, new ItemStack(output, n))
                )
        );
    }

    public static void addrecipe(Item input, ItemStack output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(input))
                        ),
                        new RecipeOutput(null, new ItemStack(output.getItem(), n,
                                output.getItemDamage()
                        )
                        )
                )
        );

    }

    public static void addrecipe(Item input, Item output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(input))
                        ),
                        new RecipeOutput(null, new ItemStack(output)
                        )
                )
        );
    }

    public static void addrecipe(ItemStack input, ItemStack output) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;

        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput((input))
                        ),
                        new RecipeOutput(null, output
                        )
                )
        );
    }

    public static void addrecipe(ItemStack input, ItemStack output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        output = output.copy();
        output.setCount(n);
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput((input))
                        ),
                        new RecipeOutput(null, output
                        )
                )
        );
    }

    public static void addrecipe(Item input, Item output, int n) {
        final IInputHandler input1 = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                input1.getInput(new ItemStack(input))
                        ),
                        new RecipeOutput(null, new ItemStack(output, n)
                        )
                )
        );
    }

    public IMultiTileBlock getTeBlock() {
        return BlockMoreMachine3.farmer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines_base3;
    }

    public void init() {
        addrecipe(Items.WHEAT_SEEDS, Items.WHEAT, 2);
        addrecipe(Items.WHEAT, Items.WHEAT_SEEDS, 1);
        addrecipe(new ItemStack(IUItem.rubberSapling), new ItemStack(IUItem.rubWood), 1);
        addrecipe(new ItemStack(IUItem.rubWood), IUItem.latex, 2);
        addrecipe(IUItem.latex, new ItemStack(IUItem.rubberSapling), 1);
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
