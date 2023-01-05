package com.denfop.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.core.IC2;
import ic2.core.item.DamageHandler;
import ic2.core.slot.ArmorSlot;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ElectricItemManager implements IElectricItemManager {

    public ElectricItemManager() {
    }

    private static int mapChargeLevelToDamage(double charge, double maxCharge, int maxDamage) {
        if (maxDamage < 2) {
            return 0;
        } else {
            --maxDamage;
            return maxDamage - (int) Util.map(charge, maxCharge, maxDamage);
        }
    }

    public static ItemStack getCharged(Item item, double charge) {
        if (!(item instanceof IElectricItem)) {
            throw new IllegalArgumentException("no electric item");
        } else {
            ItemStack ret = new ItemStack(item);
            ElectricItem.manager.charge(ret, charge, 2147483647, true, false);
            return ret;
        }
    }

    public static void addChargeVariants(Item item, List<ItemStack> list) {
        list.add(getCharged(item, 0.0D));
        list.add(getCharged(item, Double.MAX_VALUE));
    }

    public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        IElectricItem item = (IElectricItem) stack.getItem();

        assert item.getMaxCharge(stack) > 0.0D;
        if (this.getCharge(stack) == item.getMaxCharge(stack)) {
            return 0;
        }
        if (!(amount < 0.0D) && StackUtil.getSize(stack) <= 1 && item.getTier(stack) <= tier) {
            if (!ignoreTransferLimit && amount > item.getTransferLimit(stack)) {
                amount = item.getTransferLimit(stack);
            }

            NBTTagCompound tNBT = StackUtil.getOrCreateNbtData(stack);
            double newCharge = tNBT.getDouble("charge");
            amount = Math.min(amount, item.getMaxCharge(stack) - newCharge);
            if (!simulate) {
                newCharge += amount;
                if (newCharge > 0.0D) {
                    tNBT.setDouble("charge", newCharge);
                } else {
                    tNBT.setDouble("charge", 0);
                }

                if (stack.getItem() instanceof IElectricItem) {
                    item = (IElectricItem) stack.getItem();
                    int maxDamage = DamageHandler.getMaxDamage(stack);
                    DamageHandler.setDamage(stack, mapChargeLevelToDamage(newCharge, item.getMaxCharge(stack), maxDamage), true);
                } else {
                    DamageHandler.setDamage(stack, 0, true);
                }
            }

            return amount;
        } else {
            return 0.0D;
        }
    }

    public double discharge(
            ItemStack stack,
            double amount,
            int tier,
            boolean ignoreTransferLimit,
            boolean externally,
            boolean simulate
    ) {
        IElectricItem item = (IElectricItem) stack.getItem();

        assert item.getMaxCharge(stack) > 0.0D;
        if (!(amount < 0.0D) && StackUtil.getSize(stack) <= 1 && item.getTier(stack) <= tier) {
            if (externally && !item.canProvideEnergy(stack)) {
                return 0.0D;
            } else {
                if (!ignoreTransferLimit && amount > item.getTransferLimit(stack)) {
                    amount = item.getTransferLimit(stack);
                }

                NBTTagCompound tNBT = StackUtil.getOrCreateNbtData(stack);
                double newCharge = tNBT.getDouble("charge");
                amount = Math.min(amount, newCharge);
                if (!simulate) {
                    newCharge -= amount;
                    if (newCharge > 0.0D) {
                        tNBT.setDouble("charge", newCharge);
                    } else {
                        tNBT.setDouble("charge", 0);
                    }

                    if (stack.getItem() instanceof IElectricItem) {
                        item = (IElectricItem) stack.getItem();
                        int maxDamage = DamageHandler.getMaxDamage(stack);
                        DamageHandler.setDamage(
                                stack,
                                mapChargeLevelToDamage(newCharge, item.getMaxCharge(stack), maxDamage),
                                true
                        );
                    } else {
                        DamageHandler.setDamage(stack, 0, true);
                    }
                }

                return amount;
            }
        } else {
            return 0.0D;
        }
    }

    public double getCharge(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getDouble("charge");
    }

    public double getMaxCharge(ItemStack stack) {
        return ((IElectricItem) stack.getItem()).getMaxCharge(stack);
    }

    public boolean canUse(ItemStack stack, double amount) {
        return ElectricItem.manager.getCharge(stack) >= amount;
    }

    public boolean use(ItemStack stack, double amount, EntityLivingBase entity) {

        double transfer = this.getCharge(stack);
        if (transfer >= amount) {
            ElectricItem.manager.discharge(stack, amount, 2147483647, true, false, false);
            if (entity != null) {
                this.chargeFromArmor(stack, entity);
            }

            return true;
        } else {
            return false;
        }
    }

    public void chargeFromArmor(ItemStack target, EntityLivingBase entity) {
        boolean transferred = false;

        for (final EntityEquipmentSlot slot : ArmorSlot.getAll()) {
            ItemStack source = entity.getItemStackFromSlot(slot);
            if (source.isEmpty()) {
                continue;
            }
            if (!(source.getItem() instanceof IElectricItem)) {
                continue;
            }
            int tier;
            if (source.getItem() instanceof IElectricItem) {
                tier = ((IElectricItem) source.getItem()).getTier(target);
            } else {
                tier = 2147483647;
            }

            double transfer = Math.min(this.getCharge(target), this.getCharge(source));
            if (!(transfer <= 0.0D)) {
                transfer = ElectricItem.manager.charge(target, transfer, tier, true, false);
                ElectricItem.manager.discharge(source, transfer, 2147483647, true, false, false);
                transferred = true;

            }
        }

        if (transferred && entity instanceof EntityPlayer && IC2.platform.isSimulating()) {
            ((EntityPlayer) entity).openContainer.detectAndSendChanges();
        }

    }

    public String getToolTip(ItemStack stack) {
        if (stack.getItem() instanceof IElectricItem) {
            double charge = ElectricItem.manager.getCharge(stack);
            return Util.toSiString(charge, 3) + "/" + Util.toSiString(
                    ((IElectricItem) stack.getItem()).getMaxCharge(stack),
                    3
            ) + " EU";
        } else {
            return "";
        }
    }

    public int getTier(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IElectricItem
                ? ((IElectricItem) stack.getItem()).getTier(stack)
                : 0;
    }

}
