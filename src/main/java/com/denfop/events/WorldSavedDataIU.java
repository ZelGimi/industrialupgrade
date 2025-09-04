package com.denfop.events;

import com.denfop.IUCore;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.radiation.Radiation;
import com.denfop.api.pollution.radiation.RadiationSystem;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.fakebody.*;
import com.denfop.api.vein.common.VeinBase;
import com.denfop.api.vein.common.VeinSystem;
import com.denfop.api.vein.gas.GasVeinBase;
import com.denfop.api.vein.gas.GasVeinSystem;
import com.denfop.blockentity.quarry_earth.BlockEntityEarthQuarryController;
import com.denfop.items.relocator.Point;
import com.denfop.items.relocator.RelocatorNetwork;
import com.denfop.render.streak.PlayerStreakInfo;
import com.denfop.world.GenData;
import com.denfop.world.WorldGenGas;
import com.denfop.world.vein.noise.ShellCluster;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.*;

import static com.denfop.api.guidebook.GuideBookCore.uuidGuideMap;
import static com.denfop.world.vein.AlgorithmVein.*;

public class WorldSavedDataIU extends SavedData {

    public int col;
    Level world;
    private CompoundTag tagCompound = new CompoundTag();

    public WorldSavedDataIU() {
        this(new CompoundTag());

    }

    public WorldSavedDataIU(String name) {
        super();
    }

