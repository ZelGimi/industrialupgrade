package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWelding extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiWelding(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 104, 58, EnumTypeComponent.HEAT,
                new Component<>(this.container.base.heat)
        ));
        this.addComponent(new GuiComponent(this, 58, 35, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));

    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        new AdvArea(this, 80, 35, 101, 49)
                .withTooltip(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (Math.min(
                        this.container.base.getProgress(),
                        1D
                ) * 100) + "%")
                .drawForeground(mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (24.0F * this.container.base.getProgress());

        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiAlloySmelter.png");
    }


}
