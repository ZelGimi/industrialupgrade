package com.denfop.api.space;

import com.denfop.api.space.colonies.IColonyNet;
import com.denfop.api.space.fakebody.IFakeSpaceSystemBase;
import com.denfop.api.space.research.IResearchSystem;

import java.util.List;
import java.util.Map;

public interface ISpaceNet {

    IColonyNet getColonieNet();

    List<IPlanet> getPlanetList();

    List<ISatellite> getSatelliteList();

    void addPlanet(IPlanet planet);

    void addAsteroid(IAsteroid asteroid);

    void addStar(IStar star);

    void addSatellite(ISatellite satellite);

    void addSystem(ISystem system);

    void addResource(IBaseResource resource);

    IFakeSpaceSystemBase getFakeSpaceSystem();

    IBody getBodyFromName(String name);

    Map<String, IBody> getBodyMap();

    IResearchSystem getResearchSystem();

    List<IAsteroid> getAsteroidList();

    List<IBody> getBodyList();

}
