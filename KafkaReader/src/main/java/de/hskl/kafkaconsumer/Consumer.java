package de.hskl.kafkaconsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Consumer.class.getClassLoader().getResourceAsStream("consumer.properties"));
        String kafkaHost = properties.getProperty("kafka.host");
        String kafkaPort = properties.getProperty("kafka.port");
        String kafkaTopic = properties.getProperty("kafka.topic");

        if(System.getProperty("kafka.host") != null)
            kafkaHost = System.getProperty("kafka.host");
        if(System.getProperty("kafka.port") != null)
            kafkaPort = System.getProperty("kafka.port");
        if(System.getProperty("kafka.topic") != null)
            kafkaTopic = System.getProperty("kafka.topic");

        System.out.println(System.getProperty("kafka.host"));
        System.out.println(System.getProperty("kafka.port"));
        System.out.println(System.getProperty("kafka.topic"));

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost + ":" + kafkaPort);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "java-consumer");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(kafkaTopic));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("offset: " + record.offset() + ", key: " + record.key() + ", value: " + record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
