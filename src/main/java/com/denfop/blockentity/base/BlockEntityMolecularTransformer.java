package com.denfop.blockentity.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.energy.networking.EnergyNetGlobal;
import com.denfop.api.recipe.*;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMolecularEntity;
import com.denfop.componets.Energy;
import com.denfop.containermenu.ContainerMenuBase;
import com.denfop.containermenu.ContainerMenuBaseMolecular;
import com.denfop.items.resource.ItemNuclearResource;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.screen.ScreenIndustrialUpgrade;
import com.denfop.screen.ScreenMolecularTransformer;
import com.denfop.sound.EnumSound;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;

public class BlockEntityMolecularTransformer extends BlockEntityElectricMachine implements
        IUpdatableTileEvent, IUpdateTick, IHasRecipe, IIsMolecular {

    public double[] energyShare;
    public boolean queue;
    public byte redstoneMode;
    public InventoryRecipes[] inputSlot;
    public MachineRecipe[] output;
    public double perenergy;
    public double differenceenergy;
    public ItemStack[] output_stack = new ItemStack[4];
    public int maxAmount = 1;
    public InventoryOutput[] outputSlot;
    public double[] energySlots;
    public double[] maxEnergySlots;
    protected double[] guiProgress;
    protected double[] size_recipe;
    int index;
    int indexModel = 0;
    int tick = 0;
    private boolean need = false;
    @OnlyIn(Dist.CLIENT)
    private BakedModel[] bakedModel;
    @OnlyIn(Dist.CLIENT)
    private BakedModel[] transformedModel;

    public BlockEntityMolecularTransformer(BlockPos pos, BlockState state) {
        super(0, 14, 0, BlockMolecularEntity.molecular, pos, state);
        this.queue = false;
        this.redstoneMode = 0;
        this.outputSlot = new InventoryOutput[4];
        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));
        this.inputSlot = new InventoryRecipes[4];
        this.output = new MachineRecipe[4];
        this.energySlots = new double[4];
        this.guiProgress = new double[4];
        this.energyShare = new double[4];
        this.maxEnergySlots = new double[4];
        if (FMLEnvironment.dist.isClient()) {
            this.bakedModel = new BakedModel[4];
            this.transformedModel = new BakedModel[4];
        }
        this.size_recipe = new double[4];

        for (int i = 0; i < 4; i++) {
            this.output_stack[i] = new ItemStack(Items.AIR);
            this.inputSlot[i] = new InventoryRecipes(this, "molecular", this) {
                @Override
                public boolean canPlaceItem(int index, ItemStack itemStack) {
                    return getIndex() < maxAmount;
                }
            };
            this.inputSlot[i].setIndex(i);
            this.outputSlot[i] = new InventoryOutput(this, 1);
        }
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack stack, ItemStack stack1, double energy) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "molecular",
                new BaseMachineRecipe(new Input(input.getInput(stack)), new RecipeOutput(nbt, stack1))
        );
    }

    public static void addrecipe(String stack, ItemStack stack1, double energy) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "molecular",
                new BaseMachineRecipe(new Input(input.getInput(stack)), new RecipeOutput(nbt, stack1))
        );

    }

    public MultiBlockEntity getTeBlock() {
        return BlockMolecularEntity.molecular;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockmolecular.getBlock(0);
    }

    public ItemStack getOutput_stack() {
        return output_stack[0];
    }

    public void init() {


        addrecipe(new ItemStack(Items.WITHER_SKELETON_SKULL, 1), new ItemStack(Items.NETHER_STAR, 1), 4000000D);


        addrecipe(
                new ItemStack(IUItem.nuclear_res.getItem(ItemNuclearResource.Types.plutonium)),
                new ItemStack(IUItem.proton.getItem(), 1),
                15500000D
        );

        addrecipe("forge:ingots/Spinel", new ItemStack(IUItem.iuingot.getStack(5), 1), 2500000);

        addrecipe(new ItemStack(IUItem.photoniy.getItem()), new ItemStack(IUItem.photoniy_ingot.getItem()), 12000000D);

        addrecipe(new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.GUNPOWDER, 2), 70000D);

        addrecipe(new ItemStack(Blocks.SAND), new ItemStack(Blocks.GRAVEL, 1), 45000D);


        addrecipe("forge:ingots/Iridium", new ItemStack(IUItem.core.getStack(0), 1), 1500D);

        addrecipe(new ItemStack(IUItem.core.getStack(0), 4), new ItemStack(IUItem.core.getStack(1)), 11720D);

        addrecipe(new ItemStack(IUItem.core.getStack(1), 4), new ItemStack(IUItem.core.getStack(2)), 60000D);

        addrecipe(new ItemStack(IUItem.core.getStack(2), 4), new ItemStack(IUItem.core.getStack(3)), 300000D);

        addrecipe(new ItemStack(IUItem.core.getStack(3), 4), new ItemStack(IUItem.core.getStack(4)), 1500000D);

        addrecipe(new ItemStack(IUItem.core.getStack(4), 4), new ItemStack(IUItem.core.getStack(5)), 7500000D);

        addrecipe(new ItemStack(IUItem.core.getStack(5), 4), new ItemStack(IUItem.core.getStack(6)), 45000000D);

        addrecipe(new ItemStack(IUItem.core.getStack(6), 4), new ItemStack(IUItem.core.getStack(7)), 180000000D);

        addrecipe(new ItemStack(IUItem.core.getStack(7), 4), new ItemStack(IUItem.core.getStack(8)), 900000000D);

        addrecipe(new ItemStack(IUItem.core.getStack(8), 4), new ItemStack(IUItem.core.getStack(9)), 2700000000D);
        addrecipe(new ItemStack(IUItem.core.getStack(9), 4), new ItemStack(IUItem.core.getStack(10)), 4500000000D);

        addrecipe(new ItemStack(IUItem.core.getStack(10), 4), new ItemStack(IUItem.core.getStack(11)), 9000000000D);
        addrecipe(new ItemStack(IUItem.core.getStack(11), 4), new ItemStack(IUItem.core.getStack(12)), 12000000000D);
        addrecipe(new ItemStack(IUItem.core.getStack(12), 4), new ItemStack(IUItem.core.getStack(13)), 21000000000D);


        addrecipe(new ItemStack(IUItem.crafting_elements.getStack(649), 8), new ItemStack(IUItem.crafting_elements.getStack(645)),
                20000000D
        );
        addrecipe(new ItemStack(IUItem.matter.getStack(1)), new ItemStack(IUItem.lens.getStack(5)), 25000000D);

        addrecipe(new ItemStack(IUItem.matter.getStack(2)), new ItemStack(IUItem.lens.getStack(6)), 25000000D);

        addrecipe(new ItemStack(IUItem.matter.getStack(3)), new ItemStack(IUItem.lens.getStack(2)), 25000000D);

        addrecipe(new ItemStack(IUItem.matter.getStack(4)), new ItemStack(IUItem.lens.getStack(4)), 25000000D);

        addrecipe(new ItemStack(IUItem.matter.getStack(5)), new ItemStack(IUItem.lens.getStack(1)), 25000000D);

        addrecipe(new ItemStack(IUItem.matter.getStack(6)), new ItemStack(IUItem.lens.getStack(3)), 25000000D);

        addrecipe(new ItemStack(IUItem.matter.getStack(7)), new ItemStack(IUItem.lens.getStack(0), 1), 25000000D);
        addrecipe(new ItemStack(Items.IRON_INGOT), IUItem.iridiumOre,
                7500000
        );
        addrecipe(
                IUItem.iridiumOre,
                new ItemStack(IUItem.photoniy.getItem()),
                1450000D
        );


    }

    public List<Double> getTime(int i) {
        final double dif = this.energyShare[i];
        return ModUtils.Time(((1 - this.guiProgress[i]) * this.maxEnergySlots[i]) / (dif * 20));
    }

    public double getInput() {
        return EnergyNetGlobal.instance
                .getNodeStats(this.energy.getDelegate(), this.level)
                .getEnergyIn();
    }

    @Override
    public ContainerMenuBaseMolecular getGuiContainer(Player entityPlayer) {
        return new ContainerMenuBaseMolecular(entityPlayer, this);
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double[]) DecoderHandler.decode(customPacketBuffer);
            energyShare = (double[]) DecoderHandler.decode(customPacketBuffer);
            energySlots = (double[]) DecoderHandler.decode(customPacketBuffer);
            maxEnergySlots = (double[]) DecoderHandler.decode(customPacketBuffer);
            queue = (boolean) DecoderHandler.decode(customPacketBuffer);
            redstoneMode = (byte) DecoderHandler.decode(customPacketBuffer);
            differenceenergy = (double) DecoderHandler.decode(customPacketBuffer);
            perenergy = (double) DecoderHandler.decode(customPacketBuffer);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, energyShare);
            EncoderHandler.encode(packet, energySlots);
            EncoderHandler.encode(packet, maxEnergySlots);
            EncoderHandler.encode(packet, queue);
            EncoderHandler.encode(packet, redstoneMode);
            EncoderHandler.encode(packet, differenceenergy);
            EncoderHandler.encode(packet, perenergy);
            EncoderHandler.encode(packet, output_stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public boolean onActivated(
            final Player player,
            final InteractionHand hand,
            final Direction side,
            final Vec3 hitX
    ) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.getWorld().isClientSide && maxAmount == 1 && stack.getItem() == IUItem.double_molecular.getItem()) {
            maxAmount = 2;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "maxAmount", this.maxAmount);
            return true;
        }
        if (!this.getWorld().isClientSide && maxAmount == 2 && stack.getItem() == IUItem.quad_molecular.getItem()) {
            maxAmount = 4;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "maxAmount", this.maxAmount);
            return true;
        }
        return super.onActivated(player, hand, side, hitX);
    }

    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, energyShare);
            EncoderHandler.encode(packet, energySlots);
            EncoderHandler.encode(packet, queue);
            EncoderHandler.encode(packet, maxAmount);
            EncoderHandler.encode(packet, redstoneMode);
            EncoderHandler.encode(packet, energy, false);
            EncoderHandler.encode(packet, output_stack);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            guiProgress = (double[]) DecoderHandler.decode(customPacketBuffer);
            energyShare = (double[]) DecoderHandler.decode(customPacketBuffer);
            energySlots = (double[]) DecoderHandler.decode(customPacketBuffer);
            queue = (boolean) DecoderHandler.decode(customPacketBuffer);
            maxAmount = (int) DecoderHandler.decode(customPacketBuffer);
            redstoneMode = (byte) DecoderHandler.decode(customPacketBuffer);
            energy.onNetworkUpdate(customPacketBuffer);
            output_stack = (ItemStack[]) DecoderHandler.decode(customPacketBuffer);
            for (int i = 0; i < Objects.requireNonNull(output_stack).length; i++) {
                if (!output_stack[i].isEmpty()) {

                    this.bakedModel[i] = Minecraft.getInstance().getItemRenderer().getModel(
                            output_stack[i],
                            this.getWorld(),
                            null, 0
                    );
                    this.transformedModel[i] = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(new PoseStack(),
                            this.bakedModel[i],
                            GROUND,
                            false
                    );
                } else {
                    this.bakedModel[i] = null;
                    this.transformedModel[i] = null;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public String getInventoryName() {
        return "Molecular Transformer";
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        maxAmount = nbttagcompound.getInt("maxAmount");
        for (int i = 0; i < maxAmount; i++) {
            this.energySlots[i] = nbttagcompound.getDouble("energySlots" + i);
        }
        this.queue = nbttagcompound.getBoolean("queue");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");

    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putByte("redstoneMode", this.redstoneMode);
        nbttagcompound.putBoolean("queue", this.queue);
        nbttagcompound.putInt("maxAmount", this.maxAmount);
        for (int i = 0; i < maxAmount; i++) {
            nbttagcompound.putDouble("energySlots" + i, this.energySlots[i]);
        }
        return nbttagcompound;

    }

    @OnlyIn(Dist.CLIENT)
    public ScreenIndustrialUpgrade<ContainerMenuBase<? extends CustomWorldContainer>> getGui(Player entityPlayer, ContainerMenuBase<? extends CustomWorldContainer> isAdmin) {
        return new ScreenMolecularTransformer((ContainerMenuBaseMolecular) isAdmin);
    }

    public void updateTileServer(Player player, double event) {
        if (this.getWorld().isClientSide) {
            return;
        }
        if (event == 0) {
            this.redstoneMode = (byte) (this.redstoneMode + 1);
            if (this.redstoneMode >= 8) {
                this.redstoneMode = 0;
            }
            new PacketUpdateFieldTile(this, "redstoneMode", this.redstoneMode);
        }
        if (event == -1) {
            this.redstoneMode = (byte) (this.redstoneMode - 1);
            if (this.redstoneMode < 0) {
                this.redstoneMode = 7;
            }
            new PacketUpdateFieldTile(this, "redstoneMode", this.redstoneMode);
        }
        if (event == 1) {
            this.queue = !this.queue;
            if (this.need) {
                this.queue = false;
            }
            setOverclockRates();
        }
    }

    public void setChanged() {
        super.setChanged();
        if (!this.getWorld().isClientSide) {
            setOverclockRates();
        }
    }

    public void operate(int i, MachineRecipe output) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(i, processResult);
    }

    public void operate(int i, MachineRecipe output, int size) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(i, processResult, size);
    }

    public void operateOnce(int i, List<ItemStack> processResult) {

        this.inputSlot[i].consume();
        this.outputSlot[i].addAll(processResult);
        this.energySlots[i] = 0;
        if (!this.inputSlot[i].continue_process(this.output[i]) || !this.outputSlot[i].canAdd(output[i].getRecipe().output.items)) {
            getOutput(i);
        }
        setOverclockRates();
    }

    public void operateOnce(int index, List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {
            this.inputSlot[index].consume();
            this.outputSlot[index].addAll(processResult);
        }
        this.energySlots[index] = 0;
        if (!this.inputSlot[index].continue_process(this.output[index]) || !this.outputSlot[index].canAdd(output[index].getRecipe().output.items)) {
            getOutput(index);
        }
        setOverclockRates();


    }

    @Override
    public int getMode() {
        return this.redstoneMode;
    }

    @Override
    public ItemStack getItemStack() {
        return this.output_stack[0];
    }

    @Override
    public BlockEntityBase getEntityBlock() {
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BakedModel getBakedModel() {
        if (tick % 120 == 0) {
            tick = 1;
            boolean found = false;
            for (int i = Math.min(indexModel + 1, maxAmount); i < maxAmount; i++) {
                if (bakedModel[i] != null) {
                    indexModel = i;
                    found = true;
                    break;
                }
            }
            if (!found) {
                for (int i = 0; i < maxAmount; i++) {
                    if (bakedModel[i] != null) {
                        indexModel = i;
                        break;
                    }
                }
            }
        } else {
            tick++;
        }
        return bakedModel[indexModel];
    }

    public void onLoaded() {
        super.onLoaded();
        if (!this.getLevel().isClientSide) {
            for (int i = 0; i < maxAmount; i++) {
                inputSlot[i].load();
            }
            this.setOverclockRates();
            new PacketUpdateFieldTile(this, "redstoneMode", this.redstoneMode);
        }

    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("redstoneMode")) {
            try {
                this.redstoneMode = (byte) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("maxAmount")) {
            try {
                this.maxAmount = (int) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (name.equals("output")) {
            try {
                this.output_stack = (ItemStack[]) DecoderHandler.decode(is);
                for (int i = 0; i < Objects.requireNonNull(output_stack).length; i++) {
                    if (!output_stack[i].isEmpty()) {
                        this.bakedModel[i] = Minecraft.getInstance().getItemRenderer().getModel(
                                output_stack[i],
                                this.getWorld(),
                                null, 0
                        );
                        this.transformedModel[i] = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(new PoseStack(),
                                this.bakedModel[i],
                                GROUND,
                                false
                        );
                    } else {
                        this.bakedModel[i] = null;
                        this.transformedModel[i] = null;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @OnlyIn(Dist.CLIENT)
    public BakedModel getTransformedModel() {
        return this.transformedModel[indexModel];
    }

    public void setOverclockRates() {
        double newStorage = 0;
        for (int i = 0; i < maxAmount; i++) {
            MachineRecipe output = this.getRecipeOutput(i) == null ? getOutput(i) : this.getRecipeOutput(i);
            if (!this.queue) {
                if (inputSlot[i].isEmpty() || !this.inputSlot[i].continue_proccess(this.outputSlot[i])) {
                    this.maxEnergySlots[i] = 0;
                    this.setActive(true);
                } else if (output != null) {
                    newStorage += output.getRecipe().output.metadata.getDouble("energy");
                    this.maxEnergySlots[i] = output.getRecipe().output.metadata.getDouble("energy");
                    energySlots[i] = Math.max(0, energySlots[i]);
                    energySlots[i] = Math.min(energySlots[i], maxEnergySlots[i]);
                    newStorage -= energySlots[i];
                } else {
                    this.maxEnergySlots[i] = 0;
                    this.energySlots[i] = 0;
                }
            } else {

                if (inputSlot[i].isEmpty() || !this.inputSlot[i].continue_proccess(this.outputSlot[i])) {
                    this.maxEnergySlots[i] = 0;
                    this.energySlots[i] = 0;
                } else if (output != null) {
                    int size;
                    ItemStack output1 = this.output[i].getRecipe().output.items.get(0);
                    size = this.output[i].getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();
                    size = (int) Math.floor((float) this.inputSlot[i].get(0).getCount() / size);
                    int size1 = !this.outputSlot[i].isEmpty()
                            ? (64 - this.outputSlot[i].get(0).getCount()) / output1.getCount()
                            : 64 / output1.getCount();

                    size = Math.min(size1, size);
                    size = Math.min(size, output1.getMaxStackSize());
                    this.size_recipe[i] = size;

                    newStorage += output.getRecipe().output.metadata.getDouble("energy") * size;
                    this.maxEnergySlots[i] = output.getRecipe().output.metadata.getDouble("energy") * size;
                    energySlots[i] = Math.max(0, energySlots[i]);
                    energySlots[i] = Math.min(energySlots[i], maxEnergySlots[i]);
                    newStorage -= energySlots[i];
                } else {
                    this.maxEnergySlots[i] = 0;
                    this.energySlots[i] = 0;
                }
            }
        }
        this.energy.setCapacity(newStorage);

    }

    public void updateEntityServer() {
        super.updateEntityServer();
        this.differenceenergy = this.energy.getEnergy() - this.perenergy;
        this.perenergy = this.energy.getEnergy();
        this.energy.useEnergy(perenergy);
        this.energy.setCapacity(this.energy.getCapacity() - perenergy);
        double prev = this.perenergy;
        distributeEnergy(prev);
        boolean active = false;
        for (int i = 0; i < maxAmount; i++) {
            MachineRecipe output = this.getRecipeOutput(i);
            if (!queue) {
                this.guiProgress[i] = (Math.ceil(this.energySlots[i]) / this.maxEnergySlots[i]);
                if (output != null && this.outputSlot[i].canAdd(output.getRecipe().output.items) && this.guiProgress[i] >= 0) {
                    if (!this.getActive()) {
                        if (this.level.getGameTime() % 2 == 0) {
                            initiate(0);
                        }
                        setActive(true);
                        setOverclockRates();
                    }
                    active = true;

                    this.guiProgress[i] = (Math.ceil(this.energySlots[i]) / this.maxEnergySlots[i]);
                    if (this.guiProgress[i] >= 1 && this.guiProgress[i] != Double.POSITIVE_INFINITY) {
                        operate(i, output);
                        this.guiProgress[i] = 0;
                        if (this.level.getGameTime() % 2 == 0) {
                            initiate(2);
                        }
                    }
                } else {
                    if (this.energy.getEnergy() != 0 && getActive()) {
                        if (this.level.getGameTime() % 2 == 0) {
                            initiate(1);
                        }
                    }
                    this.guiProgress[i] = 0;
                    setActive(false);
                }

            } else {
                if (output != null && this.inputSlot[i].continue_proccess(this.outputSlot[i])) {
                    active = true;


                    int size;
                    ItemStack output1 = this.output[i].getRecipe().output.items.get(0);
                    size = this.output[i].getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();

                    size = (int) Math.floor((float) this.inputSlot[i].get(0).getCount() / size);
                    int size1 = !this.outputSlot[i].isEmpty()
                            ? (64 - this.outputSlot[i].get(0).getCount()) / output1.getCount()
                            : 64 / output1.getCount();

                    size = Math.min(size1, size);
                    size = Math.min(size, output1.getMaxStackSize());
                    if (this.size_recipe[i] != size) {
                        this.setOverclockRates();
                    }


                    this.guiProgress[i] = (Math.ceil(this.energySlots[i]) / this.maxEnergySlots[i]);

                    if (this.guiProgress[i] >= 1) {
                        operate(i, output, size);
                        initiate(2);
                    }
                } else {
                    if (this.energy.getEnergy() != 0 && getActive()) {
                        initiate(1);
                    }
                    setActive(false);
                }

            }
            if (getActive() && output == null) {
                setActive(false);
            }
        }
        if (!this.getActive() && active) {
            if (this.level.getGameTime() % 2 == 0) {
                initiate(0);
            }
            setActive(true);
            setOverclockRates();

        }
        for (int ii = 0; ii < maxAmount; ii++) {
            if (!this.outputSlot[ii].isEmpty() && this.getWorld().getGameTime() % 10 == 0) {
                ModUtils.tick(this.outputSlot[ii], this);
            }
        }
    }

    private void distributeEnergy(double inputEnergy) {
        int size = maxAmount;
        double totalNeed = 0;
        for (int i = 0; i < 4; i++) {
            energySlots[i] = Math.max(energySlots[i], 0);
        }
        int size1 = 0;
        for (int i = 0; i < size; i++) {
            totalNeed += Math.max(0, maxEnergySlots[i] - energySlots[i]);
            if (maxEnergySlots[i] > 0)
                size1++;
        }
        if (totalNeed <= 0)
            return;
        double minEnergyPerSlot = inputEnergy * 0.1 / size1;
        if (inputEnergy >= totalNeed) {
            for (int i = 0; i < size; i++) {
                energySlots[i] = maxEnergySlots[i];
            }
        } else {
            for (int i = 0; i < size; i++) {
                double need = maxEnergySlots[i] - energySlots[i];
                if (need > 0) {
                    energyShare[i] = Math.max(Math.min(minEnergyPerSlot, need), (inputEnergy * need / totalNeed));
                    energySlots[i] += energyShare[i];
                    inputEnergy -= energyShare[i];
                }
            }
            for (int i = 0; i < size && inputEnergy > 0; i++) {
                double remainingCapacity = maxEnergySlots[i] - energySlots[i];
                if (remainingCapacity > 0) {
                    double additionalEnergy = Math.min(inputEnergy, remainingCapacity);
                    energySlots[i] += additionalEnergy;
                    energyShare[i] += additionalEnergy;
                    inputEnergy -= additionalEnergy;
                }
            }
        }


    }

    public MachineRecipe getOutput(int i) {

        this.output[i] = this.inputSlot[i].process();
        if (this.output[i] != null) {
            output_stack[i] = this.output[i].getRecipe().output.items.get(0);
        } else {
            output_stack[i] = new ItemStack(Items.AIR);
        }

        return this.output[i];
    }

    public double getProgress(int i) {
        return Math.min(this.guiProgress[i], 1);
    }


    public String getStartSoundFile() {
        return "Machines/molecular.ogg";
    }

    @Override
    public void onUpdate() {

        for (int i = 0; i < 4; i++) {
            if (this.inputSlot[i].get(0).getItem() instanceof EnchantedBookItem) {
                this.need = true;
                return;
            }
            if (this.inputSlot[i].get(0).getItem() instanceof PotionItem) {
                this.need = true;
                return;
            }
        }
        this.need = false;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return getRecipeOutput(0);
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        setRecipeOutput(0, output);
    }

    @Override
    public MachineRecipe getRecipeOutput(int i) {
        return this.output[i];
    }

    @Override
    public void setRecipeOutput(int i, final MachineRecipe output) {
        this.output[i] = output;
        if (this.output[i] != null) {
            output_stack[i] = this.output[i].getRecipe().output.items.get(0);
        } else {
            output_stack[i] = new ItemStack(Items.AIR);
            this.energySlots[i] = 0;
            this.maxEnergySlots[i] = 0;
        }
        new PacketUpdateFieldTile(this, "output", this.output_stack);
        this.setOverclockRates();

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.molecular.getSoundEvent();
    }

}
