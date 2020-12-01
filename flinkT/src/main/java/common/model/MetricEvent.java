package common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @program: flinkT
 * @description:
 * @author: geqx
 * @create: 2020-11-26 10:58
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetricEvent {

    /**
     * Metric name
     */
    private String name;

    /**
     * Metric timestamp
     */
    private Long timestamp;

    /**
     * Metric fields
     */
    private Map<String, Object> fields;

    /**
     * Metric tags
     */
    private Map<String, String> tags;
}