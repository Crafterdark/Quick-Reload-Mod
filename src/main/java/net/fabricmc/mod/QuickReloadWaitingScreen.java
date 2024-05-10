package net.fabricmc.mod;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuickReloadWaitingScreen extends Screen
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static boolean SafeReloadAfterSavingChunks;
    private final String currentLevelName;
    public int safeTicksBeforeMenu;

    public QuickReloadWaitingScreen(String levelName)
    {
        currentLevelName = levelName;
    }

    public void init() {
        this.buttons.clear();
    }

    public void render(int mouseX, int mouseY, float tickDelta) {
        this.renderDirtBackground(0);
        this.drawCenteredString(this.textRenderer, "Reloading world...", this.width / 2, this.height / 2 - 50, 16777215);
        super.render(mouseX, mouseY, tickDelta);
    }

    @Override
    public void tick(){
        super.tick();
        if (SafeReloadAfterSavingChunks){
            SafeReloadAfterSavingChunks = false;
            if (this.client.getCurrentSave().levelExists(currentLevelName)) {
                LOGGER.info("Reloaded current level: " + currentLevelName);
                this.client.startIntegratedServer(currentLevelName, currentLevelName, null);
            }
            else
            {
                LOGGER.info("Couldn't reload current level: " + currentLevelName);
                LOGGER.info("Going to main menu...");
                this.client.setScreen(new TitleScreen());
            }
        }

        if (safeTicksBeforeMenu++ == 600)
        {
            LOGGER.info("TIMED OUT from current level: " + currentLevelName);
            LOGGER.info("Going to main menu...");
            this.client.setScreen(new TitleScreen());
        }
    }
}