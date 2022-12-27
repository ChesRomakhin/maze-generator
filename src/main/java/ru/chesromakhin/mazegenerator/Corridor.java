package ru.chesromakhin.mazegenerator;

import java.util.ArrayList;
import java.util.List;

public class Corridor {

  private int x;
  private int y;
  private int direction;

  private Corridor parent = null;
  private List<Corridor> childs = new ArrayList<>(3);
  private List<Integer> directions = new ArrayList<>(3);

  private Generator generator;

  public Corridor(Generator generator, int x, int y) {
    this.generator = generator;
    this.x = x;
    this.y = y;
  }

  public Corridor(Generator generator, Corridor parent, int x, int y, int direction) {
    this.generator = generator;
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.parent = parent;

    detectDirections();

    generator.write(this);
  }

  public boolean isRoot() {
    return parent == null;
  }

  public boolean isEnd() {
    return directions.isEmpty();
  }

  public Corridor tunnel() {
    Corridor result = null;

    if (!directions.isEmpty()) {
      //int number = generator.getRandom(directions.size());
      int number = pickDirection();
      int direction = directions.get(number);

      directions.remove(number);

      if (generator.canTunnel(x + Generator.VECTOR[direction][0], y + Generator.VECTOR[direction][1], direction)) {
        result = new Corridor(generator, this, x + Generator.VECTOR[direction][0], y + Generator.VECTOR[direction][1], direction);
        childs.add(result);
      } else {
        if (isEnd()) {
          result = parent;
        } else {
          result = this;
        }
      }
    }

    return result;
  }

  public void detectDirections() {
    int fDirection = direction;
    int cwDirection = (direction + 1) % Generator.DIRECTION_COUNT;
    int ccwDirection = (direction + Generator.DIRECTION_COUNT - 1) % Generator.DIRECTION_COUNT;

    if (generator.canTunnel(x + Generator.VECTOR[fDirection][0], y + Generator.VECTOR[fDirection][1], fDirection)) {
      directions.add(fDirection);
    }
    if (generator.canTunnel(x + Generator.VECTOR[cwDirection][0], y + Generator.VECTOR[cwDirection][1], cwDirection)) {
      directions.add(cwDirection);
    }
    if (generator.canTunnel(x + Generator.VECTOR[ccwDirection][0], y + Generator.VECTOR[ccwDirection][1], ccwDirection)) {
      directions.add(ccwDirection);
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Corridor getParent() {
    return parent;
  }

  public int pickDirection() {
    if (directions.size() > 1) {
      double d = generator.getRandom();
      double d2 = generator.getRandom();
      if (d > generator.getSwitchDirectionThresold()) {
        if (directions.size() > 2) {
          if (d2 > generator.getSwitchDirectionCwThresold()) {
            return 2;
          } else {
            return 1;
          }
        } else {
          return 1;
        }
      } else {
        return 0;
      }
    } else {
      return 0;
    }
  }

}
