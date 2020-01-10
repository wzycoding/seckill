package com.wzy.seckill.rabbitmq;

import com.wzy.seckill.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送者
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    AmqpTemplate amqpTemplate;


    /**
     * 发送消息到直连交换机
     */
    public void sendDirectExchange(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send msg to direct exchange  : " + msg);
        //第一个参数指将消息发送到该名称的交换机，第二个参数为对应的routing_key,第三个参数为发送的具体消息
        amqpTemplate.convertAndSend(MQConfig.DIRECT_EXCHANGE_NAME, "key.1", msg);
    }

    /**
     * 发送消息到扇形交换机，不需要指定routingKey但是要设置为空字符串
     */
    public void sendFanoutExchange(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send msg to fanout exchange : " + msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE_NAME, "", msg);
    }


    /**
     * 发送消息到主题交换机
     */
    public void sendTopicExchange(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send msg to topic exchange : " + msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE_NAME, "topic", msg);
    }

    /**
     * 发送消息到首部交换机
     */
    public void sendHeadersExchange(Object message) {
        String msgString = RedisService.beanToString(message);
        log.info("send msg to headers exchange : " + msgString);
        //配置消息规则
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("headers1", "value1");
        messageProperties.setHeader("headers2", "value2");
        //要发送的消息，第一个参数为具体的消息字节数组，第二个参数为消息规则
        Message msg = new Message(msgString.getBytes(), messageProperties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE_NAME, "", msg);
    }
}
