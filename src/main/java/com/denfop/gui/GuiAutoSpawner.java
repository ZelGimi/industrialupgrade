package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.container.ContainerAutoSpawner;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class GuiAutoSpawner<T extends ContainerAutoSpawner> extends GuiCore<ContainerAutoSpawner> {

    public final ContainerAutoSpawner container;

    public GuiAutoSpawner(ContainerAutoSpawner container1) {
        super(container1, 214, 176);
        this.container = container1;
    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        int[] progress = new int[4];
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());

        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;
        drawTexturedModalRect(poseStack, xoffset, yoffset, 0, 0, this.imageWidth, this.imageHeight);

        for (int i = 0; i < 4; i++) {
            progress[i] = 31 * this.container.base.progress[i] / this.container.base.tempprogress[i];
            progress[i] = Math.min(progress[i], 31);
            if (progress[i] > 0) {
                drawTexturedModalRect(poseStack,
                        xoffset + 177,
                        yoffset + 51 + i * 35 - progress[i],
                        215,
                        77 - progress[i],
                        4,
                        progress[i]
                );
            }
        }
        int exp = (int) (34 * this.container.base.exp.getEnergy() / this.container.base.exp.getCapacity());
        exp = Math.min(exp, 34);
        if (exp > 0) {
            drawTexturedModalRect(poseStack, xoffset + 94, yoffset + 80, 216, 35, exp, 2);
        }
        int energy =
                (int) (34 * this.container.base.energy.getEnergy() / this.container.base.energy.getCapacity());
        energy = Math.min(energy, 34);
        if (energy > 0) {
            drawTexturedModalRect(poseStack, xoffset + 134, yoffset + 75, 216, 43, energy, 2);
        }
        int energy2 = (int) (34 * this.container.base.energy2 / this.container.base.maxEnergy2);
        energy2 = Math.min(energy2, 34);
        if (energy2 > 0) {
            drawTexturedModalRect(poseStack, xoffset + 134, yoffset + 88, 216, 39, energy2, 2);
        }


    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        poseStack.drawString(Minecraft.getInstance().font, ModUtils.getString(ExperienceUtils.getLevelForExperience((int) this.container.base.exp.getEnergy())),
                66, 77, ModUtils.convertRGBcolorToInt(13, 229, 34), false
        );
        new Area(
                this,
                133,
                74,
                168 - 133,
                77 - 74
        ).withTooltip("EF: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(
                this.container.base.energy.getCapacity())).drawForeground(poseStack, par1, par2);
        new Area(
                this,
                133,
                87,
                168 - 133,
                90 - 87
        ).withTooltip("RF: " + ModUtils.getString(this.container.base.energy2) + "/" + ModUtils.getString(
                this.container.base.maxEnergy2)).drawForeground(poseStack, par1, par2);
        new Area(
                this,
                93,
                79,
                128 - 93,
                82 - 79
        ).withTooltip("XP: " + ModUtils.getString(this.container.base.exp.getEnergy()) + "/" + ModUtils.getString(
                this.container.base.exp.getCapacity())).drawForeground(poseStack, par1, par2);

        for (int i = 0; i < 4; i++) {
            int progress1 = Math.min(
                    (100 * this.container.base.progress[i] / this.container.base.tempprogress[i]),
                    100
            );
            new Area(this, 177,
                    33 + i * 35,
                    3, 63 + (i * 35) - (33 + i * 35)
            ).withTooltip(Localization.translate("gui.MolecularTransformer.progress") +
                    ": " + ModUtils.getString(progress1) + "%" + (!container.base.description_mobs[i].isEmpty() ? (
                    "\n" + container.base.description_mobs[i]) : "")).drawForeground(
                    poseStack, par1,
                    par2
            );


        }
    }


    public String getName() {
        return Localization.translate("iu.blockSpawner.name");
    }

    @Override
    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiAutoSpawner.png".toLowerCase());
    }

}
