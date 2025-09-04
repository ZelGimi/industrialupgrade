package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blockentity.panels.overtime.*;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.DeferredHolder;

import javax.annotation.Nonnull;
import java.util.Set;

;

public enum BlockSolarPanelsEntity implements MultiBlockEntity {

    advanced_solar_paneliu(BlockEntityAdvancedSolarPanel.class, 0, Rarity.RARE),
    hybrid_solar_paneliu(BlockEntityHybridSolarPanel.class, 1, Rarity.RARE),
    ultimate_solar_paneliu(BlockEntityUltimateSolarPanel.class, 2, Rarity.EPIC),
    quantum_solar_paneliu(BlockEntityQuantumSolarPanel.class, 3, Rarity.EPIC),
    spectral_solar_panel(BlockEntitySpectralSolarPanel.class, 4, Rarity.EPIC),
    proton_solar_panel(BlockEntityProtonSolarPanel.class, 5, Rarity.EPIC),
    singular_solar_panel(BlockEntitySingularSolarPanel.class, 6, Rarity.RARE),
    admin_solar_panel(BlockEntityDiffractionSolarPanel.class, 7, Rarity.EPIC),
    photonic_solar_panel(BlockEntityPhotonicSolarPanel.class, 8, Rarity.EPIC),
    neutronium_solar_panel(BlockEntityNeutronSolarPanel.class, 9, Rarity.EPIC),
    barion_solar_panel(BlockEntityBarionSolarPanel.class, 10, Rarity.EPIC),
    hadron_solar_panel(BlockEntityHadronSolarPanel.class, 11, Rarity.EPIC),
    graviton_solar_panel(BlockEntityGravitonSolarPanel.class, 12, Rarity.EPIC),
    quark_solar_panel(BlockEntityQuarkSolarPanel.class, 13, Rarity.EPIC),


    ;


    private final Class<? extends BlockEntityBase> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private BlockEntityBase dummyTe;
    private BlockState defaultState;
    private DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BlockEntityBase>> blockType;

    ;

    BlockSolarPanelsEntity(final Class<? extends BlockEntityBase> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    ;

    public static BlockSolarPanelsEntity getFromID(final int ID) {
        return values()[ID % values().length];
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
        if (this.teClass != null) {
            try {
                this.dummyTe = (BlockEntityBase) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public BlockEntityType<? extends BlockEntityBase> getBlockType() {
        return this.blockType.get();
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
    public String getMainPath() {
        return "machines";
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
        return false;
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

    @Override
    public String[] getMultiModels(final MultiBlockEntity teBlock) {
        return new String[]{"aer", "earth", "nether", "end", "night", "sun", "rain"};
    }
}
