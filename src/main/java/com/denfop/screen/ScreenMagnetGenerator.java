package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuMagnetGenerator;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.List;


public class ScreenMagnetGenerator<T extends ContainerMenuMagnetGenerator> extends ScreenMain<ContainerMenuMagnetGenerator> {

    public final ContainerMenuMagnetGenerator container;

    public ScreenMagnetGenerator(ContainerMenuMagnetGenerator container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 5, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 76, 28, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault(new ComponentProgress(this.container.base, 1, (short) 0) {
                    @Override
                    public double getBar() {
                        return container.base.timer
                                / 86400D;
                    }

                }) {
                    @Override
                    public String getText(final ScreenWidget screenWidget) {
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

    protected void drawForegroundLayer(GuiGraphics pose, int par1, int par2) {
        super.drawForegroundLayer(pose, par1, par2);

        String name = Localization.translate(this.container.base.getName());

        int nmPos = (this.imageWidth - this.getStringWidth(name)) / 2;
        draw(pose, name, nmPos, 6, 4210752);
    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);


    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
