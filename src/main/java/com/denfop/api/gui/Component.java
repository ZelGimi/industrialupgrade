package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.api.cool.ICoolSink;
import com.denfop.api.heat.IHeatSink;
import com.denfop.blocks.FluidName;
import com.denfop.componets.*;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;

public class Component<T> {

    private final T component;

    public Component(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }

    public boolean visible() {
        return true;
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY, final GuiComponent guiComponent) {
        if (visible()) {
            if (this.component instanceof Energy) {
                Energy component = (Energy) this.component;
                double fillratio = component.getFillRatio();
                fillratio = Math.min(1, fillratio);
                if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= guiComponent.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY() + guiComponent
                                    .getType()
                                    .getHeight() - chargeLevel,
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1() + guiComponent.getType().getHeight() - chargeLevel,
                            guiComponent.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= guiComponent.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1(),
                            chargeLevel,
                            guiComponent.getType().getHeight()
                    );
                }

            } else if (this.component instanceof ComponentSteamEnergy) {
                ComponentSteamEnergy component = (ComponentSteamEnergy) this.component;

                if (component != null && component.storage >= 1) {
                    FluidStack fs = new FluidStack(FluidName.fluidsteam.getInstance().get(), 1);
                    int fluidX = guiComponent.x + 1;
                    int fluidY = guiComponent.y + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    guiComponent.getGui().drawSprite(poseStack,
                          mouseX+  fluidX,
                        mouseY+    fluidY,
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

                if (component != null && component.storage >= 1) {
                    FluidStack fs = new FluidStack(FluidName.fluidbiomass.getInstance().get(), 1);
                    int fluidX = guiComponent.x + 1;
                    int fluidY = guiComponent.y + 1;
                    int fluidWidth = 16;
                    int fluidHeight = 16;
                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    guiComponent.getGui().drawSprite(poseStack,
                            mouseX+     fluidX,
                            mouseY+    fluidY,
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
                double fillratio = component.storage / component.capacity;
                if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= guiComponent.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY() + guiComponent
                                    .getType()
                                    .getHeight() - chargeLevel,
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1() + guiComponent.getType().getHeight() - chargeLevel,
                            guiComponent.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= guiComponent.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1(),
                            chargeLevel,
                            guiComponent.getType().getHeight()
                    );
                }
            } else if (this.component instanceof CoolComponent) {
                CoolComponent component = (CoolComponent) this.component;
                double fillratio = component.storage / component.capacity;
                if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= guiComponent.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY() + guiComponent
                                    .getType()
                                    .getHeight() - chargeLevel,
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1() + guiComponent.getType().getHeight() - chargeLevel,
                            guiComponent.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= guiComponent.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1(),
                            chargeLevel,
                            guiComponent.getType().getHeight()
                    );
                }
            } else if (this.component instanceof HeatComponent) {
                HeatComponent component = (HeatComponent) this.component;
                double fillratio = component.storage / component.capacity;
                if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= guiComponent.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY() + guiComponent
                                    .getType()
                                    .getHeight() - chargeLevel,
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1() + guiComponent.getType().getHeight() - chargeLevel,
                            guiComponent.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= guiComponent.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX+guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1(),
                            chargeLevel,
                            guiComponent.getType().getHeight()
                    );
                }
            } else if (this.component instanceof FluidTank) {
                FluidTank component = (FluidTank) this.component;
                double fillratio =
                        component.getFluidAmount() * 1D / component.getCapacity();
                if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                    fillratio *= guiComponent.getType().getHeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY() + guiComponent
                                    .getType()
                                    .getHeight() - chargeLevel,
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1() + guiComponent.getType().getHeight() - chargeLevel,
                            guiComponent.getType().getWeight(),
                            chargeLevel
                    );
                } else {
                    fillratio *= guiComponent.getType().getWeight();
                    int chargeLevel = (int) fillratio;
                    guiComponent.getGui().drawTexturedModalRect(
                            poseStack, mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                            mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                            guiComponent.getType().getX1(),
                            guiComponent.getType().getY1(),
                            chargeLevel + 1,
                            guiComponent.getType().getHeight()
                    );
                }
            }
        }
    }

    public String getText(final GuiComponent guiComponent) {
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
            text = ModUtils.getString(component.getProcess().getProgress(guiComponent.getIndex()) * 100) + "%";
        } else if (this.component instanceof ComponentSteamProcessRender) {
            ComponentSteamProcessRender component = (ComponentSteamProcessRender) this.component;
            text = ModUtils.getString(component.getProcess().getProgress(guiComponent.getIndex()) * 100) + "%";
        } else if (this.component instanceof ComponentBioProcessRender) {
            ComponentBioProcessRender component = (ComponentBioProcessRender) this.component;
            text = ModUtils.getString(component.getProcess().getProgress(guiComponent.getIndex()) * 100) + "%";
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
                if (component.need) {
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
