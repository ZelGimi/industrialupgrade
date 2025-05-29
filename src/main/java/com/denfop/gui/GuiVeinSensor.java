package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerVeinSensor;
import com.denfop.items.DataOres;
import com.denfop.network.packet.PacketItemStackEvent;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Vector2;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.ChanceOre;
import com.denfop.world.vein.VeinType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.*;
import java.util.stream.Collectors;

import static com.denfop.items.ItemVeinSensor.getDataChunk;
import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR;

@OnlyIn(Dist.CLIENT)
public class GuiVeinSensor<T extends ContainerVeinSensor> extends GuiIU<ContainerVeinSensor> implements GuiPageButtonList.GuiResponder,
        GuiVerticalSliderList.FormatHelper {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png".toLowerCase());
    private final String name;
    int[][] colors;
    List<ItemStack> itemStackList;
    boolean update = false;
    List<Integer> integerList = new ArrayList<>();
    int tick = 0;
    private GuiVerticalSliderList slider;
    private int value;

    public GuiVeinSensor(ContainerVeinSensor container, final ItemStack itemStack1) {
        super(container);

        this.name = Localization.translate(itemStack1.getDescriptionId());
        this.imageHeight = 242;
        this.imageWidth += 50;
        this.componentList.clear();
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );
        colors = new int[9 * 16][9 * 16];
        final CompoundTag nbt = ModUtils.nbt(itemStack1);
        integerList = Arrays.stream(nbt.getIntArray("list"))
                .boxed()
                .collect(Collectors.toList());
        itemStackList = new LinkedList<>();
        final List<VeinType> veins = WorldBaseGen.veinTypes;
        for (VeinType veinType : veins) {
            if (veinType.getHeavyOre() != null) {
                itemStackList.add(new ItemStack(veinType.getHeavyOre().getBlock(), 1));
            }
            for (ChanceOre chanceOre : veinType.getOres()) {
                boolean find = false;
                ItemStack stack1 = new ItemStack(chanceOre.getBlock().getBlock(), 1);
                for (ItemStack stack : itemStackList) {
                    if (stack.is(stack1.getItem())) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    itemStackList.add(stack1);
                }
            }
        }
        final List<Map<Vector2, DataOres>> list = new ArrayList<>(container.base.getMap().values());
        for (Map<Vector2, DataOres> map : list) {
            for (Map.Entry<Vector2, DataOres> entry : map.entrySet()) {
                colors[entry.getKey().getX() - container.base.getVector().getX()][entry.getKey().getZ() - container.base
                        .getVector()
                        .getZ()] = entry.getValue().getColor();
                ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1);
                if (!integerList.isEmpty()) {
                    boolean find = false;
                    for (Integer integer : integerList) {
                        if (itemStackList.get(integer).is(stack.getItem())) {
                            find = true;
                            this.addElement(new Area(
                                    this,
                                    20 + entry.getKey().getX() - container.base.getVector().getX(),
                                    10 + entry.getKey().getZ() - container.base
                                            .getVector()
                                            .getZ(),
                                    1,
                                    1
                            ).withTooltip(() -> stack.getDisplayName().getString() + "\n" + "X: " + entry
                                    .getKey()
                                    .getX() + "\n" + "Z: " + entry
                                    .getKey()
                                    .getZ()));
                            break;
                        }
                    }
                    if (!find) {
                        colors[entry.getKey().getX() - container.base.getVector().getX()][entry
                                .getKey()
                                .getZ() - container.base
                                .getVector()
                                .getZ()] = MapColor.STONE.col | -16777216;
                    }
                } else if (entry.getValue().getColor() != 0xFFFFFFFF) {
                    this.addElement(new Area(
                            this,
                            20 + entry.getKey().getX() - container.base.getVector().getX(),
                            10 + entry.getKey().getZ() - container.base
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
        componentList.add(slots);
        this.addElement(new ImageInterface(this, 0, 0, imageWidth, imageHeight));


    }

    @Override
    public String getText(final int var1, final String var2, final float var3) {
        return "";
    }

    @Override
    public void setEntryValue(final int i, final boolean b) {

    }

    @Override
    public void setEntryValue(final int i, final float v) {
        value = (int) v;

    }

    @Override
    public void setEntryValue(final int i, final String s) {

    }

    public void init() {
        super.init();


        slider = new GuiVerticalSliderList(this, 2, (this.width - this.imageWidth) / 2 + 201,
                (this.height - this.imageHeight) / 2 + 8 + 10,
                "",
                0, 0, 0,
                this, 210
        );
        this.addWidget(slider);
        this.addRenderableWidget(slider);
    }

    protected void drawForegroundLayer(GuiGraphics poseStack, int par1, int par2) {
        super.drawForegroundLayer(poseStack, par1, par2);
        for (int i = value, j = 0; i < Math.min(
                itemStackList.size(),
                value + 12
        ); i++, j++) {
            final int finalI = i;
            new ItemStackImage(this, 175, 5 + 10 + 18 * j, () -> itemStackList.get(finalI)).drawForeground(poseStack, par1, par2);
        }
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.imageWidth) / 2;
        int yMin = (this.height - this.imageHeight) / 2;
        int x = i - xMin;
        int y = j - yMin;
        for (int ii = value, jj = 0; ii < Math.min(
                itemStackList.size(),
                value + 12
        ); ii++, jj++) {
            if (x >= 175 && x <= 175 + 18 && y >= 5 + 10 + 18 * jj && y < 5 + 10 + 18 * jj + 18) {
                if (!integerList.contains(ii)) {
                    integerList.add(ii);
                } else {
                    integerList.remove((Object) ii);
                }
                new PacketItemStackEvent(ii, minecraft.player);
                final CompoundTag nbt = ModUtils.nbt(this.container.base.itemStack1);
                nbt.putIntArray("list", integerList);
                update = true;
            }
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        slider.setMax(itemStackList.size() - 12);
        if (tick == 400 || update) {
            tick = 0;
            update = false;
            Map<Integer, Map<Vector2, DataOres>> map = new HashMap<>();
            ChunkPos pos = new ChunkPos(new BlockPos((int) minecraft.player.getX(), (int) minecraft.player.getY(), (int) minecraft.player.getZ()));
            int i = 0;
            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    ChunkPos chunkPos = new ChunkPos(pos.x + x, pos.z + z);
                    LevelChunk chunk = minecraft.player.level().getChunk(chunkPos.x, chunkPos.z);
                    Map<Vector2, DataOres> map1 = getDataChunk(chunk);
                    map.put(i, map1);
                    i++;
                }
            }
            colors = new int[9 * 16][9 * 16];
            final List<Map<Vector2, DataOres>> list = new ArrayList<>(map.values());
            this.elements.clear();
            this.componentList.clear();
            ChunkPos pos2 = new ChunkPos(pos.x - 4, pos.z - 4);
            container.base.vector = new Vector2(pos2.x * 16, pos2.z * 16);
            for (Map<Vector2, DataOres> map1 : list) {
                for (Map.Entry<Vector2, DataOres> entry : map1.entrySet()) {
                    colors[entry.getKey().getX() - container.base.getVector().getX()][entry.getKey().getZ() - container.base
                            .getVector()
                            .getZ()] = entry.getValue().getColor();
                    ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1);
                    if (!integerList.isEmpty()) {
                        boolean find = false;
                        for (Integer integer : integerList) {
                            if (itemStackList.get(integer).is(stack.getItem())) {
                                find = true;
                                this.addElement(new Area(
                                        this,
                                        20 + entry.getKey().getX() - container.base.getVector().getX(),
                                        10 + entry.getKey().getZ() - container.base
                                                .getVector()
                                                .getZ(),
                                        1,
                                        1
                                ).withTooltip(() -> stack.getDisplayName().getString() + "\n" + "X: " + entry
                                        .getKey()
                                        .getX() + "\n" + "Z: " + entry
                                        .getKey()
                                        .getZ()));
                                break;
                            }
                        }
                        if (!find) {
                            colors[entry.getKey().getX() - container.base.getVector().getX()][entry
                                    .getKey()
                                    .getZ() - container.base
                                    .getVector()
                                    .getZ()] = MapColor.STONE.col | -16777216;
                        }
                    } else if (entry.getValue().getColor() != 0xFFFFFFFF) {
                        this.addElement(new Area(
                                this,
                                20 + entry.getKey().getX() - container.base.getVector().getX(),
                                10 + entry.getKey().getZ() - container.base
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
            componentList.add(slots);
            this.addElement(new ImageInterface(this, 0, 0, imageWidth, imageHeight));
        } else {
            tick++;
        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics poseStack, final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(poseStack, partialTicks, mouseX, mouseY);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, POSITION_COLOR);
        for (int i = 0; i < 9 * 16; i++) {
            for (int j = 0; j < 9 * 16; j++) {
                this.drawColoredRect(poseStack, 20 + i,
                        10 + j, 1, 1,
                        colors[i][j], bufferBuilder
                );
            }
        }
        BufferUploader.drawWithShader(bufferBuilder.end());
        int centerX = 20 + (9 * 16) / 2;
        int centerY = 10 + (9 * 16) / 2;


        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        PoseStack pose = poseStack.pose();
        pose.pushPose();
        Matrix4f martix = pose.last().pose();

        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, POSITION_COLOR);
        buffer.vertex(martix,this.guiLeft + centerX, this.guiTop + 10, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.vertex(martix,this.guiLeft + centerX, this.guiTop + 10 + (9 * 16), 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();


        buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, POSITION_COLOR);
        buffer.vertex(martix,this.guiLeft + 20, this.guiTop + centerY, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.vertex(martix,this.guiLeft + 20 + (9 * 16), this.guiTop + centerY, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.end();



        pose.popPose();
        pose.pushPose();
        int chunkSize = 16;
        int numChunks = 9;

        for (int x = 0; x < numChunks; x++) {
            for (int y = 0; y < numChunks; y++) {
                int offsetX = 20 + (x * chunkSize);
                int offsetY = 10 + (y * chunkSize);

                martix = pose.last().pose();
                buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, POSITION_COLOR);
                buffer.vertex(martix,this.guiLeft + offsetX, this.guiTop + offsetY, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Нижний левый угол
                buffer.vertex(martix,this.guiLeft + offsetX + chunkSize, this.guiTop + offsetY, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Нижний правый угол
                buffer.vertex(martix,this.guiLeft + offsetX + chunkSize, this.guiTop + offsetY + chunkSize, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Верхний правый угол
                buffer.vertex(martix,this.guiLeft + offsetX, this.guiTop + offsetY + chunkSize, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex();
                tessellator.end();
            }
        }

        pose.popPose();


        new ImageResearchTableInterface(this, 171, 11, 40, (int) (18 * 12.4)).drawBackground(poseStack, this.guiLeft, guiTop);

        for (int i = value, j = 0; i < Math.min(
                itemStackList.size(),
                value + 12
        ); i++, j++) {
            final int finalI = i;
            if (integerList.contains(i)) {
                new ImageResearchTableInterface(this, 175, 5 + 10 + 18 * j, 16, 16).drawBackground(poseStack, this.guiLeft, guiTop);
            }
            new ItemStackImage(this, 175, 5 + 10 + 18 * j, () -> itemStackList.get(finalI)).drawBackground(poseStack, this.guiLeft, guiTop);

        }

    }

    protected void drawBackgroundAndTitle(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
