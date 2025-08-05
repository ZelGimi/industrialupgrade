package com.denfop.api.gui;

import com.denfop.Localization;
import com.denfop.componets.ComponentBioFuelEnergy;
import com.denfop.componets.ComponentSteamEnergy;
import com.denfop.componets.Fluids;
import com.denfop.gui.GuiAdvCokeOven;
import com.denfop.gui.GuiBlastFurnace;
import com.denfop.gui.GuiCokeOven;
import com.denfop.gui.GuiCore;
import com.denfop.items.ItemPipette;
import com.denfop.network.packet.PacketDrainFluidPipette;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import java.util.List;

public class TankGauge extends GuiElement<TankGauge> {


    public static final int v = 100;
    protected final IFluidTank tank;
    private final TankGauge.TankGuiStyle style;

    public TankGauge(GuiCore<?> gui, int x, int y, int width, int height, IFluidTank tank, TankGauge.TankGuiStyle style) {
        super(gui, x, y, width, height);
        if (tank == null) {
            throw new NullPointerException("null tank");
        } else {
            this.tank = tank;
            this.style = style;
        }
    }

    public static TankGauge createNormal(GuiCore<?> gui, int x, int y, IFluidTank tank) {
        return new TankGauge(gui, x, y, 20, 55, tank, TankGauge.TankGuiStyle.Normal);
    }

    public static TankGauge createPlain(GuiCore<?> gui, int x, int y, int width, int height, IFluidTank tank) {
        return new TankGauge(gui, x, y, width, height, tank, TankGauge.TankGuiStyle.Plain);
    }

    public static TankGauge createBorderless(GuiCore<?> gui, int x, int y, IFluidTank tank, boolean mirrored) {
        return new TankGauge(
                gui,
                x,
                y,
                12,
                47,
                tank,
                mirrored ? TankGauge.TankGuiStyle.BorderlessMirrored : TankGauge.TankGuiStyle.Borderless
        );
    }

    public TankGuiStyle getStyle() {
        return style;
    }

    @Override
    protected boolean onMouseClick(int mouseX, int mouseY, MouseButton button) {
        if (this.visible() && this.contains(mouseX, mouseY) && !this.tank.getFluid().isEmpty() && this.tank instanceof Fluids.InternalFluidTank fluidTank && Minecraft.getInstance().player.containerMenu.getCarried().getItem() instanceof ItemPipette pipette) {
            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(Minecraft.getInstance().player.containerMenu.getCarried());
            TileEntityBlock block = (TileEntityBlock) this.gui.container.base;

            ComponentSteamEnergy steamEnergy = block.getComp(ComponentSteamEnergy.class);
            ComponentBioFuelEnergy bioEnergy = block.getComp(ComponentBioFuelEnergy.class);
            if (steamEnergy != null && !steamEnergy.getFluidTank().getFluid().isEmpty() && steamEnergy.getFluidTank().getFluid().getAmount() == this.tank.getFluid().getAmount() && FluidStack.isSameFluid(steamEnergy.getFluidTank().getFluid(), this.tank.getFluid()))
                return false;
            if (bioEnergy != null && !bioEnergy.getFluidTank().getFluid().isEmpty() && bioEnergy.getFluidTank().getFluid().getAmount() == this.tank.getFluid().getAmount() && FluidStack.isSameFluid(bioEnergy.getFluidTank().getFluid(), this.tank.getFluid()))
                return false;
            if (this.gui instanceof GuiBlastFurnace<?> || this.gui instanceof GuiCokeOven<?> || gui instanceof GuiAdvCokeOven<?>)
                return false;
            if (handler.getFluidInTank(0).isEmpty() || (FluidStack.isSameFluid(handler.getFluidInTank(0), fluidTank.getFluid()))) {
                this.getGui().getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

                new PacketDrainFluidPipette((BlockEntity) gui.container.base, fluidTank.getIdentifier(), Math.min(fluidTank.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).getAmount(), handler.getTankCapacity(0) - handler.getFluidInTank(0).getAmount()));
            }
        }
        return super.onMouseClick(mouseX, mouseY, button);
    }

    public void drawBackground(GuiGraphics poseStack, int mouseX, int mouseY) {
        bindCommonTexture();
        FluidStack fs = this.tank.getFluid();
        if (!fs.isEmpty() && fs.getAmount() > 0) {
            if (this.style.withBorder) {
                this.gui.drawTexturedModalRect(poseStack,
                        mouseX + this.x,
                        mouseY + this.y,
                        70,
                        100,
                        this.width,
                        this.height
                );
            }

            int fluidX = this.x;
            int fluidY = this.y;
            int fluidWidth = this.width;
            int fluidHeight = this.height;
            if (this.style.withBorder) {
                fluidX += 4;
                fluidY += 4;
                fluidWidth = 12;
                fluidHeight = 48;
            }

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
            if (this.style.withGauge) {
                bindCommonTexture();
                int gaugeX = this.x;
                int gaugeY = this.y;
                if (!this.style.withBorder) {
                    gaugeX -= 4;
                    gaugeY -= 4;
                }

                this.gui.drawTexturedModalRect(poseStack, mouseX + gaugeX, mouseY + gaugeY, 38, 100, 20, 55);
            }
        } else if (this.style.withBorder) {
            this.gui.drawTexturedModalRect(poseStack,
                    mouseX + this.x,
                    mouseY + this.y,
                    70,
                    100,
                    this.width,
                    this.height
            );
        } else if (this.style.withGauge) {
            this.gui.drawTexturedModalRect(poseStack,
                    mouseX + this.x,
                    mouseY + this.y,
                    74,
                    104,
                    this.width,
                    this.height
            );
        }

    }

    protected List<String> getToolTip() {
        List<String> ret = super.getToolTip();
        FluidStack fs = this.tank.getFluid();
        if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            if (this.tank instanceof Fluids.InternalFluidTank) {
                Fluids.InternalFluidTank tank1 = (Fluids.InternalFluidTank) this.tank;
                ret.add(Localization.translate("iu.tank.fluids"));
                ret.addAll(tank1.getFluidList());
            }
        } else if (!fs.isEmpty() && fs.getAmount() > 0) {
            Fluid fluid = fs.getFluid();
            if (fluid != null) {
                ret.add(Localization.translate(fs.getFluid().getFluidType().getDescriptionId()) + ": " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
            } else {
                ret.add("invalid fluid stack");
            }
        } else {
            ret.add(Localization.translate("iu.generic.text.empty"));
        }

        return ret;
    }

    public enum TankGuiStyle {
        Normal(true, true, false),
        Borderless(false, true, false),
        BorderlessMirrored(false, true, true),
        Plain(false, false, false);

        public final boolean withBorder;
        public final boolean withGauge;
        public final boolean mirrorGauge;

        TankGuiStyle(boolean withBorder, boolean withGauge, boolean mirrorGauge) {
            this.withBorder = withBorder;
            this.withGauge = withGauge;
            this.mirrorGauge = mirrorGauge;
        }
    }

}
