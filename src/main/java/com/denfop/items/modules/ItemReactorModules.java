package com.denfop.items.modules;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.Localization;
import com.denfop.api.IModelRegister;
import com.denfop.api.reactors.IReactorModule;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemReactorModules extends ItemSubTypes<ItemReactorModules.CraftingTypes> implements
        IModelRegister, IReactorModule {

    protected static final String NAME = "reactormodules";

    public ItemReactorModules() {
        super(ItemReactorModules.CraftingTypes.class);
        this.setCreativeTab(IUCore.ModuleTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {

        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":reactor_module/" + CraftingTypes
                        .getFromID(meta).getName(), null)
        );
    }


    @Override
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> info,
            @Nonnull final ITooltipFlag flagIn
    ) {
        super.addInformation(itemStack, worldIn, info, flagIn);
        CraftingTypes craftingTypes = CraftingTypes.getFromID(itemStack.getItemDamage());
        double generation = craftingTypes.generation - 1;
        double radiation = craftingTypes.radiation - 1;
        double stableHeat = craftingTypes.stableHeat - 1;
        double capacitor = craftingTypes.capacitor - 1;
        double exchanger = craftingTypes.exchanger - 1;
        double vent = craftingTypes.vent - 1;
        double componentVent = craftingTypes.componentVent - 1;
        if (generation > 0) {
            info.add(Localization.translate("reactor.generation_plus") + ((int) (craftingTypes.generation * 100) - 100) + "%");
        } else if (generation < 0) {
            info.add(Localization.translate("reactor.generation_minus") + (100 - (int) (craftingTypes.generation * 100)) + "%");
        }
        if (radiation > 0) {
            info.add(Localization.translate("reactor.radiation_plus") + ((int) (craftingTypes.radiation * 100) - 100) + "%");
        } else if (radiation < 0) {
            info.add(Localization.translate("reactor.radiation_minus") + (100 - (int) (craftingTypes.radiation * 100)) + "%");
        }
        if (vent > 0) {
            info.add(Localization.translate("reactor.vent_plus") + ((int) (craftingTypes.vent * 100) - 100) + "%");
        } else if (vent < 0) {
            info.add(Localization.translate("reactor.vent_minus") + (100 - (int) (craftingTypes.vent * 100)) + "%");
        }
        if (stableHeat > 0) {
            info.add(Localization.translate("reactor.stableheat_plus") + ((int) (craftingTypes.stableHeat * 100) - 100) + "%");
        } else if (stableHeat < 0) {
            info.add(Localization.translate("reactor.stableheat_minus") + (100 - (int) (craftingTypes.stableHeat * 100)) + "%");
        }
        if (capacitor > 0) {
            info.add(Localization.translate("reactor.capacitor_plus") + ((int) (craftingTypes.capacitor * 100) - 100) + "%");
        } else if (capacitor < 0) {
            info.add(Localization.translate("reactor.capacitor_minus") + (100 - (int) (craftingTypes.capacitor * 100)) + "%");
        }
        if (componentVent > 0) {
            info.add(Localization.translate("reactor.componentvent_plus") + ((int) (craftingTypes.componentVent * 100) - 100) + "%");
        } else if (componentVent < 0) {
            info.add(Localization.translate("reactor.componentvent_minus") + (100 - (int) (craftingTypes.componentVent * 100)) + "%");
        }
        if (exchanger > 0) {
            info.add(Localization.translate("reactor.exchanger_plus") + ((int) (craftingTypes.exchanger * 100) - 100) + "%");
        } else if (exchanger < 0) {
            info.add(Localization.translate("reactor.exchanger_minus") + (100 - (int) (craftingTypes.exchanger * 100)) + "%");
        }
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @Override
    public double getStableHeat(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getStableHeat();
    }

    @Override
    public double getRadiation(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getRadiation();
    }

    @Override
    public double getGeneration(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getGeneration();
    }

    @Override
    public double getComponentVent(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getComponentVent();
    }

    @Override
    public double getVent(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getVent();
    }

    @Override
    public double getExchanger(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getExchanger();
    }

    @Override
    public double getCapacitor(final ItemStack stack) {
        CraftingTypes craftingTypes = CraftingTypes.getFromID(stack.getItemDamage());
        return craftingTypes.getCapacitor();
    }

    public enum CraftingTypes implements ISubEnum {
        generation0(0.98, 1, 1.05, 1, 1, 1, 1),
        generation1(0.95, 1, 1.1, 1, 1, 1, 1),
        generation2(0.92, 1, 1.15, 1, 1, 1, 1),
        generation3(0.90, 1, 1.2, 1, 1, 1, 1),
        radiation0(1.02, 1.25, 1, 1, 1, 1, 1),
        radiation1(1.04, 2.5, 1, 1, 1, 1, 1),
        radiation2(1.06, 3, 1, 1, 1, 1, 1),
        radiation3(1.08, 4, 1, 1, 1, 1, 1),
        stableheat0(1.05, 1.25, 0.98, 1, 1, 1, 1),
        stableheat1(1.1, 1.5, 0.95, 1, 1, 1, 1),
        stableheat2(1.15, 1.75, 0.9, 1, 1, 1, 1),
        stableheat3(1.2, 2, 0.85, 1, 1, 1, 1),
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

        CraftingTypes(
                double stableHeat, double radiation, double generation, double componentVent,
                double vent, double exchanger, double capacitor
        ) {
            this.name = this.name().toLowerCase(Locale.US);
            this.stableHeat = stableHeat;
            this.radiation = radiation;
            this.generation = generation;
            this.vent = vent;
            this.componentVent = componentVent;
            this.exchanger = exchanger;
            this.capacitor = capacitor;
        }

        public static CraftingTypes getFromID(final int ID) {
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

        public int getId() {
            return ordinal();
        }
    }

}
