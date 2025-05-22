package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;

public class ItemBaseCircuit extends ItemSubTypes<ItemBaseCircuit.Types> implements IModelRegister {

    protected static final String NAME = "circuit";

    public ItemBaseCircuit() {
        super(Types.class);
        this.setCreativeTab(IUCore.ItemTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        if (meta >= 9 && meta <= 11 || meta == 21) {
            ModelLoader.setCustomMeshDefinition(this, stack -> {
                final NBTTagCompound nbt = ModUtils.nbt(stack);
                int level = nbt.getInteger("level");
                switch (stack.getItemDamage()) {
                    case 9:
                        level = level - 5;
                        break;
                    case 10:
                        level = level - 7;
                        break;
                    case 11:
                        level = level - 9;
                        break;
                    case 21:
                        level = level - 11;
                        break;
                }
                return new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(stack.getItemDamage()).getName() + (level == 1
                                ? "_1"
                                : ""),
                        null
                );

            });
            String[] mode = {"", "_1"};
            for (final String s : mode) {
                ModelBakery.registerItemVariants(
                        this,
                        new ModelResourceLocation(
                                Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName() + s,
                                null
                        )
                );

            }
        } else {
            ModelLoader.setCustomModelResourceLocation(
                    this,
                    meta,
                    new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
            );
        }
    }

    public enum Types implements ISubEnum {
        nanocircuit_part1(0),
        quantumcircuit_part1(1),
        spectralcircuit_part1(2),
        nanocircuit_part2(3),
        quantumcircuit_part2(4),
        spectralcircuit_part2(5),
        nanocircuit_part3(6),
        quantumcircuit_part3(7),
        spectralcircuit_part3(8),
        nanocircuit(9),
        quantumcircuit(10),
        spectralcircuit(11),
        advanced_part1(12),
        advanced_part2(13),
        advanced_part3(14),
        circuit_part1(15),
        circuit_part2(16),
        circuit_part3(17),
        photon_part1(18),
        photon_part2(19),
        photon_part3(20),
        photoncircuit(21),
        ;

        private final String name;
        private final int ID;

        Types(final int ID) {
            this.name = this.name().toLowerCase(Locale.US);
            this.ID = ID;
        }

        public static Types getFromID(final int ID) {
            return values()[ID % values().length];
        }

        public String getName() {
            return this.name;
        }

        public int getId() {
            return this.ID;
        }
    }

}
