package net.fabricmc.mod.mixin;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.mod.QuickReloadWaitingScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.SaveHandler;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen
{
    @Inject(at = @At("TAIL"), method = "init()V")
	private void init(CallbackInfo info) 
    {
		this.buttons.add(new ButtonWidget(8, this.width / 2 - 100, this.height / 4 + 120 + 16, "Quick Reload"));
	}

    @Dynamic
    SaveHandler saveHandler;

    @Inject(at = @At("TAIL"), method = "buttonClicked()V")
    private void initTwo(ButtonWidget button, CallbackInfo info) {
    {
        if (button.id == 8)
        {
            boolean IWasServerIntegratedRunning = this.client.isIntegratedServerRunning();
            button.active = false;

            saveHandler = this.client.getServer().getWorld().getSaveHandler();

            this.client.world.disconnect();
            this.client.connect((ClientWorld)null);

            if (IWasServerIntegratedRunning)
            {
                this.client.setScreen(new QuickReloadWaitingScreen(saveHandler));
            } 
            else
            {
                this.client.setScreen(new MultiplayerScreen(new TitleScreen()));
            }
        }
    }
}
}