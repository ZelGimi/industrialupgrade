package com.powerutils;


import net.minecraft.item.Item;

public class PowerItem {

    public static Item module_te;
    public static Item module_ic;
    public static Item module_rf;
    public static Item module_fe;
    public static Item module_qe;

    public static void init() {
        module_te = new PUItemBase("module_te");
        module_ic = new PUItemBase("module_ic");
        module_rf = new PUItemBase("module_rf");
        module_fe = new PUItemBase("module_fe");
        module_qe = new PUItemBase("module_qe");
    }

}
