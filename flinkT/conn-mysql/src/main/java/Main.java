import com.google.common.collect.Lists;
import common.ExecutionEnvUtil;
import common.config.KafkaConfigUtil;
import common.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import model.Student;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.util.Collector;
import sinks.SinkToMySQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static common.constant.PropertiesConstants.*;

//import org.apache.flink.api.common.time.Time;

/**
 * @program: flinkT
 * @description:
 * @author: geqx
 * @create: 2020-11-26 15:22
 **/
@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("--------------" );
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool parameterTool = ExecutionEnvUtil.PARAMETER_TOOL;
        Properties props = KafkaConfigUtil.buildKafkaProps(parameterTool);
        System.out.println("0000000000000000" );
        SingleOutputStreamOperator<Student> student = env.addSource(new FlinkKafkaConsumer011<>(
                parameterTool.get(METRICS_TOPIC),   //这个 kafka topic 需要和上面的工具类的 topic 一致
                new SimpleStringSchema(),
                props)).setParallelism(parameterTool.getInt(STREAM_PARALLELISM, 1))
                .map(string -> GsonUtil.fromJson(string, Student.class)).setParallelism(4); //解析字符串成 student 对象
        System.out.println("11111111111111");
//        SinkToMySQL s= new SinkToMySQL();
//        s.open(new Configuration());
        //timeWindowAll 并行度只能为 1
        student.timeWindowAll(Time.minutes(1)).apply(new AllWindowFunction<Student, List<Student>, TimeWindow>() {
            @Override
            public void apply(TimeWindow window, Iterable<Student> values, Collector<List<Student>> out) throws Exception {
                ArrayList<Student> students = Lists.newArrayList(values);
                System.out.println("22222222222222" );
                if (students.size() > 0) {
                    log.info("1 分钟内收集到 student 的数据条数是：" + students.size());
                    out.collect(students);
                }
            }
        }).addSink(new SinkToMySQL()).setParallelism(parameterTool.getInt(STREAM_SINK_PARALLELISM, 1));

        env.execute("flink learning connectors mysql");
    }
}
