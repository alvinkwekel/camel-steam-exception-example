package com.example.camel;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(classes = Routes.class)
class ExampleApplicationTests {

    @Autowired
    ProducerTemplate producerTemplate;

    @Test
    void convertBodyWithStreamCache() {
        // Cached stream is closed by explicit type converter
        String data = "data";
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:convertBodyWithStreamCache", is, Object.class);
        Assertions.assertEquals(data, out);
    }

    @Test
    void convertBodyWithoutStreamCache() {
        // Uncached stream is closed by reading with type converter
        String data = "data";
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:convertBodyWithoutStreamCache", is, Object.class);
        Assertions.assertEquals(data, out);
    }

    @Test
    void unmarshallWithStreamCache() {
        // Cached stream is closed unmarshalling
        String data = """
        {"test": "data"}
        """;
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:unmarshallWithStreamCache", is, Object.class);
        Assertions.assertEquals(data, out);
    }

    @Test
    void unmarshallWithoutStreamCache() {
        // Uncached stream is closed by reading with unmarshaller
        String data = """
        {"test": "data"}
        """;
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:unmarshallWithoutStreamCache", is, Object.class);
        Assertions.assertEquals(data, out);
    }

    @Test
    void unmarshallInvalidWithoutStreamCache() {
        // Uncached stream is closed by reading with unmarshaller
        String data = """
        {"test": "data
        """;
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:convertBodyInvalidUnmarshallWithoutStreamCache", is, Object.class);
        Assertions.assertEquals(data, out);
    }

    @Test
    void noStreamReading() {
        // Both cached and uncached streams are available because if it is not used
        String data = "data";
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:noStreamReading", is, Object.class);
        Assertions.assertEquals(data, out);
    }

    @Test
    void withoutOriginalMessage() {
        // Data is converted to string and put in exchange property
        String data = "data";
        InputStream is = new ByteArrayInputStream(data.getBytes());
        Object out = producerTemplate.requestBody("direct:withoutOriginalMessage", is, Object.class);
        Assertions.assertEquals(data, out);
    }
}
