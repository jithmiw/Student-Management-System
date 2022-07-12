package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import view.tm.StudentTM;

public class ManageStudentFormController {
    public AnchorPane root;
    public JFXTextField txtStudentName;
    public JFXTextField txtStudentId;
    public JFXButton btnDelete;
    public JFXButton btnSave;
    public JFXTextField txtAddress;
    public TableView<StudentTM> tblStudents;
    public JFXButton btnAddNewStudent;
    public JFXTextField txtEmail;
    public JFXTextField txtContact;
    public JFXTextField txtNIC;
    public JFXTextField txtSearch;

    public void btnAddNewOnAction(ActionEvent event) {
    }

    public void btnSaveOnAction(ActionEvent event) {
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }

    public void btnSearchOnAction(ActionEvent event) {
    }
}
