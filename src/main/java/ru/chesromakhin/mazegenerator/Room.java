package ru.chesromakhin.mazegenerator;

import java.util.ArrayList;
import java.util.List;


public class Room {
	
	private int x;
	private int y;
	private int h;
	private int w;
	
	private Generator generator;
	
	public Room(Generator generator, int x, int y, int h, int w) {
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
		this.generator = generator;
	}
	
	public void placeConnectors(int n) {
		List<Corridor> connectors = getConnectors();
		
		for (int i = 0; i < n; i++) {
			if (connectors.isEmpty()) {
				break;
			}

			int number = generator.getRandom(connectors.size());
			
			generator.write(connectors.get(number));
			connectors.remove(number);
		}
	}
	
	public List<Corridor> getConnectors() {
		List<Corridor> result = new ArrayList<>();
		
		for (int i = 0; i < h; i++) {
			if (x - 2 >= 0 && generator.getTile(x - 1, y + i) == 0 && generator.getTile(x - 2, y + i) == 1) {
				result.add(new Corridor(generator, x - 1, y + i));
			}
			if (x + w + 1 < generator.getWidth() && generator.getTile(x + w, y + i) == 0 && generator.getTile(x + w + 1, y + i) == 1) {
				result.add(new Corridor(generator, x + w, y + i));
			}
		}
		
		for (int i = 0; i < w; i++) {
			if (y - 2 >= 0 && generator.getTile(x + i, y - 1) == 0 && generator.getTile(x + i, y - 2) == 1) {
				result.add(new Corridor(generator, x + i, y - 1));
			}
			if (y + h + 1 < generator.getHeight() && generator.getTile(x + i, y + h) == 0 && generator.getTile(x + i, y + h + 1) == 1) {
				result.add(new Corridor(generator, x + i, y + h + 1));
			}
		}
		
		return result;
	}

}
