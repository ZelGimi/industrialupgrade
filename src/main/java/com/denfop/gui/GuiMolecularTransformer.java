package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.container.ContainerBaseMolecular;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMolecularTransformer extends GuiCore<ContainerBaseMolecular> {

    public final ContainerBaseMolecular container;

    public GuiMolecularTransformer(ContainerBaseMolecular container1) {
        super(container1, container1.base.maxAmount > 1 ? (container1.base.maxAmount == 2 ? 250 : 255) : 225, 225);
        this.container = container1;
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2, 4, name, ModUtils.convertRGBcolorToInt(255, 255, 255), false);
    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        new AdvArea(this, 21, 29, 58, 41).withTooltip(Localization.translate("iu.molecular_info") + "\n" + Localization.translate(
                "iu.molecular_info3") + " " + (this.container.base.queue ? "x64" : "x1")).drawForeground(mouseX, mouseY);
        int dopX = container.base.maxAmount == 1 ? 0 : (container.base.maxAmount == 2 ? 25 : 30);
        new AdvArea(this, 152 + dopX, 25, 217 + dopX, 45)
                .withTooltip(Localization.translate("iu.molecular_info1") + "\n" + Localization.translate(
                        "iu.molecular_info2"))
                .drawForeground(
                        mouseX,
                        mouseY
                );
        String input = Localization.translate("gui.MolecularTransformer.input") + ": ";
        String output = Localization.translate("gui.MolecularTransformer.output") + ": ";
        String energyPerOperation = Localization.translate("gui.MolecularTransformer.energyPerOperation") + ": ";
        String progress = Localization.translate("gui.MolecularTransformer.progress") + ": ";


        if (container.base.maxAmount > 1) {
            int dopX1 = container.base.maxAmount == 4 ? 10 : 26;
            int mult = container.base.maxAmount == 4 ? 19 : 20;
            for (int i = 0; i < container.base.maxAmount; i++) {
                if (!this.container.base.inputSlot[i].isEmpty() && this.container.base.inputSlot[i].continue_proccess(
                        this.container.base.outputSlot[i])) {
                    double chargeLevel = (20.0D * this.container.base.getProgress(i));
                    if (chargeLevel > 0) {
                        String toolip = "";
                        if (!this.container.base.queue) {
                            if (this.container.base.getProgress(i) * 100 <= 100) {

                                toolip +=
                                        progress + (this.container.base.getProgress(i) <= 0.01 ? 0 :
                                                ModUtils.getString(this.container.base.getProgress(i) * 100)) + "%" + "\n";
                            }
                            toolip += energyPerOperation + ModUtils.getString(-container.base.energySlots[i]) + " EF/t" + "\n";
                            MachineRecipe output1 = this.container.base.output[i];
                            toolip +=
                                    input + this.container.base.inputSlot[i].get(0).getDisplayName() + "\n";
                            toolip +=
                                    output + output1.getRecipe().output.items.get(0).getDisplayName() + "\n";
                            toolip += energyPerOperation + ModUtils.getString(output1.getRecipe().output.metadata.getDouble(
                                    "energy")) +
                                    " EF" + "\n";
                            ;
                        } else {
                            if (this.container.base.getProgress(i) * 100 <= 100) {

                                toolip +=
                                        progress + (this.container.base.getProgress(i) <= 0.01 ? 0 :
                                                ModUtils.getString(this.container.base.getProgress(i) * 100)) + "%" + "\n";
                            }
                            toolip += energyPerOperation + ModUtils.getString(container.base.energySlots[i]) + " EF/t" + "\n";
                            int coef = (int) (this.container.base.maxEnergySlots[i] / this.container.base
                                    .getOutput(i)
                                    .getRecipe().output.metadata.getDouble("energy"));
                            toolip += input + this.container.base
                                    .getOutput(i)
                                    .getRecipe().input
                                    .getInputs()
                                    .get(0)
                                    .getInputs()
                                    .get(0)
                                    .getCount() * coef + "x" + this.container.base.inputSlot[i]
                                    .get()
                                    .getDisplayName() + "\n";

                            toolip +=
                                    output + this.container.base
                                            .getOutput(i)
                                            .getRecipe().output.items
                                            .get(0)
                                            .getCount() * coef + "x" + this.container.base
                                            .getOutput(i).getRecipe().output.items.get(0).getDisplayName() + "\n";
                            toolip += energyPerOperation + ModUtils.getString(this.container.base.maxEnergySlots[i]) + "\n";
                        }
                        new Area(this, dopX1 + i * mult, 76, 14, 20)
                                .withTooltip(toolip)
                                .drawForeground(
                                        mouseX,
                                        mouseY
                                );
                    }
                }
            }
        }

    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.bindTexture();
        String input = Localization.translate("gui.MolecularTransformer.input") + ": ";
        String output = Localization.translate("gui.MolecularTransformer.output") + ": ";
        String energyPerOperation = Localization.translate("gui.MolecularTransformer.energyPerOperation") + ": ";
        String progress = Localization.translate("gui.MolecularTransformer.progress") + ": ";


        double chargeLevel = (20.0D * this.container.base.getProgress(0));
        if (container.base.maxAmount == 1) {
            if (!this.container.base.queue) {
                drawTexturedModalRect(this.guiLeft + 22, this.guiTop + 30, 240, 20, 16, 11);

            } else {
                drawTexturedModalRect(this.guiLeft + 42, this.guiTop + 30, 240, 20, 16, 11);

            }
        } else {
            if (!this.container.base.queue) {
                drawTexturedModalRect(this.guiLeft + 22, this.guiTop + 30, 27, 244, 16, 11);

            } else {
                drawTexturedModalRect(this.guiLeft + 42, this.guiTop + 30, 27, 244, 16, 11);

            }
        }
        if (container.base.maxAmount == 1) {
            if (chargeLevel > 0 && !this.container.base.inputSlot[0].isEmpty() && this.container.base.inputSlot[0].continue_proccess(
                    this.container.base.outputSlot[0])) {
                MachineRecipe output1 = this.container.base.output[0];
                this.bindTexture();
                drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 75, 242, 32, 14, (int) chargeLevel);

                List<Double> time;
                if (!this.container.base.queue) {

                    this.fontRenderer.drawString(input + this.container.base.inputSlot[0].get().getDisplayName(),
                            this.guiLeft + 60, this.guiTop + 55, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );

                    this.fontRenderer.drawString(
                            output + output1.getRecipe().output.items.get(0).getDisplayName(),
                            this.guiLeft + 60,
                            this.guiTop + 65,
                            ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(container.base.energySlots[0]) +
                                    " EF",
                            this.guiLeft + 60, this.guiTop + 75, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    if (this.container.base.getProgress(0) * 100 <= 100) {
                        this.fontRenderer.drawString(
                                progress + (this.container.base.getProgress(0) <= 0.01 ? 0 :
                                        ModUtils.getString(this.container.base.getProgress(0) * 100)) + "%",
                                this.guiLeft + 60, this.guiTop + 85, ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );
                    }
                    this.fontRenderer.drawString(
                            "EF/t: " + ModUtils.getString(this.container.base.perenergy),
                            this.guiLeft + 60, this.guiTop + 95, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    time = this.container.base.getTime(0);
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
                            this.guiLeft + 60, this.guiTop + 105, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );

                } else {

                    if (this.container.base.outputSlot[0].get().isEmpty() || this.container.base.outputSlot[0]
                            .get()
                            .getCount() < 64) {
                        int coef =
                                (int) (this.container.base.maxEnergySlots[0] / this.container.base
                                        .getOutput(0)
                                        .getRecipe().output.metadata.getDouble("energy"));
                        this.fontRenderer.drawString(input + this.container.base
                                        .getOutput(0)
                                        .getRecipe().input
                                        .getInputs()
                                        .get(0)
                                        .getInputs()
                                        .get(0)
                                        .getCount() * coef + "x" + this.container.base.inputSlot[0]
                                        .get()
                                        .getDisplayName(),
                                this.guiLeft + 60, this.guiTop + 55, ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );

                        this.fontRenderer.drawString(
                                output + this.container.base
                                        .getOutput(0)
                                        .getRecipe().output.items
                                        .get(0)
                                        .getCount() * coef + "x" + output1.getRecipe().output.items.get(0).getDisplayName(),
                                this.guiLeft + 60,
                                this.guiTop + 65,
                                ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );
                        this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(this.container.base.energySlots[0]) +
                                        " EF",
                                this.guiLeft + 60, this.guiTop + 75, ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );
                        if (this.container.base.getProgress(0) * 100 <= 100) {
                            this.fontRenderer.drawString(
                                    progress + (this.container.base.getProgress(0) <= 0.01 ? 0 :
                                            ModUtils.getString(this.container.base.getProgress(0) * 100)) + "%",
                                    this.guiLeft + 60, this.guiTop + 85, ModUtils.convertRGBcolorToInt(255, 255, 255)
                            );
                        }


                        this.fontRenderer.drawString(
                                "EF/t: " + ModUtils.getString(this.container.base.perenergy),
                                this.guiLeft + 60, this.guiTop + 95, ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );
                        double hours = 0;
                        double minutes = 0;
                        double seconds = 0;
                        time = this.container.base.getTime(0);


                        if (!time.isEmpty()) {
                            hours = time.get(0);
                            minutes = time.get(1);
                            seconds = time.get(2);
                        } else {
                            seconds = 1;
                        }
                        String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") : "";
                        String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") : "";
                        String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") : "";

                        this.fontRenderer.drawString(
                                Localization.translate("iu.timetoend") + time1 + time2 + time3,
                                this.guiLeft + 60, this.guiTop + 105, ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );


                    }
                }

            }
        } else if (container.base.maxAmount == 2) {
            this.bindTexture();
            for (int i = 0; i < container.base.maxAmount; i++) {
                chargeLevel = 20.0D * container.base.getProgress(i);
                if (chargeLevel > 0 && !this.container.base.inputSlot[i].isEmpty() && this.container.base.inputSlot[i].continue_proccess(
                        this.container.base.outputSlot[i])) {
                    this.bindTexture();
                    drawTexturedModalRect(this.guiLeft + 26 + i * 20, this.guiTop + 76, 44, 235, 14, (int) chargeLevel);
                    this.fontRenderer.drawString(
                            "EF/t: " + ModUtils.getString(this.container.base.perenergy),
                            this.guiLeft + 85, this.guiTop + 55, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    final List<Double> time = this.container.base.getTime(i);


                    if (!time.isEmpty()) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    } else {
                        seconds = 1;
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") : "";

                    this.fontRenderer.drawString(
                            Localization.translate("iu.timetoend") + time1 + time2 + time3,
                            this.guiLeft + 85, this.guiTop + 65 + i * 10, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                }
            }

        } else if (container.base.maxAmount == 4) {
            this.bindTexture();
            for (int i = 0; i < container.base.maxAmount; i++) {
                chargeLevel = 20.0D * container.base.getProgress(i);
                if (chargeLevel > 0 && !this.container.base.inputSlot[i].isEmpty() && this.container.base.inputSlot[i].continue_proccess(
                        this.container.base.outputSlot[i])) {
                    this.bindTexture();
                    drawTexturedModalRect(this.guiLeft + 10 + i * 19, this.guiTop + 76, 44, 235, 14, (int) chargeLevel);
                    this.fontRenderer.drawString(
                            "EF/t: " + ModUtils.getString(this.container.base.perenergy),
                            this.guiLeft + 100, this.guiTop + 55, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    final List<Double> time = this.container.base.getTime(i);


                    if (!time.isEmpty()) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    } else {
                        seconds = 1;
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") : "";

                    this.fontRenderer.drawString(
                            Localization.translate("iu.timetoend") + time1 + time2 + time3,
                            this.guiLeft + 100, this.guiTop + 65 + i * 10, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                }
            }

        }
    }


    public String getName() {
        return Localization.translate("blockMolecularTransformer.name");
    }

    public ResourceLocation getTexture() {

        if (container.base.maxAmount == 1) {
            if (this.container.base.redstoneMode == 1) {

                return new ResourceLocation(
                        Constants.MOD_ID,
                        "textures/gui/guimoleculartransformernew_chemical_green.png"
                );
            } else if (this.container.base.redstoneMode == 2) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew_gold.png");
            } else if (this.container.base.redstoneMode == 3) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew_red.png");
            } else if (this.container.base.redstoneMode == 4) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew_silver.png");
            } else if (this.container.base.redstoneMode == 5) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew_violet.png");
            } else if (this.container.base.redstoneMode == 6) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew_blue.png");
            } else if (this.container.base.redstoneMode == 7) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew_green.png");
            } else {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew.png");
            }
        } else if (container.base.maxAmount == 2) {
            if (this.container.base.redstoneMode == 1) {

                return new ResourceLocation(
                        Constants.MOD_ID,
                        "textures/gui/guidualmoleculartransformernew_chemical_green.png"
                );
            } else if (this.container.base.redstoneMode == 2) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew_gold.png");
            } else if (this.container.base.redstoneMode == 3) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew_red.png");
            } else if (this.container.base.redstoneMode == 4) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew_silver.png");
            } else if (this.container.base.redstoneMode == 5) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew_violet.png");
            } else if (this.container.base.redstoneMode == 6) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew_blue.png");
            } else if (this.container.base.redstoneMode == 7) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew_green.png");
            } else {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidualmoleculartransformernew.png");
            }
        } else {
            if (this.container.base.redstoneMode == 1) {

                return new ResourceLocation(
                        Constants.MOD_ID,
                        "textures/gui/guiquadmoleculartransformernew_chemical_green.png"
                );
            } else if (this.container.base.redstoneMode == 2) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew_gold.png");
            } else if (this.container.base.redstoneMode == 3) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew_red.png");
            } else if (this.container.base.redstoneMode == 4) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew_silver.png");
            } else if (this.container.base.redstoneMode == 5) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew_violet.png");
            } else if (this.container.base.redstoneMode == 6) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew_blue.png");
            } else if (this.container.base.redstoneMode == 7) {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew_green.png");
            } else {

                return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquadmoleculartransformernew.png");
            }
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        int dopX = container.base.maxAmount == 1 ? 0 : (container.base.maxAmount == 2 ? 25 : 30);
        if (x >= 205 + dopX && x <= 217 + dopX && y >= 25 && y <= 45) {
            new PacketUpdateServerTile(this.container.base, 0);
        }

        if (x >= 152 + dopX && x <= 163 + dopX && y >= 25 && y <= 45) {
            new PacketUpdateServerTile(this.container.base, -1);
        }
        if (x >= 22 && x <= 57 && y >= 30 && y <= 40) {
            new PacketUpdateServerTile(this.container.base, 1);
        }

    }

}
