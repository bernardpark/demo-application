package com.bpark.pasdemo.redis.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.bpark.pasdemo.form.GuestForm;

@Service
public class GuestPublisher {
	
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChannelTopic topic;
    
    public GuestPublisher() {
    }
    
    public GuestPublisher( final RedisTemplate<String, Object> redisTemplate, final ChannelTopic topic ) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }
    
    public void publish( final GuestForm guestEntry ) {
        redisTemplate.convertAndSend( topic.getTopic(), guestEntry );
    }
    
}