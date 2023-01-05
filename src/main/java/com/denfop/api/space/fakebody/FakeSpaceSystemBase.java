package com.denfop.api.space.fakebody;

import com.denfop.api.space.IAsteroid;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.IPlanet;
import com.denfop.api.space.ISatellite;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.IResearchTable;
import com.denfop.api.space.rovers.EnumTypeUpgrade;
import com.denfop.api.space.rovers.IRovers;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import ic2.api.item.ElectricItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FakeSpaceSystemBase implements IFakeSpaceSystemBase {

    private final List<FakePlanet> fakePlanetList;
    private final List<FakeSatellite> fakeSatelliteList;
    private final Map<FakePlanet, List<IBaseResource>> fakePlanetListMap;
    private final Map<FakeSatellite, List<IBaseResource>> fakeSatelliteListMap;
    private final Map<FakePlayer, List<IFakeBody>> entityPlayerListMap;
    private final Map<FakePlayer, IResearchTable> MapEntityPlayer;
    private final List<FakePlayer> fakePlayerList;
    private final Map<FakePlayer, List<SpaceOperation>> fakePlayerMapMap;
    private final Map<FakePlayer, Map<IBody, SpaceOperation>> fakeBodyMap;
    private final List<FakeAsteroid> fakeAsteroids;
    private final Map<FakeAsteroid, List<IBaseResource>> fakeAsteroidListMap;

    private final Random rand;

    public FakeSpaceSystemBase() {
        MinecraftForge.EVENT_BUS.register(new EventHandlerPlanet());
        this.fakePlanetList = new ArrayList<>();
        this.fakeSatelliteList = new ArrayList<>();
        this.rand = new Random();
        this.fakePlanetListMap = new HashMap<>();
        this.fakeSatelliteListMap = new HashMap<>();
        this.entityPlayerListMap = new HashMap<>();
        this.MapEntityPlayer = new HashMap<>();
        this.fakePlayerList = new ArrayList<>();
        this.fakePlayerMapMap = new HashMap<>();
        this.fakeBodyMap = new HashMap<>();
        this.fakeAsteroids = new ArrayList<>();
        this.fakeAsteroidListMap = new HashMap<>();
    }

    @Override
    public Map<FakePlanet, List<IBaseResource>> getFakePlanetListMap() {
        return this.fakePlanetListMap;
    }

    @Override
    public void loadSpaceOperation(final List<SpaceOperation> map, final FakePlayer player) {
        this.fakePlayerMapMap.put(player, map);
        Map<IBody, SpaceOperation> map1 = SpaceNet.instance.getFakeSpaceSystem().getSpaceTable(player);
        if (map1.isEmpty()) {
            map1 = new HashMap<>();
        }
        for (SpaceOperation operation : map) {
            map1.put(operation.getBody(), operation);
        }

    }

    @Override
    public Map<FakeSatellite, List<IBaseResource>> getFakeSatelliteListMap() {
        return this.fakeSatelliteListMap;
    }

    @Override
    public Map<FakeAsteroid, List<IBaseResource>> getFakeFakeAsteroidListMap() {
        return this.fakeAsteroidListMap;
    }

    @Override
    public List<FakeAsteroid> getFakeAsteroidList() {
        return this.fakeAsteroids;
    }

    @Override
    public List<SpaceOperation> getSpaceOperationMap(FakePlayer player) {
        if (this.fakePlayerMapMap.containsKey(player)) {
            return this.fakePlayerMapMap.get(player);
        } else {
            List<SpaceOperation> map = new ArrayList<>();
            this.fakePlayerMapMap.put(player, map);
            return map;
        }
    }

    @Override
    public Map<IBody, SpaceOperation> getSpaceTable(final FakePlayer player) {
        return this.fakeBodyMap.get(player);
    }

    @Override
    public void addFakePlayer(final FakePlayer player) {
        this.fakePlayerList.add(player);
    }

    @Override
    public List<FakePlayer> getFakePlayers() {
        return this.fakePlayerList;
    }

    @Override
    public Map<FakePlayer, IResearchTable> getResearchTableMap() {
        return this.MapEntityPlayer;
    }

    @Override
    public Map<FakePlayer, List<IFakeBody>> getBodyMap() {
        return this.entityPlayerListMap;
    }

    @Override
    public void loadFakeBody(final IFakeBody body, final List<IBaseResource> resourceList, final FakePlayer player) {
        if (!this.entityPlayerListMap.containsKey(player)) {
            this.entityPlayerListMap.put(player, Collections.singletonList(body));
        } else {
            List<IFakeBody> list = this.entityPlayerListMap.get(player);
            list.add(body);
        }
        if (body instanceof IFakePlanet) {
            if (!this.fakePlanetList.contains((FakePlanet) body)) {
                this.fakePlanetList.add((FakePlanet) body);
                this.fakePlanetListMap.put((FakePlanet) body, resourceList);
            }
        } else if (body instanceof IFakeSatellite) {
            if (!this.fakeSatelliteList.contains((FakeSatellite) body)) {
                this.fakeSatelliteList.add((FakeSatellite) body);
                this.fakeSatelliteListMap.put((FakeSatellite) body, resourceList);
            }
        } else if (body instanceof IFakeAsteroid) {
            if (!this.fakeAsteroids.contains((FakeAsteroid) body)) {
                this.fakeAsteroids.add((FakeAsteroid) body);
                this.fakeAsteroidListMap.put((FakeAsteroid) body, resourceList);
            }
        }
    }

    @Override
    public void addFakeBodyFromPlayer(final IResearchTable table, final IFakeBody body) {

        if (!this.entityPlayerListMap.containsKey(table.getPlayer())) {
            this.entityPlayerListMap.put(table.getPlayer(), Collections.singletonList(body));
        } else {
            List<IFakeBody> list = this.entityPlayerListMap.get(table.getPlayer());
            list.add(body);
        }
    }


    @Override
    public void removeFakeBodyFromPlayer(final IResearchTable table, final IBody body) {
        List<IFakeBody> list = this.entityPlayerListMap.get(table.getPlayer());
        list.removeIf(body1 -> body1.matched(body));
    }


    @Override
    public List<FakePlanet> getFakePlanetList() {
        return this.fakePlanetList;
    }

    @Override
    public void working() {
        for (FakePlanet fakePlanet : this.fakePlanetList) {
            if (ElectricItem.manager.getCharge(fakePlanet.getRover().getItemStack()) >= 100) {

                fakePlanet.setTime(1);
                ElectricItem.manager.use(fakePlanet.getRover().getItemStack(), 100, null);
                if (fakePlanet.getTime() % 150 == 0) {
                    if (!this.fakePlanetListMap.containsKey(fakePlanet)) {
                        this.fakePlanetListMap.put(fakePlanet, new ArrayList<>());
                    } else {
                        int solar = 0;
                        if (SpaceUpgradeSystem.system.hasModules(
                                EnumTypeUpgrade.SOLAR,
                                fakePlanet.getRover().getItemStack()
                        )) {
                            solar = SpaceUpgradeSystem.system.getModules(
                                    EnumTypeUpgrade.HEATER,
                                    fakePlanet.getRover().getItemStack()
                            ).number;
                        }
                        if (solar != 0) {
                            ElectricItem.manager.charge(fakePlanet.getRover().getItemStack(), 30 * solar, Integer.MAX_VALUE, true,
                                    false
                            );
                        }
                        List<IBaseResource> resourceList = this.fakePlanetListMap.get(fakePlanet);
                        resourceList.add(this.getBaseResource(fakePlanet, (int) fakePlanet.getData().getPercent()));
                        if (fakePlanet.getTime() == 0) {
                            fakePlanet.setEnd();
                            if (this.MapEntityPlayer.containsKey(fakePlanet.getPlayer())) {
                                final IResearchTable table = this.MapEntityPlayer.get(fakePlanet.getPlayer());
                                if (table.getContainerBlock() != null) {
                                    for (IBaseResource resource : resourceList) {
                                        table.getContainerBlock().getSlotOutput().add(resource.getItemStack());
                                    }
                                    if (!table.getSpaceBody().get(fakePlanet.getPlanet()).getAuto()) {
                                        table.getContainerBlock().getSlotOutput().add(fakePlanet.getRover().getItemStack());
                                    }
                                    this.fakePlanetListMap.remove(fakePlanet);
                                    this.fakePlanetList.remove(fakePlanet);
                                    fakePlanet.getData().addInformation();
                                    table.getSpaceBody().get(fakePlanet.getPlanet()).setOperation(EnumOperation.SUCCESS);
                                    removeFakeBodyFromPlayer(table, fakePlanet.getPlanet());

                                    if (table.getSpaceBody().get(fakePlanet.getPlanet()).getAuto()) {
                                        SpaceNet.instance.getResearchSystem().sendingOperation(fakePlanet.getRover(),
                                                fakePlanet.getPlanet(), table
                                        );
                                    }
                                    fakePlanet.remove();
                                }
                            }
                        }
                    }
                }
            } else {
                fakePlanet.setEnd();
                if (this.MapEntityPlayer.containsKey(fakePlanet.getPlayer())) {
                    final IResearchTable table = this.MapEntityPlayer.get(fakePlanet.getPlayer());
                    if (table.getContainerBlock() != null) {
                        this.fakePlanetListMap.remove(fakePlanet);
                        this.fakePlanetList.remove(fakePlanet);
                        fakePlanet.getData().addInformation(Math.abs(fakePlanet.getTime() - 1800) / 1800);
                        table.getSpaceBody().get(fakePlanet.getPlanet()).setOperation(EnumOperation.FAIL);
                        removeFakeBodyFromPlayer(table, fakePlanet.getPlanet());
                        fakePlanet.remove();
                    }
                }
            }
        }
        for (FakeSatellite fakeSatellite : this.fakeSatelliteList) {
            if (ElectricItem.manager.getCharge(fakeSatellite.getRover().getItemStack()) >= 100) {
                ElectricItem.manager.use(fakeSatellite.getRover().getItemStack(), 100, null);
                fakeSatellite.setTime(1);
                int solar = 0;
                if (SpaceUpgradeSystem.system.hasModules(
                        EnumTypeUpgrade.SOLAR,
                        fakeSatellite.getRover().getItemStack()
                )) {
                    solar = SpaceUpgradeSystem.system.getModules(
                            EnumTypeUpgrade.HEATER,
                            fakeSatellite.getRover().getItemStack()
                    ).number;
                }
                if (solar != 0) {
                    ElectricItem.manager.charge(fakeSatellite.getRover().getItemStack(), 30 * solar, Integer.MAX_VALUE, true,
                            false
                    );
                }
                if (fakeSatellite.getTime() % 150 == 0) {
                    if (!this.fakeSatelliteListMap.containsKey(fakeSatellite)) {
                        this.fakeSatelliteListMap.put(fakeSatellite, new ArrayList<>());
                    } else {

                        List<IBaseResource> resourceList = this.fakeSatelliteListMap.get(fakeSatellite);
                        resourceList.add(this.getBaseResource(fakeSatellite, (int) fakeSatellite.getData().getPercent()));
                        if (fakeSatellite.getTime() == 0) {
                            fakeSatellite.setEnd();
                            if (this.MapEntityPlayer.containsKey(fakeSatellite.getPlayer())) {
                                final IResearchTable table = this.MapEntityPlayer.get(fakeSatellite.getPlayer());
                                if (table.getContainerBlock() != null) {
                                    for (IBaseResource resource : resourceList) {
                                        table.getContainerBlock().getSlotOutput().add(resource.getItemStack());
                                    }
                                    if (!table.getSpaceBody().get(fakeSatellite.getSatellite()).getAuto()) {
                                        table.getContainerBlock().getSlotOutput().add(fakeSatellite.getRover().getItemStack());
                                    }
                                    this.fakeSatelliteListMap.remove(fakeSatellite);
                                    this.fakeSatelliteList.remove(fakeSatellite);
                                    fakeSatellite.getData().addInformation();
                                    table.getSpaceBody().get(fakeSatellite.getSatellite()).setOperation(EnumOperation.SUCCESS);
                                    removeFakeBodyFromPlayer(table, fakeSatellite.getSatellite());
                                    if (table.getSpaceBody().get(fakeSatellite.getSatellite()).getAuto()) {
                                        SpaceNet.instance.getResearchSystem().sendingOperation(fakeSatellite.getRover(),
                                                fakeSatellite.getSatellite(), table
                                        );
                                    }
                                    fakeSatellite.remove();
                                }
                            }
                        }
                    }
                }

            } else {
                fakeSatellite.setEnd();
                if (this.MapEntityPlayer.containsKey(fakeSatellite.getPlayer())) {
                    final IResearchTable table = this.MapEntityPlayer.get(fakeSatellite.getPlayer());
                    if (table.getContainerBlock() != null) {
                        this.fakeSatelliteListMap.remove(fakeSatellite);
                        this.fakeSatelliteList.remove(fakeSatellite);
                        fakeSatellite.getData().addInformation(Math.abs(fakeSatellite.getTime() - 1800) / 1800);
                        table.getSpaceBody().get(fakeSatellite.getSatellite()).setOperation(EnumOperation.FAIL);
                        removeFakeBodyFromPlayer(table, fakeSatellite.getSatellite());
                        fakeSatellite.remove();
                    }
                }
            }
        }
        //
        for (FakeAsteroid fakePlanet : this.fakeAsteroids) {
            if (ElectricItem.manager.getCharge(fakePlanet.getRover().getItemStack()) >= 100) {

                fakePlanet.setTime(1);
                ElectricItem.manager.use(fakePlanet.getRover().getItemStack(), 100, null);
                if (fakePlanet.getTime() % 150 == 0) {
                    if (!this.fakeAsteroidListMap.containsKey(fakePlanet)) {
                        this.fakeAsteroidListMap.put(fakePlanet, new ArrayList<>());
                    } else {
                        int solar = 0;
                        if (SpaceUpgradeSystem.system.hasModules(
                                EnumTypeUpgrade.SOLAR,
                                fakePlanet.getRover().getItemStack()
                        )) {
                            solar = SpaceUpgradeSystem.system.getModules(
                                    EnumTypeUpgrade.HEATER,
                                    fakePlanet.getRover().getItemStack()
                            ).number;
                        }
                        if (solar != 0) {
                            ElectricItem.manager.charge(fakePlanet.getRover().getItemStack(), 30 * solar, Integer.MAX_VALUE, true,
                                    false
                            );
                        }
                        List<IBaseResource> resourceList = this.fakeAsteroidListMap.get(fakePlanet);
                        resourceList.add(this.getBaseResource(fakePlanet, (int) fakePlanet.getData().getPercent()));
                        if (fakePlanet.getTime() == 0) {
                            fakePlanet.setEnd();
                            if (this.MapEntityPlayer.containsKey(fakePlanet.getPlayer())) {
                                final IResearchTable table = this.MapEntityPlayer.get(fakePlanet.getPlayer());
                                if (table.getContainerBlock() != null) {
                                    for (IBaseResource resource : resourceList) {
                                        table.getContainerBlock().getSlotOutput().add(resource.getItemStack());
                                    }
                                    if (!table.getSpaceBody().get(fakePlanet.getAsteroid()).getAuto()) {
                                        table.getContainerBlock().getSlotOutput().add(fakePlanet.getRover().getItemStack());
                                    }
                                    this.fakeAsteroidListMap.remove(fakePlanet);
                                    this.fakeAsteroids.remove(fakePlanet);
                                    fakePlanet.getData().addInformation();
                                    table.getSpaceBody().get(fakePlanet.getAsteroid()).setOperation(EnumOperation.SUCCESS);
                                    removeFakeBodyFromPlayer(table, fakePlanet.getAsteroid());

                                    if (table.getSpaceBody().get(fakePlanet.getAsteroid()).getAuto()) {
                                        SpaceNet.instance.getResearchSystem().sendingOperation(fakePlanet.getRover(),
                                                fakePlanet.getAsteroid(), table
                                        );
                                    }
                                    fakePlanet.remove();
                                }
                            }
                        }
                    }
                }
            } else {
                fakePlanet.setEnd();
                if (this.MapEntityPlayer.containsKey(fakePlanet.getPlayer())) {
                    final IResearchTable table = this.MapEntityPlayer.get(fakePlanet.getPlayer());
                    if (table.getContainerBlock() != null) {
                        this.fakeAsteroidListMap.remove(fakePlanet);
                        this.fakeAsteroids.remove(fakePlanet);
                        fakePlanet.getData().addInformation(Math.abs(fakePlanet.getTime() - 1800) / 1800);
                        table.getSpaceBody().get(fakePlanet.getAsteroid()).setOperation(EnumOperation.FAIL);
                        removeFakeBodyFromPlayer(table, fakePlanet.getAsteroid());
                        fakePlanet.remove();
                    }
                }
            }
        }
    }


    @Override
    public List<FakeSatellite> getFakeSatelliteList() {
        return this.fakeSatelliteList;
    }

    @Override
    public void addFakeSatellite(final FakeSatellite satellite) {
        this.fakeSatelliteList.add(satellite);

    }

    @Override
    public boolean cadAddFakeSatellite(final FakeSatellite satellite) {
        if (!this.fakeSatelliteList.contains(satellite)) {
            if (satellite.getSatellite().getPressure()) {
                if (!SpaceUpgradeSystem.system.hasModules(
                        EnumTypeUpgrade.PRESSURE,
                        satellite.getRover().getItemStack()
                )) {
                    return false;
                }
            }
            int temperature = satellite.getSatellite().getTemperature();
            int[] temperature_mod = getLimitTemperatureFromRovers(satellite.getRover());
            return temperature < temperature_mod[0] && temperature > temperature_mod[1];
        } else {
            return false;
        }
    }

    @Override
    public boolean canAddFakeAsteroid(final FakeAsteroid asteroid) {
        if (!this.fakeAsteroids.contains(asteroid)) {
            int temperature = asteroid.getAsteroid().getTemperature();
            int[] temperature_mod = getLimitTemperatureFromRovers(asteroid.getRover());
            if (!(temperature < temperature_mod[0] && temperature > temperature_mod[1])) {
                return false;
            }
            double distance = asteroid.getAsteroid().getDistanceFromStar();
            double distance_min = 2;
            double coef = 1;
            if (SpaceUpgradeSystem.system.getModules(
                    EnumTypeUpgrade.ENGINE,
                    asteroid.getRover().getItemStack()
            ) != null) {
                coef = SpaceUpgradeSystem.system.getModules(
                        EnumTypeUpgrade.ENGINE,
                        asteroid.getRover().getItemStack()
                ).number;
            }
            if (distance < 2) {
                distance_min /= (1 + coef);
                return distance > distance_min;
            } else {
                distance_min *= (1 + coef);
                return distance < distance_min;
            }
        } else {
            return false;
        }
    }

    @Override
    public void addFakePlanet(final FakePlanet planet) {
        this.fakePlanetList.add(planet);
    }

    @Override
    public void addFakeAsteroid(final FakeAsteroid asteroid) {
        this.fakeAsteroids.add(asteroid);
    }

    @Override
    public void loadFakePlanet(final FakePlanet planet) {
        this.fakePlanetList.add(planet);
    }

    @Override
    public void loadFakeSatellite(final FakeSatellite satellite) {
        this.fakeSatelliteList.add(satellite);
    }

    @Override
    public void loadFakeAsteroid(final FakeAsteroid asteroid) {
        this.fakeAsteroids.add(asteroid);
    }

    @Override
    public void loadDataFromPlayer(final FakePlayer player) {
        if (!this.fakePlayerList.contains(player)) {
            this.addFakePlayer(player);
            final NBTTagCompound tag = player.getTag().getCompoundTag("space_iu");
            for (IPlanet body : SpaceNet.instance.getPlanetList()) {
                if (tag.hasKey(body.getName())) {
                    this.addFakePlanet(new FakePlanet(player, body.getName()));
                }
            }
            for (ISatellite body : SpaceNet.instance.getSatelliteList()) {
                if (tag.hasKey(body.getName())) {
                    this.addFakeSatellite(new FakeSatellite(player, body.getName()));
                }
            }
            for (IAsteroid body : SpaceNet.instance.getAsteroidList()) {
                if (tag.hasKey(body.getName())) {
                    this.addFakeAsteroid(new FakeAsteroid(player, body.getName()));
                }
            }
        }
    }

    @Override
    public void unloadDataFromPlayer(final FakePlayer player) {
        for (FakePlanet planet : this.fakePlanetList) {
            planet.writeNBT(this.fakePlanetListMap.get(planet));
        }
        for (FakeSatellite satellite : this.fakeSatelliteList) {
            satellite.writeNBT(fakeSatelliteListMap.get(satellite));
        }
    }

    @Override
    public int[] getLimitTemperatureFromRovers(final IRovers rover) {
        int max_temperature = 100;
        int min_temperature = -50;
        int cooler_mod = 0;
        int heater_mod = 0;
        if (SpaceUpgradeSystem.system.getModules(
                EnumTypeUpgrade.COOLER,
                rover.getItemStack()
        ) != null) {
            cooler_mod = SpaceUpgradeSystem.system.getModules(
                    EnumTypeUpgrade.COOLER,
                    rover.getItemStack()
            ).number;
        }
        if (SpaceUpgradeSystem.system.getModules(
                EnumTypeUpgrade.HEATER,
                rover.getItemStack()
        ) != null) {
            heater_mod = SpaceUpgradeSystem.system.getModules(
                    EnumTypeUpgrade.HEATER,
                    rover.getItemStack()
            ).number;
        }
        int[] temperatures = new int[2];
        max_temperature += 100 * heater_mod;
        min_temperature -= 75 * cooler_mod;
        temperatures[0] = max_temperature;
        temperatures[1] = min_temperature;
        return temperatures;
    }

    @Override
    public boolean cadAddFakePlanet(final FakePlanet planet) {
        if (!this.fakePlanetList.contains(planet)) {
            if (planet.getPlanet().getPressure()) {
                if (!SpaceUpgradeSystem.system.hasModules(
                        EnumTypeUpgrade.PRESSURE,
                        planet.getRover().getItemStack()
                )) {
                    return false;
                }
            }
            int temperature = planet.getPlanet().getTemperature();
            int[] temperature_mod = getLimitTemperatureFromRovers(planet.getRover());
            if (!(temperature < temperature_mod[0] && temperature > temperature_mod[1])) {
                return false;
            }
            double distance = planet.getPlanet().getDistanceFromStar();
            double distance_min = 2;
            double coef = 1;
            if (SpaceUpgradeSystem.system.getModules(
                    EnumTypeUpgrade.ENGINE,
                    planet.getRover().getItemStack()
            ) != null) {
                coef = SpaceUpgradeSystem.system.getModules(
                        EnumTypeUpgrade.ENGINE,
                        planet.getRover().getItemStack()
                ).number;
            }
            if (distance < 2) {
                distance_min /= (1 + coef);
                return distance > distance_min;
            } else {
                distance_min *= (1 + coef);
                return distance < distance_min;
            }
        } else {
            return false;
        }

    }

    @Override
    public IRovers getRoversFromPlanet(final FakePlanet planet) {
        return planet.getRover();
    }

    @Override
    public IRovers getRoversFromSatellite(final FakeSatellite satellite) {
        return satellite.getRover();
    }

    @Override
    public IRovers getRoversFromAsteroid(final FakeAsteroid asteroid) {
        return asteroid.getRover();
    }

    @Override
    public IBaseResource getBaseResource(final FakePlanet planet, final int percent) {
        List<IBaseResource> list = planet.getPlanet().getResources();
        List<IBaseResource> newlist = new ArrayList<>();
        for (IBaseResource resource : list) {
            if (resource.getChance() <= percent) {
                if (resource.getChance() > rand.nextInt(resource.getMaxChance())) {
                    newlist.add(resource);
                }
            }
        }
        int chance = this.rand.nextInt(newlist.size());
        return newlist.get(chance);
    }

    @Override
    public IBaseResource getBaseResource(final FakeSatellite satellite, final int percent) {
        List<IBaseResource> list = satellite.getSatellite().getResources();
        List<IBaseResource> newlist = new ArrayList<>();
        for (IBaseResource resource : list) {
            if (resource.getChance() <= percent) {
                if (resource.getChance() > rand.nextInt(resource.getMaxChance())) {
                    newlist.add(resource);
                }
            }
        }
        int chance = this.rand.nextInt(newlist.size());
        return newlist.get(chance);
    }

    @Override
    public IBaseResource getBaseResource(final FakeAsteroid planet, final int percent) {
        List<IBaseResource> list = planet.getAsteroid().getResources();
        List<IBaseResource> newlist = new ArrayList<>();
        for (IBaseResource resource : list) {
            if (resource.getChance() <= percent) {
                if (resource.getChance() > rand.nextInt(resource.getMaxChance())) {
                    newlist.add(resource);
                }
            }
        }
        int chance = this.rand.nextInt(newlist.size());
        return newlist.get(chance);
    }

    @Override
    public void unload() {
        this.fakePlanetList.clear();
        this.fakeSatelliteList.clear();
        this.fakePlanetListMap.clear();
        this.fakeSatelliteListMap.clear();
        this.entityPlayerListMap.clear();
        this.MapEntityPlayer.clear();
        this.fakePlayerList.clear();
        this.fakePlayerMapMap.clear();
        this.fakeBodyMap.clear();
        this.fakeAsteroids.clear();
        this.fakeAsteroidListMap.clear();
    }


}
