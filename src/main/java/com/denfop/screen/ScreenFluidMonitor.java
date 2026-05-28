package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.storage.autocrafting.AutoCraftSystemVisual;
import com.denfop.api.storage.autocrafting.AutoCraftVisualStep;
import com.denfop.api.storage.autocrafting.PatternStack;
import com.denfop.api.storage.autocrafting.SameStack;
import com.denfop.api.storage.autocrafting.graph.*;
import com.denfop.api.widget.*;
import com.denfop.containermenu.ContainerFluidMonitor;
import com.denfop.network.packet.*;
import com.denfop.utils.FluidHandlerFix;
import com.denfop.utils.Keyboard;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;

import java.text.Collator;
import java.util.*;

import static com.denfop.screen.ScreenResearchTableSpace.enableScissor;
import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;

public class ScreenFluidMonitor<T extends ContainerFluidMonitor> extends ScreenMain<ContainerFluidMonitor> {

    public static boolean canAddAutoCraft = false;
    private final ImageInterfaceWidget imageOne;
    private final StorageInterfaceWidget imageTwo;
    private final StorageInterfaceWidget imageThree;
    private final StorageButtonWidget button;
    private final StorageButtonWidget createButton;
    private final StorageButtonWidget oneItemButton;
    private final StorageButtonWidget tenItemButton;
    private final StorageButtonWidget minusHundredButton;
    private final StorageButtonWidget minusThousandButton;
    private final StorageButtonWidget minusTenThousandButton;
    private final StorageButtonWidget hundredButton;
    private final StorageButtonWidget thousandButton;
    private final StorageButtonWidget tenThousandButton;
    private final StorageButtonWidget minusOneItemButton;
    private final StorageButtonWidget minusTenItemButton;
    private final StorageButtonWidget exitButton;
    private final StorageButtonWidget cancelButton;
    private final StorageButtonWidget graphButton;
    private final StorageButtonWidget backGraphButton;
    private final CraftGraphBuilder craftGraphBuilder = new CraftGraphBuilder();
    private final CraftGraphLayout craftGraphLayout = new CraftGraphLayout();
    public EditBox searchField;
    public EditBox countField;
    public int decreasing = 0;
    public int viewMode = 0;
    public boolean viewModeVisible = false;
    public boolean viewSizeVisible = false;
    public boolean viewModeDecreasing = false;
    public boolean viewModeField = false;
    AutoCraftSystemVisual autoCraftSystemVisual;
    SameStack canCreate;
    boolean hasCreateGraphScreen = false;
    boolean hasCreateCraftScreen = false;
    boolean hasCreateVisualCraftScreen = false;
    int hover = -1;
    private double pointScroll;
    private double pointScroll1;
    private int value;
    private int maxValue = 0;
    private int value1;
    private int maxValue1 = 0;
    private CraftGraph craftGraph;
    private CraftGraphRenderer craftGraphRenderer;
    private double graphCameraX = 0.0;
    private double graphCameraY = 0.0;
    private double graphZoom = 1.0;
    private boolean graphDragging = false;
    private double graphLastMouseX = 0.0;
    private double graphLastMouseY = 0.0;
    private Integer graphHoveredNodeId = null;
    private Integer graphSelectedNodeId = null;
    private Set<Integer> graphHighlightedNodes = Collections.emptySet();
    private boolean mainScrollbarDragging = false;
    private boolean visualScrollbarDragging = false;

    public ScreenFluidMonitor(ContainerFluidMonitor guiContainer) {
        super(guiContainer);
        this.imageWidth += 25;
        this.componentList.clear();
        this.inventory.addY(226 - 166);
        this.imageHeight += 226 - 166;

        imageOne = new ImageInterfaceWidget(this, 0, 0, this.imageWidth, this.imageHeight);
        imageTwo = new StorageInterfaceWidget(this, this.imageWidth / 2 - 118, 60, 210, 140);
        imageThree = new StorageInterfaceWidget(this, 0, 0, this.imageWidth, 140);

        this.addWidget(imageTwo);
        imageTwo.visible = false;
        imageThree.visible = false;

        button = new StorageButtonWidget(this, 5, 145, 170, 20, guiContainer.base, 0, Localization.translate("iu.monitor.create_craft"));
        createButton = new StorageButtonWidget(this, 126, 143, 175 - 126, 20, guiContainer.base, 0, Localization.translate("iu.monitor.create_craft"));
        exitButton = new StorageButtonWidget(this, 5, 170, 170, 20, guiContainer.base, 0, Localization.translate("iu.monitor.cancel_craft"));
        cancelButton = new StorageButtonWidget(this, 167, 3, 11, 9, guiContainer.base, 0, Localization.translate("iu.monitor.cancel_craft"));

        oneItemButton = new StorageButtonWidget(this, 5, 68, 20, 20, guiContainer.base, 0, "+1");
        tenItemButton = new StorageButtonWidget(this, 30, 68, 25, 20, guiContainer.base, 0, "+10");
        hundredButton = new StorageButtonWidget(this, 60, 68, 30, 20, guiContainer.base, 0, "+100");
        thousandButton = new StorageButtonWidget(this, 95, 68, 35, 20, guiContainer.base, 0, "+1000");
        tenThousandButton = new StorageButtonWidget(this, 135, 68, 40, 20, guiContainer.base, 0, "+10000");

        minusOneItemButton = new StorageButtonWidget(this, 5, 118, 20, 20, guiContainer.base, 0, "-1");
        minusTenItemButton = new StorageButtonWidget(this, 30, 118, 25, 20, guiContainer.base, 0, "-10");
        minusHundredButton = new StorageButtonWidget(this, 60, 118, 30, 20, guiContainer.base, 0, "-100");
        minusThousandButton = new StorageButtonWidget(this, 95, 118, 35, 20, guiContainer.base, 0, "-1000");
        minusTenThousandButton = new StorageButtonWidget(this, 135, 118, 40, 20, guiContainer.base, 0, "-10000");

        graphButton = new StorageButtonWidget(this, 12, 143, 52, 20, guiContainer.base, 0, Localization.translate("iu.monitor.graph"));
        backGraphButton = new StorageButtonWidget(this, (255 / 2) - (52 / 2), 160, 52, 20, guiContainer.base, 0, Localization.translate("iu.monitor.back"));
    }

    private int getMainScrollbarX() {
        return 193;
    }

    private int getMainScrollbarY() {
        return 17;
    }

    private int getMainScrollbarWidth() {
        return 9;
    }

    private int getMainScrollbarHeight() {
        return 13;
    }

    private int getMainScrollbarTravel() {
        return switch (this.container.base.sizeMode) {
            default -> 96;
            case 1 -> 133;
            case 2 -> 43;
        };
    }

    private boolean isInsideMainScrollbar(double mouseX, double mouseY) {
        int x = getMainScrollbarX();
        int y = getMainScrollbarY();
        int w = getMainScrollbarWidth();
        int h = getMainScrollbarHeight();
        int travel = getMainScrollbarTravel();

        return mouseX >= x && mouseX <= x + w
                && mouseY >= y && mouseY <= y + travel + h;
    }

    private void updateMainScrollFromMouse(double relativeMouseY) {
        if (this.maxValue <= 0) {
            this.value = 0;
            return;
        }

        double top = getMainScrollbarY();
        double travel = getMainScrollbarTravel();
        double thumbHalf = getMainScrollbarHeight() / 2.0D;

        double thumbTop = Mth.clamp(relativeMouseY - top - thumbHalf, 0.0D, travel);
        double progress = thumbTop / travel;

        this.value = Mth.clamp((int) Math.round(progress * this.maxValue), 0, this.maxValue);
    }

    private int getVisualScrollbarX() {
        return 167;
    }

    private int getVisualScrollbarY() {
        return 14;
    }

    private int getVisualScrollbarWidth() {
        return 9;
    }

    private int getVisualScrollbarHeight() {
        return 13;
    }

    private int getVisualScrollbarTravel() {
        return 114;
    }

    private boolean isInsideVisualScrollbar(double mouseX, double mouseY) {
        int x = getVisualScrollbarX();
        int y = getVisualScrollbarY();
        int w = getVisualScrollbarWidth();
        int h = getVisualScrollbarHeight();
        int travel = getVisualScrollbarTravel();

        return mouseX >= x && mouseX <= x + w
                && mouseY >= y && mouseY <= y + travel + h;
    }

    private void updateVisualScrollFromMouse(double relativeMouseY) {
        if (this.maxValue1 <= 0) {
            this.value1 = 0;
            return;
        }

        double top = getVisualScrollbarY();
        double travel = getVisualScrollbarTravel();
        double thumbHalf = getVisualScrollbarHeight() / 2.0D;

        double thumbTop = Mth.clamp(relativeMouseY - top - thumbHalf, 0.0D, travel);
        double progress = thumbTop / travel;

        this.value1 = Mth.clamp((int) Math.round(progress * this.maxValue1), 0, this.maxValue1);
    }

