package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IUCore;
import com.denfop.api.item.IEnergyItem;
import com.denfop.api.upgrade.EnumUpgrades;
import com.denfop.api.upgrade.IUpgradeItem;
import com.denfop.api.upgrade.UpgradeSystem;
import com.denfop.api.upgrade.event.EventItemLoad;
import com.denfop.audio.EnumSound;
import com.denfop.audio.SoundHandler;
import com.denfop.items.EnumInfoUpgradeModules;
import com.denfop.items.IProperties;
import com.denfop.network.packet.PacketSoundPlayer;
import com.denfop.network.packet.PacketStopSoundPlayer;
import com.denfop.utils.ElectricItemManager;
import com.denfop.utils.KeyboardClient;
import com.denfop.utils.ModUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;

import static com.denfop.IUCore.runnableListAfterRegisterItem;

public class ItemQuantumSaber extends TieredItem implements IEnergyItem, IUpgradeItem, IProperties {
    public static int ticker = 0;
    public final int maxCharge;
    public final int transferLimit;
    public final int tier;
    private final int damage1;
    public int activedamage;
    private int soundTicker;
    private boolean wasEquipped;
    private String nameItem;

    public ItemQuantumSaber(
            int maxCharge,
            int transferLimit, int tier, int activedamage1, int damage
    ) {
        super(Tiers.DIAMOND,  new Properties().setNoRepair().tab(IUCore.EnergyTab).setNoRepair().stacksTo(1));
        this.soundTicker = 0;
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.activedamage = activedamage1;
        this.damage1 = damage;
        IUCore.proxy.addProperties(this);
        runnableListAfterRegisterItem.add(() -> UpgradeSystem.system.addRecipe(this, EnumUpgrades.SABERS.list));

    }
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (pState.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = pState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && !pState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 1.5F;
        }
    }
    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.is(Blocks.COBWEB);
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? getAttributeModifiers(EquipmentSlot.MAINHAND,new ItemStack(this)) : super.getDefaultAttributeModifiers(pEquipmentSlot);
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
            this.nameItem = "iu.itemnanosaber1";
        }

        return this.nameItem;
    }
    private static boolean isActive(ItemStack stack) {
        CompoundTag nbt = ModUtils.nbt(stack);
        return isActive(nbt);
    }

    private static boolean isActive(CompoundTag nbt) {
        return nbt.getBoolean("active");
    }

    private static void setActive(CompoundTag nbt, boolean active) {
        nbt.putBoolean("active", active);
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            ElectricItemManager.addChargeVariants(this, p_41392_);
        }
    }

    public String[] properties() {
        return new String[]{"active"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack stack, ClientLevel world, LivingEntity entityIn, int p174679, String property) {
        return ModUtils.nbt(stack).getBoolean("active") ? 1.0F : 0.0F;

    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
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
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if (isActive(itemstack)) {
            drainSaber(itemstack, 80.0D, player);
        }

        return false;
    }

    public void drainSaber(ItemStack itemStack, double amount, LivingEntity entity) {
        int saberenergy = (UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABERENERGY, itemStack) ?
                UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABERENERGY, itemStack).number : 0);


        if (!ElectricItem.manager.use(itemStack, amount - amount * 0.15 * saberenergy, entity)) {
            CompoundTag nbtData = ModUtils.nbt(itemStack);
            nbtData.putBoolean("active", false);
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

        if (world.isClientSide) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        } else {
            CompoundTag nbt = stack.getOrCreateTag();
            if (!IUCore.keyboard.isChangeKeyDown(player)) {
                if (isActive(nbt)) {
                    setActive(nbt, false);
                    new PacketStopSoundPlayer(EnumSound.NanosabreIdle, player);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                } else if (ElectricItem.manager.canUse(stack, 16.0D)) {
                    setActive(nbt, true);
                    new PacketSoundPlayer(this.getStartSound(), player);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
                } else {
                    return super.use(world, player, hand);
                }
            } else {
                int mode = nbt.getInt("attackMode");
                nbt.putInt("attackMode", mode == 0 ? 1 : 0);
                return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
            }
        }
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
            tooltip.add(Component.translatable("iu.changemode_key")
                    .append(KeyboardClient.changemode.getKey().getDisplayName())
                    .append(Component.translatable("iu.changemode_rcm")));
        }

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) {
            return super.getAttributeModifiers(slot, stack);
        } else {
            int saberdamage = UpgradeSystem.system.hasModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack) ?
                    UpgradeSystem.system.getModules(EnumInfoUpgradeModules.SABER_DAMAGE, stack).number : 0;
            int dmg = (int) (damage1 + damage1 * 0.15 * saberdamage);

            if (ElectricItem.manager.canUse(stack, 400.0D)) {
                CompoundTag nbtData = ModUtils.nbt(stack);
                if (nbtData.getBoolean("active")) {
                    dmg = (int) (activedamage + activedamage * 0.15 * saberdamage);
                }
            }

            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", 2, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", dmg, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level world, Entity entity, int slot, boolean p_41408_) {
        super.inventoryTick(itemStack, world, entity, slot, p_41408_);
        CompoundTag nbt = ModUtils.nbt(itemStack);

        if (!UpgradeSystem.system.hasInMap(itemStack)) {
            nbt.putBoolean("hasID", false);
            MinecraftForge.EVENT_BUS.post(new EventItemLoad(world, this, itemStack));
        }
        if (world.isClientSide && isActive(itemStack) && world.getGameTime() % 20 == 0) {
            SoundHandler.playSound(IUCore.proxy.getPlayerInstance(), getIdleSound());
        }
        CompoundTag nbtData = ModUtils.nbt(itemStack);

        if (!nbtData.getBoolean("active")) {
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
        CompoundTag nbtData = ModUtils.nbt(stack);
        if (!nbtData.getBoolean("active")) {
            return true;
        }
        if (!target.getLevel().isClientSide) {
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
            int attackMode = nbtData.getInt("attackMode");

            if (ElectricItem.manager.canUse(stack, 400.0D)) {
                CompoundTag nbtData1 = ModUtils.nbt(stack);
                if (nbtData1.getBoolean("active")) {
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
        List<LivingEntity> entities = source.level.getEntitiesOfClass(
                LivingEntity.class,
                source.getBoundingBox().inflate(radius)
        );
        for (LivingEntity entity : entities) {
            if (entity != source && entity != target) {
                entity.hurt(DamageSource.playerAttack((Player) source), (float) damage);
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
}
