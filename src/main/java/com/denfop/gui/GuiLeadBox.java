package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.container.ContainerLeadBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiLeadBox<T extends ContainerLeadBox> extends GuiIU<ContainerLeadBox> {

    private static final ResourceLocation background = ResourceLocation.tryBuild(Constants.TEXTURES, "textures/gui/guileadbox.png");
    private final String name;

    public GuiLeadBox(ContainerLeadBox container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getDescriptionId());
        this.componentList.clear();
    }


    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        draw(poseStack, this.name, (this.imageWidth - getStringWidth(this.name)) / 2 - 10, 5, 0);
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft(), this.guiTop(), 0, 0, this.imageWidth, this.imageHeight);
        int slots = this.menu.inventorySize;

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
