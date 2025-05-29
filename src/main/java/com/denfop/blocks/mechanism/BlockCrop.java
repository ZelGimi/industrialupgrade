package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.crop.TileEntityCrop;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockCrop implements IMultiTileBlock {

    crop(TileEntityCrop.class, 0),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    BlockCrop(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    BlockCrop(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    public void buildDummies() {
        final ModContainer mc = ModLoadingContext.get().getActiveContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "crop";
    }

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    ;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }


    @Override
    public boolean hasItem() {
        return true;
    }

    @Override
    public Class<? extends TileEntityBlock> getTeClass() {
        return this.teClass;
    }

    @Override
    public boolean hasActive() {
        return true;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.noFacings;
    }

    @Override
    public float getHardness() {
        return 0.1F;
    }

    @Override
    public boolean hasOtherVersion() {
        return false;
    }

    @Override
    public String[] getMultiModels(IMultiTileBlock teBlock) {
        return new String[]{
                "wheat_0", "wheat_1", "wheat_2", "wheat_3", "wheat_4", "wheat_5", "wheat_6", "wheat_7", "reed_0", "reed_1", "reed_2", "reed_3", "weed_seed_0", "weed_seed_1", "weed_seed_2", "weed_seed_3", "tulip_pink_0", "tulip_pink_1", "tulip_pink_2", "tulip_pink_3", "haustonia_gray_0", "haustonia_gray_1", "haustonia_gray_2", "haustonia_gray_3", "poppy_0", "poppy_1", "poppy_2", "poppy_3", "onion_0", "onion_1", "onion_2", "onion_3", "brown_mushroom_0", "brown_mushroom_1", "brown_mushroom_2", "brown_mushroom_3", "tulip_orange_0", "tulip_orange_1", "tulip_orange_2", "tulip_orange_3", "chamomile_0", "chamomile_1", "chamomile_2", "chamomile_3", "tulip_white_0", "tulip_white_1", "tulip_white_2", "tulip_white_3", "dandelion_0", "dandelion_1", "dandelion_2", "dandelion_3", "tulip_red_0", "tulip_red_1", "tulip_red_2", "tulip_red_3", "red_mushroom_0", "red_mushroom_1", "red_mushroom_2", "red_mushroom_3", "blue_orchid_0", "blue_orchid_1", "blue_orchid_2", "blue_orchid_3", "corn_0", "corn_1", "corn_2", "corn_3", "rubber_reed_0", "rubber_reed_1", "rubber_reed_2", "rubber_reed_3", "beet_0", "beet_1", "beet_2", "beet_3", "carrot_0", "carrot_1", "carrot_2", "carrot_3", "potato_0", "potato_1", "potato_2", "potato_3", "tomato_0", "tomato_1", "tomato_2", "tomato_3", "raspberry_0", "raspberry_1", "raspberry_2", "raspberry_3", "hops_0", "hops_1", "hops_2", "hops_3", "melon_0", "melon_1", "melon_2", "melon_3", "pumpkin_0", "pumpkin_1", "pumpkin_2", "pumpkin_3", "copper_heart_0", "copper_heart_1", "copper_heart_2", "copper_heart_3", "iron_crimson_0", "iron_crimson_1", "iron_crimson_2", "iron_crimson_3", "gold_astral_0", "gold_astral_1", "gold_astral_2", "gold_astral_3", "diamond_island_0", "diamond_island_1", "diamond_island_2", "diamond_island_3", "emerald_heart_0", "emerald_heart_1", "emerald_heart_2", "emerald_heart_3", "quartz_storm_0", "quartz_storm_1", "quartz_storm_2", "quartz_storm_3", "lead_stream_0", "lead_stream_1", "lead_stream_2", "lead_stream_3", "tin_ghost_0", "tin_ghost_1", "tin_ghost_2", "tin_ghost_3", "red_fury_0", "red_fury_1", "red_fury_2", "red_fury_3", "silicon_avalanche_0", "silicon_avalanche_1", "silicon_avalanche_2", "silicon_avalanche_3", "mikhailovskaya_lavra_0", "mikhailovskaya_lavra_1", "mikhailovskaya_lavra_2", "mikhailovskaya_lavra_3", "aluminum_plutonka_0", "aluminum_plutonka_1", "aluminum_plutonka_2", "aluminum_plutonka_3", "tungsten_moon_flower_0", "tungsten_moon_flower_1", "tungsten_moon_flower_2", "tungsten_moon_flower_3", "silver_melody_0", "silver_melody_1", "silver_melody_2", "silver_melody_3", "nickel_firework_0", "nickel_firework_1", "nickel_firework_2", "nickel_firework_3", "invar_crystal_0", "invar_crystal_1", "invar_crystal_2", "invar_crystal_3", "cobalt_grapefruit_0", "cobalt_grapefruit_1", "cobalt_grapefruit_2", "cobalt_grapefruit_3", "magnesium_diamond_0", "magnesium_diamond_1", "magnesium_diamond_2", "magnesium_diamond_3", "platinum_turner_0", "platinum_turner_1", "platinum_turner_2", "platinum_turner_3", "spinel_zenith_0", "spinel_zenith_1", "spinel_zenith_2", "spinel_zenith_3", "iridium_snowflake_0", "iridium_snowflake_1", "iridium_snowflake_2", "iridium_snowflake_3", "karav_green_0", "karav_green_1", "karav_green_2", "karav_green_3", "titanium_fantasy_0", "titanium_fantasy_1", "titanium_fantasy_2", "titanium_fantasy_3", "chromium_gentleness_0", "chromium_gentleness_1", "chromium_gentleness_2", "chromium_gentleness_3", "electrum_symphony_0", "electrum_symphony_1", "electrum_symphony_2", "electrum_symphony_3", "zinc_storm_0", "zinc_storm_1", "zinc_storm_2", "zinc_storm_3", "manganese_lotus_0", "manganese_lotus_1", "manganese_lotus_2", "manganese_lotus_3", "germanite_wonders_0", "germanite_wonders_1", "germanite_wonders_2", "germanite_wonders_3", "uranium_fairy_0", "uranium_fairy_1", "uranium_fairy_2", "uranium_fairy_3", "vanadium_dawn_0", "vanadium_dawn_1", "vanadium_dawn_2", "vanadium_dawn_3", "arsenite_flower_0", "arsenite_flower_1", "arsenite_flower_2", "arsenite_flower_3", "barium_star_0", "barium_star_1", "barium_star_2", "barium_star_3", "bismuth_garden_0", "bismuth_garden_1", "bismuth_garden_2", "bismuth_garden_3", "gadolinium_lotus_0", "gadolinium_lotus_1", "gadolinium_lotus_2", "gadolinium_lotus_3", "gallium_song_0", "gallium_song_1", "gallium_song_2", "gallium_song_3", "hafnium_leaf_0", "hafnium_leaf_1", "hafnium_leaf_2", "hafnium_leaf_3", "ytterbium_bright_0", "ytterbium_bright_1", "ytterbium_bright_2", "ytterbium_bright_3", "molybdenum_whirlwind_0", "molybdenum_whirlwind_1", "molybdenum_whirlwind_2", "molybdenum_whirlwind_3", "neodymium_glow_0", "neodymium_glow_1", "neodymium_glow_2", "neodymium_glow_3", "niobium_obelisk_0", "niobium_obelisk_1", "niobium_obelisk_2", "niobium_obelisk_3", "palladium_gentleness_0", "palladium_gentleness_1", "palladium_gentleness_2", "palladium_gentleness_3", "polonium_crystal_0", "polonium_crystal_1", "polonium_crystal_2", "polonium_crystal_3", "strontium_storm_0", "strontium_storm_1", "strontium_storm_2", "strontium_storm_3", "thallium_sunlight_0", "thallium_sunlight_1", "thallium_sunlight_2", "thallium_sunlight_3", "zirconium_dragon_0", "zirconium_dragon_1", "zirconium_dragon_2", "zirconium_dragon_3", "osmium_silk_0", "osmium_silk_1", "osmium_silk_2", "osmium_silk_3", "tantalum_moon_0", "tantalum_moon_1", "tantalum_moon_2", "tantalum_moon_3", "cadmium_gentleness_0", "cadmium_gentleness_1", "cadmium_gentleness_2", "cadmium_gentleness_3", "nether_wart_0", "nether_wart_1", "nether_wart_2", "ender_lily_0", "ender_lily_1", "ender_lily_2", "ender_lily_3", "blaze_storm_0", "blaze_storm_1", "blaze_storm_2", "blaze_storm_3", "ghast_dew_0", "ghast_dew_1", "ghast_dew_2", "ghast_dew_3", "bone_leaf_0", "bone_leaf_1", "bone_leaf_2", "bone_leaf_3", "americium_moss_0", "americium_moss_1", "americium_moss_2", "americium_moss_3", "neptunium_wisdom_0", "neptunium_wisdom_1", "neptunium_wisdom_2", "neptunium_wisdom_3", "curie_berry_0", "curie_berry_1", "curie_berry_2", "curie_berry_3", "thorium_fist_0", "thorium_fist_1", "thorium_fist_2", "thorium_fist_3", "terra_wart_0", "terra_wart_1", "terra_wart_2", "terra_wart_3"
        };
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        return HarvestTool.Axe;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public MapColor getMaterial() {
        return MapColor.PLANT;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }


}
