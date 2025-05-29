package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

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


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockMoreMachine2(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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
        return "moremachine2";
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
