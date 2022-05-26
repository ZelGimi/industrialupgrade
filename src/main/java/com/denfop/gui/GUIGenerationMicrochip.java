package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.invslot.InvSlotProcessableGenerationMicrochip;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.RecipeOutput;
import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GUIGenerationMicrochip extends GuiIC2<ContainerBaseGenerationChipMachine> {

    public final ContainerBaseGenerationChipMachine container;

    public GUIGenerationMicrochip(
            ContainerBaseGenerationChipMachine container1
    ) {
        super(container1);
        this.container = container1;
    }

    @Override
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        new AdvArea(this, 70, 62, 108, 73)
                .withTooltip(Localization.translate("iu.temperature") + ModUtils.getString(this.container.base.getTemperature()) + "/" + ModUtils.getString(
                        this.container.base.getMaxTemperature()) + "°C")
                .drawForeground(par1, par2);
        final RecipeOutput output = this.container.base.inputSlotA.process();
        if (output != null) {
            if (!Recipes.mechanism.hasHeaters(this.container.base) && this.container.base.getTemperature() < output.metadata.getShort(
                    "temperature")) {
                new AdvArea(this, 48, 61, 66, 79)
                        .withTooltip(Localization.translate("iu.needheaters"))
                        .drawForeground(par1, par2);
            }
        }
        handleUpgradeTooltip(par1, par2);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 162 && mouseX <= 172 && mouseY >= 3 && mouseY <= 13) {
            for (Slot slot : this.container.inventorySlots) {
                if (slot instanceof SlotInvSlot) {
                    int xX = slot.xPos;
                    int yY = slot.yPos;
                    SlotInvSlot slotInv = (SlotInvSlot) slot;
                    if (slotInv.invSlot instanceof InvSlotProcessableGenerationMicrochip) {
                        drawString(xX + 13, yY - 2, "" + (slot.getSlotIndex() - 6), ModUtils.convertRGBcolorToInt(14, 14, 14),
                                false
                        );

                    }
                }
            }
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (15.0F * this.container.base.getProgress());
        int progress1 = (int) (10.0F * this.container.base.getProgress());
        int progress2 = (int) (19.0F * this.container.base.getProgress());
        int temperature = 38 * this.container.base.getTemperature() / this.container.base.getMaxTemperature();
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 6, yoffset + 76 - 13 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 27, yoffset + 13, 176, 34, progress + 1, 28);
        }
        if (progress1 > 0) {
            drawTexturedModalRect(xoffset + 60, yoffset + 17, 176, 64, progress1 + 1, 19);
        }
        if (progress2 > 0) {
            drawTexturedModalRect(xoffset + 88, yoffset + 23, 176, 85, progress2 + 1, 7);
        }
       /* this.mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        this.drawTexturedRect(162.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        this.mc.getTextureManager().bindTexture(getTexture());*/

        if (temperature > 0) {
            drawTexturedModalRect(xoffset + 70, yoffset + 62, 176, 20, temperature + 1, 11);
        }
        if (this.container.base.inputSlotA.process() != null) {
            if (!Recipes.mechanism.hasHeaters(this.container.base)) {
                drawTexturedModalRect(xoffset + 37, yoffset + 60, 177, 97, 9, 16);

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                this.zLevel = 100.0F;
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                itemRender.renderItemAndEffectIntoGUI(new ItemStack(IUItem.basemachine2, 1, 5), xoffset + 49,
                        yoffset + 61
                );

                GL11.glDisable(GL11.GL_LIGHTING);
                GlStateManager.enableLighting();
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
    }

    public String getName() {
        return "";
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/GUICirsuit.png");
    }

}
