package ru.smipealex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 530;

    public static void main(String[] args) throws InterruptedException {
        JFrame jFrame = getFrame();
        jFrame.add(new MyComponent());

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        long currentTime = new Date().getTime();
        long different = TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(currentTime) + 1) - currentTime;

        Thread.sleep(different);

        executorService.scheduleWithFixedDelay(new FrameUpdater(jFrame), 0, 1000, TimeUnit.MILLISECONDS);
    }

    record FrameUpdater(JFrame jFrame) implements Runnable {
        public static int seconds;
        public static int minutes;
        public static int hours;

        @Override
        public void run() {
            long currentDate = new Date().getTime();

            int globalSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(currentDate);

            if (seconds != globalSeconds % 60) {
                seconds = globalSeconds % 60;

                minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(currentDate)) % 60;

                hours = (int) (TimeUnit.MILLISECONDS.toHours(currentDate)) % 12;

                jFrame.repaint();
            }
        }
    }

    static class MyComponent extends JComponent {
        Font font = new Font("Comic Sans MS", Font.PLAIN, 60);

        @Override
        protected void paintComponent(Graphics g) {
            float secondsRotation = 6 * FrameUpdater.seconds - 90;
            float minutesRotation = 6 * FrameUpdater.minutes - 90;
            float hoursRotation = 30 * FrameUpdater.hours;

            Graphics2D graphics2D = (Graphics2D) g;
            graphics2D.setFont(font);

            for(int i = 0;i < 12;i++){
                int rotation = 30 * i - 60;

                double x = Math.cos((double) rotation / 180 * Math.PI) * 220 + 250 - 30 * (i > 9 ? 1: 0.5);
                double y = Math.sin((double) rotation / 180 * Math.PI) * 220 + 250 + 25;

                graphics2D.drawString(String.valueOf(i + 1), (int) x, (int) y);
            }

            Ellipse2D ellipse2D = new Ellipse2D.Float(0, 0, 500, 500);
            graphics2D.draw(ellipse2D);

            Point2D defPoint = new Point(250, 250);
            Point2D secondsPoint = new Point((int) (Math.cos(secondsRotation / 180 * Math.PI) * 175 + 250),
                    (int) (Math.sin(secondsRotation / 180 * Math.PI) * 175 + 250));
            Point2D minutesPoint = new Point((int) (Math.cos(minutesRotation / 180 * Math.PI) * 170 + 250),
                    (int) (Math.sin(minutesRotation / 180 * Math.PI) * 170 + 250));
            Point2D hoursPoint = new Point((int) (Math.cos(hoursRotation / 180 * Math.PI) * 165 + 250),
                    (int) (Math.sin(hoursRotation / 180 * Math.PI) * 165 + 250));

            Line2D secondsArrow = new Line2D.Float(defPoint, secondsPoint);
            Line2D minutesArrow = new Line2D.Float(defPoint, minutesPoint);
            Line2D hoursArrow = new Line2D.Float(defPoint, hoursPoint);

            graphics2D.setColor(Color.GRAY);

            graphics2D.setStroke(new BasicStroke(2));
            graphics2D.draw(secondsArrow);
            graphics2D.setStroke(new BasicStroke(4));
            graphics2D.draw(minutesArrow);
            graphics2D.setStroke(new BasicStroke(6));
            graphics2D.draw(hoursArrow);
            graphics2D.setStroke(new BasicStroke(8));

            graphics2D.setColor(Color.RED);

            graphics2D.draw(new Line2D.Float(defPoint, defPoint));
        }
    }

    private static JFrame getFrame(){
        JFrame jFrame = new JFrame();
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setBounds(toolkit.getScreenSize().width/2-WINDOW_WIDTH/2,toolkit.getScreenSize().height/2- WINDOW_HEIGHT /2, WINDOW_WIDTH, WINDOW_HEIGHT);
        jFrame.setTitle("My Swing Application");
        return jFrame;
    }
}
