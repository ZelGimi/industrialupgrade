package com.denfop.tiles.mechanism.dual;


import com.denfop.IUItem;
import com.denfop.api.Recipes;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.sytem.EnergyType;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.ComponentBaseEnergy;
import com.denfop.container.ContainerDoubleElectricMachine;
import com.denfop.gui.GuiEnriched;
import com.denfop.invslot.InvSlot;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumDoubleElectricMachine;
import com.denfop.tiles.base.TileDoubleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEnrichment extends TileDoubleElectricMachine implements IHasRecipe {

    public final ComponentBaseEnergy rad_energy;
    public final InvSlot input_slot;

    public TileEnrichment() {
        super(1, 300, 1, EnumDoubleElectricMachine.ENRICH);
        Recipes.recipes.addInitRecipes(this);
        this.rad_energy = this.addComponent(ComponentBaseEnergy.asBasicSink(EnergyType.RADIATION, this, 10000));
        this.input_slot = new InvSlot(this, InvSlot.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileEnrichment) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileEnrichment) this.base).inputSlotA.changeAccepts(this.get());
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

    public static void addenrichment(ItemStack container, ItemStack fill, ItemStack output, int rad_amount) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("rad_amount", rad_amount);
        Recipes.recipes.addRecipe(
                "enrichment",
                new BaseMachineRecipe(
                        new Input(input.getInput(container), input.getInput(fill)),
                        new RecipeOutput(nbt, output)
                )
        );
    }

    public static void addenrichment(ItemStack container, String fill, ItemStack output, int rad_amount) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        NBTTagCompound nbt = ModUtils.nbt();
        nbt.setInteger("rad_amount", rad_amount);
        Recipes.recipes.addRecipe("enrichment", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill)),
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
        return BlockBaseMachine1.enrichment;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.enrichment.getSoundEvent();
    }

    public void init() {
        addenrichment(
                new ItemStack(IUItem.toriy),
                new ItemStack(Items.GLOWSTONE_DUST),
                new ItemStack(IUItem.radiationresources, 1, 4), 25
        );
        addenrichment(
                new ItemStack(IUItem.preciousgem, 4, 1),
                "blockCobalt",
                new ItemStack(IUItem.crafting_elements, 1, 269), 200
        );
        addenrichment(
                new ItemStack(Blocks.GLOWSTONE, 1),
                "ingotUranium",
                new ItemStack(IUItem.itemiu, 1, 0), 10
        );
        addenrichment(new ItemStack(IUItem.itemiu, 1, 0), IUItem.reinforcedGlass, new ItemStack(IUItem.itemiu, 2, 1), 10);

        addenrichment(
                new ItemStack(IUItem.sunnarium, 1, 3),
                new ItemStack(IUItem.itemiu, 1, 0),
                new ItemStack(IUItem.sunnarium, 1, 0), 20
        );
        addenrichment(
                new ItemStack(IUItem.itemiu),
                IUItem.advancedAlloy,
                new ItemStack(IUItem.crafting_elements, 1, 453), 20
        );
        addenrichment(
                new ItemStack(Items.REDSTONE, 4),
                new ItemStack(IUItem.itemiu, 1, 0),
                new ItemStack(IUItem.crafting_elements, 1, 445), 40
        );

        addenrichment(
                new ItemStack(Items.STRING, 2),
                new ItemStack(IUItem.nuclear_res, 1, 5),
                new ItemStack(IUItem.crafting_elements, 1, 444), 25
        );

        addenrichment(
                new ItemStack(Items.DYE, 8, 4),
                IUItem.Plutonium,
                new ItemStack(IUItem.crafting_elements, 1, 446), 50
        );

        addenrichment(
                new ItemStack(IUItem.iudust, 2, 31),
                new ItemStack(IUItem.iudust, 8, 27),
                new ItemStack(IUItem.iudust, 1, 40), 50
        );
        addenrichment(
                new ItemStack(IUItem.nuclear_res, 8, 16),
                new ItemStack(IUItem.purifiedcrushed, 1, 24),
                new ItemStack(IUItem.iudust, 1, 78), 500
        );
    }

    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiEnriched(new ContainerDoubleElectricMachine(entityPlayer, this, type));
    }

    public String getStartSoundFile() {
        return "Machines/enrichment.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }

    public float getWrenchDropRate() {
        return 0.85F;
    }


}
