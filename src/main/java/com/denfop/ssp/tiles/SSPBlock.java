package com.denfop.ssp.tiles;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.common.Constants;
import com.denfop.ssp.molecular.TileEntityMolecularAssembler;
import com.denfop.ssp.tiles.mfsu.AdvancedMFSU;
import com.denfop.ssp.tiles.mfsu.QuantumMFSU;
import com.denfop.ssp.tiles.mfsu.UltimateMFSU;
import com.denfop.ssp.tiles.neutronfabricator.TileEntityMassFabricator;
import com.denfop.ssp.tiles.panels.overtime.*;
import com.denfop.ssp.tiles.panels.rain.*;
import com.denfop.ssp.tiles.panels.sun.*;
import com.denfop.ssp.tiles.transformer.TileEntityTransformerEV;
import com.denfop.ssp.tiles.transformer.TileEntityTransformerEV1;
import com.denfop.ssp.tiles.transformer.TileEntityTransformerEV2;
import com.denfop.ssp.tiles.transformer.TileEntityTransformerEV3;
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

public enum SSPBlock implements ITeBlock {

	advanced_solar_panel(TileEntityAdvancedSolar.class, 1),
	hybrid_solar_panel(TileEntityHybridSolar.class, 2, EnumRarity.RARE),
	ultimate_solar_panel(TileEntityUltimateSolar.class, 3, EnumRarity.EPIC),
	quantum_solar_panel(TileEntityQuantumSolar.class, 4, EnumRarity.EPIC),
	spectral_solar_panel(TileEntitySpectralSolar.class, 5),
	proton_solar_panel(TileEntityProtonSolar.class, 11, EnumRarity.EPIC),
	singular_solar_panel(TileEntitySingularSolar.class, 6, EnumRarity.RARE),
	admin_solar_panel(TileEntityLightAdsorbSolar.class, 7, EnumRarity.EPIC),
	photonic_solar_panel(TileEntityPhotonicSolar.class, 8, EnumRarity.EPIC),
	neutronium_solar_panel(TileEntityNeutronSolar.class, 9, EnumRarity.EPIC),
	nutronfabricator(TileEntityMassFabricator.class, 10),
	transformator(TileEntityTransformerEV.class, 12),
	transformator1(TileEntityTransformerEV1.class, 13),
	transformator2(TileEntityTransformerEV2.class, 14),
	transformator3(TileEntityTransformerEV3.class, 15),
	advanced_solar_panelsun(TileEntityAdvancedSolarSun.class, 16),
	hybrid_solar_panelsun(TileEntityHybridSolarSun.class, 17, EnumRarity.RARE),
	ultimate_solar_panelsun(TileEntityUltimateSolarSun.class, 18, EnumRarity.EPIC),
	quantum_solar_panelsun(TileEntityQuantumSolarSun.class, 19, EnumRarity.EPIC),
	spectral_solar_panelsun(TileEntitySpectralSun.class, 20),
	proton_solar_panelsun(TileEntityProtonSun.class, 50),
	singular_solar_panelsun(TileEntitySingularSun.class, 21, EnumRarity.RARE),
	admin_solar_panelsun(TileEntityLightAdsorbSun.class, 22, EnumRarity.EPIC),
	photonic_solar_panelsun(TileEntityPhotonicSun.class, 23, EnumRarity.EPIC),
	neutronium_solar_panelsun(TileEntityNeutronSun.class, 24, EnumRarity.EPIC),
	//

	molecular_transformer(TileEntityMolecularAssembler.class, 25, EnumRarity.RARE),
	advanced_solar_panelrain(TileEntityAdvancedSolarRain.class, 36),
	hybrid_solar_panelrain(TileEntityHybridSolarRain.class, 37, EnumRarity.RARE),
	ultimate_solar_panelrain(TileEntityUltimateSolarRain.class, 38, EnumRarity.EPIC),
	quantum_solar_panelrain(TileEntityQuantumSolarRain.class, 39, EnumRarity.EPIC),
	spectral_solar_panelrain(TileEntitySpectralRain.class, 40),
	proton_solar_panelrain(TileEntityProtonRain.class, 49, EnumRarity.EPIC),
	singular_solar_panelrain(TileEntitySingularRain.class, 41, EnumRarity.RARE),
	admin_solar_panelrain(TileEntityLightAdsorbRain.class, 42, EnumRarity.EPIC),
	photonic_solar_panelrain(TileEntityPhotonicRain.class, 43, EnumRarity.EPIC),
	neutronium_solar_panelrain(TileEntityNeutronRain.class, 44, EnumRarity.EPIC),
	advancedmfsu(AdvancedMFSU.class, 45, EnumRarity.EPIC),
	ultimatemfsu(UltimateMFSU.class, 46, EnumRarity.EPIC),
	quantummfsu(QuantumMFSU.class, 47, EnumRarity.EPIC);

	//
	public static final ResourceLocation IDENTITY = SuperSolarPanels.getIdentifier("machines");

	private final Class<? extends TileEntityBlock> teClass;
	private final int itemMeta;
	private final EnumRarity rarity;
	private TileEntityBlock dummyTe;


	SSPBlock(final Class<? extends TileEntityBlock> teClass, final int itemMeta) {
		this(teClass, itemMeta, EnumRarity.UNCOMMON);
	}

	SSPBlock(final Class<? extends TileEntityBlock> teClass, final int itemMeta, final EnumRarity rarity) {
		this.teClass = teClass;
		this.itemMeta = itemMeta;
		this.rarity = rarity;
		GameRegistry.registerTileEntity(teClass, SuperSolarPanels.getIdentifier(this.getName()));
	}

	@Override
	public String getName() {
		return this.name();
	}

	@Override
	public int getId() {
		return this.itemMeta;
	}

	public static void buildDummies() {
		final ModContainer mc = Loader.instance().activeModContainer();
		if (mc == null || !Constants.MOD_ID.equals(mc.getModId())) {
			throw new IllegalAccessError("Don't mess with this please.");
		}
		for (final SSPBlock block : SSPBlock.values()) {
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
	@Nonnull
	public ResourceLocation getIdentifier() {
		return SSPBlock.IDENTITY;
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
		return TeBlock.HarvestTool.Pickaxe;
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
		return false;
	}

	@Override
	public TileEntityBlock getDummyTe() {
		return this.dummyTe;
	}
}
