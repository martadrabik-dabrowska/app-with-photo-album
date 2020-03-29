package com.photos.view;

import com.photos.model.Picture;
import com.photos.services.PictureService;

import com.photos.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Route(value = UploadView.ROUTE)
public class UploadView extends MenuView {

    public static final String ROUTE = "upload" ;

    private PictureService pictureService;

    private UserService userService;

    private List<TextArea> descriptionsField = new ArrayList<>();

    private VerticalLayout verticalLayout;

    private String fileName;

    private Button saveButton;

    private Button addDescription;

    private MultiFileMemoryBuffer buffer;


    public UploadView(PictureService pictureService, UserService userService) {
        this.pictureService = pictureService;
        this.userService = userService;
        verticalLayout = createVerticalLayout();
        buffer = createUpload();
    }

    private VerticalLayout createVerticalLayout() {
        verticalLayout = new VerticalLayout();
        add(verticalLayout);
        return verticalLayout;
    }
    private MultiFileMemoryBuffer createUpload() {
        buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg","image/png","image/gif");
        add(upload);

        verticalLayout.add(upload);
        upload.setMaxFiles(1);
        upload.addSucceededListener(p->{
            fileName = p.getFileName();
            verticalLayout.setVisible(false);
            HorizontalLayout saveLayout = new HorizontalLayout();
            add(saveLayout);
            addDescription = new Button("Dodaj opis i zapisz zdjęcie");
            addDescription.setVisible(true);
            saveLayout.add(addDescription);
            addDescription.addClickListener(r-> setDescription());

            saveButton = new Button("Zapisz zdjęcie bez dodawania opisu");
            saveButton.setVisible(true);
            saveLayout.add(saveButton);
            saveButton.addClickListener(s -> savePicture());
        });
        return buffer;
    }

    private void createDescriptionAndSavePicture(String fil, MultiFileMemoryBuffer buffer) {
        byte[] bytes = buffer.getOutputBuffer(fil).toByteArray();
        Picture pic = new Picture();
        pic.setData(bytes);
        pic.setPictureName(fil);
        pic.setDescription(findDescription(fil));
        Picture savePicture = pictureService.savePicture(pic);
        addPictureToUser(savePicture);
        getUI().ifPresent(ui -> ui.navigate(MyPicturesWithDescriptionView.ROUTE));
    }

    private void savePicture(String fil, MultiFileMemoryBuffer buffer){
        byte[] bytes = buffer.getOutputBuffer(fil).toByteArray();
        Picture picture = new Picture();
        picture.setData(bytes);
        picture.setPictureName(fil);
        Picture savePicture = pictureService.savePicture(picture);
        addPictureToUser(savePicture);

        getUI().ifPresent(ui -> ui.navigate(MyPicturesView.ROUTE));
    }

    private void addPictureToUser(Picture savePicture) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        userService.addPictureToUser(userEmail,savePicture);
    }


    private String findDescription(String fil) {
        TextArea textArea = descriptionsField.stream().filter(p -> p.getId().get().equals(fil)).findFirst().get();
        return textArea.getValue();
    }

    private void setMaxFiles(int maxFiles) {
        setMaxFiles((int) maxFiles);
    }

    private void setDescription(){

        TextArea descriptionText = new TextArea("Dodaj opis do zdjęcia (max. 50 znaków) ");
        descriptionText.setId(fileName);
        descriptionText.setClearButtonVisible(true);
        descriptionText.setMinWidth("300px");
        descriptionsField.add(descriptionText);
        add(descriptionText);
        verticalLayout.setVisible(false);
        saveButton.setVisible(false);
        addDescription.setVisible(false);
        Button saveAll = new Button("Zapisz wszystko");
        add(saveAll);
        saveAll.addClickListener(s-> createDescriptionAndSavePicture(fileName, buffer));
    }

    private void savePicture(){
        Set<String> files = buffer.getFiles();
        files.forEach(fil->savePicture(fil,buffer));
    }
}
