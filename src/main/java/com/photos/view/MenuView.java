package com.photos.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@StyleSheet("styles.css")
public class MenuView extends VerticalLayout {

    private HorizontalLayout mainLayout;

    private VerticalLayout logoLayout;

    private HorizontalLayout majorLayout;

    private Image myGalleryImg;

    private Image allGalleryImg;

    private Image addImage;

    private Image usersImage;

    private Image registerImage;

    private Image loginImage;

    private final HorizontalLayout userImageLayout;

    private VerticalLayout welcomeUserLayout;

    private Button logoutButton;


    public MenuView() {

        createMainLayout();

        createLogoLayout();

        createLayout();

        createLogoImg();

        createMyGallery();

        createAllGallery();

        addImage();

        createUsersImg();

        createRegisterImg();

        createLoginImage();

        majorLayout.setJustifyContentMode(JustifyContentMode.AROUND);

        majorLayout.add(myGalleryImg, allGalleryImg, addImage, usersImage, registerImage, loginImage);

        userImageLayout = new HorizontalLayout();

        majorLayout.add(userImageLayout);


        createWelcomeLayout();

        createLogoutButton();

        setAuthentication();

        if(myGalleryImg.isVisible()){
            registerImage.setVisible(false);
            loginImage.setVisible(false);
        }
    }

    private void createMainLayout() {
        mainLayout = new HorizontalLayout();
        mainLayout.setWidth("1500px");
        add(mainLayout);
    }

    private void createLogoLayout() {
        logoLayout = new VerticalLayout();
        logoLayout.setWidth("200px");
        mainLayout.add(logoLayout);

    }

    private void createLayout() {
        majorLayout = new HorizontalLayout();
        majorLayout.setClassName("menu-layout");
        majorLayout.setWidth("1200px");
        majorLayout.setHeight("80px");
        majorLayout.setClassName("menu-layout");
        mainLayout.add(majorLayout);

    }
    private void createLogoImg() {
        Image logoImage = new Image("/images/projector.jpg", "Main logo");
        logoImage.setTitle("Home");
        logoImage.setWidth("150px");
        logoImage.getStyle().set("cursor", "pointer");
        logoLayout.add(logoImage);
        logoImage.addClickListener(p-> showMainPage());

    }

    private void createMyGallery() {
        myGalleryImg = new Image("/images/mypictures.png", "Gallery Logo");
        myGalleryImg.setTitle("Moje zdjęcia");
        myGalleryImg.setHeight("80x");
        myGalleryImg.setWidth("100px");
        myGalleryImg.getStyle().set("cursor", "pointer");
        myGalleryImg.addClickListener(p -> showMyPictures());

    }

    private void createAllGallery() {
        allGalleryImg = new Image("/images/photo2.png", "Gallery Logo");
        allGalleryImg.setTitle("Galeria zdjęć");
        allGalleryImg.setHeight("80x");
        allGalleryImg.setWidth("100px");
        allGalleryImg.getStyle().set("cursor", "pointer");
        allGalleryImg.addClickListener(p-> showAllPictures());

    }

    private void addImage(){
        addImage = new Image("/images/add.png", "Add Image");
        addImage.setTitle("Dodaj zdjęcie");
        addImage.setHeight("80px");
        addImage.getStyle().set("cursor", "pointer");
        addImage.addClickListener(p-> addNextPictureToGallery());

    }

    private void createUsersImg() {
        usersImage = new Image("/images/users.png", "Użytkownicy Image");
        usersImage.setTitle("Użytkownicy");
        usersImage.setHeight("80x");
        usersImage.setWidth("100px");
        usersImage.getStyle().set("cursor", "pointer");
        usersImage.addClickListener(p-> showUsers());
    }

    private void createRegisterImg() {
        registerImage = new Image("/images/register.png", "Rejestracja logo");
        registerImage.setTitle("Rejestracja");
        registerImage.setHeight("50px");
        registerImage.getStyle().set("cursor", "pointer");
        registerImage.addClickListener(p -> register());
    }

    private void createLoginImage() {
        loginImage = new Image("/images/login.png", "Login Image");
        loginImage.setTitle("Zaloguj się");
        loginImage.setHeight("50px");
        loginImage.getStyle().set("cursor", "pointer");
        loginImage.addClickListener(p -> login());
    }


    private void createWelcomeLayout() {
        welcomeUserLayout = new VerticalLayout();
        welcomeUserLayout.setHeight("10px");

        userImageLayout.add(welcomeUserLayout);
        Label loggedAsLabel = new Label("Zalogowany jako: ");
        welcomeUserLayout.add(loggedAsLabel);
        welcomeUserLayout.addClickListener(p-> updateData());
        welcomeUserLayout.getStyle().set("cursor", "pointer");
        welcomeUserLayout.setClassName("welcome-layout");
    }

    private void updateData(){
        getUI().ifPresent(ui -> ui.navigate(UpdateUserView.ROUTE));
    }

    private void createLogoutButton() {
        logoutButton = new Button("Wyloguj się");
        logoutButton.getStyle().set("cursor", "pointer");
        logoutButton.setClassName("logout-style");
        majorLayout.add(logoutButton);
        logoutButton.addClickListener(p -> logout());
    }

    private void setAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        welcomeUserLayout.add(authentication.getName());

        boolean isAdmin = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(p -> p.equals("admin"));
        boolean isUser = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(p -> p.equals("user"));

        myGalleryImg.setVisible(!"anonymousUser".equals(authentication.getName()));
        allGalleryImg.setVisible(!"anonymousUser".equals(authentication.getName()));
        addImage.setVisible(!"anonymousUser".equals(authentication.getName()));
        welcomeUserLayout.setVisible(isAdmin||isUser);
        logoutButton.setVisible(!"anonymousUser".equals(authentication.getName()));
        usersImage.setVisible(isAdmin);
    }

    private void showMainPage() {
        getUI().ifPresent(ui -> ui.navigate(MainView.ROUTE));
    }

    private void showMyPictures() {
        getUI().ifPresent(ui -> ui.navigate(MyPicturesView.ROUTE));
    }

    private void showAllPictures(){
        getUI().ifPresent(ui -> ui.navigate(ShowAllPicturesView.ROUTE));
    }

    private void addNextPictureToGallery(){getUI().ifPresent(ui -> ui.navigate(UploadView.ROUTE));}

    private void showUsers(){
        getUI().ifPresent(ui -> ui.navigate(UsersView.ROUTE));
    }

    private void login() {
        getUI().ifPresent(ui -> ui.navigate(LoginView.ROUTE));
    }

    private void register() {
        getUI().ifPresent(ui -> ui.navigate(RegisterView.ROUTE));
    }

    private void logout(){
        VaadinService.getCurrentRequest().getWrappedSession().invalidate();
        VaadinSession.getCurrent().close();
        Notification.show("Nastąpiło wylogowanie");
        getUI().ifPresent(ui -> ui.navigate(MainView.ROUTE));
    }
}
