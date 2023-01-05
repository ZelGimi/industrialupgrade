package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.mechanism.multimechanism.dual.TileEntityDoubleCutting;
import com.denfop.tiles.mechanism.multimechanism.dual.TileEntityDoubleExtruding;
import com.denfop.tiles.mechanism.multimechanism.dual.TileEntityDoubleRolling;
import com.denfop.tiles.mechanism.multimechanism.quad.TileEntityQuadCutting;
import com.denfop.tiles.mechanism.multimechanism.quad.TileEntityQuadExtruding;
import com.denfop.tiles.mechanism.multimechanism.quad.TileEntityQuadRolling;
import com.denfop.tiles.mechanism.multimechanism.simple.TileEntityCutting;
import com.denfop.tiles.mechanism.multimechanism.simple.TileEntityExtruding;
import com.denfop.tiles.mechanism.multimechanism.simple.TileEntityRolling;
import com.denfop.tiles.mechanism.multimechanism.triple.TileEntityTripleCutting;
import com.denfop.tiles.mechanism.multimechanism.triple.TileEntityTripleExtruding;
import com.denfop.tiles.mechanism.multimechanism.triple.TileEntityTripleRolling;
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

public enum BlockMoreMachine2 implements ITeBlock {
    rolling(TileEntityRolling.class, 0),
    double_rolling(TileEntityDoubleRolling.class, 1),
    triple_rolling(TileEntityTripleRolling.class, 2),
    quad_rolling(TileEntityQuadRolling.class, 3),
    extruder(TileEntityExtruding.class, 4),
    double_extruder(TileEntityDoubleExtruding.class, 5),
    triple_extruder(TileEntityTripleExtruding.class, 6),
    quad_extruder(TileEntityQuadExtruding.class, 7),
    cutting(TileEntityCutting.class, 8),
    double_cutting(TileEntityDoubleCutting.class, 9),
    triple_cutting(TileEntityTripleCutting.class, 10),
    quad_cutting(TileEntityQuadCutting.class, 11),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine2");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockMoreMachine2(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockMoreMachine2(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
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
        for (final BlockMoreMachine2 block : BlockMoreMachine2.values()) {
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
        return BlockMoreMachine2.IDENTITY;
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
    @Deprecated
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
