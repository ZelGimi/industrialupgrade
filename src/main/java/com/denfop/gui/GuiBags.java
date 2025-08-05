package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerBags;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.packet.PacketItemStackEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiBags<T extends ContainerBags> extends GuiIU<ContainerBags> {

    private static final ResourceLocation background = ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/GUIBags.png".toLowerCase());
    private final String name;
    int index = 0;
    private Boolean white;

    public GuiBags(ContainerBags container, final ItemStack itemStack1) {
        super(container);
        this.white = container.base.containerStack.getOrDefault(DataComponentsInit.BLACK_LIST, false);
        this.name = Localization.translate(itemStack1.getDescriptionId());

        this.componentList.clear();

        Item item = container.base.containerStack.getItem();
        if (item == IUItem.bags.getItem()) {
            this.imageWidth = 204;
            this.imageHeight = 177;
            this.componentList.add(new GuiComponent(this, 143, 4, 166 - 142, 10 - 3,
                    new Component<>(new ComponentButton(null, 0, "") {
                        @Override
                        public String getText() {
                            return "";
                        }

                        @Override
                        public boolean active() {
                            return false;
                        }

                        @Override
                        public void ClickEvent() {
                            Minecraft.getInstance().getSoundManager().play(
                                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                            );
                            white = !white;
                            new PacketItemStackEvent(0, container.base.player);
                            container.base.containerStack.set(DataComponentsInit.BLACK_LIST, white);

                        }

                    })
            ));
        } else {
            if (item == IUItem.adv_bags.getItem()) {
                this.imageWidth = 204;
                this.imageHeight = 202;
            } else {
                this.imageWidth = 204;
                this.imageHeight = 238;
            }
            this.componentList.add(new GuiComponent(this, 176, 4, 166 - 142, 10 - 3,
                    new Component<>(new ComponentButton(null, 0, "") {
                        @Override
                        public String getText() {
                            return "";
                        }

                        @Override
                        public boolean active() {
                            return false;
                        }

                        @Override
                        public void ClickEvent() {
                            Minecraft.getInstance().getSoundManager().play(
                                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                            );
                            white = !white;
                            new PacketItemStackEvent(0, container.base.player);
                            container.base.containerStack.set(DataComponentsInit.BLACK_LIST, white);

                        }

                    })
            ));
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = mouseX - xMin;
        int y = mouseY - yMin;


    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        Item item = container.base.containerStack.getItem();
        if (item == IUItem.bags.getItem()) {
            poseStack.drawString(Minecraft.getInstance().font, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 16, 0, false);
        } else {
            poseStack.drawString(Minecraft.getInstance().font, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 7, 0, false);

        }

        if (white) {
            if (item == IUItem.bags.getItem()) {
                new Area(this, 143, 4, 24, 7).withTooltip(Localization.translate("iu.whitelist")).drawForeground(poseStack, par1, par2);
            } else {
                new Area(this, 176, 4, 24, 7).withTooltip(Localization.translate("iu.whitelist")).drawForeground(poseStack, par1, par2);
            }
        } else {
            if (item == IUItem.bags.getItem()) {
                new Area(this, 143, 4, 24, 7).withTooltip(Localization.translate("iu.blacklist")).drawForeground(poseStack, par1, par2);
            } else {
                new Area(this, 176, 4, 24, 7).withTooltip(Localization.translate("iu.blacklist")).drawForeground(poseStack, par1, par2);
            }
        }
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, imageWidth, this.imageHeight);


        Item item = container.base.containerStack.getItem();
        if (white) {
            if (item == IUItem.bags.getItem()) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 155, this.guiTop + 4, 217, 0, 12, 7);
            } else {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 188, this.guiTop + 4, 217, 0, 12, 7);

            }
        } else {
            if (item == IUItem.bags.getItem()) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 143, this.guiTop + 4, 205, 0, 12, 7);
            } else {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 176, this.guiTop + 4, 205, 0, 12, 7);

            }
        }
    }


    protected ResourceLocation getTexture() {
        Item item = container.base.itemStack1.getItem();
        if (item == IUItem.bags.getItem()) {
            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guibags.png");
        }
        if (item == IUItem.adv_bags.getItem()) {
            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiadvbags.png");
        }
        if (item == IUItem.imp_bags.getItem()) {
            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiimpbags.png");
        }
        return background;
    }

}
