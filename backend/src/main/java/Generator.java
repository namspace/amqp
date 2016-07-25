import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

public class Generator {

    Collection<MyPOJO> generateRandom(int nPojos) {

        Collection<MyPOJO> generated = new ArrayList<MyPOJO>();
        for(int i=0; i<nPojos; i++) {
            MyPOJO myPOJO = new MyPOJO(
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10)
            );
            generated.add(myPOJO);
        }

        return generated;
    }
}
