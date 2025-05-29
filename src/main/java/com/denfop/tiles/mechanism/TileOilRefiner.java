package com.denfop.tiles.mechanism;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.IUpgradableBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerOilRefiner;
import com.denfop.gui.GuiOilRefiner;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.render.tank.DataFluid;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableObject;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class TileOilRefiner extends TileElectricMachine implements IManufacturerBlock, IUpgradableBlock, IHasRecipe {

    public final Fluids.InternalFluidTank fluidTank2;
    public final Fluids.InternalFluidTank fluidTank1;
    public final Fluids.InternalFluidTank fluidTank3;
    public final FluidHandlerRecipe fluid_handler;
    public final InvSlotFluidByList fluidSlot1;
    public final InvSlotFluidByList fluidSlot2;
    public final InvSlotFluidByList fluidSlot3;
    public final InvSlotUpgrade upgradeSlot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    private final Fluids fluids;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid1;
    @SideOnly(Side.CLIENT)
    public DataFluid dataFluid2;
    private boolean needUpdate;
    private int level;

    public TileOilRefiner() {
        super(24000, 14, 2);
        this.fluids = this.addComponent(new Fluids(this));
        this.fluidTank1 = fluids.addTank("fluidTank1", 12 * 1000, InvSlot.TypeItemSlot.INPUT);
        this.needUpdate = false;

        this.fluidTank2 = fluids.addTank("fluidTank2", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);

        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
        this.fluidTank3 = fluids.addTank("fluidTank3", 12 * 1000, InvSlot.TypeItemSlot.OUTPUT);
        this.fluid_handler = new FluidHandlerRecipe("oil_refiner", fluids);
        this.fluidTank1.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getFluids(0)));
        this.fluidTank2.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(0)));
        this.fluidTank3.setAcceptedFluids(Fluids.fluidPredicate(this.fluid_handler.getOutputFluids(1)));
        this.fluidSlot1 = new InvSlotFluidByList(this, 1, this.fluid_handler.getFluids(0));
        this.fluidSlot2 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(0));
        this.fluidSlot3 = new InvSlotFluidByList(this, 1, this.fluid_handler.getOutputFluids(1));
        this.fluidSlot2.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.fluidSlot3.setTypeFluidSlot(InvSlotFluid.TypeFluidSlot.OUTPUT);
        this.upgradeSlot = new com.denfop.invslot.InvSlotUpgrade(this, 4);
        Recipes.recipes.getRecipeFluid().addInitRecipes(this);
    }
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound nbttagcompound) {
        NBTTagCompound compound = super.writeToNBT(nbttagcompound);
        compound.setInteger("levelMech",level);
        return compound;
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        level = nbttagcompound.getInteger("levelMech");
    }
    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fluidtank1")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank2")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank2.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank3")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank3.readFromNBT(fluidTank1.writeToNBT(new NBTTagCompound()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<AxisAlignedBB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2D, 1.0D));

    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidneft.getInstance(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance(),
                        8
                ), new FluidStack(FluidName.fluiddizel.getInstance(), 2))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsweet_heavy_oil.getInstance(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluiddizel.getInstance(),
                        8
                ), new FluidStack(FluidName.fluidbenz.getInstance(), 2))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsweet_medium_oil.getInstance(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance(),
                        6
                ), new FluidStack(FluidName.fluiddizel.getInstance(), 4))));

        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsour_light_oil.getInstance(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance(),
                        5
                ), new FluidStack(FluidName.fluiddizel.getInstance(), 1))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsour_heavy_oil.getInstance(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluiddizel.getInstance(),
                        5
                ), new FluidStack(FluidName.fluidbenz.getInstance(), 1))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsour_medium_oil.getInstance(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance(),
                        4
                ), new FluidStack(FluidName.fluiddizel.getInstance(), 2))));


    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            if (this.getWorld().provider.getWorldTime() % 5 == 0) {
                switch (facing) {
                    case 2:
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        break;
                    case 3:
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.2, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.2, 0, 0.025, 0
                        );
                        break;
                    case 4:
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.82,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.8,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.2, 0, 0.025, 0
                        );
                        break;
                    case 5:
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.3, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.8, 0, 0.1, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 2,
                                this.getPos().getZ() + 0.2, 0, 0.1, 0
                        );

                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.8, 0, 0.025, 0
                        );
                        this.world.spawnParticle(EnumParticleTypes.FLAME, this.getPos().getX() + 0.2,
                                this.getPos().getY() + 1.9,
                                this.getPos().getZ() + 0.2, 0, 0.025, 0
                        );
                        break;
                }
            }

        }
    }


    public IMultiTileBlock getTeBlock() {
        return BlockRefiner.refiner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.oilrefiner;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setLevel(final int level) {
        this.level = level;
    }

    @Override
    public void removeLevel(final int level) {
        this.level -= level;
    }

    @Override
    public ItemStack getPickBlock(final EntityPlayer player, final RayTraceResult target) {
        return new ItemStack(IUItem.oilrefiner);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean isNormalCube() {
        return false;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }


    public boolean isSideSolid(EnumFacing side) {
        return false;
    }

    public boolean clientNeedsExtraModelInfo() {
        return true;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public ContainerOilRefiner getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerOilRefiner(entityPlayer, this);

    }

    public FluidTank getFluidTank(int i) {
        switch (i) {
            case 1:
                return this.fluidTank2;
            case 2:
                return this.fluidTank3;
            default:
                return this.fluidTank1;
        }
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiOilRefiner(new ContainerOilRefiner(entityPlayer, this));
    }
    public List<ItemStack> getWrenchDrops(EntityPlayer player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.level != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation, this.level));
            this.level = 0;
        }
        return ret;
    }
    @Override
    public boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        if (level < 10) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation)) {
                return super.onActivated(player, hand, side, hitX, hitY, hitZ);
            } else {
                stack.shrink(1);
                this.level++;
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, hitX, hitY, hitZ);
        }
    }
    public void updateEntityServer() {
        super.updateEntityServer();

        boolean drain = false;
        boolean drain1 = false;
        MutableObject<ItemStack> output1 = new MutableObject<>();
        boolean check = false;
        if (this.fluidTank1.getFluidAmount() + 1000 <= this.fluidTank1.getCapacity() && this.fluidSlot1.transferToTank(
                this.fluidTank1,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot1.transferToTank(this.fluidTank1, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank2.getFluidAmount() - 1000 >= 0 && this.fluidSlot2.transferFromTank(
                this.fluidTank2,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot2.transferFromTank(this.fluidTank2, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
            check = true;
        }
        if (this.fluidTank3.getFluidAmount() - 1000 >= 0 && this.fluidSlot3.transferFromTank(
                this.fluidTank3,
                output1,
                true
        ) && (output1.getValue() == null || this.outputSlot.canAdd(output1.getValue()))) {
            this.fluidSlot3.transferFromTank(this.fluidTank3, output1, false);
            if (output1.getValue() != null) {
                this.outputSlot.add(output1.getValue());
            }
        }
        if (check || (this.fluid_handler.output() == null && this.fluidTank1.getFluidAmount() >= 1)) {
            this.fluid_handler.getOutput();
        } else {
            if (this.fluid_handler.output() != null && !this.fluid_handler.checkFluids()) {
                this.fluid_handler.setOutput(null);
            }
        }
        if (this.fluid_handler.output() != null && this.fluid_handler.canOperate() && this.fluid_handler.canFillFluid() && this.energy.canUseEnergy(
                10)) {
            final BaseFluidMachineRecipe output = this.fluid_handler.output();
            final FluidStack inputFluidStack = output.input.getInputs().get(0);
            int size = this.getFluidTank(0).getFluidAmount() / inputFluidStack.amount;
            size = Math.min(this.level + 1, size);
            int cap = this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount();
            FluidStack outputFluidStack = output.output_fluid.get(0);
            cap /= outputFluidStack.amount;
            cap = Math.min(cap, size);
            int cap1 = this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount();
            FluidStack outputFluidStack1 = output.output_fluid.get(1);
            cap1 /= outputFluidStack1.amount;
            size = Math.min(Math.min(size, cap1), cap);
            if (this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount() >= outputFluidStack.amount) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack.getFluid(),
                        outputFluidStack.amount * size
                );
                this.fluidTank2.fill(fluidStack, true);
                drain = true;

            }
            if (this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount() >= outputFluidStack1.amount) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack1.getFluid(),
                        outputFluidStack1.amount * size
                );
                this.fluidTank3.fill(fluidStack, true);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = size * inputFluidStack.amount;
                this.getFluidTank(0).drain(drains, true);
                if (!this.getActive()) {
                    this.setActive(true);
                    initiate(0);
                }
                this.energy.useEnergy(10);
                setActive(true);
            } else {
                setActive(false);
            }
            needUpdate = true;
        } else {
            if (this.getActive()) {
                initiate(2);
                setActive(false);
            }
        }

        if (this.world.provider.getWorldTime() % 20 == 0 && needUpdate) {
            needUpdate = false;
            for (int i = 0; i < this.fluids.getManagedTanks().size(); i++) {
                FluidTank tank = this.fluids.getManagedTanks().get(i);
                new PacketUpdateFieldTile(this, "fluidTank" + (i + 1), tank);
            }
        }
        if (this.upgradeSlot.tickNoMark()) {
            setOverclockRates();
        }

    }

    public void setOverclockRates() {
        int tier = this.upgradeSlot.getTier(14);
        this.energy.setSinkTier(tier);
        this.energy.setCapacity(this.upgradeSlot.getEnergyStorage(
                24000
        ));
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.FluidExtract,
                UpgradableProperty.FluidInput, UpgradableProperty.ItemExtract
        );
    }

    public void addInformation(ItemStack stack, List<String> tooltip) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("press.lshift"));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            tooltip.add(Localization.translate("iu.machines_work_energy") + 10 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.oilrefiner.getSoundEvent();
    }

}
