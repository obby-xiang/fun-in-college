package org.example.graph;

/**
 * 封闭图形
 *
 * @author obby-xiang
 * @since 2016-10-31
 */
public abstract class ClosedFigure extends Figure {
    protected String shape;

    protected ClosedFigure(Point point, String shape) {
        super(point);
        this.shape = shape;
    }

    protected ClosedFigure() {
        this(new Point(), "未知");
    }

    public abstract double perimeter();

    public abstract double area();

    public abstract boolean contains(Point p);

    public void print() {
        System.out.println(this + ",周长" + String.format("%1.2f,面积%1.2f", this.perimeter(), this.area()));
    }
}
