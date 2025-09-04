package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuObsidianGenerator;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenObsidianGenerator<T extends ContainerMenuObsidianGenerator> extends ScreenMain<ContainerMenuObsidianGenerator> {

    public final ContainerMenuObsidianGenerator container;

    public ScreenObsidianGenerator(ContainerMenuObsidianGenerator container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 5, 30, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addWidget(TankWidget.createNormal(this, 43, 21, container.base.fluidTank1));
        this.addWidget(TankWidget.createNormal(this, 66, 21, container.base.fluidTank2));
        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }


    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());

        int progress = (int) (32 * this.container.base.getProgress());
        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;

        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 88, yoffset + 40, 177, 41, progress, 19);
        }
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2 + 15, 5, net.minecraft.network.chat.Component.literal(name), 4210752, false);

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiObsidianGenerator.png".toLowerCase());
    }

}
