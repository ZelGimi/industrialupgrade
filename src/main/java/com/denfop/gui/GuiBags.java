package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerBags;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.ModUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiBags extends GuiIU<ContainerBags> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png");
    private final String name;
    int index = 0;

    public GuiBags(ContainerBags container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getUnlocalizedName() + ".name");

        this.componentList.clear();

        Item item = container.base.itemStack1.getItem();
        if (item == IUItem.bags) {
            this.xSize = 204;
            this.ySize = 177;
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
                            mc.getSoundHandler()
                                    .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                            new PacketItemStackEvent(0, container.base.player);
                            final NBTTagCompound nbt = ModUtils.nbt(container.base.itemStack1);
                            nbt.setBoolean("white", !nbt.getBoolean("white"));
                        }

                    })
            ));
        } else {
            if (item == IUItem.adv_bags) {
                this.xSize = 204;
                this.ySize = 202;
            } else {
                this.xSize = 204;
                this.ySize = 238;
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
                            mc.getSoundHandler()
                                    .playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                            new PacketItemStackEvent(0, container.base.player);
                            final NBTTagCompound nbt = ModUtils.nbt(container.base.itemStack1);
                            nbt.setBoolean("white", !nbt.getBoolean("white"));
                        }

                    })
            ));
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = mouseX - xMin;
        int y = mouseY - yMin;


    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        Item item = container.base.itemStack1.getItem();
        if (item == IUItem.bags) {
            this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 16, 0);
        } else {
            this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 7, 0);

        }
        NBTTagCompound tagCompound = ModUtils.nbt(this.container.base.itemStack1);
        boolean white = tagCompound.getBoolean("white");
        if (white){
            if (item == IUItem.bags) {
                new Area(this,143,4,24,7).withTooltip(Localization.translate("iu.whitelist")).drawForeground(par1, par2);
            }else{
                new Area(this,176,4,24,7).withTooltip(Localization.translate("iu.whitelist")).drawForeground(par1, par2);
            }
        }else{
            if (item == IUItem.bags) {
                new Area(this,143,4,24,7).withTooltip(Localization.translate("iu.blacklist")).drawForeground(par1, par2);
            }else{
                new Area(this,176,4,24,7).withTooltip(Localization.translate("iu.blacklist")).drawForeground(par1, par2);
            }
        }
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, this.ySize);


        NBTTagCompound tagCompound = ModUtils.nbt(this.container.base.itemStack1);
        boolean white = tagCompound.getBoolean("white");
        Item item = container.base.itemStack1.getItem();
        if (white) {
            if (item == IUItem.bags){
                this.drawTexturedModalRect(this.guiLeft + 155, this.guiTop + 4, 217, 0, 12, 7);
            }else{
                this.drawTexturedModalRect(this.guiLeft + 188, this.guiTop + 4, 217, 0, 12, 7);

            }
        } else {
            if (item == IUItem.bags){
                this.drawTexturedModalRect(this.guiLeft + 143, this.guiTop + 4, 205, 0, 12, 7);
            }else{
                this.drawTexturedModalRect(this.guiLeft + 176, this.guiTop + 4, 205, 0, 12, 7);

            }
        }
    }

    public void drawItemStack(ItemStack stack, int x, int y, String altText) {
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) {
            font = fontRenderer;
        }
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    private boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        return this.isPointInRegion(slotIn.xPos, slotIn.yPos, 16, 16, mouseX, mouseY);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void drawSlot(Slot slotIn) {
        super.drawSlot(slotIn);


    }

    protected ResourceLocation getTexture() {
        Item item = container.base.itemStack1.getItem();
        if (item == IUItem.bags) {
            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guibags.png");
        }
        if (item == IUItem.adv_bags) {
            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiadvbags.png");
        }
        if (item == IUItem.imp_bags) {
            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiimpbags.png");
        }
        return background;
    }

}
