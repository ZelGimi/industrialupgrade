package com.denfop.client.intro;

import java.util.List;

public record IntroChangelogResult(
        String mcVersion,
        String localVersion,
        String checkedUrl,
        boolean remoteAvailable,
        List<String> currentChangelogLines,
        List<String> fullChangelogLines,
        List<String> allChangelogLines
) {
    public IntroChangelogResult {
        currentChangelogLines = currentChangelogLines == null ? List.of() : List.copyOf(currentChangelogLines);
        fullChangelogLines = fullChangelogLines == null ? List.of() : List.copyOf(fullChangelogLines);
        allChangelogLines = allChangelogLines == null ? List.of() : List.copyOf(allChangelogLines);
    }

    public static IntroChangelogResult unavailable(String mcVersion, String localVersion, String checkedUrl) {
        return new IntroChangelogResult(
                mcVersion,
                localVersion,
                checkedUrl,
                false,
                List.of(),
                List.of(),
                List.of()
        );
    }
}
