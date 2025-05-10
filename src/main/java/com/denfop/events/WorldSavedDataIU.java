package com.denfop.events;

import com.denfop.IUCore;
import com.denfop.api.gasvein.GasVein;
import com.denfop.api.gasvein.GasVeinSystem;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.radiationsystem.Radiation;
import com.denfop.api.radiationsystem.RadiationSystem;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.*;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryController;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;
import java.util.*;

public class WorldSavedDataIU extends SavedData {

    public int col;
    Level world;
    private CompoundTag tagCompound = new CompoundTag();

    public WorldSavedDataIU() {
        super();
    }

    public WorldSavedDataIU(String name) {
        super();
    }

    public WorldSavedDataIU(@Nonnull CompoundTag compound) {


        SpaceNet.instance.getFakeSpaceSystem().unload();
        if (compound.contains("fakePlayers")) {
            ListTag fakePlayersList = compound.getList("fakePlayers", 10);
            for (int i = 0; i < fakePlayersList.size(); i++) {
                CompoundTag nbt = fakePlayersList.getCompound(i);
                UUID name = nbt.getUUID("name");
                ListTag fakesBody = nbt.getList("fakesBody", 10);
                ListTag dataBody = nbt.getList("dataBody", 10);
                final Map<IBody, Data> map = new HashMap<>();
                for (int ii = 0; ii < dataBody.size(); ii++) {
                    CompoundTag nbt1 = dataBody.getCompound(ii);
                    Data data = new Data(nbt1.getCompound("data"));
                    map.put(data.getBody(), data);
                }
                SpaceNet.instance.getFakeSpaceSystem().addDataBody(name, map);
                List<IFakeBody> fakeBodies = new LinkedList<>();
                for (int ii = 0; ii < fakesBody.size(); ii++) {
                    CompoundTag nbt1 = fakesBody.getCompound(ii);
                    byte id = nbt1.getByte("id");
                    if (id == 0) {
                        FakePlanet fakePlanet = new FakePlanet(nbt1);
                        fakeBodies.add(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().addFakePlanet(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(fakePlanet.getPlayer()).put(fakePlanet.getPlanet(),
                                fakePlanet.getSpaceOperation());
                    }
                    if (id == 1) {
                        FakeSatellite fakePlanet = new FakeSatellite(nbt1);
                        fakeBodies.add(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().addFakeSatellite(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(fakePlanet.getPlayer()).put(fakePlanet.getSatellite(),
                                fakePlanet.getSpaceOperation());
                    }
                    if (id == 2) {
                        FakeAsteroid fakePlanet = new FakeAsteroid(nbt1);
                        fakeBodies.add(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().addFakeAsteroid(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(fakePlanet.getPlayer()).put(fakePlanet.getAsteroid(),
                                fakePlanet.getSpaceOperation());
                    }

                }
                SpaceNet.instance.getFakeSpaceSystem().getBodyMap().put(name, new ArrayList<>(fakeBodies));
            }
        }

        VeinSystem.system.unload();
        if (compound.contains("veins")) {
            ListTag veinsList = compound.getList("veins", 10);
            for (int i = 0; i < veinsList.size(); i++) {
                CompoundTag veinTag = veinsList.getCompound(i);
                VeinSystem.system.addVein(veinTag);
            }
        }

        SpaceNet.instance.getColonieNet().unload();
        if (compound.contains("colonies")) {
            ListTag coloniesList = compound.getList("colonies", 10);
            for (int i = 0; i < coloniesList.size(); i++) {
                CompoundTag colonyTag = coloniesList.getCompound(i);
                SpaceNet.instance.getColonieNet().addColony(colonyTag);
            }
        }

        RadiationSystem.rad_system.clear();
        if (compound.contains("radiations")) {
            ListTag radiationsList = compound.getList("radiations", 10);
            for (int i = 0; i < radiationsList.size(); i++) {
                CompoundTag radiationTag = radiationsList.getCompound(i);
                RadiationSystem.rad_system.addRadiation(radiationTag);
            }
        }
        IUCore.mapStreakInfo.clear();
        if (compound.contains("streaks")) {
            ListTag streaksList = compound.getList("streaks", 10);
            for (int i = 0; i < streaksList.size(); i++) {
                CompoundTag streakTag = streaksList.getCompound(i);
                String nick = streakTag.getString("nick");
                PlayerStreakInfo streakInfo = new PlayerStreakInfo(streakTag.getCompound("streak"));
                IUCore.mapStreakInfo.putIfAbsent(nick, streakInfo);
            }
        }
        if (compound.contains("pollution")) {
            CompoundTag pollutionTag = compound.getCompound("pollution");
            PollutionManager.pollutionManager.loadData(pollutionTag);
        }

          TileEntityEarthQuarryController.chunkPos.clear();
        if (compound.contains("earth_quarry")) {
            ListTag earthQuarryList = compound.getList("earth_quarry", 10);
            for (int i = 0; i < earthQuarryList.size(); i++) {
                CompoundTag chunkTag = earthQuarryList.getCompound(i);
                int x = chunkTag.getInt("x");
                int z = chunkTag.getInt("z");
                TileEntityEarthQuarryController.chunkPos.add(new ChunkPos(x, z));
            }
        }

        WorldGenGas.gasMap.clear();
        if (compound.contains("gen_gas")) {
            ListTag gasMapList = compound.getList("gen_gas", 10);
            for (int i = 0; i < gasMapList.size(); i++) {
                CompoundTag gasTag = gasMapList.getCompound(i);
                int x = gasTag.getInt("x");
                int z = gasTag.getInt("z");
                CompoundTag dataTag = gasTag.getCompound("data");
                WorldGenGas.gasMap.put(new ChunkPos(x, z), new GenData(dataTag));
            }
        }
        PrimitiveHandler.getMapPrimitives().clear();
        if (compound.contains("primitive")) {
            ListTag primitiveList = compound.getList("primitive", 10);

            for (int i = 0; i < primitiveList.size(); i++) {
                CompoundTag primitiveCompound = primitiveList.getCompound(i);
                ListTag playersList = primitiveCompound.getList("listPlayers", 10);
                Map<UUID, Double> playerMap = new HashMap<>();

                for (int j = 0; j < playersList.size(); j++) {
                    CompoundTag playerCompound = playersList.getCompound(j);
                    UUID playerUUID = playerCompound.getUUID("uuid");
                    double value = playerCompound.getDouble("value");

                    playerMap.put(playerUUID, value);
                }


                EnumPrimitive primitiveType = EnumPrimitive.values()[primitiveCompound.getInt("id")];
                PrimitiveHandler.getMapPrimitives().put(primitiveType, playerMap);
            }
        }
        GasVeinSystem.system.unload();
        if (compound.contains("gasvein")) {
            ListTag gasVeinsList = compound.getList("gasvein", 10);
            for (int i = 0; i < gasVeinsList.size(); i++) {
                CompoundTag gasVeinTag = gasVeinsList.getCompound(i);
                GasVeinSystem.system.addVein(gasVeinTag);
            }
        }
        RelocatorNetwork.instance.onUnload();
        if (compound.contains("relocator")) {

            CompoundTag tag8 = compound.getCompound("relocator");


            ListTag nbtTagList = tag8.getList("worldUUID", 10);

            for (int i = 0; i < nbtTagList.size(); i++) {
                CompoundTag tag9 = nbtTagList.getCompound(i);
                ResourceKey<Level> resourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(tag9.getString("id")));
                ListTag nbtTagList1 = tag9.getList("listUUID", 10);
                Map<UUID, List<Point>> uuidMap = new HashMap<>();
                for (int j = 0; j < nbtTagList1.size(); j++) {
                    CompoundTag tag10 = nbtTagList1.getCompound(j);
                    if (tag10.contains("listPoint")) {
                        UUID uuid = tag10.getUUID("uuid");
                        ListTag nbtTagList2 = tag10.getList("listPoint", 10);
                        List<Point> points = new LinkedList<>();
                        for (int k = 0; k < nbtTagList2.size(); k++) {
                            CompoundTag pointTag = nbtTagList2.getCompound(k);
                            Point point = new Point(pointTag);
                            points.add(point);
                        }
                        uuidMap.put(uuid, points);
                    }


                    RelocatorNetwork.instance.getWorldDataPoints().put(resourceKey, uuidMap);
                }

            }
        } else {
            compound.put("relocator", new CompoundTag());
        }
    }

    public Level getWorld() {
        return world;
    }

    public void setWorld(final Level world) {
        this.world = world;
    }

    public CompoundTag getTagCompound() {

        return this.tagCompound;
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        compound = new CompoundTag();

        ListTag fakePlayersList = new ListTag();
        for (UUID player : SpaceNet.instance.getFakeSpaceSystem().getBodyMap().keySet()) {
            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("name", player);
            final List<IFakeBody> list = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().get(
                    player);
            final Map<IBody, Data> map = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(
                    player);
            ListTag fakesBody = new ListTag();
            for (IFakeBody fakeBody : list) {
                CompoundTag nbt1 = new CompoundTag();
                if (fakeBody instanceof IFakePlanet) {
                    nbt1.putByte("id", (byte) 0);
                }
                if (fakeBody instanceof IFakeSatellite) {
                    nbt1.putByte("id", (byte) 1);
                }
                if (fakeBody instanceof IFakeAsteroid) {
                    nbt1.putByte("id", (byte) 2);
                }
                fakeBody.writeNBTTagCompound(nbt1);
                fakesBody.add(nbt1);
            }
            nbt.put("fakesBody", fakesBody);
            ListTag dataBody = new ListTag();
            for (Map.Entry<IBody, Data> dataEntry : map.entrySet()) {
                CompoundTag nbt1 = new CompoundTag();
                nbt1.put("data", dataEntry.getValue().writeNBT());
                dataBody.add(nbt1);
            }
            nbt.put("dataBody", dataBody);
            fakePlayersList.add(nbt);
        }
        compound.put("fakePlayers", fakePlayersList);

        ListTag veinsList = new ListTag();
        for (Vein vein : VeinSystem.system.getVeinsList()) {
            veinsList.add(vein.writeTag());
        }
        compound.put("veins", veinsList);

        ListTag coloniesList = new ListTag();
        for (UUID player : SpaceNet.instance.getColonieNet().getList()) {
            coloniesList.add(SpaceNet.instance.getColonieNet().writeNBT(new CompoundTag(), player));
        }
        compound.put("colonies", coloniesList);

        ListTag radiationsList = new ListTag();
        for (Radiation radiation : RadiationSystem.rad_system.radiationList) {
            radiationsList.add(radiation.writeCompound());
        }
        compound.put("radiations", radiationsList);
        ListTag primitive = new ListTag();
        for (Map.Entry<EnumPrimitive, Map<UUID, Double>> entry : PrimitiveHandler.getMapPrimitives().entrySet()) {
            CompoundTag primitives = new CompoundTag();
            ListTag players = new ListTag();
            for (Map.Entry<UUID, Double> entry1 : entry.getValue().entrySet()) {
                CompoundTag player = new CompoundTag();
                player.putUUID("uuid", entry1.getKey());
                player.putDouble("value", entry1.getValue());
                players.add(player);
            }
            primitives.put("listPlayers", players);
            primitives.putInt("id", entry.getKey().ordinal());
            primitive.add(primitives);
        }
        compound.put("primitive", primitive);

        ListTag streaksList = new ListTag();
        for (Map.Entry<String, PlayerStreakInfo> entry : IUCore.mapStreakInfo.entrySet()) {
            CompoundTag streakTag = new CompoundTag();
            streakTag.putString("nick", entry.getKey());
            streakTag.put("streak", entry.getValue().writeNBT());
            streaksList.add(streakTag);
        }
        compound.put("streaks", streaksList);

        compound.put("pollution", PollutionManager.pollutionManager.writeCompound());

        ListTag earthQuarryList = new ListTag();
        for (ChunkPos chunkPos : TileEntityEarthQuarryController.chunkPos) {
            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putInt("x", chunkPos.x);
            chunkTag.putInt("z", chunkPos.z);
            earthQuarryList.add(chunkTag);
        }
        compound.put("earth_quarry", earthQuarryList);

        ListTag gasMapList = new ListTag();
        for (Map.Entry<ChunkPos, GenData> entry : WorldGenGas.gasMap.entrySet()) {
            CompoundTag gasTag = new CompoundTag();
            gasTag.putInt("x", entry.getKey().x);
            gasTag.putInt("z", entry.getKey().z);
            gasTag.put("data", entry.getValue().writeNBT());
            gasMapList.add(gasTag);
        }
        compound.put("gen_gas", gasMapList);

        ListTag gasVeinsList = new ListTag();
        for (GasVein gasVein : GasVeinSystem.system.getVeinsList()) {
            gasVeinsList.add(gasVein.writeTag());
        }
        compound.put("gasvein", gasVeinsList);

        final Map<ResourceKey<Level>, Map<UUID, List<Point>>> map = RelocatorNetwork.instance.getWorldDataPoints();
        CompoundTag relocatorTag = new CompoundTag();
        ListTag worldListTag = new ListTag();

        for (Map.Entry<ResourceKey<Level>, Map<UUID, List<Point>>> worldEntry : map.entrySet()) {
            CompoundTag worldTag = new CompoundTag();
            worldTag.putString("id", worldEntry.getKey().location().toString());
            ListTag uuidListTag = new ListTag();

            for (Map.Entry<UUID, List<Point>> uuidEntry : worldEntry.getValue().entrySet()) {
                CompoundTag uuidTag = new CompoundTag();
                uuidTag.putUUID("uuid", uuidEntry.getKey());

                ListTag pointsListTag = new ListTag();
                for (Point point : uuidEntry.getValue()) {
                    pointsListTag.add(point.writeToNBT(new CompoundTag()));
                }

                uuidTag.put("listPoint", pointsListTag);
                uuidListTag.add(uuidTag);
            }

            worldTag.put("listUUID", uuidListTag);
            worldListTag.add(worldTag);
        }

        relocatorTag.put("worldUUID", worldListTag);
        compound.put("relocator", relocatorTag);


        this.tagCompound = compound;
        return compound;
    }

}
