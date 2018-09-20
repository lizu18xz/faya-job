package com.fayayo.job.manager.core.cluster;

import java.util.concurrent.atomic.AtomicInteger;

public class Endpoint {
    private final String host;
    private final int port;
    private Integer weight;

    //统计当前时间地址被调用的次数
    private AtomicInteger activeCount = new AtomicInteger(0);


    public void incrActiveCount() {
        activeCount.incrementAndGet();
    }

    public void decrActiveCount() {
        activeCount.decrementAndGet();
    }


    public int activeCount() {
        return activeCount.get();
    }


    public Endpoint(String host, int port) {
        this.host = host;
        this.port = port;
        this.weight = 1;//默认不给是1
    }

    public Endpoint(String host, int port, Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Endpoint)) {
            return false;
        }
        Endpoint other = (Endpoint) o;
        return other.host.equals(this.host) && other.port == this.port;
    }

    public int hashCode() {
        return host.hashCode() + port;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                ", activeCount=" + activeCount() +
                '}';
    }

}
