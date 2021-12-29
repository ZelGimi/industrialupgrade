package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.Recipes;
import com.denfop.container.ContainerBaseDoubleMolecular;
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
public class GuiDoubleMolecularTransformer extends GuiIC2<ContainerBaseDoubleMolecular> {

    public final ContainerBaseDoubleMolecular container;

    public GuiDoubleMolecularTransformer(ContainerBaseDoubleMolecular container1) {
        super(container1, 220, 193);
        this.container = container1;
    }

    public static int floor_double(double p_76128_0_) {
        int i = (int) p_76128_0_;
        return p_76128_0_ < (double) i ? i - 1 : i;
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

        RecipeOutput output1 = Recipes.doublemolecular.getOutputFor(
                this.container.base.inputSlot.get(0),
                this.container.base.inputSlot.get(1),
                false,
                false
        );
        if (chargeLevel > 0 && !this.container.base.inputSlot.isEmpty() && Recipes.doublemolecular.getOutputFor(this.container.base.inputSlot.get(
                0), this.container.base.inputSlot.get(1), false, false) != null) {
            if (!this.container.base.queue) {
                this.mc.getTextureManager().bindTexture(getTexture());
                drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 48, 221, 7, 10, (int) chargeLevel);
                this.mc.getTextureManager().bindTexture(getTexture());

                this.fontRenderer.drawString(input + this.container.base.inputSlot.get().getDisplayName(),
                        this.guiLeft + 60, this.guiTop + 25, 4210752
                );
                this.fontRenderer.drawString(input + this.container.base.inputSlot.get(1).getDisplayName(),
                        this.guiLeft + 60, this.guiTop + 36, 4210752
                );

                this.fontRenderer.drawString(output + output1.items.get(0).getDisplayName(), this.guiLeft + 60,
                        this.guiTop + 47, 4210752
                );
                this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(output1.metadata.getDouble("energy")) + " EU",
                        this.guiLeft + 60, this.guiTop + 58, 4210752
                );
                if (this.container.base.getProgress() * 100 <= 100) {
                    this.fontRenderer.drawString(
                            progress + floor_double(this.container.base.getProgress() * 100) + "%",
                            this.guiLeft + 60, this.guiTop + 69, 4210752
                    );
                }
                if (this.container.base.getProgress() * 100 > 100) {
                    this.fontRenderer.drawString(
                            progress + floor_double(100) + "%",
                            this.guiLeft + 60, this.guiTop + 69, 4210752
                    );
                }
                this.fontRenderer.drawString(
                        "EU/t: " + ModUtils.getString(this.container.base.differenceenergy),
                        this.guiLeft + 60, this.guiTop + 80, 4210752
                );

            } else {
                ItemStack output2 = null;
                int size = 0;
                int size2 = 0;
                int col = 0;
                int col1 = 0;
                boolean getrecipe = false;
                for (int i = 0; !getrecipe; i++) {
                    for (int j = 0; j < 4; j++) {
                        ItemStack stack = new ItemStack(
                                this.container.base.inputSlot.get(0).getItem(),
                                i,
                                this.container.base.inputSlot.get().getItemDamage()
                        );
                        ItemStack stack1 = new ItemStack(
                                this.container.base.inputSlot.get(1).getItem(),
                                j,
                                this.container.base.inputSlot.get(1).getItemDamage()
                        );

                        if (Recipes.doublemolecular.getOutputFor(stack, stack1, false, false) != null) {
                            size = i;
                            size2 = j;
                            col = i;
                            col1 = j;
                            getrecipe = true;
                            output2 = Recipes.doublemolecular.getOutputFor(stack, stack1, false, false).items.get(0);
                            break;

                        }
                    }
                }

                size = (int) Math.floor((float) this.container.base.inputSlot.get().stackSize / size);
                size2 = (int) Math.floor((float) this.container.base.inputSlot.get(1).stackSize / size2);
                size = Math.min(size, size2);

                int size1 = this.container.base.outputSlot.get() != null
                        ? 64 - this.container.base.outputSlot.get().stackSize
                        : 64;
                size = Math.min(size1, size);
                size = Math.min(size, output2.getMaxStackSize());
                if (this.container.base.outputSlot.get() == null || this.container.base.outputSlot.get().stackSize < 64) {
                    this.mc.getTextureManager().bindTexture(getTexture());
                    drawTexturedModalRect(this.guiLeft + 23, this.guiTop + 48, 221, 7, 10, (int) chargeLevel);
                    this.mc.getTextureManager().bindTexture(getTexture());
                    this.fontRenderer.drawString(input + col * size + "x" + this.container.base.inputSlot.get().getDisplayName(),
                            this.guiLeft + 60, this.guiTop + 25, 4210752
                    );

                    this.fontRenderer.drawString(input + col1 * size + "x" + this.container.base.inputSlot
                                    .get(1)
                                    .getDisplayName(),
                            this.guiLeft + 60, this.guiTop + 36, 4210752
                    );

                    this.fontRenderer.drawString(output + output2.stackSize * size + "x" + output1.items.get(0).getDisplayName()
                            , this.guiLeft + 60,
                            this.guiTop + 47, 4210752
                    );
                    this.fontRenderer.drawString(energyPerOperation + ModUtils.getString(output1.metadata.getDouble("energy") * size) + " EU",
                            this.guiLeft + 60, this.guiTop + 58, 4210752
                    );
                    if (this.container.base.getProgress() * 100 <= 100) {
                        this.fontRenderer.drawString(
                                progress + floor_double(this.container.base.getProgress() * 100) + "%",
                                this.guiLeft + 60, this.guiTop + 69, 4210752
                        );
                    }
                    if (this.container.base.getProgress() * 100 > 100) {
                        this.fontRenderer.drawString(
                                progress + floor_double(100) + "%",
                                this.guiLeft + 60, this.guiTop + 69, 4210752
                        );
                    }

                    this.fontRenderer.drawString(
                            "EU/t: " + ModUtils.getString(this.container.base.differenceenergy),
                            this.guiLeft + 60, this.guiTop + 80, 4210752
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
                    Constants.TEXTURES,
                    "textures/gui/guiDoubleMolecularTransformerNew_chemical_green.png"
            );
        } else if (this.container.base.redstoneMode == 2) {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_gold.png");
        } else if (this.container.base.redstoneMode == 3) {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_red.png");
        } else if (this.container.base.redstoneMode == 4) {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_silver.png");
        } else if (this.container.base.redstoneMode == 5) {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_violet.png");
        } else if (this.container.base.redstoneMode == 6) {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_blue.png");
        } else if (this.container.base.redstoneMode == 7) {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew_green.png");
        } else {

            return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiDoubleMolecularTransformerNew.png");
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
