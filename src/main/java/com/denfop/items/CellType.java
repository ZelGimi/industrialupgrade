package com.denfop.items;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.ISubEnum;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CellType implements ISubEnum {
    empty(0),
    neutron(1, FluidName.fluidNeutron.getInstance()),
    helium(2, FluidName.fluidHelium.getInstance()),
    benz(3, FluidName.fluidbenz.getInstance()),
    dizel(4, FluidName.fluiddizel.getInstance()),
    neft(5, FluidName.fluidneft.getInstance()),
    polyeth(6, FluidName.fluidpolyeth.getInstance()),
    polyprop(7, FluidName.fluidpolyprop.getInstance()),
    oxy(8, FluidName.fluidoxy.getInstance()),
    hyd(9, FluidName.fluidhyd.getInstance()),
    azot(10, FluidName.fluidazot.getInstance()),
    co2(11, FluidName.fluidco2.getInstance()),
    gas(12, FluidName.fluidgas.getInstance()),
    chlorum(13, FluidName.fluidchlorum.getInstance()),
    bromine(14, FluidName.fluidbromine.getInstance()),
    iodine(15, FluidName.fluidiodine.getInstance()),
    air(16, FluidName.fluidair.getInstance()),
    biogas(17, FluidName.fluidbiogas.getInstance()),
    biomass(18, FluidName.fluidbiomass.getInstance()),
    construction_foam(19, FluidName.fluidconstruction_foam.getInstance()),

    coolant(20, FluidName.fluidcoolant.getInstance()),
    distilled_water(21, FluidName.fluiddistilled_water.getInstance()),
    hot_coolant(22, FluidName.fluidhot_coolant.getInstance()),
    hot_water(23, FluidName.fluidhot_water.getInstance()),
    pahoehoe_lava(24, FluidName.fluidpahoehoe_lava.getInstance()),
    steam(25, FluidName.fluidsteam.getInstance()),
    superheated_steam(26, FluidName.fluidsuperheated_steam.getInstance()),
    uu_matter(27, FluidName.fluiduu_matter.getInstance()),
    water(28, FluidRegistry.WATER),
    lava(29, FluidRegistry.LAVA);
    final Fluid fluid;
    private final int id;

    CellType(int id) {
        this(id, null);
    }

    CellType(int id, Fluid fluid) {
        this.id = id;
        this.fluid = fluid;
    }

    public static void register() {
        for (CellType type : values()) {
            IUItem.celltype.put(type.fluid, type.id);
            IUItem.celltype1.put(type.id, type.fluid);
        }
    }

    public static CellType getFromID(int meta) {
        return values()[meta % values().length];
    }

    public Fluid getFluid() {
        return fluid;
    }

    public String getName() {
        return this.name();
    }

    public int getId() {
        return this.id;
    }

    public boolean isFluidContainer() {
        return this.fluid != null || this == empty;
    }

    public static class CellFluidHandler extends FluidBucketWrapper {

        private static final Map<Fluid, CellType> VALID_FLUIDS = new IdentityHashMap<>(Arrays
                .stream(CellType.values())
                .filter((type) -> type.fluid != null)
                .collect(Collectors.toMap(
                        type -> type.fluid,
                        Function.identity(),
                        (existing, replacement) -> existing
                )));
        protected final CellType typeGetter;

        public CellFluidHandler(ItemStack container, CellType typeGetter) {
            super(container);
            this.typeGetter = typeGetter;
        }

        public FluidStack getFluid() {
            CellType type = this.typeGetter;

            assert type.isFluidContainer();

            return type.fluid != null ? new FluidStack(type.fluid, 1000) : null;
        }

        protected void setFluid(FluidStack stack) {
            if (stack == null) {
                assert this.typeGetter != CellType.empty;

                this.container = new ItemStack(IUItem.cell_all);
            } else {
                assert this.typeGetter == CellType.empty;

                assert VALID_FLUIDS.containsKey(stack.getFluid());

                this.container = new ItemStack(IUItem.cell_all, 1, VALID_FLUIDS.get(stack.getFluid()).id);
            }

        }

        public boolean canFillFluidType(FluidStack fluid) {
            assert fluid != null;

            assert fluid.getFluid() != null;

            return this.typeGetter == CellType.empty && VALID_FLUIDS.containsKey(fluid.getFluid());
        }

    }
}
