package com.denfop.events;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.utils.Localization;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class EventUpdate {

    private boolean playerNotified = false;
    private int delay = 120;

    public EventUpdate() {
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        if (!this.playerNotified) {
            this.playerNotified = true;
            sendModCheckMessage(player);
        }
    }

    private void sendModCheckMessage(Player player) {
        if (!player.getLevel().isClientSide) {
            String modVersion = Constants.MOD_VERSION;

            boolean hasPowerUtilities = ModList.get().isLoaded("powerutils");
            boolean hasSimplyQuarry = ModList.get().isLoaded("simplyquarries");
            boolean hasQuantumGenerators = ModList.get().isLoaded("quantumgenerators");
            boolean hasJEI = ModList.get().isLoaded("jei");
            boolean oneprobe = ModList.get().isLoaded("oneprobe");
            boolean jade = ModList.get().isLoaded("jade");

            String message = ChatFormatting.DARK_GRAY + "================" + "\n" +
                    ChatFormatting.GREEN + Localization.translate("iu.mod.name") + " " + modVersion + "\n" +
                    ChatFormatting.WHITE + Localization.translate("iu.addons.installed") + "\n" +
                    formatAddonStatus("Power Utilities", hasPowerUtilities) + "\n" +
                    formatAddonStatus("Simply Quarry", hasSimplyQuarry) + "\n" +
                    formatAddonStatus("Quantum Generators", hasQuantumGenerators) + "\n" +
                    ChatFormatting.YELLOW + Localization.translate("iu.addons.optional") + "\n\n" +
                    formatAddonStatus("Diamond Vein", ModList.get().isLoaded("diamondvein")) + "\n" +
                    formatAddonStatus("Fast Primitive Age", ModList.get().isLoaded("fastprimitiveage")) + "\n" +
                    formatAddonStatus("Fast Steam Age", ModList.get().isLoaded("faststeameage")) + "\n"+
                    formatAddonStatus("Mining World Upgrade", ModList.get().isLoaded("miningworldupgrade")) + "\n"+
                    formatAddonStatus("No Damage Bee", ModList.get().isLoaded("nodamagebee")) + "\n"+
                    formatAddonStatus("No Heat Machine", ModList.get().isLoaded("noheatmachine")) + "\n"+
                    formatAddonStatus("No Weed", ModList.get().isLoaded("noweed")) + "\n"+
                    formatAddonStatus("Reactor Plus", ModList.get().isLoaded("reactorplus")) + "\n"+
                    formatAddonStatus("Watering Can", ModList.get().isLoaded("wateringcan")) + "\n"+
                    "\n"+
                    ChatFormatting.YELLOW + Localization.translate("iu.addons.required") + "\n" +
                    formatAddonStatus("JEI", hasJEI) + "\n" +
                    formatAddonStatus("One Probe", oneprobe) + "\n" +
                    formatAddonStatus("Jade", jade) + "\n" +
                    ChatFormatting.DARK_GRAY + "================";


            IUCore.proxy.messagePlayer(player, message);
            sendDiscordLink(player);
        }
    }


    private String formatAddonStatus(String addonName, boolean isInstalled) {
        return ChatFormatting.WHITE + " " + addonName + ": " + (isInstalled
                ? ChatFormatting.GREEN + "[" + "\u2611" + "]"
                : ChatFormatting.RED + "[" + "\u274C" + "]");
    }

    private void sendDiscordLink(Player player) {
        Component discordMessage = Component.literal("[" + Localization.translate("iu.discord.click") + "]")
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.AQUA)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/nFHcxqVx")));

        Component fullMessage = Component.literal(Localization.translate("iu.discord.question") + " ")
                .withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA))
                .append(discordMessage);

        player.sendSystemMessage(fullMessage);
    }

}
