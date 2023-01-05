package cofh.redstoneflux.api;

import net.minecraft.util.EnumFacing;

public interface IEnergyProvider extends IEnergyHandler {

    int extractEnergy(EnumFacing var1, int var2, boolean var3);

}
