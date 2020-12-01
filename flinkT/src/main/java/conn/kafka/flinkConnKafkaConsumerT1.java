package conn.kafka;
import common.ExecutionEnvUtil;
import common.model.MetricEvent;
import common.schemas.MetricSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static common.config.KafkaConfigUtil.buildKafkaProps;

/**
 * @program: flinkT
 * @description: 测试Kafka消费者
 * @author: geqx
 * @create: 2020-11-26 10:47
 **/
public class flinkConnKafkaConsumerT1 {
    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ExecutionEnvUtil.createParameterTool(args);
        StreamExecutionEnvironment env = ExecutionEnvUtil.prepare(parameterTool);
        Properties props = buildKafkaProps(parameterTool);
        //kafka topic list
        List<String> topics = Arrays.asList(parameterTool.get("metrics.topic"), parameterTool.get("logs.topic"));
        FlinkKafkaConsumer011<MetricEvent> consumer = new FlinkKafkaConsumer011<>(topics, new MetricSchema(), props);
        //kafka topic Pattern
        //FlinkKafkaConsumer011<MetricEvent> consumer = new FlinkKafkaConsumer011<>(java.utils.regex.Pattern.compile("test-topic-[0-9]"), new MetricSchema(), props);


//        consumer.setStartFromLatest();
//        consumer.setStartFromEarliest()
        DataStreamSource<MetricEvent> data = env.addSource(consumer);

        data.print();

        env.execute("flink kafka connector test");
    }
}
