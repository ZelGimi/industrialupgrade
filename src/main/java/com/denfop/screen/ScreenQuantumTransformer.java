package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.containermenu.ContainerMenuQuantumMolecular;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ScreenQuantumTransformer<T extends ContainerMenuQuantumMolecular> extends ScreenIndustrialUpgrade<ContainerMenuQuantumMolecular> {

    public final ContainerMenuQuantumMolecular container;

    public ScreenQuantumTransformer(ContainerMenuQuantumMolecular container1) {
        super(container1, 238, 225);
        this.container = container1;
    }

    public static int floor_double(double p_76128_0_) {
        int i = (int) p_76128_0_;
        return p_76128_0_ < (double) i ? i - 1 : i;
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        new AdvancedTooltipWidget(this, 21, 29, 58, 41).withTooltip(Localization.translate("iu.molecular_info") + "\n" + Localization.translate(
                "iu.molecular_info3") + " " + (this.container.base.queue ? "x64" : "x1")).drawForeground(poseStack, mouseX, mouseY);
        new AdvancedTooltipWidget(this, 165, 22, 232, 47)
                .withTooltip(Localization.translate("iu.molecular_info1") + "\n" + Localization.translate(
                        "iu.molecular_info2"))
                .drawForeground(
                        poseStack, mouseX,
                        mouseY
                );

    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack, (int) ((this.imageWidth / 2)), 5, name, ModUtils.convertRGBcolorToInt(255, 255, 255), false);
    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        this.bindTexture();
        String input = Localization.translate("gui.MolecularTransformer.input") + ": ";
        String output = Localization.translate("gui.MolecularTransformer.output") + ": ";
        String energyPerOperation = Localization.translate("gui.MolecularTransformer.energyPerOperation") + ": ";
        String progress = Localization.translate("gui.MolecularTransformer.progress") + ": ";

        this.bindTexture();
        double chargeLevel = (20.0D * this.container.base.getProgress());
        if (!this.container.base.queue) {
            drawTexturedModalRect(poseStack, this.guiLeft + 22, this.guiTop + 30, 26, 245, 16, 11);

        } else {
            drawTexturedModalRect(poseStack, this.guiLeft + 42, this.guiTop + 30, 26, 245, 16, 11);

        }
        final MachineRecipe output3 = this.container.base.output;
        if (chargeLevel > 0 && !this.container.base.inputSlot.isEmpty() && output3 != null && this.container.base.outputSlot.canAdd(
                output3.getRecipe().getOutput().items)) {
            if (!this.container.base.queue) {
                bindTexture(getTexture());
                drawTexturedModalRect(poseStack, this.guiLeft + 30, this.guiTop + 75, 43, 237, 14, (int) chargeLevel);
                bindTexture(getTexture());

                draw(poseStack, input + this.container.base.inputSlot.get(0).getDisplayName().getString(),
                        this.guiLeft + 73, this.guiTop + 55, ModUtils.convertRGBcolorToInt(255, 255, 255)
                );
                draw(poseStack, input + this.container.base.inputSlot.get(1).getDisplayName().getString(),
                        this.guiLeft + 73, this.guiTop + 65, ModUtils.convertRGBcolorToInt(255, 255, 255)
                );

                draw(poseStack, output + output3.getRecipe().output.items.get(0).getDisplayName().getString(), this.guiLeft + 73,
                        this.guiTop + 75, ModUtils.convertRGBcolorToInt(255, 255, 255)
                );
                draw(poseStack, energyPerOperation + ModUtils.getString(output3.getRecipe().output.metadata.getDouble(
                                "energy")) +
                                " QE",
                        this.guiLeft + 73, this.guiTop + 85, ModUtils.convertRGBcolorToInt(255, 255, 255)
                );
                if (this.container.base.getProgress() * 100 <= 100) {
                    draw(poseStack,
                            progress + floor_double(this.container.base.getProgress() * 100) + "%",
                            this.guiLeft + 73, this.guiTop + 95, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                }
                draw(poseStack,
                        "QE/t: " + ModUtils.getString(this.container.base.differenceenergy),
                        this.guiLeft + 73, this.guiTop + 105, ModUtils.convertRGBcolorToInt(255, 255, 255)
                );

            } else {
                int size;
                int size2;
                int col;
                int col1;
                ItemStack output2 = this.container.base.output.getRecipe().output.items.get(0);
                size = this.container.base.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();
                size2 = this.container.base.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                col = size;
                col1 = size2;
                size = (int) Math.floor((float) this.container.base.inputSlot.get(0).getCount() / size);
                size2 = (int) Math.floor((float) this.container.base.inputSlot.get(1).getCount() / size2);
                size = Math.min(size, size2);

                int size1 = !this.container.base.outputSlot.get(0).isEmpty()
                        ? 64 - this.container.base.outputSlot.get(0).getCount()
                        : 64;
                size = Math.min(size1, size);
                size = Math.min(size, output2.getMaxStackSize());
                if (this.container.base.outputSlot.get(0).isEmpty() || this.container.base.outputSlot.get(0).getCount() < 64) {
                    bindTexture(getTexture());
                    drawTexturedModalRect(poseStack, this.guiLeft + 30, this.guiTop + 75, 43, 237, 14, (int) chargeLevel);
                    bindTexture(getTexture());
                    draw(poseStack, input + col * size + "x" + this.container.base.inputSlot.get(0).getDisplayName().getString(),
                            this.guiLeft + 73, this.guiTop + 55, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );

                    draw(poseStack, input + col1 * size + "x" + this.container.base.inputSlot
                                    .get(1)
                                    .getDisplayName().getString(),
                            this.guiLeft + 73, this.guiTop + 65, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );

                    draw(poseStack, output + output2.getCount() * size + "x" + output3.getRecipe().output.items
                                    .get(0)
                                    .getDisplayName().getString()
                            , this.guiLeft + 73,
                            this.guiTop + 75, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    draw(poseStack, energyPerOperation + ModUtils.getString(output3.getRecipe().output.metadata.getDouble(
                                    "energy") * size) + " QE",
                            this.guiLeft + 73, this.guiTop + 85, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );
                    if (this.container.base.getProgress() * 100 <= 100) {
                        draw(poseStack,
                                progress + floor_double(this.container.base.getProgress() * 100) + "%",
                                this.guiLeft + 73, this.guiTop + 95, ModUtils.convertRGBcolorToInt(255, 255, 255)
                        );
                    }


                    draw(poseStack,
                            "QE/t: " + ModUtils.getString(this.container.base.differenceenergy),
                            this.guiLeft + 73, this.guiTop + 105, ModUtils.convertRGBcolorToInt(255, 255, 255)
                    );

                }
            }

        }
    }


    public ResourceLocation getTexture() {


        if (this.container.base.redstoneMode == 1) {

            return ResourceLocation.tryBuild(
                    Constants.TEXTURES,
                    "textures/gui/guiDoubleMolecularTransformerNew_chemical_green.png".toLowerCase()
            );
        } else if (this.container.base.redstoneMode == 2) {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_gold.png".toLowerCase());
        } else if (this.container.base.redstoneMode == 3) {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_red.png".toLowerCase());
        } else if (this.container.base.redstoneMode == 4) {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_silver.png".toLowerCase());
        } else if (this.container.base.redstoneMode == 5) {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_violet.png".toLowerCase());
        } else if (this.container.base.redstoneMode == 6) {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_blue.png".toLowerCase());
        } else if (this.container.base.redstoneMode == 7) {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_green.png".toLowerCase());
        } else {

            return ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew.png".toLowerCase());
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 22 && x <= 57 && y >= 30 && y <= 40) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
        if (x >= 152 && x <= 163 && y >= 25 && y <= 45) {
            new PacketUpdateServerTile(this.container.base, -1);
        }
        if (x >= 22 && x <= 57 && y >= 30 && y <= 40) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
    }

}
