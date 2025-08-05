package com.denfop.api.space.fakebody;

import com.denfop.api.space.*;
import com.denfop.api.space.rovers.Rovers;
import com.denfop.api.space.rovers.api.IRovers;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.utils.Timer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.denfop.api.space.BaseSpaceSystem.fluidToLevel;

public class FakeSatellite implements IFakeSatellite {

    SpaceOperation spaceOperation;
    private UUID player;
    private ISatellite planet;
    private IRovers rovers;
    private IData data;
    private List<IBaseResource> iBaseResourceList = new ArrayList<>();
    private Timer timerToPlanet;
    private Timer timerFromPlanet;

    public FakeSatellite(UUID player, ISatellite planet, IRovers rovers, IData data, SpaceOperation spaceOperation) {
        this.player = player;
        this.planet = planet;
        this.rovers = rovers;
        this.spaceOperation = spaceOperation;
        this.data = data;
        double distancePlanetToPlanet = (planet
                .getPlanet()
                .getDistance() - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance());
        double distanceSatellite = Math.abs(SpaceInit.moon.getDistanceFromPlanet() - planet.getDistanceFromPlanet()) / SpaceInit.moon.getDistanceFromPlanet();
        if (planet.getPlanet() == SpaceInit.earth) {
            distanceSatellite = 1;
        }
        int seconds = (int) (Math.abs(distanceSatellite * 2.5 * 60 * 0.8 + distancePlanetToPlanet * (12 * 60 * 0.8)));
        if (SpaceUpgradeSystem.system.hasModules(
                EnumTypeUpgrade.ENGINE,
                rovers.getItemStack()
        )) {
            final int engine = SpaceUpgradeSystem.system.getModules(
                    EnumTypeUpgrade.ENGINE,
                    rovers.getItemStack()
            ).number;
            seconds = (int) (seconds * (1 - (engine * 0.125D)));
        }
        FluidStack fluidStack = rovers.getItem().getFluidHandler(rovers.getItemStack()).drain(1000, IFluidHandler.FluidAction.SIMULATE);
        double coef = BaseSpaceSystem.rocketFuelCoef.get(fluidToLevel.get(fluidStack.getFluid()));
        seconds = (int) (seconds / coef);
        this.timerToPlanet = new Timer(seconds);
        this.timerFromPlanet = new Timer(seconds);
        timerFromPlanet.setCanWork(false);
    }

    public FakeSatellite(final CompoundTag nbtTagCompound, HolderLookup.Provider p_323640_) {
        player = nbtTagCompound.getUUID("uuid");
        this.planet = (ISatellite) SpaceNet.instance.getBodyFromName(nbtTagCompound.getString("body"));
        this.data = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(player).get(planet);
        ItemStack stack = ItemStack.parseOptional(p_323640_, nbtTagCompound.getCompound("rover"));
        this.rovers = new Rovers((IRoversItem) stack.getItem(), stack);
        this.timerToPlanet = new Timer(nbtTagCompound.getCompound("timerTo"));
        this.timerFromPlanet = new Timer(nbtTagCompound.getCompound("timerFrom"));
        this.spaceOperation = new SpaceOperation(this.planet, nbtTagCompound.getCompound("operation"));
        final ListTag tagList = nbtTagCompound.getList("baseResource", 10);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag tagCompound = tagList.getCompound(i);
            IBaseResource baseResource = new BaseResource(tagCompound, p_323640_);
            iBaseResourceList.add(baseResource);
        }
        SpaceUpgradeSystem.system.updateListFromNBT(rovers.getItem(), rovers.getItemStack());
    }

    @Override
    public Timer getTimerTo() {
        return timerToPlanet;
    }

    @Override
    public List<IBaseResource> getResource() {
        return iBaseResourceList;
    }

    public void addBaseResource(IBaseResource baseResource) {
        iBaseResourceList.add(baseResource);
    }

    @Override
    public IBody getBody() {
        return getSatellite();
    }

    @Override
    public void resetAuto() {
        this.spaceOperation.setAuto(!this.getSpaceOperation().getAuto());
    }

    @Override
    public Timer getTimerFrom() {
        return timerFromPlanet;
    }

    @Override
    public SpaceOperation getSpaceOperation() {
        return spaceOperation;
    }

    @Override
    public CompoundTag writeNBTTagCompound(final CompoundTag nbtTagCompound, HolderLookup.Provider p_323640_) {
        nbtTagCompound.putUUID("uuid", player);
        nbtTagCompound.putString("body", planet.getName());
        nbtTagCompound.put("rover", this.rovers.getItemStack().save(p_323640_));
        nbtTagCompound.put("timerTo", timerToPlanet.writeNBT(new CompoundTag()));
        nbtTagCompound.put("timerFrom", timerFromPlanet.writeNBT(new CompoundTag()));
        nbtTagCompound.put("operation", spaceOperation.writeTag(new CompoundTag()));
        ListTag tagList = new ListTag();
        for (IBaseResource baseResource : iBaseResourceList) {
            CompoundTag nbt = new CompoundTag();
            baseResource.writeNBTTag(nbt, p_323640_);
            tagList.add(nbt);
        }
        nbtTagCompound.put("baseResource", tagList);
        return nbtTagCompound;
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FakeSatellite that = (FakeSatellite) o;
        return Objects.equals(player, that.player) && Objects.equals(planet, that.planet);
    }


    @Override
    public boolean matched(final IBody body) {
        return this.planet.getName().equals(body.getName());
    }

    @Override
    public ISatellite getSatellite() {
        return this.planet;
    }

    @Override
    public UUID getPlayer() {
        return this.player;
    }

    @Override
    public IRovers getRover() {
        return this.rovers;
    }


    @Override
    public IData getData() {
        return this.data;
    }

}
