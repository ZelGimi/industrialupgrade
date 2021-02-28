package com.denfop.ssp.items.armorbase;


import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.common.Configs;
import com.denfop.ssp.common.Constants;
import com.denfop.ssp.common.Utils;
import com.denfop.ssp.keyboard.SSPKeys;
import com.google.common.base.CaseFormat;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.core.IC2;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.ref.ItemName;
import ic2.core.util.StackUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemAdvancedElectricJetpack extends ItemArmorElectric {

    protected final String name;

    public ItemAdvancedElectricJetpack() {
        this("advancedJetpack");
    }

    protected ItemAdvancedElectricJetpack(String name) {
        this(name, 3000000.0D, 30000.0D, 4);
    }

    protected ItemAdvancedElectricJetpack(String name, double maxCharge, double transferLimit, int tier) {
        super(null, null, EntityEquipmentSlot.CHEST, maxCharge, transferLimit, tier);
        BlocksItems.registerItem((Item) this, SuperSolarPanels.getIdentifier(this.name = name)).setUnlocalizedName(name);
        setMaxDamage(27);
        setMaxStackSize(1);
        setNoRepair();
    }

    public static boolean isHovering(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack).getBoolean("hoverMode");
    }

    @SideOnly(Side.CLIENT)
    public void registerModels(ItemName name) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                0,
                new ModelResourceLocation(Constants.MOD_ID + ":" + CaseFormat.LOWER_CAMEL.to(
                        CaseFormat.LOWER_UNDERSCORE,
                        this.name
                ), null)
        );
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Constants.MOD_ID + ":textures/armour/" + this.name + ".png";
    }

    public String getTranslationKey() {
        return Constants.MOD_ID + "." + super.getUnlocalizedName().substring(4);
    }

    @Nonnull
    public EnumRarity getForgeRarity(@Nonnull ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    public void onArmorTick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbt.getByte("toggleTimer");
        boolean ret = false;
        NBTTagCompound nbtData = Utils.getOrCreateNbtData(player);
        boolean nightVision = nbtData.getBoolean("nightVision");
        short hudMode = nbtData.getShort("HudMode");
        if (SSPKeys.Isremovepoison2(player) && toggleTimer == 0) {
            toggleTimer = 10;
            nightVision = !nightVision;
            if (IC2.platform.isSimulating()) {
                nbtData.setBoolean("nightVision", nightVision);
                if (nightVision) {
                    IC2.platform.messagePlayer(player, "Effects enabled.");
                } else {
                    IC2.platform.messagePlayer(player, "Effects disabled.");
                }
            }
        }
        if (IC2.keyboard.isAltKeyDown(player) && IC2.keyboard.isHudModeKeyDown(player) && toggleTimer == 0) {
            toggleTimer = 10;
            if (hudMode == HudMode.getMaxMode()) {
                hudMode = 0;
            } else {
                ++hudMode;
            }
            if (IC2.platform.isSimulating()) {
                nbtData.setShort("HudMode", hudMode);
                IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hudMode).getTranslationKey()));
            }
        }
        if (IC2.platform.isSimulating() && toggleTimer > 0) {
            final String s = "toggleTimer";
            --toggleTimer;
            nbtData.setByte(s, toggleTimer);
        }
        if (nightVision && IC2.platform.isSimulating() && ElectricItem.manager.use(stack, 1.0, player)) {
            final BlockPos pos = new BlockPos(
                    (int) Math.floor(player.posX),
                    (int) Math.floor(player.posY),
                    (int) Math.floor(player.posZ)
            );
            if (Configs.canCraftDoubleSlabs) {
                player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 300, 0, true, true));
            }
        }
    }

    public float getBaseThrust(ItemStack stack, boolean hover) {
        return hover ? 0.65F : 0.3F;
    }

    public float getBoostThrust(EntityPlayer player, ItemStack stack, boolean hover) {
        return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0D)
                ? (hover ? 0.07F : 0.09F)
                : 0.0F;
    }

    public boolean useBoostPower(ItemStack stack, float boostAmount) {
        return (ElectricItem.manager.discharge(stack, 60.0D, 2147483647, true, false, false) > 0.0D);
    }

    public float getHoverBoost(EntityPlayer player, ItemStack stack, boolean up) {
        if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 60.0D) {
            if (!player.onGround) {
                ElectricItem.manager.discharge(stack, 60.0D, 2147483647, true, false, false);
            }
            return 2.0F;
        }
        return 1.0F;
    }

    public boolean drainEnergy(ItemStack pack, int amount) {
        return (ElectricItem.manager.discharge(pack, (amount * 6), 2147483647, true, false, false) > 0.0D);
    }

    public float getPower(ItemStack stack) {
        return 1.0F;
    }

    public float getDropPercentage(ItemStack stack) {
        return 0.05F;
    }

    public double getChargeLevel(ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) / getMaxCharge(stack);
    }


    public float getHoverMultiplier(ItemStack stack, boolean upwards) {
        return 0.2F;
    }

    public float getWorldHeightDivisor(ItemStack stack) {
        return 1.0F;
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }

    public double getDamageAbsorptionRatio() {
        return 0.0D;
    }

    public int getEnergyPerDamage() {
        return 0;
    }

}
