package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.vein.gas.TypeGas;
import com.denfop.api.widget.*;
import com.denfop.blocks.FluidName;
import com.denfop.componets.Fluids;
import com.denfop.containermenu.ContainerMenuGasWellController;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ScreenGasWellController<T extends ContainerMenuGasWellController> extends ScreenMain<ContainerMenuGasWellController> {
    boolean hoverController = false;

    public ScreenGasWellController(ContainerMenuGasWellController guiContainer) {
        super(guiContainer);
        componentList.clear();
        this.componentList.add(new ScreenWidget(this, 7, 64, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(guiContainer.base.getEnergy())
        ));

        this.addWidget(new TankWidget(
                this,
                75,
                13,
                26,
                61,
                guiContainer.base.tank.getTank(),
                TankWidget.TankGuiStyle.Normal
        ) {

            protected List<String> getToolTip() {
                List<String> ret = new ArrayList<>();
                FluidStack fs = guiContainer.base.tank.getTank().getFluid();
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

                FluidStack fs = guiContainer.base.tank.getTank().getFluid();
                if (!fs.isEmpty() && fs.getAmount() > 0) {
                    int fluidX = this.x;
                    int fluidY = this.y;
                    int fluidWidth = this.width;
                    int fluidHeight = this.height;
                    if (this.getStyle().withBorder) {
                        fluidX += 3;
                        fluidY += 3;
                        fluidWidth = 20;
                        fluidHeight = 55;
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
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        if (hoverController) {

            new PacketUpdateServerTile(container.base, 0);
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        handleUpgradeTooltip(par1, par2);
        hoverController = false;
        if (par1 >= 122 && par2 >= 32 && par1 <= 142 && par2 <= 52) {
            hoverController = true;
            new AdvancedTooltipWidget(this, 122, 32, 142, 52).withTooltip((this.container.base).work ? Localization.translate(
                    "turn_off") :
                    Localization.translate("turn_on")).drawForeground(poseStack, par1, par2);
        }
        if (container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE) {
            String text = "";
            if (container.base.vein.getType() == TypeGas.IODINE) {
                text = Localization.translate(FluidName.fluidiodine.getInstance().get().getFluidType().getDescriptionId());
            }


            if (container.base.vein.getType() == TypeGas.BROMIDE) {
                text = Localization.translate(FluidName.fluidbromine.getInstance().get().getFluidType().getDescriptionId());
            }

            if (container.base.vein.getType() == TypeGas.CHLORINE) {
                text = Localization.translate(FluidName.fluidchlorum.getInstance().get().getFluidType().getDescriptionId());
            }
            if (container.base.vein.getType() == TypeGas.FLORINE) {
                text = Localization.translate(FluidName.fluidfluor.getInstance().get().getFluidType().getDescriptionId());
            }

            new AdvancedTooltipWidget(this, 34, 36, 48, 50)
                    .withTooltip((text + " " + container.base.vein.getCol() + "/" + container.base.vein.getMaxCol() + "mb"))
                    .drawForeground(poseStack,
                            par1,
                            par2
                    );


        }

    }

    @Override
    protected void drawBackground(PoseStack poseStack) {

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        componentList.forEach(guiComponent -> guiComponent.drawBackground(poseStack, guiLeft(), guiTop()));
        bindTexture();
        if ((this.container.base).work) {
            this.drawTexturedModalRect(poseStack, this.guiLeft + 122, this.guiTop + 32, 235, 64, 21, 21);

        }
        if (container.base.vein != null && container.base.vein.isFind() && container.base.vein.getType() != TypeGas.NONE) {

            FluidStack fs;
            int x1 = 0;
            int y1 = 0;
            switch (container.base.vein.getType()) {
                case CHLORINE:
                    x1 = 241;
                    y1 = 16;
                    break;
                case BROMIDE:
                    x1 = 241;
                    y1 = 0;
                    break;
                case FLORINE:
                    x1 = 241;
                    y1 = 36;
                    break;
                default:
                    x1 = 241;
                    y1 = 48;
                    break;
            }
            this.drawTexturedModalRect(poseStack, this.guiLeft + 34, this.guiTop + 36, x1, y1, 15, 16);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigaswell_controller.png");
    }

}
