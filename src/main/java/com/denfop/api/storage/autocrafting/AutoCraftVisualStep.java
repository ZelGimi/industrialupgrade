package com.denfop.api.storage.autocrafting;

public record AutoCraftVisualStep(
        Object stack,
        int have,
        int create,
        int need,
        int willBeCreate
) {

}
