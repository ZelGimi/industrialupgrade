package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.storage.*;
import com.denfop.blockentity.storage.processor.BlockEntityAdvProcessor;
import com.denfop.blockentity.storage.processor.BlockEntityImpProcessor;
import com.denfop.blockentity.storage.processor.BlockEntityPerProcessor;
import com.denfop.blockentity.storage.processor.BlockEntitySimpleProcessor;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockStorageSystemEntity implements MultiBlockEntity {
    storage_cells(BlockEntityEntityStorageCells.class, 0),
    controller(BlockEntityController.class, 1),
    interface_workbench(BlockEntityInterfaceWorkbench.class, 2),
    interface_entity(BlockEntityInterface.class, 3),
    importbus(BlockEntityImportBus.class, 4),
    exportbus(BlockEntityExportBus.class, 5),
    fluid_importbus(BlockEntityFluidImportBus.class, 6),
    fluid_exportbus(BlockEntityFluidExportBus.class, 7),
    processor(BlockEntitySimpleProcessor.class, 8),
    adv_processor(BlockEntityAdvProcessor.class, 9),
    imp_processor(BlockEntityImpProcessor.class, 10),
    per_processor(BlockEntityPerProcessor.class, 11),
    monitor(BlockEntityMonitor.class, 14),
    pattern_monitor(BlockEntityPatternMonitor.class, 15),
    fluid_monitor(BlockEntityFluidMonitor.class, 16),
    precraft(BlockEntityPreCraft.class, 12),
    autocraft_monitor(BlockEntityAutoCraftMonitor.class, 13),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockStorageSystemEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockStorageSystemEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    @Override
    public MapColor getMaterial() {
        if (this.itemMeta >= 4 && itemMeta <= 7)
            return MultiBlockEntity.CABLE;
        return MultiBlockEntity.super.getMaterial();
    }

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
        return "storagesystem";
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

        return true;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        if (this.itemMeta >= 4 && itemMeta <= 7)
            return ModUtils.allFacings;
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
        return DefaultDrop.Self;
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
