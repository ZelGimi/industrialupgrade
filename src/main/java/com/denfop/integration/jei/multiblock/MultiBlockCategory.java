package com.denfop.integration.jei.multiblock;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerMatterFactory;
import com.denfop.container.ContainerMoonSpotter;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityMatterFactory;
import com.denfop.tiles.mechanism.TileEntityMoonSpotter;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MultiBlockCategory extends Gui implements IRecipeCategory<MultiBlockWrapper> {

    private final IDrawableStatic bg;
    public MultiBlockCategory(
            final IGuiHelper guiHelper
    ) {

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein" +
                        ".png"), 3, 3, 140,
                140
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return "multiblock_iu";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate("multiblock.jei");
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
    public void drawExtras(@Nonnull final Minecraft mc) {



    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final MultiBlockWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        final MultiBlockStructure structure = recipes.structure;
        for (  int i = 0; i < structure.itemStackList.size(); i++) {
            int x = 5 + (i%6) * 20;
            int y = 30 + (i / 6) * 20;
            isg.init(i, true,x,y);
            isg.set(i,  structure.itemStackList.get(i));

        }
        double y = structure.itemStackList.size();
        y/=6;
        y+=1;
        isg.init(structure.itemStackList.size(), true, (int) (50 + Localization.translate("multiblock.jei2").length() * 3),
                (int) (10 +y *23));
        isg.set(structure.itemStackList.size(),  structure.itemStackList.get(0));

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
