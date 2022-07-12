package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import util.SQLUtil;
import view.tm.StudentTM;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void initialize() {
        tblStudents.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("student_id"));
        tblStudents.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("student_name"));
        tblStudents.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("email"));
        tblStudents.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("contact"));
        tblStudents.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("address"));
        tblStudents.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("nic"));

        initUI();

        tblStudents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnDelete.setDisable(newValue == null);
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtStudentId.setText(newValue.getStudent_id());
                txtStudentName.setText(newValue.getStudent_name());
                txtEmail.setText(newValue.getEmail());
                txtContact.setText(newValue.getContact());
                txtAddress.setText(newValue.getAddress());
                txtNIC.setText(newValue.getNic());

                txtStudentId.setDisable(false);
                txtStudentName.setDisable(false);
                txtEmail.setDisable(false);
                txtContact.setDisable(false);
                txtAddress.setDisable(false);
                txtNIC.setDisable(false);
            }
        });

        txtNIC.setOnAction(event -> btnSave.fire());
        loadAllStudents();
    }

    private void loadAllStudents() {
        tblStudents.getItems().clear();
        /*Get all students*/
        try {
            ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Student");

            while (rst.next()) {
                tblStudents.getItems().add(new StudentTM(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getString(6)));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void initUI() {
        txtStudentId.clear();
        txtStudentName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIC.clear();

        txtStudentId.setDisable(true);
        txtStudentName.setDisable(true);
        txtEmail.setDisable(true);
        txtContact.setDisable(true);
        txtAddress.setDisable(true);
        txtNIC.setDisable(true);

        txtStudentId.setEditable(false);
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
    }
    
    public void btnAddNewOnAction(ActionEvent event) {
    }

    public void btnSaveOnAction(ActionEvent event) {
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }

    public void btnSearchOnAction(ActionEvent event) {
    }
}
