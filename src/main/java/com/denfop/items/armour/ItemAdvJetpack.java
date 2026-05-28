package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.item.upgrade.EnumUpgrades;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.utils.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemAdvJetpack extends ItemArmorEnergy implements EnergyItem, ISpecialArmor, IProperties, UpgradeItem {

    private final String armorName;
    private final double maxStorage;
    private final double transferLimit;
    private final int tier;

    public ItemAdvJetpack(String name, double maxStorage, double transferLimit, int tier) {
        super("", Type.CHESTPLATE, maxStorage, transferLimit, tier);

        this.armorName = name;
        this.maxStorage = maxStorage;
        this.transferLimit = transferLimit;
        this.tier = tier;

        IUCore.runnableListAfterRegisterItem.add(() ->
                UpgradeSystem.system.addRecipe(this, EnumUpgrades.JETPACK.list)
        );
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowedIn(tab)) {
            final ItemStack charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 2.147483647E9D, Integer.MAX_VALUE, true, false);
            items.add(charged);
            items.add(new ItemStack(this, 1));
        }
    }

    @Override
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
            this.nameItem = pathBuilder.toString().split("\\.")[2] + ".name";
        }

        return this.nameItem;
    }

    @Override
    public String[] properties() {
        return new String[]{"", "Demon", "Dark", "Cold", "Ender"};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getItemProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int seed, String property) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        return nbt.getString("mode").equals(property) ? 1.0F : 0.0F;
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity p_41406_, int p_41407_, boolean p_41408_) {

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            NeoForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
        if (p_41407_ >= Inventory.INVENTORY_SIZE && p_41407_ < Inventory.INVENTORY_SIZE + 4 && p_41406_ instanceof Player player)
            this.onArmorTick(itemStack, world, (Player) p_41406_);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, tooltipContext, tooltip, flag);

        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FLY, stack)) {
            tooltip.add(Component.literal(Localization.translate("iu.fly") + " " + ModUtils.Boolean(stack.getOrDefault(DataComponentsInit.JETPACK, false))));
            tooltip.add(Component.literal(Localization.translate("iu.fly_need")));

            if (!KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
                tooltip.add(Component.literal(Localization.translate("press.lshift")));
            }

            if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
                tooltip.add(Component.literal(Localization.translate("iu.changemode_fly") + com.denfop.utils.ModUtils.cleanComponentString(KeyboardClient.flymode.getKey().getDisplayName().getString())));
            }
        }
    }

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.4D;
    }

    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(
            ItemStack stack,
            Entity entity,
            EquipmentSlot slot,
            ArmorMaterial.Layer layer,
            boolean innerModel
    ) {
        int suffix = (this.getEquipmentSlot() == EquipmentSlot.LEGS) ? 2 : 1;
        CompoundTag nbtData = ModUtils.nbt(stack);

        if (!nbtData.getString("mode").isEmpty()) {
            return ResourceLocation.parse(Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + nbtData.getString("mode") + "_" + suffix + ".png");
        }

        return ResourceLocation.parse(Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png");
    }

    public double getCharge(ItemStack itemStack) {
        return ElectricItem.manager.getCharge(itemStack);
    }

    public void use(ItemStack itemStack, double amount) {
        ElectricItem.manager.discharge(itemStack, amount, 2147483647, true, false, false);
    }

    public boolean useJetpack(Player player, boolean hoverMode) {
        ItemStack jetpack = player.getInventory().armor.get(2);
        if (this.getCharge(jetpack) <= 0.0D) {
            return false;
        } else {
            boolean electric = true;
            float power = 0.7F;
            float dropPercentage = 0.05F;

            if (this.getCharge(jetpack) / this.getMaxEnergy(jetpack) <= (double) dropPercentage) {
                power = (float) ((double) power * (this.getCharge(jetpack) / (this.getMaxEnergy(jetpack) * (double) dropPercentage)));
            }

            if (IUCore.keyboard.isForwardKeyDown(player)) {
                float retruster = 0.15F;
                if (hoverMode) {
                    retruster = 1.0F;
                }

                if (electric) {
                    retruster += 0.15F;
                }

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

            double prevmotion = affectedMotion.y;
            double moveY = Math.min(affectedMotion.y + (double) (power * 0.2F), 0.6000000238418579D);
            player.setDeltaMovement(affectedMotion.x, moveY, affectedMotion.z);

            if (hoverMode) {
                float maxHoverY = 0.0F;
                if (IUCore.keyboard.isJumpKeyDown(player)) {
                    maxHoverY = electric ? 0.1F : 0.2F;
                }

                if (IUCore.keyboard.isSneakKeyDown(player)) {
                    maxHoverY = electric ? -0.1F : -0.2F;
                }

                if (moveY > (double) maxHoverY) {
                    moveY = maxHoverY;
                    if (prevmotion > moveY) {
                        moveY = prevmotion;
                    }
                    player.setDeltaMovement(affectedMotion.x, moveY, affectedMotion.z);
                }
            }

            int consume = hoverMode ? 1 : 2;
            if (electric) {
                consume += 6;
            }

            if (!player.onGround()) {
                this.use(jetpack, consume);
            }

            player.fallDistance = 0.0F;
            player.walkDist = 0.0F;
            return true;
        }
    }

    private void dispatchClientJetpackVisuals(
            final Player player,
            final ItemStack itemStack,
            final boolean jetpackEnabled,
            final boolean hoverMode,
            final boolean jetpackUsed
    ) {
        if (!player.level().isClientSide()) {
            return;
        }

        if (player != IUCore.proxy.getPlayerInstance()) {
            return;
        }

        final boolean jumpPressed = IUCore.keyboard.isJumpKeyDown(player);
        final boolean descendPressed = IUCore.keyboard.isSneakKeyDown(player);
        final boolean forwardPressed = IUCore.keyboard.isForwardKeyDown(player);

        final Vec3 motion = player.getDeltaMovement();
        final boolean sustainedFlight = jetpackEnabled
                && !player.onGround()
                && (Math.abs(motion.y) > 0.03D
                || (motion.x * motion.x + motion.z * motion.z) > 0.0025D
                || hoverMode);

        final boolean hoverThrust = hoverMode && (jumpPressed || descendPressed || !player.onGround());
        final boolean thrustActive = jetpackUsed || sustainedFlight || hoverThrust;

        final float energyRatio = (float) Mth.clamp(
                this.getCharge(itemStack) / Math.max(1.0D, this.getMaxEnergy(itemStack)),
                0.0D,
                1.0D
        );

        JetpackVisualClient.tick(
                player,
                thrustActive,
                jetpackEnabled,
                hoverMode,
                jumpPressed,
                descendPressed,
                forwardPressed,
                energyRatio
        );
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return this.maxStorage;
    }

    @Override
    public short getTierItem(ItemStack itemStack) {
        return (short) this.tier;
    }

    @Override
    public double getTransferEnergy(ItemStack itemStack) {
        return this.transferLimit;
    }


    public void onArmorTick(@Nonnull ItemStack itemStack, @Nonnull Level world, Player player) {
        if (!player.getInventory().armor.get(2).is(itemStack.getItem())) {
            return;
        }

        CompoundTag nbtData = ModUtils.nbt(itemStack);
        boolean hoverMode = nbtData.getBoolean("hoverMode");
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean jetpackUsed = false;

        if (IUCore.keyboard.isJumpKeyDown(player) && IUCore.keyboard.isVerticalMode(player) && toggleTimer == 0) {
            toggleTimer = 10;
            hoverMode = !hoverMode;

            if (!player.level().isClientSide()) {
                nbtData.putBoolean("hoverMode", hoverMode);
                if (hoverMode) {
                    IUCore.proxy.messagePlayer(player, "Hover Mode enabled.");
                } else {
                    IUCore.proxy.messagePlayer(player, "Hover Mode disabled.");
                }
            }
        }

        if (itemStack.getOrDefault(DataComponentsInit.JETPACK, false)) {
            player.fallDistance = 0.0F;
            if (nbtData.getBoolean("jump")
                    && !nbtData.getBoolean("canFly")
                    && !player.getAbilities().mayfly
                    && IUCore.keyboard.isJumpKeyDown(player)
                    && !nbtData.getBoolean("isFlyActive")
                    && toggleTimer == 0) {
                toggleTimer = 10;
                nbtData.putBoolean("canFly", true);
            }

            nbtData.putBoolean("jump", !player.onGround());

            if (!player.onGround()) {
                if (ElectricItem.manager.canUse(itemStack, 25)) {
                    ElectricItem.manager.use(itemStack, 25, null);
                } else {
                    itemStack.set(DataComponentsInit.JETPACK, false);
                }
            }
        }

        boolean jetpack = itemStack.getOrDefault(DataComponentsInit.JETPACK, false);

        if ((IUCore.keyboard.isJumpKeyDown(player) || hoverMode) && !jetpack) {
            jetpackUsed = this.useJetpack(player, hoverMode);
        }

        if (IUCore.keyboard.isFlyModeKeyDown(player)
                && toggleTimer == 0
                && UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FLY, itemStack)) {
            toggleTimer = 10;
            jetpack = !jetpack;

            if (!player.level().isClientSide()) {
                itemStack.set(DataComponentsInit.JETPACK, jetpack);
                if (jetpack) {
                    IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info"));
                } else {
                    IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));
                }
            }
        }

        if (!player.level().isClientSide() && toggleTimer > 0) {
            --toggleTimer;
            nbtData.putByte("toggleTimer", toggleTimer);
        }

        this.dispatchClientJetpackVisuals(
                player,
                itemStack,
                itemStack.getOrDefault(DataComponentsInit.JETPACK, false),
                hoverMode,
                jetpackUsed
        );

        boolean fireResistance = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack);
        if (fireResistance) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300));
        }

        int resistance = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack)
                ? UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number
                : 0;

        if (resistance != 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, resistance));
        }

        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300));
        }

        if (jetpackUsed) {
            player.containerMenu.broadcastChanges();
        }
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    @Override
    public ArmorProperties getProperties(
            LivingEntity player,
            @Nonnull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        return new ArmorProperties(0, 0.0D, 0, this);
    }

    @Override
    public int getArmorDisplay(Player player, @Nonnull ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.JETPACK.list;
    }
}