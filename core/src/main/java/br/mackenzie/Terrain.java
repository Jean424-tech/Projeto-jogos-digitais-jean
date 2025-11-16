package br.mackenzie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private List<Vector2> points;
    private float segmentLength = 30f;
    private float amplitude = 80f;
    private float frequency = 0.005f;
    private float baseHeight = 80f;
    private TextureRegion whitePixelRegion;
    private TextureRegion greenPixelRegion;

    public Terrain() {
        points = new ArrayList<>();
        createWhitePixelTexture();
        createGreenPixelTexture();
        generateInitialTerrain();
    }

    private void createWhitePixelTexture() {
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1,
            com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();
        whitePixelRegion = new TextureRegion(texture);
    }

    private void createGreenPixelTexture() {
        com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(1, 1,
            com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.6f, 0.2f, 1);
        pixmap.fill();
        com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(pixmap);
        pixmap.dispose();
        greenPixelRegion = new TextureRegion(texture);
    }

    private void generateInitialTerrain() {
        for (float x = -200; x < 1200; x += segmentLength) {
            float y = baseHeight + (float)Math.sin(x * frequency) * amplitude;
            points.add(new Vector2(x, y));
        }
    }

    public void update(float playerX) {
        float furthestX = points.get(points.size() - 1).x;

        while (furthestX < playerX + 1000) {
            float newX = furthestX + segmentLength;
            float newY = baseHeight + (float)Math.sin(newX * frequency) * amplitude;
            points.add(new Vector2(newX, newY));
            furthestX = newX;
        }

        while (points.size() > 0 && points.get(0).x < playerX - 500) {
            points.remove(0);
        }
    }

    public void render(SpriteBatch batch) {
        // ✅ CORREÇÃO: Preenchimento suave com polígonos
        batch.setColor(0.3f, 0.6f, 0.2f, 1);

        for (int i = 0; i < points.size() - 1; i++) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            // ✅ CORREÇÃO: Desenha quadrilátero suave entre os pontos
            // Cria um polígono que vai de p1 até p2, e desce até o fundo
            drawQuad(batch, p1.x, p1.y, p2.x, p2.y, 0f);
        }

        // Linha do terreno (marrom)
        batch.setColor(0.4f, 0.3f, 0.2f, 1);
        for (int i = 0; i < points.size() - 1; i++) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            drawThickLine(batch, p1.x, p1.y, p2.x, p2.y, 8f);

            // Grama em cima
            batch.setColor(0.3f, 0.6f, 0.2f, 1);
            drawThickLine(batch, p1.x, p1.y, p2.x, p2.y, 3f);
            batch.setColor(0.4f, 0.3f, 0.2f, 1);
        }

        batch.setColor(Color.WHITE);
    }

    // ✅ NOVO MÉTODO: Desenha quadrilátero suave entre dois pontos
    private void drawQuad(SpriteBatch batch, float x1, float y1, float x2, float y2, float bottomY) {
        // Calcula a largura e altura do quadrilátero
        float width = x2 - x1;
        float height1 = y1 - bottomY;
        float height2 = y2 - bottomY;

        // Se for uma linha reta, desenha retângulo simples
        if (Math.abs(y1 - y2) < 0.1f) {
            batch.draw(greenPixelRegion, x1, bottomY, width, height1);
        } else {
            // Para inclinações, desenha dois triângulos formando um quadrilátero
            drawTriangle(batch, x1, bottomY, x1, y1, x2, y2);
            drawTriangle(batch, x1, bottomY, x2, y2, x2, bottomY);
        }
    }

    // ✅ NOVO MÉTODO: Desenha triângulo
    private void drawTriangle(SpriteBatch batch, float x1, float y1, float x2, float y2, float x3, float y3) {
        // Para desenhar triângulos, usamos múltiplos retângulos brancos pequenos
        // Esta é uma aproximação - em um motor mais avançado usaríamos PolygonSpriteBatch

        // Calcula a bounding box do triângulo
        float minX = Math.min(x1, Math.min(x2, x3));
        float maxX = Math.max(x1, Math.max(x2, x3));
        float minY = Math.min(y1, Math.min(y2, y3));
        float maxY = Math.max(y1, Math.max(y2, y3));

        // Desenha a área aproximada com pequenos retângulos
        for (float x = minX; x < maxX; x += 2f) {
            for (float y = minY; y < maxY; y += 2f) {
                if (pointInTriangle(x, y, x1, y1, x2, y2, x3, y3)) {
                    batch.draw(greenPixelRegion, x, y, 2f, 2f);
                }
            }
        }
    }

    // ✅ NOVO MÉTODO: Verifica se ponto está dentro do triângulo
    private boolean pointInTriangle(float px, float py, float x1, float y1, float x2, float y2, float x3, float y3) {
        float d1 = sign(px, py, x1, y1, x2, y2);
        float d2 = sign(px, py, x2, y2, x3, y3);
        float d3 = sign(px, py, x3, y3, x1, y1);

        boolean hasNeg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        boolean hasPos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(hasNeg && hasPos);
    }

    private float sign(float x1, float y1, float x2, float y2, float x3, float y3) {
        return (x1 - x3) * (y2 - y3) - (x2 - x3) * (y1 - y3);
    }

    private void drawThickLine(SpriteBatch batch, float x1, float y1, float x2, float y2, float thickness) {
        float angle = (float)Math.atan2(y2 - y1, x2 - x1);
        float cos = (float)Math.cos(angle);
        float sin = (float)Math.sin(angle);
        float halfThickness = thickness / 2;

        batch.draw(whitePixelRegion,
            x1 - halfThickness * sin, y1 + halfThickness * cos,
            halfThickness * sin, -halfThickness * cos,
            (float)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)), thickness,
            1f, 1f, (float)Math.toDegrees(angle));
    }

    public float getHeightAt(float x) {
        for (int i = 0; i < points.size() - 1; i++) {
            Vector2 p1 = points.get(i);
            Vector2 p2 = points.get(i + 1);

            if (x >= p1.x && x <= p2.x) {
                float t = (x - p1.x) / (p2.x - p1.x);
                return p1.y + (p2.y - p1.y) * t;
            }
        }
        return baseHeight;
    }

    public void dispose() {
        if (whitePixelRegion != null) {
            whitePixelRegion.getTexture().dispose();
        }
        if (greenPixelRegion != null) {
            greenPixelRegion.getTexture().dispose();
        }
    }
}
