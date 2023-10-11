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

// left pieces are white
// right pieces are brown


public class TakGameGUI extends ApplicationAdapter {
    private TakPiece selectedPiece;
    private float[][] boardHeights;
    public enum PlayerTurn {
        RED, GRAY
    }

    board logicBoard = new board();

    private final double DOUBLE_CLICK_THRESHOLD = 0.3 * 1000000000;


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


    private Vector3 getScreenCoords(Vector3 worldCoords) {
        return cam.project(new Vector3(worldCoords));
    }
    private int countPiecesAtPosition(int boardX, int boardZ) {
        int count = 0;
        for (TakPiece piece : pieces) {
            if (piece.boardX == boardX && piece.boardZ == boardZ) {
                count++;
            }
        }
        return count;
    }
    public boolean flag = true;
    private boolean pieceSelected = false;


    private TakPiece getClickedPiece(float screenX, float screenY) {
        if (pieceSelected) return null;
        Vector3 touchPosScreen = new Vector3(screenX, screenY, 0);
        for (TakPiece piece : pieces) {
            Vector3 piecePosScreen = getScreenCoords(piece.instance.transform.getTranslation(new Vector3()));
            float distance = piecePosScreen.dst(touchPosScreen);
            if (distance < 50) {
                // Ensure that the top piece of the stack determines who can move it.
                TakPiece topPiece = piece.getTopPiece();

                if ((currentTurn == PlayerTurn.RED && topPiece.owner == TakPiece.Owner.LEFT) ||
                        (currentTurn == PlayerTurn.GRAY && topPiece.owner == TakPiece.Owner.RIGHT)) {
                    return piece;
                }
            }
        }
        return null;
    }



    private Vector3 getClickedBoardPosition(float screenX, float screenY) {
        List<TakPiece>[][] boardPieces = new ArrayList[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                boardPieces[i][j] = new ArrayList<>();
            }
        }


        int closestX = 0;
        int closestY = 0;
        Vector3 closestPos = null;
        float minDistance = Float.MAX_VALUE;

        int startX = 0, endX = boardSize, startZ = 0, endZ = boardSize;

        if (selectedPiece != null && selectedPiece.boardX != -1 && selectedPiece.boardZ != -1) {
            startX = Math.max(0, selectedPiece.boardX - 1);
            endX = Math.min(boardSize, selectedPiece.boardX + 2);
            startZ = Math.max(0, selectedPiece.boardZ - 1);
            endZ = Math.min(boardSize, selectedPiece.boardZ + 2);
        }

        for (int x = startX; x < endX; x++) {
            for (int z = startZ; z < endZ; z++) {
                if (selectedPiece != null && Math.abs(selectedPiece.boardX - x) == 1 && Math.abs(selectedPiece.boardZ - z) == 1) {
                    continue;
                }

                Vector3 squareCenterWorld = new Vector3(x * squareSize + squareSize / 2, boardHeights[x][z], z * squareSize + squareSize / 2);
                Vector3 squareCenterScreen = getScreenCoords(squareCenterWorld);
                float distance = squareCenterScreen.dst(screenX, screenY, 0);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestPos = squareCenterWorld;
                    closestX = x;
                    closestY = z;
                }
            }
        }
        if (boardHeights[closestX][closestY] != 0 && selectedPiece != null && selectedPiece.boardX == -1 && selectedPiece.boardZ == -1) {
            return null;  // If there's already a stack and the selected piece is not on the board, then we don't allow placement.
        }
        // Check if the position already has a piece and the selected piece is not on the board


        boardPieces[closestX][closestY].add(selectedPiece);

        if(selectedPiece != null && selectedPiece.boardX == -1 && selectedPiece.boardZ == -1){
            logicBoard.addPiece(selectedPiece.getIdNum(), closestX+1, closestY+1);
        }
