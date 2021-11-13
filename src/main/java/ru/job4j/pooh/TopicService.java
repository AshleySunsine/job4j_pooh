package ru.job4j.pooh;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    /* Map<topic, Map<id, param>>*/
    private ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> queueTopic = new ConcurrentHashMap<>();

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
        queueTopic.get(req.getSourceName()).forEach((key, value) -> value.add(req.getParam()));
    }

    private String topicExtract(Req req) {
       if (null != queueTopic.get(req.getSourceName())) {
           if (null != queueTopic.get(req.getSourceName()).get(req.getParam())) {
               return queueTopic.get(req.getSourceName()).get(req.getParam()).poll();

           } else {
               queueTopic.get(req.getSourceName()).put(req.getParam(), new ConcurrentLinkedQueue<>());
               return "";
           }
       } else {
           queueTopic.put(req.getSourceName(), new ConcurrentHashMap<>());
           queueTopic.get(req.getSourceName()).put(req.getParam(), new ConcurrentLinkedQueue<>());
           return "";
       }
    }
}