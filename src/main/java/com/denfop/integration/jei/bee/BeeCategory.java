package com.denfop.integration.jei.bee;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityBatteryFactory;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockHiveEntity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
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

public class BeeCategory extends ScreenMain implements IRecipeCategory<BeeHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public BeeCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityBatteryFactory) BlockBaseMachine3Entity.battery_factory.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockHiveEntity.forest_hive).getDescriptionId());
    }


    @Override
    public RecipeType<BeeHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(BeeHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.drawSplitString(stack, "+", 27, 28,
                140 - 5, 4210752
        );
        this.drawSplitString(stack, "->", 51, 28,
                140 - 5, 4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BeeHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 25).addItemStack(recipes.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 30, 25).addItemStack(new ItemStack(IUItem.net.getItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 25).addItemStack(recipes.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
