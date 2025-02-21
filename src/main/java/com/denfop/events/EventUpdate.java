package com.denfop.events;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EventUpdate {

    private final UpdateCheckThread thread;
    private int delay = 40;
    private boolean playerNotified = false;

    public EventUpdate() {
        this.thread = new UpdateCheckThread();
        this.thread.start();
    }

    @SubscribeEvent
    public void tickStart(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (this.delay > 0) {
            this.delay--;
            return;
        }

        EntityPlayer player = event.player;
        if (!this.playerNotified && this.thread.isComplete()) {
            this.playerNotified = true;
            MinecraftForge.EVENT_BUS.unregister(this);
            handleVersionCheck(player);
        } else if (this.thread.isFailed()) {
            notifyPlayerFailure(player);
        }
    }

    private void handleVersionCheck(EntityPlayer player) {
        if (this.thread.getVersion().equals(Constants.MOD_VERSION)) {
            return;
        }

        if (IUCore.proxy.isSimulating()) {
            IUCore.proxy.messagePlayer(player, getVersionUpdateMessage());
         //   IUCore.proxy.messagePlayer(player, this.thread.getChangelog());
            IUCore.proxy.messagePlayer(player, getDownloadMessage());
        }
    }

    private String getVersionUpdateMessage() {
        return TextFormatting.AQUA + "" + TextFormatting.BOLD + Localization.translate("updatemod4") + " " +
                TextFormatting.RESET + TextFormatting.BOLD + Localization.translate("updatemod") +
                TextFormatting.RESET + TextFormatting.GREEN + "" + TextFormatting.BOLD + this.thread.getVersion();
    }

    private String getDownloadMessage() {
        return TextFormatting.BLUE + "" + TextFormatting.BOLD + "[IU] " + Localization.translate("updatemod2") +
                TextFormatting.RESET + this.thread.getDownload();
    }

    private void notifyPlayerFailure(EntityPlayer player) {
        this.playerNotified = true;
        MinecraftForge.EVENT_BUS.unregister(this);
        if (IUCore.proxy.isSimulating()) {
            IUCore.proxy.messagePlayer(player, TextFormatting.DARK_PURPLE + Localization.translate("updatemod4") +
                    TextFormatting.RED + Localization.translate("updatemod3"));
            if (!StringUtils.isNullOrEmpty(this.thread.getNote().get(0))) {
                IUCore.proxy.messagePlayer(player, TextFormatting.RED + this.thread.getNote().get(0));
            }
        }
    }

    public static class UpdateCheckThread extends Thread {

        private final List<String> notes = new ArrayList<>();
        private String version = null;
        private boolean complete = false;
        private boolean failed = false;
        private String changelogUrl = null;
        private String download = null;

        public void run() {
            ModUtils.info("[Update Checker] Thread Started");
            try {
                URL versionURL = new URL("https://raw.githubusercontent.com/ZelGimi/industrialupgrade/main/version.txt");
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(versionURL.openStream()))) {
                    parseVersionData(bufferedReader);
                }
                complete = this.download != null && this.version != null;
                if (!complete) {
                    notes.add("[Invalid Response]");
                    failed = true;
                }
                ModUtils.info("[Update Checker] Thread Finished");
            } catch (Exception e) {
                handleException(e);
            }
        }

        private void parseVersionData(BufferedReader bufferedReader) throws Exception {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(":")) {
                    String value = line.substring(line.indexOf(":") + 1).trim();
                    if (line.contains("Version")) {
                        this.version = value;
                    } else if (line.contains("CurseForge")) {
                        this.download = value;
                    } else if (line.contains("Changelog")) {
                        parseChangelog(value);
                    }
                }
            }
        }

        private void parseChangelog(String value) throws Exception {
            String url = "https://raw.githubusercontent.com/ZelGimi/industrialupgrade/1.12.2-dev/" + value;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                String line;
                boolean getVersion = false;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains("#")) continue;
                    if (line.contains(":")) {
                        getVersion = this.version.equals(line.substring(line.indexOf(":") + 1).trim());
                    } else if (getVersion) {
                        notes.add("- " + line.substring(line.indexOf(".") + 1).trim());
                    }
                }
            }
        }

        private void handleException(Exception e) {
            ModUtils.info("[Update Checker] Check Failed");
            this.failed = true;
            notes.add(e.getClass().toString());
            e.printStackTrace();
        }

        public String getVersion() {
            return this.version;
        }

        public String getChangelog() {
            return this.changelogUrl;
        }

        public String getDownload() {
            return this.download;
        }

        public List<String> getNote() {
            return this.notes;
        }

        public boolean isComplete() {
            return this.complete;
        }

        public boolean isFailed() {
            return this.failed;
        }
    }
}
