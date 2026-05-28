package com.denfop.api.pollution.analyzer;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class PollutionAnalyzerCleanerOption {

    private final String medium;
    private final ItemStack stack;
    private final String displayName;
    private final String descriptionKey;
    private final boolean blockLike;

    private final double estimatedReductionPerSecond;
    private final double baseReductionPerSecond;
    private final double levelBonusReductionPerSecond;

    private final int detectedLevel;
    private final int maxLevel;

    private final int recommendedBaseMachineCount;
    private final int recommendedSingleMachineLevel;

    public PollutionAnalyzerCleanerOption(
            String medium,
            ItemStack stack,
            String displayName,
            String descriptionKey,
            boolean blockLike,
            double estimatedReductionPerSecond,
            double baseReductionPerSecond,
            double levelBonusReductionPerSecond,
            int detectedLevel,
            int maxLevel,
            int recommendedBaseMachineCount,
            int recommendedSingleMachineLevel
    ) {
        this.medium = medium;
        this.stack = stack.copy();
        this.displayName = displayName;
        this.descriptionKey = descriptionKey;
        this.blockLike = blockLike;
        this.estimatedReductionPerSecond = estimatedReductionPerSecond;
        this.baseReductionPerSecond = baseReductionPerSecond;
        this.levelBonusReductionPerSecond = levelBonusReductionPerSecond;
        this.detectedLevel = detectedLevel;
        this.maxLevel = maxLevel;
        this.recommendedBaseMachineCount = recommendedBaseMachineCount;
        this.recommendedSingleMachineLevel = recommendedSingleMachineLevel;
    }

    public static PollutionAnalyzerCleanerOption fromTag(CompoundTag tag, RegistryAccess registryAccess) {
        return new PollutionAnalyzerCleanerOption(
                tag.getString("medium"),
                ItemStack.parseOptional(registryAccess, tag.getCompound("stack")),
                tag.getString("displayName"),
                tag.getString("descriptionKey"),
                tag.getBoolean("blockLike"),
                tag.getDouble("estimatedReductionPerSecond"),
                tag.getDouble("baseReductionPerSecond"),
                tag.getDouble("levelBonusReductionPerSecond"),
                tag.getInt("detectedLevel"),
                tag.getInt("maxLevel"),
                tag.getInt("recommendedBaseMachineCount"),
                tag.getInt("recommendedSingleMachineLevel")
        );
    }

    public String getMedium() {
        return medium;
    }

    public ItemStack getStack() {
        return stack.copy();
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public boolean isBlockLike() {
        return blockLike;
    }

    public double getEstimatedReductionPerSecond() {
        return estimatedReductionPerSecond;
    }

    public double getBaseReductionPerSecond() {
        return baseReductionPerSecond;
    }

    public double getLevelBonusReductionPerSecond() {
        return levelBonusReductionPerSecond;
    }

    public int getDetectedLevel() {
        return detectedLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getRecommendedBaseMachineCount() {
        return recommendedBaseMachineCount;
    }

    public int getRecommendedSingleMachineLevel() {
        return recommendedSingleMachineLevel;
    }

    public Component descriptionComponent() {
        return Component.translatable(descriptionKey);
    }

    public CompoundTag toTag(RegistryAccess registryAccess) {
        CompoundTag tag = new CompoundTag();
        tag.putString("medium", medium);
        tag.put("stack", stack.save(registryAccess));
        tag.putString("displayName", displayName);
        tag.putString("descriptionKey", descriptionKey);
        tag.putBoolean("blockLike", blockLike);

        tag.putDouble("estimatedReductionPerSecond", estimatedReductionPerSecond);
        tag.putDouble("baseReductionPerSecond", baseReductionPerSecond);
        tag.putDouble("levelBonusReductionPerSecond", levelBonusReductionPerSecond);

        tag.putInt("detectedLevel", detectedLevel);
        tag.putInt("maxLevel", maxLevel);
        tag.putInt("recommendedBaseMachineCount", recommendedBaseMachineCount);
        tag.putInt("recommendedSingleMachineLevel", recommendedSingleMachineLevel);
        return tag;
    }
}