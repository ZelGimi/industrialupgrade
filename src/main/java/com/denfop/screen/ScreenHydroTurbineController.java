package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.api.widget.ItemStackWidget;
import com.denfop.api.widget.ScrollDirection;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.api.windsystem.EnumTypeWind;
import com.denfop.api.windsystem.WindSystem;
import com.denfop.containermenu.ContainerMenuHydroTurbineController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenHydroTurbineController<T extends ContainerMenuHydroTurbineController> extends ScreenMain<ContainerMenuHydroTurbineController> {

    private final ResourceLocation background;
    public boolean hoverChangeSide;
    public boolean hoverChangePower;

    public ScreenHydroTurbineController(ContainerMenuHydroTurbineController guiContainer) {
        super(guiContainer, guiContainer.base.getStyle());
        this.imageHeight = 247;
        this.imageWidth = 207;
        this.background = ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiwater_turbine.png");


        this.inventory.setY(159);
        this.inventory.setX(22);
    }


    @Override
    public void init() {
        super.init();

    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (this.container.base.getRotor() != null && hoverChangeSide) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d4, double d3) {
        ScrollDirection scrollDirection = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        if (mouseX >= 35 && mouseX <= 171 && mouseY >= 118 && mouseY <= 124) {
            new PacketUpdateServerTile(this.container.base, this.container.base.coefficient_power + d3);
            hoverChangePower = true;
        }
        return super.mouseScrolled(d, d2, d4, d3);

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.water_generator.info_main"));
            List<String> compatibleUpgrades = ListInformationUtils.water_generator;
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
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        String name = this.container.base.getDisplayName().getString();
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
        int textY = (int) ((this.guiTop + 3) / scale);


        poseStack.drawString(Minecraft.getInstance().font, name, textX, textY, 4210752, false);
        pose.scale(1 / scale, 1 / scale, 1);

        pose.popPose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindTexture();
        int xoffset = guiLeft;
        int yoffset = guiTop;
        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, xoffset + 3, yoffset + 3, 0, 0, 10, 10);
        bindTexture();
        drawTexturedModalRect(poseStack, (int) (xoffset + 34 + ((this.container.base.coefficient_power - 100) / 50D) * 130), yoffset + 116, 235, 0, 10, 12);
        if (hoverChangePower) {
            drawTexturedModalRect(poseStack, (int) (xoffset + 34 + ((this.container.base.coefficient_power - 100) / 50D) * 130), yoffset + 116, 246, 0, 10, 12);

        }
        hoverChangePower = false;
        if (!this.container.base.slot.isEmpty()) {
            final List<ItemStack> list = RotorUpgradeSystem.instance.getListStack(this.container.base.slot.get(0));
            int i = 0;
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                new ItemStackWidget(this, 80 + i * 18, 134, () -> stack).drawBackground(poseStack, xoffset, yoffset);
                i++;

            }

        }
        bindTexture(getTexture());
        ItemStack rotor = container.base.slot.get(0);
        if (rotor.getDamageValue() >= rotor.getMaxDamage() * 0.25) {
            drawTexturedModalRect(poseStack, this.guiLeft + 46, (int) (this.guiTop + 137)
                    , 245, (int) 34, 11, (int) 11);
        }
        ;
        if (this.container.base.getRotor() != null) {
            int windHeight = 74;
            double renderHeight = (double) windHeight * ModUtils.limit(
                    Math.min(24.4 + this.container.base.mind_speed, WindSystem.windSystem.getSpeedFromWaterPower(
                                    this.container.base.getBlockPos(),
                                    this.container.base,
                                    this.container.base.generation / ((this.container.base.coefficient_power) / 100D)
                            ) * this.container.base.getCoefficient()
                    ) / 24.4,
                    0.0D,
                    1.0D
            );
            double bar = renderHeight;
            drawTexturedModalRect(poseStack, this.guiLeft + 11, (int) (this.guiTop + 15 + windHeight - (bar))
                    , 236, (int) (120 - (bar)), 9, (int) (bar));

            windHeight = 74;
            renderHeight = (double) windHeight * this.container.base.energy.getEnergy().getFillRatio();
            bar = renderHeight;
            drawTexturedModalRect(poseStack, this.guiLeft + 186, (int) (this.guiTop + 15 + windHeight - (bar))
                    , 247, (int) (120 - (bar)), 9, (int) (bar));

            if (hoverChangeSide) {
                drawTexturedModalRect(poseStack, this.guiLeft + 157, (int) (this.guiTop + 133)
                        , 236, (int) 13, 20, (int) (20));
            }
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        handleUpgradeTooltip(mouseX, mouseY);
        hoverChangeSide = false;
        if (mouseX >= 157 && mouseY >= 133 && (mouseX <= 176 && mouseY <= 152)) {
            hoverChangeSide = true;
        }
        if (this.container.base.getWorld().dimension() == Level.OVERWORLD) {
            String fields = "";
            if (this.container.base.getMinWind() != 0) {
                fields += Localization.translate("iu.water_meter.info") + String.format(
                        "%.1f",
                        Math.min(24.7 + this.container.base.mind_speed, WindSystem.windSystem.getSpeedFromWaterPower(
                                        this.container.base.getBlockPos(),
                                        this.container.base,
                                        this.container.base.generation / ((this.container.base.coefficient_power) / 100D)
                                ) / this.container.base.getCoefficient()
                        )
                ) + " m/s";
            } else {
                fields += Localization.translate("iu.water_meter.info") + String.format(
                        "%.1f",
                        this.container.base.wind_speed + this.container.base.mind_speed
                ) + " m/s";
            }
            PoseStack pose = poseStack.pose();
            pose.pushPose();
            pose.translate(175 - getStringWidth(fields), 100, 0);
            pose.scale(0.85f, 0.85f, 1);
            draw(poseStack, fields, 0, 0, ModUtils.convertRGBcolorToInt(13, 229, 34));
            pose.popPose();
        }


        if (!this.container.base.slot.isEmpty()) {
            final List<ItemStack> list = RotorUpgradeSystem.instance.getListStack(this.container.base.slot.get(0));
            int i = 0;
            for (ItemStack stack : list) {

                if (stack.isEmpty()) {
                    i++;
                    continue;
                }
                new AdvancedTooltipWidget(this, 80 + i * 18, 134, 80 + i * 18 + 18, 134 + 18)
                        .withTooltip(stack.getDisplayName().getString())
                        .drawForeground(poseStack, mouseX, mouseY);
                i++;
            }
        }

        if (this.container.base.getRotor() != null) {
            if (container.base.wind_side == null)
                return;
            String fields = Localization.translate("iu.water_side") + Localization.translate(("iu.wind." + container.base.wind_side
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
            fields += "\n" + Localization.translate("iu.water_coef") + String.format(
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
                new AdvancedTooltipWidget(this, 9, 12, 23, 91)
                        .withTooltip(tooltip3)
                        .drawForeground(poseStack, mouseX, mouseY);
            }
            new AdvancedTooltipWidget(this, 35, 117, 172, 125)
                    .withTooltip(this.container.base.coefficient_power + "%")
                    .drawForeground(poseStack, mouseX, mouseY);

            new AdvancedTooltipWidget(this, 183, 12, 197, 91)
                    .withTooltip(ModUtils.getString(Math.min(
                            this.container.base.energy.getEnergy().getEnergy(),
                            this.container.base.energy.getEnergy().getCapacity()
                    )) + "/" + ModUtils.getString(this.container.base.energy.getEnergy().getCapacity()) + " " +
                            "EF" + "\n" + Localization.translate(
                            "EUStorage.gui.info.output",
                            ModUtils.getString(EnergyNetGlobal.instance.getPowerFromTier(this.container.base.energy.getEnergy().getSourceTier())
                            )
                    ))
                    .drawForeground(poseStack, mouseX, mouseY);
            ItemStack rotor = container.base.slot.get(0);
            if (rotor.getDamageValue() >= rotor.getMaxDamage() * 0.25) {

                new TooltipWidget(this, 46, 137, 11, 11)
                        .withTooltip(Localization.translate("iu.wind.repair"))
                        .drawForeground(poseStack, mouseX, mouseY);
            }
            ;
            float scale = 1;
            List<String> lines = wrapTextWithNewlines(fields, 255);
            int y = 0;
            for (String line : lines) {
                PoseStack poseStack1 = poseStack.pose();
                poseStack1.pushPose();
                poseStack1.translate(32, 20 + y, 0);
                poseStack1.scale(0.7f, 0.7f, 1);
                draw(poseStack, line, 0, 0, ModUtils.convertRGBcolorToInt(13, 229, 34));
                poseStack1.popPose();
                y += 10;
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return background;
    }

}
