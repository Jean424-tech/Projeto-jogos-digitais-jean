package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

public class PlayScreen implements Screen {

    private final GameMain game;
    private SpriteBatch batch;
    private Texture img;

    public PlayScreen(GameMain game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        loadTexture();
    }

    private void loadTexture() {
        // Tente carregar a textura com tratamento de erro robusto
        String[] possibleFilenames = {"dc1d7afacc108198962377bef2bfb825.jpg", "character.png"};

        for (String filename : possibleFilenames) {
            try {
                if (Gdx.files.internal(filename).exists()) {
                    img = new Texture(Gdx.files.internal(filename));
                    System.out.println("✅ " + filename + " carregado com sucesso!");
                    return;
                } else {
                    System.out.println("⚠️ " + filename + " não encontrado");
                }
            } catch (Exception e) {
                System.out.println("❌ Erro ao carregar " + filename + ": " + e.getMessage());
            }
        }

        // Se nenhum arquivo foi encontrado
        System.out.println("❌ Nenhuma textura foi carregada. Verifique:");
        System.out.println("   - Nome do arquivo");
        System.out.println("   - Localização (deve estar em android/assets/)");
        System.out.println("   - Extensão do arquivo");
    }

    @Override
    public void render(float delta) {
        // Fundo branco
        Gdx.gl.glClearColor(1, 1, 1, 1); // RGB: 1,1,1 = Branco | Alpha: 1 = Totalmente opaco
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (img != null) {
            // Para centralizar a imagem em 500x380
            float x = 250 - (img.getWidth() / 2);   // Centro X menos metade da largura da imagem
            float y = 190 - (img.getHeight() / 2);  // Centro Y menos metade da altura da imagem
            batch.draw(img, x, y);
        }
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (img != null) img.dispose();
        if (batch != null) batch.dispose();
    }
}
