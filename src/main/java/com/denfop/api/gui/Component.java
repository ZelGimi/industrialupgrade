package com.denfop.api.gui;

import com.denfop.componets.AdvEnergy;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.CoolComponent;
import com.denfop.componets.EXPComponent;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.QEComponent;
import com.denfop.componets.RFComponent;
import com.denfop.componets.SEComponent;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraftforge.fluids.FluidTank;

public class Component<T> {

    private final T component;

    public Component(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }


    public void drawBackground(int mouseX, int mouseY, final GuiComponent guiComponent) {
        if (this.component instanceof AdvEnergy) {
            AdvEnergy component = (AdvEnergy) this.component;
            double fillratio = component.storage / component.capacity;
            if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                fillratio *= guiComponent.getType().getHeight();
                int chargeLevel = (int) fillratio;
                guiComponent.getGui().drawTexturedModalRect(
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
                        guiComponent.getType().getHeight()
                );
            }

        } else if (this.component instanceof RFComponent) {
            RFComponent component = (RFComponent) this.component;
            double fillratio = component.storage / component.capacity;
            if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                fillratio *= guiComponent.getType().getHeight();
                int chargeLevel = (int) fillratio;
                guiComponent.getGui().drawTexturedModalRect(
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
                        guiComponent.getType().getHeight()
                );
            }
        } else if (this.component instanceof QEComponent) {
            QEComponent component = (QEComponent) this.component;
            double fillratio = component.storage / component.capacity;
            if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                fillratio *= guiComponent.getType().getHeight();
                int chargeLevel = (int) fillratio;
                guiComponent.getGui().drawTexturedModalRect(
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
                        guiComponent.getType().getHeight()
                );
            }
        } else if (this.component instanceof SEComponent) {
            SEComponent component = (SEComponent) this.component;
            double fillratio = component.storage / component.capacity;
            if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                fillratio *= guiComponent.getType().getHeight();
                int chargeLevel = (int) fillratio;
                guiComponent.getGui().drawTexturedModalRect(
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
                        guiComponent.getType().getHeight()
                );
            }
        } else if (this.component instanceof EXPComponent) {
            EXPComponent component = (EXPComponent) this.component;
            double fillratio = component.storage / component.capacity;
            if (guiComponent.getType().getRender() == EnumTypeRender.HEIGHT) {
                fillratio *= guiComponent.getType().getHeight();
                int chargeLevel = (int) fillratio;
                guiComponent.getGui().drawTexturedModalRect(
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
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
                        mouseX + guiComponent.getX() + guiComponent.getType().getEndX(),
                        mouseY + guiComponent.getType().getEndY() + guiComponent.getY(),
                        guiComponent.getType().getX1(),
                        guiComponent.getType().getY1(),
                        chargeLevel + 1,
                        guiComponent.getType().getHeight()
                );
            }
        }
    }

    public String getText(final GuiComponent guiComponent) {
        String text = "";

        if (this.component instanceof ComponentButton) {

            ComponentButton component = (ComponentButton) this.component;
            text = component.getText();

        } else if (this.component instanceof AdvEnergy) {

            AdvEnergy component = (AdvEnergy) this.component;
            text =
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity()) + " " +
                            "EU";

        } else if (this.component instanceof RFComponent) {
            RFComponent component = (RFComponent) this.component;
            text =
                    ModUtils.getString(Math.min(
                            component.getEnergy(),
                            component.getCapacity()
                    )) + "/" + ModUtils.getString(component.getCapacity()) + " " +
                            "RF";

        } else if (this.component instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.component;
            text = ModUtils.getString(component.getProcess().getProgress(guiComponent.getIndex()) * 100) + "%";
        } else if (this.component instanceof CoolComponent) {
            CoolComponent component = (CoolComponent) this.component;
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

        } else if (this.component instanceof HeatComponent) {
            HeatComponent component = (HeatComponent) this.component;
            text =
                    ModUtils.getString(component
                            .getEnergy()) + "°C" + "/" + ModUtils.getString(component.getCapacity()) + "°C";
            if (component.need) {
                text += "\n" + Localization.translate("iu.need_heat");
            }

        } else if (this.component instanceof EXPComponent) {
            EXPComponent component = (EXPComponent) this.component;
            text = "EXP: " +
                    ModUtils.getString(component
                            .getEnergy()) + "/" + ModUtils.getString(component.getCapacity());

        } else if (this.component instanceof QEComponent) {
            QEComponent component = (QEComponent) this.component;
            text = "QE: " +
                    ModUtils.getString(component
                            .getEnergy()) + "/" + ModUtils.getString(component.getCapacity());

        } else if (this.component instanceof SEComponent) {
            SEComponent component = (SEComponent) this.component;
            text = "SE: " +
                    ModUtils.getString(component
                            .getEnergy()) + "/" + ModUtils.getString(component.getCapacity());

        } else if (this.component instanceof FluidTank) {
            FluidTank component = (FluidTank) this.component;
            String text1;
            try {
                text1 = component.getFluid().getLocalizedName();
            } catch (Exception e) {
                text1 = "";
            }

            text = "Fluid " + text1 + ": " +
                    ModUtils.getString(component
                            .getFluidAmount()) + "/" + ModUtils.getString(component.getCapacity());

        }


        return text;
    }

}
