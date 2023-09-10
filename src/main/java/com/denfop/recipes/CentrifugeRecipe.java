package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import net.minecraft.init.Blocks;
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
        addcentrifuge(2, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 10));
        addcentrifuge(3, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge(6, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 1));
        addcentrifuge(7, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge(8, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 0));
        addcentrifuge(9, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 2));
        addcentrifuge(10, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 0));
        addcentrifuge(11, new ItemStack(IUItem.smalldust, 1, 7));
        addcentrifuge(12, new ItemStack(IUItem.smalldust, 1, 10));
        addcentrifuge(14, null);
        addcentrifuge(15, null);
        addcentrifuge(16, null);
        addcentrifuge(17, new ItemStack(IUItem.smalldust, 1, 9));
        addcentrifuge(18, new ItemStack(IUItem.smalldust, 1, 15));

        addcentrifuge1(0, null);
        addcentrifuge1(1, new ItemStack(Blocks.SAND));
        addcentrifuge1(2, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 10));
        addcentrifuge1(3, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge1(6, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 1));
        addcentrifuge1(7, new ItemStack(IUItem.smalldust, 1, 16));
        addcentrifuge1(8, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 0));
        addcentrifuge1(9, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 2));
        addcentrifuge1(10, new ItemStack(IUItem.smallLeadDust.getItem(), 1, 0));
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
        addcentrifuge1("Lead", IUItem.smallCopperDust);
        addcentrifuge("Tin", IUItem.smallIronDust);
        addcentrifuge("Copper", IUItem.smallTinDust);
        addcentrifuge1("Copper", IUItem.smallTinDust);
        addcentrifuge1("Tin", IUItem.smallIronDust);
        addcentrifuge1("Iron", IUItem.smallGoldDust);
        addcentrifuge("Iron", IUItem.smallGoldDust);
        addcentrifuge(
                new ItemStack(IUItem.clayDust.getItem(), 4, IUItem.clayDust.getItemDamage()),
                IUItem.silicondioxideDust
        );

        addcentrifuge(new ItemStack(Blocks.COBBLESTONE), IUItem.stoneDust);
        addcentrifuge(IUItem.RTGPellets, growCount(IUItem.Plutonium, 3),
                new ItemStack(IUItem.smallIronDust.getItem(), 54, IUItem.smallIronDust.getItemDamage())
        );

        addcentrifuge(IUItem.reactorDepletedmoxSimple, growCount(IUItem.Plutonium, 3),
                growCount(IUItem.smallPlutonium, 1), growCount(IUItem.ironDust, 1)
        );
        addcentrifuge(IUItem.reactorDepletedmoxDual, growCount(IUItem.Plutonium, 6),
                growCount(IUItem.smallPlutonium, 2), growCount(IUItem.ironDust, 3)
        );
        addcentrifuge(IUItem.reactorDepletedmoxQuad, growCount(IUItem.Plutonium, 12),
                growCount(IUItem.smallPlutonium, 4), growCount(IUItem.ironDust, 7)
        );

        addcentrifuge(IUItem.reactorDepleteduranSimple, growCount(IUItem.Uran238, 4),
                growCount(IUItem.smallPlutonium, 1), growCount(IUItem.ironDust, 1)
        );
        addcentrifuge(IUItem.reactorDepleteduranDual, growCount(IUItem.Uran238, 8),
                growCount(IUItem.smallPlutonium, 2), growCount(IUItem.ironDust, 3)
        );
        addcentrifuge(IUItem.reactorDepleteduranQuad, growCount(IUItem.Uran238, 16),
                growCount(IUItem.smallPlutonium, 4), growCount(IUItem.ironDust, 7)
        );

        addcentrifuge1("crushedPurifiedUranium", new ItemStack(IUItem.Plutonium.getItem(), 1, 5),
                new ItemStack(IUItem.Uran238.getItem(), 6, IUItem.Uran238.getItemDamage())
        );
        addcentrifuge1("crushedUranium", new ItemStack(IUItem.Plutonium.getItem(), 1, 5),
                new ItemStack(IUItem.Uran238.getItem(), 4, IUItem.Uran238.getItemDamage()), IUItem.stoneDust
        );
        addcentrifuge(growCount(IUItem.UranFuel, 20), growCount(IUItem.Uran238, 112),
                growCount(IUItem.Uran235, 7)
        );
    }

    public static void addcentrifuge(ItemStack stack, ItemStack... output) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 5000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(stack)
                        ),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void addcentrifuge1(String stack, ItemStack... output) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 5000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(stack)
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
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(stack)
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
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 2000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crushed, 1, meta))
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
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("minHeat", (short) 2000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("crushed" + meta)
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
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("crushedPurified" + meta)
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
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.purifiedcrushed, 1, meta))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

}
