import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.qpid.QpidException;
import org.apache.qpid.client.AMQConnection;
import org.apache.qpid.client.AMQQueue;
import org.apache.qpid.url.URLSyntaxException;

import javax.jms.*;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * Created by nmadzharov on 19/07/2016.
 */
public class MessageConsumer extends BackEnd {

    private final Session session;
    private final javax.jms.MessageConsumer messageConsumer;

    MessageConsumer() throws JMSException, URLSyntaxException, QpidException {
        AMQConnection connection = new AMQConnection("amqp://admin:admin@test/default?brokerlist='tcp://localhost:5672'");

        connection.start();
        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.setExceptionListener(new ExceptionListener() {
            public void onException(JMSException e) {
                System.out.println(e.getMessage());
            }
        });

        Queue destination = new AMQQueue(connection, "direct://amq.direct//request");
        messageConsumer = session.createConsumer(destination);
    }

    public void consume() throws JMSException, JsonProcessingException {
        Message requestMessage;
        boolean end=false;
        System.out.println("Start listening...");
        while (!end)
        {
            long start = System.currentTimeMillis();
            requestMessage=messageConsumer.receive();
            System.out.println(requestMessage);

            String text;
            if (requestMessage instanceof TextMessage)
            {
                text=((TextMessage) requestMessage).getText();
            }
            else
            {
                byte[] body=new byte[(int) ((BytesMessage) requestMessage).getBodyLength()];
                ((BytesMessage) requestMessage).readBytes(body);
                text=new String(body);
            }

            System.out.println("Message command read in " + (System.currentTimeMillis()-start) + "ms.");

            if (requestMessage.getJMSReplyTo() != null)
            {
                start = System.currentTimeMillis();
                BytesMessage responseMessage = session.createBytesMessage();
                responseMessage.writeBytes(getPayload(Integer.valueOf(text)).getBytes(Charset.forName("UTF-8")));
                System.out.println("Message payload created in " + (System.currentTimeMillis()-start) + "ms.");

                start = System.currentTimeMillis();
                MessageProducer messageProducer = session.createProducer(requestMessage.getJMSReplyTo());
                messageProducer.send(responseMessage);
                System.out.println("Replied in " + (System.currentTimeMillis()-start) + "ms.");
            }
        }
    }

}
