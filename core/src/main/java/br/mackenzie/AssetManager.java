package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;

public class AssetManager {
    private HashMap<String, Texture> textures;
    private HashMap<String, TextureRegion> textureRegions;

    public AssetManager() {
        textures = new HashMap<>();
        textureRegions = new HashMap<>();
        loadAllAssets();
    }

    private void loadAllAssets() {
        System.out.println("📦 Carregando assets...");

        // PERSONAGENS PARA O JOGO (versões _play)
        loadTexture("personagem_guerreiro", "ciclista_amarelo_play.png");
        loadTexture("personagem_mago", "ciclista_azul_play.png");
        loadTexture("personagem_arqueiro", "ciclista_roxo_play.png");
        loadTexture("personagem_padrao", "ciclista_vermelho_play.png");

        // PERSONAGENS PARA O MENU (versões _menu)
        loadTexture("personagem_guerreiro_menu", "ciclista_amarelo_menu.png");
        loadTexture("personagem_mago_menu", "ciclista_azul_menu.png");
        loadTexture("personagem_padrao_menu", "ciclista_vermelho_menu.png");

        // ✅ ÚNICA TEXTURA PARA VITÓRIA (para todos os personagens)
        loadTexture("personagem_vitoria", "vitoria.png");

        // Texturas existentes
        loadTexture("player", "img.png");
        loadTexture("background", "teste2.png");
        loadTexture("ground", "ground.png");
        loadTexture("obstacle", "teste.jpg");
        loadTexture("coin", "player.png");
        loadTexture("cyclist_sheet", "ciclista_vermelho_play.png");
        loadTexture("cyclist_waving", "ciclista_vermelho_menu.png");

        System.out.println("✅ Assets carregados: " + textures.size() + " texturas");

        // Debug: mostrar quais assets foram carregados
        for (String key : textures.keySet()) {
            System.out.println("   📁 " + key + " -> " + (textures.get(key) != null ? "✅" : "❌"));
        }
    }

    // MÉTODO PARA PEGAR TEXTURA DO PERSONAGEM NO JOGO (versões _play)
    public Texture getPersonagemTexture(String tipoPersonagem) {
        switch(tipoPersonagem.toLowerCase()) {
            case "guerreiro":
                return getTexture("personagem_guerreiro");
            case "mago":
                return getTexture("personagem_mago");
            case "arqueiro":
                return getTexture("personagem_arqueiro");
            default:
                return getTexture("personagem_padrao");
        }
    }

    // MÉTODO PARA PEGAR TEXTURA DO PERSONAGEM NO MENU (versões _menu)
    public Texture getPersonagemMenuTexture(String tipoPersonagem) {
        switch(tipoPersonagem.toLowerCase()) {
            case "guerreiro":
                return getTexture("personagem_guerreiro_menu");
            case "mago":
                return getTexture("personagem_mago_menu");
            case "arqueiro":
                // Arqueiro não tem versão menu, usamos a versão play como fallback
                Texture arqueiroMenu = getTexture("personagem_arqueiro_menu");
                if (arqueiroMenu == null) {
                    System.out.println("⚠️ Usando versão play para arqueiro no menu");
                    return getTexture("personagem_arqueiro");
                }
                return arqueiroMenu;
            default:
                return getTexture("personagem_padrao_menu");
        }
    }

    // ✅ MÉTODO SIMPLIFICADO: PEGA A MESMA TEXTURA DE VITÓRIA PARA TODOS OS PERSONAGENS
    public Texture getPersonagemVitoriaTexture(String tipoPersonagem) {
        Texture vitoriaTexture = getTexture("personagem_vitoria");

        if (vitoriaTexture == null) {
            System.out.println("❌ Textura de vitória não encontrada, usando fallback");
            // Fallback: usa a textura do menu se a de vitória não existir
            return getPersonagemMenuTexture(tipoPersonagem);
        }

        System.out.println("✅ Usando textura única de vitória para: " + tipoPersonagem);
        return vitoriaTexture;
    }

    // MÉTODO PARA VERIFICAR SE UM ASSET FOI CARREGADO
    public boolean isAssetLoaded(String name) {
        return textures.containsKey(name) && textures.get(name) != null;
    }

    // MÉTODO PARA LISTAR TODOS OS ASSETS CARREGADOS (útil para debug)
    public void listLoadedAssets() {
        System.out.println("📋 Assets carregados:");
        for (String key : textures.keySet()) {
            Texture tex = textures.get(key);
            System.out.println("   " + key + ": " + (tex != null ?
                tex.getWidth() + "x" + tex.getHeight() : "NULL"));
        }
    }

    private void loadTexture(String name, String filename) {
        try {
            if (Gdx.files.internal(filename).exists()) {
                Texture texture = new Texture(Gdx.files.internal(filename));
                texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                textures.put(name, texture);
                System.out.println("✅ " + name + " carregado: " + filename);
            } else {
                System.out.println("⚠️ " + filename + " não encontrado, criando placeholder para " + name);
                createPlaceholder(name);
            }
        } catch (Exception e) {
            System.out.println("❌ Erro ao carregar " + filename + ": " + e.getMessage());
            createPlaceholder(name);
        }
    }

    private void createPlaceholder(String name) {
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(64, 64,
            com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);

        if (name.startsWith("personagem_")) {
            if (name.contains("guerreiro")) {
                pixmap.setColor(1.0f, 0.8f, 0.0f, 1); // AMARELO
            } else if (name.contains("mago")) {
                pixmap.setColor(0.0f, 0.5f, 1.0f, 1); // AZUL
            } else if (name.contains("arqueiro")) {
                pixmap.setColor(0.6f, 0.0f, 0.8f, 1); // ROXO
            } else if (name.contains("vitoria")) {
                pixmap.setColor(1.0f, 0.8f, 0.0f, 1); // DOURADO para vitória
                System.out.println("🎨 Criando placeholder DOURADO para Vitória");
            } else {
                pixmap.setColor(1.0f, 0.0f, 0.0f, 1); // VERMELHO padrão
            }
            pixmap.fill();

            // Adiciona detalhes para diferenciar
            pixmap.setColor(1, 1, 1, 1);
            pixmap.drawRectangle(0, 0, 64, 64);

        } else {
            // Placeholders para outras texturas (mantenha o código existente)
            // ... seu código existente para outros placeholders ...
        }

        Texture texture = new Texture(pixmap);
        textures.put(name, texture);
        pixmap.dispose();
    }

    // ... resto dos métodos permanecem iguais ...

    public Texture getTexture(String name) {
        Texture texture = textures.get(name);
        if (texture == null) {
            System.out.println("⚠️ Texture não encontrada: " + name);
        }
        return texture;
    }

    public TextureRegion getTextureRegion(String name) {
        if (!textureRegions.containsKey(name)) {
            Texture texture = getTexture(name);
            if (texture != null) {
                textureRegions.put(name, new TextureRegion(texture));
            }
        }
        return textureRegions.get(name);
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        textures.clear();
        textureRegions.clear();
        System.out.println("🗑️ Assets liberados");
    }
}
