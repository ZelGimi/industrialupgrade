package com.denfop.ssp.items.armor;

import com.denfop.ssp.SuperSolarPanels;
import com.denfop.ssp.common.Configs;
import com.denfop.ssp.common.Constants;
import com.denfop.ssp.items.armorbase.ItemLeggins;
import com.denfop.ssp.keyboard.SSPKeys;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.core.IC2;
import ic2.core.init.Localization;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import ic2.core.util.StackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemArmorQuantumLeggings extends ItemLeggins {

    public ItemArmorQuantumLeggings() {
        super("graviLeggins", Configs.maxCharge5, Configs.transferLimit5, Configs.tier5);
        this.setCreativeTab(SuperSolarPanels.SSPTab);
    }

    @Override
    public String getArmorTexture(final ItemStack stack, final Entity entity, final EntityEquipmentSlot slot, final String type) {
        return Constants.MOD_ID + ":textures/armour/" + this.name + "Overlay" + ".png";
    }

    @Override
    public void onArmorTick(final World world, final EntityPlayer player, final ItemStack stack) {
        super.onArmorTick(world, player, stack);
        player.extinguish();
        final NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        byte toggleTimer = nbtData.getByte("toggleTimer");
        boolean ret = false;
        final boolean enableQuantumSpeedOnSprint = !IC2.platform.isRendering() || ConfigUtil.getBool(
                MainConfig.get(),
                "misc/quantumSpeedOnSprint"
        );
        if (ElectricItem.manager.canUse(
                stack,
                1000.0
        ) && (player.onGround || player.isInWater()) && IC2.keyboard.isForwardKeyDown(player) && ((enableQuantumSpeedOnSprint && player
                .isSprinting()) || (!enableQuantumSpeedOnSprint && IC2.keyboard.isBoostKeyDown(player)))) {
            byte speedTicker = nbtData.getByte("speedTicker");
            ++speedTicker;
            if (speedTicker >= 10) {
                speedTicker = 0;
                ElectricItem.manager.use(stack, 1000.0, null);
            }
            nbtData.setByte("speedTicker", speedTicker);
            float speed = 0.22f;
            if (player.isInWater()) {
                speed = 0.12f;
                if (IC2.keyboard.isJumpKeyDown(player)) {
                    player.motionY += 0.12000000149011612;
                }
            }
            player.moveRelative(0.0f, 0.0f, 1.0f, speed);
        }
        IC2.platform.profilerEndSection();


        boolean nightVision = nbtData.getBoolean("nightVision");
        short hubMode = nbtData.getShort("HudMode");
        if (SSPKeys.Isremovepoison(player) && toggleTimer == 0) {
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
            if (hubMode == HudMode.getMaxMode()) {
                hubMode = 0;
            } else {
                ++hubMode;
            }
            if (IC2.platform.isSimulating()) {
                nbtData.setShort("HudMode", hubMode);
                IC2.platform.messagePlayer(player, Localization.translate(HudMode.getFromID(hubMode).getTranslationKey()));
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
            final int skylight = player.getEntityWorld().getLightFromNeighbors(pos);
            if (Configs.canCraftHSP) {
                player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 300, 2, true, true));
            } else {
                return;
            }
            if (Configs.canCraftHSH) {
                player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 300, 2, true, true));
            }
        }
    }

    @Override
    public float getBaseThrust(final ItemStack stack, final boolean hover) {
        return hover ? 1.0f : 0.5f;
    }

    @Override
    public float getBoostThrust(final EntityPlayer player, final ItemStack stack, final boolean hover) {
        return (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0)
                ? (hover ? 0.1f : 0.3f)
                : 0.0f;
    }

    @Override
    public boolean useBoostPower(final ItemStack stack, final float boostAmount) {
        return ElectricItem.manager.discharge(stack, 834.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }

    @Override
    public float getHoverBoost(final EntityPlayer player, final ItemStack stack, final boolean up) {
        if (IC2.keyboard.isBoostKeyDown(player) && ElectricItem.manager.getCharge(stack) >= 834.0) {
            if (!player.onGround) {
                ElectricItem.manager.discharge(stack, 834.0, Integer.MAX_VALUE, true, false, false);
            }
            return 3.0f;
        }
        return 1.0f;
    }

    @Override
    public boolean drainEnergy(final ItemStack pack, final int amount) {
        return ElectricItem.manager.discharge(pack, 278.0, Integer.MAX_VALUE, true, false, false) > 0.0;
    }

    @Override
    public float getPower(final ItemStack stack) {
        return 1.5f;
    }

    @Override
    public float getDropPercentage(final ItemStack stack) {
        return 0.01f;
    }

    @Override
    public float getHoverMultiplier(final ItemStack stack, final boolean upwards) {
        return 0.25f;
    }

    @Override
    public float getWorldHeightDivisor(final ItemStack stack) {
        return 0.91071427f;
    }

    @Override
    public boolean canProvideEnergy(final ItemStack stack) {
        return true;
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 1.1;
    }

    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }

    public void func_82813_b(final ItemStack stack, final int colour) {
        this.getDisplayNbt(stack, true).setInteger("colour", colour);
    }

    protected NBTTagCompound getDisplayNbt(final ItemStack stack, final boolean create) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            if (!create) {
                return null;
            }
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        NBTTagCompound out;
        if (!nbt.hasKey("display", 10)) {
            if (!create) {
                return null;
            }
            out = new NBTTagCompound();
            nbt.setTag("display", out);
        } else {
            out = nbt.getCompoundTag("display");
        }
        return out;
    }

    public boolean func_82816_b_(final ItemStack stack) {
        return this.func_82814_b(stack) != -1;
    }

    public int func_82814_b(final ItemStack stack) {
        final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        return (nbt == null || !nbt.hasKey("colour", 3)) ? -1 : nbt.getInteger("colour");
    }

    public void func_82815_c(final ItemStack stack) {
        final NBTTagCompound nbt = this.getDisplayNbt(stack, false);
        if (nbt == null || !nbt.hasKey("colour", 3)) {
            return;
        }
        nbt.removeTag("colour");
        if (nbt.hasNoTags() && stack.getTagCompound() != null) {
            stack.getTagCompound().removeTag("display");
        }
    }

    @Nonnull
    @Override
    public EnumRarity getForgeRarity(@Nonnull final ItemStack stack) {
        return EnumRarity.EPIC;
    }

}
