package com.denfop.blocks;

import com.denfop.Constants;
import com.denfop.Localization;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public enum FluidName implements ISubEnum {

    fluidNeutron,
    fluidHelium,
    fluidbenz,
    fluiddizel,
    fluidneft,
    fluidsweet_medium_oil(),
    fluidsweet_heavy_oil(),
    fluidsour_light_oil(),
    fluidsour_medium_oil(),
    fluidsour_heavy_oil(),
    fluidpolyeth,
    fluidpolyprop,
    fluidoxy,
    fluidhyd,
    fluidfluorhyd(),
    fluidazot,
    fluidco2,
    fluidgas,
    fluidpropane(),
    fluidacetylene(),
    fluidethylene(),
    fluidpropylene(),
    fluidmethane(),
    fluiddibromopropane(),
    fluidchlorum,
    fluidfluor,
    fluidbromine,
    fluidiodine,
    fluidnitrogenoxy(),
    fluidair(false),
    fluidbiogas(false),
    fluidbiomass,
    fluidconstruction_foam,

    fluidcoolant,
    fluiddistilled_water,
    fluidweed_ex(false),
    fluidglowstone,
    fluidorthophosphoricacid,
    fluidhot_coolant,
    fluidhot_water,
    fluidpahoehoe_lava(),
    fluidsteam(false),
    fluidsuperheated_steam(false),
    fluiduu_matter,
    fluidwater,
    fluidlava,
    fluidcryogen,
    fluidazurebrilliant,
    fluidrawlatex,
    fluidcreosote,
    fluidblackoil,
    fluidindustrialoil,
    fluidmotoroil,
    fluidsulfuricacid,
    fluidwastesulfuricacid,
    fluidaniline(fluidpolyprop),
    fluidmethylsulfate(fluidpolyprop),
    fluidmethyltrichloroaniline(fluidpolyprop),
    fluidtrichloroaniline(fluidpolyprop),
    fluidsulfuroxide(),
    fluidsulfurtrioxide(),
    fluidhydrogensulfide(),
    fluidnitricoxide(),
    fluidcoppersulfate(),

    fluidapianroyaljelly,
    fluidprotein,
    fluidbeeswax,
    fluidseedoil,
    fluidbacteria,
    fluidplantmixture,
    fluidbeerna,
    fluidcroprna,
    fluidbeedna,
    fluidcropdna,
    fluidunstablemutagen,
    fluidmutagen,
    fluidbeegenetic,
    fluidcropgenetic,

    fluidiron(),
    fluidtitanium(),
    fluidirontitanium(),
    fluidnickel(),
    fluidcarbon(),
    fluidtitaniumsteel(),
    fluidsteel(),
    fluidgold(),
    fluidsilver(),
    fluidelectrum(),
    fluidinvar(),
    fluidcopper(),
    fluidtin(),
    fluidtungsten(),
    fluidwolframite,
    fluidbronze(),
    fluidgallium(),
    fluidarsenicum(),
    fluidaluminium(),
    fluidmanganese,
    fluidferromanganese,
    fluidaluminiumbronze(),
    fluidarsenicum_gallium(),
    fluidnitrogenhydride(),
    fluidnitrogendioxide(),
    fluidnitricacid(),


    fluidbenzene,

    fluidtoluene,
    fluidmethylbromide,
    fluidmethylchloride(fluidpolyeth),
    fluidhydrogenbromide,
    fluidtrinitrotoluene,

    fluidethane(fluidpolyeth),
    fluidethanol,
    fluidbutadiene,
    fluidpolybutadiene,
    fluidacrylonitrile,
    fluidpolyacrylonitrile,
    fluidbutadiene_nitrile,

    fluidhydrogenchloride,
    fluidchloroethane,
    fluidtetraethyllead,
    fluidpetrol90,
    fluidpetrol95,
    fluidcarbonmonoxide,
    fluidmethanol,
    fluidbutene,
    fluidtertbutylsulfuricacid,
    fluidtertbutylalcohol,
    fluidisobutylene,
    fluidtertbutylmethylether,
    fluidmonochlorobenzene(fluidpolyeth),
    fluidpetrol100,
    fluidpetrol105,

    fluidmethylpentane,
    fluidmethylpentanal,
    fluidethylhexanol,
    fluidethylhexylnitrate,
    fluida_diesel,
    fluidaa_diesel,
    fluidaaa_diesel,
    fluidaaaa_diesel,

    fluidcyclohexane,
    fluidmethylcyclohexane,
    fluidmethylcyclohexylnitrate,
    fluidquartz,
    fluidchromium,
    fluidnichrome,
    fluidmagnesium,
    fluidduralumin,
    fluidobsidian,
    fluidhoney,
    fluidroyaljelly,
    fluidtemperedglass(fluidquartz),
    fluidmoltenmikhail,
    fluidmoltenaluminium,
    fluidmoltenvanadium,
    fluidmoltentungsten,
    fluidmoltencobalt,
    fluidmoltenmagnesium,
    fluidmoltennickel,
    fluidmoltenplatinum,
    fluidmoltentitanium,
    fluidmoltenchromium,
    fluidmoltenspinel,
    fluidmoltensilver,
    fluidmoltenzinc,
    fluidmoltenmanganese,
    fluidmolteniridium,
    fluidmoltengermanium,
    fluidmoltencopper,
    fluidmoltengold,
    fluidmolteniron,
    fluidmoltenlead,
    fluidmoltentin,
    fluidmoltenuranium,
    fluidmoltenosmium,
    fluidmoltentantalum,
    fluidmoltencadmium,
    fluidmoltenarsenic,
    fluidmoltenbarium,
    fluidmoltenbismuth,
    fluidmoltengadolinium,
    fluidmoltengallium,
    fluidmoltenhafnium,
    fluidmoltenyttrium,
    fluidmoltenmolybdenum,
    fluidmoltenneodymium,
    fluidmoltenniobium,
    fluidmoltenpalladium,
    fluidmoltenpolonium,
    fluidmoltenstrontium,
    fluidmoltenthallium,
    fluidmoltenzirconium,

    fluidhydrazine(fluidbenzene),
    fluiddimethylhydrazine(fluidbenzene),
    fluiddecane(fluidbenzene),
    fluidxenon(fluidbenzene),
    fluidpropionic_acid(fluidbenzene),
    fluidacetic_acid(fluidbenzene),
    fluidglucose(fluidbenzene),
    fluidsodiumhydroxide(fluidbenzene),
    fluidsodium_hypochlorite(fluidbenzene),
    ;
    public static final FluidName[] values = values();
    private final boolean hasFlowTexture;
    private FluidName fluidname;
    private Fluid instance;

    FluidName() {
        this(true);
    }

    FluidName(FluidName fluidName) {
        this(true);
        this.fluidname = fluidName;
    }
    FluidName(boolean hasFlowTexture) {
        this.hasFlowTexture = hasFlowTexture;
    }

    public static Fluid isFluid(Fluid fluid) {
        for (FluidName fluidName : FluidName.values) {
            if (fluidName.getInstance() == fluid) {
                if (fluidName.fluidname != null) {
                    return fluidName.fluidname.getInstance();
                }
            }
        }
        return fluid;
    }

    public String getName() {
        return "iu" + this.name();
    }

    public int getId() {
        throw new UnsupportedOperationException();
    }

    public ResourceLocation getTextureLocation(boolean flowing) {
        if (fluidname == null) {
            if (this.name().startsWith("fluidmolten")) {
                return new ResourceLocation(Constants.MOD_ID, "blocks/fluid/molten_flow");
            } else {
                String type = flowing && this.hasFlowTexture ? "flow" : "still";
                return new ResourceLocation(Constants.MOD_ID, "blocks/fluid/" + this.name().substring(5) + "_" + type);
            }
        }else{
            if (fluidname.name().startsWith("fluidmolten")) {
                return new ResourceLocation(Constants.MOD_ID, "blocks/fluid/molten_flow");
            } else {
                String type = flowing && this.hasFlowTexture ? "flow" : "still";
                return new ResourceLocation(Constants.MOD_ID, "blocks/fluid/" + fluidname.name().substring(5) + "_" + type);
            }
        }
    }

    public boolean hasInstance() {
        return this.instance != null;
    }

    public Fluid getInstance() {
        if (this.instance == null) {
            throw new IllegalStateException("the requested fluid instance for " + this.name() + " isn't set (yet)");
        } else {
            return this.instance;
        }
    }

    public void setInstance(Fluid fluid) {
        if (fluid == null) {
            throw new NullPointerException("null fluid");
        } else if (this.instance != null) {
            throw new IllegalStateException("conflicting instance");
        } else {
            this.instance = fluid;
        }
    }

    public boolean canBeLava() {
        return this == fluidhot_coolant || this == fluidlava || this == fluidpahoehoe_lava;
    }
}
