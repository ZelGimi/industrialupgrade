package com.denfop.api.guidebook;

import com.denfop.Localization;
import net.minecraft.world.item.ItemStack;

public class GuideTab {

    public final String name;
    public final ItemStack icon;
    public final String description;
    public final String unLocalized;

    public GuideTab(String name, ItemStack icon, String description){
        this.name = Localization.translate("iu.guidetab."+name);
        this.icon = icon;
        this.unLocalized = "iu.guidetab."+name;
        this.description = Localization.translate("iu.guide."+description);
        GuideBookCore.instance.addTab(this);
    }

    public String getUnLocalized() {
        return unLocalized;
    }

    public String getName() {
        return name;
    }

}
