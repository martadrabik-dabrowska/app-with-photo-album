package com.photos.view;

import com.photos.model.User;
import com.photos.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.Route;


@StyleSheet("styles.css")
@Route(RegisterView.ROUTE)

public class RegisterView extends MenuView {

    public static final String ROUTE = "register";

    private UserService userService;

    private HorizontalLayout registerLayout;

    private VerticalLayout registerLayout1;

    private VerticalLayout imageLayout;

    private Binder<User> binder;

    private TextField firstName;

    private TextField lastName;

    private PasswordField password;

    private EmailField email;

    private VerticalLayout buttonLayout;

    private Button registerButton;


    public RegisterView(UserService userService){
        this.userService = userService;

        registerLayout = createRegisterLayout();

        add(registerLayout);

        imageLayout = createImgLayout();

        registerLayout1 = createRegLayout();

        binder = createBinder();

        buttonLayout = createButtonLayout();

    }

    private HorizontalLayout createRegisterLayout() {
        registerLayout = new HorizontalLayout();
        registerLayout.setWidth("1000px");
        registerLayout.setClassName("register-layout");
        return registerLayout;
    }

    private VerticalLayout createImgLayout() {
        imageLayout = new VerticalLayout();
        registerLayout.add(imageLayout);
        Image imageImg = new Image("images/camera.jpg", "Camera image");
        imageLayout.add(imageImg);

        return imageLayout;
    }

    private VerticalLayout createRegLayout() {
        VerticalLayout registerLayout1 = new VerticalLayout();
        registerLayout1.setClassName("register-style");
        registerLayout1.setWidth("250px");
        registerLayout1.setClassName("register-style");
        registerLayout.add(registerLayout1);
        return registerLayout1;
    }

    private Binder<User> createBinder() {
        binder = new Binder<>(User.class);

        firstName = new TextField("Imię");
        binder.forField(firstName)
                .withValidator(
                        firstName -> firstName.length() >= 3, "Imię powinno zawierać co najmniej 3 znaki")
                .bind(User::getFirstName, User::setFirstName);

        lastName = new TextField("Nazwisko");
        binder.forField(lastName)
                .withValidator(
                        lastName -> lastName.length() >= 3, "Nazwisko powinno zawierać co najmniej 3 znaki")
                .bind(User::getLastName, User::setLastName);

        email = new EmailField("email");
        binder.forField(email)
                .withValidator(new EmailValidator(
                        "Ten zapis nie wyglada jak adres email"))
                .bind(User::getEmail, User::setEmail);

        password = new PasswordField("Hasło");
        binder.forField(password)
                .withValidator(
                        password-> password.length() >= 6, "Hasło powinno zawierać co najmniej 6 znaków")
                .bind(User::getPassword, User::setPassword);

        registerLayout1.add(firstName);
        registerLayout1.add(lastName);
        registerLayout1.add(email);
        registerLayout1.add(password);
        return binder;
    }

    private VerticalLayout createButtonLayout() {
        buttonLayout = new VerticalLayout();
        registerLayout.add(buttonLayout);
        registerButton = new Button("Zarejestruj się");
        registerButton.getStyle().set("cursor", "pointer");
        registerButton.setClassName("register-button");

        Button cancelButton = new Button("Zrezygnuj");
        cancelButton.setClassName("cancel-button");
        cancelButton.getStyle().set("cursor", "pointer");

        buttonLayout.add(registerButton, cancelButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);

        registerButton.addClickListener(p-> register()) ;
        cancelButton.addClickListener(p-> setCancelButton());
        return buttonLayout;
    }

    private void register(){
        String firstNameValue = firstName.getValue();
        String lastNameValue = lastName.getValue();
        String passwordValue = password.getValue();
        String emailValue = email.getValue();

        if(binder.validate().isOk()) {

            User user = new User();
            user.setEmail(emailValue);
            user.setFirstName(firstNameValue);
            user.setLastName(lastNameValue);
            user.setPassword(passwordValue);
            user.setActive(1);

            userService.saveUser(user);
            getUI().ifPresent(ui -> ui.navigate(LoginView.ROUTE));
        } else{
            registerButton.setEnabled(false);
        }
    }

    private void setCancelButton(){
        getUI().ifPresent(ui -> ui.navigate(MainView.ROUTE));
    }
}
