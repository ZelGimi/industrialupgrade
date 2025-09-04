package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntitySunnariumPanelMaker;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuDoubleElectricMachine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class ScreenSunnariumPanelMaker<T extends ContainerMenuDoubleElectricMachine> extends ScreenMain<ContainerMenuDoubleElectricMachine> {

    public final ContainerMenuDoubleElectricMachine container;

    public ScreenSunnariumPanelMaker(ContainerMenuDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        componentList.add(new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((BlockEntitySunnariumPanelMaker) this.container.base).input_slot)
                ))
        ));
        componentList.add(new ScreenWidget(this, 26, 60, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new WidgetDefault<>(((BlockEntitySunnariumPanelMaker) this.container.base).sunenergy)
        ));
        componentList.add(new ScreenWidget(this, 7, 65, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(((BlockEntitySunnariumPanelMaker) this.container.base).energy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        bindTexture(getTexture());
        int xoffset = guiLeft;
        int yoffset = guiTop;

        int progress = (int) (14 * this.container.base.getProgress());


        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 77, yoffset + 36, 177, 15, progress + 1, 15);
        }

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/GuiSunnariumPanelMaker.png".toLowerCase());
    }

}
