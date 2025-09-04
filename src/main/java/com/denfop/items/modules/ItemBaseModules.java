package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.blocks.SubEnum;
import com.denfop.items.ItemMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemBaseModules<T extends Enum<T> & SubEnum> extends ItemMain<T> {
    public ItemBaseModules(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        if (EnumModule.getFromID(this.getElement().getId()) != null) {
            if (EnumModule.getFromID(this.getElement().getId()).type != EnumBaseType.PHASE && EnumModule.getFromID(this.getElement().getId()).type != EnumBaseType.MOON_LINSE) {
                p_41423_.add(Component.literal(Localization.translate(EnumModule.getFromID(this.getElement().getId()).description) + " +" + ModUtils.getString(
                        EnumModule.getFromID(this.getElement().getId()).percent_description) + "% "
                        + Localization.translate("iu.module")));
            }
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }

    public enum Types implements SubEnum {
        genday(0),
        genday1(1),
        genday2(2),
        gennight(3),
        gennight1(4),
        gennight2(5),
        storage(6),
        storage1(7),
        storage2(8),
        output(9),
        output1(10),
        output2(11),
        phase(12),
        phase1(13),
        phase2(14),
        moonlinse(15),
        moonlinse1(16),
        moonlinse2(17),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "modules";
        }

        public int getId() {
            return this.ID;
        }
    }
}
