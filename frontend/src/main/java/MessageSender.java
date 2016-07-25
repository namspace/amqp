import org.apache.qpid.QpidException;
import org.apache.qpid.client.AMQConnection;
import org.apache.qpid.client.AMQQueue;
import org.apache.qpid.url.URLSyntaxException;

import javax.jms.*;
import java.net.URISyntaxException;

public class MessageSender {
    private final QueueSession session;
    private final QueueRequestor requestor;

    public MessageSender() throws URLSyntaxException, QpidException, JMSException {
        AMQConnection connection = new AMQConnection("amqp://admin:admin@test/default?brokerlist='tcp://localhost:5672'");

        session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.setExceptionListener(new ExceptionListener() {
            public void onException(JMSException e) {
                System.out.println(e.getMessage());
            }
        });

        Queue destination = new AMQQueue(connection, "direct://amq.direct//request");
        requestor = new QueueRequestor(session, destination);

        connection.start();
    }

    public String sendAndReceive(String nPojos) throws URISyntaxException, QpidException, JMSException {

        TextMessage request = session.createTextMessage();
        request.setText(nPojos);

        Message response = requestor.request(request);

        String text;
        if (response instanceof TextMessage)
        {
            text=((TextMessage) response).getText();
        }
        else
        {
            byte[] body=new byte[(int) ((BytesMessage) response).getBodyLength()];
            ((BytesMessage) response).readBytes(body);
            text=new String(body);
        }

//        requestor.close();
//        connection.close();

        return text;
    }
}
