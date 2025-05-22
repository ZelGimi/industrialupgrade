package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiElement;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantSeparate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class GuiChemicalSeparate extends GuiIU<ContainerDefaultMultiElement> {

    public GuiChemicalSeparate(ContainerDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addElement((new FluidItem(
                this,
                this.xSize / 2 - 10,
                20,
                ((TileEntityChemicalPlantSeparate) guiContainer.base).getFluidTank().getFluid()
        ) {
            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = ((TileEntityChemicalPlantSeparate) guiContainer.base).getFluidTank().getFluid();
                if (fs != null && fs.amount > 0) {
                    int fluidX = this.x + 1;
                    int fluidY = this.y + 1;
                    int fluidWidth = 10;
                    int fluidHeight = 45;
                    Fluid fluid = fs.getFluid();
                    TextureAtlasSprite sprite = fluid != null
                            ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                            : null;
                    int color = fluid != null ? fluid.getColor(fs) : -1;
                    bindBlockTexture();
                    this.gui.drawSprite(
                            fluidX,
                            fluidY + (45 - fluidHeight * (fs.amount / 10000D)),
                            fluidWidth,
                            fluidHeight * (fs.amount / 10000D),
                            sprite,
                            color,
                            1.0,
                            false,
                            false
                    );
                }
                Minecraft.getMinecraft().renderEngine.bindTexture(commonTexture1);
            }

            @Override
            public void drawForeground(final int mouseX, final int mouseY) {
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
        }));
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        bindTexture();
        GlStateManager.color(1, 1, 1, 1);
        drawTexturedModalRect(this.guiLeft + this.xSize / 2 - 10 - 4, guiTop + 20 - 4, 235,
                76, 20, 55
        );
        for (final GuiElement<?> element : this.elements) {
            element.drawBackground(mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guichemicalplant.png");
    }

}
