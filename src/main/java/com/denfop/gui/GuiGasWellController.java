package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gasvein.TypeGas;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.FluidName;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGasWellController;
import com.denfop.tiles.gaswell.TileEntityGasWellController;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.denfop.api.gui.GuiElement.bindBlockTexture;
import static com.denfop.api.gui.GuiElement.getBlockTextureMap;

public class GuiGasWellController extends GuiIU<ContainerGasWellController> {

    public GuiGasWellController(ContainerGasWellController guiContainer) {
        super(guiContainer);
        this.componentList.add(new GuiComponent(this, 30, 30, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGasWellController) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGasWellController) this.getEntityBlock()).work;
                    }
                })
        ));
        this.componentList.add(new GuiComponent(this, 70, 32, EnumTypeComponent.FLUIDS_SLOT,
                new Component<>(new ComponentEmpty())
        ));
        this.componentList.add(new GuiComponent(this, 90, 34, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ));
        this.componentList.add(new GuiComponent(this, 60, 58, EnumTypeComponent.ENERGY_WEIGHT_1,
                new Component<>(guiContainer.base.getEnergy())
        ));
        this.elements.add(TankGauge.createNormal(this, 110, 15, guiContainer.base.tank.getTank()));
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;

    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("quarry.guide.gas_well"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                compatibleUpgrades.add(Localization.translate("quarry.guide.gas_well" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 60, mouseY, text);
        }
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        if (container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE) {
            String text = "";
            if (container.base.vein.getType() == TypeGas.IODINE) {
                text = Localization.translate(FluidName.fluidiodine.getInstance().getUnlocalizedName());
            }


            if (container.base.vein.getType() == TypeGas.BROMIDE) {
                text = Localization.translate(FluidName.fluidbromine.getInstance().getUnlocalizedName());
            }

            if (container.base.vein.getType() == TypeGas.CHLORINE) {
                text = Localization.translate(FluidName.fluidchlorum.getInstance().getUnlocalizedName());
            }
            if (container.base.vein.getType() == TypeGas.FLORINE) {
                text = Localization.translate(FluidName.fluidfluor.getInstance().getUnlocalizedName());
            }

            new Area(this, 70, 32, 18, 18)
                    .withTooltip((text + " " + container.base.vein.getCol() + "/" + container.base.vein.getMaxCol() + "mb"))
                    .drawForeground(
                            par1,
                            par2
                    );


        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        if (container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE) {

            FluidStack fs;
            switch (container.base.vein.getType()) {
                case CHLORINE:
                    fs = new FluidStack(FluidName.fluidchlorum.getInstance(), container.base.vein.getCol());
                    break;
                case BROMIDE:
                    fs = new FluidStack(FluidName.fluidbromine.getInstance(), container.base.vein.getCol());
                    break;
                case FLORINE:
                    fs = new FluidStack(FluidName.fluidfluor.getInstance(), container.base.vein.getCol());
                    break;
                default:
                    fs = new FluidStack(FluidName.fluidiodine.getInstance(), container.base.vein.getCol());
                    break;
            }
            int fluidX = 70 + 1;
            int fluidY = 32 + 1;
            int fluidWidth = 16;
            int fluidHeight = 16;
            Fluid fluid = fs.getFluid();
            TextureAtlasSprite sprite = fluid != null
                    ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                    : null;
            int color = fluid != null ? fluid.getColor(fs) : -1;
            bindBlockTexture();
            this.drawSprite(
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
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 10, 10);
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
