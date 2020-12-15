package com.denfop.ssp.integration.botania;

import com.denfop.ssp.items.resource.CraftingThings.CraftingTypes;
import ic2.core.block.state.IIdProvider;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.IMultiItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum BotaniaItems {
	BotaniaCRAFTING, terrasteel_fuel_rod, dual_terrasteel_fuel_rod, quad_terrasteel_fuel_rod, eit_terrasteel_fuel_rod, terrasteeldrill;
	//battery_su

	public Item instance;

	public static void buildItems(Side side) {
		BotaniaCRAFTING.setInstance(new BotaniaCraftingThings());
		terrasteel_fuel_rod.setInstance(new ItemReactorBotania("terrasteel_fuel_rod", 1));
		dual_terrasteel_fuel_rod.setInstance(new ItemReactorBotania("dual_terrasteel_fuel_rod", 2));
		quad_terrasteel_fuel_rod.setInstance(new ItemReactorBotania("quad_terrasteel_fuel_rod", 4));
		eit_terrasteel_fuel_rod.setInstance(new ItemReactorBotania("eit_terrasteel_fuel_rod", 8));
		terrasteeldrill.setInstance(new ItemBotaniaDrill());

		if (side == Side.CLIENT)
			doModelGuf();
	}

	@SideOnly(Side.CLIENT)
	public static void doModelGuf() {
		for (BotaniaItems item : values())
			((IItemModelProvider) item.getInstance()).registerModels(null);
	}

	public <T extends Item> T getInstance() {
		return (T) this.instance;
	}

	public <T extends Item> void setInstance(T instance) {
		if (this.instance != null)
			throw new IllegalStateException("Duplicate instances!");
		this.instance = instance;
	}

	public <T extends Enum> ItemStack getItemStack(T variant) {
		if (this.instance == null)
			return null;
		if (this.instance instanceof IMultiItem) {
			IMultiItem<IIdProvider> multiItem = (IMultiItem<IIdProvider>) this.instance;
			return multiItem.getItemStack((IIdProvider) variant);
		}
		if (variant == null)
			return new ItemStack(this.instance);
		throw new IllegalArgumentException("Not applicable");
	}

	public ItemStack getItemStack(CraftingTypes proton, int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
