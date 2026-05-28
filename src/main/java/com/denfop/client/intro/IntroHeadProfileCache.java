package com.denfop.client.intro;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class IntroHeadProfileCache {

    private final Map<String, HeadEntry> cache = new ConcurrentHashMap<>();

    private IntroHeadProfileCache() {
    }

    public static IntroHeadProfileCache getInstance() {
        return Holder.INSTANCE;
    }

    public void renderHead(GuiGraphics guiGraphics, String nickname, int x, int y, int size) {
        HeadEntry entry = cache.computeIfAbsent(nickname, HeadEntry::new);
        startResolveIfNeeded(entry);

        if (entry.profile == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        PlayerSkin skin = minecraft.getSkinManager().getInsecureSkin(entry.profile);

        PlayerFaceRenderer.draw(guiGraphics, skin, x, y, size);
    }

    public boolean hasResolvedHead(String nickname) {
        HeadEntry entry = cache.get(nickname);
        return entry != null && entry.profile != null;
    }

    private void startResolveIfNeeded(HeadEntry entry) {
        if (!entry.started.compareAndSet(false, true)) {
            return;
        }

        GameProfile initialProfile = new GameProfile(UUID.randomUUID(), entry.nickname);

        SkullBlockEntity.fetchGameProfile(entry.nickname).thenAccept(optionalProfile ->
                entry.profile = optionalProfile.orElse(initialProfile)
        );
    }

    private static class Holder {
        private static final IntroHeadProfileCache INSTANCE = new IntroHeadProfileCache();
    }

    private static final class HeadEntry {
        private final String nickname;
        private final AtomicBoolean started = new AtomicBoolean(false);

        @Nullable
        private volatile GameProfile profile;

        private HeadEntry(String nickname) {
            this.nickname = nickname;
        }
    }
}