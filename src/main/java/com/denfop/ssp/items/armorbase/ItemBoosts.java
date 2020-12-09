package com.denfop.ssp.items.armorbase;

import com.denfop.ssp.items.api.PlayerEvents;
import com.denfop.ssp.items.api.RechargeHelper;
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
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBoosts extends ItemArmorElectric implements IBoostingJetpack {
	protected final String name;

	public ItemBoosts() {
		this("advancedJetpack");
	}

	protected ItemBoosts(final String name) {
		this(name, 3000000.0, 30000.0, 3);
	}

	protected ItemBoosts(final String name, final double maxCharge, final double transferLimit, final int tier) {
		super(null, null, EntityEquipmentSlot.FEET, maxCharge, transferLimit, tier);
		BlocksItems.registerItem((Item) this, new ResourceLocation("super_solar_panels", this.name = name)).setUnlocalizedName(name);
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

	public void onArmorTick(final World world, final EntityPlayer player, final ItemStack stack) {
		final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		byte toggleTimer = nbt.getByte("toggleTimer");

		if (!world.isRemote && player.ticksExisted % 20 == 0) {
			int e = 0;
			if (stack.hasTagCompound())
				e = stack.getTagCompound().getInteger("energy");
			if (e > 0) {
				e--;
			} else if (e <= 0 && RechargeHelper.consumeCharge(stack, player, 1)) {
				e = 60;
			}
			stack.setTagInfo("energy", new NBTTagInt(e));
		}
		boolean hasCharge = (RechargeHelper.getCharge(stack) > 0);
		if (hasCharge && !player.capabilities.isFlying && player.moveForward > 0.0F) {
			if (player.world.isRemote && !player.isSneaking()) {
				if (!PlayerEvents.prevStep.containsKey(player.getEntityId()))
					PlayerEvents.prevStep.put(player.getEntityId(), player.stepHeight);
				player.stepHeight = 1.0F;
			}
			if (player.onGround) {
				float bonus = 0.05F;
				if (player.isInWater())
					bonus /= 4.0F;
				player.moveRelative(0.0F, 0.0F, bonus, 1.0F);
			} else {
				if (player.isInWater())
					player.moveRelative(0.0F, 0.0F, 0.025F, 1.0F);
				player.jumpMovementFactor = 0.05F;
			}
		}
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
