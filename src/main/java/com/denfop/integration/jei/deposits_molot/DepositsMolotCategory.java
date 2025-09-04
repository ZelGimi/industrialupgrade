package com.denfop.integration.jei.deposits_molot;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityLaserPolisher;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.world.vein.VeinType;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class DepositsMolotCategory extends ScreenMain implements IRecipeCategory<DepositsMolotHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public DepositsMolotCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityLaserPolisher) BlockBaseMachine3Entity.laser_polisher.getDummyTe()).getGuiContainer1(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png"), 3, 3, 200,
                180
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("deposists_molot.jei");
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(DepositsMolotHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        int y = 20;
        int x = 25;
        VeinType vein = recipe.getVeinType();
        this.drawSplitString(stack, Localization.translate("deposists.jei2") + " " + (vein.getHeavyOre() != null ?
                        new ItemStack(vein.getHeavyOre().getBlock(), 1).getDisplayName().getString() :
                        new ItemStack(vein.getOres().get(0).getBlock().getBlock(), 1
                        ).getDisplayName().getString()), 5, 3,
                200 - 5, 4210752
        );

    }

    @Override
    public RecipeType<DepositsMolotHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DepositsMolotHandler recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(new ItemStack(IUItem.molot.getItem()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStacks(recipe.getInputs());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
