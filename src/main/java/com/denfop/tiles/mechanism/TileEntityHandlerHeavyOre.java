package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.tiles.base.TileEntityBaseHandlerHeavyOre;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.block.type.ResourceBlock;
import ic2.core.ref.BlockName;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityHandlerHeavyOre extends TileEntityBaseHandlerHeavyOre implements IHasRecipe {


    public TileEntityHandlerHeavyOre() {
        super(EnumTypeStyle.DEFAULT);
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addhandlerore(ItemStack container, ItemStack[] output, short temperature, int... col) {
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", temperature);
        for (int i = 0; i < col.length; i++) {
            nbt.setInteger("input" + i, col[i]);
        }
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "handlerho",
                new BaseMachineRecipe(
                        new Input(
                                input.forStack(container)
                        ),
                        new RecipeOutput(nbt, output)
                )
        );


    }

    public void init() {
        addhandlerore(
                new ItemStack(IUItem.heavyore),
                new ItemStack[]{new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.GOLD_ORE)},
                (short) 1500, 75, 25
        );
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 1), new ItemStack[]{new ItemStack(IUItem.ore, 1, 7),
                        new ItemStack(Blocks.GOLD_ORE), BlockName.resource.getItemStack(ResourceBlock.copper_ore)},
                (short) 3000, 28, 44, 28
        );
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 2), new ItemStack[]{new ItemStack(IUItem.ore, 1, 11),
                BlockName.resource.getItemStack(ResourceBlock.lead_ore)}, (short) 5000, 13, 87);
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 3),
                new ItemStack[]{new ItemStack(IUItem.ore, 1, 8), new ItemStack(IUItem.ore, 1, 6)},
                (short) 4000, 44, 56
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 4),
                new ItemStack[]{new ItemStack(Blocks.IRON_ORE), new ItemStack(IUItem.ore, 1, 4)},
                (short) 2500, 80, 20
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 5),
                new ItemStack[]{new ItemStack(Blocks.QUARTZ_ORE), new ItemStack(
                        IUItem.ore,
                        1, 12
                )},
                (short) 2500, 84, 16
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 6),
                new ItemStack[]{BlockName.resource.getItemStack(ResourceBlock.uranium_ore),
                        new ItemStack(IUItem.toriyore)},
                (short) 4500, 84, 16
        );
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 7),
                new ItemStack[]{BlockName.resource.getItemStack(ResourceBlock.copper_ore), new ItemStack(Blocks.LAPIS_ORE),
                        new ItemStack(Blocks.REDSTONE_ORE)},
                (short) 2000, 55, 23, 21
        );

        addhandlerore(new ItemStack(IUItem.heavyore, 1, 8), new ItemStack[]{new ItemStack(IUItem.ore, 1, 13),
                        new ItemStack(IUItem.ore, 1, 5), new ItemStack(Blocks.IRON_ORE)}, (short) 3000
                , 44, 28, 28);
        addhandlerore(
                new ItemStack(IUItem.heavyore, 1, 9),
                new ItemStack[]{new ItemStack(IUItem.ore, 1, 4), new ItemStack(IUItem.ore, 1, 6)},
                (short) 3500, 50, 50
        );
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 10), new ItemStack[]{new ItemStack(IUItem.ore, 1, 8),
                        new ItemStack(IUItem.toriyore), BlockName.resource.getItemStack(ResourceBlock.uranium_ore)}, (short) 3000, 50, 25
                , 25);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 11), new ItemStack[]{new ItemStack(IUItem.ore, 1, 12),
                new ItemStack(Blocks.COAL_ORE)}, (short) 4000, 65, 35);

        addhandlerore(new ItemStack(IUItem.heavyore, 1, 12), new ItemStack[]{new ItemStack(IUItem.ore, 1, 8),
                new ItemStack(Blocks.IRON_ORE)}, (short) 4500, 47, 53);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 13), new ItemStack[]{new ItemStack(IUItem.ore, 1, 13),
                new ItemStack(IUItem.ore, 1, 5), new ItemStack(IUItem.ore, 1, 1)}, (short) 4000, 66, 17, 17);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 14), new ItemStack[]{new ItemStack(Blocks.IRON_ORE),
                new ItemStack(IUItem.ore, 1, 5)}, (short) 4000, 60, 40);
        addhandlerore(new ItemStack(IUItem.heavyore, 1, 15), new ItemStack[]{new ItemStack(IUItem.ore, 1, 3),
                Ic2Items.tinOre}, (short) 4000, 80, 20);


    }


}
