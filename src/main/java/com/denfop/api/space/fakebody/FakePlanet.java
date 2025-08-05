package com.denfop.api.space.fakebody;

import com.denfop.api.space.*;
import com.denfop.api.space.rovers.Rovers;
import com.denfop.api.space.rovers.api.IRovers;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.blocks.FluidName;
import com.denfop.utils.Timer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.denfop.api.space.BaseSpaceSystem.fluidToLevel;

public class FakePlanet implements IFakePlanet {

    SpaceOperation spaceOperation;
    private UUID player;
    private IPlanet planet;
    private IRovers rovers;
    private IData data;
    private List<IBaseResource> iBaseResourceList = new ArrayList<>();
    private Timer timerToPlanet;
    private Timer timerFromPlanet;

    public FakePlanet(UUID player, IPlanet planet, IRovers rovers, IData data, SpaceOperation spaceOperation) {
        this.player = player;
        this.planet = planet;
        this.rovers = rovers;
        this.spaceOperation = spaceOperation;
        this.data = data;
        int seconds =
                (int) ((Math.abs(planet.getDistance() - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance())) * (12 * 60 * 0.8));
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
        FluidStack fluidStack = rovers.getItem().getFluidHandler(rovers.getItemStack()).drain(1, IFluidHandler.FluidAction.SIMULATE);
        double coef = BaseSpaceSystem.rocketFuelCoef.get(fluidToLevel.get(fluidStack.getFluid()));
        seconds = (int) (seconds / coef);
        this.timerToPlanet = new Timer(seconds);
        this.timerFromPlanet = new Timer(seconds);
        timerFromPlanet.setCanWork(false);
    }

    public FakePlanet(final CompoundTag nbtTagCompound) {
        player = nbtTagCompound.getUUID("uuid");
        this.planet = (IPlanet) SpaceNet.instance.getBodyFromName(nbtTagCompound.getString("body"));
        this.data = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(player).get(planet);
        ItemStack stack = ItemStack.of(nbtTagCompound.getCompound("rover"));
        this.rovers = new Rovers((IRoversItem) stack.getItem(), stack);
        this.timerToPlanet = new Timer(nbtTagCompound.getCompound("timerTo"));
        this.timerFromPlanet = new Timer(nbtTagCompound.getCompound("timerFrom"));
        this.spaceOperation = new SpaceOperation(this.planet, nbtTagCompound.getCompound("operation"));
        final ListTag tagList = nbtTagCompound.getList("baseResource", 10);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundTag tagCompound = tagList.getCompound(i);
            IBaseResource baseResource = new BaseResource(tagCompound);
            iBaseResourceList.add(baseResource);
        }
        SpaceUpgradeSystem.system.updateListFromNBT(rovers.getItem(), rovers.getItemStack());
    }

    public void addBaseResource(IBaseResource baseResource) {
        iBaseResourceList.add(baseResource);
    }

    @Override
    public IBody getBody() {
        return planet;
    }

    @Override
    public void resetAuto() {
        this.spaceOperation.setAuto(!this.getSpaceOperation().getAuto());
    }

    @Override
    public SpaceOperation getSpaceOperation() {
        return spaceOperation;
    }

    @Override
    public List<IBaseResource> getResource() {
        return iBaseResourceList;
    }

    @Override
    public Timer getTimerTo() {
        return timerToPlanet;
    }

    @Override
    public Timer getTimerFrom() {
        return timerFromPlanet;
    }

    @Override
    public CompoundTag writeNBTTagCompound(final CompoundTag nbtTagCompound) {
        nbtTagCompound.putUUID("uuid", player);
        nbtTagCompound.putString("body", planet.getName());
        nbtTagCompound.put("rover", this.rovers.getItemStack().save(new CompoundTag()));
        nbtTagCompound.put("timerTo", timerToPlanet.writeNBT(new CompoundTag()));
        nbtTagCompound.put("timerFrom", timerFromPlanet.writeNBT(new CompoundTag()));
        nbtTagCompound.put("operation", spaceOperation.writeTag(new CompoundTag()));
        ListTag tagList = new ListTag();
        for (IBaseResource baseResource : iBaseResourceList) {
            CompoundTag nbt = new CompoundTag();
            baseResource.writeNBTTag(nbt);
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
        FakePlanet that = (FakePlanet) o;
        return Objects.equals(player, that.player) && Objects.equals(planet, that.planet);
    }


    @Override
    public IPlanet getPlanet() {
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

    @Override
    public boolean matched(final IBody body) {
        return this.planet.getName().equals(body.getName());
    }


}
