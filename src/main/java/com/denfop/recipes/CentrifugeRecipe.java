package com.denfop.recipes;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.recipe.IInputHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class CentrifugeRecipe {

    public static void init() {

        addcentrifuge(IUItem.reactoramericiumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(0), 2));
        addcentrifuge(IUItem.reactoramericiumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(0), 4));
        addcentrifuge(IUItem.reactoramericiumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(0), 1));

        addcentrifuge(IUItem.reactorneptuniumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(1), 2));
        addcentrifuge(IUItem.reactorneptuniumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(1), 4));
        addcentrifuge(IUItem.reactorneptuniumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(1), 1));

        addcentrifuge(IUItem.reactorcuriumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(2), 2));
        addcentrifuge(IUItem.reactorcuriumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(2), 4));
        addcentrifuge(IUItem.reactorcuriumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(2), 1));

        addcentrifuge(IUItem.reactorcaliforniaDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(3), 2));
        addcentrifuge(IUItem.reactorcaliforniaQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(3), 4));
        addcentrifuge(IUItem.reactorcaliforniaSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(3), 1));

        addcentrifuge(IUItem.reactormendeleviumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(5), 2));
        addcentrifuge(IUItem.reactormendeleviumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(5), 4));
        addcentrifuge(IUItem.reactormendeleviumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(5), 1));

        addcentrifuge(IUItem.reactorberkeliumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(6), 2));
        addcentrifuge(IUItem.reactorberkeliumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(6), 4));
        addcentrifuge(IUItem.reactorberkeliumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(6), 1));

        addcentrifuge(IUItem.reactoreinsteiniumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(7), 2));
        addcentrifuge(IUItem.reactoreinsteiniumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(7), 4));
        addcentrifuge(IUItem.reactoreinsteiniumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(7), 1));

        addcentrifuge(IUItem.reactoruran233Dual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(8), 2));
        addcentrifuge(IUItem.reactoruran233Quad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(8), 4));
        addcentrifuge(IUItem.reactoruran233Simple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(8), 1));

        addcentrifuge(IUItem.reactortoriyDual.getItemStack(), new ItemStack(IUItem.toriy.getItem(), 2));
        addcentrifuge(IUItem.reactortoriyQuad.getItemStack(), new ItemStack(IUItem.toriy.getItem(), 4));
        addcentrifuge(IUItem.reactortoriySimple.getItemStack(), new ItemStack(IUItem.toriy.getItem(), 1));

        addcentrifuge(IUItem.reactorprotonDual.getItemStack(), new ItemStack(IUItem.proton.getItem(), 2));
        addcentrifuge(IUItem.reactorprotonQuad.getItemStack(), new ItemStack(IUItem.proton.getItem(), 4));
        addcentrifuge(IUItem.reactorprotonSimple.getItemStack(), new ItemStack(IUItem.proton.getItem(), 1));


        addcentrifuge(IUItem.reactorfermiumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(11), 2));
        addcentrifuge(IUItem.reactorfermiumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(11), 4));
        addcentrifuge(IUItem.reactorfermiumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(11), 1));


        addcentrifuge(IUItem.reactornobeliumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(10), 2));
        addcentrifuge(IUItem.reactornobeliumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(10), 4));
        addcentrifuge(IUItem.reactornobeliumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(10), 1));


        addcentrifuge(IUItem.reactorlawrenciumDual.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(9), 2));
        addcentrifuge(IUItem.reactorlawrenciumQuad.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(9), 4));
        addcentrifuge(IUItem.reactorlawrenciumSimple.getItemStack(), new ItemStack(IUItem.radiationresources.getStack(9), 1));

        addcentrifuge(0, null);
        addcentrifuge(1, new ItemStack(Blocks.SAND));
        addcentrifuge(2, new ItemStack(IUItem.smalldust.getStack(10), 1));
        addcentrifuge(3, new ItemStack(IUItem.smalldust.getStack(16), 1));
        addcentrifuge(6, new ItemStack(IUItem.smalldust.getStack(1), 1));
        addcentrifuge(7, new ItemStack(IUItem.smalldust.getStack(16), 1));
        addcentrifuge(8, new ItemStack(IUItem.smalldust.getStack(0), 1));
        addcentrifuge(9, new ItemStack(IUItem.smalldust.getStack(2), 1));
        addcentrifuge(10, new ItemStack(IUItem.smalldust.getStack(0), 1));
        addcentrifuge(11, new ItemStack(IUItem.smalldust.getStack(7), 1));
        addcentrifuge(12, new ItemStack(IUItem.smalldust.getStack(10), 1));
        addcentrifuge(14, null);
        addcentrifuge(15, null);
        addcentrifuge(16, null);
        addcentrifuge(17, new ItemStack(IUItem.smalldust.getStack(9), 1));
        addcentrifuge(18, new ItemStack(IUItem.smalldust.getStack(15), 1));
        addcentrifuges(28, new ItemStack(IUItem.smalldust.getStack(26), 1));
        addcentrifuges(29, new ItemStack(IUItem.smalldust.getStack(27), 1));
        addcentrifuges(30, new ItemStack(IUItem.smalldust.getStack(24), 1));
        addcentrifuges(31, new ItemStack(IUItem.smalldust.getStack(46), 1));
        addcentrifuges(32, new ItemStack(IUItem.smalldust.getStack(27), 1));
        addcentrifuges(33, new ItemStack(IUItem.smalldust.getStack(10), 1));
        addcentrifuges(34, new ItemStack(IUItem.smalldust.getStack(26), 1));
        addcentrifuges(35, new ItemStack(IUItem.smalldust.getStack(24), 1));
        addcentrifuges(36, new ItemStack(IUItem.smalldust.getStack(35), 1));
        addcentrifuges(37, new ItemStack(IUItem.smalldust.getStack(45), 1));
        addcentrifuges(38, new ItemStack(IUItem.smalldust.getStack(9), 1));
        addcentrifuges(39, new ItemStack(IUItem.smalldust.getStack(42), 1));
        addcentrifuges(40, new ItemStack(IUItem.smalldust.getStack(1), 1));
        addcentrifuges(41, new ItemStack(IUItem.smalldust.getStack(14), 1));
        addcentrifuges(42, new ItemStack(IUItem.smalldust.getStack(27), 1));

        addcentrifuge1(0, null);
        addcentrifuge1(1, new ItemStack(Blocks.SAND));
        addcentrifuge1(2, new ItemStack(IUItem.smalldust.getStack(10), 1));
        addcentrifuge1(3, new ItemStack(IUItem.smalldust.getStack(16), 1));
        addcentrifuge1(6, new ItemStack(IUItem.smalldust.getStack(1), 1));
        addcentrifuge1(7, new ItemStack(IUItem.smalldust.getStack(16), 1));
        addcentrifuge1(8, new ItemStack(IUItem.smalldust.getStack(0), 1));
        addcentrifuge1(9, new ItemStack(IUItem.smalldust.getStack(2), 1));
        addcentrifuge1(10, new ItemStack(IUItem.smalldust.getStack(0), 1));
        addcentrifuge1(11, new ItemStack(IUItem.smalldust.getStack(7), 1));
        addcentrifuge1(12, new ItemStack(IUItem.smalldust.getStack(10), 1));
        addcentrifuge1(14, null);
        addcentrifuge1(15, null);
        addcentrifuge1(16, null);
        addcentrifuge1(17, new ItemStack(IUItem.smalldust.getStack(9), 1));
        addcentrifuge1(18, new ItemStack(IUItem.smalldust.getStack(15), 1));
        addcentrifuges1(28, new ItemStack(IUItem.smalldust.getStack(26), 2));
        addcentrifuges1(29, new ItemStack(IUItem.smalldust.getStack(27), 2));
        addcentrifuges1(30, new ItemStack(IUItem.smalldust.getStack(24), 2));
        addcentrifuges1(31, new ItemStack(IUItem.smalldust.getStack(46), 2));
        addcentrifuges1(32, new ItemStack(IUItem.smalldust.getStack(27), 2));
        addcentrifuges1(33, new ItemStack(IUItem.smalldust.getStack(10), 2));
        addcentrifuges1(34, new ItemStack(IUItem.smalldust.getStack(26), 2));
        addcentrifuges1(35, new ItemStack(IUItem.smalldust.getStack(24), 2));
        addcentrifuges1(36, new ItemStack(IUItem.smalldust.getStack(35), 2));
        addcentrifuges1(37, new ItemStack(IUItem.smalldust.getStack(45), 2));
        addcentrifuges1(38, new ItemStack(IUItem.smalldust.getStack(9), 2));
        addcentrifuges1(39, new ItemStack(IUItem.smalldust.getStack(42), 2));
        addcentrifuges1(40, new ItemStack(IUItem.smalldust.getStack(1), 2));
        addcentrifuges1(41, new ItemStack(IUItem.smalldust.getStack(14), 2));
        addcentrifuges1(42, new ItemStack(IUItem.smalldust.getStack(27), 2));


        addcentrifuge1("Gold", new ItemStack(IUItem.smalldust.getStack(14), 1));
        addcentrifuge("Gold", new ItemStack(IUItem.smalldust.getStack(14), 1));

        addcentrifuge("Lead", null);
        addcentrifuge1("Lead", IUItem.smallCopperDust);
        addcentrifuge("Tin", IUItem.smallIronDust);
        addcentrifuge("Copper", IUItem.smallTinDust);
        addcentrifuge1("Copper", IUItem.smallTinDust);
        addcentrifuge1("Tin", IUItem.smallIronDust);
        addcentrifuge1("Iron", IUItem.smallGoldDust);
        addcentrifuge("Iron", IUItem.smallGoldDust);
        addcentrifuge(new ItemStack(IUItem.iudust.getItemFromMeta(41), 1), new ItemStack(IUItem.crafting_elements.getItemFromMeta(481), 1));

        addcentrifuge("Osmium", new ItemStack(IUItem.smalldust.getItemFromMeta(17), 1));
        addcentrifuge1("Osmium", new ItemStack(IUItem.smalldust.getItemFromMeta(17), 1));
        addcentrifuge("Tantalum", new ItemStack(IUItem.smalldust.getItemFromMeta(11), 1));
        addcentrifuge1("Tantalum", new ItemStack(IUItem.smalldust.getItemFromMeta(11), 1));
        addcentrifuge("Cadmium", new ItemStack(IUItem.smalldust.getItemFromMeta(18), 1));
        addcentrifuge1("Cadmium", new ItemStack(IUItem.smalldust.getItemFromMeta(18), 1));

        addcentrifuge(
                new ItemStack(IUItem.clayDust.getItem(), 4),
                IUItem.silicondioxideDust
        );

        addcentrifuge(new ItemStack(Blocks.COBBLESTONE), IUItem.stoneDust);


        addcentrifuge(IUItem.mox_fuel_rod.getItemStack(), growCount(IUItem.Plutonium, 3),
                growCount(IUItem.ironDust, 1)
        );
        addcentrifuge(IUItem.dual_mox_fuel_rod.getItemStack(), growCount(IUItem.Plutonium, 6),
                growCount(IUItem.ironDust, 3)
        );
        addcentrifuge(IUItem.quad_mox_fuel_rod.getItemStack(), growCount(IUItem.Plutonium, 12),
                growCount(IUItem.ironDust, 7)
        );

        addcentrifuge(IUItem.uranium_fuel_rod.getItemStack(), growCount(IUItem.Uran238, 4),
                growCount(IUItem.ironDust, 1)
        );
        addcentrifuge(IUItem.dual_uranium_fuel_rod.getItemStack(), growCount(IUItem.Uran238, 8),
                growCount(IUItem.ironDust, 3)
        );
        addcentrifuge(IUItem.quad_uranium_fuel_rod.getItemStack(), growCount(IUItem.Uran238, 16),
                growCount(IUItem.ironDust, 7)
        );

        addcentrifuge1("forge:Purifiedcrushed/Uranium", new ItemStack(IUItem.nuclear_res.getStack(5), 1),
                new ItemStack(IUItem.Uran238, 6)
        );
        addcentrifuge1("forge:crushed/Uranium", new ItemStack(IUItem.nuclear_res.getItemFromMeta(5), 1),
                new ItemStack(IUItem.Uran238, 4), IUItem.stoneDust
        );
        addcentrifuge(growCount(IUItem.UranFuel, 10), growCount(IUItem.Uran238, 56),
                growCount(IUItem.Uran235, 3)
        );
        addcentrifuge(new ItemStack(IUItem.apatite.getItem()), new ItemStack(IUItem.iudust.getStack(65), 1), new ItemStack(IUItem.iudust.getStack(66), 1));
        addcentrifuge(
                new ItemStack(IUItem.apatite.getItem(1), 1),
                new ItemStack(IUItem.iudust.getStack(60), 1),
                new ItemStack(IUItem.iudust.getStack(68), 1),
                new ItemStack(IUItem.smalldust.getStack(1), 4)
        );
        addcentrifuge(
                new ItemStack(IUItem.iudust.getStack(68), 4),
                new ItemStack(IUItem.iudust.getStack(64), 1),
                new ItemStack(IUItem.iudust.getStack(37), 1)
        );
        addcentrifuge(new ItemStack(IUItem.white_phosphorus.getItem()), new ItemStack(IUItem.red_phosphorus.getItem(), 4));
    }

    public static void addcentrifuge(ItemStack stack, ItemStack... output) {
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 5000);
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

    public static void addcentrifuge(Item stack, ItemStack... output) {
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 5000);
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
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 5000);
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

    public static ItemStack growCount(Item stack, int count) {
        return new ItemStack(stack, count);
    }

    public static void addcentrifuge(ItemStack stack, ItemStack output) {
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 5000);
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
        stack[0] = new ItemStack(IUItem.iudust.getStack(meta), 1);
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 2000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crushed.getStack(meta), 1))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addcentrifuges(int meta, ItemStack output) {
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[3];
        } else {
            stack = new ItemStack[2];

        }
        stack[0] = new ItemStack(IUItem.iudust.getStack(meta + 16), 1);
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 2000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.crushed.getStack(meta), 1))
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
        final IInputHandler input = Recipes.inputFactory;
        stack[0] = input.getInput("forge:dusts/" + meta).getInputs().get(0).copy();
        stack[1] = IUItem.stoneDust;
        if (output != null) {
            stack[2] = output;
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 2000);

        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:crushed/" + meta)
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
        final IInputHandler input = Recipes.inputFactory;
        stack[0] = input.getInput("forge:dusts/" + meta).getInputs().get(0).copy();

        if (output != null) {
            stack[1] = output;
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 2000);
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput("forge:purifiedcrushed/" + meta)
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
        stack[0] = new ItemStack(IUItem.iudust.getStack(meta), 1);

        if (output != null) {
            stack[1] = output;
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 2000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.purifiedcrushed.getStack(meta), 1))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

    public static void addcentrifuges1(int meta, ItemStack output) {
        ItemStack[] stack;
        if (output != null) {
            stack = new ItemStack[2];
        } else {
            stack = new ItemStack[1];

        }
        stack[0] = new ItemStack(IUItem.iudust.getStack(meta + 16), 1);

        if (output != null) {
            stack[1] = output;
        }
        CompoundTag nbt = new CompoundTag();
        nbt.putShort("minHeat", (short) 2000);
        final IInputHandler input = Recipes.inputFactory;
        com.denfop.api.Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                input.getInput(new ItemStack(IUItem.purifiedcrushed.getStack(meta), 1))
                        ),
                        new RecipeOutput(nbt, stack)
                )
        );
    }

}
