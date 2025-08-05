package com.denfop.items.crop;

import com.denfop.api.agriculture.ICrop;
import com.denfop.api.agriculture.ICropItem;
import com.denfop.api.agriculture.genetics.Genome;
import com.denfop.container.ContainerAgriculturalAnalyzer;
import com.denfop.container.ContainerBase;
import com.denfop.gui.GuiAgriculturalAnalyzer;
import com.denfop.gui.GuiCore;
import com.denfop.items.ItemStackInventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ItemStackAgriculturalAnalyzer extends ItemStackInventory {


    public final ItemStack itemStack1;
    public Genome genome;
    public ICrop crop;


    public ItemStackAgriculturalAnalyzer(Player player, ItemStack stack, int inventorySize) {
        super(player, stack, inventorySize);
        this.itemStack1 = stack;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {

        return itemstack.getItem() instanceof ICropItem;
    }


    public ContainerAgriculturalAnalyzer getGuiContainer(Player player) {
        return new ContainerAgriculturalAnalyzer(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player player, ContainerBase<?> isAdmin) {
        return new GuiAgriculturalAnalyzer((ContainerAgriculturalAnalyzer) isAdmin, itemStack1);
    }

    @Override
    public ItemStackInventory getParent() {
        return this;
    }


}
