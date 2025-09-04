package com.denfop.integration.jei.reactorschemes.fluid.simple;


import com.denfop.IUItem;
import com.denfop.api.reactors.*;
import com.denfop.inventory.Inventory;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactorSchemesFluidSimpleHandler {

    private static final List<ReactorSchemesFluidSimpleHandler> recipes = new ArrayList<>();

    private final EnumReactors reactors;
    private final int generation;
    private final int radGen;
    private final List<Tuple<Character, Item>> input;
    private final List<String> pattern;
    private final CreativeReactor reactor;
    private boolean stable;
    private LogicCreativeReactor logicReactor;


    public ReactorSchemesFluidSimpleHandler(EnumReactors reactors, List<String> pattern, List<Tuple<Character, Item>> tuple) {
        this.input = tuple;
        this.reactors = reactors;
        this.pattern = pattern;
        AtomicInteger count = new AtomicInteger();
        pattern.forEach(patterns -> count.addAndGet(patterns.length()));
        Inventory invSlot = new Inventory(null, Inventory.TypeItemSlot.INPUT, count.get());
        final List<ItemStack> inputs = Arrays.asList(convertPatternToLayout(pattern, tuple, reactors.getWidth(), reactors.getHeight()));
        for (int i = 0; i < inputs.size(); i++) {
            invSlot.set(i, inputs.get(i));
        }
        this.reactor = new CreativeReactor(this.reactors, invSlot);
        switch (this.reactors.getType()) {
            case FLUID:
                this.logicReactor = new LogicCreativeFluidReactor(reactor);
                break;
            case GAS_COOLING_FAST:
                this.logicReactor = new LogicCreativeGasReactor(reactor);
                break;
            case GRAPHITE_FLUID:
                this.logicReactor = new LogicCreativeGraphiteReactor(reactor);
                break;
            case HIGH_SOLID:
                this.logicReactor = new LogicCreativeHeatReactor(reactor);
                break;
        }
        for (int i = 0; i < 100; i++)
            logicReactor.onTick();
        this.stable = true;
        for (LogicCreativeComponent component : logicReactor.getListComponent())
            if (component.getDamageItem() != component.getMaxDamage()) {
                stable = false;
                break;
            }
        this.generation = logicReactor.getGeneration();
        this.radGen = logicReactor.getRadGeneration();

    }

    public static List<ReactorSchemesFluidSimpleHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ReactorSchemesFluidSimpleHandler addRecipe(int generation, EnumReactors reactors, int radGen, List<String> pattern, List<Tuple<Character, Item>> tuple) {

        ReactorSchemesFluidSimpleHandler recipe = new ReactorSchemesFluidSimpleHandler(reactors, pattern, tuple);

        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(18, EnumReactors.FS, 3, List.of(" A ", "ABA", " A "), List.of(new Tuple<>('A', IUItem.reactor_plate.getItem()), new Tuple<>('B', IUItem.mox_fuel_rod.getItem())));
        addRecipe(149, EnumReactors.FS, 9, List.of("ABA", "BCB", "ADA"), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.quad_uranium_fuel_rod.getItem()), new Tuple<>('D', IUItem.dual_uranium_fuel_rod.getItem())));
        addRecipe(349, EnumReactors.FS, 21, List.of("ABA", "BCB", "ADA"), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.quad_mox_fuel_rod.getItem()), new Tuple<>('D', IUItem.dual_mox_fuel_rod.getItem())));
        addRecipe(300, EnumReactors.FS, 18, List.of("ABA", "BCB", "ADA"), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.reactoruran233Quad.getItem()), new Tuple<>('D', IUItem.reactoruran233Dual.getItem())));
        addRecipe(112, EnumReactors.FS, 6, List.of("ABA", "BCB", "ABA"), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.quad_uranium_fuel_rod.getItem()), new Tuple<>('D', IUItem.dual_uranium_fuel_rod.getItem())));
        addRecipe(262, EnumReactors.FS, 14, List.of("ABA", "BCB", "ABA"), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.quad_mox_fuel_rod.getItem()), new Tuple<>('D', IUItem.dual_mox_fuel_rod.getItem())));
        addRecipe(225, EnumReactors.FS, 12, List.of("ABA", "BCB", "ABA"), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.reactoruran233Quad.getItem()), new Tuple<>('D', IUItem.reactoruran233Dual.getItem())));
        addRecipe(37, EnumReactors.FS, 3, List.of(" B ", "BDB", " B "), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.quad_uranium_fuel_rod.getItem()), new Tuple<>('D', IUItem.dual_uranium_fuel_rod.getItem())));
        addRecipe(87, EnumReactors.FS, 7, List.of(" B ", "BDB", " B "), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.quad_mox_fuel_rod.getItem()), new Tuple<>('D', IUItem.dual_mox_fuel_rod.getItem())));
        addRecipe(75, EnumReactors.FS, 6, List.of(" B ", "BDB", " B "), List.of(new Tuple<>('A', IUItem.componentVent.getItem()), new Tuple<>('B', IUItem.vent.getItem()), new Tuple<>('C', IUItem.reactoruran233Quad.getItem()), new Tuple<>('D', IUItem.reactoruran233Dual.getItem())));


        addRecipe(1350, EnumReactors.FA, 72, List.of(" BAB", "BACA", "ACAB", "BAB "), List.of(new Tuple<>('A', IUItem.adv_heat_exchange.getItem()), new Tuple<>('B', IUItem.adv_componentVent.getItem()), new Tuple<>('C', IUItem.reactoramericiumQuad.getItem())));
        addRecipe(900, EnumReactors.FA, 48, List.of(" BAB", "BACA", "ACAB", "BAB "), List.of(new Tuple<>('A', IUItem.adv_heat_exchange.getItem()), new Tuple<>('B', IUItem.adv_componentVent.getItem()), new Tuple<>('C', IUItem.reactortoriyQuad.getItem())));
        addRecipe(900, EnumReactors.FA, 48, List.of("ABAB", "BACA", "ACAB", "BABA"), List.of(new Tuple<>('A', IUItem.adv_heat_exchange.getItem()), new Tuple<>('B', IUItem.adv_componentVent.getItem()), new Tuple<>('C', IUItem.reactortoriyQuad.getItem())));
        addRecipe(1050, EnumReactors.FA, 56, List.of(" BAB", "BACA", "ACAB", "BAB "), List.of(new Tuple<>('A', IUItem.adv_heat_exchange.getItem()), new Tuple<>('B', IUItem.adv_componentVent.getItem()), new Tuple<>('C', IUItem.reactorneptuniumQuad.getItem())));
        addRecipe(1050, EnumReactors.FA, 56, List.of("ABAB", "BACA", "ACAB", "BABA"), List.of(new Tuple<>('A', IUItem.adv_heat_exchange.getItem()), new Tuple<>('B', IUItem.adv_componentVent.getItem()), new Tuple<>('C', IUItem.reactorneptuniumQuad.getItem())));
        addRecipe(1397, EnumReactors.FA, 72, List.of("ABAC", "BDBA", "ABDB", "CABE"), List.of(new Tuple<>('A', IUItem.adv_componentVent.getItem()), new Tuple<>('B', IUItem.adv_Vent.getItem()), new Tuple<>('C', IUItem.reactoruran233Simple.getItem()), new Tuple<>('D', IUItem.reactoramericiumQuad.getItem()), new Tuple<>('E', IUItem.uranium_fuel_rod.getItem())));

        addRecipe(16200, EnumReactors.FI, 864, List.of("ABABA", "BCBCB", "ABABA", "BCBCB", "ABABA"), List.of(new Tuple<>('A', IUItem.imp_componentVent.getItem()), new Tuple<>('B', IUItem.imp_Vent.getItem()), new Tuple<>('C', IUItem.reactorcaliforniaQuad.getItem())));

    }

    public ItemStack[] convertPatternToLayout(List<String> pattern, List<Tuple<Character, Item>> keys, int width, int height) {
        ItemStack[] layout = new ItemStack[width * height];

        Map<Character, ItemStack> keyMap = new HashMap<>();
        for (Tuple<Character, Item> tuple : keys) {
            keyMap.put(tuple.getA(), new ItemStack(tuple.getB()));
        }

        for (int y = 0; y < pattern.size(); y++) {
            String row = pattern.get(y);
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                int index = x + y * width;
                if (c == ' ' || !keyMap.containsKey(c)) {
                    layout[index] = ItemStack.EMPTY;
                } else {
                    layout[index] = keyMap.get(c).copy();
                }
            }
        }


        for (int y = pattern.size(); y < height; y++) {
            for (int x = 0; x < width; x++) {
                layout[x + y * width] = ItemStack.EMPTY;
            }
        }

        return layout;
    }

    public List<String> getPattern() {
        return pattern;
    }

    public List<Tuple<Character, Item>> getInput() {
        return input;
    }

    public int getGeneration() {
        return generation;
    }

    public EnumReactors getReactors() {
        return reactors;
    }

    public int getRadGen() {
        return radGen;
    }


}
