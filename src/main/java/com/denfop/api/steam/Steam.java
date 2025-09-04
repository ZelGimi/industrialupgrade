package com.denfop.api.steam;


import com.denfop.blockentity.mechanism.steamturbine.IRod;
import com.denfop.world.WorldBaseGen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class Steam {

    private static final Random random = WorldBaseGen.random;
    private final ISteam steam;
    private double coef;

    public Steam(ISteam steam) {
        this.steam = steam;
        updateData();
    }

    public double getCoef() {
        return coef;
    }

    public void onTick() {
        if (this.steam.isWork() && coef > 0) {
            boolean update = false;
            FluidTank steamTank = this.steam.getSteamFluid();
            FluidTank waterTank = this.steam.getWaterFluid();
            if (steamTank.getFluidAmount() > 0 && steamTank.getFluidAmount() - (this.steam.getPressure() * (this.steam.getSteamPhase().ordinal() + 1)) >= 0) {
                int canAdd = waterTank.getCapacity() - waterTank.getFluidAmount();
                canAdd = Math.min(canAdd, (this.steam.getPressure() * (this.steam.getSteamPhase().ordinal() + 1)));
                steamTank.drain((this.steam.getPressure() * (this.steam.getSteamPhase().ordinal() + 1)), IFluidHandler.FluidAction.EXECUTE);
                if (canAdd > 0) {
                    waterTank.fill(new FluidStack(Fluids.WATER, canAdd), IFluidHandler.FluidAction.EXECUTE);
                }
                if (random.nextInt(5) <= 1 && steam.getHeat() < steam.getMaxHeat() * 0.5 && this.steam
                        .getSteamPhase()
                        .ordinal() <= this.steam.getStableSteamPhase().ordinal()) {
                    this.steam.addPhase(random.nextInt(this.steam.getPressure() + 1));
                } else if ((steam.getHeat() >= steam.getMaxHeat() * 0.5 && steam.getHeat() < steam.getMaxHeat() * 0.75) || this.steam
                        .getSteamPhase()
                        .ordinal() > this.steam.getStableSteamPhase().ordinal()) {
                    this.steam.removePhase(random.nextInt(2));
                } else if (steam.getHeat() >= steam.getMaxHeat() * 0.75) {
                    this.steam.removePhase(random.nextInt(4));
                }
                this.steam.addHeat(this.steam.getPressure() * (this.steam.getSteamPhase().ordinal() + 1));
                for (ICoolant coolant : steam.getCoolant()) {
                    if (!coolant.getCoolant().getFluid().isEmpty() && coolant
                            .getCoolant()
                            .getFluid().getAmount() >= coolant.getPressure()) {
                        this.steam.removeHeat(coolant.getPower());
                        coolant.getCoolant().drain(coolant.getPressure(), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
                for (IExchanger exchanger : steam.getExchanger()) {
                    if (!exchanger.getSlot().isEmpty()) {
                        this.steam.removeHeat(exchanger.getPower() * 6);
                    }
                }
            } else {
                this.steam.removePhase(random.nextInt(5));
            }
            for (IRod rod : this.steam.getInfo()) {
                int i = 0;
                for (ItemStack stack : rod.getSlot()) {
                    if (stack.isEmpty()) {
                        continue;
                    }
                    ISteamBlade steamBlade = rod.getRods().get(i);
                    update = update || steamBlade.damageBlade(stack);
                    i++;
                }
            }
            this.steam.setGeneration(this.getGeneration());

            if (update) {
                steam.updateInfo();
                updateData();
            }

        }
    }

    public double getGeneration() {
        EnumSteamPhase type = this.steam.getSteamPhase();
        return this.coef * this.steam.getPhase() * (type.ordinal() + 1) * 0.98;
    }

    public void updateData() {
        this.coef = 0;
        for (IRod entry : this.steam.getInfo()) {
            final AtomicReference<Double> coefBlade = new AtomicReference<>((double) 0);
            entry.getRods().forEach(steamBlade -> coefBlade.updateAndGet(v -> v + steamBlade.getCoef()));
            coef += coefBlade.get();
        }
    }

}
