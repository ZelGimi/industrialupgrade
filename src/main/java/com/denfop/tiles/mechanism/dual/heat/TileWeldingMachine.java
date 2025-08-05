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
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiWelding;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileWeldingMachine extends TileDoubleElectricMachine implements IHasRecipe {


    public final InvSlot input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileWeldingMachine() {
        super(1, 140, 1, EnumDoubleElectricMachine.WELDING);
        Recipes.recipes.addInitRecipes(this);
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.1));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.2));
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileWeldingMachine) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileWeldingMachine) this.base).inputSlotA.changeAccepts(this.get());
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
    }

    public static void addRecipe(ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.crafting_elements, 1, 122)), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(ItemStack container, ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    public static void addRecipe(String fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(new ItemStack(IUItem.crafting_elements, 1, 122)), input.getInput(fill)),
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
        return BlockBaseMachine3.welding;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.welding.getSoundEvent();
    }

    public void init() {
        addRecipe(
                new ItemStack(IUItem.crafting_elements, 1, 122),
                new ItemStack(IUItem.itemiu, 2, 2),
                new ItemStack(IUItem.radcable_item, 1),
                2000
        );
        addRecipe("plateLead", new ItemStack(IUItem.coolpipes, 4, 0), 1000);
        addRecipe("plateDenseIron", new ItemStack(IUItem.coolpipes, 4, 1), 2000);
        addRecipe("plateDenseSteel", new ItemStack(IUItem.coolpipes, 4, 2), 3000);
        addRecipe("doubleplateAluminiumSilicon", new ItemStack(IUItem.coolpipes, 4, 3), 4000);
        addRecipe("doubleplateWoods", new ItemStack(IUItem.coolpipes, 4, 4), 5000);

        addRecipe("plateAluminium", new ItemStack(IUItem.pipes, 4, 0), 1000);
        addRecipe("doubleplateAluminium", new ItemStack(IUItem.pipes, 4, 1), 2000);
        addRecipe("plateDuralumin", new ItemStack(IUItem.pipes, 4, 2), 3000);
        addRecipe("doubleplateAlcled", new ItemStack(IUItem.pipes, 4, 3), 4000);
        addRecipe("doubleplatePermalloy", new ItemStack(IUItem.pipes, 4, 4), 5000);

        for (int i = 0; i < 5; i++) {
            addRecipe(
                    new ItemStack(IUItem.coolpipes, 1, i),
                    new ItemStack(IUItem.pipes, 1, i),
                    new ItemStack(IUItem.heatcold_pipes, 1
                            , i),
                    1000 + 1000 * i
            );
        }
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnarium, 4, 2),
                new ItemStack(IUItem.crafting_elements, 1, 421), 1000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 0),
                new ItemStack(IUItem.crafting_elements, 1, 311), 1000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 1),
                new ItemStack(IUItem.crafting_elements, 1, 399), 1000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 2),
                new ItemStack(IUItem.crafting_elements, 1, 346), 1000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 3),
                new ItemStack(IUItem.crafting_elements, 1, 407), 2000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 4),
                new ItemStack(IUItem.crafting_elements, 1, 382), 2000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 5),
                new ItemStack(IUItem.crafting_elements, 1, 389), 2000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 6),
                new ItemStack(IUItem.crafting_elements, 1, 330), 2000
        );

        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 7),
                new ItemStack(IUItem.crafting_elements, 1, 430), 3000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 8),
                new ItemStack(IUItem.crafting_elements, 1, 359), 3000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 9),
                new ItemStack(IUItem.crafting_elements, 1, 307), 3000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 10),
                new ItemStack(IUItem.crafting_elements, 1, 302), 3000
        );

        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 11),
                new ItemStack(IUItem.crafting_elements, 1, 316), 4000
        );
        addRecipe(new ItemStack(IUItem.crafting_elements, 1, 437), new ItemStack(IUItem.sunnariumpanel, 4, 12),
                new ItemStack(IUItem.crafting_elements, 1, 350), 4000
        );

        addRecipe("stickTitanium", "plateIron",
                new ItemStack(IUItem.crafting_elements, 1, 338), 2000
        );

        addRecipe("stickGermanium", "plateSteel",
                new ItemStack(IUItem.crafting_elements, 1, 411), 3000
        );
        addRecipe("stickIridium", "plateIridium",
                new ItemStack(IUItem.crafting_elements, 1, 343), 4000
        );

        addRecipe(new ItemStack(IUItem.sunnarium, 1, 4), new ItemStack(IUItem.sunnarium, 1, 3),
                new ItemStack(IUItem.crafting_elements, 1, 416), 1000
        );
        addRecipe("nuggetPlatinum", "ingotPlatinum",
                new ItemStack(IUItem.crafting_elements, 1, 314), 1000
        );
        addRecipe("nuggetMikhail", "ingotMikhail",
                new ItemStack(IUItem.crafting_elements, 1, 401), 1000
        );
        addRecipe("nuggetChromium", "ingotChromium",
                new ItemStack(IUItem.crafting_elements, 1, 345), 1000
        );
        addRecipe("nuggetElectrum", "ingotElectrum",
                new ItemStack(IUItem.crafting_elements, 1, 406), 2000
        );
        addRecipe("nuggetMagnesium", "ingotMagnesium",
                new ItemStack(IUItem.crafting_elements, 1, 381), 2000
        );
        addRecipe("nuggetZinc", "ingotZinc",
                new ItemStack(IUItem.crafting_elements, 1, 391), 2000
        );
        addRecipe("nuggetManganese", "ingotManganese",
                new ItemStack(IUItem.crafting_elements, 1, 329), 2000
        );

        addRecipe("nuggetCobalt", "ingotCobalt",
                new ItemStack(IUItem.crafting_elements, 1, 429), 3000
        );
        addRecipe("nuggetNickel", "ingotNickel",
                new ItemStack(IUItem.crafting_elements, 1, 358), 3000
        );
        addRecipe("nuggetSilver", "ingotSilver",
                new ItemStack(IUItem.crafting_elements, 1, 306), 3000
        );
        addRecipe("nuggetVanadium", "ingotVanadium",
                new ItemStack(IUItem.crafting_elements, 1, 301), 3000
        );

        addRecipe("nuggetVitalium", "ingotVitalium",
                new ItemStack(IUItem.crafting_elements, 1, 315), 4000
        );
        addRecipe("nuggetCaravky", "ingotCaravky",
                new ItemStack(IUItem.crafting_elements, 1, 349), 4000
        );

        addRecipe("plateDenseSteel", "stickTungsten",
                new ItemStack(IUItem.crafting_elements, 1, 413), 2000
        );
        addRecipe("plateDenseSteel", IUItem.advancedAlloy,
                new ItemStack(IUItem.crafting_elements, 1, 370), 2000
        );
        addRecipe("plateDenseSteel", "gearTungsten",
                new ItemStack(IUItem.crafting_elements, 1, 412), 2000
        );
        addRecipe("plateDenseSteel", new ItemStack(IUItem.crafting_elements, 1, 137),
                new ItemStack(IUItem.crafting_elements, 1, 438), 2000
        );
        addRecipe("plateDenseSteel", "gemRuby",
                new ItemStack(IUItem.crafting_elements, 1, 369), 2000
        );
    }

    private void addRecipe(String container, String fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    private void addRecipe(String container, ItemStack fill, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;

        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("welding", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
                new RecipeOutput(nbt, output)
        ));
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiWelding(new ContainerDoubleElectricMachine(entityPlayer, this, this.type));
    }


    public String getStartSoundFile() {
        return "Machines/welding.ogg";
    }


    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
