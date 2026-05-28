package com.denfop.client.pollution;

import com.denfop.network.packet.PacketPollutionAnalyzerRequest;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class PollutionAnalyzerClientHooks {

    private PollutionAnalyzerClientHooks() {
    }

    public static void open(ItemStack analyzerStack) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }

        minecraft.setScreen(new PollutionAnalyzerScreen(analyzerStack));
        new PacketPollutionAnalyzerRequest(true, minecraft.level.registryAccess());
    }
}