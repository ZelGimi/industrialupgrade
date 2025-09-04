package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.creative.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockCreativeBlocksEntity implements MultiBlockEntity {
    creative_mfsu(BlockEntityCreativeMFSU.class, 0),
    creative_steam_storage(BlockEntityCreativeSteamStorage.class, 1),
    creative_solarium_storage(BlockEntityCreativeSolariumStorage.class, 2),
    creative_quantum_storage(BlockEntityCreativeQuantumStorage.class, 3),
    creative_radiation_storage(BlockEntityCreativeRadiationStorage.class, 4),
    creative_tank_storage(BlockEntityCreativeTank.class, 5),

    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    BlockCreativeBlocksEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockCreativeBlocksEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


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
    public void setType(RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockEntityType) {
        this.blockType = blockEntityType;
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
    }

    @Override
    public String getMainPath() {
        return "creative_blocks";
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
        return false;
    }

    @Override
    @Nonnull
    public Set<Direction> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 1.0f;
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
