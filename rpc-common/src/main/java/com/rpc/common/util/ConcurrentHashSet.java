package com.rpc.common.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created on 2017/9/30.
 *
 * @author zhoushengfan
 */
public class ConcurrentHashSet<T> extends AbstractSet<T> implements Set<T>, Serializable {

    private static final long serialVersionUID = -3698965012619656710L;
    private static final Object PLACEHOLDER = new Object();

    private volatile ConcurrentMap<T, Object> map;

    public ConcurrentHashSet(){
        this(16);
    }

    public ConcurrentHashSet(int initialCapacity) {
        if (map == null){
            synchronized (ConcurrentHashSet.class){
                if (map == null){
                    map = new ConcurrentHashMap<>(initialCapacity);
                }
            }
        }
    }

    public boolean add(T e) {
        return map.put(e, PLACEHOLDER) == null;
    }

    public boolean remove(Object e){
        return map.remove(e) == null;
    }

    @Override
    public Iterator<T> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}
