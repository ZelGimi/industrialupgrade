package com.denfop.items.crop;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import com.denfop.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemCrops<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements ICropItem {
    public ItemCrops(T element) {
        super(new Item.Properties(), element);
    }


    protected String getOrCreateDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade.";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = "iu.crops.seeds";
        }

        return this.nameItem;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level level,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
       tooltip.add(Component.translatable("iu.use_agriculture_analyzer").append(Component.translatable(IUItem.agricultural_analyzer.getItem().getDescriptionId())));

        super.appendHoverText(stack, level, tooltip, flag);

        ICrop crop = getCrop(0, stack);
        if (crop.getId() != 3) {
            ItemStack soil = crop.getSoil().getStack();
            if (!soil.isEmpty()) {
                tooltip.add(Component.translatable("iu.crop.oneprobe.soil")
                        .append(" ")
                        .append(soil.getHoverName())
                        .withStyle(ChatFormatting.YELLOW));
            }

            if (!crop.getDrops().isEmpty()) {
                ItemStack drop = crop.getDrops().get(0);
                if (!drop.isEmpty()) {
                    tooltip.add(Component.translatable("iu.crop.oneprobe.drop")
                            .append(" ")
                            .append(drop.getHoverName())
                            .withStyle(ChatFormatting.AQUA));
                }
            }

            if (!crop.getCropCombine().isEmpty()) {
                tooltip.add(Component.translatable("iu.crop.breeding").withStyle(ChatFormatting.GREEN));
                for (ICrop crop1 : crop.getCropCombine()) {
                    tooltip.add(Component.translatable("crop." + crop1.getName()));
                }
            }
        }
    }
    @Override
    public Component getName(ItemStack stack) {
        CompoundTag tag = ModUtils.nbt(stack);
        ICrop crop = CropNetwork.instance.getCrop(tag.getInt("crop_id"));
        return  Component.translatable(super.getDescriptionId(stack)).append(Component.literal(": "))
                .append(Component.translatable("crop." + crop.getName()));
    }



    public ICrop getCrop(int meta, ItemStack stack) {
        CompoundTag tag = ModUtils.nbt(stack);
        return CropNetwork.instance.getCrop(tag.getInt("crop_id"));
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (allowedIn(tab)) {
            CropNetwork.instance.getCropMap().forEach((id, crop) -> {
                if (id != 3) {
                    ItemStack stack = new ItemStack(this);
                    CompoundTag tag = ModUtils.nbt(stack);
                    tag.putInt("crop_id", id);
                    new Genome(stack);
                    items.add(stack);
                }
            });
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.CropsTab;
    }

    public enum Types implements ISubEnum {
        crop;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        Types() {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = this.ordinal();
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "crops";
        }

        public int getId() {
            return this.ID;
        }
    }
}
