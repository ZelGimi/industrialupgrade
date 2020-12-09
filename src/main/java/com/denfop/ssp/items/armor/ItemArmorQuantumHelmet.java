package com.denfop.ssp.items.armor;


import com.denfop.ssp.Configs;
import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.keyboard.SSPKeys;
import com.google.common.base.CaseFormat;
import ic2.api.item.*;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.ItemTinCan;
import ic2.core.ref.IItemModelProvider;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Locale;

public class ItemArmorQuantumHelmet extends ItemArmor implements IItemModelProvider, IElectricItem, IMetalArmor, ISpecialArmor, IItemHudProvider {
	protected static final int DEFAULT_COLOUR = -1;
	public static boolean chargeWholeInventory;

	static {
		ItemArmorQuantumHelmet.chargeWholeInventory = false;
	}

	protected final SolarHelmetTypes type;
	protected int ticker;

	public ItemArmorQuantumHelmet(final SolarHelmetTypes type) {
		super(ItemArmor.ArmorMaterial.DIAMOND, -1, EntityEquipmentSlot.HEAD);
		BlocksItems.registerItem((Item) this, new ResourceLocation(SuperSolarPanels.MOD_ID, type.getName())).setUnlocalizedName(type.getLocalisedName());
		this.setCreativeTab(IC2.tabIC2);
		this.setMaxDamage(27);
		this.type = type;

	}

	public String func_77658_a() {
		return "super_solar_panels." + super.getUnlocalizedName().substring(5);
	}

