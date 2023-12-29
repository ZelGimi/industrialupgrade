package com.denfop.integration.jei.anvil;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.multiblock.MultiBlockStructure;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerMatterFactory;
import com.denfop.container.ContainerMoonSpotter;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.deposits.EarthQuarryWrapper;
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

public class AnvilCategory extends Gui implements IRecipeCategory<AnvilWrapper> {

    private final IDrawableStatic bg;
    public AnvilCategory(
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
        return BlockAnvil.block_anvil.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockAnvil.block_anvil).getUnlocalizedName());
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
            final AnvilWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true,5,25);
        isg.set(0, recipes.getInput());
        isg.init(1, true,30,25);
        isg.set(1,new ItemStack(IUItem.ForgeHammer));
        isg.init(2, false,60,25);
        isg.set(2, recipes.getOutput());



    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
