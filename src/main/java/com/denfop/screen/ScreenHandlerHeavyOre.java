package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuHandlerHeavyOre;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenHandlerHeavyOre<T extends ContainerMenuHandlerHeavyOre> extends ScreenMain<ContainerMenuHandlerHeavyOre> {

    public final ContainerMenuHandlerHeavyOre container;

    public ScreenHandlerHeavyOre(ContainerMenuHandlerHeavyOre container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 37, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 61, 61, EnumTypeComponent.HEAT,
                new WidgetDefault<>(container1.base.heat)
        ));
        this.addComponent(new ScreenWidget(this, 39, 62, EnumTypeComponent.ENERGY, new WidgetDefault<>(this.container.base.energy)));

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2, 2, net.minecraft.network.chat.Component.literal(name), 4210752, false);
    }


    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        this.bindTexture();

        int progress = (int) ((218 - 178) * this.container.base.componentProgress.getBar());

        if (progress > 0) {
            drawTexturedModalRect(poseStack, this.guiLeft() + 62, this.guiTop() + 37, 178, 34, progress + 1, 14);
        }


    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIHandlerHO.png".toLowerCase());
    }

}
