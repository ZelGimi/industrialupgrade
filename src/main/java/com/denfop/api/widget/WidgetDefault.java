package com.denfop.api.widget;

import com.denfop.api.otherenergies.cool.ICoolSink;
import com.denfop.api.otherenergies.heat.IHeatSink;
import com.denfop.blocks.FluidName;
import com.denfop.componets.*;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static com.denfop.api.widget.ScreenWidget.bindBlockTexture;
import static com.denfop.api.widget.ScreenWidget.getBlockTextureMap;


public class WidgetDefault<T> {

    private final T component;

    public WidgetDefault(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }

    public boolean visible() {
        return true;
    }

    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY, final ScreenWidget screenWidget) {
        if (visible()) {
            if (this.component instanceof Energy) {
                Energy component = (Energy) this.component;
                double fillratio = component.getFillRatio();
                fillratio = Math.min(1, fillratio);
                if (screenWidget.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= screenWidget.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY() + screenWidget
                                    .getType()
                                    .getHeight() - chargeLevel,
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1() + screenWidget.getType().getHeight() - chargeLevel,
                            screenWidget.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= screenWidget.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY(),
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1(),
                            chargeLevel,
                            screenWidget.getType().getHeight()
                    );
                }

            } else if (this.component instanceof ComponentSteamEnergy) {
                ComponentSteamEnergy component = (ComponentSteamEnergy) this.component;

                if (component != null && component.buffer.storage >= 1) {
                    FluidStack fs = new FluidStack(FluidName.fluidsteam.getInstance().get(), 1);
                    int fluidX = screenWidget.x + 1;
                    int fluidY = screenWidget.y + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    screenWidget.getGui().drawSprite(poseStack,
                            mouseX + fluidX,
                            mouseY + fluidY,
                            fluidWidth,
                            fluidHeight,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
            } else if (this.component instanceof ComponentBioFuelEnergy) {
                ComponentBioFuelEnergy component = (ComponentBioFuelEnergy) this.component;

                if (component != null && component.buffer.storage >= 1) {
                    FluidStack fs = new FluidStack(FluidName.fluidbiomass.getInstance().get(), 1);
                    int fluidX = screenWidget.x + 1;
                    int fluidY = screenWidget.y + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    screenWidget.getGui().drawSprite(poseStack,
                            mouseX + fluidX,
                            mouseY + fluidY,
                            fluidWidth,
                            fluidHeight,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
            } else if (this.component instanceof ComponentBaseEnergy) {
                ComponentBaseEnergy component = (ComponentBaseEnergy) this.component;
                double fillratio = component.buffer.storage / component.buffer.capacity;
                if (screenWidget.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= screenWidget.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY() + screenWidget
                                    .getType()
                                    .getHeight() - chargeLevel,
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1() + screenWidget.getType().getHeight() - chargeLevel,
                            screenWidget.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= screenWidget.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY(),
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1(),
                            chargeLevel,
                            screenWidget.getType().getHeight()
                    );
                }
            } else if (this.component instanceof CoolComponent) {
                CoolComponent component = (CoolComponent) this.component;
                double fillratio = component.buffer.storage / component.buffer.capacity;
                if (screenWidget.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= screenWidget.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY() + screenWidget
                                    .getType()
                                    .getHeight() - chargeLevel,
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1() + screenWidget.getType().getHeight() - chargeLevel,
                            screenWidget.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= screenWidget.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY(),
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1(),
                            chargeLevel,
                            screenWidget.getType().getHeight()
                    );
                }
            } else if (this.component instanceof HeatComponent) {
                HeatComponent component = (HeatComponent) this.component;
                double fillratio = component.buffer.storage / component.buffer.capacity;
                if (screenWidget.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= screenWidget.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY() + screenWidget
                                    .getType()
                                    .getHeight() - chargeLevel,
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1() + screenWidget.getType().getHeight() - chargeLevel,
                            screenWidget.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= screenWidget.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY(),
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1(),
                            chargeLevel,
                            screenWidget.getType().getHeight()
                    );
                }
            } else if (this.component instanceof FluidTank) {
                FluidTank component = (FluidTank) this.component;
                double fillratio =
                        component.getFluidAmount() * 1D / component.getCapacity();
                if (screenWidget.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= screenWidget.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY() + screenWidget
                                    .getType()
                                    .getHeight() - chargeLevel,
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1() + screenWidget.getType().getHeight() - chargeLevel,
                            screenWidget.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= screenWidget.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    screenWidget.getGui().drawTexturedModalRect(
                            poseStack, mouseX + screenWidget.getX() + screenWidget.getType().getEndX(),
                            mouseY + screenWidget.getType().getEndY() + screenWidget.getY(),
                            screenWidget.getType().getX1(),
                            screenWidget.getType().getY1(),
                            chargeLevel + 1,
                            screenWidget.getType().getHeight()
                    );
                }
            }
        }
    }

    public String getText(final ScreenWidget screenWidget) {
        String text = "";

        if (this.component instanceof ComponentButton) {

            ComponentButton component = (ComponentButton) this.component;
            text = component.getText();

        } else if (this.component instanceof Energy) {

            Energy component = (Energy) this.component;
            text =
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity()) + " " +
                            "EF";

        } else if (this.component instanceof PressureComponent) {

            PressureComponent component = (PressureComponent) this.component;
            text = "Pressure: " +
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity());

        } else if (this.component instanceof ComponentTimer) {

            text =
                    ((ComponentTimer) this.component).getTime();

        } else if (this.component instanceof ComponentProgress) {

            text =
                    ModUtils.getString(Math.min(
                            100,
                            ((ComponentProgress) this.component).getBar() * 100
                    )) + "%";

        } else if (this.component instanceof ComponentBaseEnergy) {

            ComponentBaseEnergy component = (ComponentBaseEnergy) this.component;
            text =
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity()) + component.getType().getPrefix();

        } else if (this.component instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.component;
            text = ModUtils.getString(component.getProcess().getProgress(screenWidget.getIndex()) * 100) + "%";
        } else if (this.component instanceof ComponentSteamProcessRender) {
            ComponentSteamProcessRender component = (ComponentSteamProcessRender) this.component;
            text = ModUtils.getString(component.getProcess().getProgress(screenWidget.getIndex()) * 100) + "%";
        } else if (this.component instanceof ComponentBioProcessRender) {
            ComponentBioProcessRender component = (ComponentBioProcessRender) this.component;
            text = ModUtils.getString(component.getProcess().getProgress(screenWidget.getIndex()) * 100) + "%";
        } else if (this.component instanceof CoolComponent) {
            CoolComponent component = (CoolComponent) this.component;
            if (component.delegate == null) {
                component.createDelegate();
            }
            if (component.delegate instanceof ICoolSink) {
                text =
                        ModUtils.getString(component
                                .getEnergy()) + "°C" + "/" + ModUtils.getString(component.getCapacity()) + "°C";
                if (component
                        .getEnergy() == 100) {
                    text += "\n" + Localization.translate("iu.need_colling2");

                } else if (component
                        .getEnergy() >= 50 && component
                        .getEnergy() < 100) {
                    text += "\n" + Localization.translate("iu.need_colling1");

                } else if (component
                        .getEnergy() < 50 && component
                        .getEnergy() > 10) {
                    text += "\n" + Localization.translate("iu.need_colling");
                }
            } else {
                text = ("-" + component.getEnergy() + "°C" + "/-" + component.getCapacity());
            }
        } else if (this.component instanceof HeatComponent) {
            HeatComponent component = (HeatComponent) this.component;

            if (component.delegate == null) {
                component.createDelegate();
            }


            text =
                    ModUtils.getString(component
                            .getEnergy()) + "°C" + "/" + ModUtils.getString(component.getCapacity()) + "°C";
            if (component.delegate instanceof IHeatSink) {
                if (component.buffer.need) {
                    text += "\n" + Localization.translate("iu.need_heat");
                }
            }

        } else if (this.component instanceof FluidTank) {
            FluidTank component = (FluidTank) this.component;
            String text1;
            text1 = component.getFluid().getDisplayName().getString();
            if (component.getFluid().getFluid() == Fluids.EMPTY)
                text1 = "";
            text = "Fluid " + text1 + ": " +
                    ModUtils.getString(component
                            .getFluidAmount()) + "/" + ModUtils.getString(component.getCapacity());

        }


        return text;
    }

}
