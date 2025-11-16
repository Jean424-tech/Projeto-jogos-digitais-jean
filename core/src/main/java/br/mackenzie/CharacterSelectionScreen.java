package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class CharacterSelectionScreen implements Screen {
    private final GameMain game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;

    // ✅ VARIÁVEIS DO FUNDO (MESMO LAYOUT DO MENU)
    private Texture background;
    private Terrain terrain;
    private Texture cyclistBackground;

    private float backgroundOffset = 0f;
    private float backgroundSpeed = 15f;
    private float fixedPlayerX = 400f;
    private float fixedPlayerY = 0f;

    private String personagemSelecionado;
    private Label labelNomePersonagem;

    public CharacterSelectionScreen(GameMain game) {
        this.game = game;
        this.personagemSelecionado = "padrao"; // ✅ AGORA COMEÇA COM CICLISTINHA SELECIONADO
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // ✅ CÂMERA E VIEWPORT (IGUAL AO MENU)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.position.set(400f, 240, 0);
        camera.update();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // ✅ CARREGA OS ASSETS (IGUAL AO MENU)
        background = game.getAssetManager().getTexture("background");
        terrain = new Terrain();

        // ✅ POSICIONA O CICLISTA (IGUAL AO MENU)
        findGoodPlayerPosition();

        // ✅ CARREGA O PERSONAGEM INICIAL
        carregarPersonagemFundo();

        // ✅ CRIA SKIN COM MESMO ESTILO DO MENU
        criarSkin();

        // ✅ CRIA UI COM MESMO LAYOUT DO MENU
        criarUI();

        System.out.println("✅ Tela de seleção de personagens carregada!");
    }

    // ✅ MÉTODO PARA CARREGAR PERSONAGEM DO FUNDO
    private void carregarPersonagemFundo() {
        cyclistBackground = game.getAssetManager().getPersonagemMenuTexture(personagemSelecionado);

        if (cyclistBackground == null) {
            System.out.println("❌ Personagem não encontrado: " + personagemSelecionado);
        } else {
            System.out.println("✅ Personagem carregado: " + personagemSelecionado);
        }
    }

    // ✅ MÉTODO PARA ATUALIZAR PERSONAGEM NO FUNDO
    private void atualizarPersonagemFundo() {
        cyclistBackground = null;
        cyclistBackground = game.getAssetManager().getPersonagemMenuTexture(personagemSelecionado);

        if (cyclistBackground == null) {
            System.out.println("❌ Personagem não encontrado: " + personagemSelecionado);
        } else {
            System.out.println("✅ Personagem atualizado: " + personagemSelecionado);
        }
    }

    // ✅ MÉTODO PARA ENCONTRAR BOA POSIÇÃO DO CICLISTA (IGUAL AO MENU)
    private void findGoodPlayerPosition() {
        for (float x = 300f; x < 500f; x += 10f) {
            float height = terrain.getHeightAt(x);
            if (height > 100f && height < 180f) {
                fixedPlayerX = x - 250f;
                fixedPlayerY = height - 28f;
                System.out.println("🎯 Ciclista posicionado em: " + fixedPlayerX + ", " + fixedPlayerY);
                return;
            }
        }
        fixedPlayerX = 1100f;
        fixedPlayerY = terrain.getHeightAt(1100f) - 1100f;
    }

    // ✅ MÉTODO CRIAR SKIN (MESMO ESTILO DO MENU)
    private void criarSkin() {
        skin = new Skin();

        // ✅ MESMA TEXTURA BRANCA DO MENU
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture whitePixel = new Texture(pixmap);
        pixmap.dispose();

        skin.add("white", whitePixel);

        // ✅ MESMA FONTE DO MENU
        BitmapFont font = game.getFont();
        skin.add("default-font", font);

        // ✅ MESMO ESTILO DE BOTÃO DO MENU
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.fontColor = Color.WHITE;

        // ✅ MESMAS CORES DO MENU
        textButtonStyle.up = skin.newDrawable("white", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        textButtonStyle.down = skin.newDrawable("white", new Color(0.05f, 0.2f, 0.5f, 0.9f));
        textButtonStyle.over = skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.8f));

        skin.add("default", textButtonStyle);
    }

    // ✅ MÉTODO CRIAR UI COM MESMO LAYOUT DO MENU
    private void criarUI() {
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        // ✅ BOTÕES DOS PERSONAGENS - CICLISTINHA AGORA É O PRIMEIRO
        TextButton btnCiclistinha = criarBotaoPersonagem("CICLISTINHA", "padrao"); // ✅ PRIMEIRO
        TextButton btnAmarelinho = criarBotaoPersonagem("AMARELINHO", "guerreiro");
        TextButton btnAzulzinho = criarBotaoPersonagem("AZULZINHO", "mago");

        // ✅ MESMA POSIÇÃO DOS BOTÕES DO MENU (DIREITA)
        float buttonX = centerX + 200;

        btnCiclistinha.setSize(280, 65);
        btnAmarelinho.setSize(280, 65);
        btnAzulzinho.setSize(280, 65);

        // ✅ POSIÇÃO VERTICAL - CICLISTINHA AGORA É O PRIMEIRO (MAIS ALTO)
        btnCiclistinha.setPosition(buttonX - 140, centerY + 80);   // ✅ PRIMEIRO - MAIS ALTO
        btnAmarelinho.setPosition(buttonX - 140, centerY + 0);     // ✅ SEGUNDO - MEIO
        btnAzulzinho.setPosition(buttonX - 140, centerY - 80);     // ✅ TERCEIRO - MAIS BAIXO

        stage.addActor(btnCiclistinha);
        stage.addActor(btnAmarelinho);
        stage.addActor(btnAzulzinho);

        // ✅ BOTÕES INICIAR E VOLTAR - MUITO MAIS PRA ESQUERDA E BAIXO
        TextButton btnVoltar = criarBotao("VOLTAR", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        TextButton btnIniciar = criarBotao("INICIAR JOGO", new Color(0.1f, 0.6f, 0.3f, 0.7f));

        btnVoltar.setSize(280, 65);   // ✅ MESMO TAMANHO
        btnIniciar.setSize(280, 65);  // ✅ MESMO TAMANHO

        // ✅ POSIÇÕES MUITO MAIS PRA ESQUERDA E BAIXO
        btnVoltar.setPosition(buttonX - 500, centerY - 200);  // ✅ MUITO MAIS ESQUERDA
        btnIniciar.setPosition(buttonX - 180, centerY - 200);  // ✅ MUITO MAIS ESQUERDA

        stage.addActor(btnVoltar);
        stage.addActor(btnIniciar);

        btnIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("🎮 Iniciando jogo com: " + personagemSelecionado);

                // ✅ CORREÇÃO: Salva no GameMain e cria PlayScreen apenas com game
                game.setPersonagemSelecionado(personagemSelecionado);
                game.setScreen(new PlayScreen(game)); // ✅ AGORA SÓ PASSA O GAME
            }
        });

        btnVoltar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
    }

    // ✅ MÉTODO CRIAR BOTÃO PERSONAGEM COM MESMO ESTILO
    private TextButton criarBotaoPersonagem(String texto, String tipoPersonagem) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();

        // ✅ COR DIFERENTE PARA BOTÃO SELECIONADO (AMARELO)
        if (personagemSelecionado.equals(tipoPersonagem)) {
            style.fontColor = Color.YELLOW;
            style.up = skin.newDrawable("white", new Color(0.3f, 0.5f, 0.1f, 0.9f));
        } else {
            // ✅ MESMO ESTILO PADRÃO DO MENU
            style.fontColor = Color.WHITE;
            style.up = skin.newDrawable("white", new Color(0.1f, 0.3f, 0.6f, 0.7f));
        }

        // ✅ MESMO ESTILO DO MENU PARA DOWN E OVER
        style.down = skin.newDrawable("white", new Color(0.05f, 0.2f, 0.5f, 0.9f));
        style.over = skin.newDrawable("white", new Color(0.2f, 0.4f, 0.8f, 0.8f));

        TextButton button = new TextButton(texto, style);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                personagemSelecionado = tipoPersonagem;
                System.out.println("👤 Personagem selecionado: " + personagemSelecionado);
                atualizarPersonagemFundo();
                // ✅ ATUALIZA A UI PARA MOSTRAR BOTÃO SELECIONADO
                stage.clear();
                criarUI();
            }
        });

        return button;
    }

    // ✅ MÉTODO CRIAR BOTÃO SIMPLES COM MESMO ESTILO DO MENU
    private TextButton criarBotao(String texto, Color cor) {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = game.getFont();
        style.fontColor = Color.WHITE;
        style.up = skin.newDrawable("white", cor);
        style.down = skin.newDrawable("white", new Color(cor.r * 0.7f, cor.g * 0.7f, cor.b * 0.7f, 0.9f));
        style.over = skin.newDrawable("white", new Color(cor.r * 1.2f, cor.g * 1.2f, cor.b * 1.2f, 0.8f));

        return new TextButton(texto, style);
    }

    // ✅ MÉTODO PARA OBTER NOME DE EXIBIÇÃO AMIGÁVEL
    private String getNomeExibicao(String tipoPersonagem) {
        switch (tipoPersonagem) {
            case "padrao":
                return "CICLISTINHA"; // ✅ AGORA É O PRIMEIRO
            case "guerreiro":
                return "AMARELINHO";
            case "mago":
                return "AZULZINHO";
            default:
                return "PADRÃO";
        }
    }

    // ✅ RENDER (MESMO DO MENU)
    @Override
    public void render(float delta) {
        backgroundOffset += backgroundSpeed * delta;
        if (backgroundOffset > 800f) {
            backgroundOffset = 0f;
        }

        renderGameScene();
        stage.act(delta);
        stage.draw();
        renderMenuUI();
    }

    // ✅ RENDER DO CENÁRIO (MESMO DO MENU)
    private void renderGameScene() {
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // ✅ BACKGROUND ANIMADO (MESMO DO MENU)
        if (background != null) {
            for (int i = -1; i <= 1; i++) {
                float x = backgroundOffset + (i * 800f);
                batch.draw(background, x, 0, 800, 480);
            }
        }

        terrain.render(batch);

        // ✅ PERSONAGEM NO FUNDO (MESMO DO MENU)
        if (cyclistBackground != null) {
            batch.draw(cyclistBackground, fixedPlayerX, fixedPlayerY, 400, 300);
        }

        batch.end();
    }

    // ✅ RENDER DA UI (MESMO ESTILO DO MENU)
    private void renderMenuUI() {
        batch.begin();
        batch.setProjectionMatrix(camera.projection);

        // ✅ MESMO TÍTULO DO MENU, MAS COM TEXTO DIFERENTE
        game.getFont().getData().setScale(1.5f);
        game.getFont().setColor(Color.YELLOW);
        game.getFont().draw(batch, "🚴 SELECIONE SEU PERSONAGEM", 180, 420);

        game.getFont().getData().setScale(1.0f);
        game.getFont().setColor(Color.WHITE);
        game.getFont().draw(batch, "Escolha entre Ciclistinha, Amarelinho e Azulzinho!", 180, 390);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.setToOrtho(false, 800, 480);
    }

    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (terrain != null) terrain.dispose();
        cyclistBackground = null;
        System.out.println("✅ Tela de seleção de personagens liberada");
    }
}
