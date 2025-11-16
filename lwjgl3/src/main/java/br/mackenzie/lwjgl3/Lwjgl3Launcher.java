package br.mackenzie;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        System.out.println("🎯 Iniciando Launcher...");

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("Ciclistinha");
        config.setWindowedMode(800, 480);
        config.setResizable(false);

        try {
            new Lwjgl3Application(new GameMain(), config);
            System.out.println("✅ Aplicação iniciada com sucesso!");
        } catch (Exception e) {
            System.err.println("💥 Falha ao iniciar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
