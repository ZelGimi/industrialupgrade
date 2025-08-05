package com.denfop.integration.jei.geothermal;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockGeothermalPump;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerImpOilRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.base.TileEntityRefrigeratorFluids;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
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
import java.util.List;

public class GeoThermalCategory extends GuiIU implements IRecipeCategory<GeoThermalHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private final ContainerImpOilRefiner container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;

    public GeoThermalCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileImpOilRefiner) BlockBaseMachine3.imp_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
        this.componentList.clear();
        this.container1 = (ContainerImpOilRefiner) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 40, 4,
                ((TileEntityRefrigeratorFluids) BlockBaseMachine3.refrigerator_fluids.getDummyTe()).fluidTank1
        ));

        this.addElement(TankGauge.createNormal(this, 100, 4,
                ((TileEntityRefrigeratorFluids) BlockBaseMachine3.refrigerator_fluids.getDummyTe()).fluidTank2
        ));

    }

    @Override
    public RecipeType<GeoThermalHandler> getRecipeType() {
        return jeiInform.recipeType;
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockGeothermalPump.geothermal_controller).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GeoThermalHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        drawSplitString(stack,
                Localization.translate("cost.name") + " " + ModUtils.getString((double) 10) + " QE",
                10,
                65,
                140 - 10,
                4210752
        );
        int y = 80;
        int x = 35;
        for (int i = 0; i < 4; i++) {
            double percent = 2;
            if (i > 0) {
                percent = 0.5;
            }
            drawSplitString(stack, "-> " + percent + "%", x, y, 140 - x, 4210752);
            y += 16;
        }
        bindTexture(getTexture());

        progress_bar.renderBar(stack, 0, 0, xScale);

        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GeoThermalHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 8).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getInput().getFluid(), recipe.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 8).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 15, 75).addItemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 457));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 15, 93).addItemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 461));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 15, 108).addItemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 462));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 15, 126).addItemStack(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 463));
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
