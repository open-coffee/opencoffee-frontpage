package coffee.synyx.frontpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The CoffeeNet Frontpage Server.
 *
 * @author  Tobias Schneider - schneider@synyx.de
 */
@SpringBootApplication
public class FrontpageServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(FrontpageServerApplication.class, args);
    }
}
