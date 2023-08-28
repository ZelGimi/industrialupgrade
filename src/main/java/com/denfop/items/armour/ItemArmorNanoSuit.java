package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.register.Register;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
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
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemArmorNanoSuit extends ItemArmorEnergy implements IModelRegister, IUpgradeItem {

    public ItemArmorNanoSuit(String name, EntityEquipmentSlot armorType) {
        super("nano", armorType, 1000000.0, 1600.0, 3);
        if (armorType == EntityEquipmentSlot.FEET) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        this.armorName = name;
        setUnlocalizedName(name);
        setCreativeTab(IUCore.EnergyTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(name)).setUnlocalizedName(name);
        IUCore.proxy.addIModelRegister(this);
        switch (armorType) {
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(
            final ItemStack p_77624_1_,
            @Nullable final World p_77624_2_,
            final List<String> p_77624_3_,
            final ITooltipFlag p_77624_4_
    ) {
        if (p_77624_1_.getItem() == IUItem.nano_helmet) {
            p_77624_3_.add("Nightvision Key: " + Keyboard.getKeyName(Math.abs(KeyboardClient.armormode.getKeyCode())));
        }

        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
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
        registerModels(this.armorName);
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

        if (suffix == 1) {
            return Constants.TEXTURES + ":textures/armor/" + "nano_1.png";
        } else {
            return Constants.TEXTURES + ":textures/armor/" + "nano_2.png";
        }

    }

    public ISpecialArmor.ArmorProperties getProperties(
            EntityLivingBase player,
            ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        if (source == DamageSource.FALL && this.armorType == EntityEquipmentSlot.FEET) {
            int energyPerDamage = this.getEnergyPerDamage();
            int damageLimit = Integer.MAX_VALUE;
            if (energyPerDamage > 0) {
                damageLimit = (int) Math.min(
                        damageLimit,
                        25.0 * ElectricItem.manager.getCharge(armor) / (double) energyPerDamage
                );
            }

            return new ISpecialArmor.ArmorProperties(10, damage < 8.0 ? 1.0 : 0.875, damageLimit);
        } else {
            return super.getProperties(player, armor, source, damage, slot);
        }
    }

    @SubscribeEvent(
            priority = EventPriority.LOW
    )
    public void onEntityLivingFallEvent(LivingFallEvent event) {
        if (IUCore.proxy.isSimulating() && event.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) event.getEntity();
            ItemStack armor = entity.getItemStackFromSlot(EntityEquipmentSlot.FEET);
            if (armor.getItem() == this) {
                int fallDamage = (int) event.getDistance() - 3;
                if (fallDamage >= 8) {
                    return;
                }

                double energyCost = this.getEnergyPerDamage() * fallDamage;
                if (energyCost <= ElectricItem.manager.getCharge(armor)) {
                    ElectricItem.manager.discharge(armor, energyCost, Integer.MAX_VALUE, true, false, false);
                    event.setCanceled(true);
                }
            }
        }

    }

    public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbtData = ModUtils.nbt(stack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        if (this.armorType == EntityEquipmentSlot.HEAD) {
            boolean Nightvision = nbtData.getBoolean("Nightvision");
            if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
                toggleTimer = 10;
                Nightvision = !Nightvision;
                if (IUCore.proxy.isSimulating()) {
                    nbtData.setBoolean("Nightvision", Nightvision);
                    if (Nightvision) {
                        IUCore.proxy.messagePlayer(player, "Nightvision enabled.");
                    } else {
                        IUCore.proxy.messagePlayer(player, "Nightvision disabled.");
                    }
                }
            }


            if (IUCore.proxy.isSimulating() && toggleTimer > 0) {
                --toggleTimer;
                nbtData.setByte("toggleTimer", toggleTimer);
            }

            if (Nightvision && IUCore.proxy.isSimulating() && ElectricItem.manager.use(stack, 1.0, player)) {
                BlockPos pos = new BlockPos(
                        (int) Math.floor(player.posX),
                        (int) Math.floor(player.posY),
                        (int) Math.floor(player.posZ)
                );
                int skylight = player.getEntityWorld().getLightFromNeighbors(pos);
                if (skylight > 8) {
                    IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                    player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 100, 0, true, true));
                } else {
                    IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);
                    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 300, 0, true, true));
                }

                ret = true;
            }

        }

        if (ret) {
            player.inventoryContainer.detectAndSendChanges();
        }

    }

    public double getDamageAbsorptionRatio() {
        return 0.9;
    }

    public int getEnergyPerDamage() {
        return 5000;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }


}
