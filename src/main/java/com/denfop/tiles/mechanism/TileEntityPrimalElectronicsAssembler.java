package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.primitive.EnumPrimitive;
import com.denfop.api.primitive.PrimitiveHandler;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.api.upgrades.UpgradableProperty;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockElectronicsAssembler;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerElectronicsAssembler;
import com.denfop.gui.GuiElectronicsAssemble;
import com.denfop.items.resource.ItemIngots;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.TileElectricMachine;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class TileEntityPrimalElectronicsAssembler extends TileElectricMachine implements IUpdateTick, IHasRecipe,
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
    public Map<UUID,Double> data1 = PrimitiveHandler.getPlayersData(EnumPrimitive.ELECTRONIC);

    public TileEntityPrimalElectronicsAssembler() {
        super(0, 0, 1);
        this.output = null;
        this.componentProgress = this.addComponent(new ComponentProgress(this, 1,
                (short) 300
        ));


        this.inputSlotA = new InvSlotRecipes(this, "electronics", this);
        Recipes.recipes.addInitRecipes(this);
    }

    public static int applyModifier(int base, int extra, double multiplier) {
        double ret = Math.round((base + extra) * multiplier);
        return (ret > 2.147483647E9D) ? Integer.MAX_VALUE : (int) ret;
    }

    private static void add(
            ItemStack second, ItemStack three,
            ItemStack four, ItemStack five,
            ItemStack output
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 4000);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        first1 = input.getInput("ingotAluminum");
        if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(second)[0])
                .isEmpty() && second.getItem() instanceof ItemIngots) {
            second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
        } else {
            second1 = input.getInput(second);
        }
        if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(three)[0])
                .isEmpty() && three.getItem() instanceof ItemIngots) {
            three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
        } else {
            three1 = input.getInput(three);
        }
        if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(four)[0])
                .isEmpty() && four.getItem() instanceof ItemIngots) {
            four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
        } else {
            four1 = input.getInput(four);
        }
        if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                .getOreName(OreDictionary.getOreIDs(five)[0])
                .isEmpty() && five.getItem() instanceof ItemIngots) {
            five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
        } else {
            five1 = input.getInput(five);
        }
        Recipes.recipes.addRecipe(
                "microchip",
                new BaseMachineRecipe(
                        new Input(first1, second1, three1, four1, five1),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            ItemStack five,
            ItemStack output,
            short temperatures,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", temperatures);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]), first.getCount());
            } else {
                first1 = input.getInput(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]), second.getCount());
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]), three.getCount());
            } else {
                three1 = input.getInput(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]), four.getCount());
            } else {
                four1 = input.getInput(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]), five.getCount());
            } else {
                five1 = input.getInput(five);
            }
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe("microchip", new BaseMachineRecipe(
                    new Input(
                            input.getInput(first),
                            input.getInput(second),
                            input.getInput(three),
                            input.getInput(four),
                            input.getInput(five)
                    ),
                    new RecipeOutput(nbt, output)
            ));
        }
    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            String five,
            ItemStack output,
            short temperatures,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", temperatures);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {

                first1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.getInput(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.getInput(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.getInput(four);
            }
            five1 = input.getInput(five);
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        }
    }

    public static void add(
            String first,
            ItemStack second,
            ItemStack three,
            ItemStack four,
            ItemStack five,
            ItemStack output,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 4500);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            first1 = input.getInput(first);
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.getInput(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.getInput(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.getInput(five);
            }
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe("microchip", new BaseMachineRecipe(
                    new Input(
                            input.getInput(first),
                            input.getInput(second),
                            input.getInput(three),
                            input.getInput(four),
                            input.getInput(five)
                    ),
                    new RecipeOutput(nbt, output)
            ));


        }
    }

    public static void add(
            ItemStack first,
            ItemStack second,
            ItemStack three,
            String four,
            ItemStack five,
            ItemStack output,
            boolean check
    ) {
        IInputItemStack first1;
        IInputItemStack second1;
        IInputItemStack three1;
        IInputItemStack four1;
        IInputItemStack five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 2000);
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.getInput(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.getInput(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.getInput(three);
            }
            four1 = input.getInput(four);
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.getInput(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.getInput(five);
            }
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(first1, second1, three1, four1, five1),
                            new RecipeOutput(nbt, output)
                    )
            );
        } else {
            Recipes.recipes.addRecipe(
                    "microchip",
                    new BaseMachineRecipe(
                            new Input(
                                    input.getInput(first),
                                    input.getInput(second),
                                    input.getInput(three),
                                    input.getInput(four),
                                    input.getInput(five)
                            ),
                            new RecipeOutput(nbt, output)
                    )
            );


        }
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

    private void fillGrayZones(int[] data) {
        for (int i = 0; i < SIZE; i++) {
            data[i] = GRAY;
        }
    }

    ;

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
            this.componentProgress.addProgress(0, (short) ((short) 30*(1+data1.getOrDefault(var1.getUniqueID(),0.0)/50D)));
            if (componentProgress.getProgress(0) >= 300) {
                componentProgress.setProgress((short) 300);
                if (!this.getWorld().isRemote)
                PrimitiveHandler.addExperience(EnumPrimitive.ELECTRONIC,0.75,var1.getUniqueID());
            }
            GREEN_PERCENT = (int) (80 * (1 - 0.75 * componentProgress.getBar()));
            RED_PERCENT = (int) (35 * (1 + 0.5 * componentProgress.getBar()));
            this.data = generateColorStrip();
        } else if (start && var2 == 1) {
            this.componentProgress.addProgress(0, (short) -10);
            if (componentProgress.getProgress(0) < 0) {
                componentProgress.setProgress((short) 0);
            }
            GREEN_PERCENT = (int) (80 * (1 - 0.75 * componentProgress.getBar()));
            RED_PERCENT = (int) (35 * (1 + 0.5 * componentProgress.getBar()));
            this.data = generateColorStrip();
        } else if (start && var2 == 2) {
            this.componentProgress.setProgress(0, (short) 300);
            if (!this.getWorld().isRemote)
            PrimitiveHandler.addExperience(EnumPrimitive.ELECTRONIC,0.75,var1.getUniqueID());
        }
    }

    public void onLoaded() {
        super.onLoaded();
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

    public ContainerElectronicsAssembler getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerElectronicsAssembler(
                entityPlayer, this);
    }

    public IMultiTileBlock getTeBlock() {
        return BlockElectronicsAssembler.electronics_assembler;
    }

    public BlockTileEntity getBlock() {
        return IUItem.electronics_assembler;
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

    public String getInventoryName() {

        return "Generation Microchip";
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiElectronicsAssemble(new ContainerElectronicsAssembler(entityPlayer, this));
    }

    public String getStartSoundFile() {
        return "Machines/genmirc.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }

    public Set<UpgradableProperty> getUpgradableProperties() {
        return EnumSet.of(UpgradableProperty.Processing, UpgradableProperty.Transformer,
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemExtract, UpgradableProperty.ItemInput
        );
    }

}
