package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.blocks.BlockApatite;
import com.denfop.blocks.BlockClassicOre;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockMineral;
import com.denfop.blocks.BlockOre;
import com.denfop.blocks.BlockOres2;
import com.denfop.blocks.BlockOres3;
import com.denfop.blocks.BlockPreciousOre;
import com.denfop.blocks.BlocksRadiationOre;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Vector2;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemVeinSensor extends ItemSubTypes<ItemVeinSensor.Types> implements IModelRegister, IItemStackInventory {

    protected static final String NAME = "sensor";

    public ItemVeinSensor() {
        super(Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc;
        if (!extraName.isEmpty()) {
            loc = Constants.MOD_ID +
                    ':' +
                    "sensor" + "/" + name + "_" + extraName;

        } else {
            loc = Constants.MOD_ID +
                    ':' +
                    "sensor" + "/" + name;

        }
        return new ModelResourceLocation(loc, null);
    }

    public static int getOreColor(IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.IRON_ORE) {
            return ModUtils.convertRGBcolorToInt(156, 156, 156);
        } else if (block == Blocks.GOLD_ORE) {
            return 0xFFFFD700;
        } else if (block == Blocks.DIAMOND_ORE) {
            return 0xFF00FFFF;
        } else if (block == Blocks.LAPIS_ORE) {
            return ModUtils.convertRGBcolorToInt(30, 50, 173);
        } else if (block == Blocks.REDSTONE_ORE || block == Blocks.LIT_REDSTONE_ORE) {
            return ModUtils.convertRGBcolorToInt(173, 30, 30);
        } else if (block == Blocks.COAL_ORE) {
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block == Blocks.EMERALD_ORE) {
            return ModUtils.convertRGBcolorToInt(0, 232, 0);
        } else if (block == Blocks.QUARTZ_ORE) {
            return ModUtils.convertRGBcolorToInt(223, 223, 223);
        } else if (block == IUItem.toriyore) {
            return ModUtils.convertRGBcolorToInt(134, 134, 139);
        } else if (block instanceof BlockClassicOre) {
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
                    return ModUtils.convertRGBcolorToInt(110, 110, 110);
                case 13:
                    return ModUtils.convertRGBcolorToInt(198, 147, 64);
                case 14:
                    return ModUtils.convertRGBcolorToInt(100, 76, 136);
                case 15:
                    return ModUtils.convertRGBcolorToInt(135, 84, 64);
            }
            return ModUtils.convertRGBcolorToInt(4, 4, 4);
        } else if (block instanceof BlockMineral) {
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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
            final int meta = block.getMetaFromState(state);
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

    public IAdvInventory getInventory(EntityPlayer player, ItemStack stack) {
        Map<Integer, Map<Vector2, DataOres>> map = new HashMap<>();
        ChunkPos pos = new ChunkPos(new BlockPos(player.posX, player.posY, player.posZ));
        ChunkPos pos2 = new ChunkPos(pos.x - 4, pos.z - 4);
        int i = 0;
        for (int x = -4; x < 5; x++) {
            for (int z = -4; z < 5; z++) {
                ChunkPos chunkPos = new ChunkPos(pos.x + x, pos.z + z);
                Chunk chunk = player.getEntityWorld().getChunkFromChunkCoords(chunkPos.x, chunkPos.z);
                Map<Vector2, DataOres> map1 = getDataChunk(chunk);
                map.put(i, map1);
                i++;
            }
        }
        return new ItemStackVeinSensor(player, stack, map, new Vector2(pos2.x * 16, pos2.z * 16));
    }

    @Override
    public void registerModels() {
        registerModels("sensor");
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        final NBTTagCompound nbt = ModUtils.nbt(stack);
        tooltip.add(Localization.translate("iu.sensor.info"));

        if (!nbt.getString("type").equals("")) {
            String s = nbt.getString("type");
            if (!s.equals("oil") && !s.equals("gas")) {
                tooltip.add(Localization.translate("iu.vein_info") + Localization.translate("iu." + s + ".name"));
            } else {
                if (!s.equals("gas")) {
                    tooltip.add(Localization.translate("iu.vein_info") + Localization.translate("iu.oil_vein"));
                } else {
                    tooltip.add(Localization.translate("iu.vein_info") + Localization.translate("iu.fluidgas"));
                }

            }
            int x = nbt.getInteger("x");
            int z = nbt.getInteger("z");
            tooltip.add(Localization.translate("iu.modulewirelles1") + "x: " + x + " z: " + z);
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);
            if (!nbt.getString("type").equals("")) {
                return getModelLocation1(name, nbt.getString("type"));
            }

            return getModelLocation1(name, "");

        });
        String[] mode = {"", "magnetite", "calaverite", "galena", "nickelite", "pyrite", "quartzite", "uranite", "azurite",
                "rhodonite", "alfildit", "euxenite", "smithsonite", "ilmenite", "todorokite", "ferroaugite", "sheelite",
                "oil", "arsenopyrite", "braggite", "wolframite", "germanite", "coltan", "crocoite", "xenotime", "iridosmine",
                "theoprastite", "tetrahedrite", "fergusonite", "celestine", "zircon", "crystal", "gas"};
        for (final String s : mode) {
            if (s.equals("")) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
            }
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));

        }

    }

    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, EntityPlayer player, @Nonnull EnumHand hand) {

        if (IUCore.proxy.isSimulating()) {
            player.openGui(IUCore.instance, 1, world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));

        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    public Map<Vector2, DataOres> getDataChunk(Chunk chunk) {

        Map<Vector2, DataOres> map = new HashMap<>();
        for (int x = chunk.x * 16; x < chunk.x * 16 + 16; x++) {
            for (int z = chunk.z * 16; z < chunk.z * 16 + 16; z++) {
                for (int y = 40; y < chunk.getHeight(new BlockPos(x, 0, z)); y++) {
                    IBlockState blockState = chunk.getBlockState(x, y, z);
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

    public enum Types implements ISubEnum {
        sensor(0);

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

        public int getId() {
            return this.ID;
        }
    }

}
