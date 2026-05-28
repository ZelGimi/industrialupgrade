package com.denfop.api.multiblock.preview;

import net.minecraft.network.chat.Component;

public enum PreviewMode {
    STEP_BY_STEP("preview.mode.step_by_step"),
    LAYER_BY_LAYER("preview.mode.layer_by_layer");

    private final String translationKey;

    PreviewMode(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component title() {
        return Component.translatable(this.translationKey);
    }

    public PreviewMode next() {
        return this == STEP_BY_STEP ? LAYER_BY_LAYER : STEP_BY_STEP;
    }
}