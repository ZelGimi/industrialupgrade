package com.denfop.recipes;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PotionRecipes {
    public static final List<Ingredient> containers = new ArrayList<>();
    public static final List<Mix<Potion>> potionMixes = new ArrayList<>();
    public static final List<Mix<Item>> containerMixes = new ArrayList<>();

    public static void init() {
        addContainer(Items.POTION);
        addContainer(Items.SPLASH_POTION);
        addContainer(Items.LINGERING_POTION);
        addMix(Potions.WATER, Items.GLOWSTONE_DUST, Potions.THICK);
        addMix(Potions.WATER, Items.REDSTONE, Potions.MUNDANE);
        addMix(Potions.WATER, Items.NETHER_WART, Potions.AWKWARD);
        addMix(Potions.AWKWARD, Items.GOLDEN_CARROT, Potions.NIGHT_VISION);
        addMix(Potions.NIGHT_VISION, Items.REDSTONE, Potions.LONG_NIGHT_VISION);
        addMix(Potions.NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.INVISIBILITY);
        addMix(Potions.LONG_NIGHT_VISION, Items.FERMENTED_SPIDER_EYE, Potions.LONG_INVISIBILITY);
        addMix(Potions.INVISIBILITY, Items.REDSTONE, Potions.LONG_INVISIBILITY);
        addStartMix(Items.MAGMA_CREAM, Potions.FIRE_RESISTANCE);
        addMix(Potions.FIRE_RESISTANCE, Items.REDSTONE, Potions.LONG_FIRE_RESISTANCE);
        addStartMix(Items.RABBIT_FOOT, Potions.LEAPING);
        addMix(Potions.LEAPING, Items.REDSTONE, Potions.LONG_LEAPING);
        addMix(Potions.LEAPING, Items.GLOWSTONE_DUST, Potions.STRONG_LEAPING);
        addMix(Potions.LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        addMix(Potions.LONG_LEAPING, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        addMix(Potions.SLOWNESS, Items.REDSTONE, Potions.LONG_SLOWNESS);
        addMix(Potions.SLOWNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SLOWNESS);
        addMix(Potions.AWKWARD, Items.TURTLE_HELMET, Potions.TURTLE_MASTER);
        addMix(Potions.TURTLE_MASTER, Items.REDSTONE, Potions.LONG_TURTLE_MASTER);
        addMix(Potions.TURTLE_MASTER, Items.GLOWSTONE_DUST, Potions.STRONG_TURTLE_MASTER);
        addMix(Potions.SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.SLOWNESS);
        addMix(Potions.LONG_SWIFTNESS, Items.FERMENTED_SPIDER_EYE, Potions.LONG_SLOWNESS);
        addStartMix(Items.SUGAR, Potions.SWIFTNESS);
        addMix(Potions.SWIFTNESS, Items.REDSTONE, Potions.LONG_SWIFTNESS);
        addMix(Potions.SWIFTNESS, Items.GLOWSTONE_DUST, Potions.STRONG_SWIFTNESS);
        addMix(Potions.AWKWARD, Items.PUFFERFISH, Potions.WATER_BREATHING);
        addMix(Potions.WATER_BREATHING, Items.REDSTONE, Potions.LONG_WATER_BREATHING);
        addStartMix(Items.GLISTERING_MELON_SLICE, Potions.HEALING);
        addMix(Potions.HEALING, Items.GLOWSTONE_DUST, Potions.STRONG_HEALING);
        addMix(Potions.HEALING, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        addMix(Potions.STRONG_HEALING, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        addMix(Potions.HARMING, Items.GLOWSTONE_DUST, Potions.STRONG_HARMING);
        addMix(Potions.POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        addMix(Potions.LONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.HARMING);
        addMix(Potions.STRONG_POISON, Items.FERMENTED_SPIDER_EYE, Potions.STRONG_HARMING);
        addStartMix(Items.SPIDER_EYE, Potions.POISON);
        addMix(Potions.POISON, Items.REDSTONE, Potions.LONG_POISON);
        addMix(Potions.POISON, Items.GLOWSTONE_DUST, Potions.STRONG_POISON);
        addStartMix(Items.GHAST_TEAR, Potions.REGENERATION);
        addMix(Potions.REGENERATION, Items.REDSTONE, Potions.LONG_REGENERATION);
        addMix(Potions.REGENERATION, Items.GLOWSTONE_DUST, Potions.STRONG_REGENERATION);
        addStartMix(Items.BLAZE_POWDER, Potions.STRENGTH);
        addMix(Potions.STRENGTH, Items.REDSTONE, Potions.LONG_STRENGTH);
        addMix(Potions.STRENGTH, Items.GLOWSTONE_DUST, Potions.STRONG_STRENGTH);
        addMix(Potions.WATER, Items.FERMENTED_SPIDER_EYE, Potions.WEAKNESS);
        addMix(Potions.WEAKNESS, Items.REDSTONE, Potions.LONG_WEAKNESS);
        addMix(Potions.AWKWARD, Items.PHANTOM_MEMBRANE, Potions.SLOW_FALLING);
        addMix(Potions.SLOW_FALLING, Items.REDSTONE, Potions.LONG_SLOW_FALLING);
    }

    public static void expectPotion(Item p_341194_) {
        if (!(p_341194_ instanceof PotionItem)) {
            throw new IllegalArgumentException("Expected a potion, got: " + String.valueOf(ForgeRegistries.ITEMS.getKey(p_341194_)));
        }
    }

    public static void addContainer(Item p_340911_) {
        expectPotion(p_340911_);
        containers.add(Ingredient.of(new ItemLike[]{p_340911_}));

    }

    public static void addMix(Potion p_341151_, Item p_341216_, Potion p_340841_) {
        potionMixes.add(new Mix(new Holder() {
            @Override
            public Object value() {
                return p_341151_;
            }

            @Override
            public boolean isBound() {
                return false;
            }

            @Override
            public boolean is(ResourceLocation pLocation) {
                return false;
            }

            @Override
            public boolean is(ResourceKey pResourceKey) {
                return false;
            }

            @Override
            public boolean is(Predicate pPredicate) {
                return false;
            }

            @Override
            public boolean is(TagKey pTagKey) {
                return false;
            }

            @Override
            public Stream<TagKey> tags() {
                return null;
            }

            @Override
            public Either unwrap() {
                return null;
            }

            @Override
            public Optional<ResourceKey> unwrapKey() {
                return Optional.empty();
            }

            @Override
            public Kind kind() {
                return null;
            }

            @Override
            public boolean isValidInRegistry(Registry pRegistry) {
                return false;
            }


        }, Ingredient.of(new ItemLike[]{p_341216_}), new Holder() {
            @Override
            public Object value() {
                return p_340841_;
            }

            @Override
            public boolean isBound() {
                return false;
            }

            @Override
            public boolean is(ResourceLocation pLocation) {
                return false;
            }

            @Override
            public boolean is(ResourceKey pResourceKey) {
                return false;
            }

            @Override
            public boolean is(Predicate pPredicate) {
                return false;
            }

            @Override
            public boolean is(TagKey pTagKey) {
                return false;
            }

            @Override
            public Stream<TagKey> tags() {
                return null;
            }

            @Override
            public Either unwrap() {
                return null;
            }

            @Override
            public Optional<ResourceKey> unwrapKey() {
                return Optional.empty();
            }

            @Override
            public Kind kind() {
                return null;
            }

            @Override
            public boolean isValidInRegistry(Registry pRegistry) {
                return false;
            }

        }));

    }

    public static void addStartMix(Item p_341103_,Potion p_341346_) {

        addMix(Potions.WATER, p_341103_, Potions.MUNDANE);
        addMix(Potions.AWKWARD, p_341103_, p_341346_);


    }

    public  static record Mix<T>(Holder<T> from, Ingredient ingredient, Holder<T> to) {
    }
}
