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

    public static final String CUSTOM_EXCHANGE_NAME = "assist_get_delay_exchange";
    public static final String CUSTOM_EXCHANGE_TYPE = "x-delayed-message";
    public static final String CANCEL_QUEUE_NAME = "assist_get_cancel_queue";
    public static final String FINISH_QUEUE_NAME = "assist_get_finish_queue";
    public static final String END_QUEUE_NAME = "assist_get_end_queue";
    public static final String PRE_REFUND_QUEUE_NAME = "assist_get_pre_refund_queue";

    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(CUSTOM_EXCHANGE_NAME, CUSTOM_EXCHANGE_TYPE, true, false, args);
    }

    @Bean
    public Queue cancelQueue() {
        Queue queue = new Queue(CANCEL_QUEUE_NAME, true);
        return queue;
    }

    @Bean
    public Queue finishQueue() {
        Queue queue = new Queue(FINISH_QUEUE_NAME, true);
        return queue;
    }

    @Bean
    public Queue endQueue() {
        Queue queue = new Queue(END_QUEUE_NAME, true);
        return queue;
    }

//    @Bean
//    public Queue preRefundQueue() {
//        Queue queue = new Queue(PRE_REFUND_QUEUE_NAME, true);
//        return queue;
//    }

    @Bean
    public Binding cancelBinding() {
        return BindingBuilder.bind(cancelQueue()).to(delayExchange()).with(CANCEL_QUEUE_NAME).noargs();
    }

    @Bean
    public Binding finishBinding() {
        return BindingBuilder.bind(finishQueue()).to(delayExchange()).with(FINISH_QUEUE_NAME).noargs();
    }

    @Bean
    public Binding endBinding() {
        return BindingBuilder.bind(endQueue()).to(delayExchange()).with(END_QUEUE_NAME).noargs();
    }

//    @Bean
//    public Binding preRefundBinding() {
//        return BindingBuilder.bind(preRefundQueue()).to(delayExchange()).with(PRE_REFUND_QUEUE_NAME).noargs();
//    }

}
