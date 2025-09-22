package com.denfop.api.gui;

import com.denfop.Constants;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.recipe.InventoryMultiRecipes;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.vein.Vein;
import com.denfop.componets.BioProcessMultiComponent;
import com.denfop.componets.ComponentBioProcessRender;
import com.denfop.componets.ComponentButton;
import com.denfop.componets.ComponentProcessRender;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.ComponentSoundButton;
import com.denfop.componets.ComponentSteamProcessRender;
import com.denfop.componets.ComponentTimer;
import com.denfop.componets.ComponentValue;
import com.denfop.componets.PressureComponent;
import com.denfop.componets.ProcessMultiComponent;
import com.denfop.componets.SteamProcessMultiComponent;
import com.denfop.container.SlotInvSlot;
import com.denfop.container.SlotVirtual;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiIU;
import com.denfop.invslot.Inventory;
import com.denfop.invslot.InventoryUpgrade;
import com.denfop.tiles.mechanism.EnumTypeMachines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiComponent extends GuiElement {

    public static final ResourceLocation commonTexture1 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars.png"
    );
    public static final ResourceLocation commonTexture2 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars.png"
    );
    public static final ResourceLocation commonTexture5 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/slot_render.png"
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

    public GuiComponent(GuiIU<?> gui, int x, int y, int width, int height, Component<?> component) {
        super(gui, x, y, width, height);
        this.type = null;
        this.gui_iu = gui;
        this.component = component;
    }

    public static void addLines(List<String> list, String str) {
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

    public boolean visible() {
        return true;
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

    public GuiCore<?> getGui() {
        return this.gui;
    }

    public void drawForeground(int mouseX, int mouseY) {
        if (this.contains(mouseX, mouseY)  && visible()) {
            List<String> lines = this.getToolTip();
            if (this.getType() == null || this.getType().isHasDescription()) {
                String tooltip = this.component.getText(this);
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }
            }

            if (!lines.isEmpty()) {
                this.gui.drawTooltip(mouseX, mouseY, lines);
            }
        }
        if (this.component.getComponent() instanceof PressureComponent) {
            PressureComponent pressureComponent = (PressureComponent) this.component.getComponent();
            this.gui_iu.getFontRenderer().drawString(String.valueOf(pressureComponent.storage), this.x + 2, this.y + 2,
                    4210752
            );
            if (mouseX >= this.x - 2 && mouseX <= this.x + 4 + this.gui_iu.getStringWidth(String.valueOf(pressureComponent.storage)) && mouseY >= this.y + 2 && mouseY <= this.y + 10) {
                List<String> lines = this.getToolTip();

                String tooltip = this.component.getText(this);
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }


                if (!lines.isEmpty()) {
                    this.gui.drawTooltip(mouseX, mouseY, lines);
                }
            }
        }
    }

    public void renderBar(int mouseX, int mouseY, double bar) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(commonTexture1);
        if (this.component.getComponent() instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.component.getComponent();
            final EnumTypeMachines type_machines = component.getTypeMachines();
            int down;
            int x = 0;
            int xx = 0;
            int yy = 0;
            int xx1 = 0;
            int yy1 = 0;
            int xx2 = 0;
            switch (type_machines) {

                case MACERATOR:
                case COMBMACERATOR:
                    down = 0;
                    xx -= 2;
                    break;
                case COMPRESSOR:
                    down = 24;
                    xx1 += 3;
                    yy -= 2;
                    yy1 += 1;
                    break;
                case ELECTRICFURNACE:
                    down = 54;
                    xx -= 2;
                    break;
                case EXTRACTOR:
                    down = 79;
                    xx -= 1;
                    xx1 += 2;
                    x += 1;
                    break;
                case METALFOMER:
                case ROLLING:
                case CUTTING:
                case EXTRUDING:
                    down = 68;
                    xx -= 1;
                    x += 37;
                    xx1 += 0;
                    break;
                case RECYCLER:
                case COMBRECYCLER:
                    down = 90;
                    xx -= 1;
                    x += 36;
                    xx1 += 2;
                    break;
                case FARMER:
                    down = 112;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case ASSAMPLERSCRAP:
                    down = 136;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case OreWashing:
                    down = 46;
                    xx -= 1;
                    x += 36;
                    yy -= 2;
                    xx1 += 2;
                    break;
                case Centrifuge:
                    down = 160;
                    xx -= 1;
                    x += 38;
                    yy -= 1;
                    xx1 -= 1;
                    break;
                case Gearing:
                    down = 183;
                    xx -= 1;
                    x += 35;
                    xx1 += 4;
                    yy1 -= 5;
                    yy += 2;
                    xx2 += 1;
                    break;
                default:
                    down = 0;
                    break;
            }

            this.gui.drawTexturedModalRect(
                    mouseX + this.x + xx,
                    mouseY + this.y + yy,
                    type.getX() + x,
                    type.getY() + down,
                    type.getWeight() + 1 + xx2,
                    type.getHeight() + yy1
            );
            int progress = (int) ((24.0F + yy1) * bar);
            progress = Math.min(progress, 24 + yy1);
            if (progress >= 0) {
                if (x < 0) {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                }
            }
        } else {
            if (this.type.isNextBar()) {
                bindCommonTexture2();
            }
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            if (this.type.getRender() == EnumTypeRender.WEIGHT) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (bar * type.getWeight()),
                        type.getHeight()
                );
            }
            if (this.type.getRender() == EnumTypeRender.HEIGHT) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (type.getWeight()),
                        (int) (bar * type.getHeight())
                );
            }
        }
    }

    public void drawBackground(int mouseX, int mouseY) {
        if (this.component == null || this.component.getComponent() == null || !visible() || this.type == null) {
            return;
        }
        bindCommonTexture();
        if (this.type.isNextBar()) {
            bindCommonTexture2();
        }
        if (this.type.isSteam()) {
            bindCommonTexture3();
        }
        if (this.type.isBio()) {
            bindCommonTexture4();
        }
        if (this.type.isSpace()) {
            bindCommonTexture5();
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.component.getComponent() instanceof ComponentCustomizeSize) {
            ComponentCustomizeSize componentSoundButton = (ComponentCustomizeSize) this.component.getComponent();
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    componentSoundButton.getWidth(),
                    componentSoundButton.getHeight()
            );
        } else if (this.component.getComponent() instanceof ComponentButton) {
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
            } else {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX(),
                        type.getY(),
                        type.getWeight(),
                        type.getHeight()
                );
                ComponentButton button = (ComponentButton) this.component.getComponent();
                if (button.active()) {
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

        if (!(this.component.getComponent() instanceof ComponentRenderInventory) && !(this.component.getComponent() instanceof ComponentProcessRender) && !(this.component.getComponent() instanceof ComponentValue) && !(this.component.getComponent() instanceof ComponentSteamProcessRender) && !(this.component.getComponent() instanceof ComponentBioProcessRender) && !(this.component.getComponent() instanceof ComponentTimer) && !(this.component.getComponent() instanceof ComponentProgress) && !(this.component.getComponent() instanceof Vein)) {
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            this.component.drawBackground(mouseX, mouseY, this);
        } else if (this.component.getComponent() instanceof ComponentProgress) {
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            ComponentProgress component = (ComponentProgress) this.component.getComponent();
            double scale = component.getBar();
            if (this.type.getRender() == EnumTypeRender.WEIGHT) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (Math.min(1, scale) * type.getWeight()),
                        type.getHeight()
                );
            }
            if (this.type.getRender() == EnumTypeRender.HEIGHT) {
                if (this.type != EnumTypeComponent.FIRE) {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x,
                            mouseY + this.y,
                            type.getX1(),
                            type.getY1(),
                            (int) (type.getWeight()),
                            (int) (scale * type.getHeight())
                    );
                } else {


                    scale *= type.getHeight();
                    int chargeLevel = (int) scale;
                    this.gui.drawTexturedModalRect(
                            mouseX + this.getX() + type.getEndX(),
                            mouseY + type.getEndY() + this.getY() + type
                                    .getHeight() - chargeLevel,
                            type.getX1(),
                            type.getY1() + type.getHeight() - chargeLevel,
                            type.getWeight(),
                            chargeLevel
                    );
                }
            }
        } else if (this.component.getComponent() instanceof Vein) {
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            Vein component = (Vein) this.component.getComponent();
            double scale = component.getCol() * 1D / component.getMaxCol();
            if (this.type.getRender() == EnumTypeRender.WEIGHT) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (Math.min(1, scale) * type.getWeight()),
                        type.getHeight()
                );
            }
            if (this.type.getRender() == EnumTypeRender.HEIGHT) {
                scale *= type.getHeight();
                int chargeLevel = (int) scale;
                this.gui.drawTexturedModalRect(
                        mouseX + this.getX() + type.getEndX(),
                        mouseY + type.getEndY() + this.getY() + type
                                .getHeight() - chargeLevel,
                        type.getX1(),
                        type.getY1() + type.getHeight() - chargeLevel,
                        type.getWeight(),
                        chargeLevel
                );
            }
        } else if (this.component.getComponent() instanceof ComponentTimer) {
            this.gui.drawTexturedModalRect(
                    mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            ComponentTimer component = (ComponentTimer) this.component.getComponent();
            final double scale = component.getTimes();
            if (this.type.getRender() == EnumTypeRender.WEIGHT) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (scale * type.getWeight()),
                        type.getHeight()
                );
            }
            if (this.type.getRender() == EnumTypeRender.HEIGHT) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (type.getWeight()),
                        (int) (scale * type.getHeight())
                );
            }
        } else if (this.component.getComponent() instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.component.getComponent();
            ProcessMultiComponent processMultiComponent = component.getProcess();
            final EnumTypeMachines type_machines = component.getTypeMachines();
            int down;
            int x = 0;
            int xx = 0;
            int yy = 0;
            int xx1 = 0;
            int yy1 = 0;
            int xx2 = 0;
            switch (type_machines) {

                case MACERATOR:
                case COMBMACERATOR:
                    down = 0;
                    xx -= 2;
                    break;
                case COMPRESSOR:
                    down = 24;
                    xx1 += 3;
                    yy -= 2;
                    yy1 += 1;
                    break;
                case ELECTRICFURNACE:
                    down = 54;
                    xx -= 2;
                    xx1 += 2;
                    break;
                case EXTRACTOR:
                    down = 79;
                    xx -= 1;
                    xx1 += 2;
                    x += 1;
                    break;
                case METALFOMER:
                case ROLLING:
                case CUTTING:
                case EXTRUDING:
                    down = 68;
                    xx -= 1;
                    x += 37;
                    xx1 += 0;
                    break;
                case RECYCLER:
                case COMBRECYCLER:
                    down = 90;
                    xx -= 1;
                    x += 36;
                    xx1 += 2;
                    break;
                case FARMER:
                    down = 112;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case ASSAMPLERSCRAP:
                    down = 136;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case OreWashing:
                    down = 46;
                    xx -= 1;
                    x += 36;
                    yy -= 2;
                    xx1 += 2;
                    break;
                case Centrifuge:
                    down = 160;
                    xx -= 1;
                    x += 38;
                    yy -= 1;
                    xx1 -= 1;
                    break;
                case Gearing:
                    down = 183;
                    xx -= 1;
                    x += 35;
                    xx1 += 4;
                    yy1 -= 5;
                    yy += 2;
                    xx2 += 1;
                    break;
                default:
                    down = 0;
                    break;
            }

            this.gui.drawTexturedModalRect(
                    mouseX + this.x + xx,
                    mouseY + this.y + yy,
                    type.getX() + x,
                    type.getY() + down,
                    type.getWeight() + 1 + xx2,
                    type.getHeight() + yy1
            );
            int progress = (int) ((24.0F + yy1) * processMultiComponent.getProgress(this.index));
            progress = Math.min(progress, 24 + yy1);
            if (progress >= 0) {
                if (x < 0) {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                }
            }
        } else if (this.component.getComponent() instanceof ComponentSteamProcessRender) {
            ComponentSteamProcessRender component = (ComponentSteamProcessRender) this.component.getComponent();
            SteamProcessMultiComponent processMultiComponent = component.getProcess();
            final EnumTypeMachines type_machines = component.getTypeMachines();
            int down;
            int x = 0;
            int xx = 0;
            int yy = 0;
            int xx1 = 0;
            int yy1 = 0;
            int xx2 = 0;
            switch (type_machines) {

                case MACERATOR:
                case COMBMACERATOR:
                    down = 0;
                    yy1 -= 1;
                    xx1 += 2;
                    break;
                case COMPRESSOR:
                    down = 23;
                    xx1 += 2;
                    yy -= 1;
                    yy1 += 1;
                    break;
                case ELECTRICFURNACE:
                    down = 54;
                    xx -= 2;
                    xx1 += 2;
                    break;
                case EXTRACTOR:
                    down = 87;
                    xx1 += 1;
                    break;
                case METALFOMER:
                case ROLLING:
                case CUTTING:
                case EXTRUDING:
                    down = 87 + 24;
                    yy += 4;
                    xx1 += 1;
                    break;
                case RECYCLER:
                case COMBRECYCLER:
                    down = 90;
                    xx -= 1;
                    x += 36;
                    xx1 += 2;
                    break;
                case FARMER:
                    down = 112;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case ASSAMPLERSCRAP:
                    down = 136;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case OreWashing:
                    down = 46;
                    xx -= 1;
                    x += 36;
                    yy -= 2;
                    xx1 += 2;
                    break;
                case Centrifuge:
                    down = 160;
                    xx -= 1;
                    x += 38;
                    yy -= 1;
                    xx1 -= 1;
                    break;
                case Gearing:
                    down = 183;
                    xx -= 1;
                    x += 35;
                    xx1 += 4;
                    yy1 -= 5;
                    yy += 2;
                    xx2 += 1;
                    break;
                default:
                    down = 0;
                    break;
            }

            this.gui.drawTexturedModalRect(
                    mouseX + this.x + xx,
                    mouseY + this.y + yy,
                    type.getX() + x,
                    type.getY() + down,
                    type.getWeight() + 1 + xx2,
                    type.getHeight() + yy1
            );
            int progress = (int) ((24.0F + yy1) * processMultiComponent.getProgress(this.index));
            progress = Math.min(progress, 24 + yy1);
            if (progress >= 0) {
                if (x < 0) {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                }
            }
        } else if (this.component.getComponent() instanceof ComponentBioProcessRender) {
            ComponentBioProcessRender component = (ComponentBioProcessRender) this.component.getComponent();
            BioProcessMultiComponent processMultiComponent = component.getProcess();
            final EnumTypeMachines type_machines = component.getTypeMachines();
            int down;
            int x = 0;
            int xx = 0;
            int yy = 0;
            int xx1 = 0;
            int yy1 = 0;
            int xx2 = 0;
            int weight = 0;
            switch (type_machines) {

                case MACERATOR:
                case COMBMACERATOR:
                    down = 0;
                    yy1 -= 1;
                    xx1 += 2;
                    break;
                case COMPRESSOR:
                    down = 23;
                    xx1 += 2;
                    yy -= 1;
                    yy1 += 1;
                    break;
                case ELECTRICFURNACE:
                    down = 47;
                    xx1 += 2;
                    yy -= 1;
                    yy1 += 0;
                    break;
                case EXTRACTOR:
                    down = 87;
                    xx1 += 1;
                    break;
                case METALFOMER:
                case ROLLING:
                case CUTTING:
                case EXTRUDING:
                    down = 87 + 24;
                    yy += 4;
                    xx1 += 1;
                    yy1 -= 1;
                    break;
                case RECYCLER:
                case COMBRECYCLER:
                    down = 90;
                    xx -= 1;
                    x += 36;
                    xx1 += 2;
                    break;
                case FARMER:
                    down = 112;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case ASSAMPLERSCRAP:
                    down = 136;
                    xx -= 1;
                    x += 38;
                    xx1 -= 1;
                    break;
                case OreWashing:
                    down = 87 + 24 + 20;
                    yy += 0;
                    xx -= 1;
                    xx1 += 1;
                    xx2 -= 1;
                    break;
                case Centrifuge:
                    down = 87 + 24 + 44;
                    yy += 1;
                    xx -= 1;
                    xx1 += 1;
                    xx2 -= 1;
                    break;
                case Gearing:
                    down = 87 + 24 + 44 + 27;
                    yy += 1;
                    xx -= 1;
                    xx1 += 2;
                    xx2 += 1;
                    weight += 1;
                    break;
                default:
                    down = 0;
                    break;
            }

            this.gui.drawTexturedModalRect(
                    mouseX + this.x + xx,
                    mouseY + this.y + yy,
                    type.getX() + x,
                    type.getY() + down,
                    type.getWeight() + 1 + xx2,
                    type.getHeight() + yy1
            );
            int progress = (int) ((24.0F + yy1) * processMultiComponent.getProgress(this.index));
            progress = Math.min(progress, 24 + yy1);
            if (progress >= 0) {
                if (x < 0) {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1 + weight,
                            progress
                    );
                }
            }
        } else if (this.component.getComponent() instanceof ComponentValue) {
            ComponentValue componentValue = (ComponentValue) this.component.getComponent();
            if (this.type.isNextBar()) {
                bindCommonTexture2();
            }
            if (this.getType() == EnumTypeComponent.CIRCLE_BAR) {
                this.gui.drawTexturedModalRect(
                        mouseX + this.x,
                        mouseY + this.y,
                        type.getX(),
                        type.getY(),
                        type.getWeight(),
                        type.getHeight()
                );
                // 51 34   0 0       213 2 0 0
                for (int i = 0; i <= (Integer) componentValue.getValue(); i++) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    switch (i) {
                        case 1:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 16, mouseY + this.y, type.getX1() + 16,
                                    type.getY1()
                                    , 2, 6
                            );
                            break;
                        case 2:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 17 + 1, mouseY + this.y, type.getX1() + 17 + 1,
                                    type.getY1(), 2, 6
                            );
                            break;
                        case 3:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 19 + 1, mouseY + this.y + 1, type.getX1() + 19 + 1,
                                    type.getY1() + 1, 2, 6
                            );
                            break;
                        case 4:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 21 + 1, mouseY + this.y + 2, type.getX1() + 21 + 1,
                                    type.getY1() + 2, 2, 7
                            );
                            break;
                        case 5:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 15 + 8 + 1,
                                    mouseY + this.y + 3,
                                    type.getX1() + 15 + 8 + 1,
                                    type.getY1() + 3,
                                    4,
                                    4
                            );
                            break;
                        case 6:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 59 - 51 + 15 + 1, mouseY + this.y + 41 - 34,
                                    type.getX1() + 221 - 213 + 15 + 1, type.getY1() - 2 + 9, 6, 2
                            );
                            break;
                        case 7:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 59 - 51 + 15 + 1,
                                    mouseY + this.y + 43 - 34,
                                    type.getX1() + 221 - 213 + 15 + 1,
                                    type.getY1() - 2 + 11,
                                    7,
                                    2
                            );
                            break;
                        case 8:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 60 - 51 + 15 + 1,
                                    mouseY + this.y + 45 - 34,
                                    type.getX1() + 222 - 213 + 15 + 1,
                                    type.getY1() - 2 + 13,
                                    6,
                                    2
                            );
                            break;
                        case 9:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 61 - 51 + 15 + 1,
                                    mouseY + this.y + 47 - 34,
                                    type.getX1() + 223 - 213 + 15 + 1,
                                    type.getY1() - 2 + 15,
                                    6,
                                    2
                            );
                            break;
                        case 10:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 61 - 51 + 15 + 1,
                                    mouseY + this.y + 49 - 34,
                                    type.getX1() + 223 - 213 + 15 + 1,
                                    type.getY1() - 2 + 17,
                                    6,
                                    2
                            );
                            break;
                        case 11:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 61 - 51 + 15 + 1,
                                    mouseY + this.y + 51 - 34,
                                    type.getX1() + 223 - 213 + 15 + 1,
                                    type.getY1() - 2 + 19,
                                    6,
                                    2
                            );
                            break;
                        case 12:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 60 - 51 + 15 + 1,
                                    mouseY + this.y + 53 - 34,
                                    type.getX1() + 222 - 213 + 15 + 1,
                                    type.getY1() - 2 + 21,
                                    6,
                                    2
                            );
                            break;
                        case 13:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 58 - 51 + 15 + 1,
                                    mouseY + this.y + 55 - 34,
                                    type.getX1() + 220 - 213 + 15 + 1,
                                    type.getY1() - 2 + 23,
                                    7,
                                    2
                            );
                            break;
                        case 14:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 57 - 51 + 15 + 1,
                                    mouseY + this.y + 57 - 34,
                                    type.getX1() + 219 - 213 + 15 + 1,
                                    type.getY1() - 2 + 25,
                                    7,
                                    2
                            );
                            break;
                        case 15:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 57 - 51 + 15 + 1,
                                    mouseY + this.y + 59 - 34,
                                    type.getX1() + 219 - 213 + 15 + 1,
                                    type.getY1() - 2 + 27,
                                    5,
                                    4
                            );
                            break;
                        case 16:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 55 - 51 + 15 + 1,
                                    mouseY + this.y + 58 - 34,
                                    type.getX1() + 217 - 213 + 15 + 1,
                                    type.getY1() - 2 + 26,
                                    2,
                                    6
                            );
                            break;
                        case 17:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 53 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 215 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6
                            );
                            break;
                        case 18:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 51 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 213 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6
                            );
                            break;
                        case 19:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 49 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 211 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6
                            );
                            break;
                        case 20:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 47 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 209 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6
                            );
                            break;
                        case 21:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 45 - 51 + 15 + 1, mouseY + this.y + 57 - 34,
                                    type.getX1() + 207 - 213 + 15 + 1, type.getY1() - 2 + 25, 2, 7
                            );
                            break;
                        case 22:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 43 - 51 + 15 + 1, mouseY + this.y + 55 - 34,
                                    type.getX1() + 205 - 213 + 15 + 1, type.getY1() - 2 + 23, 2, 8
                            );
                            break;
                        case 23:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 38 - 51 + 15 + 1, mouseY + this.y + 54 - 34,
                                    type.getX1() + 200 - 213 + 15 + 1, type.getY1() - 2 + 22, 5, 8
                            );
                            break;
                        case 24:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 52 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 20, 6, 4
                            );
                            break;
                        case 25:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 49 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 17, 6, 4
                            );
                            break;
                        case 26:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 46 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 14, 6, 4
                            );
                            break;
                        case 27:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 43 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 11, 7, 4
                            );
                            break;
                        case 28:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 38 - 51 + 15 + 1, mouseY + this.y + 40 - 34,
                                    type.getX1() + 200 - 213 + 15 + 1, type.getY1() - 2 + 8, 8, 4
                            );
                            break;
                        case 29:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 41 - 51 + 15 + 1,
                                    mouseY + this.y + 36 - 34,
                                    type.getX1() + 203 - 213 + 15 + 1,
                                    type.getY1() - 2 + 4,
                                    4,
                                    4
                            );
                            break;
                        case 30:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 45 - 51 + 15 + 1,
                                    mouseY + this.y + 35 - 34,
                                    type.getX1() + 207 - 213 + 15 + 1,
                                    type.getY1() - 2 + 3,
                                    2,
                                    5
                            );
                            break;
                        case 31:
                            this.gui.drawTexturedModalRect(
                                    mouseX + this.x + 47 - 51 + 15 + 1,
                                    mouseY + this.y + 35 - 34,
                                    type.getX1() + 209 - 213 + 15 + 1,
                                    type.getY1() - 2 + 3,
                                    2,
                                    5
                            );
                            break;
                        case 32:
                            this.gui.drawTexturedModalRect(mouseX + this.x + 49 - 51 + 15 + 1, mouseY + this.y + 35 - 34,
                                    type.getX1() + 211 - 213 + 15 + 1, type.getY1() - 2 + 3, 2, 5
                            );
                            break;
                    }
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
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.getSlotList().contains(slotInvSlot.inventory)) {
                                int xx = 0;
                                if (this.type.isNextBar()) {
                                    bindCommonTexture2();
                                } else if (this.type.isSteam()) {
                                    bindCommonTexture3();
                                } else if (this.type.isBio()) {
                                    bindCommonTexture4();
                                } else if (this.type.isSpace()) {
                                    bindCommonTexture5();
                                } else {

                                    bindCommonTexture();
                                }

                                int yy = 0;
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
                                    case PHOTONIC:
                                        xx = 2;
                                        yy = 42;
                                        break;
                                }
                                this.gui.drawTexturedModalRect(
                                        mouseX + this.x - 4,
                                        mouseY + this.y - 4,
                                        type.getX() + 24 - xx,
                                        type.getY() + 19 + yy,
                                        26,
                                        26
                                );
                            } else if (!this.gui_iu.isBlack) {
                                if (this.type.isNextBar()) {
                                    bindCommonTexture2();
                                } else if (this.type.isSteam()) {
                                    bindCommonTexture3();
                                } else if (this.type.isBio()) {
                                    bindCommonTexture4();
                                } else if (this.type.isSpace()) {
                                    bindCommonTexture5();
                                } else {
                                    bindCommonTexture();
                                }
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
                            if (((SlotInvSlot) slot).inventory != null) {
                                ITypeSlot typeSlot = ((SlotInvSlot) slot).inventory;
                                final EnumTypeSlot type = typeSlot.getTypeSlot(((SlotInvSlot) slot).index);
                                if (type == null) {
                                    continue;
                                }

                                bindCommonTexture();
                                if (type.next) {
                                    bindCommonTexture6();
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
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);


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
                                    mouseX + this.x,
                                    mouseY + this.y,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );


                        }
                    }
                    break;
                case SLOTS_UPGRADE_JEI:
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        if (slot instanceof SlotInvSlot) {
                            final Inventory invslot = ((SlotInvSlot) slot).inventory;
                            if (((SlotInvSlot) slot).inventory instanceof InventoryUpgrade) {
                                continue;
                            }
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.getSlotList().contains(slotInvSlot.inventory)) {
                                int xx = 0;
                                switch (this.type) {
                                    case ADVANCED:
                                        xx = 8;
                                        break;
                                    case IMPROVED:
                                        xx = 16;
                                        break;
                                    case PERFECT:
                                    case PHOTONIC:
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
                            if (((SlotInvSlot) slot).inventory instanceof ITypeSlot) {
                                ITypeSlot typeSlot = (ITypeSlot) ((SlotInvSlot) slot).inventory;
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
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);


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
                                    mouseX + this.x,
                                    mouseY + this.y,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );


                        }
                    }
                    break;
                case SLOTS__JEI:
                    InventoryOutput output = null;
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        if (slot instanceof SlotInvSlot) {
                            final Inventory invslot = ((SlotInvSlot) slot).inventory;
                            if (((SlotInvSlot) slot).inventory instanceof InventoryUpgrade || !(invslot instanceof InventoryRecipes
                                    || invslot instanceof InventoryMultiRecipes || invslot instanceof InventoryOutput)) {
                                continue;
                            }
                            if (output == null && invslot instanceof InventoryOutput) {
                                output = (InventoryOutput) invslot;
                            } else {
                                if (invslot instanceof InventoryOutput) {
                                    final InventoryOutput output1 = (InventoryOutput) invslot;
                                    if (output1 != output) {
                                        continue;
                                    }
                                }
                            }
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.getSlotList().contains(slotInvSlot.inventory)) {
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
                            if (((SlotInvSlot) slot).inventory instanceof ITypeSlot) {
                                ITypeSlot typeSlot = (ITypeSlot) ((SlotInvSlot) slot).inventory;
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
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);


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
                                    mouseX + this.x,
                                    mouseY + this.y,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );


                        }
                    }
                    break;
                case SLOTS__JEI_OUTPUT:
                    output = null;
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        if (slot instanceof SlotInvSlot) {
                            final Inventory invslot = ((SlotInvSlot) slot).inventory;
                            if (!(invslot instanceof InventoryOutput)) {
                                continue;
                            }
                            if (output == null && invslot instanceof InventoryOutput) {
                                output = (InventoryOutput) invslot;
                            } else {
                                final InventoryOutput output1 = (InventoryOutput) invslot;
                                if (output1 != output) {
                                    continue;
                                }
                            }
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.getSlotList().contains(slotInvSlot.inventory)) {
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
                            if (((SlotInvSlot) slot).inventory instanceof ITypeSlot) {
                                ITypeSlot typeSlot = (ITypeSlot) ((SlotInvSlot) slot).inventory;
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
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);


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
                                    mouseX + this.x,
                                    mouseY + this.y,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );


                        }
                    }
                    break;
                case SLOTS__JEI_INPUT:
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        if (slot instanceof SlotInvSlot) {
                            final Inventory invslot = ((SlotInvSlot) slot).inventory;
                            if (((SlotInvSlot) slot).inventory instanceof InventoryUpgrade || !(invslot instanceof InventoryRecipes
                                    || invslot instanceof InventoryMultiRecipes)) {
                                continue;
                            }
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.getSlotList().contains(slotInvSlot.inventory)) {
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
                            if (((SlotInvSlot) slot).inventory instanceof ITypeSlot) {
                                ITypeSlot typeSlot = (ITypeSlot) ((SlotInvSlot) slot).inventory;
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
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);


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
                                    mouseX + this.x,
                                    mouseY + this.y,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );


                        }
                    }
                    break;
                case SLOT:
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        if (slot instanceof SlotInvSlot) {
                            if (component.getSlotList().contains(((SlotInvSlot) slot).inventory)) {
                                int xX = slot.xPos;
                                int yY = slot.yPos;
                                this.setX(xX - 1);
                                this.setY(yY - 1);
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
                        if (slot instanceof SlotVirtual) {

                            int xX = slot.xPos;
                            int yY = slot.yPos;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
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
                    break;
                case DEFAULT:
                    for (Slot slot : this.gui.getContainer().inventorySlots) {
                        int xX = slot.xPos;
                        int yY = slot.yPos;
                        this.setX(xX - 1);
                        this.setY(yY - 1);
                        this.gui.drawTexturedModalRect(
                                mouseX + this.x,
                                mouseY + this.y,
                                type.getX(),
                                type.getY(),
                                type.getWeight(),
                                type.getHeight()
                        );

                    }
                    break;
                case ALL:
                    if (this.type.isNextBar()) {
                        bindCommonTexture2();
                    } else if (this.type.isSteam()) {
                        bindCommonTexture3();
                    } else if (this.type.isBio()) {
                        bindCommonTexture4();
                    } else if (this.type.isSpace()) {
                        bindCommonTexture5();
                    } else {
                        bindCommonTexture();
                    }
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

                    if (this.type.isNextBar()) {
                        bindCommonTexture2();
                    } else if (this.type.isSteam()) {
                        bindCommonTexture3();
                    } else if (this.type.isBio()) {
                        bindCommonTexture4();
                    } else if (this.type.isSpace()) {
                        bindCommonTexture5();
                    } else {
                        bindCommonTexture();
                    }
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
                    if (this.type.isNextBar()) {
                        bindCommonTexture2();
                    } else {
                        bindCommonTexture();
                    }
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

    private void bindCommonTexture6() {
        Minecraft.getMinecraft().renderEngine.bindTexture(commonTexture5);
    }

}
