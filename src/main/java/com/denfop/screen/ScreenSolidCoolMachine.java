package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntitySolidCooling;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuSolidCoolMachine;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenSolidCoolMachine<T extends ContainerMenuSolidCoolMachine> extends ScreenMain<ContainerMenuSolidCoolMachine> {

    public ContainerMenuSolidCoolMachine container;
    public String name;

    public ScreenSolidCoolMachine(ContainerMenuSolidCoolMachine guiContainer) {
        super(guiContainer);
        this.container = guiContainer;
        this.name = Localization.translate(guiContainer.base.getName());
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));


        this.componentList.add(new ScreenWidget(this, 51, 41, EnumTypeComponent.COOL_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.cold)
        ));
        this.componentList.add(new ScreenWidget(this, 20, 37, EnumTypeComponent.WORK_BUTTON,
                new WidgetDefault<>(new ComponentButton(this.container.base, 2, "") {
                    @Override
                    public String getText() {
                        return ((BlockEntitySolidCooling) this.getEntityBlock()).work ? Localization.translate("turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((BlockEntitySolidCooling) this.getEntityBlock()).work;
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


    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        if (this.container.base.time != 0) {
            final List<Double> time = ModUtils.Time(this.container.base.time);
            final String timer =
                    Localization.translate("iu.timetoend") + time.get(1) + Localization.translate("iu.minutes") + time.get(2) + Localization.translate(
                            "iu.seconds");
            this.font.draw(poseStack, timer, 20, 70, 4210752);

        }
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int xOffset = guiLeft;
        int yOffset = guiTop;
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, xOffset + 3, yOffset + 3, 0, 0, 10, 10);
        bindTexture(getTexture());

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");


    }


}
