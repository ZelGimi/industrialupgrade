package com.denfop.api.pollution.analyzer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PollutionAnalyzerRecommendation {

    private static final String LITERAL_PREFIX = "literal:";

    private final int severity;
    private final String titleKey;
    private final String bodyKey;
    private final List<String> args;

    public PollutionAnalyzerRecommendation(int severity, String titleKey, String bodyKey, List<String> args) {
        this.severity = severity;
        this.titleKey = titleKey;
        this.bodyKey = bodyKey;
        this.args = new ArrayList<>(args);
    }

    public static PollutionAnalyzerRecommendation literal(int severity, String title, String body) {
        return new PollutionAnalyzerRecommendation(
                severity,
                LITERAL_PREFIX + title,
                LITERAL_PREFIX + body,
                Collections.emptyList()
        );
    }

    public static PollutionAnalyzerRecommendation fromTag(CompoundTag tag) {
        List<String> args = new ArrayList<>();
        ListTag argsTag = tag.getList("args", 10);
        for (int i = 0; i < argsTag.size(); i++) {
            args.add(argsTag.getCompound(i).getString("value"));
        }
        return new PollutionAnalyzerRecommendation(
                tag.getInt("severity"),
                tag.getString("titleKey"),
                tag.getString("bodyKey"),
                args
        );
    }

    public int getSeverity() {
        return severity;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public String getBodyKey() {
        return bodyKey;
    }

    public List<String> getArgs() {
        return Collections.unmodifiableList(args);
    }

    public Component titleComponent() {
        if (titleKey != null && titleKey.startsWith(LITERAL_PREFIX)) {
            return Component.literal(titleKey.substring(LITERAL_PREFIX.length()));
        }
        return Component.translatable(titleKey);
    }

    public Component bodyComponent() {
        if (bodyKey != null && bodyKey.startsWith(LITERAL_PREFIX)) {
            return Component.literal(bodyKey.substring(LITERAL_PREFIX.length()));
        }
        return Component.translatable(bodyKey, args.toArray());
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("severity", severity);
        tag.putString("titleKey", titleKey);
        tag.putString("bodyKey", bodyKey);

        ListTag argsTag = new ListTag();
        for (String arg : args) {
            CompoundTag value = new CompoundTag();
            value.putString("value", arg);
            argsTag.add(value);
        }
        tag.put("args", argsTag);
        return tag;
    }
}