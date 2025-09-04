package com.denfop.integration.jei.crops;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.multimechanism.simple.BlockEntityMacerator;
import com.denfop.blocks.mechanism.BlockSimpleMachineEntity;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.integration.jei.IRecipeCategory;
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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class CropCategory extends ScreenMain implements IRecipeCategory<CropHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public CropCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(new ContainerMenuMultiMachine(Minecraft.getInstance().player,
                ((BlockEntityMacerator) BlockSimpleMachineEntity.macerator_iu.getDummyTe()), 1, true
        ));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }

    @Override
    public RecipeType<CropHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate("crop.jei1");
    }


    @SuppressWarnings("removal")
    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(CropHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        draw(stack, "->", 65, 48, 4210752);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CropHandler recipes, IFocusGroup focuses) {
        final List<ItemStack> list = Arrays.asList(recipes.getInputs(), new ItemStack(IUItem.crop.getItem(0)),
                recipes.getOutput().getSoil().getStack()
        );
        for (int i = 0; i < list.size(); i++) {
            int yDisplayPosition1 = 75 + (20 - list.size()) * i - list.size() * 16;
            builder.addSlot(RecipeIngredientRole.INPUT, 60 - 21, yDisplayPosition1).addItemStack(list.get(i));

        }
        builder.addSlot(RecipeIngredientRole.INPUT, 85, 44).addItemStack(recipes.getOutputs());
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
