package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerHydrogenGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiHydrogenGenerator extends GuiIU<ContainerHydrogenGenerator> {

    public ContainerHydrogenGenerator container;
    public String name;

    public GuiHydrogenGenerator(ContainerHydrogenGenerator container1) {
        super(container1);
        this.container = container1;
        this.name = Localization.translate((container.base).getName());
        this.addElement(TankGauge.createNormal(this, 70, 20, (container.base).fluidTank));
        this.addComponent(new GuiComponent(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 103, 36, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 32, 40, EnumTypeComponent.FLUID_PART2,
                new Component<>(new ComponentEmpty())
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base instanceof IUpgradableBlock) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }


    }


}
