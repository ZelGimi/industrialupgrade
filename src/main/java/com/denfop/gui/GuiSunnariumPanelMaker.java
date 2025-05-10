package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.TileSunnariumPanelMaker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;

@OnlyIn(Dist.CLIENT)
public class GuiSunnariumPanelMaker<T extends ContainerDoubleElectricMachine> extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiSunnariumPanelMaker(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        componentList.add(new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(
                        EnumTypeComponentSlot.SLOT,
                        Collections.singletonList(((TileSunnariumPanelMaker) this.container.base).input_slot)
                ))
        ));
        componentList.add(new GuiComponent(this, 26, 60, EnumTypeComponent.SOLARIUM_ENERGY_WEIGHT,
                new Component<>(((TileSunnariumPanelMaker) this.container.base).sunenergy)
        ));
        componentList.add(new GuiComponent(this, 7, 65, EnumTypeComponent.ENERGY,
                new Component<>(((TileSunnariumPanelMaker) this.container.base).energy)
        ));
    }


    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
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
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiSunnariumPanelMaker.png".toLowerCase());
    }

}
