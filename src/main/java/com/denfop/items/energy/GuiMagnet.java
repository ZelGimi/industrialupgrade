package com.denfop.items.energy;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.gui.GuiIU;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiMagnet<T extends ContainerMagnet> extends GuiIU<ContainerMagnet> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png".toLowerCase());
    private final String name;
    int index = 0;

    public GuiMagnet(ContainerMagnet container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getDescriptionId() + ".name");
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );

        componentList.add(slots);
        this.addElement(new ImageInterface(this, 0, 0, 176, imageHeight));
        this.addElement(new CustomButton(this, 10, 10, 18, 18, null, 2, "") {
            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                    );

                    new PacketItemStackEvent(0, container.base.player);

                    CompoundTag nbt = ModUtils.nbt(container.base.itemStack1);
                    nbt.putBoolean("white", !nbt.getBoolean("white"));
                }
                return true;
            }
        });
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
        draw(poseStack, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 11, 0);
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.getGuiLeft(), this.getGuiTop(), 0, 0, 176, this.imageHeight);
        int slots = this.container.inventorySize;
        slots = slots / 9;

        int col;
        for (col = 0; col < slots; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.drawTexturedModalRect(poseStack, this.guiLeft + 7 + col1 * 18, this.getGuiTop() + 23 + col * 18, 176, 0, 18, 18);
            }
        }
        CompoundTag tagCompound = ModUtils.nbt(this.container.base.itemStack1);
        boolean white = tagCompound.getBoolean("white");
        if (white) {
            RenderSystem.enableBlend();
            this.drawItemStack(poseStack, 11, 11, new ItemStack(IUItem.module9.getStack(13), 1));
            RenderSystem.disableBlend();
        } else {
            RenderSystem.enableBlend();
            this.drawItemStack(poseStack, 11, 11, new ItemStack(IUItem.module9.getStack(12), 1));
            RenderSystem.disableBlend();
        }
    }


    protected ResourceLocation getTexture() {
        return background;
    }

}
