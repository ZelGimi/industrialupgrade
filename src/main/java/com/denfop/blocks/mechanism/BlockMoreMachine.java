package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.mechanism.TileEntityDoubleCompressor;
import com.denfop.tiles.mechanism.TileEntityDoubleElectricFurnace;
import com.denfop.tiles.mechanism.TileEntityDoubleExtractor;
import com.denfop.tiles.mechanism.TileEntityDoubleMacerator;
import com.denfop.tiles.mechanism.TileEntityDoubleMetalFormer;
import com.denfop.tiles.mechanism.TileEntityQuadCompressor;
import com.denfop.tiles.mechanism.TileEntityQuadElectricFurnace;
import com.denfop.tiles.mechanism.TileEntityQuadExtractor;
import com.denfop.tiles.mechanism.TileEntityQuadMacerator;
import com.denfop.tiles.mechanism.TileEntityQuadMetalFormer;
import com.denfop.tiles.mechanism.TileEntityTripleCompressor;
import com.denfop.tiles.mechanism.TileEntityTripleElectricFurnace;
import com.denfop.tiles.mechanism.TileEntityTripleExtractor;
import com.denfop.tiles.mechanism.TileEntityTripleMacerator;
import com.denfop.tiles.mechanism.TileEntityTripleMetalFormer;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockMoreMachine implements ITeBlock {
    double_macerator(TileEntityDoubleMacerator.class, 0),
    triple_macerator(TileEntityTripleMacerator.class, 1),
    quad_macerator(TileEntityQuadMacerator.class, 2),
    double_commpressor(TileEntityDoubleCompressor.class, 3),
    triple_commpressor(TileEntityTripleCompressor.class, 4),
    quad_commpressor(TileEntityQuadCompressor.class, 5),
    double_furnace(TileEntityDoubleElectricFurnace.class, 6),
    triple_furnace(TileEntityTripleElectricFurnace.class, 7),
    quad_furnace(TileEntityQuadElectricFurnace.class, 8),
    double_extractor(TileEntityDoubleExtractor.class, 9),
    triple_extractor(TileEntityTripleExtractor.class, 10),
    quad_extractor(TileEntityQuadExtractor.class, 11),
    double_metalformer(TileEntityDoubleMetalFormer.class, 12),
    triple_metalformer(TileEntityTripleMetalFormer.class, 13),
    quad_metalformer(TileEntityQuadMetalFormer.class, 14),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockMoreMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockMoreMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }


    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockMoreMachine block : BlockMoreMachine.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {
                    if (Util.inDev()) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getIdentifier() {
        return BlockMoreMachine.IDENTITY;
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
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    @Nonnull
    public Set<EnumFacing> getSupportedFacings() {
        return Util.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    public float getExplosionResistance() {
        return 0.0f;
    }

    @Override
    @Nonnull
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Self;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity() {
        return this.rarity;
    }

    @Override
    public boolean allowWrenchRotating() {
        return true;
    }

    @Override
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
