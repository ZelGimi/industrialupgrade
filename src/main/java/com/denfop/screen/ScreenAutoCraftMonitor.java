package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.storage.autocrafting.*;
import com.denfop.api.widget.FluidDefaultWidget;
import com.denfop.api.widget.ScrollDirection;
import com.denfop.api.widget.StorageButtonWidget;
import com.denfop.containermenu.ContainerAutoCraftMonitor;
import com.denfop.network.packet.PacketUpdateServerTile;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.Slot;

import java.util.*;

public class ScreenAutoCraftMonitor<T extends ContainerAutoCraftMonitor> extends ScreenMain<ContainerAutoCraftMonitor> {

    private final StorageButtonWidget cancelButton;
    Map<String, Map<SameStack, Integer>> fluids = new HashMap<>();
    Map<String, Map<SameStack, Integer>> items = new HashMap<>();
    Map<String, Map<SameStack, Integer>> fluidsCrafts = new HashMap<>();
    Map<String, Map<SameStack, Integer>> itemsCrafts = new HashMap<>();
    int hover = -1;
    List<AutoCraftStorage> leftovers = new ArrayList<>();
    private double pointScroll1;
    private double pointScroll;
    private int value;
    private int maxValue = 0;
    private int value1;
    private int maxValue1 = 0;
    private boolean leftScrollbarDragging = false;
    private boolean rightScrollbarDragging = false;
    private int cachedOtherSize = 0;
    private int lastAutoStacksSize = -1;
    private int lastIndex = Integer.MIN_VALUE;

    public ScreenAutoCraftMonitor(ContainerAutoCraftMonitor guiContainer) {
        super(guiContainer);
        this.imageWidth = 255;
        this.componentList.clear();
        this.imageHeight = 169;

        this.addComponent(cancelButton = new StorageButtonWidget(
                this, 199, 144, 175 - 126, 20, guiContainer.base, 0, Localization.translate("iu.autocraft.cancel")) {
            @Override
            public boolean visible() {
                return container.base.index != -1;
            }

            @Override
            public void buttonClicked(int mouseX, int mouseY) {
                if (this.visible() && this.contains(mouseX, mouseY) && container.base.index != -1) {
                    this.getGui().getMinecraft().getSoundManager()
                            .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    new PacketUpdateServerTile(container.base, (container.base.index + 1) * -1);
                }
            }

            @Override
            protected List<String> getToolTip() {
                return visible() ? Collections.singletonList(getText()) : Collections.emptyList();
            }
        });
    }

    private int getLeftScrollbarX() {
        return 65;
    }

    private int getLeftScrollbarY() {
        return 13;
    }

    private int getLeftScrollbarWidth() {
        return 9;
    }

    private int getLeftScrollbarHeight() {
        return 13;
    }

    private int getLeftScrollbarTravel() {
        return 130;
    }

    private boolean isInsideLeftScrollbar(double mouseX, double mouseY) {
        int x = getLeftScrollbarX();
        int y = getLeftScrollbarY();
        int w = getLeftScrollbarWidth();
        int h = getLeftScrollbarHeight();
        int travel = getLeftScrollbarTravel();

        return mouseX >= x && mouseX <= x + w
                && mouseY >= y && mouseY <= y + travel + h;
    }

    private void updateLeftScrollFromMouse(double relativeMouseY) {
        if (this.maxValue <= 0) {
            this.value = 0;
            return;
        }

        double top = getLeftScrollbarY();
        double travel = getLeftScrollbarTravel();
        double thumbHalf = getLeftScrollbarHeight() / 2.0D;

        double thumbTop = Mth.clamp(relativeMouseY - top - thumbHalf, 0.0D, travel);
        double progress = thumbTop / travel;

        this.value = Mth.clamp((int) Math.round(progress * this.maxValue), 0, this.maxValue);
    }

    private int getRightScrollbarX() {
        return 240;
    }

    private int getRightScrollbarY() {
        return 14;
    }

    private int getRightScrollbarWidth() {
        return 9;
    }

    private int getRightScrollbarHeight() {
        return 13;
    }

    private int getRightScrollbarTravel() {
        return 116;
    }

    private boolean isInsideRightScrollbar(double mouseX, double mouseY) {
        int x = getRightScrollbarX();
        int y = getRightScrollbarY();
        int w = getRightScrollbarWidth();
        int h = getRightScrollbarHeight();
        int travel = getRightScrollbarTravel();

        return mouseX >= x && mouseX <= x + w
                && mouseY >= y && mouseY <= y + travel + h;
    }

