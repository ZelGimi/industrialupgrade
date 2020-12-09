package com.denfop.ssp.tiles;

import com.denfop.ssp.tiles.Moonpanel.*;
import com.denfop.ssp.tiles.Sunpanel.*;
import com.denfop.ssp.tiles.Transformer.TileEntityTransformerEV;
import com.denfop.ssp.tiles.Transformer.TileEntityTransformerEV1;
import com.denfop.ssp.tiles.Transformer.TileEntityTransformerEV2;
import com.denfop.ssp.tiles.Transformer.TileEntityTransformerEV3;
import com.denfop.ssp.tiles.airpanel.*;
import com.denfop.ssp.tiles.earthpanel.*;
import com.denfop.ssp.tiles.neutronfabricator.TileEntityMassFabricator;
import com.denfop.ssp.tiles.overtimepanel.*;
import com.denfop.ssp.tiles.rainpanels.*;
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

import java.util.Set;

public enum SSPBlock implements ITeBlock {

	advanced_solar_panel(TileEntityAdvancedSolar.class, 1),
	hybrid_solar_panel(TileEntityHybridSolar.class, 2, EnumRarity.RARE),
	ultimate_solar_panel(TileEntityUltimateHybridSolar.class, 3, EnumRarity.EPIC),
	quantum_solar_panel(TileEntityQuantumSolar.class, 4, EnumRarity.EPIC),
	spectral_solar_panel(TileEntitySpectral.class, 5),
	singular_solar_panel(TileEntitySingular.class, 6, EnumRarity.RARE),
	admin_solar_panel(TileEntityAdmin.class, 7, EnumRarity.EPIC),
	photonic_solar_panel(TileEntityphotonic.class, 8, EnumRarity.EPIC),
	neutronium_solar_panel(TileEntityneutron.class, 9, EnumRarity.EPIC),
	nutronfabricator(TileEntityMassFabricator.class, 10),
	proton_solar_panel(TileEntityproton.class, 11, EnumRarity.EPIC),
	transformator(TileEntityTransformerEV.class, 12),
	transformator1(TileEntityTransformerEV1.class, 13),
	transformator2(TileEntityTransformerEV2.class, 14),
	transformator3(TileEntityTransformerEV3.class, 15),
	advanced_solar_panelsun(TileEntityAdvancedSolarsun.class, 16),
	hybrid_solar_panelsun(TileEntityHybridSolarsun.class, 17, EnumRarity.RARE),
	ultimate_solar_panelsun(TileEntityUltimateHybridSolarsun.class, 18, EnumRarity.EPIC),
	quantum_solar_panelsun(TileEntityQuantumSolarsun.class, 19, EnumRarity.EPIC),
	spectral_solar_panelsun(TileEntitySpectralsun.class, 20),
	proton_solar_panelsun(TileEntityprotonsun.class, 50),
	singular_solar_panelsun(TileEntitySingularsun.class, 21, EnumRarity.RARE),
	admin_solar_panelsun(TileEntityAdminsun.class, 22, EnumRarity.EPIC),
	photonic_solar_panelsun(TileEntityphotonicsun.class, 23, EnumRarity.EPIC),
	neutronium_solar_panelsun(TileEntityneutronsun.class, 24, EnumRarity.EPIC),
	advanced_solar_panelmoon(TileEntityAdvancedSolarmoon.class, 26),
	hybrid_solar_panelmoon(TileEntityHybridSolarmoon.class, 27, EnumRarity.RARE),
	ultimate_solar_panelmoon(TileEntityUltimateHybridSolarmoon.class, 28, EnumRarity.EPIC),
	quantum_solar_panelmoon(TileEntityQuantumSolarmoon.class, 29, EnumRarity.EPIC),
	spectral_solar_panelmoon(TileEntitySpectralmoon.class, 30),
	singular_solar_panelmoon(TileEntitySingularmoon.class, 31, EnumRarity.RARE),
	admin_solar_panelmoon(TileEntityAdminmoon.class, 32, EnumRarity.EPIC),
	photonic_solar_panelmoon(TileEntityphotonicmoon.class, 33, EnumRarity.EPIC),
	neutronium_solar_panelmoon(TileEntityneutronmoon.class, 34, EnumRarity.EPIC),
	proton_solar_panelmoon(TileEntityprotonmoon.class, 48, EnumRarity.EPIC),
	//

