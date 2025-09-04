package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.componets.ComponentButton;
import com.denfop.containermenu.ContainerMenuBags;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenBags<T extends ContainerMenuBags> extends ScreenMain<ContainerMenuBags> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png".toLowerCase());
    private final String name;
    int index = 0;

    public ScreenBags(ContainerMenuBags container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getDescriptionId());

        this.componentList.clear();

        Item item = container.base.itemStack1.getItem();
        if (item == IUItem.bags.getItem()) {
            this.imageWidth = 204;
            this.imageHeight = 177;
            this.componentList.add(new ScreenWidget(this, 143, 4, 166 - 142, 10 - 3,
                    new WidgetDefault<>(new ComponentButton(null, 0, "") {
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

                            new PacketItemStackEvent(0, container.base.player);

                            CompoundTag nbt = ModUtils.nbt(container.base.itemStack1);
                            nbt.putBoolean("white", !nbt.getBoolean("white"));
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
            this.componentList.add(new ScreenWidget(this, 176, 4, 166 - 142, 10 - 3,
                    new WidgetDefault<>(new ComponentButton(null, 0, "") {
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

                            new PacketItemStackEvent(0, container.base.player);

                            CompoundTag nbt = ModUtils.nbt(container.base.itemStack1);
                            nbt.putBoolean("white", !nbt.getBoolean("white"));
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
        Item item = container.base.itemStack1.getItem();
        if (item == IUItem.bags.getItem()) {
            poseStack.drawString(Minecraft.getInstance().font, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 16, 0, false);
        } else {
            poseStack.drawString(Minecraft.getInstance().font, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 7, 0, false);

        }
        CompoundTag tagCompound = ModUtils.nbt(this.container.base.itemStack1);
        boolean white = tagCompound.getBoolean("white");
        if (white) {
            if (item == IUItem.bags.getItem()) {
                new TooltipWidget(this, 143, 4, 24, 7).withTooltip(Localization.translate("iu.whitelist")).drawForeground(poseStack, par1, par2);
            } else {
                new TooltipWidget(this, 176, 4, 24, 7).withTooltip(Localization.translate("iu.whitelist")).drawForeground(poseStack, par1, par2);
            }
        } else {
            if (item == IUItem.bags.getItem()) {
                new TooltipWidget(this, 143, 4, 24, 7).withTooltip(Localization.translate("iu.blacklist")).drawForeground(poseStack, par1, par2);
            } else {
                new TooltipWidget(this, 176, 4, 24, 7).withTooltip(Localization.translate("iu.blacklist")).drawForeground(poseStack, par1, par2);
            }
        }
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, imageWidth, this.imageHeight);


        CompoundTag tagCompound = ModUtils.nbt(this.container.base.itemStack1);
        boolean white = tagCompound.getBoolean("white");
        Item item = container.base.itemStack1.getItem();
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
            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guibags.png");
        }
        if (item == IUItem.adv_bags.getItem()) {
            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiadvbags.png");
        }
        if (item == IUItem.imp_bags.getItem()) {
            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiimpbags.png");
        }
        return background;
    }

}
