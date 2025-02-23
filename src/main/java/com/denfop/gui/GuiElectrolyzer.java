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
import com.denfop.container.ContainerElectrolyzer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiElectrolyzer extends GuiIU<ContainerElectrolyzer> {

    private static final ResourceLocation background;

    static {
        background = new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIElectolyzer.png");
    }

    public ContainerElectrolyzer container;

    public GuiElectrolyzer(ContainerElectrolyzer container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        inventory = new GuiComponent(this, 7, 119, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.addComponent(new GuiComponent(this, 15, 102, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 122, 102, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addElement(TankGauge.createNormal(this, 12, 44, container.base.getFluidTank(0)));
        this.addElement(TankGauge.createNormal(this, 74, 6, container.base.getFluidTank(1)));
        this.addElement(TankGauge.createNormal(this, 74, 63, container.base.getFluidTank(2)));
        this.addComponent(new GuiComponent(this, 16, 26, EnumTypeComponent.FLUID_PART6,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 35, 85, EnumTypeComponent.FLUID_PART8,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 35, 45, EnumTypeComponent.FLUID_PART3,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 95, 25, EnumTypeComponent.FLUID_PART3,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 95, 85, EnumTypeComponent.FLUID_PART8,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 95, 25, EnumTypeComponent.FLUID_PART8,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 95, 85, EnumTypeComponent.FLUID_PART3,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 106, 32, EnumTypeComponent.FLUID_PART9,
                new Component<>(new ComponentCustomizeSize(3, 54))
        ));
        this.addComponent(new GuiComponent(this, 106, 26, EnumTypeComponent.FLUID_PART9,
                new Component<>(new ComponentCustomizeSize(3, 5))
        ));
        this.addComponent(new GuiComponent(this, 106, 92, EnumTypeComponent.FLUID_PART9,
                new Component<>(new ComponentCustomizeSize(3, 6))
        ));
        this.ySize = 200;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);


    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }


    }

}
