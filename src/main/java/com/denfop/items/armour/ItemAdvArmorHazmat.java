package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.api.item.IHazmatLike;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.register.Register;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;

import java.util.Arrays;
import java.util.Iterator;

public class ItemAdvArmorHazmat extends ItemArmorUtility implements IHazmatLike, ISpecialArmor {

    public ItemAdvArmorHazmat(String name, Type type) {
        super(Register.HAZMAT, name, type);
        if (type.getSlot() == EquipmentSlot.FEET) {
            NeoForge.EVENT_BUS.register(this);
        }
        this.armorName = name;

    }

    public static boolean hasCompleteHazmat(LivingEntity living) {
        Iterator<EquipmentSlot> var1 =
                Arrays
                        .stream(EquipmentSlot.values())
                        .filter(slot -> slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND)
                        .iterator();

        EquipmentSlot slot;
        ItemStack stack;
        IHazmatLike hazmat;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            slot = var1.next();
            stack = living.getItemBySlot(slot);
            if (!(stack.getItem() instanceof IHazmatLike)) {
                return false;
            }

            hazmat = (IHazmatLike) stack.getItem();
            if (!hazmat.addsProtection(living, slot, stack)) {
                return false;
            }
        } while (!hazmat.fullyProtects(living, slot, stack));

        return true;
    }

    public static boolean hazmatAbsorbs(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE) && source == IUDamageSource.radiation;
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
            this.nameItem = "item." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int suffix1 = this.type.getSlot() == EquipmentSlot.LEGS ? 2 : 1;
        return Constants.MOD_ID + ":textures/armor/" + this.armorName + "_" + suffix1 + ".png";
    }


    public ISpecialArmor.ArmorProperties getProperties(
            LivingEntity player,
            ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (this.type.getSlot() == EquipmentSlot.HEAD && hazmatAbsorbs(source) && hasCompleteHazmat(player)) {
            if (source.is(DamageTypeTags.IS_FIRE)) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 1));
            }

            return new ISpecialArmor.ArmorProperties(10, 1.0, Integer.MAX_VALUE);
        } else {
            return this.type.getSlot() == EquipmentSlot.FEET && source.is(DamageTypeTags.IS_FALL) ? new ISpecialArmor.ArmorProperties(
                    10,
                    damage < 8.0 ? 1.0 : 0.875,
                    (armor.getMaxDamage() - armor.getDamageValue() + 2) * 2 * 25
            ) : new ISpecialArmor.ArmorProperties(0, 0.05, (armor.getMaxDamage() - armor.getDamageValue() + 2) / 2 * 25);
        }
    }

    public void damageArmor(LivingEntity entity, ItemStack stack, DamageSource source, float damage, int slot) {
        if (!hazmatAbsorbs(source) || !hasCompleteHazmat(entity)) {
            int damageTotal = (int) (damage * 2);
            if (this.getEquipmentSlot() == EquipmentSlot.FEET && source.is(DamageTypeTags.IS_FALL)) {
                damageTotal = (int) ((damage + 1) / 2);
            }
            if (entity instanceof ServerPlayer)
                stack.hurtAndBreak(damageTotal, ((ServerPlayer) entity).serverLevel(), (ServerPlayer) entity, ignored -> {
                });
        }
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
        if (p_41407_ >= Inventory.INVENTORY_SIZE && p_41407_ < Inventory.INVENTORY_SIZE + 4 && p_41406_ instanceof Player player)
            this.onArmorTick(p_41404_, p_41405_, (Player) p_41406_);
    }

    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            ItemStack armor = player.getInventory().armor.get(0);
            if (armor.getItem() == this) {
                int fallDamage = (int) event.getDistance() - 3;
                if (fallDamage >= 8) {
                    return;
                }

                int armorDamage = (fallDamage + 1) / 2;
                if (armorDamage <= armor.getMaxDamage() - armor.getDamageValue() && armorDamage >= 0) {
                    armor.hurtAndBreak(armorDamage, ((ServerPlayer) player).serverLevel(), (ServerPlayer) player, ignored -> {
                    });
                    event.setCanceled(true);
                }
            }
        }

    }

    public boolean isRepairable() {
        return true;
    }

    public int getArmorDisplay(Player player, ItemStack armor, int slot) {
        return 1;
    }


    public void onArmorTick(ItemStack stack, Level world, Player player) {
        if (!world.isClientSide && this.getEquipmentSlot() == EquipmentSlot.HEAD) {
            if (player.isOnFire() && hasCompleteHazmat(player)) {
                if (this.isInLava(player)) {
                    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 20, 0, true, true));
                }

                player.clearFire();
            }


        }

    }

    public boolean isInLava(Player player) {
        int x = (int) Math.floor(player.getX());
        int y = (int) Math.floor(player.getY() + 0.02);
        int z = (int) Math.floor(player.getZ());
        FluidState state = player.level().getFluidState(new BlockPos(x, y, z));
        if (state.is(Fluids.LAVA)) {
            Integer level = state.getAmount();
            if (level > 8)
                level = 0;
            float height = (float) (y + 1) - level / 9F;
            return player.getY() < (double) height;
        } else {
            return false;
        }
    }

    public boolean addsProtection(LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
        return true;
    }

    @Override
    public int getLevel() {
        return 2;
    }


}
