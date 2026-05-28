package com.denfop.client.intro;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class IntroImageManager {

    private static final int TILE_SIZE = 256;

    private final Map<String, IntroRemoteImage> cache = new ConcurrentHashMap<>();

    private IntroImageManager() {
    }

    public static IntroImageManager getInstance() {
        return Holder.INSTANCE;
    }

    public IntroRemoteImage get(String source) {
        IntroRemoteImage image = cache.computeIfAbsent(source, IntroRemoteImage::new);
        startLoadIfNeeded(image);
        return image;
    }

    private void startLoadIfNeeded(IntroRemoteImage image) {
        if (!image.tryStart()) {
            return;
        }

        image.setLoading();

        CompletableFuture.runAsync(() -> {
            try {
                byte[] data = loadBytes(image.getSource());
                registerOnMainThread(image, data);
            } catch (Throwable throwable) {
                image.setFailed(throwable.getMessage() == null ? "Unknown image loading error" : throwable.getMessage());
            }
        }, Util.backgroundExecutor());
    }

    private void registerOnMainThread(IntroRemoteImage image, byte[] data) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            NativeImage nativeImage = null;
            try {
                nativeImage = NativeImage.read(new ByteArrayInputStream(data));

                int imageWidth = nativeImage.getWidth();
                int imageHeight = nativeImage.getHeight();

                String hash = safeSha1(image.getSource());
                List<IntroRemoteImage.Tile> tiles = new ArrayList<>();

                int tileIndex = 0;
                for (int tileY = 0; tileY < imageHeight; tileY += TILE_SIZE) {
                    for (int tileX = 0; tileX < imageWidth; tileX += TILE_SIZE) {
                        int partWidth = Math.min(TILE_SIZE, imageWidth - tileX);
                        int partHeight = Math.min(TILE_SIZE, imageHeight - tileY);

                        NativeImage part = copySubImage(nativeImage, tileX, tileY, partWidth, partHeight);
                        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                                IntroConstants.MODID,
                                "intro_dynamic/" + hash + "_" + tileIndex
                        );

                        DynamicTexture texture = new DynamicTexture(part);
                        texture.setFilter(false, false);
                        minecraft.getTextureManager().register(id, texture);

                        tiles.add(new IntroRemoteImage.Tile(id, tileX, tileY, partWidth, partHeight));
                        tileIndex++;
                    }
                }

                image.setReady(tiles, imageWidth, imageHeight);
            } catch (Throwable throwable) {
                image.setFailed(throwable.getMessage() == null ? "Failed to register texture" : throwable.getMessage());
            } finally {
                if (nativeImage != null) {
                    nativeImage.close();
                }
            }
        });
    }

    private NativeImage copySubImage(NativeImage source, int srcX, int srcY, int width, int height) {
        NativeImage out = new NativeImage(width, height, false);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                out.setPixelRGBA(x, y, source.getPixelRGBA(srcX + x, srcY + y));
            }
        }

        return out;
    }

    private byte[] loadBytes(String source) throws Exception {
        if (source.startsWith("resource:")) {
            return loadResourceBytes(source);
        }

        Path cachePath = getDiskCachePath(source);
        if (Files.exists(cachePath)) {
            return Files.readAllBytes(cachePath);
        }

        byte[] downloaded = downloadBytes(source);
        Files.createDirectories(cachePath.getParent());
        Files.write(cachePath, downloaded);
        return downloaded;
    }

    private byte[] loadResourceBytes(String source) throws Exception {
        String raw = source.substring("resource:".length());
        int split = raw.indexOf(':');
        if (split <= 0 || split >= raw.length() - 1) {
            throw new IllegalArgumentException("Invalid resource source: " + source);
        }

        String namespace = raw.substring(0, split);
        String path = raw.substring(split + 1);
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(namespace, path);

        Minecraft minecraft = Minecraft.getInstance();
        Optional<Resource> resourceOptional = minecraft.getResourceManager().getResource(location);
        if (resourceOptional.isEmpty()) {
            throw new IOException("Resource not found: " + location);
        }

        try (InputStream inputStream = resourceOptional.get().open()) {
            return inputStream.readAllBytes();
        }
    }

    private byte[] downloadBytes(String urlString) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setConnectTimeout(3500);
        connection.setReadTimeout(6000);
        connection.setUseCaches(true);
        connection.setRequestProperty("User-Agent", "IndustrialUpgradeIntroScreen/1.0");
        connection.setInstanceFollowRedirects(true);

        int code = connection.getResponseCode();
        if (code < 200 || code >= 300) {
            throw new IOException("HTTP " + code + " while loading image");
        }

        try (InputStream inputStream = connection.getInputStream()) {
            return inputStream.readAllBytes();
        } finally {
            connection.disconnect();
        }
    }

    private Path getDiskCachePath(String source) {
        Path root = Minecraft.getInstance().gameDirectory.toPath().resolve(IntroConstants.CACHE_DIR);
        return root.resolve(safeSha1(source) + ".img");
    }

    private String safeSha1(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            return Integer.toHexString(value.hashCode());
        }
    }

    private static class Holder {
        private static final IntroImageManager INSTANCE = new IntroImageManager();
    }
}