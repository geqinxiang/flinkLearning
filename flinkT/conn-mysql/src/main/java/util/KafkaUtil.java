package util;
import common.utils.GsonUtil;
import model.Student;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
/**
 * @program: flinkT
 * @description:
 * @author: geqx
 * @create: 2020-11-26 15:46
 **/
public class KafkaUtil {
    public static final String broker_list = "192.168.0.116:9092";
    public static final String topic = "zy1";  //kafka topic 需要和 flink 程序用同一个 topic

    public static void writeToKafka() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = new KafkaProducer<String, String>(props);

        for (int i = 1; i <= 100; i++) {
            Student student = new Student("zy" + i, "password" + i, 18 + i);
            ProducerRecord record = new ProducerRecord<String, String>(topic, null, null, GsonUtil.toJson(student));
            producer.send(record);
            System.out.println("发送数据: " + GsonUtil.toJson(student));
            Thread.sleep(10 * 1000); //发送一条数据 sleep 10s，相当于 1 分钟 6 条
        }
        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        writeToKafka();
    }
}
