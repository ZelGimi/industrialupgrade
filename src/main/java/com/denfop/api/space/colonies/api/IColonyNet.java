package com.denfop.api.space.colonies.api;

import com.denfop.api.space.IBody;
import com.denfop.api.space.colonies.DataItem;
import com.denfop.api.space.colonies.Sends;
import com.denfop.api.space.colonies.api.IColony;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IColonyNet {

    Map<UUID, List<IColony>> getMap();

    boolean canAddColony(IBody body, EntityPlayer player);

    void addColony(IBody body, EntityPlayer player);

    void addItemToColony(IBody body, EntityPlayer player);

    void removeColony(IColony body, UUID player);
    void removeColony(IBody body, UUID player);

    void working();

    List<Sends> getSendsFromUUID(UUID uuid);

    List<IColony> getColonies();

    void addFluidStack(IBody body, short level, FluidStack fluidStack);
    void addItemStack(IBody body, short level, ItemStack fluidStack);
    List<DataItem<FluidStack>> getFluidsFromBody(IBody body);
    List<DataItem<ItemStack>> getItemsFromBody(IBody body);

    NBTTagCompound writeNBT(NBTTagCompound tag, UUID player);

    void addColony(final NBTTagCompound tag);

    List<UUID> getList();

    void unload();

    void sendResourceToPlanet(UUID uniqueID, IBody body1);

    void setAutoSends(UUID uniqueID, IBody body1);

}
