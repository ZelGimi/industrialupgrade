package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.research.IResearchTable;
import com.denfop.api.space.rovers.IRovers;

import java.util.List;
import java.util.Map;

public interface IFakeSpaceSystemBase {

    Map<IBody, SpaceOperation> getSpaceTable(FakePlayer player);

    void addFakePlayer(FakePlayer player);

    List<FakePlayer> getFakePlayers();

    Map<FakePlayer, IResearchTable> getResearchTableMap();

    Map<FakePlayer, List<IFakeBody>> getBodyMap();

    List<SpaceOperation> getSpaceOperationMap(FakePlayer player);

    Map<FakeSatellite, List<IBaseResource>> getFakeSatelliteListMap();

    Map<FakeAsteroid, List<IBaseResource>> getFakeFakeAsteroidListMap();

    List<FakeAsteroid> getFakeAsteroidList();

    void loadFakeBody(IFakeBody body, List<IBaseResource> resourceList, FakePlayer player);

    void addFakeBodyFromPlayer(IResearchTable table, IFakeBody body);

    void removeFakeBodyFromPlayer(IResearchTable table, IBody body);

    Map<FakePlanet, List<IBaseResource>> getFakePlanetListMap();

    void loadSpaceOperation(List<SpaceOperation> map, FakePlayer player);

    List<FakePlanet> getFakePlanetList();

    void working();

    int[] getLimitTemperatureFromRovers(IRovers rover);

    List<FakeSatellite> getFakeSatelliteList();

    void addFakeSatellite(FakeSatellite satellite);

    boolean cadAddFakeSatellite(FakeSatellite satellite);

    boolean canAddFakeAsteroid(FakeAsteroid asteroid);

    void addFakePlanet(FakePlanet planet);

    void addFakeAsteroid(FakeAsteroid asteroid);

    void loadFakePlanet(FakePlanet planet);

    void loadFakeSatellite(FakeSatellite planet);

    void loadFakeAsteroid(FakeAsteroid asteroid);

    void loadDataFromPlayer(FakePlayer player);

    void unloadDataFromPlayer(FakePlayer player);

    boolean cadAddFakePlanet(FakePlanet planet);

    IRovers getRoversFromPlanet(FakePlanet planet);

    IRovers getRoversFromSatellite(FakeSatellite planet);

    IRovers getRoversFromAsteroid(FakeAsteroid asteroid);

    IBaseResource getBaseResource(FakePlanet planet, int percent);

    IBaseResource getBaseResource(FakeSatellite planet, int percent);

    IBaseResource getBaseResource(FakeAsteroid planet, int percent);

    void unload();

}
