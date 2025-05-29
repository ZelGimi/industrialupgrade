package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.api.item.IHazmatLike;
import com.denfop.api.item.IVolcanoArmor;
import com.denfop.componets.Fluids;
import com.denfop.items.armour.material.ArmorMaterials;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Arrays;
import java.util.Iterator;

public class ItemArmorVolcanoHazmat extends ItemArmorUtility implements IHazmatLike, ISpecialArmor {

    public ItemArmorVolcanoHazmat(String name, Type type) {
        super(ArmorMaterials.HAZMAT, name, type);
        if (type.getSlot() == EquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        this.armorName = name;

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
    public static boolean hasCompleteHazmat(LivingEntity living) {
        Iterator<EquipmentSlot> var1 =
                Arrays
                        .stream(EquipmentSlot.values())
                        .filter(slot -> slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND)
                        .iterator();

        EquipmentSlot slot;
        ItemStack stack;
        IVolcanoArmor hazmat;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            slot = var1.next();
            stack = living.getItemBySlot(slot);
            if (!(stack.getItem() instanceof IVolcanoArmor)) {
                return false;
            }

            hazmat = (IVolcanoArmor) stack.getItem();
            if (!hazmat.addsProtection(living, slot, stack)) {
                return false;
            }
        } while (!hazmat.fullyProtects(living, slot, stack));

        return true;
    }

    public static boolean hazmatAbsorbs(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE);
    }


    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int suffix1 = this.getEquipmentSlot() == EquipmentSlot.LEGS ? 2 : 1;
        return Constants.MOD_ID + ":textures/armor/" + "volcano_hazmat" + '_' + suffix1 + ".png";
    }


    public ISpecialArmor.ArmorProperties getProperties(
            LivingEntity player,
            ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (this.getEquipmentSlot() == EquipmentSlot.HEAD && hazmatAbsorbs(source) && hasCompleteHazmat(player)) {
            if (source.is(DamageTypeTags.IS_FIRE)) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60, 1));
            }

            return new ISpecialArmor.ArmorProperties(10, 1.0, Integer.MAX_VALUE);
        } else {
            return this.getEquipmentSlot() == EquipmentSlot.FEET && source.is(DamageTypeTags.IS_FALL) ? new ISpecialArmor.ArmorProperties(
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
                stack.hurt(damageTotal, entity.getRandom(), (ServerPlayer) entity);
        }
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
                    armor.hurt(armorDamage, event.getEntity().getRandom(), player);
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


}
