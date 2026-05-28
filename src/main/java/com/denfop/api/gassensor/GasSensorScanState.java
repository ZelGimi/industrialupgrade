package com.denfop.api.gassensor;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GasSensorScanState {

    private final boolean supportedDimension;
    private final boolean scanning;

    private final int radiusChunks;
    private final int centerChunkX;
    private final int centerChunkZ;

    private final int totalCandidates;
    private final int processedCandidates;

    private final int currentChunkX;
    private final int currentChunkZ;

    private final List<GasSensorVeinEntry> veins;

    public GasSensorScanState(
            boolean supportedDimension,
            boolean scanning,
            int radiusChunks,
            int centerChunkX,
            int centerChunkZ,
            int totalCandidates,
            int processedCandidates,
            int currentChunkX,
            int currentChunkZ,
            List<GasSensorVeinEntry> veins
    ) {
        this.supportedDimension = supportedDimension;
        this.scanning = scanning;
        this.radiusChunks = radiusChunks;
        this.centerChunkX = centerChunkX;
        this.centerChunkZ = centerChunkZ;
        this.totalCandidates = totalCandidates;
        this.processedCandidates = processedCandidates;
        this.currentChunkX = currentChunkX;
        this.currentChunkZ = currentChunkZ;
        this.veins = new ArrayList<>(veins);
        this.veins.sort(Comparator.comparingDouble(GasSensorVeinEntry::getDistanceBlocks));
    }

    public static GasSensorScanState empty(int radiusChunks) {
        return new GasSensorScanState(true, false, radiusChunks, 0, 0, 0, 0, 0, 0, List.of());
    }

    public static GasSensorScanState unsupported(int radiusChunks, int centerChunkX, int centerChunkZ) {
        return new GasSensorScanState(false, false, radiusChunks, centerChunkX, centerChunkZ, 0, 0, centerChunkX, centerChunkZ, List.of());
    }

    public static GasSensorScanState fromTag(CompoundTag tag) {
        List<GasSensorVeinEntry> veins = new ArrayList<>();
        ListTag list = tag.getList("veins", 10);
        for (int i = 0; i < list.size(); i++) {
            veins.add(GasSensorVeinEntry.fromTag(list.getCompound(i)));
        }

        return new GasSensorScanState(
                tag.getBoolean("supportedDimension"),
                tag.getBoolean("scanning"),
                tag.getInt("radiusChunks"),
                tag.getInt("centerChunkX"),
                tag.getInt("centerChunkZ"),
                tag.getInt("totalCandidates"),
                tag.getInt("processedCandidates"),
                tag.getInt("currentChunkX"),
                tag.getInt("currentChunkZ"),
                veins
        );
    }

    public boolean isSupportedDimension() {
        return supportedDimension;
    }

    public boolean isScanning() {
        return scanning;
    }

    public int getRadiusChunks() {
        return radiusChunks;
    }

    public int getCenterChunkX() {
        return centerChunkX;
    }

    public int getCenterChunkZ() {
        return centerChunkZ;
    }

    public int getTotalCandidates() {
        return totalCandidates;
    }

    public int getProcessedCandidates() {
        return processedCandidates;
    }

    public int getCurrentChunkX() {
        return currentChunkX;
    }

    public int getCurrentChunkZ() {
        return currentChunkZ;
    }

    public List<GasSensorVeinEntry> getVeins() {
        return Collections.unmodifiableList(veins);
    }

    public int getFoundCount() {
        return veins.size();
    }

    public float getProgress() {
        if (totalCandidates <= 0) {
            return scanning ? 0.0F : 1.0F;
        }
        return Math.min(1.0F, processedCandidates / (float) totalCandidates);
    }

    public GasSensorVeinEntry getById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        for (GasSensorVeinEntry vein : veins) {
            if (id.equals(vein.getId())) {
                return vein;
            }
        }
        return null;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("supportedDimension", supportedDimension);
        tag.putBoolean("scanning", scanning);

        tag.putInt("radiusChunks", radiusChunks);
        tag.putInt("centerChunkX", centerChunkX);
        tag.putInt("centerChunkZ", centerChunkZ);

        tag.putInt("totalCandidates", totalCandidates);
        tag.putInt("processedCandidates", processedCandidates);

        tag.putInt("currentChunkX", currentChunkX);
        tag.putInt("currentChunkZ", currentChunkZ);

        ListTag list = new ListTag();
        for (GasSensorVeinEntry vein : veins) {
            list.add(vein.toTag());
        }
        tag.put("veins", list);
        return tag;
    }
}