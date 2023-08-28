package com.denfop.tiles.mechanism.generator.energy;

import com.denfop.IUItem;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.BlockTileEntity;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.container.ContainerSolar;
import com.denfop.gui.GuiSolar;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.packet.CustomPacketBuffer;
import com.denfop.utils.ModUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

public class TileEntitySolarGenerator extends TileEntityBaseGenerator {

    public boolean rain;
    public boolean noSunWorld;
    public boolean wetBiome;
    public boolean skyIsVisible;
    public boolean sunIsUp;
    private Biome biome;

    public TileEntitySolarGenerator() {
        super(1.0, 1, 32);
    }

    public static double getSkyLight(World world, BlockPos pos) {
        if (world.provider.isNether()) {
            return 0.0F;
        } else {
            float sunBrightness = ModUtils.limit(
                    (float) Math.cos(world.getCelestialAngleRadians(1.0F)) * 2.0F + 0.2F,
                    0.0F,
                    1.0F
            );
            if (!BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.SANDY)) {
                sunBrightness *= 1.0F - world.getRainStrength(1.0F) * 5.0F / 16.0F;
                sunBrightness *= 1.0F - world.getThunderStrength(1.0F) * 5.0F / 16.0F;
                sunBrightness = ModUtils.limit(sunBrightness, 0.0F, 1.0F);
            }

            return (float) world.getLightFor(EnumSkyBlock.SKY, pos) / 15.0F * sunBrightness;
        }
    }

    @Override
    public void readContainerPacket(final CustomPacketBuffer customPacketBuffer) {
        super.readContainerPacket(customPacketBuffer);
        try {
            sunIsUp = (boolean) DecoderHandler.decode(customPacketBuffer);
            skyIsVisible = (boolean) DecoderHandler.decode(customPacketBuffer);
            rain = (boolean) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public CustomPacketBuffer writeContainerPacket() {
        final CustomPacketBuffer packet = super.writeContainerPacket();
        try {
            EncoderHandler.encode(packet, sunIsUp);
            EncoderHandler.encode(packet, skyIsVisible);
            EncoderHandler.encode(packet, rain);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return packet;
    }

    public IMultiTileBlock getTeBlock() {
        return BlockBaseMachine3.solar_iu;
    }

    public BlockTileEntity getBlock() {
        return IUItem.basemachine2;
    }

    public void onLoaded() {
        super.onLoaded();
        this.biome = this.world.getBiome(this.pos);
        this.noSunWorld = this.world.provider.isNether();
        this.updateSunVisibility();
    }

    public boolean gainEnergy() {
        if (this.getWorld().provider.getWorldTime() % 40 == 0) {
            this.updateSunVisibility();
        }

        if (this.sunIsUp && skyIsVisible && !this.rain) {
            this.energy.addEnergy(1);
            return true;
        } else {
            return false;
        }
    }

    public boolean gainFuel() {
        return false;
    }

    public void updateSunVisibility() {
        this.wetBiome = this.biome.getRainfall() > 0.0F;
        this.rain = this.wetBiome && (this.world.isRaining() || this.world.isThundering());
        this.sunIsUp = this.world.isDaytime();
        this.skyIsVisible = this.world.canBlockSeeSky(this.pos.up()) &&
                (this.world.getBlockState(this.pos.up()).getMaterial().getMaterialMapColor() ==
                        MapColor.AIR) && !this.noSunWorld;
    }


    public boolean needsFuel() {
        return false;
    }

    @Override
    public ContainerSolar getGuiContainer(final EntityPlayer player) {
        return new ContainerSolar(this, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiSolar getGui(final EntityPlayer player, final boolean isAdmin) {
        return new GuiSolar(getGuiContainer(player));
    }

    protected boolean delayActiveUpdate() {
        return true;
    }

}
