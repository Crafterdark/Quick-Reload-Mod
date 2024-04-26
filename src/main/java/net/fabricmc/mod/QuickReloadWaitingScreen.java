package net.fabricmc.mod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.world.SaveHandler;
import net.minecraft.world.level.storage.WorldSaveException;


public class QuickReloadWaitingScreen extends Screen
{

    SaveHandler currentSaveHandler;

    public QuickReloadWaitingScreen(SaveHandler saveHandler)
    {
        currentSaveHandler = saveHandler;
    }

    public void init() {
        this.buttons.clear();

        Thread currentWorldThread = new Thread(){
            public void run(){
                while (true)
                {
                    if (assertDoesNotThrow(currentSaveHandler))
                    {
                    client.setScreen(new TitleScreen());
                    return;
                    }
                    else 
                    {
                        System.out.println("Access denied");
                    }
                }
            }
        };

        currentWorldThread.run();
    }

    public boolean assertDoesNotThrow(SaveHandler saveHandler)
    {
        try{
            saveHandler.readSessionLock();
            return true;
        }
        catch(WorldSaveException worldSaveException){
            return false;
        }
    }

    public void render(int mouseX, int mouseY, float tickDelta) {
        this.renderDirtBackground(0);
        this.drawCenteredString(this.textRenderer, "Reloading world...", this.width / 2, this.height / 2 - 50, 16777215);
        super.render(mouseX, mouseY, tickDelta);
    }

    public boolean shouldPauseGame() {
        return false;
    }
}

