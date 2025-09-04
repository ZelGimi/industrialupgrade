package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.item.upgrade.UpgradeModificator;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuAntiUpgrade;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenAntiUpgradeBlock<T extends ContainerMenuAntiUpgrade> extends ScreenMain<ContainerMenuAntiUpgrade> {

    public final ContainerMenuAntiUpgrade container;

    public ScreenAntiUpgradeBlock(ContainerMenuAntiUpgrade container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new ScreenWidget(this, 3, 14, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addWidget(new ButtonWidget(this, 8, 30, 50, 20, container1.base, 0, Localization.translate("button.need_mod")));

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.anti_modification.info"));
            List<String> compatibleUpgrades = ListInformationUtils.anti_upgrade_block;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (int m = 0; m < 4; m++) {
            if (x >= 70 && x <= 87 && y >= 10 + 18 * m && y < 27 + 18 * m) {
                new PacketUpdateServerTile(this.container.base, m + 1);
            }
        }
        final List<UpgradeModificator> list1 = UpgradeSystem.system.getListModifications(this.container.base.input.get(0));
        if (x >= 149 && x <= 167 && y >= 10 && y < 27 && !list1.isEmpty()) {
            new PacketUpdateServerTile(this.container.base, 5);
        }
        if (x >= 149 && x <= 167 && y >= 28 && y < 45 && list1.size() > 1) {
            new PacketUpdateServerTile(this.container.base, 6);
        }
    }


    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiantiupgrade.png");
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        String tooltip2 =
                ModUtils.getString(Math.min(
                        this.container.base.energy.getEnergy(),
                        this.container.base.energy.getCapacity()
                )) + "/" + ModUtils.getString(this.container.base.energy.getCapacity()) + " " +
                        "EF";
        new AdvancedTooltipWidget(this, 26, 56, 37, 71)
                .withTooltip(tooltip2)
                .drawForeground(poseStack, mouseX, mouseY);
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, this.imageWidth / 2, 1, net.minecraft.network.chat.Component.nullToEmpty(name), 4210752, false);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;
        bindTexture(getTexture());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        final int progress = Math.min(31 * this.container.base.progress / 100, 31);
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, xoffset + 3, yoffset + 3, 0, 0, 10, 10);
        bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        if (chargeLevel > 0) {
            drawTexturedModalRect(poseStack, xoffset + 25, yoffset + 57 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );

        }
        if (progress > 0) {
            drawTexturedModalRect(poseStack,
                    xoffset + 136,
                    yoffset + 58 - progress,
                    176,
                    50 - progress + 31,
                    5,
                    progress
            );
        }
        if (!this.container.base.input.isEmpty()) {
            final List<ItemStack> list = UpgradeSystem.system.getListStack(this.container.base.input.get(0));
            final List<UpgradeModificator> list1 = UpgradeSystem.system.getListModifications(this.container.base.input.get(0));
            for (int i = 0; i < list1.size(); i++) {
                drawTexturedModalRect(poseStack, xoffset + 149, yoffset + 10 + 18 * i, 200,
                        88, 18, 18
                );
            }
            int i = 0;
            RenderSystem.setShaderColor(1F, 1, 1F, 1);
            if (this.container.base.index <= 3) {
                drawTexturedModalRect(poseStack, xoffset + 70, yoffset + 8 + 18 * this.container.base.index, 200,
                        11 + 18 * this.container.base.index, 18, 18
                );
            } else {
                drawTexturedModalRect(poseStack, xoffset + 149, yoffset + 10 + 18 * (this.container.base.index - 4), 200,
                        10 + 18, 18, 18
                );
            }
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                new ItemStackWidget(this, 71, 9 + i * 18, () -> stack).drawBackground(poseStack, guiLeft, guiTop);
                i++;

            }
        }
    }

}
