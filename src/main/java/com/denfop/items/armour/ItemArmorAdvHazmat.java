package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.api.item.IHazmatLike;
import com.denfop.damagesource.IUDamageSource;
import com.denfop.register.Register;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
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

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;

public class ItemArmorAdvHazmat extends ItemArmor implements IHazmatLike, IModelRegister, ISpecialArmor {

    private final String name;

    public ItemArmorAdvHazmat(String name, EntityEquipmentSlot type) {
        super(ArmorMaterial.DIAMOND, -1, type);
        this.setMaxDamage(64);
        if (this.armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        setCreativeTab(IUCore.EnergyTab);
        this.name = name;
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);

    }

    public static boolean hasCompleteHazmat(EntityLivingBase living) {
        Iterator var1 =
                Arrays
                        .stream(EntityEquipmentSlot.values())
                        .filter(slot -> slot != EntityEquipmentSlot.MAINHAND && slot != EntityEquipmentSlot.OFFHAND)
                        .iterator();

        EntityEquipmentSlot slot;
        ItemStack stack;
        IHazmatLike hazmat;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            slot = (EntityEquipmentSlot) var1.next();
            stack = living.getItemStackFromSlot(slot);
            if (stack.isEmpty() || !(stack.getItem() instanceof IHazmatLike)) {
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
        return source == DamageSource.IN_FIRE || source == DamageSource.IN_WALL || source == DamageSource.LAVA || source == DamageSource.HOT_FLOOR || source == DamageSource.ON_FIRE || source == IUDamageSource.radiation;
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation1(String name) {
        final String loc = Constants.MOD_ID +
                ':' +
                "armour" + "/" + name;

        return new ModelResourceLocation(loc, null);
    }

    public int getItemEnchantability() {
        return 0;
    }

    public boolean getIsRepairable(@Nonnull ItemStack par1ItemStack, @Nonnull ItemStack par2ItemStack) {
        return false;
    }

    public String getArmorTexture(
            @Nonnull ItemStack stack,
            @Nonnull Entity entity,
            @Nonnull EntityEquipmentSlot slot,
            @Nonnull String type
    ) {

        int suffix = this.armorType.getSlotIndex() == 2 ? 2 : 1;
        return Constants.TEXTURES + ":textures/armor/" + this.name + "_" + suffix + ".png";
    }

    public ArmorProperties getProperties(
            EntityLivingBase player,
            @Nonnull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (this.armorType == EntityEquipmentSlot.HEAD && hazmatAbsorbs(source) && hasCompleteHazmat(player)) {
            if (source == DamageSource.IN_FIRE || source == DamageSource.LAVA || source == DamageSource.HOT_FLOOR) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 60, 1));
            }

            return new ArmorProperties(10, 1.0D, 2147483647);
        } else {
            return this.armorType == EntityEquipmentSlot.FEET && source == DamageSource.FALL ? new ArmorProperties(
                    10,
                    damage < 8.0D ? 1.0D : 0.875D,
                    (armor.getMaxDamage() - armor.getItemDamage() + 2) * 2 * 25
            ) : new ArmorProperties(0, 0.05D, (armor.getMaxDamage() - armor.getItemDamage() + 2) / 2 * 25);
        }
    }

    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
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
            if (!armor.isEmpty() && armor.getItem() == this) {
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

    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return 1;
    }

    public void onArmorTick(World world, @Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
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
        int y = (int) Math.floor(player.posY + 0.02D);
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

    @Override
    public void registerModels() {
        registerModels(this.name);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(final String name) {
        ModelLoader.setCustomMeshDefinition(this, stack -> getModelLocation1(name));


        ModelBakery.registerItemVariants(this, getModelLocation1(name));


    }

}
