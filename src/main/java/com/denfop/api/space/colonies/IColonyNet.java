package com.denfop.api.space.colonies;

import com.denfop.api.space.IBody;
import com.denfop.api.space.fakebody.FakePlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.Map;

public interface IColonyNet {

    Map<FakePlayer, List<IColony>> getMap();

    boolean canAddColony(IBody body, FakePlayer player);

    void addColony(IBody body, FakePlayer player);

    void removeColony(IColony body, FakePlayer player);

    void working();

    List<IColony> getColonies();

    NBTTagCompound writeNBT(NBTTagCompound tag, FakePlayer player);

    void addColony(final NBTTagCompound tag);

    List<FakePlayer> getList();

    void unload();

}
