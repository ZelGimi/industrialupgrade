package com.denfop.api.pollution.analyzer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PollutionAnalyzerTerrainColumn {

    private final int sampleX;
    private final int sampleZ;
    private final int groundHeight;
    private final int surfaceHeight;
    private final String groundBlockKey;
    private final List<String> upperBlockKeys;

    public PollutionAnalyzerTerrainColumn(
            int sampleX,
            int sampleZ,
            int groundHeight,
            int surfaceHeight,
            String groundBlockKey,
            List<String> upperBlockKeys
    ) {
        this.sampleX = sampleX;
        this.sampleZ = sampleZ;
        this.groundHeight = groundHeight;
        this.surfaceHeight = surfaceHeight;
        this.groundBlockKey = groundBlockKey;
        this.upperBlockKeys = new ArrayList<>(upperBlockKeys);
    }

    public static PollutionAnalyzerTerrainColumn fromTag(CompoundTag tag) {
        List<String> upper = new ArrayList<>();
        ListTag listTag = tag.getList("upperBlockKeys", 10);
        for (int i = 0; i < listTag.size(); i++) {
            upper.add(listTag.getCompound(i).getString("key"));
        }

        return new PollutionAnalyzerTerrainColumn(
                tag.getInt("sampleX"),
                tag.getInt("sampleZ"),
                tag.getInt("groundHeight"),
                tag.getInt("surfaceHeight"),
                tag.getString("groundBlockKey"),
                upper
        );
    }

    public int getSampleX() {
        return sampleX;
    }

    public int getSampleZ() {
        return sampleZ;
    }

    public int getGroundHeight() {
        return groundHeight;
    }

    public int getSurfaceHeight() {
        return surfaceHeight;
    }

    public String getGroundBlockKey() {
        return groundBlockKey;
    }

    public List<String> getUpperBlockKeys() {
        return Collections.unmodifiableList(upperBlockKeys);
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("sampleX", sampleX);
        tag.putInt("sampleZ", sampleZ);
        tag.putInt("groundHeight", groundHeight);
        tag.putInt("surfaceHeight", surfaceHeight);
        tag.putString("groundBlockKey", groundBlockKey);

        ListTag upper = new ListTag();
        for (String key : upperBlockKeys) {
            CompoundTag value = new CompoundTag();
            value.putString("key", key);
            upper.add(value);
        }
        tag.put("upperBlockKeys", upper);

        return tag;
    }
}