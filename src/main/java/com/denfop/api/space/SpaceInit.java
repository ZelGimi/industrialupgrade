package com.denfop.api.space;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.space.rovers.enums.EnumTypeRovers;
import com.denfop.blocks.FluidName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public class SpaceInit {

    public static System solarSystem;
    public static Star sun;
    public static Planet mercury;
    public static Planet venus;
    public static Planet earth;
    public static Planet mars;
    public static Planet pluto;
    public static Planet jupiter;
    public static Planet saturn;
    public static Planet uranus;
    public static Planet neptune;
    public static Satellite moon;
    public static Satellite deimos;
    public static Satellite phobos;
    public static System sextantis24;
    public static System cancri55;
    public static System kapteyn;
    public static System kepler148;
    public static System kepler149;
    public static System kepler186;
    public static Satellite io;
    public static Satellite callisto;
    public static Satellite ganymede;
    public static Satellite europe;
    public static Planet ceres;
    public static Satellite charon;
    public static Satellite enceladus;
    public static Satellite titan;
    public static Satellite dione;
    public static Satellite mimas;
    public static Satellite rhea;
    public static Satellite titania;
    public static Satellite umbriel;
    public static Satellite oberon;
    public static Satellite ariel;
    public static Satellite miranda;
    public static Satellite triton;
    public static Satellite proteus;
    public static Satellite tethys;
    public static Planet eris;
    public static Planet makemake;
    public static Planet haumea;
    public static Asteroid asteroids;
    public static List<Runnable> regSystem = new LinkedList<>();
    public static List<Runnable> regStar = new LinkedList<>();
    public static List<Runnable> regPlanet = new LinkedList<>();
    public static List<Runnable> regSatellite = new LinkedList<>();
    public static List<Runnable> regAsteroid = new LinkedList<>();
    public static List<Runnable> regBaseResource = new LinkedList<>();
    public static List<Runnable> regColonyBaseResource = new LinkedList<>();
    private static Star sextantis;
    private static Star cancri;
    private static Star kapteynSun;
    private static Star kepler186Sun;

    public static void jsonInit() {
        regSystem.forEach(Runnable::run);
        regStar.forEach(Runnable::run);
        regPlanet.forEach(Runnable::run);
        regSatellite.forEach(Runnable::run);
        regAsteroid.forEach(Runnable::run);
        regBaseResource.forEach(Runnable::run);
        regColonyBaseResource.forEach(Runnable::run);
    }

    public static void init() {
        solarSystem = new System("solarsystem");
        sextantis24 = new System("sextantis");
        cancri55 = new System("cancri");
        kapteyn = new System("kapteyn");
        kepler148 = new System("kepler148");
        kepler149 = new System("kepler149");
        kepler186 = new System("kepler186");
        sun = new Star("sun", solarSystem, getTexture("sun"), 7, 0.5);
        sextantis = new Star("24sextantis", sextantis24, getTexture("24sextantis"), 7, 0.5);
        cancri = new Star("55cancri", cancri55, getTexture("55cancri"), 7, 0.5);
        kapteynSun = new Star("kapteyn", kapteyn, getTexture("kapteyn"), 7, 0.5);
        kepler186Sun = new Star("kepler186", kepler186, getTexture("kepler186"), 7, 0.5);
        asteroids = new Asteroid("asteroid", solarSystem, getTexture("asteroid"), EnumLevels.SECOND, sun, -108, 0, EnumType.NEUTRAL
                , false, 0, 0, 1, 0, 1.8, 2, 500);
        mercury = new Planet(
                "mercury",
                solarSystem,
                getTexture("mercury"),
                EnumLevels.SECOND,
                sun,
                167,
                false,
                0.55,
                EnumType.NEUTRAL,
                false,
                true, 0, 1, 0.05, 4.5
        );
        venus = new Planet(
                "venus",
                solarSystem,
                getTexture("venus"),
                EnumLevels.FIRST,
                sun,
                464,
                true,
                0.7,
                EnumType.DANGEROUS,
                false,
                true, 177, 0.75, 0.075, 5.5
        );
        earth = new Planet(
                "earth",
                solarSystem,
                getTexture("earth"),
                EnumLevels.NONE,
                sun,
                20,
                false,
                0.9,
                EnumType.SAFE,
                true,
                false, 23, 0.6, 0.1, 4.1
        );
        moon = new Satellite(
                "moon",
                solarSystem,
                getTexture("moon"),
                EnumLevels.FIRST,
                earth,
                -20,
                false,
                0.12,
                EnumType.NEUTRAL,
                false,
                true, 0, 1.5, 0.035, 1.1
        );

        mars = new Planet(
                "mars",
                solarSystem,
                getTexture("mars"),
                EnumLevels.FIRST,
                sun,
                -65,
                false,
                1.5,
                EnumType.NEUTRAL,
                false,
                true, 25, 0.5, 0.088, 3.3
        );
        deimos = new Satellite(
                "deimos",
                solarSystem,
                getTexture("deimos"),
                EnumLevels.FIRST,
                mars,
                -65,
                false,
                0.12F,
                EnumType.NEUTRAL,
                false,
                true, 0, 1.5, 0.025, 6.1
        );
        phobos = new Satellite(
                "phobos",
                solarSystem,
                getTexture("phobos"),
                EnumLevels.FIRST,
                mars,
                -65,
                false,
                0.15,
                EnumType.NEUTRAL,
                false,
                true, 0, 0.8, 0.025, 7.1
        );
        ceres = new Planet(
                "ceres",
                solarSystem,
                getTexture("ceres"),
                EnumLevels.SECOND,
                sun,
                -45,
                false,
                1.8,
                EnumType.NEUTRAL,
                false,
                true, 1, 0.45, 0.03, 3.3
        );
        jupiter = new Planet(
                "jupiter",
                solarSystem,
                getTexture("jupiter"),
                EnumLevels.NONE,
                sun,
                -110,
                false,
                2.8,
                EnumType.DANGEROUS,
                false,
                false,
                3, 0.4, 0.5, 3.2
        );
        europe = new Satellite(
                "europe",
                solarSystem,
                getTexture("europe"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                0.52,
                EnumType.NEUTRAL,
                false,
                true,
                3, 1.844, 0.045, 3.2
        );
        ganymede = new Satellite(
                "ganymede",
                solarSystem,
                getTexture("ganymede"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                0.57,
                EnumType.NEUTRAL,
                false,
                true,
                3, 0.353, 0.0425, 3.2
        );
        io = new Satellite(
                "io",
                solarSystem,
                getTexture("io"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                0.45,
                EnumType.NEUTRAL,
                false,
                true,
                3, 0.844, 0.05, 3.2
        );
        callisto = new Satellite(
                "callisto",
                solarSystem,
                getTexture("callisto"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                0.63,
                EnumType.NEUTRAL,
                false,
                true,
                3, 0.65, 0.04, 3.2
        );
        saturn = new Planet(
                "saturn",
                solarSystem,
                getTexture("saturn"),
                EnumLevels.NONE,
                sun,
                -140,
                false,
                4.5,
                EnumType.DANGEROUS,
                false,
                false,
                26, 0.3, 0.35, 2.8, EnumRing.HORIZONTAL
        );
        enceladus = new Satellite(
                "enceladus",
                solarSystem,
                getTexture("enceladus"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                0.4,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.4, 0.05, 2.8
        );
        titan = new Satellite(
                "titan",
                solarSystem,
                getTexture("titan"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                0.625,
                EnumType.NEUTRAL,
                false,
                true,
                1, 2, 0.0375, 2.8
        );
        dione = new Satellite(
                "dione",
                solarSystem,
                getTexture("dione"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                0.475,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.6, 0.0425, 2.8
        );
        mimas = new Satellite(
                "mimas",
                solarSystem,
                getTexture("mimas"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                0.325,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.2, 0.05, 2.8
        );
        rhea = new Satellite(
                "rhea",
                solarSystem,
                getTexture("rhea"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                0.55,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.8, 0.04, 2.8
        );
        tethys = new Satellite(
                "tethys",
                solarSystem,
                getTexture("tethys"),
                EnumLevels.THIRD,
                saturn,
                -187,
                false,
                0.665,
                EnumType.NEUTRAL,
                false,
                true, 1, 2.15, 0.03, 3
        );
        uranus = new Planet(
                "uranus",
                solarSystem,
                getTexture("uranus"),
                EnumLevels.NONE,
                sun,
                -195,
                false,
                6.2,
                EnumType.DANGEROUS,
                false,
                false,
                28, 0.25, 0.25, 2.4, EnumRing.VERTICAL
        );
        titania = new Satellite(
                "titania",
                solarSystem,
                getTexture("titania"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                0.55,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.8, 0.0375, 2.4
        );
        umbriel = new Satellite(
                "umbriel",
                solarSystem,
                getTexture("umbriel"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                0.475,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.6, 0.04, 2.4
        );
        oberon = new Satellite(
                "oberon",
                solarSystem,
                getTexture("oberon"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                0.625,
                EnumType.NEUTRAL,
                false,
                true, 1, 2, 0.035, 2.4
        );
        ariel = new Satellite(
                "ariel",
                solarSystem,
                getTexture("ariel"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                0.4,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.4, 0.045, 2.4
        );
        miranda = new Satellite(
                "miranda",
                solarSystem,
                getTexture("miranda"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                0.325,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.2, 0.05, 2.4
        );
        neptune = new Planet(
                "neptune",
                solarSystem,
                getTexture("neptune"),
                EnumLevels.NONE,
                sun,
                -220,
                false,
                7.45,
                EnumType.DANGEROUS,
                false,
                false, 85, 0.25, 0.25, 2.1
        );
        triton = new Satellite(
                "triton",
                solarSystem,
                getTexture("triton"),
                EnumLevels.FIVE,
                neptune,
                -220,
                false,
                0.425,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.66, 0.045, 2.1
        );
        proteus = new Satellite(
                "proteus",
                solarSystem,
                getTexture("proteus"),
                EnumLevels.FIVE,
                neptune,
                -220,
                false,
                0.325,
                EnumType.NEUTRAL,
                false,
                true, 1, 1.2, 0.05, 2.1
        );
        pluto = new Planet(
                "pluto",
                solarSystem,
                getTexture("pluto"),
                EnumLevels.SIX,
                sun,
                -247,
                false,
                8.3,
                EnumType.NEUTRAL,
                false,
                true, 119, 0.15, 0.05, 1.8
        );
        charon = new Satellite(
                "charon",
                solarSystem,
                getTexture("charon"),
                EnumLevels.SIX,
                pluto,
                -247,
                false,
                0.125,
                EnumType.NEUTRAL,
                false,
                true, 119, 0.15, 0.035, 1.8
        );
        eris = new Planet(
                "eris",
                solarSystem,
                getTexture("eris"),
                EnumLevels.SEVEN,
                sun,
                -243,
                false,
                8.9,
                EnumType.NEUTRAL,
                false,
                true, 1, 0.1, 0.04, 1.2
        );
        makemake = new Planet(
                "makemake",
                solarSystem,
                getTexture("makemake"),
                EnumLevels.SEVEN,
                sun,
                -243,
                false,
                9.3,
                EnumType.NEUTRAL,
                false,
                true, 1, 0.075, 0.03, 1
        );
        haumea = new Planet(
                "haumea",
                solarSystem,
                getTexture("haumea"),
                EnumLevels.SEVEN,
                sun,
                -243,
                false,
                9.75,
                EnumType.NEUTRAL,
                false,
                true, 1, 0.05, 0.025, 0.85
        );
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(45), 1), 50, 100, 1, mercury, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(45), 3), 75, 100, 3, mercury, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(15), 1), 20, 100, 7, mercury, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(15), 2), 35, 100, 12, mercury, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(15), 3), 40, 100, 20, mercury, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(15), 1), 20, 100, 20, mercury, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(15), 1), 40, 100, 30, mercury, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(15), 2), 20, 100, 40, mercury, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(15), 1), 20, 100, 55, mercury, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(15), 4), 40, 100, 65, mercury, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(1), 1), 40, 100, 70, mercury, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(1), 2), 40, 100, 75, mercury, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(1), 2), 50, 100, 80, mercury, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(2), 1), 40, 100, 85, mercury, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(1), 2), 60, 100, 85, mercury, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(2), 2), 70, 100, 90, mercury, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(1), 2), 80, 100, 90, mercury, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(2), 2), 100, 100, 100, mercury, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(1), 4), 100, 100, 100, mercury, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(48), 1), 50, 100, 1, moon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(48), 3), 75, 100, 3, moon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(18), 1), 20, 100, 7, moon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(18), 2), 35, 100, 12, moon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(18), 3), 40, 100, 20, moon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(2), 1), 20, 100, 20, moon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(2), 1), 40, 100, 30, moon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(2), 2), 20, 100, 40, moon, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 400), 40, 100, 40, moon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(2), 1), 20, 100, 55, moon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(2), 4), 40, 100, 65, moon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(7), 1), 40, 100, 70, moon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(7), 2), 40, 100, 75, moon, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 1000), 50, 100, 40, moon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(7), 2), 50, 100, 80, moon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(6), 1), 40, 100, 85, moon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(6), 2), 70, 100, 90, moon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(7), 2), 80, 100, 90, moon, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 2500), 90, 100, 80, moon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(6), 2), 100, 100, 100, moon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(7), 4), 100, 100, 100, moon, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(59), 1), 50, 100, 1, venus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(59), 3), 75, 100, 3, venus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(29), 1), 20, 100, 7, venus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(29), 2), 35, 100, 12, venus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(29), 3), 40, 100, 20, venus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(13), 1), 20, 100, 20, venus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(13), 1), 40, 100, 30, venus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(13), 2), 20, 100, 40, venus, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get(), 500), 40, 100, 40, venus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(13), 1), 20, 100, 55, venus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(13), 4), 40, 100, 65, venus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(12), 1), 10, 100, 70, venus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(12), 1), 20, 100, 75, venus, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get(), 1000), 50, 100, 40, venus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(12), 1), 50, 100, 80, venus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(12), 1), 70, 100, 85, venus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(12), 1), 90, 100, 90, venus, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get(), 2000), 90, 100, 80, venus,
                EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(12), 2), 100, 100, 100, venus, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(44), 1), 50, 100, 1, mars, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(44), 3), 75, 100, 3, mars, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(14), 1), 20, 100, 7, mars, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(14), 2), 35, 100, 12, mars, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(14), 3), 40, 100, 20, mars, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(14), 1), 20, 100, 20, mars, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(14), 1), 40, 100, 30, mars, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(14), 2), 20, 100, 40, mars, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 500), 40, 100, 40, mars, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(14), 1), 20, 100, 55, mars, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(14), 4), 40, 100, 65, mars, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(0), 1), 40, 100, 70, mars, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(0), 2), 40, 100, 75, mars, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 1000), 50, 100, 40, mars, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(0), 2), 50, 100, 80, mars, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(15), 1), 40, 100, 85, mars, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(15), 2), 70, 100, 90, mars, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(0), 2), 80, 100, 90, mars, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 2000), 90, 100, 80, mars, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(15), 2), 100, 100, 100, mars, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(0), 4), 100, 100, 100, mars, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(33), 1), 50, 100, 1, ceres, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(33), 3), 75, 100, 3, ceres, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(3), 1), 20, 100, 7, ceres, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(3), 2), 35, 100, 12, ceres, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(3), 3), 40, 100, 20, ceres, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(3), 1), 20, 100, 20, ceres, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(3), 1), 40, 100, 30, ceres, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(3), 2), 20, 100, 40, ceres, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 500), 40, 100, 40, ceres, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(3), 1), 20, 100, 55, ceres, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(3), 4), 40, 100, 65, ceres, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(6), 1), 40, 100, 70, ceres, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(7), 1), 40, 100, 70, ceres, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 1000), 50, 100, 40, ceres, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(6), 1), 60, 100, 80, ceres, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(7), 1), 60, 100, 80, ceres, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(6), 2), 80, 100, 90, ceres, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(7), 2), 80, 100, 90, ceres, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 2000), 90, 100, 80, ceres, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(6), 2), 100, 100, 100, ceres, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(7), 2), 100, 100, 100, ceres, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(31), 1), 50, 100, 1, asteroids, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(31), 1), 75, 100, 3, asteroids, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(1), 1), 20, 100, 7, asteroids, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(1), 2), 15, 100, 12, asteroids, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(1), 1), 40, 100, 20, asteroids, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(1), 1), 20, 100, 20, asteroids, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(1), 1), 40, 100, 10, asteroids, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(1), 2), 20, 100, 40, asteroids, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 500), 40, 100, 40, asteroids, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(1), 1), 20, 100, 55, asteroids, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(1), 4), 40, 100, 65, asteroids, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(1), 1), 40, 100, 70, asteroids, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(2), 1), 40, 100, 70, asteroids, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 1000), 50, 100, 40, asteroids, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(1), 1), 60, 100, 80, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(2), 1), 60, 100, 80, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(1), 2), 80, 100, 90, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(2), 2), 80, 100, 90, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 2500), 75, 100, 95, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 2500), 90, 100, 80, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(1), 2), 100, 100, 100, asteroids, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(2), 2), 100, 100, 100, asteroids, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(50), 1), 50, 100, 1, phobos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(50), 1), 75, 100, 3, phobos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(20), 1), 20, 100, 7, phobos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(20), 2), 15, 100, 12, phobos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(20), 1), 40, 100, 20, phobos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(4), 1), 20, 100, 20, phobos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(4), 1), 40, 100, 10, phobos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(4), 2), 20, 100, 40, phobos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(4), 1), 20, 100, 55, phobos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(4), 4), 40, 100, 65, phobos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(10), 1), 40, 100, 70, phobos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(11), 1), 40, 100, 70, phobos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(10), 1), 60, 100, 80, phobos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(11), 1), 60, 100, 80, phobos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(10), 2), 80, 100, 90, phobos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(11), 2), 80, 100, 90, phobos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(10), 2), 100, 100, 100, phobos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(11), 2), 100, 100, 100, phobos, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(35), 1), 50, 100, 1, deimos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(35), 1), 75, 100, 3, deimos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(5), 1), 20, 100, 7, deimos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(5), 2), 15, 100, 12, deimos, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(5), 1), 40, 100, 20, deimos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(5), 1), 20, 100, 20, deimos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(5), 1), 40, 100, 10, deimos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(5), 2), 20, 100, 40, deimos, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 500), 40, 100, 40, deimos, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(5), 1), 20, 100, 55, deimos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(5), 4), 40, 100, 65, deimos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(10), 1), 10, 100, 70, deimos, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 1000), 50, 100, 40, deimos, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(10), 1), 40, 100, 80, deimos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(10), 1), 70, 100, 90, deimos, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 2500), 90, 100, 80, deimos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(10), 1), 100, 100, 100, deimos, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 5000), 100, 100, 100, deimos, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(42), 1), 50, 100, 1, io, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(42), 3), 75, 100, 3, io, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(12), 1), 20, 100, 7, io, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(12), 2), 35, 100, 12, io, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(12), 3), 40, 100, 20, io, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(12), 1), 20, 100, 20, io, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(12), 1), 40, 100, 30, io, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(12), 2), 20, 100, 40, io, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(12), 1), 20, 100, 55, io, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(12), 4), 40, 100, 65, io, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(10), 1), 40, 100, 70, io, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(11), 1), 40, 100, 70, io, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(10), 1), 60, 100, 80, io, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(11), 1), 60, 100, 80, io, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(10), 2), 80, 100, 90, io, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(11), 2), 80, 100, 90, io, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(10), 2), 100, 100, 100, io, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(11), 2), 100, 100, 100, io, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(32), 1), 50, 100, 1, callisto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(32), 3), 75, 100, 3, callisto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(2), 1), 20, 100, 7, callisto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(2), 2), 35, 100, 12, callisto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(2), 3), 40, 100, 20, callisto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(2), 1), 20, 100, 20, callisto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(2), 1), 40, 100, 30, callisto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(2), 2), 20, 100, 40, callisto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(2), 1), 20, 100, 55, callisto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(2), 4), 40, 100, 65, callisto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(3), 1), 40, 100, 70, callisto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(4), 1), 40, 100, 70, callisto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(5), 1), 40, 100, 70, callisto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(3), 1), 60, 100, 80, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(4), 1), 60, 100, 80, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(5), 1), 60, 100, 80, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(3), 2), 80, 100, 90, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(4), 2), 80, 100, 90, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(5), 2), 80, 100, 90, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(3), 2), 100, 100, 100, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(4), 2), 100, 100, 100, callisto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(5), 2), 100, 100, 100, callisto, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(40), 1), 50, 100, 1, ganymede, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(40), 3), 75, 100, 3, ganymede, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(10), 1), 20, 100, 7, ganymede, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(10), 2), 35, 100, 12, ganymede, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(10), 3), 40, 100, 20, ganymede, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(10), 1), 20, 100, 20, ganymede, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(10), 1), 40, 100, 30, ganymede, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(10), 2), 20, 100, 40, ganymede, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 500), 40, 100, 40, ganymede, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(10), 1), 20, 100, 55, ganymede, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(10), 4), 40, 100, 65, ganymede, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(2), 1), 40, 100, 70, ganymede, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(3), 1), 40, 100, 70, ganymede, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 1000), 50, 100, 40, ganymede, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(2), 1), 60, 100, 80, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(3), 1), 60, 100, 80, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(2), 2), 80, 100, 90, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(3), 2), 80, 100, 90, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 2000), 90, 100, 80, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(2), 2), 100, 100, 100, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(3), 2), 100, 100, 100, ganymede, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 5000), 100, 100, 100, ganymede,
                EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(39), 1), 50, 100, 1, europe, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(39), 3), 75, 100, 3, europe, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(9), 1), 20, 100, 7, europe, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(9), 2), 35, 100, 12, europe, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(9), 3), 40, 100, 20, europe, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(9), 1), 20, 100, 20, europe, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(9), 1), 40, 100, 30, europe, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(9), 2), 20, 100, 40, europe, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(9), 1), 20, 100, 55, europe, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(9), 4), 40, 100, 65, europe, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(0), 1), 40, 100, 70, europe, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(1), 1), 40, 100, 70, europe, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(4), 1), 40, 100, 70, europe, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(0), 1), 60, 100, 80, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(1), 1), 60, 100, 80, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(4), 1), 60, 100, 80, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(0), 2), 80, 100, 90, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(1), 2), 80, 100, 90, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(4), 2), 80, 100, 90, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(0), 2), 100, 100, 100, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(1), 2), 100, 100, 100, europe, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(4), 2), 100, 100, 100, europe, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(37), 1), 50, 100, 1, enceladus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(37), 3), 75, 100, 3, enceladus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(7), 1), 20, 100, 7, enceladus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(7), 2), 35, 100, 12, enceladus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(7), 3), 40, 100, 20, enceladus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(7), 1), 20, 100, 20, enceladus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(7), 1), 40, 100, 30, enceladus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(7), 2), 20, 100, 40, enceladus, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 500), 40, 100, 40, enceladus, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 500), 40, 100, 40, enceladus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(7), 1), 20, 100, 55, enceladus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(7), 4), 40, 100, 65, enceladus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(13), 1), 40, 100, 70, enceladus, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 1000), 50, 100, 40, enceladus, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 1000), 50, 100, 40, enceladus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(13), 1), 60, 100, 80, enceladus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(13), 2), 80, 100, 90, enceladus, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidacetylene.getInstance().get(), 2000), 90, 100, 80, enceladus, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 2000), 90, 100, 80, enceladus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(13), 2), 100, 100, 100, enceladus, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(55), 1), 50, 100, 1, titan, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(55), 3), 75, 100, 3, titan, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(25), 1), 20, 100, 7, titan, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(25), 2), 35, 100, 12, titan, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(25), 3), 40, 100, 20, titan, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(9), 1), 20, 100, 20, titan, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(9), 1), 40, 100, 30, titan, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(9), 2), 20, 100, 40, titan, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(9), 1), 20, 100, 55, titan, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(9), 4), 40, 100, 65, titan, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(5), 1), 40, 100, 70, titan, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(6), 1), 40, 100, 70, titan, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(5), 1), 60, 100, 80, titan, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(6), 1), 60, 100, 80, titan, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(5), 2), 80, 100, 90, titan, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(6), 2), 80, 100, 90, titan, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(5), 2), 100, 100, 100, titan, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(6), 2), 100, 100, 100, titan, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(36), 1), 50, 100, 1, dione, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(36), 3), 75, 100, 3, dione, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(6), 1), 20, 100, 7, dione, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(6), 2), 35, 100, 12, dione, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(6), 3), 40, 100, 20, dione, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(6), 1), 20, 100, 20, dione, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(6), 1), 40, 100, 30, dione, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(6), 2), 20, 100, 40, dione, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidmethane.getInstance().get(), 500), 40, 100, 40, dione, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(6), 1), 20, 100, 55, dione, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(6), 4), 40, 100, 65, dione, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(11), 1), 40, 100, 70, dione, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(12), 1), 40, 100, 70, dione, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidmethane.getInstance().get(), 1000), 50, 100, 40, dione, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(11), 1), 60, 100, 80, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(12), 1), 60, 100, 80, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(11), 2), 80, 100, 90, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(12), 2), 80, 100, 90, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidmethane.getInstance().get(), 2000), 80, 100, 80, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(11), 2), 100, 100, 100, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(12), 2), 100, 100, 100, dione, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidmethane.getInstance().get(), 9500), 100, 100, 100, dione, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(46), 1), 50, 100, 1, mimas, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(46), 3), 75, 100, 3, mimas, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(16), 1), 20, 100, 7, mimas, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(16), 2), 35, 100, 12, mimas, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(16), 3), 40, 100, 20, mimas, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(0), 1), 20, 100, 20, mimas, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(0), 1), 40, 100, 30, mimas, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(0), 2), 20, 100, 40, mimas, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidpropane.getInstance().get(), 500), 40, 100, 40, mimas, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidbutene.getInstance().get(), 500), 40, 100, 40, mimas, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(0), 1), 20, 100, 55, mimas, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(0), 4), 40, 100, 65, mimas, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(3), 1), 20, 100, 70, mimas, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidpropane.getInstance().get(), 1000), 50, 100, 70, mimas, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidbutene.getInstance().get(), 1000), 50, 100, 70, mimas, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(3), 1), 40, 100, 80, mimas, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidpropane.getInstance().get(), 2000), 80, 100, 80, mimas, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidbutene.getInstance().get(), 2000), 80, 100, 80, mimas, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(3), 1), 60, 100, 90, mimas, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(3), 1), 100, 100, 100, mimas, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidpropane.getInstance().get(), 5000), 90, 100, 100, mimas, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidbutene.getInstance().get(), 5000), 90, 100, 100, mimas, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(53), 1), 50, 100, 1, rhea, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(53), 3), 75, 100, 3, rhea, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(23), 1), 20, 100, 7, rhea, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(23), 2), 35, 100, 12, rhea, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(23), 3), 40, 100, 20, rhea, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(7), 1), 20, 100, 20, rhea, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(7), 1), 40, 100, 30, rhea, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(7), 2), 20, 100, 40, rhea, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 1000), 50, 100, 40, rhea, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(7), 1), 20, 100, 55, rhea, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(7), 4), 40, 100, 65, rhea, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(0), 1), 40, 100, 70, rhea, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(1), 1), 40, 100, 70, rhea, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 1000), 50, 100, 40, rhea, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(0), 1), 60, 100, 80, rhea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(2), 1), 60, 100, 80, rhea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(1), 2), 80, 100, 90, rhea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(0), 2), 80, 100, 90, rhea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(1), 2), 100, 100, 100, rhea, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 1000), 50, 100, 40, rhea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(2), 2), 100, 100, 100, rhea, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(54), 1), 50, 100, 1, tethys, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(54), 3), 75, 100, 3, tethys, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(24), 1), 20, 100, 7, tethys, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(24), 2), 35, 100, 12, tethys, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(24), 3), 40, 100, 20, tethys, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(8), 1), 20, 100, 20, tethys, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(8), 1), 40, 100, 30, tethys, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(8), 2), 20, 100, 40, tethys, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 500), 40, 100, 40, tethys, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(8), 1), 20, 100, 55, tethys, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(8), 4), 40, 100, 65, tethys, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(3), 1), 40, 100, 70, tethys, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(4), 1), 40, 100, 70, tethys, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 1000), 50, 100, 40, tethys, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(3), 1), 60, 100, 80, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(4), 1), 60, 100, 80, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(3), 2), 80, 100, 90, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(4), 2), 80, 100, 90, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 2000), 80, 100, 80, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(3), 2), 100, 100, 100, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(4), 2), 100, 100, 100, tethys, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 5000), 100, 100, 100, tethys, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(56), 1), 50, 100, 1, titania, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(56), 3), 75, 100, 3, titania, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(26), 1), 20, 100, 7, titania, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(26), 2), 35, 100, 12, titania, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(26), 3), 40, 100, 20, titania, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(10), 1), 20, 100, 20, titania, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(10), 1), 40, 100, 30, titania, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(10), 2), 20, 100, 40, titania, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidchlorum.getInstance().get(), 500), 40, 100, 40, titania, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(10), 1), 20, 100, 55, titania, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(10), 4), 40, 100, 65, titania, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(7), 1), 40, 100, 70, titania, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(8), 1), 40, 100, 70, titania, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidchlorum.getInstance().get(), 1000), 50, 100, 40, titania, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(7), 1), 60, 100, 80, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(8), 1), 60, 100, 80, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(7), 2), 80, 100, 90, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(8), 2), 80, 100, 90, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidchlorum.getInstance().get(), 2000), 80, 100, 80, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(7), 2), 100, 100, 100, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(8), 2), 100, 100, 100, titania, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidchlorum.getInstance().get(), 4000), 100, 100, 100, titania, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(58), 1), 50, 100, 1, umbriel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(58), 3), 75, 100, 3, umbriel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(28), 1), 20, 100, 7, umbriel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(28), 2), 35, 100, 12, umbriel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(28), 3), 40, 100, 20, umbriel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(12), 1), 20, 100, 20, umbriel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(12), 1), 40, 100, 30, umbriel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(12), 2), 20, 100, 40, umbriel, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 500), 40, 100, 40, umbriel, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 500), 40, 100, 40, umbriel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(12), 1), 20, 100, 55, umbriel, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(12), 4), 40, 100, 65, umbriel, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(11), 1), 20, 100, 70, umbriel, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 1000), 50, 100, 70, umbriel, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 1000), 50, 100, 70, umbriel, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(11), 1), 40, 100, 80, umbriel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 2000), 80, 100, 80, umbriel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 2000), 80, 100, 80, umbriel, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(11), 1), 80, 100, 90, umbriel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 5000), 90, 100, 100, umbriel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidhelium.getInstance().get(), 5000), 90, 100, 100, umbriel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidmethane.getInstance().get(), 4000), 90, 100, 100, umbriel, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(49), 1), 50, 100, 1, oberon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(49), 3), 75, 100, 3, oberon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(19), 1), 20, 100, 7, oberon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(19), 2), 35, 100, 12, oberon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(19), 3), 40, 100, 20, oberon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(3), 1), 20, 100, 20, oberon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(3), 1), 40, 100, 30, oberon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(3), 2), 20, 100, 40, oberon, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidethane.getInstance().get(), 500), 40, 100, 40, oberon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(3), 1), 20, 100, 55, oberon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(3), 4), 40, 100, 65, oberon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(8), 1), 40, 100, 70, oberon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(9), 1), 40, 100, 70, oberon, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidethane.getInstance().get(), 1000), 50, 100, 40, oberon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(9), 1), 60, 100, 80, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(8), 1), 60, 100, 80, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(9), 2), 80, 100, 90, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(8), 2), 80, 100, 90, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidethane.getInstance().get(), 2000), 80, 100, 80, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(9), 2), 100, 100, 100, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(8), 2), 100, 100, 100, oberon, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidethane.getInstance().get(), 4000), 100, 100, 100, oberon, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(30), 1), 50, 100, 1, ariel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(30), 3), 75, 100, 3, ariel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(0), 1), 20, 100, 7, ariel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(0), 2), 35, 100, 12, ariel, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(0), 3), 40, 100, 20, ariel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(0), 1), 20, 100, 20, ariel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(0), 1), 40, 100, 30, ariel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(0), 2), 20, 100, 40, ariel, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 500), 40, 100, 40, ariel, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidethylene.getInstance().get(), 500), 40, 100, 40, ariel, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(0), 1), 20, 100, 55, ariel, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(0), 4), 40, 100, 65, ariel, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(0), 1), 20, 100, 70, ariel, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 1000), 50, 100, 70, ariel, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidethylene.getInstance().get(), 1000), 50, 100, 70, ariel, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(0), 1), 40, 100, 80, ariel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 2000), 80, 100, 80, ariel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidethylene.getInstance().get(), 2000), 80, 100, 80, ariel, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(0), 1), 60, 100, 90, ariel, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(0), 1), 80, 100, 100, ariel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 4000), 100, 100, 100, ariel, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidethylene.getInstance().get(), 4000), 100, 100, 100, ariel, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(47), 1), 50, 100, 1, miranda, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(47), 3), 75, 100, 3, miranda, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(17), 1), 20, 100, 7, miranda, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(17), 2), 35, 100, 12, miranda, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(17), 3), 40, 100, 20, miranda, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(1), 1), 20, 100, 20, miranda, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(1), 1), 40, 100, 30, miranda, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(1), 2), 20, 100, 40, miranda, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(1), 1), 20, 100, 55, miranda, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(1), 4), 40, 100, 65, miranda, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(4), 1), 40, 100, 70, miranda, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(5), 1), 40, 100, 70, miranda, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidnitrogenoxy.getInstance().get(), 1000), 40, 100, 70, miranda, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 1000), 50, 100, 70, miranda, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(4), 1), 60, 100, 80, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(5), 1), 60, 100, 80, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(4), 2), 80, 100, 90, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(5), 2), 80, 100, 90, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(4), 2), 100, 100, 100, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(5), 2), 100, 100, 100, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitrogenoxy.getInstance().get(), 3000), 90, 100, 90, miranda, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 3000), 90, 100, 90, miranda, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(57), 1), 50, 100, 1, triton, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(57), 3), 75, 100, 3, triton, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(27), 1), 20, 100, 7, triton, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(27), 2), 35, 100, 12, triton, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(27), 3), 40, 100, 20, triton, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(11), 1), 20, 100, 20, triton, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(11), 1), 40, 100, 30, triton, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(11), 2), 20, 100, 40, triton, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 500), 40, 100, 40, triton, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(11), 1), 20, 100, 55, triton, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(11), 4), 40, 100, 65, triton, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(9), 1), 40, 100, 70, triton, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(10), 1), 40, 100, 70, triton, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 1000), 50, 100, 60, triton, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(9), 1), 60, 100, 80, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(10), 1), 60, 100, 80, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(9), 2), 80, 100, 90, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(10), 2), 80, 100, 90, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 2000), 80, 100, 80, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(9), 2), 100, 100, 100, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore3.getItemStack(10), 2), 100, 100, 100, triton, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 4000), 100, 100, 100, triton, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(52), 1), 50, 100, 1, proteus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(52), 3), 75, 100, 3, proteus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(22), 1), 20, 100, 7, proteus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(22), 2), 35, 100, 12, proteus, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(22), 3), 40, 100, 20, proteus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(6), 1), 20, 100, 20, proteus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(6), 1), 40, 100, 30, proteus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(6), 2), 20, 100, 40, proteus, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 500), 40, 100, 40, proteus, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(6), 1), 20, 100, 55, proteus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(6), 4), 40, 100, 65, proteus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(14), 1), 40, 100, 70, proteus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(15), 1), 40, 100, 70, proteus, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 1000), 50, 100, 60, proteus, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(14), 1), 60, 100, 80, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(15), 1), 60, 100, 80, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(14), 2), 80, 100, 90, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(15), 2), 80, 100, 90, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 2000), 80, 100, 80, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(14), 2), 100, 100, 100, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(15), 2), 100, 100, 100, proteus, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 4000), 100, 100, 100, proteus, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(51), 1), 50, 100, 1, pluto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(51), 3), 75, 100, 3, pluto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(21), 1), 20, 100, 7, pluto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(21), 2), 35, 100, 12, pluto, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(21), 3), 40, 100, 20, pluto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(5), 1), 20, 100, 20, pluto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(5), 1), 40, 100, 30, pluto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone1.getItemStack(5), 2), 20, 100, 40, pluto, EnumTypeRovers.PROBE);
        new BaseResource(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 500), 40, 100, 40, pluto, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(5), 1), 20, 100, 55, pluto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(13), 1), 60, 100, 60, pluto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone1.getItemStack(5), 4), 40, 100, 65, pluto, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 1000), 50, 100, 70, pluto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(12), 1), 80, 100, 70, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(13), 1), 80, 100, 70, pluto, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(12), 2), 100, 100, 80, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(13), 2), 100, 100, 80, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 2000), 80, 100, 80, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 5000), 90, 100, 90, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(12), 4), 100, 100, 90, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore2.getItemStack(13), 4), 100, 100, 90, pluto, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 9500), 100, 100, 100, pluto, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(34), 1), 50, 100, 1, charon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(34), 3), 75, 100, 3, charon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(4), 1), 20, 100, 7, charon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(4), 2), 35, 100, 12, charon, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(4), 3), 40, 100, 20, charon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(4), 1), 20, 100, 20, charon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(4), 1), 40, 100, 30, charon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(4), 2), 20, 100, 40, charon, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(4), 1), 20, 100, 55, charon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(4), 4), 40, 100, 65, charon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(8), 1), 40, 100, 70, charon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(9), 1), 40, 100, 70, charon, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidpropylene.getInstance().get(), 1000), 40, 100, 70, charon, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidethylene.getInstance().get(), 1000), 50, 100, 70, charon, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(8), 1), 60, 100, 80, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(9), 1), 60, 100, 80, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(8), 2), 80, 100, 90, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(9), 2), 80, 100, 90, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(8), 2), 100, 100, 100, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(9), 2), 100, 100, 100, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidpropylene.getInstance().get(), 5000), 90, 100, 90, charon, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidethylene.getInstance().get(), 5000), 90, 100, 90, charon,
                EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(38), 1), 50, 100, 1, eris, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(38), 1), 20, 100, 7, eris, EnumTypeRovers.ROVERS);
        ;
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(8), 1), 20, 100, 20, eris, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(8), 2), 20, 100, 40, eris, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(8), 2), 40, 100, 65, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(14), 1), 40, 100, 70, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(15), 1), 40, 100, 70, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 1000), 40, 100, 70, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidchlorum.getInstance().get(), 1000), 50, 100, 70, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 1000), 50, 100, 70, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 1000), 50, 100, 70, eris, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(14), 1), 60, 100, 80, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(15), 1), 60, 100, 80, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 3000), 90, 100, 90, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 3000), 90, 100, 90, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 3000), 90, 100, 90, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(14), 2), 100, 100, 100, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore.getItemStack(15), 2), 100, 100, 100, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidfluor.getInstance().get(), 5000), 90, 100, 100, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidchlorum.getInstance().get(), 5000), 90, 100, 100, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidbromine.getInstance().get(), 5000), 90, 100, 100, eris, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidiodine.getInstance().get(), 3000), 90, 100, 100, eris, EnumTypeRovers.ROCKET);


        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(43), 1), 50, 100, 1, makemake, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(43), 1), 20, 100, 7, makemake, EnumTypeRovers.ROVERS);
        ;
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(13), 1), 20, 100, 20, makemake, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(13), 2), 20, 100, 40, makemake, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(13), 1), 40, 100, 65, makemake, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(12), 1), 40, 100, 70, makemake, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(13), 1), 40, 100, 70, makemake, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(14), 1), 40, 100, 70, makemake, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 1000), 40, 100, 70, makemake, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 1000), 50, 100, 70, makemake, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(12), 1), 60, 100, 80, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(13), 1), 60, 100, 80, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(14), 1), 60, 100, 80, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 3000), 90, 100, 90, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 3000), 90, 100, 90, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(12), 2), 100, 100, 100, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(13), 2), 100, 100, 100, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(14), 2), 100, 100, 100, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 5000), 90, 100, 100, makemake, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 5000), 90, 100, 100, makemake, EnumTypeRovers.ROCKET);

        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(41), 1), 50, 100, 1, haumea, EnumTypeRovers.ROVERS);
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(41), 1), 20, 100, 7, haumea, EnumTypeRovers.ROVERS);
        ;
        new BaseResource(new ItemStack(IUItem.spaceItem.getStack(11), 1), 20, 100, 20, haumea, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_cobblestone.getItemStack(11), 2), 20, 100, 40, haumea, EnumTypeRovers.PROBE);
        new BaseResource(new ItemStack(IUItem.space_stone.getItemStack(11), 1), 40, 100, 65, haumea, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 1000), 40, 100, 70, haumea, EnumTypeRovers.SATELLITE);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 1000), 50, 100, 70, haumea, EnumTypeRovers.SATELLITE);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(5), 2), 60, 100, 80, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(6), 2), 60, 100, 80, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(7), 2), 60, 100, 80, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(8), 2), 60, 100, 80, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(9), 2), 60, 100, 80, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 3000), 90, 100, 90, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 3000), 90, 100, 90, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(5), 4), 100, 100, 100, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(6), 4), 100, 100, 100, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(7), 4), 100, 100, 100, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(8), 4), 100, 100, 100, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new ItemStack(IUItem.space_ore1.getItemStack(9), 4), 100, 100, 100, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluidxenon.getInstance().get(), 5000), 90, 100, 100, haumea, EnumTypeRovers.ROCKET);
        new BaseResource(new FluidStack(FluidName.fluiddecane.getInstance().get(), 5000), 90, 100, 100, haumea, EnumTypeRovers.ROCKET);

        SpaceNet.instance.getColonieNet().addItemStack(mercury, (short) 10, new ItemStack(IUItem.space_ore2.getItemStack(1), 1));
        SpaceNet.instance.getColonieNet().addItemStack(mercury, (short) 20, new ItemStack(IUItem.space_ore2.getItemStack(2), 1));
        SpaceNet.instance.getColonieNet().addItemStack(venus, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(12), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(venus, (short) 10,
                new FluidStack(FluidName.fluidhydrogensulfide.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addItemStack(mars, (short) 15, new ItemStack(IUItem.space_ore2.getItemStack(0), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(mars, (short) 10,
                new FluidStack(FluidName.fluidnitrogen.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addItemStack(mars, (short) 25, new ItemStack(IUItem.space_ore1.getItemStack(15), 1));
        SpaceNet.instance.getColonieNet().addItemStack(ceres, (short) 10, new ItemStack(IUItem.space_ore.getItemStack(7), 1));
        SpaceNet.instance.getColonieNet().addItemStack(ceres, (short) 15, new ItemStack(IUItem.space_ore.getItemStack(6), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(ceres, (short) 20,
                new FluidStack(FluidName.fluidacetylene.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(moon, (short) 10, new ItemStack(IUItem.space_ore2.getItemStack(7), 1));
        SpaceNet.instance.getColonieNet().addItemStack(moon, (short) 25, new ItemStack(IUItem.space_ore2.getItemStack(6), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(moon, (short) 17,
                new FluidStack(FluidName.fluidhydrogen.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(deimos, (short) 25, new ItemStack(IUItem.space_ore.getItemStack(10), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(deimos, (short) 15,
                new FluidStack(FluidName.fluidhelium.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(phobos, (short) 10, new ItemStack(IUItem.space_ore2.getItemStack(10), 1));
        SpaceNet.instance.getColonieNet().addItemStack(phobos, (short) 15, new ItemStack(IUItem.space_ore2.getItemStack(11), 1));
        SpaceNet.instance.getColonieNet().addItemStack(io, (short) 10, new ItemStack(IUItem.space_ore1.getItemStack(10), 1));
        SpaceNet.instance.getColonieNet().addItemStack(io, (short) 15, new ItemStack(IUItem.space_ore1.getItemStack(11), 1));
        SpaceNet.instance.getColonieNet().addItemStack(callisto, (short) 10, new ItemStack(IUItem.space_ore.getItemStack(3), 1));
        SpaceNet.instance.getColonieNet().addItemStack(callisto, (short) 15, new ItemStack(IUItem.space_ore.getItemStack(5), 1));
        SpaceNet.instance.getColonieNet().addItemStack(callisto, (short) 25, new ItemStack(IUItem.space_ore.getItemStack(4), 1));


        SpaceNet.instance.getColonieNet().addItemStack(ganymede, (short) 10, new ItemStack(IUItem.space_ore1.getItemStack(3), 1));
        SpaceNet.instance.getColonieNet().addItemStack(ganymede, (short) 25, new ItemStack(IUItem.space_ore1.getItemStack(2), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(ganymede, (short) 17,
                new FluidStack(FluidName.fluidcarbonmonoxide.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(europe, (short) 10, new ItemStack(IUItem.space_ore1.getItemStack(4), 1));
        SpaceNet.instance.getColonieNet().addItemStack(europe, (short) 15, new ItemStack(IUItem.space_ore1.getItemStack(1), 1));
        SpaceNet.instance.getColonieNet().addItemStack(europe, (short) 22, new ItemStack(IUItem.space_ore1.getItemStack(0), 1));

        SpaceNet.instance.getColonieNet().addItemStack(enceladus, (short) 10, new ItemStack(IUItem.space_ore.getItemStack(13), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(enceladus, (short) 17,
                new FluidStack(FluidName.fluidacetylene.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(enceladus, (short) 22,
                new FluidStack(FluidName.fluidhelium.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(titan, (short) 10, new ItemStack(IUItem.space_ore3.getItemStack(5), 1));
        SpaceNet.instance.getColonieNet().addItemStack(titan, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(6), 1));

        SpaceNet.instance.getColonieNet().addItemStack(dione, (short) 10, new ItemStack(IUItem.space_ore.getItemStack(12), 1));
        SpaceNet.instance.getColonieNet().addItemStack(dione, (short) 15, new ItemStack(IUItem.space_ore.getItemStack(11), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(dione, (short) 22,
                new FluidStack(FluidName.fluidmethane.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(mimas, (short) 22, new ItemStack(IUItem.space_ore2.getItemStack(3), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(mimas, (short) 17,
                new FluidStack(FluidName.fluidpropane.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(mimas, (short) 26,
                new FluidStack(FluidName.fluidbutene.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(rhea, (short) 10, new ItemStack(IUItem.space_ore3.getItemStack(0), 1));
        SpaceNet.instance.getColonieNet().addItemStack(rhea, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(1), 1));
        SpaceNet.instance.getColonieNet().addItemStack(rhea, (short) 20, new ItemStack(IUItem.space_ore3.getItemStack(2), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(rhea, (short) 25,
                new FluidStack(FluidName.fluidnitrogenhydride.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(tethys, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(3), 1));
        SpaceNet.instance.getColonieNet().addItemStack(tethys, (short) 20, new ItemStack(IUItem.space_ore3.getItemStack(4), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(tethys, (short) 25,
                new FluidStack(FluidName.fluiddecane.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(titania, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(7), 1));
        SpaceNet.instance.getColonieNet().addItemStack(titania, (short) 20, new ItemStack(IUItem.space_ore3.getItemStack(8), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(titania, (short) 25,
                new FluidStack(FluidName.fluidchlorum.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(umbriel, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(11), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(umbriel, (short) 10,
                new FluidStack(FluidName.fluidfluor.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(umbriel, (short) 20,
                new FluidStack(FluidName.fluidhelium.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(umbriel, (short) 25,
                new FluidStack(FluidName.fluidmethane.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(oberon, (short) 15, new ItemStack(IUItem.space_ore2.getItemStack(8), 1));
        SpaceNet.instance.getColonieNet().addItemStack(oberon, (short) 20, new ItemStack(IUItem.space_ore2.getItemStack(9), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(oberon, (short) 25,
                new FluidStack(FluidName.fluidethane.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(ariel, (short) 22, new ItemStack(IUItem.space_ore.getItemStack(0), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(ariel, (short) 16,
                new FluidStack(FluidName.fluidbromine.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(ariel, (short) 26,
                new FluidStack(FluidName.fluidethylene.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(miranda, (short) 10, new ItemStack(IUItem.space_ore2.getItemStack(4), 1));
        SpaceNet.instance.getColonieNet().addItemStack(miranda, (short) 20, new ItemStack(IUItem.space_ore2.getItemStack(5), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(miranda, (short) 15,
                new FluidStack(FluidName.fluidsulfurtrioxide.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(miranda, (short) 25,
                new FluidStack(FluidName.fluidnitrogenoxy.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(triton, (short) 10, new ItemStack(IUItem.space_ore3.getItemStack(9), 1));
        SpaceNet.instance.getColonieNet().addItemStack(triton, (short) 15, new ItemStack(IUItem.space_ore3.getItemStack(10), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(triton, (short) 20,
                new FluidStack(FluidName.fluidiodine.getInstance().get(), 1));


        SpaceNet.instance.getColonieNet().addItemStack(proteus, (short) 10, new ItemStack(IUItem.space_ore2.getItemStack(14), 1));
        SpaceNet.instance.getColonieNet().addItemStack(proteus, (short) 15, new ItemStack(IUItem.space_ore2.getItemStack(15), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(proteus, (short) 25,
                new FluidStack(FluidName.fluidxenon.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(pluto, (short) 10, new ItemStack(IUItem.space_ore2.getItemStack(13), 1));
        SpaceNet.instance.getColonieNet().addItemStack(pluto, (short) 22, new ItemStack(IUItem.space_ore2.getItemStack(12), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(pluto, (short) 16,
                new FluidStack(FluidName.fluidnitricacid.getInstance().get(), 1));


        SpaceNet.instance.getColonieNet().addItemStack(charon, (short) 10, new ItemStack(IUItem.space_ore.getItemStack(9), 1));
        SpaceNet.instance.getColonieNet().addItemStack(charon, (short) 20, new ItemStack(IUItem.space_ore.getItemStack(8), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(charon, (short) 15,
                new FluidStack(FluidName.fluidethylene.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(charon, (short) 25,
                new FluidStack(FluidName.fluidpropylene.getInstance().get(), 1));


        SpaceNet.instance.getColonieNet().addItemStack(eris, (short) 10, new ItemStack(IUItem.space_ore.getItemStack(14), 1));
        SpaceNet.instance.getColonieNet().addItemStack(eris, (short) 20, new ItemStack(IUItem.space_ore.getItemStack(15), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(eris, (short) 25,
                new FluidStack(FluidName.fluidchlorum.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(eris, (short) 15,
                new FluidStack(FluidName.fluidfluor.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(eris, (short) 30,
                new FluidStack(FluidName.fluidbromine.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(eris, (short) 35,
                new FluidStack(FluidName.fluidiodine.getInstance().get(), 1));


        SpaceNet.instance.getColonieNet().addItemStack(makemake, (short) 25, new ItemStack(IUItem.space_ore1.getItemStack(12), 1));
        SpaceNet.instance.getColonieNet().addItemStack(makemake, (short) 10, new ItemStack(IUItem.space_ore1.getItemStack(13), 1));
        SpaceNet.instance.getColonieNet().addItemStack(makemake, (short) 15, new ItemStack(IUItem.space_ore1.getItemStack(14), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(makemake, (short) 20,
                new FluidStack(FluidName.fluidsulfuricacid.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(makemake, (short) 35,
                new FluidStack(FluidName.fluiddecane.getInstance().get(), 1));

        SpaceNet.instance.getColonieNet().addItemStack(haumea, (short) 25, new ItemStack(IUItem.space_ore1.getItemStack(5), 1));
        SpaceNet.instance.getColonieNet().addItemStack(haumea, (short) 30, new ItemStack(IUItem.space_ore1.getItemStack(6), 1));
        SpaceNet.instance.getColonieNet().addItemStack(haumea, (short) 15, new ItemStack(IUItem.space_ore1.getItemStack(7), 1));
        SpaceNet.instance.getColonieNet().addItemStack(haumea, (short) 20, new ItemStack(IUItem.space_ore1.getItemStack(8), 1));
        SpaceNet.instance.getColonieNet().addItemStack(haumea, (short) 10, new ItemStack(IUItem.space_ore1.getItemStack(9), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(haumea, (short) 35,
                new FluidStack(FluidName.fluiddecane.getInstance().get(), 1));
        SpaceNet.instance.getColonieNet().addFluidStack(haumea, (short) 50,
                new FluidStack(FluidName.fluidxenon.getInstance().get(), 1));
    }


    public static ResourceLocation getTexture(String name) {


        return new ResourceLocation(
                Constants.TEXTURES,
                "textures/planet/" + name + ".png"
        );
    }

}
