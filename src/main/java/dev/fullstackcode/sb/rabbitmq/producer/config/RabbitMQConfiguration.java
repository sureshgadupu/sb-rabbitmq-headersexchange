package dev.fullstackcode.sb.rabbitmq.producer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    Queue queueA() {
        return new Queue("queue.A", false);
    }

    @Bean
    Queue queueB() {
        return new Queue("queue.B", false);
    }

    @Bean
    Queue queueC() {
        return new Queue("queue.C", false);
    }


    @Bean
    HeadersExchange exchange() {
        return new HeadersExchange("exchange.header");
    }

    @Bean
    Binding bindingA(Queue queueA, HeadersExchange exchange) {
        Map<String,Object> map = new HashMap<>();
        map.put("type","report");
        map.put("format","pdf");
        return BindingBuilder.bind(queueA).to(exchange).where("type").matches("report");
    }

    @Bean
    Binding bindingB(Queue queueB, HeadersExchange exchange) {
        Map<String,Object> map = new HashMap<>();
        map.put("type","report");
        map.put("format","excel");
        return BindingBuilder.bind(queueB).to(exchange).whereAny(map).match();
    }

    @Bean
    Binding bindingC(Queue queueC, HeadersExchange exchange) {
        Map<String,Object> map = new HashMap<>();
        map.put("type","report");
        map.put("format","excel");
        map.put("period","monthly");
        return BindingBuilder.bind(queueC).to(exchange).whereAll(map).match();
    }
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    ApplicationRunner runner(ConnectionFactory cf) {
        return args -> cf.createConnection().close();
    }

}
