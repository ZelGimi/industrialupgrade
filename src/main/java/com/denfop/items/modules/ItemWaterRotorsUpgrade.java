package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemWaterRotorsUpgrade<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemWaterRotorsUpgrade(T element) {
        super(new Item.Properties().tab(IUCore.ModuleTab), element);
    }

    public EnumInfoRotorUpgradeModules getType(int meta) {
        return EnumInfoRotorUpgradeModules.getFromID(meta);

    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        final RotorUpgradeItemInform upgrade = new RotorUpgradeItemInform(getType(this.getElement().getId()), 1);
        p_41423_.add(Component.literal(upgrade.getName()));
        p_41423_.add(Component.literal(Localization.translate("iu.upgrade_item.info") + upgrade.upgrade.max));
        switch (this.getElement().getId()) {
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 17:
            case 18:
            case 19:
                p_41423_.add(Component.literal(ChatFormatting.RED + Localization.translate("wind.limit_upgrades.info")));

        }
    }

    public enum Types implements ISubEnum {
        water_rotorupgrade(0),
        water_rotorupgrade1(1),
        water_rotorupgrade2(2),
        water_rotorupgrade3(3),
        water_rotorupgrade4(4),
        water_rotorupgrade5(5),
        water_rotorupgrade6(6),
        water_rotorupgrade7(7),
        water_rotorupgrade8(8),
        water_rotorupgrade9(9),
        water_rotorupgrade10(10),
        water_rotorupgrade11(11),
        water_rotorupgrade12(12),
        water_rotorupgrade13(13),
        water_rotorupgrade14(14),
        water_rotorupgrade15(15),
        water_rotorupgrade16(16),
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
            return "water_rotors_upgrade";
        }

        public int getId() {
            return this.ID;
        }
    }
}
