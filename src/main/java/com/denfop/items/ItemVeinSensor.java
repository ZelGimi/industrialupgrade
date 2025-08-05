package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.blocks.*;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.datacomponent.VeinInfo;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.IUpdatableItemStackEvent;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Vector2;
import com.denfop.world.WorldBaseGen;
import com.denfop.world.vein.VeinType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.denfop.utils.ModUtils.inChanceOre;
import static com.denfop.world.vein.AlgorithmVein.shellClusterChuncks;
import static com.denfop.world.vein.VeinType.veinTypeMap;

public class ItemVeinSensor<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IUpdatableItemStackEvent,
        IItemStackInventory, IProperties {
    public static final Map<String, Integer> ORE_INDEX_MAP = new HashMap<>();

    static {
        ORE_INDEX_MAP.put("magnetite", 0);
        ORE_INDEX_MAP.put("calaverite", 1);
        ORE_INDEX_MAP.put("galena", 2);
        ORE_INDEX_MAP.put("nickelite", 3);
        ORE_INDEX_MAP.put("pyrite", 4);
        ORE_INDEX_MAP.put("quartzite", 5);
        ORE_INDEX_MAP.put("uranite", 6);
        ORE_INDEX_MAP.put("azurite", 7);
        ORE_INDEX_MAP.put("rhodonite", 8);
        ORE_INDEX_MAP.put("alfildit", 9);
        ORE_INDEX_MAP.put("euxenite", 10);
        ORE_INDEX_MAP.put("smithsonite", 11);
        ORE_INDEX_MAP.put("ilmenite", 12);
        ORE_INDEX_MAP.put("todorokite", 13);
        ORE_INDEX_MAP.put("ferroaugite", 14);
        ORE_INDEX_MAP.put("sheelite", 15);
        ORE_INDEX_MAP.put("oil", 16);
        ORE_INDEX_MAP.put("arsenopyrite", 17);
        ORE_INDEX_MAP.put("braggite", 18);
        ORE_INDEX_MAP.put("wolframite", 19);
        ORE_INDEX_MAP.put("germanite", 20);
        ORE_INDEX_MAP.put("coltan", 21);
        ORE_INDEX_MAP.put("crocoite", 22);
        ORE_INDEX_MAP.put("xenotime", 23);
        ORE_INDEX_MAP.put("iridosmine", 24);
        ORE_INDEX_MAP.put("theoprastite", 25);
        ORE_INDEX_MAP.put("tetrahedrite", 26);
        ORE_INDEX_MAP.put("fergusonite", 27);
        ORE_INDEX_MAP.put("celestine", 28);
        ORE_INDEX_MAP.put("zircon", 29);
        ORE_INDEX_MAP.put("crystal", 30);
        ORE_INDEX_MAP.put("gas", 31);
    }

    public ItemVeinSensor(T element) {
        super(new Item.Properties().stacksTo(1), element);
        IUCore.proxy.addProperties(this);
    }

    public static Map<Vector2, DataOres> getDataChunk(ChunkAccess chunk) {

        Map<Vector2, DataOres> map = new HashMap<>();
        for (int x = chunk.getPos().x * 16; x < chunk.getPos().x * 16 + 16; x++) {
            for (int z = chunk.getPos().z * 16; z < chunk.getPos().z * 16 + 16; z++) {
                for (int y = 40; y < chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z); y++) {
                    BlockState blockState = chunk.getBlockState(new BlockPos(x, y, z));
                    int color = getOreColor(blockState);
                    Vector2 vector2 = new Vector2(x, z);
                    if (color != 0xFFFFFFFF) {
                        if (!map.containsKey(vector2)) {
                            map.put(vector2, new DataOres(blockState, color));
                        } else {
                            map.replace(vector2, new DataOres(blockState, color));
                        }
                    } else {

                        if (!map.containsKey(vector2)) {
                            map.put(vector2, new DataOres(blockState, blockState.getMapColor(
                                    chunk.getLevel(),
                                    new BlockPos(x, y, z)
                            ).col | -16777216));
                        }
                    }
                }
            }
        }
        return map;
    }

    public static int getOreColor(BlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.IRON_ORE) {
            return ModUtils.convertRGBcolorToInt(156, 156, 156);
        } else if (block == Blocks.GOLD_ORE) {
            return 0xFFFFD700;
        } else if (block == Blocks.DIAMOND_ORE) {
            return 0xFF00FFFF;
        } else if (block == Blocks.LAPIS_ORE) {
            return ModUtils.convertRGBcolorToInt(30, 50, 173);
        } else if (block == Blocks.REDSTONE_ORE) {
            return ModUtils.convertRGBcolorToInt(173, 30, 30);
        } else if (block == Blocks.COAL_ORE) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block == Blocks.EMERALD_ORE) {
            return ModUtils.convertRGBcolorToInt(0, 232, 0);
        }else if (block == Blocks.COPPER_ORE) {
            return ModUtils.convertRGBcolorToInt(255, 144, 0);
        } else if (block == Blocks.NETHER_QUARTZ_ORE) {
            return ModUtils.convertRGBcolorToInt(223, 223, 223);
        } else if (block == IUItem.toriyore.getBlock(0)) {
            return ModUtils.convertRGBcolorToInt(134, 134, 139);
        } else if (block instanceof BlockClassicOre) {
            final int meta = ((ISubEnum) ((BlockClassicOre) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(255, 144, 0);
                case 1:
                    return ModUtils.convertRGBcolorToInt(223, 223, 223);
                case 2:
                    return ModUtils.convertRGBcolorToInt(168, 176, 150);
                case 3:
                    return ModUtils.convertRGBcolorToInt(89, 158, 73);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlocksRadiationOre) {
            final int meta = ((ISubEnum) ((BlocksRadiationOre) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(120, 152, 183);
                case 1:
                    return ModUtils.convertRGBcolorToInt(97, 109, 88);
                case 2:
                    return ModUtils.convertRGBcolorToInt(150, 166, 148);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockPreciousOre) {
            final int meta = ((ISubEnum) ((BlockPreciousOre) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(251, 140, 119);
                case 1:
                    return ModUtils.convertRGBcolorToInt(38, 60, 143);
                case 2:
                    return ModUtils.convertRGBcolorToInt(204, 180, 47);
                case 3:
                    return ModUtils.convertRGBcolorToInt(200, 205, 207);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOre) {
            final int meta = ((ISubEnum) ((BlockOre) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(119, 210, 202);
                case 1:
                    return ModUtils.convertRGBcolorToInt(108, 74, 108);
                case 2:
                    return ModUtils.convertRGBcolorToInt(142, 240, 216);
                case 3:
                    return ModUtils.convertRGBcolorToInt(199, 199, 199);
                case 4:
                    return ModUtils.convertRGBcolorToInt(0, 166, 226);
                case 5:
                    return ModUtils.convertRGBcolorToInt(170, 145, 160);
                case 6:
                    return ModUtils.convertRGBcolorToInt(145, 143, 88);
                case 7:
                    return ModUtils.convertRGBcolorToInt(104, 152, 237);
                case 8:
                    return ModUtils.convertRGBcolorToInt(71, 71, 71);
                case 9:
                    return ModUtils.convertRGBcolorToInt(83, 174, 85);
                case 10:
                    return ModUtils.convertRGBcolorToInt(184, 87, 145);
                case 11:
                    return ModUtils.convertRGBcolorToInt(211, 211, 211);
                case 12:
                    return ModUtils.convertRGBcolorToInt(186, 186, 186);
                case 13:
                    return ModUtils.convertRGBcolorToInt(235, 193, 207);
                case 14:
                    return ModUtils.convertRGBcolorToInt(234, 234, 234);
                case 15:
                    return ModUtils.convertRGBcolorToInt(138, 85, 34);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockApatite) {
            final int meta = ((ISubEnum) ((BlockApatite) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(48, 86, 16);
                case 1:
                    return ModUtils.convertRGBcolorToInt(134, 95, 11);
                case 2:
                    return ModUtils.convertRGBcolorToInt(202, 202, 202);
                case 3:
                    return ModUtils.convertRGBcolorToInt(202, 202, 202);
                case 4:
                    return ModUtils.convertRGBcolorToInt(202, 202, 202);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockHeavyOre) {
            final int meta = ((ISubEnum) ((BlockHeavyOre) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(137, 131, 149);
                case 1:
                    return ModUtils.convertRGBcolorToInt(249, 175, 44);
                case 2:
                    return ModUtils.convertRGBcolorToInt(150, 215, 206);
                case 3:
                    return ModUtils.convertRGBcolorToInt(211, 202, 110);
                case 4:
                    return ModUtils.convertRGBcolorToInt(212, 175, 55);
                case 5:
                    return ModUtils.convertRGBcolorToInt(250, 246, 241);
                case 6:
                    return ModUtils.convertRGBcolorToInt(70, 145, 15);
                case 7:
                    return ModUtils.convertRGBcolorToInt(230, 107, 0);
                case 8:
                    return ModUtils.convertRGBcolorToInt(139, 0, 0);
                case 9:
                    return ModUtils.convertRGBcolorToInt(55, 135, 135);
                case 10:
                    return ModUtils.convertRGBcolorToInt(170, 123, 44);
                case 11:
                    return ModUtils.convertRGBcolorToInt(109, 206, 167);
                case 12:
                    return ModUtils.convertRGBcolorToInt(76, 76, 76);
                case 13:
                    return ModUtils.convertRGBcolorToInt(198, 147, 64);
                case 14:
                    return ModUtils.convertRGBcolorToInt(100, 76, 136);
                case 15:
                    return ModUtils.convertRGBcolorToInt(135, 84, 64);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockMineral) {
            final int meta = ((ISubEnum) ((BlockMineral) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(12, 166, 166);
                case 1:
                    return ModUtils.convertRGBcolorToInt(55, 117, 104);
                case 2:
                    return ModUtils.convertRGBcolorToInt(113, 97, 81);
                case 3:
                    return ModUtils.convertRGBcolorToInt(99, 51, 4);
                case 4:
                    return ModUtils.convertRGBcolorToInt(117, 88, 86);
                case 5:
                    return ModUtils.convertRGBcolorToInt(118, 28, 17);
                case 6:
                    return ModUtils.convertRGBcolorToInt(123, 76, 10);
                case 7:
                    return ModUtils.convertRGBcolorToInt(126, 101, 36);
                case 8:
                    return ModUtils.convertRGBcolorToInt(30, 126, 56);
                case 9:
                    return ModUtils.convertRGBcolorToInt(112, 129, 30);
                case 10:
                    return ModUtils.convertRGBcolorToInt(43, 43, 43);
                case 11:
                    return ModUtils.convertRGBcolorToInt(39, 64, 63);
                case 12:
                    return ModUtils.convertRGBcolorToInt(110, 25, 24);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOres3) {
            final int meta = ((ISubEnum) ((BlockOres3) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(191, 212, 65);
                case 1:
                    return ModUtils.convertRGBcolorToInt(253, 242, 80);
                case 2:
                    return ModUtils.convertRGBcolorToInt(37, 145, 133);
                case 3:
                    return ModUtils.convertRGBcolorToInt(255, 180, 0);
                case 4:
                    return ModUtils.convertRGBcolorToInt(252, 187, 89);
                case 5:
                    return ModUtils.convertRGBcolorToInt(212, 231, 255);
                case 6:
                    return ModUtils.convertRGBcolorToInt(222, 101, 98);
                case 7:
                    return ModUtils.convertRGBcolorToInt(118, 84, 192);
                case 8:
                    return ModUtils.convertRGBcolorToInt(125, 122, 160);
                case 9:
                    return ModUtils.convertRGBcolorToInt(61, 148, 224);
                case 10:
                    return ModUtils.convertRGBcolorToInt(230, 105, 17);
                case 11:
                    return ModUtils.convertRGBcolorToInt(84, 194, 246);
                case 12:
                    return ModUtils.convertRGBcolorToInt(168, 90, 41);
                case 13:
                    return ModUtils.convertRGBcolorToInt(121, 229, 71);
                case 14:
                    return ModUtils.convertRGBcolorToInt(255, 225, 136);
                case 15:
                    return ModUtils.convertRGBcolorToInt(217, 225, 94);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockOres2) {
            final int meta = ((ISubEnum) ((BlockOres2) block).getElement()).getId();
            switch (meta) {
                case 0:
                    return ModUtils.convertRGBcolorToInt(190, 207, 214);
                case 1:
                    return ModUtils.convertRGBcolorToInt(194, 194, 194);
                case 2:
                    return ModUtils.convertRGBcolorToInt(62, 69, 71);
                case 3:
                    return ModUtils.convertRGBcolorToInt(165, 236, 244);
                case 4:
                    return ModUtils.convertRGBcolorToInt(141, 174, 83);
                case 5:
                    return ModUtils.convertRGBcolorToInt(177, 100, 197);
                case 6:
                    return ModUtils.convertRGBcolorToInt(43, 43, 43);
                case 7:
                    return ModUtils.convertRGBcolorToInt(212, 212, 212);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        }

        return 0xFFFFFFFF;
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    public IAdvInventory getInventory(Player player, ItemStack stack) {
        Map<Integer, Map<Vector2, DataOres>> map = new HashMap<>();
        ChunkPos pos = new ChunkPos(new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()));
        ChunkPos pos2 = new ChunkPos(pos.x - 4, pos.z - 4);
        int i = 0;
        for (int x = -4; x < 5; x++) {
            for (int z = -4; z < 5; z++) {
                ChunkPos chunkPos = new ChunkPos(pos.x + x, pos.z + z);
                LevelChunk chunk = player.level().getChunk(chunkPos.x, chunkPos.z);
                Map<Vector2, DataOres> map1 = getDataChunk(chunk);
                map.put(i, map1);
                i++;
            }
        }
        return new ItemStackVeinSensor(player, stack, map, new Vector2(pos2.x * 16, pos2.z * 16));
    }

    @Override
    public void updateField(final String name, final CustomPacketBuffer buffer, final ItemStack stack) {

    }

    @Override
    public void updateEvent(final int event, final ItemStack stack) {

        List<Integer> list1 = stack.getOrDefault(DataComponentsInit.LIST_INTEGER, new ArrayList<>());
        list1 = new ArrayList<>(list1);
        if (list1.contains(event)) {
            list1.remove((Object) event);
        } else {
            list1.add(event);
        }
        stack.set(DataComponentsInit.LIST_INTEGER, list1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(
            final ItemStack stack,
            @Nullable final Level worldIn,
            final List<Component> tooltip,
            final TooltipFlag flagIn
    ) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);

        tooltip.add(Component.translatable("iu.sensor.info"));
        tooltip.add(Component.translatable("iu.scanner_ore.info4"));
        tooltip.add(Component.literal(Localization.translate("iu.vein_sensor.info7")+ KeyboardClient.changemode.getKey().getDisplayName().getString() + Localization.translate(
                "iu.changemode_rcm")));
        tooltip.add(Component.translatable("iu.scanner_ore.info4"));
        if (stack.has(DataComponentsInit.VEIN_INFO)) {
            @Nullable VeinInfo info = stack.get(DataComponentsInit.VEIN_INFO);
            String s = info.type();
            if (!s.equals("oil") && !s.equals("gas")) {
                tooltip.add(Component.translatable("iu.vein_info").append(Component.translatable("iu." + s + ".name")));
            } else {
                if (!s.equals("gas")) {
                    tooltip.add(Component.translatable("iu.vein_info").append(Component.translatable("iu.oil_vein")));
                } else {
                    tooltip.add(Component.translatable("iu.vein_info").append(Component.translatable("iu.fluidgas")));
                }
            }

            int x = info.x();
            int z = info.z();
            tooltip.add(Component.translatable("iu.modulewirelles1").append(" x: " + x + " z: " + z));
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        if (player.isShiftKeyDown()){
            stack.set(DataComponentsInit.LIST_INTEGER, new ArrayList<>());
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }else {
        if (!world.isClientSide && world.dimension() == Level.OVERWORLD) {
            if (IUCore.keyboard.isChangeKeyDown(player)) {
                final List<Integer> list1 =  stack.getOrDefault(DataComponentsInit.LIST_INTEGER, new ArrayList<>());
                if (!list1.isEmpty()) {
                    int meta = list1.get(0);
                    BlockState state = WorldBaseGen.blockStateMap.get(meta);
                    List<Integer> veinTypes = new LinkedList<>();
                    for (VeinType vein : WorldBaseGen.veinTypes) {
                        if ((vein.getHeavyOre() != null && vein.getHeavyOre().getStateMeta(vein.getMeta()) == state) || inChanceOre(
                                vein,
                                state
                        )) {
                            veinTypes.add(vein.getId());
                        }
                    }

                    ChunkPos chunkPos1 = new ChunkPos(BlockPos.containing(player.position()));
                    Set<ChunkPos> chunkPosList = new HashSet<>();
                    for (int x = -8; x < 9; x++)
                        for (int z = -8; z < 9; z++) {
                            chunkPosList.add(new ChunkPos(chunkPos1.x + x, chunkPos1.z + z));
                        }
                    for (ChunkPos chunkPos : chunkPosList) {
                        Map<Integer, Tuple<Color, Integer>> tupleMap = shellClusterChuncks.get(chunkPos.x % 256);
                        if (tupleMap == null)
                            continue;
                        Tuple<Color, Integer> tuple = tupleMap.get(chunkPos.z % 256);

                        if (tuple != null) {

                            VeinType veinType = veinTypeMap.get(tuple.getB());
                            if (veinTypes.contains(veinType.getId())){
                                final String s = Localization.translate("deposists.jei1") + (veinType.getHeavyOre() != null ?
                                        new ItemStack(veinType.getHeavyOre().getBlock(), 1).getDisplayName().getString() :
                                        new ItemStack(veinType.getOres().get(0).getBlock().getBlock(), 1
                                        ).getDisplayName().getString());
                                IUCore.proxy.messagePlayer(
                                        player,
                                        Component.literal(
                                                "X: " + (chunkPos.getMinBlockX() + 16) +
                                                        ", Z: " + (chunkPos.getMinBlockZ() + 16) +
                                                        " " + s
                                        ).getString()
                                );
                            }
                        }
                    }
                }
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
            CustomPacketBuffer growingBuffer = new CustomPacketBuffer(player.registryAccess());

            growingBuffer.writeByte(1);

            growingBuffer.flip();
            player.openMenu(getInventory(player, player.getItemInHand(hand)), buf -> buf.writeBytes(growingBuffer));


            return InteractionResultHolder.success(player.getItemInHand(hand));

        }
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public String[] properties() {
        return new String[]{"type"};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        if (itemStack.has(DataComponentsInit.VEIN_INFO)) {
            String type = itemStack.get(DataComponentsInit.VEIN_INFO).type();
            return ORE_INDEX_MAP.get(type);
        }
        return -1;
    }

    public enum Types implements ISubEnum {
        sensor(0),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "sensor";
        }

        public int getId() {
            return this.ID;
        }
    }
}
