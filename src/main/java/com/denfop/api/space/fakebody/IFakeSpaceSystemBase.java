package com.denfop.api.space.fakebody;

import com.denfop.api.space.IBaseResource;
import com.denfop.api.space.IBody;
import com.denfop.api.space.research.api.IResearchTable;
import com.denfop.api.space.research.api.IRocketLaunchPad;
import com.denfop.api.space.rovers.api.IRovers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IFakeSpaceSystemBase {

    Map<IBody, SpaceOperation> getSpaceTable(UUID player);


    Map<UUID, IRocketLaunchPad> getRocketPadMap();

    Map<UUID, IResearchTable> getResearchTableMap();

    Map<UUID, List<IFakeBody>> getBodyMap();


    List<FakeAsteroid> getFakeAsteroidList();


    Map<IBody, Data> getDataFromUUID(UUID uuid);


    List<FakePlanet> getFakePlanetList();

    void working();

    int[] getLimitTemperatureFromRovers(IRovers rover);

    List<FakeSatellite> getFakeSatelliteList();

    void addFakeSatellite(FakeSatellite satellite);


    void addFakePlanet(FakePlanet planet);

    void addFakeAsteroid(FakeAsteroid asteroid);


    IRovers getRoversFromPlanet(FakePlanet planet);

    IRovers getRoversFromSatellite(FakeSatellite planet);

    IRovers getRoversFromAsteroid(FakeAsteroid asteroid);

    List<IBaseResource> getBaseResource(FakePlanet planet, int percent);

    List<IBaseResource> getBaseResource(FakeSatellite planet, int percent);

    List<IBaseResource> getBaseResource(FakeAsteroid planet, int percent);

    void unload();

    void addDataBody(UUID name, Map<IBody, Data> map);

    void copyData(Map<IBody, Data> data, UUID uniqueID);

}
