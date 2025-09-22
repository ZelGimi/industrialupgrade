package com.denfop.integration.jei.reversetransriptor;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerImpOilRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileEntityPolymerizer;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class ReverseTransriptorCategory extends GuiIU implements IRecipeCategory<ReverseTransriptorWrapper> {

    private final IDrawableStatic bg;
    private final ContainerImpOilRefiner container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;

    public ReverseTransriptorCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileImpOilRefiner) BlockBaseMachine3.imp_refiner.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.container1 = (ContainerImpOilRefiner) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 40, 4,
                ((TileEntityPolymerizer) BlockBaseMachine3.polymerizer.getDummyTe()).fluidTank1
        ));

        this.addElement(TankGauge.createNormal(this, 100, 4,
                ((TileEntityPolymerizer) BlockBaseMachine3.polymerizer.getDummyTe()).fluidTank2
        ));

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.reverse_transcriptor.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.reverse_transcriptor).getUnlocalizedName());
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
    public void drawExtras(@Nonnull final Minecraft mc) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        mc.getTextureManager().bindTexture(getTexture());

        progress_bar.renderBar(0, 0, xScale);

        for (final GuiElement element : ((List<GuiElement>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop - 5);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final ReverseTransriptorWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();
        fff.init(0, true, 44, 8, 12, 47, 8000, true, null);
        fff.set(0, recipes.getInputstack());


        fff.init(1, false, 104, 8, 12, 47, 8000, true, null);
        fff.set(1, recipes.getOutputstack());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
