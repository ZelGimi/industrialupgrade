package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.agriculture.genetics.GeneticTraits;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.container.ContainerGenomeExtractor;
import com.denfop.network.packet.PacketUpdateServerTile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiGenomeExtractor extends GuiIU<ContainerGenomeExtractor> {

    public GuiGenomeExtractor(ContainerGenomeExtractor guiContainer) {
        super(guiContainer);
        this.addComponent(new GuiComponent(this, 117, 65, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.energy)
        ));
        this.addElement(new CustomButton(this, 7, 64, 100, 18, container.base, -1, Localization.translate("iu.remove_genome")));

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        if (container.base.genCrop != null) {
            int xMin = (this.width - this.xSize) / 2;
            int yMin = (this.height - this.ySize) / 2;
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
            int xMin = (this.width - this.xSize) / 2;
            int yMin = (this.height - this.ySize) / 2;
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
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (container.base.genCrop != null) {
            List<GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genCrop.getGeneticTraitsMap().values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackImage(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_crop, 1,
                        geneticTraitsList.get(finalI).ordinal()
                )).drawForeground(par1, par2);
            }
        }
        if (container.base.genBee != null) {
            List<com.denfop.api.bee.genetics.GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genBee
                    .getGeneticTraitsMap()
                    .values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackImage(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_bee, 1,
                        geneticTraitsList.get(finalI).ordinal()
                )).drawForeground(par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (container.base.genCrop != null) {
            List<GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genCrop.getGeneticTraitsMap().values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackImage(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_crop, 1,
                        geneticTraitsList.get(finalI).ordinal()
                )).drawBackground(this.guiLeft, guiTop);
            }
        }
        if (container.base.genBee != null) {
            List<com.denfop.api.bee.genetics.GeneticTraits> geneticTraitsList = new ArrayList<>(container.base.genBee
                    .getGeneticTraitsMap()
                    .values());
            for (int i = 0; i < geneticTraitsList.size(); i++) {
                final int finalI = i;
                new ItemStackImage(this, 40 + (i % 7) * 18, 6 + (i / 7) * 18, () -> new ItemStack(IUItem.genome_bee, 1,
                        geneticTraitsList.get(finalI).ordinal()
                )).drawBackground(this.guiLeft, guiTop);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
