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

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

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

    public final Logger logger = Logger.getLogger(ZookeeperService.class);


    public ZookeeperService(Watcher watcher, ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        this.watcher = watcher;
        this.zookeeper = zooKeeper;
        addNodeIfNotExists(new Node(PATH, TEST));
    }

    public ZookeeperService() throws InterruptedException, IOException, KeeperException {
        watcher = event -> System.out.println(ZOOKEEPER_PATH_TEST);
        zookeeper = new ZooKeeper(CONNECT_STRING, 1000, watcher);
        addNodeIfNotExists(new Node(PATH, TEST));
    }

    public String listNodeData() throws KeeperException, InterruptedException {
        List<String> children = zookeeper.getChildren(PATH, true);
        if (children.isEmpty()) return "";
        return String.join(DELIMITER, children);
    }

    public boolean deleteNode(String path) throws KeeperException, InterruptedException {
        if (validatePath(path)) {
            zookeeper.delete(path, -1);
            return true;
        }
        return false;
    }

    public boolean updateNodeData(Node node) throws KeeperException, InterruptedException {
        if (validatePath(node.getPath())) {
            zookeeper.setData(node.getPath(), node.getContext().getBytes(), -1);
            return true;
        }
        return false;
    }

    public boolean addNodeIfNotExists(Node node) throws KeeperException, InterruptedException {
        Stat stat = null;
        stat = zookeeper.exists(node.getPath(), true);
        if (stat == null || validatePath(node.getPath())) {
            addNodeData(node.getPath(), node.getContext());
            return true;
        }
        return false;
    }

    private void addNodeData(String path, String data) throws KeeperException, InterruptedException {
        zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    private boolean validatePath(String path) {
        return path.matches("^(?:[^\\\\\\?\\/\\*\\|<>:\"]+\\\\)+$");
    }

}
