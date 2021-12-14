package ru.job4j.pooh;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "204");
        if ("POST".equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>(Collections.singleton(req.getParam())));
            System.out.println(queue.get(req.getSourceName()));
            result = new Resp(req.getParam() + " added to" + req.getSourceName(), "200");
        } else if ("GET".equals(req.httpRequestType())) {
            String getQueue = queue.get(req.getSourceName()).poll();
            if (getQueue != null) {
                result = new Resp(getQueue, "500");
            }
        }
        return result;
    }
}