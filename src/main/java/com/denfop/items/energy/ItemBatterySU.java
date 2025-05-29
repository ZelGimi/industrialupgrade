package com.denfop.items.energy;

import com.denfop.ElectricItem;
import com.denfop.IItemTab;
import com.denfop.IUCore;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ModUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBatterySU extends Item implements IItemTab {
    public int capacity;
    public int tier;
    private String nameItem;

    public ItemBatterySU(int capacity1, int tier1) {
        super(new Properties());
        this.capacity = capacity1;
        this.tier = tier1;
    }
    public String getDescriptionId() {
        if (this.nameItem == null) {

            this.nameItem = "iu.single_use_battery";
        }

        return "" + this.nameItem;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = ModUtils.get(player, hand);
        double energy = this.capacity;

        for (int i = 0; i < 9 && energy > 0.0; ++i) {
            ItemStack target = player.getInventory().items.get(i);
            if (!target.isEmpty() && target != stack && target.getItem() instanceof IEnergyItem) {
                energy -= ElectricItem.manager.charge(target, energy, this.tier, true, false);
            }
        }

        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }




    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.RecourseTab;
    }
}
