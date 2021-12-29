package com.denfop.items.modules;


import com.denfop.Config;
import com.denfop.IUItem;
import net.minecraft.item.Item;

public enum EnumModule {
    DAY(0, EnumBaseType.DAY, Config.percent_day, "iu.module1"),
    DAY_I(1, EnumBaseType.DAY, Config.percent_day * 2, "iu.module1"),
    DAY_II(2, EnumBaseType.DAY, Config.percent_day * 3, "iu.module1"),
    NIGHT(3, EnumBaseType.NIGHT, Config.percent_night, "iu.module2"),
    NIGHT_I(4, EnumBaseType.NIGHT, Config.percent_night * 2, "iu.module2"),
    NIGHT_II(5, EnumBaseType.NIGHT, Config.percent_night * 3, "iu.module2"),
    STORAGE(6, EnumBaseType.STORAGE, Config.percent_storage, "iu.module3"),
    STORAGE_I(7, EnumBaseType.STORAGE, Config.percent_storage * 2, "iu.module3"),
    STORAGE_II(8, EnumBaseType.STORAGE, Config.percent_storage * 3, "iu.module3"),
    OUTPUT(9, EnumBaseType.OUTPUT, Config.percent_output, "iu.module4"),
    OUTPUT_I(10, EnumBaseType.OUTPUT, Config.percent_output * 2, "iu.module4"),
    OUTPUT_II(11, EnumBaseType.OUTPUT, Config.percent_output * 3, "iu.module4"),
    PHASE(12, EnumBaseType.PHASE, 30, "iu.module5"),
    PHASE_I(13, EnumBaseType.PHASE, 65, "iu.module5"),
    PHASE_II(14, EnumBaseType.PHASE, 100, "iu.module5"),
    MOON_LINSE(15, EnumBaseType.MOON_LINSE, 130, "iu.module6"),
    MOON_LINSE_I(16, EnumBaseType.MOON_LINSE, 165, "iu.module6"),
    MOON_LINSE_II(17, EnumBaseType.MOON_LINSE, 200, "iu.module6"),
    ;
    public final Item item;
    public final EnumBaseType type;
    public final double percent_description;
    public final String description;
    public final double percent;
    public final int meta;

    EnumModule(int meta, EnumBaseType type, double percent, String description) {
        this.item = IUItem.basemodules;
        this.type = type;
        this.meta = meta;
        this.percent_description = percent;
        this.percent = percent / 100;
        this.description = description;
    }
    public static EnumModule getFromID(final int ID) {
        return values()[ID % values().length];
    }


}
