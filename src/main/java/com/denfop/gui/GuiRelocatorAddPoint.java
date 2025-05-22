package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.CustomButton;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageScreen;
import com.denfop.api.gui.MouseButton;
import com.denfop.container.ContainerBase;
import com.denfop.items.relocator.ItemStackRelocator;
import com.denfop.items.relocator.Point;
import com.denfop.network.packet.PacketAddRelocatorPoint;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiRelocatorAddPoint extends GuiIU<ContainerBase<ItemStackRelocator>> {

    public GuiTextField textField;

    public GuiRelocatorAddPoint(ContainerBase<ItemStackRelocator> guiContainer) {
        super(guiContainer);
        this.componentList.clear();
        this.xSize = 100;
        this.ySize = 50;
        this.addElement(new ImageInterface(this, 0, 0, this.xSize, ySize));
        this.addElement(new ImageScreen(this, 8, 6, 82, 14));
        this.addElement(new CustomButton(this, 8, 26, 84, 14, null, 0, Localization.translate("button.write")) {
            @Override
            protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
                if (this.visible() && this.contains(mouseX, mouseY)) {
                    this.getGui().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                            SoundEvents.UI_BUTTON_CLICK,
                            1.0F
                    ));
                    new PacketAddRelocatorPoint(mc.player, new Point(textField.getText(), mc.player.getPosition()));
                }
                return true;
            }
        });

    }

    @Override
    public void initGui() {
        super.initGui();
        this.textField = new GuiTextField(0, this.fontRenderer, 14,
                10, 76, 10
        );
        this.textField.setMaxStringLength(50);
        this.textField.setFocused(true);
        this.textField.setEnableBackgroundDrawing(false);
        textField.setCanLoseFocus(false);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    }

    @Override
    protected void drawForegroundLayer(final int par1, final int par2) {
        super.drawForegroundLayer(par1, par2);
        this.textField.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.textField.textboxKeyTyped(typedChar, keyCode);
            return;
        }
        super.keyTyped(typedChar, keyCode);
        this.textField.textboxKeyTyped(typedChar, keyCode);

    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }

}
