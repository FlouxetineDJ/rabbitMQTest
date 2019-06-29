package com.demo.fanoutConsumer;

import com.demo.Util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 同一个消息被多个消费者获取。
 * 一个消费者队列可以有多个消费者实例，
 * 只有其中一个消费者实例会消费到消息。
 */
public class FanoutRecv {
    private final static String QUEUE_NAME="test_queue_work1";

    private final static String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] argv) throws Exception {

        //获取到连接以及mq通道
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        //同一时间服务只会发一条消息给消费者
        channel.basicQos(1);

        //定义消费者
        QueueingConsumer consumer=new QueueingConsumer(channel);
        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME,false,consumer);

        //获取消息
        while (true){
            QueueingConsumer.Delivery delivery=consumer.nextDelivery();
            String message=new String(delivery.getBody());
            System.out.println(" [Recv] Received '" + message + "'");
            Thread.sleep(10);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        }
    }
}
