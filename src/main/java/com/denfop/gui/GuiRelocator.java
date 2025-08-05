package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.ScrollDirection;
import com.denfop.container.ContainerBase;
import com.denfop.items.relocator.ItemStackRelocator;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import com.denfop.network.packet.PacketRelocatorTeleportPlayer;
import com.denfop.network.packet.PacketRemoveRelocatorPoint;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiRelocator<T extends ContainerBase<ItemStackRelocator>> extends GuiIU<ContainerBase<ItemStackRelocator>> {


    private int value;

    public GuiRelocator(ContainerBase<ItemStackRelocator> guiContainer) {
        super(guiContainer);
        this.imageWidth = 176;
        this.imageHeight = 98;
        this.componentList.clear();
        if (guiContainer.base.player.level().isClientSide) {
            container.base.points = new ArrayList<>(RelocatorNetwork.instance.getPoints(guiContainer.base.player));
            value = 0;
        }

    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d4, double d3) {
        ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        if (mouseX >= 7 && mouseY >= 18 && mouseX <= 168 && mouseY <= 78) {
            if (scrollDirection == ScrollDirection.up) {
                value--;
                value = Math.max(value, 0);
            } else if (scrollDirection == ScrollDirection.down) {
                value++;
                value = Math.min(value, container.base.points.size() / 4);
            }
        }
        return super.mouseScrolled(d, d2, d4, d3);
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        List<Point> pointList = container.base.points;
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (int index = 4 * value; index < Math.min(4 + 4 * value, pointList.size()); index++) {
            int x1 = 10;
            int y1 = 20 + 15 * (index % 4);
            int x2 = 153;
            int y2 = 20 + 15 * (index % 4);
            if (x >= x2 && x <= x2 + 12 && y >= y2 && y <= y2 + 11) {
                new PacketRelocatorTeleportPlayer(minecraft.player, pointList.get(index));
                ItemStack stack = this.container.base.itemStack1;
                if (ElectricItem.manager.canUse(stack, 1000000)) {
                    minecraft.player.closeContainer();
                }
            }
            if (x >= x1 && x <= x1 + 12 && y >= y1 && y <= y1 + 11) {
                new PacketRemoveRelocatorPoint(minecraft.player, pointList.get(index));
                minecraft.player.closeContainer();
            }
        }
    }


    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        container.base.points = new ArrayList<>(RelocatorNetwork.instance.getPoints(container.base.player));
        List<Point> pointList = container.base.points;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (int i = 4 * value; i < Math.min(4 + 4 * value, pointList.size()); i++) {
            String originalName = pointList.get(i).getName();
            String truncatedName = originalName.length() > 10 ? originalName.substring(0, 10) : originalName;
            Point point = pointList.get(i);
            draw(poseStack, truncatedName, 24, 22 + 15 * (i % 4), ModUtils.convertRGBcolorToInt(255, 255, 255));
            new Area(this, 7, 22 + 20 * (i % 4), 160, 14).withTooltip(point.getPos().toString().substring(8)).drawForeground(
                    poseStack, par1,
                    par2
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        String name = Localization.translate(this.container.base.itemStack1.getDescriptionId());
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }

        PoseStack pose = poseStack.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 8) / scale);


        poseStack.drawString(Minecraft.getInstance().font, name, textX, textY, ModUtils.convertRGBcolorToInt(255, 255, 255), false);
        pose.scale(1 / scale, 1 / scale, 1);

        pose.popPose();
        bindTexture();
        List<Point> pointList = container.base.points;
        for (int i = 4 * value; i < Math.min(4 + 4 * value, pointList.size()); i++) {
            drawTexturedModalRect(poseStack, guiLeft + 8, guiTop + 19 + 15 * (i % 4), 8, 171, 160, 14);

        }
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guirelocator.png");
    }

}
