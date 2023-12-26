package com.denfop.invslot;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.water.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.water.upgrade.IRotorUpgradeItem;
import com.denfop.api.water.upgrade.RotorUpgradeItemInform;
import com.denfop.api.water.upgrade.RotorUpgradeSystem;
import com.denfop.api.water.upgrade.event.EventRotorItemLoad;
import com.denfop.api.windsystem.IWindUpgradeBlock;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Map;

public class InvSlotWaterUpgrade extends InvSlot {

    private final IWindUpgradeBlock tile;

    public InvSlotWaterUpgrade(IWindUpgradeBlock base1) {
        super((IAdvInventory) base1, TypeItemSlot.INPUT, 4);
        setStackSizeLimit(1);
        this.tile = base1;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (this.tile.getRotor() == null) {
            return false;
        }
        if (!(stack.getItem() instanceof com.denfop.items.ItemWaterRotorsUpgrade)) {
            return false;
        }
        List<RotorUpgradeItemInform> list = RotorUpgradeSystem.instance.getInformation(this.tile
                .getItemStack());
        EnumInfoRotorUpgradeModules enumInfoRotorUpgradeModules = EnumInfoRotorUpgradeModules.getFromID(stack.getItemDamage());
        RotorUpgradeItemInform modules = RotorUpgradeSystem.instance.getModules(enumInfoRotorUpgradeModules, list);
        if (modules == null) {
            return true;
        }
        return (modules.number < enumInfoRotorUpgradeModules.getMax());
    }

    public void update() {
        for (int i = 0; i < size(); i++) {
            put(i, ItemStack.EMPTY, false);
        }
    }

    public void update(ItemStack stack) {
        Map<Integer, ItemStack> map = RotorUpgradeSystem.instance.getList(stack);
        for (Map.Entry<Integer, ItemStack> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue(), false);
        }
    }

    public void put(int index, ItemStack content, boolean updates) {
        super.put(index, content);
    }

    public void put(int index, ItemStack content) {
        super.put(index, content);

    }

}
