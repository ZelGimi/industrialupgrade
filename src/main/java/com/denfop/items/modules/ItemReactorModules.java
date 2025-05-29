package com.denfop.items.modules;

import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.reactors.IReactorModule;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.ItemMain;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class ItemReactorModules<T extends Enum<T> & ISubEnum> extends ItemMain<T> implements IReactorModule {
    public ItemReactorModules(T element) {
        super(new Item.Properties(), element);
    }
    @Override
    public CreativeModeTab getItemCategory() {
        return IUCore.ModuleTab;
    }
    @Override
    public double getStableHeat(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getStableHeat();
    }

    @Override
    public double getRadiation(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getRadiation();
    }

    @Override
    public double getGeneration(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getGeneration();
    }

    @Override
    public double getComponentVent(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getComponentVent();
    }

    @Override
    public double getVent(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getVent();
    }

    @Override
    public double getExchanger(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getExchanger();
    }

    @Override
    public double getCapacitor(final ItemStack stack) {
        Types craftingTypes = (Types) this.getElement();
        return craftingTypes.getCapacitor();
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> info, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, info, p_41424_);
        Types craftingTypes = (Types) this.getElement();
        double generation = craftingTypes.generation - 1;
        double radiation = craftingTypes.radiation - 1;
        double stableHeat = craftingTypes.stableHeat - 1;
        double capacitor = craftingTypes.capacitor - 1;
        double exchanger = craftingTypes.exchanger - 1;
        double vent = craftingTypes.vent - 1;
        double componentVent = craftingTypes.componentVent - 1;
        if (generation > 0) {
            info.add(Component.literal(Localization.translate("reactor.generation_plus") + ((int) (craftingTypes.generation * 100) - 100) + "%"));
        } else if (generation < 0) {
            info.add(Component.literal(Localization.translate("reactor.generation_minus") + (100 - (int) (craftingTypes.generation * 100)) + "%"));
        }
        if (radiation > 0) {
            info.add(Component.literal(Localization.translate("reactor.radiation_plus") + ((int) (craftingTypes.radiation * 100) - 100) + "%"));
        } else if (radiation < 0) {
            info.add(Component.literal(Localization.translate("reactor.radiation_minus") + (100 - (int) (craftingTypes.radiation * 100)) + "%"));
        }
        if (vent > 0) {
            info.add(Component.literal(Localization.translate("reactor.vent_plus") + ((int) (craftingTypes.vent * 100) - 100) + "%"));
        } else if (vent < 0) {
            info.add(Component.literal(Localization.translate("reactor.vent_minus") + (100 - (int) (craftingTypes.vent * 100)) + "%"));
        }
        if (stableHeat > 0) {
            info.add(Component.literal(Localization.translate("reactor.stableheat_plus") + ((int) (craftingTypes.stableHeat * 100) - 100) + "%"));
        } else if (stableHeat < 0) {
            info.add(Component.literal(Localization.translate("reactor.stableheat_minus") + (100 - (int) (craftingTypes.stableHeat * 100)) + "%"));
        }
        if (capacitor > 0) {
            info.add(Component.literal(Localization.translate("reactor.capacitor_plus") + ((int) (craftingTypes.capacitor * 100) - 100) + "%"));
        } else if (capacitor < 0) {
            info.add(Component.literal(Localization.translate("reactor.capacitor_minus") + (100 - (int) (craftingTypes.capacitor * 100)) + "%"));
        }
        if (componentVent > 0) {
            info.add(Component.literal(Localization.translate("reactor.componentvent_plus") + ((int) (craftingTypes.componentVent * 100) - 100) + "%"));
        } else if (componentVent < 0) {
            info.add(Component.literal(Localization.translate("reactor.componentvent_minus") + (100 - (int) (craftingTypes.componentVent * 100)) + "%"));
        }
        if (exchanger > 0) {
            info.add(Component.literal(Localization.translate("reactor.exchanger_plus") + ((int) (craftingTypes.exchanger * 100) - 100) + "%"));
        } else if (exchanger < 0) {
            info.add(Component.literal(Localization.translate("reactor.exchanger_minus") + (100 - (int) (craftingTypes.exchanger * 100)) + "%"));
        }
    }

    public enum Types implements ISubEnum {
        generation0(0.98, 1, 1.05, 1, 1, 1, 1),
        generation1(0.95, 1, 1.1, 1, 1, 1, 1),
        generation2(0.92, 1, 1.15, 1, 1, 1, 1),
        generation3(0.90, 1, 1.2, 1, 1, 1, 1),
        radiation0(1.02, 1.25, 1, 1, 1, 1, 1),
        radiation1(1.05, 1.5, 1, 1, 1, 1, 1),
        radiation2(1.08, 2, 1, 1, 1, 1, 1),
        radiation3(1.1, 4, 1, 1, 1, 1, 1),
        stableheat0(1.05, 1.25, 0.98, 1, 1, 1, 1),
        stableheat1(1.1, 2.5, 0.95, 1, 1, 1, 1),
        stableheat2(1.15, 3, 0.9, 1, 1, 1, 1),
        stableheat3(1.2, 4, 0.85, 1, 1, 1, 1),
        vent0(1.0, 1, 1, 1, 1.05, 0.95, 1),
        vent1(1, 1, 1, 1, 1.1, 0.9, 1),
        vent2(1, 1, 1, 0.8, 1.15, 0.9, 1),
        vent3(1, 1, 1, 0.75, 1.2, 0.85, 1),
        componentvent0(1.0, 1, 1, 1.25, 0.95, 0.95, 1),
        componentvent1(1, 1, 1, 1.5, 0.925, 0.925, 1),
        componentvent2(1, 1, 1, 1.75, 0.9, 0.9, 1),
        componentvent3(1, 1, 1, 2, 0.88, 0.88, 1),

        exchanger0(0.98, 1, 1, 1.1, 0.9, 1.05, 1),
        exchanger1(0.95, 1, 1, 1.25, 0.875, 1.1, 1),
        exchanger2(0.925, 1, 1, 1.25, 0.85, 1.2, 1),
        exchanger3(0.9, 1, 1, 1.25, 0.825, 1.3, 1),

        capacitor0(1, 1, 0.975, 1.1, 1, 1.0, 1.25),
        capacitor1(1, 1, 0.95, 1.2, 1, 1, 1.5),
        capacitor2(1, 1, 0.925, 1.4, 1, 1, 2),
        capacitor3(1, 1, 0.9, 1.8, 1, 1, 2.5),
        ;
        private final String name;
        private double stableHeat;
        private double radiation;
        private double generation;
        private double componentVent;
        private double vent;
        private double exchanger;
        private double capacitor;

        Types(
                double stableHeat, double radiation, double generation, double componentVent,
                double vent, double exchanger, double capacitor) {
            this.name = this.name().toLowerCase(Locale.US);
            this.stableHeat = stableHeat;
            this.radiation = radiation;
            this.generation = generation;
            this.vent = vent;
            this.componentVent = componentVent;
            this.exchanger = exchanger;
            this.capacitor = capacitor;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public double getCapacitor() {
            return capacitor;
        }

        public double getComponentVent() {
            return componentVent;
        }

        public double getExchanger() {
            return exchanger;
        }

        public double getGeneration() {
            return generation;
        }

        public double getRadiation() {
            return radiation;
        }

        public double getStableHeat() {
            return stableHeat;
        }

        public double getVent() {
            return vent;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String getMainPath() {
            return "reactormodules";
        }

        public int getId() {
            return ordinal();
        }
    }

}
