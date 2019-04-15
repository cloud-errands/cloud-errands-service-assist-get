package xyz.tostring.cloud.errands.service.assist.get.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DelayQueueConfig {

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("assist_get_delay_exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue cancelQueue() {
        Queue queue = new Queue("assist_get_cancel_queue", true);
        return queue;
    }

    @Bean
    public Queue finishQueue() {
        Queue queue = new Queue("assist_get_finish_queue", true);
        return queue;
    }

    @Bean
    public Binding cancelBinding() {
        return BindingBuilder.bind(cancelQueue()).to(delayExchange()).with("assist_get_cancel_queue").noargs();
    }

    @Bean
    public Binding finishBinding() {
        return BindingBuilder.bind(finishQueue()).to(delayExchange()).with("assist_get_finish_queue").noargs();
    }

}
