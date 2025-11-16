package br.mackenzie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private TextureRegion currentFrame;
    private AnimationManager animationManager;
    private float x, y;
    private float width, height;
    private Rectangle bounds;
    private GameMain game;
    private boolean isPedaling;
    private float rotation;
    private PhysicsEngine physics;
    private float terrainAngle;

    // ✅ VARIÁVEIS PARA SISTEMA DE PERSONAGENS
    private String tipoPersonagem;
    private float forcaPedalada;
    private float velocidadeMaxima;

    // ✅ CONSTRUTOR ORIGINAL (mantido para compatibilidade)
    public Player(GameMain game) {
        this(game, "padrao");
    }

    // ✅ NOVO CONSTRUTOR QUE RECEBE O TIPO DE PERSONAGEM
    public Player(GameMain game, String tipoPersonagem) {
        this.game = game;
        this.tipoPersonagem = tipoPersonagem;
        this.width = 170;
        this.height = 150;
        this.x = 100;
        this.y = 50;
        this.bounds = new Rectangle(x, y, width, height);
        this.isPedaling = false;
        this.rotation = 0f;
        this.physics = new PhysicsEngine();
        this.terrainAngle = 0f;

        // ✅ APLICA AS CARACTERÍSTICAS DO PERSONAGEM
        aplicarCaracteristicasPersonagem();
        setupAnimation();

        System.out.println("🎯 Player criado como: " + tipoPersonagem);
    }

    // ✅ MÉTODO PARA APLICAR CARACTERÍSTICAS DO PERSONAGEM
    private void aplicarCaracteristicasPersonagem() {
        switch (tipoPersonagem) {
            case "guerreiro":
                // Guerreiro: mais força, menos velocidade
                forcaPedalada = 1.5f;
                velocidadeMaxima = 25f;
                System.out.println("⚔️ Guerreiro selecionado - Mais força!");
                break;
            case "mago":
                // Mago: menos força, mais velocidade
                forcaPedalada = 0.7f;
                velocidadeMaxima = 40f;
                System.out.println("🔮 Mago selecionado - Mais velocidade!");
                break;
            case "arqueiro":
                // Arqueiro: equilibrado
                forcaPedalada = 1.1f;
                velocidadeMaxima = 32f;
                System.out.println("🏹 Arqueiro selecionado - Equilibrado!");
                break;
            default:
                // Personagem padrão
                forcaPedalada = 1.0f;
                velocidadeMaxima = 30f;
                System.out.println("🚴 Personagem padrão");
        }

        // ✅ CONFIGURA A PHYSICS ENGINE COM AS NOVAS CARACTERÍSTICAS
        if (physics != null) {
            physics.configurarPersonagem(forcaPedalada, velocidadeMaxima);
        }

        System.out.println("📊 Estatísticas - Força: " + forcaPedalada + ", VelMax: " + velocidadeMaxima);
    }

    // ✅ MÉTODO PARA OBTER O TIPO DE PERSONAGEM (se precisar)
    public String getTipoPersonagem() {
        return tipoPersonagem;
    }

    // ✅ MÉTODO PARA MUDAR PERSONAGEM DURANTE O JOGO (opcional)
    public void mudarPersonagem(String novoTipo) {
        this.tipoPersonagem = novoTipo;
        aplicarCaracteristicasPersonagem();
        // ✅ RECARREGA AS ANIMAÇÕES COM O NOVO PERSONAGEM
        setupAnimation();
    }

    private void setupAnimation() {
        TextureRegion[] frames = createAnimationFrames();
        animationManager = new AnimationManager(frames, 0.1f);
        currentFrame = frames[0];
    }

    private TextureRegion[] createAnimationFrames() {
        // ✅ CORREÇÃO: USA A TEXTURA CORRETA DO PERSONAGEM SELECIONADO
        TextureRegion cyclistSheet = getTextureRegionPersonagem();

        if (cyclistSheet != null) {
            System.out.println("🎨 Carregando spritesheet para: " + tipoPersonagem);
            return dividirSpritesheet(cyclistSheet);
        } else {
            System.out.println("⚠️ Spritesheet não encontrada para: " + tipoPersonagem + ", usando placeholder");
            return createPlaceholderFrames();
        }
    }

    // ✅ NOVO MÉTODO: OBTÉM A TEXTURA REGION DO PERSONAGEM CORRETO
    private TextureRegion getTextureRegionPersonagem() {
        try {
            // ✅ TENTA PEGAR A TEXTURA DO PERSONAGEM ESPECÍFICO
            com.badlogic.gdx.graphics.Texture personagemTexture = game.getAssetManager().getPersonagemTexture(tipoPersonagem);

            if (personagemTexture != null) {
                System.out.println("✅ Textura encontrada para: " + tipoPersonagem +
                    " (" + personagemTexture.getWidth() + "x" + personagemTexture.getHeight() + ")");
                return new TextureRegion(personagemTexture);
            } else {
                // ✅ FALLBACK: USA A TEXTURA PADRÃO
                System.out.println("❌ Textura não encontrada para: " + tipoPersonagem + ", usando padrão");
                return game.getAssetManager().getTextureRegion("cyclist_sheet");
            }
        } catch (Exception e) {
            System.err.println("💥 Erro ao carregar textura do personagem: " + e.getMessage());
            return game.getAssetManager().getTextureRegion("cyclist_sheet");
        }
    }

    // ✅ NOVO MÉTODO: DIVIDE A SPRITESHEET EM FRAMES
    private TextureRegion[] dividirSpritesheet(TextureRegion cyclistSheet) {
        int totalWidth = cyclistSheet.getRegionWidth();
        int totalHeight = cyclistSheet.getRegionHeight();

        System.out.println("📐 Dividindo spritesheet: " + totalWidth + "x" + totalHeight + " para " + tipoPersonagem);

        // ✅ DETECTA AUTOMATICAMENTO O LAYOUT DA SPRITESHEET
        boolean isGrid2x2 = totalHeight >= totalWidth / 2;

        if (isGrid2x2) {
            // ✅ LAYOUT 2x2 (4 frames)
            int frameWidth = totalWidth / 2;
            int frameHeight = totalHeight / 2;

            TextureRegion[] frames = new TextureRegion[4];
            frames[0] = new TextureRegion(cyclistSheet, 0, 0, frameWidth, frameHeight);
            frames[1] = new TextureRegion(cyclistSheet, frameWidth, 0, frameWidth, frameHeight);
            frames[2] = new TextureRegion(cyclistSheet, 0, frameHeight, frameWidth, frameHeight);
            frames[3] = new TextureRegion(cyclistSheet, frameWidth, frameHeight, frameWidth, frameHeight);

            System.out.println("🔄 Layout 2x2 detectado: " + frameWidth + "x" + frameHeight);
            return frames;
        } else {
            // ✅ LAYOUT 4x1 (4 frames em linha)
            int frameWidth = totalWidth / 4;
            int frameHeight = totalHeight;

            TextureRegion[] frames = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                frames[i] = new TextureRegion(cyclistSheet, i * frameWidth, 0, frameWidth, frameHeight);
            }

            System.out.println("🔄 Layout 4x1 detectado: " + frameWidth + "x" + frameHeight);
            return frames;
        }
    }

    private TextureRegion[] createPlaceholderFrames() {
        TextureRegion[] frames = new TextureRegion[4];

        for (int i = 0; i < 4; i++) {
            com.badlogic.gdx.graphics.Pixmap pixmap =
                new com.badlogic.gdx.graphics.Pixmap(64, 64,
                    com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);

            // ✅ CORES DIFERENTES PARA CADA PERSONAGEM
            switch(tipoPersonagem) {
                case "guerreiro":
                    pixmap.setColor(1.0f, 0.8f, 0.0f, 1); // AMARELO
                    break;
                case "mago":
                    pixmap.setColor(0.0f, 0.5f, 1.0f, 1); // AZUL
                    break;
                case "arqueiro":
                    pixmap.setColor(0.6f, 0.0f, 0.8f, 1); // ROXO
                    break;
                default:
                    pixmap.setColor(1.0f, 0.0f, 0.0f, 1); // VERMELHO padrão
            }

            pixmap.fillRectangle(10, 10, 44, 44);

            // Detalhes da roupa
            pixmap.setColor(0.1f, 0.1f, 0.8f, 1);
            pixmap.fillRectangle(10, 25, 44, 20);

            // Cabeça
            pixmap.setColor(1, 0.8f, 0.6f, 1);
            pixmap.fillCircle(32, 45, 8);

            // Olhos
            pixmap.setColor(0, 0, 0, 1);
            pixmap.fillCircle(28, 46, 2);
            pixmap.fillCircle(36, 46, 2);

            // Pernas em diferentes posições (animação de pedalada)
            pixmap.setColor(0.2f, 0.2f, 0.2f, 1);
            switch(i) {
                case 0:
                    pixmap.fillRectangle(25, 15, 5, 20);
                    pixmap.fillRectangle(35, 15, 5, 20);
                    break;
                case 1:
                    pixmap.fillRectangle(25, 15, 5, 20);
                    pixmap.fillRectangle(35, 25, 5, 10);
                    break;
                case 2:
                    pixmap.fillRectangle(25, 25, 5, 10);
                    pixmap.fillRectangle(35, 25, 5, 10);
                    break;
                case 3:
                    pixmap.fillRectangle(25, 25, 5, 10);
                    pixmap.fillRectangle(35, 15, 5, 20);
                    break;
            }

            // Rodas da bike
            pixmap.setColor(0.3f, 0.3f, 0.3f, 1);
            pixmap.drawCircle(15, 15, 10);
            pixmap.drawCircle(50, 15, 10);

            frames[i] = new TextureRegion(new com.badlogic.gdx.graphics.Texture(pixmap));
            pixmap.dispose();
        }

        System.out.println("🎨 Placeholder criado para: " + tipoPersonagem);
        return frames;
    }

    public void update(float delta, boolean isPedaling, Terrain terrain) {
        this.isPedaling = isPedaling;

        // Calcula inclinação do terreno
        float frontWheelX = x + width * 0.8f;
        float backWheelX = x + width * 0.2f;

        float frontWheelHeight = terrain.getHeightAt(frontWheelX);
        float backWheelHeight = terrain.getHeightAt(backWheelX);

        float deltaX = frontWheelX - backWheelX;
        float deltaY = frontWheelHeight - backWheelHeight;
        this.terrainAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
        this.rotation = terrainAngle;

        // ✅ PASSA AS CARACTERÍSTICAS DO PERSONAGEM PARA A PHYSICS
        float physicsSpeed = physics.update(delta, isPedaling, terrainAngle, forcaPedalada, velocidadeMaxima);

        // Move o player com a velocidade
        x += physicsSpeed * 3f * delta;

        // Posiciona na altura do terreno
        float averageHeight = (frontWheelHeight + backWheelHeight) / 2f;
        y = averageHeight - height * 0.01f;

        bounds.setPosition(x, y);

        // Animação
        if (physicsSpeed > 2f) {
            currentFrame = animationManager.getCurrentFrame(delta);
        } else {
            currentFrame = animationManager.getCurrentFrame(0);
        }
    }

    public void render(SpriteBatch batch) {
        if (currentFrame != null) {
            batch.draw(
                currentFrame,
                x, y,
                width / 2, height / 2,
                width, height,
                1f, 1f,
                rotation
            );
        } else {
            // ✅ FALLBACK: DESENHA UM RETÂNGULO COLORIDO COM A COR DO PERSONAGEM
            batch.setColor(getCorPersonagem());
            batch.draw(game.getAssetManager().getTexture("white"), x, y, width, height);
            batch.setColor(1, 1, 1, 1);
        }
    }

    // ✅ MÉTODO AUXILIAR PARA OBTER COR DO PERSONAGEM
    private com.badlogic.gdx.graphics.Color getCorPersonagem() {
        switch(tipoPersonagem) {
            case "guerreiro": return new com.badlogic.gdx.graphics.Color(1.0f, 0.8f, 0.0f, 1); // AMARELO
            case "mago": return new com.badlogic.gdx.graphics.Color(0.0f, 0.5f, 1.0f, 1); // AZUL
            case "arqueiro": return new com.badlogic.gdx.graphics.Color(0.6f, 0.0f, 0.8f, 1); // ROXO
            default: return new com.badlogic.gdx.graphics.Color(1.0f, 0.0f, 0.0f, 1); // VERMELHO
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    public float getRotation() {
        return rotation;
    }

    public float getPhysicsSpeed() {
        return physics.getCurrentSpeed();
    }

    public float getTerrainAngle() {
        return terrainAngle;
    }

    public void dispose() {
        // O AssetManager cuida do dispose das texturas
    }
}
