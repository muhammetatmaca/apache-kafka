package io.conduktor.demos.kafka.wikimedia;

// Gerekli import'ları ekleyin
import com.launchdarkly.eventsource.ConnectStrategy;
import com.launchdarkly.eventsource.EventSource;
import com.launchdarkly.eventsource.background.BackgroundEventHandler;
import com.launchdarkly.eventsource.background.BackgroundEventSource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.net.URI;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class WikimediaChangesProducer {

    public static void main(String[] args) throws InterruptedException {

        // Kafka Producer ayarları
        Properties properties = new Properties();
        // ----> DEĞİŞİKLİK BURADA YAPILDI <----
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.23.246.155:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "wikimedia.recentchange";
        String url = "https://stream.wikimedia.org/v2/stream/recentchange";

        BackgroundEventHandler eventHandler = new WikimediaChangeHandler(producer, topic);

        // Önce User-Agent başlığı ile bağlantı stratejisi oluşturulur.
        // Sonra bu strateji EventSource.Builder'a verilir.
        EventSource.Builder eventSourceBuilder = new EventSource.Builder(
                ConnectStrategy.http(URI.create(url))
                        .header("User-Agent", "MyKafkaWikimediaProducer/1.0")
        );

        BackgroundEventSource.Builder builder = new BackgroundEventSource.Builder(eventHandler, eventSourceBuilder);
        BackgroundEventSource eventSource = builder.build();

        // Producer'ı başlat
        eventSource.start();

        // 10 dakika bekle
        TimeUnit.MINUTES.sleep(10);
    }
}
