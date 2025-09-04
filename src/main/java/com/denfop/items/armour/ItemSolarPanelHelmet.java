package com.denfop.items.armour;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.item.upgrade.EnumUpgrades;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.potion.IUPotion;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ItemSolarPanelHelmet extends ItemArmorEnergy implements EnergyItem, ISpecialArmor,
        UpgradeItem {

    protected static final Map<MobEffect, Integer> potionRemovalCost = new HashMap<>();
    private final int solarType;
    private final String name;
    private double maxCharge;
    private double transferLimit;
    private int tier;
    private double genDay;
    private double genNight;
    private int energyPerDamage;
    private double damageAbsorptionRatio;
    private boolean sunIsUp;
    private boolean skyIsVisible;
    private boolean ret = false;
    private double storage;
    private double maxStorage;

    public ItemSolarPanelHelmet(
            final int type, String name
    ) {
        super(
                "", Type.HELMET,
                type == 1 ? 1000000.0 : type == 2 ? 1.0E7 : type == 3 ? 10000.0 : 100000000,
                type == 1 ? 3000.0 : type == 2 ? 10000.0 : type == 3 ? 10000.0 : 38000.0,
                type == 1 ? 1 : type == 2 ? 2 : type == 3 ? 3 : type == 4 ? 5 : 7
        );

        this.solarType = type;
        this.name = name;
        this.transferLimit = 3000.0;
        this.tier = 1;
        if (this.solarType == 1) {
            this.genDay = 5;
            this.genNight = genDay / 2;
            this.maxCharge = 1000000.0;
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9;
            this.storage = 0;
            this.maxStorage = 3200D / 2;

        }
        if (this.solarType == 2) {
            this.genDay = 20;
            this.genNight = genDay / 2;
            this.maxCharge = 1.0E7;
            this.transferLimit = 10000.0;
            this.tier = 2;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.storage = 0;
            this.maxStorage = 20000D / 2;
        }
        if (this.solarType == 3) {
            this.genDay = 80;
            this.genNight = genDay / 2;
            this.maxCharge = 1.0E7;
            this.transferLimit = 10000.0;
            this.tier = 3;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.storage = 0;
            this.maxStorage = 200000D / 2;
        }
        if (this.solarType == 4) {
            this.genDay = 1280;
            this.genNight = genDay / 2;
            this.transferLimit = 38000.0;
            this.maxCharge = 100000000;
            this.tier = 5;
            this.energyPerDamage = 800;
            this.damageAbsorptionRatio = 0.9;
            this.storage = 0;
            this.maxStorage = 5000000D / 2;
        }
        if (this.solarType == 5) {
            this.genDay = 5120;
            this.genNight = genDay / 2;
            this.transferLimit = 100000.0;
            this.maxCharge = 100000000;
            this.tier = 7;
            this.energyPerDamage = 2000;
            this.damageAbsorptionRatio = 1.0;
            this.storage = 0;
            this.maxStorage = 1000000000D / 2;
        }

        potionRemovalCost.put(MobEffects.POISON, 100);
        potionRemovalCost.put(IUPotion.radiation, 20);
        potionRemovalCost.put(MobEffects.WITHER, 100);
        potionRemovalCost.put(MobEffects.HUNGER, 200);
        IUCore.runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.SOLAR_HELMET.list));
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

    public void setDamage(ItemStack stack, int damage) {
        int prev = this.getDamage(stack);


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

        CompoundTag nbtData = ModUtils.nbt(stack);
        if (!nbtData.getString("mode").isEmpty()) {
            return Constants.TEXTURES + ":textures/armor/" + this.name + "_" + nbtData.getString("mode") + ".png";
        }


        return Constants.TEXTURES + ":textures/armor/" + this.name + ".png";
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    private double experimental_generating(Level world) {
        double k = 0;
        float celestialAngle = ((world.getDayTime() % 24000F) / 24000F);

        celestialAngle *= 360;
        if (celestialAngle <= 90) {
            k = celestialAngle / 90;
        } else if (celestialAngle > 90 && celestialAngle < 180) {
            celestialAngle -= 90;
            k = 1 - celestialAngle / 90;
        } else if (celestialAngle > 180 && celestialAngle < 270) {
            celestialAngle -= 180;
            k = celestialAngle / 90;
        } else if (celestialAngle > 270 && celestialAngle < 360) {
            celestialAngle -= 270;
            k = 1 - celestialAngle / 90;
        }


        return k;

    }

    public void onArmorTick(@Nonnull ItemStack itemStack, Level worldObj, @Nonnull Player player) {
        if (worldObj.isClientSide) {
            return;
        }

        gainFuel(player);
        CompoundTag nbtData = ModUtils.nbt(itemStack);
        int resistance = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.RESISTANCE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.RESISTANCE, itemStack).number : 0);
        int repaired = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.REPAIRED, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.REPAIRED, itemStack).number : 0);
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.INVISIBILITY, itemStack)) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 300));
        }
        boolean Nightvision = nbtData.getBoolean("Nightvision");
        byte toggleTimer = nbtData.getByte("toggleTimer");
        if (IUCore.keyboard.isArmorKey(player) && toggleTimer == 0) {
            toggleTimer = 10;
            Nightvision = !Nightvision;
            if (!player.level().isClientSide()) {
                nbtData.putBoolean("Nightvision", Nightvision);
                if (Nightvision) {
                    IUCore.proxy.messagePlayer(player, "Nightvision enabled.");
                } else {
                    IUCore.proxy.messagePlayer(player, "Nightvision disabled.");
                }
            }
        }
        if (!player.level().isClientSide() && toggleTimer > 0) {
            toggleTimer = (byte) (toggleTimer - 1);
            nbtData.putByte("toggleTimer", toggleTimer);
        }
        if (Nightvision && !player.level().isClientSide() &&
                ElectricItem.manager.use(itemStack, 1.0D, player)) {
            int x = Mth.floor(player.getX());
            int z = Mth.floor(player.getZ());
            int y = Mth.floor(player.getY());
            int skylight = player.level().getMaxLocalRawBrightness(new BlockPos(x, y, z));
            boolean with = this.solarType == 1;
            boolean without = this.solarType == 2 || this.solarType == 3;
            if (without || with) {
                if (skylight > 8) {
                    IUCore.proxy.removePotion(player, MobEffects.NIGHT_VISION);
                    if (with) {
                        player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, true, true));

                    }
                } else {
                    if (with) {
                        IUCore.proxy.removePotion(player, MobEffects.BLINDNESS);

                    }
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
                }
            } else {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
            }
            ret = true;
        }
        if (repaired != 0) {
            if (worldObj.dayTime() % 80 == 0) {
                ElectricItem.manager.charge(
                        itemStack,
                        this.getMaxEnergy(itemStack) * 0.00001 * repaired,
                        Integer.MAX_VALUE,
                        true,
                        false
                );
            }
        }
        if (resistance != 0) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, resistance));
        }

        int genday = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENDAY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.GENDAY, itemStack).number : 0);

        int gennight = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.GENNIGHT, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.GENNIGHT, itemStack).number : 0);
        int storage = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.STORAGE, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.STORAGE, itemStack).number : 0);

        double k = experimental_generating(worldObj);
        if (this.sunIsUp && this.skyIsVisible) {
            this.storage = nbtData.getDouble("storage");
            this.storage = this.storage + (this.genDay + this.genDay * 0.05 * genday) * k;
            nbtData.putDouble("storage", this.storage);
        }
        if (this.skyIsVisible) {
            this.storage = nbtData.getDouble("storage");
            this.storage = this.storage + (this.genNight + this.genNight * 0.05 * gennight) * k;
            nbtData.putDouble("storage", this.storage);

        }
        if (nbtData.getDouble("storage") >= (maxStorage + maxStorage * 0.05 * storage)) {
            nbtData.putDouble("storage", (maxStorage + maxStorage * 0.05 * storage));
        }
        if (nbtData.getDouble("storage") < 0) {
            nbtData.putDouble("storage", 0);
        }

        for (MobEffectInstance effect : new LinkedList<>(player.getActiveEffects())) {

            Integer cost = potionRemovalCost.get(effect.getEffect());
            if (cost != null) {
                cost = cost * (effect.getAmplifier() + 1);
                if (ElectricItem.manager.canUse(itemStack, cost)) {
                    ElectricItem.manager.use(itemStack, cost, null);
                    IUCore.proxy.removePotion(player, effect.getEffect());
                    ret = true;
                }
            }
        }

        double tempstorage = nbtData.getDouble("storage");
        if (tempstorage > 0) {
            double energyLeft = tempstorage;
            for (int i = 0; i < player.getInventory().armor.size(); i++) {
                if (energyLeft > 0) {
                    if (!player.getInventory().armor.get(i).isEmpty()
                            && player.getInventory().armor.get(i).getItem() instanceof EnergyItem) {
                        double sentPacket = ElectricItem.manager.charge(player.getInventory().armor.get(i), energyLeft,
                                2147483647, true, false
                        );

                        if (sentPacket > 0.0D) {
                            energyLeft = (energyLeft - sentPacket);
                            nbtData.putDouble("storage", energyLeft);
                            ret = true;

                        }
                    }
                } else {
                    return;
                }
            }
            for (int j = 0; j < player.getInventory().items.size(); j++) {
                if (energyLeft > 0) {
                    if (!player.getInventory().items.get(j).isEmpty()
                            && player.getInventory().items.get(j).getItem() instanceof EnergyItem) {
                        double sentPacket = ElectricItem.manager.charge(player.getInventory().items.get(j), energyLeft,
                                2147483647, true, false
                        );
                        if (sentPacket > 0.0D) {
                            energyLeft -= sentPacket;
                            nbtData.putDouble("storage", energyLeft);
                            ret = true;

                        }

                    }

                } else {
                    return;
                }
            }
        }

        if (ret) {
            player.containerMenu.broadcastChanges();
        }
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.NIGTHVISION, itemStack)) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300));
        }
        if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WATER, itemStack)) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 300));
        }


    }

    public void gainFuel(Player player) {
        if (player.level().getGameTime() % 128 == 0) {
            updateVisibility(player);
        }

    }


    public void updateVisibility(Player player) {
        BlockPos pos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());
        boolean noSunWorld = !(player.level().dimension() == Level.OVERWORLD);
        boolean rainWeather = player.level().isRaining() || player.level().isThundering();
        this.sunIsUp = player.level().isDay() && !rainWeather;

        this.skyIsVisible = player.level().canSeeSky(pos.above()) &&
                (player.level().getBlockState(pos.above()).getMapColor(player.level(), pos.above()) ==
                        MapColor.NONE) && !noSunWorld;
    }


    public ISpecialArmor.ArmorProperties getProperties(
            final LivingEntity player, @Nonnull final ItemStack armor,
            final DamageSource source, final double damage, final int slot
    ) {


        final double absorptionRatio = this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio();
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, armor) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, armor).number : 0);

        final int energyPerDamage = (int) (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect);
        final int damageLimit = (int) ((energyPerDamage > 0)
                ? (25.0 * ElectricItem.manager.getCharge(armor) / energyPerDamage)
                : 0.0);
        return new ISpecialArmor.ArmorProperties(0, absorptionRatio, damageLimit);
    }

    public int getArmorDisplay(final Player player, @Nonnull final ItemStack armor, final int slot) {
        if (ElectricItem.manager.getCharge(armor) >= this.getEnergyPerDamage()) {
            return (int) Math.round(20.0 * this.getBaseAbsorptionRatio() * this.getDamageAbsorptionRatio());
        }
        return 0;
    }

    public void damageArmor(
            final LivingEntity entity, @Nonnull final ItemStack stack, final DamageSource source,
            final float damage, final int slot
    ) {
        int protect = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.PROTECTION, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.PROTECTION, stack).number : 0);

        ElectricItem.manager.discharge(
                stack,
                damage * (this.getEnergyPerDamage() - this.getEnergyPerDamage() * 0.2 * protect),
                Integer.MAX_VALUE,
                true,
                false,
                false
        );
    }

    public int getEnergyPerDamage() {
        return this.energyPerDamage;
    }

    public double getDamageAbsorptionRatio() {
        return this.damageAbsorptionRatio;
    }


    public boolean canProvideEnergy(final ItemStack itemStack) {
        return false;
    }


    public double getMaxEnergy(final ItemStack itemStack) {
        return this.maxCharge;
    }

    public short getTierItem(final ItemStack itemStack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(final ItemStack itemStack) {
        return this.transferLimit;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level p_41422_, List<Component> info, TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, p_41422_, info, p_41424_);
        CompoundTag nbtData1 = ModUtils.nbt(itemStack);

        info.add(Component.literal(Localization.translate("iu.storage.helmet") + " "
                + ModUtils.getString(nbtData1.getDouble("storage")) + " EF"));

        boolean with = this.solarType == 1;
        boolean without = this.solarType == 2 || this.solarType == 3;
        boolean auto = this.solarType > 3;
        if (with || without || auto) {
            info.add(Component.literal(Localization.translate("iu.special_armor_nightvision") + KeyboardClient.armormode.getKey().getDisplayName().getString()));
            if (with) {
                info.add(Component.literal(Localization.translate("iu.special_armor_nightvision_1")));
            }
            if (without) {
                info.add(Component.literal(Localization.translate("iu.special_armor_nightvision_2")));
            }
            if (auto) {
                info.add(Component.literal(Localization.translate("iu.special_armor_nightvision_3")));
            }
        }
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity p_41406_, int p_41407_, boolean p_41408_) {
        CompoundTag nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
    }


    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.SOLAR_HELMET.list;
    }
}
