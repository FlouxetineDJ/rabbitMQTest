package com.demo.consumer;

import com.demo.Util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
    private final static String QUEUE_NAME = "test_queue_work";//队列名称
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        //参数1 队列的名字
        //参数2 是否持久化队列 我们的队列模式是在内存中的，rabbitmq重启会丢失
        //如果设置为true 会保存在erlang自带的数据库中 重启后会重新读取
        //参数3 是否排外，有两个作用，第一个当我们的连接关闭后是否会自动删除队列，第二个作用，是否私用当前队列，如果私用，其他通道不可以访问当前队列
        //参数4 是否自动删除
        //参数5 我们的一些其他参数
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //100份 消息内容
        for (int i = 0; i < 100; i++) {
            // 消息内容
            String message = "" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");

            Thread.sleep(i * 10);
        }

//        String message = "Hello World!";
//        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
//        System.out.println(" [x] Sent '" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
    }

}
