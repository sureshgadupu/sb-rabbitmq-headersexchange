package dev.fullstackcode.sb.rabbitmq.producer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fullstackcode.sb.rabbitmq.producer.model.Event;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.JsonbMessageConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value ="rabbitmq/event")
public class RabbitMQProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private HeadersExchange headersExchange;


    @Autowired
    private Jackson2JsonMessageConverter jsonMessageConverter;


    @PostMapping
    public String  send(@RequestBody Event event) {


        if( event.getName().equalsIgnoreCase("Event A")) {

            MessageProperties properties = new MessageProperties();
            properties.setHeader("type","report");
            properties.setHeader("format","pdf");


            Message message = jsonMessageConverter.toMessage(event, properties);
            rabbitTemplate.convertAndSend(headersExchange.getName(), "", message);
        } else if (event.getName().equalsIgnoreCase("Event B")) {

            MessageProperties properties = new MessageProperties();
            properties.setHeader("type","report");
            properties.setHeader("format","excel");

            Message message = jsonMessageConverter.toMessage(event, properties);
            rabbitTemplate.convertAndSend(headersExchange.getName(), "", message);
        } else if (event.getName().equalsIgnoreCase("Event C")) {

            MessageProperties properties = new MessageProperties();
            properties.setHeader("type","report");
            properties.setHeader("format","excel");
            properties.setHeader("period","monthly");

            Message message = jsonMessageConverter.toMessage(event, properties);
            rabbitTemplate.convertAndSend(headersExchange.getName(), "", message);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"unknown event");
        }
        return "message sent successfully";
    }

}
