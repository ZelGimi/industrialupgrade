package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiGenerationMicrochip extends GuiIU<ContainerBaseGenerationChipMachine> {

    public final ContainerBaseGenerationChipMachine container;

    public GuiGenerationMicrochip(
            ContainerBaseGenerationChipMachine container1
    ) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 7, 48, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 7, 62, EnumTypeComponent.ENERGY, new Component<>(this.container.base.energy)));
        this.addComponent(new GuiComponent(this, 70, 62, EnumTypeComponent.HEAT,
                new Component<>(container1.base.heat)
        ));
    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

    }


    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 0, name, 4210752, false);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (15.0F * this.container.base.getProgress());
        int progress1 = (int) (10.0F * this.container.base.getProgress());
        int progress2 = (int) (19.0F * this.container.base.getProgress());

        if (progress > 0) {
            drawTexturedModalRect(xoffset + 27, yoffset + 13, 176, 34, progress + 1, 28);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(xoffset + 60, yoffset + 17, 176, 64, progress1 + 1, 19);
        }
        if (progress2 > 0) {
            drawTexturedModalRect(xoffset + 88, yoffset + 23, 176, 85, progress2 + 1, 7);
        }


    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUICirsuit.png");
    }

}
