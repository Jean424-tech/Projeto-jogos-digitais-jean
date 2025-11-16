package br.mackenzie;

public class PhysicsEngine {
    // Constantes físicas BASE (serão modificadas pelo personagem)
    public static final float GRAVITY = 9.8f * 60f;
    public static final float FRICTION = 0.97f;
    public static final float AIR_RESISTANCE = 0.995f;
    public static final float BASE_MAX_SPEED = 50f;
    public static final float BASE_PEDAL_POWER = 6f;
    public static final float DOWNHILL_BOOST = 3.0f;
    public static final float UPHILL_RESISTANCE = 0.8f;

    // ✅ VARIÁVEIS MODIFICÁVEIS PELO PERSONAGEM
    private float currentMaxSpeed;
    private float currentPedalPower;

    private float currentSpeed;
    private boolean pedalPressed;

    public PhysicsEngine() {
        this.currentSpeed = 0f;
        this.pedalPressed = false;
        // ✅ VALORES PADRÃO
        this.currentMaxSpeed = BASE_MAX_SPEED;
        this.currentPedalPower = BASE_PEDAL_POWER;
    }

    // ✅ MÉTODO UPDATE ORIGINAL (mantido para compatibilidade)
    public float update(float delta, boolean isPedaling, float terrainAngle) {
        return update(delta, isPedaling, terrainAngle, 1.0f, BASE_MAX_SPEED);
    }

    // ✅ NOVO MÉTODO UPDATE QUE RECEBE CARACTERÍSTICAS DO PERSONAGEM
    public float update(float delta, boolean isPedaling, float terrainAngle, float forcaPedalada, float velocidadeMaxima) {
        // ✅ CONFIGURA AS CARACTERÍSTICAS DO PERSONAGEM
        this.currentPedalPower = BASE_PEDAL_POWER * forcaPedalada;
        this.currentMaxSpeed = velocidadeMaxima;

        // Converte ângulo para inclinação
        float slopeFactor = (float) Math.sin(Math.toRadians(terrainAngle));

        // BATIDA DE TECLA
        if (isPedaling && !pedalPressed) {
            currentSpeed += currentPedalPower; // ✅ USA A FORÇA DO PERSONAGEM
            pedalPressed = true;
        }

        // Reseta o estado da tecla quando solta
        if (!isPedaling) {
            pedalPressed = false;
        }

        // FÍSICA DAS DESCIDAS
        if (slopeFactor < -0.02f) { // DESCENDO
            float gravityForce = Math.abs(slopeFactor) * GRAVITY * delta;
            float downhillBoost = Math.abs(slopeFactor) * DOWNHILL_BOOST * 25f * delta;
            currentSpeed += (gravityForce + downhillBoost);
        }
        // FÍSICA DAS SUBIDAS
        else if (slopeFactor > 0.05f) { // SUBINDO
            float resistance = slopeFactor * UPHILL_RESISTANCE * 15f * delta;
            currentSpeed -= resistance;
        }

        // INÉRCIA
        if (slopeFactor < -0.1f) {
            currentSpeed *= 0.99f; // QUASE NENHUM ATRITO NA DESCIDA ÍNGREME
        } else {
            currentSpeed *= FRICTION;
        }

        // ✅ LIMITES USANDO A VELOCIDADE MÁXIMA DO PERSONAGEM
        currentSpeed = Math.max(0f, Math.min(currentMaxSpeed, currentSpeed));

        return currentSpeed;
    }

    // ✅ MÉTODO PARA CONFIGURAR PERSONAGEM DIRETAMENTE
    public void configurarPersonagem(float forcaPedalada, float velocidadeMaxima) {
        this.currentPedalPower = BASE_PEDAL_POWER * forcaPedalada;
        this.currentMaxSpeed = velocidadeMaxima;
        System.out.println("⚙️ Physics configurada - Força: " + currentPedalPower + ", VelMax: " + currentMaxSpeed);
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    // ✅ MÉTODOS PARA OBTER AS CONFIGURAÇÕES ATUAIS
    public float getCurrentMaxSpeed() {
        return currentMaxSpeed;
    }

    public float getCurrentPedalPower() {
        return currentPedalPower;
    }

    public void reset() {
        currentSpeed = 0f;
        pedalPressed = false;
        // ✅ MANTÉM AS CONFIGURAÇÕES DO PERSONAGEM
    }
}
