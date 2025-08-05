package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.TileCombDoubleMacerator;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleRecycler;
import com.denfop.tiles.mechanism.multimechanism.quad.TileCombQuadMacerator;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadRecycler;
import com.denfop.tiles.mechanism.multimechanism.simple.TileCombMacerator;
import com.denfop.tiles.mechanism.multimechanism.triple.TileCombTripleMacerator;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleCombRecycler;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleRecycler;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockMoreMachine1 implements IMultiTileBlock {
    double_recycler(TileDoubleRecycler.class, 0),
    triple_recycler(TileTripleRecycler.class, 1),
    quad_recycler(TileQuadRecycler.class, 2),
    double_comb_recycler(TileDoubleCombRecycler.class, 3),
    triple_comb_recycler(TileTripleCombRecycler.class, 4),
    quad_comb_recycler(TileQuadCombRecycler.class, 5),
    comb_macerator(TileCombMacerator.class, 6),
    double_comb_macerator(TileCombDoubleMacerator.class, 7),
    triple_comb_macerator(TileCombTripleMacerator.class, 8),
    quad_comb_macerator(TileCombQuadMacerator.class, 9),

    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockMoreMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


    }

    ;

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    public void buildDummies() {
        final ModContainer mc = IUCore.instance.modContainer;
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
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "moremachine1";
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
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public HarvestTool getHarvestTool() {
        return HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public DefaultDrop getDefaultDrop() {
        return DefaultDrop.Machine;
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
