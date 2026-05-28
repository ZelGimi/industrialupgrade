package com.denfop.events;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.config.ModConfig;
import com.denfop.utils.Localization;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EventUpdate {

    private final Set<UUID> notifiedPlayers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public EventUpdate() {
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!this.notifiedPlayers.add(player.getUUID())) {
            return;
        }

        sendIntroMessage(player);
    }

    private void sendIntroMessage(Player player) {
        if (player.level().isClientSide || !ModConfig.COMMON.informationText.get()) {
            return;
        }

        String modVersion = Constants.MOD_VERSION;

        String message = ChatFormatting.DARK_GRAY + "================" + "\n" +
                ChatFormatting.GREEN + Localization.translate("iu.mod.name") + " " + modVersion + "\n" +
                ChatFormatting.WHITE + Localization.translate("iu.intro.chat.title") + "\n" +
                ChatFormatting.GRAY + Localization.translate("iu.intro.chat.description") + "\n" +
                ChatFormatting.YELLOW + Localization.translate("iu.intro.chat.open_hint") + "\n" +
                ChatFormatting.DARK_GRAY + "================";

        IUCore.proxy.messagePlayer(player, message);
        sendDiscordLink(player);
    }

    private void sendDiscordLink(Player player) {
        Component discordMessage = Component.literal("[" + Localization.translate("iu.discord.click") + "]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.AQUA)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(
                                ClickEvent.Action.OPEN_URL,
                                "https://discord.gg/nFHcxqVx"
                        )));

        Component fullMessage = Component.literal(Localization.translate("iu.intro.chat.discord_question") + " ")
                .withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))
                .append(discordMessage);

        player.sendSystemMessage(fullMessage);
    }

}