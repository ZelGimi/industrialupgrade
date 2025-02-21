package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerBlastFurnace;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiBlastFurnace extends GuiIU<ContainerBlastFurnace> {

    public final ContainerBlastFurnace container;
    public boolean highlightedMinus = false;
    public boolean highlightedPlus = false;

    public GuiBlastFurnace(ContainerBlastFurnace container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.ySize = 182;


        this.addComponent(new GuiComponent(this, 142, 74, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addElement(new AdvArea(this, 137, 23, 150, 36).withTooltip("+1"));
        this.addElement(new AdvArea(this, 63, 23, 76, 36).withTooltip("-1"));
        this.addElement(new AdvArea(this, 79, 76, 134, 89) {
            @Override
            protected List<String> getToolTip() {
                if (container.base.heat == null) {
                    return Collections.singletonList("0/0 °C");
                }

                final HeatComponent component = container.base.heat;
                List<String> stringList = new ArrayList<>();
                stringList.add(ModUtils.getString(component
                        .getEnergy()) + "°C" + "/" + ModUtils.getString(component.getCapacity()) + "°C");
                if (component.need) {
                    stringList.add(Localization.translate("iu.need_heat"));
                }
                return stringList;
            }
        });


        this.addElement(new AdvArea(this, 79, 25, 134, 34) {
            @Override
            protected List<String> getToolTip() {
                String temp =
                        ModUtils.getString(container.base.bar * 100000D) + " Pa";
                return Collections.singletonList(temp);
            }
        });

        this.addElement(new AdvArea(this, 88, 46, 125, 57) {
                            @Override
                            protected List<String> getToolTip() {
                                return Collections.singletonList(Localization.translate("gui.MolecularTransformer.progress") + ": " + (int) (Math.min(
                                        container.base.getProgress() / 3600D,
                                        1D
                                ) * 100) + "%");
                            }
                        }
        );


        this.addElement(new TankGauge(this, 6, 18, 20, 55, container.base.tank1, TankGauge.TankGuiStyle.Normal) {
            @Override
            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                if (container.base.tank1 == null) {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                } else {
                    FluidStack fs = this.tank.getFluid();
                    if (fs != null && fs.amount > 0) {
                        Fluid fluid = fs.getFluid();
                        if (fluid != null) {
                            ret.add(fluid.getLocalizedName(fs) + ": " + fs.amount + " " + Localization.translate(
                                    "iu.generic.text.mb"));
                        } else {
                            ret.add("invalid fluid stack");
                        }
                    } else {
                        ret.add(Localization.translate("iu.generic.text.empty"));
                    }
                }
                return ret;
            }

            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                if (container.base.tank1 == null) {
                    return;
                }
                FluidStack fs = container.base.tank1.getFluid();
                if (fs != null && fs.amount > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    if (this.getStyle().withBorder) {
                        fluidX += 4;
                        fluidY += 4;
                        fluidWidth = 12;
                        fluidHeight = 47;
                    }

                    Fluid fluid = fs.getFluid();
                    TextureAtlasSprite sprite = fluid != null
                            ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                            : null;
                    int color = fluid != null ? fluid.getColor(fs) : -1;
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.amount / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    bindBlockTexture();
                    this.gui.drawSprite(
                            fluidX,
                            (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    if (this.getStyle().withGauge) {
                        bindCommonTexture();
                        int gaugeX = this.x;
                        int gaugeY = this.y;
                        if (!this.getStyle().withBorder) {
                            gaugeX -= 4;
                            gaugeY -= 4;
                        }

                        this.gui.drawTexturedRect(gaugeX, gaugeY, 20.0D, 55.0D, 38.0D, 100.0D, this.getStyle().mirrorGauge);
                    }
                }
            }
        });

        this.addElement(new TankGauge(this, 30, 18, 20, 55, container.base.tank, TankGauge.TankGuiStyle.Normal) {
            @Override
            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                if (container.base.tank == null) {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                } else {
                    FluidStack fs = container.base.tank.getFluid();
                    if (fs != null && fs.amount > 0) {
                        Fluid fluid = fs.getFluid();
                        if (fluid != null) {
                            ret.add(fluid.getLocalizedName(fs) + ": " + fs.amount + " " + Localization.translate(
                                    "iu.generic.text.mb"));
                        } else {
                            ret.add("invalid fluid stack");
                        }
                    } else {
                        ret.add(Localization.translate("iu.generic.text.empty"));
                    }
                }
                return ret;
            }

            @Override
            public void drawBackground(final int mouseX, final int mouseY) {
                if (container.base.tank == null) {
                    return;
                }
                FluidStack fs = this.tank.getFluid();
                if (fs != null && fs.amount > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    if (this.getStyle().withBorder) {
                        fluidX += 4;
                        fluidY += 4;
                        fluidWidth = 12;
                        fluidHeight = 47;
                    }

                    Fluid fluid = fs.getFluid();
                    TextureAtlasSprite sprite = fluid != null
                            ? getBlockTextureMap().getAtlasSprite(fluid.getStill(fs).toString())
                            : null;
                    int color = fluid != null ? fluid.getColor(fs) : -1;
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.amount / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    bindBlockTexture();
                    this.gui.drawSprite(
                            fluidX,
                            (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    if (this.getStyle().withGauge) {
                        bindCommonTexture();
                        int gaugeX = this.x;
                        int gaugeY = this.y;
                        if (!this.getStyle().withBorder) {
                            gaugeX -= 4;
                            gaugeY -= 4;
                        }

                        this.gui.drawTexturedRect(gaugeX, gaugeY, 20.0D, 55.0D, 38.0D, 100.0D, this.getStyle().mirrorGauge);
                    }
                }
            }
        });
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.blast_furnace_recipe.info"));
            List<String> compatibleUpgrades = ListInformationUtils.blast_furnace;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        handleUpgradeTooltip(par1, par2);
        highlightedMinus = false;
        highlightedPlus = false;

        if (par1 >= 63 && par2 >= 23 && par1 <= 76 && par2 <= 36) {
            highlightedMinus = true;
        }
        if (par1 >= 137 && par2 >= 23 && par1 <= 150 && par2 <= 36) {
            highlightedPlus = true;
        }
    }

    protected void mouseClicked(int i, int j, int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 63 && x <= 76 && y >= 23 && y <= 36) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
        if (x >= 137 && x <= 150 && y >= 23 && y <= 36) {
            new PacketUpdateServerTile(this.container.base, 0);
        }

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(this.xSize / 2 + 20, 6, name, 4210752, false);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());

        int progress = (int) (38.0F * this.container.base.getProgress() / 3600D);

        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        if (highlightedMinus) {
            drawTexturedModalRect(xoffset + 63, yoffset + 23, 192, 3, 14, 14);

        }
        if (highlightedPlus) {
            drawTexturedModalRect(xoffset + 137, yoffset + 23, 177, 3, 14, 14);

        }
        if (progress > 0) {
            drawTexturedModalRect(xoffset + 88, yoffset + 46, 177, 19, progress, 11);
        }
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect(xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        this.mc.getTextureManager().bindTexture(getTexture());

        int bar = (int) ((Math.min(this.container.base.bar * 1D, 5D) / 5D) * 50D);
        if (bar > 0) {
            drawTexturedModalRect(this.guiLeft + 82, this.guiTop + 28, 181, 53, bar, 5);
        }
        if (container.base.heat != null) {
            progress = (int) (50.0F * container.base.heat.getFillRatio());
            drawTexturedModalRect(this.guiLeft + 82, this.guiTop + 79, 180, 34, progress, 8);

        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guiblastfurnace.png");
    }

}
