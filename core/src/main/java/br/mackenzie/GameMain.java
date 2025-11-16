package br.mackenzie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class GameMain extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private AssetManager assetManager;

    // ✅ VARIÁVEL PARA ARMAZENAR PERSONAGEM SELECIONADO
    private String personagemSelecionado = "padrao";

    @Override
    public void create() {
        try {
            System.out.println("🚀 Iniciando aplicação...");

            batch = new SpriteBatch();
            font = new BitmapFont();
            System.out.println("✅ Fonte carregada");

            assetManager = new AssetManager();
            System.out.println("✅ AssetManager criado");

            // 🎵 INICIALIZA O AUDIO MANAGER E CARREGA A MÚSICA E EFEITOS
            System.out.println("🎵 Carregando áudio...");
            AudioManager.getInstance().carregarMusica();
            AudioManager.getInstance().carregarEfeitos();
            System.out.println("✅ Áudio configurado");

            System.out.println("🎯 Criando MenuScreen...");
            setScreen(new MenuScreen(this));
            System.out.println("🎊 Tudo pronto! Menu deve estar visível.");

        } catch (Exception e) {
            System.err.println("💥 ERRO CRÍTICO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    // ✅ MÉTODOS PARA PERSONAGEM SELECIONADO
    public void setPersonagemSelecionado(String personagem) {
        this.personagemSelecionado = personagem;
        System.out.println("💾 GameMain: Personagem salvo - " + personagem);
    }

    public String getPersonagemSelecionado() {
        return personagemSelecionado;
    }

    // ✅ SISTEMA DE METAS POR PERSONAGEM
    public float getMetaPersonagem(String personagem) {
        switch (personagem.toLowerCase()) {
            case "guerreiro": // Amarelo
                return 2000f;
            case "mago": // Azul
                return 3000f;
            default: // Vermelho (padrão) e qualquer outro
                return 1000f;
        }
    }

    public Color getCorMeta(String personagem) {
        switch (personagem.toLowerCase()) {
            case "guerreiro":
                return Color.YELLOW;
            case "mago":
                return Color.BLUE;
            default:
                return Color.RED;
        }
    }

    public String getNomeMeta(String personagem) {
        switch (personagem.toLowerCase()) {
            case "guerreiro":
                return "2000 METROS";
            case "mago":
                return "3000 METROS";
            default:
                return "1000 METROS";
        }
    }

    // ✅ MÉTODO PARA OBTER A META DO PERSONAGEM ATUAL
    public float getMetaPersonagemAtual() {
        return getMetaPersonagem(personagemSelecionado);
    }

    // ✅ MÉTODO PARA OBTER A COR DA META DO PERSONAGEM ATUAL
    public Color getCorMetaAtual() {
        return getCorMeta(personagemSelecionado);
    }

    // ✅ MÉTODO PARA OBTER O NOME DA META DO PERSONAGEM ATUAL
    public String getNomeMetaAtual() {
        return getNomeMeta(personagemSelecionado);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (batch != null) {
            batch.dispose();
            System.out.println("✅ SpriteBatch liberado");
        }
        if (font != null) {
            font.dispose();
            System.out.println("✅ Fonte liberada");
        }
        if (assetManager != null) {
            assetManager.dispose();
            System.out.println("✅ AssetManager liberado");
        }
        // 🎵 DISPOSE DO AUDIO MANAGER
        AudioManager.getInstance().dispose();
        System.out.println("✅ AudioManager liberado");
        System.out.println("✅ Todos os recursos liberados");
    }
}
