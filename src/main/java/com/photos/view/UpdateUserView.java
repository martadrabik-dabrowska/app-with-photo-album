package com.photos.view;

import com.photos.model.User;
import com.photos.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.WeakHashMap;

@Route(value = UpdateUserView.ROUTE)
public class UpdateUserView extends MenuView {

    public static final String ROUTE = "update_user";

    private UserService userService;

    private Grid<User> userGrid;

    private User editUser;

    public UpdateUserView(UserService userService) {

        this.userService = userService;

        userGrid = new Grid<>(User.class);

        loadData();

        userGrid.removeAllColumns();

        Grid.Column<User> firstNameColumn = userGrid.addColumn(User::getFirstName)
                .setHeader("Imię");
        Grid.Column<User> lastNameColumn = userGrid.addColumn(User::getLastName)
                .setHeader("Nazwisko");
        Grid.Column<User> emailColumn = userGrid.addColumn(User::getEmail)
                .setHeader("Email");

        add(userGrid);

        Binder<User> binder = new Binder<>(User.class);
        Editor<User> editor = userGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField firstNameField = new TextField();
        binder.forField(firstNameField)
                .withValidator(new StringLengthValidator("Imię powinno się składać od 3 do 20 znaków.", 3, 20))
                .withStatusLabel(validationStatus).bind("firstName");
        firstNameColumn.setEditorComponent(firstNameField);

        TextField lastNameField = new TextField();
        binder.forField(lastNameField)
                .withValidator(new StringLengthValidator("Nazwisko powinno się składać od 3 do 20 znaków", 3, 20))
                .withStatusLabel(validationStatus).bind("lastName");
        lastNameColumn.setEditorComponent(lastNameField);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<User> editorColumn = userGrid.addComponentColumn(user -> {
            Button edit = new Button("Edytuj");
            edit.addClassName("edit-style");
            edit.addClickListener(e -> {
                editor.editItem(user);
                firstNameField.focus();
                this.editUser = user;
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e ->{ editor.save();
        save();
        });
        save.addClassName("save");

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");

        userGrid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);
    }

    private void loadData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userGrid.setItems(userService.findUserByEmail(userEmail));
    }

    private void save(){
        userService.updateUser(this.editUser.getFirstName(),this.editUser.getLastName(),this.editUser.getEmail());
    }
}
