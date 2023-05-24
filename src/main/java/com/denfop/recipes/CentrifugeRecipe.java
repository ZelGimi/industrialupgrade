package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public class CentrifugeRecipe {

    public static void init() {

        addcentrifuge(IUItem.reactorDepletedamericiumDual, new ItemStack(IUItem.radiationresources, 2));
        addcentrifuge(IUItem.reactorDepletedamericiumQuad, new ItemStack(IUItem.radiationresources, 4));
        addcentrifuge(IUItem.reactorDepletedamericiumSimple, new ItemStack(IUItem.radiationresources, 1));

        addcentrifuge(IUItem.reactorDepletedneptuniumDual, new ItemStack(IUItem.radiationresources, 2, 1));
        addcentrifuge(IUItem.reactorDepletedneptuniumQuad, new ItemStack(IUItem.radiationresources, 4, 1));
        addcentrifuge(IUItem.reactorDepletedneptuniumSimple, new ItemStack(IUItem.radiationresources, 1, 1));

        addcentrifuge(IUItem.reactorDepletedcuriumDual, new ItemStack(IUItem.radiationresources, 2, 2));
        addcentrifuge(IUItem.reactorDepletedcuriumQuad, new ItemStack(IUItem.radiationresources, 4, 2));
        addcentrifuge(IUItem.reactorDepletedcuriumSimple, new ItemStack(IUItem.radiationresources, 1, 2));

        addcentrifuge(IUItem.reactorDepletedcaliforniaDual, new ItemStack(IUItem.radiationresources, 2, 3));
        addcentrifuge(IUItem.reactorDepletedcaliforniaQuad, new ItemStack(IUItem.radiationresources, 4, 3));
        addcentrifuge(IUItem.reactorDepletedcaliforniaSimple, new ItemStack(IUItem.radiationresources, 1, 3));

        addcentrifuge(IUItem.reactorDepletedmendeleviumDual, new ItemStack(IUItem.radiationresources, 2, 5));
        addcentrifuge(IUItem.reactorDepletedmendeleviumQuad, new ItemStack(IUItem.radiationresources, 4, 5));
        addcentrifuge(IUItem.reactorDepletedmendeleviumSimple, new ItemStack(IUItem.radiationresources, 1, 5));

        addcentrifuge(IUItem.reactorDepletedberkeliumDual, new ItemStack(IUItem.radiationresources, 2, 6));
        addcentrifuge(IUItem.reactorDepletedberkeliumQuad, new ItemStack(IUItem.radiationresources, 4, 6));
        addcentrifuge(IUItem.reactorDepletedberkeliumSimple, new ItemStack(IUItem.radiationresources, 1, 6));

        addcentrifuge(IUItem.reactorDepletedeinsteiniumDual, new ItemStack(IUItem.radiationresources, 2, 7));
        addcentrifuge(IUItem.reactorDepletedeinsteiniumQuad, new ItemStack(IUItem.radiationresources, 4, 7));
        addcentrifuge(IUItem.reactorDepletedeinsteiniumSimple, new ItemStack(IUItem.radiationresources, 1, 7));

        addcentrifuge(IUItem.reactorDepleteduran233Dual, new ItemStack(IUItem.radiationresources, 2, 8));
        addcentrifuge(IUItem.reactorDepleteduran233Quad, new ItemStack(IUItem.radiationresources, 4, 8));
        addcentrifuge(IUItem.reactorDepleteduran233Simple, new ItemStack(IUItem.radiationresources, 1, 8));

        addcentrifuge(IUItem.reactorDepletedtoriyDual, new ItemStack(IUItem.toriy, 2));
        addcentrifuge(IUItem.reactorDepletedtoriyQuad, new ItemStack(IUItem.toriy, 4));
        addcentrifuge(IUItem.reactorDepletedtoriySimple, new ItemStack(IUItem.toriy, 1));

        addcentrifuge(IUItem.reactorDepletedprotonDual, new ItemStack(IUItem.proton, 2));
        addcentrifuge(IUItem.reactorDepletedprotonQuad, new ItemStack(IUItem.proton, 4));
        addcentrifuge(IUItem.reactorDepletedprotoneit, new ItemStack(IUItem.proton, 8));
        addcentrifuge(IUItem.reactorDepletedprotonSimple, new ItemStack(IUItem.proton, 1));

        addcentrifuge(0, null);
        addcentrifuge(1, new ItemStack(Blocks.SAND));
        addcentrifuge(2, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 10));
        addcentrifuge(3, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge(6, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 1));
        addcentrifuge(7, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge(8, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 0));
        addcentrifuge(9, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 2));
        addcentrifuge(10, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 0));
        addcentrifuge(11, new ItemStack(IUItem.smalldust, 1, 7));
        addcentrifuge(12, new ItemStack(IUItem.smalldust, 1, 10));
        addcentrifuge(14, null);
        addcentrifuge(15, null);
        addcentrifuge(16, null);
        addcentrifuge(17, new ItemStack(IUItem.smalldust, 1, 9));
        addcentrifuge(18, new ItemStack(IUItem.smalldust, 1, 15));

        addcentrifuge1(0, null);
        addcentrifuge1(1, new ItemStack(Blocks.SAND));
        addcentrifuge1(2, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 10));
        addcentrifuge1(3, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge1(6, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 1));
        addcentrifuge1(7, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge1(8, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 0));
        addcentrifuge1(9, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 2));
        addcentrifuge1(10, new ItemStack(Ic2Items.smallLeadDust.getItem(), 1, 0));
        addcentrifuge1(11, new ItemStack(IUItem.smalldust, 1, 7));
        addcentrifuge1(12, new ItemStack(IUItem.smalldust, 1, 10));
        addcentrifuge1(14, null);
        addcentrifuge1(15, null);
        addcentrifuge1(16, null);
        addcentrifuge1(17, new ItemStack(IUItem.smalldust, 1, 9));
        addcentrifuge1(18, new ItemStack(IUItem.smalldust, 1, 15));

        addcentrifuge1("Gold", new ItemStack(IUItem.smalldust, 1, 14));
        addcentrifuge("Gold", new ItemStack(IUItem.smalldust, 1, 14));

        addcentrifuge("Lead", null);
        addcentrifuge1("Lead", Ic2Items.smallCopperDust);
        addcentrifuge("Tin", Ic2Items.smallIronDust);
        addcentrifuge("Copper", Ic2Items.smallTinDust);
        addcentrifuge1("Copper", Ic2Items.smallTinDust);
        addcentrifuge1("Tin", Ic2Items.smallIronDust);
        addcentrifuge1("Iron", Ic2Items.smallGoldDust);
        addcentrifuge("Iron", Ic2Items.smallGoldDust);
        addcentrifuge(
                new ItemStack(Ic2Items.clayDust.getItem(), 4, Ic2Items.clayDust.getItemDamage()),
                Ic2Items.silicondioxideDust
        );
        addcentrifuge(new ItemStack(Items.QUARTZ, 2), Ic2Items.lithiumDust);
        addcentrifuge(new ItemStack(Blocks.COBBLESTONE), Ic2Items.stoneDust);
        addcentrifuge(Ic2Items.RTGPellets, new ItemStack(Ic2Items.Plutonium.getItem(), 3, 3),
                new ItemStack(Ic2Items.smallIronDust.getItem(), 54, Ic2Items.smallIronDust.getItemDamage())
        );

        addcentrifuge(Ic2Items.reactorDepletedMOXSimple, growCount(Ic2Items.Plutonium, 3),
                growCount(Ic2Items.smallPlutonium, 1), growCount(Ic2Items.ironDust, 1)
        );
        addcentrifuge(Ic2Items.reactorDepletedMOXDual, growCount(Ic2Items.Plutonium, 6),
                growCount(Ic2Items.smallPlutonium, 2), growCount(Ic2Items.ironDust, 3)
        );
        addcentrifuge(Ic2Items.reactorDepletedMOXQuad, growCount(Ic2Items.Plutonium, 12),
                growCount(Ic2Items.smallPlutonium, 4), growCount(Ic2Items.ironDust, 7)
        );

        addcentrifuge(Ic2Items.reactorDepletedUraniumSimple, growCount(Ic2Items.Uran238, 4),
                growCount(Ic2Items.smallPlutonium, 1), growCount(Ic2Items.ironDust, 1)
        );
        addcentrifuge(Ic2Items.reactorDepletedUraniumDual, growCount(Ic2Items.Uran238, 8),
                growCount(Ic2Items.smallPlutonium, 2), growCount(Ic2Items.ironDust, 3)
        );
        addcentrifuge(Ic2Items.reactorDepletedUraniumQuad, growCount(Ic2Items.Uran238, 16),
                growCount(Ic2Items.smallPlutonium, 4), growCount(Ic2Items.ironDust, 7)
        );

        addcentrifuge1("crushedPurifiedUranium", new ItemStack(Ic2Items.Plutonium.getItem(), 1, 5),
                new ItemStack(Ic2Items.Uran238.getItem(), 6, Ic2Items.Uran238.getItemDamage())
        );
        addcentrifuge1("crushedUranium", new ItemStack(Ic2Items.Plutonium.getItem(), 1, 5),
                new ItemStack(Ic2Items.Uran238.getItem(), 4, Ic2Items.Uran238.getItemDamage()), Ic2Items.stoneDust
        );
        addcentrifuge(growCount(Ic2Items.UranFuel, 20), growCount(Ic2Items.Uran238, 112),
                growCount(Ic2Items.Uran235, 7)
        );
    }

    public static void addcentrifuge(ItemStack stack, ItemStack... output) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 5000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forStack(stack)
                        ),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void addcentrifuge1(String stack, ItemStack... output) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 5000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forOreDict(stack)
                        ),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static ItemStack growCount(ItemStack stack, int count) {
        final ItemStack stack1 = stack.copy();
        stack1.setCount(count);
        return stack1;
    }

    public static void addcentrifuge(ItemStack stack, ItemStack output) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 5000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forStack(stack)
                        ),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void addcentrifuge(int meta, ItemStack output) {
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = new ItemStack(IUItem.iudust, 1, meta);
        stack[1] = Ic2Items.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 2000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forStack(new ItemStack(IUItem.crushed, 1, meta))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addcentrifuge(String meta, ItemStack output) {
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = OreDictionary.getOres("dust" + meta).get(0).copy();
        stack[1] = Ic2Items.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 2000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forOreDict("crushed" + meta)
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addcentrifuge1(String meta, ItemStack output) {
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[2];
        } else {
            stack = new ItemStack[1];

        }
        stack[0] = OreDictionary.getOres("dust" + meta).get(0).copy();

        if (output != null) {
            stack[1] = output;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 2000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forOreDict("crushedPurified" + meta)
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addcentrifuge1(int meta, ItemStack output) {
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[2];
        } else {
            stack = new ItemStack[1];

        }
        stack[0] = new ItemStack(IUItem.iudust, 1, meta);

        if (output != null) {
            stack[1] = output;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 2000);
        final IRecipeInputFactory input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.forStack(new ItemStack(IUItem.purifiedcrushed, 1, meta))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

}
