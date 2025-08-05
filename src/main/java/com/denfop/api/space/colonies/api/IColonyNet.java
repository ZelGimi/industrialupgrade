package com.denfop.api.space.colonies.api;

import com.denfop.api.space.IBody;
import com.denfop.api.space.colonies.DataItem;
import com.denfop.api.space.colonies.Sends;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IColonyNet {

    Map<UUID, List<IColony>> getMap();

    boolean canAddColony(IBody body, Player player);

    void addColony(IBody body, Player player);

    void addItemToColony(IBody body, Player player);

    void removeColony(IColony body, UUID player);

    void removeColony(IBody body, UUID player);

    void working();

    List<Sends> getSendsFromUUID(UUID uuid);

    List<IColony> getColonies();

    void addFluidStack(IBody body, short level, FluidStack fluidStack);

    void addItemStack(IBody body, short level, ItemStack fluidStack);

    List<DataItem<FluidStack>> getFluidsFromBody(IBody body);

    List<DataItem<ItemStack>> getItemsFromBody(IBody body);

    CompoundTag writeNBT(CompoundTag tag, UUID player, HolderLookup.Provider p_323640_);

    void addColony(final CompoundTag tag, HolderLookup.Provider p_323640_);

    List<UUID> getList();

    void unload();

    void sendResourceToPlanet(UUID uniqueID, IBody body1);

    void setAutoSends(UUID uniqueID, IBody body1);

}
