package com.denfop.integration.jei.solidmatters;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntitySolidFluidIntegrator;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
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

import javax.annotation.Nonnull;

public class MatterCategory extends ScreenMain implements IRecipeCategory<MatterHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public MatterCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntitySolidFluidIntegrator) BlockBaseMachine3Entity.solid_fluid_integrator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolidmatter" +
                        ".png"), 3, 3, 148,
                80
        );
    }

    @Override
    public RecipeType<MatterHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.solidmatter, 1, 4).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(MatterHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("cost.name") + " " + ModUtils.getString((double) recipe.getEnergy()) + "EF",
                10,
                67,
                150 - 10,
                4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MatterHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 23).addItemStack(recipe.getInput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisynthesis.png");
    }


}
