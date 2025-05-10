package com.denfop.integration.jei.microchip;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityMatterFactory;
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

import javax.annotation.Nonnull;

public class MicrochipCategory extends GuiIU implements IRecipeCategory<MicrochipHandler> {

    private final JeiInform jeiInform;

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public MicrochipCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityMatterFactory) BlockBaseMachine3.matter_factory.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICirsuit".toLowerCase() +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.machines, 1, 6).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public RecipeType<MicrochipHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void draw(MicrochipHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        energy++;
        double energylevel = Math.min(14.0F * energy / 100, 14);
        double xScale = 24D * progress / 100;
        double xScale1 = 10.0F * progress / 100;
        double xScale2 = 19.0F * progress / 100;
        if (xScale > 24.0F) {
            progress = 0;
        }

        Minecraft.getInstance().font.draw(stack, "" + recipe.getTemperature() + "°C", 70, 80, 4210752);

      bindTexture(getTexture());
        int temperature = 38 * recipe.getTemperature() / 5000;
        if (temperature > 0) {
            drawTexturedModalRect(stack, 67, 60, 176, 21, temperature + 1, 11);
        }
        drawTexturedModalRect(stack,25, 9, 176, 34, (int) (xScale + 1), 32);


        drawTexturedModalRect(stack,57, 13, 176, 65, (int) (xScale1 + 1), 21);


        drawTexturedModalRect(stack,86, 19, 176, 86, (int) (xScale2 + 1), 7);


        drawTexturedModalRect(stack,2, 72 - 13 + 14 - (int) energylevel, 176, 14 - (int) energylevel,
                14, (int) energylevel
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MicrochipHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,7, 6).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT,7, 27).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.INPUT,40, 6).addItemStack(recipe.getInput2());
        builder.addSlot(RecipeIngredientRole.INPUT,40, 26).addItemStack(recipe.getInput3());
        builder.addSlot(RecipeIngredientRole.INPUT,68, 16).addItemStack(recipe.getInput4());
        builder.addSlot(RecipeIngredientRole.OUTPUT,109, 16).addItemStack(recipe.getOutput());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(recipe.getContainer().input.getAllStackInputs());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICirsuit.png".toLowerCase());
    }


}
