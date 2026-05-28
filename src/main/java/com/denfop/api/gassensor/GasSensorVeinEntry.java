package com.denfop.api.gassensor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GasSensorVeinEntry {

    private final String id;
    private final String translationKey;
    private final String typeName;
    private final String fluidRegistryName;

    private final int blockX;
    private final int blockY;
    private final int blockZ;

    private final int chunkX;
    private final int chunkZ;

    private final int amountMb;
    private final double distanceBlocks;

    private final int minRelativeX;
    private final int minRelativeY;
    private final int minRelativeZ;
    private final int maxRelativeX;
    private final int maxRelativeY;
    private final int maxRelativeZ;

    private final List<PreviewCell> previewCells;

    public GasSensorVeinEntry(
            String id,
            String translationKey,
            String typeName,
            String fluidRegistryName,
            int blockX,
            int blockY,
            int blockZ,
            int chunkX,
            int chunkZ,
            int amountMb,
            double distanceBlocks,
            int minRelativeX,
            int minRelativeY,
            int minRelativeZ,
            int maxRelativeX,
            int maxRelativeY,
            int maxRelativeZ,
            List<PreviewCell> previewCells
    ) {
        this.id = id;
        this.translationKey = translationKey;
        this.typeName = typeName;
        this.fluidRegistryName = fluidRegistryName;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.amountMb = amountMb;
        this.distanceBlocks = distanceBlocks;
        this.minRelativeX = minRelativeX;
        this.minRelativeY = minRelativeY;
        this.minRelativeZ = minRelativeZ;
        this.maxRelativeX = maxRelativeX;
        this.maxRelativeY = maxRelativeY;
        this.maxRelativeZ = maxRelativeZ;
        this.previewCells = new ArrayList<>(previewCells);
        this.previewCells.sort(Comparator
                .comparingInt(PreviewCell::relativeY)
                .thenComparingInt(PreviewCell::relativeZ)
                .thenComparingInt(PreviewCell::relativeX));
    }

    public static GasSensorVeinEntry fromTag(CompoundTag tag) {
        List<PreviewCell> cells = new ArrayList<>();
        ListTag list = tag.getList("previewCells", 10);
        for (int i = 0; i < list.size(); i++) {
            cells.add(PreviewCell.fromTag(list.getCompound(i)));
        }

        return new GasSensorVeinEntry(
                tag.getString("id"),
                tag.getString("translationKey"),
                tag.getString("typeName"),
                tag.getString("fluidRegistryName"),
                tag.getInt("blockX"),
                tag.getInt("blockY"),
                tag.getInt("blockZ"),
                tag.getInt("chunkX"),
                tag.getInt("chunkZ"),
                tag.getInt("amountMb"),
                tag.getDouble("distanceBlocks"),
                tag.getInt("minRelativeX"),
                tag.getInt("minRelativeY"),
                tag.getInt("minRelativeZ"),
                tag.getInt("maxRelativeX"),
                tag.getInt("maxRelativeY"),
                tag.getInt("maxRelativeZ"),
                cells
        );
    }

    public String getId() {
        return id;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getFluidRegistryName() {
        return fluidRegistryName;
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

    public int getAmountMb() {
        return amountMb;
    }

    public double getDistanceBlocks() {
        return distanceBlocks;
    }

    public int getMinRelativeX() {
        return minRelativeX;
    }

    public int getMinRelativeY() {
        return minRelativeY;
    }

    public int getMinRelativeZ() {
        return minRelativeZ;
    }

    public int getMaxRelativeX() {
        return maxRelativeX;
    }

    public int getMaxRelativeY() {
        return maxRelativeY;
    }

    public int getMaxRelativeZ() {
        return maxRelativeZ;
    }

    public int getSpanX() {
        return Math.max(1, maxRelativeX - minRelativeX + 1);
    }

    public int getSpanY() {
        return Math.max(1, maxRelativeY - minRelativeY + 1);
    }

    public int getSpanZ() {
        return Math.max(1, maxRelativeZ - minRelativeZ + 1);
    }

    public int getPreviewCellCount() {
        return previewCells.size();
    }

    public List<PreviewCell> getPreviewCells() {
        return Collections.unmodifiableList(previewCells);
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id);
        tag.putString("translationKey", translationKey == null ? "" : translationKey);
        tag.putString("typeName", typeName == null ? "" : typeName);
        tag.putString("fluidRegistryName", fluidRegistryName == null ? "" : fluidRegistryName);

        tag.putInt("blockX", blockX);
        tag.putInt("blockY", blockY);
        tag.putInt("blockZ", blockZ);
        tag.putInt("chunkX", chunkX);
        tag.putInt("chunkZ", chunkZ);

        tag.putInt("amountMb", amountMb);
        tag.putDouble("distanceBlocks", distanceBlocks);

        tag.putInt("minRelativeX", minRelativeX);
        tag.putInt("minRelativeY", minRelativeY);
        tag.putInt("minRelativeZ", minRelativeZ);
        tag.putInt("maxRelativeX", maxRelativeX);
        tag.putInt("maxRelativeY", maxRelativeY);
        tag.putInt("maxRelativeZ", maxRelativeZ);

        ListTag cells = new ListTag();
        for (PreviewCell cell : previewCells) {
            cells.add(cell.toTag());
        }
        tag.put("previewCells", cells);
        return tag;
    }

    public record PreviewCell(int relativeX, int relativeY, int relativeZ, int amountMb) {

        public static PreviewCell fromTag(CompoundTag tag) {
            return new PreviewCell(
                    tag.getInt("relativeX"),
                    tag.getInt("relativeY"),
                    tag.getInt("relativeZ"),
                    tag.getInt("amountMb")
            );
        }

        public CompoundTag toTag() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("relativeX", relativeX);
            tag.putInt("relativeY", relativeY);
            tag.putInt("relativeZ", relativeZ);
            tag.putInt("amountMb", amountMb);
            return tag;
        }
    }
}