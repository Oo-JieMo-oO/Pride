package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.features.module.*;
import net.ccbluex.liquidbounce.value.*;
import kotlin.jvm.internal.*;
import net.ccbluex.liquidbounce.utils.*;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.*;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.api.minecraft.network.*;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.*;
import net.ccbluex.liquidbounce.*;
import kotlin.*;

import net.ccbluex.liquidbounce.features.module.modules.combat.*;
import net.ccbluex.liquidbounce.features.module.modules.world.*;
import org.jetbrains.annotations.*;

@ModuleInfo(name = "Rotations", description = "Allows you to see server-sided head and body rotations.", category = ModuleCategory.RENDER)
@Metadata(mv = { 1, 1, 16 }, bv = { 1, 0, 3 }, k = 1, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0014\u0010\f\u001a\u00020\r2\n\u0010\u000e\u001a\u0006\u0012\u0002\b\u00030\u000fH\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007J\u0010\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0015H\u0007J\b\u0010\u0016\u001a\u00020\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\u0007R\u0016\u0010\b\u001a\u0004\u0018\u00010\t8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000b¨\u0006\u0017" }, d2 = { "Lnet/ccbluex/liquidbounce/features/module/modules/render/Rotations;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "bodyValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "playerYaw", "", "Ljava/lang/Float;", "tag", "", "getTag", "()Ljava/lang/String;", "getState", "", "module", "Ljava/lang/Class;", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onRender3D", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "shouldRotate", "LiquidBounce" })
public final class Rotations extends Module
{
    private final BoolValue bodyValue;
    private Float playerYaw;
    
    @EventTarget
    public final void onRender3D(@NotNull final Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, "event");
        if (RotationUtils.serverRotation != null && !(boolean)this.bodyValue.get()) {
            final IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
            if (thePlayer != null) {
                thePlayer.setRotationYawHead(RotationUtils.serverRotation.getYaw());
            }
        }
    }
    
    @EventTarget
    public final void onPacket(@NotNull final PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, "event");
        final IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
        if (!(boolean)this.bodyValue.get() || !this.shouldRotate() || thePlayer == null) {
            return;
        }
        final IPacket packet = event.getPacket();
        if (MinecraftInstance.classProvider.isCPacketPlayerPosLook((Object)packet) || MinecraftInstance.classProvider.isCPacketPlayerLook((Object)packet)) {
            final ICPacketPlayer packetPlayer = packet.asCPacketPlayer();
            this.playerYaw = packetPlayer.getYaw();
            thePlayer.setRenderYawOffset(packetPlayer.getYaw());
            thePlayer.setRotationYawHead(packetPlayer.getYaw());
        }
        else {
            if (this.playerYaw != null) {
                final IEntityPlayerSP entityPlayerSP = thePlayer;
                final Float playerYaw = this.playerYaw;
                if (playerYaw == null) {
                    Intrinsics.throwNpe();
                }
                entityPlayerSP.setRenderYawOffset((float)playerYaw);
            }
            thePlayer.setRotationYawHead(thePlayer.getRenderYawOffset());
        }
    }
    
    private final boolean getState(final Class<?> module) {
        final Module value = LiquidBounce.INSTANCE.getModuleManager().get((Class)module);
        if (value == null) {
            Intrinsics.throwNpe();
        }
        return value.getState();
    }
    
    private final boolean shouldRotate() {
        final Module module = LiquidBounce.INSTANCE.getModuleManager().getModule((Class)KillAura.class);
        if (module == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
        }
        final KillAura killAura = (KillAura)module;
        return this.getState(Scaffold.class) || this.getState(Tower.class) || (this.getState(KillAura.class) && killAura.getTarget() != null) || this.getState(BowAimbot.class) || this.getState(Fucker.class) || this.getState(CivBreak.class) || this.getState(Nuker.class) || this.getState(ChestAura.class);
    }
    

    
    public Rotations() {
        this.bodyValue = new BoolValue("Body", true);
    }
}
