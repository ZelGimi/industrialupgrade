package com.denfop.api.space;

import com.denfop.api.space.colonies.ColonyNet;
import com.denfop.api.space.colonies.api.IColonyNet;
import com.denfop.api.space.fakebody.FakeSpaceSystemBase;
import com.denfop.api.space.fakebody.IFakeSpaceSystemBase;
import com.denfop.api.space.research.BaseSpaceResearchSystem;
import com.denfop.api.space.research.api.IResearchSystem;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseSpaceSystem implements ISpaceNet {

    public IColonyNet colonienet;
    public IFakeSpaceSystemBase spaceSystemBase;
    public IResearchSystem spaceResearch;

    List<ISystem> systemList;
    Map<IBody, ISystem> systemIBodyMap;
    Map<ISystem, List<IStar>> starSystemMap;
    Map<IPlanet, List<ISatellite>> planetISatelliteMap;
    Map<IStar, List<IPlanet>> starListMap;
    Map<IPlanet, ISystem> systemIPlanetMap;
    Map<IStar, ISystem> systemIStarMap;
    Map<IStar, List<IAsteroid>> starAsteroidMap;
    List<IPlanet> planetList;
    List<IAsteroid> asteroidList;
    List<ISatellite> satelliteList;
    List<IStar> starList;
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
        this.starSystemMap = new HashMap<>();
        this.starList = new ArrayList<>();
        this.bodyMap = new HashMap<>();
        this.bodies = new ArrayList<>();
        MinecraftForge.EVENT_BUS.register(this);
        this.spaceSystemBase = new FakeSpaceSystemBase();
        this.spaceResearch = new BaseSpaceResearchSystem();
        this.asteroidList = new ArrayList<>();
        this.starAsteroidMap = new HashMap<>();
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
    public List<IStar> getPlanetList(final ISystem star) {
        return null;
    }

    @Override
    public List<IPlanet> getPlanetList(final IStar star) {
        return null;
    }

    @Override
    public List<ISatellite> getSatelliteList(final IPlanet Plant) {
        return null;
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
        }
    }

    @Override
    public void addAsteroid(final IAsteroid asteroid) {
        this.asteroidList.add(asteroid);
        this.bodies.add(asteroid);
        this.systemIBodyMap.put(asteroid, asteroid.getSystem());
        this.bodyMap.put(asteroid.getName(), asteroid);
        if (this.starAsteroidMap.containsKey(asteroid.getStar())) {
            List<IAsteroid> asteroids = this.starAsteroidMap.get(asteroid.getStar());
            asteroids.add(asteroid);
        }
    }

    @Override
    public void addStar(final IStar star) {
        this.starList.add(star);
        this.systemIStarMap.put(star, star.getSystem());
        this.starListMap.put(star, star.getPlanetList());
        this.starAsteroidMap.put(star, star.getAsteroidList());
        this.systemIBodyMap.put(star, star.getSystem());
        if (this.starSystemMap.containsKey(star.getSystem())) {
            List<IStar> satelliteList = this.starSystemMap.get(star.getSystem());
            satelliteList.add(star);
        }
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
        }
    }

    @Override
    public void addSystem(final ISystem system) {
        this.systemList.add(system);
        starSystemMap.put(system, system.getStarList());
    }

    public List<ISystem> getSystem() {
        return systemList;
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
