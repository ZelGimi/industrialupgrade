package com.denfop.integration.jei.fluidsolidmixer;

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

public class FluidSolidMixerCategory extends GuiIU implements IRecipeCategory<FluidSolidMixerRecipeWrapper> {

    private final IDrawableStatic bg;
    private final ContainerFluidIntegrator container1;
    private final GuiComponent progress_bar;
    private int progress;


    public FluidSolidMixerCategory(
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

        this.container1 = (ContainerFluidIntegrator) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 10, 17, container1.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 46 + 71, 17, container1.base.fluidTank2));
        this.addElement(TankGauge.createNormal(this, 46 + 71 - 28, 17, container1.base.fluidTank2));
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.solid_fluid_mixer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate((JEICompat.getBlockStack(BlockBaseMachine3.solid_fluid_mixer)).getUnlocalizedName());
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
        this.slots.drawBackground(-20, 0);

        progress_bar.renderBar(-10, 10, xScale);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final FluidSolidMixerRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();
        final IGuiItemStackGroup isg = layout.getItemStacks();

        fff.init(0, true, 14, 21, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInputFluid());

        fff.init(1, false, 50 + 71, 21, 12, 47, 10000, true, null);
        fff.set(1, recipes.getOutputFluid());

        fff.init(2, false, 50 + 71 - 28, 21, 12, 47, 10000, true, null);
        fff.set(2, recipes.getOutputFluid1());

        isg.init(0, true, 60 - 21, 44 - 1);
        isg.set(0, recipes.getInput());


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
