package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.container.ContainerWindGenerator;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWindGenerator extends GuiIU<ContainerWindGenerator> {

    private final ResourceLocation background;

    public GuiWindGenerator(ContainerWindGenerator guiContainer) {
        super(guiContainer, guiContainer.base.getStyle());
        this.ySize = 236;
        this.xSize = 211;
        this.background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guiwindgenerator.png");
        this.addComponent(new GuiComponent(this, 167, 98, EnumTypeComponent.ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.inventory.setY(153);
        this.inventory.setX(7);
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.wind_generator.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.wind_generator;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 12 && x <= 25 && y >= 45 && y <= 68) {
            new PacketUpdateServerTile(this.container.base, 0);
        }


    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);
        this.mc.getTextureManager().bindTexture(getTexture());
        if (!this.container.base.slot.isEmpty()) {
            final List<ItemStack> list = RotorUpgradeSystem.instance.getListStack(this.container.base.slot.get());
            int i = 0;
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                RenderHelper.enableGUIStandardItemLighting();
                GL11.glPushMatrix();
                GL11.glColor4f(1F, 1, 1F, 1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GlStateManager.disableLighting();
                GlStateManager.enableDepth();
                this.zLevel = 100.0F;
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

                itemRender.renderItemAndEffectIntoGUI(
                        stack,
                        xoffset + 181,
                        yoffset + 19 + i * 18
                );


                GL11.glEnable(GL11.GL_LIGHTING);
                GlStateManager.enableLighting();
                RenderHelper.enableStandardItemLighting();
                GL11.glColor4f(1F, 1, 1F, 1);
                GL11.glPopMatrix();
                i++;

            }

        }

    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        if (this.container.base.getWorld().provider.getDimension() == 0) {
            if (this.container.base.getMinWind() != 0) {
                this.fontRenderer.drawString(Localization.translate("iu.wind_meter.info") + String.format(
                                "%.1f",
                                Math.min(24.7 + this.container.base.mind_speed, WindSystem.windSystem.getSpeedFromPower(
                                                this.container.base.getBlockPos(),
                                                this.container.base,
                                                this.container.base.generation
                                        ) / this.container.base.getCoefficient()
                                )
                        ) + " m/s",
                        63, 48, ModUtils.convertRGBcolorToInt(13, 229, 34)
                );
            } else {
                this.fontRenderer.drawString(Localization.translate("iu.wind_meter.info") + String.format(
                                "%.1f",
                                this.container.base.wind_speed + this.container.base.mind_speed
                        ) + " m/s",
                        63, 48, ModUtils.convertRGBcolorToInt(13, 229, 34)
                );
            }
        }
        String tooltip2 =
                Localization.translate("iu.wind_change_side");
        new AdvArea(this, 12, 45, 25, 58)
                .withTooltip(tooltip2)
                .drawForeground(mouseX, mouseY);

        if (!this.container.base.slot.isEmpty()) {
            final List<ItemStack> list = RotorUpgradeSystem.instance.getListStack(this.container.base.slot.get());
            int i = 0;
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                new AdvArea(this, 180, 18 + i * 18, 197, 35 + i * 18)
                        .withTooltip(stack.getDisplayName())
                        .drawForeground(mouseX, mouseY);
                i++;
            }
        }
        if (this.container.base.getRotor() != null) {
            this.fontRenderer.drawString(Localization.translate("iu.wind_side") + Localization.translate(("iu.wind." + container.base.wind_side
                            .name()
                            .toLowerCase())),
                    33, 70, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("iu.wind_mec_side") + Localization.translate(("iu.wind." + this.container.base
                            .getRotorSide()
                            .name()
                            .toLowerCase())),
                    33, 80, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("iu.wind_gen") +
                            ModUtils.getString(this.container.base.generation) + " EF/t",
                    33, 90, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("iu.wind_coef") + String.format(
                            "%.2f",
                            this.container.base.getCoefficient()
                    ),
                    33, 100, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            this.fontRenderer.drawString(Localization.translate("iu.wind_tier") + String.format(
                            "%d",
                            this.container.base.getRotor().getLevel()
                    ),
                    33, 110, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );

            int meta = Math.min(this.container.base.enumTypeWind.ordinal() + this.container.base.getMinWind(), 9);
            EnumTypeWind enumTypeWinds = WindSystem.windSystem.getEnumTypeWind().values()[meta];


            this.fontRenderer.drawString(Localization.translate("iu.wind_level_info") + String.format(
                            "%d",
                            enumTypeWinds.ordinal() + 1
                    ),
                    33, 120, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );

            double hours = 0;
            double minutes = 0;
            double seconds = 0;
            final List<Double> time = ModUtils.Time(this.container.base.timers / 20D);
            if (time.size() > 0) {
                hours = time.get(0);
                minutes = time.get(1);
                seconds = time.get(2);
            }
            String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
            String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
            String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";
            this.fontRenderer.drawString(Localization.translate("iu.wind_change_time") + time1 + time2 + time3
                    ,
                    33, 130, ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
            String tooltip3 = Localization.translate("iu.wind_meter.info") +
                    String.format(
                            "%.1f",
                            enumTypeWinds.getMin() + this.container.base.getMinWindSpeed()
                    ) + "-" + String.format(
                    "%.1f",
                    enumTypeWinds.getMax() + this.container.base.getMinWindSpeed()
            ) + " m/s";
            new AdvArea(this, 33, 120, 123, 130)
                    .withTooltip(tooltip3)
                    .drawForeground(mouseX, mouseY);
        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
