package com.denfop.ssp.items.reactors;

import com.google.common.base.CaseFormat;
import ic2.api.reactor.IReactor;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.reactor.AbstractDamageableReactorComponent;
import ic2.core.ref.ItemName;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemReactorHeatStorage extends AbstractDamageableReactorComponent {
	public static String name;

	public ItemReactorHeatStorage(String name, int heatStorage) {
		super(null, heatStorage);
		BlocksItems.registerItem(this, new ResourceLocation("super_solar_panels", ItemReactorHeatStorage.name = name)).setUnlocalizedName(name);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemName name) {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, ItemReactorHeatStorage.name), null));
	}

	public String getUnlocalizedName() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public boolean canStoreHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return true;
	}

	public int getMaxHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return getMaxCustomDamage(stack);
	}

	public int getCurrentHeat(ItemStack stack, IReactor reactor, int x, int y) {
		return getCustomDamage(stack);
	}

	public int alterHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
		int myHeat = getCurrentHeat(stack, reactor, x, y);
		myHeat += heat;
		int max = getMaxHeat(stack, reactor, x, y);
		if (myHeat > max) {
			reactor.setItemAt(x, y, null);
			heat = max - myHeat + 1;
		} else {
			if (myHeat < 0) {
				heat = myHeat;
				myHeat = 0;
			} else {
				heat = 0;
			}
			setCustomDamage(stack, myHeat);
		}
		return heat;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		if (getCustomDamage(stack) > 0) {
			tooltip.add(Localization.translate("ic2.reactoritem.heatwarning.line1"));
			tooltip.add(Localization.translate("ic2.reactoritem.heatwarning.line2"));
		}
	}
}
