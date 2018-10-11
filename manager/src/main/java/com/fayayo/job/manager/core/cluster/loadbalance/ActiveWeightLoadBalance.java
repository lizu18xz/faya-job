package com.fayayo.job.manager.core.cluster.loadbalance;

import com.fayayo.job.core.extension.SpiMeta;
import com.fayayo.job.core.transport.protocol.request.RequestPacket;
import com.fayayo.job.manager.core.cluster.Endpoint;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * "低并发优化" 负载均衡
 * 
 * <pre>
 * 		1） 低并发度优先： referer的某时刻的call数越小优先级越高 
 * 
 * 		2） 低并发referer获取策略：
 * 				由于Referer List可能很多，比如上百台，如果每次都要从这上百个Referer或者最低并发的几个，性能有些损耗，
 * 				因此 random.nextInt(list.size()) 获取一个起始的index，然后获取最多不超过MAX_REFERER_COUNT的
 * 				状态是isAvailable的referer进行判断activeCount.
 * </pre>
 * 
 * @author motan
 */
@SpiMeta(name = "activeWeight")
public class ActiveWeightLoadBalance extends AbstractLoadBalance {

    @Override
    protected Endpoint doSelect(RequestPacket request) {
        List<Endpoint> endpoints = getEndpoints();

        int refererSize = endpoints.size();
        int startIndex = ThreadLocalRandom.current().nextInt(refererSize);
        int currentCursor = 0;
        int currentAvailableCursor = 0;

        Endpoint endpoint = null;

        while (currentAvailableCursor < MAX_REFERER_COUNT && currentCursor < refererSize) {
            Endpoint temp = endpoints.get((startIndex + currentCursor) % refererSize);
            currentCursor++;

            currentAvailableCursor++;

            if (endpoint == null) {
                endpoint = temp;
            } else {
                if (compare(endpoint, temp) > 0) {
                    endpoint = temp;
                }
            }
        }

        return endpoint;
    }

    @Override
    protected void doSelectToHolder(RequestPacket request, List<Endpoint> refersHolder) {
        List<Endpoint> endpoints = getEndpoints();

        int refererSize = endpoints.size();
        int startIndex = ThreadLocalRandom.current().nextInt(refererSize);
        int currentCursor = 0;
        int currentAvailableCursor = 0;

        while (currentAvailableCursor < MAX_REFERER_COUNT && currentCursor < refererSize) {
            Endpoint temp = endpoints.get((startIndex + currentCursor) % refererSize);
            currentCursor++;


            currentAvailableCursor++;

            refersHolder.add(temp);
        }

        Collections.sort(refersHolder, new LowActivePriorityComparator());
    }

    private int compare(Endpoint endpoint1, Endpoint endpoint2) {
        return endpoint1.activeCount() - endpoint2.activeCount();
    }

    static class LowActivePriorityComparator<T> implements Comparator<Endpoint> {
        @Override
        public int compare(Endpoint endpoint1, Endpoint endpoint2) {
            return endpoint1.activeCount() - endpoint2.activeCount();
        }
    }

}
