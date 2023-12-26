package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUPotion;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.radiationsystem.EnumCoefficient;
import com.denfop.api.radiationsystem.EnumLevelRadiation;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerSoilAnalyzer;
import com.denfop.recipes.BaseRecipes;
import com.denfop.recipes.BasicRecipeTwo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

import static com.denfop.utils.ModUtils.getUnit;

public class GuiSoilAnalyzer extends GuiIU<ContainerSoilAnalyzer> {

    public GuiSoilAnalyzer(ContainerSoilAnalyzer guiContainer) {
        super(guiContainer);
        this.ySize = 123;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 21, 88, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (!this.container.base.analyzed) {
            new AdvArea(this, 28, 24, 147, 73)
                    .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (this.container.base.progress.getBar() * 100) + " %")
                    .drawForeground(par1, par2);
        } else {
            Radiation radiation = this.container.base.radiation;
            if (radiation == null) {
                new AdvArea(this, 28, 24, 147, 73).withTooltip(Localization.translate("radiation.info_not")).drawForeground(
                        par1,
                        par2
                );

            } else {
                new AdvArea(this, 28, 24, 147, 73).withTooltip(Localization.translate("radiation." + radiation
                        .getLevel()
                        .name()
                        .toLowerCase()) + (this.container.base.radiation.getLevel().ordinal() > 1 ?
                        ("\n" + Localization.translate(
                                "radiation" +
                                        ".info1") + "\n" +
                                Localization.translate(MobEffects.HUNGER.getName()) + "\n" +
                                Localization.translate(MobEffects.BLINDNESS.getName()) + "\n" +
                                Localization.translate(MobEffects.SLOWNESS.getName()) + "\n" +
                                Localization.translate(MobEffects.POISON.getName())) : "")

                        + (this.container.base.radiation.getLevel().ordinal() == 3 ?
                        ("\n" + Localization.translate(
                                "radiation.info2") + "\n" +
                                Localization.translate(IUPotion.radiation.getName())) : "")
                        + (this.container.base.radiation.getLevel().ordinal() == 4 ?
                        ("\n" + Localization.translate(
                                "radiation.info2") + "\n" +
                                Localization.translate(IUPotion.radiation.getName())+ "\n" +
                                Localization.translate(MobEffects.WITHER.getName())) : "")
                        + ((this.container.base.radiation.getLevel().ordinal() == 2) ?
                        ("\n" + Localization.translate(
                                "radiation.info3")) : "")
                        + ((this.container.base.radiation.getLevel().ordinal() > 2) ?
                        ("\n" + Localization.translate(
                                "radiation.info5")) : "")
                        + ("\n" + Localization.translate(
                                "radiation.info4") + "\n"+ BasicRecipeTwo.getBlockStack(BlockBaseMachine3.radiation_purifier).getDisplayName())

                ).drawForeground(par1
                        , par2);
            }
        }
        if (this.container.base.analyzed) {
            if (this.container.base.radiation != null) {
                new AdvArea(this, 39, 91, 76, 103).withTooltip(Localization.translate("radiation.dose") + Math.max(
                        1,
                        (int) this.container.base.radiation.getRadiation()
                ) + " " + getUnit(this.container.base.radiation.getCoef()) + "Sv").drawForeground(par1
                        , par2);
            }else{
                new AdvArea(this, 39, 91, 76, 103).withTooltip(Localization.translate("radiation.dose") + Math.max(
                        0.5,
                        0
                ) + " " + getUnit(EnumCoefficient.NANO) + "Sv").drawForeground(par1
                        , par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base.radiation == null) {
            this.drawTexturedRect(38, 91, 6, 13, 181, 17);
        } else {
            Radiation radiation = this.container.base.radiation;
            if (radiation.getLevel() == EnumLevelRadiation.LOW) {
                final double translate = (4 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(38 + translate, 91, 6, 13, 181, 17);
            } else if (radiation.getLevel() == EnumLevelRadiation.DEFAULT) {
                final double translate = (8 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(44 + translate, 91, 6, 13, 181, 17);
            } else if (radiation.getLevel() == EnumLevelRadiation.MEDIUM) {
                final double translate = (8 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(52 + translate, 91, 6, 13, 181, 17);
            } else if (radiation.getLevel() == EnumLevelRadiation.HIGH) {
                final double translate = (8 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(60 + translate, 91, 6, 13, 181, 17);
            } else if (radiation.getLevel() == EnumLevelRadiation.VERY_HIGH) {
                final double translate = (8 * radiation.getRadiation() / 1000);
                this.drawTexturedRect(68 + translate, 91, 6, 13, 181, 17);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 13, name, 4210752, false);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisoilanalyzer.png");
    }

}
