package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerReverseTransriptor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiReverseTransriptor extends GuiIU<ContainerReverseTransriptor> {

    public final ContainerReverseTransriptor container;

    public GuiReverseTransriptor(ContainerReverseTransriptor container1) {
        super(container1);
        this.container = container1;
        this.ySize += 40;
        this.inventory.setY(this.inventory.getY() + 40);
        this.addComponent(new GuiComponent(this, 5, 30, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));

        this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        this.addElement(TankGauge.createNormal(this, 43, 21, container.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 120, 21, container.base.fluidTank2));
        this.addComponent(new GuiComponent(this, 7, 64, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.componentList.add(new GuiComponent(this, 70, 80, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.qe)
        ));
        this.componentList.add(new GuiComponent(this, 70, 100, EnumTypeComponent.RAD,
                new Component<>(this.container.base.rad)
        ));
        this.addComponent(new GuiComponent(this, 82, 40, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container.base, 1,
                        (short) 0
                ) {
                    @Override
                    public double getBar() {
                        return container.base.getProgress();
                    }
                })
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
        if (this.container.base instanceof IUpgradableBlock) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
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


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    }

}
