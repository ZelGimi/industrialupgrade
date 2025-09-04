package com.denfop.api.reactors;

import com.denfop.world.WorldBaseGen;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LogicReactor {

    public static Random rand = WorldBaseGen.random;
    static LogicComponent NULL = new LogicComponent(new IReactorItem() {
        @Override
        public EnumTypeComponent getType() {
            return null;
        }

        @Override
        public int getLevel() {
            return 0;
        }

        @Override
        public int getAutoRepair(final IAdvReactor reactor) {
            return 0;
        }

        @Override
        public int getRepairOther(final IAdvReactor reactor) {
            return 0;
        }

        @Override
        public int getDamageCFromHeat(final int heat, final IAdvReactor reactor) {
            return 0;
        }


        @Override
        public int getHeat(final IAdvReactor reactor) {
            return 0;
        }

        @Override
        public double getHeatRemovePercent(final IAdvReactor reactor) {
            return 0;
        }

        @Override
        public void damageItem(ItemStack stack, int damage) {

        }

        @Override
        public boolean updatableItem() {
            return false;
        }

        @Override
        public boolean caneExtractHeat() {
            return false;
        }

        @Override
        public double getEnergyProduction(final IAdvReactor reactor) {
            return 0;
        }

        @Override
        public boolean needClear(final ItemStack stack) {
            return false;
        }
    }, -1, -1, null, null);
    protected final IAdvReactor reactor;
    protected final List<Integer> listIndexRod = new ArrayList<>();
    protected final byte x;
    protected final byte y;
    protected List<LogicComponent> rodsList = new ArrayList<>();
    protected int generation;
    protected int rad_generation;
    private List<LogicComponent> listComponent;
    private double maxHeat;

    public LogicReactor(IAdvReactor advReactor) {
        this.reactor = advReactor;
        this.x = (byte) this.reactor.getWidth();
        this.y = (byte) this.reactor.getHeight();
        this.listComponent = new ArrayList<>(Collections.nCopies(x * y, NULL));
        this.calculateComponent();
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(final int generation) {
        this.generation = generation;
    }

    public int getRadGeneration() {
        return rad_generation;
    }

    public void setRadGeneration(final int rad_generation) {
        this.rad_generation = rad_generation;
    }


    public void calculateComponent() {

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                ItemStack stack = this.reactor.getItemAt(i, j);
                if (!stack.isEmpty() && stack.getItem() instanceof IReactorItem) {
                    this.listComponent.set(
                            i + x * j,
                            new LogicComponent((IReactorItem) stack.getItem(), i, j, stack, this.reactor)
                    );
                }
            }
        }
        this.listComponent.forEach(logicComponent -> logicComponent.updateAllInterface(
                this.listComponent,
                this.x,
                this.y,
                this.reactor
        ));


        this.listComponent.removeIf(logicComponent -> logicComponent
                .getItem()
                .getType() != EnumTypeComponent.ROD || logicComponent == NULL);
        rodsList = new ArrayList<>(this.listComponent);
        for (LogicComponent component : rodsList) {
            if (!this.listIndexRod.contains(component.getX())) {
                this.listIndexRod.add(component.getX());
            }
        }
        calculateFirstLogic(this.listComponent);
    }

    public List<Integer> getListIndexRod() {
        return listIndexRod;
    }

    private void calculateFirstLogic(List<LogicComponent> logicComponents) {
        List<LogicComponent> list = new ArrayList<>(logicComponents);
        List<LogicComponent> logicComponents1 = new ArrayList<>(logicComponents);
        List<LogicComponent> list1 = new ArrayList<>();
        maxHeat = 0;
        generation = 0;
        rad_generation = 0;
        while (!logicComponents1.isEmpty()) {
            List<LogicComponent> logicComponents2 = new ArrayList<>();
            for (LogicComponent component : logicComponents1) {
                boolean added = false;
                int col = 0;
                if (component == null || component.getItem() == null) {
                    continue;
                }
                generation += (int) (component
                        .getItem()
                        .getEnergyProduction(this.reactor) * reactor.getMulOutput(component.getX(),
                        component.getY(), component.getStack()
                ));
                rad_generation += (int) component.getItem().getRadiation();
                if (component.getItem().getType() == EnumTypeComponent.ENERGY_COUPLER) {
                    double temp_generation = 0;
                    double temp_rad_generation = 0;
                    int count = 0;
                    for (LogicComponent component1 : component.getLogicComponents()) {
                        if (component1.getItem().getType() == EnumTypeComponent.ROD) {
                            count++;
                            temp_generation += ((component1
                                    .getItem()
                                    .getEnergyProduction(this.reactor) * reactor.getMulOutput(component1.getX(),
                                    component1.getY(), component1.getStack()
                            )) * (0.45 + component.getItem().getEnergyProduction(this.reactor)));
                            temp_rad_generation += component1.getItem().getRadiation() * component
                                    .getItem()
                                    .getEnergyProduction(this.reactor);
                        }
                    }
                    if (count > 1) {
                        this.generation += (int) temp_generation;
                        this.rad_generation += (int) temp_rad_generation;
                    }
                }
                if (component.getItem().updatableItem() && !list1.contains(component)) {
                    list1.add(component);
                }
                for (LogicComponent lg : component.getLogicComponents()) {
                    if (!list.contains(lg)) {
                        col++;
                    }
                }
                if (component.getItem().getType() == EnumTypeComponent.ROD) {
                    boolean empty = true;
                    boolean coupler = false;
                    for (LogicComponent lg : component.getLogicComponents()) {
                        if (lg.getItem().getType() == EnumTypeComponent.ENERGY_COUPLER) {
                            coupler = true;
                        } else if (lg.getItem().getType() != EnumTypeComponent.ROD) {
                            empty = false;
                        }
                    }
                    if (empty && coupler) {
                        this.maxHeat += component.getHeat();
                    }
                }
                for (LogicComponent lg : component.getLogicComponents()) {
                    if (!list.contains(lg) && lg != LogicReactor.NULL) {
                        if (component.getItem().getType() == EnumTypeComponent.ROD && lg
                                .getItem()
                                .getType() != EnumTypeComponent.ROD) {
                            lg.nearRod++;
                        }
                        if (component.canExtractHeat()) {
                            lg.heat += ((component.heat / col) * lg
                                    .getItem()
                                    .getHeatRemovePercent(this.reactor)) * reactor.getMulHeat(
                                    lg.getX(),
                                    lg.getY(),
                                    lg.getStack()
                            );
                            lg.damage = (short) ((lg.heat / lg.getItem().getDamageCFromHeat(
                                    (int) lg.heat,
                                    this.reactor
                            )) * reactor.getMulDamage(lg.getX(), lg.getY(), lg.getStack()) - lg
                                    .getItem()
                                    .getAutoRepair(this.reactor));

                            if (lg.getItem().getType() == EnumTypeComponent.COOLANT_ROD) {
                                lg.damage *= 10;

                            }
                            if (component.getItem().getType() == EnumTypeComponent.ROD) {
                                if (lg.getItem().getType() == EnumTypeComponent.PLATE) {
                                    lg.heat *= 1.5;
                                }
                            }
                            if (lg.getItem().getType() == EnumTypeComponent.CAPACITOR) {
                                lg.damage *= 3;
                            }
                            added = true;
                        }
                        if (!logicComponents2.contains(lg)) {
                            logicComponents2.add(lg);
                        }


                    }
                }
                if (!added) {
                    maxHeat += component.getHeat();
                }
            }
            logicComponents1.clear();
            logicComponents1.addAll(logicComponents2);
            list.addAll(logicComponents2);
        }
        this.listComponent = list1;
        for (LogicComponent logicComponent : list1) {
            if (logicComponent.getItem().getRepairOther(this.reactor) > 0) {
                for (LogicComponent component : logicComponent.getLogicComponents()) {
                    component.damage -= logicComponent.getItem().getRepairOther(this.reactor);
                }
            }
        }
    }

    public void onTick() {
        List<LogicComponent> logicComponentList = new ArrayList<>();
        for (LogicComponent component : this.listComponent) {
            final boolean tick = component.onTick();
            if (tick) {
                logicComponentList.add(component);
            }
        }
        if (!logicComponentList.isEmpty()) {
            logicComponentList.forEach(logicComponent -> this.reactor.setItemAt(logicComponent.getX(), logicComponent.getY()));
            this.reactor.setUpdate();
        }

        if (!this.rodsList.isEmpty()) {
            this.reactor.setOutput(this.generation);
        } else {
            this.reactor.setOutput(0);
        }
        this.reactor.setRad(this.rad_generation * 20);

    }

    public List<LogicComponent> getListComponent() {
        return listComponent;
    }

    public List<LogicComponent> getRodsList() {
        return rodsList;
    }

    public double getMaxHeat() {
        return maxHeat;
    }

}
