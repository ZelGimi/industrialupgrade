package com.denfop.tabs;


import com.denfop.IUItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class TabCore extends CreativeModeTab {

    private final int type;
    private final String name1;

    public TabCore(int type, String name) {
        super(CreativeModeTab.builder().withSearchBar());
        this.name1 = name;
        this.type = type;
    }

    @Override
    public ItemStack getIconItem() {
        return switch (type) {
            case 0 -> new ItemStack(IUItem.blockpanel.getItem(0));
            case 1 -> new ItemStack(IUItem.basemodules.getItemFromMeta(0));
            case 2 -> new ItemStack(IUItem.basecircuit.getItemFromMeta(11));
            case 3 -> new ItemStack(IUItem.toriyore.getItem());
            case 4 -> new ItemStack(IUItem.spectral_helmet.getItem());
            case 5 -> new ItemStack(IUItem.block.getItem());
            case 6 -> new ItemStack(IUItem.reactormendeleviumQuad.getItem());
            case 7 -> new ItemStack(IUItem.machinekit.getStack(3), 1);
            case 9 -> new ItemStack(IUItem.crafting_elements.getItemFromMeta(21));
            case 10 -> new ItemStack(IUItem.water_reactors_component.getItem(8), 1);
            case 11->new ItemStack(IUItem.crops.getStack(0));
            case 12-> new ItemStack(IUItem.jarBees.getStack(0));
            case 13-> new ItemStack(IUItem.genome_crop.getStack(0));
            case 14-> new ItemStack(IUItem.rocket.getItem());
            case 15-> new ItemStack(IUItem.fluidCell.getItem());
            default -> new ItemStack(Blocks.COBBLESTONE);
        };
    }
    Component name = null;
    public Component getDisplayName() {
        if (name == null){
            this.name = Component.translatable("itemGroup." + name1);
        }
        return name;
    }

}
