package com.denfop.events;

import com.denfop.Constants;
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
import com.denfop.api.space.fakebody.Data;
import com.denfop.api.space.fakebody.FakeAsteroid;
import com.denfop.api.space.fakebody.FakePlanet;
import com.denfop.api.space.fakebody.FakeSatellite;
import com.denfop.api.space.fakebody.IFakeAsteroid;
import com.denfop.api.space.fakebody.IFakeBody;
import com.denfop.api.space.fakebody.IFakePlanet;
import com.denfop.api.space.fakebody.IFakeSatellite;
import com.denfop.api.vein.Vein;
import com.denfop.api.vein.VeinSystem;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.tiles.quarry_earth.TileEntityEarthQuarryController;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.denfop.api.guidebook.GuideBookCore.uuidGuideMap;

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

    public World getWorld() {
        return world;
    }

    public void setWorld(final World world) {
        this.world = world;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        SpaceNet.instance.getFakeSpaceSystem().unload();
        if (compound.hasKey("fakePlayers")) {
            NBTTagList fakePlayersList = compound.getTagList("fakePlayers", 10);
            for (int i = 0; i < fakePlayersList.tagCount(); i++) {
                NBTTagCompound nbt = fakePlayersList.getCompoundTagAt(i);
                UUID name = nbt.getUniqueId("name");
                NBTTagList fakesBody = nbt.getTagList("fakesBody", 10);
                NBTTagList dataBody = nbt.getTagList("dataBody", 10);
                final Map<IBody, Data> map = new HashMap<>();
                for (int ii = 0; ii < dataBody.tagCount(); ii++) {
                    NBTTagCompound nbt1 = dataBody.getCompoundTagAt(ii);
                    Data data = new Data(nbt1.getCompoundTag("data"));
                    map.put(data.getBody(), data);
                }
                SpaceNet.instance.getFakeSpaceSystem().addDataBody(name, map);
                List<IFakeBody> fakeBodies = new LinkedList<>();
                for (int ii = 0; ii < fakesBody.tagCount(); ii++) {
                    NBTTagCompound nbt1 = fakesBody.getCompoundTagAt(ii);
                    byte id = nbt1.getByte("id");
                    if (id == 0) {
                        FakePlanet fakePlanet = new FakePlanet(nbt1);
                        fakeBodies.add(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().addFakePlanet(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(fakePlanet.getPlayer()).put(
                                fakePlanet.getPlanet(),
                                fakePlanet.getSpaceOperation()
                        );
                    }
                    if (id == 1) {
                        FakeSatellite fakePlanet = new FakeSatellite(nbt1);
                        fakeBodies.add(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().addFakeSatellite(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(fakePlanet.getPlayer()).put(
                                fakePlanet.getSatellite(),
                                fakePlanet.getSpaceOperation()
                        );
                    }
                    if (id == 2) {
                        FakeAsteroid fakePlanet = new FakeAsteroid(nbt1);
                        fakeBodies.add(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().addFakeAsteroid(fakePlanet);
                        SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(fakePlanet.getPlayer()).put(
                                fakePlanet.getAsteroid(),
                                fakePlanet.getSpaceOperation()
                        );
                    }

                }
                SpaceNet.instance.getFakeSpaceSystem().getBodyMap().put(name, new ArrayList<>(fakeBodies));
            }
        }

        VeinSystem.system.unload();
        if (compound.hasKey("veins")) {
            NBTTagList veinsList = compound.getTagList("veins", 10);
            for (int i = 0; i < veinsList.tagCount(); i++) {
                NBTTagCompound veinTag = veinsList.getCompoundTagAt(i);
                VeinSystem.system.addVein(veinTag);
            }
        }

        SpaceNet.instance.getColonieNet().unload();
        if (compound.hasKey("colonies")) {
            NBTTagList coloniesList = compound.getTagList("colonies", 10);
            for (int i = 0; i < coloniesList.tagCount(); i++) {
                NBTTagCompound colonyTag = coloniesList.getCompoundTagAt(i);
                SpaceNet.instance.getColonieNet().addColony(colonyTag);
            }
        }

        RadiationSystem.rad_system.clear();
        if (compound.hasKey("radiations")) {
            NBTTagList radiationsList = compound.getTagList("radiations", 10);
            for (int i = 0; i < radiationsList.tagCount(); i++) {
                NBTTagCompound radiationTag = radiationsList.getCompoundTagAt(i);
                RadiationSystem.rad_system.addRadiation(radiationTag);
            }
        }
        IUCore.mapStreakInfo.clear();
        if (compound.hasKey("streaks")) {
            NBTTagList streaksList = compound.getTagList("streaks", 10);
            for (int i = 0; i < streaksList.tagCount(); i++) {
                NBTTagCompound streakTag = streaksList.getCompoundTagAt(i);
                String nick = streakTag.getString("nick");
                PlayerStreakInfo streakInfo = new PlayerStreakInfo(streakTag.getCompoundTag("streak"));
                IUCore.mapStreakInfo.putIfAbsent(nick, streakInfo);
            }
        }
        if (compound.hasKey("pollution")) {
            NBTTagCompound pollutionTag = compound.getCompoundTag("pollution");
            PollutionManager.pollutionManager.loadData(pollutionTag);
        }

        TileEntityEarthQuarryController.chunkPos.clear();
        if (compound.hasKey("earth_quarry")) {
            NBTTagList earthQuarryList = compound.getTagList("earth_quarry", 10);
            for (int i = 0; i < earthQuarryList.tagCount(); i++) {
                NBTTagCompound chunkTag = earthQuarryList.getCompoundTagAt(i);
                int x = chunkTag.getInteger("x");
                int z = chunkTag.getInteger("z");
                TileEntityEarthQuarryController.chunkPos.add(new ChunkPos(x, z));
            }
        }

        WorldGenGas.gasMap.clear();
        if (compound.hasKey("gen_gas")) {
            NBTTagList gasMapList = compound.getTagList("gen_gas", 10);
            for (int i = 0; i < gasMapList.tagCount(); i++) {
                NBTTagCompound gasTag = gasMapList.getCompoundTagAt(i);
                int x = gasTag.getInteger("x");
                int z = gasTag.getInteger("z");
                NBTTagCompound dataTag = gasTag.getCompoundTag("data");
                WorldGenGas.gasMap.put(new ChunkPos(x, z), new GenData(dataTag));
            }
        }
        PrimitiveHandler.getMapPrimitives().clear();
        if (compound.hasKey("primitive")) {
            NBTTagList primitiveList = compound.getTagList("primitive", 10);

            for (int i = 0; i < primitiveList.tagCount(); i++) {
                NBTTagCompound primitiveCompound = primitiveList.getCompoundTagAt(i);
                NBTTagList playersList = primitiveCompound.getTagList("listPlayers", 10);
                Map<UUID, Double> playerMap = new HashMap<>();

                for (int j = 0; j < playersList.tagCount(); j++) {
                    NBTTagCompound playerCompound = playersList.getCompoundTagAt(j);
                    UUID playerUUID = playerCompound.getUniqueId("uuid");
                    double value = playerCompound.getDouble("value");

                    playerMap.put(playerUUID, value);
                }


                EnumPrimitive primitiveType = EnumPrimitive.values()[primitiveCompound.getInteger("id")];
                PrimitiveHandler.getMapPrimitives().put(primitiveType, playerMap);
            }
        }
        GasVeinSystem.system.unload();
        if (compound.hasKey("gasvein")) {
            NBTTagList gasVeinsList = compound.getTagList("gasvein", 10);
            for (int i = 0; i < gasVeinsList.tagCount(); i++) {
                NBTTagCompound gasVeinTag = gasVeinsList.getCompoundTagAt(i);
                GasVeinSystem.system.addVein(gasVeinTag);
            }
        }
        RelocatorNetwork.instance.onUnload();
        if (compound.hasKey("relocator")) {

            NBTTagCompound tag8 = compound.getCompoundTag("relocator");


            NBTTagList nbtTagList = tag8.getTagList("worldUUID", 10);

            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                NBTTagCompound tag9 = nbtTagList.getCompoundTagAt(i);
                int id = tag9.getInteger("id");

                NBTTagList nbtTagList1 = tag9.getTagList("listUUID", 10);
                Map<UUID, List<Point>> uuidMap = new HashMap<>();
                for (int j = 0; j < nbtTagList1.tagCount(); j++) {
                    NBTTagCompound tag10 = nbtTagList1.getCompoundTagAt(j);
                    if (tag10.hasKey("listPoint")) {
                        UUID uuid = tag10.getUniqueId("uuid");
                        NBTTagList nbtTagList2 = tag10.getTagList("listPoint", 10);
                        List<Point> points = new LinkedList<>();
                        for (int k = 0; k < nbtTagList2.tagCount(); k++) {
                            NBTTagCompound pointTag = nbtTagList2.getCompoundTagAt(k);
                            Point point = new Point(pointTag);
                            points.add(point);
                        }
                        uuidMap.put(uuid, points);
                    }


                    RelocatorNetwork.instance.getWorldDataPoints().put(id, uuidMap);
                }

            }
        } else {
            compound.setTag("relocator", new NBTTagCompound());
        }
        Map<UUID, Map<String, List<String>>> mapData = new HashMap<>();
        uuidGuideMap.clear();
        if (compound.hasKey("guide_book")) {
            NBTTagCompound data = compound.getCompoundTag("guide_book");
            NBTTagList list = data.getTagList("list", 10);

            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound data1 = list.getCompoundTagAt(i);
                UUID uuid = data1.getUniqueId("uuid");

                Map<String, List<String>> mapQuest = new HashMap<>();
                NBTTagList list1 = data1.getTagList("list", 10);

                for (int j = 0; j < list1.tagCount(); j++) {
                    NBTTagCompound data2 = list1.getCompoundTagAt(j);
                    String tab = data2.getString("tab");

                    NBTTagList list2 = data2.getTagList("list", 8);
                    List<String> names = new ArrayList<>();

                    for (int k = 0; k < list2.tagCount(); k++) {
                        names.add(list2.getStringTagAt(k));
                    }

                    mapQuest.put(tab, names);
                }

                mapData.put(uuid, mapQuest);
            }
        }
        uuidGuideMap = mapData;


    }

    public NBTTagCompound getTagCompound() {

        return this.tagCompound;
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound = new NBTTagCompound();

        NBTTagList fakePlayersList = new NBTTagList();
        for (UUID player : SpaceNet.instance.getFakeSpaceSystem().getBodyMap().keySet()) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setUniqueId("name", player);
            final List<IFakeBody> list = SpaceNet.instance.getFakeSpaceSystem().getBodyMap().get(
                    player);
            final Map<IBody, Data> map = SpaceNet.instance.getFakeSpaceSystem().getDataFromUUID(
                    player);
            NBTTagList fakesBody = new NBTTagList();
            for (IFakeBody fakeBody : list) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                if (fakeBody instanceof IFakePlanet) {
                    nbt1.setByte("id", (byte) 0);
                }
                if (fakeBody instanceof IFakeSatellite) {
                    nbt1.setByte("id", (byte) 1);
                }
                if (fakeBody instanceof IFakeAsteroid) {
                    nbt1.setByte("id", (byte) 2);
                }
                fakeBody.writeNBTTagCompound(nbt1);
                fakesBody.appendTag(nbt1);
            }
            nbt.setTag("fakesBody", fakesBody);
            NBTTagList dataBody = new NBTTagList();
            for (Map.Entry<IBody, Data> dataEntry : map.entrySet()) {
                NBTTagCompound nbt1 = new NBTTagCompound();
                nbt1.setTag("data", dataEntry.getValue().writeNBT());
                dataBody.appendTag(nbt1);
            }
            nbt.setTag("dataBody", dataBody);
            fakePlayersList.appendTag(nbt);
        }
        compound.setTag("fakePlayers", fakePlayersList);

        NBTTagList veinsList = new NBTTagList();
        for (Vein vein : VeinSystem.system.getVeinsList()) {
            veinsList.appendTag(vein.writeTag());
        }
        compound.setTag("veins", veinsList);

        NBTTagList coloniesList = new NBTTagList();
        for (UUID player : SpaceNet.instance.getColonieNet().getList()) {
            coloniesList.appendTag(SpaceNet.instance.getColonieNet().writeNBT(new NBTTagCompound(), player));
        }
        compound.setTag("colonies", coloniesList);

        NBTTagList radiationsList = new NBTTagList();
        for (Radiation radiation : RadiationSystem.rad_system.radiationList) {
            radiationsList.appendTag(radiation.writeCompound());
        }
        compound.setTag("radiations", radiationsList);
        NBTTagList primitive = new NBTTagList();
        for (Map.Entry<EnumPrimitive, Map<UUID, Double>> entry : PrimitiveHandler.getMapPrimitives().entrySet()) {
            NBTTagCompound primitives = new NBTTagCompound();
            NBTTagList players = new NBTTagList();
            for (Map.Entry<UUID, Double> entry1 : entry.getValue().entrySet()) {
                NBTTagCompound player = new NBTTagCompound();
                player.setUniqueId("uuid", entry1.getKey());
                player.setDouble("value", entry1.getValue());
                players.appendTag(player);
            }
            primitives.setTag("listPlayers", players);
            primitives.setInteger("id", entry.getKey().ordinal());
            primitive.appendTag(primitives);
        }
        compound.setTag("primitive", primitive);

        NBTTagList streaksList = new NBTTagList();
        for (Map.Entry<String, PlayerStreakInfo> entry : IUCore.mapStreakInfo.entrySet()) {
            NBTTagCompound streakTag = new NBTTagCompound();
            streakTag.setString("nick", entry.getKey());
            streakTag.setTag("streak", entry.getValue().writeNBT());
            streaksList.appendTag(streakTag);
        }
        compound.setTag("streaks", streaksList);

        compound.setTag("pollution", PollutionManager.pollutionManager.writeCompound());

        NBTTagList earthQuarryList = new NBTTagList();
        for (ChunkPos chunkPos : TileEntityEarthQuarryController.chunkPos) {
            NBTTagCompound chunkTag = new NBTTagCompound();
            chunkTag.setInteger("x", chunkPos.x);
            chunkTag.setInteger("z", chunkPos.z);
            earthQuarryList.appendTag(chunkTag);
        }
        compound.setTag("earth_quarry", earthQuarryList);

        NBTTagList gasMapList = new NBTTagList();
        for (Map.Entry<ChunkPos, GenData> entry : WorldGenGas.gasMap.entrySet()) {
            NBTTagCompound gasTag = new NBTTagCompound();
            gasTag.setInteger("x", entry.getKey().x);
            gasTag.setInteger("z", entry.getKey().z);
            gasTag.setTag("data", entry.getValue().writeNBT());
            gasMapList.appendTag(gasTag);
        }
        compound.setTag("gen_gas", gasMapList);

        NBTTagList gasVeinsList = new NBTTagList();
        for (GasVein gasVein : GasVeinSystem.system.getVeinsList()) {
            gasVeinsList.appendTag(gasVein.writeTag());
        }
        compound.setTag("gasvein", gasVeinsList);

        final Map<Integer, Map<UUID, List<Point>>> map = RelocatorNetwork.instance.getWorldDataPoints();
        NBTTagCompound relocatorTag = new NBTTagCompound();
        NBTTagList worldListTag = new NBTTagList();

        for (Map.Entry<Integer, Map<UUID, List<Point>>> worldEntry : map.entrySet()) {
            NBTTagCompound worldTag = new NBTTagCompound();
            worldTag.setInteger("id", worldEntry.getKey());
            NBTTagList uuidListTag = new NBTTagList();

            for (Map.Entry<UUID, List<Point>> uuidEntry : worldEntry.getValue().entrySet()) {
                NBTTagCompound uuidTag = new NBTTagCompound();
                uuidTag.setUniqueId("uuid", uuidEntry.getKey());

                NBTTagList pointsListTag = new NBTTagList();
                for (Point point : uuidEntry.getValue()) {
                    pointsListTag.appendTag(point.writeToNBT(new NBTTagCompound()));
                }

                uuidTag.setTag("listPoint", pointsListTag);
                uuidListTag.appendTag(uuidTag);
            }

            worldTag.setTag("listUUID", uuidListTag);
            worldListTag.appendTag(worldTag);
        }

        relocatorTag.setTag("worldUUID", worldListTag);
        final Map<UUID, Map<String, List<String>>> mapData = uuidGuideMap;
        if (!mapData.isEmpty()){
            NBTTagCompound data = new NBTTagCompound();
            NBTTagList list = new NBTTagList();
            for (Map.Entry<UUID, Map<String, List<String>>> entry : mapData.entrySet()){
                NBTTagCompound data1 = new NBTTagCompound();
                data1.setUniqueId("uuid",entry.getKey());
                NBTTagList list1 = new NBTTagList();
                Map<String, List<String>> mapQuest = entry.getValue();
                for (Map.Entry<String, List<String>> quest : mapQuest.entrySet()){
                    NBTTagCompound data2 = new NBTTagCompound();
                    data2.setString("tab",quest.getKey());
                    NBTTagList list2 = new NBTTagList();
                    quest.getValue().forEach(name -> list2.appendTag(new NBTTagString(name)));
                    data2.setTag("list",list2);
                    list1.appendTag(data2);
                }
                data1.setTag("list",list1);
                list.appendTag(data1);
            }
            data.setTag("list",list);
            compound.setTag("guide_book", data);
        }
        compound.setTag("relocator", relocatorTag);


        this.tagCompound = compound;
        return compound;
    }

}
