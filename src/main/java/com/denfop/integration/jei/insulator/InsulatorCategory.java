package com.denfop.integration.jei.insulator;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerFluidIntegrator;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityFluidIntegrator;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class InsulatorCategory extends GuiIU implements IRecipeCategory<InsulatorRecipeWrapper> {

    private final IDrawableStatic bg;
    private final ContainerFluidIntegrator container1;
    private final GuiComponent slots1;
    private final GuiComponent progress_bar;
    private int progress;


    public InsulatorCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileEntityFluidIntegrator) BlockBaseMachine3.fluid_integrator.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                107
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerFluidIntegrator) this.getContainer();
        progress_bar = new GuiComponent(this, 70 + 30, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 10, 17, container1.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 46, 17, container1.base.fluidTank2));
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.insulator.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate((JEICompat.getBlockStack(BlockBaseMachine3.insulator)).getUnlocalizedName());
    }

    @Nonnull
    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void drawExtras(final Minecraft mc) {
        progress++;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(10, 0);
        this.slots1.drawBackground(5, 0);

        progress_bar.renderBar(-10, 10, xScale);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final InsulatorRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();
        final IGuiItemStackGroup isg = layout.getItemStacks();

        fff.init(0, true, 14, 21, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInputFluid());

        fff.init(1, false, 50, 21, 12, 47, 10000, true, null);
        fff.set(1, recipes.getOutputFluid());


        isg.init(0, true, 60 + 10 - 1, 44 - 1);
        isg.set(0, recipes.getInput());
        isg.init(1, false, 115 + 5 - 1, 44 - 1);
        isg.set(1, recipes.getOutput());


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
