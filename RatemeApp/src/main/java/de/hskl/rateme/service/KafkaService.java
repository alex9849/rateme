package de.hskl.rateme.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Singleton
public class KafkaService {
    private Producer<String, String> producer;
    private String topic;

    public KafkaService() throws NamingException {
        Properties properties = new Properties();
        Object kafkaHost = InitialContext.doLookup("kafkaHost");
        Object kafkaPort = InitialContext.doLookup("kafkaPort");
        Object kafkaTopic = InitialContext.doLookup("kafkaTopic");
        this.topic = String.valueOf(kafkaTopic);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        this.producer = new KafkaProducer<>(properties);
    }

    public void produce(String key, String value) {
        this.producer.send(new ProducerRecord<>(this.topic, key, value));
    }

}
