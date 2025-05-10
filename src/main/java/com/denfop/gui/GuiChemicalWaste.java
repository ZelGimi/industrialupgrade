package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiElement;
import com.denfop.container.ContainerDefaultMultiElement;
import com.denfop.tiles.chemicalplant.TileEntityChemicalPlantWaste;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class GuiChemicalWaste<T extends ContainerDefaultMultiElement> extends GuiIU<ContainerDefaultMultiElement> {

    public GuiChemicalWaste(ContainerDefaultMultiElement guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addElement((new FluidItem(
                this,
                this.imageWidth / 2 - 10,
                20,
                ((TileEntityChemicalPlantWaste) guiContainer.base).getFluidTank().getFluid()
        ) {
            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = ((TileEntityChemicalPlantWaste) guiContainer.base).getFluidTank().getFluid();
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
                            mouseX+  fluidX,
                            mouseY+   fluidY + (45 - fluidHeight * (fs.getAmount() / 10000D)),
                            fluidWidth,
                            fluidHeight * (fs.getAmount() / 10000D),
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
            public void drawForeground(PoseStack poseStack,final int mouseX, final int mouseY) {
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
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        drawTexturedModalRect(poseStack, this.guiLeft + this.imageWidth / 2 - 10 - 4, guiTop + 20 - 4, 235,
                76, 20, 55
        );
        for (final GuiElement<?> element : this.elements) {
            element.drawBackground(poseStack, mouseX, mouseY);
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guichemicalplant.png");
    }

}
