package com.denfop.integration.jei.painting;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityOreWashing;
import com.denfop.blocks.mechanism.BlockMoreMachine3Entity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class PaintingCategory extends ScreenMain implements IRecipeCategory<PaintingHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public PaintingCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityOreWashing) BlockMoreMachine3Entity.orewashing.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPainter".toLowerCase() +
                        ".png"), 3, 3, 140,
                75
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine1, 1, 3).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(PaintingHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;


        int xScale = (int) (9 * progress / 100D);
        if (xScale >= 9) {
            progress = 0;
        }
        Minecraft.getInstance().font.draw(stack, ModUtils.mode(recipe.metadata), 64, 59, 4210752);
        bindTexture(getTexture());


        drawTexturedModalRect(stack, 76, 35, 179, 34, xScale, 13);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PaintingHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 33).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 33).addItemStack(recipe.getInput1());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 103, 33).addItemStack(recipe.getOutput());
        final ItemStack item = recipe.getOutput();
        final CompoundTag nbt = ModUtils.nbt(item);
        nbt.putString("mode", recipe.metadata.getString("mode"));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 103, 33).addItemStack(item);
    }

    @Override
    public RecipeType<PaintingHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPainter.png".toLowerCase());
    }


}
