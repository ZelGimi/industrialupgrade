package com.denfop.api.gui;

import com.denfop.gui.GuiCore;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.utils.ModUtils;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;

public class CustomButton extends GuiElement<CustomButton> {


    private final EnumTypeComponent type;
    private final String text;
    private final int event;
    private final TileEntityBlock tile;
    private final int x1;
    private final int y1;
    private boolean highlighted = false;

    public CustomButton(
            GuiCore gui, int x, int y, int x1, int y1, TileEntityBlock entityBlock, int event,
            String text
    ) {
        super(gui, x, y, EnumTypeComponent.BUTTON.getWeight(), EnumTypeComponent.BUTTON.getHeight());
        this.text = text;
        this.event = event;
        this.tile = entityBlock;
        this.type = EnumTypeComponent.BUTTON;
        this.x1 = x1;
        this.y1 = y1;

    }

    public String getText() {
        return text;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x <= this.x + x1 && y >= this.y && y <= this.y + y1;
    }

    @Override
    public void drawForeground(int mouseX, int mouseY) {
        super.drawForeground(mouseX, mouseY);
        highlighted = this.contains(mouseX, mouseY);

    }

    public void drawBackground(int mouseX, int mouseY) {
        if (this.visible()) {
            if (highlighted) {
                bindCommonTexture2();
                GlStateManager.color(1, 1, 1, 1);
                mouseX = gui.guiLeft;
                mouseY = gui.guiTop;
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX(),
                        mouseY + this.getY(),
                        1,
                        97,
                        Math.min(199, x1 - 1),
                        2
                );
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX() + x1 - 5,
                        mouseY + this.getY(),
                        197,
                        97,
                        6,
                        2
                );
                int height = y1 - 4;
                int modulo = height % 16;
                if (height / 16 > 0) {
                    for (int i = mouseY + this.getY() + 2; i < mouseY + this.getY() + y1 - 2; i += 16) {
                        this.getGui().drawTexturedModalRect(
                                mouseX + this.getX(),
                                i,
                                1,
                                99,
                                Math.min(199, x1 - 1),
                                16
                        );
                        this.getGui().drawTexturedModalRect(
                                mouseX + this.getX() + x1 - 5,
                                i,
                                197,
                                99,
                                6,
                                16
                        );
                    }
                }
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX(),
                        mouseY + this.getY() + 2 + height / 16,
                        1,
                        99,
                        Math.min(199, x1 - 1),
                        modulo
                );
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX() + x1 - 5,
                        mouseY + this.getY() + 2,
                        197,
                        99,
                        6,
                        modulo
                );
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX(),
                        mouseY + this.getY() + y1 - 2,
                        1,
                        116,
                        Math.min(199, x1 - 1),
                        2
                );
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX() + x1 - 5,
                        mouseY + this.getY() + y1 - 2,
                        197,
                        116,
                        6,
                        2
                );
            } else {
                bindCommonTexture2();
                GlStateManager.color(1, 1, 1, 1);
                mouseX = gui.guiLeft;
                mouseY = gui.guiTop;
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX(),
                        mouseY + this.getY(),
                        1,
                        75,
                        Math.min(199, x1 - 1),
                        2
                );
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX() + x1 - 5,
                        mouseY + this.getY(),
                        197,
                        75,
                        6,
                        2
                );
                int height = y1 - 4;
                int modulo = height % 16;
                if (height / 16 > 0) {
                    for (int i = mouseY + this.getY() + 2; i < mouseY + this.getY() + y1 - 2; i += 16) {
                        this.getGui().drawTexturedModalRect(
                                mouseX + this.getX(),
                                i,
                                1,
                                77,
                                Math.min(199, x1 - 1),
                                16
                        );
                        this.getGui().drawTexturedModalRect(
                                mouseX + this.getX() + x1 - 5,
                                i,
                                197,
                                77,
                                6,
                                16
                        );
                    }
                }
                if (modulo > 0) {
                    this.getGui().drawTexturedModalRect(
                            mouseX + this.getX(),
                            mouseY + this.getY() + 2 + height / 16,
                            1,
                            77,
                            Math.min(199, x1 - 1),
                            modulo
                    );
                    this.getGui().drawTexturedModalRect(
                            mouseX + this.getX() + x1 - 5,
                            mouseY + this.getY() + 2,
                            197,
                            77,
                            6,
                            modulo
                    );
                }
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX(),
                        mouseY + this.getY() + y1 - 2,
                        1,
                        94,
                        Math.min(199, x1 - 1),
                        2
                );
                this.getGui().drawTexturedModalRect(
                        mouseX + this.getX() + x1 - 5,
                        mouseY + this.getY() + y1 - 2,
                        197,
                        94,
                        6,
                        2
                );
            }
            if (!this.highlighted) {
                this.getGui().drawXCenteredString(this.getX() + x1 / 2, this.getY() + y1 / 2 - 4, getText(), 4210752, false);
            } else {
                this.getGui().drawXCenteredString(this.getX() + x1 / 2, this.getY() + y1 / 2 - 4, getText(),
                        ModUtils.convertRGBcolorToInt(255, 255, 255), false
                );
            }

        }
    }

    @Override
    protected boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
        if (this.visible() && this.contains(mouseX, mouseY)) {
            this.getGui().mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(
                    SoundEvents.UI_BUTTON_CLICK,
                    1.0F
            ));
            new PacketUpdateServerTile(tile, event);
        }
        return super.onMouseClick(mouseX, mouseY, button);

    }

}
