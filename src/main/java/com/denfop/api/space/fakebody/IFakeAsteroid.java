package com.denfop.api.space.fakebody;

import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.rovers.IRovers;

import java.util.List;

public interface IFakeAsteroid extends IFakeBody {

    IAsteroid getAsteroid();

    FakePlayer getPlayer();

    IRovers getRover();

    int getTime();

    void setTime(int time);

    IData getData();

    void setEnd();

    void readNBT(FakePlayer player, String name);

    void writeNBT(List<IBaseResource> list);

    void remove();

    boolean getEnd();

}