    @Override
    protected void init() {
        super.init();
        this.craftGraphRenderer = new CraftGraphRenderer(Minecraft.getInstance(), this);

        switch (this.container.base.sizeMode) {
            default -> {
                this.imageWidth = 210;
                this.imageHeight = 218;
            }
            case 1 -> {
                this.imageWidth = 210;
                this.imageHeight = 255;
            }
            case 2 -> {
                this.imageWidth = 210;
                this.imageHeight = 164;
            }
        }
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        if (this.hasCreateVisualCraftScreen) {
            this.imageWidth = 183;
            this.imageHeight = 168;
            this.leftPos = (this.width - this.imageWidth) / 2;
            this.topPos = (this.height - this.imageHeight) / 2;
        }

        this.searchField = new EditBox(this.font, this.leftPos + 99, this.topPos + 6, 209 - 130, 10, Component.literal("")) {
            @Override
            public int getX() {
                return leftPos + 99;
            }

            @Override
            public int getY() {
                return topPos + 6;
            }
        };
        this.searchField.setMaxLength(25);
        this.searchField.setValue(this.container.base.fieldString);
        this.searchField.setCanLoseFocus(true);
        this.searchField.setFocused(this.container.base.fieldMode == 0 || this.container.base.fieldMode == 2);
        this.searchField.setBordered(false);
        this.addWidget(this.searchField);
        this.addRenderableWidget(this.searchField);

        this.countField = new EditBox(this.font, this.leftPos + 50, this.topPos + 100, 209 - 130, 10, Component.literal("")) {
            @Override
            public int getX() {
                return leftPos + 53;
            }

            @Override
            public int getY() {
                return topPos + 100;
            }
        };
        this.countField.setMaxLength(25);
        this.countField.setValue("1");
        this.countField.setCanLoseFocus(true);
        this.countField.setBordered(false);
        this.countField.visible = false;
        this.addWidget(this.countField);
        this.addRenderableWidget(this.countField);
    }

    @Override
    public boolean canRenderSlot(Slot slot) {
        return true;
    }

