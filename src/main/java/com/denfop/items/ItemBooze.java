package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.api.brewage.EnumBeerVariety;
import com.denfop.api.brewage.EnumTimeVariety;
import com.denfop.api.brewage.EnumWaterVariety;
import com.denfop.datacomponent.BeerInfo;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.Localization;
import com.denfop.utils.Timer;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBooze extends Item implements IProperties, IItemTab {

    public int[] baseDuration = new int[]{300, 900, 1200, 1600, 2000, 2400};
    public float[] baseIntensity = new float[]{0.4F, 0.75F, 1.0F, 1.5F, 2.0F};
    private String nameItem;

    public ItemBooze() {
        super(new Properties().stacksTo(1).setNoRepair());
        IUCore.proxy.addProperties(this);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
    }

    @Override
    public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> items) {
        if (allowedIn(tab)) {
            items.add(new ItemStack(this));

            for (EnumWaterVariety waterVariety : EnumWaterVariety.values()) {
                for (EnumTimeVariety timeVariety : EnumTimeVariety.values()) {
                    for (EnumBeerVariety beerVariety : EnumBeerVariety.values()) {
                        ItemStack stack = new ItemStack(this);
                        BeerInfo beerInfo = new BeerInfo(waterVariety, timeVariety, beerVariety, 5);
                        stack.set(DataComponentsInit.BEER, beerInfo);
                        items.add(stack);
                    }
                }
            }
        }
    }

    @Override
    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            int index = pathBuilder.indexOf(targetString);
            while (index != -1) {
                pathBuilder.replace(index, index + targetString.length(), replacement);
                index = pathBuilder.indexOf(targetString, index + replacement.length());
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
        if (!stack.has(DataComponentsInit.BEER)) {
            return new ItemStack(this);
        }

        BeerInfo beerInfo = stack.get(DataComponentsInit.BEER);
        if (beerInfo == null) {
            return new ItemStack(this);
        }

        int level;
        EnumWaterVariety waterVariety = beerInfo.waterVariety();
        EnumTimeVariety timeVariety = beerInfo.timeVariety();
        EnumBeerVariety beerVariety = beerInfo.beerVariety();

        if (timeVariety == EnumTimeVariety.BLACK_STUFF
                || waterVariety == EnumWaterVariety.BLACK_STUFF
                || beerVariety == EnumBeerVariety.BLACKSTUFF) {
            return this.drinkBlackStuff(living);
        }

        int solidRatio = waterVariety.ordinal();
        level = beerVariety.ordinal();
        int duration = this.baseDuration[solidRatio];
        float intensity = this.baseIntensity[timeVariety.ordinal()];

        if (living instanceof Player player) {
            player.getFoodData().eat(5 - level, solidRatio * 0.15F);
        }

        int max = (int) (intensity * level * 0.5F);
        MobEffectInstance slow = living.getEffect(MobEffects.DIG_SLOWDOWN);
        level = -1;

        if (slow != null) {
            level = slow.getAmplifier();
        }

        beerInfo = beerInfo.updateAmount(stack);

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

        return beerInfo != null ? stack : new ItemStack(this);
    }

    public void amplifyEffect(LivingEntity living, Holder<MobEffect> effect, int max, float intensity, int duration) {
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

    private String getWaterTranslationKey(EnumWaterVariety variety) {
        switch (variety) {
            case WATERY:
                return "iu.beer.water.watery";
            case LITE:
                return "iu.beer.water.lite";
            case WITHOUT_NAME:
                return "iu.beer.water.without_name";
            case STRONG:
                return "iu.beer.water.strong";
            case THICK:
                return "iu.beer.water.thick";
            case STODGE:
                return "iu.beer.water.stodge";
            case BLACK_STUFF:
            default:
                return "iu.beer.water.black_stuff";
        }
    }

    private String getTimeTranslationKey(EnumTimeVariety variety) {
        switch (variety) {
            case BREW:
                return "iu.beer.time.brew";
            case YOUNGSTER:
                return "iu.beer.time.youngster";
            case BEER:
                return "iu.beer.time.beer";
            case ALE:
                return "iu.beer.time.ale";
            case DRAGONBLOOD:
                return "iu.beer.time.dragonblood";
            case BLACK_STUFF:
            default:
                return "iu.beer.time.black_stuff";
        }
    }

    private String getBeerTranslationKey(EnumBeerVariety variety) {
        switch (variety) {
            case SOUP:
                return "iu.beer.variety.soup";
            case WHITE:
                return "iu.beer.variety.white";
            case PREFIXLESS:
                return "iu.beer.variety.prefixless";
            case DARK:
                return "iu.beer.variety.dark";
            case BLACK:
                return "iu.beer.variety.black";
            case BLACKSTUFF:
            default:
                return "iu.beer.variety.blackstuff";
        }
    }

    private Component getLocalizedWater(EnumWaterVariety variety) {
        return Component.translatable(getWaterTranslationKey(variety));
    }

    private Component getLocalizedTime(EnumTimeVariety variety) {
        return Component.translatable(getTimeTranslationKey(variety));
    }

    private Component getLocalizedBeer(EnumBeerVariety variety) {
        return Component.translatable(getBeerTranslationKey(variety));
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    private LivingEntity getTooltipViewer() {
        Minecraft mc = Minecraft.getInstance();
        return mc.player;
    }

    private int getCurrentDrinkStage(@Nullable LivingEntity living) {
        if (living == null) {
            return -1;
        }
        MobEffectInstance slow = living.getEffect(MobEffects.DIG_SLOWDOWN);
        return slow == null ? -1 : slow.getAmplifier();
    }

    private MobEffectInstance predictAmplifiedEffect(@Nullable LivingEntity living, Holder<MobEffect> effect, int max, float intensity, int duration) {
        MobEffectInstance eff = living == null ? null : living.getEffect(effect);

        if (eff == null) {
            return new MobEffectInstance(effect, duration, 0);
        } else {
            int currentDuration = eff.getDuration();
            int extraDuration = duration;
            int maxNewDuration = (int) ((duration * (1.0F + intensity * 2.0F) - currentDuration) / 2);

            if (maxNewDuration < 0) {
                maxNewDuration = 0;
            }

            if (maxNewDuration < extraDuration) {
                extraDuration = maxNewDuration;
            }

            currentDuration += extraDuration;
            int newAmp = eff.getAmplifier();
            if (newAmp < max) {
                newAmp++;
            }

            return new MobEffectInstance(effect, currentDuration, newAmp);
        }
    }

    private String toRoman(int level) {
        switch (level) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
            default:
                return String.valueOf(level);
        }
    }

    private String formatAmplifier(int amplifier) {
        return toRoman(amplifier + 1);
    }

    private String formatTicks(int ticks) {
        int totalSeconds = Math.max(1, ticks / 20);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        if (minutes > 0) {
            return minutes + "m " + seconds + "s";
        }
        return seconds + "s";
    }

    private void addPredictedEffectLine(List<Component> tooltip, MobEffectInstance instance) {
        tooltip.add(Component.translatable(
                "iu.beer.effects.line",
                instance.getEffect().value().getDisplayName(),
                Component.literal(formatAmplifier(instance.getAmplifier())),
                Component.literal(formatTicks(instance.getDuration()))
        ).withStyle(ChatFormatting.GRAY));
    }

    private void addBlackStuffPreview(List<Component> tooltip) {
        tooltip.add(Component.translatable("iu.beer.effects.blackstuff.header").withStyle(ChatFormatting.DARK_RED));
        tooltip.add(Component.translatable("iu.beer.effects.chance_none", "1/6").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.beer.effects.chance_line",
                "1/6",
                MobEffects.CONFUSION.value().getDisplayName(),
                Component.literal("I"),
                Component.literal(formatTicks(1200))
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.beer.effects.chance_line",
                "1/6",
                MobEffects.BLINDNESS.value().getDisplayName(),
                Component.literal("I"),
                Component.literal(formatTicks(2400))
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.beer.effects.chance_line",
                "1/6",
                MobEffects.POISON.value().getDisplayName(),
                Component.literal("I"),
                Component.literal(formatTicks(2400))
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.beer.effects.chance_line",
                "1/6",
                MobEffects.POISON.value().getDisplayName(),
                Component.literal("III"),
                Component.literal(formatTicks(200))
        ).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(
                "iu.beer.effects.chance_line_instant_random",
                "1/6",
                MobEffects.HARM.value().getDisplayName(),
                Component.literal("I"),
                Component.literal("IV")
        ).withStyle(ChatFormatting.GRAY));
    }

    @OnlyIn(Dist.CLIENT)
    private void addEffectPreviewTooltip(ItemStack stack, List<Component> tooltip) {
        if (!stack.has(DataComponentsInit.BEER)) {
            return;
        }

        BeerInfo beerInfo = stack.get(DataComponentsInit.BEER);
        if (beerInfo == null) {
            return;
        }

        EnumWaterVariety waterVariety = beerInfo.waterVariety();
        EnumTimeVariety timeVariety = beerInfo.timeVariety();
        EnumBeerVariety beerVariety = beerInfo.beerVariety();

        tooltip.add(Component.literal(""));
        tooltip.add(Component.translatable("iu.beer.effects.info").withStyle(ChatFormatting.AQUA));

        if (timeVariety == EnumTimeVariety.BLACK_STUFF
                || waterVariety == EnumWaterVariety.BLACK_STUFF
                || beerVariety == EnumBeerVariety.BLACKSTUFF) {
            addBlackStuffPreview(tooltip);
            return;
        }

        LivingEntity viewer = getTooltipViewer();
        int currentStage = getCurrentDrinkStage(viewer);

        if (currentStage < 0) {
            tooltip.add(Component.translatable("iu.beer.effects.stage.none").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add(Component.translatable("iu.beer.effects.stage", currentStage + 1).withStyle(ChatFormatting.DARK_GRAY));
        }

        int duration = this.baseDuration[waterVariety.ordinal()];
        float intensity = this.baseIntensity[timeVariety.ordinal()];
        int max = (int) (intensity * beerVariety.ordinal() * 0.5F);

        addPredictedEffectLine(tooltip, predictAmplifiedEffect(viewer, MobEffects.DIG_SLOWDOWN, max, intensity, duration));

        if (currentStage > -1) {
            addPredictedEffectLine(tooltip, predictAmplifiedEffect(viewer, MobEffects.DAMAGE_BOOST, max, intensity, duration));
            if (currentStage > 0) {
                addPredictedEffectLine(tooltip, predictAmplifiedEffect(viewer, MobEffects.MOVEMENT_SLOWDOWN, max / 2, intensity, duration));
                if (currentStage > 1) {
                    addPredictedEffectLine(tooltip, predictAmplifiedEffect(viewer, MobEffects.DAMAGE_RESISTANCE, max - 1, intensity, duration));
                    if (currentStage > 2) {
                        addPredictedEffectLine(tooltip, predictAmplifiedEffect(viewer, MobEffects.CONFUSION, 0, intensity, duration));
                        if (currentStage > 3) {
                            tooltip.add(Component.translatable(
                                    "iu.beer.effects.line_instant_random",
                                    MobEffects.HARM.value().getDisplayName(),
                                    Component.literal("I"),
                                    Component.literal("III")
                            ).withStyle(ChatFormatting.GRAY));
                        }
                    }
                }
            }
        } else {
            tooltip.add(Component.translatable("iu.beer.effects.next_stage_hint").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    private void addRepeatedDrinkDescription(List<Component> tooltip, EnumWaterVariety waterVariety, EnumTimeVariety timeVariety, EnumBeerVariety beerVariety) {
        if (timeVariety == EnumTimeVariety.BLACK_STUFF
                || waterVariety == EnumWaterVariety.BLACK_STUFF
                || beerVariety == EnumBeerVariety.BLACKSTUFF) {
            return;
        }

        tooltip.add(Component.literal(""));
        tooltip.add(Component.translatable("iu.beer.repeat.info").withStyle(ChatFormatting.GOLD));
        tooltip.add(Component.translatable("iu.beer.repeat.base", MobEffects.DIG_SLOWDOWN.value().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("iu.beer.repeat.2", MobEffects.DAMAGE_BOOST.value().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("iu.beer.repeat.3", MobEffects.MOVEMENT_SLOWDOWN.value().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("iu.beer.repeat.4", MobEffects.DAMAGE_RESISTANCE.value().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("iu.beer.repeat.5", MobEffects.CONFUSION.value().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("iu.beer.repeat.6", MobEffects.HARM.value().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("iu.beer.repeat.extra").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext tooltipContext,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        super.appendHoverText(stack, tooltipContext, tooltip, flagIn);

        if (stack.has(DataComponentsInit.BEER)) {
            BeerInfo beerInfo = stack.get(DataComponentsInit.BEER);
            if (beerInfo == null) {
                return;
            }

            EnumWaterVariety waterVariety = beerInfo.waterVariety();
            EnumTimeVariety timeVariety = beerInfo.timeVariety();
            EnumBeerVariety beerVariety = beerInfo.beerVariety();

            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe.info")));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe")));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe1") + " " + beerVariety.getRatioOfComponents().get(0).getB()));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe2") + " " + beerVariety.getRatioOfComponents().get(0).getA()));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe3") + " " + waterVariety.getAmount().get(0)));
            tooltip.add(Component.literal(Localization.translate("iu.beer.recipe4") + " " + new Timer((int) (timeVariety.getTime() * 60 * 60)).getDisplay()));
            tooltip.add(Component.translatable(
                    "iu.beer.recipe5_format",
                    getLocalizedWater(waterVariety),
                    getLocalizedBeer(beerVariety),
                    getLocalizedTime(timeVariety)
            ));

            addEffectPreviewTooltip(stack, tooltip);
            addRepeatedDrinkDescription(tooltip, waterVariety, timeVariety, beerVariety);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (!stack.has(DataComponentsInit.BEER)) {
            return Component.literal(Localization.translate(this.getDescriptionId(stack).replace("item.", "iu.") + ".name"));
        }

        BeerInfo beerInfo = stack.get(DataComponentsInit.BEER);
        if (beerInfo == null) {
            return Component.literal(Localization.translate(this.getDescriptionId(stack).replace("item.", "iu.") + ".name"));
        }

        EnumWaterVariety waterVariety = beerInfo.waterVariety();
        EnumTimeVariety timeVariety = beerInfo.timeVariety();
        EnumBeerVariety beerVariety = beerInfo.beerVariety();

        return Component.translatable(
                "iu.beer.display_name",
                getLocalizedWater(waterVariety),
                getLocalizedBeer(beerVariety),
                getLocalizedTime(timeVariety)
        );
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return stack.has(DataComponentsInit.BEER) ? UseAnim.DRINK : UseAnim.NONE;
    }

    @Override
    public String[] properties() {
        return new String[]{"time_variety"};
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public float getItemProperty(ItemStack itemStack, ClientLevel level, LivingEntity entity, int p174679, String property) {
        if (!itemStack.has(DataComponentsInit.BEER)) {
            return 0;
        }

        BeerInfo beerInfo = itemStack.get(DataComponentsInit.BEER);
        if (beerInfo == null) {
            return 0;
        }

        return beerInfo.timeVariety().ordinal() + 1;
    }
}