package com.example.oversell.service;

import org.apache.zookeeper.Watcher;

public interface ZookeeperService {
    public Boolean acquireDistributedLock(Long productId);
    public void releaseDistributedLock(Long productId);
}
