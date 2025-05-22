package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Area;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiVerticalSliderList;
import com.denfop.api.gui.ImageInterface;
import com.denfop.api.gui.ImageResearchTableInterface;
import com.denfop.api.gui.ItemStackImage;
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
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.denfop.items.ItemVeinSensor.getOreColor;

@SideOnly(Side.CLIENT)
public class GuiVeinSensor extends GuiIU<ContainerVeinSensor> implements GuiPageButtonList.GuiResponder,
        GuiVerticalSliderList.FormatHelper {

    private static final ResourceLocation background = new ResourceLocation(Constants.TEXTURES, "textures/gui/GUIBags.png");
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

        this.name = Localization.translate(itemStack1.getUnlocalizedName());
        this.ySize = 242;
        this.xSize += 50;
        this.componentList.clear();
        this.slots = new GuiComponent(this, 0, 0, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.DEFAULT))
        );
        colors = new int[9 * 16][9 * 16];
        final NBTTagCompound nbt = ModUtils.nbt(itemStack1);
        integerList = Arrays.stream(nbt.getIntArray("list"))
                .boxed()
                .collect(Collectors.toList());
        itemStackList = new LinkedList<>();
        final List<VeinType> veins = WorldBaseGen.veinTypes;
        for (VeinType veinType : veins) {
            if (veinType.getHeavyOre() != null) {
                itemStackList.add(new ItemStack(veinType.getHeavyOre().getBlock(), 1, veinType.getMeta()));
            }
            for (ChanceOre chanceOre : veinType.getOres()) {
                boolean find = false;
                ItemStack stack1 = new ItemStack(chanceOre.getBlock().getBlock(), 1, chanceOre.getMeta());
                for (ItemStack stack : itemStackList) {
                    if (stack.isItemEqual(stack1)) {
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
                int meta = entry.getValue().getBlockState().getBlock().getMetaFromState(entry.getValue().getBlockState());
                ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1, meta);
                if (!integerList.isEmpty()) {
                    boolean find = false;
                    for (Integer integer : integerList) {
                        if (itemStackList.get(integer).isItemEqual(stack)) {
                            find = true;
                            this.addElement(new Area(
                                    this,
                                    20 + entry.getKey().getX() - container.base.getVector().getX(),
                                    10 + entry.getKey().getZ() - container.base
                                            .getVector()
                                            .getZ(),
                                    1,
                                    1
                            ).withTooltip(() -> stack.getDisplayName() + "\n" + "X: " + entry
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
                                .getZ()] = MapColor.STONE.colorValue | -16777216;
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
                    ).withTooltip(() -> stack.getDisplayName() + "\n" + "X: " + entry.getKey().getX() + "\n" + "Z: " + entry
                            .getKey()
                            .getZ()));
                }
            }
        }
        componentList.add(slots);
        this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));


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

    public void initGui() {
        super.initGui();


        slider = new GuiVerticalSliderList(this, 2, (this.width - this.xSize) / 2 + 201,
                (this.height - this.ySize) / 2 + 8 + 10,
                "",
                0, 0, 0,
                this, 210
        );
        this.buttonList.add(slider);
    }

    protected void drawForegroundLayer(int par1, int par2) {
        super.drawForegroundLayer(par1, par2);
        for (int i = value, j = 0; i < Math.min(
                itemStackList.size(),
                value + 12
        ); i++, j++) {
            final int finalI = i;
            new ItemStackImage(this, 175, 5 + 10 + 18 * j, () -> itemStackList.get(finalI)).drawForeground(par1, par2);
        }
    }

    @Override
    protected void mouseClicked(final int i, final int j, final int k) throws IOException {
        super.mouseClicked(i, j, k);
        int xMin = (this.width - this.xSize) / 2;
        int yMin = (this.height - this.ySize) / 2;
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
                new PacketItemStackEvent(ii, mc.player);
                final NBTTagCompound nbt = ModUtils.nbt(this.container.base.itemStack1);
                nbt.setIntArray("list", integerList.stream().mapToInt(Integer::intValue).toArray());
                update = true;
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        slider.setMax(itemStackList.size() - 12);
        if (tick == 400 || update) {
            tick = 0;
            update = false;
            Map<Integer, Map<Vector2, DataOres>> map = new HashMap<>();
            ChunkPos pos = new ChunkPos(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ));
            int i = 0;
            for (int x = -4; x < 5; x++) {
                for (int z = -4; z < 5; z++) {
                    ChunkPos chunkPos = new ChunkPos(pos.x + x, pos.z + z);
                    Chunk chunk = mc.player.getEntityWorld().getChunkFromChunkCoords(chunkPos.x, chunkPos.z);
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
                    int meta = entry.getValue().getBlockState().getBlock().getMetaFromState(entry.getValue().getBlockState());
                    ItemStack stack = new ItemStack(entry.getValue().getBlockState().getBlock(), 1, meta);
                    if (!integerList.isEmpty()) {
                        boolean find = false;
                        for (Integer integer : integerList) {
                            if (itemStackList.get(integer).isItemEqual(stack)) {
                                find = true;
                                this.addElement(new Area(
                                        this,
                                        20 + entry.getKey().getX() - container.base.getVector().getX(),
                                        10 + entry.getKey().getZ() - container.base
                                                .getVector()
                                                .getZ(),
                                        1,
                                        1
                                ).withTooltip(() -> stack.getDisplayName() + "\n" + "X: " + entry
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
                                    .getZ()] = MapColor.STONE.colorValue | -16777216;
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
                        ).withTooltip(() -> stack.getDisplayName() + "\n" + "X: " + entry.getKey().getX() + "\n" + "Z: " + entry
                                .getKey()
                                .getZ()));
                    }
                }
            }
            componentList.add(slots);
            this.addElement(new ImageInterface(this, 0, 0, xSize, ySize));
        } else {
            tick++;
        }
    }

    public Map<Vector2, DataOres> getDataChunk(Chunk chunk) {

        Map<Vector2, DataOres> map = new HashMap<>();
        List<Vector2> list = new ArrayList<>();
        for (int x = chunk.x * 16; x < chunk.x * 16 + 16; x++) {
            for (int z = chunk.z * 16; z < chunk.z * 16 + 16; z++) {
                for (int y = 40; y < chunk.getHeight(new BlockPos(x, 0, z)); y++) {
                    IBlockState blockState = chunk.getBlockState(x, y, z);
                    int color = getOreColor(blockState);
                    Vector2 vector2 = new Vector2(x, z);
                    if (color != 0xFFFFFFFF) {
                        int meta = blockState.getBlock().getMetaFromState(blockState);
                        ItemStack stack = new ItemStack(blockState.getBlock(), 1, meta);

                        if (!integerList.isEmpty()) {
                            boolean find = false;
                            for (Integer integer : integerList) {
                                if (itemStackList.get(integer).isItemEqual(stack)) {
                                    if (!map.containsKey(vector2)) {
                                        map.put(vector2, new DataOres(blockState, color));
                                    } else {
                                        map.replace(vector2, new DataOres(blockState, color));
                                    }
                                    list.add(vector2);
                                    find = true;
                                    break;
                                }
                            }
                            if (!find) {
                                if (!list.contains(vector2)) {
                                    if (!map.containsKey(vector2)) {
                                        map.put(vector2, new DataOres(blockState, MapColor.STONE.colorValue | -16777216));
                                    } else {
                                        map.replace(vector2, new DataOres(blockState, MapColor.STONE.colorValue | -16777216));
                                    }
                                }
                            }
                        } else {
                            if (!map.containsKey(vector2)) {
                                map.put(vector2, new DataOres(blockState, color));
                            } else {
                                map.replace(vector2, new DataOres(blockState, color));
                            }
                        }
                    } else {

                        if (!map.containsKey(vector2)) {
                            map.put(vector2, new DataOres(blockState, blockState.getMapColor(
                                    chunk.getWorld(),
                                    new BlockPos(x, y, z)
                            ).colorValue | -16777216));
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        for (int i = 0; i < 9 * 16; i++) {
            for (int j = 0; j < 9 * 16; j++) {
                this.drawColoredRect(20 + i,
                        10 + j, 1, 1,
                        colors[i][j]
                );
            }
        }
        int centerX = 20 + (9 * 16) / 2;
        int centerY = 10 + (9 * 16) / 2;


        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(1.0F);


        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(this.guiLeft + centerX, this.guiTop + 10, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.pos(this.guiLeft + centerX, this.guiTop + 10 + (9 * 16), 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.draw();


        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(this.guiLeft + 20, this.guiTop + centerY, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        buffer.pos(this.guiLeft + 20 + (9 * 16), this.guiTop + centerY, 0).color(1.0F, 0.0F, 0.0F, 1.0F).endVertex();
        tessellator.draw();


        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.glLineWidth(4.0F);
        int chunkSize = 16;
        int numChunks = 9;

        for (int x = 0; x < numChunks; x++) {
            for (int y = 0; y < numChunks; y++) {
                int offsetX = 20 + (x * chunkSize);
                int offsetY = 10 + (y * chunkSize);


                buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
                buffer
                        .pos(this.guiLeft + offsetX, this.guiTop + offsetY, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Нижний левый угол
                buffer
                        .pos(this.guiLeft + offsetX + chunkSize, this.guiTop + offsetY, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Нижний правый угол
                buffer
                        .pos(this.guiLeft + offsetX + chunkSize, this.guiTop + offsetY + chunkSize, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Верхний правый угол
                buffer
                        .pos(this.guiLeft + offsetX, this.guiTop + offsetY + chunkSize, 0)
                        .color(0.0F, 1.0F, 0.0F, 1.0F)
                        .endVertex(); // Верхний левый угол
                tessellator.draw();
            }
        }

        GlStateManager.popMatrix();
        new ImageResearchTableInterface(this, 171, 11, 40, (int) (18 * 12.4)).drawBackground(this.guiLeft, guiTop);

        for (int i = value, j = 0; i < Math.min(
                itemStackList.size(),
                value + 12
        ); i++, j++) {
            final int finalI = i;
            if (integerList.contains(i)) {
                new ImageResearchTableInterface(this, 175, 5 + 10 + 18 * j, 16, 16).drawBackground(this.guiLeft, guiTop);
            }
            new ItemStackImage(this, 175, 5 + 10 + 18 * j, () -> itemStackList.get(finalI)).drawBackground(this.guiLeft, guiTop);

        }

    }

    protected void drawBackgroundAndTitle(float partialTicks, int mouseX, int mouseY) {
        this.bindTexture();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

    }

    protected ResourceLocation getTexture() {
        return background;
    }

}
