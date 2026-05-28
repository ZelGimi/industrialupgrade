package com.denfop.client.intro;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntroDashboardData {

    public static final String DEFAULT_MARKDOWN = "intro.markdown.default";
    private final String modTitle;
    private final String subtitle;
    private final String logoSource;
    private final String markdown;
    private final List<IntroPartnerEntry> partners;
    private final List<IntroTeamMember> teamMembers;
    private final List<IntroAddonEntry> addons;
    private final List<IntroFaqEntry> faqEntries;
    private final List<IntroFormerMember> formerMembers;
    private final List<IntroBillboardSlide> billboardSlides;
    private final List<String> currentChangelogFallbackLines;
    private final List<String> fullChangelogFallbackLines;
    private final List<IntroExternalBanner> thirdPagePartners;
    private final List<IntroAddonEntry> thirdPageRecommendedMods;
    private final List<IntroExternalBanner> thirdPageSupportLinks;

    public IntroDashboardData(
            String modTitle,
            String subtitle,
            String logoSource,
            String markdown,
            List<IntroPartnerEntry> partners,
            List<IntroTeamMember> teamMembers,
            List<IntroAddonEntry> addons,
            List<IntroFaqEntry> faqEntries,
            List<IntroFormerMember> formerMembers,
            List<IntroBillboardSlide> billboardSlides,
            List<String> currentChangelogFallbackLines,
            List<String> fullChangelogFallbackLines,
            List<IntroExternalBanner> partnersPage3,
            List<IntroAddonEntry> recommendedMods,
            List<IntroExternalBanner> supportLinks
    ) {
        this.modTitle = modTitle;
        this.subtitle = subtitle;
        this.logoSource = logoSource;
        this.markdown = markdown;

        this.partners = List.copyOf(partners);
        this.teamMembers = List.copyOf(teamMembers);
        this.addons = List.copyOf(addons);

        this.faqEntries = List.copyOf(faqEntries);
        this.formerMembers = List.copyOf(formerMembers);
        this.billboardSlides = List.copyOf(billboardSlides);

        this.currentChangelogFallbackLines = List.copyOf(currentChangelogFallbackLines);
        this.fullChangelogFallbackLines = List.copyOf(fullChangelogFallbackLines);

        this.thirdPagePartners = List.copyOf(partnersPage3);
        this.thirdPageRecommendedMods = List.copyOf(recommendedMods);
        this.thirdPageSupportLinks = List.copyOf(supportLinks);
    }

    public static IntroDashboardData createDefault() {
        List<IntroPartnerEntry> partners = new ArrayList<>();
        partners.add(new IntroPartnerEntry(
                "Paradise-mc",
                "intro.partner.paradise.subtitle",
                "resource:industrialupgrade:textures/gui/intro/paradise.png"
        ));

        List<IntroTeamMember> team = new ArrayList<>();
        team.add(new IntroTeamMember(
                "intro.team.role.lead_developer",
                "_Denfop_",
                "Denfop",
                "intro.team.contribution.denfop"
        ));
        team.add(new IntroTeamMember(
                "intro.team.role.game_designer",
                "-frei",
                "-frei",
                ""
        ));
        team.add(new IntroTeamMember(
                "intro.team.role.designer_3d_modeler",
                "texnounit",
                "texnounit",
                ""
        ));
        team.add(new IntroTeamMember(
                "intro.team.role.localizer",
                "MakandIv",
                "MakandIv",
                ""
        ));

        List<IntroAddonEntry> addons = new ArrayList<>(Arrays.asList(
                new IntroAddonEntry(
                        "Power Utilities",
                        "intro.addon.power_utilities.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/power_utilities.png",
                        "powerutils"
                ),
                new IntroAddonEntry(
                        "Simply Quarry",
                        "intro.addon.simply_quarry.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/simply_quarry.png",
                        "simplyquarries"
                ),
                new IntroAddonEntry(
                        "Quantum Generators",
                        "intro.addon.quantum_generators.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/quantum_generators.png",
                        "quantumgenerators"
                ),
                new IntroAddonEntry(
                        "Diamond Vein",
                        "intro.addon.diamond_vein.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/diamondvein.png",
                        "diamondvein"
                ),
                new IntroAddonEntry(
                        "Reactor Plus",
                        "intro.addon.reactor_plus.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/reactorplus.png",
                        "reactorplus"
                ),
                new IntroAddonEntry(
                        "Watering Can",
                        "intro.addon.watering_can.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/wateringcan.png",
                        "wateringcan"
                ),
                new IntroAddonEntry(
                        "Fast Steam Age",
                        "intro.addon.fast_steam_age.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/faststeamage.png",
                        "faststeamage"
                ),
                new IntroAddonEntry(
                        "Fast Primitive Age",
                        "intro.addon.fast_primitive_age.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/fastprimitiveage.png",
                        "fastprimitiveage"
                ),
                new IntroAddonEntry(
                        "No Weed",
                        "intro.addon.no_weed.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/noweed.png",
                        "noweed"
                ),
                new IntroAddonEntry(
                        "No Heat Machine",
                        "intro.addon.no_heat_machine.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/noheatmachine.png",
                        "noheatmachine"
                ),
                new IntroAddonEntry(
                        "No Damage Bee",
                        "intro.addon.no_damage_bee.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/nodamagebee.png",
                        "nodamagebee"
                )
        ));

        List<IntroExternalBanner> partnersPage3 = List.of(
                new IntroExternalBanner(
                        "Paradise-mc",
                        "intro.partner.paradise.official",
                        "resource:industrialupgrade:textures/gui/intro/paradise_mc.png",
                        "https://discord.gg/UEWnurU5Kn"
                )
        );

        List<IntroAddonEntry> recommendedMods = List.of(
                new IntroAddonEntry(
                        "JEI",
                        "intro.recommended.jei.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/jei.png",
                        "jei"
                ),
                new IntroAddonEntry(
                        "The One Probe",
                        "intro.recommended.top.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/top.png",
                        "theoneprobe"
                ),
                new IntroAddonEntry(
                        "Jade",
                        "intro.recommended.jade.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/jade.png",
                        "jade"
                ),
                new IntroAddonEntry(
                        "Embeddium",
                        "intro.recommended.embeddium.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/embeddium.png",
                        "embeddium"
                )
        );

        List<IntroExternalBanner> supportLinks = List.of(
                new IntroExternalBanner(
                        "Discord",
                        "intro.support.discord.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/discord.png",
                        "https://discord.gg/uCybshDEaY"
                ),
                new IntroExternalBanner(
                        "CurseForge",
                        "intro.support.curseforge.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/curseforge.png",
                        "https://www.curseforge.com/minecraft/mc-mods/industrial-upgrade"
                ),
                new IntroExternalBanner(
                        "Modrinth",
                        "intro.support.modrinth.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/modrinth.png",
                        "https://modrinth.com/"
                ),
                new IntroExternalBanner(
                        "intro.support.wiki.title",
                        "intro.support.wiki.subtitle",
                        "resource:industrialupgrade:textures/gui/intro/wiki.png",
                        "https://zelgimi.github.io/industrialupgrade/docs/intro/"
                )
        );

        List<String> rawFallbackChangelog = buildFullChangelogSource();
        String localVersion = com.denfop.Constants.MOD_VERSION;

        List<String> currentFallbackChangelog = IntroChangelogService.extractCurrentChangelogLines(
                rawFallbackChangelog,
                localVersion
        );

        List<String> fullFallbackChangelog = IntroChangelogService.extractFullChangelogLines(
                rawFallbackChangelog,
                localVersion
        );

        return new IntroDashboardData(
                "intro.title",
                "intro.subtitle",
                "resource:" + "industrialupgrade:textures/gui/intro/logo.png",
                IntroLocalization.tr("intro.markdown.default"),
                partners,
                team,
                addons,
                buildFaqEntries(),
                buildFormerWorkers(),
                buildBillboardSlides(),
                currentFallbackChangelog,
                fullFallbackChangelog,
                partnersPage3,
                recommendedMods,
                supportLinks
        );
    }

    private static List<IntroFaqEntry> buildFaqEntries() {
        List<IntroFaqEntry> list = new ArrayList<>();
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.independent.question", "intro.faq.general.independent.answer"));
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.frequent_updates.question", "intro.faq.general.frequent_updates.answer"));
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.size.question", "intro.faq.general.size.answer"));
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.unusual_mechanics.question", "intro.faq.general.unusual_mechanics.answer"));
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.addons.question", "intro.faq.general.addons.answer"));
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.auto_addons.question", "intro.faq.general.auto_addons.answer"));
        list.add(new IntroFaqEntry("intro.faq.category.general", "intro.faq.general.ideas.question", "intro.faq.general.ideas.answer"));
        return list;
    }

    private static List<IntroFormerMember> buildFormerWorkers() {
        return List.of(
                new IntroFormerMember("AlexTM", "AlexTM", "intro.team.role.designer", "", ""),
                new IntroFormerMember("Aslan", "Aslan", "intro.team.role.designer", "", ""),
                new IntroFormerMember("FiXrD", "FiXrD", "intro.team.role.designer", "", ""),
                new IntroFormerMember("IIIap", "IIIap", "intro.team.role.designer", "", ""),
                new IntroFormerMember("theBr1z1k", "theBr1z1k", "intro.team.role.designer", "", ""),
                new IntroFormerMember("VanFek", "VanFek", "intro.team.role.localizer", "", "")
        );
    }

    private static List<IntroBillboardSlide> buildBillboardSlides() {
        String base = "resource:" + IntroConstants.MODID + ":textures/gui/intro/";
        return List.of(
                new IntroBillboardSlide("intro.billboard.factories", base + "billboard_01.png"),
                new IntroBillboardSlide("intro.billboard.steam_age", base + "billboard_02.png"),
                new IntroBillboardSlide("intro.billboard.energy_systems", base + "billboard_03.png"),
                new IntroBillboardSlide("intro.billboard.colony_development", base + "billboard_04.png"),
                new IntroBillboardSlide("intro.billboard.late_game", base + "billboard_05.png")
        );
    }

    private static List<String> buildFullChangelogSource() {
        return List.of();
    }

    public String getModTitle() {
        return modTitle;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getLogoSource() {
        return logoSource;
    }

    public String getMarkdown() {
        return markdown;
    }

    public List<IntroPartnerEntry> getPartners() {
        return partners;
    }

    public List<IntroTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public List<IntroAddonEntry> getAddons() {
        return addons;
    }

    public List<IntroFaqEntry> getFaqEntries() {
        return faqEntries;
    }

    public List<IntroFormerMember> getFormerMembers() {
        return formerMembers;
    }

    public List<IntroBillboardSlide> getBillboardSlides() {
        return billboardSlides;
    }

    public List<String> getCurrentChangelogFallbackLines() {
        return currentChangelogFallbackLines;
    }

    public List<String> getFullChangelogFallbackLines() {
        return fullChangelogFallbackLines;
    }

    public List<String> getFullChangelogLines() {
        return fullChangelogFallbackLines;
    }

    public List<IntroExternalBanner> getThirdPagePartners() {
        return thirdPagePartners;
    }

    public List<IntroAddonEntry> getThirdPageRecommendedMods() {
        return thirdPageRecommendedMods;
    }

    public List<IntroExternalBanner> getThirdPageSupportLinks() {
        return thirdPageSupportLinks;
    }
}
