package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gasvein.GasVeinSystem;
import com.denfop.api.gasvein.TypeGas;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.blocks.FluidName;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerEarthAnalyzer;
import com.denfop.container.ContainerGasWellAnalyzer;
import com.denfop.tiles.gaswell.TileEntityGasWellAnalyzer;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryAnalyzer;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;

public class GuiGasWellAnalyzer extends GuiIU<ContainerGasWellAnalyzer> {

    public GuiGasWellAnalyzer(ContainerGasWellAnalyzer guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGasWellAnalyzer) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGasWellAnalyzer) this.getEntityBlock()).work;
                    }
                })
        ));

    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        if (this.container.base.vein != null && !container.base.vein.isFind()) {
            if (this.container.base.progress < 1200) {
                this.fontRenderer.drawString(
                        (this.container.base.progress * 100 / 1200) + "%",
                        69,
                        34,
                        ModUtils.convertRGBcolorToInt(13, 229, 34)
                );


            }
        }else if (this.container.base.vein != null &&container.base.vein.isFind() && container.base.vein.getType() == TypeGas.NONE){
            this.fontRenderer.drawString(
                    Localization.translate("earth_quarry.error"),
                    69,
                    34,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        }else if (this.container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE){
            this.fontRenderer.drawSplitString(
                    Localization.translate("earth_quarry.send_work"),
                    69,
                    34,this.xSize - 69 - 5,
                    ModUtils.convertRGBcolorToInt(13, 229, 34)
            );
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    }

    @Override
    protected void drawBackgroundAndTitle(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(partialTicks, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
