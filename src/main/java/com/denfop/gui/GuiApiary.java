package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.blocks.FluidName;
import com.denfop.container.ContainerApiary;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiApiary extends GuiIU<ContainerApiary> {


    public ContainerApiary container;

    public GuiApiary(ContainerApiary container1) {
        super(container1);
        this.container = container1;
        componentList.clear();
        this.ySize = 207;
        this.addElement(new AdvArea(this,10,21,21,69).withTooltip(() -> ModUtils.getString(container1.base.food)));
        this.addElement(new AdvArea(this,154,21,165,69).withTooltip(() -> ModUtils.getString(container1.base.royalJelly)));
        this.addElement(new AdvArea(this,37,22,50,32).withTooltip(() -> ModUtils.getString(container1.base.workers)));
        this.addElement(new AdvArea(this,37,35,50,49).withTooltip(() -> ModUtils.getString(container1.base.doctors)));
        this.addElement(new AdvArea(this,37,51,50,64).withTooltip(() -> ModUtils.getString(container1.base.builders)));
        this.addElement(new AdvArea(this,37,67,50,80).withTooltip(() -> ModUtils.getString(container1.base.attacks)));
        this.addElement(new AdvArea(this,37,83,50,94).withTooltip(() -> ModUtils.getString(container1.base.ill)));
        this.addElement(new AdvArea(this,37,97,50,109).withTooltip(() -> ModUtils.getString(container1.base.birth) + "/" +ModUtils.getString(container1.base.death) ));

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);

    }

    @Override
    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiapiary.png");
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture();
        switch (this.container.base.task) {
            case 0:
                this.drawTexturedModalRect(this.guiLeft + 60, this.guiTop + 24, 249, 1, 6, 6);
                break;
            case 1:
                this.drawTexturedModalRect(this.guiLeft + 60, this.guiTop + 71, 249, 1, 6, 6);
                break;
            case 2:
                this.drawTexturedModalRect(this.guiLeft + 60, this.guiTop + 39, 249, 1, 6, 6);
                break;
            case 3:
                this.drawTexturedModalRect(this.guiLeft + 60, this.guiTop + 55, 249, 1, 6, 6);
                break;
        }
        if (this.container.base.deathTask == 1) {
            this.drawTexturedModalRect(this.guiLeft + 60, this.guiTop + 101, 249, 1, 6, 6);
        }

        if (this.container.base.illTask == 1) {
            this.drawTexturedModalRect(this.guiLeft + 60, this.guiTop + 86, 249, 1, 6, 6);
        }
        int renderHeight = (int) (49 * ModUtils.limit(
                        this.container.base.food / (this.container.base.maxFood * 1D),
                        0.0D,
                        1.0D
                ));
        this.drawTexturedModalRect(
                this.guiLeft + 10,
                this.guiTop + 20 + 50 - renderHeight,
                236,
                50 - renderHeight,
                12,
                renderHeight
        );
        renderHeight = (int) (49 * ModUtils.limit(
                this.container.base.royalJelly / (this.container.base.maxJelly * 1D),
                0.0D,
                1.0D
        ));
        this.drawTexturedModalRect(
                this.guiLeft + 154,
                this.guiTop + 20 + 50 - renderHeight,
                223,
                50 - renderHeight,
                12,
                renderHeight
        );
    }

}
