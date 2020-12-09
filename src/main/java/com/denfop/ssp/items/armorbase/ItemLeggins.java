package com.denfop.ssp.items.armorbase;

import com.denfop.ssp.common.Constants;
import com.google.common.base.CaseFormat;
import ic2.api.item.ElectricItem;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.item.armor.jetpack.IBoostingJetpack;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemLeggins extends ItemArmorElectric implements IBoostingJetpack {
	protected final String name;

	public ItemLeggins() {
		this("advancedJetpack");
	}

	protected ItemLeggins(final String name) {
		this(name, 3000000.0, 30000.0, 3);
	}

	protected ItemLeggins(final String name, final double maxCharge, final double transferLimit, final int tier) {
		super(null, null, EntityEquipmentSlot.LEGS, maxCharge, transferLimit, tier);
		BlocksItems.registerItem((Item) this, new ResourceLocation(Constants.MOD_ID, this.name = name)).setUnlocalizedName(name);
		this.setMaxDamage(27);
		this.setMaxStackSize(1);
		this.setNoRepair();
	}

	public static boolean isHovering(final ItemStack stack) {
		return StackUtil.getOrCreateNbtData(stack).getBoolean("hoverMode");
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(final ItemName name) {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.name), null));
	}

	public String getArmorTexture(final ItemStack stack, final Entity entity, final EntityEquipmentSlot slot, final String type) {
		return "super_solar_panels:textures/armour/" + this.name + ".png";
	}

	public String func_77658_a() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(4);
	}

	public EnumRarity func_77613_e(final ItemStack stack) {
		return EnumRarity.UNCOMMON;
	}

	public void onArmorTick(@Nonnull final World world, @Nonnull final EntityPlayer player, @Nonnull final ItemStack stack) {
		final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		byte toggleTimer = nbt.getByte("toggleTimer");


	}

	public float getBaseThrust(final ItemStack stack, final boolean hover) {
		return hover ? 0.65f : 0.3f;
	}

	public float getBoostThrust(final EntityPlayer player, final ItemStack stack, final boolean hover) {
		return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0) ? (hover ? 0.07f : 0.09f) : 0.0f;
	}

	public boolean useBoostPower(final ItemStack stack, final float boostAmount) {
		return ElectricItem.manager.discharge(stack, 60.0, Integer.MAX_VALUE, true, false, false) > 0.0;
	}

	public float getHoverBoost(final EntityPlayer player, final ItemStack stack, final boolean up) {
		if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0) {
			if (!player.onGround) {
				ElectricItem.manager.discharge(stack, 60.0, Integer.MAX_VALUE, true, false, false);
			}
			return 2.0f;
		}
		return 1.0f;
	}

	public boolean drainEnergy(final ItemStack pack, final int amount) {
		return ElectricItem.manager.discharge(pack, amount * 6, Integer.MAX_VALUE, true, false, false) > 0.0;
	}

	public float getPower(final ItemStack stack) {
		return 1.0f;
	}

	public float getDropPercentage(final ItemStack stack) {
		return 0.05f;
	}

	public double getChargeLevel(final ItemStack stack) {
		return ElectricItem.manager.getCharge(stack) / this.getMaxCharge(stack);
	}

	@Override
	public boolean isJetpackActive(ItemStack arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public float getHoverMultiplier(final ItemStack stack, final boolean upwards) {
		return 0.2f;
	}

	public float getWorldHeightDivisor(final ItemStack stack) {
		return 1.0f;
	}

	public boolean canProvideEnergy(final ItemStack stack) {
		return true;
	}

	public double getDamageAbsorptionRatio() {
		return 0.0;
	}

	public int getEnergyPerDamage() {
		return 0;
	}
}
