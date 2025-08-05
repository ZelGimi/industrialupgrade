package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerSolidMatter;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiSolidMatter<T extends ContainerSolidMatter> extends GuiCore<ContainerSolidMatter> {

    public final ContainerSolidMatter container;

    public GuiSolidMatter(ContainerSolidMatter container1) {
        super(container1);
        this.container = container1;
        String progress1 = Localization.translate("gui.MolecularTransformer.progress") + ": ";
        this.addElement(new AdvArea(this, 58, 54, 117, 67) {
            @Override
            protected List<String> getToolTip() {
                return Arrays.asList(progress1 + ModUtils.getString(container.base.getProgress() * 100) + "%");
            }
        });
    }

    protected void renderBg(GuiGraphics poseStack, float f, int x, int y) {
        super.renderBg(poseStack, f, x, y);
        bindTexture(getTexture());
        int progress = (int) (54.0D * (this.container.base).getChargeLevel());
        int xoffset = (this.width - this.imageWidth) / 2;
        int yoffset = (this.height - this.imageHeight) / 2;
        int index = 0;
        switch (this.container.base.getTeBlock().getId()) {
            case 4:
                index = 0;
                break;
            case 1:
                index = 1;
                break;
            case 0:
                index = 2;
                break;
            case 7:
                index = 3;
                break;
            case 5:
                index = 4;
                break;
            case 6:
                index = 5;
                break;
            case 2:
                index = 6;
                break;
            case 3:
                index = 7;
                break;
        }
        if (progress > 0) {
            drawTexturedModalRect(poseStack,
                    xoffset + 62,
                    yoffset + 58,
                    181,
                    5 + 15 * index,
                    progress,
                    6
            );
        }


    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GuiSolidMatter.png".toLowerCase());
    }

}
