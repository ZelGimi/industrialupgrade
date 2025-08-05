package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.FluidName;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.HeatComponent;
import com.denfop.container.ContainerAdvCokeOven;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.ListInformationUtils;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiAdvCokeOven<T extends ContainerAdvCokeOven> extends GuiIU<ContainerAdvCokeOven> {

    public final ContainerAdvCokeOven container;
    public boolean highlightedMinus = false;
    public boolean highlightedPlus = false;

    public GuiAdvCokeOven(ContainerAdvCokeOven container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.imageHeight = 182;


        this.addComponent(new GuiComponent(this, 155, 44, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addElement(new AdvArea(this, 108, 23, 150, 36).withTooltip("+1"));
        this.addElement(new AdvArea(this, 34, 23, 47, 36).withTooltip("-1"));
        this.addElement(new AdvArea(this, 63, 76, 118, 89) {
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


        this.addElement(new AdvArea(this, 50, 25, 105, 34) {
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


        this.addElement(new TankGauge(this, 6, 18, 20, 55, container.base.tank, TankGauge.TankGuiStyle.Normal) {
            @Override
            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                if (container.base.tank == null) {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                } else {
                    FluidStack fs = container.base.tank.getFluid();
                    if (!fs.isEmpty() && fs.getAmount() > 0) {
                        Fluid fluid = fs.getFluid();
                        if (fluid != null) {
                            ret.add(Localization.translate(fluid.getFluidType().getDescriptionId()) + ": " + fs.getAmount() + " " + Localization.translate(
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
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {
                if (container.base.tank == null) {
                    return;
                }
                FluidStack fs = container.base.tank.getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
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
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.getAmount() / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    this.gui.drawSprite(poseStack,
                            mouseX + fluidX,
                            mouseY + (double) (fluidY + fluidHeight) - renderHeight,
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

                        this.gui.drawTexturedModalRect(poseStack, mouseX + gaugeX, mouseY + gaugeY, 38, 100, 20, 55);
                    }
                }
            }
        });

        this.addElement(new TankGauge(this, 129, 18, 20, 55, container.base.tank1, TankGauge.TankGuiStyle.Normal) {
            @Override
            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                if (container.base.tank1 == null) {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                } else {
                    FluidStack fs = container.base.tank1.getFluid();
                    if (!fs.isEmpty() && fs.getAmount() > 0) {
                        Fluid fluid = fs.getFluid();
                        if (fluid != null) {
                            ret.add(Localization.translate(fluid.getFluidType().getDescriptionId()) + ": " + fs.getAmount() + " " + Localization.translate(
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
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {
                if (container.base.tank1 == null) {
                    return;
                }
                FluidStack fs = container.base.tank1.getFluid();
                if (!fs.isEmpty()&& fs.getAmount() > 0) {
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
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    bindBlockTexture();
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.getAmount() / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    this.gui.drawSprite(poseStack,
                            mouseX + fluidX,
                            mouseY + (double) (fluidY + fluidHeight) - renderHeight,
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

                        this.gui.drawTexturedModalRect(poseStack, mouseX + gaugeX, mouseY + gaugeY, 38, 100, 20, 55);
                    }
                }
            }
        });
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 15 && mouseY >= 3 && mouseY <= 15) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.coke_oven_recipe.info"));
            List<String> compatibleUpgrades = ListInformationUtils.coke_oven;
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        handleUpgradeTooltip(par1, par2);
        highlightedMinus = false;
        highlightedPlus = false;

        if (par1 >= 34 && par2 >= 23 && par1 <= 47 && par2 <= 36) {
            highlightedMinus = true;
        }
        if (par1 >= 108 && par2 >= 23 && par1 <= 121 && par2 <= 36) {
            highlightedPlus = true;
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (x >= 34 && x <= 47 && y >= 23 && y <= 36) {
            new PacketUpdateServerTile(this.container.base, 1);
        }
        if (x >= 108 && x <= 121 && y >= 23 && y <= 36) {
            new PacketUpdateServerTile(this.container.base, 0);
        }

    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack,this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        String name = Localization.translate(this.container.base.getName());
        this.drawXCenteredString(poseStack,this.imageWidth / 2 + 20, 6, net.minecraft.network.chat.Component.nullToEmpty(name), 4210752, false);
    }

    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack,f, x, y);
        bindTexture(getTexture());

        int progress = (int) (38.0F * this.container.base.getProgress() / 3600D);

        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        if (highlightedMinus) {
            drawTexturedModalRect( poseStack,xoffset + 34, yoffset + 23, 192, 3, 14, 14);

        }
        if (highlightedPlus) {
            drawTexturedModalRect( poseStack,xoffset + 108, yoffset + 23, 177, 3, 14, 14);

        }
        if (progress > 0) {
            drawTexturedModalRect(poseStack, xoffset + 88, yoffset + 46, 177, 19, progress, 11);
        }
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/infobutton.png"));
        drawTexturedModalRect( poseStack,xoffset + 3, yoffset + 3, 0, 0, 10, 10);

        bindTexture(getTexture());

        int bar = (int) ((Math.min(this.container.base.bar * 1D, 5D) / 5D) * 50D);
        if (bar > 0) {
            drawTexturedModalRect( poseStack,this.guiLeft + 53, this.guiTop + 28, 181, 53, bar, 5);
        }
        if (container.base.heat != null) {
            progress = (int) (50.0F * container.base.heat.getFillRatio());
            drawTexturedModalRect( poseStack,this.guiLeft + 66, this.guiTop + 79, 180, 34, progress, 8);

        }
    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.TEXTURES, "textures/gui/guicokeoven.png");
    }

}
