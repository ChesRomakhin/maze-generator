package ru.chesromakhin.mazegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Generator {

	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	
	public static final int DIRECTION_COUNT = 4;
	
	public static final int[][] VECTOR = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
	
	private int width = 200;
	private int height = 200;
	
	private double switchDirectionThresold = 0.33;
	private double switchDirectionCwThresold = 0.5;
	
	private int[][] board;
	
	private Random random;
	
	private Corridor root;
	private Corridor current;
	
	private List<Room> rooms = new ArrayList<>();
	
	public Generator(int width, int height) {
		this.width = width;
		this.height = height;
		
		board = new int[width][height];
		random = new Random();
		
		/*for (int i = 40; i < 60; i++) {
			for (int j = 40; j < 60; j++) {
				board[i][j] = 1;
			}
		}*/
	}
	
	public Generator() {
		board = new int[width][height];
		random = new Random();
	}

	public double getSwitchDirectionThresold() {
		return switchDirectionThresold;
	}

	public double getSwitchDirectionCwThresold() {
		return switchDirectionCwThresold;
	}

	public boolean canTunnel(int x, int y, int direction) {
		boolean result = true;
		
		result = (x != 0 && y != 0 && x != width - 1 && y != height - 1);
		
		if (result) {
			int sum = 0;
			switch (direction) {
			case LEFT:
				sum = board[x + 1][y] + board[x + 1][y + 1] + board[x + 1][y - 1] + 
					board[x][y + 1] + board[x][y - 1];
				break;
			case RIGHT:
				sum = board[x - 1][y] + board[x - 1][y + 1] + board[x - 1][y - 1] +
					board[x][y + 1] + board[x][y - 1];
				break;
			case UP:
				sum = board[x + 1][y + 1] + board[x - 1][y + 1] + board[x][y + 1] + 
					board[x + 1][y] + board[x - 1][y];
				break;
			case DOWN:
				sum = board[x + 1][y - 1] + board[x - 1][y - 1] + board[x][y - 1] + 
				board[x + 1][y] + board[x - 1][y];
				break;
			}
			/*switch (direction) {
			case LEFT:
				sum = board[x + 1][y] + 
					board[x][y + 1] + board[x][y - 1];
				break;
			case RIGHT:
				sum = board[x - 1][y] +
					board[x][y + 1] + board[x][y - 1];
				break;
			case UP:
				sum = board[x][y + 1] + 
					board[x + 1][y] + board[x - 1][y];
				break;
			case DOWN:
				sum = board[x][y - 1] + 
				board[x + 1][y] + board[x - 1][y];
				break;
			}*/
			if (sum > 0) {
				result = false;
			}
		}
		
		return result;
	}
	
	public void depthFirstSearch(int x, int y, int direction) {
		
		if (canTunnel(x, y, direction)) {
			board[x][y] = 1;
		} else {
			return;
		}
		
		int newDirection = direction;
		int fDirection = direction;
		int cwDirection = (direction + 1) % DIRECTION_COUNT;
		int ccwDirection = (direction + DIRECTION_COUNT - 1) % DIRECTION_COUNT;
		
		float r = random.nextFloat();  
		System.out.println(r);
		
		if (r > switchDirectionThresold) {
			r = random.nextFloat();
			System.out.println(r);
			
			if (r > switchDirectionCwThresold) {
				newDirection = cwDirection;
				cwDirection = -1;
			} else {
				newDirection = ccwDirection;
				ccwDirection = -1;
			}
		} else {
			fDirection = -1;
		}
		
		switch (newDirection) {
		case LEFT:
			depthFirstSearch(x + 1, y, newDirection);
			break;
		case RIGHT:
			depthFirstSearch(x - 1, y, newDirection);
			break;
		case UP:
			depthFirstSearch(x, y + 1, newDirection);
			break;
		case DOWN:
			depthFirstSearch(x, y - 1, newDirection);
			break;
		}
		
		switch (fDirection) {
		case LEFT:
			depthFirstSearch(x + 1, y, fDirection);
			break;
		case RIGHT:
			depthFirstSearch(x - 1, y, fDirection);
			break;
		case UP:
			depthFirstSearch(x, y + 1, fDirection);
			break;
		case DOWN:
			depthFirstSearch(x, y - 1, fDirection);
			break;
		}
		
		switch (cwDirection) {
		case LEFT:
			depthFirstSearch(x + 1, y, cwDirection);
			break;
		case RIGHT:
			depthFirstSearch(x - 1, y, cwDirection);
			break;
		case UP:
			depthFirstSearch(x, y + 1, cwDirection);
			break;
		case DOWN:
			depthFirstSearch(x, y - 1, cwDirection);
			break;
		}
		
		switch (ccwDirection) {
		case LEFT:
			depthFirstSearch(x + 1, y, ccwDirection);
			break;
		case RIGHT:
			depthFirstSearch(x - 1, y, ccwDirection);
			break;
		case UP:
			depthFirstSearch(x, y + 1, ccwDirection);
			break;
		case DOWN:
			depthFirstSearch(x, y - 1, ccwDirection);
			break;
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getTile(int x, int y) {
		return board[x][y];
	}
	
	public int getRandom(int bound) {
		return random.nextInt(bound);
	}
	
	public double getRandom() {
		return random.nextDouble();
	}
	
	public void write(Corridor c) {
		board[c.getX()][c.getY()] = 1;
	}
	
	public void generateNonRecursive() {
		root = new Corridor(this, null, 1, 1, Generator.LEFT);
		current = root;
		
		while (current != null) {
			current = generateStep(current);
		}
	}
	
	public Corridor generateStep(Corridor current) {		
		if (current.isEnd()) {
			return current.getParent();
		}
		
		return current.tunnel();
	}
	
	public void initStepByStepGeneration() {
		board = new int[width][height];
		
		root = new Corridor(this, null, 1, 1, Generator.LEFT);
		current = root;
	}
	
	public void randomInitStepByStepGeneration() {
		root = new Corridor(this, null, random.nextInt(width - 1) + 1, random.nextInt(height - 1) + 1, random.nextInt(DIRECTION_COUNT));
		current = root;
	}
	
	public boolean generateStepByStep() {
		if (current != null) {
			current = generateStep(current);
		}
		
		return current != null;
	}
	
	public void placeRooms() {
		int n = 0;
		while (n < 50) {
			int x = (random.nextInt(width / 10 - 1) + 1) * 10;
			int y = (random.nextInt(height / 10 - 1) + 1) * 10;
			int h = random.nextInt(5) + 5;
			int w = random.nextInt(5) + 5;
			
			if (board[x][y] == 1) {
				continue;
			}
			
			for (int i = x; i < x + h; i++) {
				for (int j = y; j < y + w; j++) {
					board[i][j] = 1;
				}
			}
			
			rooms.add(new Room(this, x, y, h, w));
			n++;
		}
	}
	
	public void placeConnectors() {
		for (int i = 0; i < rooms.size(); i++) {
			rooms.get(i).placeConnectors(4);
		}
	}
	
	public boolean removeDeadEnds() {
		boolean result = false;
		
		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				if (board[x][y] == 1) {
					boolean deadEnd = (board[x + 1][y] + board[x - 1][y] + board[x][y + 1] + board[x][y - 1]) == 1;
					
					if (deadEnd) {
						board[x][y] = 0;
						result |= deadEnd;
					}
				}
			}
		}
		
		return result;
	}
	
}
