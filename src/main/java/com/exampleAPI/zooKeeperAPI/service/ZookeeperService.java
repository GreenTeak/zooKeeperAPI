package com.exampleAPI.zooKeeperAPI.service;

import com.exampleAPI.zooKeeperAPI.model.Node;
import lombok.Data;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ZookeeperService {

    public ZooKeeper zookeeper;
    public Watcher watcher;

    public final static String PATH = "/test";
    public static final String ZOOKEEPER_PATH_TEST = "zookeeper PATH : /test";
    public static final String CONNECT_STRING = "127.0.0.1:2181";
    public static final String TEST = "test";
    public static final String DELIMITER = ",";


    public ZookeeperService(Watcher watcher, ZooKeeper zooKeeper){
        this.watcher = watcher;
        this.zookeeper = zooKeeper;
    }

    public String listNodeData() throws KeeperException, InterruptedException {
        List<String> children = zookeeper.getChildren(PATH, true);
        if (children.isEmpty()) return "";
        return String.join(DELIMITER, children);
    }

    public void deleteNode(String path) throws KeeperException, InterruptedException {
        zookeeper.delete(path, -1);
    }

    public void updateNodeData(Node node) throws KeeperException, InterruptedException {
        zookeeper.setData(node.getPath(), node.getContent().getBytes(), -1);
    }

    public boolean addNodeIfNotExists(Node node) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.exists(node.getPath(), true);
        if (stat == null) {
            addNodeData(node.getPath(), node.getContent());
            return true;
        }
        return false;
    }

    private void addNodeData(String path, String data) throws KeeperException, InterruptedException {
        zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }
}
