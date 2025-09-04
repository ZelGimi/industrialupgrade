package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.brewage.EnumBeerVariety;
import com.denfop.api.brewage.EnumTimeVariety;
import com.denfop.api.brewage.EnumWaterVariety;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import com.denfop.utils.Timer;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBooze extends Item implements IProperties {
    public int[] baseDuration = new int[]{300, 900, 1200, 1600, 2000, 2400};
    public float[] baseIntensity = new float[]{0.4F, 0.75F, 1.0F, 1.5F, 2.0F};
    private String nameItem;

    public ItemBooze() {
        super(new Properties().tab(IUCore.EnergyTab).stacksTo(1).setNoRepair());
        IUCore.proxy.addProperties(this);
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
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living) {
        CompoundTag nbtTagCompound = stack.getOrCreateTag();

        if (!nbtTagCompound.contains("beer")) {
            return new ItemStack(this);
        } else {
            int level;
            EnumWaterVariety waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
            EnumBeerVariety beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("beerVariety")];

            if (timeVariety == EnumTimeVariety.BLACK_STUFF || waterVariety == EnumWaterVariety.BLACK_STUFF || beerVariety == EnumBeerVariety.BLACKSTUFF) {
                return this.drinkBlackStuff(living);
            }

            int solidRatio = waterVariety.ordinal();
            level = beerVariety.ordinal();
            int duration = this.baseDuration[solidRatio];
            float intensity = this.baseIntensity[timeVariety.ordinal()];

            if (living instanceof Player) {
                ((Player) living).getFoodData().eat(5 - level, solidRatio * 0.15F);
            }

            int max = (int) (intensity * level * 0.5F);
            MobEffectInstance slow = living.getEffect(MobEffects.DIG_SLOWDOWN);
            level = -1;

            if (slow != null) {
                level = slow.getAmplifier();
            }

            nbtTagCompound.putByte("amount", (byte) (nbtTagCompound.getByte("amount") - 1));
            this.amplifyEffect(living, MobEffects.DIG_SLOWDOWN, max, intensity, duration);

            if (level > -1) {
                this.amplifyEffect(living, MobEffects.DAMAGE_BOOST, max, intensity, duration);
                if (level > 0) {
                    this.amplifyEffect(living, MobEffects.MOVEMENT_SLOWDOWN, max / 2, intensity, duration);
                    if (level > 1) {
                        this.amplifyEffect(living, MobEffects.DAMAGE_RESISTANCE, max - 1, intensity, duration);
                        if (level > 2) {
                            this.amplifyEffect(living, MobEffects.CONFUSION, 0, intensity, duration);
                            if (level > 3) {
                                living.addEffect(new MobEffectInstance(
                                        MobEffects.HARM,
                                        1,
                                        world.random.nextInt(3)
                                ));
                            }
                        }
                    }
                }
            }

            return nbtTagCompound.getByte("amount") > 0 ? stack : new ItemStack(this);
        }
    }

    public void amplifyEffect(LivingEntity living, MobEffect effect, int max, float intensity, int duration) {
        MobEffectInstance eff = living.getEffect(effect);

        if (eff == null) {
            living.addEffect(new MobEffectInstance(effect, duration, 0));
        } else {
            int currentDuration = eff.getDuration();
            int maxNewDuration = (int) ((duration * (1.0F + intensity * 2.0F) - currentDuration) / 2);

            if (maxNewDuration < 0) {
                maxNewDuration = 0;
            }

            if (maxNewDuration < duration) {
                duration = maxNewDuration;
            }

            currentDuration += duration;
            int newAmp = eff.getAmplifier();
            if (newAmp < max) {
                newAmp++;
            }

            living.addEffect(new MobEffectInstance(effect, currentDuration, newAmp));
        }
    }

    public ItemStack drinkBlackStuff(LivingEntity living) {
        switch (living.getCommandSenderWorld().random.nextInt(6)) {
            case 1:
                living.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 1200, 0));
                break;
            case 2:
                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 2400, 0));
                break;
            case 3:
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 2400, 0));
                break;
            case 4:
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 2));
                break;
            case 5:
                living.addEffect(new MobEffectInstance(MobEffects.HARM, 1, living.getCommandSenderWorld().random.nextInt(4)));
                break;
        }

        return new ItemStack(this);
    }


    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level worldIn,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag nbtTagCompound = stack.getTag();
        if (nbtTagCompound != null && nbtTagCompound.contains("beer")) {
            EnumWaterVariety waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
            EnumBeerVariety beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("beerVariety")];

            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe.info")));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe")));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe1") + " " + beerVariety.getRatioOfComponents().get(0).getB()));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe2") + " " + beerVariety.getRatioOfComponents().get(0).getA()));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe3") + " " + waterVariety.getAmount().get(0)));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe4") + " " + new Timer((int) (timeVariety.getTime() * 60 * 60)).getDisplay()));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe5") + " " + waterVariety.name() + " " + beerVariety.name() + " " + timeVariety.name()));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public Component getName(ItemStack stack) {
        CompoundTag nbtTagCompound = stack.getTag();
        if (nbtTagCompound == null || !nbtTagCompound.contains("beer")) {
            return Component.literal(Localization.translate(this.getDescriptionId(stack).replace("item.", "iu.") + ".name"));
        } else {
            EnumWaterVariety waterVariety = EnumWaterVariety.values()[nbtTagCompound.getByte("waterVariety")];
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbtTagCompound.getByte("timeVariety")];
            EnumBeerVariety beerVariety = EnumBeerVariety.values()[nbtTagCompound.getByte("beerVariety")];

            return Component.literal(waterVariety.name() + " " + beerVariety.name() + " " + timeVariety.name());
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return ModUtils.nbt(stack).contains("beer") ? UseAnim.DRINK : UseAnim.NONE;
    }


    @Override
    public String[] properties() {
        return new String[]{"time_variety"};
    }


    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        final CompoundTag nbt = ModUtils.nbt(itemStack);
        boolean hasKey = nbt.contains("beer");
        if (!hasKey) {
            return 0;
        } else {
            EnumTimeVariety timeVariety = EnumTimeVariety.values()[nbt.getByte("timeVariety")];
            return timeVariety.ordinal() + 1;
        }
    }
}
