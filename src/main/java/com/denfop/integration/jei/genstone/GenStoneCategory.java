package com.denfop.integration.jei.genstone;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.annotation.Nonnull;

public class GenStoneCategory extends Gui implements IRecipeCategory<GenStoneRecipeWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;

    public GenStoneCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenStone" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine.gen_stone.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 7).getUnlocalizedName());
    }

    @Nonnull
    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void drawExtras(final Minecraft mc) {
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);


        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(
                54 - 48, 33 + 14 - energylevel, 176,
                14 - energylevel, 14, energylevel
        );
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        this.zLevel = 100.0F;
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Blocks.COBBLESTONE), +61,
                +25
        );

        GL11.glDisable(GL11.GL_LIGHTING);
        GlStateManager.enableLighting();

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GenStoneRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 34, 18);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 34, 46);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 86, 5);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiGenStone.png");
    }


}
