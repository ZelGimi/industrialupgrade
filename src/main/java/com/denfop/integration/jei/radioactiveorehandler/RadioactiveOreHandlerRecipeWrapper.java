package com.denfop.integration.jei.radioactiveorehandler;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RadioactiveOreHandlerRecipeWrapper implements IRecipeWrapper {


    public final int chance;
    public final ItemStack inputstack1;
    private final ItemStack inputstack;
    private final ItemStack outputstack;

    public RadioactiveOreHandlerRecipeWrapper(RadioactiveOreHandlerHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.chance = container.getChance();
        this.outputstack = container.getOutput();

    }

    public List<ItemStack> getInputs1() {
        return Arrays.asList(inputstack);
    }

    public ItemStack getInput() {
        return inputstack;
    }

    public ItemStack getInputstack1() {
        return inputstack1;
    }

    public List<ItemStack> getInputs() {


        return Arrays.asList(this.inputstack);
    }

    public List<ItemStack> getOutputs() {
        return Arrays.asList(this.outputstack, this.inputstack1);
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
        int x = 120;

        int chance = this.chance;

        minecraft.fontRenderer.drawSplitString("" + chance + "%", x, y, recipeWidth - x, 4210752);

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
