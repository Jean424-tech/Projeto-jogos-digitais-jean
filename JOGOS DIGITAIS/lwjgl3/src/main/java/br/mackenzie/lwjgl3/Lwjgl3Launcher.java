package br.mackenzie.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import br.mackenzie.GameMain;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Menu Teste");
        config.setWindowedMode(500, 380);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new GameMain(), config);
    }
}
