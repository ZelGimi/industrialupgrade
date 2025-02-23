package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerObsidianGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiObsidianGenerator extends GuiIU<ContainerObsidianGenerator> {

    public final ContainerObsidianGenerator container;

    public GuiObsidianGenerator(ContainerObsidianGenerator container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 5, 30, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addElement(TankGauge.createNormal(this, 43, 21, container.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 66, 21, container.base.fluidTank2));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());

        int progress = (int) (32 * this.container.base.getProgress());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;

        if (progress > 0) {
            drawTexturedModalRect(xoffset + 88, yoffset + 40, 177, 41, progress, 19);
        }
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 15, 5, name, 4210752, false);

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GuiObsidianGenerator.png");
    }

}
