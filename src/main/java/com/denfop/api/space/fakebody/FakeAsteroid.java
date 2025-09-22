package com.denfop.api.space.fakebody;

import com.denfop.api.space.BaseResource;
import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.rovers.Rovers;
import com.denfop.api.space.rovers.api.IRovers;
import com.denfop.api.space.rovers.api.IRoversItem;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import com.denfop.blocks.FluidName;
import com.denfop.utils.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FakeAsteroid implements IFakeAsteroid {

    SpaceOperation spaceOperation;
    private UUID player;
    private IAsteroid planet;
    private IRovers rovers;
    private IData data;
    private List<IBaseResource> iBaseResourceList = new ArrayList<>();
    private Timer timerToPlanet;
    private Timer timerFromPlanet;


    public FakeAsteroid(UUID player, IAsteroid planet, IRovers rovers, IData data, SpaceOperation spaceOperation) {
        this.player = player;
        this.planet = planet;
        this.rovers = rovers;
        this.spaceOperation = spaceOperation;
        this.data = data;
        int seconds = (int) ((Math.abs(((planet.getMaxDistance() - planet.getMinDistance()) / 2 + planet.getMinDistance()) - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance())) * (12 * 60 * 0.8));
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
        FluidStack fluidStack = rovers.getItem().getFluidHandler(rovers.getItemStack()).drain(1000, false);
        double coef = 1;
        if (fluidStack.getFluid().equals(FluidName.fluiddimethylhydrazine.getInstance())) {
            coef = 3;
        }
        if (fluidStack.getFluid().equals(FluidName.fluiddecane.getInstance())) {
            coef = 4.4;
        }
        if (fluidStack.getFluid().equals(FluidName.fluidxenon.getInstance())) {
            coef = 7.5;
        }
        seconds = (int) (seconds / coef);
        this.timerToPlanet = new Timer(seconds);
        this.timerFromPlanet = new Timer(seconds);
        timerFromPlanet.setCanWork(false);
    }

    public FakeAsteroid(final NBTTagCompound nbtTagCompound) {
        player = nbtTagCompound.getUniqueId("uuid");
        this.planet = (IAsteroid) SpaceNet.instance.getBodyFromName(nbtTagCompound.getString("body"));
        this.data = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(player).get(planet);
        ItemStack stack = new ItemStack(nbtTagCompound.getCompoundTag("rover"));
        this.rovers = new Rovers((IRoversItem) stack.getItem(), stack);
        this.timerToPlanet = new Timer(nbtTagCompound.getCompoundTag("timerTo"));
        this.timerFromPlanet = new Timer(nbtTagCompound.getCompoundTag("timerFrom"));
        this.spaceOperation = new SpaceOperation(this.planet, nbtTagCompound.getCompoundTag("operation"));
        final NBTTagList tagList = nbtTagCompound.getTagList("baseResource", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            IBaseResource baseResource = new BaseResource(tagCompound);
            iBaseResourceList.add(baseResource);
        }
        SpaceUpgradeSystem.system.updateListFromNBT(rovers.getItem(), rovers.getItemStack());
    }

    @Override
    public SpaceOperation getSpaceOperation() {
        return spaceOperation;
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FakeAsteroid that = (FakeAsteroid) o;
        return Objects.equals(player, that.player) && Objects.equals(planet, that.planet);
    }

    @Override
    public NBTTagCompound writeNBTTagCompound(final NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setUniqueId("uuid", player);
        nbtTagCompound.setString("body", planet.getName());
        nbtTagCompound.setTag("rover", this.rovers.getItemStack().writeToNBT(new NBTTagCompound()));
        nbtTagCompound.setTag("timerTo", timerToPlanet.writeNBT(new NBTTagCompound()));
        nbtTagCompound.setTag("timerFrom", timerFromPlanet.writeNBT(new NBTTagCompound()));
        nbtTagCompound.setTag("operation", spaceOperation.writeTag(new NBTTagCompound()));
        NBTTagList tagList = new NBTTagList();
        for (IBaseResource baseResource : iBaseResourceList) {
            NBTTagCompound nbt = new NBTTagCompound();
            baseResource.writeNBTTag(nbt);
            tagList.appendTag(nbt);
        }
        nbtTagCompound.setTag("baseResource", tagList);
        return nbtTagCompound;
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
    public IAsteroid getAsteroid() {
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
    public List<IBaseResource> getResource() {
        return iBaseResourceList;
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
