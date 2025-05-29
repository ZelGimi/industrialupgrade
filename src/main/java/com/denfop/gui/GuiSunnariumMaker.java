package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerSunnariumMaker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class GuiSunnariumMaker<T extends ContainerSunnariumMaker> extends GuiIU<ContainerSunnariumMaker> {

    public final ContainerSunnariumMaker container;

    public GuiSunnariumMaker(ContainerSunnariumMaker container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(this.container.base.input_slot)
                ))
        ));
        componentList.add(new GuiComponent(this, 36, 63, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new Component<>((this.container.base).sunenergy)
        ));
        componentList.add(new GuiComponent(this, 7, 65, EnumTypeComponent.ENERGY,
                new Component<>((this.container.base).energy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
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
