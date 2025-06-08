package com.denfop.api.guidebook;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockAnvil;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.network.packet.PacketUpdateInformationAboutQuestsPlayer;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuideBookCore {

    public static GuideBookCore instance;
    public static Map<UUID, Map<String,List<String>>> uuidGuideMap = new HashMap<>();
    List<GuideTab> guideTabs = new ArrayList<>();
    public GuideBookCore() {
        if (instance == null) {
            instance = this;
        }
    }

    public List<GuideTab> getGuideTabs() {
        return guideTabs;
    }

    public Map<GuideTab, List<Quest>> getGuideTabListMap() {
        return guideTabListMap;
    }

    public static Map<UUID, Map<String, List<String>>> getUuidGuideMap() {
        return uuidGuideMap;
    }
    public static ItemStack getBlockStack(IMultiTileBlock block) {
        return TileBlockCreator.instance.get(block.getIDBlock()).getItemStack();
    }

    public static void init() {

        GuideTab guideTab = new GuideTab("main",ItemStackHelper.fromData(  IUItem.blockadmin),"main");

        Quest.Builder.create().name("start").icon(ItemStackHelper.fromData(IUItem.book)).tab(guideTab).shape(Shape.EPIC).description(
                "start").position(0,
                0).build();
        Quest.Builder.create().name("energy").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.efReader)).shape(Shape.DEFAULT).description(
                "energy").position(35,
                0).build();
        Quest.Builder.create().name("heat").tab(guideTab).icon(getBlockStack(BlockBaseMachine3.cooling)).shape(Shape.DEFAULT).description("heat").position(35,
                35).build();
        Quest.Builder.create().name("vein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.heavyore)).shape(Shape.DEFAULT).description("vein").position(0,
                35).build();
        Quest.Builder.create().name("energies").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.imp_se_generator)).shape(Shape.DEFAULT).description(
                "energies").position(70,
                0).build();
        Quest.Builder.create().name("radiation").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.crafting_elements,1,40)).shape(Shape.DEFAULT).description(
                "radiation").position(70,
                35).build();
        Quest.Builder.create().name("volcano").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.basalts)).shape(Shape.DEFAULT).description(
                "volcano").position(-35,
                0).build();
        Quest.Builder.create().name("pollution").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.pollutionDevice)).shape(Shape.DEFAULT).description(
                "pollution").position(-35,
                -35).build();
        Quest.Builder.create().name("bee").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.jarBees)).shape(Shape.DEFAULT).description(
                "bee").position(0,
                -35).build();
        Quest.Builder.create().name("crop").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.crops)).shape(Shape.DEFAULT).description(
                "crop").position(35,
                -35).build();
        Quest.Builder.create().name("gasvein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.gasBlock)).shape(Shape.DEFAULT).description(
                "gasvein").position(-35,
                35).build();
        Quest.Builder.create().name("mineralvein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.mineral)).shape(Shape.DEFAULT).description(
                "mineralvein").position(70,
                -35).build();
        Quest.Builder.create().name("rubber_tree").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.rubberSapling.getItem())).shape(Shape.DEFAULT).description(
                "rubber_tree").position(-70,
                0).build();
        Quest.Builder.create().name("other_features").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.ore2,1,6)).shape(Shape.DEFAULT).description(
                "other_features").position(-70,
                -35).build();
        Quest.Builder.create().name("oil_vein").tab(guideTab).icon(ItemStackHelper.fromData(IUItem.oilblock)).shape(Shape.DEFAULT).description(
                "oil_vein").position(-70,
                35).build();

        GuideTab primalTab = new GuideTab("primal",getBlockStack(BlockAnvil.block_anvil),"primal");
        Quest.Builder.create().name("anvil").localizationItem().useItemInform().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.anvil)).position(0, 0).build();
        Quest.Builder.create().name("forge_hammer").localizationItem().noDescription()
                .tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.ForgeHammer)).position(40, 0).prev("anvil").build();
        Quest.Builder.create().name("casings").noDescription().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.casing,1,12),
                ItemStackHelper.fromData(IUItem.casing,1,35)).position(80, 0).prev(
                "forge_hammer").build();
        Quest.Builder.create().name("smelterystart").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.smeltery)).position(120, 0).prev(
                "casings").build();
        Quest.Builder.create().name("smelteryforms").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.crafting_elements,1,497),
                ItemStackHelper.fromData(IUItem.crafting_elements,1,496)).position(150, 0).prev(
                "smelterystart").build();
        Quest.Builder.create().name("electrum").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.iuingot,1,13)).position(170,
                -40).prev("smelteryforms").build();
        Quest.Builder.create().name("squeezer").localizationItem().useItemInform().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.squeezer)).position(200,
                -80).prev("electrum").build();
        Quest.Builder.create().name("dryer").localizationItem().useItemInform().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.dryer)).position(240,
                -80).prev("squeezer").build();
        Quest.Builder.create().name("raw_latex").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.rawLatex)).position(280,
                -80).prev("dryer").build();
        Quest.Builder.create().name("latex").localizationItem().tab(primalTab).itemStack(IUItem.latex).position(320,
                -80).prev("raw_latex").build();
        Quest.Builder.create().name("primal_heater").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.primalFluidHeater)).position(220,
                -40).prev("electrum").build();
        Quest.Builder.create().name("steam").localizationItem().tab(primalTab).fluidStack(new FluidStack(FluidName.fluidsteam.getInstance().get(),50)).position(280,
                -40).prev("primal_heater").build();
        Quest.Builder.create().name("superheated_steam").localizationItem().tab(primalTab).fluidStack(new FluidStack(FluidName.fluidsuperheated_steam.getInstance().get(),50)).position(330,
                -40).prev("steam").build();
        Quest.Builder.create().name("ferromanganese").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.alloysingot,1,
                9)).position(190,
                0).prev("smelteryforms").build();
        Quest.Builder.create().name("molot").noDescription().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.molot)).position(250,
                -20).prev("ferromanganese").build();
        Quest.Builder.create().name("diamond").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(Items.DIAMOND)).position(303,
                -20).prev("molot").build();
        Quest.Builder.create().name("compressor").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.blockCompressor)).position(250,
                20).prev("ferromanganese").build();
        Quest.Builder.create().name("primal_rolling").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.basemachine2,1,124)).position(290,
                20).prev("compressor").build();
        Quest.Builder.create().name("primal_wire_insulator").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.blockwireinsulator)).position(330,
                20).prev("primal_rolling").build();
        Quest.Builder.create().name("macerator").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.blockMacerator)).position(220,
                70).prev("ferromanganese").build();
        Quest.Builder.create().name("flint_dust").localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.iudust,1,60)).position(260,
                70).prev("macerator").build();
        Quest.Builder.create().name("silicon_handler").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.primalSiliconCrystal)).position(300,
                70).prev("flint_dust").build();
        Quest.Builder.create().name("primal_fluid_integrator").useItemInform().localizationItem().tab(primalTab).itemStack(ItemStackHelper.fromData(IUItem.fluidIntegrator)).position(350,
                70).prev("silicon_handler").build();
    }

    Map<GuideTab, List<Quest>> guideTabListMap = new HashMap<>();

    public void addQuestToTab(Quest quest, final GuideTab guideTab) {
        guideTabListMap.computeIfAbsent(guideTab, k -> new ArrayList<>()).add(quest);
    }

    public Quest getPrev(String name, GuideTab guideTab) {
        List<Quest> quests = guideTabListMap.get(guideTab);
        if (quests == null || quests.isEmpty()) {
            return null;
        }
        quests = quests.stream().filter(quest -> quest.unLocalizedName.equals(name)).collect(Collectors.toList());
        return quests.get(0);
    }

    public void load(UUID uniqueID, final Player player) {
        Map<String,List<String>>  map = new HashMap<>();
        for (GuideTab guideTab : guideTabs){
            List<String> quests = new ArrayList<>();
            List<Quest> quests1 = guideTabListMap.get(guideTab);
            if (quests1 != null)
                quests1.forEach(quest -> quests.add(quest.unLocalizedName));
            map.put(guideTab.getUnLocalized(),quests);
        }
        uuidGuideMap.put(uniqueID,map);
        new PacketUpdateInformationAboutQuestsPlayer(map,player);
    }

    public void addTab(GuideTab guideTab) {
        this.guideTabs.add(guideTab);
    }

    public List<Quest> getQuests(int i) {
        GuideTab guideTab = guideTabs.get(i);
        return this.guideTabListMap.get(guideTab);
    }
    public Quest getQuests( List<Quest> quests, String name) {
        final List<Quest> findQuest = quests
                .stream()
                .filter(quest1 -> quest1.unLocalizedName.equals(name))
                .collect(Collectors.toList());
        return findQuest.get(0);
    }

    public void setData(UUID uuid, Map<String, List<String>> map) {
        uuidGuideMap.put(uuid,map);
    }

    public void remove(UUID uniqueID, String tab, String quest) {
        uuidGuideMap.get(uniqueID).get(tab).remove(quest);
    }

}
