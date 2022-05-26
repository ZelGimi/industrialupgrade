package com.denfop.items;

import com.denfop.IUItem;
import com.denfop.blocks.FluidName;
import ic2.core.block.state.IIdProvider;
import ic2.core.crop.TileEntityCrop;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
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

    public int getUsage(ItemStack stack) {


        return 0;

    }

    public int getMaximum(ItemStack stack) {
        return 0;
    }

    public EnumActionResult doCropAction(ItemStack stack, Consumer<ItemStack> result, TileEntityCrop crop, boolean manual) {
        assert this.hasCropAction();

        throw new IllegalStateException("Type was " + this);
    }

    private static class HydrationHandler implements IFluidHandlerItem {

        public static final String NBT = "hydration";
        public static final int CHARGES = 10000;
        protected final boolean manual;
        protected ItemStack container;

        public HydrationHandler(ItemStack stack, boolean manual) {
            this.container = stack;
            this.manual = manual;
        }

        public ItemStack getContainer() {
            return this.container;
        }

        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return resource != null && resource.getFluid() == FluidRegistry.WATER ? this.drain(resource.amount, doDrain) : null;
        }

        public FluidStack drain(int maxDrain, boolean doDrain) {
            int remaining;
            if (this.container.hasTagCompound()) {
                remaining = 10000 - this.container.getTagCompound().getInteger("hydration");
            } else {
                remaining = 10000;
            }

            int target = Math.min(maxDrain, remaining);
            if (!this.manual && target > 180) {
                target = 180;
            }

            if (doDrain) {
                NBTTagCompound nbt = StackUtil.getOrCreateNbtData(this.container);
                int amount = nbt.getInteger("hydration") + target;
                if (amount >= 10000) {
                    this.container = StackUtil.decSize(this.container);
                } else {
                    nbt.setInteger("hydration", amount);
                }
            }

            return new FluidStack(FluidRegistry.WATER, target);
        }

        public IFluidTankProperties[] getTankProperties() {
            return new IFluidTankProperties[]{new FluidTankProperties(new FluidStack(
                    FluidRegistry.WATER,
                    (10000 - this.container.getItemDamage()) / 200
            ), 50, false, true)};
        }

    }

    private static class WeedExHandler implements IFluidHandlerItem {

        public static final String NBT = "weedEX";
        public static final int CHARGES = 64;
        private static final int DRAIN = 50;
        protected ItemStack container;

        public WeedExHandler(ItemStack stack) {
            this.container = stack;
        }

        public ItemStack getContainer() {
            return this.container;
        }

        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        public FluidStack drain(FluidStack resource, boolean doDrain) {
            return null;
        }

        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }

        public IFluidTankProperties[] getTankProperties() {
            return null;
        }

    }

    public static class CellFluidHandler extends FluidBucketWrapper {

        private static final Map<Fluid, CellType> VALID_FLUIDS = new IdentityHashMap(Arrays
                .stream(CellType.values()).filter((type) -> {
                    return type.fluid != null;
                }).collect(Collectors.toMap((type) -> {
                    return type.fluid;
                }, Function.identity())));
        protected final Supplier<CellType> typeGetter;

        public CellFluidHandler(ItemStack container, Function<ItemStack, CellType> typeGetter) {
            super(container);
            this.typeGetter = () -> {
                return (CellType) typeGetter.apply(this.container);
            };
        }

        public FluidStack getFluid() {
            CellType type = this.typeGetter.get();

            assert type.isFluidContainer();

            return type != null && type.fluid != null ? new FluidStack(type.fluid, 1000) : null;
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
