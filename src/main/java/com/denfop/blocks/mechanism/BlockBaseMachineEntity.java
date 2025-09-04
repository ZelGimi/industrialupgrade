package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.base.BlockEntityNeutronGenerator;
import com.denfop.blockentity.mechanism.BlockEntityGenerationMicrochip;
import com.denfop.blockentity.mechanism.BlockEntityGenerationStone;
import com.denfop.blockentity.mechanism.BlockEntityModuleMachine;
import com.denfop.blockentity.mechanism.dual.heat.BlockEntityAlloySmelter;
import com.denfop.blockentity.mechanism.generator.energy.coal.BlockEntityGeneratorAdv;
import com.denfop.blockentity.mechanism.generator.energy.coal.BlockEntityGeneratorImp;
import com.denfop.blockentity.mechanism.generator.energy.coal.BlockEntityGeneratorPer;
import com.denfop.blockentity.mechanism.generator.things.matter.BlockEntityAdvancedMatter;
import com.denfop.blockentity.mechanism.generator.things.matter.BlockEntityImprovedMatter;
import com.denfop.blockentity.mechanism.generator.things.matter.BlockEntityUltimateMatter;
import com.denfop.blockentity.mechanism.quarry.BlockEntityAdvQuantumQuarry;
import com.denfop.blockentity.mechanism.quarry.BlockEntityImpQuantumQuarry;
import com.denfop.blockentity.mechanism.quarry.BlockEntityPerQuantumQuarry;
import com.denfop.blockentity.mechanism.quarry.BlockEntityQuantumQuarry;
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

public enum BlockBaseMachineEntity implements MultiBlockEntity {

    adv_matter(BlockEntityAdvancedMatter.class, 1),
    imp_matter(BlockEntityImprovedMatter.class, 2),
    per_matter(BlockEntityUltimateMatter.class, 3),
    alloy_smelter(BlockEntityAlloySmelter.class, 4),
    neutron_generator(BlockEntityNeutronGenerator.class, 5),
    generator_microchip(BlockEntityGenerationMicrochip.class, 6),
    gen_stone(BlockEntityGenerationStone.class, 7),
    quantum_quarry(BlockEntityQuantumQuarry.class, 8),
    modulator(BlockEntityModuleMachine.class, 9),
    adv_gen(BlockEntityGeneratorAdv.class, 10),
    imp_gen(BlockEntityGeneratorImp.class, 11),
    per_gen(BlockEntityGeneratorPer.class, 12),
    adv_quantum_quarry(BlockEntityAdvQuantumQuarry.class, 13),
    imp_quantum_quarry(BlockEntityImpQuantumQuarry.class, 14),
    per_quantum_quarry(BlockEntityPerQuantumQuarry.class, 15),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockBaseMachineEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        return "basemachine";
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

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0F;
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
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }
}
