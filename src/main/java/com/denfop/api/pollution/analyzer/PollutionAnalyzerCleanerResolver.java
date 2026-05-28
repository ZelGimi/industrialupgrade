package com.denfop.api.pollution.analyzer;

import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityPurifierSoil;
import com.denfop.blockentity.mechanism.generator.things.fluid.BlockEntityAirCollector;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class PollutionAnalyzerCleanerResolver {

    private static final int MAX_LEVEL = 10;

    private static final ResourceLocation[] AIR_CANDIDATES = new ResourceLocation[]{
            ResourceLocation.tryBuild("industrialupgrade", "aircollector"),
            ResourceLocation.tryBuild("industrialupgrade", "air_collector"),
            ResourceLocation.tryBuild("industrialupgrade", "air_purifier"),
            ResourceLocation.tryBuild("industrialupgrade", "air_cleaner"),
            ResourceLocation.tryBuild("industrialupgrade", "pollution_air_cleaner")
    };

    private static final ResourceLocation[] SOIL_CANDIDATES = new ResourceLocation[]{
            ResourceLocation.tryBuild("industrialupgrade", "purifier_soil"),
            ResourceLocation.tryBuild("industrialupgrade", "soil_purifier"),
            ResourceLocation.tryBuild("industrialupgrade", "soil_cleaner"),
            ResourceLocation.tryBuild("industrialupgrade", "pollution_soil_cleaner")
    };

    private PollutionAnalyzerCleanerResolver() {
    }

    public static PollutionAnalyzerCleanerOption resolveAirCleaner(List<PollutionAnalyzerSourceSnapshot> sources) {
        PollutionAnalyzerSourceSnapshot detected = sources.stream()
                .filter(PollutionAnalyzerSourceSnapshot::isCleaner)
                .filter(source -> source.getAirCleaningPerSecond() > 0.0D)
                .max(Comparator.comparingDouble(PollutionAnalyzerSourceSnapshot::getAirCleaningPerSecond))
                .orElse(null);

        double base = BlockEntityAirCollector.getAirPurificationPerSecond(0);
        double levelBonus = BlockEntityAirCollector.getAirPurificationPerSecond(1) - base;
        double target = Math.max(
                base,
                sources.stream()
                        .mapToDouble(PollutionAnalyzerSourceSnapshot::getAirCurrentContribution)
                        .filter(v -> v > 0.0D)
                        .sum()
        );

        CleanerPlan plan = buildPlan(target, base, levelBonus, MAX_LEVEL);

        ItemStack stack = detected != null && !detected.getRenderStack().isEmpty()
                ? detected.getRenderStack()
                : resolveRegisteredStack(AIR_CANDIDATES);

        boolean blockLike = !stack.isEmpty() && stack.getItem() instanceof BlockItem;
        String descriptionKey = "iu.pollution_analyzer.cleaner.air.block";

        if (stack.isEmpty()) {
            stack = new ItemStack(IUItem.antiairpollution.getItem());
            blockLike = false;
            descriptionKey = "iu.pollution_analyzer.cleaner.air.module";
        }

        String displayName = detected != null
                ? detected.getName()
                : normalizeName(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()), "Air Cleaner");

        return new PollutionAnalyzerCleanerOption(
                "air",
                stack,
                displayName,
                descriptionKey,
                blockLike,
                detected != null ? detected.getAirCleaningPerSecond() : base,
                base,
                levelBonus,
                detected != null ? detected.getMechanismLevel() : -1,
                MAX_LEVEL,
                plan.baseCopies(),
                plan.singleLevel()
        );
    }

    public static PollutionAnalyzerCleanerOption resolveSoilCleaner(List<PollutionAnalyzerSourceSnapshot> sources) {
        PollutionAnalyzerSourceSnapshot detected = sources.stream()
                .filter(PollutionAnalyzerSourceSnapshot::isCleaner)
                .filter(source -> source.getSoilCleaningPerSecond() > 0.0D)
                .max(Comparator.comparingDouble(PollutionAnalyzerSourceSnapshot::getSoilCleaningPerSecond))
                .orElse(null);

        double base = BlockEntityPurifierSoil.getSoilPurificationPerSecond(0);
        double levelBonus = BlockEntityPurifierSoil.getSoilPurificationPerSecond(1) - base;
        double target = Math.max(
                base,
                sources.stream()
                        .mapToDouble(PollutionAnalyzerSourceSnapshot::getSoilCurrentContribution)
                        .filter(v -> v > 0.0D)
                        .sum()
        );

        CleanerPlan plan = buildPlan(target, base, levelBonus, MAX_LEVEL);

        ItemStack stack = detected != null && !detected.getRenderStack().isEmpty()
                ? detected.getRenderStack()
                : resolveRegisteredStack(SOIL_CANDIDATES);

        boolean blockLike = !stack.isEmpty() && stack.getItem() instanceof BlockItem;
        String descriptionKey = "iu.pollution_analyzer.cleaner.soil.block";

        if (stack.isEmpty()) {
            stack = new ItemStack(IUItem.antisoilpollution.getItem());
            blockLike = false;
            descriptionKey = "iu.pollution_analyzer.cleaner.soil.module";
        }

        String displayName = detected != null
                ? detected.getName()
                : normalizeName(com.denfop.utils.ModUtils.cleanComponentString(stack.getHoverName().getString()), "Soil Cleaner");

        return new PollutionAnalyzerCleanerOption(
                "soil",
                stack,
                displayName,
                descriptionKey,
                blockLike,
                detected != null ? detected.getSoilCleaningPerSecond() : base,
                base,
                levelBonus,
                detected != null ? detected.getMechanismLevel() : -1,
                MAX_LEVEL,
                plan.baseCopies(),
                plan.singleLevel()
        );
    }

    private static CleanerPlan buildPlan(double required, double base, double perLevel, int maxLevel) {
        double safeBase = Math.max(0.0001D, base);
        int copies = Math.max(1, (int) Math.ceil(required / safeBase));

        int singleLevel;
        if (required <= base) {
            singleLevel = 0;
        } else if (perLevel <= 0.0D) {
            singleLevel = -1;
        } else {
            singleLevel = (int) Math.ceil((required - base) / perLevel);
            if (singleLevel > maxLevel) {
                singleLevel = -1;
            } else if (singleLevel < 0) {
                singleLevel = 0;
            }
        }

        return new CleanerPlan(copies, singleLevel);
    }

    private static ItemStack resolveRegisteredStack(ResourceLocation[] candidates) {
        for (ResourceLocation rl : candidates) {
            Optional<Item> item = BuiltInRegistries.ITEM.getOptional(rl);
            if (item.isPresent()) {
                return new ItemStack(item.get());
            }
        }
        return ItemStack.EMPTY;
    }

    private static String normalizeName(String input, String fallback) {
        if (input == null || input.isBlank()) {
            return fallback;
        }
        String value = input.trim();
        String lower = value.toLowerCase(Locale.ROOT);
        if (lower.contains("block.industrialupgrade") || lower.contains("item.industrialupgrade")) {
            return fallback;
        }
        return value;
    }

    private record CleanerPlan(int baseCopies, int singleLevel) {
    }
}