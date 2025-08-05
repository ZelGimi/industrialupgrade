package com.denfop.tiles.mechanism;

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
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerOilRefiner;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiOilRefiner;
import com.denfop.invslot.InvSlot;
import com.denfop.invslot.InvSlotFluid;
import com.denfop.invslot.InvSlotFluidByList;
import com.denfop.invslot.InvSlotUpgrade;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.tiles.base.IManufacturerBlock;
import com.denfop.tiles.base.TileElectricMachine;
import com.denfop.utils.Keyboard;
import com.denfop.utils.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.apache.commons.lang3.mutable.MutableObject;

import java.io.IOException;
import java.util.*;

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

    private boolean needUpdate;
    private int levelMech;

    public TileOilRefiner(BlockPos pos, BlockState state) {
        super(24000, 14, 2, BlockRefiner.refiner, pos, state);
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
    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        CompoundTag compoundTag =  super.writeToNBT(nbttagcompound);
        compoundTag.putInt("levelMech",levelMech);
        return compoundTag;
    }

    @Override
    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        levelMech = nbttagcompound.getInt("levelMech");
    }
    public List<ItemStack> getWrenchDrops(Player player, int fortune) {
        List<ItemStack> ret = super.getWrenchDrops(player, fortune);
        if (this.levelMech != 0) {
            ret.add(new ItemStack(IUItem.upgrade_speed_creation.getItem(), this.levelMech));
            this.levelMech = 0;
        }
        return ret;
    }

    @Override
    public boolean onActivated(Player player, InteractionHand hand, Direction side, Vec3 vec3) {
        if (levelMech < 10) {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.getItem().equals(IUItem.upgrade_speed_creation.getItem())) {
                return super.onActivated(player, hand, side, vec3);
            } else {
                stack.shrink(1);
                this.levelMech++;
                return true;
            }
        } else {

            return super.onActivated(player, hand, side, vec3);
        }
    }


    @Override
    public void updateField(final String name, final CustomPacketBuffer is) {
        super.updateField(name, is);
        if (name.equals("fluidtank1")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank1.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank2")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank2.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("fluidtank3")) {
            try {
                FluidTank fluidTank1 = (FluidTank) DecoderHandler.decode(is);
                if (fluidTank1 != null) {
                    this.fluidTank3.readFromNBT(fluidTank1.writeToNBT(new CompoundTag()));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<AABB> getAabbs(boolean forCollision) {
        return Collections.singletonList(new AABB(0.0D, 0.0D, 0.0D, 1.0D, 2D, 1.0D));

    }

    @Override
    public void init() {
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidneft.getInstance().get(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance().get(),
                        8
                ), new FluidStack(FluidName.fluiddizel.getInstance().get(), 2))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsweet_heavy_oil.getInstance().get(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluiddizel.getInstance().get(),
                        8
                ), new FluidStack(FluidName.fluidbenz.getInstance().get(), 2))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsweet_medium_oil.getInstance().get(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance().get(),
                        6
                ), new FluidStack(FluidName.fluiddizel.getInstance().get(), 4))));

        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsour_light_oil.getInstance().get(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance().get(),
                        5
                ), new FluidStack(FluidName.fluiddizel.getInstance().get(), 1))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsour_heavy_oil.getInstance().get(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluiddizel.getInstance().get(),
                        5
                ), new FluidStack(FluidName.fluidbenz.getInstance().get(), 1))));
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidStack(FluidName.fluidsour_medium_oil.getInstance().get(), 10)), Arrays.asList(
                new FluidStack(
                        FluidName.fluidbenz.getInstance().get(),
                        4
                ), new FluidStack(FluidName.fluiddizel.getInstance().get(), 2))));


    }


    public void onLoaded() {
        super.onLoaded();
        if (!this.getLevel().isClientSide) {
            setOverclockRates();
            this.fluid_handler.load();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.getActive()) {
            Level world = this.getLevel();
            if (world != null && world.getGameTime() % 5 == 0) {
                Vec3 pos = Vec3.atLowerCornerOf(this.getBlockPos());
                double x = pos.x;
                double y = pos.y;
                double z = pos.z;

                switch (ModUtils.facings[facing]) {
                    case NORTH:
                        spawnParticles(world, x + 0.8, y + 2, z + 0.8, 0, 0.3, 0);
                        spawnParticles(world, x + 0.2, y + 2, z + 0.8, 0, 0.3, 0);
                        break;
                    case SOUTH:
                        spawnParticles(world, x + 0.8, y + 2, z + 0.2, 0, 0.3, 0);
                        spawnParticles(world, x + 0.2, y + 2, z + 0.2, 0, 0.3, 0);
                        break;
                    case WEST:
                        spawnParticles(world, x + 0.8, y + 2, z + 0.8, 0, 0.3, 0);
                        spawnParticles(world, x + 0.8, y + 2, z + 0.2, 0, 0.3, 0);
                        break;
                    case EAST:
                        spawnParticles(world, x + 0.2, y + 2, z + 0.8, 0, 0.3, 0);
                        spawnParticles(world, x + 0.2, y + 2, z + 0.2, 0, 0.3, 0);
                        break;
                }
            }
        }
    }

    private void spawnParticles(Level world, double x, double y, double z, double dx, double dy, double dz) {
        world.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, dx, dy, dz);
        world.addParticle(ParticleTypes.SMOKE, x, y, z, dx * 0.1, dy * 0.1, dz * 0.1);
        world.addParticle(ParticleTypes.FLAME, x, y - 0.1, z, dx * 0.025, dy * 0.025, dz * 0.025);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockRefiner.refiner;
    }

    public BlockTileEntity getBlock() {
        return IUItem.oilrefiner.getBlock();
    }

    @Override
    public int getLevelMechanism() {
        return this.levelMech;
    }

    @Override
    public void setLevelMech(final int levelMech) {
        this.levelMech = levelMech;
    }

    @Override
    public void removeLevel(final int level) {
        this.levelMech -= level;
    }


    @Override
    public ContainerOilRefiner getGuiContainer(Player entityPlayer) {
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

    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<?>> getGui(Player entityPlayer, ContainerBase<?> isAdmin) {
        return new GuiOilRefiner((ContainerOilRefiner) isAdmin);
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
                5)) {
            final BaseFluidMachineRecipe output = this.fluid_handler.output();
            final FluidStack inputFluidStack = output.input.getInputs().get(0);
            int size = this.getFluidTank(0).getFluidAmount() / inputFluidStack.getAmount();
            size = Math.min(this.levelMech + 1, size);
            int cap = this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount();
            FluidStack outputFluidStack = output.output_fluid.get(0);
            cap /= outputFluidStack.getAmount();
            cap = Math.min(cap, size);
            int cap1 = this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount();
            FluidStack outputFluidStack1 = output.output_fluid.get(1);
            cap1 /= outputFluidStack1.getAmount();
            size = Math.min(Math.min(size, cap1), cap);
            if (this.getFluidTank(1).getCapacity() - this.getFluidTank(1).getFluidAmount() >= outputFluidStack.getAmount()) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack.getFluid(),
                        outputFluidStack.getAmount() * size
                );
                this.fluidTank2.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                drain = true;

            }
            if (this.getFluidTank(2).getCapacity() - this.getFluidTank(2).getFluidAmount() >= outputFluidStack1.getAmount()) {
                FluidStack fluidStack = new FluidStack(
                        outputFluidStack1.getFluid(),
                        outputFluidStack1.getAmount() * size
                );
                this.fluidTank3.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                drain1 = true;
            }
            if (drain || drain1) {
                int drains = size * inputFluidStack.getAmount();
                this.getFluidTank(0).drain(drains, IFluidHandler.FluidAction.EXECUTE);
                if (!this.getActive()) {
                    this.setActive(true);
                    initiate(0);
                }
                this.energy.useEnergy(5);
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

        if (this.level.getGameTime() % 20 == 0 && needUpdate) {
            needUpdate = false;
            for (int i = 0; i < this.fluids.getManagedTanks().size(); i++) {
                FluidTank tank = this.fluids.getManagedTanks().get(i);
                new PacketUpdateFieldTile(this, "fluidtank" + (i + 1), tank);
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
            tooltip.add(Localization.translate("iu.machines_work_energy") + 5 + Localization.translate("iu" +
                    ".machines_work_energy_type_eu"));
        }
        super.addInformation(stack, tooltip);

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.oilrefiner.getSoundEvent();
    }

}
