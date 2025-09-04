package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.item.upgrade.EnumUpgrades;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.sound.EnumSound;
import com.denfop.sound.SoundHandler;
import com.denfop.utils.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class ItemAdvJetpack extends ItemArmorEnergy implements EnergyItem, ISpecialArmor, IProperties,
        UpgradeItem {

    private static boolean lastJetpackUsed = false;
    private final String armorName;
    private final double maxStorage;
    private final double TransferLimit;
    private final int tier;

    public ItemAdvJetpack(String name, double maxStorage, double TransferLimit, int tier) {
        super("", EquipmentSlot.CHEST, maxStorage, TransferLimit, tier);


        this.armorName = name;
        this.maxStorage = maxStorage;
        this.TransferLimit = TransferLimit;
        this.tier = tier;


        IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.JETPACK.list));

    }

    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", Registry.ITEM.getKey(this)));
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
    public float getItemProperty(ItemStack stack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        final CompoundTag nbt = ModUtils.nbt(stack);
        return nbt.getString("mode").equals(property) ? 1 : 0;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity p_41406_, int p_41407_, boolean p_41408_) {
        super.inventoryTick(stack, worldIn, p_41406_, p_41407_, p_41408_);
        CompoundTag nbt = ModUtils.nbt(stack);

        if (!UpgradeSystem.system.hasInMap(stack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(worldIn, this, stack));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> tooltip, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, tooltip, p_41424_);
        CompoundTag nbtData = ModUtils.nbt(stack);
        if (UpgradeSystem.system.hasModules(
                EnumInfoUpgradeModules.FLY,
                stack
        )) {
            tooltip.add(Component.literal(Localization.translate("iu.fly") + " " + ModUtils.Boolean(nbtData.getBoolean("jetpack"))));
            tooltip.add(Component.literal(Localization.translate("iu.fly_need")));

            if (!KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
                tooltip.add(Component.literal(Localization.translate("press.lshift")));
            }


            if (KeyboardIU.isKeyDown(InputConstants.KEY_LSHIFT)) {
                tooltip.add(Component.literal(Localization.translate("iu.changemode_fly") + KeyboardClient.flymode.getKey().getDisplayName().getString()));
            }
        }
    }


    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);


    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.4;
    }

    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }


    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        int suffix = (this.slot == EquipmentSlot.LEGS) ? 2 : 1;
        CompoundTag nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + nbtData.getString("mode") + "_" + suffix + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.armorName + "_" + suffix + ".png";
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
            float power = 1.0F;
            float dropPercentage = 0.2F;
            power = 0.7F;
            dropPercentage = 0.05F;

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

            int worldHeight = player.getLevel().getHeight();
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
                    if (electric) {
                        maxHoverY = 0.1F;
                    } else {
                        maxHoverY = 0.2F;
                    }
                }

                if (IUCore.keyboard.isSneakKeyDown(player)) {
                    if (electric) {
                        maxHoverY = -0.1F;
                    } else {
                        maxHoverY = -0.2F;
                    }
                }

                if (moveY > (double) maxHoverY) {
                    moveY = maxHoverY;
                    if (prevmotion > moveY) {
                        moveY = prevmotion;
                    }
                    player.setDeltaMovement(affectedMotion.x, moveY, affectedMotion.z);
                }
            }

            int consume = 2;
            if (hoverMode) {
                consume = 1;
            }

            if (electric) {
                consume += 6;
            }

            if (!player.isOnGround()) {
                this.use(jetpack, consume);
            }

            player.fallDistance = 0.0F;
            player.walkDist = 0.0F;
            return true;
        }
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }


    public double getMaxEnergy(ItemStack itemStack) {
        return maxStorage;
    }

    public short getTierItem(ItemStack itemStack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack itemStack) {
        return this.TransferLimit;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            final ItemStack var4 = new ItemStack(this, 1);
            ElectricItem.manager.charge(var4, 2.147483647E9, Integer.MAX_VALUE, true, false);
            p_41392_.add(var4);
            p_41392_.add(new ItemStack(this, 1));
        }
    }


    public void onArmorTick(@Nonnull ItemStack itemStack, @Nonnull Level world, Player player) {

        if (player.getInventory().armor.get(2).is(itemStack.getItem())) {
            CompoundTag nbtData = ModUtils.nbt(itemStack);
            boolean hoverMode = nbtData.getBoolean("hoverMode");
            byte toggleTimer = nbtData.getByte("toggleTimer");
            boolean jetpackUsed = false;
            if (IUCore.keyboard.isJumpKeyDown(player) && IUCore.keyboard.isVerticalMode(player) && toggleTimer == 0) {
                toggleTimer = 10;
                hoverMode = !hoverMode;
                if (!player.getLevel().isClientSide()) {
                    nbtData.putBoolean("hoverMode", hoverMode);
                    if (hoverMode) {
                        IUCore.proxy.messagePlayer(player, "Hover Mode enabled.");
                    } else {
                        IUCore.proxy.messagePlayer(player, "Hover Mode disabled.");
                    }
                }
            }
            boolean jetpack;
            if (nbtData.getBoolean("jetpack")) {
                player.fallDistance = 0;

                if (nbtData.getBoolean("jump") && !nbtData.getBoolean("canFly") && !player.getAbilities().mayfly && IUCore.keyboard.isJumpKeyDown(
                        player) && !nbtData.getBoolean(
                        "isFlyActive") && toggleTimer == 0) {
                    toggleTimer = 10;
                    nbtData.putBoolean("canFly", true);
                }
                nbtData.putBoolean("jump", !player.isOnGround());

                if (!player.isOnGround()) {
                    if (ElectricItem.manager.canUse(itemStack, 25)) {
                        ElectricItem.manager.use(itemStack, 25, null);
                    } else {
                        nbtData.putBoolean("jetpack", false);
                    }
                }
            }
            jetpack = nbtData.getBoolean("jetpack");
            if ((IUCore.keyboard.isJumpKeyDown(player) || hoverMode) && !jetpack) {
                jetpackUsed = this.useJetpack(player, hoverMode);
            }
            if (IUCore.keyboard.isFlyModeKeyDown(player) && toggleTimer == 0 && UpgradeSystem.system.hasModules(
                    EnumInfoUpgradeModules.FLY,
                    itemStack
            )) {
                toggleTimer = 10;
                jetpack = !jetpack;
                if (!player.getLevel().isClientSide()) {

                    nbtData.putBoolean("jetpack", jetpack);
                    if (jetpack) {
                        IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info"));
                    } else {
                        IUCore.proxy.messagePlayer(player, Localization.translate("iu.flymode_armor.info1"));

                    }
                }
            }
            if (!player.getLevel().isClientSide() && toggleTimer > 0) {
                --toggleTimer;
                nbtData.putByte("toggleTimer", toggleTimer);
            }

            if (world.isClientSide() && player == IUCore.proxy.getPlayerInstance()) {
                if (lastJetpackUsed != jetpackUsed) {
                    if (jetpackUsed) {
                        SoundHandler.playSound(player, "JetpackLoop");
                    }


                    lastJetpackUsed = jetpackUsed;
                    if (!lastJetpackUsed) {
                        SoundHandler.stopSound(EnumSound.JetpackLoop);
                    }
                }
                final Random rnd = IUCore.random;
                Vec3 motion = player.getDeltaMovement();
                if (jetpackUsed) {
                    for (int i = 0; i < rnd.nextInt(10); i++) {
                        world.addParticle(
                                ParticleTypes.SMOKE,
                                (float) (player.getX() - motion.x) + rnd.nextFloat(),
                                (float) (player.getY()),
                                (float) (player.getZ() - motion.z) + rnd.nextFloat(), 0, -0.25, 0
                        );
                    }
                    for (int i = 0; i < rnd.nextInt(10); i++) {
                        world.addParticle(
                                ParticleTypes.FLAME,
                                (float) (player.getX() - motion.x) + rnd.nextFloat(),
                                (float) (player.getY()),
                                (float) (player.getZ() - motion.z) + rnd.nextFloat(), 0, -0.25, 0
                        );
                    }
                }


            }
            boolean fireResistance = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.FIRE_PROTECTION, itemStack);
            if (fireResistance) {
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300));
            }
            int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);

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
    }

    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }


    public ArmorProperties getProperties(
            LivingEntity player,
            @Nonnull ItemStack armor,
            DamageSource source,
            double damage,
            int slot
    ) {
        return new ArmorProperties(0, 0.0D, 0);
    }

    public int getArmorDisplay(Player player, @Nonnull ItemStack armor, int slot) {
        return 0;
    }


    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.JETPACK.list;
    }
}
