package com.denfop.integration.jei.battery_factory;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BatteryRecipeWrapper implements IRecipeWrapper {


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

    public BatteryRecipeWrapper(BatteryHandler container) {


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
    public List<ItemStack> getInputs1(){
        return Arrays.asList(inputstack,inputstack1,inputstack2,inputstack3,inputstack4,inputstack5,inputstack6,inputstack7,
                inputstack8);
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
        if (OreDictionary.getOreIDs(this.inputstack2).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack2)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack2);
        }
        if (OreDictionary.getOreIDs(this.inputstack3).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack3)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack3);
        }
        if (OreDictionary.getOreIDs(this.inputstack4).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack4)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack4);
        }
        if (OreDictionary.getOreIDs(this.inputstack5).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack5)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack5);
        }
        if (OreDictionary.getOreIDs(this.inputstack6).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack6)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack6);
        }
        if (OreDictionary.getOreIDs(this.inputstack7).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack7)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack7);
        }
        if (OreDictionary.getOreIDs(this.inputstack8).length > 0) {
            int id = OreDictionary.getOreIDs(this.inputstack8)[0];
            stack.addAll(OreDictionary.getOres(OreDictionary.getOreName(id)));
        } else {
            stack.add(this.inputstack8);
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
