package cofh.redstoneflux.api;

import net.minecraft.util.EnumFacing;

public interface IEnergyReceiver extends IEnergyHandler {

    int receiveEnergy(EnumFacing var1, int var2, boolean var3);

}
