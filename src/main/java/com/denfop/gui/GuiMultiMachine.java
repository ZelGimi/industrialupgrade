package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.api.recipe.InventoryMultiRecipes;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerMultiMachine;
import com.denfop.container.SlotInvSlot;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiMultiMachine extends GuiIU<ContainerMultiMachine> {

    private final ContainerMultiMachine container;
    private final GuiComponent process;
    private final List<ItemStack> itemStackList = new ArrayList<>();

    public GuiMultiMachine(ContainerMultiMachine container1) {
        super(container1, container1.base.getMachine().getComponent());
        this.container = container1;
        if (container1.base.getMachine().sizeWorkingSlot == 8) {
            this.xSize += 60;
            this.addElement(new ImageInterface(this, 0, 0, this.xSize, this.ySize));
        } else {
            xSize = 176 + 16;
        }

        this.process = new GuiComponent(this, 0, 0, EnumTypeComponent.MULTI_PROCESS,
                new Component<>(new ComponentProcessRender(container1.base.multi_process, container1.base.getTypeMachine()))
        );
        this.addComponent(new GuiComponent(this, 4, 15, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 10, 45, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        if (this.container.base.getMachine().type != EnumTypeMachines.Centrifuge && container.base.sizeWorkingSlot != 8) {
            this.addComponent(new GuiComponent(this, 27, 47, EnumTypeComponent.COLD,
                    new Component<>(this.container.base.cold)
            ));
        }
        if (this.container.base.tank != null) {
            this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.WATER,
                    new Component<>(this.container.base.tank)
            ));
        }
        if (this.container.base.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.COLD,
                    new Component<>(this.container.base.heat)
            ));
        }
        this.addComponent(new GuiComponent(this, 27, 63, EnumTypeComponent.EXP,
                new Component<>(this.container.base.exp)
        ));
        isBlack = false;

        if (this.container.base.multi_process.quickly) {
            itemStackList.add(new ItemStack(IUItem.module_quickly));
        }
        if (this.container.base.multi_process.modulesize) {
            itemStackList.add(new ItemStack(IUItem.module_stack));
        }
        if (this.container.base.multi_process.modulestorage) {
            itemStackList.add(new ItemStack(IUItem.module_storage));
        }
        if (this.container.base.multi_process.module_infinity_water) {
            itemStackList.add(new ItemStack(IUItem.module_infinity_water));
        }
        if (this.container.base.multi_process.module_separate) {
            itemStackList.add(new ItemStack(IUItem.module_separate));
        }
        if (this.container.base.solartype != null) {
            itemStackList.add(new ItemStack(IUItem.module6, 1, this.container.base.solartype.meta));
        }

        if (this.container.base.cold.upgrade && this.container.base.getTypeMachine() != EnumTypeMachines.Centrifuge) {
            itemStackList.add(new ItemStack(IUItem.coolupgrade, 1, this.container.base.cold.meta));

        }

    }

    @Override
    protected void drawForegroundLayer(final int mouseX, final int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.drawForeground(mouseX, mouseY);
        int i = 0;
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawForeground(mouseX, mouseY);
                    i++;
                }

            }
        }
        i = 0;
        for (ItemStack stack : this.itemStackList) {
            new Area(this, xSize, 5 + i * 18, 16, 16).withTooltip(stack.getDisplayName()).drawForeground(mouseX, mouseY);
            i++;
        }
    }

    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(f, x, y);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(getTexture());
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, 176, this.ySize);
        for (final GuiElement guiElement : this.elements) {
            guiElement.drawBackground(x - this.guiLeft, y - this.guiTop);

        }
        this.mc.getTextureManager().bindTexture(new ResourceLocation("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        this.drawBackground();
        int i = 0;
        for (Slot slot : this.container.inventorySlots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.xPos;
                int yY = slot.yPos;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawBackground(xoffset, yoffset);
                    i++;
                }
            }
        }
        this.mc.getTextureManager().bindTexture(getTexture());
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.fontRenderer.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }


        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.xSize / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        this.fontRenderer.drawString(name, textX, textY, 4210752);


        GlStateManager.popMatrix();

        i = 0;
        for (ItemStack stack : this.itemStackList) {
            new ItemStackImage(this, this.xSize, 5 + i * 18, () -> stack).drawBackground(this.guiLeft, guiTop);
            i++;
        }

    }


    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main.png");
    }

}
