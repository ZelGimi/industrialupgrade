package com.denfop.api.space.fakebody;

import com.denfop.api.space.BaseResource;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.rovers.EnumTypeUpgrade;
import com.denfop.api.space.rovers.IRovers;
import com.denfop.api.space.rovers.IRoversItem;
import com.denfop.api.space.rovers.Rovers;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FakePlanet implements IFakePlanet {

    private FakePlayer player;
    private IPlanet planet;
    private IRovers rovers;
    private IData data;
    private int time;
    private boolean end;

    public FakePlanet(FakePlayer player, IPlanet planet, IRovers rovers, IData data) {
        this.player = player;
        this.planet = planet;
        this.rovers = rovers;
        int temp = SpaceUpgradeSystem.system.getModules(EnumTypeUpgrade.PROTECTION, this.rovers.getItemStack()) != null ?
                SpaceUpgradeSystem.system.getModules(EnumTypeUpgrade.PROTECTION, this.rovers.getItemStack()).number * 600 : 0;
        this.time = 1800 + temp;
        this.data = data;
        this.end = false;
    }

    public FakePlanet(FakePlayer player, String name) {
        this.readNBT(player, name);
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
    public void setEnd() {
        assert !this.end;
        this.end = true;
    }

    @Override
    public boolean getEnd() {
        return this.end;
    }

    @Override
    public IPlanet getPlanet() {
        return this.planet;
    }

    @Override
    public FakePlayer getPlayer() {
        return this.player;
    }

    @Override
    public IRovers getRover() {
        return this.rovers;
    }

    @Override
    public int getTime() {
        return this.time;
    }

    @Override
    public void setTime(final int time) {
        assert this.time > 0;
        this.time -= time;
    }

    @Override
    public IData getData() {
        return this.data;
    }

    @Override
    public boolean matched(final IBody body) {
        return this.planet.getName().equals(body.getName());
    }

    @Override
    public void readNBT(FakePlayer player, String name) {
        this.player = player;
        final NBTTagCompound nbt = player.getTag().getCompoundTag("space_iu");
        this.planet = (IPlanet) SpaceNet.instance.getBodyFromName(name);
        NBTTagCompound nbt1 = nbt.getCompoundTag(this.planet.getName());
        this.time = nbt1.getInteger("time");
        this.end = nbt1.getBoolean("end");
        final NBTTagCompound rovers_tag = nbt1.getCompoundTag("rovers");
        final ItemStack rover = new ItemStack(rovers_tag);
        this.rovers = new Rovers((IRoversItem) rover.getItem(), rover);
        this.data = new Data(player, this.planet);
        List<IBaseResource> list = new ArrayList<>();
        final NBTTagCompound resources_tag = nbt1.getCompoundTag("resource");
        int col = resources_tag.getInteger("resource");
        for (int i = 0; i < col; i++) {
            list.add(new BaseResource(new ItemStack((NBTTagCompound) resources_tag.getTag("resource" + i)), this.planet));
        }
        SpaceNet.instance.getFakeSpaceSystem().loadFakeBody(this, list, this.player);
    }

    @Override
    public void writeNBT(List<IBaseResource> list) {
        if (!this.player.getTag().hasKey("space_iu")) {
            final NBTTagCompound nbt = new NBTTagCompound();
            this.player.getTag().setTag("space_iu", nbt);
        }
        final NBTTagCompound nbt = this.player.getTag().getCompoundTag("space_iu");
        NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setInteger("time", this.time);
        nbt1.setBoolean("end", this.end);
        NBTTagCompound rovers_tag = new NBTTagCompound();
        this.rovers.getItemStack().writeToNBT(rovers_tag);
        nbt1.setTag("rovers", rovers_tag);

        NBTTagCompound resources = new NBTTagCompound();
        for (int i = 0; i < list.size(); i++) {
            resources.setTag("resource" + i, list.get(i).getItemStack().writeToNBT(new NBTTagCompound()));
        }
        resources.setInteger("col", list.size());
        nbt1.setTag("resource", resources);
        nbt.setTag(this.planet.getName(), nbt1);
    }

    @Override
    public void remove() {
        final NBTTagCompound nbt = this.player.getTag().getCompoundTag("space_iu");
        nbt.removeTag(this.planet.getName());
    }

}
