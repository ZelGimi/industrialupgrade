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
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockMolecular;
import com.denfop.componets.AdvEnergy;
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
import net.minecraft.client.gui.GuiScreen;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

public class TileMolecularTransformer extends TileElectricMachine implements
        IUpdatableTileEvent, IUpdateTick, IHasRecipe, IIsMolecular {

    public boolean queue;
    public byte redstoneMode;
    public int operationLength;
    public int operationsPerTick;
    public InvSlotRecipes inputSlot;
    public MachineRecipe output;
    public double perenergy;
    public double differenceenergy;
    public double size;
    protected double progress;
    protected double guiProgress;
    protected double size_recipe = 0;
    protected ItemStack output_stack = new ItemStack(Items.AIR);
    private boolean need = false;

    public TileMolecularTransformer() {
        super(0, 14, 1);
        this.progress = 0;
        this.queue = false;
        this.redstoneMode = 0;
        this.energy = this.addComponent(AdvEnergy.asBasicSink(this, 0, 14).addManagedSlot(this.dischargeSlot));
        this.inputSlot = new InvSlotRecipes(this, "molecular", this);
        this.output = null;
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
        return output_stack;
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


    public List<Double> getTime(final double energy) {
        final double dif = this.differenceenergy;
        return ModUtils.Time((energy - this.energy.getEnergy()) / (dif * 20));
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
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
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
            EncoderHandler.encode(packet, queue);
            EncoderHandler.encode(packet, redstoneMode);
            EncoderHandler.encode(packet, differenceenergy);
            EncoderHandler.encode(packet, perenergy);
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
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
    }


    public CustomPacketBuffer writePacket() {
        final CustomPacketBuffer packet = super.writePacket();
        try {
            EncoderHandler.encode(packet, guiProgress);
            EncoderHandler.encode(packet, queue);
            EncoderHandler.encode(packet, redstoneMode);
            EncoderHandler.encode(packet, energy);
            EncoderHandler.encode(packet, output_stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public void readPacket(CustomPacketBuffer customPacketBuffer) {
        super.readPacket(customPacketBuffer);
        try {
            guiProgress = (double) DecoderHandler.decode(customPacketBuffer);
            queue = (boolean) DecoderHandler.decode(customPacketBuffer);
            redstoneMode = (byte) DecoderHandler.decode(customPacketBuffer);
            energy.readFromNbt((NBTTagCompound) DecoderHandler.decode(customPacketBuffer));
            output_stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
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


        this.queue = nbttagcompound.getBoolean("queue");
        this.redstoneMode = nbttagcompound.getByte("redstoneMode");

        this.progress = nbttagcompound.getDouble("progress");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setByte("redstoneMode", this.redstoneMode);
        nbttagcompound.setBoolean("queue", this.queue);
        nbttagcompound.setDouble("progress", this.progress);
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

    public void operate(MachineRecipe output) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult);
    }

    public void operate(MachineRecipe output, int size) {
        List<ItemStack> processResult = output.getRecipe().output.items;
        operateOnce(processResult, size);
    }

    public void operateOnce(List<ItemStack> processResult) {

        this.inputSlot.consume();
        this.outputSlot.add(processResult);
        if (!this.inputSlot.continue_process(this.output) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
            getOutput();
        }
    }

    public void operateOnce(List<ItemStack> processResult, int size) {
        for (int i = 0; i < size; i++) {
            this.inputSlot.consume();
            this.outputSlot.add(processResult);
        }
        if (!this.inputSlot.continue_process(this.output) || !this.outputSlot.canAdd(output.getRecipe().output.items)) {
            getOutput();
        }
    }

    @Override
    public int getMode() {
        return this.redstoneMode;
    }

    @Override
    public ItemStack getItemStack() {
        return this.output_stack;
    }

    @Override
    public TileEntityBlock getEntityBlock() {
        return this;
    }

    public void onLoaded() {
        super.onLoaded();
        if (IUCore.proxy.isSimulating()) {
            inputSlot.load();
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
        super.updateField(name, is);
    }

    public void setOverclockRates() {
        MachineRecipe output = this.getRecipeOutput() == null ? getOutput() : this.getRecipeOutput();
        if (!this.queue) {
            if (inputSlot.isEmpty() || !this.inputSlot.continue_proccess(this.outputSlot)) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy"));
            } else {
                this.energy.setCapacity(0);
            }
        } else {

            if (inputSlot.isEmpty() || !this.inputSlot.continue_proccess(this.outputSlot)) {
                this.energy.setCapacity(0);
            } else if (output != null) {
                int size;
                ItemStack output1 = this.output.getRecipe().output.items.get(0);
                size = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();
                size = (int) Math.floor((float) this.inputSlot.get().getCount() / size);
                int size1 = !this.outputSlot.isEmpty()
                        ? (64 - this.outputSlot.get().getCount()) / output1.getCount()
                        : 64 / output1.getCount();

                size = Math.min(size1, size);
                size = Math.min(size, output1.getMaxStackSize());
                this.size_recipe = size;
                this.energy.setCapacity(output.getRecipe().output.metadata.getDouble("energy") * size);
            } else {
                this.energy.setCapacity(0);
            }
        }


    }

    public void updateEntityServer() {
        super.updateEntityServer();

        MachineRecipe output = this.getRecipeOutput();


        if (!queue) {
            if (output != null && this.outputSlot.canAdd(output.getRecipe().output.items)) {
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                if (!this.getActive()) {
                    if (this.world.provider.getWorldTime() % 2 == 0) {
                        initiate(0);
                    }
                    setActive(true);
                    setOverclockRates();
                }


                try {
                    this.guiProgress = (this.energy.getEnergy() / this.energy.getCapacity());
                } catch (Exception e) {
                    this.guiProgress = 0;
                }

                if (this.guiProgress >= 1) {
                    operate(output);

                    this.progress = 0;
                    this.energy.useEnergy(this.energy.getEnergy());
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
                this.energy.useEnergy(this.energy.getEnergy());
                this.energy.setCapacity(0);
                setActive(false);
            }

        } else {
            if (output != null && this.inputSlot.continue_proccess(this.outputSlot)) {
                this.differenceenergy = this.energy.getEnergy() - this.perenergy;
                this.perenergy = this.energy.getEnergy();
                if (!this.getActive()) {
                    if (this.world.provider.getWorldTime() % 2 == 0) {
                        initiate(0);
                    }
                    setActive(true);
                    setOverclockRates();

                }


                int size;
                ItemStack output1 = this.output.getRecipe().output.items.get(0);
                size = this.output.getRecipe().input.getInputs().get(0).getInputs().get(0).getCount();

                size = (int) Math.floor((float) this.inputSlot.get().getCount() / size);
                int size1 = !this.outputSlot.isEmpty()
                        ? (64 - this.outputSlot.get().getCount()) / output1.getCount()
                        : 64 / output1.getCount();

                size = Math.min(size1, size);
                size = Math.min(size, output1.getMaxStackSize());
                if (this.size_recipe != size) {
                    this.setOverclockRates();
                }

                try {
                    this.guiProgress = (this.energy.getEnergy() / this.energy.getCapacity());
                } catch (Exception e) {
                    this.guiProgress = 0;
                }

                if (this.guiProgress >= 1) {
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
                setActive(false);
            }

        }
        if (getActive() && output == null) {
            setActive(false);
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


        this.setOverclockRates();

    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.molecular.getSoundEvent();
    }

}
