package android.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

import android.util.Log;

import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class LibgdxTest implements ApplicationListener{
	
	private Stage stage;
	private SpriteBatch batch;
	Controller controller;
	GestureDetector gestureDetector;
	
	Player player;
	
	double levelStartTime;
	
	ArrayList<Planet> planets;
	Planet win;
	
	Audio audio;
	static Music bambi;
	static Music fart0;
	static Music fart1;
	static Music fart2;
	static Music meow0;
	static Music meow1;
	static Music meow2;
	static Music thbb0;
	static Music thbb1;
	
	static Music bgMusic;
	static Music menuMusic;
	static Music happyMeow;
	static Music sadMeow;
	static Music lostMeow;
	
	int levelEndWait = 0;
	int maxLevelEndWait = 70;
	String doWhatAfterWait;
	
	Sprite fuel;
	Sprite fuelLow;
	
	Camera camera;
	
	int x, y;
	
	int levelNum;
	String levels = "levels";
	
	TiledSprite bg;
	
	ArrayList<ParticleSource> particleSources;
	
	boolean menuMode = true;
	Menu currentMenu;
	Menu herpMenu;
	Menu levelMenu;
	Menu mainMenu;
	Menu creditsMenu;
	Menu tutorialMenu;
	Menu scoresMenu;
	
	Sprite glowG, glowR, glowB;
	
	Sprite menuButton;
	
	BitmapFont terminus;
	
	private TextureAtlas atlas;
    private BitmapFont font;
    
    Rectangle levelBox;
    

	public void create() {	
		
		particleSources = new ArrayList<ParticleSource>();
		
		stage = new Stage(0, 0, true);
		batch = new SpriteBatch();
		
		controller = new Controller();
        controller.model = this;
        gestureDetector = new GestureDetector(controller);
        Gdx.input.setInputProcessor(gestureDetector);
        planets = new ArrayList<Planet>();
        
        player = new Player();

        loadLevel("level0.png");
        
        audio = Gdx.audio;
        bambi = audio.newMusic(Gdx.files.internal("bambi.ogg"));
        fart0 = audio.newMusic(Gdx.files.internal("sound/fart-0.ogg"));
        fart1 = audio.newMusic(Gdx.files.internal("sound/fart-1.ogg"));
        fart2 = audio.newMusic(Gdx.files.internal("sound/fart-2.ogg"));
        meow0 = audio.newMusic(Gdx.files.internal("sound/meow-0.ogg"));
        meow1 = audio.newMusic(Gdx.files.internal("sound/meow-1.ogg"));
        meow2 = audio.newMusic(Gdx.files.internal("sound/meow-2.ogg"));
        thbb0 = audio.newMusic(Gdx.files.internal("sound/thhhbbb-1.ogg"));
        thbb1 = audio.newMusic(Gdx.files.internal("sound/thhhbbb-3.ogg"));
        
        bgMusic =   audio.newMusic(Gdx.files.internal("sound/3-1_final.ogg"));
        menuMusic = audio.newMusic(Gdx.files.internal("sound/blue_danube_hardcore.ogg"));
    	happyMeow = audio.newMusic(Gdx.files.internal("sound/happy_meow_0.ogg"));
    	sadMeow =   audio.newMusic(Gdx.files.internal("sound/sad_meow_0.ogg"));
    	lostMeow =  audio.newMusic(Gdx.files.internal("sound/lost_meow_0.ogg"));
    	bgMusic.setLooping(true);
    	menuMusic.setLooping(true);
    	bgMusic.setVolume(.3f);
        //bambi.setLooping(true);
      
        fuel = new Sprite("fuelbar.png");
        fuel.height = 15;
        fuel.width = Gdx.graphics.getWidth();
        
        fuelLow = new Sprite("fuelLow.png");
        fuelLow.width *= 3;
        fuelLow.height *= 3;
        fuelLow.x = Gdx.graphics.getWidth() / 2 - fuelLow.width / 2;
        fuelLow.y = 15;
        
        glowR = new Sprite("redGlow.png", 100, 100);
        glowG = new Sprite("greenGlow.png", 200, 200);
        glowB = new Sprite("blueGlow.png", 100, 100);

        this.camera = new Camera(this.player);
        
        bg = new TiledSprite("starbg.png");
        
//        terminus = new BitmapFont(Gdx.files.internal("terminus.fon"), false);
        
        menuButton = new Sprite("menuback.png", 64, 64);
        menuButton.x = 0;
        menuButton.y = Gdx.graphics.getHeight() - menuButton.height;
        
        tutorialMenu = new Menu("tutorial.png");
        tutorialMenu.addButton(new Button(0, 0, 512, 512, true){
        	public void react(LibgdxTest model){
        		currentMenu = mainMenu;
        	}
        });
        
        levelMenu = new Menu("levelmenu.png");
        levelMenu.addButton(new Button(28, 64 - 5 - 10, 10, 10, true){
        	public void react(LibgdxTest model){
        		model.currentMenu = model.mainMenu;
        	}
        });
        levelMenu.addButton(new LevelButton(17, 64 - 18 - 10, 10, 10, true, 1));
        levelMenu.addButton(new LevelButton(28, 64 - 18 - 10, 10, 10, true, 2));
        levelMenu.addButton(new LevelButton(39, 64 - 18 - 10, 10, 10, true, 3));
        levelMenu.addButton(new LevelButton(17, 64 - 29 - 10, 10, 10, true, 4));
        levelMenu.addButton(new LevelButton(28, 64 - 29 - 10, 10, 10, true, 5));
        levelMenu.addButton(new LevelButton(39, 64 - 29 - 10, 10, 10, true, 6));
        levelMenu.addButton(new LevelButton(17, 64 - 40 - 10, 10, 10, true, 7));
        levelMenu.addButton(new LevelButton(28, 64 - 40 - 10, 10, 10, true, 8));
        levelMenu.addButton(new LevelButton(39, 64 - 40 - 10, 10, 10, true, 9));
        
        scoresMenu = new Menu("scoresMenu.png");
        scoresMenu.addButton(new Button(0, 0, 256, 256, true){
        	public void react(LibgdxTest model){
        		model.currentMenu = model.mainMenu;
        	}
        });
        
        mainMenu = new Menu("mainmenu.png");
        /*
         * Start Game
         */
        mainMenu.addButton(new Button(35, 256 - 90, 175 - 35, 90 - 75, true){
			public void react(LibgdxTest model) {
				Log.d("Button", "Start Game");
				model.menuMode = false;
				loadLevel(1);
			}
        });
        /*
         * Tutorial
         */
        mainMenu.addButton(new Button(60, 256 - 120, 178 - 60, 120 - 103, true){
			public void react(LibgdxTest model) {
				Log.d("Button", "Tutorial");
				model.currentMenu = tutorialMenu;
			}
        });
        /*
         * Levels
         */
        mainMenu.addButton(new Button(60, 256 - 146, 178 - 60, 146 - 129, true){
			public void react(LibgdxTest model) {
				Log.d("Button", "Levels");
				model.currentMenu = model.levelMenu;
			}
        });
        /*
         * Scores
         */
        mainMenu.addButton(new Button(60, 256 - 171, 178 - 60, 171 - 156, true){
			public void react(LibgdxTest model) {
				Log.d("Button", "Scores");
				currentMenu = scoresMenu;
			}
        });
        /*
         * Credits
         */
        mainMenu.addButton(new Button(60, 256 - 197, 178 - 60, 197 - 183, true){
			public void react(LibgdxTest model) {
				Log.d("Button", "Credits");
				currentMenu = creditsMenu;
			}
        });
        currentMenu = mainMenu;
        
        creditsMenu = new Menu("credits.png");
        /*
         * Credits
         */
        creditsMenu.addButton(new Button(60, 256 - 197, 178 - 60, 197 - 183, true){
			public void react(LibgdxTest model) {
				Log.d("Button", "BACK");
				currentMenu = mainMenu;
			}
        });
	}

	public void render() {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if (!menuMode){
			if (!bgMusic.isPlaying()){
				bgMusic.play();
				menuMusic.stop();
			}
			
			if (levelEndWait <= 0){
				if (!player.launchMode){
					player.move();
					for (Planet planet : planets){
						int response = player.influence(planet);
						if (response == 1){
							LibgdxTest.happyMeow.play();
							levelEndWait = maxLevelEndWait;
							doWhatAfterWait = "nextlevel";
							particleSources.add(new ParticleSource(150, 100, 2, new Vector3D(0, 0, 0), new Vector3D(player.x + player.width/2, player.y + player.height/2, 0), 40, "spark_3.png"));
						} else if (response == 2){
							//loadLevel(levelNum);
							LibgdxTest.lostMeow.play();
							levelEndWait = maxLevelEndWait;
							doWhatAfterWait = "restartlevel";
							particleSources.add(new ParticleSource(150, 100, 2, new Vector3D(0, 0, 0), new Vector3D(player.x + player.width/2, player.y + player.height/2, 0), 40, "spark_4.png"));
						} else if (response == 3){
							particleSources.add(new ParticleSource(150, 100, 2, new Vector3D(0, 0, 0), new Vector3D(player.x + player.width/2, player.y + player.height/2, 0), 40, "confetti_3.png"));
						}
					}
				}
			} else if (levelEndWait == 1){
				levelEndWait = 0;
				if (doWhatAfterWait == "nextlevel") {
					loadLevel(levelNum + 1);
				} else if (doWhatAfterWait == "restartlevel") {
					loadLevel(levelNum);
				}
			} else {
				levelEndWait --;
			}
		} else {
			if (!menuMusic.isPlaying()){
				bgMusic.stop();
				menuMusic.play();
			}
		}
		
		Rectangle window = new Rectangle(x, y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
		batch.begin();
		
		bg.draw(batch, x / 2, y / 2);

		this.camera.update(player);
		x = (int) camera.loc.getX();
		y = (int) camera.loc.getY();
		
		for (Planet planet : planets){
			planet.draw(batch, x, y);
			
		}
		
		
		for (Planet planet : planets){
			Vector3D toPlanet = planet.p.subtract(player.p);
			Vector3D wallIntersect = LibgdxTest.vectorIntersectionWithRectangle(player.p, toPlanet, x, y, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Vector3D position = wallIntersect;//.add(toPlanet.scale(20 / toPlanet.lengthSquared()));
			
			if (!window.contains((float) planet.x, (float) planet.y)){
				if (planet.t == Planet.type.WIN){
					glowG.x = position.getX() - glowG.width / 2;
					glowG.y = position.getY() - glowG.height / 2;
					glowG.draw(batch, x, y);
					//batch.draw(texture, (int) x - dx, (int) y - dy, width / 2, height / 2, width, height, 1, 1, (float) rotation);
				} else if (toPlanet.length() < Math.sqrt(Gdx.graphics.getWidth() * Gdx.graphics.getHeight())){
					if (planet.t == Planet.type.HOSTILE){
						position = position.add(toPlanet.scale(1 / 30));
						glowR.x = position.getX() - glowR.width / 2;
						glowR.y = position.getY() - glowR.height / 2;
						glowR.draw(batch, x, y);
					} else if (planet.t == Planet.type.FRIENDLY){
						position = position.add(toPlanet.scale(1 / 30));
						glowB.x = position.getX() - glowB.width / 2;
						glowB.y = position.getY() - glowB.height / 2;
						glowB.draw(batch, x, y);
					}
				}
			
			}
		}
		
		if (!player.getBoundingBox().overlaps(levelBox)) loadLevel(levelNum);
		
		if ((!player.launchMode) && (levelEndWait == 0)){
			particleSources.add(new ParticleSource(30, 2, 1, new Vector3D(0, 0, 0), new Vector3D(player.x + player.width/2, player.y + player.height/2, 0), 15, "red.png"));
		}
		
		player.draw(batch, x, y, (int) win.p.getX(), (int) win.p.getY());
		
		int i = 0;
		while (i < particleSources.size()) {
			if (particleSources.get(i).draw(batch, x, y)) {
				i++;
			} else {
				particleSources.remove(i);
			}
		}
		while (particleSources.size() > 15){
			particleSources.remove(0);
		}
//		Log.d("LibgdxTest", "Number of particle sources: " + particleSources.size());
		
		menuButton.draw(batch, 0, 0);
		
		fuel.width = (int) (Gdx.graphics.getWidth() * player.fuel / player.maxFuel);
		fuel.x = (int) ((Gdx.graphics.getWidth() - fuel.width) / 2);
		fuel.draw(batch, 0, 0);
		
		//MAYBE
		player.draw(batch, x, y);
		
		if (player.fuel < player.maxFuel / 3){
			fuelLow.draw(batch, 0, 0);
		}
		
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
			
		/*
		 * Draw menu if wanted
		 */
		if (menuMode){
			batch.begin();
			
			currentMenu.draw(batch, 0, 0);
			
			batch.end();
		}
	}

	public void resize(int width, int height) {
		stage.setViewport(width, height, true);
	}

	public void dispose() {
		stage.dispose();
	}

	public void pause() {
		
	}
	
	public void resume() {
		
	}
	
	/*
	 * Load a series of levels
	 */
	public boolean loadLevel(int levelNum){
		FileHandle file = Gdx.files.internal(levels);
		BufferedReader reader = new BufferedReader(file.reader());
		
		try {
			for (int i = 0; i < levelNum - 1; i++){
					reader.readLine();
			}
//			Log.v("hello", reader.readLine());
			loadLevel(reader.readLine());
			
			levelBox = new Rectangle();
			
			for (Planet p : planets){
				levelBox.merge(p.getBoundingBox());
				
				int boundary = 500;
				
				levelBox.x -= boundary;
				levelBox.y -= boundary;
				levelBox.width += boundary * 2;
				levelBox.height += boundary * 2;
			}
			
		} catch (IOException e) {
			Log.e("LibgdxTest", "Not enough levels in level file!");
			return false;
		}
		
		this.levelNum = levelNum;
		
		player.launchMode = true;
		
		return true;
	}
	
	/*
	 * Load the level stored in the specified file.
	 */
	public void loadLevel(String levelName){
		Log.d("LibgdxTest", "Loading level " + levelName);
		FileHandle file = Gdx.files.internal(levelName);

//		BufferedReader reader = new BufferedReader(file.reader());
		
//		String line;
		
		planets = new ArrayList<Planet>();
		particleSources = new ArrayList<ParticleSource>();
		
		Random random = new Random();
		
		int cscalar = 100;
		int rscalar = 5;
		
		if(levelName.contains(".png")) { 
			Pixmap levelBMP = new Pixmap(file);
			for (int x = 0; x < levelBMP.getWidth() - 1; x++) {
				for (int y = 0; y < levelBMP.getHeight() - 1; y++) {
					String filename = "";
					Planet.type type = Planet.type.NEUTRAL;

					//				Log.v("LibgdxTest", "hello");

					int rgb = levelBMP.getPixel(x, y);
					int r = (rgb & 0xff000000) >> 24;
				int g = (rgb & 0x00ff0000) >> 16;
			int b = (rgb & 0x0000ff00) >> 8;
			int a = rgb & 0x000000ff;

			int cx = x * cscalar;
			int cy = y * cscalar;
			int cr = a * rscalar;

			if (r == -1) r = 255;

			if (a <= 0) {
				continue;
			} else {
				Log.d("LibgdxTest", "RGBA: (" + r + ", " + g + ", " + b + ", " + a + ")");
			}



			if (r == 0 && g == 0 && b == 0) {
				BlackHole bh = new BlackHole("blackhole.png", new Vector3D(cx, cy, 0), cr, random.nextDouble() * 360);
				planets.add(bh);
				continue;
			} else if (r == 255 && g == 255 && b == 255) {
				player = new Player();
				player.p = new Vector3D(cx, cy, 0);
				continue;
			} else if (g > 1){
				type = Planet.type.WIN;
				filename = "earth.png";
			} else if (b > 0){
				type = Planet.type.FRIENDLY;
				filename = "neptune.png";
			} else if (r > 0){
				type = Planet.type.HOSTILE;
				filename = "mars.png";
			} else {
				type = Planet.type.NEUTRAL;
				filename = "sun.png";
			}

			Log.d("LibgdxTest1", filename + "(" + x + ", " + y + ")");
			Planet p = new Planet(filename, type, new Vector3D(cx, cy, 0), cr, random.nextDouble() * 360);
			planets.add(p);
			if (type == Planet.type.WIN){
				win = p;
			}

				}
			}
		} else if (levelName.contains(".lvl")) {
			BufferedReader reader = new BufferedReader(file.reader());
			String line;
			
			try {
				while ((line = reader.readLine()) != null){
					String[] elements = line.split(" ");
					String filename;
					Planet.type type;
					int x, y, r, m;
					
					if (elements[0].equals("player")) {
						player = new Player();
						player.p = new Vector3D(Integer.parseInt(elements[1]),
												Integer.parseInt(elements[2]),
												0);
						continue;
					} else if (elements[0].equals("blackhole")) {
						BlackHole b = new BlackHole("blackhole.png", 
								new Vector3D(Integer.parseInt(elements[1]),
											Integer.parseInt(elements[2]),
											0),
								Integer.parseInt(elements[3]),
								Integer.parseInt(elements[4]),
								random.nextDouble() * 360);
						planets.add(b);
						continue;
					} else if (elements[0].equals("win")){
						type = Planet.type.WIN;
						filename = "earth.png";
					} else if (elements[0].equals("friendly")){
						type = Planet.type.FRIENDLY;
						filename = "neptune.png";
					} else if (elements[0].equals("hostile")){
						type = Planet.type.HOSTILE;
						filename = "mars.png";
					} else {
						type = Planet.type.NEUTRAL;
						filename = "sun.png";
					}
					
					x = Integer.parseInt(elements[1]);
					y = Integer.parseInt(elements[2]);
					r = Integer.parseInt(elements[3]);
					m = Integer.parseInt(elements[4]);
					
					Planet p = new Planet(filename, type, new Vector3D(x, y, 0), r, m, random.nextDouble() * 360);
					planets.add(p);
					if (type == Planet.type.WIN){
						win = p;
					}
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		player.launchMode = true;
		}
	}
	
	public static Vector3D vectorIntersectionWithRectangle(Vector3D p, Vector3D v, int x, int y, int w, int h){
		Vector3D toReturn = new Vector3D();
		int distanceToPoint = 100000000;
		
		double a = - v.getY();
		double b = v.getX();
		double d = a * p.getX() + b * p.getY();
		
		/*
		 * Check against each side of the rectangle, represented by four point-vector pairs
		 */
		for (Vector3D[] side : new Vector3D[][] {{new Vector3D(x, y, 0), new Vector3D(1, 0, 0)}, {new Vector3D(x, y, 0), new Vector3D(0, 1, 0)},
												 {new Vector3D(x + w, y, 0), new Vector3D(0, 1, 0)}, {new Vector3D(x, y + h, 0), new Vector3D(1, 0, 0)}}){
			Vector3D p1 = side[0];
			Vector3D v1 = side[1];
			
			double a1 = - v1.getY();
			double b1 = v1.getX();
			double d1 = a1 * p1.getX() + b1 * p1.getY();
			
			Vector3D intersection = new Vector3D((b1 * d - b * d1) / (a * b1 - a1 * b),
					  							(a * d1 - a1 * d) / (a * b1 - a1 * b), 0);
			
			int dist = (int) intersection.subtract(p).length();
			
			if (dist < distanceToPoint && intersection.subtract(p).dotProduct(v) > 0){
				toReturn = intersection;
				distanceToPoint = dist;
			}
		}
		
		return toReturn;
	}

}
