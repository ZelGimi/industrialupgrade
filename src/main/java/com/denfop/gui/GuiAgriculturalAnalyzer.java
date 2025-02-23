package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.container.ContainerLeadBox;
import com.denfop.utils.Timer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class GuiAgriculturalAnalyzer extends GuiIU<ContainerAgriculturalAnalyzer> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    private final String name;
    private int prevText;
    private float scaled;
    private int textIndex;

    public GuiAgriculturalAnalyzer(ContainerAgriculturalAnalyzer container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getUnlocalizedName() + ".name");
        this.ySize = 232;
        this.componentList.clear();
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );

        componentList.add(slots);
        this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));
        this.addElement(new ImageScreen(this, 30, 20, xSize - 36, 124));
    }
    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.crop.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 16; i++) {
                compatibleUpgrades.add(Localization.translate("iu.crop.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX-160, mouseY-10, text);
        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }
    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 11, 0);
      if (this.container.base.genome != null) {
          String text = getInformationFromCrop();
          int canvasX = 34;
          int canvasY = 24;
          int canvasWidth = xSize - 42;
          int canvasHeight = 124 - 8;
          float scale = (float) (2D / new ScaledResolution(mc).getScaleFactor());
          if (prevText != text.length()) {
              scaled = -1;
              prevText = text.length();
          }
          if (scaled == -1) {
              scale = adjustTextScale(text, canvasWidth, canvasHeight, scale, 0.8F);
              scaled = scale;
          } else {
              scale = scaled;
          }
          if (this.container.base.player.getEntityWorld().getWorldTime() % 2 == 0) {
              if (textIndex < text.length()) {
                  textIndex++;
              }
          }
          if (textIndex > text.length()) {
              textIndex = text.length();
          }
          String visibleText = text.substring(0, textIndex);
          drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale*1.39f);
      }
    }

    private String getInformationFromCrop() {
        ICrop crop = container.base.crop;

        return
                Localization.translate("iu.crop_analyzer.name") + Localization.translate("crop."+crop.getName()) + "\n"
                        + Localization.translate("iu.crop_analyzer.grow_time") + (new Timer(crop.getMaxTick() / 20)).getDisplay() + "\n"
                        + Localization.translate("iu.crop_analyzer.yield") + crop.getYield() + "\n"
                        + Localization.translate("iu.crop_analyzer.drop") + crop.getDrops().get(0).getDisplayName() + "\n"
                        + Localization.translate("iu.crop_analyzer.soil") + crop.getSoil().getStack().getDisplayName() + "\n"
                        + Localization.translate("iu.crop_analyzer.daytime") + (crop.isSun() ? Localization.translate("iu.space_yes") : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.crop_analyzer.nighttime") + (crop.isNight() ? Localization.translate("iu.space_yes") : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.crop_analyzer.grow_speed") + crop.getGrowthSpeed() + "t \n"
                        + Localization.translate("iu.crop_analyzer.min_light_level") + crop.getLightLevel() + "\n"
                        + Localization.translate("iu.crop_analyzer.max_soil_pollution") + crop.getSoilRequirements().name() + "\n"
                        + Localization.translate("iu.crop_analyzer.max_air_pollution") + crop.getAirRequirements().name() + "\n"
                        + Localization.translate("iu.crop_analyzer.max_radiation_pollution") + crop.getRadiationRequirements().name() + "\n"
                        + Localization.translate("iu.crop_analyzer.chance_weed") + (100 - crop.getChanceWeed()) + "%" + "\n"
                        + Localization.translate("iu.crop_analyzer.pest_resistance") + crop.getPestResistance() + "%" + "\n"
                        + Localization.translate("iu.crop_analyzer.ignore_soil") + (crop.isIgnoreSoil() ? Localization.translate("iu.space_yes") : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.crop_analyzer.weather_resistance") + (crop.getWeatherResistance() == 0 ? Localization.translate("iu.space_no") :
                        (crop.getWeatherResistance() == 1 ? Localization.translate("iu.space_rain") : Localization.translate("iu.space_thunder"))) + "\n"
                        + Localization.translate("iu.crop_analyzer.percent_genome_adaptive") + Math.max(5, crop.getGenomeAdaptive()) + "%" + "\n"
                        + Localization.translate("iu.crop_analyzer.percent_genome_resistance") + Math.max(5, crop.getGenomeResistance()) + "%" + "\n"
                        + Localization.translate("iu.crop_analyzer.water_requirement") + (crop.getWaterRequirement() == 0 ? Localization.translate("iu.space_no") : (crop.getWaterRequirement() + " blocks")) + "\n"
                        + Localization.translate("iu.crop_analyzer.biomes") + crop.getBiomes().stream()
                        .map(Biome::getBiomeName)
                        .collect(Collectors.joining(", "));




    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int slots = this.container.inventorySize;
        slots = slots / 9;

        int col;
        for (col = 0; col < slots; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.drawTexturedModalRect(this.guiLeft + 7 + col1 * 18, this.guiTop + 23 + col * 18, 176, 0, 18, 18);
            }
        }
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
