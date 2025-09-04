package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.bee.Bee;
import com.denfop.api.bee.genetics.Genome;
import com.denfop.containermenu.ContainerMenuBeeAnalyzer;
import com.denfop.items.bee.ItemJarBees;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ScreenBeeAnalyzer<T extends ContainerMenuBeeAnalyzer> extends ScreenMain<ContainerMenuBeeAnalyzer> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guibeeanalyzer.png");
    private final String name;
    private int prevText;
    private float scaled;
    private int textIndex;

    public ScreenBeeAnalyzer(ContainerMenuBeeAnalyzer container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getDescriptionId());
        this.componentList.clear();
        this.imageWidth = 203;

    }


    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.bee.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 21; i++) {
                compatibleUpgrades.add(Localization.translate("iu.bee.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 160, mouseY - 10, text);
        }
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        drawString(poseStack, Minecraft.getInstance().font, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 4, 0);
        handleUpgradeTooltip(par1, par2);
        if (!this.container.base.get(0).isEmpty() && this.container.base.genome == null) {
            ModUtils.nbt(this.container.base.get(0)).putBoolean("analyzed", true);
            this.container.base.genome = new Genome(this.container.base.get(0));
            this.container.base.crop = ItemJarBees.getBee(this.container.base.get(0));
            this.container.base.set();
            textIndex = 0;
        } else if (this.container.base.get(0).isEmpty() && this.container.base.genome != null) {
            this.container.base.genome = null;
            this.container.base.crop = null;
            this.container.base.set();
            textIndex = 0;
        }
        if (this.container.base.genome != null) {
            String text = getInformationFromCrop();
            int canvasX = 13;
            int canvasY = 19;
            int canvasWidth = 150;
            int canvasHeight = 112;

            float scale = 0.5f;
            int maxWidth = (int) (canvasWidth / scale);
            double lineHeight = (font.lineHeight * 0.5);
            int x = canvasX;
            double y = canvasY;
            PoseStack pose = poseStack;
            List<String> lines = wrapTextWithNewlines(text, maxWidth);

            for (String line : lines) {
                if (y + lineHeight > canvasY + canvasHeight) break;

                pose.pushPose();
                pose.translate(x, y, 0);
                pose.scale(scale, scale, scale);
                drawString(poseStack, font, line, 0, 0, 0xFFFFFF);
                pose.popPose();

                y += lineHeight;
            }
        }
    }

    private String getInformationFromCrop() {
        Bee queen = container.base.crop;
        List<String> namesBiomes = new ArrayList<>();
        queen.getBiomes().forEach(biomeKey -> namesBiomes.add(Localization.translate("biome." + biomeKey.location().getNamespace() + "." + biomeKey.location().getPath())));

        Genome genome = container.base.genome;
        return
                Localization.translate("iu.bee_analyzer.name") + " " + Localization.translate("bee_" + queen.getName()) + "\n"
                        + Localization.translate("iu.bee_analyzer.time_life") + " " + (new Timer((int) ((queen.getTickLifecycles() * container.base.populationGenome) / 20))).getDisplay() + "\n"
                        + Localization.translate("iu.bee_analyzer.main_crop") + " " + Localization.translate("crop." + queen
                        .getCropFlower()
                        .getName()) + "\n"
                        + Localization.translate("iu.bee_analyzer.birth_rate") + " " + ModUtils.getString(queen.getOffspring() * container.base.birthRateGenome) + "\n"
                        + Localization.translate("iu.bee_analyzer.pest_crop") + " " + ModUtils.getString(Math.abs(container.base.pestGenome - 1) * 100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.daytime") + " " + ((queen.isSun() || container.base.sunGenome)
                        ? Localization.translate("iu.space_yes")
                        : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.bee_analyzer.nighttime") + " " + ((queen.isNight() || container.base.nightGenome)
                        ? Localization.translate("iu.space_yes")
                        : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.bee_analyzer.radius") + " " + ModUtils.getString(queen.getSizeTerritory().maxX * container.base.radiusGenome) + "x" + ModUtils.getString(
                        queen.getSizeTerritory().maxY * container.base.radiusGenome) + "x" + ModUtils.getString(queen.getSizeTerritory().maxZ * container.base.radiusGenome) + "\n"
                        + Localization.translate("iu.bee_analyzer.increase_food") + " " + ModUtils.getString(Math.abs(container.base.foodGenome - 1) * 100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.increase_jelly") + " " + ModUtils.getString(Math.abs(container.base.jellyGenome - 1) * 100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.increase_getting_crop_percent") + " " + ModUtils.getString(Math.abs(
                        container.base.productGenome - 1) * 100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.decrease_sicken") + " " + ModUtils.getString(Math.abs(container.base.hardeningGenome - 1) * 100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.max_population") + " " + ModUtils.getString(queen.getMaxSwarm() * container.base.swarmGenome) + "\n"
                        + Localization.translate("iu.bee_analyzer.mortality_rate") + " " + ModUtils.getString(queen.getMaxMortalityRate() * container.base.mortalityGenome * 100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.weather_resistance") + " " + (container.base.weatherGenome == 0
                        ? Localization.translate("iu.space_no")
                        :
                        (container.base.weatherGenome == 1
                                ? Localization.translate("iu.space_rain")
                                : Localization.translate("iu.space_thunder"))) + "\n"
                        + Localization.translate("iu.bee_analyzer.percent_genome_adaptive") + " " + Math.max(
                        5,
                        container.base.genomeAdaptive
                ) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.percent_genome_resistance") + " " + Math.max(5,
                        container.base.genomeResistance) + "%" + "\n"
                        + Localization.translate("iu.crop_analyzer.biomes") + namesBiomes.stream()
                        .collect(Collectors.joining(", "));


    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
