package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.steam.EnumSteamPhase;
import com.denfop.api.widget.AdvancedTooltipWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.componets.Energy;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuBaseSteamTurbineController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScreenBaseSteamTurbineController<T extends ContainerMenuBaseSteamTurbineController> extends ScreenMain<ContainerMenuBaseSteamTurbineController> {
    boolean hoverOne;
    boolean hoverTwo;
    boolean hoverThree;
    boolean hoverFour;
    boolean hoverFive;
    boolean hoverSix;
    boolean hoverSeven;
    boolean hoverEight;
    boolean hoverNine;
    boolean hoverTen;
    boolean hoverWork;
    int index = -1;

    public ScreenBaseSteamTurbineController(ContainerMenuBaseSteamTurbineController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.addWidget(new TankWidget(
                this,
                26,
                6,
                18,
                42,
                container.base.getWaterFluid()
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = container.base.getWaterFluid().getFluid();
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (this.tank instanceof Fluids.InternalFluidTank) {
                        Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                        ret.add(Localization.translate("iu.tank.fluids"));
                        ret.addAll(tank1.getFluidList());
                    }
                } else if (!fs.isEmpty() && fs.getAmount() > 0) {
                    Fluid fluid = fs.getFluid();
                    if (fluid != null) {
                        ret.add(fluid.getFluidType().getDescription().getString() + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                    } else {
                        ret.add("invalid fluid stack");
                    }
                } else {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                }

                return ret;
            }

            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = container.base.getWaterFluid().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    fluidX += 4;
                    fluidY += 3;
                    fluidWidth = 11;
                    fluidHeight = 35;

                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.getAmount() / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    bindBlockTexture();
                    this.gui.drawSprite(poseStack, mouseX +
                                    fluidX,
                            mouseY + (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + 97, this.gui.guiTop + 14, 191, 5, 7, 46);

                }


            }
        });
        this.addWidget(new TankWidget(
                this,
                6,
                6,
                18,
                42,
                container.base.getSteamFluid()
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = container.base.getSteamFluid().getFluid();
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    if (this.tank instanceof Fluids.InternalFluidTank) {
                        Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                        ret.add(Localization.translate("iu.tank.fluids"));
                        ret.addAll(tank1.getFluidList());
                    }
                } else if (!fs.isEmpty() && fs.getAmount() > 0) {
                    Fluid fluid = fs.getFluid();
                    if (fluid != null) {
                        ret.add(fluid.getFluidType().getDescription().getString() + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                    } else {
                        ret.add("invalid fluid stack");
                    }
                } else {
                    ret.add(Localization.translate("iu.generic.text.empty"));
                }

                return ret;
            }

            @Override
            public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                FluidStack fs = container.base.getSteamFluid().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    fluidX += 4;
                    fluidY += 3;
                    fluidWidth = 11;
                    fluidHeight = 35;

                    Fluid fluid = fs.getFluid();
                    IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                    TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                    int color = extensions.getTintColor();
                    double renderHeight = (double) fluidHeight * ModUtils.limit(
                            (double) fs.getAmount() / (double) this.tank.getCapacity(),
                            0.0D,
                            1.0D
                    );
                    bindBlockTexture();
                    this.gui.drawSprite(poseStack, mouseX +
                                    fluidX,
                            mouseY + (double) (fluidY + fluidHeight) - renderHeight,
                            fluidWidth,
                            renderHeight,
                            sprite,
                            color,
                            1.0D,
                            false,
                            true
                    );
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    this.gui.bindTexture();
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + 97, this.gui.guiTop + 14, 191, 5, 7, 46);

                }


            }
        });
        for (int i = 0; i < 2; i++) {
            FluidTank fluidTank = guiContainer.base.listCoolant.get(i).getCoolant();
            this.addWidget(new TankWidget(
                    this,
                    132 + i * 20,
                    6,
                    18,
                    42,
                    fluidTank
            ) {

                protected List<String> getToolTip() {
                    List<String> ret = new ArrayList<>();
                    FluidStack fs = fluidTank.getFluid();
                    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        if (this.tank instanceof Fluids.InternalFluidTank) {
                            Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                            ret.add(Localization.translate("iu.tank.fluids"));
                            ret.addAll(tank1.getFluidList());
                        }
                    } else if (!fs.isEmpty() && fs.getAmount() > 0) {
                        Fluid fluid = fs.getFluid();
                        if (fluid != null) {
                            ret.add(fluid.getFluidType().getDescription().getString() + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                        } else {
                            ret.add("invalid fluid stack");
                        }
                    } else {
                        ret.add(Localization.translate("iu.generic.text.empty"));
                    }

                    return ret;
                }

                @Override
                public void drawBackground(PoseStack poseStack, final int mouseX, final int mouseY) {

                    FluidStack fs = fluidTank.getFluid();
                    if (!fs.isEmpty() && fs.getAmount() > 0) {
                        int fluidX = this.x;
                        int fluidY = this.y;
                        int fluidWidth = this.width;
                        int fluidHeight = this.height;
                        fluidX += 4;
                        fluidY += 3;
                        fluidWidth = 11;
                        fluidHeight = 35;

                        Fluid fluid = fs.getFluid();
                        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
                        TextureAtlasSprite sprite = getBlockTextureMap().getSprite(extensions.getStillTexture(fs));
                        int color = extensions.getTintColor();
                        double renderHeight = (double) fluidHeight * ModUtils.limit(
                                (double) fs.getAmount() / (double) this.tank.getCapacity(),
                                0.0D,
                                1.0D
                        );
                        bindBlockTexture();
                        this.gui.drawSprite(poseStack, mouseX +
                                        fluidX,
                                mouseY + (double) (fluidY + fluidHeight) - renderHeight,
                                fluidWidth,
                                renderHeight,
                                sprite,
                                color,
                                1.0D,
                                false,
                                true
                        );
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        this.gui.bindTexture();
                        this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + 97, this.gui.guiTop + 14, 191, 5, 7, 46);

                    }


                }
            });
        }


    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        if (hoverWork) {
            new PacketUpdateServerTile(this.container.base, 0);
        }
        if (this.index != -1) {
            new PacketUpdateServerTile(this.container.base, (this.index + 1) * -1);
        }
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.steam_turbine.info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 19; i++) {
                compatibleUpgrades.add(Localization.translate("iu.steam_turbine.info" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 147, mouseY - 20, text);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);

        hoverOne = false;
        hoverTwo = false;
        hoverThree = false;
        hoverFour = false;
        hoverFive = false;
        hoverSix = false;
        hoverSeven = false;
        hoverEight = false;
        hoverNine = false;
        hoverTen = false;
        hoverWork = false;
        this.index = -1;
        new TooltipWidget(this, 10, 51, 156, 14).withTooltip(() -> "Phase: " + String.valueOf(this.container.base.phase) + "\n" + "Stage: " + String.valueOf(
                this.container.base.enumSteamPhase.name()) +
                "\n" + "Generate: " + ModUtils.getString(this.container.base.generation) + " EF/t" + "\n" +
                "Heat: " + ModUtils.getString(this.container.base.heat)).drawForeground(poseStack, par1, par2);

        for (int i = 0; i < 10; i++)
            if (par1 >= 10 + 15 * i && par2 >= 66 && par1 <= 22 + 15 * i && par2 <= 80) {
                index = i;
                new AdvancedTooltipWidget(this, 10 + 15 * i, 66, 22 + 15 * i, 80).withTooltip(String.valueOf(EnumSteamPhase.values()[index].name())).drawForeground(poseStack, par1, par2);
            }
        if (par1 >= 76 && par2 >= 6 && par1 <= 99 && par2 <= 30) {
            hoverWork = true;
        }
        if (par1 >= 62 && par2 >= 33 && par1 <= 114 && par2 <= 47) {
            Energy component = this.container.base.energy.getEnergy();
            String text = ModUtils.getString(Math.min(
                    component.getEnergy(),
                    component.getCapacity()
            )) + "/" + ModUtils.getString(component.getCapacity()) + " " +
                    "EF";
            new AdvancedTooltipWidget(this, 62, 33, 114, 47).withTooltip(text).drawForeground(poseStack, par1, par2);

        }
        if (hoverWork) {
            new AdvancedTooltipWidget(this, 76, 6, 99, 30).withTooltip(this.container.base.work ? Localization.translate(
                    "turn_off") :
                    Localization.translate("turn_on")).drawForeground(poseStack, par1, par2);
        }
        switch (index) {
            case 0 -> hoverOne = true;
            case 1 -> hoverTwo = true;
            case 2 -> hoverThree = true;
            case 3 -> hoverFour = true;
            case 4 -> hoverFive = true;
            case 5 -> hoverSix = true;
            case 6 -> hoverSeven = true;
            case 7 -> hoverEight = true;
            case 8 -> hoverNine = true;
            case 9 -> hoverTen = true;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        bindTexture();
        double bar = this.container.base.phase / 230D;
        bar = Math.min(1, bar);
        double energy = this.container.base.energy.getEnergy().getFillRatio();
        energy = Math.min(1, energy);
        drawTexturedModalRect(poseStack, this.guiLeft + 11, this.guiTop + 52
                , 11, 168, (int) (bar * 154), 12);
        drawTexturedModalRect(poseStack, this.guiLeft + 65, this.guiTop + 36
                , 206, 93, (int) (energy * 47), 9);
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        bindTexture();
        if (hoverWork) {
            drawTexturedModalRect(poseStack, this.guiLeft + 76, (int) this.guiTop + 6
                    , 232, 48, 24, 24);
        }
        if (container.base.work) {
            drawTexturedModalRect(poseStack, this.guiLeft + 76, (int) this.guiTop + 6
                    , 207, 48, 24, 24);
        }
        switch (this.container.base.stableenumSteamPhase) {
            case ONE -> drawTexturedModalRect(poseStack, this.guiLeft + 10, (int) this.guiTop + 66
                    , 198, 0, 13, 15);
            case TWO -> drawTexturedModalRect(poseStack, this.guiLeft + 25, (int) this.guiTop + 66
                    , 213, 0, 13, 15);
            case THREE -> drawTexturedModalRect(poseStack, this.guiLeft + 40, (int) this.guiTop + 66
                    , 228, 0, 13, 15);
            case FOUR -> drawTexturedModalRect(poseStack, this.guiLeft + 55, (int) this.guiTop + 66
                    , 243, 0, 13, 15);
            case FIVE -> drawTexturedModalRect(poseStack, this.guiLeft + 70, (int) this.guiTop + 66
                    , 198, 16, 13, 15);
            case SIX -> drawTexturedModalRect(poseStack, this.guiLeft + 85, (int) this.guiTop + 66
                    , 213, 16, 13, 15);
            case SEVEN -> drawTexturedModalRect(poseStack, this.guiLeft + 100, (int) this.guiTop + 66
                    , 228, 16, 13, 15);
            case EIGHT -> drawTexturedModalRect(poseStack, this.guiLeft + 115, (int) this.guiTop + 66
                    , 243, 16, 13, 15);
            case NINE -> drawTexturedModalRect(poseStack, this.guiLeft + 130, (int) this.guiTop + 66
                    , 220, 32, 13, 15);
            case TEN -> drawTexturedModalRect(poseStack, this.guiLeft + 145, (int) this.guiTop + 66
                    , 235, 32, 21, 15);
        }
        if (this.index != -1)
            switch (EnumSteamPhase.values()[index]) {
                case ONE -> drawTexturedModalRect(poseStack, this.guiLeft + 10, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case TWO -> drawTexturedModalRect(poseStack, this.guiLeft + 25, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case THREE -> drawTexturedModalRect(poseStack, this.guiLeft + 40, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case FOUR -> drawTexturedModalRect(poseStack, this.guiLeft + 55, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case FIVE -> drawTexturedModalRect(poseStack, this.guiLeft + 70, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case SIX -> drawTexturedModalRect(poseStack, this.guiLeft + 85, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case SEVEN -> drawTexturedModalRect(poseStack, this.guiLeft + 100, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case EIGHT -> drawTexturedModalRect(poseStack, this.guiLeft + 115, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case NINE -> drawTexturedModalRect(poseStack, this.guiLeft + 130, (int) this.guiTop + 66
                        , 220, 74, 13, 15);
                case TEN -> drawTexturedModalRect(poseStack, this.guiLeft + 145, (int) this.guiTop + 66
                        , 235, 74, 21, 15);
            }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);


    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisteamturbine_main.png");
    }

}
