package com.denfop.tiles.mechanism.triple.heat;

import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.audio.EnumTypeAudio;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.IType;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.IHasRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.audio.EnumSound;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
import com.denfop.componets.AirPollutionComponent;
import com.denfop.componets.EnumTypeStyle;
import com.denfop.componets.HeatComponent;
import com.denfop.componets.SoilPollutionComponent;
import com.denfop.container.ContainerTripleElectricMachine;
import com.denfop.gui.GuiAdvAlloySmelter;
import com.denfop.invslot.Inventory;
import com.denfop.network.DecoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.network.packet.PacketUpdateFieldTile;
import com.denfop.recipe.IInputHandler;
import com.denfop.tiles.base.EnumTripleElectricMachine;
import com.denfop.tiles.base.TileTripleElectricMachine;
import com.denfop.utils.ModUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileAdvAlloySmelter extends TileTripleElectricMachine implements IHasRecipe, IType {

    public final HeatComponent heat;
    public final Inventory input_slot;
    private final SoilPollutionComponent pollutionSoil;
    private final AirPollutionComponent pollutionAir;

    public TileAdvAlloySmelter() {
        super(1, 300, 1, Localization.translate("iu.AdvAlloymachine.name"), EnumTripleElectricMachine.ADV_ALLOY_SMELTER);
        this.heat = this.addComponent(HeatComponent
                .asBasicSink(this, 5000));
        Recipes.recipes.addInitRecipes(this);
        this.input_slot = new Inventory(this, Inventory.TypeItemSlot.INPUT, 1) {
            @Override
            public void put(final int index, final ItemStack content) {
                super.put(index, content);
                if (this.get().isEmpty()) {
                    ((TileAdvAlloySmelter) this.base).inputSlotA.changeAccepts(ItemStack.EMPTY);
                } else {
                    ((TileAdvAlloySmelter) this.base).inputSlotA.changeAccepts(this.get());
                }
            }

            @Override
            public boolean isItemValidForSlot(final int index, final ItemStack stack) {
                return stack.getItem() == IUItem.recipe_schedule;
            }

            @Override
            public EnumTypeSlot getTypeSlot() {
                return EnumTypeSlot.RECIPE_SCHEDULE;
            }
        };
        this.pollutionSoil = this.addComponent(new SoilPollutionComponent(this, 0.075));
        this.pollutionAir = this.addComponent(new AirPollutionComponent(this, 0.15));
    }

    public static void addAlloysmelter(Object container, Object fill, Object fill1, ItemStack output, int temperature) {
        final IInputHandler input = com.denfop.api.Recipes.inputFactory;
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("temperature", (short) temperature);
        Recipes.recipes.addRecipe("advalloysmelter", new BaseMachineRecipe(
                new Input(input.getInput(container), input.getInput(fill), input.getInput(fill1)),
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
        return BlockBaseMachine1.adv_alloy_smelter;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine;
    }

    @Override

    public void init() {
        addAlloysmelter("ingotCopper", "ingotZinc", "ingotLead", new ItemStack(IUItem.alloysingot, 1, 3), 4500);
        addAlloysmelter("ingotAluminium", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5), 4000);
        addAlloysmelter("ingotAluminum", "ingotMagnesium", "ingotManganese", new ItemStack(IUItem.alloysingot, 1, 5), 4000);

        addAlloysmelter("ingotAluminium",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0), 3000
        );
        addAlloysmelter("ingotAluminum",
                "ingotCopper", "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 0), 3000
        );
        addAlloysmelter("ingotAluminium",
                "ingotVanadium", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6), 4500
        );
        addAlloysmelter("ingotAluminum",
                "ingotVanadium", "ingotCobalt",
                new ItemStack(IUItem.alloysingot, 1, 6), 4500
        );
        addAlloysmelter("ingotChromium",
                "ingotTungsten", "ingotNickel",
                new ItemStack(IUItem.alloysingot, 1, 7), 5000
        );

        addAlloysmelter(
                new ItemStack(IUItem.alloysingot, 4, 2),
                new ItemStack(IUItem.alloysingot, 2, 1),
                new ItemStack(IUItem.crafting_elements
                        , 1, 310),
                new ItemStack(IUItem.crafting_elements,
                        1, 355
                ),
                3000
        );
        addAlloysmelter(
                new ItemStack(IUItem.smalldust, 2, 9),
                new ItemStack(IUItem.smalldust, 2, 29),
                new ItemStack(IUItem.crafting_elements
                        , 1, 498),
                new ItemStack(IUItem.crafting_elements,
                        1, 499
                ),
                3000
        );
        addAlloysmelter("ingotAluminum", "ingotMagnesium", Items.FLINT, new ItemStack(IUItem.alloysingot, 1, 10), 3000);

        addAlloysmelter(
                "gemBeryllium",
                new ItemStack(IUItem.iuingot, 3, 21),
                "ingotTin",
                new ItemStack(IUItem.alloysingot, 1, 11),
                4000
        );
        addAlloysmelter("ingotCopper", "ingotNickel", "ingotZinc", new ItemStack(IUItem.alloysingot, 1, 12), 4500);


        addAlloysmelter("ingotHafnium",
                "gemBor", "ingotTantalum",
                new ItemStack(IUItem.alloysingot, 1, 17), 5000
        );
        addAlloysmelter("ingotHafnium",
                "ingotTantalum", "ingotTungsten",
                new ItemStack(IUItem.alloysingot, 1, 20), 5000
        );
        addAlloysmelter("ingotHafnium",
                "ingotTantalum", new ItemStack(IUItem.iudust, 4, 21),
                new ItemStack(IUItem.alloysingot, 1, 24), 5000
        );

        addAlloysmelter("ingotMolybdenum",
                "ingotSteel", "ingotThallium",
                new ItemStack(IUItem.alloysingot, 1, 25), 5000
        );

        addAlloysmelter("ingotNeodymium", "ingotYttrium", "ingotAluminum", new ItemStack(IUItem.alloysingot, 1,
                        30
                )
                , 5000);

        addAlloysmelter(new ItemStack(IUItem.iuingot, 2, 23), new ItemStack(IUItem.iuingot, 1, 10),
                new ItemStack(IUItem.iuingot
                        , 1, 29),
                new ItemStack(IUItem.crafting_elements,
                        1, 504
                ), 3000
        );
    }


    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin) {
        return new GuiAdvAlloySmelter(new ContainerTripleElectricMachine(entityPlayer, this, type));
    }


    @Override
    public EnumTypeStyle getStyle() {
        return EnumTypeStyle.ADVANCED;
    }

    @Override
    public void updateTileServer(final EntityPlayer entityPlayer, final double i) {
        sound = !sound;
        new PacketUpdateFieldTile(this, "sound", this.sound);

        if (!sound) {
            if (this.getTypeAudio() == EnumTypeAudio.ON) {
                setType(EnumTypeAudio.OFF);
                initiate(2);

            }
        }
    }

    public void updateField(String name, CustomPacketBuffer is) {

        if (name.equals("sound")) {
            try {
                this.sound = (boolean) DecoderHandler.decode(is);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.updateField(name, is);
    }

    @Override
    public SoundEvent getSound() {
        return EnumSound.alloysmelter.getSoundEvent();
    }

}
