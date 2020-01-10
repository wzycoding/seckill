package com.wzy.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 接收者
 */
@Service
@Slf4j
public class MQReceiver {

    /** ==================================直连交换机start======================================= **/
    //此注解表示监听某个队列，参数为队列名
    @RabbitListener(queues = MQConfig.QUEUE_NAME1)
    public void receive1(String message) {
        log.info("queue1 receive msg : {}", message);
    }

    @RabbitListener(queues = MQConfig.QUEUE_NAME2)
    public void receive2(String message) {
        log.info("queue2 receive msg : {}", message);
    }

    @RabbitListener(queues = MQConfig.QUEUE_NAME3)
    public void receive3(String message) {
        log.info("queue3 receive msg : {}", message);
    }

    /** ==================================直连交换机end======================================= **/

    /** ==================================扇形交换机start======================================= **/

    @RabbitListener(queues = MQConfig.QUEUE_NAME4)
    public void receive4(String message) {
        log.info("queue4 receive msg : {}", message);
    }

    @RabbitListener(queues = MQConfig.QUEUE_NAME5)
    public void receive5(String message) {
        log.info("queue5 receive msg : {}", message);
    }

    @RabbitListener(queues = MQConfig.QUEUE_NAME6)
    public void receive6(String message) {
        log.info("queue6 receive msg : {}", message);
    }
    /** ==================================扇形交换机end======================================= **/


    /** ==================================主题交换机start======================================= **/
    @RabbitListener(queues = MQConfig.QUEUE_NAME7)
    public void receive7(String message) {
        log.info("queue7 receive msg : {}", message);
    }

    @RabbitListener(queues = MQConfig.QUEUE_NAME8)
    public void receive8(String message) {
        log.info("queue8 receive msg : {}", message);
    }
    /** ==================================主题交换机end======================================= **/



    /** ==================================首部交换机end======================================= **/
    @RabbitListener(queues = MQConfig.HEADERS_QUEUE_NAME)
    public void receiveHeaders(byte[] message) {
        log.info("headers queue receive msg : {}", new String(message));
    }
    /** ==================================首部交换机end======================================= **/



}
