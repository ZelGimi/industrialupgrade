package com.denfop.api.research;

import net.minecraft.entity.player.EntityPlayer;

public interface IResearchSystem {
    void uploadData(EntityPlayer player);

    void downloadData(EntityPlayer player);

    boolean checkData(EntityPlayer player);


}
