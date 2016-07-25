import org.apache.qpid.QpidException;
import org.apache.qpid.jms.Message;
import org.apache.qpid.url.URLSyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.jms.JMSException;
import java.net.URISyntaxException;

@Controller
@EnableAutoConfiguration
public class MyController {

    private final MessageSender messageSender;
    private RestTemplate restTemplate = new RestTemplate();
    private String uri = "http://localhost:8082/?nPojos=";

    MyController() throws QpidException, URLSyntaxException, JMSException {
        messageSender = new MessageSender();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody String load(@RequestParam String nPojos, @RequestParam String protocol) throws QpidException, JMSException, URISyntaxException {
        switch (protocol) {
            case "http":
                return restTemplate.getForObject(uri.concat(String.valueOf(nPojos)), String.class);
            case "amqp":
                return messageSender.sendAndReceive(nPojos);
            default:
                throw new UnsupportedOperationException("Protocol is not specified or not supported.");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(MyController.class, args);
    }
}
