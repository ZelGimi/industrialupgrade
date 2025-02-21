package com.denfop.integration.jei.upgrademachine;

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

public class UpgradeMachineRecipeWrapper implements IRecipeWrapper {


    private final ItemStack inputstack;
    private final ItemStack outputstack;
    private final ItemStack inputstack1;
    private final ItemStack inputstack2;
    private final ItemStack inputstack3;
    private final ItemStack inputstack4;
    private final ItemStack inputstack5;
    private final ItemStack inputstack7;
    private final ItemStack inputstack8;
    private final ItemStack inputstack6;

    public UpgradeMachineRecipeWrapper(UpgradeMachineHandler container) {


        this.inputstack = container.getInput();
        this.inputstack1 = container.getInput1();
        this.inputstack2 = container.getInput2();
        this.inputstack3 = container.getInput3();
        this.inputstack4 = container.getInput4();
        this.inputstack5 = container.getInput5();
        this.inputstack6 = container.getInput6();
        this.inputstack7 = container.getInput7();
        this.inputstack8 = container.getInput8();
        this.outputstack = container.getOutput();

    }

    public List<ItemStack> getInputs1() {
        return Arrays.asList(
                inputstack,
                inputstack1,
                inputstack2,
                inputstack3,
                inputstack4,
                inputstack5,
                inputstack6,
                inputstack7,
                inputstack8
        );
    }

    public ItemStack getInput() {
        return inputstack;
    }

    public ItemStack getInput1() {
        return inputstack1;
    }

    public ItemStack getInput2() {
        return inputstack2;
    }

    public ItemStack getInput3() {
        return inputstack3;
    }

    public ItemStack getInput4() {
        return inputstack4;
    }


    public ItemStack getInput5() {
        return inputstack5;
    }

    public ItemStack getInput6() {
        return inputstack6;
    }

    public ItemStack getInput7() {
        return inputstack7;
    }

    public ItemStack getInput8() {
        return inputstack8;
    }


    public List<ItemStack> getInputs() {
        List<ItemStack> stack = new ArrayList<>();
        for (ItemStack stack1 : getInputs1()) {
            if (!stack1.isEmpty()) {
                stack.add(stack1);
            }
        }
        return stack.isEmpty() ? Collections.emptyList() : stack;
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
