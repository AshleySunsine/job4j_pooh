package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp("", "204 No content");
        ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> topic = topics.get(req.getSourceName());
        if (topic != null && "POST".equals(req.httpRequestType())) {
            topic.forEach((key, value) -> value.add(req.getParam()));
            result = new Resp(req.getParam(), "201 Created");
        } else if ("GET".equals(req.httpRequestType())) {
            topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            String getString = topics.get(req.getSourceName()).get(req.getParam()).poll();
            if (getString != null) {
                result = new Resp(getString, "200 OK");
            }
        }
        return result;

    }

}