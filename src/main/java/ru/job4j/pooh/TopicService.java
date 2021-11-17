package ru.job4j.pooh;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    /* Map<topic, Map<id, param>>*/
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> ids = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if ("POST".equals(req.httpRequestType())) {
            topicPut(req);
            return new Resp("Topic-putted", "200");
        }
        if ("GET".equals(req.httpRequestType())) {
            String s = topicExtract(req);
            if (null != s) {
                return new Resp(s, "200");
            }
        }
        return new Resp("", "200");
    }

    private void topicPut(Req req) {
        for (var id : topic.get(req.getSourceName())) {
            ids.getOrDefault(id, new ConcurrentLinkedQueue<>()).offer(req.getParam());
        }
    }

    private String topicExtract(Req req) {
        topic.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
        if (!topic.get(req.getSourceName()).contains(req.getParam())) {
            topic.get(req.getSourceName()).offer(req.getParam());
        }
        ids.putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
        if (null != ids.get(req.getParam()).peek()) {
            return ids.get(req.getParam()).poll();
        }
        return "";
    }
}