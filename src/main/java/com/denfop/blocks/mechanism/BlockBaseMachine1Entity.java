package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.item.MultiBlockItem;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.mechanism.BlockEntityHandlerHeavyOre;
import com.denfop.blockentity.mechanism.BlockEntityMagnet;
import com.denfop.blockentity.mechanism.BlockEntityMagnetGenerator;
import com.denfop.blockentity.mechanism.BlockEntityWitherMaker;
import com.denfop.blockentity.mechanism.dual.BlockEntityEnrichment;
import com.denfop.blockentity.mechanism.dual.BlockEntitySynthesis;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityAdvGeoGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityImpGeoGenerator;
import com.denfop.blockentity.mechanism.generator.energy.fluid.BlockEntityPerGeoGenerator;
import com.denfop.blockentity.mechanism.triple.heat.BlockEntityAdvAlloySmelter;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockBaseMachine1Entity implements MultiBlockEntity, MultiBlockItem {

    adv_alloy_smelter(BlockEntityAdvAlloySmelter.class, 3),
    adv_geo(BlockEntityAdvGeoGenerator.class, 4),
    imp_geo(BlockEntityImpGeoGenerator.class, 5),
    per_geo(BlockEntityPerGeoGenerator.class, 6),
    enrichment(BlockEntityEnrichment.class, 10),
    synthesis(BlockEntitySynthesis.class, 11),
    handler_ho(BlockEntityHandlerHeavyOre.class, 12),
    gen_wither(BlockEntityWitherMaker.class, 13),
    magnet(BlockEntityMagnet.class, 14),
    magnet_generator(BlockEntityMagnetGenerator.class, 15),

    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockBaseMachine1Entity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
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
        return "basemachine1";
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

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

}
