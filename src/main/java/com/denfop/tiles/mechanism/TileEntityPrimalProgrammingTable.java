package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockPrimalProgrammingTable;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerPrimalProgrammingTable;
import com.denfop.gui.GuiPrimalProgrammingTable;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TileEntityPrimalProgrammingTable extends TileElectricMachine implements IUpdateTick, IHasRecipe,
        IUpdatableTileEvent {


    private static final int RED = 0;
    private static final int GRAY = 1;
    private static final int GREEN = 2;
    private static final int YELLOW = 3;
    private static final int SIZE = 146;
    private static Random random = new Random();
    public final ComponentProgress componentProgress;
    private final int MIN_DISTANCE = 5;
    private final int MAX_ATTEMPTS = 100;
    public InvSlotRecipes inputSlotA;
    public MachineRecipe output;
    public boolean start;
    public int[] data;
    private int RED_PERCENT = 35;
    private int GREEN_PERCENT = 80;
    private int YELLOW_PERCENT = 100 - RED_PERCENT - GREEN_PERCENT;
    public Map<UUID,Double> data1 = PrimitiveHandler.getPlayersData(EnumPrimitive.PCB);

    public TileEntityPrimalProgrammingTable() {
        super(0, 0, 1);
        this.output = null;
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));


        this.inputSlotA = new InvSlotRecipes(this, "programming", this);
        Recipes.recipes.addInitRecipes(this);
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    @Override
    public boolean hasCapability(@NotNull final Capability<?> capability, final EnumFacing facing) {
        return super.hasCapability(capability, facing) && capability != CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    public boolean doesSideBlockRendering(EnumFacing side) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(EnumFacing side, BlockPos otherPos) {
        return false;
    }

    public boolean shouldRenderInPass(int pass) {
        return true;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    private int[] generateColorStrip() {
        int[] data = new int[SIZE];

        fillGrayZones(data);

        createGrayGaps(data);

        createIslandsWithSpacing(data, RED, RED_PERCENT);
        createIslandsWithSpacing(data, GREEN, GREEN_PERCENT);

        placeYellow(data);

        return data;
    }

    private void fillGrayZones(int[] data) {
        for (int i = 0; i < SIZE; i++) {
            data[i] = GRAY;
        }
    }

    private void createGrayGaps(int[] data) {
        int index = 0;
        while (index < SIZE) {
            int length = random.nextInt(11) + 10;
            if (index + length > SIZE) {
                length = SIZE - index;
            }
            for (int i = index; i < index + length; i++) {
                data[i] = GRAY;
            }
            index += length + MIN_DISTANCE;
        }
    }

    private void createIslandsWithSpacing(int[] data, int color, int percent) {
        int totalColorCount = SIZE * percent / 100;
        int numIslands = random.nextInt(3) + 3;

        while (totalColorCount > 0 && numIslands > 0) {
            int islandLength = random.nextInt(21) + 10;
            if (islandLength > totalColorCount) {
                islandLength = totalColorCount;
            }


            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < MAX_ATTEMPTS) {
                int startIndex = random.nextInt(SIZE - islandLength);
                if (isValidPlacement(data, startIndex, islandLength, color)) {
                    for (int i = startIndex; i < startIndex + islandLength; i++) {
                        data[i] = color;
                    }
                    totalColorCount -= islandLength;
                    placed = true;
                    numIslands--;
                }
                attempts++;
            }

            if (attempts >= MAX_ATTEMPTS) {
                break;
            }
        }
    }

    private boolean isValidPlacement(int[] data, int startIndex, int length, int color) {
        for (int i = startIndex; i < startIndex + length; i++) {
            if (data[i] != GRAY) {
                return false;
            }
        }


        if (startIndex > 0 && data[startIndex - 1] == color) {
            return false;
        }
        if (startIndex + length < SIZE && data[startIndex + length] == color) {
            return false;
        }

        return true;
    }

    private void placeYellow(int[] data) {
        int yellowStart = random.nextInt(SIZE - 2);
        data[yellowStart] = YELLOW;
        data[yellowStart + 1] = YELLOW;


        if (yellowStart > 0) {
            data[yellowStart - 1] = GRAY;
        }
        if (yellowStart + 2 < SIZE) {
            data[yellowStart + 2] = GRAY;
        }
    }

    public void updateTileServer(EntityPlayer var1, double var2) {
        if (start && var2 == 0) {
            this.componentProgress.addProgress(0, (short) ((short) 60*(1+data1.getOrDefault(var1.getUniqueID(),0.0)/66D)));
            if (componentProgress.getProgress(0) >= 300) {
                componentProgress.setProgress((short) 300);
                if (!this.getWorld().isRemote)
                PrimitiveHandler.addExperience(EnumPrimitive.PCB,0.75,var1.getUniqueID());
            }
            GREEN_PERCENT = (int) (80 * (1 - 0.75 * componentProgress.getBar()));
            RED_PERCENT = (int) (35 * (1 + 0.5 * componentProgress.getBar()));
            this.data = generateColorStrip();
        } else if (start && var2 == 1) {
            this.componentProgress.addProgress(0, (short) -20);
            if (componentProgress.getProgress(0) < 0) {
                componentProgress.setProgress((short) 0);
            }
            GREEN_PERCENT = (int) (80 * (1 - 0.75 * componentProgress.getBar()));
            RED_PERCENT = (int) (35 * (1 + 0.5 * componentProgress.getBar()));
            this.data = generateColorStrip();
        } else if (start && var2 == 2) {
            this.componentProgress.setProgress(0, (short) 300);
            if (!this.getWorld().isRemote)
            PrimitiveHandler.addExperience(EnumPrimitive.PCB,0.5,var1.getUniqueID());
        }
    }

    ;

    public void onLoaded() {
        super.onLoaded();
        data1 = PrimitiveHandler.getPlayersData(EnumPrimitive.PCB);
        inputSlotA.load();
        this.getOutput();
    }

    public void onUnloaded() {
        super.onUnloaded();
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            guiChargeLevel = (double) DecoderHandler.decode(customPacketBuffer);
            start = (boolean) DecoderHandler.decode(customPacketBuffer);
            data = (int[]) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, guiChargeLevel);
            EncoderHandler.encode(packet, start);
            EncoderHandler.encode(packet, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    @Override
    public void addInformation(final ItemStack stack, final List<String> tooltip) {

        super.addInformation(stack, tooltip);
    }


    public MachineRecipe getOutput() {
        this.output = this.inputSlotA.process();


        return this.output;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (!start) {
            if (output != null) {
                if (this.inputSlotA.continue_process(this.output) && this.outputSlot.canAdd(this.output
                        .getRecipe()
                        .getOutput().items)) {
                    this.start = true;
                    this.data = generateColorStrip();
                } else {
                    this.start = false;
                }
            }
        } else {
            if (output == null) {
                start = false;
            }
        }
        if (start && componentProgress.getBar() >= 1) {
            this.inputSlotA.consume();
            this.componentProgress.setProgress(0, (short) 0);
            this.outputSlot.add(this.output.getRecipe().getOutput().items);
            this.getOutput();
        }
    }

    public ContainerPrimalProgrammingTable getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerPrimalProgrammingTable(
                entityPlayer, this);
    }


    public IMultiTileBlock getTeBlock() {
        return BlockPrimalProgrammingTable.primal_programming_table;
    }

    public BlockTileEntity getBlock() {
        return IUItem.programming_table;
    }

    public void init() {

    }


    @Override
    public void onUpdate() {

    }

    @Override
    public MachineRecipe getRecipeOutput() {
        return this.output;
    }

    @Override
    public void setRecipeOutput(final MachineRecipe output) {
        this.output = output;
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiPrimalProgrammingTable(new ContainerPrimalProgrammingTable(entityPlayer, this));
    }


}
