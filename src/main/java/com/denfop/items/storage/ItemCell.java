package com.denfop.items.storage;

import com.denfop.IUCore;
import com.denfop.api.storage.cell.CellInfo;
import com.denfop.api.storage.cell.ICellItem;
import com.denfop.api.storage.cell.ItemStackCell;
import com.denfop.tabs.IItemTab;
import com.denfop.utils.ModUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ItemCell extends Item implements ICellItem, IItemTab {

    public final CellInfo cellInfo;
    protected String nameItem;

    public ItemCell(CellInfo cellInfo) {
        super(new Properties().setNoRepair().stacksTo(1));
        this.cellInfo = cellInfo;

    }

    @Override
    public void appendHoverText(ItemStack p_41421_, TooltipContext p_339594_, List<Component> p_41423_, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_339594_, p_41423_, p_41424_);
        try {
            ItemStackCell stackCell = getCell(p_41421_);
            if (stackCell != null) {
                p_41423_.add(Component.literal(ModUtils.getString(stackCell.getStorage()) + "/" + ModUtils.getString(this.cellInfo.capacity())));

            }
        } catch (Exception e) {
        }
        ;
        if (!Screen.hasShiftDown()) {
            p_41423_.add(Component.translatable("iu.tooltip.shift_contents"));
        }
    }


    @Override
    public void fillItemCategory(CreativeModeTab p_41391_, NonNullList<ItemStack> p_41392_) {
        if (this.allowedIn(p_41391_)) {
            p_41392_.add(new ItemStack(this, 1));
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ItemTab;
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
            this.nameItem = "iu." + pathBuilder.toString().split("\\.")[2];
        }

        return this.nameItem;
    }


    @Override
    public CellInfo getCellInfo() {
        return cellInfo;
    }

    @Override
    public ItemStackCell getCell(ItemStack itemStack) {
        return new ItemStackCell(itemStack, cellInfo);
    }

}
