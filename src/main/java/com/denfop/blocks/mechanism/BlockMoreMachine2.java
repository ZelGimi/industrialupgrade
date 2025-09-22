package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleCutting;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleExtruding;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleRolling;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadCutting;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadExtruding;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadRolling;
import com.denfop.tiles.mechanism.multimechanism.simple.TileCutting;
import com.denfop.tiles.mechanism.multimechanism.simple.TileExtruding;
import com.denfop.tiles.mechanism.multimechanism.simple.TileRolling;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleCutting;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleExtruding;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleRolling;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockMoreMachine2 implements IMultiTileBlock {
    rolling(TileRolling.class, 0),
    double_rolling(TileDoubleRolling.class, 1),
    triple_rolling(TileTripleRolling.class, 2),
    quad_rolling(TileQuadRolling.class, 3),
    extruder(TileExtruding.class, 4),
    double_extruder(TileDoubleExtruding.class, 5),
    triple_extruder(TileTripleExtruding.class, 6),
    quad_extruder(TileQuadExtruding.class, 7),
    cutting(TileCutting.class, 8),
    double_cutting(TileDoubleCutting.class, 9),
    triple_cutting(TileTripleCutting.class, 10),
    quad_cutting(TileQuadCutting.class, 11),

    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("moremachine2");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    int idBlock;
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

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    ;

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockMoreMachine2 block : BlockMoreMachine2.values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

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
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Machine;
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
