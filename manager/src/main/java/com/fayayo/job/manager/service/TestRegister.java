package com.fayayo.job.manager.service;

import com.fayayo.job.core.register.ServiceRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author dalizu on 2018/12/5.
 * @version v1.0
 * @desc
 */
@Service("serviceRegistry")
public class TestRegister implements ServiceRegistry {
    @Override
    public void register(String serviceName, String serviceAddress) {

    }

    @Override
    public List<String> discover(String executorName) {
        return null;
    }

    @Override
    public String getData(String path) {
        return null;
    }
}
