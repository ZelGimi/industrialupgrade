package com.denfop.tiles.base;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.recipe.*;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerBase;
import com.denfop.container.ContainerQuantumMolecular;
import com.denfop.gui.GuiCore;
import com.denfop.gui.GuiQuantumTransformer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.item.ItemDisplayContext.GROUND;


public class TileQuantumMolecular extends TileElectricMachine implements
        IUpdatableTileEvent, IUpdateTick, IHasRecipe, IIsMolecular {

    public boolean need;
    public MachineRecipe output;
    public List<Double> time;
    public boolean queue;
    public byte redstoneMode;
    public int operationLength;
    public boolean need_put_check = false;
    public int operationsPerTick;
    public InvSlotRecipes inputSlot;
    public double perenergy;
    public double differenceenergy;
    protected double progress;
    protected double guiProgress;
    protected int size_recipe = 0;
    protected ItemStack output_stack;
    ComponentBaseEnergy energy;
    @OnlyIn(Dist.CLIENT)
    private BakedModel bakedModel;
    @OnlyIn(Dist.CLIENT)
    private BakedModel transformedModel;

    public TileQuantumMolecular(BlockPos pos, BlockState state) {
        super(0, 14, 1, BlockBaseMachine3.quantum_transformer, pos, state);
        this.progress = 0;
        this.time = new ArrayList<>();
        this.queue = false;
        this.redstoneMode = 0;
        this.inputSlot = new InvSlotRecipes(this, "quantummolecular", this) {
            @Override
            public ItemStack set(final int index, final ItemStack content) {
                super.set(index, content);
                if (((TileQuantumMolecular) this.tile).getOutput() == null) {
                    if (!content.isEmpty()) {
                        final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                this.getRecipe(),
                                this.getRecipe_list(),
                                false,
                                Arrays.asList(
                                        content,
                                        content
                                )
                        );
                        ((TileQuantumMolecular) this.tile).need_put_check = recipe1 != null;
                    } else {
                        if (!this.get(0).isEmpty() || !this.get(1).isEmpty()) {
                            if (!this.get(0).isEmpty()) {
                                final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                        this.getRecipe(),
                                        this.getRecipe_list(),
                                        false,
                                        Arrays.asList(
                                                this.get(0),
                                                this.get(0)
                                        )
                                );
                                ((TileQuantumMolecular) this.tile).need_put_check = recipe1 != null;
                            } else {
                                final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                        this.getRecipe(),
                                        this.getRecipe_list(),
                                        false,
                                        Arrays.asList(
                                                this.get(1),
                                                this.get(1)
                                        )
                                );
                                ((TileQuantumMolecular) this.tile).need_put_check = recipe1 != null;
                            }
                        } else {
                            ((TileQuantumMolecular) this.tile).need_put_check = false;
                        }
                    }
                } else {
                    ((TileQuantumMolecular) this.tile).need_put_check = false;
                }
                return content;
            }
        };
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 0, 14));
        this.output = null;
        this.need = false;
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack stack, ItemStack stack2, ItemStack stack1, double energy) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("quantummolecular", new BaseMachineRecipe(
                new Input(input.getInput(stack), input.getInput(stack2)),
                new RecipeOutput(nbt, stack1)
        ));
    }

    public static void addrecipe(ItemStack stack, String stack2, ItemStack stack1, double energy) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("quantummolecular", new BaseMachineRecipe(
                new Input(input.getInput(stack), input.getInput(stack2)),
                new RecipeOutput(nbt, stack1)
        ));
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.quantum_transformer;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2.getBlock(getTeBlock());
    }

    @Override
    public ItemStack getItemStack() {
        return this.output_stack;
    }

    @Override
    public TileEntityBlock getEntityBlock() {
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BakedModel getBakedModel() {
        return bakedModel;
    }

    public void init() {


        addrecipe(
                new ItemStack(IUItem.crafting_elements.getStack(352)),
                new ItemStack(IUItem.crafting_elements.getStack(645)),
                new ItemStack(IUItem.crafting_elements.getStack(642)),
                25000000
        );
        addrecipe(
                new ItemStack(IUItem.crafting_elements.getStack(642)),
                new ItemStack(IUItem.crafting_elements.getStack(647)),
                new ItemStack(IUItem.crafting_elements.getStack(646)),
                25000000
        );

    }


    @Override
    public ContainerQuantumMolecular getGuiContainer(Player entityPlayer) {
        return new ContainerQuantumMolecular(entityPlayer, this);
    }



    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
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
            energy.onNetworkUpdate(customPacketBuffer);
            output_stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);

            if (!output_stack.isEmpty()) {
                this.bakedModel = Minecraft.getInstance().getItemRenderer().getModel(
                        output_stack,
                        this.getWorld(),
                        null, 0
                );
                this.transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(new PoseStack(),
                        this.bakedModel,
                       GROUND,
                        false
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
            queue = (boolean) DecoderHandler.decode(customPacketBuffer);
            perenergy = (double) DecoderHandler.decode(customPacketBuffer);
            differenceenergy = (double) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, queue);
            EncoderHandler.encode(packet, perenergy);
            EncoderHandler.encode(packet, differenceenergy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    public void readFromNBT(CompoundTag nbttagcompound) {
        super.readFromNBT(nbttagcompound);


        this.queue = nbttagcompound.getBoolean("queue");
        this.progress = nbttagcompound.getDouble("progress");

    }

    public void onLoaded() {
        super.onLoaded();
        if (!level.isClientSide) {
            inputSlot.load();
            this.setOverclockRates();
            this.onUpdate();

        }

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.molecular.getSoundEvent();
    }

    public CompoundTag writeToNBT(CompoundTag nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.putByte("redstoneMode", this.redstoneMode);
        nbttagcompound.putDouble("progress", this.progress);
        nbttagcompound.putBoolean("queue", this.queue);
        return nbttagcompound;

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public GuiCore<ContainerBase<? extends IAdvInventory>> getGui(Player var1, ContainerBase<? extends IAdvInventory> menu) {

        return new GuiQuantumTransformer((ContainerQuantumMolecular) menu);
    }


    public void updateTileServer(Player player, double event) {

        if (event == 1) {
            this.queue = !this.queue;
            if (this.need) {
                this.queue = false;
            }
            this.setOverclockRates();
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
    }

    public void updateField(String name, CustomPacketBuffer is) {
        if (name.equals("redstoneMode")) {
            try {
                redstoneMode = (byte) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (name.equals("output")) {
            try {
                this.output_stack = (ItemStack) DecoderHandler.decode(is);
                if (!output_stack.isEmpty()) {
                    this.bakedModel = Minecraft.getInstance().getItemRenderer().getModel(
                            output_stack,
                            this.getWorld(),
                            null, 0
                    );
                    this.transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(new PoseStack(),
                            this.bakedModel,
                            GROUND,
                            false
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BakedModel getTransformedModel() {
        return transformedModel;
    }

    public void setChanged() {
        super.setChanged();
        if (!level.isClientSide) {
            setOverclockRates();
        }
    }

    public void operate(MachineRecipe output) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult);
        if (!this.inputSlot.continue_process(this.output)) {
            getOutput();
            if (!this.inputSlot.continue_process(this.output)) {
                getOutput();
                if (!this.inputSlot.get(0).isEmpty() || !this.inputSlot.get(1).isEmpty()) {
                    if (!this.inputSlot.get(0).isEmpty()) {
                        final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                this.inputSlot.getRecipe(),
                                this.inputSlot.getRecipe_list(),
                                false,
                                Arrays.asList(
                                        this.inputSlot.get(0),
                                        this.inputSlot.get(0)
                                )
                        );
                        this.need_put_check = recipe1 != null;
                    } else {
                        final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                                this.inputSlot.getRecipe(),
                                this.inputSlot.getRecipe_list(),
                                false,
                                Arrays.asList(
                                        this.inputSlot.get(1),
                                        this.inputSlot.get(1)
                                )
                        );
                        this.need_put_check = recipe1 != null;
                    }
                } else {
                    this.need_put_check = false;
                }
            }
        }
    }

    public void operate(MachineRecipe output, int size) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult, size);
        if (!this.inputSlot.continue_process(this.output)) {
            getOutput();
            if (!this.inputSlot.get(0).isEmpty() || !this.inputSlot.get(1).isEmpty()) {
                if (!this.inputSlot.get(0).isEmpty()) {
                    final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                            this.inputSlot.getRecipe(),
                            this.inputSlot.getRecipe_list(),
                            false,
                            Arrays.asList(
                                    this.inputSlot.get(0),
                                    this.inputSlot.get(0)
                            )
                    );
                    this.need_put_check = recipe1 != null;
                } else {
                    final MachineRecipe recipe1 = Recipes.recipes.getRecipeMachineRecipeOutput(
                            this.inputSlot.getRecipe(),
                            this.inputSlot.getRecipe_list(),
                            false,
                            Arrays.asList(
                                    this.inputSlot.get(1),
                                    this.inputSlot.get(1)
                            )
                    );
                    this.need_put_check = recipe1 != null;
                }
            } else {
                this.need_put_check = false;
            }
        }
    }

    public void operateOnce(List<ItemStack> processResult) {
        if (this.outputSlot.canAdd(processResult)) {
            this.inputSlot.consume();
            this.outputSlot.add(processResult);
        }
    }

    public void operateOnce(List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {
            if (this.outputSlot.canAdd(processResult)) {
                this.inputSlot.consume();
                this.outputSlot.add(processResult);
            }
        }
    }

    public void setOverclockRates() {


        MachineRecipe output = getOutput();

        this.output = output;
        this.onUpdate();
        if (!this.queue) {
            if (inputSlot.isEmpty()) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy"));
            } else {
                this.energy.setCapacity(0);
            }
        } else {

            if (inputSlot.isEmpty()) {
                this.energy.setCapacity(0);
            } else if (output != null) {

                int size;
                int size2;
                ItemStack output1 = this.output.getRecipe().output.items.get(0);
                size = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();
                size2 = this.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                size = (int) Math.floor((float) this.inputSlot.get(0).getCount() / size);
                size2 = (int) Math.floor((float) this.inputSlot.get(1).getCount() / size2);
                size = Math.min(size, size2);
                int size1 = !this.outputSlot.isEmpty()
                        ? (64 - this.outputSlot.get(0).getCount()) / output1.getCount()
                        : 64 / output1.getCount();
                size = Math.min(size1, size);
                size = Math.min(output1.getMaxStackSize(), size);
                this.size_recipe = size;
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy") * size);
            } else {
                this.energy.setCapacity(0);
            }
        }


    }

    public void updateEntityServer() {
        super.updateEntityServer();

        MachineRecipe output = this.output;
        if (this.need_put_check) {
            if (!this.inputSlot.get(0).isEmpty()) {
                if (this.inputSlot.get(0).getCount() > 1) {
                    int count = this.inputSlot.get(0).getCount() / 2;
                    this.inputSlot.get(0).shrink(count);
                    ItemStack stack = this.inputSlot.get(0).copy();
                    stack.setCount(count);
                    this.inputSlot.set(1, stack);
                } else if (!this.inputSlot.get(1).isEmpty()) {
                    if (this.inputSlot.get(1).getCount() > 1) {
                        int count = this.inputSlot.get(1).getCount() / 2;
                        this.inputSlot.get(1).shrink(count);
                        ItemStack stack = this.inputSlot.get(1).copy();
                        stack.setCount(count);
                        this.inputSlot.set(0, stack);

                    }
                }
            }

        }
        if (!queue) {
            if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items)) {

                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                if (!this.getActive()) {
                    initiate(0);
                    setActive(true);
                    setOverclockRates();
                }


                this.progress = this.energy.getEnergy();
                double k = this.progress;
                this.guiProgress = (Math.ceil(k) / this.energy.getCapacity());
                if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                    operate(output);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    initiate(2);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    initiate(1);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }

        } else {
            if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items)) {
                if (!this.getActive()) {
                    initiate(2);
                    setActive(true);
                    setOverclockRates();
                }
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();

                int size = 0;
                int size2 = 0;
                ItemStack output1;
                output1 = this.output.getRecipe().getOutput().items.get(0);

                final List<ItemStack> list = this.output.getRecipe().input.getInputs().get(0).getInputs();
                boolean is = false;
                for (ItemStack stack : list) {
                    if (stack.is(this.inputSlot.get(0).getItem())) {
                        is = true;
                        size = stack.getCount();
                        size2 = this.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                        break;
                    }
                }
                if (!is) {
                    size = this.output.getRecipe().input.getInputs().get(1).getInputs().get(0).getCount();
                    size2 = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();

                }
                size = (int) Math.floor((float) this.inputSlot.get(0).getCount() / size);
                size2 = (int) Math.floor((float) this.inputSlot.get(1).getCount() / size2);
                size = Math.min(size, size2);
                int size1 = !this.outputSlot.isEmpty()
                        ? (64 - this.outputSlot.get(0).getCount()) / output1.getCount()
                        : 64 / output1.getCount();
                size = Math.min(size1, size);
                size = Math.min(output1.getMaxStackSize(), size);
                if (size != this.size_recipe) {
                    this.setOverclockRates();
                }
                this.progress = this.energy.getEnergy();
                double k = this.progress;
                double p = (k / (this.energy.getCapacity()));
                if (p <= 1) {
                    this.guiProgress = p;
                }
                if (p > 1) {
                    this.guiProgress = 1;
                }
                if (this.energy.getEnergy() >= this.energy.getCapacity()) {
                    operate(output, size);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());

                    initiate(2);
                }
            } else {
                if (this.energy.getEnergy() != 0 && getActive()) {
                    initiate(1);
                }
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                this.setActive(false);
            }
        }
        if (this.getActive() && output == null) {
            this.setActive(false);
        }
        if (!this.outputSlot.isEmpty()) {
            ModUtils.tick(this.outputSlot, this);
        }
    }

    public MachineRecipe getOutput() {

        this.output = this.inputSlot.process();
        if (this.output != null) {
            output_stack = this.output.getRecipe().output.items.get(0);
        } else {
            output_stack = ItemStack.EMPTY;
        }

        return this.output;
    }

    public double getProgress() {
        return Math.min(this.guiProgress, 1);
    }


    public String getStartSoundFile() {
        return "Machines/molecular.ogg";
    }


    @Override
    public void onUpdate() {
        for (int i = 0; i < this.inputSlot.size(); i++) {
            if (this.inputSlot.get(i).getItem() instanceof EnchantedBookItem) {
                this.need = true;
                return;
            }
            if (this.inputSlot.get(i).getItem() instanceof PotionItem) {
                this.need = true;
                return;
            }
        }
        this.need = false;
    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
        if (this.output != null) {
            output_stack = this.output.getRecipe().output.items.get(0);
        } else {
            output_stack = ItemStack.EMPTY;
        }

        new PacketUpdateFieldTile(this, "output", this.output_stack);
        this.setOverclockRates();

    }

    @Override
    public int getMode() {
        return this.redstoneMode;
    }

}
