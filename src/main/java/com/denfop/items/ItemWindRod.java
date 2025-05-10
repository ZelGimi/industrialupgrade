package com.denfop.items;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.blocks.ISubEnum;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemWindRod<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemWindRod(T element) {
        super(new Item.Properties().tab(IUCore.ItemTab), element);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
        p_41423_.add(Component.literal(Localization.translate("wind.need_level3")));
    }

    public boolean getLevel(int level, int damage) {
        if (level == 9 && damage == 10) {
            return true;
        } else if (level == 10 && damage == 9) {
            return true;
        } else {
            return level - 1 == damage;
        }
    }

    public enum Types implements ISubEnum {
        oak_rotor_model(0),
        bronze_rotor_model(1),
        iron_rotor_model(2),
        steel_rotor_model(3),
        carbon_rotor_model(4),
        carbon_rotor_model1(5),
        carbon_rotor_model2(6),
        carbon_rotor_model3(7),
        carbon_rotor_model4(8),
        carbon_rotor_model5(9),
        carbon_rotor_model6(10),
        carbon_rotor_model7(11),
        carbon_rotor_model8(12),
        carbon_rotor_model9(13),
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
            return "windrod";
        }

        public int getId() {
            return this.ID;
        }
    }
}
