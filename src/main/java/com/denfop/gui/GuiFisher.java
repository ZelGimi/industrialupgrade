package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerFisher;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiFisher<T extends ContainerFisher> extends GuiIU<ContainerFisher> {

    public final ContainerFisher container;

    public GuiFisher(ContainerFisher container1) {
        super(container1);
        this.container = container1;
        this.addComponent(new GuiComponent(this, 5, 20, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 140 + 1 + 5, 28, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addComponent(new GuiComponent(this, 41, 45, EnumTypeComponent.PROGRESS4,
                new Component<>(new ComponentProgress(this.container.base, 1, (short) 0) {
                    @Override
                    public double getBar() {
                        return container.base.progress / 100D;
                    }
                })
        ));

    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        this.drawForeground(poseStack, par1, par2);
        this.font.draw(poseStack,
                this.getName(),
                (float) (this.imageWidth - this.getStringWidth(this.getName())) / 2,
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
        new AdvArea(this, 147, 27, 158, 76)
                .withTooltip(tooltip2)
                .drawForeground(poseStack, par1, par2);
        new AdvArea(this, 41, 45, 55, 60)
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


    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {

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
