package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.widget.*;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuTransformer;
import com.denfop.utils.Localization;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenTransformer<T extends ContainerMenuTransformer> extends ScreenMain<ContainerMenuTransformer> {

    public String[] mode = new String[]{"", "", "", ""};

    public ScreenTransformer(ContainerMenuTransformer container) {
        super(container);
        this.imageHeight = 219;
        componentList.clear();
        inventory = new ScreenWidget(this, 7, 119, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new ScreenWidget(this, 0, 0, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.mode[1] = Localization.translate("Transformer.gui.switch.mode1");
        this.mode[2] = Localization.translate("Transformer.gui.switch.mode2");
        this.mode[3] = Localization.translate("Transformer.gui.switch.mode3");
        this.addWidget(new ButtonWidget(this, 7, 50, 144, 20, container.base, 0, this.mode[1]));
        this.addWidget(new ButtonWidget(this, 7, 72, 144, 20, container.base, 1, this.mode[2]));
        this.addWidget(new ButtonWidget(this, 7, 94, 144, 20, container.base, 2, this.mode[3]));
        this.addComponent(new ScreenWidget(this, 153, 28, EnumTypeComponent.CHANGEMODE,
                new WidgetDefault<>(new ComponentButton(container.base, 3) {


                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
                        );

                    }
                })
        ));
        this.addWidget(new ImageScreenWidget(this, 7, 16, 144, 30));
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int mouseX, int mouseY) {
        super.drawForegroundLayer(poseStack, mouseX, mouseY);
        draw(poseStack, Localization.translate("Transformer.gui.Output"), 16, 20, 2157374);
        draw(poseStack, Localization.translate("Transformer.gui.Input"), 16, 35, 2157374);
        draw(poseStack, this.container.base.getoutputflow() + " " + Localization.translate(
                Constants.ABBREVIATION + ".generic.text.EUt"), 62, 20, 2157374);
        draw(poseStack, this.container.base.getinputflow() + " " + Localization.translate(
                Constants.ABBREVIATION + ".generic.text.EUt"), 62, 35, 2157374);
        switch (this.container.base.getMode()) {
            case redstone:
                new ItemWidget(this, 152, 52, () -> new ItemStack(IUItem.wrench.getItem())).drawForeground(poseStack, guiLeft, guiTop);
                break;
            case stepdown:
                new ItemWidget(this, 152, 74, () -> new ItemStack(IUItem.wrench.getItem())).drawForeground(poseStack, guiLeft, guiTop);

                break;
            case stepup:
                new ItemWidget(this, 152, 96, () -> new ItemStack(IUItem.wrench.getItem())).drawForeground(poseStack, guiLeft, guiTop);
                break;
        }

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
