package com.denfop.integration.jei.alloysmelter;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlloySmelterRecipeWrapper implements IRecipeWrapper {


    public final ItemStack inputstack;
    public final ItemStack outputstack;
    public final ItemStack inputstack1;
    public final short temperature;


    public AlloySmelterRecipeWrapper(AlloySmelterHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.outputstack = container.getOutput();
        this.temperature = container.temperature;

    }

    public ItemStack getInput() {
        return inputstack;
    }

    public ItemStack getInput1() {
        return inputstack1;
    }

    public List<List<ItemStack>> getInputs() {
        ItemStack inputs = this.inputstack;
        List<ItemStack> stack = new ArrayList<>();

        if (OreDictionary.getOreIDs(inputs).length > 0) {
            int id = OreDictionary.getOreIDs(inputs)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(inputs);
        }
        if (OreDictionary.getOreIDs(this.inputstack1).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack1)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack1);
        }

        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return new ArrayList<>(Collections.singleton(this.outputstack));
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public ItemStack getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int temperature = 38 * this.temperature / 5000;
        int temp = this.temperature;
        if (temperature > 0) {
            drawTexturedModalRect(99, 42, 176, 35, temperature + 1, 11);
        }
        minecraft.fontRenderer.drawString("" + temp + "°C", 72, 44, 4210752);

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
                .tex((float) (textureX + width) * 0.00390625F, (float) (textureY + 0) * 0.00390625F)
                .endVertex();
        bufferbuilder.pos(x, y, 0).tex(
                (float) (textureX) * 0.00390625F,
                (float) (textureY) * 0.00390625F
        ).endVertex();
        tessellator.draw();
    }

}
