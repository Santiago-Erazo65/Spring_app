package com.example.di.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class Devs4jKafkaConfiguration {
	/**
	 * Consumer
	 */ 
	private Map<String, Object> consumerProps() { 
		Map<String, Object>props = new HashMap<>(); 
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); 
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "devs4jGroup"); 
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true); 
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100"); 
		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000"); 
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class); 
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); 
		return props;
	}
	
	@Bean 
	public ConsumerFactory<Integer, String> consumerFactory() {  
		return new DefaultKafkaConsumerFactory<>(consumerProps());
	}
	
	@Bean 
	public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() { 
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>(); 
		factory.setConsumerFactory(consumerFactory()); 
		return factory; 
	}
	/**
	 * Producer
	 */
	
	private Map<String, Object> producerProps() {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    props.put(ProducerConfig.RETRIES_CONFIG, 0);
	    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
	    props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
	    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	    return props;
	}

	@Bean
	public KafkaTemplate<Integer, String> createTemplate() {
	    Map<String, Object> senderProps = producerProps();
	    ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<>(senderProps);
	    KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);
	    return template;
	}

}
