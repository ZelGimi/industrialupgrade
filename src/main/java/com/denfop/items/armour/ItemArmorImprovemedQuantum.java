package com.denfop.items.armour;


import cofh.redstoneflux.api.IEnergyContainerItem;
import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.energy.ItemBattery;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IHazmatLike;
import ic2.api.item.IItemHudInfo;
import ic2.api.item.IMetalArmor;
import ic2.core.IC2;
import ic2.core.IC2Potion;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.item.BaseElectricItem;
import ic2.core.item.ItemTinCan;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.ref.ItemName;
import ic2.core.slot.ArmorSlot;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
public class ItemArmorImprovemedQuantum extends ItemArmorElectric
        implements IModelRegister, IUpgradeItem, ISpecialArmor, IElectricItem, IItemHudInfo, IMetalArmor, IHazmatLike {

    protected static final Map<Potion, Integer> potionRemovalCost = new HashMap<>();
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    private final String armorName;
    private final String name;
    private float jumpCharge;

    public ItemArmorImprovemedQuantum(
            String name, EntityEquipmentSlot armorType1, double maxCharge1, double transferLimit1,
            int tier1
    ) {
        super(null, "", armorType1, maxCharge1, transferLimit1 * 16, tier1);
        if (armorType1 == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        potionRemovalCost.put(MobEffects.POISON, 100);
        potionRemovalCost.put(IC2Potion.radiation, 20);
        potionRemovalCost.put(MobEffects.WITHER, 100);
        this.name = name;
        potionRemovalCost.put(MobEffects.HUNGER, 200);
        this.maxCharge = maxCharge1;
        this.tier = tier1;
        this.transferLimit = transferLimit1 * 16;
        setMaxDamage(27);
        setMaxStackSize(1);
        setNoRepair();
        this.armorName = name;
        setUnlocalizedName(name);
        setCreativeTab(IUCore.EnergyTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        switch (armorType1) {
            case HEAD:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.HELMET.list);
                break;
            case CHEST:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.BODY.list);
                break;
            case LEGS:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.LEGGINGS.list);
                break;
            case FEET:
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.BOOTS.list);
                break;
        }
    }

    public static boolean hasCompleteHazmat(EntityLivingBase living) {
        Iterator var1 = ArmorSlot.getAll().iterator();

        EntityEquipmentSlot slot;
        ItemStack stack;
        IHazmatLike hazmat;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            slot = (EntityEquipmentSlot) var1.next();
            stack = living.getItemStackFromSlot(slot);
            if (!stack.isEmpty() || !(stack.getItem() instanceof IHazmatLike)) {
                return false;
            }

            hazmat = (IHazmatLike) stack.getItem();
            if (!hazmat.addsProtection(living, slot, stack)) {
                return false;
            }
        } while (!hazmat.fullyProtects(living, slot, stack));

        return true;
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name + extraName;

        return new ModelResourceLocation(loc, null);
    }

    @SubscribeEvent
    public void Potion(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        NBTTagCompound nbtData = player.getEntityData();
        if (!player.inventory.armorInventory.get(0).isEmpty()
                && player.inventory.armorInventory.get(0).getItem() == IUItem.spectral_boots) {
            nbtData.setBoolean("stepHeight", true);
            player.stepHeight = 1.0F;


        } else {
            if (nbtData.getBoolean("stepHeight")) {
                player.stepHeight = 0.5F;
                nbtData.setBoolean("stepHeight", false);
            }


        }
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return true;
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(
                    LogCategory.Armor,
                    new Throwable(),
                    "Detected invalid armor damage application (%d):",
                    damage - prev
            );
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> info,
            @Nonnull final ITooltipFlag flagIn
    ) {
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);

        if (itemStack.getItem() == IUItem.spectral_chestplate) {
            info.add(Localization.translate("iu.fly") + " " + ModUtils.Boolean(nbtData.getBoolean("jetpack")));
            info.add(Localization.translate("iu.fly_need"));

            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(Localization.translate("press.lshift"));
            }


            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                info.add(Localization.translate("iu.changemode_fly") + Keyboard.getKeyName(Math.abs(KeyboardClient.flymode.getKeyCode())));
                info.add(Localization.translate("iu.vertical") + Keyboard.getKeyName(Math.abs(KeyboardClient.verticalmode.getKeyCode())));
                info.add(Localization.translate("iu.magnet_mode") + Keyboard.getKeyName(Math.abs(KeyboardClient.changemode.getKeyCode())) + " + " + Keyboard.getKeyName(
                        Math.abs(
                                Keyboard.KEY_LSHIFT)));
                info.add(Localization.translate("iu.streak") + Keyboard.getKeyName(KeyboardClient.streakmode.getKeyCode()));
                info.add(Localization.translate("iu.changemode_key") + Localization.translate(
                        "iu.changemode_rcm1"));
                int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                if (mode > 2 || mode < 0) {
                    mode = 0;
                }

                info.add(
                        TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + Localization.translate("message.magnet.mode." + mode)
                );

            }


        }
        ModUtils.mode(itemStack, info);
    }

    @Override
    public void getSubItems(final CreativeTabs subs, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(subs)) {
            ItemStack stack = new ItemStack(this, 1);

            NBTTagCompound nbt = ModUtils.nbt(stack);
            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1, 27);
            nbt = ModUtils.nbt(itemstack);
            nbt.setInteger("ID_Item", Integer.MAX_VALUE);
            items.add(itemstack);
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            String mode = nbt.getString("mode");
            if (nbt.getString("mode").equals("")) {
                return getModelLocation1(name, "");
            } else {
                return getModelLocation1("armor", "_" + this.armorType.ordinal() + "_" + mode);
            }
        });
        String[] mode = {"", "_Zelen", "_Demon", "_Dark", "_Cold", "_Ender", "_Ukraine", "_Fire", "_Snow", "_Taiga", "_Desert",
                "_Emerald"};
        for (final String s : mode) {
            if (s.equals("")) {
                ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
            } else {
                ModelBakery.registerItemVariants(this, getModelLocation1("armor", "_" + this.armorType.ordinal() + s));
            }

        }

    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix = (this.armorType == EntityEquipmentSlot.LEGS) ? 2 : 1;
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + "armor" + this.armorType.ordinal() + "_" + nbtData.getString("mode") + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png";
    }


    public String getUnlocalizedName() {
        return "supersolarpanels." + super.getUnlocalizedName().substring(4);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return getUnlocalizedName();
    }

    public String getItemStackDisplayName(ItemStack itemStack) {
        return Localization.translate(getUnlocalizedName(itemStack));
    }

    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
        return true;
    }


    public boolean hasColor(@Nonnull ItemStack aStack) {
        return (getColor(aStack) != 10511680);
    }


    public void removeColor(ItemStack par1ItemStack) {
        NBTTagCompound tNBT = par1ItemStack.getTagCompound();
        if (tNBT != null) {
            tNBT = tNBT.getCompoundTag("display");
            if (tNBT.hasKey("color")) {
                tNBT.removeTag("color");
            }
        }
    }

    public int getColor(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            return 10511680;
        }
        tNBT = tNBT.getCompoundTag("display");
        return tNBT.hasKey("color") ? tNBT.getInteger("color") : 10511680;
    }

    public ArmorProperties getProperties(
            EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source,
            double damage, int slot
    ) {
        if (Config.spectralquantumprotection) {
            double absorptionRatio = getBaseAbsorptionRatio() * getDamageAbsorptionRatio();
            int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);
            int energyPerDamage = (int) (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect);
            int damageLimit = (int) ((energyPerDamage > 0)
                    ? (25.0D * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                    : 0.0D);
            return new ISpecialArmor.ArmorProperties(10, absorptionRatio, damageLimit);
        } else {
            if (source == DamageSource.FALL && this.armorType.getIndex() == 3) {
                int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

                int energyPerDamage = (int) (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect);
                int damageLimit = 2147483647;
                if (energyPerDamage > 0) {
                    damageLimit = (int) Math.min(
                            damageLimit,
                            25.0D * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                    );
                }

                return new ArmorProperties(10, 1.0D, damageLimit);
            } else {
                return getProperties1(armor, source);
            }
        }
    }

    public ArmorProperties getProperties1(ItemStack armor, DamageSource source) {
        if (source.isUnblockable()) {
            return new ArmorProperties(0, 0.0D, 0);
        } else {
            double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
            int energyPerDamage = this.getEnergyPerDamage();
            int damageLimit = 2147483647;
            if (energyPerDamage > 0) {
                damageLimit = (int) Math.min(
                        damageLimit,
                        25.0D * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                );
            }

            return new ArmorProperties(0, absorptionRatio, damageLimit);
        }
    }

    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, stack).number : 0);

        ElectricItem.manager.discharge(
                stack,
                (damage * (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect)),
                2147483647,
                true,
                false,
                false
        );


    }


    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @SubscribeEvent
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IC2.platform.isSimulating() && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (armor.getItem() == this) {
                int fallDamage = Math.max((int) event.getDistance() - 10, 0);

                double energyCost = (5000 * fallDamage) * (1 - (UpgradeSystem.system.hasModules(
                        EnumInfoUpgradeModules.FALLING_DAMAGE,
                        armor
                ) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.FALLING_DAMAGE, armor).number : 0) * 0.25);

                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, 2147483647, true, false, false);
                    event.setCanceled(true);
                }
            }
            if (entity instanceof EntityPlayer && entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == this) {
                if (entity.getEntityData().getBoolean("isFlyActive")) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingJumpEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntity();
        if (!player.inventory.armorInventory.get(0).isEmpty()
                && (player.inventory.armorInventory.get(0).getItem() == IUItem.spectral_boots || player.inventory.armorInventory
                .get(0)
                .getItem() == IUItem.adv_nano_boots)) {
            player.motionY = 0.8;
            ElectricItem.manager.use(player.inventory.armorInventory.get(0), 4000.0D, player);

        }

    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(
            @Nonnull final World p_77659_1_,
            @Nonnull final EntityPlayer p_77659_2_,
            @Nonnull final EnumHand p_77659_3_
    ) {
        if (armorType.getIndex() == 2) {
            if (IC2.platform.isSimulating()) {
                int mode = ModUtils.NBTGetInteger(p_77659_2_.getHeldItem(p_77659_3_), "mode1");
                mode++;
                if (mode > 2 || mode < 0) {
                    mode = 0;
                }

                ModUtils.NBTSetInteger(p_77659_2_.getHeldItem(p_77659_3_), "mode1", mode);
                IC2.platform.messagePlayer(
                        p_77659_2_,
                        TextFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + Localization.translate("message.magnet.mode." + mode)
                );
                return new ActionResult<>(EnumActionResult.SUCCESS, p_77659_2_.getHeldItem(p_77659_3_));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, p_77659_2_.getHeldItem(p_77659_3_));

    }


    public double getDamageAbsorptionRatio() {
        if (this.armorType.getIndex() == 1) {
            return 1.1D;
        }
        return 1.0D;
    }


    public int getEnergyPerDamage() {
        return 15000;
    }


    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack itemStack) {

        int air;
        boolean Nightvision;
        short hubmode;
        boolean jetpack, enableQuantumSpeedOnSprint;
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);


        int repaired = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.REPAIRED, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.REPAIRED, itemStack).number : 0);
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
            player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300));
        }


        if (repaired != 0) {
            if (world.provider.getWorldTime() % 80 == 0) {
                ElectricItem.manager.charge(
                        itemStack,
                        this.getMaxCharge(itemStack) * 0.00001 * repaired,
                        Integer.MAX_VALUE,
                        true,
                        false
                );
            }
        }
        if (resistance != 0) {
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300, resistance));
        }


        switch (this.armorType.getIndex()) {
            case 3:
                IC2.platform.profilerStartSection("QuantumHelmet");
                air = player.getAir();
                if (ElectricItem.manager.canUse(itemStack, 1000.0D) && air < 100) {
                    player.setAir(air + 200);
                    ElectricItem.manager.use(itemStack, 1000.0D, null);
                    ret = true;
                } else if (air <= 0) {
                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }
                if (ElectricItem.manager.canUse(itemStack, 1000.0D) && player.getFoodStats().needFood()) {
                    int slot = -1;
                    for (int i = 0; i < player.inventory.mainInventory.size(); i++) {
                        if (!player.inventory.mainInventory.get(i).isEmpty()
                                && player.inventory.mainInventory.get(i).getItem() instanceof ItemFood) {
                            slot = i;
                            break;
                        }
                    }
                    if (slot > -1) {
                        ItemStack stack = player.inventory.mainInventory.get(slot);
                        ItemFood can = (ItemFood) stack.getItem();
                        stack = can.onItemUseFinish(stack, world, player);
                        if (stack.getCount() <= 0) {
                            player.inventory.mainInventory.set(slot, StackUtil.emptyStack);
                        }
                        ElectricItem.manager.use(itemStack, 1000.0D, player);
                        ret = true;
                        slot = -1;
                    }

                    for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                        ItemStack playerStack = player.inventory.mainInventory.get(i);
                        if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() == ItemName.filled_tin_can.getInstance()) {
                            slot = i;
                            break;
                        }
                    }

                    if (slot > -1) {
                        ItemStack playerStack = player.inventory.mainInventory.get(slot);
                        ItemTinCan can = (ItemTinCan) playerStack.getItem();
                        ActionResult<ItemStack> result = can.onEaten(player, playerStack);
                        playerStack = result.getResult();
                        if (StackUtil.isEmpty(playerStack)) {
                            player.inventory.mainInventory.set(slot, StackUtil.emptyStack);
                        }

                        if (result.getType() == EnumActionResult.SUCCESS) {
                            ElectricItem.manager.use(itemStack, 1000.0D, null);
                        }

                        ret = true;
                    }
                } else if (player.getFoodStats().getFoodLevel() <= 0) {
                    IC2.achievements.issueAchievement(player, "starveWithQHelmet");
                }

                for (PotionEffect effect : new LinkedList<>(player.getActivePotionEffects())) {

                    Integer cost = potionRemovalCost.get(effect.getPotion());
                    if (cost != null) {
                        cost = cost * (effect.getAmplifier() + 1);
                        if (ElectricItem.manager.canUse(itemStack, cost)) {
                            ElectricItem.manager.use(itemStack, cost, null);
                            IC2.platform.removePotion(player, effect.getPotion());
                        }
                    }
                }

                Nightvision = nbtData.getBoolean("Nightvision");
                hubmode = nbtData.getShort("HudMode");
                if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    Nightvision = !Nightvision;
                    if (IC2.platform.isSimulating()) {
                        nbtData.setBoolean("Nightvision", Nightvision);
                        if (Nightvision) {
                            IC2.platform.messagePlayer(player, "Nightvision enabled.");
                        } else {
                            IC2.platform.messagePlayer(player, "Nightvision disabled.");
                        }
                    }
                }
                if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    if (hubmode == 2) {
                        hubmode = 0;
                    } else {
                        hubmode = (short) (hubmode + 1);
                    }
                    if (IC2.platform.isSimulating()) {
                        nbtData.setShort("HudMode", hubmode);
                        switch (hubmode) {
                            case 0:
                                IC2.platform.messagePlayer(player, "HUD disabled.");
                                break;
                            case 1:
                                IC2.platform.messagePlayer(player, "HUD (basic) enabled.");
                                break;
                            case 2:
                                IC2.platform.messagePlayer(player, "HUD (extended) enabled");
                                break;
                        }
                    }
                }
                if (IC2.platform.isSimulating() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.setByte("toggleTimer", toggleTimer);
                }
                if (Nightvision && IC2.platform.isSimulating() &&
                        ElectricItem.manager.use(itemStack, 1.0D, player)) {
                    int x = MathHelper.floor(player.posX);
                    int z = MathHelper.floor(player.posZ);
                    int y = MathHelper.floor(player.posY);
                    int skylight = player.getEntityWorld().getLightFromNeighbors(new BlockPos(x, y, z));
                    if (skylight > 8) {
                        IC2.platform.removePotion(player, MobEffects.NIGHT_VISION);

                    } else {

                        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0));
                    }
                    ret = true;
                }
                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WATER, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300));
                }
                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.NIGTHVISION, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300));
                }


                IC2.platform.profilerEndSection();

                break;
            case 2:

                if (nbtData.getBoolean("jetpack")) {
                    player.fallDistance = 0;

                    if (nbtData.getBoolean("jump") && !nbtData.getBoolean("canFly") && !player.capabilities.allowFlying && IC2.keyboard.isJumpKeyDown(
                            player) && !nbtData.getBoolean(
                            "isFlyActive") && toggleTimer == 0) {
                        toggleTimer = 10;
                        nbtData.setBoolean("canFly", true);
                    }
                    nbtData.setBoolean("jump", !player.onGround);

                    if (!player.onGround) {
                        if (ElectricItem.manager.canUse(itemStack, 25)) {
                            ElectricItem.manager.use(itemStack, 25, null);
                        } else {
                            nbtData.setBoolean("jetpack", false);
                        }
                    }
                }


                jetpack = nbtData.getBoolean("jetpack");
                boolean vertical = nbtData.getBoolean("vertical");
                if (IUCore.keyboard.isVerticalMode(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    vertical = !vertical;
                    if (IC2.platform.isSimulating()) {

                        nbtData.setBoolean("vertical", vertical);
                        if (vertical) {
                            IC2.platform.messagePlayer(player, Localization.translate("iu.flymode_armor.info2"));

                        } else {
                            IC2.platform.messagePlayer(player, Localization.translate("iu.flymode_armor.info3"));

                        }
                    }
                }
                if (vertical && jetpack) {
                    double motion = 0;
                    if (IC2.keyboard.isJumpKeyDown(player)) {
                        motion = 0.3;
                    }
                    if (player.isSneaking()) {
                        motion = -0.3;
                    }

                    player.motionY += motion;
                }

                int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                if (mode != 0) {

                    int radius = 11;
                    if (mode != 0) {
                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(player.posX - radius, player.posY - radius,
                                player.posZ - radius, player.posX + radius, player.posY + radius, player.posZ + radius
                        );
                        List<Entity> list = player.getEntityWorld().getEntitiesWithinAABBExcludingEntity(
                                player,
                                axisalignedbb
                        );
                        boolean ret1 = false;
                        for (Entity entityinlist : list) {
                            if (entityinlist instanceof EntityItem) {
                                EntityItem item = (EntityItem) entityinlist;
                                if (ElectricItem.manager.canUse(itemStack, 200)) {
                                    if (mode == 1) {

                                        item.setLocationAndAngles(player.posX, player.posY, player.posZ, 0.0F, 0.0F);
                                        if (!player.world.isRemote) {
                                            ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityTeleport(item));
                                        }
                                        item.setPickupDelay(0);
                                        ElectricItem.manager.use(itemStack, 200, null);
                                        ret1 = true;
                                    } else if (mode == 2) {
                                        boolean xcoord = item.posX + 2 >= player.posX && item.posX - 2 <= player.posX;
                                        boolean zcoord = item.posZ + 2 >= player.posZ && item.posZ - 2 <= player.posZ;

                                        if (!xcoord && !zcoord) {
                                            item.setPosition(player.posX, player.posY - 1, player.posZ);
                                            item.setPickupDelay(10);
                                        }

                                    }
                                }

                            }
                        }
                        if (ret1) {
                            player.inventoryContainer.detectAndSendChanges();
                        }

                    }
                }


                boolean magnet = !nbtData.getBoolean("magnet");

                if (IUCore.keyboard.isChangeKeyDown(player) && IC2.keyboard.isSneakKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    if (IC2.platform.isSimulating()) {
                        if (magnet) {
                            IC2.platform.messagePlayer(player, "Magnet enabled.");
                        }
                        if (!magnet) {
                            IC2.platform.messagePlayer(player, "Magnet disabled.");
                        }
                        nbtData.setBoolean("magnet", magnet);
                    }
                }
                if (IUCore.keyboard.isStreakKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    player.openGui(
                            IUCore.instance,
                            4,
                            player.getEntityWorld(),
                            (int) player.posX,
                            (int) player.posY,
                            (int) player.posZ
                    );

                }
                if (IUCore.keyboard.isFlyModeKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    jetpack = !jetpack;
                    if (IC2.platform.isSimulating()) {

                        nbtData.setBoolean("jetpack", jetpack);
                        if (jetpack) {
                            IC2.platform.messagePlayer(player, Localization.translate("iu.flymode_armor.info"));
                        } else {
                            IC2.platform.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));

                        }
                    }
                }

                if (!player.getEntityWorld().isRemote && player.getEntityWorld().provider.getWorldTime() % 40 == 0) {
                    for (int i = 0; i < player.inventory.armorInventory.size(); i++) {

                        if (!player.inventory.armorInventory.get(i).isEmpty() && player.inventory.armorInventory
                                .get(i)
                                .getItem() instanceof IElectricItem && player.inventory.armorInventory
                                .get(i)
                                .getItem() != this) {
                            if (ElectricItem.manager.getCharge(itemStack) > 0) {
                                double sentPacket = ElectricItem.manager.charge(
                                        player.inventory.armorInventory.get(i),
                                        ElectricItem.manager.getCharge(itemStack),
                                        2147483647,
                                        true,
                                        false
                                );

                                if (sentPacket > 0.0D) {
                                    ElectricItem.manager.discharge(itemStack, sentPacket, Integer.MAX_VALUE, true, false, false);
                                    ret = true;

                                }
                            }
                        }
                        IEnergyContainerItem item;
                        if (!player.inventory.armorInventory.get(i).isEmpty()
                                && player.inventory.armorInventory.get(i).getItem() instanceof IEnergyContainerItem) {
                            if (ElectricItem.manager.getCharge(itemStack) > 0) {
                                item = (IEnergyContainerItem) player.inventory.armorInventory.get(i).getItem();

                                int amountRfCanBeReceivedIncludesLimit = item.receiveEnergy(
                                        player.inventory.armorInventory.get(i),
                                        Integer.MAX_VALUE,
                                        true
                                );
                                double realSentEnergyRF = Math.min(
                                        amountRfCanBeReceivedIncludesLimit,
                                        ElectricItem.manager.getCharge(itemStack) * Config.coefficientrf
                                );
                                item.receiveEnergy(player.inventory.armorInventory.get(i), (int) realSentEnergyRF, false);
                                ElectricItem.manager.discharge(
                                        itemStack,
                                        realSentEnergyRF / (double) Config.coefficientrf,
                                        Integer.MAX_VALUE,
                                        true,
                                        false,
                                        false
                                );
                            }
                        }
                    }
                }
                for (int j = 0; j < player.inventory.mainInventory.size(); j++) {

                    if (!player.inventory.mainInventory.get(j).isEmpty()
                            && player.inventory.mainInventory.get(j).getItem() instanceof ic2.api.item.IElectricItem
                            && !(player.inventory.mainInventory
                            .get(j)
                            .getItem() instanceof ItemBattery && ((ItemBattery) player.inventory.mainInventory
                            .get(j)
                            .getItem()).wirelessCharge)) {
                        if (ElectricItem.manager.getCharge(itemStack) > 0) {
                            double sentPacket = ElectricItem.manager.charge(
                                    player.inventory.mainInventory.get(j),
                                    ElectricItem.manager.getCharge(itemStack),
                                    2147483647,
                                    true,
                                    false
                            );

                            if (sentPacket > 0.0D) {
                                ElectricItem.manager.discharge(itemStack, sentPacket, Integer.MAX_VALUE, true, false, false);
                                ret = true;

                            }
                        }

                    }
                    IEnergyContainerItem item;
                    if (!player.inventory.mainInventory.get(j).isEmpty()
                            && player.inventory.mainInventory.get(j).getItem() instanceof IEnergyContainerItem) {
                        if (ElectricItem.manager.getCharge(itemStack) > 0) {
                            item = (IEnergyContainerItem) player.inventory.mainInventory.get(j).getItem();

                            int amountRfCanBeReceivedIncludesLimit = item.receiveEnergy(
                                    player.inventory.mainInventory.get(j),
                                    Integer.MAX_VALUE,
                                    true
                            );
                            double realSentEnergyRF = Math.min(
                                    amountRfCanBeReceivedIncludesLimit,
                                    ElectricItem.manager.getCharge(itemStack) * Config.coefficientrf
                            );
                            item.receiveEnergy(player.inventory.mainInventory.get(j), (int) realSentEnergyRF, false);
                            ElectricItem.manager.discharge(
                                    itemStack,
                                    realSentEnergyRF / (double) Config.coefficientrf,
                                    Integer.MAX_VALUE,
                                    true,
                                    false,
                                    false
                            );
                        }
                    }
                }

                if (IC2.platform.isSimulating() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.setByte("toggleTimer", toggleTimer);
                }


                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300));
                }
                if (ret) {
                    player.openContainer.detectAndSendChanges();
                }

                player.extinguish();
                break;
            case 1:
                IC2.platform.profilerStartSection("QuantumLeggings");
                if (IC2.platform.isRendering()) {
                    enableQuantumSpeedOnSprint = ConfigUtil.getBool(MainConfig.get(), "misc/quantumSpeedOnSprint");
                } else {
                    enableQuantumSpeedOnSprint = true;
                }

                if (ElectricItem.manager.canUse(itemStack, 1000.0D) && (player.onGround || player.isInWater())
                        && IC2.keyboard.isForwardKeyDown(player) && ((enableQuantumSpeedOnSprint && player.isSprinting())
                        || (!enableQuantumSpeedOnSprint && IC2.keyboard.isBoostKeyDown(player)))) {
                    byte speedTicker = nbtData.getByte("speedTicker");
                    speedTicker = (byte) (speedTicker + 1);
                    if (speedTicker >= 10) {
                        speedTicker = 0;
                        ElectricItem.manager.use(itemStack, 1000.0D, null);
                        ret = true;
                    }
                    nbtData.setByte("speedTicker", speedTicker);
                    float speed = 0.22F;
                    if (player.isInWater()) {
                        speed = 0.1F;
                        if (IC2.keyboard.isJumpKeyDown(player)) {
                            player.motionY += 0.10000000149011612D;
                        }
                    }
                    player.moveRelative(0.0F, 0.0F, 1.0F, speed);
                }

                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SPEED, itemStack)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 300));

                }
                IC2.platform.profilerEndSection();
                break;
            case 0:
                IC2.platform.profilerStartSection("QuantumBoots");
                if (IC2.platform.isSimulating()) {
                    boolean wasOnGround = !nbtData.hasKey("wasOnGround") || nbtData.getBoolean("wasOnGround");
                    if (wasOnGround && !player.onGround && IC2.keyboard

                            .isJumpKeyDown(player) && IC2.keyboard
                            .isBoostKeyDown(player)) {
                        ElectricItem.manager.use(itemStack, 4000.0D, null);
                        ret = true;
                    }
                    if (player.onGround != wasOnGround) {
                        nbtData.setBoolean("wasOnGround", player.onGround);
                    }
                } else {
                    if (ElectricItem.manager.canUse(itemStack, 4000.0D) && player.onGround) {

                        this.jumpCharge = 1.0F;
                    }
                    if (player.motionY >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater()) {
                        if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isBoostKeyDown(player)) {
                            if (this.jumpCharge == 1.0F) {
                                player.motionX *= 3.5D;
                                player.motionZ *= 3.5D;
                            }
                            player.motionY += (this.jumpCharge * 0.3F);
                            this.jumpCharge = (float) (this.jumpCharge * 0.75D);
                        } else if (this.jumpCharge < 1.0F) {
                            this.jumpCharge = 0.0F;
                        }
                    }
                }

                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.JUMP, itemStack)) {


                    player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 300));
                }
                IC2.platform.profilerEndSection();
                break;

        }

        if (ret) {
            player.openContainer.detectAndSendChanges();
        }
    }

    public double getMaxCharge(ItemStack itemStack) {
        return this.maxCharge;
    }

    public int getTier(ItemStack itemStack) {
        return this.tier;
    }

    public double getTransferLimit(ItemStack itemStack) {
        return this.transferLimit;
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }


    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return ElectricItem.manager.getCharge(armor) >= (double) this.getEnergyPerDamage()
                ? (int) Math.round(20.0D * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio())
                : 0;
    }


    @Override
    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new ArrayList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }


    @Override
    public void onUpdate(@Nonnull ItemStack itemStack, @Nonnull World world, @Nonnull Entity entity, int slot, boolean par5) {
        NBTTagCompound nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.setBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }


    }


}
