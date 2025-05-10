package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.base.TileNeutronGenerator;
import com.denfop.tiles.mechanism.TileGenerationMicrochip;
import com.denfop.tiles.mechanism.TileGenerationStone;
import com.denfop.tiles.mechanism.TileModuleMachine;
import com.denfop.tiles.mechanism.dual.heat.TileAlloySmelter;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorAdv;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorImp;
import com.denfop.tiles.mechanism.generator.energy.coal.TileEntityGeneratorPer;
import com.denfop.tiles.mechanism.generator.things.matter.TileAdvancedMatter;
import com.denfop.tiles.mechanism.generator.things.matter.TileImprovedMatter;
import com.denfop.tiles.mechanism.generator.things.matter.TileUltimateMatter;
import com.denfop.tiles.mechanism.quarry.TileAdvQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileImpQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TilePerQuantumQuarry;
import com.denfop.tiles.mechanism.quarry.TileQuantumQuarry;
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

public enum BlockBaseMachine implements IMultiTileBlock {

    adv_matter(TileAdvancedMatter.class, 1),
    imp_matter(TileImprovedMatter.class, 2),
    per_matter(TileUltimateMatter.class, 3),
    alloy_smelter(TileAlloySmelter.class, 4),
    neutron_generator(TileNeutronGenerator.class, 5),
    generator_microchip(TileGenerationMicrochip.class, 6),
    gen_stone(TileGenerationStone.class, 7),
    quantum_quarry(TileQuantumQuarry.class, 8),
    modulator(TileModuleMachine.class, 9),
    adv_gen(TileEntityGeneratorAdv.class, 10),
    imp_gen(TileEntityGeneratorImp.class, 11),
    per_gen(TileEntityGeneratorPer.class, 12),
    adv_quantum_quarry(TileAdvQuantumQuarry.class, 13),
    imp_quantum_quarry(TileImpQuantumQuarry.class, 14),
    per_quantum_quarry(TilePerQuantumQuarry.class, 15),
    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockBaseMachine(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }
}
