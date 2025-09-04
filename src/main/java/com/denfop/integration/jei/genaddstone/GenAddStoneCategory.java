package com.denfop.integration.jei.genaddstone;

import com.denfop.Constants;
import com.denfop.api.widget.ItemStackWidget;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityGearMachine;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;

public class GenAddStoneCategory extends ScreenMain implements IRecipeCategory<GenAddStoneHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int energy = 0;

    public GenAddStoneCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityGearMachine) BlockMoreMachine3Entity.gearing.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiGenStone".toLowerCase() +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Override
    public RecipeType<GenAddStoneHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.gen_addition_stone).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GenAddStoneHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);


        bindTexture(getTexture());
        drawTexturedModalRect(stack,
                54 - 48, 33 + 14 - energylevel, 176,
                14 - energylevel, 14, energylevel
        );
        new ItemStackWidget(this, 61, 25, () -> new ItemStack(Blocks.COBBLESTONE)).drawBackground(stack, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GenAddStoneHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 35, 19).addItemStack(recipes.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 35, 47).addItemStack(recipes.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 87, 6).addItemStack(recipes.getOutput());
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiGenStone.png".toLowerCase());
    }


}
