package com.denfop.api.reactors;


import net.minecraft.world.item.ItemStack;

import java.util.*;

public class LogicCreativeReactor {

    public static Random rand = new Random();
    static LogicCreativeComponent NULL = new LogicCreativeComponent(new IReactorItem() {
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
    }, -1, -1, ItemStack.EMPTY, null);
    protected final IAdvReactor reactor;
    protected final List<Integer> listIndexRod = new ArrayList<>();
    protected final int x;
    protected final int y;
    protected List<LogicCreativeComponent> rodsList = new ArrayList<>();
    protected int generation;
    protected int rad_generation;
    List<ItemStack> infoStack = new LinkedList<>();
    private List<LogicCreativeComponent> listComponent;
    private double maxHeat;

    public LogicCreativeReactor(IAdvReactor advReactor) {
        this.reactor = advReactor;
        this.x = this.reactor.getWidth();
        this.y = this.reactor.getHeight();
        this.listComponent = new ArrayList<>(Collections.nCopies(x * y, NULL));
        this.calculateComponent();
        for (LogicCreativeComponent component : this.listComponent) {
            ItemStack stack = component.getStack();
            boolean find = false;
            for (ItemStack stack1 : infoStack) {
                if (stack1.is(stack.getItem())) {
                    stack1.grow(1);
                    find = true;
                    break;
                }
            }
            if (!find) {
                infoStack.add(stack.copy());
            }
        }
    }

    public List<ItemStack> getInfoStack() {
        return infoStack;
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
                            new LogicCreativeComponent((IReactorItem) stack.getItem(), i, j, stack, this.reactor)
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
        for (LogicCreativeComponent component : rodsList) {
            if (!this.listIndexRod.contains(component.getX())) {
                this.listIndexRod.add(component.getX());
            }
        }
        calculateFirstLogic(this.listComponent);
    }

    public List<Integer> getListIndexRod() {
        return listIndexRod;
    }

    private void calculateFirstLogic(List<LogicCreativeComponent> logicComponents) {
        List<LogicCreativeComponent> list = new ArrayList<>(logicComponents);
        List<LogicCreativeComponent> logicComponents1 = new ArrayList<>(logicComponents);
        List<LogicCreativeComponent> list1 = new ArrayList<>();
        maxHeat = 0;
        generation = 0;
        rad_generation = 0;
        while (!logicComponents1.isEmpty()) {
            List<LogicCreativeComponent> logicComponents2 = new ArrayList<>();
            for (LogicCreativeComponent component : logicComponents1) {
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
                    int temp_generation = 0;
                    int temp_rad_generation = 0;
                    int count = 0;
                    for (LogicCreativeComponent component1 : component.getLogicComponents()) {
                        if (component1.getItem().getType() == EnumTypeComponent.ROD) {
                            count++;
                            temp_generation += (int) ((int) (component1
                                    .getItem()
                                    .getEnergyProduction(this.reactor) * reactor.getMulOutput(component1.getX(),
                                    component1.getY(), component1.getStack()
                            )) * component.getItem().getEnergyProduction(this.reactor));
                            temp_rad_generation += (int) ((int) component1.getItem().getRadiation() * component
                                    .getItem()
                                    .getEnergyProduction(this.reactor));
                        }
                    }
                    if (count > 1) {
                        this.generation += temp_generation;
                        this.rad_generation += temp_rad_generation;
                    }
                }
                if (component.getItem().updatableItem() && !list1.contains(component)) {
                    list1.add(component);
                }
                for (LogicCreativeComponent lg : component.getLogicComponents()) {
                    if (!list.contains(lg)) {
                        col++;
                    }
                }
                if (component.getItem().getType() == EnumTypeComponent.ROD) {
                    boolean empty = true;
                    boolean coupler = false;
                    for (LogicCreativeComponent lg : component.getLogicComponents()) {
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
                for (LogicCreativeComponent lg : component.getLogicComponents()) {
                    if (!list.contains(lg) && lg != LogicCreativeReactor.NULL) {
                        if (component.getItem().getType() == EnumTypeComponent.ROD && lg
                                .getItem()
                                .getType() != EnumTypeComponent.ROD) {
                            lg.nearRod++;
                        }
                        if (component.canExtractHeat()) {
                            final double percent = lg
                                    .getItem()
                                    .getHeatRemovePercent(this.reactor);
                            ;
                            lg.heat += (component.heat / col) * percent;
                            lg.damage = (int) ((lg.heat / lg.getItem().getDamageCFromHeat(
                                    (int) lg.heat,
                                    this.reactor
                            )) - lg
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
        this.listComponent.forEach(LogicCreativeComponent::calculateDamageOther);
    }

    public void onTick() {
        List<LogicCreativeComponent> logicComponentList = new ArrayList<>();
        for (LogicCreativeComponent component : this.listComponent) {
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
        this.reactor.setRad(this.rad_generation);

    }

    public List<LogicCreativeComponent> getRodsList() {
        return rodsList;
    }

    public double getMaxHeat() {
        return maxHeat;
    }

}
