package uk.co.nutmeg;

import feign.Client;
import org.springframework.context.annotation.Bean;

public class Config {

    @Bean
    public Client client() {
        return new CustomFeignClient(null, null);
    }

}
