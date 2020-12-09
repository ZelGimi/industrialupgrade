package com.denfop.ssp.items.resource;

import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class CraftingThings extends ItemMulti {
	protected static final String NAME = "crafting";

	public CraftingThings() {
		super(null, CraftingTypes.class);
		BlocksItems.registerItem((Item) this, new ResourceLocation("super_solar_panels", "crafting")).setUnlocalizedName("crafting");
	}

	@SideOnly(Side.CLIENT)
	protected void registerModel(final int meta, final ItemName name, final String extraName) {
		ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation("super_solar_panels:crafting/" + CraftingTypes.getFromID(meta).getName(), null));
	}

	public String func_77658_a() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public enum CraftingTypes implements IIdProvider {
		enderquantumcomponent(0),
		SUNNARIUM(1),
		SUNNARIUM_PART(2),
		SUNNARIUM_ALLOY(3),
		IRRADIANT_URANIUM(4),
		ENRICHED_SUNNARIUM(5),
		ENRICHED_SUNNARIUM_ALLOY(6),
		IRRADIANT_GLASS_PANE(7),
		IRIDIUM_IRON_PLATE(8),
		REINFORCED_IRIDIUM_IRON_PLATE(9),
		IRRADIANT_REINFORCED_PLATE(10),
		IRIDIUM_INGOT(11),
		URANIUM_INGOT(12),
		QUANTUM_CORE(13),
		advanced_core(14),
		hybrid_core(15),
		ultimate_core(16),
		solarsplitter(17),
		bluecomponent(18),
		greencomponent(19),
		redcomponent(20),
		singularcore(21),
		spectralcore(22),
		quantumitems6(23),
		dust(24),
		protoncore(25),
		protonshard(26),
		proton(27),
		neutronshard(28),
		neutron(29),
		neutroncore(30),
		EnrichedSunnariumAlloy2(31),
		EnrichedSunnariumAlloy3(32),
		EnrichedSunnariumAlloy4(33),
		quantcore1(34),
		quantcore2(35),
		nanobox(36),
		depleted_proton_fuel_rod(37),
		depleted_dual_proton_fuel_rod(38),
		depleted_quad_proton_fuel_rod(39),
		depleted_eit_proton_fuel_rod(40),
		QuantumItems2(41),
		QuantumItems3(42),
		QuantumItems4(43),
		QuantumItems5(44),
		compresscarbon(45),
		coal_chunk(46),
		compresscarbonultra(47),
		sunlinse(48),
		nightlinse(49),
		rainlinse(50),
		zinc_plate(51),
		chromium_plate(52),
		wolfram_plate(53),
		titan_plate(54),
		platium_plate(55),
		cobalt_plate(56),
		compress_zinc_plate(57),
		compress_chromium_plate(58),
		compress_wolfram_plate(59),
		comress_titan_plate(60),
		comress_platium_plate(61),
		compress_cobalt_plate(62),
		QuantimItems7(63),
		photoniy_ingot(64),
		photoniy(65);

		private static final CraftingTypes[] VALUES;

		static {
			VALUES = values();
		}

		private final String name;
		private final int ID;

		CraftingTypes(final int ID) {
			this.name = this.name().toLowerCase(Locale.ENGLISH);
			this.ID = ID;
		}

		public static CraftingTypes getFromID(final int ID) {
			return CraftingTypes.VALUES[ID % CraftingTypes.VALUES.length];
		}

		public String getName() {
			return this.name;
		}

		public int getId() {
			return this.ID;
		}
	}
}
