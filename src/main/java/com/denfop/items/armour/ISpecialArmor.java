package com.denfop.items.armour;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;


public interface ISpecialArmor {
    static boolean hasCompleteArmor(LivingEntity living) {
        EquipmentSlot[] var1 = EquipmentSlot.values();
        int var2 = var1.length;

        for (EquipmentSlot slot : var1) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack stack = living.getItemBySlot(slot);
                if (stack.isEmpty() || !(stack.getItem() instanceof ISpecialArmor hazmat)) {
                    return false;
                }

            }

        }

        return true;
    }

    public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
        float f = 2.0F + toughnessAttribute / 4.0F;
        float f1 = Mth.clamp(totalArmor - damage / f, totalArmor * 0.2F, 20.0F);
        return damage * (1.0F - f1 / 25.0F);
    }

    public static float getDamageAfterMagicAbsorb(float damage, float enchantModifiers) {
        float f = Mth.clamp(enchantModifiers, 0.0F, 20.0F);
        return damage * (1.0F - f / 25.0F);
    }

    ArmorProperties getProperties(LivingEntity player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot);

    int getArmorDisplay(Player player, @Nonnull ItemStack armor, int slot);

    void damageArmor(LivingEntity entity, @Nonnull ItemStack stack, DamageSource source, float damage, int slot);

    default boolean handleUnblockableDamage(LivingEntity entity, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        return false;
    }

    public static class ArmorProperties implements Comparable<ArmorProperties> {
        private static final boolean DEBUG = false; //Only enable this if you wish to be spammed with debugging information.
        public int Priority = 0;
        public int AbsorbMax = Integer.MAX_VALUE;
        public double AbsorbRatio = 0;
        public double Armor = 0;        //Additional armor, separate from the armor added by vanilla attributes.
        public double Toughness = 0;        //Additional toughness, separate from the armor added by vanilla attributes.
        public int Slot = 0;
        //Left it in because I figured it'd be useful for modders developing custom armor.


        public ArmorProperties(int priority, double ratio, int max) {
            Priority = priority;
            AbsorbRatio = ratio;
            Armor = 0;
            Toughness = 0;
            AbsorbMax = max;
        }


        public static float applyArmor(LivingEntity entity, NonNullList<ItemStack> inventory, DamageSource source, double damage) {
            if (DEBUG) {
                System.out.println("Start: " + damage);
            }

            double totalArmor = entity.getArmorValue();
            double totalToughness = entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

            if (source.is(DamageTypeTags.BYPASSES_ARMOR)) {
                totalArmor = 0;
                totalToughness = 0;
            }

            ArrayList<ArmorProperties> dmgVals = new ArrayList<ArmorProperties>();
            for (int slot = 0; slot < inventory.size(); slot++) {
                ItemStack stack = inventory.get(slot);

                if (stack.isEmpty()) {
                    continue;
                }

                ArmorProperties prop = null;
                if (stack.getItem() instanceof ISpecialArmor) {
                    if (!source.is(DamageTypeTags.BYPASSES_ARMOR) || ((ISpecialArmor) stack.getItem()).handleUnblockableDamage(entity, stack, source, damage, slot)) {
                        ISpecialArmor armor = (ISpecialArmor) stack.getItem();
                        prop = armor.getProperties(entity, stack, source, damage, slot).copy();
                        totalArmor += prop.Armor;
                        totalToughness += prop.Toughness;
                    }
                } else {
                    continue;
                }
                if (prop != null) {
                    prop.Slot = slot;
                    dmgVals.add(prop);
                }
            }
            if (!dmgVals.isEmpty()) {
                ArmorProperties[] props = dmgVals.toArray(new ArmorProperties[0]);
                StandardizeList(props, damage);
                int level = props[0].Priority;
                double ratio = 0;
                for (ArmorProperties prop : props) {
                    if (level != prop.Priority) {
                        damage -= (damage * ratio);
                        ratio = 0;
                        level = prop.Priority;
                    }
                    ratio += prop.AbsorbRatio;

                    double absorb = damage * prop.AbsorbRatio;
                    if (absorb > 0) {
                        ItemStack stack = inventory.get(prop.Slot);
                        if (stack.getItem() instanceof ISpecialArmor) {
                            ((ISpecialArmor) stack.getItem()).damageArmor(entity, stack, source, (float) absorb, prop.Slot);
                        }
                        if (stack.isEmpty()) {
                            inventory.set(prop.Slot, ItemStack.EMPTY);
                        }
                    }
                }
                damage -= (damage * ratio);
            }
            if (damage > 0 && (totalArmor > 0 || totalToughness > 0)) {
                double armorDamage = Math.max(1.0F, damage / 4.0F);

                for (int i = 0; i < inventory.size(); i++) {
                    if (inventory.get(i).getItem() instanceof ArmorItem && inventory.get(i).getItem() instanceof ISpecialArmor) {

                        inventory.get(i).hurtAndBreak((int) armorDamage, (ServerLevel) entity.level(), entity, ignored -> {

                        });
                        if (inventory.get(i).getCount() == 0) {
                            inventory.set(i, ItemStack.EMPTY);
                        }
                    }
                }
                damage = getDamageAfterAbsorb((float) damage, (float) totalArmor, (float) totalToughness);
            }
            if (DEBUG) {
                System.out.println("Return: " + (int) (damage) + " " + damage);
            }
            return (float) (damage);
        }


        private static void StandardizeList(ArmorProperties[] armor, double damage) {
            Arrays.sort(armor);

            int start = 0;
            double total = 0;
            int priority = armor[0].Priority;
            int pStart = 0;
            boolean pChange = false;
            boolean pFinished = false;

            if (DEBUG) {
                for (ArmorProperties prop : armor) {
                    System.out.println(prop);
                }
                System.out.println("========================");
            }

            for (int x = 0; x < armor.length; x++) {
                total += armor[x].AbsorbRatio;
                if (x == armor.length - 1 || armor[x].Priority != priority) {
                    if (armor[x].Priority != priority) {
                        total -= armor[x].AbsorbRatio;
                        x--;
                        pChange = true;
                    }
                    if (total > 1) {
                        for (int y = start; y <= x; y++) {
                            double newRatio = armor[y].AbsorbRatio / total;
                            if (newRatio * damage > armor[y].AbsorbMax) {
                                armor[y].AbsorbRatio = (double) armor[y].AbsorbMax / damage;
                                total = 0;
                                for (int z = pStart; z <= y; z++) {
                                    total += armor[z].AbsorbRatio;
                                }
                                start = y + 1;
                                x = y;
                                break;
                            } else {
                                armor[y].AbsorbRatio = newRatio;
                                pFinished = true;
                            }
                        }
                        if (pChange && pFinished) {
                            damage -= (damage * total);
                            total = 0;
                            start = x + 1;
                            priority = armor[start].Priority;
                            pStart = start;
                            pChange = false;
                            pFinished = false;
                            if (damage <= 0) {
                                for (int y = x + 1; y < armor.length; y++) {
                                    armor[y].AbsorbRatio = 0;
                                }
                                break;
                            }
                        }
                    } else {
                        for (int y = start; y <= x; y++) {
                            total -= armor[y].AbsorbRatio;
                            if (damage * armor[y].AbsorbRatio > armor[y].AbsorbMax) {
                                armor[y].AbsorbRatio = (double) armor[y].AbsorbMax / damage;
                            }
                            total += armor[y].AbsorbRatio;
                        }
                        damage -= (damage * total);
                        total = 0;
                        if (x != armor.length - 1) {
                            start = x + 1;
                            priority = armor[start].Priority;
                            pStart = start;
                            pChange = false;
                            if (damage <= 0) {
                                for (int y = x + 1; y < armor.length; y++) {
                                    armor[y].AbsorbRatio = 0;
                                }
                                break;
                            }
                        }
                    }
                }
            }
            if (DEBUG) {
                for (ArmorProperties prop : armor) {
                    System.out.println(prop);
                }
            }
        }

        @Override
        public int compareTo(ArmorProperties o) {
            if (o.Priority != Priority) {
                return o.Priority - Priority;
            }
            double left = (AbsorbRatio == 0 ? 0 : AbsorbMax * 100.0D / AbsorbRatio);
            double right = (o.AbsorbRatio == 0 ? 0 : o.AbsorbMax * 100.0D / o.AbsorbRatio);
            return (int) (left - right);
        }

        @Override
        public String toString() {
            return String.format("%d, %d, %f, %d", Priority, AbsorbMax, AbsorbRatio, (AbsorbRatio == 0 ? 0 : (int) (AbsorbMax * 100.0D / AbsorbRatio)));
        }

        public ArmorProperties copy() {
            ArmorProperties copy = new ArmorProperties(Priority, AbsorbRatio, AbsorbMax);
            copy.Armor = Armor;
            copy.Toughness = Toughness;
            return copy;
        }
    }
}
