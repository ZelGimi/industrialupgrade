package com.denfop.integration.jei.reactorschemes;


import com.denfop.IUItem;
import com.denfop.api.reactors.EnumReactors;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ReactorSchemesHandler {

    private static final List<ReactorSchemesHandler> recipes = new ArrayList<>();

    private final EnumReactors reactors;
    private final int generation;
    private final int radGen;
    private final List<Tuple<Character,Item>> input;
    private final List<String> pattern;


    public ReactorSchemesHandler(int generation, EnumReactors reactors, int radGen, List<String> pattern, List<Tuple<Character,Item>> tuple) {
        this.input = tuple;
        this.reactors = reactors;
        this.generation = generation;
        this.radGen = radGen;
        this.pattern=pattern;
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

    public static List<ReactorSchemesHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ReactorSchemesHandler addRecipe(int generation, EnumReactors reactors, int radGen, List<String> pattern, List<Tuple<Character,Item>> tuple) {

        ReactorSchemesHandler recipe = new ReactorSchemesHandler(generation,reactors,radGen,pattern,tuple);

        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        addRecipe(18,EnumReactors.FS,3, List.of(" A ", "ABA", " A "), List.of(new Tuple<>('A',IUItem.reactor_plate.getItem()),new Tuple<>('B',IUItem.mox_fuel_rod.getItem())));

    }


}
