// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp;

import net.minecraft.client.settings.GameSettings;
import java.util.Set;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import java.util.Locale;
import ic2.core.util.ReflectionUtil;
import java.lang.reflect.Field;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import ic2.core.IC2;
import ic2.core.util.Keyboard;

public final class SSPKeys extends Keyboard
{
    private static final Keyboard.IKeyWatcher FLY_KEY;
    
    private SSPKeys() {
    }
    
    static void addFlyKey() {
        IC2.keyboard.addKeyWatcher(SSPKeys.FLY_KEY);
    }
    
    public static boolean isFlyKeyDown(final EntityPlayer player) {
        return IC2.keyboard.isKeyDown(player, SSPKeys.FLY_KEY);
    }
    
    static {
        FLY_KEY = (Keyboard.IKeyWatcher)new KeyWatcher(SSPKey.fly);
    }
    
    private enum SSPKey
    {
        fly(33, "Fly Key");
        
        private final Keyboard.Key key;
        @SideOnly(Side.CLIENT)
        private KeyBinding binding;
        
        private static Field getKeysField() {
            try {
                final Field field = ReflectionUtil.getField((Class)Keyboard.Key.class, new String[] { "keys" });
                ReflectionUtil.getField((Class)Field.class, new String[] { "modifiers" }).setInt(field, field.getModifiers() & 0xFFFFFFEF);
                return field;
            }
            catch (Exception e) {
                throw new RuntimeException("Error reflecting keys field!", e);
            }
        }
        
        private SSPKey(final int keyID, final String description) {
            this.key = this.addKey(this.name());
            if (IC2.platform.isRendering()) {
                ClientRegistry.registerKeyBinding(this.binding = new KeyBinding(description, keyID, "SuperSolarPanels".substring(0, 1).toUpperCase(Locale.ENGLISH) + "SuperSolarPanels".substring(1)));
            }
        }
        
        private Keyboard.Key addKey(final String name) {
            final Keyboard.Key key = (Keyboard.Key)EnumHelper.addEnum((Class)Keyboard.Key.class, name, new Class[0], new Object[0]);
            ReflectionUtil.setValue((Object)null, getKeysField(), (Object)ArrayUtils.add((Object[])Keyboard.Key.keys, (Object)key));
            return key;
        }
    }
    
    private static class KeyWatcher implements Keyboard.IKeyWatcher
    {
        private final SSPKey key;
        
        public KeyWatcher(final SSPKey key) {
            this.key = key;
        }
        
        public Keyboard.Key getRepresentation() {
            return this.key.key;
        }
        
        @SideOnly(Side.CLIENT)
        public void checkForKey(final Set<Keyboard.Key> pressedKeys) {
            if (GameSettings.isKeyDown(this.key.binding)) {
                pressedKeys.add(this.getRepresentation());
            }
        }
    }
}
