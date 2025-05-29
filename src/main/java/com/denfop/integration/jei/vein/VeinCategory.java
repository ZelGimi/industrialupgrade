package com.denfop.integration.jei.vein;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileEntityUpgradeMachineFactory;
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
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VeinCategory extends GuiIU implements IRecipeCategory<VeinHandler> {

    private final IDrawableStatic bg;
    JeiInform jeiInform;

    public VeinCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityUpgradeMachineFactory) BlockBaseMachine3.upgrade_machine.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png"), 3, 3, 200,
                150
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }

    @Override
    public RecipeType<VeinHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.oilquarry).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(VeinHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("iu.vein.info") + ". " + Localization.translate("iu.vein.info1") + ". " + Localization.translate(
                        "iu.vein.info2") + ". " + Localization.translate("iu.vein.info3") + ". " + Localization.translate(
                        "iu.vein.info4") + ". " + Localization.translate("iu.vein.info5"),
                6,
                14, 200 - 5,
                4210752
        );
    }
    public List<ItemStack> getInputs() {
        List<ItemStack> stack = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            stack.add(ItemStackHelper.fromData(IUItem.heavyore, 1, i));
        }
        for (int i = 0; i < 14; i++) {
            stack.add(ItemStackHelper.fromData(IUItem.mineral, 1, i));
        }
        stack.add(ItemStackHelper.fromData(IUItem.oilblock));
        return stack;
    }
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, VeinHandler recipe, IFocusGroup focuses) {
        List<FluidStack> fluids = Arrays.asList(new FluidStack(
                        FluidName.fluidneft.getInstance().get(),
                        1000
                ), new FluidStack(FluidName.fluidgas.getInstance().get(), 1000)
                , new FluidStack(FluidName.fluidsour_heavy_oil.getInstance().get(), 1000)
                , new FluidStack(FluidName.fluidsour_medium_oil.getInstance().get(), 1000)
                , new FluidStack(FluidName.fluidsour_light_oil.getInstance().get(), 1000)
                , new FluidStack(FluidName.fluidsweet_medium_oil.getInstance().get(), 1000)
                , new FluidStack(FluidName.fluidsweet_heavy_oil.getInstance().get(), 1000));
        fluids.forEach(fluidStack -> builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addFluidStack(fluidStack.getFluid(),fluidStack.getAmount()));
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStacks(this.getInputs());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
