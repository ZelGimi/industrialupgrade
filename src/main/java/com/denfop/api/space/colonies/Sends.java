package com.denfop.api.space.colonies;

import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.SpaceInit;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.utils.Timer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Sends {

    private final UUID uuid;
    private final Timer timerToPlanet;
    List<ItemStack> stacks = new LinkedList<>();
    List<FluidStack> fluidStacks = new LinkedList<>();

    public Sends(UUID uuid, IBody body, IColony colony) {
        this.uuid = uuid;
        int seconds = 0;
        if (body instanceof IPlanet) {
            seconds = (int) ((Math.abs(body.getDistance() - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance())) * (16.66 * 60 * 0.8));
        } else if (body instanceof ISatellite) {
            double distancePlanetToPlanet =
                    (((ISatellite) body)
                            .getPlanet()
                            .getDistance() - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance());
            double distanceSatellite = Math.abs(SpaceInit.moon.getDistanceFromPlanet() - ((ISatellite) body).getDistanceFromPlanet()) / SpaceInit.moon.getDistanceFromPlanet();
            if (((ISatellite) body).getPlanet() == SpaceInit.earth) {
                distanceSatellite = 1;
            }
            seconds = (int) (Math.abs(distanceSatellite * 5 * 60 * 0.8 + distancePlanetToPlanet * (16.66 * 60 * 0.8)));
        } else {
            seconds =
                    (int) ((Math.abs(((((IAsteroid) body).getMaxDistance() - ((IAsteroid) body).getMinDistance()) / 2 + ((IAsteroid) body).getMinDistance()) - SpaceInit.earth.getDistance()) / (SpaceInit.mars.getDistance() - SpaceInit.earth.getDistance())) * (16.66 * 60 * 0.8));
        }
        int dop = colony.getLevel() / 20;
        this.timerToPlanet = new Timer(seconds / (4+dop));
    }

    public Sends(NBTTagCompound tagCompound) {
        this.uuid = tagCompound.getUniqueId("uuid");
        this.timerToPlanet = new Timer(tagCompound.getCompoundTag("time"));
        NBTTagList nbtTagList = tagCompound.getTagList("items", 10);
        this.stacks.clear();
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            this.stacks.add(new ItemStack(nbtTagList.getCompoundTagAt(i)));
        }
        NBTTagList nbtTagList1 = tagCompound.getTagList("fluids", 10);
        this.fluidStacks.clear();
        for (int i = 0; i < nbtTagList1.tagCount(); i++) {
            this.fluidStacks.add(FluidStack.loadFluidStackFromNBT(nbtTagList1.getCompoundTagAt(i)));
        }
    }

    public NBTTagCompound writeToNbt() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setUniqueId("uuid", uuid);
        tagCompound.setTag("time", this.timerToPlanet.writeNBT(new NBTTagCompound()));
        NBTTagList nbtTagList = new NBTTagList();
        for (ItemStack stack : stacks) {
            nbtTagList.appendTag(stack.serializeNBT());
        }
        tagCompound.setTag("items", nbtTagList);
        NBTTagList nbtTagList1 = new NBTTagList();
        for (FluidStack fluidStack : fluidStacks) {
            nbtTagList1.appendTag(fluidStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setTag("fluids", nbtTagList1);
        return tagCompound;
    }

    public Timer getTimerToPlanet() {
        return timerToPlanet;
    }

    public boolean needRemove() {
        return !this.timerToPlanet.canWork();
    }

    public void works() {
        this.timerToPlanet.work();
        if (!this.timerToPlanet.canWork()) {
            final IRocketLaunchPad spacePad = SpaceNet.instance
                    .getFakeSpaceSystem()
                    .getRocketPadMap()
                    .get(uuid);
            if (spacePad != null) {
                for (ItemStack stack : this.stacks) {
                    spacePad.getSlotOutput().add(stack);
                }
                for (FluidStack fluidStack : fluidStacks) {
                    spacePad.addFluidStack(fluidStack);
                }

            } else {
                this.timerToPlanet.setCanWork(true);
            }
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void addStack(FluidStack fluidStack) {
        fluidStacks.add(fluidStack);
    }

    public void addStack(ItemStack itemStack) {
        stacks.add(itemStack);
    }

}
