package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.recipe.InventoryMultiRecipes;
import com.denfop.api.widget.*;
import com.denfop.blockentity.mechanism.EnumTypeMachines;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.containermenu.ContainerMenuMultiMachine;
import com.denfop.containermenu.SlotInvSlot;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScreenMultiMachine<T extends ContainerMenuMultiMachine> extends ScreenMain<ContainerMenuMultiMachine> {

    private final ContainerMenuMultiMachine container;
    private final ScreenWidget process;
    private final List<ItemStack> itemStackList = new ArrayList<>();

    public ScreenMultiMachine(ContainerMenuMultiMachine container1) {
        super(container1, container1.base.getMachine().getComponent());
        this.container = container1;
        if (container1.base.getMachine().sizeWorkingSlot == 8) {
            this.imageWidth += 60;
            this.addWidget(new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight));
        } else {
            imageWidth = 176 + 16;
        }

        this.process = new ScreenWidget(this, 0, 0, EnumTypeComponent.MULTI_PROCESS,
                new WidgetDefault<>(new ComponentProcessRender(container1.base.multi_process, container1.base.getTypeMachine()))
        );
        this.addComponent(new ScreenWidget(this, 4, 15, EnumTypeComponent.SOUND_BUTTON,
                new WidgetDefault<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new ScreenWidget(this, 10, 45, EnumTypeComponent.ENERGY,
                new WidgetDefault<>(this.container.base.energy)
        ));
        if (this.container.base.getMachine().type != EnumTypeMachines.Centrifuge && container.base.sizeWorkingSlot != 8) {
            this.addComponent(new ScreenWidget(this, 27, 47, EnumTypeComponent.COLD,
                    new WidgetDefault<>(this.container.base.cold)
            ));
        }
        if (this.container.base.tank != null)
            this.addComponent(new ScreenWidget(this, 27, 63, EnumTypeComponent.WATER,
                    new WidgetDefault<>(this.container.base.tank)
            ));
        if (this.container.base.getMachine().type == EnumTypeMachines.Centrifuge) {
            this.addComponent(new ScreenWidget(this, 27, 63, EnumTypeComponent.COLD,
                    new WidgetDefault<>(this.container.base.heat)
            ));
        }
        this.addComponent(new ScreenWidget(this, 27, 63, EnumTypeComponent.EXP,
                new WidgetDefault<>(this.container.base.exp)
        ));
        isBlack = false;

        if (this.container.base.multi_process.quickly) {
            itemStackList.add(new ItemStack(IUItem.module_quickly.getItem()));
        }
        if (this.container.base.multi_process.modulesize) {
            itemStackList.add(new ItemStack(IUItem.module_stack.getItem()));
        }
        if (this.container.base.multi_process.modulestorage) {
            itemStackList.add(new ItemStack(IUItem.module_storage.getItem()));
        }
        if (this.container.base.multi_process.module_infinity_water) {
            itemStackList.add(new ItemStack(IUItem.module_infinity_water.getItem()));
        }
        if (this.container.base.multi_process.module_separate) {
            itemStackList.add(new ItemStack(IUItem.module_separate.getItem()));
        }
        if (this.container.base.solartype != null) {
            itemStackList.add(new ItemStack(IUItem.module6.getStack(this.container.base.solartype.meta), 1));
        }

        if (this.container.base.cold.upgrade && this.container.base.getTypeMachine() != EnumTypeMachines.Centrifuge) {
            itemStackList.add(new ItemStack(IUItem.coolupgrade.getStack(this.container.base.cold.meta), 1));

        }

    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int mouseX, final int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        this.drawForeground(poseStack, mouseX, mouseY);
        if (this.container.base.getMachine().type == EnumTypeMachines.FARMER) {
            draw(poseStack, String.valueOf(this.container.base.getFertilizer()), 12, 35, ModUtils.convertRGBcolorToInt(0, 0, 0));
            new TooltipWidget(this, 10, 33, 10, 10).withTooltip(Localization.translate("iu.farmer.fertilizer.info") + "\n" + Localization.translate("iu.farmer.fertilizer.info1")).drawForeground(poseStack, mouseX, mouseY);
        }
        int i = 0;
        for (Slot slot : this.container.slots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.x;
                int yY = slot.y;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawForeground(poseStack, mouseX, mouseY);
                    i++;
                }

            }
        }
        i = 0;
        for (ItemStack stack : this.itemStackList) {
            new TooltipWidget(this, imageWidth, 5 + i * 18, 16, 16).withTooltip(stack.getDisplayName().getString()).drawForeground(poseStack, mouseX, mouseY);
            i++;
        }
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {

        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(getTexture());
        int j = guiLeft;
        int k = guiTop;
        drawTexturedModalRect(poseStack, j, k, 0, 0, 176, this.imageHeight);
        for (final ScreenWidget guiElement : this.elements) {
            guiElement.drawBackground(poseStack, this.guiLeft, this.guiTop);

        }
        bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/infobutton.png"));
        this.drawTexturedRect(poseStack, 3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        bindTexture(getTexture());
        int xoffset = guiLeft;
        int yoffset = guiTop;
        this.drawBackground(poseStack);
        int i = 0;
        for (Slot slot : this.container.slots) {
            if (slot instanceof SlotInvSlot) {
                int xX = slot.x;
                int yY = slot.y;
                SlotInvSlot slotInv = (SlotInvSlot) slot;
                if (slotInv.inventory instanceof InventoryMultiRecipes) {
                    this.process.setIndex(i);
                    this.process.setX(xX);
                    this.process.setY(yY + 19);
                    this.process.drawBackground(poseStack, xoffset, yoffset);
                    i++;
                }
            }
        }
        bindTexture(getTexture());
        String name = Localization.translate(this.container.base.getName());
        int textWidth = this.getStringWidth(name);
        float scale = 1.0f;


        if (textWidth > 120) {
            scale = 120f / textWidth;
        }

        PoseStack pose = poseStack.pose();
        pose.pushPose();
        pose.scale(scale, scale, 1.0f);


        int centerX = this.guiLeft + this.imageWidth / 2;
        int textX = (int) ((centerX / scale) - (textWidth / 2.0f));
        int textY = (int) ((this.guiTop + 6) / scale);


        draw(poseStack, name, textX, textY, 4210752);


        pose.popPose();

        i = 0;
        for (ItemStack stack : this.itemStackList) {
            new ItemStackWidget(this, this.imageWidth, 5 + i * 18, () -> stack).drawBackground(poseStack, this.guiLeft, guiTop);
            i++;
        }

    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine_main.png");
    }

}
