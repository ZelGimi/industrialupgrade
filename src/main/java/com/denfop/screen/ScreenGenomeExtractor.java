package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.crop.genetics.GeneticTraits;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerMenuGenomeExtractor;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScreenGenomeExtractor<T extends ContainerMenuGenomeExtractor> extends ScreenMain<ContainerMenuGenomeExtractor> {

    public ScreenGenomeExtractor(ContainerMenuGenomeExtractor guiContainer) {
        super(guiContainer);
        this.addComponent(new ScreenWidget(this, 117, 65, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.energy)
        ));
        this.addWidget(new ButtonWidget(this, 7, 64, 100, 18, container.base, -1, Localization.translate("iu.remove_genome")));

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        if (container.base.genCrop != null) {
            int xMin = guiLeft;
            int yMin = guiTop;
            int x = i - xMin;
            int y = j - yMin;
            List<GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genCrop.getGeneticTraitsMap().values());
            for (int index = 0; index < geneticTraitsList.size(); index++) {
                final int coordX = 40 + (index % 7) * 18;
                final int coordY = 6 + (index / 7) * 18;
                if (x >= coordX && x < coordX + 18 && y >= coordY && y < coordY + 18) {
                    new PacketUpdateServerTile(this.container.base, geneticTraitsList.get(index).ordinal());
                }
            }
        }
        if (container.base.genBee != null) {
            int xMin = guiLeft;
            int yMin = guiTop();
            int x = i - xMin;
            int y = j - yMin;
            List<com.denfop.api.bee.genetics.GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genBee
                    .getGeneticTraitsMap()
                    .values());
            for (int index = 0; index < geneticTraitsList.size(); index++) {
                final int coordX = 40 + (index % 7) * 18;
                final int coordY = 6 + (index / 7) * 18;
                if (x >= coordX && x < coordX + 18 && y >= coordY && y < coordY + 18) {
                    new PacketUpdateServerTile(this.container.base, geneticTraitsList.get(index).ordinal());
                }
            }
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        if (container.base.genCrop != null) {
            List<GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genCrop.getGeneticTraitsMap().values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackWidget(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_crop.getStack(
                        geneticTraitsList.get(finalI).ordinal())
                )).drawForeground(poseStack, par1, par2);
            }
        }
        if (container.base.genBee != null) {
            List<com.denfop.api.bee.genetics.GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genBee
                    .getGeneticTraitsMap()
                    .values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackWidget(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_bee.getStack(
                        geneticTraitsList.get(finalI).ordinal())
                )).drawForeground(poseStack, par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        if (container.base.genCrop != null) {
            List<GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genCrop.getGeneticTraitsMap().values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackWidget(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_crop.getStack(
                        geneticTraitsList.get(finalI).ordinal())
                )).drawBackground(poseStack, this.guiLeft, guiTop);
            }
        }
        if (container.base.genBee != null) {
            List<com.denfop.api.bee.genetics.GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genBee
                    .getGeneticTraitsMap()
                    .values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackWidget(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_bee.getStack(
                        geneticTraitsList.get(finalI).ordinal())
                )).drawBackground(poseStack, this.guiLeft, guiTop);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
