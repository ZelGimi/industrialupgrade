package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.multimechanism.dual.*;
import com.denfop.blockentity.mechanism.multimechanism.quad.*;
import com.denfop.blockentity.mechanism.multimechanism.simple.*;
import com.denfop.blockentity.mechanism.multimechanism.triple.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
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

public enum BlockMoreMachine3Entity implements MultiBlockEntity {
    farmer(BlockEntityFermer.class, 0),
    double_farmer(BlockEntityDoubleFermer.class, 1),
    triple_farmer(BlockEntityTripleFermer.class, 2),
    quad_farmer(BlockEntityQuadFermer.class, 3),
    assamplerscrap(BlockEntityAssamplerScrap.class, 4),
    double_assamplerscrap(BlockEntityDoubleAssamplerScrap.class, 5),
    triple_assamplerscrap(BlockEntityTripleAssamplerScrap.class, 6),
    quad_assamplerscrap(BlockEntityQuadAssamplerScrap.class, 7),
    orewashing(BlockEntityOreWashing.class, 8),
    doubleorewashing(BlockEntityDoubleOreWashing.class, 9),
    tripleorewashing(BlockEntityTripleOreWashing.class, 10),
    quadorewashing(BlockEntityQuadOreWashing.class, 11),
    centrifuge_iu(BlockEntityCentrifuge.class, 12),
    doublecentrifuge(BlockEntityDoubleCentrifuge.class, 13),
    triplecentrifuge(BlockEntityTripleCentrifuge.class, 14),
    quadcentrifuge(BlockEntityQuadCentrifuge.class, 15),
    gearing(BlockEntityGearMachine.class, 16),
    doublegearing(BlockEntityDoubleGearMachine.class, 17),
    triplegearing(BlockEntityTripleGearMachine.class, 18),
    quadgearing(BlockEntityQuadGearMachine.class, 19),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    BlockMoreMachine3Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;


    }

    public void buildDummies() {
        final ModContainer mc = IUCore.instance.modContainer;
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        if (this.getTeClass() != null) {
            try {
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void setDefaultState(BlockState blockState) {
        this.defaultState = blockState;
    }

    @Override
    public void setType(DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
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
    public Class<? extends BlockEntityBase> getTeClass() {
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }
}
