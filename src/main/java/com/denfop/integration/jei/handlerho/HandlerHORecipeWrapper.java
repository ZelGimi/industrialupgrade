package com.denfop.integration.jei.handlerho;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerHORecipeWrapper implements IRecipeWrapper {

    protected final ItemStack inputstack;
    protected final List<ItemStack> outputstack;
    protected final NBTTagCompound nbt;


    public HandlerHORecipeWrapper(HandlerHOHandler container) {


        this.inputstack = container.getInput();
        this.outputstack = container.getOutput();
        this.nbt = container.getNBT();
    }

    public ItemStack getInput() {
        return inputstack;
    }

    public NBTTagCompound getMeta() {
        return nbt;
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
        return inputs.isEmpty() ? Collections.emptyList() : Collections.singletonList(stack);
    }

    public List<ItemStack> getOutputs() {
        return this.outputstack;
    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, this.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, this.getOutputs());
    }


    public List<ItemStack> getOutput() {
        return outputstack;
    }

    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int size = this.getOutputs().size();
        int y = 11;
        int x = 122;
        int temperature = 38 * this.nbt.getShort("temperature") / 5000;
        int temp = this.nbt.getShort("temperature");
        if (temperature > 0) {
            drawTexturedModalRect(48, 49, 176, 50, temperature + 1, 11);
        }

        for (int i = 0; i < size; i++) {
            minecraft.fontRenderer.drawSplitString("" + this.nbt.getInteger("input" + i) + "%", x, y, recipeWidth - x, 4210752);
            y += 18;
        }
        minecraft.fontRenderer.drawString("" + temp + "°C", 56, 67, 4210752);

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
