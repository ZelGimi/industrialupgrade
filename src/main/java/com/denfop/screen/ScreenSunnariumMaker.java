package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuSunnariumMaker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class ScreenSunnariumMaker<T extends ContainerMenuSunnariumMaker> extends ScreenMain<ContainerMenuSunnariumMaker> {

    public final ContainerMenuSunnariumMaker container;

    public ScreenSunnariumMaker(ContainerMenuSunnariumMaker container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(this.container.base.input_slot)
                ))
        ));
        componentList.add(new ScreenWidget(this, 36, 63, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new WidgetDefault<>((this.container.base).sunenergy)
        ));
        componentList.add(new ScreenWidget(this, 7, 65, EnumTypeComponent.ENERGY,
                new WidgetDefault<>((this.container.base).energy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());
        int progress = (int) (17 * this.container.base.componentProgress.getBar());
        int xoffset = guiLeft;
        int yoffset = guiTop;

        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 49, yoffset + 24, 177, 20, progress + 1, 32);
        }
        progress = (int) (12 * this.container.base.componentProgress.getBar());
        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 85, yoffset + 24, 177, 56, progress + 1, 33);
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiSunnariumMaker.png".toLowerCase());
    }

}
