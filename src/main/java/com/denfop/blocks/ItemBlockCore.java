package com.denfop.blocks;

import com.denfop.IUCore;
import com.denfop.datagen.itemtag.IItemTag;
import com.denfop.datagen.itemtag.ItemTagProvider;
import com.denfop.tabs.IItemTab;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class ItemBlockCore<T extends Enum<T> & ISubEnum> extends BlockItem implements IItemTab {
    private final T element;
    private final ResourceLocation registryName;
    private final CreativeModeTab modeTab;
    protected String nameItem;

    public ItemBlockCore(Block p_40565_, T element, Properties property, CreativeModeTab modeTab) {
        super(p_40565_, property);
        this.element = element;
        this.modeTab = modeTab;
        this.registryName = ResourceLocation.tryBuild(IUCore.MODID, element.getMainPath() + "/" + element.getSerializedName());
        if (this instanceof IItemTag)
            ItemTagProvider.list.add((IItemTag) this);
        ;
    }

    public ItemBlockCore(Block p_40565_, T element, Properties property) {
        super(p_40565_, property);
        this.element = element;
        this.modeTab = null;
        this.registryName = ResourceLocation.tryBuild(IUCore.MODID, element.getMainPath() + "/" + element.getSerializedName());
        if (this instanceof IItemTag)
            ItemTagProvider.list.add((IItemTag) this);
        ;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public void fillItemCategory(CreativeModeTab p_40569_, NonNullList<ItemStack> p_40570_) {
        if (this.allowedIn(p_40569_) && element.canAddToTab()) {
            p_40570_.add(new ItemStack(this));
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return modeTab;
    }

    public T getElement() {
        return element;
    }

    public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn) {
        blockToItemMap.remove(this.getBlock());
    }

    public String getDescriptionId() {
        if (this.nameItem == null) {
            StringBuilder pathBuilder = new StringBuilder(Util.makeDescriptionId("iu", BuiltInRegistries.ITEM.getKey(this)));
            String targetString = "industrialupgrade." + getElement().getMainPath() + ".";
            String replacement = "";
            if (replacement != null) {
                int index = pathBuilder.indexOf(targetString);
                while (index != -1) {
                    pathBuilder.replace(index, index + targetString.length(), replacement);
                    index = pathBuilder.indexOf(targetString, index + replacement.length());
                }
            }
            this.nameItem = pathBuilder.toString();
        }

        return this.nameItem + ".name";
    }


    @Override
    protected BlockState getPlacementState(BlockPlaceContext p_40613_) {

        BlockState blockstate = ((BlockCore) this.getBlock()).getStateForPlacement(this.element, p_40613_);
        return blockstate != null && this.canPlace(p_40613_, blockstate) ? blockstate : null;

    }
}
