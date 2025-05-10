package com.denfop.integration.jei.heavyanvil;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.BlockStrongAnvil;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;
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
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class HeavyAnvilCategory extends GuiIU implements IRecipeCategory<HeavyAnvilHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public HeavyAnvilCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileBaseHandlerHeavyOre) BlockBaseMachine1.handler_ho.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockStrongAnvil.block_strong_anvil).getDescriptionId());
    }

    @Override
    public RecipeType<HeavyAnvilHandler> getRecipeType() {
        return jeiInform.recipeType;
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(HeavyAnvilHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString(stack,Localization.translate("iu.anvil.jei"), 5, 3,
                140 - 5, 4210752
        );
        drawSplitString(stack,"+", 26, 31,
                140 - 5, 4210752
        );
       drawSplitString(stack,"->", 47, 31,
               140 - 5, 4210752
        );
       drawSplitString(stack,Localization.translate("iu.anvil.jei1") + "." + Localization.translate(
                        "iu.anvil.jei2"), 5, 50,
               140 - 5, 4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HeavyAnvilHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,5,25).addItemStack(recipes.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT,30,25).addItemStack(new ItemStack(IUItem.ObsidianForgeHammer.getItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT,60,25).addItemStack(recipes.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipes.getContainer().input.getAllStackInputs());


    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
