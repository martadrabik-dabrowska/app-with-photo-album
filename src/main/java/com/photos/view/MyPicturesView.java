package com.photos.view;


import com.photos.model.Picture;
import com.photos.model.Role;
import com.photos.services.PictureService;
import com.photos.utilities.UserUtilities;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.CollectionUtils;
import java.io.ByteArrayInputStream;
import java.util.List;

@Route(value = MyPicturesView.ROUTE)
@PageTitle("Moje zdjęcia")
@Secured({Role.admin, Role.user})
public class MyPicturesView extends MenuView {

    public static final String ROUTE = "mypictures";

    private final PictureService pictureService;

    private List<Picture> pictureList;

    private int indexPicture = 0;

    private Button showButton;

    private VerticalLayout verticalLayout;

    private Image filmImage;

    private HorizontalLayout sliderLayout;

    private VerticalLayout leftLayout;

    private VerticalLayout centerLayout;

    private VerticalLayout rightLayout;


    public MyPicturesView(PictureService pictureService){
        this.pictureService = pictureService;

        loadData();

        showButton = createButton();

        verticalLayout = createVerticalLayout();

        filmImage = createFilmImg();

        sliderLayout = createSliderLayout();

        leftLayout = createLeftLayout();

        centerLayout = createCenterLayout();

        rightLayout = createRightLayout();

        createFilmImg();
    }

    private VerticalLayout createLeftLayout() {
        leftLayout = new VerticalLayout();
        leftLayout.setWidth("100px");
        leftLayout.setClassName("slider-layout");
        sliderLayout.add(leftLayout);

        Icon leftIcon = new Icon(VaadinIcon.ANGLE_LEFT);
        leftIcon.setSize("100px");
        leftIcon.setClassName("icon-vaadin");
        leftIcon.setColor("green");
        leftIcon.getStyle().set("cursor", "pointer");
        leftLayout.add(leftIcon);
        leftIcon.addClickListener(l -> showPreviousPicture());
        return leftLayout;
    }

    private void showPreviousPicture() {

        if(indexPicture >0) {
            indexPicture--;
        }
        if (indexPicture == 0 ) {
            indexPicture = pictureList.size()-1;
        }
        addImageSlider();
    }

    private void addImageSlider() {
        if (CollectionUtils.isEmpty(pictureList)) {
            return;
        }
        Picture p = pictureList.get(indexPicture);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(p.getData());
        StreamResource resource = new StreamResource(p.getPictureName(), () -> byteArrayInputStream);
        Image image = new Image(resource, p.getDescription() != null ? p.getDescription() : p.getPictureName());
        image.setMaxHeight("550px");
        image.setMinHeight("450");
        image.setMaxWidth("1100px");
        image.setClassName("image-style");

        centerLayout.add();
        centerLayout.removeAll();
        centerLayout.add(image);
    }

    private void loadData() {
        String loggedUser = UserUtilities.getLoggedUser();
        pictureList = pictureService.findByUser(loggedUser);
    }

    private Button createButton() {

        showButton = new Button("Pokaż zdjęcia z opisami");
        add(showButton);
        showButton.getStyle().set("cursor", "pointer");
        showButton.addClickListener(p-> showPicturesAndDescriptions());
        return showButton;
    }

    private void showPicturesAndDescriptions() {
        getUI().ifPresent(ui -> ui.navigate(MyPicturesWithDescriptionView.ROUTE));
    }

    private VerticalLayout createVerticalLayout() {
        verticalLayout = new VerticalLayout();
        verticalLayout.setHeight("700px");
        verticalLayout.setWidth("1450px");
        verticalLayout.setClassName("slider-layout");
        add(verticalLayout);
        verticalLayout.setJustifyContentMode(JustifyContentMode.AROUND);
        return verticalLayout;
    }

    private Image createFilmImg() {
        filmImage = new Image("/images/film1.jpg", "Film");
        filmImage.setWidth("1400px");
        filmImage.setHeight("50px");
        filmImage.setClassName("film-style");
        verticalLayout.add(filmImage);
        return filmImage;
    }

    private HorizontalLayout createSliderLayout() {
        sliderLayout = new HorizontalLayout();
        sliderLayout.setClassName("slider-layout");
        sliderLayout.setWidth("1400px");
        verticalLayout.add(sliderLayout);
        return sliderLayout;
    }

    private VerticalLayout createCenterLayout() {
        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setHeight("550px");
        centerLayout.setClassName("slider-layout");
        centerLayout.setWidth("1100px");

        sliderLayout.add(centerLayout);
        return centerLayout;
    }

    private VerticalLayout createRightLayout() {
        rightLayout = new VerticalLayout();
        rightLayout.setWidth("100px");
        sliderLayout.add(rightLayout);

        addImageSlider();

        Icon rightIcon = new Icon(VaadinIcon.ANGLE_RIGHT);
        rightIcon.setSize("100px");
        rightIcon.setColor("orange");
        rightIcon.setClassName("icon-vaadin");
        rightIcon.getStyle().set("cursor", "pointer");
        rightIcon.addClickListener(r -> showNextPicture());
        rightLayout.add(rightIcon);
        return rightLayout;
    }

    private void showNextPicture() {

        if(indexPicture < pictureList.size()-1){
            indexPicture++;
        }else {
            indexPicture = 0;
        }
        addImageSlider();
    }
    }
