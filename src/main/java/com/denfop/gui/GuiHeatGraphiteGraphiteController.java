package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerHeatGraphiteController;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiHeatGraphiteGraphiteController<T extends ContainerHeatGraphiteController> extends GuiIU<ContainerHeatGraphiteController> {

    public GuiHeatGraphiteGraphiteController(ContainerHeatGraphiteController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.imageWidth = 186;
        this.imageHeight = 211;
        this.addComponent(new GuiComponent(this, 83, 70 - 26, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 0) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 83, 70 + 20, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 1) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));

        this.addComponent(new GuiComponent(this, 60, 67, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 2) {
                    @Override
                    public String getText() {
                        return "+1";
                    }
                })
        ));
        this.addComponent(new GuiComponent(this, 106, 67, 22, 22,
                new Component<>(new ComponentButton(this.container.base, 3) {
                    @Override
                    public String getText() {
                        return "-1";
                    }
                })
        ));

    }



    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, Localization.translate("reactor.level_graphite") + this.container.base.getLevelGraphite(),
                23 - 6
                , 10,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
        draw(poseStack, Localization.translate("reactor.level_fuel_graphite") + ModUtils.getString(this.container.base.getFuelGraphite()),
                23 - 6
                , 20,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
        draw(poseStack, Localization.translate("reactor.index_graphite") + this.container.base.getIndex(),
                23 - 6, 30,
                ModUtils.convertRGBcolorToInt(15,
                        125, 205
                )
        );
    }



    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        this.drawTexturedModalRect(poseStack,this.guiLeft + 83, this.guiTop + 67, 188, 3, 23, 23);

        this.drawTexturedModalRect(poseStack,this.guiLeft + 83, this.guiTop + 70 - 26, 188, 43, 22, 22);
        this.drawTexturedModalRect(poseStack,this.guiLeft + 83, this.guiTop + 70 + 20, 211, 43, 22, 22);


        this.drawTexturedModalRect(poseStack,this.guiLeft + 60, this.guiTop + 67, 188, 43, 22, 22);
        this.drawTexturedModalRect(poseStack,this.guiLeft + 106, this.guiTop + 67, 211, 43, 22, 22);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiheat4.png");
    }

}
