package me.sound;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;

import java.awt.*;


public class Sound {
    public static Sound INSTANCE;
    public static final Minecraft mc = Minecraft.getMinecraft();

    private static boolean notificationsAllowed = false;

    public static void notificationsAllowed(boolean value) {
        notificationsAllowed = value;
    }

    public  Sound(){
        new SoundPlayer().playSound(SoundPlayer.SoundType.SPECIAL, LiquidBounce.moduleManager.getToggleVolume());
    }

    public void Volll(){

        new SoundPlayer().playSound(SoundPlayer.SoundType.VICTORY, LiquidBounce.moduleManager.getToggleVolume());
    }

}
