package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerBaseMolecular;
import com.denfop.utils.ModUtils;
import ic2.api.recipe.RecipeOutput;
import ic2.core.GuiIC2;
import ic2.core.IC2;
import ic2.core.init.Localization;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiMolecularTransformer extends GuiIC2<ContainerBaseMolecular> {

    public final ContainerBaseMolecular container;

    public GuiMolecularTransformer(ContainerBaseMolecular container1) {
        super(container1, 220, 193);
        this.container = container1;
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

        double chargeLevel = (15.0D * this.container.base.energy.getFillRatio());


        if (chargeLevel > 0 && !this.container.base.inputSlot.isEmpty() && Recipes.molecular.getOutputFor(this.container.base.inputSlot.get(
                0), false) != null) {
            RecipeOutput output1 = Recipes.molecular.getOutputFor(this.container.base.inputSlot.get(0), false);

            if (!this.container.base.queue) {
                this.bindTexture();
                drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 48, 221, 7, 10, (int) chargeLevel);
                this.bindTexture();
                this.fontRenderer.drawString(input + this.container.base.inputSlot.get().getDisplayName(),
                        this.guiLeft + 60, this.guiTop + 25, 4210752
                );

                this.fontRenderer.drawString(output + output1.items.get(0).getDisplayName(), this.guiLeft + 60,
                        this.guiTop + 25 + 11, 4210752
                );
                this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(output1.metadata.getDouble("energy")) + " EU",
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
                if (this.container.base.time.size() > 0) {
                    hours = this.container.base.time.get(0);
                    minutes = this.container.base.time.get(1);
                    seconds = this.container.base.time.get(2);
                }
                String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                this.fontRenderer.drawString(
                        Localization.translate("iu.timetoend") + time1 + time2 + time3,
                        this.guiLeft + 60, this.guiTop + 25 + 55, 4210752
                );

            } else {
                ItemStack output2;
                int size;
                for (int i = 0; ; i++) {
                    ItemStack stack = new ItemStack(
                            this.container.base.inputSlot.get().getItem(),
                            i,
                            this.container.base.inputSlot.get().getItemDamage()
                    );
                    if (Recipes.molecular.getOutputFor(stack, false) != null) {
                        output2 = Recipes.molecular.getOutputFor(stack, false).items.get(0);
                        size = i;
                        break;
                    }
                }
                int col = size;
                size = (int) Math.floor((float) this.container.base.inputSlot.get().getCount() / size);
                int size1 = this.container.base.outputSlot.get() != null
                        ? (64 - this.container.base.outputSlot.get().stackSize) / output2.stackSize
                        : 64 / output2.stackSize;

                size = Math.min(size1, size);
                size = Math.min(size, output2.getMaxStackSize());
                if (this.container.base.outputSlot.get() == null || this.container.base.outputSlot.get().getCount() < 64) {
                    this.bindTexture();
                    drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 48, 221, 7, 10, (int) chargeLevel);
                    this.bindTexture();
                    this.fontRenderer.drawString(input + col * size + "x" + this.container.base.inputSlot
                                    .get()
                                    .getDisplayName(),
                            this.guiLeft + 60, this.guiTop + 25, 4210752
                    );

                    this.fontRenderer.drawString(output + output2.stackSize * size + "x" + output1.items.get(0).getDisplayName(),
                            this.guiLeft + 60,
                            this.guiTop + 25 + 11,
                            4210752
                    );
                    this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(output1.metadata.getDouble("energy") * size) + " EU",
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
                    if (this.container.base.time.size() > 0) {
                        hours = this.container.base.time.get(0);
                        minutes = this.container.base.time.get(1);
                        seconds = this.container.base.time.get(2);
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
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 0);
        }

        if (x >= 7 && x <= 60 && y >= 3 && y <= 17) {
            IC2.network.get(false).initiateClientTileEntityEvent(this.container.base, 1);
        }

    }

}
