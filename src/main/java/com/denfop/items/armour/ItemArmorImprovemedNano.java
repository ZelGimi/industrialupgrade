package com.denfop.items.armour;


import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.utils.EnumInfoUpgradeModules;
import com.denfop.utils.ModUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
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
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
public class ItemArmorImprovemedNano extends ItemArmorElectric
        implements IModelRegister, ISpecialArmor, IElectricItem, IItemHudInfo, IMetalArmor {

    protected static final Map<Potion, Integer> potionRemovalCost = new HashMap<Potion, Integer>();
    protected final double maxCharge;
    protected final double transferLimit;
    protected final int tier;
    private final ThreadLocal<Boolean> allowDamaging;
    private final String armorName;
    private final String name;
    private float jumpCharge;

    public ItemArmorImprovemedNano(
            String name, EntityEquipmentSlot armorType1, double maxCharge1, double transferLimit1,
            int tier1
    ) {
        super(null, "",armorType1,maxCharge1,transferLimit1 * 16,tier1);

        if (armorType1 == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        potionRemovalCost.put(MobEffects.POISON, 100);
        potionRemovalCost.put(IC2Potion.radiation, 20);
        potionRemovalCost.put(MobEffects.WITHER, 100);
        this.name = name;
        potionRemovalCost.put(MobEffects.HUNGER, 200);
        this.allowDamaging = new ThreadLocal<>();
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
    }
    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
        if (damage != prev && BaseElectricItem.logIncorrectItemDamaging) {
            IC2.log.warn(LogCategory.Armor, new Throwable(), "Detected invalid armor damage application (%d):", new Object[]{damage - prev});
        }

    }
    @Override
    public void getSubItems(final CreativeTabs p_150895_1_, final NonNullList<ItemStack> var3) {
        if (this.isInCreativeTab(p_150895_1_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            var3.add(var4);
            var3.add(new ItemStack(this, 1, this.getMaxDamage()));
        }
    }

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name, String extraName) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("armour").append("/").append(name + extraName);

        return new ModelResourceLocation(loc.toString(), null);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            final NBTTagCompound nbt = ModUtils.nbt(stack);

            return getModelLocation1(name, !nbt.getString("mode").isEmpty() ? "_" + nbt.getString("mode") : "");
        });
        String[] mode = {"", "_Zelen", "_Demon", "_Dark", "_Cold", "_Ender"};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));
        }

    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix = (this.armorType == EntityEquipmentSlot.LEGS) ? 2 : 1;
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + nbtData.getString("mode") + "_" + suffix + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png";
    }

    public List<String> getHudInfo(ItemStack itemStack) {
        List<String> info = new LinkedList<>();
        info.add(ElectricItem.manager.getToolTip(itemStack));
        info.add(Localization.translate("ic2.item.tooltip.PowerTier") + " " + this.tier);
        return info;
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


    public boolean hasColor(ItemStack aStack) {
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
        return (tNBT == null) ? 10511680 : (tNBT.hasKey("color") ? tNBT.getInteger("color") : 10511680);
    }

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase player, ItemStack armor, DamageSource source,
            double damage, int slot
    ) {
        if (Config.spectralquantumprotection) {
            double absorptionRatio = getBaseAbsorptionRatio() * getDamageAbsorptionRatio();
            NBTTagCompound nbt = ModUtils.nbt(armor);
            int protect = 0;
            for (int i = 0; i < 4; i++) {
                if (nbt.getString("mode_module" + i).equals("protect")) {
                    protect++;
                }

            }
            protect = Math.min(protect, EnumInfoUpgradeModules.PROTECTION.max);
            int energyPerDamage = (int) (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect);
            int damageLimit = (int) ((energyPerDamage > 0)
                    ? (25.0D * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                    : 0.0D);
            return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
        } else {
            if (source == DamageSource.FALL && this.armorType.getIndex() == 3) {
                NBTTagCompound nbt = ModUtils.nbt(armor);
                int protect = 0;
                for (int i = 0; i < 4; i++) {
                    if (nbt.getString("mode_module" + i).equals("protect")) {
                        protect++;
                    }

                }
                protect = Math.min(protect, EnumInfoUpgradeModules.PROTECTION.max);


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

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        NBTTagCompound nbt = ModUtils.nbt(stack);
        int protect = 0;
        for (int i = 0; i < 4; i++) {
            if (nbt.getString("mode_module" + i).equals("protect")) {
                protect++;
            }

        }
        protect = Math.min(protect, EnumInfoUpgradeModules.PROTECTION.max);


        ElectricItem.manager.discharge(
                stack,
                (damage * (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect) * 2),
                2147483647,
                true,
                false,
                false
        );


    }


    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @SubscribeEvent
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IC2.platform.isSimulating() && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (armor.getItem() == this) {
                int fallDamage = Math.max((int) event.getDistance() - 10, 0);
                double energyCost = (getEnergyPerDamage() * fallDamage);
                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, 2147483647, true, false, false);
                    event.setCanceled(true);
                }
            }
        }
    }


    public double getDamageAbsorptionRatio() {
        if (this.armorType.getIndex() == 1) {
            return 1.1D;
        }
        return 1.0D;
    }


    public int getEnergyPerDamage() {
        return 90000;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.EPIC;
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack itemStack) {
        int air;
        boolean Nightvision;
        short hubmode;
        boolean jetpack, hoverMode, enableQuantumSpeedOnSprint;
        NBTTagCompound nbtData = ModUtils.nbt(itemStack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        int resistance = 0;
        int repaired = 0;
        for (int i = 0; i < 4; i++) {
            if (nbtData.getString("mode_module" + i).equals("invisibility")) {
                player.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 300));
            }
            if (nbtData.getString("mode_module" + i).equals("resistance")) {
                resistance++;
            }
            if (nbtData.getString("mode_module" + i).equals("repaired")) {
                repaired++;
            }
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
                            player.inventory.mainInventory.set(slot, null);
                        }
                        ElectricItem.manager.use(itemStack, 1000.0D, null);
                        ret = true;
                    }
                    for(int i = 0; i < player.inventory.mainInventory.size(); ++i) {
                        ItemStack playerStack = (ItemStack)player.inventory.mainInventory.get(i);
                        if (!StackUtil.isEmpty(playerStack) && playerStack.getItem() == ItemName.filled_tin_can.getInstance()) {
                            slot = i;
                            break;
                        }
                    }

                    if (slot > -1) {
                        ItemStack playerStack = (ItemStack)player.inventory.mainInventory.get(slot);
                        ItemTinCan can = (ItemTinCan)playerStack.getItem();
                        ActionResult<ItemStack> result = can.onEaten(player, playerStack);
                        playerStack = (ItemStack)result.getResult();
                        if (StackUtil.isEmpty(playerStack)) {
                            player.inventory.mainInventory.set(slot, StackUtil.emptyStack);
                        }

                        if (result.getType() == EnumActionResult.SUCCESS) {
                            ElectricItem.manager.use(itemStack, 1000.0D, (EntityLivingBase)null);
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
                NBTTagCompound nbt = ModUtils.nbt(itemStack);
                boolean waterBreathing = false;
                for (int i = 0; i < 4; i++) {
                    if (nbt.getString("mode_module" + i).equals("waterBreathing")) {
                        waterBreathing = true;
                        break;
                    }

                }
                if (waterBreathing) {
                    player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, 300));
                }
                IC2.platform.profilerEndSection();

                break;
            case 2:
             if(!player.onGround)
                if (nbtData.getBoolean("jetpack")) {

                    if (ElectricItem.manager.canUse(itemStack, 25)) {
                        ElectricItem.manager.use(itemStack, 25, player);
                    } else {
                        nbtData.setBoolean("jetpack", false);
                    }
                }
                jetpack = nbtData.getBoolean("jetpack");

                hoverMode = nbtData.getBoolean("hoverMode");
                if (IC2.keyboard.isJumpKeyDown(player) && IC2.keyboard.isModeSwitchKeyDown(player) && toggleTimer == 0) {
                    ItemStack jetpack1 = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
                    ElectricItem.manager.discharge(jetpack1, 3000, 2147483647, true, false, false);
                    toggleTimer = 10;
                    hoverMode = !hoverMode;
                    if (IC2.platform.isSimulating()) {
                        nbtData.setBoolean("hoverMode", hoverMode);
                        if (hoverMode) {
                            IC2.platform.messagePlayer(player, "Quantum Hover Mode enabled.");
                        } else {
                            IC2.platform.messagePlayer(player, "Quantum Hover Mode disabled.");
                        }
                    }
                }


                if( IUCore.keyboard.isFlyModeKeyDown(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    jetpack = !jetpack;
                    if (IC2.platform.isSimulating()) {

                        nbtData.setBoolean("jetpack", jetpack);
                        if (jetpack) {
                            IC2.platform.messagePlayer(player, "Nano Jetpack enabled.");
                            player.capabilities.isFlying = true;

                            player.capabilities.allowFlying = true;
                            player.fallDistance = 0.0F;
                            player.distanceWalkedModified = 0.0F;
                        } else {
                            IC2.platform.messagePlayer(player, "Nano Jetpack disabled.");

                        }
                    }
                }


                if (IC2.platform.isSimulating() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.setByte("toggleTimer", toggleTimer);
                }

                nbt = ModUtils.nbt(itemStack);
                boolean fireResistance = false;
                for (int i = 0; i < 4; i++) {
                    if (nbt.getString("mode_module" + i).equals("fireResistance")) {
                        fireResistance = true;
                        break;
                    }

                }

                if (fireResistance) {
                    player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 300));
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
                nbt = ModUtils.nbt(itemStack);
                boolean moveSpeed = false;
                for (int i = 0; i < 4; i++) {
                    if (nbt.getString("mode_module" + i).equals("moveSpeed")) {
                        moveSpeed = true;
                        break;
                    }

                }
                if (moveSpeed) {
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
                nbt = ModUtils.nbt(itemStack);
                boolean jump = false;
                for (int i = 0; i < 4; i++) {
                    if (nbt.getString("mode_module" + i).equals("jump")) {
                        jump = true;
                        break;
                    }

                }
                if (jump) {


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


    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        if (ElectricItem.manager.getCharge(armor) >= getEnergyPerDamage()) {
            return (int) Math.round(20.0D * getBaseAbsorptionRatio() * getDamageAbsorptionRatio());
        }
        return 0;
    }



    @Override

    public List<String> getHudInfo(ItemStack stack, boolean advanced) {
        List<String> info = new ArrayList<>();
        info.add(ElectricItem.manager.getToolTip(stack));
        return info;
    }
}
