package com.denfop.api.widget;

import com.denfop.Constants;
import com.denfop.api.recipe.InventoryMultiRecipes;
import com.denfop.api.recipe.InventoryOutput;
import com.denfop.api.recipe.InventoryRecipes;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.blockentity.mechanism.EnumTypeMachines;
import com.denfop.componets.*;
import com.denfop.containermenu.slot.SlotInvSlot;
import com.denfop.containermenu.slot.SlotVirtual;
import com.denfop.inventory.Inventory;
import com.denfop.inventory.InventoryUpgrade;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ScreenWidget extends AbstractWidget {

    public static final ResourceLocation commonTexture1 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars.png"
    );
    public static final ResourceLocation commonTexture2 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/gui_progressbars1.png"
    );
    public static final ResourceLocation commonTexture5 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/slot_render.png"
    );
    public static final ResourceLocation commonTexture = new ResourceLocation(Constants.MOD_ID, "textures/gui/common.png");
    public static final ResourceLocation commonTexture3 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/steam_progressbars.png"
    );
    public static final ResourceLocation commonTexture4 = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/bio_progressbars.png"
    );
    public static final ResourceLocation space_progress = new ResourceLocation(
            Constants.MOD_ID,
            "textures/gui/guispace_progress.png"
    );
    private final EnumTypeComponent type;
    private final WidgetDefault<?> widgetDefault;
    private final ScreenMain<?> gui_iu;
    public ScreenIndustrialUpgrade<?> gui;
    protected int x;
    protected int y;
    private int index = 0;
    private Supplier<String> tooltipProvider;


    public ScreenWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, EnumTypeComponent type, WidgetDefault<?> widgetDefault) {
        super(x, y, type.getWeight(), type.getHeight(), Component.literal(type.toString()));
        this.x = x;
        this.y = y;
        this.type = type;
        this.gui_iu = gui instanceof ScreenMain<?> ? (ScreenMain<?>) gui : null;
        this.gui = gui;
        this.widgetDefault = widgetDefault;
    }

    public ScreenWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("empty"));
        this.x = x;
        this.y = y;
        this.type = null;
        this.gui_iu = gui instanceof ScreenMain<?> ? (ScreenMain<?>) gui : null;
        this.gui = gui;
        this.widgetDefault = new WidgetDefault<>(new EmptyWidget());
    }

    public ScreenWidget(ScreenIndustrialUpgrade<?> gui, int x, int y, int width, int height, WidgetDefault<?> widgetDefault) {
        super(x, y, width, height, Component.literal(widgetDefault.toString()));
        this.x = x;
        this.y = y;
        this.type = null;
        this.gui_iu = gui instanceof ScreenMain<?> ? (ScreenMain<?>) gui : null;
        this.gui = gui;
        this.widgetDefault = widgetDefault;
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

    protected static String processText(String text) {
        return Localization.translate(text);
    }

    protected static void bindTexture(ResourceLocation texture) {
        ScreenIndustrialUpgrade.bindTexture(texture);
    }

    public static void bindCommonTexture2() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture2);
    }

    public static void bindBlockTexture() {
        ScreenIndustrialUpgrade.bindTexture(TextureAtlas.LOCATION_BLOCKS);
    }

    public static TextureAtlas getBlockTextureMap() {
        return (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS);
    }

    public static void bindCommonTexture() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture);
    }

    public static void bindCommonTexture1() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture1);
    }

    public static void bindCommonTexture3() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture3);
    }

    public static void bindCommonTexture4() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture4);
    }

    public static void bindCommonTexture5() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture5);
    }

    public Supplier<String> getTooltipProvider() {
        return tooltipProvider;
    }

    public ScreenIndustrialUpgrade<?> getGui() {
        return gui;
    }

    public void setGui(final ScreenIndustrialUpgrade<?> gui) {
        this.gui = gui;
    }

    public boolean contains(int x, int y) {
        return x >= this.x && x < this.x + this.width && y >= this.y && y < this.y + this.height;
    }

    public ScreenWidget withTooltip(String tooltip) {
        return this.withTooltip(Suppliers.ofInstance(tooltip));
    }

    public ScreenWidget withTooltip(Supplier<String> tooltipProvider) {
        this.tooltipProvider = tooltipProvider;
        return this;
    }

    public boolean visible() {
        return true;
    }

    public WidgetDefault<?> getComponent() {
        return widgetDefault;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void buttonClicked(int mouseX, int mouseY) {
        if (widgetDefault.getComponent() instanceof ComponentButton) {
            if (this.contains(mouseX, mouseY)) {
                ComponentButton button = (ComponentButton) widgetDefault.getComponent();
                button.ClickEvent();
            }
        }
    }

    public EnumTypeComponent getType() {
        return type;
    }

    protected List<String> getToolTip() {
        if (tooltipProvider != null) {
            String tooltip = tooltipProvider.get();
            if (tooltip != null)
                return new ArrayList<>(List.of(tooltip.split("\n")));
        }
        return new LinkedList<>();
    }

    public void drawForeground(PoseStack poseStack, int mouseX, int mouseY) {
        if (this.contains(mouseX, mouseY)) {
            List<String> lines = this.getToolTip();
            if (this.getType() == null || this.getType().isHasDescription()) {
                String tooltip = this.widgetDefault.getText(this);
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }
            }

            if (!lines.isEmpty()) {
                this.gui.drawTooltip(mouseX, mouseY, lines);
            }
        }
        if (this.widgetDefault.getComponent() instanceof PressureComponent) {
            PressureComponent pressureComponent = (PressureComponent) this.widgetDefault.getComponent();
            this.gui_iu.drawString(poseStack, String.valueOf(pressureComponent.buffer.storage), this.x + 2, this.y + 2,
                    4210752
            );
            if (mouseX >= this.x - 2 && mouseX <= this.x + 4 + this.gui_iu.getStringWidth(String.valueOf(pressureComponent.buffer.storage)) && mouseY >= this.y + 2 && mouseY <= this.y + 10) {
                List<String> lines = this.getToolTip();

                String tooltip = this.widgetDefault.getText(this);
                if (tooltip != null && !tooltip.isEmpty()) {
                    addLines(lines, tooltip);
                }


                if (!lines.isEmpty()) {
                    this.gui.drawTooltip(mouseX, mouseY, lines);
                }
            }
        }
    }

    public void renderBar(PoseStack poseStack, int mouseX, int mouseY, double bar) {
        ScreenIndustrialUpgrade.bindTexture(commonTexture1);
        if (this.widgetDefault.getComponent() instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.widgetDefault.getComponent();
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
                    poseStack, mouseX + this.x + xx,
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
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x + xx,
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
                    poseStack, mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            if (this.type.getRender() == EnumTypeRender.WIDTH) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (bar * type.getWeight()),
                        type.getHeight()
                );
            }
            if (this.type.getRender() == EnumTypeRender.HEIGHT) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (type.getWeight()),
                        (int) (bar * type.getHeight())
                );
            }
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(final int width) {
        this.width = width;
    }


    public void drawBackground(PoseStack poseStack, int mouseX, int mouseY) {
        if (this.widgetDefault == null || this.widgetDefault.getComponent() == null || !visible() || this.type == null) {
            return;
        }
        bindCommonTexture1();
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
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.widgetDefault.getComponent() instanceof WidgetSize) {
            WidgetSize componentSoundButton = (WidgetSize) this.widgetDefault.getComponent();
            this.gui.drawTexturedModalRect(
                    poseStack, mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    componentSoundButton.getWidth(),
                    componentSoundButton.getHeight()
            );
        } else if (this.widgetDefault.getComponent() instanceof ComponentButton) {
            if (this.widgetDefault.getComponent() instanceof ComponentSoundButton) {
                ComponentSoundButton componentSoundButton = (ComponentSoundButton) this.widgetDefault.getComponent();
                if (componentSoundButton.getAudioFixer().getEnable()) {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x,
                            mouseY + this.y,
                            type.getX(),
                            type.getY(),
                            type.getWeight(),
                            type.getHeight()
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x,
                            mouseY + this.y,
                            type.getX1(),
                            type.getY1(),
                            type.getWeight(),
                            type.getHeight()
                    );
                }
            } else {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
                        mouseY + this.y,
                        type.getX(),
                        type.getY(),
                        type.getWeight(),
                        type.getHeight()
                );
                ComponentButton button = (ComponentButton) this.widgetDefault.getComponent();
                if (button.active()) {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x,
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

        if (!(this.widgetDefault.getComponent() instanceof ComponentRenderInventory) && !(this.widgetDefault.getComponent() instanceof ComponentProcessRender) && !(this.widgetDefault.getComponent() instanceof ComponentValue) && !(this.widgetDefault.getComponent() instanceof ComponentSteamProcessRender) && !(this.widgetDefault.getComponent() instanceof ComponentBioProcessRender) && !(this.widgetDefault.getComponent() instanceof ComponentTimer) && !(this.widgetDefault.getComponent() instanceof ComponentProgress) && !(this.widgetDefault.getComponent() instanceof VeinBase)) {
            this.gui.drawTexturedModalRect(
                    poseStack, mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            this.widgetDefault.drawBackground(poseStack, mouseX, mouseY, this);
        } else if (this.widgetDefault.getComponent() instanceof ComponentProgress) {
            this.gui.drawTexturedModalRect(
                    poseStack, mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            ComponentProgress component = (ComponentProgress) this.widgetDefault.getComponent();
            double scale = component.getBar();
            if (this.type.getRender() == EnumTypeRender.WIDTH) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
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
                            poseStack, mouseX + this.x,
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
                            poseStack, mouseX + this.getX() + type.getEndX(),
                            mouseY + type.getEndY() + this.getY() + type
                                    .getHeight() - chargeLevel,
                            type.getX1(),
                            type.getY1() + type.getHeight() - chargeLevel,
                            type.getWeight(),
                            chargeLevel
                    );
                }
            }
        } else if (this.widgetDefault.getComponent() instanceof VeinBase) {
            this.gui.drawTexturedModalRect(
                    poseStack, mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            VeinBase component = (VeinBase) this.widgetDefault.getComponent();
            double scale = component.getCol() * 1D / component.getMaxCol();
            if (this.type.getRender() == EnumTypeRender.WIDTH) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
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
                        poseStack, mouseX + this.getX() + type.getEndX(),
                        mouseY + type.getEndY() + this.getY() + type
                                .getHeight() - chargeLevel,
                        type.getX1(),
                        type.getY1() + type.getHeight() - chargeLevel,
                        type.getWeight(),
                        chargeLevel
                );
            }
        } else if (this.widgetDefault.getComponent() instanceof ComponentTimer) {
            this.gui.drawTexturedModalRect(
                    poseStack, mouseX + this.x,
                    mouseY + this.y,
                    type.getX(),
                    type.getY(),
                    type.getWeight(),
                    type.getHeight()
            );
            ComponentTimer component = (ComponentTimer) this.widgetDefault.getComponent();
            final double scale = component.getTimes();
            if (this.type.getRender() == EnumTypeRender.WIDTH) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (scale * type.getWeight()),
                        type.getHeight()
                );
            }
            if (this.type.getRender() == EnumTypeRender.HEIGHT) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
                        mouseY + this.y,
                        type.getX1(),
                        type.getY1(),
                        (int) (type.getWeight()),
                        (int) (scale * type.getHeight())
                );
            }
        } else if (this.widgetDefault.getComponent() instanceof ComponentProcessRender) {
            ComponentProcessRender component = (ComponentProcessRender) this.widgetDefault.getComponent();
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
                    poseStack, mouseX + this.x + xx,
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
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                }
            }
        } else if (this.widgetDefault.getComponent() instanceof ComponentSteamProcessRender) {
            ComponentSteamProcessRender component = (ComponentSteamProcessRender) this.widgetDefault.getComponent();
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
                    poseStack, mouseX + this.x + xx,
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
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                }
            }
        } else if (this.widgetDefault.getComponent() instanceof ComponentBioProcessRender) {
            ComponentBioProcessRender component = (ComponentBioProcessRender) this.widgetDefault.getComponent();
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
                    poseStack, mouseX + this.x + xx,
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
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + 2 + xx1,
                            type.getY() + down,
                            type.getWeight() + 1,
                            progress
                    );
                } else {
                    this.gui.drawTexturedModalRect(
                            poseStack, mouseX + this.x + xx,
                            mouseY + this.y + yy,
                            type.getX() + 16 + x + xx1,
                            type.getY() + down,
                            type.getWeight() + 1 + weight,
                            progress
                    );
                }
            }
        } else if (this.widgetDefault.getComponent() instanceof ComponentValue) {
            ComponentValue componentValue = (ComponentValue) this.widgetDefault.getComponent();
            if (this.type.isNextBar())
                bindCommonTexture2();
            if (this.getType() == EnumTypeComponent.CIRCLE_BAR) {
                this.gui.drawTexturedModalRect(
                        poseStack, mouseX + this.x,
                        mouseY + this.y,
                        type.getX(),
                        type.getY(),
                        type.getWeight(),
                        type.getHeight()
                );

                for (int i = 0; i <= (Integer) componentValue.getValue(); i++) {
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

                    switch (i) {
                        case 1:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 16, mouseY + this.y, type.getX1() + 16,
                                    type.getY1()
                                    , 2, 6);
                            break;
                        case 2:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 17 + 1, mouseY + this.y, type.getX1() + 17 + 1,
                                    type.getY1(), 2, 6);
                            break;
                        case 3:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 19 + 1, mouseY + this.y + 1, type.getX1() + 19 + 1,
                                    type.getY1() + 1, 2, 6);
                            break;
                        case 4:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 21 + 1, mouseY + this.y + 2, type.getX1() + 21 + 1,
                                    type.getY1() + 2, 2, 7);
                            break;
                        case 5:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 15 + 8 + 1, mouseY + this.y + 3, type.getX1() + 15 + 8 + 1,
                                    type.getY1() + 3, 4, 4);
                            break;
                        case 6:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 59 - 51 + 15 + 1, mouseY + this.y + 41 - 34,
                                    type.getX1() + 221 - 213 + 15 + 1, type.getY1() - 2 + 9, 6, 2);
                            break;
                        case 7:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 59 - 51 + 15 + 1, mouseY + this.y + 43 - 34, type.getX1() + 221 - 213 + 15 + 1, type.getY1() - 2 + 11, 7, 2);
                            break;
                        case 8:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 60 - 51 + 15 + 1, mouseY + this.y + 45 - 34, type.getX1() + 222 - 213 + 15 + 1, type.getY1() - 2 + 13, 6, 2);
                            break;
                        case 9:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 61 - 51 + 15 + 1, mouseY + this.y + 47 - 34, type.getX1() + 223 - 213 + 15 + 1, type.getY1() - 2 + 15, 6, 2);
                            break;
                        case 10:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 61 - 51 + 15 + 1, mouseY + this.y + 49 - 34, type.getX1() + 223 - 213 + 15 + 1, type.getY1() - 2 + 17, 6, 2);
                            break;
                        case 11:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 61 - 51 + 15 + 1, mouseY + this.y + 51 - 34, type.getX1() + 223 - 213 + 15 + 1, type.getY1() - 2 + 19, 6, 2);
                            break;
                        case 12:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 60 - 51 + 15 + 1, mouseY + this.y + 53 - 34, type.getX1() + 222 - 213 + 15 + 1, type.getY1() - 2 + 21, 6, 2);
                            break;
                        case 13:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 58 - 51 + 15 + 1, mouseY + this.y + 55 - 34, type.getX1() + 220 - 213 + 15 + 1, type.getY1() - 2 + 23, 7, 2);
                            break;
                        case 14:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 57 - 51 + 15 + 1, mouseY + this.y + 57 - 34, type.getX1() + 219 - 213 + 15 + 1, type.getY1() - 2 + 25, 7, 2);
                            break;
                        case 15:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 57 - 51 + 15 + 1, mouseY + this.y + 59 - 34, type.getX1() + 219 - 213 + 15 + 1, type.getY1() - 2 + 27, 5, 4);
                            break;
                        case 16:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 55 - 51 + 15 + 1, mouseY + this.y + 58 - 34, type.getX1() + 217 - 213 + 15 + 1, type.getY1() - 2 + 26, 2, 6);
                            break;
                        case 17:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 53 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 215 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6);
                            break;
                        case 18:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 51 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 213 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6);
                            break;
                        case 19:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 49 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 211 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6);
                            break;
                        case 20:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 47 - 51 + 15 + 1, mouseY + this.y + 59 - 34,
                                    type.getX1() + 209 - 213 + 15 + 1, type.getY1() - 2 + 27, 2, 6);
                            break;
                        case 21:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 45 - 51 + 15 + 1, mouseY + this.y + 57 - 34,
                                    type.getX1() + 207 - 213 + 15 + 1, type.getY1() - 2 + 25, 2, 7);
                            break;
                        case 22:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 43 - 51 + 15 + 1, mouseY + this.y + 55 - 34,
                                    type.getX1() + 205 - 213 + 15 + 1, type.getY1() - 2 + 23, 2, 8);
                            break;
                        case 23:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 38 - 51 + 15 + 1, mouseY + this.y + 54 - 34,
                                    type.getX1() + 200 - 213 + 15 + 1, type.getY1() - 2 + 22, 5, 8);
                            break;
                        case 24:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 52 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 20, 6, 4);
                            break;
                        case 25:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 49 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 17, 6, 4);
                            break;
                        case 26:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 46 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 14, 6, 4);
                            break;
                        case 27:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 37 - 51 + 15 + 1, mouseY + this.y + 43 - 34,
                                    type.getX1() + 199 - 213 + 15 + 1, type.getY1() - 2 + 11, 7, 4);
                            break;
                        case 28:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 38 - 51 + 15 + 1, mouseY + this.y + 40 - 34,
                                    type.getX1() + 200 - 213 + 15 + 1, type.getY1() - 2 + 8, 8, 4);
                            break;
                        case 29:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 41 - 51 + 15 + 1, mouseY + this.y + 36 - 34, type.getX1() + 203 - 213 + 15 + 1, type.getY1() - 2 + 4, 4, 4);
                            break;
                        case 30:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 45 - 51 + 15 + 1, mouseY + this.y + 35 - 34, type.getX1() + 207 - 213 + 15 + 1, type.getY1() - 2 + 3, 2, 5);
                            break;
                        case 31:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 47 - 51 + 15 + 1, mouseY + this.y + 35 - 34, type.getX1() + 209 - 213 + 15 + 1, type.getY1() - 2 + 3, 2, 5);
                            break;
                        case 32:
                            this.gui.drawTexturedModalRect(poseStack, mouseX + this.x + 49 - 51 + 15 + 1, mouseY + this.y + 35 - 34,
                                    type.getX1() + 211 - 213 + 15 + 1, type.getY1() - 2 + 3, 2, 5);
                            break;
                    }
                }
            }
        } else if (this.widgetDefault.getComponent() instanceof ComponentRenderInventory) {
            ComponentRenderInventory component = (ComponentRenderInventory) this.widgetDefault.getComponent();
            switch (component.getTypeComponentSlot()) {
                case SLOTS_UPGRADE:
                    for (Slot slot : this.gui.getContainer().slots) {
                        if (slot instanceof SlotInvSlot) {
                            int xX = slot.x;
                            int yY = slot.y;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.contains(slotInvSlot.inventory)) {
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

                                    bindCommonTexture1();
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
                                this.gui.drawTexturedModalRect(poseStack,
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
                                    bindCommonTexture1();
                                }
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            } else {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
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

                                bindCommonTexture1();
                                if (type.next) {
                                    bindCommonTexture6();
                                }
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.x;
                            int yY = slot.y;
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
                                    poseStack, mouseX + this.x,
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
                    for (Slot slot : this.gui.getContainer().slots) {
                        if (slot instanceof SlotInvSlot) {
                            final Inventory invslot = ((SlotInvSlot) slot).inventory;
                            if (((SlotInvSlot) slot).inventory instanceof InventoryUpgrade) {
                                continue;
                            }
                            int xX = slot.x;
                            int yY = slot.y;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.contains(slotInvSlot.inventory)) {
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
                                        poseStack, mouseX + this.x - 4,
                                        mouseY + this.y - 4,
                                        type.getX() - 160 - xx,
                                        type.getY() + 114,
                                        26,
                                        26
                                );
                            } else if (!this.gui_iu.isBlack) {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            } else {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
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
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.x;
                            int yY = slot.y;
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
                                    poseStack, mouseX + this.x,
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
                    for (Slot slot : this.gui.getContainer().slots) {
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
                            int xX = slot.x;
                            int yY = slot.y;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.contains(slotInvSlot.inventory)) {
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
                                        poseStack, mouseX + this.x - 4,
                                        mouseY + this.y - 4,
                                        type.getX() - 160 - xx,
                                        type.getY() + 114,
                                        26,
                                        26
                                );
                            } else if (!this.gui_iu.isBlack) {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            } else {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
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
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.x;
                            int yY = slot.y;
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
                                    poseStack, mouseX + this.x,
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
                    for (Slot slot : this.gui.getContainer().slots) {
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
                            int xX = slot.x;
                            int yY = slot.y;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.contains(slotInvSlot.inventory)) {
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
                                        poseStack, mouseX + this.x - 4,
                                        mouseY + this.y - 4,
                                        type.getX() - 160 - xx,
                                        type.getY() + 114,
                                        26,
                                        26
                                );
                            } else if (!this.gui_iu.isBlack) {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            } else {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
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
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.x;
                            int yY = slot.y;
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
                                    poseStack, mouseX + this.x,
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
                    for (Slot slot : this.gui.getContainer().slots) {
                        if (slot instanceof SlotInvSlot) {
                            final Inventory invslot = ((SlotInvSlot) slot).inventory;
                            if (((SlotInvSlot) slot).inventory instanceof InventoryUpgrade || !(invslot instanceof InventoryRecipes
                                    || invslot instanceof InventoryMultiRecipes)) {
                                continue;
                            }
                            int xX = slot.x;
                            int yY = slot.y;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            SlotInvSlot slotInvSlot = (SlotInvSlot) slot;
                            if (component.getBlackSlotList().contains(slotInvSlot.inventory)) {
                                continue;
                            }
                            if (component.contains(slotInvSlot.inventory)) {
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
                                        poseStack, mouseX + this.x - 4,
                                        mouseY + this.y - 4,
                                        type.getX() - 160 - xx,
                                        type.getY() + 114,
                                        26,
                                        26
                                );
                            } else if (!this.gui_iu.isBlack) {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            } else {
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
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
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        } else if (slot instanceof SlotVirtual) {
                            int xX = slot.x;
                            int yY = slot.y;
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
                                    poseStack, mouseX + this.x,
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
                    for (Slot slot : this.gui.getContainer().slots) {
                        if (slot instanceof SlotInvSlot) {
                            if (component.contains(((SlotInvSlot) slot).inventory)) {
                                int xX = slot.x;
                                int yY = slot.y;
                                this.setX(xX - 1);
                                this.setY(yY - 1);
                                this.gui.drawTexturedModalRect(
                                        poseStack, mouseX + this.x,
                                        mouseY + this.y,
                                        type.getX(),
                                        type.getY(),
                                        type.getWeight(),
                                        type.getHeight()
                                );
                            }
                        }
                        if (slot instanceof SlotVirtual) {

                            int xX = slot.x;
                            int yY = slot.y;
                            this.setX(xX - 1);
                            this.setY(yY - 1);
                            this.gui.drawTexturedModalRect(
                                    poseStack, mouseX + this.x,
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
                    for (Slot slot : this.gui.getContainer().slots) {
                        int xX = slot.x;
                        int yY = slot.y;
                        this.setX(xX - 1);
                        this.setY(yY - 1);
                        this.gui.drawTexturedModalRect(
                                poseStack, mouseX + this.x,
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
                        bindCommonTexture1();
                    }
                    for (int i = 0; i < 27; i++) {
                        if (!this.gui_iu.isBlack) {
                            this.gui.drawTexturedModalRect(
                                    poseStack, mouseX + this.x + 18 * (i % 9),
                                    mouseY + this.y + 18 * (i / 9),
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );
                        } else {
                            this.gui.drawTexturedModalRect(
                                    poseStack, mouseX + this.x + 18 * (i % 9),
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
                                    poseStack, mouseX + this.x + 18 * (j % 9),
                                    mouseY + this.y + 58,
                                    type.getX(),
                                    type.getY(),
                                    type.getWeight(),
                                    type.getHeight()
                            );
                        } else {
                            this.gui.drawTexturedModalRect(
                                    poseStack, mouseX + this.x + 18 * (j % 9),
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
                        bindCommonTexture1();
                    }
                    for (int i = 0; i < 27; i++) {
                        this.gui.drawTexturedModalRect(
                                poseStack, mouseX + this.x + 18 * (i % 9),
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
                        bindCommonTexture1();
                    }
                    for (int j = 0; j < 9; j++) {
                        this.gui.drawTexturedModalRect(
                                poseStack, mouseX + this.x + 18 * (j % 9),
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

    private void bindCommonTexture6() {
        ScreenIndustrialUpgrade.bindTexture(commonTexture5);
    }

    public boolean onMouseClick(final int mouseX, final int mouseY, final MouseButton button) {
        return false;
    }

    public void addY(int i) {
        this.y += i;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
