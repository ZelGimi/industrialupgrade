package com.denfop.ssp.items.armor;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.items.armorbase.ItemBoosts;
import com.denfop.ssp.keyboard.SSPKeys;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.util.StackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemArmorQuantumBoosts extends ItemBoosts {
	protected static final int DEFAULT_COLOUR = -1;

	private float jumpCharge;

	public ItemArmorQuantumBoosts() {
		super("graviBoosts", Configs.maxCharge3, Configs.transferLimit3, Configs.tier3);
	}

	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
		return "super_solar_panels:textures/armour/" + this.name + "Overlay" + ".png";
	}

	public void onArmorTick(World world, @Nonnull EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);
		player.fallDistance = 0;
		NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
		byte toggleTimer = nbtData.getByte("toggleTimer");
		boolean ret = false;
		if (IC2.platform.isSimulating()) {
			boolean wasOnGround = (!nbtData.hasKey("wasOnGround") || nbtData.getBoolean("wasOnGround"));
			if (wasOnGround && !player.onGround && IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player)) {
				ElectricItem.manager.use(stack, 4000.0D, null);
			}
			if (player.onGround != wasOnGround)
				nbtData.setBoolean("wasOnGround", player.onGround);
		} else {
			if (ElectricItem.manager.canUse(stack, 4000.0D) && player.onGround)
				this.jumpCharge = 1.0F;
			if (player.motionY >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater()) {
				if (this.jumpCharge == 0.9F) {
					player.motionX *= 1.0D;
					player.motionZ *= 1.0D;
				}
				player.motionY += (this.jumpCharge * 0.3F);
				this.jumpCharge = (float) (this.jumpCharge * 0.75D);
			} else if (this.jumpCharge < 1.0F) {
				this.jumpCharge = 0.0F;
			}
		}

		boolean Nightvision = nbtData.getBoolean("Nightvision");
		short hubmode = nbtData.getShort("HudMode");
		if (SSPKeys.Isremovepoison1(player) && toggleTimer == 0) {
			toggleTimer = 10;
			Nightvision = !Nightvision;
			if (IC2.platform.isSimulating()) {
				nbtData.setBoolean("Nightvision", Nightvision);
				if (Nightvision) {
					IC2.platform.messagePlayer(player, "Effects enabled.");
				} else {
					IC2.platform.messagePlayer(player, "Effects disabled.");
				}
			}
		}
		if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
			toggleTimer = 10;
			if (hubmode == HudMode.getMaxMode()) {
				hubmode = 0;
			} else {
				++hubmode;
			}
			if (IC2.platform.isSimulating()) {
				nbtData.setShort("HudMode", hubmode);
				IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hubmode).getTranslationKey()));
			}
		}
		if (IC2.platform.isSimulating() && toggleTimer > 0) {
			final String s = "toggleTimer";
			--toggleTimer;
			nbtData.setByte(s, toggleTimer);
		}
		if (Nightvision && IC2.platform.isSimulating() && ElectricItem.manager.use(stack, 1.0, player)) {
			final BlockPos pos = new BlockPos((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
			final int skylight = player.getEntityWorld().getLightFromNeighbors(pos);
			if (Configs.canCraftMT) {
				player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300, 0, true, true));
			} else {
				return;
			}
			if (Configs.canCraftASP) {
				player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 300, 0, true, true));
			} else {
				return;
			}
			if (Configs.canCraftASH) {
				player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 300, 4, true, true));
			} else {
			}
		}
	}

	public float getBaseThrust(ItemStack stack, boolean hover) {
		return hover ? 1.0F : 0.5F;
	}

	public float getBoostThrust(EntityPlayer player, ItemStack stack, boolean hover) {
		return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0D) ? (hover ? 0.1F : 0.3F) : 0.0F;
	}

	public boolean useBoostPower(ItemStack stack, float boostAmount) {
		return (ElectricItem.manager.discharge(stack, 834.0D, 2147483647, true, false, false) > 0.0D);
	}

	public float getHoverBoost(EntityPlayer player, ItemStack stack, boolean up) {
		if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0D) {
			if (!player.onGround)
				ElectricItem.manager.discharge(stack, 834.0D, 2147483647, true, false, false);
			return 3.0F;
		}
		return 1.0F;
	}

	public boolean drainEnergy(ItemStack pack, int amount) {
		return (ElectricItem.manager.discharge(pack, 278.0D, 2147483647, true, false, false) > 0.0D);
	}

	public float getPower(ItemStack stack) {
		return 1.5F;
	}

	public float getDropPercentage(ItemStack stack) {
		return 0.01F;
	}

	public boolean isJetpackActive(ItemStack stack) {
		return (super.isJetpackActive(stack) && ElectricItem.manager.getCharge(stack) >= 10000.0D);
	}

	public float getHoverMultiplier(ItemStack stack, boolean upwards) {
		return 0.25F;
	}

	public float getWorldHeightDivisor(ItemStack stack) {
		return 0.91071427F;
	}

	public boolean canProvideEnergy(ItemStack stack) {
		return true;
	}

	public double getDamageAbsorptionRatio() {
		return 1.1D;
	}

	public int getEnergyPerDamage() {
		return 20000;
	}

	public boolean hasColor(@Nonnull ItemStack stack) {
		return (getColor(stack) != -1);
	}

	public int getColor(@Nonnull ItemStack stack) {
		NBTTagCompound nbt = getDisplayNbt(stack, false);
		return (nbt == null || !nbt.hasKey("colour", 3)) ? -1 : nbt.getInteger("colour");
	}

	public void removeColor(@Nonnull ItemStack stack) {
		NBTTagCompound nbt = getDisplayNbt(stack, false);
		if (nbt == null || !nbt.hasKey("colour", 3))
			return;
		nbt.removeTag("colour");
		if (nbt.hasNoTags() && stack.getTagCompound() != null) {
			stack.getTagCompound().removeTag("display");
		}
	}

	public void setColor(@Nonnull ItemStack stack, int colour) {
		getDisplayNbt(stack, true).setInteger("colour", colour);
	}

	protected NBTTagCompound getDisplayNbt(ItemStack stack, boolean create) {
		NBTTagCompound out, nbt = stack.getTagCompound();
		if (nbt == null) {
			if (!create)
				return null;
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		if (!nbt.hasKey("display", 10)) {
			if (!create)
				return null;
			out = new NBTTagCompound();
			nbt.setTag("display", out);
		} else {
			out = nbt.getCompoundTag("display");
		}
		return out;
	}

	@Nonnull
	public EnumRarity getRarity(@Nonnull ItemStack stack) {
		return EnumRarity.EPIC;
	}
}
