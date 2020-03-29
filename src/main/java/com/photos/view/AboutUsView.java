package com.photos.view;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = AboutUsView.ROUTE)
public class AboutUsView extends MenuView{

    public static final String ROUTE = "about" ;

    public AboutUsView(){
        createAndaddInformationsLayout();
    }

    public void createAndaddInformationsLayout(){
        VerticalLayout informationsLayout = new VerticalLayout();
        add(informationsLayout);
    }
}
