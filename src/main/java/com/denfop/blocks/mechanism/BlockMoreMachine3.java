package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.multimechanism.dual.*;
import com.denfop.tiles.mechanism.multimechanism.quad.*;
import com.denfop.tiles.mechanism.multimechanism.simple.*;
import com.denfop.tiles.mechanism.multimechanism.triple.*;
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

public enum BlockMoreMachine3 implements IMultiTileBlock {
    farmer(TileFermer.class, 0),
    double_farmer(TileDoubleFermer.class, 1),
    triple_farmer(TileTripleFermer.class, 2),
    quad_farmer(TileQuadFermer.class, 3),
    assamplerscrap(TileAssamplerScrap.class, 4),
    double_assamplerscrap(TileDoubleAssamplerScrap.class, 5),
    triple_assamplerscrap(TileTripleAssamplerScrap.class, 6),
    quad_assamplerscrap(TileQuadAssamplerScrap.class, 7),
    orewashing(TileOreWashing.class, 8),
    doubleorewashing(TileDoubleOreWashing.class, 9),
    tripleorewashing(TileTripleOreWashing.class, 10),
    quadorewashing(TileQuadOreWashing.class, 11),
    centrifuge_iu(TileCentrifuge.class, 12),
    doublecentrifuge(TileDoubleCentrifuge.class, 13),
    triplecentrifuge(TileTripleCentrifuge.class, 14),
    quadcentrifuge(TileQuadCentrifuge.class, 15),
    gearing(TileGearMachine.class, 16),
    doublegearing(TileDoubleGearMachine.class, 17),
    triplegearing(TileTripleGearMachine.class, 18),
    quadgearing(TileQuadGearMachine.class, 19),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    BlockMoreMachine3(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


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
        return "moremachine3";
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

    public int getIDBlock() {
        return idBlock;
    }

    ;

    public void setIdBlock(int id) {
        idBlock = id;
    }

    ;

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
