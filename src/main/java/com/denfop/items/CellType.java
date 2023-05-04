package com.denfop.items;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.IIdProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public enum CellType implements IIdProvider {
    empty(0),
    neutron(1, FluidName.fluidNeutron.getInstance()),
    helium(2, FluidName.fluidHelium.getInstance()),
    benz(3, FluidName.fluidbenz.getInstance()),
    dizel(4, FluidName.fluiddizel.getInstance()),
    neft(5, FluidName.fluidneft.getInstance()),
    polyeth(6, FluidName.fluidpolyeth.getInstance()),
    polyprop(7, FluidName.fluidpolyprop.getInstance()),
    oxy(8, FluidName.fluidoxy.getInstance()),
    hyd(9, FluidName.fluidhyd.getInstance());

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

    public String getName() {
        return this.name();
    }

    public int getId() {
        return this.id;
    }

    public int getStackSize() {
        return 64;
    }

    public boolean isFluidContainer() {
        return this.fluid != null || this == empty;
    }

    public boolean hasCropAction() {
        return false;
    }

    public int getUsage() {


        return 0;

    }

    public int getMaximum() {
        return 0;
    }

    public EnumActionResult doCropAction() {
        assert this.hasCropAction();

        throw new IllegalStateException("Type was " + this);
    }

    public static class CellFluidHandler extends FluidBucketWrapper {

        private static final Map<Fluid, CellType> VALID_FLUIDS = new IdentityHashMap<>(Arrays
                .stream(CellType.values())
                .filter((type) -> type.fluid != null)
                .collect(Collectors.toMap((type) -> type.fluid, Function.identity())));
        protected final Supplier<CellType> typeGetter;

        public CellFluidHandler(ItemStack container, Function<ItemStack, CellType> typeGetter) {
            super(container);
            this.typeGetter = () -> (CellType) typeGetter.apply(this.container);
        }

        public FluidStack getFluid() {
            CellType type = this.typeGetter.get();

            assert type.isFluidContainer();

            return type.fluid != null ? new FluidStack(type.fluid, 1000) : null;
        }

        protected void setFluid(FluidStack stack) {
            if (stack == null) {
                assert this.typeGetter.get() != CellType.empty;

                this.container = new ItemStack(IUItem.cell_all);
            } else {
                assert this.typeGetter.get() == CellType.empty;

                assert VALID_FLUIDS.containsKey(stack.getFluid());

                this.container = new ItemStack(IUItem.cell_all, 1, VALID_FLUIDS.get(stack.getFluid()).id);
            }

        }

        public boolean canFillFluidType(FluidStack fluid) {
            assert fluid != null;

            assert fluid.getFluid() != null;

            return this.typeGetter.get() == CellType.empty && VALID_FLUIDS.containsKey(fluid.getFluid());
        }

    }
}
