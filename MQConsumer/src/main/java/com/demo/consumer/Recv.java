package com.demo.consumer;

import com.demo.Util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

public class Recv {

    private final static String QUEUE_NAME="test_queue_work";//队列名称

    public static void main(String[] args) throws Exception {

        //获取到连接mq的通道
        Connection connection= ConnectionUtil.getConnection();
        //从连接中创建通道
        Channel channel=connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //同一时刻服务器只会发一条消息给消费者
        channel.basicQos(1);

        //定义队列的消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        // 接受消息
        channel.basicConsume(QUEUE_NAME, false, consumer);

        // 获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
            //休眠
            Thread.sleep(10);
            // 返回确认状态，注释掉表示使用自动确认模式
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
