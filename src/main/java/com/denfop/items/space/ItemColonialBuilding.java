package com.denfop.items.space;

import com.denfop.IUCore;
import com.denfop.api.space.colonies.api.IBuildingItem;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.api.building.IBuildingHouse;
import com.denfop.api.space.colonies.api.building.IFactory;
import com.denfop.api.space.colonies.building.*;
import com.denfop.api.space.colonies.enums.*;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.blocks.SubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemColonialBuilding<T extends Enum<T> & SubEnum> extends ItemMain<T> implements IBuildingItem {
    public ItemColonialBuilding(T element) {
        super(new Item.Properties(), element);
    }

    public static EnumTypeUpgrade getType(int meta) {
        return EnumTypeUpgrade.getFromID(meta);

    }

    @Override
    public EnumTypeBuilding getBuilding(final ItemStack stack) {
        switch (this.getElement().getId()) {
            case 0:
            case 1:
            case 2:
            case 3:
                return EnumTypeBuilding.PROTECTION;
            case 4:
            case 5:
            case 6:
                return EnumTypeBuilding.HOUSES;
            case 19:
            case 24:
            case 25:
                return EnumTypeBuilding.OXYGEN;
            case 20:
                return EnumTypeBuilding.STORAGE;
            case 16:
            case 17:
            case 18:
                return EnumTypeBuilding.GENERATORS;
            case 21:
            case 22:
            case 23:
                return EnumTypeBuilding.ENTERTAINMENT;
            default:
                return EnumTypeBuilding.FABRIC;
        }
    }

    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.SpaceTab;
    }

    @Override
    public IColonyBuilding getBuilding(final IColony colony, final ItemStack stack, boolean simulate) {
        EnumTypeBuilding building = getBuilding(stack);
        switch (building) {
            case HOUSES:
                EnumHouses houses = null;
                switch (this.getElement().getId()) {
                    case 4:
                        houses = EnumHouses.LOW;
                        break;
                    case 5:
                        houses = EnumHouses.MEDIUM;
                        break;
                    case 6:
                        houses = EnumHouses.HIGH;
                        break;
                }
                assert houses != null;
                return new ColonyHouse(houses, colony, simulate);
            case PROTECTION:
                EnumProtectionLevel protectionLevel = null;
                switch (this.getElement().getId()) {
                    case 0:
                        protectionLevel = EnumProtectionLevel.LOW;
                        break;
                    case 1:
                        protectionLevel = EnumProtectionLevel.MEDIUM;
                        break;
                    case 2:
                        protectionLevel = EnumProtectionLevel.HIGH;
                        break;
                    case 3:
                        protectionLevel = EnumProtectionLevel.VERY_HIGH;
                        break;
                }
                assert protectionLevel != null;
                return new ProtectionBuilding(protectionLevel, colony, simulate);
            case STORAGE:
                return new StorageBuilding(colony, simulate);
            case OXYGEN:
                EnumTypeOxygenFactory oxygenFactory = null;
                switch (this.getElement().getId()) {
                    case 19:
                        oxygenFactory = EnumTypeOxygenFactory.LOW;
                        break;
                    case 24:
                        oxygenFactory = EnumTypeOxygenFactory.MEDIUM;
                        break;
                    case 25:
                        oxygenFactory = EnumTypeOxygenFactory.HIGH;
                        break;
                }
                return new OxygenFactory(colony, oxygenFactory, simulate);
            case ENTERTAINMENT:
                EnumEntertainment entertainment = null;
                switch (this.getElement().getId()) {
                    case 21:
                        entertainment = EnumEntertainment.LOW;
                        break;
                    case 22:
                        entertainment = EnumEntertainment.MEDIUM;
                        break;
                    case 23:
                        entertainment = EnumEntertainment.HIGH;
                        break;
                }
                assert entertainment != null;
                return new ColonyEntertainment(entertainment, colony, simulate);
            case GENERATORS:
                EnumTypeSolarPanel solarPanel = null;
                switch (this.getElement().getId()) {
                    case 16:
                        solarPanel = EnumTypeSolarPanel.LOW;
                        break;
                    case 17:
                        solarPanel = EnumTypeSolarPanel.MEDIUM;
                        break;
                    case 18:
                        solarPanel = EnumTypeSolarPanel.HIGH;
                        break;
                }
                assert solarPanel != null;
                return new ColonyPanelFactory(colony, solarPanel, simulate);
            case FABRIC:
                switch (this.getElement().getId()) {
                    case 13:
                        return new Factory(colony, EnumTypeFactory.LOW, simulate);
                    case 14:
                        return new Factory(colony, EnumTypeFactory.MEDIUM, simulate);
                    case 15:
                        return new Factory(colony, EnumTypeFactory.HIGH, simulate);
                }
                switch (this.getElement().getId()) {
                    case 7:
                        return new ItemFactory(colony, EnumMiningFactory.LOW, simulate);
                    case 8:
                        return new ItemFactory(colony, EnumMiningFactory.MEDIUM, simulate);
                    case 9:
                        return new ItemFactory(colony, EnumMiningFactory.HIGH, simulate);
                }
                switch (this.getElement().getId()) {
                    case 10:
                        return new FluidFactory(colony, EnumMiningFactory.LOW, simulate);
                    case 11:
                        return new FluidFactory(colony, EnumMiningFactory.MEDIUM, simulate);
                    case 12:
                        return new FluidFactory(colony, EnumMiningFactory.HIGH, simulate);
                }
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(
            ItemStack stack,
            @Nullable Level worldIn,
            List<Component> tooltip,
            TooltipFlag flagIn
    ) {
        IColonyBuilding building = getBuilding(null, stack, true);
        if (building != null) {
            if (building instanceof IBuildingHouse) {
                IBuildingHouse buildingHouse = (IBuildingHouse) building;
                tooltip.add(Component.translatable("iu.colony_building.houses").append(" " + buildingHouse.getMaxPeople()));
                tooltip.add(Component.translatable("iu.colony_building.houses_oxygen").append(" " + (int) (buildingHouse.getMaxPeople() * buildingHouse.getHouses().getConsumeOxygen())));
                tooltip.add(Component.translatable("iu.colony_building.houses_food").append(" " + (int) (buildingHouse.getMaxPeople())));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getHouses().getEnergy())));
            }
            if (building instanceof IFactory) {
                IFactory buildingHouse = (IFactory) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.generate_food").append(" " + (int) (buildingHouse.needWorkers() * 2)));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getEnergy())));
            }
            if (building instanceof ColonyPanelFactory) {
                ColonyPanelFactory buildingHouse = (ColonyPanelFactory) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.generate_energy").append(" " + (int) (buildingHouse.getEnergy())));
            }
            if (building instanceof OxygenFactory) {
                OxygenFactory buildingHouse = (OxygenFactory) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getEnergy())));
                tooltip.add(Component.translatable("iu.colony_building.generate_oxygen").append(" " + (int) (buildingHouse.getGeneration())));
            }
            if (building instanceof ProtectionBuilding) {
                ProtectionBuilding buildingHouse = (ProtectionBuilding) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.protection").append(" " + (int) (buildingHouse.getProtectionBuilding().getProtection())));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getProtectionBuilding().getEnergy())));
            }
            if (building instanceof ItemFactory) {
                ItemFactory buildingHouse = (ItemFactory) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getEnergy())));
            }
            if (building instanceof FluidFactory) {
                FluidFactory buildingHouse = (FluidFactory) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getEnergy())));
            }
            if (building instanceof StorageBuilding) {
                StorageBuilding buildingHouse = (StorageBuilding) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getEnergy())));
            }
            if (building instanceof ColonyEntertainment) {
                ColonyEntertainment buildingHouse = (ColonyEntertainment) building;
                tooltip.add(Component.translatable("iu.colony_building.need_workers").append(" " + buildingHouse.needWorkers()));
                tooltip.add(Component.translatable("iu.colony_building.houses_energy").append(" " + (int) (buildingHouse.getType().getEnergy())));
                tooltip.add(Component.translatable("iu.colony_building.entertainment").append(" " + (int) (buildingHouse.getType().getEntertainment())));
            }
            tooltip.add(Component.translatable("iu.colonial_building.info").append(" " + building.getMinLevelColony()));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public enum Types implements SubEnum {
        low_protection(0),
        medium_protection(1),
        high_protection(2),
        very_high_protection(3),
        low_house(4),
        medium_house(5),
        high_house(6),

        low_mining_factory(7),
        medium_mining_factory(8),
        high_mining_factory(9),
        low_fluid_mining_factory(10),
        medium_fluid_mining_factory(11),
        high_fluid_mining_factory(12),
        low_factory(13),
        medium_factory(14),
        high_factory(15),

        low_panel(16),
        medium_panel(17),
        high_panel(18),
        oxygen(19),
        storage(20),
        low_entertainment(21),
        medium_entertainment(22),
        high_entertainment(23),
        medium_oxygen(24),
        high_oxygen(25);

        private final String name;
        private final int ID;

        Types(int id) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = id;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getMainPath() {
            return "colonial_building";
        }

        public int getId() {
            return this.ID;
        }
    }

}
