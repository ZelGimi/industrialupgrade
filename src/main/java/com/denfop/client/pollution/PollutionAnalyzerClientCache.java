package com.denfop.client.pollution;

import com.denfop.api.pollution.analyzer.PollutionAnalyzerSnapshot;
import com.denfop.network.packet.PacketPollutionAnalyzerRequest;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class PollutionAnalyzerClientCache {

    private static PollutionAnalyzerSnapshot snapshot;

    private PollutionAnalyzerClientCache() {
    }

    public static PollutionAnalyzerSnapshot getSnapshot() {
        return snapshot;
    }

    public static void setSnapshot(PollutionAnalyzerSnapshot snapshot) {
        PollutionAnalyzerClientCache.snapshot = snapshot;
    }

    public static void clear() {
        snapshot = null;
    }

    public static void requestRefresh() {
        new PacketPollutionAnalyzerRequest(true, Minecraft.getInstance().level.registryAccess());
    }
}