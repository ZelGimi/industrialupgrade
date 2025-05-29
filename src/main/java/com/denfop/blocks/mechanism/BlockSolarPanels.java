package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.state.DefaultDrop;
import com.denfop.blocks.state.HarvestTool;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.panels.overtime.*;
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

public enum BlockSolarPanels implements IMultiTileBlock {

    advanced_solar_paneliu(TileAdvancedSolarPanel.class, 0, Rarity.RARE),
    hybrid_solar_paneliu(TileHybridSolarPanel.class, 1, Rarity.RARE),
    ultimate_solar_paneliu(TileUltimateSolarPanel.class, 2, Rarity.EPIC),
    quantum_solar_paneliu(TileQuantumSolarPanel.class, 3, Rarity.EPIC),
    spectral_solar_panel(TileSpectralSolarPanel.class, 4, Rarity.EPIC),
    proton_solar_panel(TileProtonSolarPanel.class, 5, Rarity.EPIC),
    singular_solar_panel(TileSingularSolarPanel.class, 6, Rarity.RARE),
    admin_solar_panel(TileDiffractionSolarPanel.class, 7, Rarity.EPIC),
    photonic_solar_panel(TilePhotonicSolarPanel.class, 8, Rarity.EPIC),
    neutronium_solar_panel(TileNeutronSolarPanel.class, 9, Rarity.EPIC),
    barion_solar_panel(TileBarionSolarPanel.class, 10, Rarity.EPIC),
    hadron_solar_panel(TileHadronSolarPanel.class, 11, Rarity.EPIC),
    graviton_solar_panel(TileGravitonSolarPanel.class, 12, Rarity.EPIC),
    quark_solar_panel(TileQuarkSolarPanel.class, 13, Rarity.EPIC),


    ;


    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final Rarity rarity;
    int idBlock;
    private TileEntityBlock dummyTe;
    private BlockState defaultState;
    private RegistryObject<BlockEntityType<? extends TileEntityBlock>> blockType;

    ;

    BlockSolarPanels(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final Rarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


    }

    ;

    public static BlockSolarPanels getFromID(final int ID) {
        return values()[ID % values().length];
    }

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
        if (this.teClass != null) {
            try {
                this.dummyTe = (TileEntityBlock) this.teClass.getConstructors()[0].newInstance(BlockPos.ZERO, defaultState);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public BlockEntityType<? extends TileEntityBlock> getBlockType() {
        return this.blockType.get();
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
    public Class<? extends TileEntityBlock> getTeClass() {
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
    public TileEntityBlock getDummyTe() {
        return this.dummyTe;
    }

    @Override
    public String[] getMultiModels(final IMultiTileBlock teBlock) {
        return new String[]{"aer", "earth", "nether", "end", "night", "sun", "rain"};
    }
}
