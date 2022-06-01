package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.rovers.IRovers;

import java.util.List;

public interface IFakeSatellite extends IFakeBody {

    ISatellite getSatellite();

    FakePlayer getPlayer();

    IRovers getRover();

    int getTime();

    void setTime(int time);

    IData getData();

    void setEnd();

    boolean getEnd();

    void readNBT(FakePlayer player, String name);

    void writeNBT(List<IBaseResource> list);

    void remove();

}
