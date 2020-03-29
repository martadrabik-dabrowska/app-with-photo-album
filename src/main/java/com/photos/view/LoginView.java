package com.photos.view;

import com.photos.security.CustomRequestCache;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;


@StyleSheet("styles.css")
@Route(value = LoginView.ROUTE)
@PageTitle("Login")

public class LoginView extends MenuView{
    public static final String ROUTE = "login";

    private LoginOverlay login = new LoginOverlay();


    public LoginView(AuthenticationManager authenticationManager,
                     CustomRequestCache requestCache){

        login.setOpened(true);
        login.setTitle("Zaloguj się na konto");
        login.setForgotPasswordButtonVisible(false);
        login.setDescription("");
        login.addLoginListener(e->login(e,authenticationManager,requestCache ));
    }

    private void login(AbstractLogin.LoginEvent e,AuthenticationManager authenticationManager,
                       CustomRequestCache requestCache){
        try {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));

            if(authentication != null ) {

                String userName = authentication.getName();
                login.close();
                SecurityContextHolder.getContext().setAuthentication(authentication);
                Notification.show("Zalogowałeś się na konto ");
                getUI().ifPresent(ui -> ui.navigate(MainView.ROUTE));
            }

        } catch (AuthenticationException ex) {
            login.setError(true);
        }
    }
}
