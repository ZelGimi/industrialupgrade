package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.mechanism.TileEntityCombDoubleMacerator;
import com.denfop.tiles.mechanism.TileEntityCombMacerator;
import com.denfop.tiles.mechanism.TileEntityCombQuadMacerator;
import com.denfop.tiles.mechanism.TileEntityCombTripleMacerator;
import com.denfop.tiles.mechanism.TileEntityDoubleCombRecycler;
import com.denfop.tiles.mechanism.TileEntityDoubleRecycler;
import com.denfop.tiles.mechanism.TileEntityQuadCombRecycler;
import com.denfop.tiles.mechanism.TileEntityQuadRecycler;
import com.denfop.tiles.mechanism.TileEntityTripleCombRecycler;
import com.denfop.tiles.mechanism.TileEntityTripleRecycler;
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

public enum BlockMoreMachine1 implements ITeBlock {
    double_recycler(TileEntityDoubleRecycler.class, 0),
    triple_recycler(TileEntityTripleRecycler.class, 1),
    quad_recycler(TileEntityQuadRecycler.class, 2),
    double_comb_recycler(TileEntityDoubleCombRecycler.class, 3),
    triple_comb_recycler(TileEntityTripleCombRecycler.class, 4),
    quad_comb_recycler(TileEntityQuadCombRecycler.class, 5),
    comb_macerator(TileEntityCombMacerator.class, 6),
    double_comb_macerator(TileEntityCombDoubleMacerator.class, 7),
    triple_comb_macerator(TileEntityCombTripleMacerator.class, 8),
    quad_comb_macerator(TileEntityCombQuadMacerator.class, 9),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine1");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockMoreMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockMoreMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;

        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockMoreMachine1 block : BlockMoreMachine1.values()) {
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
    public String getName() {
        return this.name();
    }

    @Override
    public int getId() {
        return this.itemMeta;
    }

    @Override
    @Nonnull
    public ResourceLocation getIdentifier() {
        return BlockMoreMachine1.IDENTITY;
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
