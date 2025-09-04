package com.denfop.screen;

import com.denfop.Constants;
import com.denfop.api.widget.ItemStackWidget;
import com.denfop.api.widget.ItemWidget;
import com.denfop.api.widget.ScrollDirection;
import com.denfop.api.widget.TooltipWidget;
import com.denfop.containermenu.ContainerMenuVeinSensor;
import com.denfop.items.DataOres;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Vector2;
import com.denfop.world.WorldBaseGen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR;

@OnlyIn(Dist.CLIENT)
public class ScreenVeinSensor<T extends ContainerMenuVeinSensor> extends ScreenMain<ContainerMenuVeinSensor> {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/gui_scanner_ores.png".toLowerCase());
    public Map<BlockState, Integer> idToblockStateMap = new HashMap<>();
    public Map<Integer, ItemStack> ItemStackMap = new HashMap<>();
    public EditBox searchField;
    public int playerX = 0;
    public int playerY = 0;
    int[][] colors;
    boolean update = false;
    List<Integer> integerList;
    Direction direction = null;
    private double pointScroll;
    private int value;
    private int maxValue = 0;

    public ScreenVeinSensor(ContainerMenuVeinSensor container, final ItemStack itemStack1) {
        super(container);
        this.imageHeight = 178;
        this.imageWidth = 218;
        this.componentList.clear();
        colors = new int[9 * 16][9 * 16];
        final CompoundTag nbt = ModUtils.nbt(itemStack1);
        integerList = Arrays.stream(nbt.getIntArray("list"))
                .boxed()
                .collect(Collectors.toList());
        final List<Map<Vector2, DataOres>> list = new ArrayList<>(container.base.getMap().values());
        WorldBaseGen.blockStateMap.forEach((integer, blockstate) -> {
            ItemStack stack = new ItemStack(blockstate.getBlock(), 1);
            if (!idToblockStateMap.containsKey(blockstate)) {
                if (integer != null) {
                    idToblockStateMap.put(blockstate, integer);
                    ItemStackMap.put(integer, stack);
                }
            }
        });
        for (Map<Vector2, DataOres> map : list) {
            for (Map.Entry<Vector2, DataOres> entry : map.entrySet()) {

                ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1);
                Integer meta1 = null;
                if (!idToblockStateMap.containsKey(entry.getValue().getBlockState()) && WorldBaseGen.idToblockStateMap.containsKey(entry.getValue().getBlockState())) {

                } else {
                    meta1 = idToblockStateMap.get(entry.getValue().getBlockState());
                }

                if (entry.getValue().getColor() != 0xFFFFFFFF && meta1 != null && (integerList.isEmpty() || integerList.contains(meta1))) {
                    colors[entry.getKey().getX() - container.base.getVector().getX()][entry.getKey().getZ() - container.base
                            .getVector()
                            .getZ()] = entry.getValue().getColor();
                    this.addWidget(new TooltipWidget(
                            this,
                            19 + entry.getKey().getX() - container.base.getVector().getX(),
                            25 + entry.getKey().getZ() - container.base
                                    .getVector()
                                    .getZ(),
                            1,
                            1
                    ).withTooltip(() -> stack.getDisplayName().getString() + "\n" + "X: " + entry.getKey().getX() + "\n" + "Z: " + entry
                            .getKey()
                            .getZ()));
                }
            }
        }
        int posX = (int) container.player.getX();
        int posZ = (int) container.player.getZ();
        playerX = posX - container.base.getVector().getX();
        playerY = posZ - container.base
                .getVector()
                .getZ();
        direction = container.player.getMotionDirection();
        this.pointScroll = 127D / (idToblockStateMap.values().size() - 8);
        maxValue = idToblockStateMap.values().size() - 8;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.searchField.charTyped(codePoint, modifiers)) {
            value = 0;
            return true;
        }

        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == this.minecraft.options.keyInventory.getKey().getValue()) {
            this.searchField.keyPressed(keyCode, scanCode, modifiers);
            value = 0;
            return true;
        }
        if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
            value = 0;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void init() {
        super.init();
        this.searchField = new EditBox(this.font, this.leftPos + 97, this.topPos + 9, 209 - 95, 10, Component.literal(""));
        this.searchField.setMaxLength(25);
        this.searchField.setValue("");
        this.searchField.setCanLoseFocus(true);
        searchField.setBordered(false);
        this.addWidget(this.searchField);
        this.addRenderableWidget(this.searchField);
    }

    @Override
    public boolean mouseScrolled(double d, double d2, double d3) {
        super.mouseScrolled(d, d2, d3);
        int mouseX = (int) (d - this.guiLeft);
        int mouseY = (int) (d2 - this.guiTop);
        ScrollDirection direction = d3 != 0.0 ? (d3 < 0.0 ? ScrollDirection.down : ScrollDirection.up) : ScrollDirection.stopped;
        if (mouseX >= 166 && mouseX <= 209 && mouseY >= 24 && mouseY <= 169)
            if (direction != ScrollDirection.stopped) {
                if (direction == ScrollDirection.down) {
                    value++;
                    value = Math.min(value, maxValue);
                } else {
                    value--;
                    value = Math.max(0, value);

                }
            }
        return super.mouseScrolled(d, d2, d3);
    }


    @Override
    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        blit(poseStack, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());
    }

    protected void drawForegroundLayer(PoseStack poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        int i = 0;
        int j = 0;
        boolean emptySearch = searchField.getValue().isEmpty();
        String name = "[" + searchField.getValue().toLowerCase();
        if (emptySearch) {
            for (Map.Entry<Integer, ItemStack> entry : ItemStackMap.entrySet()) {

                if (i < value) {
                    i++;
                    continue;
                }

                if (i >= Math.min(
                        ItemStackMap.values().size(),
                        value + 8
                ))
                    break;
                new ItemWidget(this, 173, 26 + 18 * j, entry::getValue).withTooltip(() -> entry.getValue().getDisplayName().getString()).drawForeground(poseStack, par1, par2);
                j++;
                i++;

            }
        } else {
            for (Map.Entry<Integer, ItemStack> entry : ItemStackMap.entrySet()) {


                String builder = entry.getValue().getDisplayName().getString().toLowerCase();
                if (builder.toLowerCase().startsWith(name)) {
                    if (i < value) {
                        i++;
                        continue;
                    }
                    new ItemWidget(this, 173, 26 + 18 * j, entry::getValue).withTooltip(() -> entry.getValue().getDisplayName().getString()).drawForeground(poseStack, par1, par2);
                    j++;
                    i++;
                } else {
                    continue;
                }
                if (j >= 8)
                    break;
            }
        }
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        int ii = 0;
        int jj = 0;
        if (this.searchField.mouseClicked(i, j, k)) {
            searchField.setFocus(true);
            return;
        } else {
            searchField.setFocus(false);
        }
        boolean emptySearch = searchField.getValue().isEmpty();
        String name = "[" + searchField.getValue().toLowerCase();
        if (emptySearch) {
            for (Map.Entry<Integer, ItemStack> entry : ItemStackMap.entrySet()) {

                if (ii < value) {
                    ii++;
                    continue;
                }

                if (ii >= Math.min(
                        ItemStackMap.values().size(),
                        value + 8
                ))
                    break;
                String builder = entry.getValue().getDisplayName().getString().toLowerCase();
                if (emptySearch || builder.startsWith(name)) {
                    if (x >= 173 && x <= 173 + 18 && y >= 26 + 18 * jj && y < 26 + 18 * jj + 18) {
                        new PacketItemStackEvent(entry.getKey(), minecraft.player);
                        update = true;
                        if (integerList.contains(entry.getKey()))
                            integerList.remove(entry.getKey());
                        else
                            this.integerList.add(entry.getKey());
                    }
                    jj++;
                    ii++;
                }

            }
        } else {
            for (Map.Entry<Integer, ItemStack> entry : ItemStackMap.entrySet()) {


                String builder = entry.getValue().getDisplayName().getString().toLowerCase();
                if (builder.startsWith(name)) {
                    if (ii < value) {
                        ii++;
                        continue;
                    }
                    if (x >= 173 && x <= 173 + 18 && y >= 26 + 18 * jj && y < 26 + 18 * jj + 18) {
                        new PacketItemStackEvent(entry.getKey(), minecraft.player);
                        update = true;
                        if (integerList.contains(entry.getKey()))
                            integerList.remove(entry.getKey());
                        else
                            this.integerList.add(entry.getKey());
                    }
                    jj++;
                    ii++;
                } else {
                    continue;
                }
                if (jj >= 8)
                    break;
            }
        }
    }

    @Override
    public void render(@NotNull PoseStack graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        if (update) {
            update = false;
            this.elements.clear();
            final List<Map<Vector2, DataOres>> list = new ArrayList<>(container.base.getMap().values());
            for (Map<Vector2, DataOres> map : list) {
                for (Map.Entry<Vector2, DataOres> entry : map.entrySet()) {

                    ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1);
                    Integer meta1 = null;
                    if (!idToblockStateMap.containsKey(entry.getValue().getBlockState()) && WorldBaseGen.idToblockStateMap.containsKey(entry.getValue().getBlockState().getBlock())) {
                        Integer meta = WorldBaseGen.idToblockStateMap.get(entry.getValue().getBlockState().getBlock());
                        if (meta != null) {
                            idToblockStateMap.put(entry.getValue().getBlockState(), meta);
                            ItemStackMap.put(meta, stack);
                            meta1 = meta;
                        }
                    } else {
                        meta1 = idToblockStateMap.get(entry.getValue().getBlockState());
                    }
                    if (entry.getValue().getColor() != 0xFFFFFFFF && meta1 != null && (integerList.isEmpty() || integerList.contains(meta1))) {
                        colors[entry.getKey().getX() - container.base.getVector().getX()][entry.getKey().getZ() - container.base
                                .getVector()
                                .getZ()] = entry.getValue().getColor();
                        this.addWidget(new TooltipWidget(
                                this,
                                19 + entry.getKey().getX() - container.base.getVector().getX(),
                                25 + entry.getKey().getZ() - container.base
                                        .getVector()
                                        .getZ(),
                                1,
                                1
                        ).withTooltip(() -> stack.getDisplayName().getString() + "\n" + "X: " + entry.getKey().getX() + "\n" + "Z: " + entry
                                .getKey()
                                .getZ()));
                    } else {
                        colors[entry.getKey().getX() - container.base.getVector().getX()][entry.getKey().getZ() - container.base
                                .getVector()
                                .getZ()] = Blocks.STONE.defaultBlockState().getMapColor(
                                container.base.player.getLevel(),
                                BlockPos.ZERO
                        ).col | -16777216;
                    }
                }
            }
            this.pointScroll = 127D / (idToblockStateMap.values().size() - 8);
            maxValue = idToblockStateMap.values().size() - 8;

        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(PoseStack poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        drawTexturedModalRect(poseStack, this.guiLeft + 197, (int) (guiTop + 25 + value * pointScroll), 242, 20, 254 - 241, 36 - 19);
        PoseStack pose = poseStack;
        pose.pushPose();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, POSITION_COLOR);
        boolean empty = true;
        for (int i = 0; i < 9 * 16; i++) {
            for (int j = 0; j < 9 * 16; j++) {
                int color = colors[i][j];
                if (color != 0) {
                    this.drawColoredRect(poseStack, 19 + i,
                            25 + j, 1, 1,
                            colors[i][j], bufferBuilder
                    );
                    empty = false;
                } else {
                    colors[i][j] = Blocks.STONE.defaultBlockState().getMapColor(
                            container.base.player.getLevel(),
                            BlockPos.ZERO
                    ).col | -16777216;
                }
            }
        }
        pose.popPose();
        BufferUploader.drawWithShader(bufferBuilder.end());

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        pose = poseStack;
        Matrix4f martix;
        pose.pushPose();
        int chunkSize = 16;
        int numChunks = 9;

        for (int x = 0; x < numChunks; x++) {
            for (int y = 0; y < numChunks; y++) {
                int offsetX = 19 + (x * chunkSize);
                int offsetY = 25 + (y * chunkSize);

                martix = pose.last().pose();
                buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, POSITION_COLOR);
                buffer.vertex(martix, this.guiLeft + offsetX, this.guiTop + offsetY, 0)
                        .color(0, 255 / 255f, 204 / 255f, 1.0F)
                        .endVertex();
                buffer.vertex(martix, this.guiLeft + offsetX + chunkSize, this.guiTop + offsetY, 0)
                        .color(0, 255 / 255f, 204 / 255f, 1.0F)
                        .endVertex();
                buffer.vertex(martix, this.guiLeft + offsetX + chunkSize, this.guiTop + offsetY + chunkSize, 0)
                        .color(0, 255 / 255f, 204 / 255f, 1.0F)
                        .endVertex();
                buffer.vertex(martix, this.guiLeft + offsetX, this.guiTop + offsetY + chunkSize, 0)
                        .color(0, 255 / 255f, 204 / 255f, 1.0F)
                        .endVertex();
                tessellator.end();
                buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, POSITION_COLOR);
                buffer.vertex(martix, this.guiLeft + offsetX, this.guiTop + offsetY, 0)
                        .color(0, 255 / 255f, 204 / 255f, 1.0F)
                        .endVertex();
                buffer.vertex(martix, this.guiLeft + offsetX, this.guiTop + offsetY + chunkSize, 0)
                        .color(0, 255 / 255f, 204 / 255f, 1.0F)
                        .endVertex();
                tessellator.end();
            }
        }

        pose.popPose();
        int i = 0;
        int j = 0;
        boolean emptySearch = searchField.getValue().isEmpty();
        String name = "[" + searchField.getValue().toLowerCase();
        if (emptySearch) {
            for (Map.Entry<Integer, ItemStack> entry : ItemStackMap.entrySet()) {

                if (i < value) {
                    i++;
                    continue;
                }

                if (i >= Math.min(
                        ItemStackMap.values().size(),
                        value + 8
                ))
                    break;
                String builder = entry.getValue().getDisplayName().getString().toLowerCase();
                if (integerList.contains(entry.getKey())) {
                    bindTexture();
                    drawTexturedModalRect(poseStack, this.guiLeft + 172, (int) (guiTop + 25 + 18 * j), 237, 1, 18, 18);

                }
                new ItemStackWidget(this, 173, 26 + 18 * j, entry::getValue).drawBackground(poseStack, this.guiLeft, guiTop);
                j++;
                i++;

            }
        } else {
            for (Map.Entry<Integer, ItemStack> entry : ItemStackMap.entrySet()) {


                String builder = entry.getValue().getDisplayName().getString().toLowerCase();
                if (builder.startsWith(name)) {
                    if (i < value) {
                        i++;
                        continue;
                    }
                    if (integerList.contains(entry.getKey())) {
                        bindTexture();
                        drawTexturedModalRect(poseStack, this.guiLeft + 172, (int) (guiTop + 25 + 18 * j), 237, 1, 18, 18);

                    }
                    new ItemStackWidget(this, 173, 26 + 18 * j, entry::getValue).drawBackground(poseStack, this.guiLeft, guiTop);
                    j++;
                    i++;
                } else {
                    continue;
                }
                if (i < value) {
                    i++;
                    continue;
                }

                if (j >= 8)
                    break;
            }
        }
        pose.pushPose();
        pose.translate(guiLeft + 19 + playerX - 0.5, guiTop + 25 + playerY - 0.5, 20);
        switch (direction.getOpposite()) {
            case NORTH -> pose.mulPose(Vector3f.ZP.rotationDegrees(180));
            case SOUTH -> pose.mulPose(Vector3f.ZP.rotationDegrees(0));
            case WEST -> pose.mulPose(Vector3f.ZP.rotationDegrees(90));
            case EAST -> pose.mulPose(Vector3f.ZP.rotationDegrees(-90));
            case UP -> pose.mulPose(Vector3f.XP.rotationDegrees(-90));
            case DOWN -> pose.mulPose(Vector3f.XP.rotationDegrees(90));
        }
        ;

        pose.scale(0.3f, 0.3f, 1f);
        float texW = 42 - 29;
        float texH = 142 - 120;
        switch (direction.getOpposite()) {
            case NORTH -> pose.translate(-texW, -texH, 0);
            case SOUTH -> pose.translate(0, -texH, 0);
            case WEST -> pose.translate(-texW, -texH, 0);
            case EAST -> pose.translate(-texW / 2, -texH / 2, 0);
        }
        ;
        bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_space_icons.png"));
        drawTexturedModalRect(poseStack, 0, 0, 29, 120, 42 - 29, 142 - 120);
        pose.popPose();
    }


    protected ResourceLocation getTexture() {
        return background;
    }

}
