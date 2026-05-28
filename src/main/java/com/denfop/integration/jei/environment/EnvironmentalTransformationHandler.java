package com.denfop.integration.jei.environment;

import com.denfop.IUItem;
import com.denfop.blocks.BlockNitrateMud;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class EnvironmentalTransformationHandler {

    private static final List<EnvironmentalTransformationHandler> recipes = new ArrayList<>();

    private final Type type;
    private final ItemStack firstInput;
    private final ItemStack secondInput;
    private final ItemStack intermediateOutput;
    private final ItemStack finalOutput;
    private final String titleKey;
    private final String descriptionKey;
    private final int totalTimeTicks;

    public EnvironmentalTransformationHandler(
            Type type,
            ItemStack firstInput,
            ItemStack secondInput,
            ItemStack intermediateOutput,
            ItemStack finalOutput,
            String titleKey,
            String descriptionKey,
            int totalTimeTicks
    ) {
        this.type = type;
        this.firstInput = firstInput.copy();
        this.secondInput = secondInput.copy();
        this.intermediateOutput = intermediateOutput.copy();
        this.finalOutput = finalOutput.copy();
        this.titleKey = titleKey;
        this.descriptionKey = descriptionKey;
        this.totalTimeTicks = totalTimeTicks;
    }

    public static List<EnvironmentalTransformationHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static EnvironmentalTransformationHandler addRecipe(
            Type type,
            ItemStack firstInput,
            ItemStack secondInput,
            ItemStack intermediateOutput,
            ItemStack finalOutput,
            String titleKey,
            String descriptionKey,
            int totalTimeTicks
    ) {
        EnvironmentalTransformationHandler recipe = new EnvironmentalTransformationHandler(
                type,
                firstInput,
                secondInput,
                intermediateOutput,
                finalOutput,
                titleKey,
                descriptionKey,
                totalTimeTicks
        );
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(
                Type.PEAT_FROM_COMPOSTER,
                new ItemStack(Blocks.COMPOSTER),
                new ItemStack(Items.WATER_BUCKET),
                ItemStack.EMPTY,
                new ItemStack(IUItem.blockResource.getItem(10)),
                "environmental_transformations.peat.title",
                "environmental_transformations.peat.description",
                0
        );

        addRecipe(
                Type.SALTPETER_FROM_MUD,
                new ItemStack(Blocks.MUD),
                new ItemStack(Items.ROTTEN_FLESH),
                new ItemStack(IUItem.nitrate_mud.getDefaultState().getBlock()),
                new ItemStack(IUItem.raw_saltpeter.getDefaultState().getBlock()),
                "environmental_transformations.saltpeter.title",
                "environmental_transformations.saltpeter.description",
                BlockNitrateMud.STAGE_TIME * 4
        );
    }

    public Type getType() {
        return type;
    }

    public ItemStack getFirstInput() {
        return firstInput.copy();
    }

    public ItemStack getSecondInput() {
        return secondInput.copy();
    }

    public ItemStack getIntermediateOutput() {
        return intermediateOutput.copy();
    }

    public ItemStack getFinalOutput() {
        return finalOutput.copy();
    }

    public boolean hasSecondInput() {
        return !secondInput.isEmpty();
    }

    public boolean hasIntermediateOutput() {
        return !intermediateOutput.isEmpty();
    }

    public String getTitleKey() {
        return titleKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public int getTotalTimeTicks() {
        return totalTimeTicks;
    }

    public int getTotalTimeSeconds() {
        return totalTimeTicks / 20;
    }

    public enum Type {
        PEAT_FROM_COMPOSTER,
        SALTPETER_FROM_MUD
    }
}
