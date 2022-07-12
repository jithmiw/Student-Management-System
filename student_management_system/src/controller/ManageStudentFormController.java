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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        txtStudentId.setDisable(false);
        txtStudentName.setDisable(false);
        txtEmail.setDisable(false);
        txtContact.setDisable(false);
        txtAddress.setDisable(false);
        txtNIC.setDisable(false);
        txtStudentId.clear();
        txtStudentId.setText(generateNewId());
        txtStudentName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtAddress.clear();
        txtNIC.clear();
        txtStudentName.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblStudents.getSelectionModel().clearSelection();
    }

    public void btnSaveOnAction(ActionEvent event) {
        txtStudentId.setDisable(false);
        txtStudentName.setDisable(false);
        txtEmail.setDisable(false);
        txtContact.setDisable(false);
        txtAddress.setDisable(false);
        txtNIC.setDisable(false);

        String id = txtStudentId.getText();
        String name = txtStudentName.getText();
        String email = txtEmail.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();
        String nic = txtNIC.getText();

        if (btnSave.getText().equalsIgnoreCase("save")) {
            /*Save student*/
            try {
                if (existStudent(id)) {
                    new Alert(Alert.AlertType.ERROR, id + " already exists").show();
                }
                SQLUtil.executeUpdate("INSERT INTO Student (student_id, student_name, email, contact, address, nic) VALUES (?,?,?,?,?,?)", id, name, email, contact, address, nic);

                tblStudents.getItems().add(new StudentTM(id, name, email, contact, address, nic));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the student " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            /*Update student*/
            try {
                if (!existStudent(id)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such student associated with the id " + id).show();
                }
                SQLUtil.executeUpdate("UPDATE Student SET student_name=?, email=?, contact=?, address=?, nic=? WHERE student_id=?", name, email, contact, address, nic, id);

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the student " + id + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            StudentTM selectedStudent = tblStudents.getSelectionModel().getSelectedItem();
            selectedStudent.setStudent_name(name);
            selectedStudent.setEmail(email);
            selectedStudent.setContact(contact);
            selectedStudent.setAddress(address);
            selectedStudent.setNic(nic);
            tblStudents.refresh();
        }
        btnAddNewStudent.fire();
    }

    boolean existStudent(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.executeQuery("SELECT student_id FROM Student WHERE student_id=?", id).next();
    }

    public void btnDeleteOnAction(ActionEvent event) {
    }

    public void btnSearchOnAction(ActionEvent event) {
    }

    private String generateNewId() {
        try {
            ResultSet rst = SQLUtil.executeQuery("SELECT student_id FROM Student ORDER BY student_id DESC LIMIT 1;");
            if (rst.next()) {
                String id = rst.getString("student_id");
                int newStudentId = Integer.parseInt(id.replace("S00-", "")) + 1;
                return String.format("S00-%03d", newStudentId);
            } else {
                return "S00-001";
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new id " + e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (tblStudents.getItems().isEmpty()) {
            return "S00-001";
        } else {
            String id = getLastStudentId();
            int newStudentId = Integer.parseInt(id.replace("S", "")) + 1;
            return String.format("S00-%03d", newStudentId);
        }
    }

    private String getLastStudentId() {
        List<StudentTM> tempStudentsList = new ArrayList<>(tblStudents.getItems());
        Collections.sort(tempStudentsList);
        return tempStudentsList.get(tempStudentsList.size() - 1).getStudent_id();
    }
}
