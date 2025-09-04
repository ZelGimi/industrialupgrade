package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.upgrades.BlockEntityUpgrade;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuSingleFluidAdapter;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

public class ScreenSingleFluidAdapter<T extends ContainerMenuSingleFluidAdapter> extends ScreenMain<ContainerMenuSingleFluidAdapter> {

    public final ContainerMenuSingleFluidAdapter container;

    public ScreenSingleFluidAdapter(ContainerMenuSingleFluidAdapter container1) {
        super(container1);
        this.container = container1;
        this.imageHeight += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.imageWidth += 20;
        this.addComponent(new ScreenWidget(this, 5, 30, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        this.addWidget(TankWidget.createNormal(this, 34, 21, container.base.fluidTank1));
        this.addWidget(TankWidget.createNormal(this, 66 + 75, 21, container.base.fluidTank2));
        this.addComponent(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 112, 45, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
        ));
    }


    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        if (this.container.base instanceof BlockEntityUpgrade) {
            bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        bindTexture(getTexture());


        int xoffset = guiLeft;
        int yoffset = guiTop;


        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2 + 15, 5, name, 4210752, false);

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
