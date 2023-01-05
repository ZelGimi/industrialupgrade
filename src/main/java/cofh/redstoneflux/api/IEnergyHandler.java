package cofh.redstoneflux.api;

import net.minecraft.util.EnumFacing;

public interface IEnergyHandler extends IEnergyConnection {

    int getEnergyStored(EnumFacing var1);

    int getMaxEnergyStored(EnumFacing var1);

}
