package com.denfop.integration.jei.multiblock;

import com.denfop.Localization;
import com.denfop.api.multiblock.MultiBlockStructure;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MultiBlockWrapper implements IRecipeWrapper {


    public final MultiBlockStructure structure;
    public final String name;

    public MultiBlockWrapper(MultiBlockHandler container) {


        this.structure = container.getStructure();
        this.name = container.getName();

    }




    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM,   this.structure.itemStackList);
    }



    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        String name1= Localization.translate("multiblock.jei1");
        String name2= Localization.translate("multiblock."+this.name.toLowerCase());
        minecraft.fontRenderer.drawString(name1,-40 + 140 / 2 - name1.length(),0,4210752);
        minecraft.fontRenderer.drawString(name2,-35 + 140 / 2 - name2.length() ,10,4210752);
        double y = structure.itemStackList.size();
        y/=6;
        y+=1;
        minecraft.fontRenderer.drawString(Localization.translate("multiblock.jei2"),10 ,
                (int) (10 +y *25),4210752);
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.75,0.75,0.75);
        minecraft.fontRenderer.drawString(Localization.translate("multiblock.jei3"),-10 ,
                (int) (10 +y *25) + 42,4210752);
        GlStateManager.popMatrix();
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
