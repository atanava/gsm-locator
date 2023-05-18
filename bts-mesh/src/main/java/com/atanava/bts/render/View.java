package com.atanava.bts.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;


public class View extends JPanel {

    //Массив для всех точек
    private final ArrayList<Point> points = new ArrayList<>();
    //Массив для всех треугольников
    private final ArrayList<Polygon> triangles = new ArrayList<>();

    //Линия и ее стиль
    private final Stroke stroke = new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    //Передаем в конструктор размер окна, покдлючаем мышку
    public View() {
        setPreferredSize(new Dimension(1280, 720));
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    //Отрисовываем на экране
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPolygon((Graphics2D) g);
        drawTriangles((Graphics2D) g);
    }

    //функия создания линии для многоугольника по 2ум точках и устанвока цвета для линии
    private void drawPolygon(Graphics2D g) {
        g.setStroke(stroke);
        for (int p = 0; p < points.size(); p++) { // Пробегаемся по всем точкам
            Point p1 = points.get(p % points.size());
            Point p2 = points.get((p + 1) % points.size());
            g.setColor(Color.BLUE);
            g.drawLine(p1.x, p1.y, p2.x, p2.y); //Линия между двумя точками
        }
    }

    //контур и заполненеие треугольников
    private void drawTriangles(Graphics2D g) {
        g.setStroke(stroke);

        //Проход по всем элементам массива треугольников
        for(int index = 0; index < triangles.size(); index++) {
            Polygon triangle = triangles.get(index);

            //Контур
            g.setColor(Color.BLUE);
            g.drawPolygon(triangle);

            //Заливка
            g.setColor(new Color((int)(Math.random() * 0x1000000)));
            g.fillPolygon(triangle);
        }

    }


    //Проверка действительности треугольника
    public boolean validTriangle(Polygon triangle, Point p1, Point p2, Point p3, List<Point> points) {
        for (Point p : points) {
            if (p != p1 && p != p2 && p != p3 && triangle.contains(p)) {
                return false;
            }
        }
        return true;
    }

    //Инициализация окна
    public static void main(String[] args) {
        View view = new View();
        JFrame frame = new JFrame();
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        view.requestFocus();
    }

    //Обработка мышки
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                points.add(new Point(e.getX(), e.getY())); //Добавление в массив точки, получение координат x, y
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
//                triangulatePolygon(); // Вызов триангляции
            }
            else if (SwingUtilities.isMiddleMouseButton(e)) {
                triangles.clear(); //Очищаем все элементы массива
            }
            repaint();
        }

    }


}