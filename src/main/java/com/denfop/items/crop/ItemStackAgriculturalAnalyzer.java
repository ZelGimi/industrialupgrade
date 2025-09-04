package com.denfop.items.crop;

import com.denfop.api.crop.ICrop;
import com.denfop.api.crop.ICropItem;
import com.denfop.api.crop.genetics.Genome;
import com.denfop.containermenu.ContainerMenuAgriculturalAnalyzer;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.items.ItemStackInventory;
import com.denfop.screen.ScreenAgriculturalAnalyzer;
import com.denfop.screen.ScreenIndustrialUpgrade;
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


    public ContainerMenuAgriculturalAnalyzer getGuiContainer(Player player) {
        return new ContainerMenuAgriculturalAnalyzer(player, this);
    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<?>> getGui(Player player, ContainerMenuBase<?> isAdmin) {
        return new ScreenAgriculturalAnalyzer((ContainerMenuAgriculturalAnalyzer) isAdmin, itemStack1);
    }


}
