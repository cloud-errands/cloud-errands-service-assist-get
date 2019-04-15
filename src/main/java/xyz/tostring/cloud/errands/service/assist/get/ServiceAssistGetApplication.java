package xyz.tostring.cloud.errands.service.assist.get;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableScheduling
public class ServiceAssistGetApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAssistGetApplication.class, args);
    }
}
