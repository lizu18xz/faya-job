package com.fayayo.job.core.swicher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author dalizu on 2018/9/3.
 * @version v1.0
 * @desc
 */
public class LocalSwitcherService implements SwitcherService {

    private static ConcurrentMap<String, Switcher> switchers = new ConcurrentHashMap<String, Switcher>();

    private ConcurrentHashMap<String, List<SwitcherListener>> listenerMap = new ConcurrentHashMap<>();

    public static Switcher getSwitcherStatic(String name) {
        return switchers.get(name);
    }

    @Override
    public Switcher getSwitcher(String name) {
        return switchers.get(name);
    }

    @Override
    public List<Switcher> getAllSwitchers() {
        return new ArrayList<Switcher>(switchers.values());
    }

    public static void putSwitcher(Switcher switcher) {
        if (switcher == null) {
            throw new RuntimeException("LocalSwitcherService addSwitcher Error: switcher is null");
        }

        switchers.put(switcher.getName(), switcher);
    }

    @Override
    public void initSwitcher(String switcherName, boolean initialValue) {
        setValue(switcherName, initialValue);
    }

    @Override
    public boolean isOpen(String switcherName) {
        Switcher switcher = switchers.get(switcherName);
        return switcher != null && switcher.isOn();
    }

    @Override
    public boolean isOpen(String switcherName, boolean defaultValue) {
        Switcher switcher = switchers.get(switcherName);
        if (switcher == null) {
            switchers.putIfAbsent(switcherName, new Switcher(switcherName, defaultValue));
            switcher = switchers.get(switcherName);
        }
        return switcher.isOn();
    }

     /**
       *@描述 设置是否开启
     */
    @Override
    public void setValue(String switcherName, boolean value) {
        putSwitcher(new Switcher(switcherName, value));

        //如果存在监听，调用回调方法
        List<SwitcherListener> listeners = listenerMap.get(switcherName);
        if(listeners != null) {
            for (SwitcherListener listener : listeners) {
                listener.onValueChanged(switcherName, value);
            }
        }
    }

    @Override
    public void registerListener(String switcherName, SwitcherListener listener) {
        List listeners = Collections.synchronizedList(new ArrayList());
        List preListeners= listenerMap.putIfAbsent(switcherName, listeners);
        if (preListeners == null) {
            listeners.add(listener);
        } else {
            preListeners.add(listener);
        }
    }

    @Override
    public void unRegisterListener(String switcherName, SwitcherListener listener) {
            List<SwitcherListener> listeners = listenerMap.get(switcherName);
            if (listener == null) {
                // keep empty listeners
                listeners.clear();
            } else {
                listeners.remove(listener);
            }
    }
}
