package com.denfop.items.reactors;

import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class RadiationResources<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IRadioactiveItemType {
    public RadiationResources(T element) {
        super(new Item.Properties(), element);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotIndex, boolean isCurrentItem) {
        if (entity instanceof LivingEntity) {
            LivingEntity entityLiving = (LivingEntity) entity;
            if (!IHazmatLike.hasCompleteHazmat(entityLiving)) {
                IUPotion.radiation.applyEffect(entityLiving, this.getRadiationDuration());
            }
        }
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ReactorsTab;
    }
    @Override
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }

    public enum Types implements ISubEnum {
        americium_gem(0),
        neptunium_gem(1),
        curium_gem(2),
        california_gem(3),
        rad_toriy(4),
        mendelevium_gem(5),
        berkelium_gem(6),
        einsteinium_gem(7),
        uran233_gem(8),
        lawrencium(9),
        nobelium(10),
        fermium(11),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "radiationresources";
        }

        public int getId() {
            return this.ID;
        }
    }
}
