package com.denfop.api.gui;

import com.denfop.gui.GuiCore;

public class CustomButton extends GuiElement<CustomButton>{

    private final IButtonClick handler;

    public CustomButton(GuiCore gui, int x, int y, int width, int height, IButtonClick handler) {
        super(gui,x,y,width,height);
        this.handler = handler;
    }

    @Override
    protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
        if(this.contains(mouseX,mouseY)){
            handler.click(button);
        }
        return super.onMouseClick(mouseX, mouseY, button);

    }

}
