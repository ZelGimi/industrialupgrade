package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.ITemperature;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.RecipeOutput;
import ic2.core.GuiIC2;
import ic2.core.init.Localization;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class GUIAdvAlloySmelter extends GuiIC2<ContainerTripleElectricMachine> {

    public final ContainerTripleElectricMachine container;

    public GUIAdvAlloySmelter(ContainerTripleElectricMachine container1) {
        super(container1);
        this.container = container1;
    }
    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        final RecipeOutput output = this.container.base.inputSlotA.process();
        if (output != null) {
            if (!Recipes.mechanism.hasHeaters(this.container.base.getWorld(),this.container.base.getPos()) && ((ITemperature) this.container.base).getTemperature() < output.metadata.getShort(
                    "temperature")) {
                new AdvArea(this, 81, 58, 99, 76)
                        .withTooltip(Localization.translate("iu.needheaters"))
                        .drawForeground(mouseX, mouseY);
            }
        }
        new AdvArea(this, 104, 58, 142, 69)
                .withTooltip(Localization.translate("iu.temperature") + ModUtils.getString( ((ITemperature) this.container.base).getTemperature()) + "/" + ModUtils.getString(
                        ((ITemperature) this.container.base).getMaxTemperature()) + "°C")
                .drawForeground(mouseX, mouseY);
    }
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(getTexture());
        int chargeLevel = (int) (14.0F * this.container.base.getChargeLevel());
        int progress = (int) (24.0F * this.container.base.getProgress());
        if (chargeLevel > 0) {
            drawTexturedModalRect(xoffset + 56 + 1, yoffset + 36 + 14 - chargeLevel, 176, 14 - chargeLevel,
                    14, chargeLevel
            );
        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 79, yoffset + 34, 176, 14, progress + 1, 16);
        }
        int temperature = 38 *((ITemperature) this.container.base).getTemperature() / ((ITemperature) this.container.base).getMaxTemperature();
        if (temperature > 0) {
            drawTexturedModalRect(this.guiLeft + 105, this.guiTop + 59, 176, 34, temperature + 1, 11);
        }
        final RecipeOutput output = this.container.base.inputSlotA.process();

        if (output != null) {
            if (!Recipes.mechanism.hasHeaters(this.container.base.getWorld(),this.container.base.getPos())&& ((ITemperature) this.container.base).getTemperature() < output.metadata.getShort(
                    "temperature")) {
                drawTexturedModalRect(this.guiLeft + 75, this.guiTop + 57, 177, 52, 9, 16);

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glPushMatrix();
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                this.zLevel = 100.0F;
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                itemRender.renderItemAndEffectIntoGUI(new ItemStack(IUItem.basemachine2, 1, 5), this.guiLeft + 86,
                        this.guiTop + 57
                );

                GL11.glDisable(GL11.GL_LIGHTING);
                GlStateManager.enableLighting();
                RenderHelper.enableStandardItemLighting();
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIAdvAlloySmelter.png");
    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

}
