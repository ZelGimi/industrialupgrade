package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.api.widget.FluidDefaultWidget;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.containermenu.ContainerPreCraft;
import com.denfop.containermenu.SlotVirtualPreCraft;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.packet.PacketChangeSameStack;
import com.denfop.network.packet.PacketUpdatePreCraft;
import com.denfop.utils.FluidHandlerFix;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.List;

public class ScreenPreCraft<T extends ContainerPreCraft> extends ScreenMain<ContainerPreCraft> {


    public EditBox[] countField = new EditBox[10];

    public ScreenPreCraft(ContainerPreCraft guiContainer) {
        super(guiContainer, EnumTypeStyle.STORAGE);
        this.componentList.clear();
        this.imageHeight = 190;
        this.imageWidth = 236;
    }

    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
    }

    @Override
    protected void init() {
        super.init();
        for (int i = 0; i < 10; i++) {
            int x = 8 + (i / 5) * 116;
            int y = 12 + (i % 5) * 18;
            this.countField[i] = new EditBox(this.font, this.leftPos + 29 + (i / 5) * 114, this.topPos + 16 + (i % 5) * 18, 85 - 29, 10, Component.literal("")) {
            };
            this.countField[i].setMaxLength(9);
            this.countField[i].setValue("1");
            if (this.container.base.inputItems.sameStackList.get(i) != null && !this.container.base.inputItems.sameStackList.get(i).isEmpty()) {
                this.countField[i].setValue(String.valueOf(this.container.base.inputItems.integerList.get(i)));
            }
            this.countField[i].setCanLoseFocus(true);
            countField[i].setBordered(false);
            countField[i].visible = true;
            this.addWidget(this.countField[i]);
            this.addRenderableWidget(this.countField[i]);
        }
    }

    @Override
    public void updateTick() {
        super.updateTick();
        for (int i = 0; i < 10; i++) {
            if (!this.countField[i].isFocused())
                this.countField[i].setValue(String.valueOf(this.container.base.inputItems.integerList.get(i)));
        }
    }

    @Override
    protected void mouseClicked(int ii, int j, int k) {
        super.mouseClicked(ii, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = ii - xMin;
        int y = j - yMin;
        InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(k);
        if (this.container.base.network == null)
            return;
        for (int i = 0; i < 10; i++) {
            if (this.countField[i].mouseClicked(i, j, k)) {
                countField[i].setFocused(true);
                return;
            } else {
                countField[i].setFocused(false);
            }
        }
        for (int i = 0; i < 10; i++) {
            int x1 = 8 + (i / 5) * 116;
            int y1 = 12 + (i % 5) * 18;
            if (x >= x1 && x <= x1 + 18 && y >= y1 && y <= y1 + 18 && mouseKey.getValue() == 2) {
                changeMode(container.base.inputItems.sameStackList, container.base.inputItems.booleanList, i, true);
                return;
            }
        }
    }

    private void changeMode(List<SameStack> stacks, List<Boolean> flags, int globalIndex, boolean input) {
        if (globalIndex < 0 || globalIndex >= stacks.size()) return;

        SameStack ss = stacks.get(globalIndex);
        if (!ss.isItem() && !ss.isFluid()) return;

        boolean ignoreFluidFlag = flags.get(globalIndex);
        if (ignoreFluidFlag) {
            ignoreFluidFlag = false;
            new PacketChangeSameStack(this.container.base, input, globalIndex, ss, ignoreFluidFlag);
        } else {
            ItemStack stack = ss.getStack();
            if (!stack.isEmpty() && FluidHandlerFix.hasFluidHandler(stack)) {
                ignoreFluidFlag = true;
                FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack).getFluidInTank(0);
                if (!fluidStack.isEmpty()) {
                    ss.setFluidStack(fluidStack);
                    new PacketChangeSameStack(this.container.base, input, globalIndex, ss, ignoreFluidFlag);
                }
            }
        }

    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (int i = 0; i < 10; i++) {
            if (this.container.base.inputItems.sameStackList.get(i) != null && !this.container.base.inputItems.sameStackList.get(i).isEmpty() && this.countField[i].charTyped(codePoint, modifiers)) {
                this.countField[i].setValue(countField[i].getValue().replaceAll("\\D+", ""));
                if (this.countField[i].getValue().isEmpty() || this.countField[i].getValue().startsWith("0")) {
                    continue;
                }

                this.container.base.inputItems.sameStackList.get(i).setAmount(Integer.parseInt(this.countField[i].getValue()));
                new PacketUpdatePreCraft(this.container.player, this.container.base.pos, i, this.container.base.inputItems.sameStackList.get(i));

                return true;
            }
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
            return true;
        }
        for (int i = 0; i < 10; i++) {

            if (this.container.base.inputItems.sameStackList.get(i) != null && !this.container.base.inputItems.sameStackList.get(i).isEmpty() && this.countField[i].keyPressed(keyCode, scanCode, modifiers)) {
                this.countField[i].setValue(this.countField[i].getValue().replaceAll("\\D+", ""));
                if (this.countField[i].getValue().isEmpty() || this.countField[i].getValue().startsWith("0")) {
                    continue;
                }
                this.container.base.inputItems.sameStackList.get(i).setAmount(Integer.parseInt(this.countField[i].getValue()));
                new PacketUpdatePreCraft(this.container.player, this.container.base.pos, i, this.container.base.inputItems.sameStackList.get(i));

                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);

    }

    @Override
    public boolean canRenderSlot(Slot slot) {
        if (slot instanceof SlotVirtualPreCraft)
            return false;
        return true;
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);
        for (int i = 0; i < 10; i++) {
            ItemStack stack = this.container.base.inputItems.get(i);
            int x1 = 8 + (i / 5) * 116;
            int y1 = 12 + (i % 5) * 18;
            if (!stack.isEmpty()) {
                if (this.container.base.inputItems.booleanList.get(i)) {
                    if (stack.getItem() != IUItem.reinforcedFluidCell.getItem()) {
                        FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack).drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                        stack = IUItem.reinforcedFluidCell.getItemStack();
                        fluidStack.setAmount(1);
                        FluidHandlerFix.getFluidHandler(stack).fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                        stack.set(DataComponentsInit.TYPE_RECIPE, true);
                        this.container.base.setItem(i, stack);

                    }
                    FluidStack fluidStack = FluidHandlerFix.getFluidHandler(stack).getFluidInTank(0);
                    if (!fluidStack.isEmpty()) {
                        new FluidDefaultWidget(this, x1 - 1,
                                y1 - 1, fluidStack).drawBackground(poseStack, guiLeft, guiTop);

                    }
                } else {
                    this.drawItem(poseStack, x1, y1, stack);
                }
            }
        }

    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiprecraft.png");

    }

}
