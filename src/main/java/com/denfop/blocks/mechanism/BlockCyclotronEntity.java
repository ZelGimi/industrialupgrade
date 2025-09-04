package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.cyclotron.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockCyclotronEntity implements MultiBlockEntity {

    cyclotron_controller(BlockEntityCyclotronController.class, 0),
    cyclotron_positrons(BlockEntityCyclotronPositrons.class, 1),
    cyclotron_cryogen(BlockEntityCyclotronCryogen.class, 2),
    cyclotron_coolant(BlockEntityCyclotronCoolant.class, 3),
    cyclotron_bombardment_chamber(BlockEntityCyclotronChamber.class, 4),
    cyclotron_quantum(BlockEntityCyclotronQuantum.class, 5),
    cyclotron_casing(BlockEntityCyclotronCasing.class, 6),
    cyclotron_particle_accelerator(BlockEntityCyclotronParticleAccelerator.class, 7),
    cyclotron_electrostatic_deflector(BlockEntityCyclotronElectrostaticDeflector.class, 8),
    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockCyclotronEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta) {
        this(teClass, itemMeta, Rarity.UNCOMMON);

    }

    ;

    BlockCyclotronEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    public int getIDBlock() {
        return idBlock;
    }

    public void setIdBlock(int id) {
        idBlock = id;
    }

    @Override
    public MapColor getMaterial() {
        return MapColor.METAL;
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
        return "cyclotron";
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
        return DefaultDrop.Self;
    }

    @Override
    public boolean allowWrenchRotating() {
        return false;
    }

    @Override
    public BlockEntityBase getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String[] getMultiModels(final MultiBlockEntity teBlock) {
        return MultiBlockEntity.super.getMultiModels(teBlock);
    }
}
