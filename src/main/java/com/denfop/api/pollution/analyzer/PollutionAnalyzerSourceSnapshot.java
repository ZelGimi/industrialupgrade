package com.denfop.api.pollution.analyzer;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class PollutionAnalyzerSourceSnapshot {

    private final String id;
    private final String name;
    private final String registryName;
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final int chunkX;
    private final int chunkZ;
    private final ItemStack renderStack;
    private final boolean active;

    private final double airCurrentContribution;
    private final double airBaseContribution;
    private final double airReductionPercent;

    private final double soilCurrentContribution;
    private final double soilBaseContribution;
    private final double soilReductionPercent;

    private final boolean cleaner;
    private final String cleanerMedium;
    private final int mechanismLevel;
    private final int maxMechanismLevel;
    private final double airCleaningPerSecond;
    private final double soilCleaningPerSecond;

    public PollutionAnalyzerSourceSnapshot(
            String id,
            String name,
            String registryName,
            int blockX,
            int blockY,
            int blockZ,
            int chunkX,
            int chunkZ,
            ItemStack renderStack,
            boolean active,
            double airCurrentContribution,
            double airBaseContribution,
            double airReductionPercent,
            double soilCurrentContribution,
            double soilBaseContribution,
            double soilReductionPercent,
            boolean cleaner,
            String cleanerMedium,
            int mechanismLevel,
            int maxMechanismLevel,
            double airCleaningPerSecond,
            double soilCleaningPerSecond
    ) {
        this.id = id;
        this.name = name;
        this.registryName = registryName;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.renderStack = renderStack.copy();
        this.active = active;
        this.airCurrentContribution = airCurrentContribution;
        this.airBaseContribution = airBaseContribution;
        this.airReductionPercent = airReductionPercent;
        this.soilCurrentContribution = soilCurrentContribution;
        this.soilBaseContribution = soilBaseContribution;
        this.soilReductionPercent = soilReductionPercent;
        this.cleaner = cleaner;
        this.cleanerMedium = cleanerMedium;
        this.mechanismLevel = mechanismLevel;
        this.maxMechanismLevel = maxMechanismLevel;
        this.airCleaningPerSecond = airCleaningPerSecond;
        this.soilCleaningPerSecond = soilCleaningPerSecond;
    }

    public static PollutionAnalyzerSourceSnapshot fromTag(CompoundTag tag, RegistryAccess access) {
        return new PollutionAnalyzerSourceSnapshot(
                tag.getString("id"),
                tag.getString("name"),
                tag.getString("registryName"),
                tag.getInt("blockX"),
                tag.getInt("blockY"),
                tag.getInt("blockZ"),
                tag.getInt("chunkX"),
                tag.getInt("chunkZ"),
                ItemStack.parseOptional(access, tag.getCompound("renderStack")),
                tag.getBoolean("active"),
                tag.getDouble("airCurrentContribution"),
                tag.getDouble("airBaseContribution"),
                tag.getDouble("airReductionPercent"),
                tag.getDouble("soilCurrentContribution"),
                tag.getDouble("soilBaseContribution"),
                tag.getDouble("soilReductionPercent"),
                tag.getBoolean("cleaner"),
                tag.getString("cleanerMedium"),
                tag.getInt("mechanismLevel"),
                tag.getInt("maxMechanismLevel"),
                tag.getDouble("airCleaningPerSecond"),
                tag.getDouble("soilCleaningPerSecond")
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Component nameComponent() {
        return Component.literal(name);
    }

    public String getRegistryName() {
        return registryName;
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public ItemStack getRenderStack() {
        return renderStack.copy();
    }

    public boolean isActive() {
        return active;
    }

    public double getAirCurrentContribution() {
        return airCurrentContribution;
    }

    public double getAirBaseContribution() {
        return airBaseContribution;
    }

    public double getAirReductionPercent() {
        return airReductionPercent;
    }

    public double getSoilCurrentContribution() {
        return soilCurrentContribution;
    }

    public double getSoilBaseContribution() {
        return soilBaseContribution;
    }

    public double getSoilReductionPercent() {
        return soilReductionPercent;
    }

    public boolean isCleaner() {
        return cleaner;
    }

    public String getCleanerMedium() {
        return cleanerMedium;
    }

    public int getMechanismLevel() {
        return mechanismLevel;
    }

    public int getMaxMechanismLevel() {
        return maxMechanismLevel;
    }

    public double getAirCleaningPerSecond() {
        return airCleaningPerSecond;
    }

    public double getSoilCleaningPerSecond() {
        return soilCleaningPerSecond;
    }

    public double getTotalCurrentContribution() {
        return airCurrentContribution + soilCurrentContribution;
    }

    public double getTotalCleaningPerSecond() {
        return airCleaningPerSecond + soilCleaningPerSecond;
    }

    public CompoundTag toTag(RegistryAccess registryAccess) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id);
        tag.putString("name", name);
        tag.putString("registryName", registryName);
        tag.putInt("blockX", blockX);
        tag.putInt("blockY", blockY);
        tag.putInt("blockZ", blockZ);
        tag.putInt("chunkX", chunkX);
        tag.putInt("chunkZ", chunkZ);
        tag.put("renderStack", renderStack.save(registryAccess));
        tag.putBoolean("active", active);

        tag.putDouble("airCurrentContribution", airCurrentContribution);
        tag.putDouble("airBaseContribution", airBaseContribution);
        tag.putDouble("airReductionPercent", airReductionPercent);

        tag.putDouble("soilCurrentContribution", soilCurrentContribution);
        tag.putDouble("soilBaseContribution", soilBaseContribution);
        tag.putDouble("soilReductionPercent", soilReductionPercent);

        tag.putBoolean("cleaner", cleaner);
        tag.putString("cleanerMedium", cleanerMedium == null ? "" : cleanerMedium);
        tag.putInt("mechanismLevel", mechanismLevel);
        tag.putInt("maxMechanismLevel", maxMechanismLevel);
        tag.putDouble("airCleaningPerSecond", airCleaningPerSecond);
        tag.putDouble("soilCleaningPerSecond", soilCleaningPerSecond);
        return tag;
    }
}