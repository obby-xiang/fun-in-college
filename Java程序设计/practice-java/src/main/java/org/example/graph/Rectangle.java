package org.example.graph;

import java.awt.*;
import javax.swing.*;

/**
 * @since 2016-10-31
 */
public class Rectangle extends ClosedFigure{
	public double length,width;
	public Point point2=new Point(),point3=new Point(),point4=new Point();
	public Rectangle(Point p,double length,double width){
		//以左上角的点及长和宽构造对象
		super(p,"长方形");
		this.length=length;
		this.width=width;
		point2.x=point.x+length;
		point2.y=point.y+width;
		point3.x=point.x+length;
		point3.y=point.y;
		point4.x=point.x;
		point4.y=point.y+width;
	}
	public Rectangle(Point p1,Point p2){
		//以不相邻的两点构造对象
		super(p1,"长方形");
		this.length=Math.abs(p2.x-p1.x);
		this.width=Math.abs(p2.y-p1.y);
		point3.x=point.x+length;
		point3.y=point.y;
		point4.x=point.x;
		point4.y=point.y+width;
	}
	public String toString(){
		return this.getClass().getName()+this.shape
				+",四点坐标"+this.point.toString()+","
				+this.point2.toString()+","
				+this.point3.toString()+","
				+this.point4.toString()
				+",长宽"+String.format("%1.2f,%1.2f",length,width);
	}
	public double perimeter(){
		//计算周长
		return 2*(length+width);
	}
	public static double perimeter(double length,double width){
		//静态方法，计算周长
		return 2*(length+width);
	}
	public double area(){
		//计算面积
		return length*width;
	}
	public static double area(double length,double width){
		//静态方法，计算面积
		return length*width;
	}
	public void revolve(int angle){
		//绕长方形中心旋转
		Point p0=new Point(point.x+(point2.x-point.x)/2,point.y+(point2.y-point.y)/2);//矩形中心
		Line line=new Line(p0,point),line2=new Line(p0,point2),line3=new Line(p0,point3),line4= new Line(p0,point4);
		line.revolve(angle);
		line2.revolve(angle);
		line3.revolve(angle);
		line4.revolve(angle);
		point=line.point2;
		point2=line2.point2;
		point3=line3.point2;
		point4=line4.point2;
	}
	public void zoom(int percentage){
		//指定点point缩放
		Line line=new Line(point,point2),line2=new Line(point,point3),line3=new Line(point,point4);//指定顶点缩小
		line.zoom(percentage);
		line2.zoom(percentage);
		line3.zoom(percentage);
		point2=line.point2;
		point3=line2.point2;
		point4=line3.point2;
		length=length*percentage/100;
		width=width*percentage/100;
	}
	public void draw(){
		//画图
		Point p0=new Point(point.x+(point2.x-point.x)/2,point.y+(point2.y-point.y)/2);//矩形中心
		new RectangleFrame(this.toString(),p0,point,point2,point3,point4);
	}
	public boolean contains(Point p){
		//判断点是否在长方形内
		if(new Triangle(point,point2,point3).contains(p)||new Triangle(point,point2,point4).contains(p))
			return true;
		else return false;
	}
	public static void main(String args[]){
		Point p=new Point(100,100);
		Rectangle rectangle=new Rectangle(p,100,150);
		System.out.print("rectangle:");
		rectangle.print();
		rectangle.revolve(60);
		System.out.print("将rectangle绕点"+new Point(rectangle.point.x+(rectangle.point2.x-rectangle.point.x)/2,
				rectangle.point.y+(rectangle.point2.y-rectangle.point.y)/2).toString()+"顺时针旋转60度");
		System.out.print(" rectangle:");
		rectangle.print();
		rectangle.zoom(130);
		rectangle.draw();
		System.out.print("将rectangle指定顶点"+rectangle.point.toString()+"缩放130%");
		System.out.print(" rectangle:");
		rectangle.print();
		Point p1=new Point(139,250),p2=new Point(300,500);
		System.out.println("点p1"+p1.toString()+",点p2"+p2.toString());
		System.out.println("点p1在矩形rectangle内? "+rectangle.contains(p1));
		System.out.println("点p2在矩形rectangle内? "+rectangle.contains(p2));
	}
}
/*
程序运行结果:
rectangle:Rectangle长方形,四点坐标(100.00,100.00),(200.00,250.00),(200.00,100.00),(100.00,250.00),长宽100.00,150.00,周长500.00,面积15000.00
将rectangle绕点(150.00,175.00)顺时针旋转60度 rectangle:Rectangle长方形,四点坐标(189.95,94.20),(110.05,255.80),(239.95,180.80),(60.05,169.20),长宽100.00,150.00,周长500.00,面积15000.00
将rectangle指定顶点(189.95,94.20)缩放130% rectangle:Rectangle长方形,四点坐标(189.95,94.20),(86.08,304.28),(254.95,206.78),(21.08,191.70),长宽130.00,195.00,周长650.00,面积25350.00
点p1(139.00,250.00),点p2(300.00,500.00)
点p1在矩形rectangle内? true
点p2在矩形rectangle内? false
 */
class RectangleFrame extends JFrame{
	private static final long serialVersionUID = 6666780209770825254L;
	private JCanvas canvas;
	private Point p0,point1,point2,point3,point4;
	public RectangleFrame(String title,Point p0,Point p1,Point p2,Point p3,Point p4){
		super(title);
		this.p0=p0;
		point1=p1;
		point2=p2;
		point3=p3;
		point4=p4;
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0,0,dim.width/2,dim.height/2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel jpanel=new JPanel();
		this.getContentPane().add(jpanel);
		this.canvas=new JCanvas();
		this.getContentPane().add(canvas);
		this.setVisible(true);
	}
	class JCanvas extends Canvas{
		private static final long serialVersionUID = 645540858294406722L;
		public JCanvas(){
			super();
		}
		public void paint(Graphics g){
			g.setColor(new Color(0xffff0000));
			g.drawString("."+p0.toString(), (int)p0.x,(int)p0.y);
			g.drawString("."+point1.toString(), (int)point1.x,(int)point1.y);
			g.drawString("."+point2.toString(), (int)point2.x,(int)point2.y);
			g.drawString("."+point3.toString(), (int)point3.x,(int)point3.y);
			g.drawString("."+point4.toString(), (int)point4.x,(int)point4.y);
			g.setColor(new Color(0xff000000));
			g.drawLine((int)point1.x,(int)point1.y,(int)point4.x,(int)point4.y);
			g.drawLine((int)point1.x,(int)point1.y,(int)point3.x,(int)point3.y);
			g.drawLine((int)point2.x,(int)point2.y,(int)point3.x,(int)point3.y);
			g.drawLine((int)point2.x,(int)point2.y,(int)point4.x,(int)point4.y);
		}
	}
}
