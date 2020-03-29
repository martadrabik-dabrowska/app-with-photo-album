package com.photos.view;

import com.photos.model.User;
import com.photos.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.access.annotation.Secured;


@StyleSheet("styles.css")
@Configurable
@Route(value = UsersView.ROUTE)
@PageTitle("UsersList")
@Secured("admin")
public class UsersView extends MenuView {

    protected static final String ROUTE = "users";

    private VerticalLayout verticalLayout;

    private final UserService userService;

    private Grid<User> userGrid;

    public UsersView(UserService userService){

        this.userService = userService;

        verticalLayout = createVrticalLayout();

        userGrid = createUserGrid();
    }

    private VerticalLayout createVrticalLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setClassName("layout-style");
        add(verticalLayout);
        return verticalLayout;
    }

    private Grid<User> createUserGrid() {
        userGrid = new Grid<>(User.class);

        userGrid.setItems(userService.findAll());
        userGrid.removeAllColumns();
        userGrid.addColumn(User::getFirstName).setHeader("Imię");
        userGrid.addColumn(User::getLastName).setHeader("Nazwisko");
        userGrid.addColumn(User::getEmail).setHeader("Email");
        userGrid.addColumn(new ComponentRenderer<>(this::addDeleteColumn));
        userGrid.setClassName("grid-style");
        userGrid.setHeightByRows(true);
        verticalLayout.add(userGrid);
        return userGrid;
    }

    private Component addDeleteColumn(User user) {
        Button delete = new Button("Delete");
        delete.setClassName("delete-style");
        delete.addClickListener(p->{
            userService.deleteUser(user);
            userGrid.setItems(userService.findAll());
        });
        return delete;
    }
}
