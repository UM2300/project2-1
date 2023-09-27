package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TakGameGUI extends ApplicationAdapter {
	boolean isHoveringOverStart = false;

	Texture startColoredTexture;
	private OrthographicCamera guiCam;
	private int boardSize = 5; 
	private float squareSize = 1.5f;
	Texture startTexture;
	private static final int VIRTUAL_WIDTH = 1280;
	private static final int VIRTUAL_HEIGHT = 720;
	private FitViewport viewport;


	SpriteBatch batch;
	Texture img;
	BitmapFont font;


	ModelBatch modelBatch;
	Model boardModel;
	ModelInstance boardInstance;
	PerspectiveCamera cam;
	Environment environment;
	float imageScale = 0.5f;

	boolean displayTitle = true;

	@Override
	public void create() {
		startColoredTexture = new Texture(Gdx.files.internal("startcolored.png"));
		startTexture = new Texture(Gdx.files.internal("startuncolored.png"));

		batch = new SpriteBatch();

		guiCam = new OrthographicCamera();
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, guiCam);

		img = new Texture("badlogic.jpg");
		font = new BitmapFont();

		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(7f, 12f, 7f);  
		cam.lookAt(boardSize * squareSize / 2, 0, boardSize * squareSize / 2);  // Adjusted lookAt to center of the board
		cam.near = 1f;
		cam.far = 300f;
		cam.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));

		modelBatch = new ModelBatch();
		ModelBuilder modelBuilder = new ModelBuilder();


		modelBuilder.begin();
		Material whiteMat = new Material(ColorAttribute.createDiffuse(Color.WHITE));
		Material blackMat = new Material(ColorAttribute.createDiffuse(Color.BLACK));

		float squareSize = 1.5f;  
		int boardSize = 5;       

		for (int x = 0; x < boardSize; x++) {
			for (int z = 0; z < boardSize; z++) {
				Material mat = (x + z) % 2 == 0 ? whiteMat : blackMat;
				modelBuilder.part("square_" + x + "_" + z, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, mat)
						.box(squareSize * x, 0, squareSize * z, squareSize, 0.15f, squareSize);  // Made the height a bit taller for visual appeal
			}
		}
		boardModel = modelBuilder.end();
		boardInstance = new ModelInstance(boardModel);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}



	@Override
	public void render() {
		Gdx.gl.glClearColor(0.35f, 0.2f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		guiCam.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

		float imgWidth = startTexture.getWidth() * imageScale;
		float imgHeight = startTexture.getHeight() * imageScale;
		float imgX = (VIRTUAL_WIDTH - imgWidth) / 2;
		float imgY = (VIRTUAL_HEIGHT - imgHeight) / 2;


		if (Gdx.input.justTouched()) {
			if (touchPos.x >= imgX && touchPos.x <= imgX + imgWidth &&
					touchPos.y >= imgY && touchPos.y <= imgY + imgHeight) {
				displayTitle = false;
			}
		}


		if (touchPos.x >= imgX && touchPos.x <= imgX + imgWidth &&
				touchPos.y >= imgY && touchPos.y <= imgY + imgHeight) {
			isHoveringOverStart = true;
		} else {
			isHoveringOverStart = false;
		}


		modelBatch.begin(cam);
		modelBatch.render(boardInstance, environment);
		modelBatch.end();


		batch.setProjectionMatrix(guiCam.combined);
		batch.begin();

		if (displayTitle) {

			Texture currentTexture = isHoveringOverStart ? startColoredTexture : startTexture;
			batch.draw(currentTexture, imgX, imgY, imgWidth, imgHeight);
		}

		batch.end();
	}



	@Override
	public void dispose() {
		batch.dispose();
		img.dispose();
		font.dispose();


		modelBatch.dispose();
		boardModel.dispose();
		startColoredTexture.dispose();
	}
}