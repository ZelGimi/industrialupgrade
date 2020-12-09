package com.denfop.ssp.items;


import com.denfop.ssp.common.Constants;
import com.google.common.base.CaseFormat;
import ic2.api.item.IKineticRotor;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemGradualInt;
import ic2.core.profile.NotClassic;
import ic2.core.ref.ItemName;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

@NotClassic
public class ItemWindRotor extends ItemGradualInt implements IKineticRotor {
	private final int maxWindStrength;

	private final int minWindStrength;

	private final int radius;
	private final ResourceLocation renderTexture;
	private final float efficiency;

	private final String name;


	public ItemWindRotor(String name, int Radius, int durability, float efficiency, int minWindStrength, int maxWindStrength, ResourceLocation RenderTexture) {
		super(null, durability);
		setMaxStackSize(1);
		this.radius = Radius;
		this.efficiency = efficiency;
		this.renderTexture = RenderTexture;
		this.minWindStrength = minWindStrength;
		this.maxWindStrength = maxWindStrength;
		BlocksItems.registerItem((Item) this, new ResourceLocation(Constants.MOD_ID, this.name = name)).setUnlocalizedName(name);

	}

	@SideOnly(Side.CLIENT)
	public void registerModels(ItemName name) {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), null));
	}

	public String getUnlocalizedName() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, World world, List<String> tooltip, @Nonnull ITooltipFlag advanced) {
		tooltip.add(Localization.translate("ic2.itemrotor.wind.info", this.minWindStrength, this.maxWindStrength));
		IKineticRotor.GearboxType type = null;
		if ((Minecraft.getMinecraft()).currentScreen instanceof ic2.core.block.kineticgenerator.gui.GuiWaterKineticGenerator) {
			type = IKineticRotor.GearboxType.WATER;
		} else if ((Minecraft.getMinecraft()).currentScreen instanceof ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator) {
			type = IKineticRotor.GearboxType.WIND;
		}
		if (type != null)
			tooltip.add(Localization.translate("ic2.itemrotor.fitsin." + isAcceptedType(stack, type)));
	}

	public int getDiameter(ItemStack stack) {
		return this.radius;
	}

	public ResourceLocation getRotorRenderTexture(ItemStack stack) {
		return this.renderTexture;
	}

	public float getEfficiency(ItemStack stack) {
		return this.efficiency;
	}

	public int getMinWindStrength(ItemStack stack) {
		return this.minWindStrength;
	}

	public int getMaxWindStrength(ItemStack stack) {
		return this.maxWindStrength;
	}

	public boolean isAcceptedType(ItemStack stack, IKineticRotor.GearboxType type) {
		return (type == IKineticRotor.GearboxType.WIND);
	}

}
