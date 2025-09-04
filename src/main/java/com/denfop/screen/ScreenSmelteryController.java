package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ButtonWidget;
import com.denfop.api.widget.FluidDefaultWidget;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.blockentity.smeltery.ITank;
import com.denfop.containermenu.ContainerMenuSmelteryController;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ScreenSmelteryController<T extends ContainerMenuSmelteryController> extends ScreenMain<ContainerMenuSmelteryController> {

    List<FluidTank> fluidTanks = new ArrayList<>();
    List<FluidTank> fluidTanks1 = new ArrayList<>();
    List<Integer> integerList = new ArrayList<>();
    boolean hover1 = false;
    boolean hover2 = false;
    boolean hover3 = false;

    public ScreenSmelteryController(ContainerMenuSmelteryController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        int i = 0;
        for (ITank tank : guiContainer.base.listTank) {
            fluidTanks.add(tank.getTank());
            integerList.add(i);
            i++;
        }
        this.addWidget(new ButtonWidget(this, 139, 33, 161 - 139, 55 - 33, container.base, -1, "") {
            @Override
            public String getText() {
                return Localization.translate("iu.mix");
            }

            @Override
            public boolean visible() {
                return container.base.list.size() > 1;
            }

            public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
                this.getGui().bindTexture();
                if (highlighted) {
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + x, guiTop + y, 177,
                            25, 161 - 139, 55 - 33
                    );
                }
            }

        });
        this.addWidget(new ButtonWidget(this, 139, 9, 161 - 139, 31 - 9, container.base, -3, "") {
            @Override
            public String getText() {
                return Localization.translate("iu.separate");
            }

            @Override
            public boolean visible() {
                return container.base.list.size() == 1;
            }

            public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
                this.getGui().bindTexture();
                if (highlighted) {
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + x, guiTop + y, 177,
                            1, 161 - 139, 55 - 33
                    );
                }
            }
        });
        this.addWidget(new ButtonWidget(this, 139, 57, 161 - 139, 79 - 57, container.base, -2, "") {
            @Override
            public String getText() {
                return Localization.translate("iu.mix_max");
            }

            @Override
            public boolean visible() {
                return container.base.list.size() > 1;
            }

            public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
                this.getGui().bindTexture();
                if (highlighted) {
                    this.gui.drawTexturedModalRect(poseStack, this.gui.guiLeft + x, guiTop + y, 177,
                            49, 161 - 139, 55 - 33
                    );
                }

            }
        });
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 3 && mouseX <= 13 && mouseY >= 3 && mouseY <= 13) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("smeltery_info"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 10; i++) {
                compatibleUpgrades.add(Localization.translate("smeltery_info" + i));
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
    protected void mouseClicked(int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;
        for (i = 0; i < fluidTanks1.size(); i++) {
            if (x >= 21 + (i % 6) * 18 && x < 21 + ((i % 6) + 1) * 18 && y >= 17 + (i / 6) * 18 && y < 17 + ((i / 6) + 1) * 18) {
                new PacketUpdateServerTile(this.container.base, i);
            }
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        tanksRefresh();
        handleUpgradeTooltip(par1, par2);
        for (int i = 0; i < fluidTanks1.size(); i++) {
            final int finalI = i;
            (new FluidDefaultWidget(this, 21 + (finalI % 6) * 18, 17 + (finalI / 6) * 18, fluidTanks1.get(finalI).getFluid()) {
                protected List<String> getToolTip() {
                    List<String> ret = new ArrayList<>();
                    FluidStack fs = fluidTanks1.get(finalI).getFluid();
                    if (!fs.isEmpty() && fs.getAmount() > 0) {
                        Fluid fluid = fs.getFluid();
                        if (fluid != null) {
                            ret.add(Localization.translate(fluid.getFluidType().getDescriptionId()));
                            ret.add("Amount: " + fs.getAmount() + " " + Localization.translate("iu.generic.text.mb"));
                            String state = "Liquid";
                            ret.add("Type: " + state);
                            ret.add("Ingots: " + fs.getAmount() / 144);
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
            }).drawForeground(poseStack,
                    par1,
                    par2
            );
        }
    }

    private void tanksRefresh() {
        fluidTanks1 = fluidTanks.stream()
                .sorted((tank1, tank2) -> {
                    FluidStack fluid1 = tank1.getFluid();
                    FluidStack fluid2 = tank2.getFluid();

                    boolean hasFluid1 = !fluid1.isEmpty() && fluid1.getAmount() > 0;
                    boolean hasFluid2 = !fluid2.isEmpty() && fluid2.getAmount() > 0;
                    if (hasFluid1 && !hasFluid2) {
                        return -1;
                    } else if (!hasFluid1 && hasFluid2) {
                        return 1;
                    }


                    return 0;
                })
                .collect(Collectors.toList());

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);
        for (ScreenWidget element : elements) {
            element.drawBackground(poseStack, guiLeft, guiTop);
        }
        this.bindTexture();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        for (int i = 0; i < fluidTanks1.size(); i++) {
            this.bindTexture();
            new FluidDefaultWidget(this, 21 + (i % 6) * 18, 17 + (i / 6) * 18, fluidTanks1.get(i).getFluid()).drawBackground(poseStack,
                    this.guiLeft,
                    guiTop
            );
        }
        bindTexture();
        for (int i = 0; i < container.base.list.size(); i++) {
            int index = container.base.list.get(i);
            drawTexturedModalRect(poseStack, this.guiLeft + 21 + (index % 6) * 18, guiTop + 17 + (index / 6) * 18, 200,
                    1, 18, 18
            );
        }
        bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guismeltery.png");
    }

}