    public WorldSavedDataIU(@Nonnull CompoundTag compound) {

        if (shellClusterChuncks != null)
            shellClusterChuncks.clear();
        if (shellClusterChuncks == null)
            shellClusterChuncks = new HashMap<>();

        if (volcano != null)
            volcano = null;
        if (compound.contains("shells")) {
            loadShellClusterChunks(compound.getCompound("shells"));
        }

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

        BlockEntityEarthQuarryController.chunkPos.clear();
        if (compound.contains("earth_quarry")) {
            ListTag earthQuarryList = compound.getList("earth_quarry", 10);
            for (int i = 0; i < earthQuarryList.size(); i++) {
                CompoundTag chunkTag = earthQuarryList.getCompound(i);
                int x = chunkTag.getInt("x");
                int z = chunkTag.getInt("z");
                BlockEntityEarthQuarryController.chunkPos.add(new ChunkPos(x, z));
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
                ResourceKey<Level> resourceKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(tag9.getString("id")));
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
        Map<UUID, Map<String, List<String>>> mapData = new HashMap<>();
        uuidGuideMap.clear();
        if (compound.contains("guide_book")) {
            CompoundTag data = compound.getCompound("guide_book");
            ListTag list = data.getList("list", 10);

            for (int i = 0; i < list.size(); i++) {
                CompoundTag data1 = list.getCompound(i);
                UUID uuid = data1.getUUID("uuid");

                Map<String, List<String>> mapQuest = new HashMap<>();
                ListTag list1 = data1.getList("list", 10);

                for (int j = 0; j < list1.size(); j++) {
                    CompoundTag data2 = list1.getCompound(j);
                    String tab = data2.getString("tab");

                    ListTag list2 = data2.getList("list", 8);
                    List<String> names = new ArrayList<>();

                    for (int k = 0; k < list2.size(); k++) {
                        names.add(list2.getString(k));
                    }

                    mapQuest.put(tab, names);
                }

                mapData.put(uuid, mapQuest);
            }
        }
        uuidGuideMap = mapData;
    }

    public static void loadShellClusterChunks(CompoundTag tag) {
        Map<Integer, Map<Integer, Tuple<Color, Integer>>> result = new HashMap<>();
        ShellCluster cluster = new ShellCluster();
        cluster.point = new com.denfop.world.vein.noise.Point(tag.getCompound("volcano").getInt("x"), tag.getCompound("volcano").getInt("z"));
        volcano = cluster;
        ListTag outerList = tag.getList("shellClusterChunks", 10);

        for (int i = 0; i < outerList.size(); i++) {
            CompoundTag outerTag = outerList.getCompound(i);
            int outerKey = outerTag.getInt("outer");

            Map<Integer, Tuple<Color, Integer>> innerMap = new HashMap<>();
            ListTag innerList = outerTag.getList("innerList", 10);

            for (int j = 0; j < innerList.size(); j++) {
                CompoundTag innerTag = innerList.getCompound(j);
                int innerKey = innerTag.getInt("inner");

                int r = innerTag.getInt("r");
                int g = innerTag.getInt("g");
                int b = innerTag.getInt("b");
                int a = innerTag.getInt("a");
                int value = innerTag.getInt("value");

                Color color = new Color(r, g, b, a);
                innerMap.put(innerKey, new Tuple<>(color, value));
            }

            result.put(outerKey, innerMap);
        }
        shellClusterChuncks = result;
        Map<Integer, Map<Integer, List<Integer>>> veinMap = new HashMap<>();
        ListTag outerVeinList = tag.getList("veinCoordination", 10);

        for (int i = 0; i < outerVeinList.size(); i++) {
            CompoundTag outerTag = outerVeinList.getCompound(i);
            int outerKey = outerTag.getInt("outer");

            Map<Integer, List<Integer>> innerMap = new HashMap<>();
            ListTag innerList = outerTag.getList("innerList", 10);

            for (int j = 0; j < innerList.size(); j++) {
                CompoundTag innerTag = innerList.getCompound(j);
                int innerKey = innerTag.getInt("inner");

                ListTag coordsList = innerTag.getList("coords", 3);
                List<Integer> coords = new ArrayList<>();
                for (int k = 0; k < coordsList.size(); k++) {
                    coords.add(coordsList.getInt(k));
                }

                innerMap.put(innerKey, coords);
            }

            veinMap.put(outerKey, innerMap);
        }

        veinCoordination = veinMap;
    }

    public static CompoundTag saveShellClusterChunks() {
        CompoundTag tag = new CompoundTag();
        ListTag outerList = new ListTag();
        CompoundTag volcanoTag = new CompoundTag();
        if (volcano != null) {
            volcanoTag.putInt("x", volcano.point.x);
            volcanoTag.putInt("z", volcano.point.y);
            tag.put("volcano", volcanoTag);
        }
        for (Map.Entry<Integer, Map<Integer, Tuple<Color, Integer>>> outer : shellClusterChuncks.entrySet()) {
            int outerKey = outer.getKey();
            CompoundTag outerTag = new CompoundTag();
            outerTag.putInt("outer", outerKey);

            ListTag innerList = new ListTag();
            for (Map.Entry<Integer, Tuple<Color, Integer>> inner : outer.getValue().entrySet()) {
                int innerKey = inner.getKey();
                Tuple<Color, Integer> tuple = inner.getValue();
                Color color = tuple.getA();
                int number = tuple.getB();

                CompoundTag innerTag = new CompoundTag();
                innerTag.putInt("inner", innerKey);
                innerTag.putInt("r", color.getRed());
                innerTag.putInt("g", color.getGreen());
                innerTag.putInt("b", color.getBlue());
                innerTag.putInt("a", color.getAlpha());
                innerTag.putInt("value", number);

                innerList.add(innerTag);
            }

            outerTag.put("innerList", innerList);
            outerList.add(outerTag);
        }

        tag.put("shellClusterChunks", outerList);
        ListTag veinOuterList = new ListTag();
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> outer : veinCoordination.entrySet()) {
            int outerKey = outer.getKey();
            CompoundTag outerTag = new CompoundTag();
            outerTag.putInt("outer", outerKey);

            ListTag innerList = new ListTag();
            for (Map.Entry<Integer, List<Integer>> inner : outer.getValue().entrySet()) {
                int innerKey = inner.getKey();
                List<Integer> coords = inner.getValue();

                CompoundTag innerTag = new CompoundTag();
                innerTag.putInt("inner", innerKey);

                ListTag coordsList = new ListTag();
                for (Integer coord : coords) {
                    coordsList.add(IntTag.valueOf(coord));
                }

                innerTag.put("coords", coordsList);
                innerList.add(innerTag);
            }

            outerTag.put("innerList", innerList);
            veinOuterList.add(outerTag);
        }

        tag.put("veinCoordination", veinOuterList);
        return tag;
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
        for (VeinBase vein : VeinSystem.system.getVeinsList()) {
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
        for (ChunkPos chunkPos : BlockEntityEarthQuarryController.chunkPos) {
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
        for (GasVeinBase gasVein : GasVeinSystem.system.getVeinsList()) {
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
        final Map<UUID, Map<String, List<String>>> mapData = uuidGuideMap;
        if (!mapData.isEmpty()) {
            CompoundTag data = new CompoundTag();
            ListTag list = new ListTag();
            for (Map.Entry<UUID, Map<String, List<String>>> entry : mapData.entrySet()) {
                CompoundTag data1 = new CompoundTag();
                data1.putUUID("uuid", entry.getKey());
                ListTag list1 = new ListTag();
                Map<String, List<String>> mapQuest = entry.getValue();
                for (Map.Entry<String, List<String>> quest : mapQuest.entrySet()) {
                    CompoundTag data2 = new CompoundTag();
                    data2.putString("tab", quest.getKey());
                    ListTag list2 = new ListTag();
                    quest.getValue().forEach(name -> list2.add(StringTag.valueOf(name)));
                    data2.put("list", list2);
                    list1.add(data2);
                }
                data1.put("list", list1);
                list.add(data1);
            }
            data.put("list", list);
            compound.put("guide_book", data);
        }

        compound.put("shells", saveShellClusterChunks());
        this.tagCompound = compound;
        return compound;
    }

}
