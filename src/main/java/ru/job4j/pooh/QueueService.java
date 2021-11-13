package ru.job4j.pooh;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        if ("POST".equals(req.httpRequestType())) {
            return post(req);
        }
        if ("GET".equals(req.httpRequestType())) {
            return get(req);
        }
        return new Resp("type GET or POST", "500");
    }

    private Resp post(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>(Collections.singleton(req.getParam())));
        System.out.println(queue.get(req.getSourceName()));
        return new Resp(req.getParam() + " added to" + req.getSourceName(), "200");
    }

    private Resp get(Req req) {

        return new Resp(queue.get(req.getSourceName()).poll(), "500");
    }
}