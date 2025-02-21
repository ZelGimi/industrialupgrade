package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.bee.IBee;
import com.denfop.api.bee.genetics.Genome;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.container.ContainerBeeAnalyzer;
import com.denfop.container.ContainerLeadBox;
import com.denfop.utils.ModUtils;
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
public class GuiBeeAnalyzer extends GuiIU<ContainerBeeAnalyzer> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guimachine.png");
    private final String name;
    private int prevText;
    private float scaled;
    private int textIndex;

    public GuiBeeAnalyzer(ContainerBeeAnalyzer container, final ItemStack itemStack1) {
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

            this.drawTooltip(mouseX-160, mouseY-10, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 11, 0);
        handleUpgradeTooltip(par1, par2);
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
            drawTextInCanvas(visibleText, canvasX, canvasY, canvasWidth, canvasHeight, scale*1f);
        }
    }

    private String getInformationFromCrop() {
        IBee queen = container.base.crop;
        Genome genome = container.base.genome;
        return
                Localization.translate("iu.bee_analyzer.name") + " " + Localization.translate("bee_" + queen.getName()) + "\n"
                        + Localization.translate("iu.bee_analyzer.time_life") + " " + (new Timer((int) ((queen.getTickLifecycles() * container.base.populationGenome)/ 20))).getDisplay() + "\n"
                        + Localization.translate("iu.bee_analyzer.main_crop") + " " + Localization.translate("crop."+queen.getCropFlower().getName()) + "\n"
                        + Localization.translate("iu.bee_analyzer.birth_rate") + " " +  ModUtils.getString(queen.getOffspring() * container.base.birthRateGenome)+ "\n"
                        + Localization.translate("iu.bee_analyzer.pest_crop") + " " + ModUtils.getString(Math.abs(container.base.pestGenome-1)*100)+"%" + "\n"
                        + Localization.translate("iu.bee_analyzer.daytime") + " " + ((queen.isSun()||container.base.sunGenome) ? Localization.translate("iu.space_yes") : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.bee_analyzer.nighttime") + " " + ((queen.isNight()||container.base.nightGenome) ? Localization.translate("iu.space_yes") : Localization.translate("iu.space_no")) + "\n"
                        + Localization.translate("iu.bee_analyzer.radius") + " " + ModUtils.getString(queen.getSizeTerritory().maxX*container.base.radiusGenome)+ "x" + ModUtils.getString(queen.getSizeTerritory().maxY*container.base.radiusGenome)+ "x" + ModUtils.getString(queen.getSizeTerritory().maxZ*container.base.radiusGenome) + "\n"
                        + Localization.translate("iu.bee_analyzer.increase_food") + " " + ModUtils.getString(Math.abs(container.base.foodGenome-1)*100)+"%" + "\n"
                        + Localization.translate("iu.bee_analyzer.increase_jelly") + " " + ModUtils.getString(Math.abs(container.base.jellyGenome-1)*100)+"%" + "\n"
                        + Localization.translate("iu.bee_analyzer.increase_getting_crop_percent") + " " + ModUtils.getString(Math.abs(container.base.productGenome-1)*100)+"%" + "\n"
                        + Localization.translate("iu.bee_analyzer.decrease_sicken") + " " + ModUtils.getString(Math.abs(container.base.hardeningGenome-1)*100)+"%" + "\n"
                        + Localization.translate("iu.bee_analyzer.max_population") + " " + ModUtils.getString(queen.getMaxSwarm() * container.base.swarmGenome) + "\n"
                        + Localization.translate("iu.bee_analyzer.mortality_rate") + " " + ModUtils.getString(queen.getMaxMortalityRate() * container.base.mortalityGenome*100) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.weather_resistance") + " " + (container.base.weatherGenome == 0 ? Localization.translate("iu.space_no") :
                        (container.base.weatherGenome == 1 ? Localization.translate("iu.space_rain") : Localization.translate("iu.space_thunder"))) + "\n"
                        + Localization.translate("iu.bee_analyzer.percent_genome_adaptive") + " " + Math.max(5, container.base.genomeAdaptive) + "%" + "\n"
                        + Localization.translate("iu.bee_analyzer.percent_genome_resistance") + " " + Math.max(5,container.base.genomeResistance) + "%";




    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
