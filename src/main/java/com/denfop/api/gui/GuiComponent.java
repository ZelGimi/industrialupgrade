package com.denfop.api.gui;

import com.denfop.Constants;
import com.denfop.componets.*;
import com.denfop.container.SlotInvSlot;
import com.denfop.gui.GuiIC2;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiComponent extends GuiElement<GuiComponent> {

    public static final ResourceLocation commonTexture1 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars.png"
    );
    private final EnumTypeComponent type;
    private final Component<?> component;
    private final GuiIU<?> gui_iu;
    private int index = 0;

    public GuiComponent(GuiIU<?> gui, int x, int y, EnumTypeComponent type, Component<?> component) {
        super(gui, x, y, type.getWeight(), type.getHeight());
        this.type = type;
        this.gui_iu = gui;
        this.component = component;
    }

    private static void addLines(List<String> list, String str) {
        int startPos;
        int pos;
        for (startPos = 0; (pos = str.indexOf(10, startPos)) != -1; startPos = pos + 1) {
            list.add(processText(str.substring(startPos, pos)));
        }

        if (startPos == 0) {
            list.add(processText(str));
        } else {
            list.add(processText(str.substring(startPos)));
        }

    }

    public static void bindCommonTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(commonTexture1);
    }

    public Component<?> getComponent() {
        return component;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getX() {
        return x;

    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;

    }

    public void setY(int y) {
        this.y = y;
    }

    public void buttonClicked(int mouseX, int mouseY) {
        if (component.getComponent() instanceof ComponentButton) {
            if (this.contains(mouseX, mouseY)) {
                ComponentButton button = (ComponentButton) component.getComponent();
                button.ClickEvent();
            }
        }
    }

    public EnumTypeComponent getType() {
        return type;
    }

    public GuiIC2<?> getGui() {
        return this.gui;
    }

    public void drawForeground(int mouseX, int mouseY) {
        if (this.contains(mouseX, mouseY) && !this.suppressTooltip(mouseX, mouseY)) {
            List<String> lines = this.getToolTip();
            if (this.getType().isHasDescription()) {
                String tooltip = this.component.getText(this);
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }
            }

            if (!lines.isEmpty()) {
                this.gui.drawTooltip(mouseX, mouseY, lines);
            }
        }

    }

    public void drawBackground(int mouseX, int mouseY) {
        if (this.component == null || this.component.getComponent() == null) {
            return;
        }
        bindCommonTexture();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.component.getComponent() instanceof ComponentButton) {
            if (this.component.getComponent() instanceof ComponentSoundButton) {
                ComponentSoundButton componentSoundButton = (ComponentSoundButton) this.component.getComponent();
                if (componentSoundButton.getAudioFixer().getEnable()) {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x,
                            mouseY + this.y,
                            type.getX(),
                            type.getY(),
                            type.getWeight(),
                            type.getHeight()
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x,
                            mouseY + this.y,
                            type.getX1(),
                            type.getY1(),
                            type.getWeight(),
                            type.getHeight()
                    );
                }
            }
            return;
        }

        if (!(this.component.getComponent() instanceof ComponentRenderInventory) && !(this.component.getComponent() instanceof ComponentProcessRender)) {
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            this.component.drawBackground(mouseX, mouseY, this);
        } else if (this.component.getComponent() instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.component.getComponent();
            ProcessMultiComponent processMultiComponent = component.getProcess();
            final EnumTypeMachines type_machines = component.getTypeMachines();
            int down;
            int x = 0;
            switch (type_machines) {

                case MACERATOR:
                case COMBMACERATOR:
                    down = 0;

                    break;
                case COMPRESSOR:
                    down = 24;
                    break;
                case ELECTRICFURNACE:
                    down = 48;
                    break;
                case EXTRACTOR:
                    down = 72;
                    break;
                case METALFOMER:
                case ROLLING:
                case CUTTING:
                case EXTRUDING:
                    down = 96;
                    break;
                case RECYCLER:
                case COMBRECYCLER:
                    down = 120;
                    break;
                case FARMER:
                    down = 144;
                    break;
                case ASSAMPLERSCRAP:
                    down = 168;
                    break;
                case OreWashing:
                    down = 0;
                    x = -77;
                    break;
                case Centrifuge:
                    down = 24;
                    x = -77;
                    break;
                case Gearing:
                    down = 48;
                    x = -77;
                    break;
                default:
                    down = 0;
                    break;
            }

            if (!this.gui_iu.isBlack) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX() + x,
                        type.getY() + down,
                        type.getWeight(),
                        type.getHeight()
                );
            } else {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX() - 39,
                        type.getY() + down,
                        type.getWeight(),
                        type.getHeight()
                );
            }
            int progress = (int) (24.0F * processMultiComponent.getProgress(this.index));
            progress = Math.min(progress, 24);
            if (progress >= 0) {
                if (!this.gui_iu.isBlack) {
                    if (x < 0) {
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x,
                                mouseY + this.y,
                                type.getX() + 16 + x + 2,
                                type.getY() + down,
                                type.getWeight(),
                                progress
                        );
                    } else {
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x,
                                mouseY + this.y,
                                type.getX() + 16 + x,
                                type.getY() + down,
                                type.getWeight(),
                                progress
                        );
                    }
                } else {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x,
                            mouseY + this.y,
                            type.getX() + 16 - 39,
                            type.getY() + down,
                            type.getWeight(),
                            progress
                    );
                }
            }
        } else if (this.component.getComponent() instanceof ComponentRenderInventory) {
            ComponentRenderInventory component = (ComponentRenderInventory) this.component.getComponent();
            switch (component.getTypeComponentSlot()) {
                case SLOTS_UPGRADE:
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        if (slot instanceof SlotInvSlot) {
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.invSlot)) {
                                continue;
                            }
                            if (component.getSlotList().contains(slotInvSlot.invSlot)) {
                                int xx = 0;
                                switch (this.type) {
                                    case ADVANCED:
                                        xx = 8;
                                        break;
                                    case IMPROVED:
                                        xx = 16;
                                        break;
                                    case PERFECT:
                                        xx = 24;
                                        break;
                                }
                                this.gui.drawTexturedModalRect(
                                        mouseX + this.x - 4,
                                        mouseY + this.y - 4,
                                        type.getX() - 160 - xx,
                                        type.getY() + 114,
                                        26,
                                        26
                                );
                            } else if (!this.gui_iu.isBlack) {
                                this.gui.drawTexturedModalRect(
                                        mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            } else {
                                this.gui.drawTexturedModalRect(
                                        mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY() + 18,
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                            if (((SlotInvSlot) slot).invSlot instanceof ITypeSlot) {
                                ITypeSlot typeSlot = (ITypeSlot) ((SlotInvSlot) slot).invSlot;
                                final EnumTypeSlot type = typeSlot.getTypeSlot(((SlotInvSlot) slot).index);
                                if (type == null) {
                                    continue;
                                }
                                this.gui.drawTexturedModalRect(
                                        mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        }
                    }
                    break;
                case SLOT:
                    if (!this.gui_iu.isBlack) {
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x,
                                mouseY + this.y,
                                type.getX(),
                                type.getY(),
                                type.getWeight(),
                                type.getHeight()
                        );
                    } else {
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x,
                                mouseY + this.y,
                                type.getX(),
                                type.getY() + 18,
                                type.getWeight(),
                                type.getHeight()
                        );
                    }
                    break;
                case ALL:
                    for (int i = 0; i < 27; i++) {
                        if (!this.gui_iu.isBlack) {
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 18 * (i % 9),
                                    mouseY + this.y + 18 * (i / 9),
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );
                        } else {
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 18 * (i % 9),
                                    mouseY + this.y + 18 * (i / 9),
                                    type.getX(),
                                    type.getY() + 18,
                                    type.getWeight(),
                                    type.getHeight()
                            );
                        }
                    }
                    for (int j = 0; j < 9; j++) {
                        if (!this.gui_iu.isBlack) {
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 18 * (j % 9),
                                    mouseY + this.y + 58,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );
                        } else {
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 18 * (j % 9),
                                    mouseY + this.y + 58,
                                    type.getX(),
                                    type.getY() + 18,
                                    type.getWeight(),
                                    type.getHeight()
                            );
                        }
                    }
                    break;
                case INVENTORY:


                    for (int i = 0; i < 27; i++) {
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x + 18 * (i % 9),
                                mouseY + this.y + 18 * (i / 9),
                                type.getX(),
                                type.getY(),
                                type.getWeight(),
                                type.getHeight()
                        );
                    }

                    break;
                case MAIN_INVENTORY:
                    for (int j = 0; j < 9; j++) {
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x + 18 * (j % 9),
                                mouseY + this.y,
                                type.getX(),
                                type.getY(),
                                type.getWeight(),
                                type.getHeight()
                        );
                    }
                    break;
            }
        }


    }

}
