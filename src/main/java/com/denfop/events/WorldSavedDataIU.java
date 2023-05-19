package com.denfop.events;

import com.denfop.Constants;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.FakePlayer;
import com.denfop.api.space.fakebody.SpaceOperation;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldSavedDataIU extends WorldSavedData {

    public int col;
    World world;
    private NBTTagCompound tagCompound = new NBTTagCompound();

    public WorldSavedDataIU() {
        super(Constants.MOD_ID);
    }

    public WorldSavedDataIU(String name) {
        super(name);
    }

    public void setWorld(final World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {


        this.col = compound.getInteger("col");
        for (int i = 0; i < this.col; i++) {
            final NBTTagCompound nbt = compound.getCompoundTag(String.valueOf(i));
            final String name = nbt.getString("name");
            final NBTTagCompound tag = (NBTTagCompound) nbt.getTag("tag");
            FakePlayer player = new FakePlayer(name, tag);
            SpaceNet.instance.getFakeSpaceSystem().loadDataFromPlayer(player);
            final NBTTagCompound nbt_operation = nbt.getCompoundTag("operation");
            List<SpaceOperation> spaceOperations = new ArrayList<>();
            for (Map.Entry<String, IBody> map : SpaceNet.instance.getBodyMap().entrySet()) {
                if (nbt_operation.hasKey(map.getKey())) {
                    final SpaceOperation spaceOperation = new SpaceOperation(nbt_operation.getCompoundTag(map.getKey()));
                    spaceOperations.add(spaceOperation);

                }
            }
            SpaceNet.instance.getFakeSpaceSystem().loadSpaceOperation(spaceOperations, player);

        }

        if (compound.hasKey("vein")) {
            final NBTTagCompound tag = compound.getCompoundTag("vein");
            int size = tag.getInteger("max");
            for (int i = 0; i < size; i++) {
                final NBTTagCompound tag1 = tag.getCompoundTag(String.valueOf(i));
                VeinSystem.system.addVein(tag1);
            }

        } else {
            compound.setTag("vein", new NBTTagCompound());
        }
        if (compound.hasKey("colonies")) {
            NBTTagCompound tag1 = compound.getCompoundTag("colonies");
            int size = tag1.getInteger("col");
            for (int i = 0; i < size; i++) {
                final NBTTagCompound tag2 = tag1.getCompoundTag(String.valueOf(i));
                SpaceNet.instance.getColonieNet().addColony(tag2);
            }
        } else {
            compound.setTag("colonies", new NBTTagCompound());
        }
        if (compound.hasKey("radiations")) {
            NBTTagCompound tag1 = compound.getCompoundTag("radiations");
            int size = tag1.getInteger("col");
            for (int i = 0; i < size; i++) {
                final NBTTagCompound tag2 = tag1.getCompoundTag(String.valueOf(i));
                RadiationSystem.rad_system.addRadiation(tag2);
            }
        } else {
            compound.setTag("radiations", new NBTTagCompound());
        }



    }

    public NBTTagCompound getTagCompound() {

        return this.tagCompound;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound = new NBTTagCompound();

        List<FakePlayer> list = SpaceNet.instance.getFakeSpaceSystem().getFakePlayers();
        int i = 0;
        for (FakePlayer player : list) {
            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("name", player.getName());
            nbt.setTag("tag", player.getTag());
            final NBTTagCompound nbt_operation = new NBTTagCompound();
            final List<SpaceOperation> list1 = SpaceNet.instance
                    .getFakeSpaceSystem()
                    .getSpaceOperationMap(player);
            for (SpaceOperation spaceOperation : list1) {
                spaceOperation.writeTag(nbt_operation);
            }
            nbt.setTag("operation", nbt_operation);
            compound.setTag(String.valueOf(i), compound);
            i++;
        }
        compound.setInteger("col", i);
        final List<Vein> list1 = VeinSystem.system.getVeinsList();
        NBTTagCompound tag = new NBTTagCompound();
        for (int i1 = 0; i1 < list1.size(); i1++) {
            tag.setTag(String.valueOf(i1), list1.get(i1).writeTag());
        }
        tag.setInteger("max", list1.size());
        compound.setTag("vein", tag);
        NBTTagCompound tag1 = new NBTTagCompound();
        tag1.setInteger("col", SpaceNet.instance.getColonieNet().getList().size());
        List<FakePlayer> list2 = SpaceNet.instance.getColonieNet().getList();
        i = 0;
        for (FakePlayer player : list2) {
            tag1.setTag(String.valueOf(i), SpaceNet.instance.getColonieNet().writeNBT(tag1, player));
            i++;
        }

        compound.setTag("colonies", tag1);

        NBTTagCompound tag2 = new NBTTagCompound();
        tag2.setInteger("col", RadiationSystem.rad_system.radiationList.size());
        final List<Radiation> list3 = RadiationSystem.rad_system.radiationList;
        i = 0;
        for (Radiation radiation : list3) {
            tag2.setTag(String.valueOf(i), radiation.writeCompound());
            i++;
        }

        compound.setTag("radiations", tag2);
        this.tagCompound = compound;
        return compound;
    }

}
