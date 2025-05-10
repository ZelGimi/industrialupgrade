package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.componets.ComponentButton;
import com.denfop.container.ContainerGeothermalController;
import com.denfop.tiles.geothermalpump.TileEntityGeothermalController;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GuiGeothermalController<T extends ContainerGeothermalController> extends GuiIU<ContainerGeothermalController> {

    private boolean hover = false;

    public GuiGeothermalController(ContainerGeothermalController guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.componentList.add(new GuiComponent(this, 70, 35, 24,
                24,
                new Component<>(new ComponentButton(this.container.base, 0, "") {
                    @Override
                    public String getText() {
                        return ((TileEntityGeothermalController) this.getEntityBlock()).work ? Localization.translate(
                                "turn_off") :
                                Localization.translate("turn_on");
                    }

                    @Override
                    public boolean active() {
                        return !((TileEntityGeothermalController) this.getEntityBlock()).work;
                    }
                })
        ));
    }

    private void handleUpgradeTooltip(int mouseX, int mouseY) {
        if (mouseX >= 0 && mouseX <= 12 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("guide.geothermalpump"));
            List<String> compatibleUpgrades = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                compatibleUpgrades.add(Localization.translate("guide.geothermalpump" + i));
            }
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX, mouseY, text);
        }
    }



    @Override
    protected void drawForegroundLayer(PoseStack poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip(par1, par2);
        if (!this.container.base.work) {
            this.hover = par1 >= 70 && par1 <= 70 + 24 && par2 >= 35 && par2 <= 35 + 24;
        } else {
            this.hover = false;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack,final float partialTicks, final int mouseX, final int mouseY) {
        super.drawBackgroundAndTitle(poseStack, partialTicks, mouseX, mouseY);

     RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        drawTexturedModalRect( poseStack,this.guiLeft + 70,
                this.guiTop + 35
                , 231, 1, 24, 24
        );
        if (this.container.base.work) {
            drawTexturedModalRect( poseStack,this.guiLeft + 70,
                    this.guiTop + 35
                    , 231, 51, 24, 24
            );
        }
        if (this.hover) {
            drawTexturedModalRect( poseStack,this.guiLeft + 70,
                    this.guiTop + 35
                    , 231, 26, 24, 24
            );
        }
       bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        drawTexturedModalRect( poseStack,this.guiLeft, this.guiTop, 0, 0, 10, 10);
    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guigeothermalpump.png");
    }

}
