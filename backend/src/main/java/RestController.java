import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.qpid.QpidException;
import org.apache.qpid.url.URLSyntaxException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@EnableAutoConfiguration
public class RestController extends BackEnd {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody String consume(@RequestParam String nPojos) throws JsonProcessingException {
        return getPayload(Integer.valueOf(nPojos));
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    new MessageConsumer().consume();
                } catch (JMSException e) {
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (URLSyntaxException e) {
                    e.printStackTrace();
                } catch (QpidException e) {
                    e.printStackTrace();
                }
            }
        });

        SpringApplication.run(RestController.class, args);

//        final byte[] utf8Bytes = getPayload(310).getBytes("UTF-8");
//        System.out.println(utf8Bytes.length);
    }
}
