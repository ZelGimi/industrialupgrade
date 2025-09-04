package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.cooling.BlockEntityCooling;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuCoolMachine;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenCoolMachine<T extends ContainerMenuCoolMachine> extends ScreenMain<ContainerMenuCoolMachine> {

    public ContainerMenuCoolMachine container;
    public String name;

    public ScreenCoolMachine(ContainerMenuCoolMachine guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.componentList.add(new ScreenWidget(this, 113, 21, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));

        this.componentList.add(new ScreenWidget(this, 51, 41, EnumTypeComponent.COOL_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.cold)
        ));
        this.componentList.add(new ScreenWidget(this, 20, 37, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntityCooling) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntityCooling) this.getEntityBlock()).work;
                    }
                })
        ));
        this.componentList.add(new ScreenWidget(this, 53, 60, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return "+4";
                    }

                })
        ));
        this.componentList.add(new ScreenWidget(this, 78, 60, EnumTypeComponent.MINUS_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 1, "") {
                    @Override
                    public String getText() {
                        return "-4";
                    }

                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.cool_storage.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.cooling;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }


    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        draw(poseStack, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2, 6, 4210752);


    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int xOffset = (this.width - this.imageWidth) / 2;
        int yOffset = (this.height - this.imageHeight) / 2;
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, xOffset + 3, yOffset + 3, 0, 0, 10, 10);
        bindTexture(getTexture());


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }


}
