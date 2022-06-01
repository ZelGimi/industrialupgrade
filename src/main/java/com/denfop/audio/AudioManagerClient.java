package com.denfop.audio;

import ic2.core.IC2;
import ic2.core.IHitSoundOverride;
import ic2.core.init.MainConfig;
import ic2.core.util.ConfigUtil;
import ic2.core.util.LogCategory;
import ic2.core.util.ReflectionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public final class AudioManagerClient extends AudioManager {


    private final Map<AudioManagerClient.WeakObject, List<AudioSourceClient>> objectToAudioSourceMap = new HashMap<>();
    private final Queue<AudioSource> validAudioSources = new PriorityQueue<>();
    private final Map<String, FutureSound> singleSoundQueue = new HashMap<>();
    public float fadingDistance = 16.0F;
    float masterVolume = 0.5F;
    private boolean enabled = true;
    private boolean wasPaused = false;
    private int maxSourceCount = 32;
    private SoundManager soundManager;
    private Field soundManagerLoaded;
    private volatile Thread initThread;
    private SoundSystem soundSystem = null;
    private int nextId = 0;

    public AudioManagerClient() {
    }

    private static SoundManager getSoundManager() {
        SoundHandler handler = Minecraft.getMinecraft().getSoundHandler();
        return ReflectionUtil.getValue(handler, SoundManager.class);
    }

    private static SoundSystem getSoundSystem(SoundManager soundManager) {
        try {
            return ReflectionUtil.getValueRecursive(soundManager, SoundSystem.class, false);
        } catch (NoSuchFieldException var2) {
            return null;
        }
    }

    static URL getSourceURL(String soundFile) {
        int colonIndex = soundFile.indexOf(58);
        if (colonIndex > -1) {
            ClassLoader var10000 = AudioSource.class.getClassLoader();
            StringBuilder var10001 = (new StringBuilder()).append("assets/").append(soundFile, 0, colonIndex).append(
                    "/sounds/");
            ++colonIndex;
            return var10000.getResource(var10001.append(soundFile.substring(colonIndex)).toString());
        } else {
            return AudioSource.class.getClassLoader().getResource("ic2/sounds/" + soundFile);
        }
    }

    private static void removeSources(List<AudioSourceClient> sources) {

        for (final AudioSourceClient audioSource : sources) {
            audioSource.remove();
        }

    }

    private static RayTraceResult getMovingObjectPositionFromPlayer(World worldIn, EntityPlayer playerIn) {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double) playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        Vec3d vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
        return worldIn.rayTraceBlocks(vec3, vec31, false, true, false);
    }

    private static String getSourceName(int id) {
        return "asm_snd" + id;
    }

    public void initialize() {
        this.enabled = ConfigUtil.getBool(MainConfig.get(), "audio/enabled");
        this.masterVolume = ConfigUtil.getFloat(MainConfig.get(), "audio/volume");
        this.fadingDistance = ConfigUtil.getFloat(MainConfig.get(), "audio/fadeDistance");
        this.maxSourceCount = ConfigUtil.getInt(MainConfig.get(), "audio/maxSourceCount");
        if (this.maxSourceCount <= 6) {
            IC2.log.info(LogCategory.Audio, "The audio source limit is too low to enable IC2 sounds.");
            this.enabled = false;
        }

        if (!this.enabled) {
            IC2.log.debug(LogCategory.Audio, "Sounds disabled.");
        } else {
            IC2.log.debug(LogCategory.Audio, "Using %d audio sources.", this.maxSourceCount);
            SoundSystemConfig.setNumberStreamingChannels(4);
            SoundSystemConfig.setNumberNormalChannels(this.maxSourceCount - 4);
            this.soundManagerLoaded = ReflectionUtil.getField(SoundManager.class, Boolean.TYPE);
            if (this.soundManagerLoaded == null) {
                IC2.log.warn(LogCategory.Audio, "Can't find SoundManager.loaded, IC2 audio disabled.");
                this.enabled = false;
            } else {
                MinecraftForge.EVENT_BUS.register(this);
            }
        }
    }

    @SubscribeEvent
    public void onSoundSetup(SoundLoadEvent event) {
        if (this.enabled) {

            for (final List<AudioSourceClient> audioSourceClients : this.objectToAudioSourceMap.values()) {

                for (final AudioSourceClient source : audioSourceClients) {
                    if (source.isValid()) {
                        source.setInvalid();
                    }
                }
            }

            this.objectToAudioSourceMap.clear();
            Thread thread = this.initThread;
            if (thread != null) {
                thread.interrupt();

                try {
                    thread.join();
                } catch (InterruptedException ignored) {
                }
            }

            IC2.log.debug(LogCategory.Audio, "IC2 audio starting.");
            this.soundSystem = null;
            this.soundManager = getSoundManager();
            this.initThread = new Thread(() -> {
                while (true) {
                    try {
                        if (!Thread.currentThread().isInterrupted()) {
                            boolean loaded;
                            try {
                                loaded = AudioManagerClient.this.soundManagerLoaded.getBoolean(AudioManagerClient.this.soundManager);
                            } catch (Exception var3) {
                                throw new RuntimeException(var3);
                            }

                            if (!loaded) {
                                Thread.sleep(100L);
                                continue;
                            }

                            AudioManagerClient.this.soundSystem = AudioManagerClient.getSoundSystem(AudioManagerClient.this.soundManager);
                            if (AudioManagerClient.this.soundSystem == null) {
                                IC2.log.warn(LogCategory.Audio, "IC2 audio unavailable.");
                                AudioManagerClient.this.enabled = false;
                            } else {
                                IC2.log.debug(LogCategory.Audio, "IC2 audio ready.");
                            }
                        }
                    } catch (InterruptedException ignored) {
                    }

                    AudioManagerClient.this.initThread = null;
                    return;
                }
            }, "IC2 audio init thread");
            this.initThread.setDaemon(true);
            this.initThread.start();
        }
    }

    public void onTick() {
        if (this.enabled && this.valid()) {
            assert IC2.platform.isRendering();

            IC2.platform.profilerStartSection("UpdateSourceVolume");
            EntityPlayer player = IC2.platform.getPlayerInstance();
            if (player == null) {

                for (final List<AudioSourceClient> sources : this.objectToAudioSourceMap.values()) {
                    removeSources(sources);
                }

                this.objectToAudioSourceMap.clear();
                this.singleSoundQueue.clear();
            } else {
                boolean isPaused = Minecraft.getMinecraft().isGamePaused();
                Entry entry;
                Iterator it;
                if (!isPaused && !this.singleSoundQueue.isEmpty()) {
                    IC2.platform.profilerStartSection("SoundQueuing");
                    it = this.singleSoundQueue.entrySet().iterator();

                    while (it.hasNext()) {
                        entry = (Entry) it.next();
                        if (!this.soundSystem.playing((String) entry.getKey())) {
                            ((FutureSound) entry.getValue()).onFinish();
                            it.remove();
                        } else if (((FutureSound) entry.getValue()).isCancelled()) {
                            this.removeSource((String) entry.getKey());
                            it.remove();
                        }
                    }

                    synchronized (SoundSystemConfig.THREAD_SYNC) {
                        IC2.platform.profilerEndSection();
                    }
                }

                it = this.objectToAudioSourceMap.entrySet().iterator();

                while (true) {
                    while (it.hasNext()) {
                        entry = (Entry) it.next();
                        if (((AudioManagerClient.WeakObject) entry.getKey()).get() == null) {
                            it.remove();
                            removeSources(entry.getValue());
                        } else {

                            for (final Object o : (List) entry.getValue()) {
                                AudioSource audioSource = (AudioSource) o;
                                if (!this.wasPaused) {
                                    audioSource.updateVolume(player);
                                }

                                if (audioSource.getRealVolume() > 0.0F) {
                                    this.validAudioSources.add(audioSource);
                                }
                            }
                        }
                    }

                    IC2.platform.profilerEndStartSection("Culling");
                    if (!isPaused) {
                        AudioSource source;
                        if (this.wasPaused) {
                            it = this.validAudioSources.iterator();

                            while (it.hasNext()) {
                                source = (AudioSource) it.next();
                                source.play();
                            }

                            this.wasPaused = false;
                        }

                        for (int i = 0; !this.validAudioSources.isEmpty(); ++i) {
                            source = this.validAudioSources.poll();
                            if (i < this.maxSourceCount) {
                                source.activate();
                            } else {
                                source.cull();
                            }
                        }
                    } else if (!this.wasPaused) {
                        this.wasPaused = true;

                        while (!this.validAudioSources.isEmpty()) {
                            this.validAudioSources.poll().pause();
                        }
                    } else {

                        this.validAudioSources.clear();
                    }
                    break;
                }
            }

            IC2.platform.profilerEndSection();
        }
    }

    public AudioSource createSource(Object obj, String initialSoundFile) {
        return this.createSource(obj, PositionSpec.Center, initialSoundFile, false, false, this.getDefaultVolume());
    }

    public AudioSource createSource(Object obj, String initialSoundFile, PositionSpec spec) {
        return this.createSource(obj, PositionSpec.Hand, initialSoundFile, false, false, this.getDefaultVolume());
    }

    public AudioSource createSource(
            Object obj,
            PositionSpec positionSpec,
            String initialSoundFile,
            boolean loop,
            boolean priorized,
            float volume
    ) {
        if (!this.enabled) {
            return null;
        } else if (!this.valid()) {
            return null;
        } else {
            assert IC2.platform.isRendering();

            String sourceName = getSourceName(this.nextId);
            ++this.nextId;
            AudioSourceClient audioSource = new AudioSourceClient(
                    this.soundSystem,
                    sourceName,
                    obj,
                    initialSoundFile,
                    loop,
                    priorized,
                    volume
            );

            AudioManagerClient.WeakObject key = new AudioManagerClient.WeakObject(obj);
            List<AudioSourceClient> sources = this.objectToAudioSourceMap.computeIfAbsent(key, k -> new ArrayList<>());

            sources.add(audioSource);
            return audioSource;
        }
    }

    public void removeSources(Object obj) {
        if (this.valid()) {
            assert IC2.platform.isRendering();

            AudioManagerClient.WeakObject key;
            if (obj instanceof AudioManagerClient.WeakObject) {
                key = (AudioManagerClient.WeakObject) obj;
            } else {
                key = new AudioManagerClient.WeakObject(obj);
            }

            List<AudioSourceClient> sources = this.objectToAudioSourceMap.remove(key);
            if (sources != null) {
                removeSources(sources);
            }
        }
    }

    public void playOnce(Object obj, String soundFile) {
        this.playOnce(obj, PositionSpec.Center, soundFile, true, this.getDefaultVolume());
    }

    public String playOnce(Object obj, PositionSpec positionSpec, String soundFile, boolean priorized, float volume) {
        if (!this.enabled) {
            return null;
        } else if (!this.valid()) {
            return null;
        } else {
            assert IC2.platform.isRendering();

            AudioPosition position = AudioPosition.getFrom(obj);
            if (position == null) {
                return null;
            } else {
                URL url = AudioSource.class.getClassLoader().getResource("assets/industrialupgrade/sounds/" + soundFile);
                if (url == null) {
                    url = AudioSource.class.getClassLoader().getResource("ic2/sounds/" + soundFile);
                }

                if (url == null) {
                    IC2.log.warn(LogCategory.Audio, "Invalid sound file: %s.", soundFile);
                    return null;
                } else {
                    String sourceName = this.soundSystem.quickPlay(
                            priorized,
                            url,
                            soundFile,
                            false,
                            position.x,
                            position.y,
                            position.z,
                            2,
                            this.fadingDistance * Math.max(volume, 1.0F)
                    );
                    this.soundSystem.setVolume(sourceName, this.masterVolume * Math.min(volume, 1.0F));
                    return sourceName;
                }
            }
        }
    }

    public void removeSource(String source) {
        if (source != null) {
            this.soundSystem.stop(source);
            this.soundSystem.removeSource(source);
        }

    }

    public float getDefaultVolume() {
        return 1.0F;
    }

    public float getMasterVolume() {
        return this.masterVolume;
    }

    protected boolean valid() {
        try {
            return this.soundSystem != null && this.soundManager != null && this.soundManagerLoaded.getBoolean(this.soundManager);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    @SubscribeEvent
    public void onSoundPlayed(PlaySoundEvent event) {
        SoundCategory category = event.getSound().getCategory();
        if (category == SoundCategory.NEUTRAL && event.getName().endsWith(".hit") || category == SoundCategory.BLOCKS && event
                .getName()
                .endsWith(".break")) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            ItemStack stack = player.inventory.getCurrentItem();
            if (!stack.isEmpty() && stack.getItem() instanceof IHitSoundOverride) {
                World world = player.getEntityWorld();
                RayTraceResult mop = getMovingObjectPositionFromPlayer(world, player);
                BlockPos pos = new BlockPos(
                        event.getSound().getXPosF(),
                        event.getSound().getYPosF(),
                        event.getSound().getZPosF()
                );
                if (mop != null && mop.typeOfHit == Type.BLOCK && pos.equals(mop.getBlockPos())) {
                    String replace;
                    if (event.getSound().getCategory() == SoundCategory.NEUTRAL) {
                        replace = ((IHitSoundOverride) stack.getItem()).getHitSoundForBlock(player, world, pos, stack);
                    } else {
                        replace = ((IHitSoundOverride) stack.getItem()).getBreakSoundForBlock(player, world, pos, stack);
                    }

                    if (replace != null) {
                        event.setResultSound(null);
                        if (!replace.isEmpty()) {
                            IC2.platform.playSoundSp(replace, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }

    }

    public static class WeakObject extends WeakReference<Object> {

        public WeakObject(Object referent) {
            super(referent);
        }

        public boolean equals(Object object) {
            if (object instanceof AudioManagerClient.WeakObject) {
                return ((AudioManagerClient.WeakObject) object).get() == this.get();
            } else {
                return this.get() == object;
            }
        }

        public int hashCode() {
            Object object = this.get();
            return object == null ? 0 : object.hashCode();
        }

    }

}
