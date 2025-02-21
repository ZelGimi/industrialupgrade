package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentCustomizeSize;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerAirCollector;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiAirCollector extends GuiIU<ContainerAirCollector> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guiaircollector.png");
    }

    public ContainerAirCollector container;

    public GuiAirCollector(ContainerAirCollector container1) {
        super(container1);
        this.container = container1;
        this.ySize = 200;
        componentList.clear();
        inventory = new GuiComponent(this, 7, 119, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);

        this.addComponent(new GuiComponent(this, 153, 21, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));

        this.addComponent(new GuiComponent(this, 16, 56, EnumTypeComponent.FLUID_PART4,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 28, 67, EnumTypeComponent.FLUID_PART7,
                new Component<>(new ComponentCustomizeSize(7, 3))
        ));

        this.addComponent(new GuiComponent(this, 60, 56, EnumTypeComponent.FLUID_PART4,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 72, 67, EnumTypeComponent.FLUID_PART7,
                new Component<>(new ComponentCustomizeSize(7, 3))
        ));

        this.addComponent(new GuiComponent(this, 108, 56, EnumTypeComponent.FLUID_PART4,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 120, 67, EnumTypeComponent.FLUID_PART7,
                new Component<>(new ComponentCustomizeSize(7, 3))
        ));

        this.addComponent(new GuiComponent(this, 23, 79, EnumTypeComponent.FLUID_PART7,
                new Component<>(new ComponentCustomizeSize(43, 3))
        ));
        this.addComponent(new GuiComponent(this, 67, 79, EnumTypeComponent.FLUID_PART7,
                new Component<>(new ComponentCustomizeSize(47, 3))
        ));
        this.addComponent(new GuiComponent(this, 131, 76, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        TankGauge.createNormal(this, 36, 20, container.base.fluidTank[0]).drawForeground(par1, par2);
        TankGauge.createNormal(this, 80, 20, container.base.fluidTank[1]).drawForeground(par1, par2);
        TankGauge.createNormal(this, 128, 20, container.base.fluidTank[2]).drawForeground(par1, par2);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        TankGauge.createNormal(this, 36, 20, container.base.fluidTank[0]).drawBackground(this.guiLeft, guiTop);
        TankGauge.createNormal(this, 80, 20, container.base.fluidTank[1]).drawBackground(this.guiLeft, guiTop);
        TankGauge.createNormal(this, 128, 20, container.base.fluidTank[2]).drawBackground(this.guiLeft, guiTop);

    }

}
