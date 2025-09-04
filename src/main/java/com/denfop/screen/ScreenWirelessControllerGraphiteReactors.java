package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ItemStackWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.blockentity.reactors.graphite.graphite_controller.BlockEntityGraphiteController;
import com.denfop.containermenu.ContainerMenuWirelessControllerGraphiteReactors;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenWirelessControllerGraphiteReactors<T extends ContainerMenuWirelessControllerGraphiteReactors> extends ScreenMain<ContainerMenuWirelessControllerGraphiteReactors> {


    public ScreenWirelessControllerGraphiteReactors(ContainerMenuWirelessControllerGraphiteReactors guiContainer) {
        super(guiContainer);

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;
        for (int index = 0; index < this.container.base.itemStacks.size(); index++) {
            if (x >= 8 + (index) * 18 && x < 26 + (index) * 18 && y >= 25 && y <= 43) {
                if (!this.container.base.itemStacks.get(index).isEmpty()) {
                    final BlockEntityGraphiteController tileMultiBlockBase = this.container.base.graphiteControllers.get(
                            index);

                    if (tileMultiBlockBase != null && tileMultiBlockBase.getMain() != null && tileMultiBlockBase
                            .getMain()
                            .isFull() && !tileMultiBlockBase.isRemoved()) {

                        new PacketUpdateServerTile(this.container.base, index);
                    }
                }

            }
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.wireless_graphite_controller.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                compatibleUpgrades.add(Localization.translate("iu.wireless_graphite_controller.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 120, mouseY, text);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        for (int i = 0; i < this.container.base.itemStacks.size(); i++) {
            if (!this.container.base.invslot.get(0).isEmpty()) {
                ItemStack stack = this.container.base.itemStacks.get(i);
                if (!stack.isEmpty()) {
                    final BlockEntityGraphiteController tileMultiBlockBase = this.container.base.graphiteControllers.get(
                            i);
                    BlockPos pos = tileMultiBlockBase.getPos();
                    new TooltipWidget(this, 8 + i * 18, 25, 18, 18).withTooltip(stack.getDisplayName().getString() + "\n" + "x" +
                            ": " + pos.getX() + " y: " + pos.getY() + " z: " + pos.getZ()).drawForeground(poseStack, par1, par2);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        for (int i = 0; i < this.container.base.itemStacks.size(); i++) {
            if (!this.container.base.invslot.get(0).isEmpty()) {
                ItemStack stack = this.container.base.itemStacks.get(i);
                if (!stack.isEmpty()) {
                    new ItemStackWidget(this, 8 + i * 18, 25, () -> stack).drawBackground(
                            poseStack, this.guiLeft,
                            this.guiTop
                    );
                }
            }
        }
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft + 3, guiTop + 3, 0, 0, 10, 10);
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
