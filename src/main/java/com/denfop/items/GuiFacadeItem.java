package com.denfop.items;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.gui.GuiIU;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiFacadeItem<T extends ContainerFacadeItem> extends GuiIU<ContainerFacadeItem> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png".toLowerCase());
    private final String name;

    public GuiFacadeItem(ContainerFacadeItem container, final ItemStack itemStack1) {
        super(container);
        this.componentList.clear();
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );

        componentList.add(slots);

        this.name = itemStack1.getDisplayName().getString();
        this.imageHeight = 232;
        this.addElement(new ImageInterface(this, 0, 0, imageWidth, imageHeight));
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        this.font.draw(poseStack, this.name, (this.imageWidth - this.getStringWidth(this.name)) / 2 - 10, 11, 0);
    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(poseStack, this.guiLeft, this.guiTop, 0, 0, this.imageWidth, this.imageHeight);

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