	@SideOnly(Side.CLIENT)
	public void registerModels(final ItemName name) {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("super_solar_panels:" + CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.type.getName()), null));
	}

	public boolean getIsRepairable(@Nonnull final ItemStack toRepair, @Nonnull final ItemStack repair) {
		return false;
	}

	@Nonnull
	public String getUnlocalizedName(@Nonnull final ItemStack stack) {
		return this.getUnlocalizedName();
	}

	public boolean canBeDyed() {
		return this.type != SolarHelmetTypes.Helmet;
	}

	public void func_82813_b(final ItemStack stack, final int colour) {
		this.getDisplayNbt(stack, true).setInteger("colour", colour);
	}

	public ISpecialArmor.ArmorProperties getProperties(final EntityLivingBase player, @Nonnull final ItemStack armour, final DamageSource source, final double damage, final int slot) {
		if (source.isUnblockable()) {
			return new ISpecialArmor.ArmorProperties(0, 0.0, 0);
		}
		return new ISpecialArmor.ArmorProperties(0, 0.15 * this.type.damageAbsorptionRatio, (int) (25.0 * ElectricItem.manager.getCharge(armour) / this.type.energyPerDamage));
	}

	@Nonnull
	public String getItemStackDisplayName(@Nonnull final ItemStack stack) {
		return Localization.translate(this.getUnlocalizedName(stack));
	}

	protected NBTTagCompound getDisplayNbt(final ItemStack stack, final boolean create) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) {
			if (!create) {
				return null;
			}
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}
		NBTTagCompound out;
		if (!nbt.hasKey("display", 50)) {
			if (!create) {
				return null;
			}
			out = new NBTTagCompound();
			nbt.setTag("display", out);
		} else {
			out = nbt.getCompoundTag("display");
		}
		return out;

	}

	public boolean func_82816_b_(final ItemStack stack) {
		return this.func_82814_b(stack) != -1;
	}

	public int getArmorDisplay(final EntityPlayer player, @Nonnull final ItemStack armour, final int slot) {
		if (ElectricItem.manager.getCharge(armour) >= this.type.energyPerDamage) {
			return (int) Math.round(3.0 * this.type.damageAbsorptionRatio);
		}
		return 0;
	}

	public int getMetadata(@Nonnull final ItemStack stack) {
		return 0;
	}

	public int func_82814_b(final ItemStack stack) {
		final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
		if (nbt == null || !nbt.hasKey("colour", 3)) {
			return -1;
		}
		return nbt.getInteger("colour");
	}

	public void func_82815_c(final ItemStack stack) {
		final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
		if (nbt == null || !nbt.hasKey("colour", 3)) {
			return;
		}
		nbt.removeTag("colour");
		if (nbt.hasNoTags()) {
			stack.getTagCompound().removeTag("display");
		}
	}

	public boolean isMetalArmor(final ItemStack stack, final EntityPlayer player) {
		return true;
	}

	public void damageArmor(final EntityLivingBase entity, @Nonnull final ItemStack stack, final DamageSource source, final int damage, final int slot) {
		ElectricItem.manager.discharge(stack, damage * this.type.energyPerDamage, Integer.MAX_VALUE, true, false, false);
	}

	public String getArmorTexture(@Nonnull final ItemStack stack, @Nonnull final Entity entity, @Nonnull final EntityEquipmentSlot slot, @Nonnull final String type) {
		return "super_solar_panels:textures/armour/" + this.type.getName() + ((type != null) ? "Overlay" : "") + ".png";
	}

	public int getItemEnchantability() {
		return 0;
	}


	public boolean canProvideEnergy(final ItemStack stack) {
		return false;
	}

	public double getMaxCharge(final ItemStack stack) {
		return this.type.maxCharge;
	}

	public int getTier(final ItemStack stack) {
		return this.type.tier;
	}

	public void onArmorTick(final World world, @Nonnull final EntityPlayer player, @Nonnull final ItemStack stack) {
		if (this.HUDstuff(world.isRemote, player, stack)) {
			return;
		}
		if (this.ticker++ % this.tickRate() == 0) {
			this.checkTheSky(world, player.getPosition());
		}
		if (this.type != SolarHelmetTypes.Helmet) {
			final int airLevel = player.getAir();
			if (ElectricItem.manager.canUse(stack, 1000.0) && airLevel < 100) {
				player.setAir(airLevel + 200);
				ElectricItem.manager.use(stack, 1000.0, player);
			}
		}
		int output = 0;

		for (final ItemStack playerStack : player.inventory.armorInventory.subList(0, player.inventory.armorInventory.size() - 1)) {
			if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() instanceof IElectricItem) {
				output -= (int) ElectricItem.manager.charge(playerStack, output, this.type.tier, false, false);
				if (output <= 0) {
					return;
				}
			}
		}
		if (ItemArmorQuantumHelmet.chargeWholeInventory) {
			for (final ItemStack playerStack : player.inventory.offHandInventory) {
				if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() instanceof IElectricItem) {
					output -= (int) ElectricItem.manager.charge(playerStack, output, this.type.tier, false, false);
					if (output <= 0) {
						return;
					}
				}
			}
			for (final ItemStack playerStack : player.inventory.mainInventory) {
				if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() instanceof IElectricItem) {
					output -= (int) ElectricItem.manager.charge(playerStack, output, this.type.tier, false, false);
					if (output <= 0) {
						return;
					}
				}
			}
		}
		final NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
		byte toggleTimer = nbtData.getByte("toggleTimer");
		boolean ret = false;
		ElectricItem.manager.charge(stack, output, Integer.MAX_VALUE, true, false);
		final int air = player.getAir();
		if (ElectricItem.manager.canUse(stack, 1000.0) && air < 100) {
			player.setAir(air + 200);
			ElectricItem.manager.use(stack, 1000.0, null);
		} else if (air <= 0) {
			IC2.achievements.issueAchievement(player, "starveWithQHelmet");
		}
		if (ElectricItem.manager.canUse(stack, 1000.0) && player.getFoodStats().needFood()) {
			int slot = -1;
			for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
				final ItemStack playerStack = player.inventory.mainInventory.get(i);

			}
			if (slot > -1) {
				ItemStack playerStack2 = player.inventory.mainInventory.get(slot);
				final ItemTinCan can = (ItemTinCan) playerStack2.getItem();
				final ActionResult<ItemStack> result = can.onEaten(player, playerStack2);
				playerStack2 = result.getResult();
				if (StackUtil.isEmpty(playerStack2)) {
					player.inventory.mainInventory.set(slot, StackUtil.emptyStack);
				}
				if (result.getType() == EnumActionResult.SUCCESS) {
					ElectricItem.manager.use(stack, 1000.0, null);
				}
			}
		} else if (player.getFoodStats().getFoodLevel() <= 0) {
			IC2.achievements.issueAchievement(player, "starveWithQHelmet");
		}
		for (final Object effect : new LinkedList<Object>(player.getActivePotionEffects())) {
			final Potion potion = ((PotionEffect) effect).getPotion();


		}
		boolean Nightvision = nbtData.getBoolean("Nightvision");
		short hubmode = nbtData.getShort("HudMode");
		if (SSPKeys.Isremovepoison(player) && toggleTimer == 0) {
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
			if (skylight > 8) {

				player.removeActivePotionEffect(MobEffects.NIGHT_VISION);
			} else {


				player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));

			}
		}


		player.removePotionEffect(MobEffects.POISON);
		player.removePotionEffect(MobEffects.UNLUCK);
		player.removePotionEffect(MobEffects.WITHER);

		//   potionRemovalCost.put(IC2Potion.radiation, Integer.valueOf(10000));
		//      IC2.platform.removePotion((EntityLivingBase)player, MobEffects.WITHER);
		//   IC2.platform.removePotion((EntityLivingBase)player, MobEffects.POISON);
	}

	public double getTransferLimit(final ItemStack stack) {
		return this.type.transferLimit;
	}

	public boolean doesProvideHUD(final ItemStack stack) {
		return ElectricItem.manager.getCharge(stack) > 0.0;
	}

	protected boolean HUDstuff(final boolean isRemote, final EntityPlayer player, final ItemStack stack) {
		final NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
		byte toggleTimer = nbt.getByte("toggleTimer");
		if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
			byte hubmode = nbt.getByte("hudMode");
			toggleTimer = 10;
			if (hubmode == HudMode.getMaxMode()) {
				hubmode = 0;
			} else {
				++hubmode;
			}
			if (!isRemote) {
				nbt.setByte("hudMode", hubmode);
				IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hubmode).getTranslationKey()));
			}
		}
		if (!isRemote && toggleTimer > 0) {
			final String s = "toggleTimer";
			--toggleTimer;
			nbt.setByte(s, toggleTimer);
		}
		return isRemote;
	}

	public HudMode getHudMode(final ItemStack stack) {
		return HudMode.getFromID(StackUtil.getOrCreateNbtData(stack).getByte("hudMode"));
	}

	public enum SolarHelmetTypes {
		Helmet(Configs.tier4, Configs.maxCharge4, Configs.transferLimit4);

		public final double maxCharge;
		public final double transferLimit;
		public final double damageAbsorptionRatio;
		public final int tier;
		public final int energyPerDamage;
		public final EnumRarity rarity;
		private final String name;

		SolarHelmetTypes(final int tier, final double maxCharge, final double transferLimit) {
			this.name = this.name().toLowerCase(Locale.ENGLISH);
			this.rarity = EnumRarity.EPIC;
			this.tier = tier;
			this.maxCharge = maxCharge;
			this.transferLimit = transferLimit;
			this.energyPerDamage = 42000;
			this.damageAbsorptionRatio = 9;
			assert (double) 9 > 0.0;
		}

		public String getName() {
			return this.name + "SolarHelmet";
		}

		protected String getLocalisedName() {
			return "solar_helmets." + this.name;
		}
	}

	protected int tickRate() {
		return 128;
	}

	public void checkTheSky(final World world, final BlockPos pos) {
		if (!world.provider.hasSkyLight() && world.canBlockSeeSky(pos)) {
			if (world.isDaytime()) {
				if ((world.getBiome(pos).canRain() || !(world.getBiome(pos).getRainfall() <= 0.0f))) {
					if (!world.isRaining()) {
						world.isThundering();
					}
				}
			}
		}
	}

	public void getSubItems(@Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) {
			ElectricItemManager.addChargeVariants(this, items);
		}
	}

	@Nonnull
	public EnumRarity getRarity(@Nonnull final ItemStack stack) {
		return this.type.rarity;
	}


}
