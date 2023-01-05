package com.denfop.blocks.mechanism;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.tiles.panels.overtime.TileEntityAdvancedSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityBarionSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityDiffractionSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityGravitonSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityHadronSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityHybridSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityNeutronSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityPhotonicSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityProtonSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityQuantumSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityQuarkSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntitySingularSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntitySpectralSolarPanel;
import com.denfop.tiles.panels.overtime.TileEntityUltimateSolarPanel;
import ic2.core.block.ITeBlock;
import ic2.core.block.TileEntityBlock;
import ic2.core.ref.TeBlock;
import ic2.core.util.Util;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Set;

public enum BlockSolarPanels implements ITeBlock {

    advanced_solar_paneliu(TileEntityAdvancedSolarPanel.class, 0, EnumRarity.RARE),
    hybrid_solar_paneliu(TileEntityHybridSolarPanel.class, 1, EnumRarity.RARE),
    ultimate_solar_paneliu(TileEntityUltimateSolarPanel.class, 2, EnumRarity.EPIC),
    quantum_solar_paneliu(TileEntityQuantumSolarPanel.class, 3, EnumRarity.EPIC),
    spectral_solar_panel(TileEntitySpectralSolarPanel.class, 4, EnumRarity.EPIC),
    proton_solar_panel(TileEntityProtonSolarPanel.class, 5, EnumRarity.EPIC),
    singular_solar_panel(TileEntitySingularSolarPanel.class, 6, EnumRarity.RARE),
    admin_solar_panel(TileEntityDiffractionSolarPanel.class, 7, EnumRarity.EPIC),
    photonic_solar_panel(TileEntityPhotonicSolarPanel.class, 8, EnumRarity.EPIC),
    neutronium_solar_panel(TileEntityNeutronSolarPanel.class, 9, EnumRarity.EPIC),
    barion_solar_panel(TileEntityBarionSolarPanel.class, 10, EnumRarity.EPIC),
    hadron_solar_panel(TileEntityHadronSolarPanel.class, 11, EnumRarity.EPIC),
    graviton_solar_panel(TileEntityGravitonSolarPanel.class, 12, EnumRarity.EPIC),
    quark_solar_panel(TileEntityQuarkSolarPanel.class, 13, EnumRarity.EPIC),


    ;


    //
    public static final ResourceLocation IDENTITY = IUCore.getIdentifier("machines");

    private final Class<? extends TileEntityBlock> teClass;
    private final int itemMeta;
    private final EnumRarity rarity;
    private TileEntityBlock dummyTe;


    BlockSolarPanels(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
        this(teClass, itemMeta, EnumRarity.UNCOMMON);

    }

    BlockSolarPanels(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
        this.teClass = teClass;
        this.itemMeta = itemMeta;
        this.rarity = rarity;


        GameRegistry.registerTileEntity(teClass, IUCore.getIdentifier(this.getName()));


    }

    public static BlockSolarPanels getFromID(final int ID) {
        return values()[ID % values().length];
    }

    public static void buildDummies() {
        final ModContainer mc = Loader.instance().activeModContainer();
        if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
            throw new IllegalAccessError("Don't mess with this please.");
        }
        for (final BlockSolarPanels block : values()) {
            if (block.teClass != null) {
                try {
                    block.dummyTe = block.teClass.newInstance();
                } catch (Exception e) {
                    if (Util.inDev()) {
                        e.printStackTrace();
                    }
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
        return Util.horizontalFacings;
    }

    @Override
    public float getHardness() {
        return 3.0f;
    }

    @Override
    public float getExplosionResistance() {
        return 0.0f;
    }

    @Override
    @Nonnull
    public TeBlock.HarvestTool getHarvestTool() {
        return TeBlock.HarvestTool.Wrench;
    }

    @Override
    @Nonnull
    public TeBlock.DefaultDrop getDefaultDrop() {
        return TeBlock.DefaultDrop.Self;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity() {
        return this.rarity;
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
