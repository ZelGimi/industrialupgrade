package com.denfop.integration.jei.handlerho;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.base.TileBaseHandlerHeavyOre;
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

public class HandlerHOCategory extends GuiIU implements IRecipeCategory<HandlerHOHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int energy = 0;
    private int progress = 0;

    public HandlerHOCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileBaseHandlerHeavyOre) BlockBaseMachine1.handler_ho.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());

        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guihandlerho" +
                        ".png"), 3, 3, 140,
                80
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine, 1, 12).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(HandlerHOHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;

        energy++;

        int xScale = (218 - 178) * progress / 100;
        if (xScale > (218 - 178)) {
            progress = 0;
        }
        int size = recipe.getOutput().size();
        int y = 17;
        int x = 128;

        int temp = recipe.nbt.getShort("temperature");


        for (int i = 0; i < size; i++) {
            drawSplitString(stack, "" + recipe.nbt.getInt("input" + i) + "%", x, y, 140 - x, 4210752);
            y += 19;
        }
        draw(stack, "" + temp + "°C", 62, 75, 4210752);


        bindTexture(getTexture());
        drawTexturedModalRect(stack, 59, 34, 178, 34, xScale + 1, 14);

    }

    @Override
    public RecipeType<HandlerHOHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HandlerHOHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 34, 33)
                .addItemStack(recipe.getInput());


        for (int i = 0; i < recipe.getOutput().size(); ++i) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 108, 15 + 18 * i)
                    .addItemStack(recipe.getOutput().get(i));
        }
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guihandlerho.png");
    }


}
