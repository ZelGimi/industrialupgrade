package com.denfop.api.space.colonies;

import com.denfop.api.space.*;
import com.denfop.api.space.colonies.api.IColony;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.utils.Timer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Sends {

    private final UUID uuid;
    private final Timer timerToPlanet;
    private final IBody body;
    List<ItemStack> stacks = new LinkedList<>();
    List<FluidStack> fluidStacks = new LinkedList<>();

    public Sends(UUID uuid, IBody body, IColony colony) {
        this.uuid = uuid;
        int seconds = 0;
        this.body = body;
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
        int dop = colony.getLevel() / 10;
        this.timerToPlanet = new Timer(seconds / (4 + dop));
    }

    public Sends(CompoundTag tagCompound) {
        this.uuid = tagCompound.getUUID("uuid");
        this.body = SpaceNet.instance.getBodyFromName(tagCompound.getString("body"));
        this.timerToPlanet = new Timer(tagCompound.getCompound("time"));
        ListTag nbtTagList = tagCompound.getList("items", 10);
        this.stacks.clear();
        for (int i = 0; i < nbtTagList.size(); i++) {
            this.stacks.add(ItemStack.of(nbtTagList.getCompound(i)));
        }
        ListTag nbtTagList1 = tagCompound.getList("fluids", 10);
        this.fluidStacks.clear();
        for (int i = 0; i < nbtTagList1.size(); i++) {
            this.fluidStacks.add(FluidStack.loadFluidStackFromNBT(nbtTagList1.getCompound(i)));
        }

    }

    public IBody getBody() {
        return body;
    }

    public CompoundTag writeToNbt() {
        CompoundTag tagCompound = new CompoundTag();
        tagCompound.putUUID("uuid", uuid);
        tagCompound.putString("body", body.getName());
        tagCompound.put("time", this.timerToPlanet.writeNBT(new CompoundTag()));
        ListTag nbtTagList = new ListTag();
        for (ItemStack stack : stacks) {
            nbtTagList.add(stack.serializeNBT());
        }
        tagCompound.put("items", nbtTagList);
        ListTag nbtTagList1 = new ListTag();
        for (FluidStack fluidStack : fluidStacks) {
            nbtTagList1.add(fluidStack.writeToNBT(new CompoundTag()));
        }
        tagCompound.put("fluids", nbtTagList1);
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
