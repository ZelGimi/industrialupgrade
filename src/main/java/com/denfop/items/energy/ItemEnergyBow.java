package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemEnergyBow extends BowItem implements IEnergyItem, IUpgradeItem, IProperties {
    static final int[] CHARGE = new int[]{1500, 750, 2000, 5000, 1000};

    static final String[] MODE = new String[]{"normal", "rapidfire", "spread", "sniper", "flame"};
    private final float type;
    private final String name;
    private final double nanoBowBoost;
    private final int tier;
    private final int transferenergy;
    private final int maxenergy;
    private String nameItem;

    public ItemEnergyBow(String name, double nanoBowBoost, int tier, int transferenergy, int maxenergy, float type) {
        super(new Properties().stacksTo(1).tab(IUCore.EnergyTab));
        this.name = name;
        this.nanoBowBoost = nanoBowBoost;
        this.tier = tier;
        this.transferenergy = transferenergy;
        this.maxenergy = maxenergy;
        this.type = type;
        if (properties().length > 0)
            IUCore.proxy.addProperties(this);
        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.BOW.list));
    }

    public static float getArrowVelocity(int charge) {
        float f = charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.5F);
    }


    public void inventoryTick(
            ItemStack stack,
            Level world,
            Entity entity,
            int itemSlot,
            boolean isSelected
    ) {
        CompoundTag nbt = ModUtils.nbt(stack);

        if (!UpgradeSystem.system.hasInMap(stack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, stack));
        }
    }

    public void onUsingTick(ItemStack stack, LivingEntity livingEntity, int i) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        CompoundTag nbt = ModUtils.nbt(stack);
        int mode = nbt.getInt("bowMode");

        if (mode == 1) {
            int bowEnergy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWENERGY, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWENERGY, stack).number : 0;

            int j = getUseDuration(stack) - i;
            if (j >= 10 && ElectricItem.manager.canUse(stack, CHARGE[1] - CHARGE[1] * 0.1 * bowEnergy)) {
                releaseUsing(stack, livingEntity.level, livingEntity, i);
                player.stopUsingItem();
            }
        }
    }

    public String[] properties() {
        return new String[]{"pulling", "pull","mode"};
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag nbt = stack.getOrCreateTag();

        int mode = nbt.getInt("bowMode");
        int bowenergy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWENERGY, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWENERGY, stack).number : 0;

        if (IUCore.keyboard.isChangeKeyDown(player)) {
            if (!world.isClientSide) {
                mode++;
                if (mode >= CHARGE.length) {
                    mode = 0;
                }
                nbt.putInt("bowMode", mode);
            }
            if (!world.isClientSide) {
                IUCore.proxy.messagePlayer(player, Localization.translate("info.nanobow." + MODE[mode]));
            }
        } else if (player.getAbilities().instabuild || ElectricItem.manager.canUse(
                stack,
                CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy
        )) {
            player.startUsingItem(hand);
        }

        ArrowNockEvent event = new ArrowNockEvent(player, stack, hand, world, false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return event.getAction();
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public void releaseUsing(
            @Nonnull ItemStack stack,
            @Nonnull Level world,
            @Nonnull LivingEntity entityLiving,
            int timeLeft
    ) {
        if (!(entityLiving instanceof Player)) {
            super.releaseUsing(stack, world, entityLiving, timeLeft);
            return;
        }

        Player player = (Player) entityLiving;
        CompoundTag nbt = stack.getOrCreateTag();
        int mode = nbt.getInt("bowMode");
        int charge = getMaxItemUseDuration(stack) - timeLeft;
        ArrowLooseEvent event = new ArrowLooseEvent(player, stack, world, charge, false);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            return;
        }

        charge = event.getCharge();
        if (mode == 3) {
            charge /= 2;
        }
        if (mode == 1) {
            charge *= 4;
        }
        float f = getArrowVelocity(charge);
        if (f < 0.1D) {
            return;
        }

        if (!world.isClientSide) {
            EntityAdvArrow arrow = createArrow(world, stack, player);
            arrow = this.customArrow(arrow);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);

            if (f == 1.5F) {
                arrow.setCritArrow(true);
            }

            int bowdamage = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWDAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWDAMAGE, stack).number : 0;
            arrow.setBaseDamage(arrow.getBaseDamage() + type * 2.5D + 0.5D + type * 2.5D * 0.25 * bowdamage);
            int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
            if (j > 0) {
                arrow.setBaseDamage(arrow.getBaseDamage() + j * 0.5D + 0.5D);
            }
            if (mode == 0 && arrow.isCritArrow()) {
                j += 3;
            } else if (mode == 1 && arrow.isCritArrow()) {
                j++;
            } else if (mode == 3 && arrow.isCritArrow()) {
                j += 8;
            }
            if (j > 0) {
                arrow.setBaseDamage(arrow.getBaseDamage() + j * 0.5D + 0.5D);
            }
            if (nanoBowBoost > 0) {
                arrow.setBaseDamage(arrow.getBaseDamage() + nanoBowBoost * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
            if (mode == 0 && arrow.isCritArrow()) {
                k++;
            } else if (mode == 3 && arrow.isCritArrow()) {
                k += 5;
            }
            if (k > 0) {
                arrow.setKnockback(k);
            }
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack) > 0) {
                arrow.setSecondsOnFire(100);
            }
            if (mode == 4 && arrow.isCritArrow()) {
                arrow.setSecondsOnFire(2000);
            }
            arrow.pickup = Arrow.Pickup.CREATIVE_ONLY;

            int bowenergy = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.BOWENERGY, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.BOWENERGY, stack).number : 0;

            if (mode == 2) {
                if (ElectricItem.manager.canUse(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy)) {
                    ElectricItem.manager.use(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy, player);
                    new PacketSoundPlayer(EnumSound.bow, player);
                    world.addFreshEntity(arrow);

                    if (arrow.isCritArrow()) {
                        spawnAdditionalArrows(world, player, f, arrow);
                    }
                }
            } else {
                if (ElectricItem.manager.canUse(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy)) {
                    ElectricItem.manager.use(stack, CHARGE[mode] - CHARGE[mode] * 0.1 * bowenergy, player);
                    new PacketSoundPlayer(EnumSound.bow, player);
                    world.addFreshEntity(arrow);
                }
            }
        }
    }

    private void spawnAdditionalArrows(Level world, Player player, float f, Arrow originalArrow) {
        for (int i = 0; i < 4; i++) {
            Arrow arrow = createArrow(world, player.getItemInHand(InteractionHand.MAIN_HAND), player);
            arrow = this.customArrow(arrow);
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 1.0F);
            arrow.setBaseDamage(f * 2.0F);
            arrow.setPosRaw(originalArrow.getX() + (i - 2) * 0.25D, originalArrow.getY(), originalArrow.getZ());
            arrow.setCritArrow(true);
            arrow.pickup = Arrow.Pickup.CREATIVE_ONLY;
            world.addFreshEntity(arrow);
        }
    }

    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        CompoundTag nbt = ModUtils.nbt(stack);
        switch (nbt.getInt("bowMode")) {
            case 3:
            case 5:
                return 144000;
            case 1:
                return 18000;
        }
        return 72000;
    }

    public EntityAdvArrow customArrow(AbstractArrow arrow) {
        return (EntityAdvArrow) arrow;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getItemProperty(ItemStack stack, ClientLevel world, LivingEntity entityIn, int p174679, String property) {
        if (property.equals("pulling"))
            return entityIn != null && entityIn.isUsingItem() && entityIn.getUseItem() == stack ? 1.0F : 0.0F;
        if (property.equals("mode")){

            final CompoundTag nbt = ModUtils.nbt(stack);
            if (nbt.getString("mode").isEmpty())
                return -1;
            String[] mode = {"Zelen", "Demon", "Dark", "Cold", "Ender", "Ukraine", "Fire", "Snow", "Taiga", "Desert", "Emerald"};
            for (int i = 0; i < mode.length; i++)
                if (nbt.getString("mode").equals(mode[i]))
                    return i;
        }
        if (entityIn == null) {
            return 0.0F;
        } else {
            return !(entityIn.getUseItem().getItem() instanceof BowItem) ? 0.0F : (float) (stack.getUseDuration() - entityIn.getUseItemRemainingTicks()) / 20.0F;
        }
    }

    public EntityAdvArrow createArrow(Level worldIn, ItemStack stack, LivingEntity shooter) {
        EntityAdvArrow entitytippedarrow = new EntityAdvArrow(worldIn, shooter);
        entitytippedarrow.setEffectsFromItem(stack);
        entitytippedarrow.setStack(stack);
        return entitytippedarrow;
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
            this.nameItem = "item."+pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(
            @Nonnull final ItemStack stack,
            final Level world,
            @Nonnull final List<Component> tooltip,
            @Nonnull final TooltipFlag flag
    ) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            tooltip.add(Component.translatable("iu.changemode_key")
                    .append(KeyboardClient.changemode.getKey().getDisplayName())
                    .append(Component.translatable("iu.changemode_rcm")));
        }

        switch (this.tier) {
            case 2:
                tooltip.add(Component.translatable("iu.bow.maxdamage").append(String.valueOf(47)));
                break;
            case 3:
                tooltip.add(Component.translatable("iu.bow.maxdamage").append(String.valueOf(56)));
                break;
            case 4:
                tooltip.add(Component.translatable("iu.bow.maxdamage").append(String.valueOf(71)));
                break;
        }

        ModUtils.mode(stack, tooltip);
        super.appendHoverText(stack, world, tooltip, flag);
    }

    public boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    public int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    public int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }

    public boolean canProvideEnergy(ItemStack stack) {
        return false;
    }

    public double getMaxEnergy(ItemStack stack) {
        return maxenergy;
    }

    public short getTierItem(ItemStack stack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack stack) {
        return transferenergy;
    }

    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.BOW.list;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }
}
