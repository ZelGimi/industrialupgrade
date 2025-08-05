package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.blocks.ISubEnum;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.items.ItemMain;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ItemQuarryModule<T extends Enum<T> & ISubEnum> extends ItemMain<T> {
    public ItemQuarryModule(T element) {
        super(new Properties(), element);
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable TooltipContext p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, p_41422_, p_41423_, p_41424_);
        int meta = getElement().getId();
        switch (meta) {
            case 0:
                p_41423_.add(Component.literal(Localization.translate("iu.quarry")));
                p_41423_.add(Component.literal(Localization.translate("iu.quarry1")));
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                p_41423_.add(Component.literal(Localization.translate("iu.quarry")));
                p_41423_.add(Component.literal(Localization.translate("iu.quarry2")));
                break;
            case 12:
                p_41423_.add(Component.literal(Localization.translate("iu.blacklist")));
                List<String> stringList = itemStack.getOrDefault(DataComponentsInit.LIST_STRING, Collections.emptyList());

                for (String ore : stringList) {
                    ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.create(ResourceLocation.parse(ore))).iterator().next().value());
                    p_41423_.add(stack.getDisplayName());
                }
                break;
            case 13:
                p_41423_.add(Component.literal(Localization.translate("iu.whitelist")));
                stringList = itemStack.getOrDefault(DataComponentsInit.LIST_STRING, Collections.emptyList());

                for (String ore : stringList) {
                    ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.getTagOrEmpty(ItemTags.create(ResourceLocation.parse(ore))).iterator().next().value());
                    p_41423_.add(stack.getDisplayName());
                }

                break;
            case 14:
                p_41423_.add(Component.literal(ChatFormatting.DARK_PURPLE + Localization.translate("iu.macerator")));
                break;
            case 15:
                p_41423_.add(Component.literal(ChatFormatting.DARK_PURPLE + Localization.translate("iu.comb_macerator")));
                break;
        }
        EnumQuarryModules enumQuarryModules = EnumQuarryModules.values()[meta];
        if (enumQuarryModules.cost < 0) {
            p_41423_.add(Component.literal(ChatFormatting.GREEN + Localization.translate("iu.quarry_energy1") + (int) (Math.abs(enumQuarryModules.cost) * 100) +
                    "%"));
        } else if (enumQuarryModules.cost > 0) {
            p_41423_.add(Component.literal(ChatFormatting.RED + Localization.translate("iu.quarry_energy") + (int) (enumQuarryModules.cost * 100) + "%"));
        }
    }

    public enum CraftingTypes implements ISubEnum {
        per(0),
        ef(1),
        ef1(2),
        ef2(3),
        ef3(4),
        ef4(5),
        for1(6),
        for2(7),
        for3(8),
        kar1(9),
        kar2(10),
        kar3(11),
        blackmodule(12),
        whitemodule(13),
        macerator(14),
        comb_macerator(15),
        ;

        private final String name;
        private final int ID;

        CraftingTypes(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static CraftingTypes getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "quarrymodules";
        }

        public int getId() {
            return this.ID;
        }
    }
}
