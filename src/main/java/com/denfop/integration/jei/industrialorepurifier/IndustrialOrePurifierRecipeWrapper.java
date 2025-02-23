package com.denfop.integration.jei.industrialorepurifier;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IndustrialOrePurifierRecipeWrapper implements IRecipeWrapper {


    public final int chance;
    private final ItemStack inputstack;
    private final ItemStack outputstack;

    public IndustrialOrePurifierRecipeWrapper(IndustrialOrePurifierHandler container) {


        this.inputstack = container.getInput();
        this.chance = container.getChance();
        this.outputstack = container.getOutput();

    }

    public List<ItemStack> getInputs1() {
        return Arrays.asList(inputstack);
    }

    public ItemStack getInput() {
        return inputstack;
    }


    public List<ItemStack> getInputs() {


        return Arrays.asList(this.inputstack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int y = 58;
        int x = 80;

        int chance = this.chance;

        minecraft.fontRenderer.drawSplitString("" + chance + " SE/t", x, y, recipeWidth - x, 4210752);

    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0).tex(
                (float) (textureX) * 0.00390625F,
                (float) (textureY + height) * 0.00390625F
        ).endVertex();
        bufferbuilder
                .pos(x + width, y + height, 0)
                .tex((float) (textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F)
                .endVertex();
        bufferbuilder
                .pos(x + width, y, 0)
                .tex((float) (textureX + width) * 0.00390625F, (float) (textureY) * 0.00390625F)
                .endVertex();
        bufferbuilder.pos(x, y, 0).tex(
                (float) (textureX) * 0.00390625F,
                (float) (textureY) * 0.00390625F
        ).endVertex();
        tessellator.draw();
    }

}
