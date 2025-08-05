package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleCompressor;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleExtractor;
import com.denfop.tiles.mechanism.multimechanism.dual.TileDoubleMacerator;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadCompressor;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadExtractor;
import com.denfop.tiles.mechanism.multimechanism.quad.TileQuadMacerator;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleCompressor;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleElectricFurnace;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleExtractor;
import com.denfop.tiles.mechanism.multimechanism.triple.TileTripleMacerator;
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

public enum BlockMoreMachine implements IMultiTileBlock {
    double_macerator(TileDoubleMacerator.class, 0),
    triple_macerator(TileTripleMacerator.class, 1),
    quad_macerator(TileQuadMacerator.class, 2),
    double_commpressor(TileDoubleCompressor.class, 3),
    triple_commpressor(TileTripleCompressor.class, 4),
    quad_commpressor(TileQuadCompressor.class, 5),
    double_furnace(TileDoubleElectricFurnace.class, 6),
    triple_furnace(TileTripleElectricFurnace.class, 7),
    quad_furnace(TileQuadElectricFurnace.class, 8),
    double_extractor(TileDoubleExtractor.class, 9),
    triple_extractor(TileTripleExtractor.class, 10),
    quad_extractor(TileQuadExtractor.class, 11),


    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockMoreMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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
        return "moremachine";
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
