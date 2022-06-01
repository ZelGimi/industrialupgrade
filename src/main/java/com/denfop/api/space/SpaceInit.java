package com.denfop.api.space;

import com.denfop.Constants;
import net.minecraft.util.ResourceLocation;

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

    public static void init() {
        solarSystem = new System("solarsystem");
        sextantis24 = new System("24sextantis");
        cancri55 = new System("55cancri");
        kapteyn = new System("kapteyn");
        kepler148 = new System("kepler148");
        kepler149 = new System("kepler149");
        kepler186 = new System("kepler186");
        sun = new Star("sun", solarSystem, getTexture("sun"));
        mercury = new Planet(
                "mercury",
                solarSystem,
                getTexture("mercury"),
                EnumLevels.SECOND,
                sun,
                167,
                false,
                0.3,
                EnumType.NEUTRAL,
                false,
                true
        );
        venus = new Planet(
                "venus",
                solarSystem,
                getTexture("venus"),
                EnumLevels.FIRST,
                sun,
                464,
                true,
                0.65,
                EnumType.DANGEROUS,
                false,
                false
        );
        earth = new Planet(
                "earth",
                solarSystem,
                getTexture("earth"),
                EnumLevels.NONE,
                sun,
                20,
                false,
                1,
                EnumType.SAFE,
                true,
                false
        );
        moon = new Satellite(
                "moon",
                solarSystem,
                getTexture("moon"),
                EnumLevels.FIRST,
                earth,
                -20,
                false,
                1,
                EnumType.NEUTRAL,
                false,
                true
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
                true
        );
        deimos = new Satellite(
                "deimos",
                solarSystem,
                getTexture("deimos"),
                EnumLevels.FIRST,
                mars,
                -65,
                false,
                1.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        phobos = new Satellite(
                "phobos",
                solarSystem,
                getTexture("phobos"),
                EnumLevels.FIRST,
                mars,
                -65,
                false,
                1.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        ceres = new Planet(
                "ceres",
                solarSystem,
                getTexture("ceres"),
                EnumLevels.SECOND,
                sun,
                -45,
                false,
                2,
                EnumType.NEUTRAL,
                false,
                true
        );
        jupiter = new Planet(
                "jupiter",
                solarSystem,
                getTexture("jupiter"),
                EnumLevels.NONE,
                sun,
                -110,
                false,
                2.5,
                EnumType.DANGEROUS,
                false,
                false
        );
        europe = new Satellite(
                "europe",
                solarSystem,
                getTexture("europe"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                2.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        ganymede = new Satellite(
                "ganymede",
                solarSystem,
                getTexture("ganymede"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                2.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        io = new Satellite(
                "io",
                solarSystem,
                getTexture("io"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                2.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        callisto = new Satellite(
                "callisto",
                solarSystem,
                getTexture("callisto"),
                EnumLevels.SECOND,
                jupiter,
                -110,
                false,
                2.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        saturn = new Planet(
                "saturn",
                solarSystem,
                getTexture("saturn"),
                EnumLevels.NONE,
                sun,
                -140,
                false,
                3.5,
                EnumType.DANGEROUS,
                false,
                false
        );
        enceladus = new Satellite(
                "enceladus",
                solarSystem,
                getTexture("enceladus"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                3.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        titan = new Satellite(
                "titan",
                solarSystem,
                getTexture("titan"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                3.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        dione = new Satellite(
                "dione",
                solarSystem,
                getTexture("dione"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                3.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        mimas = new Satellite(
                "mimas",
                solarSystem,
                getTexture("mimas"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                3.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        rhea = new Satellite(
                "rhea",
                solarSystem,
                getTexture("rhea"),
                EnumLevels.THIRD,
                saturn,
                -140,
                false,
                3.5,
                EnumType.NEUTRAL,
                false,
                true
        );
        uranus = new Planet(
                "uranus",
                solarSystem,
                getTexture("uranus"),
                EnumLevels.NONE,
                sun,
                -195,
                false,
                4.25,
                EnumType.DANGEROUS,
                false,
                false
        );
        titania = new Satellite(
                "titania",
                solarSystem,
                getTexture("titania"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                4.25,
                EnumType.NEUTRAL,
                false,
                true
        );
        umbriel = new Satellite(
                "umbriel",
                solarSystem,
                getTexture("umbriel"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                4.25,
                EnumType.NEUTRAL,
                false,
                true
        );
        oberon = new Satellite(
                "oberon",
                solarSystem,
                getTexture("oberon"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                4.25,
                EnumType.NEUTRAL,
                false,
                true
        );
        ariel = new Satellite(
                "ariel",
                solarSystem,
                getTexture("ariel"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                4.25,
                EnumType.NEUTRAL,
                false,
                true
        );
        miranda = new Satellite(
                "miranda",
                solarSystem,
                getTexture("miranda"),
                EnumLevels.FOURTH,
                uranus,
                -195,
                false,
                4.25,
                EnumType.NEUTRAL,
                false,
                true
        );
        neptune = new Planet(
                "neptune",
                solarSystem,
                getTexture("neptune"),
                EnumLevels.NONE,
                sun,
                -220,
                false,
                4.75,
                EnumType.DANGEROUS,
                false,
                false
        );
        triton = new Satellite(
                "triton",
                solarSystem,
                getTexture("triton"),
                EnumLevels.FIVE,
                neptune,
                -220,
                false,
                4.75,
                EnumType.NEUTRAL,
                false,
                true
        );
        proteus = new Satellite(
                "proteus",
                solarSystem,
                getTexture("proteus"),
                EnumLevels.FIVE,
                neptune,
                -220,
                false,
                4.75,
                EnumType.NEUTRAL,
                false,
                true
        );
        proteus = new Satellite(
                "proteus",
                solarSystem,
                getTexture("proteus"),
                EnumLevels.FIVE,
                neptune,
                -220,
                false,
                4.75,
                EnumType.NEUTRAL,
                false,
                true
        );
        pluto = new Planet(
                "pluto",
                solarSystem,
                getTexture("pluto"),
                EnumLevels.SIX,
                sun,
                -247,
                false,
                5.25,
                EnumType.NEUTRAL,
                false,
                false
        );
        charon = new Satellite(
                "charon",
                solarSystem,
                getTexture("charon"),
                EnumLevels.SIX,
                pluto,
                -247,
                false,
                5.25,
                EnumType.NEUTRAL,
                false,
                true
        );

    }

    public static ResourceLocation getTexture(String name) {


        return new ResourceLocation(
                Constants.TEXTURES,
                "textures/body/" + name + ".png"
        );
    }

}
