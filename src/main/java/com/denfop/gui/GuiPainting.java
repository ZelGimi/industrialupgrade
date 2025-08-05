package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ItemStackImage;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.items.ItemPaints;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GuiPainting<T extends ContainerDoubleElectricMachine> extends GuiIU<ContainerDoubleElectricMachine> {

    public final ContainerDoubleElectricMachine container;

    public GuiPainting(ContainerDoubleElectricMachine container1) {
        super(container1);
        this.container = container1;
        this.componentList.clear();
        this.addComponent(new GuiComponent(this, 5, 14, EnumTypeComponent.SOUND_BUTTON,
                new Component<>(new ComponentSoundButton(this.container.base, 10, this.container.base))
        ));
        this.addComponent(new GuiComponent(this, 42, 58, EnumTypeComponent.ENERGY,
                new Component<>(this.container.base.energy)
        ));
        this.addElement(new ItemStackImage(this, 62, 57, () -> ItemStack.EMPTY) {
            @Override
            public void drawForeground(GuiGraphics poseStack, final int mouseX, final int mouseY) {

            }

            @Override
            public void drawBackground(GuiGraphics poseStack, final int mouseX, final int mouseY) {
                if (container.base.output == null) {
                    return;
                }
                super.drawBackground(poseStack, mouseX, mouseY);
                ItemStack stack = container.base.inputSlotA.get(0).getItem() instanceof ItemPaints
                        ? container.base.inputSlotA.get(1) : container.base.inputSlotA.get(0);
                stack = stack.copy();
                stack = ModUtils.mode(container.base.output.getRecipe().output.metadata, stack);
                if (!ModUtils.isEmpty(stack)) {

                    this.gui.drawItemStack(poseStack, this.x, this.y, stack);
                }

            }

            @Override
            public boolean visible() {
                return container.base.output != null;
            }
        });
    }

    private static List<String> getInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Localization.translate("iu.paintinginformation1"));
        ret.add(Localization.translate("iu.paintinginformation2"));
        ret.add(Localization.translate("iu.paintinginformation3"));
        ret.add(Localization.translate("iu.paintinginformation4"));
        ret.add(Localization.translate("iu.paintinginformation5"));


        return ret;
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        handleUpgradeTooltip1(par1, par2);

        if (!this.container.base.inputSlotA.get(1).isEmpty() && !this.container.base.inputSlotA
                .get(0)
                .isEmpty() && this.container.base.output != null) {
            draw(poseStack, ModUtils.mode(this.container.base.output.getRecipe().output.items.get(0)), 80, 63, 4210752);
        }
    }

    private void handleUpgradeTooltip1(int mouseX, int mouseY) {
        if (mouseX >= 165 && mouseX <= 175 && mouseY >= 0 && mouseY <= 12) {
            List<String> text = new ArrayList<>();
            text.add(Localization.translate("iu.paintinginformation"));
            List<String> compatibleUpgrades = getInformation();
            Iterator<String> var5 = compatibleUpgrades.iterator();
            String itemstack;
            while (var5.hasNext()) {
                itemstack = var5.next();
                text.add(itemstack);
            }

            this.drawTooltip(mouseX - 80, mouseY, text);
        }
    }

    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        bindTexture(getTexture());

        bindTexture(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/infobutton.png"));
        this.drawTexturedModalRect(poseStack, this.guiLeft() + 165, this.guiTop(), 0, 0, 10, 10);
        bindTexture(this.getTexture());


        int progress = (int) (10 * this.container.base.componentProgress.getBar());


        if (progress > 0) {
            drawTexturedModalRect(poseStack, this.guiLeft() + 79, this.guiTop() + 38, 179, 34, progress, 13);
        }


    }


    public ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/GUIPainter.png".toLowerCase());
    }

}
