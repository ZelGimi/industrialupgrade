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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IntroVersionCheckService {

    private static final String MOD_ID = Constants.MOD_ID;
    private static final String RAW_URL_PATTERN =
            "https://raw.githubusercontent.com/ZelGimi/industrialupgrade/%s/version.txt";

    private static final Pattern VERSION_HEADER_PATTERN =
            Pattern.compile("^\\s*Version\\s*:\\s*([^:]+?)\\s*:?\\s*$", Pattern.CASE_INSENSITIVE);

    private IntroVersionCheckService() {
    }

    public static CompletableFuture<IntroVersionCheckResult> checkAsync() {
        return CompletableFuture.supplyAsync(IntroVersionCheckService::checkNow, Util.backgroundExecutor());
    }

    public static IntroVersionCheckResult checkNow() {
        String mcVersion = SharedConstants.getCurrentVersion().getName();
        String localVersion = getLocalModVersion();
        String branch = resolveBranchForMcVersion(mcVersion);
        String url = String.format(RAW_URL_PATTERN, branch);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(2500);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "IndustrialUpgrade-IntroScreen");
            connection.connect();

            int code = connection.getResponseCode();
            if (code != 200) {
                return unavailable(mcVersion, localVersion, url);
            }

            String remoteVersion = null;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String candidate = line.trim();
                    if (candidate.isBlank()) {
                        continue;
                    }

                    Matcher matcher = VERSION_HEADER_PATTERN.matcher(candidate);
                    if (matcher.matches()) {
                        remoteVersion = matcher.group(1).trim();
                        break;
                    }

                    if (remoteVersion == null && looksLikeVersion(candidate)) {
                        remoteVersion = candidate;
                        break;
                    }
                }
            }

            if (remoteVersion == null || remoteVersion.isBlank()) {
                return unavailable(mcVersion, localVersion, url);
            }

            boolean updateRecommended = !normalize(remoteVersion).equals(normalize(localVersion));

            return new IntroVersionCheckResult(
                    mcVersion,
                    localVersion,
                    remoteVersion,
                    true,
                    updateRecommended,
                    url
            );
        } catch (Exception ignored) {
            return unavailable(mcVersion, localVersion, url);
        }
    }

    private static IntroVersionCheckResult unavailable(String mcVersion, String localVersion, String url) {
        return new IntroVersionCheckResult(
                mcVersion,
                localVersion,
                null,
                false,
                false,
                url
        );
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

    private static boolean looksLikeVersion(String value) {
        return value.matches("\\d+(\\.\\d+){0,4}([A-Za-z]+\\d*)?");
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase().replace(" ", "");
    }
}
