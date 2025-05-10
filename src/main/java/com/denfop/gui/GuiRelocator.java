package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
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
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiRelocator<T extends ContainerBase<ItemStackRelocator>> extends GuiIU<ContainerBase<ItemStackRelocator>> {


    private int value;

    public GuiRelocator(ContainerBase<ItemStackRelocator> guiContainer) {
        super(guiContainer);
        this.imageWidth = 138;
        this.imageHeight = 220;
        this.componentList.clear();
        if (guiContainer.base.player.getLevel().isClientSide) {
            container.base.points = new ArrayList<>(RelocatorNetwork.instance.getPoints(guiContainer.base.player));
            value = 0;
        }
        this.addElement(new ImageInterface(this, 0, 0, this.imageWidth, imageHeight));
        this.componentList.add(new GuiComponent(this, 117, 10, EnumTypeComponent.SCROLL_UP,
                new Component<>(new ComponentButton(null, 0, "") {
                    @Override
                    public void ClickEvent() {
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );
                        value--;
                        value = Math.max(value, 0);
                    }
                })));
        this.componentList.add(new GuiComponent(this, 117, 190, EnumTypeComponent.SCROLL_DOWN,
                new Component<>(new ComponentButton(null, 0, "") {
                    @Override
                    public void ClickEvent() {
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );
                        value++;
                        value = Math.min(value, container.base.points.size() / 10);

                    }
                })));
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        List<Point> pointList = container.base.points;
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (int index = 10 * value; index < Math.min(10 + 10 * value, pointList.size()); index++) {
            int x1 = 78;
            int y1 = 10 + 20 * (index % 10);
            int x2 = EnumTypeComponent.ACCEPT.getWeight() + x1;
            int y2 = EnumTypeComponent.ACCEPT.getHeight() + y1;
            if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                new PacketRelocatorTeleportPlayer(minecraft.player, pointList.get(index));
                ItemStack stack = this.container.base.itemStack1;
                if (ElectricItem.manager.canUse(stack, 1000000)) {
                    minecraft.player.closeContainer();
                }
            }
            if (x >= x1 + 20 && x <= x2 + 20 && y >= y1 && y <= y2) {
                new PacketRemoveRelocatorPoint(minecraft.player, pointList.get(index));
                minecraft.player.closeContainer();
            }
        }
    }


    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        List<Point> pointList = container.base.points;
        RenderSystem.setShaderColor(1, 1, 1, 1);
        for (int i = 10 * value; i < Math.min(10 + 10 * value, pointList.size()); i++) {
            String originalName = pointList.get(i).getName();
            String truncatedName = originalName.length() > 10 ? originalName.substring(0, 10) : originalName;
            Point point = pointList.get(i);
            font.draw(poseStack, truncatedName, 10, 14 + 20 * (i % 10), ModUtils.convertRGBcolorToInt(255, 255, 255));
            new Area(this, 5, 10 + 20 * (i % 10), 70, 14).withTooltip(point.getPos().toString().substring(8)).drawForeground(
                    poseStack, par1,
                    par2
            );

        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        List<Point> pointList = container.base.points;
        for (int i = 10 * value; i < Math.min(10 + 10 * value, pointList.size()); i++) {
            new ImageScreen(this, 5, 10 + 20 * (i % 10), 70, 14).drawBackground(poseStack, this.guiLeft, this.guiTop);
            new GuiComponent(this, 78, 10 + 20 * (i % 10), EnumTypeComponent.ACCEPT,
                    new Component<>(new ComponentEmpty())
            ).drawBackground(poseStack, this.guiLeft, this.guiTop);
            new GuiComponent(this, 78 + 20, 10 + 20 * (i % 10), EnumTypeComponent.CANCEL,
                    new Component<>(new ComponentEmpty())
            ).drawBackground(poseStack, this.guiLeft, this.guiTop);
        }
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
