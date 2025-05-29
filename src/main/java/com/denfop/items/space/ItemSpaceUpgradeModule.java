package com.denfop.items.space;

import com.denfop.IUCore;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.info.SpaceUpgradeItemInform;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemSpaceUpgradeModule<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemSpaceUpgradeModule(T element) {
        super(new Item.Properties(), element);
    }

    public static EnumTypeUpgrade getType(int meta) {
        return EnumTypeUpgrade.getFromID(meta);

    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level worldIn,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        final SpaceUpgradeItemInform upgrade = new SpaceUpgradeItemInform(getType(this.getElement().getId()), 1);
        tooltip.add(Component.translatable(upgrade.getName()));
        tooltip.add(Component.translatable("iu.upgrade_item.info").append(String.valueOf(upgrade.upgrade.getMax())));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public enum Types implements ISubEnum {
        space_upgrademodule(0),
        space_upgrademodule1(1),
        space_upgrademodule2(2),
        space_upgrademodule3(3),
        space_upgrademodule4(4),
        space_upgrademodule5(5),
        space_upgrademodule6(6);

        private final String name;
        private final int ID;

        Types(int id) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = id;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "spaceupgrademodules";
        }

        public int getId() {
            return this.ID;
        }
    }

}
