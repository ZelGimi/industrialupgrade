package com.simplequarries;

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
import com.simplequarries.SimplyQuarries;
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

public enum BlockQuarry implements ITeBlock {
    simply_quarry(TileEntitySimplyQuarry.class, 0),
    adv_simply_quarry(TileEntityAdvSimplyQuarry.class, 1),
    imp_simply_quarry(TileEntityImpSimplyQuarry.class, 2),
    per_simply_quarry(TileEntityPerSimplyQuarry.class, 3),

    ;


    public static final ResourceLocation IDENTITY = SimplyQuarries.getIdentifier("simplyquarries");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockQuarry(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockQuarry(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, SimplyQuarries.getIdentifier(this.getName()));


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
        if (mc == null || !SQConstants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockQuarry block : BlockQuarry.values()) {
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
        return IDENTITY;
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
        return TeBlock.DefaultDrop.Machine;
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
