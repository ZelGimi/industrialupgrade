package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuRedstoneGenerator;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenRedstoneGenerator<T extends ContainerMenuRedstoneGenerator> extends ScreenMain<ContainerMenuRedstoneGenerator> {

    private static final ResourceLocation background;

    static {
        background = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiRedstoneGenerator.png".toLowerCase());
    }

    public ContainerMenuRedstoneGenerator container;
    public String name;

    public ScreenRedstoneGenerator(ContainerMenuRedstoneGenerator container1) {
        super(container1, container1.base.getStyle());
        this.container = container1;
        this.name = Localization.translate(this.container.base.getName());
        this.addComponent(new ScreenWidget(this, 66, 36, EnumTypeComponent.FIRE,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1, (short) 1) {
                    @Override
                    public double getBar() {
                        return container.base.gaugeFuelScaled(12) / 12D;
                    }

                })
        ));
        this.addComponent(new ScreenWidget(this, 86, 36, EnumTypeComponent.ENERGY_WEIGHT_1,
                new WidgetDefault<>(this.container.base.energy)
        ));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);


    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
