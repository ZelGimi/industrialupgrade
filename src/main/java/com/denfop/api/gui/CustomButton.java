package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class CustomButton extends GuiElement<CustomButton>{

    private final IButtonClick handler;
    private final EnumTypeComponent type;

    public CustomButton(GuiCore gui, int x, int y, IButtonClick handler, EnumTypeComponent typeComponent) {
        super(gui,x,y,typeComponent.getWeight(),typeComponent.getHeight());
        this.handler = handler;
        this.type = typeComponent;
    }
    public void drawBackground(int mouseX, int mouseY) {
        this.getGui().drawTexturedModalRect(
                mouseX + this.getX(),
                mouseY + this.getY(),
                type.getX(),
                type.getY(),
                type.getWeight(),
                type.getHeight()
        );
    }
    @Override
    protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
        if(this.contains(mouseX,mouseY)){
            this.getGui().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            handler.click(button);
        }
        return super.onMouseClick(mouseX, mouseY, button);

    }

}
