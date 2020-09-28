package com.starboy.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME;
    }

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private StarBoy game;
    private Screen targetScreen;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;

    private SpriteBatch batch;
    private Viewport viewport;
    private Camera screenCamera;

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }

    public void init(StarBoy game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.screenCamera = new OrthographicCamera(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT);
        this.viewport = new FitViewport(Rules.WORLD_WIDTH, Rules.WORLD_HEIGHT, screenCamera);
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.screenCamera.position.set(Rules.WORLD_WIDTH / 2, Rules.WORLD_HEIGHT / 2, 0);
        this.screenCamera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        batch.setProjectionMatrix(screenCamera.combined);
        Gdx.input.setInputProcessor(null);
        if (screen != null) {
            screen.dispose();
        }
        game.setScreen(loadingScreen);
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}