    private void updateRightScrollFromMouse(double relativeMouseY) {
        if (this.maxValue1 <= 0) {
            this.value1 = 0;
            return;
        }

        double top = getRightScrollbarY();
        double travel = getRightScrollbarTravel();
        double thumbHalf = getRightScrollbarHeight() / 2.0D;

        double thumbTop = Mth.clamp(relativeMouseY - top - thumbHalf, 0.0D, travel);
        double progress = thumbTop / travel;

        this.value1 = Mth.clamp((int) Math.round(progress * this.maxValue1), 0, this.maxValue1);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public boolean canRenderSlot(Slot slot) {
        return true;
    }

    private List<DisplayCraftOutput> getDisplayCraftOutputs() {
        Map<Integer, List<AutoCraftOutput>> source = this.container.base.getCraftOutputs();
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, List<AutoCraftOutput>> map = new HashMap<>();
        for (Map.Entry<Integer, List<AutoCraftOutput>> entry : source.entrySet()) {
            List<AutoCraftOutput> value = entry.getValue();
            map.put(entry.getKey(), value == null ? Collections.emptyList() : new ArrayList<>(value));
        }

        List<DisplayCraftOutput> result = new ArrayList<>();
        List<Integer> processorIds = new ArrayList<>(map.keySet());
        processorIds.sort(Integer::compareTo);

        for (Integer processorId : processorIds) {
            List<AutoCraftOutput> outputs = map.get(processorId);
            if (outputs == null || outputs.isEmpty()) {
                continue;
            }

            List<AutoCraftOutput> sortedOutputs = new ArrayList<>(outputs);
            sortedOutputs.sort(Comparator.comparingInt(AutoCraftOutput::getIndex));

            for (AutoCraftOutput output : sortedOutputs) {
                if (output != null) {
                    result.add(new DisplayCraftOutput(processorId, output));
                }
            }
        }

        return result;
    }

    @Override
    public void updateTickInterface() {
        super.updateTickInterface();

        if (container.base.index != -1 && container.base.autoCraftSystem != null) {
            recomputeOtherIfNeeded();
            maxValue1 = ((container.base.autoCraftSystem.getAutoStacks().size() + cachedOtherSize + 1) / 3) - 5;
        } else {
            maxValue1 = 0;
        }

        if (maxValue1 < 0) {
            maxValue1 = 0;
        }
        if (value1 > maxValue1) {
            value1 = maxValue1;
        }

        if (maxValue1 != 0) {
            this.pointScroll1 = 116D / maxValue1;
        } else {
            pointScroll1 = 0;
        }

        List<DisplayCraftOutput> outputs = getDisplayCraftOutputs();
        maxValue = outputs.size() - 6;
        if (maxValue < 0) {
            maxValue = 0;
        }
        if (value > maxValue) {
            value = maxValue;
        }

        if (maxValue != 0) {
            pointScroll = 130D / maxValue;
        } else {
            pointScroll = 0;
        }
    }

    private void recomputeOtherIfNeeded() {
        if (container.base.autoCraftSystem == null) {
            cachedOtherSize = 0;
            leftovers.clear();
            return;
        }

        int idx = container.base.index;
        int sz = container.base.autoCraftSystem.getAutoStacks().size();

        if (idx != lastIndex || sz != lastAutoStacksSize) {
            lastIndex = idx;
            lastAutoStacksSize = sz;
            cachedOtherSize = getSizeOtherItem();
        }
    }

    private Map<String, Map<SameStack, Integer>> copy(Map<String, Map<SameStack, Integer>> original) {
        Map<String, Map<SameStack, Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Map<SameStack, Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashMap<>(entry.getValue()));
        }
        return copy;
    }

    private int getSizeOtherItem() {
        fluidsCrafts.clear();
        itemsCrafts.clear();
        leftovers.clear();

        this.fluids = copy(container.base.autoCraftSystem.getReservedFluidSlots());
        this.items = copy(container.base.autoCraftSystem.getReservedItemSlots());

        for (AutoCraftStack stack : new ArrayList<>(container.base.autoCraftSystem.getAutoStacks())) {
            SameStack sameStack = stack.getSameStack();
            String key = sameStack.getKey();

            if (sameStack.isFluid()) {
                Map<SameStack, Integer> map = fluids.get(key);
                if (map != null) {
                    int count = map.getOrDefault(sameStack, 0);
                    if (count != 0) {
                        fluidsCrafts.computeIfAbsent(key, k -> new HashMap<>()).merge(sameStack, count, Integer::sum);
                    }
                }
            } else {
                Map<SameStack, Integer> map = items.get(key);
                if (map != null) {
                    int count = map.getOrDefault(sameStack, 0);
                    if (count != 0) {
                        itemsCrafts.computeIfAbsent(key, k -> new HashMap<>()).merge(sameStack, count, Integer::sum);
                    }
                }
            }
        }

        subtractNested(fluids, fluidsCrafts);
        subtractNested(items, itemsCrafts);
        leftovers = buildLeftovers(items, fluids);

        return leftovers.size();
    }

    public List<AutoCraftStorage> buildLeftovers(
            Map<String, Map<SameStack, Integer>> items,
            Map<String, Map<SameStack, Integer>> fluids
    ) {
        Map<SameStack, Integer> sumByStack = new HashMap<>();

        addNestedToSum(sumByStack, items);
        addNestedToSum(sumByStack, fluids);

        List<AutoCraftStorage> result = new ArrayList<>(sumByStack.size());
        for (Map.Entry<SameStack, Integer> e : sumByStack.entrySet()) {
            int amount = e.getValue() == null ? 0 : e.getValue();
            if (amount > 0) {
                result.add(new AutoCraftStorage(e.getKey(), amount));
            }
        }

        result.sort(Comparator.comparing(a -> a.stack().getKey()));
        return result;
    }

    private void addNestedToSum(Map<SameStack, Integer> target, Map<String, Map<SameStack, Integer>> nested) {
        if (nested == null || nested.isEmpty()) {
            return;
        }

        for (Map<SameStack, Integer> inner : nested.values()) {
            if (inner == null || inner.isEmpty()) {
                continue;
            }

            for (Map.Entry<SameStack, Integer> e : inner.entrySet()) {
                SameStack stack = e.getKey();
                if (stack == null) {
                    continue;
                }

                int amount = e.getValue() == null ? 0 : e.getValue();
                if (amount <= 0) {
                    continue;
                }

                target.merge(stack, amount, Integer::sum);
            }
        }
    }

    private void subtractNested(Map<String, Map<SameStack, Integer>> base, Map<String, Map<SameStack, Integer>> toRemove) {
        if (base == null || base.isEmpty() || toRemove == null || toRemove.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Map<SameStack, Integer>> e : toRemove.entrySet()) {
            String key = e.getKey();
            Map<SameStack, Integer> removeInner = e.getValue();
            if (removeInner == null || removeInner.isEmpty()) {
                continue;
            }

            Map<SameStack, Integer> baseInner = base.get(key);
            if (baseInner == null || baseInner.isEmpty()) {
                continue;
            }

            for (SameStack ss : removeInner.keySet()) {
                baseInner.remove(ss);
            }

            if (baseInner.isEmpty()) {
                base.remove(key);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d4, double d3) {
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);

        if (this.container.base.index != -1L) {
            ScrollDirection direction = d3 != 0.0
                    ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up)
                    : ScrollDirection.stopped;

            if (mouseX >= 82 && mouseX <= 247 && mouseY >= 15 && mouseY <= 141) {
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

        ScrollDirection direction = d3 != 0.0
                ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up)
                : ScrollDirection.stopped;

        if (mouseX >= 7 && mouseX <= 72 && mouseY >= 15 && mouseY <= 152) {
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

        return false;
    }

    @Override
    protected void drawBackgroundAndTitle(GuiGraphics poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        poseStack.blit(currentTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
    }

    @Override
    protected void drawForegroundLayer(GuiGraphics poseStack, final int par1, final int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);

        List<DisplayCraftOutput> outputs = getDisplayCraftOutputs();
        if (!this.container.base.hasSystem || outputs.isEmpty()) {
            return;
        }

        int startIndex = value;
        int endIndex = Math.min(startIndex + 6, outputs.size());

        for (int i = startIndex; i < endIndex; i++) {
            DisplayCraftOutput display = outputs.get(i);
            AutoCraftOutput step = display.output();
            if (step == null) {
                continue;
            }

            List<String> lines = new LinkedList<>();
            String mb = step.getSameStack().isItem() ? "" : "mb";

            lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(step.getAll()) + mb);
            lines.add(Localization.translate("iu.monitor.require") + ModUtils.getString(step.getAmount() * step.getSameStack().getAmount()) + mb);
            lines.add(Localization.translate("iu.autocraft.processor") + display.processorId());

            SameStack item = step.getSameStack();

            int x = 8;
            int y = 16 + (i - startIndex) * 23;
            boolean inX = par1 >= x && par1 < x + 54;
            boolean inY = par2 >= y && par2 < y + 21;

            if (inX && inY) {
                if (item.isItem()) {
                    this.drawTooltipOnlyName(poseStack, par1, par2, item.getStack(), lines);
                }
                if (item.isFluid()) {
                    this.drawTooltipOnlyName(poseStack, par1, par2, item.getFluidStack(), lines);
                }
            }
        }

        if (container.base.index != -1 && container.base.autoCraftSystem != null) {
            int cols = 3;
            int slotSize = 52;
            int startX = 84;
            int startY = 17;

            AutoCraftSystem system = container.base.autoCraftSystem;
            List<Object> steps = new LinkedList<>();
            steps.add(system.getAutoCraftOutput());
            steps.addAll(system.getAutoStacks());
            steps.addAll(this.leftovers);
            steps = new ArrayList<>(steps);

            RenderSystem.enableBlend();

            startIndex = value1 * 3;
            endIndex = Math.min(startIndex + 6 * 3, steps.size());

            for (int i = startIndex; i < endIndex; i++) {
                if (steps.get(i) instanceof AutoCraftOutput step) {
                    List<String> lines = new LinkedList<>();
                    String mb = step.getSameStack().isItem() ? "" : "mb";

                    lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(step.getAll()) + mb);
                    lines.add(Localization.translate("iu.monitor.require") + ModUtils.getString(step.getAmount() * step.getSameStack().getAmount()) + mb);

                    SameStack item = step.getSameStack();

                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    int x = startX + col * slotSize;
                    int y = startY + row * 21;
                    boolean inX = par1 >= x && par1 < x + slotSize;
                    boolean inY = par2 >= y && par2 < y + 21;

                    if (inX && inY) {
                        if (item.isItem()) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item.getStack(), lines);
                        }
                        if (item.isFluid()) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item.getFluidStack(), lines);
                        }
                    }

                } else if (steps.get(i) instanceof AutoCraftStack step) {
                    List<String> lines = new LinkedList<>();
                    String mb = "mb";
                    Map<SameStack, Integer> map;

                    if (step.getSameStack().isItem()) {
                        map = this.itemsCrafts.get(step.getSameStack().getKey());
                        mb = "";
                    } else {
                        map = this.fluidsCrafts.get(step.getSameStack().getKey());
                    }

                    SameStack item = step.getSameStack();
                    lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(step.getAll()) + mb);
                    lines.add(Localization.translate("iu.monitor.will_create") + ModUtils.getString(step.getCreate()) + mb);

                    if (map != null) {
                        int amount = map.getOrDefault(item, 0);
                        if (amount != 0) {
                            lines.add(Localization.translate("iu.monitor.available") + ModUtils.getString(amount) + mb);
                        }
                    }

                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    int x = startX + col * slotSize;
                    int y = startY + row * 21;
                    boolean inX = par1 >= x && par1 < x + slotSize;
                    boolean inY = par2 >= y && par2 < y + 21;

                    if (inX && inY) {
                        if (item.isItem()) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item.getStack(), lines);
                        }
                        if (item.isFluid()) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item.getFluidStack(), lines);
                        }
                    }

                } else if (steps.get(i) instanceof AutoCraftStorage step) {
                    List<String> lines = new LinkedList<>();
                    String mb = step.getSameStack().isItem() ? "" : "mb";

                    SameStack item = step.getSameStack();
                    lines.add(Localization.translate("iu.monitor.available") + ModUtils.getString(step.getAmount()) + mb);

                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    int x = startX + col * slotSize;
                    int y = startY + row * 21;
                    boolean inX = par1 >= x && par1 < x + slotSize;
                    boolean inY = par2 >= y && par2 < y + 21;

                    if (inX && inY) {
                        if (item.isItem()) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item.getStack(), lines);
                        }
                        if (item.isFluid()) {
                            this.drawTooltipOnlyName(poseStack, par1, par2, item.getFluidStack(), lines);
                        }
                    }
                }
            }

            RenderSystem.disableBlend();
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            if (this.leftScrollbarDragging) {
                this.updateLeftScrollFromMouse(mouseY - this.topPos);
                return true;
            }

            if (this.rightScrollbarDragging && this.container.base.index != -1) {
                this.updateRightScrollFromMouse(mouseY - this.topPos);
                return true;
            }
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean handled = false;

        if (button == 0) {
            if (this.leftScrollbarDragging) {
                this.leftScrollbarDragging = false;
                handled = true;
            }
            if (this.rightScrollbarDragging) {
                this.rightScrollbarDragging = false;
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

        if (k == 0) {
            if (isInsideLeftScrollbar(x, y)) {
                this.leftScrollbarDragging = true;
                this.rightScrollbarDragging = false;
                this.updateLeftScrollFromMouse(y);
                return;
            }

            if (this.container.base.index != -1 && isInsideRightScrollbar(x, y)) {
                this.rightScrollbarDragging = true;
                this.leftScrollbarDragging = false;
                this.updateRightScrollFromMouse(y);
                return;
            }
        }

        List<DisplayCraftOutput> outputs = getDisplayCraftOutputs();
        if (outputs.isEmpty()) {
            return;
        }

        int startIndex = value;
        int endIndex = Math.min(startIndex + 6, outputs.size());

        for (int ii = startIndex; ii < endIndex; ii++) {
            int x1 = 8;
            int y1 = 16 + (ii - startIndex) * 23;
            boolean inX = x >= x1 && x < x1 + 54;
            boolean inY = y >= y1 && y < y1 + 21;

            if (inX && inY) {
                DisplayCraftOutput selected = outputs.get(ii);

                new PacketUpdateServerTile(this.container.base, selected.processorId() + 8);
                new PacketUpdateServerTile(this.container.base, selected.output().getIndex());
                return;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, float f, int x, int y) {
        super.drawGuiContainerBackgroundLayer(poseStack, f, x, y);

        List<DisplayCraftOutput> outputs = getDisplayCraftOutputs();
        if (!this.container.base.hasSystem || outputs.isEmpty()) {
            return;
        }

        drawTexturedModalRect(poseStack, guiLeft + 65, (int) (guiTop + 13 + (value * pointScroll)), 65, 170, 9, 13);
        drawTexturedModalRect(poseStack, guiLeft + 240, (int) (guiTop + 14 + (value1 * pointScroll1)), 65, 170, 9, 13);

        int startIndex = value;
        int endIndex = Math.min(startIndex + 6, outputs.size());

        for (int i = startIndex; i < endIndex; i++) {
            DisplayCraftOutput display = outputs.get(i);
            AutoCraftOutput autoCraftOutput = display.output();

            if (autoCraftOutput == null) {
                continue;
            }

            x = 10;
            y = 18 + (i - startIndex) * 23;

            drawTexturedModalRect(poseStack, guiLeft + 8, guiTop + 16 + (i - startIndex) * 23, 9, 170, 55, 23);

            if (autoCraftOutput.getIndex() == this.container.base.index
                    && display.processorId() == this.container.base.processorId) {
                drawTexturedModalRect(poseStack, guiLeft + 8, guiTop + 16 + (i - startIndex) * 23, 9, 194, 55, 23);
            }

            SameStack stack = autoCraftOutput.getSameStack();

            if (stack.isItem()) {
                this.drawItem(poseStack, x, y, stack.getStack());
            }
            if (stack.isFluid()) {
                new FluidDefaultWidget(this, x, y, stack.getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);
            }

            List<String> lines = new ArrayList<>();
            String mb = stack.isItem() ? "" : "mb";

            lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(autoCraftOutput.getAll()) + mb);
            lines.add(Localization.translate("iu.monitor.require") + ModUtils.getString(autoCraftOutput.getAmount() * stack.getAmount()) + mb);

            float textX = guiLeft + 10 + 18;
            float textY = guiTop + y + 2 + 2 * (3 - lines.size());

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

        if (this.container.base.index != -1 && container.base.autoCraftSystem != null) {
            RenderSystem.setShaderColor(1, 1, 1, 1);

            int cols = 3;
            int slotSize = 52;
            int startX = 84;
            int startY = 17;

            AutoCraftSystem system = container.base.autoCraftSystem;
            List<Object> steps = new LinkedList<>();
            steps.add(system.getAutoCraftOutput());
            steps.addAll(system.getAutoStacks());
            steps.addAll(this.leftovers);
            steps = new ArrayList<>(steps);

            RenderSystem.enableBlend();

            startIndex = value1 * 3;
            endIndex = Math.min(startIndex + 6 * 3, steps.size());

            for (int i = startIndex; i < endIndex; i++) {
                if (steps.get(i) instanceof AutoCraftOutput step) {
                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    x = startX + col * slotSize;
                    y = startY + row * 21;

                    int x1 = 84 + col * slotSize;
                    int y2 = 17 + row * 21;
                    int y1 = startY + row * 21 + 2;

                    if (step.getCreate() > 0) {
                        ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        drawTexturedModalRect(poseStack, guiLeft + x1 - 1, guiTop + y2 - 1, 85, 170, slotSize, 20);
                    }

                    if (step.getSameStack().isItem()) {
                        this.drawItem(poseStack, x, y1, step.getSameStack().getStack());
                    }
                    if (step.getSameStack().isFluid()) {
                        new FluidDefaultWidget(this, x, y1, step.getSameStack().getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);
                    }

                    List<String> lines = new ArrayList<>();
                    String mb = step.getSameStack().isItem() ? "" : "mb";

                    lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(step.getAll()) + mb);
                    lines.add(Localization.translate("iu.monitor.require") + ModUtils.getString(step.getAmount() * step.getSameStack().getAmount()) + mb);

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

                } else if (steps.get(i) instanceof AutoCraftStack step) {
                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    x = startX + col * slotSize;
                    y = startY + row * 21;

                    int x1 = 84 + col * slotSize;
                    int y2 = 17 + row * 21;
                    int y1 = startY + row * 21 + 2;

                    if (step.getCreate() > 0) {
                        ScreenIndustrialUpgrade.bindTexture(ResourceLocation.tryBuild("industrialupgrade", "textures/gui/guicreatecraft.png"));
                        RenderSystem.setShaderColor(1, 1, 1, 1);
                        drawTexturedModalRect(poseStack, guiLeft + x1 - 1, guiTop + y2 - 1, 85, 170, slotSize, 20);
                    }

                    if (step.getSameStack().isItem()) {
                        this.drawItem(poseStack, x, y1, step.getSameStack().getStack());
                    }
                    if (step.getSameStack().isFluid()) {
                        new FluidDefaultWidget(this, x, y1, step.getSameStack().getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);
                    }

                    List<String> lines = new ArrayList<>();
                    String mb = "mb";
                    Map<SameStack, Integer> map;

                    if (step.getSameStack().isItem()) {
                        map = this.itemsCrafts.get(step.getSameStack().getKey());
                        mb = "";
                    } else {
                        map = this.fluidsCrafts.get(step.getSameStack().getKey());
                    }

                    SameStack item = step.getSameStack();
                    lines.add(Localization.translate("iu.monitor.create") + ModUtils.getString(step.getAll()) + mb);
                    lines.add(Localization.translate("iu.monitor.will_create") + ModUtils.getString(step.getCreate()) + mb);

                    if (map != null) {
                        int amount = map.getOrDefault(item, 0);
                        if (amount != 0) {
                            lines.add(Localization.translate("iu.monitor.available") + ModUtils.getString(amount) + mb);
                        }
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

                } else if (steps.get(i) instanceof AutoCraftStorage step) {
                    int col = (i - startIndex) % cols;
                    int row = (i - startIndex) / cols;

                    x = startX + col * slotSize;
                    y = startY + row * 21;

                    int x1 = 84 + col * slotSize;
                    int y2 = 17 + row * 21;
                    int y1 = startY + row * 21 + 2;

                    if (step.getSameStack().isItem()) {
                        this.drawItem(poseStack, x, y1, step.getSameStack().getStack());
                    }
                    if (step.getSameStack().isFluid()) {
                        new FluidDefaultWidget(this, x, y1, step.getSameStack().getFluidStack()).drawBackground(poseStack, guiLeft, guiTop);
                    }

                    List<String> lines = new ArrayList<>();
                    String mb = step.getSameStack().isItem() ? "" : "mb";
                    lines.add(Localization.translate("iu.monitor.available") + ModUtils.getString(step.getAmount()) + mb);

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
            }

            RenderSystem.disableBlend();
        }
    }

    @Override
    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guiallprocesscrafts.png");
    }

    private record DisplayCraftOutput(int processorId, AutoCraftOutput output) {
    }
}