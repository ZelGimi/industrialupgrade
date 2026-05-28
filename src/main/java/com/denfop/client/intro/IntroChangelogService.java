package com.denfop.client.intro;

import com.denfop.Constants;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.neoforged.fml.ModList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IntroChangelogService {

    private static final String MOD_ID = Constants.MOD_ID;
    private static final String RAW_URL_PATTERN =
            "https://raw.githubusercontent.com/ZelGimi/industrialupgrade/%s/changelog.txt";

    private static final Pattern VERSION_HEADER_PATTERN =
            Pattern.compile("^\\s*Version\\s*:\\s*([^:]+?)\\s*:?\\s*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern FIRST_TWO_NUMBERS_PATTERN =
            Pattern.compile("(\\d+)(?:\\.(\\d+))?");

    private IntroChangelogService() {
    }

    public static CompletableFuture<IntroChangelogResult> requestAsync() {
        return CompletableFuture.supplyAsync(IntroChangelogService::requestNow, Util.backgroundExecutor());
    }

    public static IntroChangelogResult requestNow() {
        String mcVersion = SharedConstants.getCurrentVersion().getName();
        String localVersion = getLocalModVersion();
        String branch = resolveBranchForMcVersion(mcVersion);
        String url = String.format(RAW_URL_PATTERN, branch);

        try {
            List<String> rawLines = downloadLines(url);

            List<String> current = extractCurrentChangelogLines(rawLines, localVersion);
            List<String> full = extractFullChangelogLines(rawLines, localVersion);
            List<String> all = extractAllChangelogLines(rawLines);

            return new IntroChangelogResult(
                    mcVersion,
                    localVersion,
                    url,
                    true,
                    current,
                    full,
                    all
            );
        } catch (Exception ignored) {
            return IntroChangelogResult.unavailable(mcVersion, localVersion, url);
        }
    }

    public static List<String> extractCurrentChangelogLines(List<String> rawLines, String localVersion) {
        List<ChangelogBlock> blocks = parseBlocks(rawLines);
        if (blocks.isEmpty()) {
            return safeCopy(rawLines);
        }

        VersionBase localBase = VersionBase.from(localVersion);
        List<ChangelogBlock> selected = new ArrayList<>();

        for (ChangelogBlock block : blocks) {
            if (block.base().equals(localBase)) {
                selected.add(block);
            }
        }

        return flattenBlocks(selected);
    }

    public static List<String> extractFullChangelogLines(List<String> rawLines, String localVersion) {
        List<ChangelogBlock> blocks = parseBlocks(rawLines);
        if (blocks.isEmpty()) {
            return safeCopy(rawLines);
        }

        VersionBase localBase = VersionBase.from(localVersion);
        List<ChangelogBlock> selected = new ArrayList<>();

        for (ChangelogBlock block : blocks) {
            if (block.base().compareTo(localBase) >= 0) {
                selected.add(block);
            }
        }

        return flattenBlocks(selected);
    }

    public static List<String> extractAllChangelogLines(List<String> rawLines) {
        List<ChangelogBlock> blocks = parseBlocks(rawLines);
        if (blocks.isEmpty()) {
            return safeCopy(rawLines);
        }
        return flattenBlocks(blocks);
    }

    public static VersionBase getBaseVersion(String version) {
        return VersionBase.from(version);
    }

    private static List<String> downloadLines(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(2500);
        connection.setReadTimeout(2500);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "IndustrialUpgrade-IntroScreen");
        connection.connect();

        int code = connection.getResponseCode();
        if (code != 200) {
            throw new IllegalStateException("Unexpected response code: " + code);
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    private static List<ChangelogBlock> parseBlocks(List<String> rawLines) {
        if (rawLines == null || rawLines.isEmpty()) {
            return List.of();
        }

        List<ChangelogBlock> blocks = new ArrayList<>();

        String currentVersion = null;
        List<String> currentLines = new ArrayList<>();

        for (String rawLine : rawLines) {
            String line = rawLine == null ? "" : rawLine;
            Matcher matcher = VERSION_HEADER_PATTERN.matcher(line);

            if (matcher.matches()) {
                if (currentVersion != null) {
                    blocks.add(new ChangelogBlock(
                            currentVersion,
                            VersionBase.from(currentVersion),
                            trimTrailingBlankLines(currentLines)
                    ));
                }

                currentVersion = matcher.group(1).trim();
                currentLines = new ArrayList<>();
                currentLines.add("Version:" + currentVersion + ":");
                continue;
            }

            if (currentVersion != null) {
                currentLines.add(line);
            }
        }

        if (currentVersion != null) {
            blocks.add(new ChangelogBlock(
                    currentVersion,
                    VersionBase.from(currentVersion),
                    trimTrailingBlankLines(currentLines)
            ));
        }

        return blocks;
    }

    private static List<String> flattenBlocks(List<ChangelogBlock> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return List.of();
        }

        List<String> out = new ArrayList<>();

        for (ChangelogBlock block : blocks) {
            if (!out.isEmpty()) {
                out.add("");
            }
            out.addAll(block.lines());
        }

        return out;
    }

    private static List<String> trimTrailingBlankLines(List<String> source) {
        if (source == null || source.isEmpty()) {
            return List.of();
        }

        int end = source.size();
        while (end > 0 && source.get(end - 1).isBlank()) {
            end--;
        }

        if (end <= 0) {
            return List.of();
        }

        return List.copyOf(source.subList(0, end));
    }

    private static List<String> safeCopy(List<String> source) {
        if (source == null || source.isEmpty()) {
            return List.of();
        }
        return List.copyOf(source);
    }

    private static String getLocalModVersion() {
        Optional<String> version = ModList.get()
                .getModContainerById(MOD_ID)
                .map(container -> container.getModInfo().getVersion().toString());

        return version.orElse(Constants.MOD_VERSION);
    }

    private static String resolveBranchForMcVersion(String mcVersion) {
        return switch (mcVersion) {
            case "1.20.1" -> "1-20-1-dev";
            case "1.21.1" -> "1-21-1-dev";
            case "1.19.2" -> "1-19-2-dev";
            default -> mcVersion.replace('.', '-') + "-dev";
        };
    }

    private record ChangelogBlock(
            String version,
            VersionBase base,
            List<String> lines
    ) {
    }

    public record VersionBase(int major, int minor) implements Comparable<VersionBase> {

        public static VersionBase from(String version) {
            if (version == null || version.isBlank()) {
                return new VersionBase(0, 0);
            }

            Matcher matcher = FIRST_TWO_NUMBERS_PATTERN.matcher(version.trim());
            if (!matcher.find()) {
                return new VersionBase(0, 0);
            }

            int major = parseIntSafe(matcher.group(1));
            int minor = matcher.group(2) == null ? 0 : parseIntSafe(matcher.group(2));

            return new VersionBase(major, minor);
        }

        private static int parseIntSafe(String value) {
            try {
                return Integer.parseInt(value);
            } catch (Exception ignored) {
                return 0;
            }
        }

        @Override
        public int compareTo(VersionBase other) {
            if (other == null) {
                return 1;
            }

            int majorCompare = Integer.compare(this.major, other.major);
            if (majorCompare != 0) {
                return majorCompare;
            }

            return Integer.compare(this.minor, other.minor);
        }

        @Override
        public String toString() {
            return major + "." + minor;
        }
    }
}
