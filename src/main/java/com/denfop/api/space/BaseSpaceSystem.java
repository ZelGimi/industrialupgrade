package com.denfop.api.space;

import com.denfop.api.space.colonies.ColonyNet;
import com.denfop.api.space.colonies.IColonyNet;
import com.denfop.api.space.fakebody.FakeSpaceSystemBase;
import com.denfop.api.space.fakebody.IFakeSpaceSystemBase;
import com.denfop.api.space.research.BaseSpaceResearchSystem;
import com.denfop.api.space.research.IResearchSystem;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseSpaceSystem implements ISpaceNet {

    public IColonyNet colonienet;
    public IFakeSpaceSystemBase spaceSystemBase;
    public IResearchSystem spaceResearch;
    Map<IBody, ISystem> systemIBodyMap;
    Map<IPlanet, List<ISatellite>> planetISatelliteMap;
    Map<IStar, List<IPlanet>> starListMap;
    Map<IPlanet, ISystem> systemIPlanetMap;
    Map<IStar, ISystem> systemIStarMap;
    List<IPlanet> planetList;
    List<IAsteroid> asteroidList;
    List<ISatellite> satelliteList;
    List<IStar> starList;
    List<ISystem> systemList;
    Map<String, IBody> bodyMap;
    List<IBody> bodies;

    public BaseSpaceSystem() {
        this.planetISatelliteMap = new HashMap<>();
        this.systemIBodyMap = new HashMap<>();
        this.systemIPlanetMap = new HashMap<>();
        this.systemIStarMap = new HashMap<>();
        this.systemList = new ArrayList<>();
        this.planetList = new ArrayList<>();
        this.satelliteList = new ArrayList<>();
        this.starListMap = new HashMap<>();
        this.starList = new ArrayList<>();
        this.bodyMap = new HashMap<>();
        this.bodies = new ArrayList<>();
        MinecraftForge.EVENT_BUS.register(this);
        this.spaceSystemBase = new FakeSpaceSystemBase();
        this.spaceResearch = new BaseSpaceResearchSystem();
        this.asteroidList = new ArrayList<>();
        this.colonienet = new ColonyNet();

    }

    @Override
    public IResearchSystem getResearchSystem() {
        return this.spaceResearch;
    }

    @Override
    public List<IAsteroid> getAsteroidList() {
        return this.asteroidList;
    }

    @Override
    public List<IBody> getBodyList() {
        return this.bodies;
    }

    @Override
    public IFakeSpaceSystemBase getFakeSpaceSystem() {
        return this.spaceSystemBase;
    }

    @Override
    public IBody getBodyFromName(final String name) {
        return this.bodyMap.get(name);
    }

    public Map<String, IBody> getBodyMap() {
        return this.bodyMap;
    }

    @Override
    public IColonyNet getColonieNet() {
        return this.colonienet;
    }

    @Override
    public List<IPlanet> getPlanetList() {
        return this.planetList;
    }

    @Override
    public List<ISatellite> getSatelliteList() {
        return this.satelliteList;
    }

    @Override
    public void addPlanet(final IPlanet planet) {
        this.planetList.add(planet);
        this.planetISatelliteMap.put(planet, planet.getSatelliteList());
        this.systemIPlanetMap.put(planet, planet.getSystem());
        this.systemIBodyMap.put(planet, planet.getSystem());
        this.bodyMap.put(planet.getName(), planet);
        this.bodies.add(planet);
        if (this.starListMap.containsKey(planet.getStar())) {
            List<IPlanet> planetList = this.starListMap.get(planet.getStar());
            planetList.add(planet);
            planet.getStar().getPlanetList().clear();
            planet.getStar().getPlanetList().addAll(planetList);
        }
    }

    @Override
    public void addAsteroid(final IAsteroid asteroid) {
        this.asteroidList.add(asteroid);
        this.bodies.add(asteroid);
        this.systemIBodyMap.put(asteroid, asteroid.getSystem());
        this.bodyMap.put(asteroid.getName(), asteroid);

    }

    @Override
    public void addStar(final IStar star) {
        this.starList.add(star);
        this.systemIStarMap.put(star, star.getSystem());
        this.starListMap.put(star, star.getPlanetList());
        this.systemIBodyMap.put(star, star.getSystem());
    }

    @Override
    public void addSatellite(final ISatellite satellite) {
        this.satelliteList.add(satellite);
        this.systemIBodyMap.put(satellite, satellite.getSystem());
        this.bodyMap.put(satellite.getName(), satellite);
        this.bodies.add(satellite);
        if (this.planetISatelliteMap.containsKey(satellite.getPlanet())) {
            List<ISatellite> satelliteList = this.planetISatelliteMap.get(satellite.getPlanet());
            satelliteList.add(satellite);
            satellite.getPlanet().getSatelliteList().clear();
            satellite.getPlanet().getSatelliteList().addAll(satelliteList);
        }
    }

    @Override
    public void addSystem(final ISystem system) {
        this.systemList.add(system);
    }

    @Override
    public void addResource(final IBaseResource resource) {
        if (resource.getBody() instanceof IPlanet) {
            IPlanet planet = this.planetList.get(this.planetList.indexOf((IPlanet) resource.getBody()));
            planet.getResources().add(resource);
        }
        if (resource.getBody() instanceof ISatellite) {
            ISatellite satellite = this.satelliteList.get(this.satelliteList.indexOf((ISatellite) resource.getBody()));
            satellite.getResources().add(resource);
        }
        if (resource.getBody() instanceof IAsteroid) {
            IAsteroid asteroid = this.asteroidList.get(this.asteroidList.indexOf((IAsteroid) resource.getBody()));
            asteroid.getResources().add(resource);
        }
    }

}
