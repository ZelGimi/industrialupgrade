package com.denfop.integration.jei.multiblock;

import com.denfop.Localization;
import com.denfop.api.multiblock.MultiBlockStructure;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import javax.annotation.Nonnull;
import java.util.List;

public class MultiBlockWrapper implements IRecipeWrapper {


    public final MultiBlockStructure structure;
    public final String name;

    public MultiBlockWrapper(MultiBlockHandler container) {


        this.structure = container.getStructure();
        this.name = container.getName();

    }


    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, this.structure.itemStackList);
    }

    public void renderTextCentered(@Nonnull Minecraft minecraft,String text, int xCenter, int y, int maxWidth, int color) {
        FontRenderer fontRenderer = minecraft.fontRenderer;


        List<String> lines = fontRenderer.listFormattedStringToWidth(text, maxWidth);


        int lineHeight = fontRenderer.FONT_HEIGHT;


        int totalHeight = lines.size() * lineHeight;


        int startY = y - totalHeight / 2;


        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineWidth = fontRenderer.getStringWidth(line);
            int x = xCenter - lineWidth / 2;
            fontRenderer.drawString(line, x, startY + i * lineHeight, color);
        }
    }
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String name1 = Localization.translate("multiblock.jei1");
        int xCenter = recipeWidth / 2;
        int maxWidth = recipeWidth - 20;
        int color = 4210752;

        renderTextCentered(minecraft, name1, xCenter, 25, maxWidth, color);
        int y = 2;

        renderTextCentered(minecraft, Localization.translate("multiblock.jei2"), xCenter, (57 + y * 25), maxWidth,
                color);

        renderTextCentered(minecraft, Localization.translate("multiblock.jei3"), xCenter, (10 + y * 25) + 90, maxWidth,
                color);

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
