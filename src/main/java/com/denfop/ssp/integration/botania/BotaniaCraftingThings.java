package com.denfop.ssp.integration.botania;

import com.denfop.ssp.SuperSolarPanels;
import ic2.core.block.state.IIdProvider;
import ic2.core.init.BlocksItems;
import ic2.core.item.ItemMulti;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class BotaniaCraftingThings extends ItemMulti {
	protected static final String NAME = "crafting";

	public BotaniaCraftingThings() {
		super(null, CraftingTypes.class);
		BlocksItems.registerItem((Item) this, SuperSolarPanels.getIdentifier("crafting1")).setUnlocalizedName("crafting1");
	}

	@SideOnly(Side.CLIENT)
	protected void registerModel(final int meta, final ItemName name, final String extraName) {
		ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation("super_solar_panels:crafting/" + CraftingTypes.getFromID(meta).getName(), null));
	}

	public String func_77658_a() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public enum CraftingTypes implements IIdProvider {
		terrasteel_plate(0),
		manasteel_plate(1),
		elementium_plate(2),
		terrasteel_core(3),
		manasteel_core(4),
		elementium_core(5),
		depleted_terrasteel_fuel_rod(6),
		depleted_dual_terrasteel_fuel_rod(7),
		depleted_quad_terrasteel_fuel_rod(8),
		depleted_eit_terrasteel_fuel_rod(9),
		rune_energy(10),
		rune_sun(11),
		rune_night(12);
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
