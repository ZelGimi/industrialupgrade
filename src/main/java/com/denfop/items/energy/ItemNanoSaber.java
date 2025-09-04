package com.denfop.items.energy;

import com.denfop.IUCore;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.api.item.upgrade.EnumUpgrades;
import com.denfop.api.item.upgrade.UpgradeItem;
import com.denfop.api.item.upgrade.UpgradeSystem;
import com.denfop.api.item.upgrade.event.EventItemLoad;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.network.packet.PacketStopSoundPlayer;
import com.denfop.sound.EnumSound;
import com.denfop.sound.SoundHandler;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;
import static net.minecraft.world.item.SwordItem.createToolProperties;

public class ItemNanoSaber extends TieredItem implements EnergyItem, UpgradeItem, IProperties, IItemTab {
    public static int ticker = 0;
    public final int maxCharge;
    public final int transferLimit;
    public final int tier;
    private final int damage1;
    public int activedamage;
    private int soundTicker;
    private boolean wasEquipped;
    private String nameItem;

    public ItemNanoSaber(
            int maxCharge,
            int transferLimit, int tier, int activedamage1, int damage
    ) {
        super(Tiers.DIAMOND, new Properties().setNoRepair().setNoRepair().stacksTo(1).component(DataComponentsInit.MODE, 0).component(DataComponentsInit.ENERGY, 0D).component(DataComponentsInit.ACTIVE, false).component(DataComponents.TOOL, createToolProperties()));
        this.soundTicker = 0;
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.activedamage = activedamage1;
        this.damage1 = damage;

        IUCore.proxy.addProperties(this);
        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.SABERS.list));
    }

    private static boolean isActive(ItemStack stack) {

        return stack.getOrDefault(DataComponentsInit.ACTIVE, false);
    }

    private static void setActive(ItemStack stack, boolean active) {
        stack.set(DataComponentsInit.ACTIVE, active);
    }

    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (pState.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return pState.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack p_336002_, BlockState pBlock) {
        return pBlock.is(Blocks.COBWEB);
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
            this.nameItem = "iu.nano_saber";
        }

        return this.nameItem;
    }

    public String[] properties() {
        return new String[]{"active"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel world, LivingEntity entityIn, int p174679, String property) {
        return stack.getOrDefault(DataComponentsInit.ACTIVE, false) ? 1.0F : 0.0F;

    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }


    public String getRandomSwingSound() {
        switch (IUCore.random.nextInt(3)) {
            default:
                return "nanosabreswing1";
            case 1:
                return "nanosabreswing2";
            case 2:
                break;
        }
        return "nanosabreswing3";
    }

    @Override
    public boolean mineBlock(ItemStack itemstack, Level p_41417_, BlockState p_41418_, BlockPos p_41419_, LivingEntity p_41420_) {
        if (isActive(itemstack)) {
            drainSaber(itemstack, 80.0D, p_41420_);
        }

        return false;
    }


    public void drainSaber(ItemStack itemStack, double amount, LivingEntity entity) {
        int saberenergy = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABERENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABERENERGY, itemStack).number : 0);


        if (!ElectricItem.manager.use(itemStack, amount - amount * 0.15 * saberenergy, entity)) {
            setActive(itemStack, false);
        }
    }

    protected String getIdleSound() {
        return "NanosabreIdle".toLowerCase();
    }

    protected String getStartSound() {
        return "NanosabrePowerup".toLowerCase();
    }

    protected void removeAudioSource() {


    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            if (!IUCore.keyboard.isChangeKeyDown(player)) {
                if (isActive(stack)) {
                    setActive(stack, false);
                    new PacketStopSoundPlayer(EnumSound.NanosabreIdle, player);
                    player.containerMenu.broadcastChanges();
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                } else if (ElectricItem.manager.canUse(stack, 16.0D)) {
                    setActive(stack, true);
                    new PacketSoundPlayer(this.getStartSound(), player);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                } else {
                    return super.use(world, player, hand);
                }
            } else {
                int mode = stack.getOrDefault(DataComponentsInit.MODE, 0);
                stack.set(DataComponentsInit.MODE, mode == 0 ? 1 : 0);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }


    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext worldIn,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("press.lshift"));
        } else {
            tooltip.add(Component.translatable("iu.changemode_key")
                    .append(KeyboardClient.changemode.getKey().getDisplayName())
                    .append(Component.translatable("iu.changemode_rcm")));
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        int saberdamage = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack).number : 0;
        int dmg = (int) (damage1 + damage1 * 0.15 * saberdamage);

        if (ElectricItem.manager.canUse(stack, 400.0D)) {
            if (isActive(stack)) {
                dmg = (int) (activedamage + activedamage * 0.15 * saberdamage);
            }
        }

        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, dmg, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);

        return builder.build();
    }


    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean p_41408_) {
        super.inventoryTick(itemStack, world, entity, slot, p_41408_);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            NeoForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
        if (world.isClientSide && isActive(itemStack) && world.getGameTime() % 20 == 0) {
            SoundHandler.playSound(IUCore.proxy.getPlayerInstance(), getIdleSound());
        }

        if (!isActive(itemStack)) {
            return;
        }
        if (ticker % 16 == 0 && entity instanceof ServerPlayer) {
            if (slot < 9) {
                drainSaber(itemStack, 64.0D, (ServerPlayer) entity);
            } else if (ticker % 64 == 0) {
                drainSaber(itemStack, 16.0D, (ServerPlayer) entity);
            }

        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        this.removeAudioSource();
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity source) {
        if (!isActive(stack)) {
            return true;
        }
        if (!target.level().isClientSide) {
            drainSaber(stack, 400.0D, source);
            int vampires = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.VAMPIRES, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.VAMPIRES, stack).number : 0;
            boolean wither = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.WITHER, stack);
            boolean poison = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.POISON, stack);

            if (vampires != 0) {
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
            if (source instanceof ServerPlayer) {
                new PacketSoundPlayer(getRandomSwingSound(), (Player) source);
            }

            int saberdamage = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack).number : 0;
            int dmg = (int) (damage1 + damage1 * 0.15 * saberdamage);
            int attackMode = stack.getOrDefault(DataComponentsInit.MODE, 0);

            if (ElectricItem.manager.canUse(stack, 400.0D)) {
                if (isActive(stack)) {
                    dmg = (int) (activedamage + activedamage * 0.15 * saberdamage);
                }
                if (attackMode != 0) {
                    areaAttack(stack, target, source, 2, dmg);
                }
            }
        }
        return true;
    }

    private void areaAttack(ItemStack stack, LivingEntity target, LivingEntity source, int radius, double damage) {
        List<LivingEntity> entities = source.level().getEntitiesOfClass(
                LivingEntity.class,
                source.getBoundingBox().inflate(radius)
        );
        for (LivingEntity entity : entities) {
            if (entity != source && entity != target) {
                entity.hurt(source.damageSources().playerAttack((Player) source), (float) damage);
            }
        }
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
        return this.maxCharge;
    }

    public short getTierItem(ItemStack stack) {
        return (short) this.tier;
    }

    public double getTransferEnergy(ItemStack stack) {
        return this.transferLimit;
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
    public CreativeModeTab getItemCategory() {
        return IUCore.EnergyTab;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
    }
}
