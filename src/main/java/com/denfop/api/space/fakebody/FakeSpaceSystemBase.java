package com.denfop.api.space.fakebody;

import com.denfop.ElectricItem;
import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.SpaceNet;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.rovers.api.IRovers;
import com.denfop.api.space.rovers.enums.EnumTypeUpgrade;
import com.denfop.api.space.upgrades.SpaceUpgradeSystem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.*;

public class FakeSpaceSystemBase implements IFakeSpaceSystemBase {

    private final Map<UUID, List<IFakeBody>> uuidListMap = new HashMap<>();
    private final Map<UUID, Map<IBody, Data>> dataMap = new HashMap<>();
    private final List<FakePlanet> fakePlanetList;
    private final List<FakeSatellite> fakeSatelliteList;
    private final Map<UUID, IResearchTable> MapEntityPlayer;
    private final Map<UUID, IRocketLaunchPad> rocketLaunchPadMap = new HashMap<>();
    private final List<FakeAsteroid> fakeAsteroids;
    private final Random rand;
    Map<UUID, Map<IBody, SpaceOperation>> fakeBodyMap = new HashMap<>();
    List<IFakeBody> deletingBody = new LinkedList<>();

    public FakeSpaceSystemBase() {
        MinecraftForge.EVENT_BUS.register(new EventHandlerPlanet());
        this.fakePlanetList = new LinkedList<>();
        this.fakeSatelliteList = new LinkedList<>();
        this.rand = new Random();
        this.rand.setSeed(rand.nextLong());
        this.MapEntityPlayer = new HashMap<>();
        this.fakeAsteroids = new LinkedList<>();
    }

