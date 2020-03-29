package com.photos.view;

import com.photos.model.Picture;
import com.photos.services.PictureService;
import com.photos.utilities.UserUtilities;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.List;


@StyleSheet("styles.css")
@Secured({"admin", "user"})
@Route(MyPicturesWithDescriptionView.ROUTE)
public class MyPicturesWithDescriptionView extends MenuView {

    public static final String ROUTE = "pictures";

    private final PictureService pictureService;

    private List<Picture> pictureList;

    private int indexPicture = 0;

    private VerticalLayout verticalLayout;

    private HorizontalLayout sliderLayout;

    private VerticalLayout centerLayout;

    public MyPicturesWithDescriptionView(PictureService pictureService) {

        this.pictureService = pictureService;

        loadData();

        createVerticalLayout();

        createSliderLayout();

        createLeftLayout();

        createCenterLayout();

        addImageSlider();

        createRightLayout();
    }

    private void loadData() {
        String loggedUser = UserUtilities.getLoggedUser();
        pictureList = pictureService.findByUser(loggedUser);
    }

    private void createVerticalLayout() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("650px");
        add(verticalLayout);

    }

    private void createSliderLayout() {
        sliderLayout = new HorizontalLayout();
        sliderLayout.setWidth("1500px");
        sliderLayout.setHeight("600px");
        verticalLayout.add(sliderLayout);
    }

    private void createLeftLayout() {
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("100px");
        sliderLayout.add(leftLayout);

        Icon leftIcon = new Icon(VaadinIcon.ANGLE_LEFT);
        leftIcon.setSize("100px");
        leftIcon.setClassName("icon-vaadin");
        leftIcon.setColor("green");
        leftIcon.getStyle().set("cursor", "pointer");
        leftLayout.add(leftIcon);
        leftIcon.addClickListener(l -> showPreviousPicture());
    }

    private void showPreviousPicture () {

        if (indexPicture > 0) {
            indexPicture--;
        }
        if (indexPicture == 0) {
            indexPicture = pictureList.size() - 1;
        }
        addImageSlider();
    }

    private void addImageSlider () {
        if (CollectionUtils.isEmpty(pictureList)) {
            return;
        }
        Picture p = pictureList.get(indexPicture);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(p.getData());
        StreamResource resource = new StreamResource(p.getPictureName(), () -> byteArrayInputStream);
        Image image = new Image(resource, p.getDescription() != null ? p.getDescription() : p.getPictureName());
        image.setHeight("550px");
        image.setMaxWidth("1100px");
        image.setClassName("image-style");

        centerLayout.add();
        centerLayout.removeAll();
        centerLayout.add(image);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        centerLayout.add(horizontalLayout);
        horizontalLayout.setHeight("50px");
        horizontalLayout.setWidth("1000px");
        horizontalLayout.setClassName("description-style");
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.add(new Label(p.getDescription()));

    }

    private void createCenterLayout() {
        centerLayout = new VerticalLayout();
        centerLayout.setHeight("500px");
        centerLayout.setMaxWidth("1200px");
        sliderLayout.add(centerLayout);
    }

    private void createRightLayout() {
        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("100px");
        sliderLayout.add(rightLayout);

        Icon rightIcon = new Icon(VaadinIcon.ANGLE_RIGHT);
        rightIcon.setSize("100px");
        rightIcon.setColor("orange");
        rightIcon.setClassName("icon-vaadin");
        rightIcon.getStyle().set("cursor", "pointer");
        rightIcon.addClickListener(r -> showNextPicture());
        rightLayout.add(rightIcon);
    }

    private void showNextPicture() {
        if (indexPicture < pictureList.size() - 1) {
            indexPicture++;
        } else {
            indexPicture = 0;
        }
        addImageSlider();
    }
}


