package net.fabricmc.mod.mixin;

import net.minecraft.client.gui.screen.*;
import net.minecraft.realms.RealmsBridge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.mod.QuickReloadWaitingScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.world.ClientWorld;

import java.util.Objects;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen
{
    @Inject(at = @At("TAIL"), method = "init()V")
	private void init(CallbackInfo info) 
    {
		this.buttons.add(new ButtonWidget(8, this.width / 2 - 100, this.height / 4 + 120 + 8, "Quick Reload"));
	}

    @Override
    public void buttonClicked(ButtonWidget button)
    {
        switch (button.id) {
            case 0: {
                this.client.setScreen(new SettingsScreen(this, this.client.options));
                break;
            }
            case 1: {
                boolean bl = this.client.isIntegratedServerRunning();
                boolean bl2 = this.client.isConnectedToRealms();
                button.active = false;
                this.client.world.disconnect();
                this.client.connect(null);
                if (bl) {
                    this.client.setScreen(new TitleScreen());
                    break;
                }
                if (bl2) {
                    RealmsBridge realmsBridge = new RealmsBridge();
                    realmsBridge.switchToRealms(new TitleScreen());
                    break;
                }
                this.client.setScreen(new MultiplayerScreen(new TitleScreen()));
                break;
            }
            case 4: {
                this.client.setScreen(null);
                this.client.closeScreen();
                break;
            }
            case 5: {
                this.client.setScreen(new AdvancementsScreen(this.client.player.networkHandler.method_14672()));
                break;
            }
            case 6: {
                this.client.setScreen(new StatsScreen(this, this.client.player.getStatHandler()));
                break;
            }
            case 7: {
                this.client.setScreen(new OpenToLanScreen(this));
            }
            case 8: {
                boolean IWasServerIntegratedRunning = this.client.isIntegratedServerRunning();
                button.active = false;

                String levelName = Objects.requireNonNull(this.client.getServer()).getLevelName();

                this.client.world.disconnect();
                this.client.connect((ClientWorld)null);

                QuickReloadWaitingScreen.SafeReloadAfterSavingChunks = false;

                if (IWasServerIntegratedRunning)
                {
                    this.client.setScreen(new QuickReloadWaitingScreen(levelName));
                }
                else
                {
                    this.client.setScreen(new MultiplayerScreen(new TitleScreen()));
                }
            }
        }
    }
}