package com.denfop.items.armour.special;


import com.denfop.*;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IItemStackInventory;
import com.denfop.items.armour.ISpecialArmor;
import com.denfop.items.armour.material.ArmorMaterials;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.KeyboardIU;
import com.denfop.utils.ModUtils;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.*;

import static net.minecraftforge.common.ForgeMod.SWIM_SPEED;

public class ItemSpecialArmor extends ArmorItem implements ISpecialArmor, IItemStackInventory, IEnergyItem,
        IUpgradeItem, IItemTab{

    protected final Map<MobEffect, Integer> potionRemovalCost = new IdentityHashMap<>();
    private final List<EnumCapability> listCapability;
    private final double maxCharge;
    private final int tier;
    private final double transferLimit;
    private final EnumTypeArmor armor;
    private final String name;
    private float jumpCharge;
    private boolean lastJetpackUsed = false;
    private String nameItem;

    public ItemSpecialArmor(EnumSubTypeArmor subTypeArmor, EnumTypeArmor typeArmor) {
        super(ArmorMaterials.ENERGY_ITEM, subTypeArmor.getEntityType(), new Properties().stacksTo(1).setNoRepair());
        final List<EnumCapability> list = new ArrayList<>(subTypeArmor.getCapabilities());
        list.removeIf(capability -> !typeArmor.getListCapability().contains(capability));
        this.listCapability = list;
        if (getEquipmentSlot() == EquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        this.armor = typeArmor;
        this.name = typeArmor.name().toLowerCase() + "_" + subTypeArmor.name().toLowerCase();
        this.maxCharge = typeArmor.getMaxEnergy();
        this.tier = typeArmor.getTier();
        this.transferLimit = typeArmor.getMaxTransfer();
        if (this.listCapability.contains(EnumCapability.ACTIVE_EFFECT) || this.listCapability.contains(EnumCapability.ALL_ACTIVE_EFFECT)) {
            potionRemovalCost.put(IUPotion.radiation, 20);
            if (this.listCapability.contains(EnumCapability.ALL_ACTIVE_EFFECT)) {
                potionRemovalCost.put(MobEffects.POISON, 100);
                potionRemovalCost.put(MobEffects.WITHER, 100);
                potionRemovalCost.put(MobEffects.HUNGER, 200);
                potionRemovalCost.put(MobEffects.MOVEMENT_SLOWDOWN, 200);
                potionRemovalCost.put(MobEffects.UNLUCK, 200);
                potionRemovalCost.put(MobEffects.LEVITATION, 200);
                potionRemovalCost.put(MobEffects.CONFUSION, 200);
                potionRemovalCost.put(MobEffects.BLINDNESS, 200);
                potionRemovalCost.put(MobEffects.WEAKNESS, 200);
            }
        }


        switch (getEquipmentSlot()) {
            case HEAD:
                IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.HELMET.list));
                break;
            case CHEST:
                IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.BODY.list));
                break;
            case LEGS:
                IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.LEGGINGS.list));
                break;
            case FEET:
                IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.BOOTS.list));
                break;
        }
    }

    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        switch (getEquipmentSlot()) {
            case HEAD:
                return EnumUpgrades.HELMET.list;

            case CHEST:
                return EnumUpgrades.BODY.list;

            case LEGS:
                return EnumUpgrades.LEGGINGS.list;

            case FEET:
                return EnumUpgrades.BOOTS.list;

        }
        return EnumUpgrades.HELMET.list;
    }

    @Override
    public IAdvInventory getInventory(final Player player, final ItemStack stack) {
        if (this.getEquipmentSlot() == EquipmentSlot.LEGS)
            return new ItemStackLegsBags(player, stack);
        else
            return new ItemStackStreakSettings(player, stack);

    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem ="iu."+ pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }


    public EnumTypeArmor getArmor() {
        return armor;
    }

    public List<EnumCapability> getListCapability() {
        return listCapability;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity p_41406_, int p_41407_, boolean p_41408_) {
        CompoundTag nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
    }





    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int suffix = (this.getEquipmentSlot() == EquipmentSlot.LEGS) ? 2 : 1;
        CompoundTag nbtData = ModUtils.nbt(stack);
        final String mode = nbtData.getString("mode");
        if (!mode.isEmpty() && armor.getSkinsList().contains(mode)) {
            if (suffix == 1) {
                return Constants.TEXTURES + ":textures/armor/" + this.armor
                        .name()
                        .toLowerCase() + "_" + mode.toLowerCase() + "_1.png";
            } else {
                return Constants.TEXTURES + ":textures/armor/" + this.armor
                        .name()
                        .toLowerCase() + "_" + mode.toLowerCase() + "_2.png";
            }
        }

        if (suffix == 1) {
            return Constants.TEXTURES + ":textures/armor/" + this.armor.name().toLowerCase() + "_1.png";
        } else {
            return Constants.TEXTURES + ":textures/armor/" + this.armor.name().toLowerCase() + "_2.png";
        }

    }


    @SubscribeEvent
    public void Potion(LivingEvent.LivingTickEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide) return;

        CompoundTag nbtData = player.getPersistentData();
        ItemStack boots = player.getInventory().armor.get(0);

        boolean hasStepCap = false;
        if (!boots.isEmpty() && boots.getItem() instanceof ItemSpecialArmor armor) {
            hasStepCap = armor.listCapability.contains(EnumCapability.AUTO_JUMP);
        }

        if (hasStepCap) {
            if (!nbtData.getBoolean("stepHeight")) {
              
                nbtData.putBoolean("stepHeight", true);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(1F);
            }
        } else {
            if (nbtData.getBoolean("stepHeight")) {

                nbtData.putBoolean("stepHeight", false);
                player.getAttribute(ForgeMod.STEP_HEIGHT_ADDITION.get()).setBaseValue(0F);
            }
        }
    }


    public void use(ItemStack itemStack, double amount) {
        ElectricItem.manager.discharge(itemStack, amount, 2147483647, true, false, false);
    }

    public double getCharge(ItemStack itemStack) {
        return ElectricItem.manager.getCharge(itemStack);
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }


    public void moveRelative(Player player, float strafe, float up, float forward, float friction) {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4F) {
            f = Mth.sqrt(f);
            if (f < 1.0F) f = 1.0F;
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            if (player.isInWater() || player.isInLava()) {

                strafe = strafe * (float) player.getAttribute(SWIM_SPEED.get()).getValue();
                up = up * (float) player.getAttribute(SWIM_SPEED.get()).getValue();
                forward = forward * (float) player.getAttribute(SWIM_SPEED.get()).getValue();
            }
            float f1 = Mth.sin(player.getYRot() * 0.017453292F);
            float f2 = Mth.cos(player.getYRot() * 0.017453292F);
            Vec3 deltaMovement = player.getDeltaMovement();
            player.setDeltaMovement(new Vec3(deltaMovement.x + (strafe * f2 - forward * f1), deltaMovement.y + up, deltaMovement.z + (forward * f2 + strafe * f1)));
        }
    }

    public void onArmorTick(ItemStack itemStack, @Nonnull Level world, @Nonnull Player player) {

        boolean Nightvision;
        boolean jetpack;
        CompoundTag nbtData = ModUtils.nbt(itemStack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);


        int repaired = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.REPAIRED, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.REPAIRED, itemStack).number : 0);
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300));
        }


        if (repaired != 0) {
            if (world.getGameTime() % 80 == 0) {
                ElectricItem.manager.charge(
                        itemStack,
                        this.getMaxEnergy(itemStack) * 0.00001 * repaired,
                        Integer.MAX_VALUE,
                        true,
                        false
                );
            }
        }
        if (resistance != 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, resistance));
        }


        switch (this.getEquipmentSlot()) {
            case HEAD:
                List<MobEffectInstance> effects = new ArrayList<>(player.getActiveEffects());

                for (MobEffectInstance effect : effects) {

                    Integer cost = potionRemovalCost.get(effect.getEffect());
                    if (cost != null) {
                        cost = cost * (effect.getAmplifier() + 1);
                        if (ElectricItem.manager.canUse(itemStack, cost)) {
                            ElectricItem.manager.use(itemStack, cost, null);
                            IUCore.proxy.removePotion(player, effect.getEffect());
                        }
                    }
                }

                Nightvision = nbtData.getBoolean("Nightvision");
                if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    Nightvision = !Nightvision;
                    if (!world.isClientSide()) {
                        nbtData.putBoolean("Nightvision", Nightvision);
                        if (!world.isClientSide())
                            if (Nightvision) {
                                IUCore.proxy.messagePlayer(player, "Nightvision enabled.");
                            } else {
                                IUCore.proxy.messagePlayer(player, "Nightvision disabled.");
                            }
                    }
                }
                if (!world.isClientSide() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.putByte("toggleTimer", toggleTimer);
                }

                if (Nightvision && !world.isClientSide &&
                        ElectricItem.manager.use(itemStack, 1.0D, player)) {
                    int x = Mth.floor(player.getX());
                    int z = Mth.floor(player.getZ());
                    int y = Mth.floor(player.getY());
                    BlockPos pos = new BlockPos(x, y, z);
                    int skylight = player.level().getMaxLocalRawBrightness(pos);
                    boolean with = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITH);
                    boolean without = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITHOUT);
                    if (without || with) {
                        if (skylight > 8) {
                            IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                            if (with) {
                                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, true, true));

                            }
                        } else {
                            if (with) {
                                IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);

                            }
                            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
                        }
                    } else {
                        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
                    }
                    ret = true;
                }
                if (this.listCapability.contains(EnumCapability.FOOD) && ElectricItem.manager.canUse(itemStack, 1000.0D) && player
                        .getFoodData()
                        .needsFood()) {
                    int slot = -1;
                    for (int i = 0; i < player.getInventory().items.size(); i++) {
                        if (!player.getInventory().items.get(i).isEmpty()
                                && player.getInventory().items.get(i).getItem().isEdible()) {
                            slot = i;
                            break;
                        }
                    }
                    if (slot > -1) {
                        ItemStack stack = player.getInventory().items.get(slot);
                        stack = stack.getItem().finishUsingItem(stack,world, player);
                        if (stack.getCount() <= 0) {
                            player.getInventory().items.set(slot, ItemStack.EMPTY);
                        }
                        ElectricItem.manager.use(itemStack, 1000.0D, player);
                        ret = true;
                    }
                }
                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WATER, itemStack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 300));
                }
                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.NIGTHVISION, itemStack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300));
                }


                break;
            case CHEST:

                if (nbtData.getBoolean("jetpack")) {
                    player.fallDistance = 0;

                    if (nbtData.getBoolean("jump") && !nbtData.getBoolean("canFly") && !player.getAbilities().mayfly && IUCore.keyboard.isJumpKeyDown(
                            player) && !nbtData.getBoolean(
                            "isFlyActive") && toggleTimer == 0) {
                        toggleTimer = 10;
                        nbtData.putBoolean("canFly", true);
                    }
                    nbtData.putBoolean("jump", !player.onGround());

                    if (!player.onGround()) {
                        if (ElectricItem.manager.canUse(itemStack, 45)) {
                            ElectricItem.manager.use(itemStack, 45, null);
                        } else {
                            nbtData.putBoolean("jetpack", false);
                        }
                    }
                }


                jetpack = nbtData.getBoolean("jetpack");
                boolean vertical = nbtData.getBoolean("vertical");

                if (this.listCapability.contains(EnumCapability.VERTICAL_FLY) && IUCore.keyboard.isVerticalMode(player) && toggleTimer == 0) {
                    toggleTimer = 10;
                    vertical = !vertical;
                    if (!world.isClientSide()) {

                        nbtData.putBoolean("vertical", vertical);
                        if (!world.isClientSide())
                            if (vertical) {
                                IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info2"));

                            } else {
                                IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info3"));

                            }
                    }
                }
                if (vertical && jetpack) {
                    double motion = 0;
                    if (IUCore.keyboard.isJumpKeyDown(player)) {
                        motion = 0.3;
                    }
                    if (player.isShiftKeyDown()) {
                        motion = -0.3;
                    }
                    Vec3 deltaMotion = player.getDeltaMovement();
                    player.setDeltaMovement(new Vec3(deltaMotion.x, deltaMotion.y + motion, deltaMotion.z));
                }


                if (IUCore.keyboard.isStreakKeyDown(player) && toggleTimer == 0 && IUItem.spectral_chestplate.getItem() == this) {
                    toggleTimer = 10;
                    if (!world.isClientSide()) {
                        save(itemStack, player);
                        CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

                        growingBuffer.writeByte(3);

                        growingBuffer.flip();
                        NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, itemStack), buf -> buf.writeBytes(growingBuffer));


                    }


                }
                int reTimer = nbtData.getInt("reTimer");
                if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {

                    if (reTimer > 0) {
                        reTimer--;
                        nbtData.putInt("reTimer", reTimer);
                    }
                }
                if ((this.listCapability.contains(EnumCapability.FLY) || this.listCapability.contains(EnumCapability.JETPACK_FLY)) && IUCore.keyboard.isFlyModeKeyDown(
                        player) && toggleTimer == 0 && reTimer == 0) {
                    toggleTimer = 10;
                    jetpack = !jetpack;
                    if (!world.isClientSide()) {
                        nbtData.putBoolean("jetpack", jetpack);

                        if (jetpack) {
                            if (!world.isClientSide())
                                IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info"));
                            if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                                nbtData.putInt("timer", 600);
                            }
                        } else {
                            if (!world.isClientSide())
                                IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));

                        }
                    }
                }
                if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                    jetpack = nbtData.getBoolean("jetpack");
                    int timer = nbtData.getInt("timer");
                    if (timer > 0) {
                        timer--;
                        nbtData.putInt("timer", timer);
                    } else {
                        if (jetpack) {
                            nbtData.putBoolean("jetpack", false);
                            nbtData.putInt("reTimer", 5 * 20 * 60);
                            if (!world.isClientSide())
                                IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));
                        }
                    }

                    boolean jetpackUsed = false;
                    if (!jetpack) {
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            jetpackUsed = this.useJetpack(player);
                        }
                    }
                    if (world.isClientSide && player == IUCore.proxy.getPlayerInstance()) {
                        if (lastJetpackUsed != jetpackUsed) {
                            if (jetpackUsed) {
                                SoundHandler.playSound(player, "JetpackLoop");
                            }


                            lastJetpackUsed = jetpackUsed;
                            if (!lastJetpackUsed) {
                                SoundHandler.stopSound(EnumSound.JetpackLoop);
                            }
                        }
                        final Random rnd = IUCore.random;
                        if (jetpackUsed) {
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.addParticle(
                                        ParticleTypes.SMOKE,
                                        (float) (player.getX() - player.getDeltaMovement().x) + rnd.nextFloat(),
                                        (float) (player.getY()),
                                        (float) (player.getZ() - player.getDeltaMovement().z) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.addParticle(
                                        ParticleTypes.FLAME,
                                        (float) (player.getX() - player.getDeltaMovement().x) + rnd.nextFloat(),
                                        (float) (player.getY()),
                                        (float) (player.getZ() - player.getDeltaMovement().z) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                        }


                    }
                }
                if (listCapability.contains(EnumCapability.JETPACK)) {
                    boolean jetpackUsed = false;
                    if (!jetpack) {
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            jetpackUsed = this.useJetpack(player);
                        }
                    }
                    if (world.isClientSide && player == IUCore.proxy.getPlayerInstance()) {
                        if (lastJetpackUsed != jetpackUsed) {
                            if (jetpackUsed) {
                                SoundHandler.playSound(player, "JetpackLoop");
                            }


                            lastJetpackUsed = jetpackUsed;
                            if (!lastJetpackUsed) {
                                SoundHandler.stopSound(EnumSound.JetpackLoop);
                            }
                        }
                        final Random rnd = IUCore.random;
                        if (jetpackUsed) {
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.addParticle(
                                        ParticleTypes.SMOKE,
                                        (float) (player.getX() - player.getDeltaMovement().x) + rnd.nextFloat(),
                                        (float) (player.getY()),
                                        (float) (player.getZ() - player.getDeltaMovement().z) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                            for (int i = 0; i < rnd.nextInt(10); i++) {
                                world.addParticle(
                                        ParticleTypes.FLAME,
                                        (float) (player.getX() - player.getDeltaMovement().x) + rnd.nextFloat(),
                                        (float) (player.getY()),
                                        (float) (player.getZ() - player.getDeltaMovement().z) + rnd.nextFloat(), 0, -0.25, 0
                                );
                            }
                        }


                    }
                }

                if (!world.isClientSide() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.putByte("toggleTimer", toggleTimer);
                }


                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300));
                }

                player.clearFire();
                break;
            case LEGS:


                if (this.listCapability.contains(EnumCapability.SPEED) && ElectricItem.manager.canUse(
                        itemStack,
                        1000.0D
                ) && (player.onGround() || player.isInWater()) && player.isSprinting()) {
                    byte speedTicker = nbtData.getByte("speedTicker");
                    speedTicker = (byte) (speedTicker + 1);
                    if (speedTicker >= 10) {
                        speedTicker = 0;
                        ElectricItem.manager.use(itemStack, 1000.0D, null);
                        ret = true;
                    }
                    nbtData.putByte("speedTicker", speedTicker);
                    float speed = 0.22F;
                    if (player.isInWater()) {
                        speed = 0.1F;
                        if (IUCore.keyboard.isJumpKeyDown(player)) {
                            player.getDeltaMovement().add(0, 0.10000000149011612D, 0);
                        }
                    }
                    moveRelative(player, 0.0F, (float) 0, 1.0F, (float) (speed));
                }
                if (this.listCapability.contains(EnumCapability.BAGS)) {
                    if (IUCore.keyboard.isLeggingsMode(player) && IUCore.keyboard.isBootsMode(player) && toggleTimer == 0) {
                        toggleTimer = 10;
                        if (!world.isClientSide()) {
                            save(itemStack, player);
                            CustomPacketBuffer growingBuffer = new CustomPacketBuffer();

                            growingBuffer.writeByte(2);

                            growingBuffer.flip();
                            NetworkHooks.openScreen((ServerPlayer) player, getInventory(player, itemStack), buf -> buf.writeBytes(growingBuffer));


                        }
                    }
                }
                if (this.listCapability.contains(EnumCapability.MAGNET)) {
                    boolean magnet = !nbtData.getBoolean("magnet");

                    if (IUCore.keyboard.isLeggingsMode(player) && IUCore.keyboard.isChangeKeyDown(player) && toggleTimer == 0) {
                        toggleTimer = 10;
                        if (!world.isClientSide()) {
                            if (magnet) {
                                if (!world.isClientSide())
                                    IUCore.proxy.messagePlayer(player, "Magnet enabled.");
                            }
                            if (!magnet) {
                                if (!world.isClientSide())
                                    IUCore.proxy.messagePlayer(player, "Magnet disabled.");
                            }
                            nbtData.putBoolean("magnet", magnet);
                        }
                    }
                    if (IUCore.keyboard.isLeggingsMode(player) && IUCore.keyboard.isSaveModeKeyDown(player) && toggleTimer == 0) {
                        toggleTimer = 10;
                        if (!world.isClientSide()) {
                            int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                            mode++;
                            if (mode > 2 || mode < 0) {
                                mode = 0;
                            }

                            ModUtils.NBTSetInteger(itemStack, "mode1", mode);
                            if (!world.isClientSide())
                                IUCore.proxy.messagePlayer(
                                        player,
                                        ChatFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                                + Localization.translate("message.magnet.mode." + mode)
                                );
                        }
                    }
                    int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                    if (mode != 0) {
                        int radius = 11;
                        AABB axisalignedbb = new AABB(player.getX() - radius, player.getY() - radius,
                                player.getZ() - radius, player.getX() + radius, player.getY() + radius, player.getZ() + radius
                        );
                        if (world instanceof ServerLevel) {
                            LevelEntityGetter<Entity> list1 = ((ServerLevel) world).getEntities();
                            List<Entity> list = Lists.newArrayList();
                            list1.get(axisalignedbb, (p_151522_) -> {
                                if (p_151522_ instanceof ItemEntity) {
                                    list.add(p_151522_);
                                }
                            });
                            boolean ret1 = false;
                            for (Entity entityinlist : list) {
                                if (entityinlist instanceof ItemEntity) {
                                    ItemEntity item = (ItemEntity) entityinlist;
                                    if (ElectricItem.manager.canUse(itemStack, 200)) {
                                        if (mode == 1) {

                                            item.moveTo(player.getX(), player.getY(), player.getZ(), 0.0F, 0.0F);
                                            if (!player.level().isClientSide) {
                                                ((ServerPlayer) player).connection.send(new ClientboundTeleportEntityPacket(item));
                                            }
                                            item.setPickUpDelay(0);
                                            ElectricItem.manager.use(itemStack, 200, null);
                                            ret1 = true;
                                        } else if (mode == 2) {
                                            boolean xcoord = item.getX() + 2 >= player.getX() && item.getX() - 2 <= player.getX();
                                            boolean zcoord = item.getZ() + 2 >= player.getZ() && item.getZ() - 2 <= player.getZ();

                                            if (!xcoord && !zcoord) {
                                                item.moveTo(player.getX(), player.getY() - 1, player.getZ());
                                                item.setPickUpDelay(10);
                                            }

                                        }
                                    }

                                }
                            }
                            if (ret1) {
                                player.containerMenu.broadcastChanges();
                            }
                        }
                    }
                }

                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SPEED, itemStack)) {
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300));

                }
                if (!world.isClientSide() && toggleTimer > 0) {
                    toggleTimer = (byte) (toggleTimer - 1);
                    nbtData.putByte("toggleTimer", toggleTimer);
                }
                break;
            case FEET:
                if (!world.isClientSide()) {
                    boolean wasOnGround = !nbtData.contains("wasOnGround") || nbtData.getBoolean("wasOnGround");
                    if (wasOnGround && !player.onGround() && IUCore.keyboard

                            .isJumpKeyDown(player) && IUCore.keyboard
                            .isChangeKeyDown(player)) {
                        ElectricItem.manager.use(itemStack, 4000.0D, null);
                        ret = true;
                    }
                    if (player.onGround() != wasOnGround) {
                        nbtData.putBoolean("wasOnGround", player.onGround());
                    }
                    if (ElectricItem.manager.canUse(itemStack, 4000.0D) && player.onGround()) {

                        this.jumpCharge = 1.0F;
                    }
                    if (player.getDeltaMovement().y >= 0.0D && this.jumpCharge > 0.0F && !player.isInWater()) {
                        if (IUCore.keyboard.isJumpKeyDown(player) && IUCore.keyboard.isBootsMode(player)) {
                            Vec3 deltaMovement = player.getDeltaMovement();
                            if (this.jumpCharge == 1.0F) {
                                player.setDeltaMovement(new Vec3(deltaMovement.x * 3.5D, deltaMovement.y + this.jumpCharge * 0.6F, deltaMovement.z * 3.5D));
                            }
                            this.jumpCharge = (float) (this.jumpCharge * 0.75D);
                        } else if (this.jumpCharge < 1.0F) {
                            this.jumpCharge = 0.0F;
                        }
                    }
                }

                if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.JUMP, itemStack)) {


                    player.addEffect(new MobEffectInstance(MobEffects.JUMP, 300));
                }
                break;

        }

        if (ret) {
            player.containerMenu.broadcastChanges();
        }
    }


    public void save(ItemStack stack, Player player) {
     /*   final NBTTagCompound nbt = ModUtils.nbt(stack);
        nbt.setBoolean("open", true);
        nbt.setInteger("slot_inventory", player.inventory.currentItem);*/
    }

    public boolean onDroppedByPlayer(@Nonnull ItemStack stack, Player player) {
     /*   if (!player.getEntityWorld().isRemote && !ModUtils.isEmpty(stack) && player.openContainer instanceof ContainerBags) {
            ItemStackBags toolbox = ((ContainerBags) player.openContainer).base;
            if (toolbox.isThisContainer(stack)) {
                toolbox.saveAsThrown(stack);
                player.closeScreen();
            }
        }
*/
        return true;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level p_41422_, List<Component> info, TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, p_41422_, info, p_41424_);
        CompoundTag nbtData = ModUtils.nbt(itemStack);

        if (this.listCapability.contains(EnumCapability.FLY) || this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
            info.add(Component.literal(Localization.translate("iu.fly") + " " + ModUtils.Boolean(nbtData.getBoolean("jetpack"))));
        }


        if (!KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            info.add(Component.literal(Localization.translate("press.lshift")));
        }


        if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
            boolean with = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITH);
            boolean without = this.listCapability.contains(EnumCapability.NIGHT_VISION_WITHOUT);
            boolean auto = this.listCapability.contains(EnumCapability.NIGHT_VISION_AUTO);
            if (listCapability.contains(EnumCapability.SPEED)) {
                info.add(Component.literal(Localization.translate("iu.special_armor_speed")));
            }
            if (with || without || auto) {
                info.add(Component.literal(Localization.translate("iu.special_armor_nightvision") + KeyboardClient.armormode.getKey().getDisplayName().getString()));
                if (with) {
                    info.add(Component.literal(Localization.translate("iu.special_armor_nightvision_1")));
                }
                if (without) {
                    info.add(Component.literal(Localization.translate("iu.special_armor_nightvision_2")));
                }
                if (auto) {
                    info.add(Component.literal(Localization.translate("iu.special_armor_nightvision_3")));
                }
            }
            if (this.listCapability.contains(EnumCapability.BIG_JUMP)) {
                info.add(Component.literal(Localization.translate("iu.special armor big jump") + InputConstants.getKey(InputConstants.KEY_SPACE, InputConstants.KEY_SPACE).getDisplayName().getString() + " + " + KeyboardClient.bootsmode.getKey().getDisplayName().getString()));
            }
            if (this.listCapability.contains(EnumCapability.AUTO_JUMP)) {
                info.add(Component.literal(Localization.translate("iu.special_armor_auto_jump")));
            }
            if (this.listCapability.contains(EnumCapability.ACTIVE_EFFECT)) {
                info.add(Component.literal(Localization.translate("iu.special_armor_active_effect")));
            }
            if (this.listCapability.contains(EnumCapability.ALL_ACTIVE_EFFECT)) {
                info.add(Component.literal(Localization.translate("iu.special_armor_all_active_effect")));
            }
            if (this.listCapability.contains(EnumCapability.BAGS)) {
                info.add(Component.literal("Open bag: " + KeyboardClient.bootsmode.getKey().getDisplayName().getString() + " + " + KeyboardClient.leggingsmode.getKey().getDisplayName().getString()));

            /*    final NBTTagCompound nbt = ModUtils.nbt(itemStack);
                if (nbt.hasKey("bag")) {

                    List<BagsDescription> list = new ArrayList<>();
                    final NBTTagCompound nbt1 = nbt.getCompoundTag("bag");
                    int size = nbt1.getInteger("size");
                    for (int i = 0; i < size; i++) {
                        list.add(new BagsDescription(nbt1.getCompoundTag(String.valueOf(i))));
                    }
                    for (BagsDescription description : list) {
                        info.add(TextFormatting.GREEN + "" + description.getCount() + "x " + description
                                .getStack()
                                .getDisplayName());
                    }
                }*/
            }
            if (this.listCapability.contains(EnumCapability.FLY) || this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                info.add(Component.literal(Localization.translate("iu.fly_need")));
                info.add(Component.literal(Localization.translate("iu.changemode_fly") + KeyboardClient.flymode.getKey().getDisplayName().getString()));
            }
            if (this.listCapability.contains(EnumCapability.VERTICAL_FLY)) {

                info.add(Component.literal(Localization.translate("iu.vertical") + KeyboardClient.verticalmode.getKey().getDisplayName().getString()));
            }
            if (this.listCapability.contains(EnumCapability.FOOD)) {
                info.add(Component.translatable(Localization.translate("iu.food_mode_helmet")));
            }
            if (this.listCapability.contains(EnumCapability.JETPACK_FLY)) {
                info.add(Component.literal(Localization.translate("iu.jetpack_fly_chestplate")));

                final CompoundTag nbt = ModUtils.nbt(itemStack);
                final int reTimer = nbt.getInt("reTimer");
                final int timer = nbt.getInt("timer");
                if (timer > 0) {
                    final List<Double> time = ModUtils.Time(timer / 20D);
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    if (time.size() > 0) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                    info.add(Component.literal(Localization.translate("iu.timetoend") + time1 + time2 + time3 + " " + Localization.translate(
                            "iu.jetpack_fly_chestplate_2")));
                }
                if (reTimer > 0) {
                    final List<Double> time = ModUtils.Time(reTimer / 20D);
                    double hours = 0;
                    double minutes = 0;
                    double seconds = 0;
                    if (time.size() > 0) {
                        hours = time.get(0);
                        minutes = time.get(1);
                        seconds = time.get(2);
                    }
                    String time1 = hours > 0 ? ModUtils.getString(hours) + Localization.translate("iu.hour") + "" : "";
                    String time2 = minutes > 0 ? ModUtils.getString(minutes) + Localization.translate("iu.minutes") + "" : "";
                    String time3 = seconds > 0 ? ModUtils.getString(seconds) + Localization.translate("iu.seconds") + "" : "";

                    info.add(Component.translatable(Localization.translate("iu.timetoend") + time1 + time2 + time3 + " " + Localization.translate(
                            "iu.jetpack_fly_chestplate_1")));
                }
            }
            if (itemStack.getItem() == IUItem.spectral_chestplate.getItem()) {
                info.add(Component.literal(Localization.translate("iu.streak") + KeyboardClient.streakmode.getKey().getDisplayName().getString()));
            }
            if (this.listCapability.contains(EnumCapability.MAGNET)) {
                info.add(Component.literal(Localization.translate("iu.magnet_mode") + KeyboardClient.changemode.getKey().getDisplayName().getString() + " + " + KeyboardClient.leggingsmode.getKey().getDisplayName().getString()));
                info.add(Component.literal(Localization.translate("iu.changemode_key") + KeyboardClient.leggingsmode.getKey().getDisplayName().getString() + " + " + KeyboardClient.savemode.getKey().getDisplayName().getString()));
                int mode = ModUtils.NBTGetInteger(itemStack, "mode1");
                if (mode > 2 || mode < 0) {
                    mode = 0;
                }

                info.add(Component.literal(
                        ChatFormatting.GREEN + Localization.translate("message.text.mode") + ": "
                                + Localization.translate("message.magnet.mode." + mode)
                ));
            }


        }
        ModUtils.mode(itemStack, info);
    }


    public boolean useJetpack(Player player) {
        ItemStack jetpack = player.getInventory().armor.get(2);
        if (this.getCharge(jetpack) <= 0.0D) {
            return false;
        } else {
            boolean electric = true;
            float power;
            float dropPercentage;
            power = 0.7F;
            dropPercentage = 0.05F;

            if (this.getCharge(jetpack) / this.getMaxEnergy(jetpack) <= (double) dropPercentage) {
                power = (float) ((double) power * (this.getCharge(jetpack) / (this.getMaxEnergy(jetpack) * (double) dropPercentage)));
            }

            if (IUCore.keyboard.isForwardKeyDown(player)) {
                float retruster = 0.15F;


                retruster += 0.15F;

                float forwardpower = power * retruster * 2.0F;
                if (forwardpower > 0.0F) {
                    player.moveRelative(0.0F, new Vec3(0.0F, 0.4F * forwardpower, 0.02F));
                }
            }

            int worldHeight = player.level().getHeight();
            int maxFlightHeight = electric ? (int) ((float) worldHeight / 1.28F) : worldHeight;
            double y = player.getY();
            if (y > (double) (maxFlightHeight - 25)) {
                if (y > (double) maxFlightHeight) {
                    y = maxFlightHeight;
                }

                power = (float) ((double) power * (((double) maxFlightHeight - y) / 25.0D));
            }

            Vec3 affectedMotion = player.getDeltaMovement();

            double moveY = Math.min(affectedMotion.y + (double) (power * 0.2F), 0.6000000238418579D);
            player.setDeltaMovement(affectedMotion.x, moveY, affectedMotion.z);


            int consume = 2;


            consume += 6;

            if (!player.onGround()) {
                this.use(jetpack, consume);
            }

            player.fallDistance = 0.0F;
            player.walkDist = 0.0F;
            return true;
        }
    }

    @SubscribeEvent
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            ItemStack armor = entity.getItemBySlot(EquipmentSlot.FEET);
            if (armor.getItem() == this) {
                int fallDamage = Math.max((int) event.getDistance() - 10, 0);

                double energyCost = (this.armor.getDamageEnergy() * fallDamage) * (1 - (UpgradeSystem.system.hasModules(
                        EnumInfoUpgradeModules.FALLING_DAMAGE,
                        armor
                ) ?
                        UpgradeSystem.system.getModules(EnumInfoUpgradeModules.FALLING_DAMAGE, armor).number : 0) * 0.25);

                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, 2147483647, true, false, false);
                    event.setCanceled(true);
                }
            }
            if (entity instanceof Player && entity.getItemBySlot(EquipmentSlot.CHEST).getItem() == this) {
                if (entity.getPersistentData().getBoolean("isFlyActive")) {
                    event.setCanceled(true);
                }
            }
        }
    }


    @Override
    public boolean canProvideEnergy(final ItemStack var1) {
        return false;
    }

    @Override
    public double getMaxEnergy(final ItemStack var1) {
        return this.maxCharge;
    }

    @Override
    public short getTierItem(final ItemStack var1) {
        return (short) this.tier;
    }

    @Override
    public double getTransferEnergy(final ItemStack var1) {
        return this.transferLimit;
    }

    @Override
    public ArmorProperties getProperties(
            final LivingEntity player,
            @NotNull final ItemStack armor,
            final DamageSource source,
            final double damage,
            final int slot
    ) {
        if (source.is(DamageTypeTags.IS_FALL) && this.getEquipmentSlot().getIndex() == 0) {
            int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

            int energyPerDamage = (int) (this.armor.getDamageEnergy() - this.armor.getDamageEnergy() * 0.2 * protect);
            int damageLimit = 2147483647;
            if (energyPerDamage > 0) {
                damageLimit = (int) Math.min(
                        damageLimit,
                        25.0D * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                );
            }

            return new ArmorProperties(10, 1.0D, damageLimit);
        }
        double absorptionRatio = getBaseAbsorptionRatio();
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);
        int energyPerDamage = (int) (this.armor.getDamageEnergy() - this.armor.getDamageEnergy() * 0.2 * protect);
        int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0D * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                : 0.0D);
        return new ArmorProperties(10, absorptionRatio, damageLimit);
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }


    public int getArmorDisplay(Player player, @Nonnull ItemStack armor, int slot) {
        return ElectricItem.manager.getCharge(armor) >= this.armor.getDamageEnergy()
                ? (int) Math.round(40.0D * this.getBaseAbsorptionRatio())
                : 0;
    }

    private double getBaseAbsorptionRatio() {
        switch (this.getEquipmentSlot()) {
            case FEET:
                return this.armor.getArmorMulDamage().getBootsMul();
            case HEAD:
                return this.armor.getArmorMulDamage().getHeadMul();
            case CHEST:
                return this.armor.getArmorMulDamage().getChestMul();
            case LEGS:
                return this.armor.getArmorMulDamage().getLeggingsMul();
        }
        return 1;
    }


    @Override
    public void damageArmor(
            final LivingEntity entity,
            @NotNull final ItemStack stack,
            final DamageSource source,
            final float damage,
            final int slot
    ) {
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, stack).number : 0);

        ElectricItem.manager.discharge(
                stack,
                (damage * (this.armor.getDamageEnergy() - this.armor.getDamageEnergy() * 0.2 * protect)),
                2147483647,
                true,
                false,
                false
        );
    }


    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> items) {
        if (this.allowedIn(p_41391_)) {
            ItemStack stack = new ItemStack(this, 1);

            CompoundTag nbt = ModUtils.nbt(stack);
            ElectricItem.manager.charge(stack, 2.147483647E9D, 2147483647, true, false);
            nbt.putInt("ID_Item", Integer.MAX_VALUE);
            items.add(stack);
            ItemStack itemstack = new ItemStack(this, 1);
            nbt = ModUtils.nbt(itemstack);
            nbt.putInt("ID_Item", Integer.MAX_VALUE);
            items.add(itemstack);
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }
}
