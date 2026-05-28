package com.denfop.api.multiblock.preview;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class MultiblockPreviewModel {

    private static final Comparator<MultiblockPreviewEntry> STEP_ORDER = (a, b) -> {
        if (a.isOrigin() && !b.isOrigin()) return -1;
        if (!a.isOrigin() && b.isOrigin()) return 1;

        BlockPos pa = a.relativePos();
        BlockPos pb = b.relativePos();

        int byY = Integer.compare(pa.getY(), pb.getY());
        if (byY != 0) return byY;

        int byZ = Integer.compare(pa.getZ(), pb.getZ());
        if (byZ != 0) return byZ;

        return Integer.compare(pa.getX(), pb.getX());
    };

    private final List<MultiblockPreviewEntry> stepEntries;
    private final List<LayerGroup> layers;
    private final List<UsedStackEntry> usedStacks;

    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public MultiblockPreviewModel(Map<BlockPos, ItemStack> structureMap, Map<BlockPos, Direction> rotationMap) {
        if (structureMap == null || structureMap.isEmpty()) {
            throw new IllegalArgumentException("structureMap cannot be null or empty");
        }

        Map<BlockPos, Direction> safeRotationMap = rotationMap == null ? Collections.emptyMap() : rotationMap;

        List<MultiblockPreviewEntry> entries = new ArrayList<>(structureMap.size());
        for (Map.Entry<BlockPos, ItemStack> e : structureMap.entrySet()) {
            if (e.getKey() == null || e.getValue() == null || e.getValue().isEmpty()) {
                continue;
            }

            BlockPos pos = e.getKey().immutable();
            Direction direction = safeRotationMap.get(pos);

            entries.add(new MultiblockPreviewEntry(
                    pos,
                    e.getValue().copy(),
                    direction
            ));
        }

        if (entries.isEmpty()) {
            throw new IllegalArgumentException("No valid preview entries");
        }

        entries.sort(STEP_ORDER);
        this.stepEntries = Collections.unmodifiableList(entries);

        TreeMap<Integer, List<MultiblockPreviewEntry>> byLayer = new TreeMap<>();
        for (MultiblockPreviewEntry entry : this.stepEntries) {
            byLayer.computeIfAbsent(entry.relativePos().getY(), y -> new ArrayList<>()).add(entry);
        }

        List<LayerGroup> groupedLayers = new ArrayList<>(byLayer.size());
        for (Map.Entry<Integer, List<MultiblockPreviewEntry>> layerEntry : byLayer.entrySet()) {
            List<MultiblockPreviewEntry> layerBlocks = new ArrayList<>(layerEntry.getValue());
            layerBlocks.sort(STEP_ORDER);
            groupedLayers.add(new LayerGroup(layerEntry.getKey(), Collections.unmodifiableList(layerBlocks)));
        }
        this.layers = Collections.unmodifiableList(groupedLayers);

        Map<String, UsedStackEntry> usedMap = new LinkedHashMap<>();
        for (MultiblockPreviewEntry entry : this.stepEntries) {
            ItemStack stack = entry.stack();
            String key = buildStackKey(stack);
            UsedStackEntry existing = usedMap.get(key);
            if (existing == null) {
                usedMap.put(key, new UsedStackEntry(stack.copy(), 1));
            } else {
                existing.increment();
            }
        }
        this.usedStacks = Collections.unmodifiableList(new ArrayList<>(usedMap.values()));

        int localMinX = Integer.MAX_VALUE;
        int localMinY = Integer.MAX_VALUE;
        int localMinZ = Integer.MAX_VALUE;
        int localMaxX = Integer.MIN_VALUE;
        int localMaxY = Integer.MIN_VALUE;
        int localMaxZ = Integer.MIN_VALUE;

        for (MultiblockPreviewEntry entry : this.stepEntries) {
            BlockPos pos = entry.relativePos();
            localMinX = Math.min(localMinX, pos.getX());
            localMinY = Math.min(localMinY, pos.getY());
            localMinZ = Math.min(localMinZ, pos.getZ());

            localMaxX = Math.max(localMaxX, pos.getX());
            localMaxY = Math.max(localMaxY, pos.getY());
            localMaxZ = Math.max(localMaxZ, pos.getZ());
        }

        this.minX = localMinX;
        this.minY = localMinY;
        this.minZ = localMinZ;
        this.maxX = localMaxX;
        this.maxY = localMaxY;
        this.maxZ = localMaxZ;
    }

    public static MultiblockPreviewModel fromMap(Map<BlockPos, ItemStack> structureMap) {
        return new MultiblockPreviewModel(structureMap, Collections.emptyMap());
    }

    public static MultiblockPreviewModel fromMap(Map<BlockPos, ItemStack> structureMap, Map<BlockPos, Direction> rotationMap) {
        return new MultiblockPreviewModel(structureMap, rotationMap);
    }

    private static String buildStackKey(ItemStack stack) {
        String itemKey = stack.getItem().toString();
        String tagKey = !stack.getComponents().isEmpty() ? stack.getComponents().toString() : "";
        return itemKey + "|" + tagKey;
    }

    public List<MultiblockPreviewEntry> stepEntries() {
        return this.stepEntries;
    }

    public List<LayerGroup> layers() {
        return this.layers;
    }

    public List<UsedStackEntry> usedStacks() {
        return this.usedStacks;
    }

    public int stageCount(PreviewMode mode) {
        return switch (mode) {
            case STEP_BY_STEP -> this.stepEntries.size();
            case LAYER_BY_LAYER -> this.layers.size();
        };
    }

    public MultiblockPreviewEntry getStepEntry(int index) {
        if (index < 0 || index >= this.stepEntries.size()) {
            return null;
        }
        return this.stepEntries.get(index);
    }

    public LayerGroup getLayer(int index) {
        if (index < 0 || index >= this.layers.size()) {
            return null;
        }
        return this.layers.get(index);
    }

    public int minX() {
        return minX;
    }

    public int minY() {
        return minY;
    }

    public int minZ() {
        return minZ;
    }

    public int maxX() {
        return maxX;
    }

    public int maxY() {
        return maxY;
    }

    public int maxZ() {
        return maxZ;
    }

    public int sizeX() {
        return this.maxX - this.minX + 1;
    }

    public int sizeY() {
        return this.maxY - this.minY + 1;
    }

    public int sizeZ() {
        return this.maxZ - this.minZ + 1;
    }

    public int maxSpan() {
        return Math.max(this.sizeX(), Math.max(this.sizeY(), this.sizeZ()));
    }

    public record LayerGroup(int yLevel, List<MultiblockPreviewEntry> entries) {
        public int size() {
            return this.entries.size();
        }

        public Collection<MultiblockPreviewEntry> blocks() {
            return this.entries;
        }
    }

    public static class UsedStackEntry {
        private final ItemStack stack;
        private int count;

        public UsedStackEntry(ItemStack stack, int count) {
            this.stack = stack;
            this.count = count;
        }

        public ItemStack stack() {
            return this.stack;
        }

        public int count() {
            return this.count;
        }

        public void increment() {
            this.count++;
        }
    }
}