package com.denfop.items.space;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.space.colonies.api.IBuildingItem;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.colonies.api.IColonyBuilding;
import com.denfop.api.space.colonies.api.building.IBuildingHouse;
import com.denfop.api.space.colonies.api.building.IFactory;
import com.denfop.api.space.colonies.building.ColonyEntertainment;
import com.denfop.api.space.colonies.building.ColonyHouse;
import com.denfop.api.space.colonies.building.ColonyPanelFactory;
import com.denfop.api.space.colonies.building.Factory;
import com.denfop.api.space.colonies.building.FluidFactory;
import com.denfop.api.space.colonies.building.ItemFactory;
import com.denfop.api.space.colonies.building.OxygenFactory;
import com.denfop.api.space.colonies.building.ProtectionBuilding;
import com.denfop.api.space.colonies.building.StorageBuilding;
import com.denfop.api.space.colonies.enums.EnumEntertainment;
import com.denfop.api.space.colonies.enums.EnumHouses;
import com.denfop.api.space.colonies.enums.EnumMiningFactory;
import com.denfop.api.space.colonies.enums.EnumProtectionLevel;
import com.denfop.api.space.colonies.enums.EnumTypeBuilding;
import com.denfop.api.space.colonies.enums.EnumTypeFactory;
import com.denfop.api.space.colonies.enums.EnumTypeOxygenFactory;
import com.denfop.api.space.colonies.enums.EnumTypeSolarPanel;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemColonialBuilding extends ItemSubTypes<ItemColonialBuilding.Types> implements IModelRegister, IBuildingItem {

    protected static final String NAME = "colonial_building";

    public ItemColonialBuilding() {
        super(Types.class);
        this.setCreativeTab(IUCore.SpaceTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(
            final ItemStack stack,
            @Nullable final World worldIn,
            final List<String> tooltip,
            final ITooltipFlag flagIn
    ) {
        IColonyBuilding building = getBuilding(null, stack, true);
        if (building != null) {
            if (building instanceof IBuildingHouse) {
                IBuildingHouse buildingHouse = (IBuildingHouse) building;
                tooltip.add(Localization.translate("iu.colony_building.houses") + " " + buildingHouse.getMaxPeople());
                tooltip.add(Localization.translate("iu.colony_building.houses_oxygen") + " " + (int) (buildingHouse.getMaxPeople() * buildingHouse
                        .getHouses()
                        .getConsumeOxygen()));
                tooltip.add(Localization.translate("iu.colony_building.houses_food") + " " + (int) (buildingHouse.getMaxPeople()));
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse
                        .getHouses()
                        .getEnergy()));

            }
            if (building instanceof IFactory) {
                IFactory buildingHouse = (IFactory) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.generate_food") + " " + (int) (buildingHouse.needWorkers() * 2));
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse.getEnergy()));

            }
            if (building instanceof ColonyPanelFactory) {
                ColonyPanelFactory buildingHouse = (ColonyPanelFactory) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.generate_energy") + " " + (int) (buildingHouse.getEnergy()));

            }
            if (building instanceof OxygenFactory) {
                OxygenFactory buildingHouse = (OxygenFactory) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse.getEnergy()));
                tooltip.add(Localization.translate("iu.colony_building.generate_oxygen") + " " + (int) (buildingHouse.getGeneration()));

            }
            if (building instanceof ProtectionBuilding) {
                ProtectionBuilding buildingHouse = (ProtectionBuilding) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.protection") + " " + (int) (buildingHouse
                        .getProtectionBuilding()
                        .getProtection()));
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse
                        .getProtectionBuilding()
                        .getEnergy()));

            }
            if (building instanceof ItemFactory) {
                ItemFactory buildingHouse = (ItemFactory) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse.getEnergy()));

            }
            if (building instanceof FluidFactory) {
                FluidFactory buildingHouse = (FluidFactory) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse.getEnergy()));

            }
            if (building instanceof StorageBuilding) {
                StorageBuilding buildingHouse = (StorageBuilding) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse.getEnergy()));

            }
            if (building instanceof ColonyEntertainment) {
                ColonyEntertainment buildingHouse = (ColonyEntertainment) building;
                tooltip.add(Localization.translate("iu.colony_building.need_workers") + " " + buildingHouse.needWorkers());
                tooltip.add(Localization.translate("iu.colony_building.houses_energy") + " " + (int) (buildingHouse
                        .getType()
                        .getEnergy()));
                tooltip.add(Localization.translate("iu.colony_building.entertainment") + " " + (int) (buildingHouse
                        .getType()
                        .getEntertainment()));

            }
            tooltip.add(Localization.translate("iu.colonial_building.info") + building.getMinLevelColony());
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    @Override
    public EnumTypeBuilding getBuilding(final ItemStack stack) {
        switch (stack.getItemDamage()) {
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
    public IColonyBuilding getBuilding(final IColony colony, final ItemStack stack, boolean simulate) {
        EnumTypeBuilding building = getBuilding(stack);
        switch (building) {
            case HOUSES:
                EnumHouses houses = null;
                switch (stack.getItemDamage()) {
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
                switch (stack.getItemDamage()) {
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
                switch (stack.getItemDamage()) {
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
                switch (stack.getItemDamage()) {
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
                switch (stack.getItemDamage()) {
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
                switch (stack.getItemDamage()) {
                    case 13:
                        return new Factory(colony, EnumTypeFactory.LOW, simulate);
                    case 14:
                        return new Factory(colony, EnumTypeFactory.MEDIUM, simulate);
                    case 15:
                        return new Factory(colony, EnumTypeFactory.HIGH, simulate);
                }
                switch (stack.getItemDamage()) {
                    case 7:
                        return new ItemFactory(colony, EnumMiningFactory.LOW, simulate);
                    case 8:
                        return new ItemFactory(colony, EnumMiningFactory.MEDIUM, simulate);
                    case 9:
                        return new ItemFactory(colony, EnumMiningFactory.HIGH, simulate);
                }
                switch (stack.getItemDamage()) {
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


    public enum Types implements ISubEnum {
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
        high_oxygen(25),
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

        public int getId() {
            return this.ID;
        }
    }

}
