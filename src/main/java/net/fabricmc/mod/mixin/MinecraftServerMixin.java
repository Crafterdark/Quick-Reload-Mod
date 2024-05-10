package net.fabricmc.mod.mixin;

import net.fabricmc.mod.QuickReloadWaitingScreen;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
    @Inject(at = @At("TAIL"), method = "stopServer()V")
	private void init(CallbackInfo info)
    {
        QuickReloadWaitingScreen.SafeReloadAfterSavingChunks = true;
    }
}