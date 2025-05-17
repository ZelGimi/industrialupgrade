package com.denfop.integration.jei.anvil;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
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

public class AnvilCategory extends GuiIU implements IRecipeCategory<AnvilHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;
    public AnvilCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileAdvAlloySmelter) BlockBaseMachine1.adv_alloy_smelter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
        this.init();
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Override
    public RecipeType<AnvilHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockAnvil.block_anvil).getDescriptionId());
    }



    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AnvilHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,5,25).addItemStack(recipes.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT,30,25).addItemStack(new ItemStack(IUItem.ForgeHammer.getItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT,60,25).addItemStack(recipes.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipes.getContainer().input.getAllStackInputs());


    }


    @Override
    public void draw(AnvilHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.drawSplitString(stack, Minecraft.getInstance().font, Localization.translate("iu.anvil.jei"), 5, 3,
                140 - 5, 4210752
        );
        this.drawSplitString(stack, Minecraft.getInstance().font,"+", 26, 31,
                140 - 5, 4210752
        );
        this.drawSplitString(stack, Minecraft.getInstance().font,"->", 47, 31,
                140 - 5, 4210752
        );
        this.drawSplitString(stack, Minecraft.getInstance().font,Localization.translate("iu.anvil.jei1") + "." + Localization.translate(
                        "iu.anvil.jei2"), 5, 50,
                140 - 5, 4210752
        );
    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
