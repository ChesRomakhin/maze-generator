package ru.chesromakhin.mazegenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main {

  public static Generator generator0;
  public static Generator generator1;
  public static Generator generator2;

  public static void main(String[] args) {
    generator0 = new Generator(200, 200);
    generator1 = new Generator(200, 200);
    generator2 = new Generator(200, 200);
    // generator.generateNonRecursive();
    // generator.depthFirstSearch(1, 1, ru.chesromakhin.mazegenerator.Generator.UP);

    /*
     * for (int j = generator.getHeight() - 1; j > -1; j--) { for (int i =
     * 0; i < generator.getWidth(); i++) {
     * System.out.print(generator.getTile(i, j)); } System.out.println(); }
     */

    JFrame f = new JFrame("Maze");

    JPanel p = new JPanel() {

      public Generator generator0 = Main.generator0;
      public Generator generator1 = Main.generator1;
      public Generator generator2 = Main.generator2;

      int squareSize = 4;

      @Override
      public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        float rd, gr, bl;

        for (int j = generator0.getHeight() - 1; j > -1; j--) {
          for (int i = 0; i < generator0.getWidth(); i++) {
            g2d.setColor(Color.BLACK);

            rd = 0;
            gr = 0;
            bl = 0;

            if (generator0.getTile(i, j) == 1) {
              rd = 1;
            }
            if (generator1.getTile(i, j) == 1) {
              gr = 1;
            }
            if (generator2.getTile(i, j) == 1) {
              bl = 1;
            }

            g2d.setColor(new Color(rd, rd, rd));

            g2d.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
          }
        }
      }
    };

    f.add(p);

    f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    f.setSize(500, 500);
    f.setLocationRelativeTo(null);

    f.setVisible(true);

    final long time = 1;

    Thread t = new Thread(() -> {
      while (true) {
        generator0.initStepByStepGeneration();
        generator0.placeRooms();

        while (generator0.generateStepByStep()) {
          try {
            Thread.sleep(time);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        generator0.placeConnectors();

        while (generator0.removeDeadEnds()) {
          try {
            Thread.sleep(time);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    Thread t1 = new Thread(() -> {
      while (true) {
        generator1.initStepByStepGeneration();

        while (generator1.generateStepByStep()) {
          try {
            Thread.sleep(time);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        // generator.placeConnectors();

        while (generator1.removeDeadEnds()) {
          try {
            Thread.sleep(time);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    Thread t2 = new Thread(() -> {
      while (true) {
        generator2.initStepByStepGeneration();

        while (generator2.generateStepByStep()) {
          try {
            Thread.sleep(time);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        // generator.placeConnectors();

        while (generator2.removeDeadEnds()) {
          try {
            Thread.sleep(time);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    t.start();
//        t1.start();
//        t2.start();

    Timer timer = new Timer();
    timer.schedule(new

                       TimerTask() {

                         @Override
                         public void run() {
                           p.repaint();
                         }
                       }, 1, 1);
  }

  public static double[] a = {0.278393, 0.230389, 0.000972, 0.078108};

  public static double errorFunction(double x) {
    double result = 1;
    double downPart = 1 + a[0] * x + a[1] * x * x + a[2] * x * x * x + a[3] * x * x * x * x;

    return result - 1 / (downPart * downPart * downPart * downPart);
  }

}
