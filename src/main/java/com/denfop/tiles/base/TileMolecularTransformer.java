package com.denfop.tiles.base;

import com.denfop.Config;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.energy.EnergyNetGlobal;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotOutput;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.componets.Energy;
import com.denfop.container.ContainerBaseMolecular;
import com.denfop.gui.GuiMolecularTransformer;
import com.denfop.items.resource.ItemNuclearResource;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TileMolecularTransformer extends TileElectricMachine implements
        IUpdatableTileEvent, IUpdateTick, IHasRecipe, IIsMolecular {

    public double[] energyShare;
    public boolean queue;
    public byte redstoneMode;
    public InvSlotRecipes[] inputSlot;
    public MachineRecipe[] output;
    public double perenergy;
    public double differenceenergy;
    public ItemStack[] output_stack = new ItemStack[4];
    public int maxAmount = 1;
    public InvSlotOutput[] outputSlot;
    public double[] energySlots;
    public double[] maxEnergySlots;
    protected double[] guiProgress;
    protected double[] size_recipe;
    int index;
    int indexModel = 0;
    int tick = 0;
    private boolean need = false;
    @SideOnly(Side.CLIENT)
    private IBakedModel[] bakedModel;
    @SideOnly(Side.CLIENT)
    private IBakedModel[] transformedModel;

    public TileMolecularTransformer() {
        super(0, 14, 0);
        this.queue = false;
        this.redstoneMode = 0;
        this.outputSlot = new InvSlotOutput[4];
        this.energy = this.addComponent(Energy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));
        this.inputSlot = new InvSlotRecipes[4];
        this.output = new MachineRecipe[4];
        this.energySlots = new double[4];
        this.guiProgress = new double[4];
        this.energyShare = new double[4];
        this.maxEnergySlots = new double[4];
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.bakedModel = new IBakedModel[4];
            this.transformedModel = new IBakedModel[4];
        }
        this.size_recipe = new double[4];

        for (int i = 0; i < 4; i++) {
            this.output_stack[i] = new ItemStack(Items.AIR);
            this.inputSlot[i] = new InvSlotRecipes(this, "molecular", this);
            this.inputSlot[i].setIndex(i);
            this.outputSlot[i] = new InvSlotOutput(this, 1);
        }
        Recipes.recipes.addInitRecipes(this);
    }

    public static void addrecipe(ItemStack stack, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "molecular",
                new BaseMachineRecipe(new Input(input.getInput(stack)), new RecipeOutput(nbt, stack1))
        );
    }

    public static void addrecipe(String stack, ItemStack stack1, double energy) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setDouble("energy", energy);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        Recipes.recipes.addRecipe(
                "molecular",
                new BaseMachineRecipe(new Input(input.getInput(stack)), new RecipeOutput(nbt, stack1))
        );

    }

    public IMultiTileBlock getTeBlock() {
        return BlockMolecular.molecular;
    }

    public BlockTileEntity getBlock() {
        return IUItem.blockmolecular;
    }

    public ItemStack getOutput_stack() {
        return output_stack[0];
    }

    public void init() {


        addrecipe(new ItemStack(Items.SKULL, 1, 1), new ItemStack(Items.NETHER_STAR, 1), Config.molecular1);


        addrecipe(
                IUItem.nuclear_res.getItemStack(ItemNuclearResource.Types.plutonium),
                new ItemStack(IUItem.proton, 1),
                Config.molecular3
        );

        addrecipe("ingotSpinel", new ItemStack(IUItem.iuingot, 1, 5), 2500000);

        addrecipe(new ItemStack(IUItem.photoniy), new ItemStack(IUItem.photoniy_ingot), Config.molecular5);

        addrecipe(new ItemStack(Blocks.NETHERRACK), new ItemStack(Items.GUNPOWDER, 2), Config.molecular6);

        addrecipe(new ItemStack(Blocks.SAND), new ItemStack(Blocks.GRAVEL, 1), Config.molecular7);


        addrecipe("ingotIridium", new ItemStack(IUItem.core, 1, 0), Config.molecular17);

        addrecipe(new ItemStack(IUItem.core, 4, 0), new ItemStack(IUItem.core, 1, 1), Config.molecular18);

        addrecipe(new ItemStack(IUItem.core, 4, 1), new ItemStack(IUItem.core, 1, 2), Config.molecular19);

        addrecipe(new ItemStack(IUItem.core, 4, 2), new ItemStack(IUItem.core, 1, 3), Config.molecular20);

        addrecipe(new ItemStack(IUItem.core, 4, 3), new ItemStack(IUItem.core, 1, 4), Config.molecular21);

        addrecipe(new ItemStack(IUItem.core, 4, 4), new ItemStack(IUItem.core, 1, 5), Config.molecular22);

        addrecipe(new ItemStack(IUItem.core, 4, 5), new ItemStack(IUItem.core, 1, 6), Config.molecular23);

        addrecipe(new ItemStack(IUItem.core, 4, 6), new ItemStack(IUItem.core, 1, 7), Config.molecular24);

        addrecipe(new ItemStack(IUItem.core, 4, 7), new ItemStack(IUItem.core, 1, 8), Config.molecular25);

        addrecipe(new ItemStack(IUItem.core, 4, 8), new ItemStack(IUItem.core, 1, 9), Config.molecular26);
        addrecipe(new ItemStack(IUItem.core, 4, 9), new ItemStack(IUItem.core, 1, 10), Config.molecular38);

        addrecipe(new ItemStack(IUItem.core, 4, 10), new ItemStack(IUItem.core, 1, 11), Config.molecular39);
        addrecipe(new ItemStack(IUItem.core, 4, 11), new ItemStack(IUItem.core, 1, 12), Config.molecular40);
        addrecipe(new ItemStack(IUItem.core, 4, 12), new ItemStack(IUItem.core, 1, 13), Config.molecular41);

        //
        addrecipe(new ItemStack(IUItem.crafting_elements, 8, 649), new ItemStack(IUItem.crafting_elements, 1, 645),
                20000000D
        );
        addrecipe(new ItemStack(IUItem.matter, 1, 1), new ItemStack(IUItem.lens, 1, 5), Config.molecular27);

        addrecipe(new ItemStack(IUItem.matter, 1, 2), new ItemStack(IUItem.lens, 1, 6), Config.molecular28);

        addrecipe(new ItemStack(IUItem.matter, 1, 3), new ItemStack(IUItem.lens, 1, 2), Config.molecular29);

        addrecipe(new ItemStack(IUItem.matter, 1, 4), new ItemStack(IUItem.lens, 1, 4), Config.molecular30);

        addrecipe(new ItemStack(IUItem.matter, 1, 5), new ItemStack(IUItem.lens, 1, 1), Config.molecular31);

        addrecipe(new ItemStack(IUItem.matter, 1, 6), new ItemStack(IUItem.lens, 1, 3), Config.molecular32);

        addrecipe(new ItemStack(IUItem.matter, 1, 7), new ItemStack(IUItem.lens, 1), Config.molecular33);
        addrecipe(new ItemStack(Items.IRON_INGOT, 1, 0), IUItem.iridiumOre,
                7500000
        );
        addrecipe(
                IUItem.iridiumOre,
                new ItemStack(IUItem.photoniy),
                Config.molecular34
        );


    }

    public List<Double> getTime(int i) {
        final double dif = this.energyShare[i];
        return ModUtils.Time(((1 - this.guiProgress[i]) * this.maxEnergySlots[i]) / (dif * 20));
    }

    public double getInput() {
        return EnergyNetGlobal.instance
                .getNodeStats(this.energy.getDelegate())
                .getEnergyIn();
    }

    @Override
    public ContainerBaseMolecular getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerBaseMolecular(entityPlayer, this);
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
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!this.getWorld().isRemote && maxAmount == 1 && stack.getItem() == IUItem.double_molecular) {
            maxAmount = 2;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "maxAmount", this.maxAmount);
            return true;
        }
        if (!this.getWorld().isRemote && maxAmount == 2 && stack.getItem() == IUItem.quad_molecular) {
            maxAmount = 4;
            stack.shrink(1);
            new PacketUpdateFieldTile(this, "maxAmount", this.maxAmount);
            return true;
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
                    this.bakedModel[i] = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                            output_stack[i],
                            this.getWorld(),
                            null
                    );
                    this.transformedModel[i] = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                            this.bakedModel[i],
                            ItemCameraTransforms.TransformType.GROUND,
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

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);

        maxAmount = nbttagcompound.getInteger("maxAmount");
        for (int i = 0; i < maxAmount; i++) {
            this.energySlots[i] = nbttagcompound.getDouble("energySlots" + i);
        }
        this.queue = nbttagcompound.getBoolean("queue");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
        nbttagcompound.setBoolean("queue", this.queue);
        nbttagcompound.setInteger("maxAmount", this.maxAmount);
        for (int i = 0; i < maxAmount; i++) {
            nbttagcompound.setDouble("energySlots" + i, this.energySlots[i]);
        }
        return nbttagcompound;

    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiMolecularTransformer(new ContainerBaseMolecular(entityPlayer, this));
    }

    public void updateTileServer(EntityPlayer player, double event) {
        if (this.getWorld().isRemote) {
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

    public void markDirty() {
        super.markDirty();
        if (IUCore.proxy.isSimulating()) {
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
        this.outputSlot[i].add(processResult);
        this.energySlots[i] = 0;
        if (!this.inputSlot[i].continue_process(this.output[i]) || !this.outputSlot[i].canAdd(output[i].getRecipe().output.items)) {
            getOutput(i);
        }
        setOverclockRates();
    }

    public void operateOnce(int index, List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {
            this.inputSlot[index].consume();
            this.outputSlot[index].add(processResult);
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
    public TileEntityBlock getEntityBlock() {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBakedModel() {
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
        if (IUCore.proxy.isSimulating()) {
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
                        this.bakedModel[i] = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(
                                output_stack[i],
                                this.getWorld(),
                                null
                        );
                        this.transformedModel[i] = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(
                                this.bakedModel[i],
                                ItemCameraTransforms.TransformType.GROUND,
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

    public IBakedModel getTransformedModel() {
        return this.transformedModel[indexModel];
    }

    public void setOverclockRates() {
        double newStorage = 0;
        for (int i = 0; i < maxAmount; i++) {
            MachineRecipe output = this.getRecipeOutput(i) == null ? getOutput(i) : this.getRecipeOutput(i);
            if (!this.queue) {
                if (inputSlot[i].isEmpty() || !this.inputSlot[i].continue_proccess(this.outputSlot[i])) {
                    this.maxEnergySlots[i] = 0;
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
                    size = (int) Math.floor((float) this.inputSlot[i].get().getCount() / size);
                    int size1 = !this.outputSlot[i].isEmpty()
                            ? (64 - this.outputSlot[i].get().getCount()) / output1.getCount()
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
                if (output != null && this.outputSlot[i].canAdd(output.getRecipe().output.items) && this.guiProgress[i] >= 0) {
                    if (!this.getActive()) {
                        if (this.world.provider.getWorldTime() % 2 == 0) {
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
                        if (this.world.provider.getWorldTime() % 2 == 0) {
                            initiate(2);
                        }
                    }
                } else {
                    if (this.energy.getEnergy() != 0 && getActive()) {
                        if (this.world.provider.getWorldTime() % 2 == 0) {
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

                    size = (int) Math.floor((float) this.inputSlot[i].get().getCount() / size);
                    int size1 = !this.outputSlot[i].isEmpty()
                            ? (64 - this.outputSlot[i].get().getCount()) / output1.getCount()
                            : 64 / output1.getCount();

                    size = Math.min(size1, size);
                    size = Math.min(size, output1.getMaxStackSize());
                    if (this.size_recipe[i] != size) {
                        this.setOverclockRates();
                    }


                    this.guiProgress[i] = (Math.ceil(this.energySlots[i]) / this.maxEnergySlots[i]);

                    if (this.guiProgress[i] >= 1 && this.guiProgress[i] != Double.POSITIVE_INFINITY) {
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
            if (this.world.provider.getWorldTime() % 2 == 0) {
                initiate(0);
            }
            setActive(true);
            setOverclockRates();

        }
        for (int ii = 0; ii < maxAmount; ii++) {
            if (!this.outputSlot[ii].isEmpty() && this.getWorld().provider.getWorldTime() % 10 == 0) {
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
            if (maxEnergySlots[i] > 0) {
                size1++;
            }
        }
        if (totalNeed <= 0) {
            return;
        }
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
            if (this.inputSlot[i].get().getItem() instanceof ItemEnchantedBook) {
                this.need = true;
                return;
            }
            if (this.inputSlot[i].get().getItem() instanceof ItemPotion) {
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
