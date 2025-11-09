import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * This is our "Publisher"
 * It will 'publish' a single message and be done...
 */
public class Send {

    // DESC: Name the 'Queue'
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        // DESC: Handle socket-connection and protocol-version negotiation
        ConnectionFactory factory = new ConnectionFactory();

        // DESC: Connect to RabbitMQ node (on local machine)
        factory.setHost("localhost");

        // NOTE: Connection and Channel implement AutoCloseable
        // NOTE: Try-with-Resources that has multiple resource declarations
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()){

            // DESC: Declare a Queue
            // NOTE: Queues are 'idempotent' -- it will only be created if it
            // ... does not already exist
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // DESC: Set 'Message', then 'Publish' it
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
