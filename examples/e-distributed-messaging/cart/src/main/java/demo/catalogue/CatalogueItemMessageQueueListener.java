package demo.catalogue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogueItemMessageQueueListener {

    @Bean
    public Queue queue() {
        return new Queue("demo-queue", false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("demo-exchange");
    }

    @Bean
    public Binding binding(final Queue queue, final TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("demo.catalogue.new");
    }

    @Bean
    public SimpleMessageListenerContainer container(final ConnectionFactory connectionFactory,
                                                    final MessageListenerAdapter listenerAdapter) {

        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("demo-queue");
        container.setMessageListener(listenerAdapter);

        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(final CatalogueItemGateway gateway) {
        return new MessageListenerAdapter(gateway, "newCatalogueItem");
    }
}
