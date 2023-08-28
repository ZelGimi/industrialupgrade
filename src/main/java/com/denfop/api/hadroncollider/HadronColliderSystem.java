package com.denfop.api.hadroncollider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HadronColliderSystem implements IHadronColliderSystem {

    public static IHadronColliderSystem system;
    Random rand = new Random();
    Map<IMainController, List<Protons>> mainControllerProtonsMap;
    Map<EnumLevelCollider, Structures> mainController;

    public HadronColliderSystem() {
        system = this;
        this.mainControllerProtonsMap = new HashMap<>();
        this.mainController = new HashMap<>();
        for (EnumLevelCollider collider : EnumLevelCollider.values()) {
            mainController.put(collider, new Structures(collider));
        }
    }


    @Override
    public void startProcess(final IMainController mainController) {
        if (mainController.getPurifierBlock() != null && mainController.getOverclockingBlock() != null && mainController
                .getEnergy()
                .getEnergy() >= 3000) {
            if (mainController.getPurifierBlock().getFluidTank().getFluid() != null) {
                int amount = 1000 + 100 * mainController.getPurifierBlock().getPercent();
                if (mainController.getPurifierBlock().getFluidTank().getFluidAmount() >= amount) {
                    int percent = rand.nextInt(10001);
                    if (percent > 9999) {
                        mainController.getOverclockingBlock().getProtons().add(new Protons(TypeProtons.VERY_SMALL));
                    }
                    mainController.getEnergy().useEnergy(3000);
                }
            }
        }
    }

    @Override
    public void addProtonsInProcess(final IMainController mainController) {
        if (mainController.getOverclockingBlock() != null) {
            final List<Protons> protonsList1 = mainController.getOverclockingBlock().getProtons();
            if (!this.mainControllerProtonsMap.containsKey(mainController)) {
                this.mainControllerProtonsMap.put(mainController, protonsList1);
            } else {
                final List<Protons> protonlist = this.mainControllerProtonsMap.get(mainController);
                for (Protons protons : protonsList1) {
                    if (!protonlist.contains(protons)) {
                        protonlist.add(protons);
                    }
                }
                this.mainControllerProtonsMap.replace(mainController, protonlist);
            }
        }
    }

    @Override
    public void Overclocking() {
        for (Map.Entry<IMainController, List<Protons>> entry : this.mainControllerProtonsMap.entrySet()) {
            List<Protons> protons = entry.getValue();
            for (Protons protons1 : protons) {
                boolean can = false;
                switch (protons1.getType()) {
                    case VERY_SMALL:
                        if (entry.getKey().getEnergy().getEnergy() >= 2000) {
                            can = true;
                        }
                        break;
                    case SMALL:
                        if (entry.getKey().getEnergy().getEnergy() >= 4000) {
                            can = true;
                        }
                        break;
                    case MEDIUM:
                        if (entry.getKey().getEnergy().getEnergy() >= 6000) {
                            can = true;
                        }
                        break;
                    case HIGH:
                        if (entry.getKey().getEnergy().getEnergy() >= 8000) {
                            can = true;
                        }
                        break;
                }

                if (protons1.getPercent() < 100) {
                    if (can) {
                        switch (protons1.getType()) {
                            case VERY_SMALL:
                                if (protons1.getTick() % 200 == 0) {
                                    protons1.addPercent(1);
                                }
                                protons1.setTick(protons1.getTick() + 1);
                                break;
                            case SMALL:
                                if (protons1.getTick() % 400 == 0) {
                                    protons1.addPercent(1);
                                }
                                protons1.setTick(protons1.getTick() + 1);
                                break;
                            case MEDIUM:
                                if (protons1.getTick() % 800 == 0) {
                                    protons1.addPercent(1);
                                }
                                protons1.setTick(protons1.getTick() + 1);
                                break;
                            case HIGH:
                                if (protons1.getTick() % 1600 == 0) {
                                    protons1.addPercent(1);
                                }
                                protons1.setTick(protons1.getTick() + 1);
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean transfer(final IExtractBlock block, final Protons proton) {
        if (block.canTransfer()) {
            if (block.getController() != null && block.getReceivedBlock() != null && block
                    .getReceivedBlock()
                    .getExtractBlock() == block && block.getReceivedBlock().getController() != null) {
                if (block.getController().getEnumLevel().ordinal() + 1 ==
                        block.getReceivedBlock().getController().getEnumLevel().ordinal()) {
                    block.getReceivedBlock().getController().getProtons().add(new Protons(proton.getType(), true));
                    return true;
                }

            }
        }
        return false;
    }

}
