package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.widget.*;
import com.denfop.blockentity.cyclotron.BlockEntityCyclotronController;
import com.denfop.containermenu.ContainerMenuCyclotronController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenCyclotronController<T extends ContainerMenuCyclotronController> extends ScreenMain<ContainerMenuCyclotronController> {

    private boolean hover = false;

    public ScreenCyclotronController(ContainerMenuCyclotronController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        boolean start = x >= 51 && x <= 68 && y >= 59 && y <= 76;
        if (start) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.cyclotron.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 6; i++) {
                compatibleUpgrades.add(Localization.translate("iu.cyclotron.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 120, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        if (container.base.progress > 0) {
            final double progress = Math.min(1, container.base.progress / 1000D);
            drawTexturedModalRect(poseStack, this.guiLeft + 72, guiTop + 31, 232,
                    55, (int) (23 * progress), 16
            );
        }
        (new FluidDefaultWidget(this, 11, 28, container.base.cryogen.getCryogenTank().getFluid()) {
            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = container.base.cryogen.getCryogenTank().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x + 1;
                    int fluidY = this.y + 1;
                    int fluidWidth = 10;
                    int fluidHeight = 45;
                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    this.gui.drawSprite(poseStack,
                            mouseX + fluidX,
                            mouseY + fluidY + (45 - fluidHeight * (fs.getAmount() / 10000D)),
                            fluidWidth,
                            fluidHeight * fs.getAmount() / 10000D,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
                bindTexture(commonTexture1);
            }

            @Override
            public void drawForeground(PoseStack poseStack, final int mouseX, final int mouseY) {
                if (mouseX >= this.x - 4 && mouseX <= this.x + 15 && mouseY >= this.y - 4 && mouseY <= this.y + 51) {
                    List<String> lines = this.getToolTip();
                    if (this.getTooltipProvider() != null) {
                        String tooltip = this.getTooltipProvider().get();
                        if (tooltip != null && !tooltip.isEmpty()) {
                            addLines(lines, tooltip);
                        }
                    }

                    if (!lines.isEmpty()) {
                        this.gui.drawTooltip(mouseX, mouseY, lines);
                    }
                }
            }
        }).drawBackground(poseStack,
                this.guiLeft,
                this.guiTop
        );
        (new FluidDefaultWidget(this, 33, 28, container.base.coolant.getCoolantTank().getFluid()) {
            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = container.base.coolant.getCoolantTank().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x + 1;
                    int fluidY = this.y + 1;
                    int fluidWidth = 10;
                    int fluidHeight = 45;
                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    this.gui.drawSprite(poseStack,
                            mouseX + fluidX,
                            mouseY + fluidY + (45 - fluidHeight * (fs.getAmount() / 10000D)),
                            fluidWidth,
                            fluidHeight * fs.getAmount() / 10000D,
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
                bindTexture(commonTexture1);
            }

            @Override
            public void drawForeground(PoseStack poseStack1, final int mouseX, final int mouseY) {
                if (mouseX >= this.x - 4 && mouseX <= this.x + 15 && mouseY >= this.y - 4 && mouseY <= this.y + 51) {
                    List<String> lines = this.getToolTip();
                    if (this.getTooltipProvider() != null) {
                        String tooltip = this.getTooltipProvider().get();
                        if (tooltip != null && !tooltip.isEmpty()) {
                            addLines(lines, tooltip);
                        }
                    }

                    if (!lines.isEmpty()) {
                        this.gui.drawTooltip(mouseX, mouseY, lines);
                    }
                }
            }
        }).drawBackground(poseStack,
                this.guiLeft,
                this.guiTop
        );
        new ItemStackWidget(
                this,
                52,
                31,
                () -> this.container.base.bombardmentChamber.getInputSlot().get(0)
        ).drawBackground(poseStack, this.guiLeft, this.guiTop);
        new ItemStackWidget(this, 103, 31, () -> this.container.base.electrostaticDeflector.getOutputSlot().get(0)).drawBackground(
                poseStack, this.guiLeft,
                this.guiTop
        );
        this.bindTexture();
        drawTexturedModalRect(poseStack, this.guiLeft + 130, guiTop + 26, 216,
                5, (int) (35 * this.container.base.positrons.getPositrons().getFillRatio()), 8
        );
        drawTexturedModalRect(poseStack, this.guiLeft + 130, guiTop + 44, 216,
                23, (int) (35 * this.container.base.quantum.getQuantum().getFillRatio()), 8
        );
        if (hover) {
            drawTexturedModalRect(poseStack, this.guiLeft + 51, guiTop + 59, 237,
                    36, 18, 18
            );
        }
        final MachineRecipe output = this.container.base.bombardmentChamber.getRecipeOutput();
        if (output != null) {
            this.font.draw(poseStack,
                    ChatFormatting.GREEN + Localization.translate("chance") + output.getRecipe().output.metadata.getInt(
                            "chance") + "%", this.guiLeft + 88,
                    guiTop + 63, ModUtils.convertRGBcolorToInt(217, 217, 217)
            );
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, 10, 10);


    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.hover = par1 >= 51 && par1 <= 68 && par2 >= 59 && par2 <= 76;
        new AdvancedTooltipWidget(this, 72, 31, 94, 47)
                .withTooltip((int) (Math.min(1, container.base.progress / 1000D) * 100) + "%")
                .drawForeground(
                        poseStack, par1,
                        par2
                );

        new AdvancedTooltipWidget(this, 51, 59, 68, 76).withTooltip(((BlockEntityCyclotronController) this.container.base).work ?
                Localization.translate(
                        "turn_off") :
                Localization.translate("turn_on")).drawForeground(poseStack, par1, par2);
        (new FluidDefaultWidget(this, 11, 28, container.base.cryogen.getCryogenTank().getFluid()) {

            @Override
            public void drawForeground(PoseStack poseStack, final int mouseX, final int mouseY) {
                if (mouseX >= this.x - 4 && mouseX <= this.x + 15 && mouseY >= this.y - 4 && mouseY <= this.y + 51) {
                    List<String> lines = this.getToolTip();
                    if (this.getTooltipProvider() != null) {
                        String tooltip = this.getTooltipProvider().get();
                        if (tooltip != null && !tooltip.isEmpty()) {
                            addLines(lines, tooltip);
                        }
                    }

                    if (!lines.isEmpty()) {
                        this.gui.drawTooltip(mouseX, mouseY, lines);
                    }
                }
            }
        }).drawForeground(poseStack, par1, par2);
        (new FluidDefaultWidget(this, 33, 28, container.base.coolant.getCoolantTank().getFluid()) {

            @Override
            public void drawForeground(PoseStack poseStack, final int mouseX, final int mouseY) {
                if (mouseX >= this.x - 4 && mouseX <= this.x + 15 && mouseY >= this.y - 4 && mouseY <= this.y + 51) {
                    List<String> lines = this.getToolTip();
                    if (this.getTooltipProvider() != null) {
                        String tooltip = this.getTooltipProvider().get();
                        if (tooltip != null && !tooltip.isEmpty()) {
                            addLines(lines, tooltip);
                        }
                    }

                    if (!lines.isEmpty()) {
                        this.gui.drawTooltip(mouseX, mouseY, lines);
                    }
                }
            }
        }).drawForeground(poseStack, par1, par2);
        new ScreenWidget(this, 126, 22, EnumTypeComponent.POSITRONS,
                new WidgetDefault<>(this.container.base.positrons.getPositrons())
        ).drawForeground(poseStack, par1, par2);
        new ScreenWidget(this, 126, 40, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new WidgetDefault<>(this.container.base.quantum.getQuantum())
        ).drawForeground(poseStack, par1, par2);
        new ItemStackWidget(this, 52,
                31, () -> this.container.base.bombardmentChamber.getInputSlot().get(0)
        ).drawForeground(
                poseStack, par1,
                par2
        );
        new ItemStackWidget(this, 103, 31, () -> this.container.base.electrostaticDeflector.getOutputSlot().get(0)).drawForeground(
                poseStack, par1,
                par2
        );

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotroncontroller.png");
    }

}
