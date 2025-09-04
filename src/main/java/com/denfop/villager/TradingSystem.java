package com.denfop.villager;

import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.BlockEntityGenerationMicrochip;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradingSystem {
    public static TradingSystem instance;
    Map<Profession, Map<Integer, List<MerchantOffer>>> trading = new HashMap<>();
    Map<VillagerProfession, Profession> villagerProfession = new HashMap<>();
    private boolean init;

    public static TradingSystem init() {
        if (instance == null) {
            instance = new TradingSystem();
            NeoForge.EVENT_BUS.register(instance);
        }
        return instance;
    }

    public void register(Tuple<Profession, Integer> option, MerchantOffer offer) {
        trading.computeIfAbsent(option.getA(), l -> new HashMap<>()).computeIfAbsent(option.getB(), l -> new ArrayList<>()).add(offer);
    }

    @SubscribeEvent
    public void onVillagerEvent(VillagerTradesEvent event) {
        initMap();
        VillagerProfession profession = event.getType();
        if (villagerProfession.containsKey(profession)) {
            Profession profession1 = villagerProfession.get(profession);
            Map<Integer, List<MerchantOffer>> tradingList = trading.computeIfAbsent(profession1, k -> new HashMap<>());
            tradingList.forEach((level, list) -> {
                List<VillagerTrades.ItemListing> tradingList1 = event.getTrades().computeIfAbsent(level, l -> new ArrayList<>());
                for (MerchantOffer merchantOffer : list) {
                    tradingList1.add(new BasicItemListing(merchantOffer.getCostA(), merchantOffer.getCostB(), merchantOffer.assemble(), merchantOffer.getMaxUses(), merchantOffer.getXp(), merchantOffer.getPriceMultiplier()));
                }
            });


        }
    }

    private void initMap() {
        if (!this.init) {
            this.init = true;
            villagerProfession.put(VillagerInit.CHEMIST.get(), Profession.CHEMIST);
            villagerProfession.put(VillagerInit.METALLURG.get(), Profession.METALLURG);
            villagerProfession.put(VillagerInit.BOTANIST.get(), Profession.BOTANIST);
            villagerProfession.put(VillagerInit.MECHANIC.get(), Profession.MECHANIC);
            villagerProfession.put(VillagerInit.NUCLEAR.get(), Profession.NUCLEAR);
            villagerProfession.put(VillagerInit.ENGINEER.get(), Profession.ENGINEER);
            for (Profession profession : villagerProfession.values())
                NeoForge.EVENT_BUS.post(new EventTradeVillager(profession));
        }
    }

    @SubscribeEvent
    public void onEngineerTrading(EventTradeVillager event) {
        if (event.getProfession() == Profession.ENGINEER) {
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.laser)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.solderingIron)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(568))).build();
            TradeBuilder.create().setEmeralds(8).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(568), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(578), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(578), 2)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(494), 1)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(494), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(570), 1)).build();
            TradeBuilder.create().setEmeralds(6).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(570), 2)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(580), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(3).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(580), 2)).build();

            TradeBuilder.create().setEmeralds(5).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 15)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 15)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 12)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 12)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 16)).build();
            TradeBuilder.create().setEmeralds(16).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 16)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(571), 1)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(571), 2)).build();
            TradeBuilder.create().setEmeralds(11).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(562), 1)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(562), 2)).build();
            TradeBuilder.create().setEmeralds(6).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(581), 1)).build();
            TradeBuilder.create().setEmeralds(11).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(581), 2)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(582), 1)).build();
            TradeBuilder.create().setEmeralds(36).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(582), 2)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 0)).build();
            TradeBuilder.create().setEmeralds(27).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 0)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(577), 1)).build();
            TradeBuilder.create().setEmeralds(16).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(577), 2)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(574), 1)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(7).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(574), 2)).build();


            TradeBuilder.create().setEmeralds(15).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(576), 1)).build();
            TradeBuilder.create().setEmeralds(30).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(576), 2)).build();
            TradeBuilder.create().setEmeralds(25).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 1)).build();
            TradeBuilder.create().setEmeralds(45).setLevel(2).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 1)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(489), 1)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(489), 2)).build();
            TradeBuilder.create().setEmeralds(35).setLevel(3).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(489), 1)).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(487), 1)).build();
            TradeBuilder.create().setEmeralds(64).setLevel(3).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(489), 2)).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(487), 2)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(573), 1)).build();
            TradeBuilder.create().setEmeralds(28).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(573), 2)).build();
            TradeBuilder.create().setEmeralds(25).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(559), 1)).build();
            TradeBuilder.create().setEmeralds(45).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(559), 2)).build();
            TradeBuilder.create().setEmeralds(12).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(588), 1)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(588), 2)).build();
            TradeBuilder.create().setEmeralds(28).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(598), 1)).build();
            TradeBuilder.create().setEmeralds(56).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(598), 2)).build();
            TradeBuilder.create().setEmeralds(22).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(595), 1)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(595), 2)).build();
            TradeBuilder.create().setEmeralds(18).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(575), 1)).build();
            TradeBuilder.create().setEmeralds(36).setLevel(3).setProfession(event.getProfession()).setMaxUse(4).setXp(14).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(575), 2)).build();

            TradeBuilder.create().setEmeralds(35).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(539), 1)).build();
            TradeBuilder.create().setEmeralds(45).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(557), 1)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(533), 1)).build();
            TradeBuilder.create().setEmeralds(30).setLevel(4).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(487), 1)).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(488), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(488), 1)).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(491), 1)).build();
            TradeBuilder.create().setEmeralds(42).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(547), 1)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 17)).build();
            TradeBuilder.create().setEmeralds(28).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 17)).build();
            TradeBuilder.create().setEmeralds(36).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(557), 1)).build();
            TradeBuilder.create().setEmeralds(50).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 14)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 1, 13)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.basecircuit, 2, 13)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(566), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(579), 1)).build();
            TradeBuilder.create().setEmeralds(16).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(579), 2)).build();
            TradeBuilder.create().setEmeralds(24).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(579), 4)).build();
            TradeBuilder.create().setEmeralds(35).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(538), 1)).build();
            TradeBuilder.create().setEmeralds(12).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(560), 1)).build();
            TradeBuilder.create().setEmeralds(24).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(560), 2)).build();
            TradeBuilder.create().setEmeralds(36).setLevel(4).setProfession(event.getProfession()).setMaxUse(4).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(560), 4)).build();

            TradeBuilder.create().setEmeralds(35).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(21), 1)).build();
            TradeBuilder.create().setEmeralds(64).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(21), 2)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(535), 1)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(552), 1)).build();
            TradeBuilder.create().setEmeralds(64).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(533), 2)).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.electronicCircuit, 1)).build();
            TradeBuilder.create().setEmeralds(64).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(541), 2)).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(BlockEntityGenerationMicrochip.getLevelCircuit(IUItem.advancedCircuit, 3)).build();
            TradeBuilder.create().setEmeralds(50).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(541), 1)).build();
            TradeBuilder.create().setEmeralds(30).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(583), 1)).build();
            TradeBuilder.create().setEmeralds(60).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(50).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(583), 2)).build();
        }
    }

    @SubscribeEvent
    public void onMetallurgTrading(EventTradeVillager event) {
        if (event.getProfession() == Profession.METALLURG) {
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.GOLD_INGOT, 2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.IRON_INGOT, 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(24), 4)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.COPPER_INGOT, 4)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(29), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(41), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(31), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(12), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(34), 3)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.GOLD_INGOT, 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.IRON_INGOT, 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(24), 8)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.COPPER_INGOT, 8)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(29), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(41), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(31), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(12), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(34), 6)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.GOLD_INGOT, 8)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.IRON_INGOT, 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(24), 16)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(Items.COPPER_INGOT, 16)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(29), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(41), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(31), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(12), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(34), 12)).build();

            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(25), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(26), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(8), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(1), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(7), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(30), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(33), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(38), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(40), 3)).build();

            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(25), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(26), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(8), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(1), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(7), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(30), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(33), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(38), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(40), 6)).build();

            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(25), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(26), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(8), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(1), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(7), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(30), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(33), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(38), 12)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(40), 12)).build();

            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(9), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(4), 2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(18), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(17), 2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(4), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(13), 2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(0), 3)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(36), 3)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(0), 2)).build();

            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(9), 4)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(4), 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(18), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(17), 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(4), 4)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(13), 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(0), 6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(36), 6)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(0), 4)).build();

            TradeBuilder.create().setEmeralds(3).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(23), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(31), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(8), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(1), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(20), 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(2), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.wolframite, 2)).build();

            TradeBuilder.create().setEmeralds(6).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(23), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(31), 4)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(8), 4)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(1), 4)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(20), 8)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(2), 4)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(20).setResult(ItemStackHelper.fromData(IUItem.wolframite, 4)).build();


            TradeBuilder.create().setEmeralds(5).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(504), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(30), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(3), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(7), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(10), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(5), 1)).build();

            TradeBuilder.create().setEmeralds(10).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(504), 2)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(30), 2)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(3), 2)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(7), 2)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(10), 2)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(25).setResult(ItemStackHelper.fromData(IUItem.alloysingot.getStack(5), 2)).build();
        }
    }

    @SubscribeEvent
    public void onMechanicTrading(EventTradeVillager event) {
        if (event.getProfession() == Profession.MECHANIC) {
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(5).setResult(ItemStackHelper.fromData(IUItem.ForgeHammer, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(5).setResult(ItemStackHelper.fromData(IUItem.cutter, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(5).setResult(ItemStackHelper.fromData(IUItem.treetap, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(5).setResult(ItemStackHelper.fromData(IUItem.wrench, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(4).setXp(5).setResult(ItemStackHelper.fromData(IUItem.connect_item, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(271), 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(271), 8)).build();

            TradeBuilder.create().setEmeralds(35).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(42), 1)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.polonium_palladium_composite, 2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(284), 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(294), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iuingot.getStack(19), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.compressed_redstone, 1)).build();

            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(282), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(282), 2)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(282), 4)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(569), 1)).build();
            TradeBuilder.create().setEmeralds(28).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(569), 2)).build();
            TradeBuilder.create().setEmeralds(22).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(591), 1)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(591), 2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.electronic_stabilizers, 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.electronic_stabilizers, 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.electronic_stabilizers, 4)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.upgrade_casing, 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(286), 1)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(286), 2)).build();

            TradeBuilder.create().setEmeralds(35).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(21), 1)).build();
            TradeBuilder.create().setEmeralds(45).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(60), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(476), 1)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(476), 2)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.graphene)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.graphene, 2)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(274), 1)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(274), 2)).build();


            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(137), 1)).build();
            TradeBuilder.create().setEmeralds(35).setLevel(5).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(489), 1)).setProfession(event.getProfession()).setMaxUse(4).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(487), 1)).build();
            TradeBuilder.create().setEmeralds(64).setLevel(5).addSecondStack(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(489), 2)).setProfession(event.getProfession()).setMaxUse(4).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(487), 2)).build();
            TradeBuilder.create().setEmeralds(25).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(479), 1)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.advBattery, 1)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.graphene_wire, 2)).build();
            TradeBuilder.create().setEmeralds(20).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(15).setResult(ItemStackHelper.fromData(IUItem.graphene_wire, 4)).build();

        }
    }

    @SubscribeEvent
    public void onChemistTrading(EventTradeVillager event) {
        if (event.getProfession() == Profession.CHEMIST) {
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.preciousgem.getStack(0), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.preciousgem.getStack(1), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.preciousgem.getStack(2), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.preciousgem.getStack(0), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.preciousgem.getStack(1), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.preciousgem.getStack(2), 2)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(33), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(41), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(31), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(33), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(41), 2)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(31), 2)).build();

            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.blockResource.getItem(10), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.ore2.getItem(6), 1)).build();
            TradeBuilder.create().setEmeralds(3).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.ore2.getItem(7), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.peat_balls, 4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.cultivated_peat_balls, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(482), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(70), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(66), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(69), 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(43), 1)).build();

            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(460), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(456), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(454), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(79), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(42), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(65), 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(66), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(59), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(62), 1)).build();

            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(63), 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.raw_apatite, 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(73), 1)).build();
            TradeBuilder.create().setEmeralds(3).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(37), 1)).build();
            TradeBuilder.create().setEmeralds(3).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(64), 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(71), 1)).build();
            TradeBuilder.create().setEmeralds(3).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.iudust.getStack(61), 1)).build();

            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(483), 1)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(484), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(464), 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.fertilizer, 4)).build();
            TradeBuilder.create().setEmeralds(12).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(10).setResult(ItemStackHelper.fromData(IUItem.cathode, 1)).build();
            TradeBuilder.create().setEmeralds(12).setLevel(5).setProfession(event.getProfession()).setMaxUse(4).setXp(10).setResult(ItemStackHelper.fromData(IUItem.anode, 1)).build();

        }
    }

    @SubscribeEvent
    public void onBotanistTrading(EventTradeVillager event) {
        if (event.getProfession() == Profession.BOTANIST) {
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(IUItem.crops.getStack(0).getCrop(7)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(IUItem.crops.getStack(0).getCrop(6)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(IUItem.crops.getStack(0).getCrop(0)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(IUItem.crops.getStack(0).getCrop(1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(IUItem.crops.getStack(0).getCrop(5)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crop, 8)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.apiary, 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.barrel, 1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(IUItem.jarBees.getStack(0).getBeeStack(2)).build();


            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.jarBees.getStack(0).getBeeStack(3)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(19)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(9)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(18)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(16)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(15)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(12)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(13)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.bee_frame_template, 2)).build();


            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.jarBees.getStack(0).getBeeStack(4)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(14)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(10)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(11)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(22)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(2)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.honeycomb, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(295), 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.adv_bee_frame_template, 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(6), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(9), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(21), 1)).build();

            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(21)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(20)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.jarBees.getStack(0).getBeeStack(1)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.imp_bee_frame_template, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.crafting_elements.getStack(293), 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.beeswax, 1)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.royal_jelly, 1)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.bee_pollen, 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.wax_stick, 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(0), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(3), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(12), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(15), 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(18), 1)).build();

            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(44)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(52)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(45)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(46)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.crops.getStack(0).getCrop(39)).build();
            TradeBuilder.create().setEmeralds(2).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(IUItem.jarBees.getStack(0).getBeeStack(5)).build();

            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(4), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(16), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(19), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.frame.getStack(1), 1)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(10).setResult(ItemStackHelper.fromData(IUItem.polished_stick, 1)).build();

        }
    }

    @SubscribeEvent
    public void onNuclearTrading(EventTradeVillager event) {
        if (event.getProfession() == Profession.NUCLEAR) {
            TradeBuilder.create().setEmeralds(5).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(IUItem.heavyore.getStack(6)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(IUItem.classic_ore.getStack(3)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(1).setProfession(event.getProfession()).setMaxUse(8).setXp(2).setResult(IUItem.toriyore.getStack(0)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(1).setProfession(event.getProfession()).setMaxUse(16).setXp(2).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 4, 16)).build();

            TradeBuilder.create().setEmeralds(10).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 21)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 453)).build();
            TradeBuilder.create().setEmeralds(6).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 444)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 443)).build();
            TradeBuilder.create().setEmeralds(1).setLevel(2).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 478)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(2).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 19)).build();


            TradeBuilder.create().setEmeralds(10).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.itemiu, 1, 1)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 364)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(3).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 366)).build();
            TradeBuilder.create().setEmeralds(4).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 0)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crushed, 1, 24)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(3).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.toriy)).build();

            TradeBuilder.create().setEmeralds(8).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 365)).build();
            TradeBuilder.create().setEmeralds(8).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 363)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 2, 13)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 2, 14)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(4).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 2, 15)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.radiationresources, 1, 4)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(4).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.itemiu, 1, 2)).build();

            TradeBuilder.create().setEmeralds(8).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 2, 365)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 10)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 11)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 12)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(ItemStackHelper.fromData(IUItem.itemiu, 1, 0)).build();
            TradeBuilder.create().setEmeralds(5).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 2)).build();
            TradeBuilder.create().setEmeralds(15).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.nuclear_res, 1, 1)).build();
            TradeBuilder.create().setEmeralds(10).setLevel(5).setProfession(event.getProfession()).setMaxUse(16).setXp(5).setResult(ItemStackHelper.fromData(IUItem.crafting_elements, 1, 445)).build();
            TradeBuilder.create().setEmeralds(40).setLevel(5).setProfession(event.getProfession()).setMaxUse(8).setXp(5).setResult(IUItem.crops.getStack(0).getCrop(50)).build();

        }
    }
}
