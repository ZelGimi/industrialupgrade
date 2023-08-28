package com.denfop.items.modules;

import com.denfop.Config;
import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.api.IModelRegister;
import com.denfop.blocks.ISubEnum;
import com.denfop.items.resource.ItemSubTypes;
import com.denfop.register.Register;
import com.denfop.utils.CapturedMobUtils;
import com.denfop.utils.ModUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public class ItemEntityModule extends ItemSubTypes<ItemEntityModule.Types> implements IModelRegister {

    protected static final String NAME = "entitymodules";

    public ItemEntityModule() {
        super(null, Types.class);
        this.setCreativeTab(IUCore.ModuleTab);
        Register.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    @Override
    public boolean itemInteractionForEntity(
            @Nonnull final ItemStack stack,
            @Nonnull final EntityPlayer player,
            final EntityLivingBase entity,
            @Nonnull final EnumHand hand
    ) {
        if (entity.getEntityWorld().isRemote) {
            return false;
        }
        if (stack.getItemDamage() == 1) {
            if (entity instanceof EntityPlayer) {
                return false;
            }

            String entityId = EntityList.getEntityString(entity);
            if (Config.EntityList.contains(entityId)) {
                return false;
            }
            NBTTagCompound root = new NBTTagCompound();
            assert entityId != null;
            root.setString("id", entityId);
            entity.writeToNBT(root);
            root.setString("nameEntity", entity.getName());
            root.setInteger("id_mob", entity.getEntityId());


            CapturedMobUtils capturedMobUtils = CapturedMobUtils.create(entity);
            if (capturedMobUtils == null) {
                return false;
            }
            entity.setDead();
            stack.shrink(1);
            ItemStack stack1 = capturedMobUtils.toStack(this, 1, 1);


            double var8 = 0.7D;
            double var10 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
            double var12 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
            double var14 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
            EntityItem var16 = new EntityItem(
                    player.getEntityWorld(),
                    player.posX + var10,
                    player.posY + var12,
                    player.posZ + var14,
                    stack1
            );
            var16.setDefaultPickupDelay();
            player.getEntityWorld().spawnEntity(var16);

            return true;
        } else if (stack.getItemDamage() == 0) {

            if (entity instanceof EntityPlayer) {
                ItemStack stack1 = stack.copy();
                NBTTagCompound root = new NBTTagCompound();
                root.setString("name", entity.getDisplayName().getFormattedText());
                stack.shrink(1);

                double var8 = 0.7D;
                double var10 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                double var12 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                double var14 = (double) player.getEntityWorld().rand.nextFloat() * var8 + (1.0D - var8) * 0.5D;
                EntityItem var16 = new EntityItem(
                        player.getEntityWorld(),
                        player.posX + var10,
                        player.posY + var12,
                        player.posZ + var14,
                        stack1
                );
                var16.setDefaultPickupDelay();
                player.getEntityWorld().spawnEntity(var16);


                return true;
            } else {
                return false;
            }
        }
        return false;


    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItemDamage() == 1) {
            if (player.isSneaking()) {
                stack.setTagCompound(new NBTTagCompound());
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(
            @Nonnull final ItemStack itemStack,
            @Nullable final World worldIn,
            @Nonnull final List<String> info,
            @Nonnull final ITooltipFlag flagIn
    ) {
        super.addInformation(itemStack, worldIn, info, flagIn);
        if (itemStack.getItemDamage() != 1) {


            NBTTagCompound nbt = ModUtils.nbt(itemStack);
            if (!(nbt.getString("name").isEmpty())) {
                info.add(nbt.getString("name"));
            }
        }
    }

    public String getUnlocalizedName() {
        return "iu." + super.getUnlocalizedName().substring(3);
    }

    @SideOnly(Side.CLIENT)
    public void registerModel(Item item, int meta, String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(Constants.MOD_ID + ":" + NAME + "/" + Types.getFromID(meta).getName(), null)
        );
    }

    public enum Types implements ISubEnum {
        module_player(0),
        module_mob(1),
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
