package com.denfop.client.intro;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class IntroHeadProfileCache {

    private final Map<String, HeadEntry> cache = new ConcurrentHashMap<>();

    private IntroHeadProfileCache() {
    }

    public static IntroHeadProfileCache getInstance() {
        return Holder.INSTANCE;
    }

    public void renderHead(IntroGuiGraphics guiGraphics, String nickname, int x, int y, int size) {
        HeadEntry entry = cache.computeIfAbsent(nickname, HeadEntry::new);
        startResolveIfNeeded(entry);

        if (entry.profile == null) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        ResourceLocation skin = minecraft.getSkinManager().getInsecureSkinLocation(entry.profile);

        RenderSystem.enableBlend();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0.0F);
        float scale = size / 8.0F;
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.blit(skin, 0, 0, 8.0F, 8.0F, 8, 8, 64, 64);
        guiGraphics.blit(skin, 0, 0, 40.0F, 8.0F, 8, 8, 64, 64);
        guiGraphics.pose().popPose();
    }

    public boolean hasResolvedHead(String nickname) {
        HeadEntry entry = cache.get(nickname);
        return entry != null && entry.profile != null;
    }

    private void startResolveIfNeeded(HeadEntry entry) {
        if (!entry.started.compareAndSet(false, true)) {
            return;
        }

        GameProfile initialProfile = new GameProfile(null, entry.nickname);

        SkullBlockEntity.updateGameprofile(initialProfile, resolvedProfile -> {
            if (resolvedProfile != null) {
                entry.profile = resolvedProfile;
            } else {
                entry.profile = initialProfile;
            }
        });
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