package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.IMultiBlockItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.mechanism.TileHandlerHeavyOre;
import com.denfop.tiles.mechanism.TileMagnet;
import com.denfop.tiles.mechanism.TileMagnetGenerator;
import com.denfop.tiles.mechanism.TileWitherMaker;
import com.denfop.tiles.mechanism.dual.TileEnrichment;
import com.denfop.tiles.mechanism.dual.TileSynthesis;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityAdvGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityImpGeoGenerator;
import com.denfop.tiles.mechanism.generator.energy.fluid.TileEntityPerGeoGenerator;
import com.denfop.tiles.mechanism.triple.heat.TileAdvAlloySmelter;
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

public enum BlockBaseMachine1 implements IMultiTileBlock, IMultiBlockItem {

    adv_alloy_smelter(TileAdvAlloySmelter.class, 3),
    adv_geo(TileEntityAdvGeoGenerator.class, 4),
    imp_geo(TileEntityImpGeoGenerator.class, 5),
    per_geo(TileEntityPerGeoGenerator.class, 6),
    enrichment(TileEnrichment.class, 10),
    synthesis(TileSynthesis.class, 11),
    handler_ho(TileHandlerHeavyOre.class, 12),
    gen_wither(TileWitherMaker.class, 13),
    magnet(TileMagnet.class, 14),
    magnet_generator(TileMagnetGenerator.class, 15),

    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockBaseMachine1(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
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

    @Override
    public boolean hasUniqueRender(final ItemStack itemStack) {
        return false;
    }

}
