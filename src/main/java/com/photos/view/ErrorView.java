package com.photos.view;

import com.photos.model.Role;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route(value = ErrorView.ROUTE)

@Secured(Role.admin)
public class ErrorView extends VerticalLayout {

    public static final String ROUTE = "error";
    private HorizontalLayout horizontalLayout;

    public ErrorView() {
        createHorizontalLayout();

    }

    private void createHorizontalLayout(){
        horizontalLayout = new HorizontalLayout();
        add(horizontalLayout);
        createErrorText();
    }

    private void createErrorText() {
        TextField textField = new TextField("Coś poszło nie tak");
        horizontalLayout.add(textField);
    }
}
