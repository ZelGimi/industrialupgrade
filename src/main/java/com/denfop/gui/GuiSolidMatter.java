package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerSolidMatter;
import com.denfop.utils.ModUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSolidMatter extends GuiCore<ContainerSolidMatter> {

    public final ContainerSolidMatter container;

    public GuiSolidMatter(ContainerSolidMatter container1) {
        super(container1);
        this.container = container1;
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(f, x, y);
        this.mc.getTextureManager().bindTexture(getTexture());
        int progress = (int) (27.0D * (this.container.base).getChargeLevel());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        if (progress > 0) {
            drawTexturedModalRect(
                    xoffset + 62,
                    yoffset + 26,
                    176,
                    7 + 28 * this.container.base.getBlockMetadata(),
                    29,
                    progress + 1
            );
        }
        String progress1 = Localization.translate("gui.MolecularTransformer.progress") + ": ";
        this.fontRenderer.drawString(progress1 + ModUtils.getString(this.container.base.getProgress() * 100) + "%", xoffset + 5,
                yoffset + 36, 4210752
        );

    }

    public String getName() {
        return this.container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSolidMatter.png");
    }

}
