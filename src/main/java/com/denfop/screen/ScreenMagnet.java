package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuMagnet;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenMagnet<T extends ContainerMenuMagnet> extends ScreenMain<ContainerMenuMagnet> {

    public final ContainerMenuMagnet container;

    public ScreenMagnet(ContainerMenuMagnet container1) {
        super(container1);
        this.container = container1;
        this.inventory.setY(this.inventory.getY() + 20);
        this.addComponent(new ScreenWidget(this, 7, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(
                this,
                147,
                27,
                EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.imageWidth = 230;
        this.imageHeight = 190;
        this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        this.componentList.add(new ScreenWidget(this, 10, 85, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "x: +1";
                    }

                })
        ));
        this.componentList.add(new ScreenWidget(this, 35, 85, EnumTypeComponent.MINUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "x: -1";
                    }

                })
        ));

        this.componentList.add(new ScreenWidget(this, 60, 85, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 3, "") {
                    @Override
                    public String getText() {
                        return "y: +1";
                    }

                })
        ));
        this.componentList.add(new ScreenWidget(this, 85, 85, EnumTypeComponent.MINUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return "y: -1";
                    }

                })
        ));
        this.componentList.add(new ScreenWidget(this, 110, 85, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 5, "") {
                    @Override
                    public String getText() {
                        return "z: +1";
                    }

                })
        ));
        this.componentList.add(new ScreenWidget(this, 135, 85, EnumTypeComponent.MINUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 4, "") {
                    @Override
                    public String getText() {
                        return "z: -1";
                    }

                })
        ));

        this.addWidget(new AdvancedTooltipWidget(
                this,
                175,
                18,
                175 + 18,
                18 + 9 * 18
        ).withTooltip(Localization.translate("iu.blacklist_tube")));
        this.addWidget(new AdvancedTooltipWidget(
                this,
                202,
                18,
                202 + 18,
                18 + 9 * 18
        ).withTooltip(Localization.translate("iu.whitelist_tube")));

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        draw(poseStack, String.valueOf(this.container.base.x), 29 - getStringWidth(String.valueOf(this.container.base.x)) / 2, 87,
                4210752
        );
        draw(poseStack, String.valueOf(this.container.base.y), 79 - getStringWidth(String.valueOf(this.container.base.y)) / 2, 87,
                4210752
        );
        draw(poseStack, String.valueOf(this.container.base.z), 129 - getStringWidth(String.valueOf(this.container.base.z)) / 2, 87,
                4210752
        );
    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);

    }


    public String getName() {
        return null;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
