
package com.fayayo.job.manager.core.cluster.loadbalance;


import com.fayayo.job.common.exception.CommonException;
import com.fayayo.job.manager.core.cluster.Endpoint;
import com.fayayo.job.manager.core.cluster.LoadBalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {
    public static final int MAX_REFERER_COUNT = 10;

    private volatile List<Endpoint> endpoints;

    @Override
    public void onRefresh(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }


    @Override
    public Endpoint select() {
        List<Endpoint>endpoints = this.endpoints;
        if (endpoints == null) {
            throw new CommonException(999999,this.getClass().getSimpleName() + " No available referers for call request:");
        }
        Endpoint ref = null;
        if (endpoints.size() > 1) {
            ref = doSelect();
        } else if (endpoints.size() == 1) {
            ref = endpoints.get(0);
        }

        if (ref != null) {
            return ref;
        }
        throw new CommonException(999999,this.getClass().getSimpleName() + " No available referers for call request:");
    }

    /**
       *@描述 根据规则获取服务集合
     */
    @Override
    public void selectToHolder(List<Endpoint> endpointHolder) {

        List<Endpoint>endpoints = this.endpoints;

        if (endpoints == null) {
            throw new CommonException(999999,this.getClass().getSimpleName() + " No available endpoints for call : endpoints_size= 0");
        }
        if (endpoints.size() > 1) {
            doSelectToHolder(endpointHolder);
        } else if (endpoints.size() == 1) {
            endpointHolder.add(endpoints.get(0));
        }
        if (endpointHolder.isEmpty()) {
            throw new CommonException(999999,this.getClass().getSimpleName() + " No available referers for call : referers_size="
                    + endpointHolder.size());
        }
    }


    protected List<Endpoint>getEndpoints() {
        return endpoints;
    }

    protected abstract Endpoint doSelect();

    protected abstract void doSelectToHolder(List<Endpoint> refersHolder);

}
