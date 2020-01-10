package com.wzy.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbitmq配置
 */
@Configuration
public class MQConfig {

    /** ==================================直连交换机start======================================= **/
    public static final String QUEUE_NAME1 = "queue1";
    public static final String QUEUE_NAME2 = "queue2";
    public static final String QUEUE_NAME3 = "queue3";
    public static final String DIRECT_EXCHANGE_NAME = "DIRECT_EXCHANGE_NAME";

    /**
     * 创建三个队列：queue1
     */
    @Bean
    public Queue queue1() {
        //第二个参数是是否要做持久化
        return new Queue(QUEUE_NAME1, true);
    }

    /**
     * queue2
     */
    @Bean
    public Queue queue2() {
        //第二个参数是是否要做持久化
        return new Queue(QUEUE_NAME2, true);
    }

    /**
     * queue3
     */
    @Bean
    public Queue queue3() {
        //第二个参数是是否要做持久化
        return new Queue(QUEUE_NAME3, true);
    }

    /**
     * 创建直连交换机，交换机为参数的名称
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE_NAME);
    }

    /**
     * 将三个队列都与该直连交换机绑定起来，并赋予上面说的binding_key(也可以说是routing_key)
     */
    @Bean
    public Binding bindDirectExchange1() {
        return BindingBuilder.bind(queue1()).to(directExchange()).with("key.1");
    }
    @Bean
    public Binding bindDirectExchange2() {
        return BindingBuilder.bind(queue2()).to(directExchange()).with("key.1");
    }
    @Bean
    public Binding bindDirectExchange3() {
        return BindingBuilder.bind(queue2()).to(directExchange()).with("key.2");
    }

    /** ==================================直连交换机end======================================= **/

    /** ==================================扇形交换机start======================================= **/

    public static final String QUEUE_NAME4 = "queue4";
    public static final String QUEUE_NAME5 = "queue5";
    public static final String QUEUE_NAME6 = "queue6";

    public static final String FANOUT_EXCHANGE_NAME = "FANOUT_EXCHANGE_NAME";

    @Bean
    public Queue queue4() {
        return new Queue(QUEUE_NAME4);
    }

    @Bean
    public Queue queue5() {
        return new Queue(QUEUE_NAME5);
    }

    @Bean
    public Queue queue6() {
        return new Queue(QUEUE_NAME6);
    }

    /**
     * 创建扇形交换机，参数为交换机的名称
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE_NAME);
    }

    /**
     * 为队列绑定扇形交换机,无需绑定routing_key，因为所有队列都会发送消息
     */
    @Bean
    public Binding bindFanoutExchange1() {
        return BindingBuilder.bind(queue4()).to(fanoutExchange());
    }

    @Bean
    public Binding bindFanoutExchange2() {
        return BindingBuilder.bind(queue5()).to(fanoutExchange());
    }

    @Bean
    public Binding bindFanoutExchange3() {
        return BindingBuilder.bind(queue6()).to(fanoutExchange());
    }

    /** ==================================扇形交换机end======================================= **/




    /** ==================================主题交换机start======================================= **/

    public static final String QUEUE_NAME7 = "QUEUE_NAME7_TOPIC1";
    public static final String QUEUE_NAME8 = "QUEUE_NAME8_TOPIC2";

    public static final String TOPIC_EXCHANGE_NAME = "TOPIC_EXCHANGE_NAME";

    @Bean
    public Queue queue7() {
        return new Queue(QUEUE_NAME7);
    }

    @Bean
    public Queue queue8() {
        return new Queue(QUEUE_NAME8);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Binding bindTopicExchange1() {
        return BindingBuilder.bind(queue7()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding bindTopicExchange2() {
        return BindingBuilder.bind(queue8()).to(topicExchange()).with("topic#");
    }
    /** ==================================主题交换机end======================================= **/




    /** ==================================首部交换机end======================================= **/

    public static final String HEADERS_QUEUE_NAME = "HEADERS_QUEUE_NAME";
    public static final String HEADERS_EXCHANGE_NAME = "HEADERS_EXCHANGE_NAME";

    @Bean
    public Queue headersQueue() {
        return new Queue(HEADERS_QUEUE_NAME);
    }

    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE_NAME);
    }

    //将headersQueue与HeadersExchange交换机绑定
    @Bean
    public Binding bindHeadersQueue() {
        Map<String, Object> map = new HashMap<>();
        map.put("headers1", "value1");
        map.put("headers2", "value2");
        //whereAll表示需要满足所有条件
        return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
    }
    /** ==================================首部交换机end======================================= **/

}
