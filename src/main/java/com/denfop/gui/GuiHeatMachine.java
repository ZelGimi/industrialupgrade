package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerHeatMachine;
import com.denfop.tiles.base.TileBaseHeatMachine;
import com.denfop.utils.ListInformationUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiHeatMachine<T extends ContainerHeatMachine> extends GuiIU<ContainerHeatMachine> {

    public ContainerHeatMachine container;
    public String name;

    public GuiHeatMachine(ContainerHeatMachine guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());

        if (this.container.base.hasFluid) {
            this.componentList.add(new GuiComponent(this, 117, 41, EnumTypeComponent.FLUID_PART,
                    new Component<>(new ComponentEmpty())
            ));
            this.addElement(TankGauge.createNormal(this, 96, 22, (container.base).fluidTank));
        } else {
            this.componentList.add(new GuiComponent(this, 113, 21, EnumTypeComponent.ENERGY_HEIGHT,
                    new Component<>(this.container.base.energy)
            ));
        }
        this.componentList.add(new GuiComponent(this, 51, 41, EnumTypeComponent.HEAT,
                new Component<>(this.container.base.heat)
        ));
        this.componentList.add(new GuiComponent(this, 20, 37, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return ((TileBaseHeatMachine) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileBaseHeatMachine) this.getEntityBlock()).work;
                    }
                })
        ));
        this.componentList.add(new GuiComponent(this, 53, 60, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "+1000";
                    }

                })
        ));
        this.componentList.add(new GuiComponent(this, 78, 60, EnumTypeComponent.MINUS_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "-1000";
                    }

                })
        ));
    }



    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.heat_storage.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.heating;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer( poseStack,par1, par2);
        handleUpgradeTooltip(par1, par2);


    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer( poseStack,f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect( poseStack,this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
          bindTexture(getTexture());





    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");

    }


}
