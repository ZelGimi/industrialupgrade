package com.denfop.utils;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.mechanism.BlockMoreMachine;
import com.denfop.blocks.mechanism.BlockMoreMachine1;
import com.denfop.blocks.mechanism.BlockMoreMachine2;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
import com.denfop.tiles.base.EnumMultiMachine;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListInformationUtils {

    public static final List<String> panelinform = new ArrayList<>();
    public static final List<String> storageinform = new ArrayList<>();
    public static final List<String> fisherinform = new ArrayList<>();
    public static final List<String> analyzeinform = new ArrayList<>();
    public static final List<String> quarryinform = new ArrayList<>();
    public static final List<String> mechanism_info = new ArrayList<>();
    public static final Map<IMultiTileBlock, Component> mechanism_info1 = new HashMap<>();
    public static final List<String> mechanism_info2 = new ArrayList<>();
    public static final List<String> limiter_info = new ArrayList<>();
    public static final List<String> cooling = new ArrayList<>();
    public static final List<String> heating = new ArrayList<>();
    public static final List<String> wind_generator = new ArrayList<>();
    public static final List<String> water_generator = new ArrayList<>();
    public static final List<String> blast_furnace = new ArrayList<>();
    public static final List<String> anti_upgrade_block = new ArrayList<>();
    public static final List<String> quarry = new ArrayList<>();
    public static final List<String> quarryvein = new ArrayList<>();
    public static final List<String> solar = new ArrayList<>();
    public static final List<String> heat_limiter = new ArrayList<>();
    public static final List<String> facade = new ArrayList<>();
    public static final List<String> coke_oven = new ArrayList<>();
    public static final Map<Integer, List<String>> integerListMap = new HashMap<>();
    public static int tick = 0;
    public static int index = 0;
    public static int index1 = 0;
    public static int index2 = 0;

    public static void init() {
        for (int i = 0; i < BlockMoreMachine.values().length; i++) {
            mechanism_info.add(Localization.translate(new ItemStack(IUItem.machines_base.getItem(i), 1).getDescriptionId()));
        }
        for (int i = 0; i < BlockMoreMachine1.values().length; i++) {
            mechanism_info.add(Localization.translate(new ItemStack(IUItem.machines_base1.getItem(i), 1).getDescriptionId()));
        }
        for (int i = 0; i < BlockMoreMachine2.values().length; i++) {
            mechanism_info.add(Localization.translate(new ItemStack(IUItem.machines_base2.getItem(i), 1).getDescriptionId()));
        }
        for (int i = 0; i < BlockMoreMachine3.values().length; i++) {
            mechanism_info.add(Localization.translate(new ItemStack(IUItem.machines_base3.getItem(i)).getDescriptionId()));
        }
        //     mechanism_info1.add(Localization.translate(new ItemStack(IUItem.basemachine2, 1, 11).getUnlocalizedName()));

        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.machines.getItem(4), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.basemachine.getItem(3), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.basemachine.getItem(12), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.machines.getItem(6), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.machines_base3.getItem(12), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.machines_base3.getItem(13), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.machines_base3.getItem(14), 1).getDescriptionId()));
        mechanism_info2.add(Localization.translate(new ItemStack(IUItem.machines_base3.getItem(15), 1).getDescriptionId()));

        quarryinform.add(Localization.translate("iu.quarryinformation1"));
        quarryinform.add(Localization.translate("iu.quarryinformation2"));
        quarryinform.add(Localization.translate("iu.quarryinformation3"));
        quarryinform.add(Localization.translate("iu.quarryinformation4"));
        quarryinform.add(Localization.translate("iu.quarryinformation5"));
        quarryinform.add(Localization.translate("iu.quarryinformation6"));
        quarryinform.add(Localization.translate("iu.quarryinformation7"));
        quarryinform.add(Localization.translate("iu.quarryinformation8"));
        quarryinform.add(Localization.translate("iu.quarryinformation9"));
        coke_oven.add(Localization.translate("iu.coke_oven_recipe.info1"));
        coke_oven.add(Localization.translate("iu.coke_oven_recipe.info2"));
        coke_oven.add(Localization.translate("iu.coke_oven_recipe.info3"));
        coke_oven.add(Localization.translate("iu.coke_oven_recipe.info4"));
        coke_oven.add(Localization.translate("iu.coke_oven_recipe.info5"));
        coke_oven.add(Localization.translate("iu.coke_oven_recipe.info6"));

        fisherinform.add(Localization.translate("iu.fisherinformation1"));
        fisherinform.add(Localization.translate("iu.fisherinformation2"));
        fisherinform.add(Localization.translate("iu.fisherinformation3"));

        panelinform.add(Localization.translate("iu.panelinformation1"));
        panelinform.add(Localization.translate("iu.panelinformation2"));
        panelinform.add(Localization.translate("iu.panelinformation3"));
        panelinform.add(Localization.translate("iu.panelinformation4"));
        panelinform.add(Localization.translate("iu.panelinformation5"));
        panelinform.add(Localization.translate("iu.panelinformation6"));
        panelinform.add(Localization.translate("iu.panelinformation7"));
        panelinform.add(Localization.translate("iu.panelinformation8"));
        panelinform.add(Localization.translate("iu.panelinformation9"));
        storageinform.add(Localization.translate("iu.electricstorageinformation1"));
        storageinform.add(Localization.translate("iu.electricstorageinformation2"));
        storageinform.add(Localization.translate("iu.electricstorageinformation3"));
        storageinform.add(Localization.translate("iu.electricstorageinformation4"));
        storageinform.add(Localization.translate("iu.electricstorageinformation5"));
        storageinform.add(Localization.translate("iu.electricstorageinformation6"));
        storageinform.add(Localization.translate("iu.electricstorageinformation7"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation1"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation2"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation3"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation4"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation5"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation6"));
        analyzeinform.add(Localization.translate("iu.analyzerinformation7"));


        heating.add(Localization.translate("iu.heat_storage.info"));
        heating.add(Localization.translate("iu.heat_storage.info1"));
        heating.add(Localization.translate("iu.heat_storage.info2"));
        cooling.add(Localization.translate("iu.cool_storage.info"));
        cooling.add(Localization.translate("iu.cool_storage.info1"));
        cooling.add(Localization.translate("iu.cool_storage.info2"));

        wind_generator.add(Localization.translate("iu.wind_generator.info1"));
        wind_generator.add(Localization.translate("iu.wind_generator.info2"));
        wind_generator.add(Localization.translate("iu.wind_generator.info3"));
        wind_generator.add(Localization.translate("iu.wind_generator.info4"));
        wind_generator.add(Localization.translate("iu.wind_generator.info5"));
        wind_generator.add(Localization.translate("iu.wind_generator.info6"));
        wind_generator.add(Localization.translate("iu.wind_generator.info7"));

        limiter_info.add(Localization.translate("iu.limiter.info1"));
        limiter_info.add(Localization.translate("iu.limiter.info2"));
        limiter_info.add(Localization.translate("iu.limiter.info3"));
        limiter_info.add(Localization.translate("iu.limiter.info4"));
        limiter_info.add(Localization.translate("iu.limiter.info5"));
        limiter_info.add(Localization.translate("iu.limiter.info6"));
        limiter_info.add(Localization.translate("iu.limiter.info7"));
        limiter_info.add(Localization.translate("iu.limiter.info8"));

        water_generator.add(Localization.translate("iu.water_generator.info1"));
        water_generator.add(Localization.translate("iu.water_generator.info2"));
        water_generator.add(Localization.translate("iu.water_generator.info3"));
        water_generator.add(Localization.translate("iu.water_generator.info4"));
        water_generator.add(Localization.translate("iu.water_generator.info5"));
        water_generator.add(Localization.translate("iu.water_generator.info6"));
        water_generator.add(Localization.translate("iu.water_generator.info7"));

        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info1"));
        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info2"));
        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info3"));
        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info4"));
        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info5"));
        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info6"));
        blast_furnace.add(Localization.translate("iu.blast_furnace_recipe.info7"));

        anti_upgrade_block.add(Localization.translate("iu.anti_modification.info1"));
        anti_upgrade_block.add(Localization.translate("iu.anti_modification.info2"));

        heat_limiter.add(Localization.translate("iu.heat_limiter.main_info"));
        heat_limiter.add(Localization.translate("iu.heat_limiter.info"));
        heat_limiter.add(Localization.translate("iu.heat_limiter.info1"));
        heat_limiter.add(Localization.translate("iu.heat_limiter.info2"));
        heat_limiter.add(Localization.translate("iu.heat_limiter.info3"));
        heat_limiter.add(Localization.translate("iu.heat_limiter.info4"));

        facade.add(Localization.translate("facademechanism_text"));
        facade.add(Localization.translate("facademechanism_text1"));
        facade.add(Localization.translate("facademechanism_text2"));
        facade.add(Localization.translate("facademechanism_text3"));
        facade.add(Localization.translate("facademechanism_text4"));
        facade.add(Localization.translate("facademechanism_text5"));

        for (int i = 1; i < 9; i++) {
            quarry.add(Localization.translate("iu.simplyquarries_info" + i));
        }
        for (int i = 1; i < 4; i++) {
            quarryvein.add(Localization.translate("iu.quarryvein_info" + i));
        }
        for (int i = 0; i < 6; i++) {
            solar.add(new ItemStack(IUItem.basemodules.getItemFromMeta(i), 1).getDisplayName().getString());
        }
        for (int i = 15; i < 18; i++) {
            solar.add(new ItemStack(IUItem.basemodules.getItemFromMeta(i), 1).getDisplayName().getString());
        }

        for (EnumMultiMachine machines : EnumMultiMachine.values()) {
            switch (machines.sizeWorkingSlot) {
                case 1:
                case 2:
                    if (integerListMap.containsKey(0)) {
                        integerListMap.get(0).add(new ItemStack(machines.block.getItem(machines.meta), 1).getDisplayName().getString());
                    } else {
                        List<String> stringList = new ArrayList<>();
                        stringList.add(new ItemStack(machines.block.getItem(machines.meta)).getDisplayName().getString());
                        integerListMap.put(0, stringList);
                    }
                    break;
                case 3:
                    if (integerListMap.containsKey(1)) {
                        integerListMap.get(1).add(new ItemStack(machines.block.getItem(machines.meta)).getDisplayName().getString());
                    } else {
                        List<String> stringList = new ArrayList<>();
                        stringList.add(new ItemStack(machines.block.getItem(machines.meta)).getDisplayName().getString());
                        integerListMap.put(1, stringList);
                    }
                    break;
                case 4:
                    if (integerListMap.containsKey(2)) {
                        integerListMap.get(2).add(new ItemStack(machines.block.getItem(machines.meta)).getDisplayName().getString());
                    } else {
                        List<String> stringList = new ArrayList<>();
                        stringList.add(new ItemStack(machines.block.getItem(machines.meta)).getDisplayName().getString());
                        integerListMap.put(2, stringList);
                    }
                    break;
            }

        }


        integerListMap.get(1).addAll(integerListMap.get(0));
        integerListMap.get(2).addAll(integerListMap.get(1));
    }

}
