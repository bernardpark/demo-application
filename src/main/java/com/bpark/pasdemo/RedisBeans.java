package com.bpark.pasdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

import com.bpark.pasdemo.redis.pubsub.GuestPublisher;
import com.bpark.pasdemo.redis.pubsub.MessageSubscriber;
import com.bpark.pasdemo.redis.repository.GuestRepository;

@Configuration
public class RedisBeans {
	
	private final String guestbookTopic = "guestbook";
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
	    return new JedisConnectionFactory();
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
	    final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
	    template.setConnectionFactory( jedisConnectionFactory() );
	    template.setValueSerializer( new GenericToStringSerializer<Object>( Object.class ) );
	    return template;
	}
	
	@Bean
	ChannelTopic guestbookChannelTopic() {
		return new ChannelTopic( guestbookTopic );
	}
	
	@Bean
	MessageListenerAdapter messageListener() {
	    return new MessageListenerAdapter( new MessageSubscriber() );
	}
	
	@Bean
	GuestPublisher guestPublisher() {
	    return new GuestPublisher( redisTemplate(), guestbookChannelTopic() );
	}
	
	@Bean
	GuestRepository guestRepository() {
		return new GuestRepository( redisTemplate(), guestbookTopic );
		
	}

}
