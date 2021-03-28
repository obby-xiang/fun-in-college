package org.example.graph;

import java.awt.*;
import javax.swing.*;
import java.text.DecimalFormat;

/**
 * @since 2016-10-31
 */
public class Line extends Figure{
	public Point point2;
	public static DecimalFormat df=new DecimalFormat("0.00");//小数格式
	public Line(Point point1,Point point2){
		//以两点构造对象
		point =point1;
		this.point2=point2;
	}
	public Line(){
		//无参数的构造方法
		point=new Point();
		point2=new Point(1,1);
	}
	public String toString(){
		//返回直线信息
		return "Line,直线 两点坐标("+df.format(point.x)+","+df.format(point.y)+
				"),("+df.format(point2.x)+","+df.format(point2.y)+") "+"长度 "+df.format(this.length());
	}
	public double length(){
		//计算长度
		return length(point,point2);
	}
	public static double length(Point point1,Point point2){
		//静态方法，计算长度
		double a=point1.x-point2.x,b=point1.y-point2.y;
		return Math.sqrt(a*a+b*b);
	}
	public boolean contains(Point p){
		//判断定点是否在直线上
		double x,y;
		x=point2.x-point.x;
		y=point2.y-point.y;
		if(p.x==point.x&&p.y==point.y) return true;//p与point重合
		else if(p.x>=(point.x<point2.x?point.x:point2.x)&&p.x<=(point.x>point2.x?point.x:point2.x)
					&&p.y>=(point.y<point2.y?point.y:point2.y)&&p.y<=(point.y>point2.y?point.y:point2.y)){
			//运用向量等数学知识求解
			if(x==0.0||(x!=0.0&&(p.y-point.y)/(p.x-point.x)==y/x)) return true;
			else return false;
		}
		else return false;
	}
	public Point intersects(Line line){
		//判断两条直线是否相交
		double x0,y0,x,y,a1,b1,c1,a2,b2,c2;
		Point p1=line.point,p2=line.point2;
		x0=point2.x-point.x;
		y0=point2.y-point.y;
		x=p2.x-p1.x;
		y=p2.y-p1.y;
		//运用数学知识求解
		if(x0==0.0){
			//直线平行y轴
			a1=1.0;
			b1=0.0;
			c1=-point.x;
		}
		else if(y0==0.0){
			//直线平行x轴
			a1=0.0;
			b1=1.0;
			c1=-point.y;
		}
		else{
			//直线不与坐标轴平行
			a1=1.0;
			b1=-1.0/(y0/x0);
			c1=-(a1*point.x+b1*point.y);
		}
		if(x==0.0){
			//直线line平行y轴
			a2=1.0;
			b2=0.0;
			c2=-p1.x;
		}
		else if(y==0.0){
			//直线line平行x轴
			a2=0.0;
			b2=1.0;
			c2=-p1.y;
		}
		else{
			//直线line不与坐标轴平行
			a2=1.0;
			b2=-1.0/(y/x);
			c2=-(a2*p1.x+b2*p1.y);
		}
		if(a1==a2&&b1==b2){
			//两直线共线
			if(line.contains(point)) return point;
			else if(line.contains(point2)) return point2;
			else return null;
		}
		else if(a1==0.0){
			//直线平行x轴
			Point p=new Point(-(c2+b2*point.y)/a2,point.y);
			if(this.contains(p)) return p;
			else return null;
		}
		else if(a2==0.0){
			//直线line平行x轴
			Point p=new Point(-(c1+b1*point.y)/a1,p1.y);
			if(this.contains(p)) return p;
			else return null;
		}
		else{
			Point p=new Point(-c1-b1*(c2-c1)/(b1-b2),(c2-c1)/(b1-b2));
			if(line.contains(p)==true) return p;
			else return null;
		}
	}
	public double distance(Point p){
		//点到线段的距离
		if(this.contains(p)) return 0.0;//点在直线上
		else{
			double a=length(p,point),b=length(point,point2),c=length(point2,p);
			if(a*a+b*b-c*c<=0||b*b+c*c-a*a<=0) return (a<c?a:c);//若点与两端点的连线与直线成钝角，则点到该直线的距离是点到端点的最短距离
			else if(point.y==point2.y) return Math.abs(p.y-point.y);//直线平行x轴
			else if(point.x==point2.x) return Math.abs(p.x-point.x);//直线平行y轴
			else{
				double k=(point.y-point2.y)/(point.x-point2.x),m=point.y-k*point.x;
				return Math.abs(k*p.x-p.y+m)/Math.sqrt(k*k+1);
			}
		}
	}
	public void revolve(int angle){
		//旋转
		double x,y,k;
		//运用平面向量知识求解
		x=point2.x-point.x;
		y=point2.y-point.y;
		k=angle*Math.PI/180;
		point2.x=point.x+x*Math.cos(k)-y*Math.sin(k);
		point2.y=point.y+x*Math.sin(k)+y*Math.cos(k);
	}
	public void zoom(int percentage){
		//缩小
		double x,y;
		//运用平面向量知识求解
		x=point2.x-point.x;
		y=point2.y-point.y;
		point2.x=point.x+x*percentage/100;
		point2.y=point.y+y*percentage/100;
	}
	public void draw(){
		//画图
		new LineFrame(this.toString(),point,point2);
	}
	public static void main(String args[]){
		Line line=new Line(new Point(90,206),new Point(200,300));
		Line line2=new Line(new Point(250,150),new Point(85,360));
		line.draw();
		line2.draw();
		System.out.println("line:"+line.toString());
		line.zoom(120);
		System.out.println("将line指定端点"+line.point.toString()+"缩放120% line:"+line.toString());
		System.out.println("line2:"+line2.toString());
		line2.revolve(10);
		System.out.println("将line2绕点"+line.point.toString()+"顺时针旋转10度    line2:"+line.toString());
		System.out.println("两直线是否相交? "+(line.intersects(line2)==null?"否":"是，交点"+line.intersects(line2)));
		Point p1=new Point(90,206),p2=new Point(20,20);
		System.out.println("p1"+p1.toString()+",p2"+p2.toString()+
				"\n点p1是否在直线line上? "+line.contains(p1)+
				"\n点p2是否在直线line上? "+line.contains(p2));
		System.out.println("点p2到line的距离:"+String.format("%.2f", line.distance(p2)));
	}
}
/*
程序运行结果:
line:Line,直线 两点坐标(90.00,206.00),(200.00,300.00) 长度 144.69
将line指定端点(90.00,206.00)缩放120% line:Line,直线 两点坐标(90.00,206.00),(222.00,318.80) 长度 173.63
line2:Line,直线 两点坐标(250.00,150.00),(85.00,360.00) 长度 267.07
将line2绕点(90.00,206.00)顺时针旋转10度    line2:Line,直线 两点坐标(90.00,206.00),(222.00,318.80) 长度 173.63
两直线是否相交? 是，交点(139.87,248.62)
p1(90.00,206.00),p2(20.00,20.00)
点p1是否在直线line上? true
点p2是否在直线line上? false
点p2到line的距离:198.74
 */
class LineFrame extends JFrame{
	private static final long serialVersionUID = -5257752033597004061L;
	private JCanvas canvas;
	private Point point1,point2;
	public LineFrame(String title,Point p1,Point p2){
		super(title);
		point1=p1;
		point2=p2;
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();//获得屏幕分辨率
		this.setBounds(0,0,dim.width/2,dim.height/2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel jpanel=new JPanel();
		this.getContentPane().add(jpanel);
		this.canvas=new JCanvas();
		this.getContentPane().add(canvas);
		this.setVisible(true);
	}
	class JCanvas extends Canvas{
		private static final long serialVersionUID = 1687759079138646291L;
		public JCanvas(){
			super();
		}
		public void paint(Graphics g){
			g.setColor(new Color(0xffff0000));//红色
			g.drawString("."+point1.toString(), (int)point1.x,(int)point1.y);
			g.drawString("."+point2.toString(), (int)point2.x,(int)point2.y);
			g.setColor(new Color(0xff000000));//黑色
			g.drawLine((int)point1.x,(int)point1.y,(int)point2.x,(int)point2.y);
		}
	}
}