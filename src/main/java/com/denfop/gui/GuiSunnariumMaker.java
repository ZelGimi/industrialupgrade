package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerSunnariumMaker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiSunnariumMaker extends GuiIU<ContainerSunnariumMaker> {

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

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (17 * this.container.base.componentProgress.getBar());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        if (progress > 0) {
            drawTexturedModalRect(xoffset + 49, yoffset + 24, 177, 20, progress + 1, 32);
        }
        progress = (int) (12 * this.container.base.componentProgress.getBar());
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 85, yoffset + 24, 177, 56, progress + 1, 33);
        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiSunnariumMaker.png");
    }

}
