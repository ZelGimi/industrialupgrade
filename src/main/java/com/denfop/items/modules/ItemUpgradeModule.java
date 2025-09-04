package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.api.item.upgrade.UpgradeItemInform;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import com.denfop.utils.Localization;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemUpgradeModule<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemUpgradeModule(T element) {
        super(new Item.Properties().tab(IUCore.ModuleTab), element);
    }

    public static com.denfop.items.EnumInfoUpgradeModules getType(int meta) {
        return com.denfop.items.EnumInfoUpgradeModules.getFromID(meta);

    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        final UpgradeItemInform upgrade = new UpgradeItemInform(getType(getElement().getId()), 1);
        p_41423_.add(Component.literal(upgrade.getName()));
        p_41423_.add(Component.literal(Localization.translate("iu.upgrade_item.info") + upgrade.upgrade.max));

    }

    public enum Types implements ISubEnum {
        upgrademodule(0),
        upgrademodule1(1),
        upgrademodule2(2),
        upgrademodule3(3),
        upgrademodule4(4),
        upgrademodule5(5),
        upgrademodule6(6),
        upgrademodule7(7),
        upgrademodule8(8),
        upgrademodule9(9),
        upgrademodule10(10),
        upgrademodule11(11),
        upgrademodule12(12),
        upgrademodule13(13),
        upgrademodule14(14),
        upgrademodule15(15),
        upgrademodule16(16),
        upgrademodule17(17),
        upgrademodule18(18),
        upgrademodule19(19),
        upgrademodule20(20),
        upgrademodule21(21),
        upgrademodule22(22),
        upgrademodule23(23),
        upgrademodule24(24),
        upgrademodule25(25),
        upgrademodule26(26),
        upgrademodule27(27),
        upgrademodule28(28),
        upgrademodule29(29),
        upgrademodule30(30),
        upgrademodule31(31),
        upgrademodule32(32),
        upgrademodule33(33),
        upgrademodule34(34),
        upgrademodule35(35),
        upgrademodule36(36),
        upgrademodule37(37),
        upgrademodule38(38),
        upgrademodule39(39),
        upgrademodule40(40),
        upgrademodule41(41),
        upgrademodule42(42),
        upgrademodule43(43),
        upgrademodule44(44),
        upgrademodule45(45),
        upgrademodule46(46),
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
            return "upgrademodules";
        }

        public int getId() {
            return this.ID;
        }
    }
}
