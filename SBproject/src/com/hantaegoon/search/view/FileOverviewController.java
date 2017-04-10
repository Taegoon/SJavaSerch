package com.hantaegoon.search.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.DirectoryChooser;

public class FileOverviewController implements Initializable {
	@FXML
	private TreeView<String> fileTreeview;
	@FXML
	private TextField namefield;
	@FXML
	private TextField valuefield;

	private final static String defaultPath = "C:/Users/tae/";
	private File choice;

	private static void configureFileChooser(final DirectoryChooser directoryChooser) {
		directoryChooser.setTitle("Select Fold");
		directoryChooser.setInitialDirectory(new File(defaultPath));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void NfileFoldbtn(ActionEvent event) {
		System.out.println("Set Name Folder Path!!!!!!!!!");
		final DirectoryChooser directoryChooserName = new DirectoryChooser();
		configureFileChooser(directoryChooserName);
		choice = directoryChooserName.showDialog(null);

		if (choice == null || !choice.isDirectory()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Could not open directory");
			alert.setContentText("The file is invalid.");

			alert.showAndWait();
		} else {
			fileTreeview.setRoot(getNodesForDirectory(choice));
			fileTreeview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

				@Override
				public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {

					@SuppressWarnings("unchecked")
					TreeItem<String> selectedItem = (TreeItem<String>) newValue;
					System.out.println("Selected Text : " +choice.getAbsolutePath()+"\\"+ selectedItem.getValue());
					// do what ever you want
				}

			});

		}

	}

	public void VfileFoldbtn(ActionEvent event) {
		System.out.println("Set Value FolderPath~~~~~~~~~");

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			System.out.println("selectedDirectory.getAbsolutePath(): "+selectedDirectory.getAbsolutePath());
		}

	}

	public void searchNamebtn(ActionEvent event) {
		System.out.println("Search File Button***********");
		System.out.println("Input Name: " + namefield.getText().toLowerCase());

	}

	public void searchValuebtn(ActionEvent event) {
		System.out.println("Search Value Button++++++++++");

	}

	public TreeItem<String> getNodesForDirectory(File directory) {
		// Returns a TreeItem representation of the specified directory
		TreeItem<String> root = new TreeItem<String>(directory.getName());
		for (File f : directory.listFiles()) {
			System.out.println("Loading : " + f.getName());
			if (f.isDirectory()) { // Then we call the function recursively
				root.getChildren().add(getNodesForDirectory(f));
			} else {
				root.getChildren().add(new TreeItem<String>(f.getName()));
			}
		}
		return root;
	}

}
