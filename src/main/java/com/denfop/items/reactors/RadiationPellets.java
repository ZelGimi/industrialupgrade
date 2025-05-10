package com.denfop.items.reactors;

import com.denfop.IUCore;
import com.denfop.IUPotion;
import com.denfop.api.item.IHazmatLike;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class RadiationPellets<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IRadioactiveItemType {
    public RadiationPellets(T element) {
        super(new Item.Properties().tab(IUCore.ReactorsTab), element);
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
    public int getRadiationDuration() {
        return 200;
    }

    @Override
    public int getRadiationAmplifier() {
        return 100;
    }

    public enum Types implements ISubEnum {
        americium_pellet(0),
        neptunium_pellet(1),
        curium_pellet(2),
        california_pellet(3),
        rad_toriy_pellet(4),
        mendelevium_gem_pellet(5),
        berkelium_gem_pellet(6),
        einsteinium_gem_pellet(7),
        uran233_gem_pellet(8),
        proton_pellet(9),
        fermium_pellet(10),
        nobelium_pellet(11),
        lawrencium_pellet(12),
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
            return "pellets";
        }

        public int getId() {
            return this.ID;
        }
    }
}
