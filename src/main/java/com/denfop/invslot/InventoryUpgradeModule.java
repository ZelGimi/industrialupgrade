package com.denfop.invslot;


import com.denfop.items.modules.EnumSpawnerModules;
import com.denfop.items.modules.EnumSpawnerType;
import com.denfop.items.modules.ItemSpawnerModules;
import com.denfop.tiles.base.TileAutoSpawner;
import net.minecraft.item.ItemStack;

public class InventoryUpgradeModule extends Inventory {

    private final TileAutoSpawner tile;
    private int stackSizeLimit;

    public InventoryUpgradeModule(TileAutoSpawner base1) {
        super(base1, TypeItemSlot.INPUT, 4);
        this.tile = base1;
        this.stackSizeLimit = 1;
    }

    public void update() {
        int spawn = 1;
        int chance = 0;
        int speed = 0;
        int experience = 0;
        this.tile.costenergy = this.tile.defaultconsume;
        int fireAspect = 0;

        for (int i = 0; i < this.size(); i++) {
            this.tile.lootContext[i] = null;
            if (!this.get(i).isEmpty()) {
                EnumSpawnerModules module = EnumSpawnerModules.getFromID(this.get(i).getItemDamage());
                EnumSpawnerType type = module.type;
                switch (type) {
                    case SPAWN:
                        spawn += module.percent;
                        if (spawn <= 4) {
                            this.tile.costenergy *= module.percent;
                        }
                        break;
                    case LUCKY:
                        chance += module.percent;
                        if (chance <= 3) {
                            this.tile.costenergy += module.percent * this.tile.costenergy * 0.2;
                        }
                        break;
                    case SPEED:
                        speed += module.percent;
                        if (speed <= 80) {
                            this.tile.costenergy += module.percent * this.tile.costenergy / 100;
                        }
                        break;
                    case EXPERIENCE:
                        experience += module.percent;
                        if (experience <= 100) {
                            this.tile.costenergy += (module.percent * this.tile.costenergy) / 100;
                        }
                        break;
                    case FIRE:
                        fireAspect += 1;
                        this.tile.costenergy += module.percent * this.tile.costenergy / 100;
                        break;
                }
            }
        }
        this.tile.chance = Math.min(3, chance);
        this.tile.spawn = Math.min(4, spawn);
        this.tile.speed = Math.min(80, speed);
        this.tile.experience = Math.min(100, experience);
        this.tile.fireAspect = Math.min(1, fireAspect);

    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        int spawn = 1;
        int chance = 0;
        int speed = 0;
        int experience = 0;
        this.tile.costenergy = this.tile.defaultconsume;
        int fireAspect = 0;
        for (int i = 0; i < this.size(); i++) {
            this.tile.lootContext[i] = null;
            if (!this.get(i).isEmpty()) {
                EnumSpawnerModules module = EnumSpawnerModules.getFromID(this.get(i).getItemDamage());
                EnumSpawnerType type = module.type;
                switch (type) {
                    case SPAWN:
                        spawn += module.percent;
                        if (spawn <= 4) {
                            this.tile.costenergy *= module.percent;
                        }
                        break;
                    case LUCKY:
                        chance += module.percent;
                        if (chance <= 3) {
                            this.tile.costenergy += module.percent * this.tile.costenergy * 0.2;
                        }
                        break;
                    case SPEED:
                        speed += module.percent;
                        if (speed <= 80) {
                            this.tile.costenergy += module.percent * this.tile.costenergy / 100;
                        }
                        break;
                    case EXPERIENCE:
                        experience += module.percent;
                        if (experience <= 100) {
                            this.tile.costenergy += (module.percent * this.tile.costenergy) / 100;
                        }
                        break;
                    case FIRE:
                        fireAspect += 1;
                        this.tile.costenergy += module.percent * this.tile.costenergy / 100;
                        break;
                }
            }
        }
        this.tile.chance = Math.min(3, chance);
        this.tile.spawn = Math.min(4, spawn);
        this.tile.speed = Math.min(80, speed);
        this.tile.experience = Math.min(100, experience);
        this.tile.fireAspect = Math.min(1, fireAspect);

    }

    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {

        return itemStack.getItem() instanceof ItemSpawnerModules;
    }

    public int getInventoryStackLimit() {
        return this.stackSizeLimit;
    }

    public void setInventoryStackLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
