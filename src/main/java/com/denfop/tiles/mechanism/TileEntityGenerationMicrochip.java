package com.denfop.tiles.mechanism;

import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.api.ITemperature;
import com.denfop.api.Recipes;
import com.denfop.api.heat.IHeatEmitter;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IUpdateTick;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InvSlotRecipes;
import com.denfop.api.recipe.MachineRecipe;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.container.ContainerBaseGenerationChipMachine;
import com.denfop.gui.GuiGenerationMicrochip;
import com.denfop.items.resource.ItemIngots;
import com.denfop.tiles.base.TileEntityBaseGenerationMicrochip;
import com.denfop.tiles.base.TileEntityElectricMachine;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.api.upgrade.UpgradableProperty;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.EnumSet;
import java.util.Set;

public class TileEntityGenerationMicrochip extends TileEntityBaseGenerationMicrochip implements IUpdateTick {

    private boolean auto;

    public TileEntityGenerationMicrochip() {
        super(1, 300, 1);
        this.inputSlotA = new InvSlotRecipes(this, "microchip", this);
        this.auto = false;
    }

    public static void init() {
        add(new ItemStack(Items.FLINT), new ItemStack(Items.DYE, 1, 4), new ItemStack(Items.IRON_INGOT),
                new ItemStack(IUItem.iuingot, 1
                        , 11), new ItemStack(IUItem.iuingot, 1, 15), new ItemStack(IUItem.basecircuit), (short) 2000, false
        );
        add(
                new ItemStack(IUItem.iuingot, 1, 1),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.GOLD_INGOT),
                new ItemStack(IUItem.iuingot, 1, 7),
                new ItemStack(IUItem.iuingot, 1, 14),
                new ItemStack(IUItem.basecircuit, 1, 1),
                (short) 3000, true
        );

