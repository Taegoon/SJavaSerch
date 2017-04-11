package com.hantaegoon.search.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.function.Function;

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
	private TreeView<FilePath> fileTreeview = new TreeView<FilePath>();
	@FXML
	private TextField namefield;
	@FXML
	private TextField valuefield;

	private static String defaultPath = "C:/Users/tae/";
	private File choice;
	private TreeItem<FilePath> rootTreeItem;

	private static void configureFileChooser(final DirectoryChooser directoryChooser) {
		directoryChooser.setTitle("Select Fold");
		directoryChooser.setInitialDirectory(new File(defaultPath));

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void NfileFoldbtn(ActionEvent event) throws IOException {
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
			// create tree
			defaultPath = choice.getAbsolutePath();
			createTree(defaultPath);
			fileTreeview.setRoot(rootTreeItem);
			fileTreeview.getSelectionModel().selectedItemProperty()
					.addListener((observable, oldValue, newValue) -> handle(newValue));

		}

	}

	public void VfileFoldbtn(ActionEvent event) {
		System.out.println("Set Value FolderPath~~~~~~~~~");

		final DirectoryChooser directoryChooser = new DirectoryChooser();
		final File selectedDirectory = directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			System.out.println("selectedDirectory.getAbsolutePath(): " + selectedDirectory.getAbsolutePath());
		}

	}

	public void searchNamebtn(ActionEvent event) {
		System.out.println("Search File Button***********");
		System.out.println("Input Name: " + namefield.getText().toLowerCase());

		filterChanged(namefield.getText().toString());

	}

	public void searchValuebtn(ActionEvent event) {
		System.out.println("Search Value Button++++++++++");

	}

	/**
	 * Create original tree structure
	 * This is filtering and create tree view method
	 * @throws IOException
	 */
	private void createTree(String path) throws IOException {

		// create root
		rootTreeItem = createTreeRoot(path);

		// create tree structure recursively
		createTree(rootTreeItem);

		// sort tree structure by name
		rootTreeItem.getChildren().sort(Comparator.comparing(new Function<TreeItem<FilePath>, String>() {
			@Override
			public String apply(TreeItem<FilePath> t) {
				return t.getValue().toString().toLowerCase();
			}
		}));

	}

	/**
	 * Iterate through the directory structure and create a file tree
	 * 
	 * @param rootItem
	 * @throws IOException
	 */
	public static void createTree(TreeItem<FilePath> rootItem) throws IOException {

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue().getPath())) {

			for (Path path : directoryStream) {

				TreeItem<FilePath> newItem = new TreeItem<FilePath>(new FilePath(path));
				newItem.setExpanded(true);

				rootItem.getChildren().add(newItem);

				if (Files.isDirectory(path)) {
					createTree(newItem);
				}
			}
		}
		// catch exceptions, e. g. java.nio.file.AccessDeniedException:
		// c:\System Volume Information, c:\$RECYCLE.BIN
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Create new filtered tree structure
	 * 
	 * @param root
	 * @param filter
	 * @param filteredRoot
	 */
	private void filter(TreeItem<FilePath> root, String filter, TreeItem<FilePath> filteredRoot) {

		for (TreeItem<FilePath> child : root.getChildren()) {

			TreeItem<FilePath> filteredChild = new TreeItem<>(child.getValue());
			filteredChild.setExpanded(true);

			filter(child, filter, filteredChild);

			if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
				filteredRoot.getChildren().add(filteredChild);
			}

		}
	}

	/**
	 * Comparator for tree filter
	 * 
	 * @param value
	 * @param filter
	 * @return
	 */
	private boolean isMatch(FilePath value, String filter) {
		return value.toString().toLowerCase().contains(filter.toLowerCase());
		// TODO: optimize or change (check file extension, etc)
	}

	/**
	 * Show original tree or filtered tree depending on filter
	 * 
	 * @param filter
	 */
	private void filterChanged(String filter) {
		if (filter.isEmpty()) {
			fileTreeview.setRoot(rootTreeItem);
		} else {
			TreeItem<FilePath> filteredRoot = createTreeRoot(defaultPath);
			filter(rootTreeItem, filter, filteredRoot);
			fileTreeview.setRoot(filteredRoot);
		}
	}

	/**
	 * Create root node. Used for the original tree and the filtered tree.
	 * Another option would be to clone the root.
	 * 
	 * @return
	 */
	private TreeItem<FilePath> createTreeRoot(String path) {
		TreeItem<FilePath> root = new TreeItem<FilePath>(new FilePath(Paths.get(path)));
		root.setExpanded(true);
		return root;
	}

	/**
	 * Wrapper for the path with overwritte toString method. We only want to see
	 * the last path part as tree node, not the entire path.
	 */
	private static class FilePath {

		Path path;
		String text;

		public FilePath(Path path) {

			this.path = path;

			// display text: the last path part
			// consider root, e. g. c:\
			if (path.getNameCount() == 0) {
				this.text = path.toString();
			}
			// consider folder structure
			else {
				this.text = path.getName(path.getNameCount() - 1).toString();
			}

		}

		public Path getPath() {
			return path;
		}

		public String toString() {

			// hint: if you'd like to see the entire path, use this:
			// return path.toString();
			// show only last path part
			return text;

		}
	}

	private void handle(TreeItem<FilePath> newValue) {
		// TODO Auto-generated method stub
		System.out.println("Selected Text : " + choice.getAbsolutePath() + "\\" + newValue.getValue());
	}
}
