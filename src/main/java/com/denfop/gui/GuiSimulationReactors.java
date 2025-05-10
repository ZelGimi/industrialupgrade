package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerSimulationReactors;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.base.TileEntitySimulatorReactor;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiSimulationReactors<T extends ContainerSimulationReactors> extends GuiIU<ContainerSimulationReactors> {

    private final String nameReactor;

    public GuiSimulationReactors(ContainerSimulationReactors guiContainer) {

        super(guiContainer);
        this.imageWidth = 255;
        this.imageHeight = 254;
        componentList.clear();
        this.addElement(new ImageInterface(this, 0, 0, imageWidth, imageHeight));
        inventory = new GuiComponent(this, 7, 171, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        this.addComponent(new GuiComponent(this, 178, 187, EnumTypeComponent.DEFAULT_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 190, 187, EnumTypeComponent.ADV_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 202, 187, EnumTypeComponent.IMP_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 214, 187, EnumTypeComponent.PER_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 178, 198, EnumTypeComponent.FLUID_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 190, 198, EnumTypeComponent.GAS_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 202, 198, EnumTypeComponent.GRAPHITE_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 214, 198, EnumTypeComponent.HEAT_REACTOR,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 230, 188, EnumTypeComponent.INFO,
                new Component<>(new ComponentEmpty())
        ));
        this.addComponent(new GuiComponent(this, 230, 188, EnumTypeComponent.INFO,
                new Component<>(new ComponentEmpty())
        ));
        this.componentList.add(new GuiComponent(this, 190, 164, EnumTypeComponent.WORK_BUTTON,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntitySimulatorReactor) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public void ClickEvent() {
                        new PacketUpdateServerTile(this.getEntityBlock(), 0);
                        ((TileEntitySimulatorReactor) this.getEntityBlock()).updateTileServer(Minecraft.getInstance().player, 0);
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntitySimulatorReactor) this.getEntityBlock()).work;
                    }
                })
        ));
        componentList.add(inventory);
        componentList.add(slots);
        String nameReactor1;
        nameReactor1 = "";
        if (guiContainer.base.reactors != null) {
            nameReactor1 = Localization.translate("multiblock." + guiContainer.base.reactors.getNameReactor().toLowerCase());
        }

        this.nameReactor = nameReactor1;
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = guiLeft;
        int yMin = guiTop;
        int x = i - xMin;
        int y = j - yMin;

        for (int index = 0; index < 4; index++) {
            if (x >= 178 + index * 12 && x <= 185 + index * 12 && y >= 187 && y <= 194) {
                new PacketUpdateServerTile(this.container.base, -(index + 1));
                this.container.base.updateTileServer(Minecraft.getInstance().player, -(index + 1));
            }
        }
        for (int index = 0; index < 4; index++) {
            if (x >= 178 + index * 12 && x <= 185 + index * 12 && y >= 198 && y <= 205) {
                new PacketUpdateServerTile(this.container.base, (index + 1));
                this.container.base.updateTileServer(Minecraft.getInstance().player, (index + 1));
            }
        }
    }

    private void handleUpgradeTooltip2(
            int x,
            int y
    ) {
        if (this.container.base.work && this.container.base.reactors != null && this.container.base.logicReactor != null) {
            if (x >= 230 && x <= 247 && y >= 188 && y <= 205) {


                List<String> compatibleUpgrades = new ArrayList<>();
                compatibleUpgrades.add("Output: " +
                        ModUtils.getString(this.container.base.output) + " EF"
                );
                compatibleUpgrades.add("Radiation: " +
                        ModUtils.getString(this.container.base.rad) + " ☢"
                );
                compatibleUpgrades.add("Heat: " +
                        (int) this.container.base.heat + " °C"
                );
                compatibleUpgrades.add("Stable: " +
                        (int) this.container.base.reactor.getStableMaxHeat() + " °C"
                );
                compatibleUpgrades.add("Max Heat: " +
                        (int) this.container.base.reactor.getMaxHeat() + " °C"
                );
                compatibleUpgrades.add(Localization.translate(
                        "waterreactor.security." + this.container.base.security.name().toLowerCase())
                );

                this.drawTooltip(x, y, compatibleUpgrades);
            }
        }
    }

    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack,par1, par2);
        handleUpgradeTooltip2(par1, par2);
        if (this.container.base.logicReactor != null) {
            for (int i = 0; i < this.container.base.logicReactor.getInfoStack().size(); i++) {
                ItemStack stack = this.container.base.logicReactor.getInfoStack().get(i);
                new ItemStackImage(this, 5 + (i / 8) * 20, 30 + i % 8 * 20, () -> stack).drawForeground(poseStack,par1, par2);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack,partialTicks, mouseX, mouseY);
        this.bindTexture();
        if (this.container.base.levelReactor != -1) {
            new GuiComponent(this, 178 + 12 * (this.container.base.levelReactor - 1), 184, EnumTypeComponent.CHECK_MARK,
                    new Component<>(new ComponentEmpty())
            ).drawBackground(poseStack,guiLeft, guiTop);

        }
        if (this.container.base.type != -1) {
            new GuiComponent(this, 178 + 12 * (this.container.base.type - 1), 195, EnumTypeComponent.CHECK_MARK,
                    new Component<>(new ComponentEmpty())
            ).drawBackground(poseStack,guiLeft, guiTop);
        }
        String name = Localization.translate(this.container.base.getName());
        if (!this.isBlack) {
            this.drawXCenteredString(poseStack,this.imageWidth / 2, 6, name, 4210752, false);
        } else {
            this.drawXCenteredString(poseStack,this.imageWidth / 2, 6, name, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }

        if (!this.isBlack) {
            this.drawXCenteredString(poseStack,this.imageWidth / 2, 17, this.nameReactor, 4210752, false);
        } else {
            this.drawXCenteredString(poseStack,this.imageWidth / 2, 17, this.nameReactor, ModUtils.convertRGBcolorToInt(216, 216, 216), false);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        if (this.container.base.logicReactor != null) {
            for (int i = 0; i < this.container.base.logicReactor.getInfoStack().size(); i++) {
                ItemStack stack = this.container.base.logicReactor.getInfoStack().get(i);
                new ItemStackImage(this, 5 + (i / 8) * 20, 30 + i % 8 * 20, () -> stack).drawBackground(poseStack,this.guiLeft, guiTop);
            }
        }
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guireactorsimulator.png");
    }

}
