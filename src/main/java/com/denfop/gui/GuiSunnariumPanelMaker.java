package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.tiles.mechanism.TileSunnariumPanelMaker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiSunnariumPanelMaker extends GuiIU<ContainerDoubleElectricMachine> {

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

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        int progress = (int) (14 * this.container.base.getProgress());


        if (progress > 0) {
            drawTexturedModalRect(xoffset + 77, yoffset + 36, 177, 15, progress + 1, 15);
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiSunnariumPanelMaker.png");
    }

}
