package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IVolcanoArmor;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Iterator;

public class ItemArmorVolcanoHazmat extends ItemArmorUtility implements IVolcanoArmor, IModelRegister {

    public ItemArmorVolcanoHazmat(String name, EntityEquipmentSlot type) {
        super(name, type);
        this.setMaxDamage(64);
        if (this.armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        this.armorName = name;
        setUnlocalizedName(armorName);
        setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(armorName)).setUnlocalizedName(armorName);
        IUCore.proxy.addIModelRegister(this);

    }

    public static boolean hasCompleteHazmat(EntityLivingBase living) {
        Iterator<EntityEquipmentSlot> var1 =
                Arrays
                        .stream(EntityEquipmentSlot.values())
                        .filter(slot -> slot != EntityEquipmentSlot.MAINHAND && slot != EntityEquipmentSlot.OFFHAND)
                        .iterator();

        EntityEquipmentSlot slot;
        ItemStack stack;
        IVolcanoArmor hazmat;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            slot = var1.next();
            stack = living.getItemStackFromSlot(slot);
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
        return source == DamageSource.IN_FIRE || source == DamageSource.IN_WALL || source == DamageSource.LAVA || source == DamageSource.HOT_FLOOR || source == DamageSource.ON_FIRE;
    }

    @Override
    public void registerModels() {
        registerModels(this.armorName);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        int suffix1 = this.armorType == EntityEquipmentSlot.LEGS ? 2 : 1;
        return Constants.MOD_ID + ":textures/armor/" + "volcano_hazmat" + '_' + suffix1 + ".png";
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
        String[] mode = {""};
        for (final String s : mode) {
            ModelBakery.registerItemVariants(this, getModelLocation1(name, s));

        }

    }

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase player,
            ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (this.armorType == EntityEquipmentSlot.HEAD && hazmatAbsorbs(source) && hasCompleteHazmat(player)) {
            if (source == DamageSource.IN_FIRE || source == DamageSource.LAVA || source == DamageSource.HOT_FLOOR) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1));
            }

            return new ISpecialArmor.ArmorProperties(10, 1.0, Integer.MAX_VALUE);
        } else {
            return this.armorType == EntityEquipmentSlot.FEET && source == DamageSource.FALL ? new ISpecialArmor.ArmorProperties(
                    10,
                    damage < 8.0 ? 1.0 : 0.875,
                    (armor.getMaxDamage() - armor.getItemDamage() + 2) * 2 * 25
            ) : new ISpecialArmor.ArmorProperties(0, 0.05, (armor.getMaxDamage() - armor.getItemDamage() + 2) / 2 * 25);
        }
    }

    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        if (!hazmatAbsorbs(source) || !hasCompleteHazmat(entity)) {
            int damageTotal = damage * 2;
            if (this.armorType == EntityEquipmentSlot.FEET && source == DamageSource.FALL) {
                damageTotal = (damage + 1) / 2;
            }

            stack.damageItem(damageTotal, entity);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IUCore.proxy.isSimulating() && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            ItemStack armor = player.inventory.armorInventory.get(0);
            if (armor.getItem() == this) {
                int fallDamage = (int) event.getDistance() - 3;
                if (fallDamage >= 8) {
                    return;
                }

                int armorDamage = (fallDamage + 1) / 2;
                if (armorDamage <= armor.getMaxDamage() - armor.getItemDamage() && armorDamage >= 0) {
                    armor.damageItem(armorDamage, player);
                    event.setCanceled(true);
                }
            }
        }

    }

    public boolean isRepairable() {
        return true;
    }

    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 1;
    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        if (!world.isRemote && this.armorType == EntityEquipmentSlot.HEAD) {
            if (player.isBurning() && hasCompleteHazmat(player)) {
                if (this.isInLava(player)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20, 0, true, true));
                }

                player.extinguish();
            }


        }

    }

    public boolean isInLava(EntityPlayer player) {
        int x = (int) Math.floor(player.posX);
        int y = (int) Math.floor(player.posY + 0.02);
        int z = (int) Math.floor(player.posZ);
        IBlockState state = player.getEntityWorld().getBlockState(new BlockPos(x, y, z));
        if (state.getBlock() instanceof BlockLiquid && (state.getMaterial() == Material.LAVA || state.getMaterial() == Material.FIRE)) {
            float height = (float) (y + 1) - BlockLiquid.getLiquidHeightPercent(state.getValue(BlockLiquid.LEVEL));
            return player.posY < (double) height;
        } else {
            return false;
        }
    }

    public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return true;
    }

    public boolean isMetalArmor(ItemStack itemstack, EntityPlayer player) {
        return false;
    }

}
