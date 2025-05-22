package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerLeadBox;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLeadBox extends GuiIU<ContainerLeadBox> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/guileadbox.png");
    private final String name;

    public GuiLeadBox(ContainerLeadBox container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getUnlocalizedName() + ".name");
        this.componentList.clear();

    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2, 5, 0);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
