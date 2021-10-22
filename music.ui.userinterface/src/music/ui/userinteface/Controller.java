package music.ui.userinteface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import music.ui.common.Album;
import music.ui.common.Artist;
import music.ui.db.datamodel.Constants;
import music.ui.db.datamodel.Datasource;

import java.util.Optional;

public class Controller {

    @FXML
    private TableView artistTable;
    @FXML
    private ProgressBar progressBar;

    @FXML
    public void listArtists() {
        Task<ObservableList<Artist>> task = new GetAllArtistsTask();
        artistTable.itemsProperty().bind(task.valueProperty());
        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
        task.setOnSucceeded(e -> progressBar.setVisible(false));
        task.setOnFailed(e -> progressBar.setVisible(false));

        new Thread(task).start();
    }

    @FXML
    public void listAlbumsForArtist() {
        try {
            if (artistTable.getSelectionModel().getSelectedItem() instanceof Artist) {

                final Artist artist = (Artist) artistTable.getSelectionModel().getSelectedItem();
                Task<ObservableList<Album>> task = new Task<ObservableList<Album>>() {
                    @Override
                    protected ObservableList<Album> call() throws Exception {
                        return FXCollections.observableArrayList(
                                Datasource.getInstance().queryAlbumsForArtistID(artist.getId())
                        );
                    }
                };
                artistTable.itemsProperty().bind(task.valueProperty());
                new Thread(task).start();

            } else if(artistTable.getSelectionModel().getSelectedItem() == null) {
                nullArtistAlert();
//                System.out.println("NO ARTIST SELECTED");
//                throw new Exception("Selected item isn't an artist or is null");
            }else {
                invalidArtistAlert();
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void invalidArtistAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ERROR!");
        alert.setHeaderText("Selected item is not an artist.");
        alert.setContentText("Press OK to return.");
        alert.getButtonTypes().remove(ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() != ButtonType.OK) {
            alert.close();
        }
    }

    private void nullArtistAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ERROR!");
        alert.setHeaderText("Please select an item.");
        alert.setContentText("Press OK to return.");
        alert.getButtonTypes().remove(ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() != ButtonType.OK) {
            alert.close();
        }
    }


    // A method to test the updating of an artists name.
    @FXML
    public void updateArtist() {
        if (artistTable.getSelectionModel().getSelectedItem() instanceof Artist && artistTable.getSelectionModel().getSelectedItem() != null) {
            final Artist artist = (Artist) artistTable.getItems().get(2);

            Task<Boolean> task = new Task<>() {
                @Override
                protected Boolean call() throws Exception {
                    return Datasource.getInstance().updateArtistName(artist.getId(), "AC/DC");
                }
            };

            task.setOnSucceeded(e -> {
                if (task.valueProperty().get()) {
                    artist.setName("AC/DC");
                    artistTable.refresh();
                }
            });

            new Thread(task).start();
        }
    }

}

class GetAllArtistsTask extends Task {

    @Override
    protected ObservableList<Artist> call() {
        return FXCollections.observableArrayList(
                Datasource.getInstance().queryArtists(Constants.ORDER_BY_ASC)
        );
    }
}
