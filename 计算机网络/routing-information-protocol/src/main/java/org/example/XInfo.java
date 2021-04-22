package org.example;

/**
 * class for the item of routing table
 *
 * @author obby-xiang
 * @since 2017-11-12
 */
public class XInfo {

    private String destination;    // 目的网络
    private int distance;    // 距离
    private String nextHop;    // 下一跳路由器

    public XInfo(final String destination, final int distance, final String nextHop) {
        set(destination, distance, nextHop);
    }

    public XInfo(final XInfo info) {
        set(info.destination, info.distance, info.nextHop);
    }

    public synchronized void set(final String destination, final int distance, final String nextHop) {    // 设置
        setDestination(destination);
        setDistance(distance);
        setNextHop(nextHop);
    }

    public String getDestination() {    // 获得目的网络
        return this.destination;
    }

    public synchronized void setDestination(final String destination) {    // 设置目的网络
        this.destination = destination;
    }

    public int getDistance() {    // 获得距离
        return this.distance;
    }

    public synchronized void setDistance(final int distance) {    // 设置距离
        this.distance = distance;
    }

    public String getNextHop() {    // 获得下一跳路由器
        return this.nextHop;
    }

    public synchronized void setNextHop(final String nextHop) {    // 设置下一跳路由器
        this.nextHop = nextHop == null ? "null" : nextHop;
    }
}
