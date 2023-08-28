package com.denfop.api.windsystem;

import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.windsystem.upgrade.EnumInfoRotorUpgradeModules;
import com.denfop.api.windsystem.upgrade.IRotorUpgradeItem;
import com.denfop.api.windsystem.upgrade.RotorUpgradeItemInform;
import com.denfop.api.windsystem.upgrade.RotorUpgradeSystem;
import com.denfop.api.windsystem.upgrade.event.EventRotorItemLoad;
import com.denfop.invslot.InvSlot;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Map;

public class InvSlotUpgrade extends InvSlot {

    private final IWindUpgradeBlock tile;

    public InvSlotUpgrade(IWindUpgradeBlock base1) {
        super((IAdvInventory) base1, TypeItemSlot.INPUT, 4);
        setStackSizeLimit(1);
        this.tile = base1;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (this.tile.getRotor() == null) {
            return false;
        }
        if (!(stack.getItem() instanceof com.denfop.items.ItemRotorsUpgrade)) {
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
        if (content.isEmpty()) {
            if (!this.tile.getItemStack().isEmpty()) {
                RotorUpgradeSystem.instance.removeUpdate(this.tile.getItemStack(), this.base.getParent().getWorld(), index);
            }
        } else {
            NBTTagCompound nbt = ModUtils.nbt(this.tile.getItemStack());
            nbt.setString("mode_module" + index, (EnumInfoRotorUpgradeModules.getFromID(content.getItemDamage())).name);
            MinecraftForge.EVENT_BUS.post(new EventRotorItemLoad(this.base
                    .getParent().getWorld(), (IRotorUpgradeItem) this.tile
                    .getItemStack().getItem(), this.tile
                    .getItemStack()));
        }
    }

}
