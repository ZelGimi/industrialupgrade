package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.container.ContainerWindTurbine;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiWindTurbine<T extends ContainerWindTurbine> extends GuiIU<ContainerWindTurbine> implements GuiPageButtonList.GuiResponder,
        GuiVerticalSlider.FormatHelper {

    private final ResourceLocation background;
    float scaled = -1;
    private int prevText;

    public GuiWindTurbine(ContainerWindTurbine guiContainer) {
        super(guiContainer, guiContainer.base.getStyle());
        this.imageHeight = 245;
        this.imageWidth = 211;
        this.background = new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
        this.addComponent(new GuiComponent(this, 183, 98, EnumTypeComponent.ENERGY_HEIGHT,
                new Component<>(this.container.base.energy.getEnergy())
        ));
        this.addComponent(new GuiComponent(this, 33, 43, EnumTypeComponent.WIND,
                new Component<>(new ComponentEmpty())
        ));
        this.elements.add(new ImageInterface(this, 0, 0, this.imageWidth, this.imageHeight));
        this.elements.add(new ImageScreen(this, 27, 40, 125, 20));
        this.elements.add(new ImageScreen(this, 27, 65, 150, 80));
        EnumTypeComponent component;
        switch (guiContainer.base.getStyle()) {
            case ADVANCED:
                component = EnumTypeComponent.ADVANCED;
                break;
            case IMPROVED:
                component = EnumTypeComponent.IMPROVED;
                break;
            case PERFECT:
                component = EnumTypeComponent.PERFECT;
                break;
            default:
                component = EnumTypeComponent.DEFAULT;
                break;
        }
        for (int i = 0; i < 4; i++) {
            componentList.add(new GuiComponent(this, 180,
                    18 + i * 18, component,
                    new Component<>(new ComponentEmpty())
            ));
        }
        this.inventory.setY(163);
        this.inventory.setX(7);
    }


    GuiVerticalSlider slider;

    @Override
    public void init() {
        super.init();
        slider = new GuiVerticalSlider(this, 0, (this.width - this.imageWidth) / 2 + 8, (this.height - this.imageHeight) / 2 + 70,
                "",
               100,  150, this.container.base.coefficient_power, this, 75
        );

        this.addWidget(slider);
        this.addRenderableWidget(slider);
    }
    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return this.container.base.coefficient_power + "%";
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        new PacketUpdateServerTile(this.container.base, v);

    }

    @Override
    public void setEntryValue(final int i, final String s) {

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



    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
       RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        int xoffset = guiLeft;
        int yoffset =guiTop;
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, xoffset + 3, yoffset + 3, 0, 0, 10, 10);
        bindTexture(getTexture());
        if (!this.container.base.slot.isEmpty()) {
            final List<ItemStack> list = RotorUpgradeSystem.instance.getListStack(this.container.base.slot.get(0));
            int i = 0;
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                new ItemStackImage(this, 181, 19 + i * 18, () -> stack).drawBackground(poseStack,xoffset, yoffset);
                i++;

            }

        }

    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack,final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack,mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        if (this.container.base.getWorld().dimension() == Level.OVERWORLD) {
            float scale = (float) (2D / Minecraft.getInstance().getWindow().getGuiScale());
            String fields = "";
            if (this.container.base.getMinWind() != 0) {
                fields += Localization.translate("iu.wind_meter.info") + String.format(
                        "%.1f",
                        Math.min(24.7 + this.container.base.mind_speed, WindSystem.windSystem.getSpeedFromPower(
                                        this.container.base.getBlockPos(),
                                        this.container.base,
                                        this.container.base.generation
                                ) / this.container.base.getCoefficient()
                        )
                ) + " m/s";
            } else {
                fields += Localization.translate("iu.wind_meter.info") + String.format(
                        "%.1f",
                        this.container.base.wind_speed + this.container.base.mind_speed
                ) + " m/s";
            }
            scale = adjustTextScale(fields, 125 - 10, 20, scale, 0.8F);
            drawTextInCanvas(poseStack, fields, 27 + 30, 48, 125 - 10, 20, scale * 0.8f, ModUtils.convertRGBcolorToInt(13, 229, 34));

        }


        if (!this.container.base.slot.isEmpty()) {
            final List<ItemStack> list = RotorUpgradeSystem.instance.getListStack(this.container.base.slot.get(0));
            int i = 0;
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                new AdvArea(this, 180, 18 + i * 18, 197, 35 + i * 18)
                        .withTooltip(stack.getDisplayName().getString())
                        .drawForeground(poseStack, mouseX, mouseY);
                i++;
            }
        }
        if (this.container.base.getRotor() != null) {
            String fields = Localization.translate("iu.wind_side") + Localization.translate(("iu.wind." + container.base.wind_side
                    .name()
                    .toLowerCase()));
            if (this.container.base.getRotorSide() != null) {
                fields += "\n" + Localization.translate("iu.wind_mec_side") + Localization.translate(("iu.wind." + this.container.base
                        .getRotorSide()
                        .name()
                        .toLowerCase()));

            }
            fields += "\n" + Localization.translate("iu.wind_gen") +
                    ModUtils.getString(this.container.base.generation) + " EF/t";
            fields += "\n" + Localization.translate("iu.wind_coef") + String.format(
                    "%.2f",
                    this.container.base.getCoefficient()
            );
            fields += "\n" + Localization.translate("iu.wind_tier") + String.format(
                    "%d",
                    this.container.base.getRotor().getLevel()
            );
            if (this.container.base.enumTypeWind != null) {
                int meta = Math.min(this.container.base.enumTypeWind.ordinal() + this.container.base.getMinWind(), 9);
                EnumTypeWind enumTypeWinds = WindSystem.windSystem.getEnumTypeWind().values()[meta];


                fields += "\n" + Localization.translate("iu.wind_level_info") + String.format(
                        "%d",
                        enumTypeWinds.ordinal() + 1
                );

                double hours = 0;
                double minutes = 0;
                double seconds = 0;
                final List<Double> time = ModUtils.Time(this.container.base.timers / 20D);
                if (!time.isEmpty()) {
                    hours = time.get(0);
                    minutes = time.get(1);
                    seconds = time.get(2);
                }
                String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";
                fields += "\n" + Localization.translate("iu.wind_change_time") + time1 + time2 + time3;
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
                        .drawForeground(poseStack, mouseX, mouseY);
            }
            float scale = (float) (2D /Minecraft.getInstance().getWindow().getGuiScale());
            if (prevText != fields.length()) {
                scaled = -1;
                prevText = fields.length();
            }
            if (scaled == -1) {
                scale = adjustTextScale(fields, 150 - 10, 80 - 10, scale, 0.8F);
                scaled = scale;
            } else {
                scale = scaled;
            }
            drawTextInCanvas(poseStack, fields, 27 + 4, 65 + 4, 150 - 4, 80 - 4, scale * 1.3f, ModUtils.convertRGBcolorToInt(13, 229, 34));
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
