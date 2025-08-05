package com.denfop.tiles.smeltery;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.FluidHandlerRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.Fluids;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerSmelteryController;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiSmelteryController;
import com.denfop.items.ItemCraftingElements;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.register.InitMultiBlockSystem;
import com.denfop.tiles.mechanism.multiblocks.base.TileMultiBlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

;

public class TileEntitySmelteryController extends TileMultiBlockBase implements IController, IHasRecipe, IUpdatableTileEvent {

    public static Map<List<FluidStack>, FluidStack> mapRecipes = new HashMap<>();
    public static Map<FluidStack, List<FluidStack>> mapRecipes1 = new HashMap<>();
    private final FluidHandlerRecipe[] fluidManager;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;
    public boolean work = false;
    public int progress;
    public List<Integer> list = new ArrayList<>();
    public List<ITank> listTank = new ArrayList<>();
    public FluidTank firstTank = null;
    public List<IFuelTank> listFuelTank = new ArrayList<>();
    public ICasting casting;
    public List<IFurnace> furnaces = new ArrayList<>();
    private Integer prevIndex;

    public TileEntitySmelteryController(BlockPos pos, BlockState state) {
        super(InitMultiBlockSystem.SmelterMultiBlock, BlockSmeltery.smeltery_controller, pos, state);
        this.fluidManager = new FluidHandlerRecipe[3];
        for (int i = 0; i < 3; i++) {
            this.fluidManager[i] = new FluidHandlerRecipe("smeltery");
        }
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.4));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.25));
    }

    public FluidTank getFirstTank() {
        return firstTank;
    }

    @Override
    public void updateTileServer(final Player var1, final double var2) {
        if (var2 >= 0 && var2 <= 18) {
            if (list.contains((int) var2)) {
                list.removeIf(i -> i == (int) var2);
            } else {
                list.add((int) var2);
            }
            if (!casting.getInputSlotB().isEmpty()) {
                casting.getFluid_handler().setOutput(null);
                casting.getFluid_handler().setName("empty");

                int damage = ((ItemCraftingElements<?>) casting.getInputSlotB().get(0).getItem()).getElement().getId();
                if (damage == 496) {
                    casting.getFluid_handler().setName("ingot_casting");
                    if (this.getMain() != null) {
                        TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
                        casting.getFluid_handler().load();
                        this.casting.getFluid_handler().getOutput(controller.getFirstTank());

                    }
                } else if (damage == 497) {
                    this.casting.getFluid_handler().setName("gear_casting");
                    if (this.getMain() != null) {
                        TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
                        casting.getFluid_handler().load();
                        casting.getFluid_handler().getOutput(controller.getFirstTank());

                    }
                } else {
                    casting.getFluid_handler().load();
                }

            }
            this.casting.getFluid_handler().setOutput(null);
            if (this.list.isEmpty()) {
                this.firstTank = null;
            }

        }
        if (var2 == -1) {
            List<FluidTank> fluidTanks = new ArrayList<>();
            for (ITank tank : listTank) {
                fluidTanks.add(tank.getTank());
            }
            final List<FluidTank> fluidTanks1 = fluidTanks.stream()
                    .sorted((tank1, tank2) -> {
                        FluidStack fluid1 = tank1.getFluid();
                        FluidStack fluid2 = tank2.getFluid();

                        boolean hasFluid1 = !fluid1.isEmpty() && fluid1.getAmount() > 0;
                        boolean hasFluid2 = !fluid2.isEmpty() && fluid2.getAmount() > 0;
                        if (hasFluid1 && !hasFluid2) {
                            return -1;
                        } else if (!hasFluid1 && hasFluid2) {
                            return 1;
                        }


                        return 0;
                    })
                    .collect(Collectors.toList());

            cycle:
            for (Map.Entry<List<FluidStack>, FluidStack> mapRecipes : mapRecipes.entrySet()) {
                if (mapRecipes.getKey().size() == list.size()) {
                    if (this.fluidManager[0].canFillFluid(mapRecipes.getValue())) {
                        Map<Integer, FluidStack> fluidStackMap = new HashMap<>();
                        for (FluidStack fluidStack : mapRecipes.getKey()) {
                            boolean canDrain = false;
                            for (Integer integer : this.list) {
                                FluidTank tank = fluidTanks1.get(integer);
                                canDrain = fluidManager[0].hasFluid(fluidStack, tank);
                                if (canDrain) {
                                    fluidStackMap.put(integer, fluidStack);
                                    break;
                                }
                            }
                            if (!canDrain) {
                                continue cycle;
                            }
                        }
                        if (!fluidStackMap.isEmpty()) {
                            fluidStackMap.forEach((integer, fluidStack) -> fluidManager[0].drainFluid(
                                    fluidStack,
                                    fluidTanks1.get(integer)
                            ));
                            this.fluidManager[0].fillFluid(mapRecipes.getValue());
                        }
                    }
                }
            }
        } else if (var2 == -3) {
            List<FluidTank> fluidTanks = new ArrayList<>();
            for (ITank tank : listTank) {
                fluidTanks.add(tank.getTank());
            }
            final List<FluidTank> fluidTanks1 = fluidTanks.stream()
                    .sorted((tank1, tank2) -> {
                        FluidStack fluid1 = tank1.getFluid();
                        FluidStack fluid2 = tank2.getFluid();

                        boolean hasFluid1 = !fluid1.isEmpty() && fluid1.getAmount() > 0;
                        boolean hasFluid2 = !fluid2.isEmpty() && fluid2.getAmount() > 0;
                        if (hasFluid1 && !hasFluid2) {
                            return -1;
                        } else if (!hasFluid1 && hasFluid2) {
                            return 1;
                        }


                        return 0;
                    })
                    .collect(Collectors.toList());

            cycle:
            for (Map.Entry<FluidStack, List<FluidStack>> mapRecipes : mapRecipes1.entrySet()) {
                if (!fluidTanks1.get(list.get(0)).getFluid().isEmpty()) {
                    if (mapRecipes.getKey().getFluid() == fluidTanks1.get(list.get(0)).getFluid().getFluid()) {
                        for (FluidStack fluidStack : mapRecipes.getValue()) {
                            if (!this.fluidManager[0].canFillFluid(fluidStack)) {
                                continue cycle;
                            }
                        }
                        if (fluidTanks1.get(list.get(0)).getFluidAmount() < mapRecipes.getKey().getAmount()) {
                            continue cycle;
                        }
                        for (FluidStack fluidStack : mapRecipes.getValue()) {
                            this.fluidManager[0].fillFluid(fluidStack);
                        }
                        this.fluidManager[0].drainFluid(mapRecipes.getKey(), fluidTanks1.get(list.get(0)));
                        break;
                    }

                }
            }
        } else if (var2 == -2) {
            List<FluidTank> fluidTanks = new ArrayList<>();
            for (ITank tank : listTank) {
                fluidTanks.add(tank.getTank());
            }
            final List<FluidTank> fluidTanks1 = fluidTanks.stream()
                    .sorted((tank1, tank2) -> {
                        FluidStack fluid1 = tank1.getFluid();
                        FluidStack fluid2 = tank2.getFluid();

                        boolean hasFluid1 = !fluid1.isEmpty() && fluid1.getAmount() > 0;
                        boolean hasFluid2 = !fluid2.isEmpty() && fluid2.getAmount() > 0;
                        if (hasFluid1 && !hasFluid2) {
                            return -1;
                        } else if (!hasFluid1 && hasFluid2) {
                            return 1;
                        }


                        return 0;
                    })
                    .collect(Collectors.toList());

            cycle:
            for (Map.Entry<List<FluidStack>, FluidStack> mapRecipes : mapRecipes.entrySet()) {
                if (mapRecipes.getKey().size() == list.size()) {
                    if (this.fluidManager[0].canFillFluid(mapRecipes.getValue())) {
                        Map<Integer, FluidStack> fluidStackMap = new HashMap<>();
                        for (FluidStack fluidStack : mapRecipes.getKey()) {
                            boolean canDrain = false;
                            for (Integer integer : this.list) {
                                FluidTank tank = fluidTanks1.get(integer);
                                canDrain = fluidManager[0].hasFluid(fluidStack, tank);
                                if (canDrain) {
                                    fluidStackMap.put(integer, fluidStack);
                                    break;
                                }
                            }
                            if (!canDrain) {
                                continue cycle;
                            }
                        }
                        cycle2:
                        while (true) {
                            if (!fluidStackMap.isEmpty()) {
                                fluidStackMap.forEach((integer, fluidStack) -> fluidManager[0].drainFluid(
                                        fluidStack,
                                        fluidTanks1.get(integer)
                                ));
                                this.fluidManager[0].fillFluid(mapRecipes.getValue());
                                for (Map.Entry<Integer, FluidStack> entry : fluidStackMap.entrySet()) {
                                    if (fluidTanks1.get(entry.getKey()).getFluidAmount() < entry.getValue().getAmount()) {
                                        break cycle2;
                                    }
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void readFromNBT(final CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        work = nbttagcompound.getBoolean("work");

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        CustomPacketBuffer customPacketBuffer = super.writeContainerPacket();
        customPacketBuffer.writeBoolean(this.work);
        customPacketBuffer.writeInt(this.progress);
        customPacketBuffer.writeInt(this.list.size());
        customPacketBuffer.writeBoolean(this.firstTank != null);
        for (Integer integer : list) {
            customPacketBuffer.writeInt(integer);
        }
        for (ITank tank : this.listTank) {
            try {
                EncoderHandler.encode(customPacketBuffer, tank.getTank());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (this.firstTank != null) {
            try {
                EncoderHandler.encode(customPacketBuffer, firstTank);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return customPacketBuffer;
    }

    @Override
    public void updateEntityClient() {
        super.updateEntityClient();
        if (this.full && this.listTank.isEmpty()) {
            updateAfterAssembly();
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        work = customPacketBuffer.readBoolean();
        progress = customPacketBuffer.readInt();
        final int size = customPacketBuffer.readInt();
        boolean hasTank = customPacketBuffer.readBoolean();
        list.clear();
        for (int i = 0; i < size; i++) {
            list.add(customPacketBuffer.readInt());
        }
        for (ITank tank : this.listTank) {
            FluidTank fluidTank2;
            try {
                fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (fluidTank2 != null) {
                tank.getTank().readFromNBT(customPacketBuffer.registryAccess(), fluidTank2.writeToNBT(customPacketBuffer.registryAccess(), new CompoundTag()));
            }
        }

        if (hasTank) {
            FluidTank fluidTank2;
            try {
                fluidTank2 = (FluidTank) DecoderHandler.decode(customPacketBuffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.firstTank = fluidTank2;
        }
    }

    @Override
    public ContainerSmelteryController getGuiContainer(final Player var1) {
        return new ContainerSmelteryController(this, var1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {
        return new GuiSmelteryController((ContainerSmelteryController) menu);
    }


    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.isFull()) {
            boolean work = false;
            if (this.getWorld().getGameTime() % 20 == 0) {
                for (int i = 0; i < listTank.size(); i++) {
                    FluidTank tank1 = listTank.get(i).getTank();
                    FluidStack fluid1 = tank1.getFluid();

                    if (!fluid1.isEmpty()) {
                        for (int j = 0; j < listTank.size(); j++) {
                            if (i == j) {
                                continue;
                            }

                            FluidTank tank2 = listTank.get(j).getTank();
                            FluidStack fluid2 = tank2.getFluid();

                            if (!fluid2.isEmpty() && FluidStack.isSameFluid(fluid1, fluid2)) {
                                int amountNeeded = tank1.getCapacity() - fluid1.getAmount();
                                if (amountNeeded > 0) {
                                    int transferAmount = Math.min(amountNeeded, fluid2.getAmount());
                                    fluid1.grow(transferAmount);
                                    fluid2.shrink(transferAmount);
                                    tank1.setFluid(fluid1);
                                    tank2.setFluid(fluid2);
                                }
                            }
                        }
                    }
                }

            }
            int i = 0;
            if (this.casting != null && !casting.getInputSlotB().isEmpty()) {
                if (this.getWorld().getGameTime() % 20 == 0) {
                    if (this.list.isEmpty()) {
                        this.firstTank = null;
                        this.prevIndex = -1;
                    } else {
                        this.firstTank = this.fluidManager[0].getOutputTank().stream()
                                .sorted((tank1, tank2) -> {
                                    FluidStack fluid1 = tank1.getFluid();
                                    FluidStack fluid2 = tank2.getFluid();

                                    boolean hasFluid1 = !fluid1.isEmpty() && fluid1.getAmount() > 0;
                                    boolean hasFluid2 = !fluid2.isEmpty() && fluid2.getAmount() > 0;


                                    if (hasFluid1 && !hasFluid2) {
                                        return -1;
                                    } else if (!hasFluid1 && hasFluid2) {
                                        return 1;
                                    }


                                    return 0;
                                })
                                .toList().get(this.list.get(0));
                        this.prevIndex = this.list.get(0);
                    }
                }


                if ((casting.getFluid_handler().output() == null && this.getFirstTank() != null && this
                        .getFirstTank()
                        .getFluidAmount() >= 1) && (Objects.equals(this.prevIndex, this.list.get(0)))) {
                    casting.getFluid_handler().getOutput(this.getFirstTank());
                } else {
                    if (casting.getFluid_handler().output() != null && !casting.getFluid_handler().checkFluids()) {
                        casting.getFluid_handler().setOutput(null);
                    }
                }
                int fuel = 0;
                for (IFuelTank fuelTank : this.listFuelTank) {
                    fuel += fuelTank.getFuelTank().getFluidAmount();
                }
                if (casting.getFluid_handler().output() != null && casting.getOutputSlot().canAdd(casting.getFluid_handler()
                        .output()
                        .getOutput().items) && casting.getFluid_handler().canOperate1(this.getFirstTank()) && fuel >= 1) {
                    work = true;
                    casting.getProgress().addProgress();
                    fuel = 1;
                    for (IFuelTank fuelTank : this.listFuelTank) {
                        int fuel1 = Math.min(fuelTank.getFuelTank().getFluidAmount(), fuel);
                        fuelTank.getFuelTank().drain(fuel1, IFluidHandler.FluidAction.EXECUTE);
                        fuel -= fuel1;
                        if (fuel == 0) {
                            break;
                        }
                    }
                    if (casting.getProgress().getBar() >= 1) {
                        casting.getProgress().setProgress((short) 0);
                        casting.getFluid_handler().consume(firstTank);
                        casting.getOutputSlot().add(casting.getFluid_handler().output().getOutput().items);
                        casting.getFluid_handler().checkOutput(this.getFirstTank());
                        work = false;
                    }

                } else {


                    if (casting.getFluid_handler().output() == null) {
                        casting.getProgress().setProgress((short) 0);
                    }
                }
            }
            for (IFurnace furnace : furnaces) {
                if (furnace != null && !this.listFuelTank.isEmpty()) {
                    if ((this.fluidManager[i].output() == null && furnace.getRecipeOutput() != null)) {
                        this.fluidManager[i].getOutput(furnace.getInvSlot().get(0));
                    } else {
                        if (this.fluidManager[i].output() != null && furnace.getRecipeOutput() == null) {
                            this.fluidManager[i].setOutput(null);
                        }
                    }
                    if (furnace.isChangeRecipe() && furnace.getRecipeOutput() != null) {
                        this.fluidManager[i].setOutput(null);
                        furnace.setChangeRecipe(false);
                    }
                    int fuel = 0;
                    int fuel1 = 0;
                    for (IFuelTank fuelTank : this.listFuelTank) {
                        if (fuelTank.getSpeed() == 1) {
                            fuel += fuelTank.getFuelTank().getFluidAmount();
                        } else {
                            fuel1 += fuelTank.getFuelTank().getFluidAmount();
                        }
                    }
                    if (furnace.getRecipeOutput() != null && !furnace.getInvSlot().isEmpty() && furnace
                            .getInvSlot()
                            .continue_process(furnace.getRecipeOutput()) && this.fluidManager[i].output() != null && this.fluidManager[i].canFillFluid1() && (fuel >= 1 || fuel1 >= 1)) {
                        furnace.getComponent().addProgress(0, (short) (fuel1 > 0 ? 2 : 1));
                        if (!furnace.isActive()) {
                            furnace.setActive(true);
                        }
                        work = true;
                        boolean drain = false;
                        if (fuel1 > 0) {
                            for (IFuelTank fuelTank : this.listFuelTank) {
                                if (fuelTank.getSpeed() > 1) {
                                    fuelTank.getFuelTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
                                    drain = true;
                                    break;
                                }
                            }
                        }
                        if (!drain) {
                            for (IFuelTank fuelTank : this.listFuelTank) {
                                if (fuelTank.getSpeed() == 1 && fuelTank.getFuelTank().getFluidAmount() > 0) {
                                    fuelTank.getFuelTank().drain(1, IFluidHandler.FluidAction.EXECUTE);
                                    break;
                                }
                            }
                        }
                        if (furnace.getComponent().getBar() >= 1) {
                            furnace.getComponent().setProgress((short) 0);
                            furnace.getInvSlot().consume();
                            this.fluidManager[i].fillFluid1();
                            furnace.getOutput();
                            if (furnace.isActive()) {
                                furnace.setActive(false);
                            }
                            work = false;
                        }

                    } else {
                        if (furnace.isActive()) {
                            furnace.setActive(false);
                        }
                    }
                    i++;
                } else {
                    i++;

                }
            }
            this.setActive(work);
        } else {
            if (this.getActive()) {
                this.setActive(false);
            }
        }
    }

    @Override
    public CompoundTag writeToNBT(final CompoundTag nbttagcompound) {
        nbttagcompound.putBoolean("work", work);
        return super.writeToNBT(nbttagcompound);
    }

    @Override
    public void setFull(final boolean full) {
        super.setFull(full);
        if (!full) {
            this.furnaces.clear();
            this.listFuelTank.clear();
            this.listTank.clear();
            if (this.casting != null) {
                this.casting.getFluid_handler().setTanks(Collections.EMPTY_LIST);
            }
            this.casting = null;
        }


    }

    @Override
    public void updateAfterAssembly() {
        listTank.clear();
        listFuelTank.clear();
        furnaces.clear();
        List<BlockPos> pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ITank.class
                );
        for (BlockPos pos2 : pos1) {
            this.listTank.add((ITank) this.getWorld().getBlockEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IFurnace.class
                );
        for (BlockPos pos2 : pos1) {
            this.furnaces.add((IFurnace) this.getWorld().getBlockEntity(pos2));
        }
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        ICasting.class
                );
        this.casting = (ICasting) this.getWorld().getBlockEntity(pos1.get(0));
        pos1 = this
                .getMultiBlockStucture()
                .getPosFromClass(this.getFacing(), this.getBlockPos(),
                        IFuelTank.class
                );
        for (BlockPos pos2 : pos1) {
            this.listFuelTank.add((IFuelTank) this.getWorld().getBlockEntity(pos2));
        }
        List<Fluids.InternalFluidTank> fluidTanks = new ArrayList<>();
        for (ITank tank : listTank) {
            fluidTanks.add((Fluids.InternalFluidTank) tank.getTank());
        }

        for (FluidHandlerRecipe recipe : fluidManager) {
            recipe.setTanks(fluidTanks);
        }
        this.casting.getFluid_handler().setTanks(fluidTanks);
        if (!casting.getInputSlotB().isEmpty()) {
            casting.getFluid_handler().setName("empty");
            casting.getFluid_handler().setOutput(null);
            int damage = ((ItemCraftingElements<?>) casting.getInputSlotB().get(0).getItem()).getElement().getId();
            if (damage == 496) {
                casting.getFluid_handler().setName("ingot_casting");
                if (this.getMain() != null) {
                    TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
                    casting.getFluid_handler().load();
                    this.casting.getFluid_handler().getOutput(controller.getFirstTank());

                }
            } else if (damage == 497) {
                this.casting.getFluid_handler().setName("gear_casting");
                if (this.getMain() != null) {
                    TileEntitySmelteryController controller = (TileEntitySmelteryController) this.getMain();
                    casting.getFluid_handler().load();
                    casting.getFluid_handler().getOutput(controller.getFirstTank());

                }
            } else {
                casting.getFluid_handler().load();
            }

        }
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isClientSide && this.isFull()) {
            for (int i = 0; i < 3; i++) {
                this.fluidManager[i].load(furnaces.get(i).getInvSlot().get(0));
            }
        }
    }

    @Override
    public void usingBeforeGUI() {

    }

    @Override
    public IMultiTileBlock getTeBlock() {
        return BlockSmeltery.smeltery_controller;
    }

    @Override
    public BlockTileEntity getBlock() {
        return IUItem.smeltery.getBlock(getTeBlock());
    }


    @Override
    public void init() {
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidiron.getInstance().get(), 144 * 3),
                new FluidStack(FluidName.fluidcarbon.getInstance().get(), 144 * 2)
        ), new FluidStack(
                FluidName.fluidsteel.getInstance().get(),
                144
        ));
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144 * 2),
                new FluidStack(FluidName.fluidiron.getInstance().get(), 144 * 1),
                new FluidStack(FluidName.fluidmagnesium.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidobsidian.getInstance().get(),
                144
        ));
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidtitanium.getInstance().get(), 144 * 2),
                new FluidStack(FluidName.fluidsteel.getInstance().get(), 144 * 2)
        ), new FluidStack(
                FluidName.fluidtitaniumsteel.getInstance().get(),
                144
        ));

        mapRecipes1.put(
                new FluidStack(
                        FluidName.fluidelectrum.getInstance().get(),
                        144
                ),
                Arrays.asList(
                        new FluidStack(FluidName.fluidgold.getInstance().get(), 72),
                        new FluidStack(FluidName.fluidsilver.getInstance().get(), 72)
                )
        );

        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidgold.getInstance().get(), 144 * 1),
                new FluidStack(FluidName.fluidsilver.getInstance().get(), 144 * 2)
        ), new FluidStack(
                FluidName.fluidelectrum.getInstance().get(),
                144
        ));
        mapRecipes1.put(
                new FluidStack(
                        FluidName.fluidinvar.getInstance().get(),
                        144
                ),
                Arrays.asList(
                        new FluidStack(FluidName.fluidiron.getInstance().get(), 144 / 3),
                        new FluidStack(FluidName.fluidnickel.getInstance().get(), 72)
                )
        );

        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidiron.getInstance().get(), 144 * 1),
                new FluidStack(FluidName.fluidnickel.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidinvar.getInstance().get(),
                144
        ));
        mapRecipes1.put(new FluidStack(
                FluidName.fluidbronze.getInstance().get(),
                144
        ), Arrays.asList(
                new FluidStack(FluidName.fluidcopper.getInstance().get(), 3 * 144 / 4),
                new FluidStack(FluidName.fluidtin.getInstance().get(), 144 / 4)
        ));
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidcopper.getInstance().get(), 144 * 4),
                new FluidStack(FluidName.fluidtin.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidbronze.getInstance().get(),
                144
        ));
        mapRecipes1.put(
                new FluidStack(
                        FluidName.fluidwolframite.getInstance().get(),
                        144
                ),
                Arrays.asList(
                        new FluidStack(FluidName.fluidtungsten.getInstance().get(), 144 * 1),
                        new FluidStack(FluidName.fluidnickel.getInstance().get(), 144 * 1)
                )
        );
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidtungsten.getInstance().get(), 144 * 1),
                new FluidStack(FluidName.fluidnickel.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidwolframite.getInstance().get(),
                144
        ));
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidmagnesium.getInstance().get(), 144 * 2),
                new FluidStack(FluidName.fluidaluminium.getInstance().get(), 144 * 2)
        ), new FluidStack(
                FluidName.fluidduralumin.getInstance().get(),
                144
        ));
        mapRecipes1.put(
                new FluidStack(
                        FluidName.fluidnichrome.getInstance().get(),
                        144
                ),
                Arrays.asList(
                        new FluidStack(FluidName.fluidchromium.getInstance().get(), 144 * 1),
                        new FluidStack(FluidName.fluidnickel.getInstance().get(), 144 * 1)
                )
        );
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidchromium.getInstance().get(), 144 * 2),
                new FluidStack(FluidName.fluidnickel.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidnichrome.getInstance().get(),
                144
        ));
        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidwolframite.getInstance().get(), 144 * 1),
                new FluidStack(FluidName.fluidquartz.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidtemperedglass.getInstance().get(),
                144
        ));

        mapRecipes1.put(
                new FluidStack(
                        FluidName.fluidarsenicum_gallium.getInstance().get(),
                        144
                ),
                Arrays.asList(
                        new FluidStack(FluidName.fluidarsenicum.getInstance().get(), 144 * 2),
                        new FluidStack(FluidName.fluidgallium.getInstance().get(), 144 * 1)
                )
        );

        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidarsenicum.getInstance().get(), 144 * 3),
                new FluidStack(FluidName.fluidgallium.getInstance().get(), 144 * 2)
        ), new FluidStack(
                FluidName.fluidarsenicum_gallium.getInstance().get(),
                144
        ));

        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidaluminium.getInstance().get(), 144 * 2),
                new FluidStack(FluidName.fluidbronze.getInstance().get(), 144 * 1)
        ), new FluidStack(
                FluidName.fluidaluminiumbronze.getInstance().get(),
                144
        ));

        mapRecipes.put(Arrays.asList(
                new FluidStack(FluidName.fluidiron.getInstance().get(), 144 * 2),
                new FluidStack(FluidName.fluidmanganese.getInstance().get(), 144 * 2)
        ), new FluidStack(
                FluidName.fluidferromanganese.getInstance().get(),
                144
        ));
    }

}
