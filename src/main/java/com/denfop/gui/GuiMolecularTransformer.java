package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.container.ContainerBaseMolecular;
import com.denfop.utils.ModUtils;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMolecularTransformer extends GuiIC2<ContainerBaseMolecular> {

    public final ContainerBaseMolecular container;

    public GuiMolecularTransformer(ContainerBaseMolecular container1) {
        super(container1, 220, 193);
        this.container = container1;
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 10, 6, name, 4210752, false);
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        new AdvArea(this, 7, 3, 58, 17).withTooltip(Localization.translate("iu.molecular_info") + "\n" + Localization.translate(
                "iu.molecular_info3")).drawForeground(mouseX, mouseY);
        new AdvArea(this, 180, 3, 195, 17)
                .withTooltip(Localization.translate("iu.molecular_info1") + "\n" + Localization.translate(
                        "iu.molecular_info2"))
                .drawForeground(
                        mouseX,
                        mouseY
                );

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();
        String input = Localization.translate("gui.MolecularTransformer.input") + ": ";
        String output = Localization.translate("gui.MolecularTransformer.output") + ": ";
        String energyPerOperation = Localization.translate("gui.MolecularTransformer.energyPerOperation") + ": ";
        String progress = Localization.translate("gui.MolecularTransformer.progress") + ": ";

        this.fontRenderer.drawString(Localization.translate("button.changemode"), this.guiLeft + 17, this.guiTop + 6,
                ModUtils.convertRGBcolorToInt(23, 119, 167)
        );
        this.fontRenderer.drawString(Localization.translate("button.rg"), this.guiLeft + 186, this.guiTop + 6,
                ModUtils.convertRGBcolorToInt(23, 119, 167)
        );

        double chargeLevel = (15.0D * this.container.base.getProgress());


        if (chargeLevel > 0 && !this.container.base.inputSlot.isEmpty() && this.container.base.inputSlot.continue_proccess(this.container.base.outputSlot)) {
            MachineRecipe output1 = this.container.base.output;

            List<Double> time;
            if (!this.container.base.queue) {
                this.bindTexture();
                drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 48, 221, 7, 10, (int) chargeLevel);
                this.bindTexture();
                this.fontRenderer.drawString(input + this.container.base.inputSlot.get().getDisplayName(),
                        this.guiLeft + 60, this.guiTop + 25, 4210752
                );

                this.fontRenderer.drawString(output + output1.getRecipe().output.items.get(0).getDisplayName(), this.guiLeft + 60,
                        this.guiTop + 25 + 11, 4210752
                );
                this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(output1.getRecipe().output.metadata.getDouble(
                                "energy")) +
                                " EU",
                        this.guiLeft + 60, this.guiTop + 25 + 22, 4210752
                );
                if (this.container.base.getProgress() * 100 <= 100) {
                    this.fontRenderer.drawString(
                            progress + ModUtils.getString(this.container.base.getProgress() * 100) + "%",
                            this.guiLeft + 60, this.guiTop + 25 + 33, 4210752
                    );
                }
                if (this.container.base.getProgress() * 100 > 100) {
                    this.fontRenderer.drawString(
                            progress + ModUtils.getString(100) + "%",
                            this.guiLeft + 60, this.guiTop + 25 + 33, 4210752
                    );
                }
                this.fontRenderer.drawString(
                        "EU/t: " + ModUtils.getString(this.container.base.differenceenergy),
                        this.guiLeft + 60, this.guiTop + 25 + 44, 4210752
                );
                double hours = 0;
                double minutes = 0;
                double seconds = 0;
                time = this.container.base.getTime(output1.getRecipe().output.metadata.getDouble("energy"));
                if (time.size() > 0) {
                    hours = time.get(0);
                    minutes = time.get(1);
                    seconds = time.get(2);
                }
                String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                this.fontRenderer.drawString(
                        Localization.translate("iu.timetoend") + time1 + time2 + time3,
                        this.guiLeft + 60, this.guiTop + 25 + 55, 4210752
                );

            } else {

                if (this.container.base.outputSlot.get().isEmpty() || this.container.base.outputSlot.get().getCount() < 64) {
                    this.bindTexture();
                    drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 48, 221, 7, 10, (int) chargeLevel);
                    this.bindTexture();
                    int coef =
                            (int) (this.container.base.energy.getCapacity() / this.container.base
                                    .getOutput()
                                    .getRecipe().output.metadata.getDouble("energy"));
                    this.fontRenderer.drawString(input + this.container.base
                                    .getOutput()
                                    .getRecipe().input
                                    .getInputs()
                                    .get(0)
                                    .getInputs()
                                    .get(0)
                                    .getCount() * coef + "x" + this.container.base.inputSlot
                                    .get()
                                    .getDisplayName(),
                            this.guiLeft + 60, this.guiTop + 25, 4210752
                    );

                    this.fontRenderer.drawString(
                            output + this.container.base
                                    .getOutput()
                                    .getRecipe().output.items
                                    .get(0)
                                    .getCount() * coef + "x" + output1.getRecipe().output.items.get(0).getDisplayName(),
                            this.guiLeft + 60,
                            this.guiTop + 25 + 11,
                            4210752
                    );
                    this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(this.container.base.energy.getCapacity()) + " EU",
                            this.guiLeft + 60, this.guiTop + 25 + 22, 4210752
                    );
                    if (this.container.base.getProgress() * 100 <= 100) {
                        this.fontRenderer.drawString(
                                progress + ModUtils.getString(this.container.base.getProgress() * 100) + "%",
                                this.guiLeft + 60, this.guiTop + 25 + 33, 4210752
                        );
                    }
                    if (this.container.base.getProgress() * 100 > 100) {
                        this.fontRenderer.drawString(
                                progress + ModUtils.getString(100) + "%",
                                this.guiLeft + 60, this.guiTop + 25 + 33, 4210752
                        );
                    }

                    this.fontRenderer.drawString(
                            "EU/t: " + ModUtils.getString(this.container.base.differenceenergy),
                            this.guiLeft + 60, this.guiTop + 25 + 44, 4210752
                    );
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    time = this.container.base.getTime(this.container.base.energy.getCapacity());


                    if (time.size() > 0) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") : "";

                    this.fontRenderer.drawString(
                            Localization.translate("iu.timetoend") + time1 + time2 + time3,
                            this.guiLeft + 60, this.guiTop + 25 + 55, 4210752
                    );


                }
            }

        }
    }


    public String getName() {
        return Localization.translate("blockMolecularTransformer.name");
    }

    public ResourceLocation getTexture() {


        if (this.container.base.redstoneMode == 1) {

            return new ResourceLocation(
                    Constants.MOD_ID,
                    "textures/gui/guiMolecularTransformerNew_chemical_green.png"
            );
        } else if (this.container.base.redstoneMode == 2) {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew_gold.png");
        } else if (this.container.base.redstoneMode == 3) {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew_red.png");
        } else if (this.container.base.redstoneMode == 4) {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew_silver.png");
        } else if (this.container.base.redstoneMode == 5) {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew_violet.png");
        } else if (this.container.base.redstoneMode == 6) {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew_blue.png");
        } else if (this.container.base.redstoneMode == 7) {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew_green.png");
        } else {

            return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiMolecularTransformerNew.png");
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 180 && x <= 197 && y >= 3 && y <= 17) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }

        if (x >= 7 && x <= 60 && y >= 3 && y <= 17) {
            IUCore.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);
        }

    }

}
