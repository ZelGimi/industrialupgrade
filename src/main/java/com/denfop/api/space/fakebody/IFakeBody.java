package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.rovers.api.IRovers;
import com.denfop.utils.Timer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;
import java.util.UUID;

public interface IFakeBody {

    boolean matched(IBody body);

    SpaceOperation getSpaceOperation();

    NBTTagCompound writeNBTTagCompound(NBTTagCompound nbtTagCompound);

    Timer getTimerTo();

    Timer getTimerFrom();

    UUID getPlayer();

    IRovers getRover();

    List<IBaseResource> getResource();

    IData getData();

    void addBaseResource(IBaseResource baseResource);

    IBody getBody();

    void resetAuto();

}
