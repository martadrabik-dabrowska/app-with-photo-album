package com.photos.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.time.LocalDateTime;

@StyleSheet("styles.css")
@Route(value = MainView.ROUTE)
public class MainView extends MenuView {

    public static final String ROUTE = "index";

    private  VerticalLayout majorLayout;

    private VerticalLayout infoLayout;

    private HorizontalLayout imageLayout;

    public MainView() {

        createMajorLayout();

        createFirstInfoImg();

        createSecondInfoImg();

        createImgLayout();

        createCenterImg();

        createFooter();
    }

    private void createCenterImg() {
        Image centerImage = new Image("/images/d.jpg", "Gerd Altmann Pixabay");
        centerImage.setHeight("300px");
        centerImage.setWidth("1500px");
        centerImage.setClassName("image-style");
        imageLayout.add(centerImage);
    }

    private void createMajorLayout() {
         majorLayout = new VerticalLayout();
         majorLayout.setWidth("1500px");
         add(majorLayout);

         infoLayout = new VerticalLayout();
         majorLayout.setClassName("main-layout");
         majorLayout.add(infoLayout);
    }
    private void createFirstInfoImg() {
        Image info1Image = new Image("images/info.png", "Dodawaj zdjęcia i udostępniaj je innym");
        info1Image.setHeight("50px");
        info1Image.setWidth("600px");
        infoLayout.add(info1Image);
    }

    private void createSecondInfoImg(){
        Image info2Image = new Image("images/info1.png", "Dodawaj zdjęcia i udostępniaj je innym");
        info2Image.setHeight("50px");
        info2Image.setWidth("500px");
        infoLayout.add(info2Image);
    }

    private void createImgLayout() {
        imageLayout = new HorizontalLayout();
        majorLayout.add(imageLayout);
    }

    private void createFooter() {
        HorizontalLayout footerLayout = new HorizontalLayout();
        add(footerLayout);
        footerLayout.setHeight("30px");
        footerLayout.setWidth("1500px");
        footerLayout.setClassName("footer-style");

        Button aboutUsButton = new Button("Regulamin korzystania, Kontakt, O nas");
        aboutUsButton.setClassName("button-style");
        aboutUsButton.getStyle().set("cursor", "pointer");
        aboutUsButton.addClickListener(p-> showAboutUsPage());

        Label textLabel = new Label("Copyright");

        Label copyrightLabel = new Label("\u00a9");

        Label emptyLabel = new Label("  ");

        LocalDateTime dateTime= LocalDateTime.now();

        Label emptyLabel1 = new Label("    ");

        footerLayout.add(aboutUsButton, textLabel, copyrightLabel, emptyLabel);
        footerLayout.add(String.valueOf(dateTime.getYear()));
        footerLayout.add(emptyLabel1);
        footerLayout.setJustifyContentMode(JustifyContentMode.END);
    }

    private void showAboutUsPage() {
        getUI().ifPresent(ui -> ui.navigate(AboutUsView.ROUTE));
    }
}











