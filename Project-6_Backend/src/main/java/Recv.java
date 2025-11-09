import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * This is our "Consumer"
 * It will continuously 'listen' for messages to output
 */
public class Recv {

    // DESC: Name the 'Queue'
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv) throws Exception {
        // DESC: Handle socket-connection and protocol-version negotiation
        ConnectionFactory factory = new ConnectionFactory();

        // DESC: Connect to RabbitMQ node (on local machine)
        factory.setHost("localhost");

        // DESC: Open a 'Connection' and a 'Channel'
        // NOTE: A Try w/ Resources is NOT used because we do not want the
        // ... Consumer to auto-close everything... it must stay open while
        // ... the Consumer is listening, asynchronously, for messages to arrive
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // DESC: Declare a Queue
        // NOTE: The Queue is declared here, as well, in the event that the
        // ... Consumer is started before the Publisher
        // NOTE: The Queue Name is the same as the Queue Name established
        // ... in the Publisher
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages... To exit press CTRL+C");

        // DESC: Establish a Callback Function that can buffer the messages
        // ... until we are ready to use them
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        // DESC: Get messages from the Queue
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }
}
