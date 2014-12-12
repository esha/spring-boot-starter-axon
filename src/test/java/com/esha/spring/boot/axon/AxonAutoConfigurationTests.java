package com.esha.spring.boot.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.IdentifierFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventListener;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventstore.EventStore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertNotNull;

public class AxonAutoConfigurationTests {

    private AnnotationConfigApplicationContext context;

    @Before
    public void setUp() {
        this.context = new AnnotationConfigApplicationContext();
    }

    @After
    public void tearDown() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void autoConfigured() throws Exception {
        this.context.register(AxonAutoConfiguration.class);
        this.context.refresh();
        assertNotNull(this.context.getBean(CommandBus.class));
        assertNotNull(this.context.getBean(CommandGateway.class));
        assertNotNull(this.context.getBean(EventBus.class));
        assertNotNull(this.context.getBean(EventStore.class));
        assertNotNull(this.context.getBean(IdentifierFactory.class));
    }

    @Test
    public void generatedHandlerAdapters() throws Exception {
        this.context.register(AxonAutoConfiguration.class);
        this.context.register(TestConfiguration.class);
        this.context.refresh();
        assertNotNull(this.context.getBean(CommandHandler.class));
        assertNotNull(this.context.getBean(EventListener.class));
    }

    @Configuration
    protected static class TestConfiguration {

        protected static class TestCommand {}

        protected static class TestEvent {}

        @Component
        protected static class TestHandlers {

            @org.axonframework.commandhandling.annotation.CommandHandler
            public void handleCommand(TestCommand command) {}

            @EventHandler
            public void handleEvent(TestEvent event) {}
        }
    }

}
