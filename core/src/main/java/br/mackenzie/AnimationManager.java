package br.mackenzie;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {
    private Animation<TextureRegion> animation;
    private float stateTime;

    public AnimationManager(TextureRegion[] frames, float frameDuration) {
        this.animation = new Animation<>(frameDuration, frames);
        this.stateTime = 0f;
    }

    public TextureRegion getCurrentFrame(float delta) {
        stateTime += delta;
        return animation.getKeyFrame(stateTime, true);
    }

    public void reset() {
        stateTime = 0f;
    }

    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(stateTime);
    }
}
