package com.denfop.integration.jei.gendiesel;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.multimechanism.simple.TileGearMachine;
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

public class GenDieselCategory extends GuiIU implements IRecipeCategory<GenDieselHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public GenDieselCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMultiMachine(Minecraft.getInstance().player,
                ((TileGearMachine) BlockMoreMachine3.gearing.getDummyTe()), 1, true
        ));
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI".toLowerCase() +
                        ".png"), 5, 5, 140,
                75
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine1, 1, 4).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public RecipeType<GenDieselHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void draw(GenDieselHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
       drawSplitString( stack,
                Localization.translate("iu.windgenerator1"),
                10,
                30,
                140 - 10,
                4210752
        );
      drawSplitString( stack,
                ModUtils.getString(recipe.getEnergy()),
                10,
                30 + 7,
              140 - 10,
                4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenDieselHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,95, 21).setFluidRenderer( 10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(),recipe.getOutput().getAmount());

    }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png".toLowerCase());
    }


}
