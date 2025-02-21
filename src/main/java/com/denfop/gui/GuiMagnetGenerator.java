package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerMagnetGenerator;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.util.List;


public class GuiMagnetGenerator extends GuiIU<ContainerMagnetGenerator> {

    public final ContainerMagnetGenerator container;

    public GuiMagnetGenerator(ContainerMagnetGenerator container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new GuiComponent(this, 5, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 76, 28, EnumTypeComponent.ENERGY_HEIGHT,
                new Component(new ComponentProgress(this.container.base, 1, (short) 0) {
                    @Override
                    public double getBar() {
                        return container.base.timer
                                / 86400D;
                    }

                }) {
                    @Override
                    public String getText(final GuiComponent guiComponent) {
                        double hours = 0;
                        double minutes = 0;
                        double seconds = 0;
                        final List<Double> time = ModUtils.Time(container.base.timer);
                        if (time.size() > 0) {
                            hours = time.get(0);
                            minutes = time.get(1);
                            seconds = time.get(2);
                        }
                        String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                        String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                        String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                        return Localization.translate("iu.timetoend") + time1 + time2 + time3;
                    }
                }
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

        String name = Localization.translate(this.container.base.getName());

        int nmPos = (this.xSize - this.fontRenderer.getStringWidth(name)) / 2;
        this.fontRenderer.drawString(name, nmPos, 6, 4210752);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int h = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(h, k, 0, 0, this.xSize, this.ySize);
        this.drawBackground();


    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
