package com.photos;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App extends VerticalLayout {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
