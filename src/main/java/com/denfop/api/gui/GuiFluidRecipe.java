package com.denfop.api.gui;


import com.denfop.Localization;
import com.denfop.api.recipe.generators.FluidGenerator;
import com.denfop.api.recipe.generators.FluidGeneratorCore;
import com.denfop.api.recipe.generators.TypeGenerator;
import com.denfop.api.recipe.generators.TypeGenerators;
import com.denfop.gui.GuiCore;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class GuiFluidRecipe extends GuiElement<GuiFluidRecipe> {

    private final FluidStack fluid;
    private final FluidGenerator recipe;
    private final double energy;
    private final double maxEnergy;

    public GuiFluidRecipe(GuiCore<?> gui, int x, int y, int width, int height, TypeGenerator typeGenerator) {
        super(gui, x, y, width, height);
        this.recipe = FluidGeneratorCore.instance.fluidGeneratorMap.get(
                typeGenerator);
        int coef = 1000 / this.recipe.getAmount();
        this.fluid = new FluidStack(this.recipe.getFluid(), 1000);
        this.energy = this.recipe.getEnergy() * coef;
        this.maxEnergy = this.energy * 2;
    }

    public static GuiFluidRecipe createFluidSlot(GuiCore<?> gui, int x, int y, TypeGenerator typeGenerator) {
        return new GuiFluidRecipe(gui, x, y, 86, 18, typeGenerator);
    }

    @Override
    public void drawForeground(final int mouseX, final int mouseY) {
        if (this.recipe.getTypeGenerators() == TypeGenerators.SOURCE) {
            if (mouseX >= this.x && mouseX <= this.x + 18 && mouseY >= this.y && mouseY <= this.y + 18) {
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
            if (mouseX >= this.x + 47 && mouseX <= this.x + 38 + 47 && mouseY >= this.y + 3 && mouseY <= this.y + 15) {
                this.gui.drawTooltip(mouseX, mouseY, Collections.singletonList(ModUtils.getString(this.energy) + " EF"));
            }
        } else {
            if (mouseX >= this.x && mouseX <= this.x + 38 && mouseY >= this.y + 3 && mouseY <= this.y + 15) {
                this.gui.drawTooltip(mouseX, mouseY, Collections.singletonList(ModUtils.getString(this.energy) + " EF"));
            }
            if (mouseX >= this.x + 67 && mouseX <= this.x + 67 + 18 && mouseY >= this.y && mouseY <= this.y + 18) {
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

    }

    public void drawBackground(int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = fluid;
        if (this.recipe.getTypeGenerators() == TypeGenerators.SINK) {
            this.gui.drawTexturedRect(this.x, this.y, 86, 18, 140, 146);
        } else {
            this.gui.drawTexturedRect(this.x, this.y, 86, 18, 140, 166.0);

        }
        if (fs != null && fs.amount > 0) {
            int fluidX = this.x + 1;
            if (recipe.getTypeGenerators() == TypeGenerators.SINK) {
                fluidX += 67;
            }
            int fluidY = this.y + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString()) : null;
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
        if (this.energy != 0) {
            if (this.recipe.getTypeGenerators() == TypeGenerators.SINK) {
                this.gui.drawTexturedRect(this.x, this.y + 3, 38, 12, 69, 2);
            } else {
                this.gui.drawTexturedRect(this.x + 47, this.y + 2, 38, 12, 69, 2);

            }
        }
    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.fluid;
        if (fs != null && fs.amount > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(fluid.getLocalizedName(fs));
                ret.add("Amount: " + fs.amount + " " + Localization.translate("iu.generic.text.mb"));
                String state = fs.getFluid().isGaseous() ? "Gas" : "Liquid";
                ret.add("Type: " + state);
            } else {
                ret.add("Invalid FluidStack instance.");
            }
        } else {
            ret.add("No Fluid");
            ret.add("Amount: 0 " + Localization.translate("iu.generic.text.mb"));
            ret.add("Type: Not Available");
        }

        return ret;
    }

}
