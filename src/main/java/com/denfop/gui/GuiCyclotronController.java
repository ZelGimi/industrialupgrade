package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.FluidItem;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.container.ContainerCyclotronController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.cyclotron.TileEntityCyclotronController;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiCyclotronController extends GuiIU<ContainerCyclotronController> {

    private boolean hover = false;

    public GuiCyclotronController(ContainerCyclotronController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
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

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        if (container.base.progress > 0) {
            final double progress = Math.min(1, container.base.progress / 1000D);
            drawTexturedModalRect(this.guiLeft + 72, guiTop + 31, 232,
                    55, (int) (23 * progress), 16
            );
        }
        (new FluidItem(this, 11, 28, container.base.cryogen.getCryogenTank().getFluid()) {
            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = container.base.cryogen.getCryogenTank().getFluid();
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
        }).drawBackground(
                this.guiLeft,
                this.guiTop
        );
        (new FluidItem(this, 33, 28, container.base.coolant.getCoolantTank().getFluid()) {
            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = container.base.coolant.getCoolantTank().getFluid();
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
                            fluidHeight * fs.amount / 10000D,
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
        }).drawBackground(
                this.guiLeft,
                this.guiTop
        );
        new ItemStackImage(
                this,
                52,
                31,
                () -> this.container.base.bombardmentChamber.getInputSlot().get()
        ).drawBackground(this.guiLeft, this.guiTop);
        new ItemStackImage(this, 103, 31, () -> this.container.base.electrostaticDeflector.getOutputSlot().get()).drawBackground(
                this.guiLeft,
                this.guiTop
        );
        this.bindTexture();
        drawTexturedModalRect(this.guiLeft + 130, guiTop + 26, 216,
                5, (int) (35 * this.container.base.positrons.getPositrons().getFillRatio()), 8
        );
        drawTexturedModalRect(this.guiLeft + 130, guiTop + 44, 216,
                23, (int) (35 * this.container.base.quantum.getQuantum().getFillRatio()), 8
        );
        if (hover) {
            drawTexturedModalRect(this.guiLeft + 51, guiTop + 59, 237,
                    36, 18, 18
            );
        }
        final MachineRecipe output = this.container.base.bombardmentChamber.getRecipeOutput();
        if (output != null) {
            this.fontRenderer.drawString(
                    TextFormatting.GREEN + Localization.translate("chance") + output.getRecipe().output.metadata.getInteger(
                            "chance") + "%", this.guiLeft + 88,
                    guiTop + 63, ModUtils.convertRGBcolorToInt(217, 217, 217)
            );
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 10, 10);


    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        this.hover = par1 >= 51 && par1 <= 68 && par2 >= 59 && par2 <= 76;
        new AdvArea(this, 72, 31, 94, 47)
                .withTooltip((int) (Math.min(1, container.base.progress / 1000D) * 100) + "%")
                .drawForeground(
                        par1,
                        par2
                );

        new AdvArea(this, 51, 59, 68, 76).withTooltip(((TileEntityCyclotronController) this.container.base).work ?
                Localization.translate(
                        "turn_off") :
                Localization.translate("turn_on")).drawForeground(par1, par2);
        (new FluidItem(this, 11, 28, container.base.cryogen.getCryogenTank().getFluid()) {
            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = container.base.cryogen.getCryogenTank().getFluid();
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
                            fluidY,
                            fluidWidth,
                            fluidHeight,
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
        }).drawForeground(par1, par2);
        (new FluidItem(this, 33, 28, container.base.coolant.getCoolantTank().getFluid()) {
            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                bindCommonTexture();
                FluidStack fs = container.base.coolant.getCoolantTank().getFluid();
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
                            fluidY,
                            fluidWidth,
                            fluidHeight,
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
        }).drawForeground(par1, par2);
        new GuiComponent(this, 126, 22, EnumTypeComponent.POSITRONS,
                new Component<>(this.container.base.positrons.getPositrons())
        ).drawForeground(par1, par2);
        new GuiComponent(this, 126, 40, EnumTypeComponent.QUANTUM_ENERGY_WEIGHT,
                new Component<>(this.container.base.quantum.getQuantum())
        ).drawForeground(par1, par2);
        new ItemStackImage(this, 52,
                31, () -> this.container.base.bombardmentChamber.getInputSlot().get()
        ).drawForeground(
                par1,
                par2
        );
        new ItemStackImage(this, 103, 31, () -> this.container.base.electrostaticDeflector.getOutputSlot().get()).drawForeground(
                par1,
                par2
        );

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicyclotroncontroller.png");
    }

}
