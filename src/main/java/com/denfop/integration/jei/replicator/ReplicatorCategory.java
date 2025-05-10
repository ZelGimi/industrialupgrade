package com.denfop.integration.jei.replicator;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
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
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class ReplicatorCategory extends GuiIU implements IRecipeCategory<ReplicatorHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public ReplicatorCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileImpOilRefiner) BlockBaseMachine3.imp_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI".toLowerCase() +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Override
    public RecipeType<ReplicatorHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.replicator_iu).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ReplicatorHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("cost.name") + "" + ModUtils.getString(recipe.getMatter() / 1000) + "b",
                10,
                30,
                160 - 10,
                4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReplicatorHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,  95, 21).setFluidRenderer(10000, true,12, 47).addFluidStack(FluidName.fluiduu_matter.getInstance().get(), (int) recipe.getMatter());
        builder.addSlot(RecipeIngredientRole.OUTPUT,31,11).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png".toLowerCase());
    }


}
