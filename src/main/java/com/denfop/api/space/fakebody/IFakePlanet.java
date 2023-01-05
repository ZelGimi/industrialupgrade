package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.rovers.IRovers;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public interface IFakePlanet extends IFakeBody {

    IPlanet getPlanet();

    FakePlayer getPlayer();

    IRovers getRover();

    int getTime();

    void setTime(int time);

    IData getData();

    void setEnd();

    void readNBT(FakePlayer player, String name);


    void writeNBT(List<IBaseResource> list);

    NBTTagCompound write(List<IBaseResource> list);

    void remove();

    boolean getEnd();


}
