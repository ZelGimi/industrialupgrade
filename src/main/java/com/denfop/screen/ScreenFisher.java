package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuFisher;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ScreenFisher<T extends ContainerMenuFisher> extends ScreenMain<ContainerMenuFisher> {

    public final ContainerMenuFisher container;

    public ScreenFisher(ContainerMenuFisher container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new ScreenWidget(this, 5, 20, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 140 + 1 + 5, 28, EnumTypeComponent.ENERGY_HEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addComponent(new ScreenWidget(this, 41, 45, EnumTypeComponent.PROGRESS4,
                new WidgetDefault<>(new ComponentProgress(this.container.base, 1, (short) 0) {
                    @Override
                    public double getBar() {
                        return container.base.progress / 100D;
                    }
                })
        ));

    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        this.drawForeground(poseStack, par1, par2);
        draw(poseStack,
                this.getName(),
                (int) ((float) (this.imageWidth - this.getStringWidth(this.getName())) / 2),
                6,
                4210752
        );

        handleUpgradeTooltip(par1, par2);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EF";
        String tooltip =
                ModUtils.getString(Math.min(
                        this.container.base.progress,
                        100
                )) + "%";
        new AdvancedTooltipWidget(this, 147, 27, 158, 76)
                .withTooltip(tooltip2)
                .drawForeground(poseStack, par1, par2);
        new AdvancedTooltipWidget(this, 41, 45, 55, 60)
                .withTooltip(tooltip)
                .drawForeground(poseStack, par1, par2);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.fisherinformation"));
            List<String> compatibleUpgrades = ListInformationUtils.fisherinform;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }


    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {

        bindTexture(getTexture());
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
        this.drawBackground(poseStack);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft() + 3, this.guiTop() + 3, 0, 0, 10, 10);
        bindTexture(getTexture());
        int chargeLevel = (int) (48.0F * this.container.base.energy.getEnergy()
                / this.container.base.energy.getCapacity());
        int progress = (15 * this.container.base.progress / 100);
        if (chargeLevel > 0) {
            drawTexturedModalRect(poseStack, this.guiLeft() + 140 + 1 + 5, this.guiTop() + 28 + 48 - chargeLevel, 176,
                    48 - chargeLevel, 48, chargeLevel
            );
        }

        if (progress > 0) {
            drawTexturedModalRect(poseStack, this.guiLeft() + 42, this.guiTop() + 46, 177, 48, progress + 1, 14);
        }


    }

    public String getName() {
        return Localization.translate(this.container.base.getName());
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
