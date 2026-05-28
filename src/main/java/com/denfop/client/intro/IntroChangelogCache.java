package com.denfop.client.intro;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class IntroChangelogCache {

    private static CompletableFuture<IntroChangelogResult> future;
    private static IntroChangelogResult result;

    private IntroChangelogCache() {
    }

    public static synchronized void request() {
        poll();

        if (result != null && result.remoteAvailable()) {
            return;
        }

        if (future != null && !future.isDone()) {
            return;
        }

        future = IntroChangelogService.requestAsync();
    }

    public static synchronized void reset() {
        future = null;
        result = null;
    }

    public static synchronized boolean isReady() {
        poll();
        return result != null && result.remoteAvailable();
    }

    public static synchronized List<String> getCurrentChangelogLines(List<String> fallbackLines) {
        poll();

        if (result != null && result.remoteAvailable() && !result.currentChangelogLines().isEmpty()) {
            return result.currentChangelogLines();
        }

        return fallbackLines == null ? List.of() : fallbackLines;
    }

    public static synchronized List<String> getFullChangelogLines(List<String> fallbackLines) {
        poll();

        if (result != null && result.remoteAvailable() && !result.fullChangelogLines().isEmpty()) {
            return result.fullChangelogLines();
        }

        return fallbackLines == null ? List.of() : fallbackLines;
    }

    public static synchronized IntroChangelogResult getResultOrNull() {
        poll();
        return result;
    }

    private static void poll() {
        if (future != null && future.isDone()) {
            IntroChangelogResult loaded = future.getNow(null);
            if (loaded != null) {
                result = loaded;
            }
            future = null;
        }
    }
}
