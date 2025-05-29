package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.windsystem.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemRotorsUpgrade<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemRotorsUpgrade(T element) {
        super(new Item.Properties(), element);
    }

    public EnumInfoRotorUpgradeModules getType(int meta) {
        return EnumInfoRotorUpgradeModules.getFromID(meta);

    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }
    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        final RotorUpgradeItemInform upgrade = new RotorUpgradeItemInform(getType(this.getElement().getId()), 1);
        p_41423_.add(Component.literal(upgrade.getName()));
        p_41423_.add(Component.literal(Localization.translate("iu.upgrade_item.info") + upgrade.upgrade.max));
        switch (getElement().getId()) {
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
                p_41423_.add(Component.literal(ChatFormatting.RED + Localization.translate("wind.limit_upgrades.info")));

        }

    }


    public enum Types implements ISubEnum {
        rotorupgrade(0),
        rotorupgrade1(1),
        rotorupgrade2(2),
        rotorupgrade3(3),
        rotorupgrade4(4),
        rotorupgrade5(5),
        rotorupgrade6(6),
        rotorupgrade7(7),
        rotorupgrade8(8),
        rotorupgrade9(9),
        rotorupgrade10(10),
        rotorupgrade11(11),
        rotorupgrade12(12),
        rotorupgrade13(13),
        rotorupgrade14(14),
        rotorupgrade15(15),
        rotorupgrade16(16),
        rotorupgrade17(17),
        rotorupgrade18(18),
        rotorupgrade19(19),
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
            return "rotors_upgrade";
        }

        public int getId() {
            return this.ID;
        }
    }
}