	proton_solar_panelrain(TileEntityprotonrain.class, 49, EnumRarity.EPIC),
	molecular_transformer(TileEntityMolecularAssembler.class, 25, EnumRarity.RARE),
	advanced_solar_panelrain(TileEntityAdvancedSolarrain.class, 36),
	hybrid_solar_panelrain(TileEntityHybridSolarrain.class, 37, EnumRarity.RARE),
	ultimate_solar_panelrain(TileEntityUltimateHybridSolarrain.class, 38, EnumRarity.EPIC),
	quantum_solar_panelrain(TileEntityQuantumSolarrain.class, 39, EnumRarity.EPIC),
	spectral_solar_panelrain(TileEntitySpectralrain.class, 40),
	singular_solar_panelrain(TileEntitySingularrain.class, 41, EnumRarity.RARE),
	admin_solar_panelrain(TileEntityAdminrain.class, 42, EnumRarity.EPIC),
	photonic_solar_panelrain(TileEntityphotonicrain.class, 43, EnumRarity.EPIC),
	neutronium_solar_panelrain(TileEntityneutronrain.class, 44, EnumRarity.EPIC),
	advancedmfsu(advancedmfsu.class, 45, EnumRarity.EPIC),
	ultimatemfsu(ultimatemfsu.class, 46, EnumRarity.EPIC),
	quantummfsu(quantummfsu.class, 47, EnumRarity.EPIC),
	//
	adminair(TileEntityAdminair.class, 70, EnumRarity.EPIC),
	advancedsolarair(TileEntityAdvancedSolarair.class, 51, EnumRarity.EPIC),
	hybridsolarair(TileEntityHybridSolarair.class, 52, EnumRarity.EPIC),
	neutronair(TileEntityneutronair.class, 53, EnumRarity.EPIC),
	photonicair(TileEntityphotonicair.class, 54, EnumRarity.EPIC),
	protonair(TileEntityprotonair.class, 55, EnumRarity.EPIC),
	quantumsolarair(TileEntityQuantumSolarair.class, 56, EnumRarity.EPIC),
	singularair(TileEntitySingularair.class, 57, EnumRarity.EPIC),
	spectralair(TileEntitySpectralair.class, 58, EnumRarity.EPIC),
	ultimatehybridsolarair(TileEntityUltimateHybridSolarair.class, 59, EnumRarity.EPIC),
	adminearth(TileEntityAdminearth.class, 60, EnumRarity.EPIC),
	advancedsolarearth(TileEntityAdvancedSolarearth.class, 61, EnumRarity.EPIC),
	hybridsolarearth(TileEntityHybridSolarearth.class, 62, EnumRarity.EPIC),
	neutronearth(TileEntityneutronearth.class, 63, EnumRarity.EPIC),
	photonicearth(TileEntityphotonicearth.class, 64, EnumRarity.EPIC),
	protonearth(TileEntityprotonearth.class, 65, EnumRarity.EPIC),
	quantumsolarearth(TileEntityQuantumSolarearth.class, 66, EnumRarity.EPIC),
	singularearth(TileEntitySingularearth.class, 67, EnumRarity.EPIC),
	spectralearth(TileEntitySpectralearth.class, 68, EnumRarity.EPIC),
	ultimatehybridsolarearth(TileEntityUltimateHybridSolarearth.class, 69, EnumRarity.EPIC);
	public static final ResourceLocation IDENTITY;
	private static final SSPBlock[] VALUES;

	static {
		VALUES = values();
		IDENTITY = new ResourceLocation("super_solar_panels", "machines");
	}

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
		GameRegistry.registerTileEntity(teClass, "super_solar_panels:" + this.getName());


	}

	public String getName() {
		return this.name();
	}

	public int getId() {
		return this.itemMeta;
	}

	public static void buildDummies() {
		final ModContainer mc = Loader.instance().activeModContainer();
		if (mc == null || !"super_solar_panels".equals(mc.getModId())) {
			throw new IllegalAccessError("Don't mess with this please.");
		}
		for (final SSPBlock block : SSPBlock.VALUES) {
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

	public ResourceLocation getIdentifier() {
		return SSPBlock.IDENTITY;
	}

	public boolean hasItem() {
		return true;
	}

	public Class<? extends TileEntityBlock> getTeClass() {
		return this.teClass;
	}

	@Override
	public boolean hasActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<EnumFacing> getSupportedFacings() {
		return Util.horizontalFacings;
	}

	public float getHardness() {
		return 3.0f;
	}

	public float getExplosionResistance() {
		return 0.0f;
	}

	public TeBlock.HarvestTool getHarvestTool() {
		return TeBlock.HarvestTool.Pickaxe;
	}

	public TeBlock.DefaultDrop getDefaultDrop() {
		return TeBlock.DefaultDrop.Self;
	}

	public EnumRarity getRarity() {
		return this.rarity;
	}

	public boolean allowWrenchRotating() {
		return false;
	}

	public TileEntityBlock getDummyTe() {
		return this.dummyTe;
	}
}