else if(selectedPiece != null && selectedPiece.boardX != -1 && selectedPiece.boardZ != -1){
            logicBoard.move(selectedPiece.boardX+1, selectedPiece.boardZ+1, 1, targetDir(selectedPiece.boardX+1, selectedPiece.boardZ+1, closestX+1, closestY+1), 1);
        }
        
        
        return (minDistance < 50) ? closestPos : null;
    }


    public int targetDir(int pieceX, int pieceY, int targetX, int targetY){
        if(pieceX!=targetX){
            if(targetX>pieceX)
                return 2;
            else
                return 0;
        }
        else{
            if(targetY>pieceY)
                return 1;
            else
                return 3;
        }

    }


    private PlayerTurn currentTurn = PlayerTurn.RED; // Start the game with RED's turn.


    @Override
    public void create() {


        boardHeights = new float[boardSize][boardSize];

        pieces = new ArrayList<>();

        ModelBuilder modelBuilder = new ModelBuilder();

        Material rightStoneMat = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        Material leftStoneMat = new Material(ColorAttribute.createDiffuse(Color.RED));

        Model rightStoneModel = modelBuilder.createCylinder(1f, 0.2f, 1f, 20,
                rightStoneMat,
                Usage.Position | Usage.Normal);
        for (int i = 0; i < 21; i++) {
            TakPiece piece = new TakPiece(TakPiece.Type.STONE, rightStoneModel, 3);
            piece.owner = TakPiece.Owner.RIGHT;
            pieces.add(piece);
        }

        Model leftStoneModel = modelBuilder.createCylinder(1f, 0.2f, 1f, 20,
                leftStoneMat,
                Usage.Position | Usage.Normal);
        for (int i = 0; i < 21; i++) {
            TakPiece piece = new TakPiece(TakPiece.Type.STONE, leftStoneModel, 0);
            piece.owner = TakPiece.Owner.LEFT;
            pieces.add(piece);
        }


        Material rightCapstoneMat =new Material(ColorAttribute.createDiffuse(Color.GRAY));
        Material leftCapstoneMat = new Material(ColorAttribute.createDiffuse(Color.RED));

        Model rightCapstoneModel = modelBuilder.createCylinder(1f, 0.8f, 1f, 20,
                rightStoneMat,
                Usage.Position | Usage.Normal);

        TakPiece rightCapstone = new TakPiece(TakPiece.Type.CAPSTONE, rightCapstoneModel, 5);
        rightCapstone.owner = TakPiece.Owner.RIGHT;
        pieces.add(rightCapstone);

        Model leftCapstoneModel = modelBuilder.createCylinder(1f, 0.8f, 1f, 20,
                leftStoneMat,
                Usage.Position | Usage.Normal);

        TakPiece leftCapstone = new TakPiece(TakPiece.Type.CAPSTONE, leftCapstoneModel, 2);
        leftCapstone.owner = TakPiece.Owner.LEFT;
        pieces.add(leftCapstone);

        float startingXRight = boardSize * squareSize + 1;
        float startingXLeft = -2;
        float startingZ = 0;
        float rowOffset = 1.2f;
        float nextRowZ = 1.5f;

        int piecesPerRow = 12;  // Number of pieces in each row.

        // Position right stones
        for (int i = 0; i < 21; i++) {
            int x = i % piecesPerRow;
            int y = i / piecesPerRow;  // Calculate which row the piece is in.
            pieces.get(i).instance.transform.setToTranslation(startingXRight + x * rowOffset, 0.1f, startingZ + y * nextRowZ);
        }

        // Position left stones
        for (int i = 21; i < 42; i++) {
            int x = (i - 21) % piecesPerRow;  // Adjusted for left stones
            int y = (i - 21) / piecesPerRow;
            pieces.get(i).instance.transform.setToTranslation(startingXLeft - x * rowOffset, 0.1f, startingZ + y * nextRowZ);
        }

        // Position right capstone
        pieces.get(42).instance.transform.setToTranslation(startingXRight, 0.25f, startingZ + 2 * nextRowZ);
        // Position left capstone
        leftCapstone.instance.transform.setToTranslation(startingXLeft, 0.25f, startingZ + 2 * nextRowZ);

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
                        .box(squareSize * x + squareSize/2, 0.075f, squareSize * z + squareSize/2, squareSize, 0.15f, squareSize);
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

        float optionsImgWidth = optionsUncoloredTexture.getWidth() * imageScale;
        float optionsImgHeight = optionsUncoloredTexture.getHeight() * imageScale;
        float optionsImgX = (VIRTUAL_WIDTH - optionsImgWidth) / 2;
        float optionsImgY = imgY - optionsImgHeight - 10;  // 10 units padding

        //for the double click for standingstone conversion
        double lastClickedTime = 0;
        Vector3 lastClickedPosition = new Vector3();
        double currentTime = System.nanoTime();

        if (Gdx.input.justTouched()) {
            if (displayTitle &&
                    ((touchPos.x >= imgX && touchPos.x <= imgX + imgWidth &&
                            touchPos.y >= imgY && touchPos.y <= imgY + imgHeight) ||
                            (touchPos.x >= optionsImgX && touchPos.x <= optionsImgX + optionsImgWidth &&
                                    touchPos.y >= optionsImgY && touchPos.y <= optionsImgY + optionsImgHeight))) {
                displayTitle = false;
            } else if (!displayTitle) {
                TakPiece clickedPiece = getClickedPiece(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                if (clickedPiece != null) {
                    selectedPiece = clickedPiece;
                    pieceSelected = true;
                } else {
                    Vector3 boardPos = getClickedBoardPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
                    if (boardPos != null && selectedPiece != null) {
                        int boardX = (int) (boardPos.x / squareSize);
                        int boardZ = (int) (boardPos.z / squareSize);

                        int stackedPiecesCount = countPiecesAtPosition(boardX, boardZ);
                        if(stackedPiecesCount > 1) {
                            // This is where we handle the condition of multiple pieces stacked at the clicked position.
                            // For now, I'll just print the number of stacked pieces.
                            // You can add logic to handle the stack here.
                            System.out.println("There are " + stackedPiecesCount + " pieces stacked at this position.");
                        }

                        boolean hasCapstone = false;
                        for (TakPiece piece : pieces) {
                            if (piece.type == TakPiece.Type.CAPSTONE && piece.boardX == boardX && piece.boardZ == boardZ) {
                                hasCapstone = true;
                                break;
                            }
                        }

                        //double click detection for standing stone conversion
                        if (currentTime - lastClickedTime < DOUBLE_CLICK_THRESHOLD && boardPos != null) {
                            
                        }

                        if (!hasCapstone) {
                            boardHeights[boardX][boardZ] += 0.2f;
                            selectedPiece.instance.transform.setToTranslation(boardPos.x, boardHeights[boardX][boardZ], boardPos.z);
                            selectedPiece.boardX = boardX;
                            selectedPiece.boardZ = boardZ;
                            currentTurn = (currentTurn == PlayerTurn.RED) ? PlayerTurn.GRAY : PlayerTurn.RED; // Switch the turn
                            selectedPiece = null; // Deselect the piece
                        }
                        pieceSelected = false;
                    }
                }
            }
        }

        // Check if mouse is hovering over the start button
        isHoveringOverStart = touchPos.x >= imgX && touchPos.x <= imgX + imgWidth &&
                touchPos.y >= imgY && touchPos.y <= imgY + imgHeight;

        // Check if mouse is hovering over the options button
        isHoveringOverOptions = touchPos.x >= optionsImgX && touchPos.x <= optionsImgX + optionsImgWidth &&
                touchPos.y >= optionsImgY && touchPos.y <= optionsImgY + optionsImgHeight;

        modelBatch.begin(cam);
        modelBatch.render(boardInstance, environment);
        for (TakPiece piece : pieces) {
            modelBatch.render(piece.instance, environment);
        }
        modelBatch.end();

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