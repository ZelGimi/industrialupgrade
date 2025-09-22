package com.denfop.items.armour.special;

import com.denfop.Constants;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageInterface;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.gui.GuiIU;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLegsBags extends GuiIU<ContainerLegsBags> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png");
    private final String name;

    public GuiLegsBags(ContainerLegsBags container, final ItemStack itemStack1) {
        super(container);

        this.name = itemStack1.getDisplayName();
        this.ySize = 232;
        this.componentList.clear();
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );

        componentList.add(slots);
        this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        this.fontRenderer.drawString(this.name, (this.xSize - this.fontRenderer.getStringWidth(this.name)) / 2 - 10, 11, 0);
    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        int slots = this.container.inventorySize;
        slots = slots / 9;

        int col;
        for (col = 0; col < slots; ++col) {
            for (int col1 = 0; col1 < 9; ++col1) {
                this.drawTexturedModalRect(this.guiLeft + 7 + col1 * 18, this.guiTop + 23 + col * 18, 176, 0, 18, 18);
            }
        }
    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
