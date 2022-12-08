package uk.co.nutmeg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.cloud.sleuth.Tracer;
//import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//Agent configuration, add to the VM arguments -> "-javaagent:vendor/otel/opentelemetry-javaagent.jar"

@Controller
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

//    @Autowired
//    private Tracer tracer;

    @Value("${spring.application.name}")
    String appName;

    @Autowired
    SimpleClient simpleClient;

    @GetMapping("/")
    @ResponseBody
//    @NewSpan("FirstLevel")
    public String firstLevel() {
        logger.info("Inside first level."); // {} {}", tracer.currentSpan().context().parentId(), tracer.currentSpan().context().spanId());
        secondLevel();
        return "Hello World!";
    }

    @GetMapping("/external")
    @ResponseBody
//    @NewSpan("ExternalCall")
    public String externalCall(HttpServletRequest request) {
        thirdLevel();
        return "External Call with trace id header = " + request.getHeader("B3");
    }

//    @NewSpan("SecondLevel")
    public void secondLevel() {
        logger.info("Inside second level.");// {} {}", tracer.currentSpan().context().parentId(), tracer.currentSpan().context().spanId());
        try {
            Thread.sleep(1000);

        } catch (InterruptedException ie) {
        }
        logger.info("Result from getMessage() = {}.", simpleClient.getMessage());

    }

//    @NewSpan("ThirdLevel")
    public void thirdLevel() {
        logger.info("Inside third level."); // {} {}", tracer.currentSpan().context().parentId(), tracer.currentSpan().context().spanId());
    }

    @FeignClient(value = "simpleClient", url = "localhost:8081", configuration = Config.class)
    public interface SimpleClient {

        @RequestMapping(method = RequestMethod.GET, value = "/external")
        String getMessage();

    }

}
