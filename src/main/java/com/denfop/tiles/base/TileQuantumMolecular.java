package com.denfop.tiles.base;

import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerQuantumMolecular;
import com.denfop.gui.GuiQuantumTransformer;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    @SideOnly(Side.CLIENT)
    private IBakedModel bakedModel;
    @SideOnly(Side.CLIENT)
    private IBakedModel transformedModel;

    public TileQuantumMolecular() {
        super(0, 14, 1);
        this.progress = 0;
        this.time = new ArrayList<>();
        this.queue = false;
        this.redstoneMode = 0;
        this.inputSlot = new InvSlotRecipes(this, "quantummolecular", this) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
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
            }
        };
        this.energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.QUANTUM, this, 0, 14));
        this.output = null;
        this.need = false;
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack stack, ItemStack stack2, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe("quantummolecular", new BaseMachineRecipe(
                new Input(input.getInput(stack), input.getInput(stack2)),
                new RecipeOutput(nbt, stack1)
        ));
    }

    public static void addrecipe(ItemStack stack, String stack2, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
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
        return IUItem.basemachine2;
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
    @SideOnly(Side.CLIENT)
    public IBakedModel getBakedModel() {
        return bakedModel;
    }

    public void init() {


        addrecipe(
                new ItemStack(IUItem.crafting_elements, 1, 352),
                new ItemStack(IUItem.crafting_elements, 1, 645),
                new ItemStack(IUItem.crafting_elements, 1, 642),
                25000000
        );
        addrecipe(
                new ItemStack(IUItem.crafting_elements, 1, 642),
                new ItemStack(IUItem.crafting_elements, 1, 647),
                new ItemStack(IUItem.crafting_elements, 1, 646),
                25000000
        );

    }


    @Override
    public ContainerQuantumMolecular getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerQuantumMolecular(entityPlayer, this);
    }


    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
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

            this.bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                    output_stack,
                    this.getWorld(),
                    null
            );
            this.transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                    this.bakedModel,
                    ItemCameraTransforms.TransformType.GROUND,
                    false
            );
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);


        this.queue = nbttagcompound.getBoolean("queue");
        this.progress = nbttagcompound.getDouble("progress");

    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlot.load();
            this.setOverclockRates();
            this.onUpdate();

        }

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.molecular.getSoundEvent();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
        nbttagcompound.setDouble("progress", this.progress);
        nbttagcompound.setBoolean("queue", this.queue);
        return nbttagcompound;

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiQuantumTransformer(new ContainerQuantumMolecular(entityPlayer, this));
    }


    public void updateTileServer(EntityPlayer player, double event) {

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
                    this.bakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                            output_stack,
                            this.getWorld(),
                            null
                    );
                    this.transformedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                            this.bakedModel,
                            ItemCameraTransforms.TransformType.GROUND,
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
    @SideOnly(Side.CLIENT)
    public IBakedModel getTransformedModel() {
        return transformedModel;
    }

    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
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
                        ? (64 - this.outputSlot.get().getCount()) / output1.getCount()
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
                    this.inputSlot.put(1, stack);
                } else if (!this.inputSlot.get(1).isEmpty()) {
                    if (this.inputSlot.get(1).getCount() > 1) {
                        int count = this.inputSlot.get(1).getCount() / 2;
                        this.inputSlot.get(1).shrink(count);
                        ItemStack stack = this.inputSlot.get(1).copy();
                        stack.setCount(count);
                        this.inputSlot.put(0, stack);

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
                    if (stack.isItemEqual(this.inputSlot.get(0))) {
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
                        ? (64 - this.outputSlot.get().getCount()) / output1.getCount()
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
            output_stack = new ItemStack(Items.AIR);
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
            if (this.inputSlot.get(i).getItem() instanceof ItemEnchantedBook) {
                this.need = true;
                return;
            }
            if (this.inputSlot.get(i).getItem() instanceof ItemPotion) {
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
            output_stack = new ItemStack(Items.AIR);
        }

        new PacketUpdateFieldTile(this, "output", this.output_stack);
        this.setOverclockRates();

    }

    @Override
    public int getMode() {
        return this.redstoneMode;
    }

}
