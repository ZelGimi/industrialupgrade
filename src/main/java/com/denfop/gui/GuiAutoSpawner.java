package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerAutoSpawner;
import com.denfop.utils.ExperienceUtils;
import com.denfop.utils.ModUtils;
import ic2.core.GuiIC2;
import ic2.core.gui.Area;
import ic2.core.init.Localization;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiAutoSpawner extends GuiIC2<ContainerAutoSpawner> {

    public final ContainerAutoSpawner container;

    public GuiAutoSpawner(ContainerAutoSpawner container1) {
        super(container1, 214, 176);
        this.container = container1;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        int[] progress = new int[4];
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());

        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xoffset, yoffset, 0, 0, this.xSize, this.ySize);

        for (int i = 0; i < 4; i++) {
            progress[i] = 29 * this.container.base.progress[i] / this.container.base.tempprogress;
            progress[i] = Math.min(progress[i], 29);
            if (progress[i] > 0) {
                drawTexturedModalRect(
                        xoffset + 177,
                        yoffset + 63 + i * 35 - progress[i],
                        215,
                        46 - progress[i] + 28,
                        4,
                        progress[i]
                );
            }
        }
        int exp = (int) (34 * this.container.base.exp.getEnergy() / this.container.base.exp.getCapacity());
        exp = Math.min(exp, 34);
        if (exp > 0) {
            drawTexturedModalRect(xoffset + 94, yoffset + 80, 216, 35, exp, 2);
        }
        int energy =
                (int) (34 * this.container.base.energy.getEnergy() / this.container.base.energy.getCapacity());
        energy = Math.min(energy, 34);
        if (energy > 0) {
            drawTexturedModalRect(xoffset + 134, yoffset + 75, 216, 43, energy, 2);
        }
        int energy2 = (int) (34 * this.container.base.energy2 / this.container.base.maxEnergy2);
        energy2 = Math.min(energy2, 34);
        if (energy2 > 0) {
            drawTexturedModalRect(xoffset + 134, yoffset + 88, 216, 39, energy2, 2);
        }


    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(ModUtils.getString(ExperienceUtils.getLevelForExperience((int) this.container.base.exp.getEnergy())),
                106, 70, ModUtils.convertRGBcolorToInt(13, 229, 34)
        );
        new Area(
                this,
                133,
                74,
                168 - 133,
                77 - 74
        ).withTooltip("EU: " + ModUtils.getString(this.container.base.energy.getEnergy()) + "/" + ModUtils.getString(
                this.container.base.energy.getCapacity())).drawForeground(par1, par2);
        new Area(
                this,
                133,
                87,
                168 - 133,
                90 - 87
        ).withTooltip("RF: " + ModUtils.getString(this.container.base.energy2) + "/" + ModUtils.getString(
                this.container.base.maxEnergy2)).drawForeground(par1, par2);
        this.addElement(new Area(
                this,
                93,
                79,
                128 - 93,
                82 - 79
        ).withTooltip("XP: " + ModUtils.getString(this.container.base.exp.getEnergy()) + "/" + ModUtils.getString(
                this.container.base.exp.getCapacity())));

        for (int i = 0; i < 4; i++) {
            int progress1 = Math.min(
                    (100 * this.container.base.progress[i] / this.container.base.tempprogress),
                    100
            );
            new Area(this, 177,
                    33 + i * 35,
                    3, 63 + (i * 35) - (33 + i * 35)
            ).withTooltip(Localization.translate("gui.MolecularTransformer.progress") +
                    ": " + ModUtils.getString(progress1) + "%" + (!container.base.description_mobs[i].isEmpty() ? (
                    "\n" + container.base.description_mobs[i]) : "")).drawForeground(
                    par1,
                    par2
            );


        }
    }


    public String getName() {
        return Localization.translate("iu.blockSpawner.name");
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiAutoSpawner.png");
    }

}
