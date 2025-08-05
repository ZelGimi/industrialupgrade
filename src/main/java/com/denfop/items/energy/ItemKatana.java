package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.items.armour.special.ItemSpecialArmor;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemKatana extends DiggerItem implements IEnergyItem, IUpgradeItem, IProperties {
    public final int maxCharge;
    public final int transferLimit;
    public final int tier;
    public int damage1;
    private int soundTicker;
    private String nameItem;

    public ItemKatana() {
        super(1, 2, Tiers.DIAMOND,  new TagKey<>(Registry.BLOCK_REGISTRY,new ResourceLocation("","block")), new Properties().setNoRepair().tab(IUCore.EnergyTab).setNoRepair().stacksTo(1));
        this.soundTicker = 0;
        this.maxCharge = 500000;
        this.transferLimit = 5000;
        this.tier = 4;
        this.damage1 = 13;
        IUCore.proxy.addProperties(this);
        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.SABERS.list));
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
            this.nameItem = "iu.katana";
        }

        return this.nameItem;
    }
    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
    }

    public String[] properties() {
        return new String[]{"type"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel world, LivingEntity entityIn, int p174679, String property) {
        CompoundTag nbt = ModUtils.nbt(stack);
        switch (nbt.getString("type")) {
            case "":
                return 0;
            case "yellow":
                return 0.25f;
            case "green":
                return 0.5f;
            case "pink":
                return 0.75f;
        }
        return 0;
    }

    public boolean drainSaber(ItemStack itemStack, double amount) {
        int saberenergy = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABERENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABERENERGY, itemStack).number : 0);


        return ElectricItem.manager.use(itemStack, amount - amount * 0.15 * saberenergy, null);
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return this.maxCharge;
    }


    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(slot, stack);
        } else {
            int saberdamage = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack).number : 0;
            int dmg = (int) (damage1 + damage1 * 0.15 * saberdamage);


            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", 2, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", dmg, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity source) {
        if (!target.getLevel().isClientSide) {
            if (!drainSaber(stack, 400.0D)) {
                return false;
            }

            int vampires = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.VAMPIRES, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.VAMPIRES, stack).number : 0);
            boolean wither = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WITHER, stack);
            boolean poison = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.POISON, stack);

            if (vampires > 0) {
                target.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, vampires));
            }
            if (wither) {
                target.addEffect(new MobEffectInstance(MobEffects.WITHER, 60));
            }
            if (poison) {
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 60));
            }
            if (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.HUNGRY, stack)) {
                target.addEffect(new MobEffectInstance(MobEffects.HUNGER, 60));
            }

            if (source instanceof ServerPlayer player) {
                new PacketSoundPlayer("katana", player);
                CompoundTag nbtData = stack.getOrCreateTag();
                boolean iaidoActive = nbtData.getBoolean("iaidoMode");

                if (iaidoActive) {
                    player.getCooldowns().addCooldown(this, 60);
                }
            }

            if (!(source instanceof ServerPlayer) || !(target instanceof Player) ||
                    ((ServerPlayer) source).canHarmPlayer((Player) target)) {

                Iterator<EquipmentSlot> armorSlots = Arrays
                        .stream(EquipmentSlot.values())
                        .filter(slot -> slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND)
                        .iterator();

                while (armorSlots.hasNext()) {
                    EquipmentSlot slot = armorSlots.next();
                    if (!ElectricItem.manager.canUse(stack, 2000.0D)) {
                        break;
                    }

                    ItemStack armor = target.getItemBySlot(slot);
                    if (!armor.isEmpty()) {
                        double amount = 0.0D;
                        if (armor.getItem() instanceof ItemSpecialArmor specialArmor) {
                            amount = specialArmor.getArmor().getDamageEnergy();
                        }

                        if (amount > 0.0D) {
                            ElectricItem.manager.discharge(armor, amount, this.tier, true, false, false);
                            if (!ElectricItem.manager.canUse(armor, 1.0D)) {
                                target.setItemSlot(slot, ItemStack.EMPTY);
                            }

                            drainSaber(stack, 2000.0D);
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean p_41408_) {
        super.inventoryTick(itemStack, world, entity, slot, p_41408_);
        CompoundTag nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
        if (entity instanceof Player player && !world.isClientSide) {
            ItemCooldowns cooldownTracker = player.getCooldowns();
            nbt.putBoolean("cooldown", cooldownTracker.isOnCooldown(itemStack.getItem()));
        }

    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, @Nonnull Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag nbt = stack.getOrCreateTag();

        if (!IUCore.keyboard.isChangeKeyDown(player)) {
            switch (nbt.getString("type")) {
                case "":
                    nbt.putString("type", "yellow");
                    break;
                case "yellow":
                    nbt.putString("type", "green");
                    break;
                case "green":
                    nbt.putString("type", "pink");
                    break;
                case "pink":
                    nbt.putString("type", "");
                    break;
            }
        } else {
            nbt.putBoolean("iaidoMode", !nbt.getBoolean("iaidoMode"));
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public short getTierItem(ItemStack itemStack) {

        return (short) this.tier;
    }

    protected void removeAudioSource() {


    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public List<EnumInfoUpgradeModules> getUpgradeModules() {
        return EnumUpgrades.SABERS.list;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public double getTransferEnergy(ItemStack itemStack) {

        return this.transferLimit;
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        this.removeAudioSource();
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Level worldIn,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            tooltip.add(Component.translatable("iu.changemode_key").append(Component.translatable(Localization.translate(
                    "iu.changemode_rcm1"))));
            tooltip.add(Component.translatable("iu.changemode_key")
                    .append(KeyboardClient.changemode.getKey().getDisplayName())
                    .append(Component.translatable("iu.changemode_rcm")));
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
