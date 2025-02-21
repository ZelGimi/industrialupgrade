package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.MultiTileBlock;
import com.denfop.tiles.base.TileEntityBlock;
import com.denfop.tiles.panels.overtime.TileAdvancedSolarPanel;
import com.denfop.tiles.panels.overtime.TileBarionSolarPanel;
import com.denfop.tiles.panels.overtime.TileDiffractionSolarPanel;
import com.denfop.tiles.panels.overtime.TileGravitonSolarPanel;
import com.denfop.tiles.panels.overtime.TileHadronSolarPanel;
import com.denfop.tiles.panels.overtime.TileHybridSolarPanel;
import com.denfop.tiles.panels.overtime.TileNeutronSolarPanel;
import com.denfop.tiles.panels.overtime.TilePhotonicSolarPanel;
import com.denfop.tiles.panels.overtime.TileProtonSolarPanel;
import com.denfop.tiles.panels.overtime.TileQuantumSolarPanel;
import com.denfop.tiles.panels.overtime.TileQuarkSolarPanel;
import com.denfop.tiles.panels.overtime.TileSingularSolarPanel;
import com.denfop.tiles.panels.overtime.TileSpectralSolarPanel;
import com.denfop.tiles.panels.overtime.TileUltimateSolarPanel;
import com.denfop.utils.ModUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockSolarPanels implements IMultiTileBlock {

    advanced_solar_paneliu(TileAdvancedSolarPanel.class, 0, EnumRarity.RARE),
    hybrid_solar_paneliu(TileHybridSolarPanel.class, 1, EnumRarity.RARE),
    ultimate_solar_paneliu(TileUltimateSolarPanel.class, 2, EnumRarity.EPIC),
    quantum_solar_paneliu(TileQuantumSolarPanel.class, 3, EnumRarity.EPIC),
    spectral_solar_panel(TileSpectralSolarPanel.class, 4, EnumRarity.EPIC),
    proton_solar_panel(TileProtonSolarPanel.class, 5, EnumRarity.EPIC),
    singular_solar_panel(TileSingularSolarPanel.class, 6, EnumRarity.RARE),
    admin_solar_panel(TileDiffractionSolarPanel.class, 7, EnumRarity.EPIC),
    photonic_solar_panel(TilePhotonicSolarPanel.class, 8, EnumRarity.EPIC),
    neutronium_solar_panel(TileNeutronSolarPanel.class, 9, EnumRarity.EPIC),
    barion_solar_panel(TileBarionSolarPanel.class, 10, EnumRarity.EPIC),
    hadron_solar_panel(TileHadronSolarPanel.class, 11, EnumRarity.EPIC),
    graviton_solar_panel(TileGravitonSolarPanel.class, 12, EnumRarity.EPIC),
    quark_solar_panel(TileQuarkSolarPanel.class, 13, EnumRarity.EPIC),


    ;


    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("machines");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockSolarPanels(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }
    int idBlock;
    public  int getIDBlock(){
        return idBlock;
    };

    public void setIdBlock(int id){
        idBlock = id;
    };
    public static BlockSolarPanels getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockSolarPanels block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {

                }
            }
        }
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
    @Nonnull
    public ResourceLocation getIdentifier() {
        return IDENTITY;
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
    public Set<EnumFacing> getSupportedFacings() {
        return ModUtils.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    @Nonnull
    public MultiTileBlock.HarvestTool getHarvestTool() {
        return MultiTileBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public MultiTileBlock.DefaultDrop getDefaultDrop() {
        return MultiTileBlock.DefaultDrop.Self;
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