    public Map<IBody, Data> getDataFromUUID(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, k -> new HashMap<>());
    }

    @Override
    public List<FakeAsteroid> getFakeAsteroidList() {
        return this.fakeAsteroids;
    }

    @Override
    public Map<IBody, SpaceOperation> getSpaceTable(final UUID player) {
        return this.fakeBodyMap.computeIfAbsent(player, k -> new HashMap<>());
    }

    @Override
    public Map<UUID, IRocketLaunchPad> getRocketPadMap() {
        return rocketLaunchPadMap;
    }

    @Override
    public Map<UUID, IResearchTable> getResearchTableMap() {
        return this.MapEntityPlayer;
    }

    @Override
    public Map<UUID, List<IFakeBody>> getBodyMap() {
        return this.uuidListMap;
    }

    @Override
    public List<FakePlanet> getFakePlanetList() {
        return this.fakePlanetList;
    }

    @Override
    public void working() {

        for (FakePlanet fakePlanet : this.fakePlanetList) {
            processFakeBody(fakePlanet, getBodyMap());
        }

        for (FakeSatellite fakeSatellite : this.fakeSatelliteList) {
            processFakeBody(fakeSatellite, getBodyMap());
        }
        for (FakeAsteroid fakeAsteroid : this.fakeAsteroids) {
            processFakeBody(fakeAsteroid, getBodyMap());
        }
        for (IFakeBody fakeBody : deletingBody) {
            if (fakeBody instanceof FakePlanet) {
                processFakeBody((FakePlanet) fakeBody, fakePlanetList);
            } else if (fakeBody instanceof FakeSatellite) {
                processFakeBody((FakeSatellite) fakeBody, fakeSatelliteList);
            } else if (fakeBody instanceof FakeAsteroid) {
                processFakeBody((FakeAsteroid) fakeBody, fakeAsteroids);
            }
        }
        deletingBody.clear();
    }

    private <T extends IFakeBody> void processFakeBody(T fakeBody, List<T> bodyList) {
        bodyList.remove(fakeBody);
        if (fakeBody.getSpaceOperation().getOperation() == EnumOperation.SUCCESS) {
            IRocketLaunchPad rocketLaunchPad = rocketLaunchPadMap.get(fakeBody.getPlayer());
            IResearchTable researchTable = getResearchTableMap().get(fakeBody.getPlayer());
            if (rocketLaunchPad != null) {

                handleSuccessfulOperation(fakeBody, rocketLaunchPad, researchTable);
            }
        }
    }

    private <T extends IFakeBody> void handleSuccessfulOperation(
            T fakeBody, IRocketLaunchPad rocketLaunchPad,
            IResearchTable researchTable
    ) {


        if (fakeBody.getSpaceOperation().getAuto()) {

            rocketLaunchPad.charge(fakeBody.getRover().getItemStack());
            rocketLaunchPad.refuel(
                    fakeBody.getRover().getItemStack(),
                    fakeBody.getRover().getItem()
            );

            Object target = getTarget(fakeBody);
            rocketLaunchPad.addDataRocket(fakeBody.getRover().getItemStack());
            SpaceNet.instance.getResearchSystem().sendingAutoOperation(
                    fakeBody.getRover(),
                    (IBody) target,
                    researchTable
            );
        } else {
            rocketLaunchPad.getSlotOutput().add(fakeBody.getRover().getItemStack());
        }
    }

    private Object getTarget(IFakeBody fakeBody) {
        if (fakeBody instanceof FakePlanet) {
            return ((FakePlanet) fakeBody).getPlanet();
        } else if (fakeBody instanceof FakeSatellite) {
            return ((FakeSatellite) fakeBody).getSatellite();
        } else if (fakeBody instanceof FakeAsteroid) {
            return ((FakeAsteroid) fakeBody).getAsteroid();
        }
        return null;
    }

    @Override
    public List<FakeSatellite> getFakeSatelliteList() {
        return this.fakeSatelliteList;
    }

    @Override
    public void addFakeSatellite(final FakeSatellite satellite) {
        this.fakeSatelliteList.add(satellite);

    }

    private void processFakeBody(IFakeBody fakeBody, Map<UUID, List<IFakeBody>> uuidListMap) {
        UUID playerId = fakeBody.getPlayer();


        if (fakeBody.getSpaceOperation().getOperation() == EnumOperation.SUCCESS) {
            removeFakeBody(fakeBody, playerId, uuidListMap);
            return;
        }
        if (fakeBody.getTimerFrom().getTime() == 0) {
            handleResourceReturn(fakeBody, playerId, uuidListMap);
            return;
        }

        ItemStack itemStack = fakeBody.getRover().getItemStack();
        if (ElectricItem.manager.getCharge(itemStack) < 100 ||
                fakeBody.getRover().getItem().getFluidHandler(itemStack).drain(2, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
            failOperation(fakeBody, playerId, uuidListMap);
            return;
        }


        processTimers(fakeBody);
        manageEnergy(fakeBody);

        if (!fakeBody.getTimerTo().canWork() && !fakeBody.getTimerFrom().canWork() && fakeBody.getTimerFrom().getTime() > 0) {
            collectResources(fakeBody);
            fakeBody.getTimerFrom().setCanWork(true);
            validateEnvironment(fakeBody, playerId, uuidListMap);
        } else if (fakeBody.getTimerFrom().getTime() == 0) {
            handleResourceReturn(fakeBody, playerId, uuidListMap);
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
    public int[] getLimitTemperatureFromRovers(final IRovers rover) {
        int max_temperature = 150;
        int min_temperature = -125;
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
        max_temperature += 350 * heater_mod;
        min_temperature -= 37 * cooler_mod;
        temperatures[0] = max_temperature;
        temperatures[1] = min_temperature;
        return temperatures;
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
    public List<IBaseResource> getBaseResource(final FakePlanet planet, final int percent) {
        List<IBaseResource> list = planet.getPlanet().getResources();
        List<IBaseResource> newlist = new ArrayList<>();
        for (IBaseResource resource : list) {
            if (resource.getTypeRovers() == planet.getRover().getType()) {
                if (resource.getPercentResearchBody() <= percent) {
                    if (resource.getChance() >= rand.nextInt(resource.getMaxChance())) {
                        newlist.add(resource);
                    }
                }
            }
        }
        return newlist;
    }

    @Override
    public List<IBaseResource> getBaseResource(final FakeSatellite satellite, final int percent) {
        List<IBaseResource> list = satellite.getSatellite().getResources();
        List<IBaseResource> newlist = new ArrayList<>();
        for (IBaseResource resource : list) {
            if (resource.getTypeRovers() == satellite.getRover().getType()) {
                if (resource.getPercentResearchBody() <= percent) {
                    if (resource.getChance() > rand.nextInt(resource.getMaxChance())) {
                        newlist.add(resource);
                    }
                }
            }
        }

        return newlist;
    }

    public List<IBaseResource> getBaseResource(final IFakeBody planet, final int percent) {
        if (planet instanceof IFakePlanet) {
            return getBaseResource((FakePlanet) planet, percent);
        }
        if (planet instanceof IFakeSatellite) {
            return getBaseResource((FakeSatellite) planet, percent);
        }
        if (planet instanceof IFakeAsteroid) {
            return getBaseResource((FakeAsteroid) planet, percent);
        }
        return Collections.emptyList();
    }

    @Override
    public List<IBaseResource> getBaseResource(final FakeAsteroid planet, final int percent) {
        List<IBaseResource> list = planet.getAsteroid().getResources();
        List<IBaseResource> newlist = new LinkedList<>();
        for (IBaseResource resource : list) {
            if (resource.getTypeRovers() == planet.getRover().getType()) {
                if (resource.getPercentResearchBody() <= percent) {
                    if (resource.getChance() > rand.nextInt(resource.getMaxChance())) {
                        newlist.add(resource);
                    }
                }
            }
        }
        return newlist;
    }

    @Override
    public void unload() {
        this.fakePlanetList.clear();
        this.fakeSatelliteList.clear();
        this.MapEntityPlayer.clear();
        rocketLaunchPadMap.clear();
        this.fakeBodyMap.clear();
        this.fakeAsteroids.clear();
        uuidListMap.clear();
        dataMap.clear();
    }

    @Override
    public void addDataBody(final UUID name, final Map<IBody, Data> map) {
        if (!dataMap.containsKey(name)) {
            dataMap.put(name, map);
        }
    }

    @Override
    public void copyData(final Map<IBody, Data> data, final UUID uniqueID) {

        if (dataMap.containsKey(uniqueID)) {
            final Map<IBody, Data> dataPlayer = dataMap.get(uniqueID);
            for (Map.Entry<IBody, Data> dataEntry : data.entrySet()) {
                Data data1 = dataPlayer.get(dataEntry.getKey());
                if (data1.getPercent() < dataEntry.getValue().getPercent()) {
                    data1.setInformation(dataEntry.getValue().getPercent());
                }
            }
        } else {
            dataMap.put(uniqueID, new HashMap<>(data));
        }

    }

    private void processTimers(IFakeBody fakeBody) {
        if (fakeBody.getTimerTo().canWork()) {
            fakeBody.getTimerTo().work();
        }
        if (fakeBody.getTimerFrom().canWork()) {
            fakeBody.getTimerFrom().work();
        }
    }

    private void manageEnergy(IFakeBody fakeBody) {
        ItemStack itemStack = fakeBody.getRover().getItemStack();
        ElectricItem.manager.discharge(itemStack, 100, 14, true, false, false);
        fakeBody.getRover().getItem().getFluidHandler(itemStack).drain(2, IFluidHandler.FluidAction.EXECUTE);

        int solar = SpaceUpgradeSystem.system.hasModules(EnumTypeUpgrade.SOLAR, itemStack)
                ? SpaceUpgradeSystem.system.getModules(EnumTypeUpgrade.SOLAR, itemStack).number
                : 0;

        if (solar > 0) {
            ElectricItem.manager.charge(itemStack, 30 * solar, Integer.MAX_VALUE, true, false);
        }
    }

    private void collectResources(IFakeBody fakeBody) {
        int drill = 1;
        int level = fakeBody.getRover().getItem().getLevel().ordinal() + 1;
        drill += level;
        if (SpaceUpgradeSystem.system.hasModules(EnumTypeUpgrade.DRILL, fakeBody.getRover().getItemStack())) {
            drill += SpaceUpgradeSystem.system.getModules(EnumTypeUpgrade.DRILL, fakeBody.getRover().getItemStack()).number;
        }
        for (int i = 0; i < drill; i++) {
            List<IBaseResource> baseResource = getBaseResource(fakeBody, (int) fakeBody.getData().getPercent());
            for (IBaseResource resource : baseResource) {
                if (resource != null) {
                    fakeBody.addBaseResource(resource);
                }
            }
        }
    }

    private void validateEnvironment(IFakeBody fakeBody, UUID playerId, Map<UUID, List<IFakeBody>> uuidListMap) {
        int temperature = fakeBody.getBody().getTemperature();
        int[] tempLimits = SpaceNet.instance.getFakeSpaceSystem().getLimitTemperatureFromRovers(fakeBody.getRover());

        if (temperature > tempLimits[0] || temperature < tempLimits[1]) {
            failOperation(fakeBody, playerId, uuidListMap);
        }
    }

    private void handleResourceReturn(IFakeBody fakeBody, UUID playerId, Map<UUID, List<IFakeBody>> uuidListMap) {
        IRocketLaunchPad rocketLaunchPad = getRocketPadMap().get(playerId);
        if (rocketLaunchPad != null) {
            for (IBaseResource resource : fakeBody.getResource()) {
                if (resource.getItemStack() != null) {
                    rocketLaunchPad.getSlotOutput().add(resource.getItemStack());
                }
                if (resource.getFluidStack() != null) {
                    rocketLaunchPad.addFluidStack(resource.getFluidStack());
                }
            }
            fakeBody.getSpaceOperation().setOperation(EnumOperation.SUCCESS);
            if (fakeBody.getTimerTo().getTime() == 0) {
                fakeBody.getData().addInformation(5);
            }
            removeFakeBody(fakeBody, playerId, uuidListMap);
        }
    }

    private void removeFakeBody(IFakeBody fakeBody, UUID playerId, Map<UUID, List<IFakeBody>> uuidListMap) {
        deletingBody.add(fakeBody);
        List<IFakeBody> list = uuidListMap.computeIfAbsent(playerId, k -> new LinkedList<>());
        list.remove(fakeBody);
    }

    private void failOperation(IFakeBody fakeBody, UUID playerId, Map<UUID, List<IFakeBody>> uuidListMap) {
        fakeBody.getSpaceOperation().setOperation(EnumOperation.FAIL);
        removeFakeBody(fakeBody, playerId, uuidListMap);
    }

}
