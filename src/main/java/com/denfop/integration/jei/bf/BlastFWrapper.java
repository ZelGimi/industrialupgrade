package com.denfop.integration.jei.bf;

import com.denfop.Ic2Items;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BlastFWrapper implements IRecipeWrapper {


    public BlastFWrapper(BlastFHandler container) {


    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, new ItemStack(Items.IRON_INGOT));
        ingredients.setOutput(VanillaTypes.ITEM, Ic2Items.advIronIngot);

    }


    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int temperature = 38 * 5000 / 5000;
        drawTexturedModalRect(62, 55, 177, 39, temperature + 1, 11);
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
