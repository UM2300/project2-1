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

import java.util.ArrayList;
import java.util.List;

public class TakGameGUI extends ApplicationAdapter {
	private List<TakPiece> pieces;
	private ArrayList<TakPiece> pieces1;
	boolean displayOptions = true;
	boolean isHoveringOverOptions = false;
	boolean isHoveringOverStart = false;
	Texture optionsUncoloredTexture;
	Texture optionsColoredTexture;

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
		pieces = new ArrayList<>();

		ModelBuilder modelBuilder = new ModelBuilder();

		Model stoneModel = modelBuilder.createCylinder(1f, 0.2f, 1f, 20,
				new Material(ColorAttribute.createDiffuse(Color.GRAY)),
				Usage.Position | Usage.Normal);
		for (int i = 0; i < 21; i++) {
			pieces.add(new TakPiece(TakPiece.Type.STONE, stoneModel));
		}

		Model capstoneModel = modelBuilder.createCylinder(0.5f, 0.8f, 0.5f, 20,
				new Material(ColorAttribute.createDiffuse(Color.DARK_GRAY)),
				Usage.Position | Usage.Normal);
		pieces.add(new TakPiece(TakPiece.Type.CAPSTONE, capstoneModel));

		float startingX = boardSize * squareSize + 1;  // Starting position of the first stone on the X-axis.
		float startingZ = 0;  // Starting position of the first stone on the Z-axis.
		float rowOffset = 1.2f;  // Distance between two pieces in the same row.
		float nextRowZ = 1.5f;  // Distance to the next row on the Z-axis.

		int piecesPerRow = 12;  // Number of pieces in each row.

		// Position stones
		for (int i = 0; i < 21; i++) {
			int x = i % piecesPerRow;  // Calculate X position based on the number of pieces per row.
			int y = i / piecesPerRow;  // Calculate which row the piece is in.

			pieces.get(i).instance.transform.setToTranslation(startingX + x * rowOffset, 0.1f, startingZ + y * nextRowZ);
		}

		// Position capstone
		pieces.get(21).instance.transform.setToTranslation(startingX, 0.25f, startingZ + 2 * nextRowZ);  // Placing the capstone below the stones.

		optionsUncoloredTexture = new Texture(Gdx.files.internal("optionsuncolored.png"));
		optionsColoredTexture = new Texture(Gdx.files.internal("optionscolored.png"));
		startColoredTexture = new Texture(Gdx.files.internal("startcolored.png"));
		startTexture = new Texture(Gdx.files.internal("startuncolored.png"));

		batch = new SpriteBatch();
		guiCam = new OrthographicCamera();
		viewport = new FitViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, guiCam);
		img = new Texture("badlogic.jpg");
		font = new BitmapFont();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(15f, 20f, 15f);
		cam.lookAt(boardSize * squareSize / 2, 0, boardSize * squareSize / 2);
		cam.near = 1f;
		cam.far = 500f;
		cam.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		modelBatch = new ModelBatch();

		// Board creation
		modelBuilder.begin();
		Material whiteMat = new Material(ColorAttribute.createDiffuse(Color.WHITE));
		Material blackMat = new Material(ColorAttribute.createDiffuse(Color.BLACK));

		for (int x = 0; x < boardSize; x++) {
			for (int z = 0; z < boardSize; z++) {
				Material mat = (x + z) % 2 == 0 ? whiteMat : blackMat;
				modelBuilder.part("square_" + x + "_" + z, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, mat)
						.box(squareSize * x, 0, squareSize * z, squareSize, 0.15f, squareSize);
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
		// Clear the background first
		Gdx.gl.glClearColor(0.35f, 0.2f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		guiCam.unproject(touchPos, viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());

		float imgWidth = startTexture.getWidth() * imageScale;
		float imgHeight = startTexture.getHeight() * imageScale;
		float imgX = (VIRTUAL_WIDTH - imgWidth) / 2;
		float imgY = (VIRTUAL_HEIGHT - imgHeight) / 2;

		float optionsImgWidth = optionsUncoloredTexture.getWidth() * imageScale;
		float optionsImgHeight = optionsUncoloredTexture.getHeight() * imageScale;
		float optionsImgX = (VIRTUAL_WIDTH - optionsImgWidth) / 2;
		float optionsImgY = imgY - optionsImgHeight - 10;  // 10 units padding

		// Check for touch
		if (Gdx.input.justTouched()) {
			if ((touchPos.x >= imgX && touchPos.x <= imgX + imgWidth &&
					touchPos.y >= imgY && touchPos.y <= imgY + imgHeight) ||
					(touchPos.x >= optionsImgX && touchPos.x <= optionsImgX + optionsImgWidth &&
							touchPos.y >= optionsImgY && touchPos.y <= optionsImgY + optionsImgHeight)) {
				displayTitle = false;
			}
		}

		// Check if mouse is hovering over the start button
		if (touchPos.x >= imgX && touchPos.x <= imgX + imgWidth &&
				touchPos.y >= imgY && touchPos.y <= imgY + imgHeight) {
			isHoveringOverStart = true;
		} else {
			isHoveringOverStart = false;
		}

		// Check if mouse is hovering over the options button
		if (touchPos.x >= optionsImgX && touchPos.x <= optionsImgX + optionsImgWidth &&
				touchPos.y >= optionsImgY && touchPos.y <= optionsImgY + optionsImgHeight) {
			isHoveringOverOptions = true;
		} else {
			isHoveringOverOptions = false;
		}

		// Render 3D objects
		modelBatch.begin(cam);
		modelBatch.render(boardInstance, environment);
		for (TakPiece piece : pieces) {
			modelBatch.render(piece.instance, environment);
		}
		modelBatch.end();

		// Render 2D objects
		batch.setProjectionMatrix(guiCam.combined);
		batch.begin();

		if (displayTitle) {
			Texture currentStartTexture = isHoveringOverStart ? startColoredTexture : startTexture;
			batch.draw(currentStartTexture, imgX, imgY, imgWidth, imgHeight);

			Texture currentOptionsTexture = isHoveringOverOptions ? optionsColoredTexture : optionsUncoloredTexture;
			batch.draw(currentOptionsTexture, optionsImgX, optionsImgY, optionsImgWidth, optionsImgHeight);
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
		optionsUncoloredTexture.dispose();
		optionsColoredTexture.dispose();
	}
}