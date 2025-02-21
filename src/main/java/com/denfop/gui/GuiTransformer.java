package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.ImageScreen;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerTransformer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiTransformer extends GuiIU<ContainerTransformer> {

    public String[] mode = new String[]{"", "", "", ""};

    public GuiTransformer(ContainerTransformer container) {
        super(container);
        this.ySize = 219;
        componentList.clear();
        inventory = new GuiComponent(this, 7, 119, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.ALL))
        );
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS_UPGRADE))
        );
        componentList.add(inventory);
        componentList.add(slots);
        this.mode[1] = Localization.translate("Transformer.gui.switch.mode1");
        this.mode[2] = Localization.translate("Transformer.gui.switch.mode2");
        this.mode[3] = Localization.translate("Transformer.gui.switch.mode3");
        this.addElement(new CustomButton(this, 7, 50, 144, 20, container.base, 0, this.mode[1]));
        this.addElement(new CustomButton(this, 7, 72, 144, 20, container.base, 1, this.mode[2]));
        this.addElement(new CustomButton(this, 7, 94, 144, 20, container.base, 2, this.mode[3]));
        this.addComponent(new GuiComponent(this, 153, 28, EnumTypeComponent.CHANGEMODE,
                new Component<>(new ComponentButton(container.base, 3) {


                    @Override
                    public void ClickEvent() {
                        super.ClickEvent();
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                                SoundEvents.UI_BUTTON_CLICK,
                                1.0F
                        ));

                    }
                })
        ));
        this.addElement(new ImageScreen(this, 7, 16, 144, 30));
    }

    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        this.fontRenderer.drawString(Localization.translate("Transformer.gui.Output"), 16, 20, 2157374);
        this.fontRenderer.drawString(Localization.translate("Transformer.gui.Input"), 16, 35, 2157374);
        this.fontRenderer.drawString(this.container.base.getoutputflow() + " " + Localization.translate(
                Constants.ABBREVIATION + ".generic.text.EUt"), 62, 20, 2157374);
        this.fontRenderer.drawString(this.container.base.getinputflow() + " " + Localization.translate(
                Constants.ABBREVIATION + ".generic.text.EUt"), 62, 35, 2157374);
        RenderItem renderItem = this.mc.getRenderItem();
        RenderHelper.enableGUIStandardItemLighting();
        switch (this.container.base.getMode()) {
            case redstone:
                renderItem.renderItemIntoGUI(IUItem.wrench.getDefaultInstance(), 152, 52);
                break;
            case stepdown:
                renderItem.renderItemIntoGUI(IUItem.wrench.getDefaultInstance(), 152, 74);
                break;
            case stepup:
                renderItem.renderItemIntoGUI(IUItem.wrench.getDefaultInstance(), 152, 96);
        }

        RenderHelper.disableStandardItemLighting();
    }

    public void initGui() {
        super.initGui();

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine_main1.png");
    }

}
