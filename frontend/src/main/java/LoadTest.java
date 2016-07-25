import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class LoadTest {

    private String uri = "http://localhost:8081/?nPojos=%s&protocol=%s";
    private RestTemplate restTemplate = new RestTemplate();

    public List<Long> run(int nTimes, int nPojos, String protocol) {
        List<Long> runTimes = new ArrayList<Long>();
        String fullUri = String.format(uri, String.valueOf(nPojos), protocol);
        for(int i=0; i<nTimes; i++) {
            long start = System.currentTimeMillis();
            restTemplate.getForObject(fullUri, String.class);
            long runTime = System.currentTimeMillis() - start;
            runTimes.add(runTime);
        }
        return runTimes;
    }

    public static void main(String[] args) {
        List<Long> runTimes = new LoadTest().run(Integer.valueOf(args[0]), Integer.valueOf(args[1]), args[2]);

        System.out.println(runTimes);

        long totalRunTime = runTimes.stream().mapToLong(Long::longValue).sum();
        System.out.println("Protocol: " + args[2]);
        System.out.println("# of runs: " + Integer.valueOf(args[0]));
        System.out.println("# of pojos in message: " + Integer.valueOf(args[1]));
        System.out.println("Total time: " + totalRunTime + " ms.");
        System.out.println("Average time: " + totalRunTime/Integer.valueOf(args[0]) + " ms.");
    }
}
