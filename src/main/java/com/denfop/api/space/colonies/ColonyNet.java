package com.denfop.api.space.colonies;

import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.FakePlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColonyNet implements IColonyNet {

    Map<FakePlayer, List<IColony>> fakePlayerListMap;
    List<IColony> colonyList;
    List<FakePlayer> fakePlayerList;

    public ColonyNet() {
        this.fakePlayerListMap = new HashMap<>();
        this.colonyList = new ArrayList<>();
        this.fakePlayerList = new ArrayList<>();
    }

    @Override
    public Map<FakePlayer, List<IColony>> getMap() {
        return this.fakePlayerListMap;
    }

    @Override
    public boolean canAddColony(final IBody body, final FakePlayer player) {
        if (!fakePlayerListMap.containsKey(player)) {
            return true;
        }
        List<IColony> list = fakePlayerListMap.get(player);
        for (IColony colony : list) {
            if (colony.matched(body)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void addColony(final IBody body, final FakePlayer player) {
        if (canAddColony(body, player)) {
            List<IColony> colonyList;
            final Colony colony = new Colony(body, player);
            if (!this.fakePlayerListMap.containsKey(player)) {
                colonyList = new ArrayList<>();
                colonyList.add(colony);
                fakePlayerListMap.put(player, colonyList);
                this.fakePlayerList.add(player);
            } else {
                colonyList = fakePlayerListMap.get(player);
                colonyList.add(colony);
            }
            this.colonyList.add(colony);
        }
    }

    @Override
    public void removeColony(final IColony colony, final FakePlayer player) {
        List<IColony> colonyList = fakePlayerListMap.get(player);
        colonyList.remove(colony);
        this.colonyList.remove(colony);
    }

    @Override
    public void working() {
        for (IColony colony : colonyList) {
            colony.update();
        }
    }

    @Override
    public List<IColony> getColonies() {
        return this.colonyList;
    }

    @Override
    public NBTTagCompound writeNBT(final NBTTagCompound tag, FakePlayer player) {
        final List<IColony> list = fakePlayerListMap.get(player);
        NBTTagCompound nbt = new NBTTagCompound();
        for (IColony colonie : list) {
            nbt.setTag(colonie.getBody().getName(), colonie.writeNBT(new NBTTagCompound()));
        }
        nbt.setTag("player", player.writeNBT());
        tag.setTag("colonia", nbt);
        return tag;
    }

    @Override
    public void addColony(final NBTTagCompound tag) {
        NBTTagCompound nbt = tag.getCompoundTag("colonia");
        List<IColony> list;
        final NBTTagCompound tagplayer = nbt.getCompoundTag("player");
        FakePlayer player = new FakePlayer(tagplayer.getString("name"), tagplayer.getCompoundTag("tag"));
        if (this.fakePlayerList.contains(player)) {
            return;
        }
        for (IBody body : SpaceNet.instance.getBodyList()) {
            if (nbt.hasKey(body.getName())) {
                NBTTagCompound nbt1 = nbt.getCompoundTag(body.getName());
                IColony colonie = new Colony(nbt1, player);
                if (!this.fakePlayerListMap.containsKey(player)) {
                    list = new ArrayList<>();
                    list.add(colonie);
                    fakePlayerListMap.put(player, colonyList);
                    this.fakePlayerList.add(player);
                } else {
                    list = fakePlayerListMap.get(player);
                    list.add(colonie);
                }
            }
        }
    }

    @Override
    public List<FakePlayer> getList() {
        return this.fakePlayerList;
    }

    @Override
    public void unload() {
        this.fakePlayerListMap.clear();
        this.colonyList.clear();
        this.fakePlayerList.clear();
    }

}
