package com.denfop.items;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerLeadBox;
import com.denfop.gui.GuiCore;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiFacadeItem extends GuiCore<ContainerFacadeItem> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png");
    private final String name;

    public GuiFacadeItem(ContainerFacadeItem container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName();
        this.ySize = 232;
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 11, 0);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 54, 176, 0, 18, 18);

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
