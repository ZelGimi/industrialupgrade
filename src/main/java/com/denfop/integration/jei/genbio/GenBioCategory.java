package com.denfop.integration.jei.genbio;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityGearMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class GenBioCategory extends ScreenMain implements IRecipeCategory<GenBioHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public GenBioCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityGearMachine) BlockMoreMachine3Entity.gearing.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI".toLowerCase() +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Override
    public RecipeType<GenBioHandler> getRecipeType() {
        return jeiInform.recipeType;
    }


    @Nonnull
    @Override
    public String getTitles() {
        return JEICompat.getBlockStack(BlockBaseMachine3Entity.gen_bio).getDisplayName().getString();
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(GenBioHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("iu.windgenerator1"),
                10,
                30,
                140 - 10,
                4210752
        );
        drawSplitString(stack,
                ModUtils.getString(recipe.getEnergy()),
                10,
                30 + 7,
                140 - 10,
                4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenBioHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 96, 22).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png".toLowerCase());
    }


}
