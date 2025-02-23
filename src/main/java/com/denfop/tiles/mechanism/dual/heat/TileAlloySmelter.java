package com.denfop.tiles.mechanism.dual.heat;

import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiAlloySmelter;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.recipe.IInputItemStack;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TileAlloySmelter extends TileDoubleElectricMachine implements IHasRecipe {


    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAlloySmelter() {
        super(1, 300, 1, EnumDoubleElectricMachine.ALLOY_SMELTER);
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileAlloySmelter) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileAlloySmelter) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean accepts(final ItemStack stack, final int index) {
                return stack.getItem() == IUItem.recipe_schedule;
            }
            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
    }

    public static void addAlloysmelter(IInputItemStack container, IInputItemStack fill, ItemStack output, int temperature) {
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("alloysmelter", new BaseMachineRecipe(
                new Input(container, fill),
                new RecipeOutput(nbt, output)
        ));
    }

    @Override
    public void onLoaded() {
        super.onLoaded();
        if (!this.getWorld().isRemote) {
            if (this.input_slot.isEmpty()) {
                (this).inputSlotA.changeAccepts(ItemStack.EMPTY);
            } else {
                (this).inputSlotA.changeAccepts(this.input_slot.get());
            }
        }
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine.alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.machines;
    }

    public void init() {

        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        addAlloysmelter(
                input.getInput(new ItemStack(Items.IRON_INGOT), 1),
                input.getInput(new ItemStack(Items.COAL), 2),
                new ItemStack(IUItem.crafting_elements, 1, 502), 4000
        );
        addAlloysmelter(
                input.getInput("gemBor", 1),
                input.getInput(new ItemStack(Items.NETHER_STAR), 1),
                new ItemStack(IUItem.nether_star_ingot), 2000
        );
        addAlloysmelter(
                input.getInput("ingotTungsten", 2),
                input.getInput("ingotNickel", 1),
                new ItemStack(IUItem.wolframite), 3000
        );

        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.alloysingot, 1, 13)),
                input.getInput(new ItemStack(IUItem.iuingot, 2, 3)),
                new ItemStack(IUItem.crafting_elements, 2, 480), 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust, 1, 64)),
                input.getInput(new ItemStack(IUItem.iudust, 1, 28)),
                new ItemStack(IUItem.iudust, 1, 73), 1000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iuingot, 2, 28)),
                input.getInput(new ItemStack(IUItem.iuingot, 1, 32)),
                new ItemStack(IUItem.alloysingot, 1, 31), 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust, 2, 1)),
                input.getInput(new ItemStack(IUItem.iudust, 3, 31)),
                new ItemStack(IUItem.iudust, 1, 59), 4000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.GOLD_INGOT), 1),
                input.getInput("ingotSilver", 1),
                new ItemStack(
                        OreDictionary.getOres("ingotElectrum").get(0).getItem(),
                        2,
                        OreDictionary.getOres("ingotElectrum").get(0).getItemDamage()
                ), 3500
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 481)),
                input.getInput("dustCoal", 2),
                new ItemStack(IUItem.crafting_elements, 1, 482), 1000
        );
        addAlloysmelter(
                input.getInput("ingotNickel", 1),
                input.getInput(new ItemStack(Items.IRON_INGOT), 2),
                new ItemStack(
                        OreDictionary.getOres("ingotInvar").get(0).getItem(),
                        3,
                        OreDictionary.getOres("ingotInvar").get(0).getItemDamage()
                ), 5000
        );

        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust, 1, 37)),
                input.getInput("dustIron", 2),
                new ItemStack(IUItem.iudust, 1, 38), 2000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(IUItem.iudust, 1, 71)),
                input.getInput(new ItemStack(IUItem.iudust, 1, 60)),
                new ItemStack(IUItem.iudust, 1, 33), 2000
        );

        addAlloysmelter(
                input.getInput(new ItemStack(Items.COAL), 1),
                input.getInput(new ItemStack(Items.QUARTZ), 4),
                new ItemStack(IUItem.crafting_elements, 1, 319), 2000
        );
        addAlloysmelter(
                input.getInput("blockSilver", 1),
                input.getInput(new ItemStack(IUItem.crafting_elements, 1, 484), 1),
                new ItemStack(IUItem.crafting_elements, 1, 434), 2000
        );
        addAlloysmelter(
                input.getInput("ingotCopper", 1),
                input.getInput("ingotZinc", 1),
                new ItemStack(IUItem.alloysingot, 1, 2), 3000
        );
        addAlloysmelter(
                input.getInput("ingotNickel", 1),
                input.getInput("ingotChromium", 1),
                new ItemStack(IUItem.alloysingot, 1, 4), 4000
        );
        addAlloysmelter(
                input.getInput("ingotNickel", 1),
                input.getInput("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 15), 4000
        );
        addAlloysmelter(
                input.getInput("ingotTin", 1),
                input.getInput("ingotCopper", 3),
                ModUtils.setSize(IUItem.bronzeIngot, 4), 1000
        );
        addAlloysmelter(
                input.getInput("ingotAluminium", 1),
                input.getInput("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.getInput("ingotAluminum", 1),
                input.getInput("ingotMagnesium", 1),
                new ItemStack(IUItem.alloysingot, 1, 8), 2000
        );
        addAlloysmelter(
                input.getInput("ingotAluminium", 1),
                input.getInput("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.getInput("ingotAluminum", 1),
                input.getInput("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 1), 5000
        );
        addAlloysmelter(
                input.getInput("ingotAluminum", 1),
                input.getInput("ingotLithium", 1),
                new ItemStack(IUItem.alloysingot, 1, 22), 2000
        );
        addAlloysmelter(
                input.getInput("ingotChromium", 1),
                input.getInput("ingotCobalt", 1),
                new ItemStack(IUItem.alloysingot, 1, 23), 2000
        );
        addAlloysmelter(
                input.getInput("ingotNiobium", 2),
                input.getInput("ingotTitanium", 1),
                new ItemStack(IUItem.alloysingot, 1, 26), 3000
        );
        addAlloysmelter(
                input.getInput("ingotOsmium", 2),
                input.getInput("ingotIridium", 1),
                new ItemStack(IUItem.alloysingot, 1, 27), 3000
        );
        addAlloysmelter(
                input.getInput(new ItemStack(Items.IRON_INGOT), 1),
                input.getInput("ingotManganese", 1),
                new ItemStack(IUItem.alloysingot, 1, 9), 4500
        );


    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAlloySmelter(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }

    public String getStartSoundFile() {
        return "Machines/alloysmelter.ogg";
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


    @Override
    public SoundEvent getSound() {
        return EnumSound.alloysmelter.getSoundEvent();
    }

}