        add(
                new ItemStack(IUItem.iuingot, 1, 18),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.DIAMOND),
                new ItemStack(IUItem.iuingot, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 2),
                (short) 4000, true
        );
        add(
                new ItemStack(IUItem.iuingot, 1, 18),
                new ItemStack(Items.REDSTONE, 1),
                new ItemStack(Items.EMERALD),
                new ItemStack(IUItem.iuingot, 1, 0),
                new ItemStack(IUItem.iuingot, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 2),
                (short) 4000, true
        );


        add(
                new ItemStack(IUItem.iuingot, 1, 2),
                new ItemStack(IUItem.iuingot, 1, 3),
                new ItemStack(IUItem.basecircuit, 1, 0),
                new ItemStack(IUItem.basecircuit, 1, 3),
                new ItemStack(IUItem.basecircuit, 1, 6),
                new ItemStack(IUItem.basecircuit, 1, 9),
                (short) 2000,
                true
        );

        add(
                new ItemStack(IUItem.iuingot, 1, 8),
                new ItemStack(IUItem.iuingot, 1, 6),
                new ItemStack(IUItem.basecircuit, 1, 1),
                new ItemStack(IUItem.basecircuit, 1, 4),
                new ItemStack(IUItem.basecircuit, 1, 7),
                new ItemStack(IUItem.basecircuit, 1, 10),
                (short) 3000,
                true
        );
        add(
                new ItemStack(IUItem.iuingot, 1, 2),
                new ItemStack(IUItem.iuingot, 1, 10),
                new ItemStack(IUItem.basecircuit, 1, 2),
                new ItemStack(IUItem.basecircuit, 1, 5),
                new ItemStack(IUItem.basecircuit, 1, 8),
                new ItemStack(IUItem.basecircuit, 1, 11),
                (short) 4000, true
        );
        add(new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(Items.DYE, 1, 4),
                new ItemStack(IUItem.basecircuit, 1, 12),
                new ItemStack(IUItem.basecircuit, 1, 13),
                new ItemStack(IUItem.basecircuit, 1, 14),
                Ic2Items.advancedCircuit,
                (short) 1000, false
        );
        add(
                new ItemStack(Items.FLINT),
                new ItemStack(Items.DYE, 1, 4),
                new ItemStack(IUItem.iuingot, 1, 9),
                "ingotSteel", new ItemStack(Items.GOLD_INGOT),

                new ItemStack(IUItem.basecircuit, 1, 12),
                true
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
        IRecipeInput first1;
        IRecipeInput second1;
        IRecipeInput three1;
        IRecipeInput four1;
        IRecipeInput five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", temperatures);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.forStack(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.forStack(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.forStack(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.forStack(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.forStack(five);
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
                            input.forStack(first),
                            input.forStack(second),
                            input.forStack(three),
                            input.forStack(four),
                            input.forStack(five)
                    ),
                    new RecipeOutput(nbt, output)
            ));
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
        IRecipeInput first1;
        IRecipeInput second1;
        IRecipeInput three1;
        IRecipeInput four1;
        IRecipeInput five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 4500);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        if (check) {
            first1 = input.forOreDict(first);
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.forStack(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.forStack(three);
            }
            if (OreDictionary.getOreIDs(four).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(four)[0])
                    .isEmpty() && four.getItem() instanceof ItemIngots) {
                four1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(four)[0]));
            } else {
                four1 = input.forStack(four);
            }
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.forStack(five);
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
                            input.forOreDict(first),
                            input.forStack(second),
                            input.forStack(three),
                            input.forStack(four),
                            input.forStack(five)
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
        IRecipeInput first1;
        IRecipeInput second1;
        IRecipeInput three1;
        IRecipeInput four1;
        IRecipeInput five1;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setShort("temperature", (short) 1000);
        final IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        if (check) {
            if (OreDictionary.getOreIDs(first).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(first)[0])
                    .isEmpty() && first.getItem() instanceof ItemIngots) {
                first1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(first)[0]));
            } else {
                first1 = input.forStack(first);
            }
            if (OreDictionary.getOreIDs(second).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(second)[0])
                    .isEmpty() && second.getItem() instanceof ItemIngots) {
                second1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(second)[0]));
            } else {
                second1 = input.forStack(second);
            }
            if (OreDictionary.getOreIDs(three).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(three)[0])
                    .isEmpty() && three.getItem() instanceof ItemIngots) {
                three1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(three)[0]));
            } else {
                three1 = input.forStack(three);
            }
            four1 = input.forOreDict(four);
            if (OreDictionary.getOreIDs(five).length > 0 && !OreDictionary
                    .getOreName(OreDictionary.getOreIDs(five)[0])
                    .isEmpty() && five.getItem() instanceof ItemIngots) {
                five1 = input.forOreDict(OreDictionary.getOreName(OreDictionary.getOreIDs(five)[0]));
            } else {
                five1 = input.forStack(five);
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
                                    input.forStack(first),
                                    input.forStack(second),
                                    input.forStack(three),
                                    input.forOreDict(four),
                                    input.forStack(five)
                            ),
                            new RecipeOutput(nbt, output)
                    )
            );


        }
    }

    @Override
    protected boolean onActivated(
            final EntityPlayer player,
            final EnumHand hand,
            final EnumFacing side,
            final float hitX,
            final float hitY,
            final float hitZ
    ) {
        final ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem().equals(IUItem.autoheater) && !this.auto) {
            this.auto = true;
            stack.shrink(1);
        }
        return super.onActivated(player, hand, side, hitX, hitY, hitZ);
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
        return new GuiGenerationMicrochip(new ContainerBaseGenerationChipMachine(entityPlayer, this));
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
                UpgradableProperty.EnergyStorage, UpgradableProperty.ItemConsuming, UpgradableProperty.ItemProducing
        );
    }

    @Override
    public short getTemperature() {
        return this.temperature;
    }

    @Override
    public void setTemperature(short temperature) {
        this.temperature = temperature;
    }

    @Override
    public short getMaxTemperature() {
        return this.maxtemperature;
    }

    @Override
    public boolean isFluidTemperature() {
        return false;
    }

    @Override
    public FluidStack getFluid() {
        return null;
    }

    @Override
    public TileEntityElectricMachine getTile() {
        return this;
    }

    @Override
    public boolean receiver() {
        return true;
    }

    @Override
    public boolean acceptsHeatFrom(final IHeatEmitter var1, final EnumFacing var2) {
        return true;
    }

    @Override
    public double getDemandedHeat() {
        return Math.max(0.0D, this.maxtemperature);
    }

    public void setHeatStored(double amount) {
        if (this.temperature < amount) {
            this.temperature = (short) amount;
        }
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.temperature = nbttagcompound.getShort("temperature");
        this.auto = nbttagcompound.getBoolean("auto");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setShort("temperature", this.temperature);
        nbttagcompound.setBoolean("auto", this.auto);
        return nbttagcompound;
    }

    @Override
    public double injectHeat(final EnumFacing var1, final double var2, final double var4) {
        this.setHeatStored(var2);
        return 0.0D;
    }

    @Override
    public void updateEntityServer() {
        super.updateEntityServer();
        if (this.auto) {
            if (this.temperature + 2 <= this.maxtemperature) {
                this.temperature += 2;
            }
        }
    }

    @Override
    public World getWorldTile() {
        return this.getWorld();
    }

    @Override
    public ITemperature getITemperature() {
        return this;
    }

}
