package com.denfop.events;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

public class EventUpdate {
    private boolean playerNotified = false;
    private int delay = 80;

    public EventUpdate() {
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (this.delay > 0) {
            this.delay--;
            return;
        }

        EntityPlayer player = event.player;
        if (!this.playerNotified) {
            this.playerNotified = true;
            MinecraftForge.EVENT_BUS.unregister(this);
            sendModCheckMessage(player);
        }
    }

    private void sendModCheckMessage(EntityPlayer player) {
        if (IUCore.proxy.isSimulating()) {
            String modVersion = Constants.MOD_VERSION;

            boolean hasPowerUtilities = Loader.isModLoaded("powerutils");
            boolean hasSimplyQuarry = Loader.isModLoaded("simplyquarries");
            boolean hasQuantumGenerators = Loader.isModLoaded("quantum_generators");
            boolean hasJEI = Loader.isModLoaded("jei");
            boolean hasTopAddons = Loader.isModLoaded("topaddons");


            String message = TextFormatting.DARK_GRAY + "================" + "\n" +
                    TextFormatting.GREEN + Localization.translate("iu.mod.name") + " " + modVersion + "\n" +
                    TextFormatting.WHITE + Localization.translate("iu.addons.installed") + "\n" +
                    formatAddonStatus("Power Utilities", hasPowerUtilities) + "\n" +
                    formatAddonStatus("Simply Quarry", hasSimplyQuarry) + "\n" +
                    formatAddonStatus("Quantum Generators", hasQuantumGenerators) + "\n\n" +
                    TextFormatting.YELLOW + Localization.translate("iu.addons.required") + "\n" +
                    formatAddonStatus("JEI", hasJEI) + "\n" +
                    formatAddonStatus("Top Addons", hasTopAddons) + "\n" +
                    TextFormatting.DARK_GRAY + "================";

            IUCore.proxy.messagePlayer(player, message);
            sendDiscordLink(player);
        }
    }

    private String formatAddonStatus(String addonName, boolean isInstalled) {
        return " " + addonName + ": " + (isInstalled ? TextFormatting.GREEN +"["+ "\u2611" + "]" : TextFormatting.RED + "["+"\u274C" + "]");
    }

    private void sendDiscordLink(EntityPlayer player) {
        TextComponentString discordMessage = new TextComponentString(TextFormatting.AQUA + Localization.translate("iu.discord.question") + " " + TextFormatting.UNDERLINE + "[" + Localization.translate("iu.discord.click") + "]");
        discordMessage.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/nFHcxqVx"));
        player.sendMessage(discordMessage);
    }
}