    @Override
    public void updateTick() {
        super.updateTick();
        this.searchField.setValue(this.container.base.fieldString);

        switch (this.container.base.sizeMode) {
            default -> {
                this.imageWidth = 210;
                this.imageHeight = 218;
            }
            case 1 -> {
                this.imageWidth = 210;
                this.imageHeight = 255;
            }
            case 2 -> {
                this.imageWidth = 210;
                this.imageHeight = 164;
            }
        }
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        if (this.hasCreateCraftScreen) {
            this.imageWidth = (210 / 2) - 118;
            this.imageHeight = 150;
            this.leftPos = (this.width - this.imageWidth) / 2 - 118;
            this.topPos = (this.height - this.imageHeight) / 2 - 60;
        }

        if (hasCreateGraphScreen && craftGraph != null) {
            this.imageWidth = 255;
            this.imageHeight = 255;
            this.leftPos = (this.width - this.imageWidth) / 2;
            this.topPos = (this.height - this.imageHeight) / 2;
        }

        if (this.hasCreateVisualCraftScreen) {
            this.imageWidth = 183;
            this.imageHeight = 168;
            this.leftPos = (this.width - this.imageWidth) / 2;
            this.topPos = (this.height - this.imageHeight) / 2;
        }

        this.decreasing = this.container.base.decreasing;
        this.viewMode = this.container.base.viewMode;

        boolean prevVisible1 = imageOne.visible;
        boolean prevVisible2 = imageTwo.visible;
        boolean prevVisible3 = imageThree.visible;

        imageOne.visible = canCreate == null;
        imageTwo.visible = canCreate != null && hasCreateCraftScreen;
        imageThree.visible = canCreate != null && hasCreateVisualCraftScreen && !hasCreateGraphScreen;

        if ((prevVisible1 != imageOne.visible) || (prevVisible2 != imageTwo.visible) || (prevVisible3 != (imageThree.visible) || craftGraph != null)) {
            this.container.isActiveAllInventory = !(this.imageTwo.visible || this.imageThree.visible || craftGraph != null);
            new PacketUpdateMonitorInterface(this.container.player, !(this.imageTwo.visible || this.imageThree.visible));
        }

        this.container.canShift = canCreate == null;
        searchField.visible = canCreate == null;
        countField.visible = canCreate != null && imageTwo.visible;
        if (this.container.base.network == null)
            return;
        if (canCreate == null) {
            List<SameStack> stacks = container.base.network.getFluids();
            List<SameStack> creatable = container.base.network.getFluidsCanCreate();

            String query = searchField != null ? searchField.getValue().trim().toLowerCase() : "";
            boolean searchActive = !query.isEmpty();

            TagKey<Fluid> key = null;
            boolean searchModId = false;
            String modId = "";

            if (query.startsWith("#") && query.length() >= 2) {
                key = TagKey.create(Registries.FLUID, ResourceLocation.tryParse(query.substring(1)));
            }
            if (query.startsWith("@") && query.length() >= 2) {
                searchModId = true;
                modId = query.substring(1);
            }

            int filteredSize = 0;
            if (searchActive) {
                if (viewMode == 0 || viewMode == 1) {
                    for (SameStack s : stacks) {
                        if (ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(s.getFluidStack().getHoverName().getString()).toLowerCase()).startsWith(query)
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || (key != null && s.getFluidStack().getFluid().is(key))) {
                            filteredSize++;
                        }
                    }
                }
                if (viewMode == 0 || viewMode == 2) {
                    if (viewMode == 2) {
                        for (SameStack s : stacks) {
                            if ((ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(s.getFluidStack().getHoverName().getString()).toLowerCase()).startsWith(query)
                                    || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                    || (key != null && s.getFluidStack().getFluid().is(key)))
                                    && this.container.base.network.findPatternFor(s.getFluidStack()) != null) {
                                filteredSize++;
                            }
                        }
                    }
                    for (SameStack s : creatable) {
                        if (ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(s.getFluidStack().getHoverName().getString()).toLowerCase()).startsWith(query)
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || (key != null && s.getFluidStack().getFluid().is(key))) {
                            filteredSize++;
                        }
                    }
                }
            } else {
                if (viewMode == 0 || viewMode == 1) {
                    filteredSize += stacks.size();
                }
                if (viewMode == 2) {
                    for (SameStack s : stacks) {
                        if (this.container.base.network.findPatternFor(s.getFluidStack()) != null) {
                            filteredSize++;
                        }
                    }
                }
                if (viewMode == 0 || viewMode == 2) {
                    filteredSize += creatable.size();
                }
            }

            switch (container.base.sizeMode) {
                default -> maxValue = (filteredSize / 9) - 5;
                case 1 -> maxValue = (filteredSize / 9) - 7;
                case 2 -> maxValue = (filteredSize / 9) - 2;
            }

            if (maxValue < 0) {
                maxValue = 0;
            }
            if (value > maxValue) {
                value = maxValue;
            }

            if (maxValue != 0) {
                if (this.container.base.sizeMode == 2) {
                    this.pointScroll = 43D / maxValue;
                } else if (this.container.base.sizeMode == 1) {
                    this.pointScroll = 133D / maxValue;
                } else {
                    this.pointScroll = 96D / maxValue;
                }
            } else {
                this.pointScroll = 0;
            }
        } else {
            if (hasCreateVisualCraftScreen && autoCraftSystemVisual != null && autoCraftSystemVisual.canAddToProcessor()) {
                new PacketCanAddCraft(container.player, container.base.pos);
            }
            if (hasCreateVisualCraftScreen && autoCraftSystemVisual != null) {
                maxValue1 = (autoCraftSystemVisual.getVisualSteps().size() / 3) - 5;
                if (maxValue1 < 0) {
                    maxValue1 = 0;
                }
                if (value1 > maxValue1) {
                    value1 = maxValue1;
                }
                if (maxValue1 != 0) {
                    this.pointScroll1 = 114D / maxValue1;
                } else {
                    this.pointScroll1 = 0;
                }
            }
        }
    }

    private void rebuildCraftGraph() {
        if (this.canCreate == null || this.container.base == null || this.container.base.network == null) {
            this.craftGraph = null;
            return;
        }

        List<PatternStack> patterns = this.container.base.network.getAllPatterns();
        if (patterns == null || patterns.isEmpty()) {
            this.craftGraph = null;
            return;
        }

        SameStack target = this.canCreate.copy();
        this.craftGraph = this.craftGraphBuilder.buildGraph(patterns, target);
        this.craftGraphLayout.layout(this.craftGraph);

        CraftNode root = this.craftGraph.getNode(this.craftGraph.getRootNodeId());
        if (root != null) {
            this.graphCameraX = root.getX();
            this.graphCameraY = root.getY();
        } else {
            this.graphCameraX = 0.0;
            this.graphCameraY = 0.0;
        }

        this.graphZoom = 0.52D;
        this.graphHoveredNodeId = null;
        this.graphSelectedNodeId = null;
        this.graphHighlightedNodes = Collections.emptySet();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (hasCreateCraftScreen) {
            if (this.countField.charTyped(codePoint, modifiers)) {
                this.countField.setValue(countField.getValue().replaceAll("\\D+", ""));
                return true;
            }
        }

        if (!hasCreateCraftScreen && !hasCreateVisualCraftScreen) {
            if (this.searchField.charTyped(codePoint, modifiers)) {
                new PacketUpdateMonitor(this.container.player, this.container.base.pos, this.searchField.getValue());
                this.container.base.fieldString = this.searchField.getValue();
                value = 0;
                return true;
            }
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (hasCreateCraftScreen) {
            if (keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
                this.countField.keyPressed(keyCode, scanCode, modifiers);
                this.countField.setValue(countField.getValue().replaceAll("\\D+", ""));
                return true;
            }
            if (this.countField.keyPressed(keyCode, scanCode, modifiers)) {
                this.countField.setValue(countField.getValue().replaceAll("\\D+", ""));
                return true;
            }
        }

        if (!hasCreateCraftScreen && !hasCreateVisualCraftScreen) {
            if (keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
                this.searchField.keyPressed(keyCode, scanCode, modifiers);
                new PacketUpdateMonitor(this.container.player, this.container.base.pos, this.searchField.getValue());
                this.container.base.fieldString = this.searchField.getValue();
                value = 0;
                return true;
            }
            if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
                new PacketUpdateMonitor(this.container.player, this.container.base.pos, this.searchField.getValue());
                this.container.base.fieldString = this.searchField.getValue();
                value = 0;
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d4, double d3) {

        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);

        if (canCreate == null) {
            ScrollDirection direction = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;

            int endMouseY = switch (this.container.base.sizeMode) {
                default -> 125;
                case 1 -> 161;
                case 2 -> 71;
            };

            if (mouseX >= 193 && mouseX <= 201 && mouseY >= 15 && mouseY <= endMouseY) {
                if (direction != ScrollDirection.stopped) {
                    if (direction == ScrollDirection.down) {
                        value++;
                        value = Math.min(value, maxValue);
                    } else {
                        value--;
                        value = Math.max(0, value);
                    }
                    return true;
                }
            }

            if (mouseX >= 25 && mouseX <= 186 && mouseY >= 17 && mouseY <= endMouseY) {
                if (direction != ScrollDirection.stopped) {
                    if (direction == ScrollDirection.down) {
                        value++;
                        value = Math.min(value, maxValue);
                    } else {
                        value--;
                        value = Math.max(0, value);
                    }
                    return true;
                }
            }
        } else {
            ScrollDirection direction = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;

            if (hasCreateGraphScreen && craftGraph != null) {
                double localX = d - this.guiLeft;
                double localY = d2 - this.guiTop;

                double panelX = 3;
                double panelY = 3;
                double panelW = 252;
                double panelH = 227;

                if (localX >= panelX && localX <= panelX + panelW && localY >= panelY && localY <= panelY + panelH) {
                    double prevZoom = graphZoom;
                    graphZoom = Mth.clamp(graphZoom + d3 * 0.08D, 0.32D, 0.79D);

                    double panelCenterX = panelX + panelW / 2.0;
                    double panelCenterY = panelY + panelH / 2.0;

                    double worldXBefore = graphCameraX + (localX - panelCenterX) / prevZoom;
                    double worldYBefore = graphCameraY + (localY - panelCenterY) / prevZoom;

                    double worldXAfter = graphCameraX + (localX - panelCenterX) / graphZoom;
                    double worldYAfter = graphCameraY + (localY - panelCenterY) / graphZoom;

                    graphCameraX += (worldXBefore - worldXAfter);
                    graphCameraY += (worldYBefore - worldYAfter);
                    return true;
                }
            }

            if (mouseX >= 8 && mouseX <= 168 && mouseY >= 18 && mouseY <= 140) {
                if (direction != ScrollDirection.stopped) {
                    if (direction == ScrollDirection.down) {
                        value1++;
                        value1 = Math.min(value1, maxValue1);
                    } else {
                        value1--;
                        value1 = Math.max(0, value1);
                    }
                    return true;
                }
            }
        }

        return super.mouseScrolled(d, d2, d4, d3);
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        if (canCreate == null) {
            this.bindTexture();
            poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
        }
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        if (this.container.base.network == null) {
            return;
        }

        viewModeVisible = false;
        viewSizeVisible = false;
        viewModeDecreasing = false;
        viewModeField = false;

        if (par1 >= 0 && par1 <= 16 && par2 >= 20 && par2 <= 38) {
            viewModeVisible = true;
            switch (this.viewMode) {
                case 0 ->
                        new AdvancedTooltipWidget(this, 0, 20, 16, 38).withTooltip(Localization.translate("iu.monitor.all")).drawForeground(poseStack, par1, par2);
                case 1 ->
                        new AdvancedTooltipWidget(this, 0, 20, 16, 38).withTooltip(Localization.translate("iu.monitor.storage")).drawForeground(poseStack, par1, par2);
                case 2 ->
                        new AdvancedTooltipWidget(this, 0, 20, 16, 38).withTooltip(Localization.translate("iu.monitor.crafts")).drawForeground(poseStack, par1, par2);
            }
        }
        if (par1 >= 0 && par1 <= 16 && par2 >= 0 && par2 <= 18) {
            viewSizeVisible = true;
            switch (this.container.base.sizeMode) {
                case 2 ->
                        new AdvancedTooltipWidget(this, 0, 0, 16, 18).withTooltip(Localization.translate("iu.monitor.small_size")).drawForeground(poseStack, par1, par2);
                case 0 ->
                        new AdvancedTooltipWidget(this, 0, 0, 16, 18).withTooltip(Localization.translate("iu.monitor.medium_size")).drawForeground(poseStack, par1, par2);
                case 1 ->
                        new AdvancedTooltipWidget(this, 0, 0, 16, 18).withTooltip(Localization.translate("iu.monitor.big_size")).drawForeground(poseStack, par1, par2);
            }
        }
        if (par1 >= 0 && par1 <= 16 && par2 >= 40 && par2 <= 58) {
            viewModeDecreasing = true;
            switch (this.decreasing) {
                case 0 ->
                        new AdvancedTooltipWidget(this, 0, 40, 16, 58).withTooltip(Localization.translate("iu.monitor.sort_decreasing")).drawForeground(poseStack, par1, par2);
                case 1 ->
                        new AdvancedTooltipWidget(this, 0, 40, 16, 58).withTooltip(Localization.translate("iu.monitor.sort_increasing")).drawForeground(poseStack, par1, par2);
                case 2 ->
                        new AdvancedTooltipWidget(this, 0, 40, 16, 58).withTooltip(Localization.translate("iu.monitor.sort_az")).drawForeground(poseStack, par1, par2);
                case 3 ->
                        new AdvancedTooltipWidget(this, 0, 40, 16, 58).withTooltip(Localization.translate("iu.monitor.sort_za")).drawForeground(poseStack, par1, par2);
            }
        }
        if (par1 >= 0 && par1 <= 16 && par2 >= 60 && par2 <= 78) {
            viewModeField = true;
            switch (this.container.base.fieldMode) {
                case 0 ->
                        new AdvancedTooltipWidget(this, 0, 60, 16, 78).withTooltip(Localization.translate("iu.monitor.search_with")).drawForeground(poseStack, par1, par2);
                case 1 ->
                        new AdvancedTooltipWidget(this, 0, 60, 16, 78).withTooltip(Localization.translate("iu.monitor.search_without")).drawForeground(poseStack, par1, par2);
                case 2 ->
                        new AdvancedTooltipWidget(this, 0, 60, 16, 78).withTooltip(Localization.translate("iu.monitor.search_with_text")).drawForeground(poseStack, par1, par2);
                case 3 ->
                        new AdvancedTooltipWidget(this, 0, 60, 16, 78).withTooltip(Localization.translate("iu.monitor.search_text")).drawForeground(poseStack, par1, par2);
            }
        }

        hover = -1;

        if (canCreate == null) {
            List<SameStack> stacks = this.container.base.network.getFluids();
            List<SameStack> creatable = this.container.base.network.getFluidsCanCreate();
            List<Boolean> isCraft = new LinkedList<>();

            String query = searchField != null ? searchField.getValue().trim().toLowerCase() : "";
            boolean searchActive = !query.isEmpty();

            TagKey<Fluid> key = null;
            boolean searchModId = false;
            String modId = "";

            if (query.startsWith("#") && query.length() >= 2) {
                key = TagKey.create(Registries.FLUID, ResourceLocation.tryParse(query.substring(1)));
            }
            if (query.startsWith("@") && query.length() >= 2) {
                searchModId = true;
                modId = query.substring(1);
            }

            List<FluidStack> filtered = new LinkedList<>();

            switch (this.viewMode) {
                case 0 -> {
                    for (SameStack s : stacks) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(stack);
                            isCraft.add(false);
                        }
                    }
                    for (SameStack s : creatable) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(stack);
                            isCraft.add(true);
                        }
                    }
                }
                case 1 -> {
                    for (SameStack s : stacks) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(stack);
                            isCraft.add(false);
                        }
                    }
                }
                case 2 -> {
                    for (SameStack s : stacks) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            PatternStack patternStack = container.base.network.findPatternFor(stack);
                            if (patternStack != null) {
                                filtered.add(stack);
                                isCraft.add(true);
                            }
                        }
                    }
                    for (SameStack s : creatable) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(s.getFluidStack().getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(stack);
                            PatternStack patternStack = container.base.network.findPatternFor(stack);
                            isCraft.add(patternStack != null);
                        }
                    }
                }
            }

            List<Integer> order = new ArrayList<>();
            for (int ii = 0; ii < filtered.size(); ii++) {
                order.add(ii);
            }

            List<FluidStack> finalFiltered1 = filtered;
            Comparator<Integer> comparator = Comparator.comparingInt(ii -> finalFiltered1.get(ii).getAmount());

            if (decreasing == 0) {
                comparator = comparator.reversed();
            }

            Collator collator = Collator.getInstance(Minecraft.getInstance().getLocale());
            collator.setStrength(Collator.PRIMARY);

            if (decreasing == 2) {
                comparator = Comparator.comparing(ii -> com.denfop.utils.ModUtils.cleanComponentString(finalFiltered1.get(ii).getHoverName().getString()), collator);
            }
            if (decreasing == 3) {
                comparator = Comparator.comparing(ii -> com.denfop.utils.ModUtils.cleanComponentString(finalFiltered1.get(ii).getHoverName().getString()), collator.reversed());
            }

            order.sort(comparator);

            List<FluidStack> sortedStacks = new ArrayList<>(filtered.size());
            List<Boolean> sortedIsCraft = new ArrayList<>(isCraft.size());

            for (int ii : order) {
                sortedStacks.add(filtered.get(ii));
                sortedIsCraft.add(isCraft.get(ii));
            }

            filtered.clear();
            filtered.addAll(sortedStacks);

            isCraft.clear();
            isCraft.addAll(sortedIsCraft);

            int totalSize = filtered.size();
            int startIndex = value * 9;
            int maxRows = 6;
            if (this.container.base.sizeMode == 2) {
                maxRows = 3;
            }
            if (this.container.base.sizeMode == 1) {
                maxRows = 8;
            }

            int endIndex = Math.min(startIndex + maxRows * 9, totalSize);

            for (int i = startIndex; i < endIndex; i++) {
                FluidStack stack = filtered.get(i);
                boolean craft = isCraft.get(i);
                int visibleIndex = i - startIndex;

                int x1 = 26 + ((visibleIndex % 9) * 18);
                int x2 = x1 + 18;
                int y1 = 18 + ((visibleIndex / 9) * 18);
                int y2 = y1 + 18;

                boolean inX = par1 >= x1 && par1 < x2;
                boolean inY = par2 >= y1 && par2 < y2;

                if (inX && inY) {
                    hover = visibleIndex;

                    if (!craft) {
                        SameStack sameStack = new SameStack(stack);
                        Integer mapForCraft = this.container.base.network.stacksMapCount
                                .getOrDefault(BuiltInRegistries.FLUID.getKey(stack.getFluid()).toString(), new HashMap<>())
                                .getOrDefault(sameStack, 0);

                        if (mapForCraft == 0) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, stack,
                                    Collections.singletonList(Localization.translate("chance.ore") + stack.getAmount() + "mB"));
                        } else {
                            this.drawTooltipOnlyName(poseStack, par1, par2, stack,
                                    Collections.singletonList(
                                            Localization.translate("chance.ore") + stack.getAmount() + "mB, "
                                                    + Localization.translate("iu.monitor.blocked") + mapForCraft + "mB"
                                    ));
                        }
                    } else {
                        this.drawTooltipOnlyName(poseStack, par1, par2, stack,
                                Collections.singletonList(Localization.translate("iu.monitor.can_create")));
                    }
                    return;
                }
            }
        }

        if (canCreate != null) {
            if (hasCreateCraftScreen) {
                int x1 = 24;
                int x2 = x1 + 18;
                int y1 = 95;
                int y2 = y1 + 18;

                boolean inX = par1 >= x1 && par1 < x2;
                boolean inY = par2 >= y1 && par2 < y2;

                if (inX && inY && canCreate.isItem()) {
                    this.drawTooltip(poseStack, par1, par2, canCreate.getStack());
                }
                if (inX && inY && canCreate.isFluid()) {
                    this.drawTooltipOnlyName(poseStack, par1, par2, canCreate.getFluidStack(),
                            Collections.singletonList(Localization.translate("iu.monitor.can_create")));
                }

                button.drawForeground(poseStack, par1, par2);
                exitButton.drawForeground(poseStack, par1, par2);
                oneItemButton.drawForeground(poseStack, par1, par2);
                tenItemButton.drawForeground(poseStack, par1, par2);
                hundredButton.drawForeground(poseStack, par1, par2);
                thousandButton.drawForeground(poseStack, par1, par2);
                tenThousandButton.drawForeground(poseStack, par1, par2);
                minusOneItemButton.drawForeground(poseStack, par1, par2);
                minusTenItemButton.drawForeground(poseStack, par1, par2);
                minusHundredButton.drawForeground(poseStack, par1, par2);
                minusThousandButton.drawForeground(poseStack, par1, par2);
                minusTenThousandButton.drawForeground(poseStack, par1, par2);
            }

            if (hasCreateGraphScreen) {
                backGraphButton.drawForeground(poseStack, par1, par2);

                int graphPanelX = guiLeft + 3;
                int graphPanelW = 249;
                int graphPanelH = 223;
                int graphPanelY = guiTop - 80;

                if (craftGraph != null) {
                    graphHoveredNodeId = craftGraphRenderer.findHoveredNode(
                            craftGraph,
                            graphPanelX,
                            graphPanelY,
                            graphPanelW,
                            graphPanelH,
                            graphCameraX,
                            graphCameraY,
                            graphZoom,
                            par1 + guiLeft,
                            par2 + guiTop
                    );

                    if (graphHoveredNodeId != null) {
                        CraftNode node = craftGraph.getNode(graphHoveredNodeId);
                        if (node != null) {
                            poseStack.renderTooltip(this.font, craftGraphRenderer.buildTooltip(node), Optional.empty(), par1, par2);
                        }
                    }
                }
            }

            if (hasCreateVisualCraftScreen) {
                graphButton.drawForeground(poseStack, par1, par2);
                cancelButton.drawForeground(poseStack, par1, par2);

                int cols = 3;
                int slotSize = 52;
                int startX = 12;
                int startY = 15;

                List<AutoCraftVisualStep> steps = autoCraftSystemVisual.getVisualSteps();

                RenderSystem.enableBlend();

                int startIndex = value1 * 3;
                int endIndex = Math.min(startIndex + 6 * 3, steps.size());

                for (int i = startIndex; i < endIndex; i++) {
                    AutoCraftVisualStep step = steps.get(i);
                    List<String> lines = new LinkedList<>();
                    String mb = "mb";

                    if (step.stack() instanceof ItemStack) {
                        mb = "";
                    }
                    if (step.stack() instanceof SameStack stack && stack.isItem()) {
                        mb = "";
                    }

                    if (step.create() > 0 && step.need() > 0) {
                        lines.add(Localization.translate("iu.monitor.create") + step.create() + mb);
                        if (i != 0) {
                            lines.add(Localization.translate("iu.monitor.require") + step.need() + mb);
                        }
                    } else if (step.need() > 0) {
                        lines.add(Localization.translate("iu.monitor.require") + step.need() + mb);
                    }
                    if (step.have() > 0) {
                        lines.add(Localization.translate("iu.monitor.available") + step.have() + mb);
                    }
                    if (step.willBeCreate() > 0) {
                        lines.add(Localization.translate("iu.monitor.will_create") + step.willBeCreate() + mb);
                    }

                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;
                    int x = startX + col * slotSize;
                    int y = startY + row * 21;

                    boolean inX = par1 >= x && par1 < x + slotSize;
                    boolean inY = par2 >= y && par2 < y + 21;

                    if (inX && inY) {
                        if (step.stack() instanceof SameStack item) {
                            if (item.isItem()) {
                                this.drawTooltipOnlyName(poseStack, par1, par2, item.getStack(), lines);
                            } else {
                                this.drawTooltipOnlyName(poseStack, par1, par2, item.getFluidStack(), lines);
                            }
                        } else if (step.stack() instanceof ItemStack item) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item, lines);
                        } else if (step.stack() instanceof FluidStack item) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item, lines);
                        }
                    }
                }

                createButton.drawForeground(poseStack, par1, par2);
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            if (this.mainScrollbarDragging && this.canCreate == null) {
                this.updateMainScrollFromMouse(mouseY - this.topPos);
                return true;
            }

            if (this.visualScrollbarDragging && this.hasCreateVisualCraftScreen) {
                this.updateVisualScrollFromMouse(mouseY - this.topPos);
                return true;
            }

            if (hasCreateGraphScreen && craftGraph != null && graphDragging) {
                this.graphCameraX -= dragX / graphZoom;
                this.graphCameraY -= dragY / graphZoom;
                this.graphLastMouseX = mouseX - this.leftPos;
                this.graphLastMouseY = mouseY - this.topPos;
                return true;
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean handled = false;

        if (button == 0) {
            if (this.mainScrollbarDragging) {
                this.mainScrollbarDragging = false;
                handled = true;
            }
            if (this.visualScrollbarDragging) {
                this.visualScrollbarDragging = false;
                handled = true;
            }
            if (hasCreateGraphScreen && graphDragging) {
                graphDragging = false;
                handled = true;
            }
        }

        if (handled) {
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);

        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;

        InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(k);

        if (this.searchField.mouseClicked(i, j, k)) {
            searchField.setFocused(true);
            return;
        } else {
            searchField.setFocused(false);
        }

        if (this.container.base.network == null) {
            return;
        }
        if (!this.container.base.network.hasFreeEnergy()) {
            return;
        }

        if (viewModeDecreasing) {
            new PacketUpdateMonitor(container.player, container.base.pos, 1);
        }
        if (viewModeField) {
            new PacketUpdateMonitor(container.player, container.base.pos, 3);
        }
        if (viewModeVisible) {
            new PacketUpdateMonitor(container.player, container.base.pos, 2);
        }
        if (viewSizeVisible) {
            new PacketUpdateMonitor(container.player, container.base.pos, 0);
        }

        if (k == 0) {
            if (this.canCreate == null && isInsideMainScrollbar(x, y)) {
                this.mainScrollbarDragging = true;
                this.visualScrollbarDragging = false;
                this.updateMainScrollFromMouse(y);
                return;
            }

            if (this.canCreate != null && this.hasCreateVisualCraftScreen && isInsideVisualScrollbar(x, y)) {
                this.visualScrollbarDragging = true;
                this.mainScrollbarDragging = false;
                this.updateVisualScrollFromMouse(y);
                return;
            }
        }

        if (hasCreateCraftScreen || hasCreateVisualCraftScreen || hasCreateGraphScreen) {
            if (hasCreateGraphScreen) {
                if (backGraphButton.visible && backGraphButton.contains(x, y)) {
                    hasCreateGraphScreen = false;
                    hasCreateVisualCraftScreen = true;
                    graphDragging = false;
                    this.craftGraph = null;
                    return;
                }

                if (x >= 3 && x <= 255 && y >= 3 && y <= 230) {
                    graphDragging = true;
                    graphLastMouseX = x;
                    graphLastMouseY = y;

                    if (craftGraph != null) {
                        graphHoveredNodeId = craftGraphRenderer.findHoveredNode(
                                craftGraph,
                                guiLeft + 3,
                                guiTop - 80,
                                249,
                                223,
                                graphCameraX,
                                graphCameraY,
                                graphZoom,
                                i,
                                j
                        );

                        if (graphHoveredNodeId != null) {
                            graphSelectedNodeId = graphHoveredNodeId;
                            Set<Integer> down = craftGraph.collectDescendantsInclusive(graphSelectedNodeId);
                            Set<Integer> up = craftGraph.collectAncestorsInclusive(graphSelectedNodeId);
                            HashSet<Integer> all = new HashSet<>(down);
                            all.addAll(up);
                            graphHighlightedNodes = all;
                        } else {
                            graphSelectedNodeId = null;
                            graphHighlightedNodes = Collections.emptySet();
                        }
                    }
                    return;
                }
            }

            if (hasCreateCraftScreen) {
                x += 118;
                y += 60;

                if (exitButton.visible && exitButton.contains(x, y)) {
                    hasCreateCraftScreen = false;
                    this.canCreate = null;
                }
                if (oneItemButton.visible && oneItemButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(amount + 1));
                }
                if (minusOneItemButton.visible && minusOneItemButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(Math.max(amount - 1, 1)));
                }
                if (minusTenItemButton.visible && minusTenItemButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(Math.max(amount - 10, 1)));
                }
                if (minusHundredButton.visible && minusHundredButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(Math.max(amount - 100, 1)));
                }
                if (minusThousandButton.visible && minusThousandButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(Math.max(amount - 1000, 1)));
                }
                if (minusTenThousandButton.visible && minusTenThousandButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(Math.max(amount - 10000, 1)));
                }
                if (tenItemButton.visible && tenItemButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(amount + 10));
                }
                if (hundredButton.visible && hundredButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(amount + 100));
                }
                if (thousandButton.visible && thousandButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(amount + 1000));
                }
                if (tenThousandButton.visible && tenThousandButton.contains(x, y)) {
                    int amount = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    this.countField.setValue(String.valueOf(amount + 10000));
                }

                if (button.visible && button.contains(x, y)
                        && !countField.getValue().isEmpty()
                        && !countField.getValue().replaceAll("\\D+", "").isEmpty()) {
                    int count = Integer.parseInt(countField.getValue().replaceAll("\\D+", ""));
                    FluidStack stack = canCreate.getFluidStack().copy();
                    stack.setAmount(count);
                    canCreate = new SameStack(stack);

                    this.autoCraftSystemVisual = this.container.base.network.createAutoCraftVisual(canCreate);
                    this.hasCreateCraftScreen = false;
                    this.hasCreateVisualCraftScreen = true;
                }
            }

            if (hasCreateGraphScreen && cancelButton.visible && cancelButton.contains(x, y)) {
                hasCreateVisualCraftScreen = true;
                hasCreateGraphScreen = false;
                craftGraph = null;
                graphDragging = false;
            }

            if (hasCreateVisualCraftScreen) {
                if (graphButton.visible && graphButton.contains(x, y)) {
                    hasCreateVisualCraftScreen = false;
                    hasCreateGraphScreen = true;
                    rebuildCraftGraph();
                    return;
                }

                if (cancelButton.visible && cancelButton.contains(x, y)) {
                    hasCreateVisualCraftScreen = false;
                    canCreate = null;
                }

                if (createButton.visible && createButton.contains(x, y) && canAddAutoCraft && autoCraftSystemVisual.canAddToProcessor()) {
                    this.autoCraftSystemVisual = null;
                    this.hasCreateCraftScreen = false;
                    this.hasCreateVisualCraftScreen = false;
                    new PacketAddAutoCraft(container.player, container.base.pos, canCreate);
                    canCreate = null;
                    this.countField.setValue("1");
                }
            }
            return;
        }

        List<SameStack> stacks = this.container.base.network.getFluids();
        List<SameStack> creatable = this.container.base.network.getFluidsCanCreate();

        String query = searchField != null ? searchField.getValue().trim().toLowerCase() : "";
        boolean searchActive = !query.isEmpty();

        TagKey<Fluid> key = null;
        boolean searchModId = false;
        String modId = "";

        if (query.startsWith("#") && query.length() >= 2) {
            key = TagKey.create(Registries.FLUID, ResourceLocation.tryParse(query.substring(1)));
        }
        if (query.startsWith("@") && query.length() >= 2) {
            searchModId = true;
            modId = query.substring(1);
        }

        List<FluidStack> filtered = new ArrayList<>();
        List<Boolean> isCraft = new ArrayList<>();

        switch (this.viewMode) {
            case 0 -> {
                for (SameStack s : stacks) {
                    FluidStack stack = s.getFluidStack();
                    if (!searchActive || (key != null && stack.is(key))
                            || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                            || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                        filtered.add(stack);
                        isCraft.add(false);
                    }
                }
                for (SameStack s : creatable) {
                    FluidStack stack = s.getFluidStack();
                    if (!searchActive || (key != null && stack.is(key))
                            || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                            || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                        filtered.add(stack);
                        isCraft.add(true);
                    }
                }
            }
            case 1 -> {
                for (SameStack s : stacks) {
                    FluidStack stack = s.getFluidStack();
                    if (!searchActive || (key != null && stack.is(key))
                            || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                            || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                        filtered.add(stack);
                        isCraft.add(false);
                    }
                }
            }
            case 2 -> {
                for (SameStack s : stacks) {
                    FluidStack stack = s.getFluidStack();
                    if (!searchActive || (key != null && stack.is(key))
                            || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                            || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                        PatternStack patternStack = container.base.network.findPatternFor(stack);
                        if (patternStack != null) {
                            filtered.add(stack);
                            isCraft.add(true);
                        }
                    }
                }
                for (SameStack s : creatable) {
                    FluidStack stack = s.getFluidStack();
                    if (!searchActive || (key != null && stack.is(key))
                            || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                            || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                        filtered.add(stack);
                        isCraft.add(true);
                    }
                }
            }
        }

        List<Integer> order = new ArrayList<>();
        for (int ii = 0; ii < filtered.size(); ii++) {
            order.add(ii);
        }

        List<FluidStack> finalFiltered1 = filtered;
        Comparator<Integer> comparator = Comparator.comparingInt(ii -> finalFiltered1.get(ii).getAmount());

        if (decreasing == 0) {
            comparator = comparator.reversed();
        }

        Collator collator = Collator.getInstance(Minecraft.getInstance().getLocale());
        collator.setStrength(Collator.PRIMARY);

        if (decreasing == 2) {
            comparator = Comparator.comparing(ii -> com.denfop.utils.ModUtils.cleanComponentString(finalFiltered1.get(ii).getHoverName().getString()), collator);
        }
        if (decreasing == 3) {
            comparator = Comparator.comparing(ii -> com.denfop.utils.ModUtils.cleanComponentString(finalFiltered1.get(ii).getHoverName().getString()), collator.reversed());
        }

        order.sort(comparator);

        List<FluidStack> sortedStacks = new ArrayList<>(filtered.size());
        List<Boolean> sortedIsCraft = new ArrayList<>(isCraft.size());
        for (int ii : order) {
            sortedStacks.add(filtered.get(ii));
            sortedIsCraft.add(isCraft.get(ii));
        }

        filtered.clear();
        filtered.addAll(sortedStacks);

        isCraft.clear();
        isCraft.addAll(sortedIsCraft);

        int totalSize = filtered.size();
        int startIndex = value * 9;
        int maxRows = 6;
        if (this.container.base.sizeMode == 2) {
            maxRows = 3;
        }
        if (this.container.base.sizeMode == 1) {
            maxRows = 8;
        }
        int visibleSlots = maxRows * 9;

        for (int visibleIndex = 0; visibleIndex < visibleSlots; visibleIndex++) {
            int globalIndex = startIndex + visibleIndex;
            if (globalIndex >= totalSize) {
                break;
            }

            int x1 = 26 + ((visibleIndex % 9) * 18);
            int x2 = x1 + 18;
            int y1 = 17 + ((visibleIndex / 9) * 18);
            int y2 = y1 + 18;

            boolean inX = x >= x1 && x < x2;
            boolean inY = y >= y1 && y < y2;

            if (!(inX && inY)) {
                continue;
            }

            boolean left = mouseKey.getValue() == 0;
            boolean right = mouseKey.getValue() == 1;
            boolean middle = mouseKey.getValue() == 2;
            boolean isShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

            ItemStack carried = container.getCarried();
            FluidStack stack = filtered.get(globalIndex);
            boolean craft = isCraft.get(globalIndex);

            if (!FluidHandlerFix.hasFluidHandler(carried) || carried.getCount() > 1) {
                if (FluidHandlerFix.hasFluidHandler(carried) && carried.getCount() > 1 && !FluidHandlerFix.getFluidHandler(carried).getFluidInTank(0).isEmpty()) {
                    carried = carried.copy();
                    int amount = carried.getCount();
                    carried.setCount(1);

                    IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);
                    @NotNull FluidStack drain = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                    if (drain == null || drain.isEmpty()) {
                        return;
                    }

                    FluidStack insert = drain.copy();
                    insert.setAmount(insert.getAmount() * amount);

                    int canAdd = this.container.base.network.canAdd(insert);
                    if (canAdd / amount == drain.getAmount()) {
                        drain = handler.drain(canAdd / amount, IFluidHandler.FluidAction.SIMULATE);
                        if (canAdd > 0 && drain != null && !drain.isEmpty()) {
                            insert.setAmount(canAdd);
                            handler.drain(canAdd / amount, IFluidHandler.FluidAction.EXECUTE);
                            carried.setCount(amount);
                            new PacketAddStack(container.player, insert, container.base.pos);
                        }
                    }
                } else if (FluidHandlerFix.hasFluidHandler(carried) && !stack.isEmpty() && carried.getCount() > 1 && FluidHandlerFix.getFluidHandler(carried).getFluidInTank(0).isEmpty()) {
                    carried = carried.copy();
                    int amount = carried.getCount();
                    carried.setCount(1);

                    IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);

                    int canRemove = this.container.base.network.canRemoveMonitor(stack);
                    if (canRemove == 0) {
                        return;
                    }

                    FluidStack insert = stack.copy();
                    insert.setAmount(canRemove);

                    int insertFluid = handler.fill(insert, IFluidHandler.FluidAction.SIMULATE);
                    if (insertFluid == 0) {
                        return;
                    }
                    if (insertFluid * amount > canRemove) {
                        return;
                    }

                    insert.setAmount(insertFluid * amount);
                    canRemove = this.container.base.network.canRemoveMonitor(insert);
                    insert.setAmount(canRemove);

                    if (canRemove / amount == insertFluid) {
                        new PacketRemoveStack(container.player, insert, container.player.level(), container.base.pos);
                    }
                } else if (craft && stack.isEmpty()) {
                    if (left || right) {
                        hasCreateCraftScreen = true;
                        this.container.isActiveAllInventory = !(this.imageTwo.visible || this.imageThree.visible);
                        new PacketUpdateMonitorInterface(this.container.player, !(this.imageTwo.visible || this.imageThree.visible));
                        this.canCreate = new SameStack(stack);
                    }
                } else if (middle) {
                    PatternStack patternStack = container.base.network.findPatternFor(stack);
                    if (patternStack != null) {
                        hasCreateCraftScreen = true;
                        this.container.isActiveAllInventory = !(this.imageTwo.visible || this.imageThree.visible);
                        new PacketUpdateMonitorInterface(this.container.player, !(this.imageTwo.visible || this.imageThree.visible));
                        this.canCreate = new SameStack(stack);
                    }
                }
                return;
            }

            IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);

            if (handler.getFluidInTank(0).isEmpty()) {
                if (craft) {
                    if (left || right) {
                        hasCreateCraftScreen = true;
                        this.container.isActiveAllInventory = !(this.imageTwo.visible || this.imageThree.visible);
                        new PacketUpdateMonitorInterface(this.container.player, !(this.imageTwo.visible || this.imageThree.visible));
                        this.canCreate = new SameStack(stack);
                    }
                    return;
                } else if (!isShift) {
                    if (left) {
                        int maxAmount = handler.getTankCapacity(0) - handler.getFluidInTank(0).getAmount();

                        FluidStack s = stack.copy();
                        int fill = handler.fill(s, IFluidHandler.FluidAction.SIMULATE);
                        if (fill == 0) {
                            return;
                        }

                        s.setAmount(Math.min(maxAmount, fill));
                        fill = handler.fill(s, IFluidHandler.FluidAction.SIMULATE);
                        if (fill != s.getAmount() || fill == 0) {
                            return;
                        }

                        int count = this.container.base.network.canRemoveMonitor(s);
                        if (count > 0) {
                            s.setAmount(count);
                            new PacketRemoveStack(container.player, s, container.player.level(), container.base.pos);
                        }
                        return;
                    }
                    if (right) {
                        int maxAmount = handler.getTankCapacity(0) - handler.getFluidInTank(0).getAmount();

                        FluidStack s = stack.copy();
                        int fill = handler.fill(s, IFluidHandler.FluidAction.SIMULATE);
                        if (fill == 0) {
                            return;
                        }

                        s.setAmount(Math.min(maxAmount, fill) / 2);
                        fill = handler.fill(s, IFluidHandler.FluidAction.SIMULATE);
                        if (fill != s.getAmount() || fill == 0) {
                            return;
                        }

                        int count = this.container.base.network.canRemoveMonitor(s);
                        if (count > 0) {
                            s.setAmount(count);
                            new PacketRemoveStack(container.player, s, container.player.level(), container.base.pos);
                        }
                        return;
                    }
                    if (middle) {
                        PatternStack patternStack = container.base.network.findPatternFor(stack);
                        if (patternStack != null) {
                            hasCreateCraftScreen = true;
                            this.container.isActiveAllInventory = !(this.imageTwo.visible || this.imageThree.visible);
                            new PacketUpdateMonitorInterface(this.container.player, !(this.imageTwo.visible || this.imageThree.visible));
                            this.canCreate = new SameStack(stack);
                        }
                    }
                }
            } else {
                @NotNull FluidStack drain = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                if (drain == null || drain.isEmpty()) {
                    return;
                }

                FluidStack insert = drain.copy();
                if (right) {
                    insert.setAmount(Math.min(Math.max(insert.getAmount() / 2, 1), handler.getTankCapacity(0) / 2));
                }

                int canAdd = this.container.base.network.canAdd(insert);
                drain = handler.drain(canAdd, IFluidHandler.FluidAction.SIMULATE);

                if (canAdd > 0 && drain != null && !drain.isEmpty()) {
                    insert.setAmount(canAdd);
                    new PacketAddStack(container.player, insert, container.base.pos);
                } else if (craft) {
                    if (left || right) {
                        hasCreateCraftScreen = true;
                        this.container.isActiveAllInventory = !(this.imageTwo.visible || this.imageThree.visible);
                        new PacketUpdateMonitorInterface(this.container.player, !(this.imageTwo.visible || this.imageThree.visible));
                        this.canCreate = new SameStack(stack);
                    }
                }
                return;
            }
        }

        boolean inX = x >= 25 && x <= 186;
        boolean inY = y >= 25 && y <= maxRows * 18 + 25;

        ItemStack carried = container.getCarried();

        if (!FluidHandlerFix.hasFluidHandler(carried) || carried.getCount() > 1) {
            if (FluidHandlerFix.hasFluidHandler(carried) && carried.getCount() > 1 && inX && inY && !FluidHandlerFix.getFluidHandler(carried).getFluidInTank(0).isEmpty()) {
                carried = carried.copy();
                int amount = carried.getCount();
                carried.setCount(1);

                IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);
                @NotNull FluidStack drain = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                if (drain == null || drain.isEmpty()) {
                    return;
                }

                FluidStack insert = drain.copy();
                insert.setAmount(insert.getAmount() * amount);

                int canAdd = this.container.base.network.canAdd(insert);
                if (canAdd / amount == drain.getAmount()) {
                    drain = handler.drain(canAdd / amount, IFluidHandler.FluidAction.SIMULATE);
                    if (canAdd > 0 && drain != null && !drain.isEmpty()) {
                        insert.setAmount(canAdd);
                        handler.drain(canAdd / amount, IFluidHandler.FluidAction.EXECUTE);
                        carried.setCount(amount);
                        new PacketAddStack(container.player, insert, container.base.pos);
                    }
                }
            }
            return;
        }

        IFluidHandlerItem handler = FluidHandlerFix.getFluidHandler(carried);

        if (inX && inY && !handler.getFluidInTank(0).isEmpty()) {
            @NotNull FluidStack drain = handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
            if (drain == null || drain.isEmpty()) {
                return;
            }

            FluidStack insert = drain.copy();
            boolean right = mouseKey.getValue() == 1;
            if (right) {
                insert.setAmount(Math.min(Math.max(insert.getAmount() / 2, 1), handler.getTankCapacity(0) / 2));
            }

            int canAdd = this.container.base.network.canAdd(insert);
            drain = handler.drain(canAdd, IFluidHandler.FluidAction.SIMULATE);
            if (canAdd > 0 && drain != null && !drain.isEmpty()) {
                insert.setAmount(canAdd);
                new PacketAddStack(container.player, insert, container.base.pos);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        if (this.container.base.network == null) {
            return;
        }

        if (canCreate == null) {
            switch (this.viewMode) {
                case 1 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 20, 239, 18, 17, 19);
                case 2 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 20, 239, 38, 17, 19);
            }

            switch (this.container.base.sizeMode) {
                case 2 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 0, 239, 118, 17, 19);
                case 1 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 0, 239, 138, 17, 19);
            }

            switch (decreasing) {
                case 1 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 40, 239, 58, 17, 19);
                case 2 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 40, 239, 78, 17, 19);
                case 3 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 40, 239, 98, 17, 19);
            }

            switch (this.container.base.fieldMode) {
                case 1 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 60, 239, 176, 17, 17);
                case 2 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 60, 239, 194, 17, 17);
                case 3 -> drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 60, 239, 212, 17, 17);
            }

            if (viewModeVisible) {
                drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 20, 221, 14, 17, 19);
            }
            if (viewModeDecreasing) {
                drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 40, 221, 14, 17, 19);
            }
            if (viewModeField) {
                drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 60, 221, 14, 17, 19);
            }
            if (viewSizeVisible) {
                drawTexturedModalRect(poseStack, this.guiLeft + 0, this.guiTop + 0, 221, 14, 17, 19);
            }

            drawTexturedModalRect(poseStack, this.guiLeft + 193, (int) (guiTop + 17 + (value) * pointScroll), 237, 0, 9, 13);

            List<SameStack> stacks = this.container.base.network.getFluids();
            List<SameStack> creatable = this.container.base.network.getFluidsCanCreate();

            String query = searchField != null ? searchField.getValue().trim().toLowerCase() : "";
            boolean searchActive = !query.isEmpty();

            TagKey<Fluid> key = null;
            boolean searchModId = false;
            String modId = "";

            if (query.startsWith("#") && query.length() >= 2) {
                key = TagKey.create(Registries.FLUID, ResourceLocation.tryParse(query.substring(1)));
            }
            if (query.startsWith("@") && query.length() >= 2) {
                searchModId = true;
                modId = query.substring(1);
            }

            List<SameStack> filtered = new LinkedList<>();
            List<Boolean> isCraft = new LinkedList<>();

            switch (this.viewMode) {
                case 0 -> {
                    for (SameStack s : stacks) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                                || (key != null && stack.getFluid().is(key))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(s);
                            isCraft.add(false);
                        }
                    }
                    for (SameStack s : creatable) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(s);
                            isCraft.add(true);
                        }
                    }
                }
                case 1 -> {
                    for (SameStack s : stacks) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(s);
                            isCraft.add(false);
                        }
                    }
                }
                case 2 -> {
                    for (SameStack s : stacks) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            PatternStack patternStack = container.base.network.findPatternFor(stack);
                            if (patternStack != null) {
                                filtered.add(s);
                                isCraft.add(true);
                            }
                        }
                    }
                    for (SameStack s : creatable) {
                        FluidStack stack = s.getFluidStack();
                        if (!searchActive || (key != null && stack.getFluid().is(key))
                                || (searchModId && BuiltInRegistries.FLUID.getKey(stack.getFluid()).getNamespace().startsWith(modId))
                                || ChatFormatting.stripFormatting(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()).toLowerCase()).startsWith(query)) {
                            filtered.add(s);
                            isCraft.add(true);
                        }
                    }
                }
            }

            List<Integer> order = new ArrayList<>();
            for (int ii = 0; ii < filtered.size(); ii++) {
                order.add(ii);
            }

            List<SameStack> finalFiltered1 = filtered;
            Comparator<Integer> comparator = Comparator.comparingInt(ii -> finalFiltered1.get(ii).getAmount());

            if (decreasing == 0) {
                comparator = comparator.reversed();
            }

            Collator collator = Collator.getInstance(Minecraft.getInstance().getLocale());
            collator.setStrength(Collator.PRIMARY);

            if (decreasing == 2) {
                comparator = Comparator.comparing(ii -> com.denfop.utils.ModUtils.cleanComponentString(finalFiltered1.get(ii).getFluidStack().getHoverName().getString()), collator);
            }
            if (decreasing == 3) {
                comparator = Comparator.comparing(ii -> com.denfop.utils.ModUtils.cleanComponentString(finalFiltered1.get(ii).getFluidStack().getHoverName().getString()), collator.reversed());
            }

            order.sort(comparator);

            List<SameStack> sortedStacks = new ArrayList<>(filtered.size());
            List<Boolean> sortedIsCraft = new ArrayList<>(isCraft.size());

            for (int ii : order) {
                sortedStacks.add(filtered.get(ii));
                sortedIsCraft.add(isCraft.get(ii));
            }

            filtered.clear();
            filtered.addAll(sortedStacks);
            isCraft.clear();
            isCraft.addAll(sortedIsCraft);

            int totalSize = filtered.size();
            int startIndex = value * 9;
            int maxRows = 6;
            if (this.container.base.sizeMode == 2) {
                maxRows = 3;
            }
            if (this.container.base.sizeMode == 1) {
                maxRows = 8;
            }

            int endIndex = Math.min(startIndex + maxRows * 9, totalSize);

            for (int i = startIndex; i < endIndex; i++) {
                SameStack stack = filtered.get(i);
                boolean craft = isCraft.get(i);

                int visibleIndex = i - startIndex;
                int drawX = 26 + ((visibleIndex % 9) * 18);
                int drawY = 18 + ((visibleIndex / 9) * 18);

                RenderSystem.enableBlend();
                new FluidDefaultWidget(this, drawX - 1, drawY - 1, stack.getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);

                String s = craft ? "+" : ModUtils.getString(stack.getAmount());
                float scale = 0.4f;
                float textWidth = font.width(s) * scale;

                poseStack.pose().pushPose();
                poseStack.pose().translate(
                        getGuiLeft() + 30 + ((visibleIndex % 9) * 18) + 12 - textWidth,
                        guiTop + 21 + ((visibleIndex / 9) * 18) + 9,
                        200.0F
                );
                poseStack.pose().scale(scale, scale, 1f);
                poseStack.drawString(font, s, 0, 0, 0xFFFFFF, true);
                poseStack.pose().popPose();

                RenderSystem.disableBlend();

                if (hover == visibleIndex) {
                    renderSlotHighlight(poseStack, guiLeft + drawX, guiTop + drawY, 0, getSlotColor(0));
                }
            }
        }

        if (canCreate != null) {
            if (hasCreateCraftScreen) {
                new ScreenWidget(this, 24, 94, EnumTypeComponent.STORAGE_DEFAULT, new WidgetDefault<>(new EmptyWidget()))
                        .drawBackground(poseStack, this.guiLeft, this.guiTop);

                ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guistoragebutton.png"));
                RenderSystem.setShaderColor(1, 1, 1, 1);
                drawTexturedModalRect(poseStack, guiLeft + 50, guiTop + 97, 2, 126, 90, 12);

                if (!canCreate.isItem()) {
                    RenderSystem.enableBlend();
                    new FluidDefaultWidget(this, 24, 94, canCreate.getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);
                    RenderSystem.disableBlend();
                }

                button.drawBackground(poseStack, guiLeft, guiTop);
                exitButton.drawBackground(poseStack, guiLeft, guiTop);
                oneItemButton.drawBackground(poseStack, guiLeft, guiTop);
                tenItemButton.drawBackground(poseStack, guiLeft, guiTop);
                hundredButton.drawBackground(poseStack, guiLeft, guiTop);
                thousandButton.drawBackground(poseStack, guiLeft, guiTop);
                tenThousandButton.drawBackground(poseStack, guiLeft, guiTop);
                minusOneItemButton.drawBackground(poseStack, guiLeft, guiTop);
                minusTenItemButton.drawBackground(poseStack, guiLeft, guiTop);
                minusHundredButton.drawBackground(poseStack, guiLeft, guiTop);
                minusThousandButton.drawBackground(poseStack, guiLeft, guiTop);
                minusTenThousandButton.drawBackground(poseStack, guiLeft, guiTop);
            }

            if (canCreate != null && hasCreateGraphScreen) {
                new StorageInterfaceWidget(this, 0, 0, this.imageWidth, 160).drawBackground(poseStack, guiLeft, guiTop);
                RenderSystem.setShaderColor(1, 1, 1, 1);

                int graphPanelX = guiLeft + 3;
                int graphPanelW = 249;
                int graphPanelH = 223;
                int graphPanelY = guiTop - 80;

                backGraphButton.drawBackground(poseStack, guiLeft, guiTop);

                if (craftGraph != null) {
                    craftGraphLayout.layout(craftGraph);

                    enableScissor(graphPanelX, guiTop + 3, graphPanelX + graphPanelW, guiTop + 3 + 155);
                    try {
                        craftGraphRenderer.render(
                                poseStack,
                                craftGraph,
                                graphPanelX,
                                graphPanelY,
                                graphPanelW,
                                graphPanelH,
                                graphCameraX,
                                graphCameraY,
                                graphZoom,
                                x,
                                y,
                                graphHoveredNodeId,
                                graphSelectedNodeId,
                                graphHighlightedNodes
                        );
                    } finally {
                        disableScissor();
                    }
                }
            }

            if (hasCreateVisualCraftScreen) {
                ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                RenderSystem.setShaderColor(1, 1, 1, 1);
                drawTexturedModalRect(poseStack, guiLeft + 0, guiTop + 0, 0, 0, 183, 168);

                if (cancelButton.highlighted) {
                    drawTexturedModalRect(poseStack, guiLeft + 167, guiTop + 3, 186, 170, 12, 10);
                }

                drawTexturedModalRect(poseStack, this.guiLeft + 167, (int) (guiTop + 14 + (value1) * pointScroll1), 65, 170, 9, 13);

                int cols = 3;
                int slotSize = 52;
                int startX = 12;
                int startY = 15;

                List<AutoCraftVisualStep> steps = autoCraftSystemVisual.getVisualSteps();

                RenderSystem.enableBlend();

                int startIndex = value1 * 3;
                int endIndex = Math.min(startIndex + 6 * 3, steps.size());

                for (int i = startIndex; i < endIndex; i++) {
                    AutoCraftVisualStep step = steps.get(i);

                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    x = startX + col * slotSize;
                    y = startY + row * 21;

                    int x1 = 10 + col * slotSize;
                    int y2 = 15 + row * 21;
                    int y1 = startY + row * 21 + 2;

                    if (step.create() > 0 && step.need() > 0 && i != 0) {
                        ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        drawTexturedModalRect(poseStack, guiLeft + x1, guiTop + y2, 85, 170, slotSize, 20);
                    }
                    if (step.create() == 0 && step.need() > 0 && i != 0) {
                        ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        drawTexturedModalRect(poseStack, guiLeft + x1, guiTop + y2, 85, 191, slotSize, 20);
                    }
                    if (step.have() > 0 && step.create() == 0) {
                        if (i != 0 && step.have() >= step.need()) {
                            ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                            RenderSystem.setShaderColor(1, 1, 1, 1);
                            drawTexturedModalRect(poseStack, guiLeft + x1, guiTop + y2, 85, 170, slotSize, 20);
                        } else if (i != 0) {
                            ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                            RenderSystem.setShaderColor(1, 1, 1, 1);
                            drawTexturedModalRect(poseStack, guiLeft + x1, guiTop + y2, 85, 191, slotSize, 20);
                        }
                    }
                    if (step.willBeCreate() > 0 && step.create() == 0 && i != 0) {
                        ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        drawTexturedModalRect(poseStack, guiLeft + x1, guiTop + y2, 85, 170, slotSize, 20);
                    }

                    if (step.stack() instanceof ItemStack item) {
                        this.drawItem(poseStack, x, y1, item);
                    }
                    if (step.stack() instanceof SameStack item) {
                        if (item.isItem()) {
                            this.drawItem(poseStack, x, y1, item.getStack());
                        } else {
                            new FluidDefaultWidget(this, x, y1, item.getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);
                        }
                    }
                    if (step.stack() instanceof FluidStack fluidStack) {
                        new FluidDefaultWidget(this, x, y1, fluidStack).drawBackground(poseStack, guiLeft, guiTop);
                    }

                    List<String> lines = new ArrayList<>();
                    String mb = "mb";

                    if (step.stack() instanceof ItemStack) {
                        mb = "";
                    }
                    if (step.stack() instanceof SameStack stack && stack.isItem()) {
                        mb = "";
                    }

                    if (step.create() > 0 && step.need() > 0) {
                        lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(step.create()) + mb);
                        if (i != 0) {
                            lines.add(Localization.translate("iu.monitor.require") + ModUtils.getString(step.need()) + mb);
                        }
                    } else if (step.need() > 0) {
                        lines.add(Localization.translate("iu.monitor.require") + ModUtils.getString(step.need()) + mb);
                    }
                    if (step.have() > 0) {
                        lines.add(Localization.translate("iu.monitor.available") + ModUtils.getString(step.have()) + mb);
                    }
                    if (step.willBeCreate() > 0) {
                        lines.add(Localization.translate("iu.monitor.will_create") + ModUtils.getString(step.willBeCreate()) + mb);
                    }

                    float textX = guiLeft + x1 + 20f;
                    float textY = guiTop + y2 + 4 + 2 * (3 - lines.size());

                    poseStack.pose().pushPose();
                    poseStack.pose().translate(textX, textY, 40);

                    float scale = 0.42f;
                    if (!mb.isEmpty()) {
                        scale = 0.36f;
                    }
                    poseStack.pose().scale(scale, scale, 1.0f);

                    int lineHeight = 5;
                    for (String line : lines) {
                        this.drawString(poseStack, 0, 0, line, ModUtils.convertRGBAcolorToInt(225, 225, 225));
                        poseStack.pose().translate(0, lineHeight / scale, 0);
                    }

                    poseStack.pose().popPose();
                }

                RenderSystem.disableBlend();
                createButton.drawBackground(poseStack, guiLeft, guiTop);
            }
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return switch (this.container.base.sizeMode) {
            default -> ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guidefaultstoragemonitor.png");
            case 1 -> ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guibigstoragemonitor.png");
            case 2 -> ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guismallstoragemonitor.png");
        };
    }
}