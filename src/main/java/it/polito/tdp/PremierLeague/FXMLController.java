/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.PlayerMinutes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="btnCreaGrafo"
	private Button btnCreaGrafo; // Value injected by FXMLLoader

	@FXML // fx:id="btnTopPlayer"
	private Button btnTopPlayer; // Value injected by FXMLLoader

	@FXML // fx:id="btnDreamTeam"
	private Button btnDreamTeam; // Value injected by FXMLLoader

	@FXML // fx:id="txtK"
	private TextField txtK; // Value injected by FXMLLoader

	@FXML // fx:id="txtGoals"
	private TextField txtGoals; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	@FXML
	void doCreaGrafo(ActionEvent event) {
		this.txtResult.clear();
		Double goals;
		try {
			goals = Double.parseDouble(this.txtGoals.getText());
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserisci un numero di goal valido");
			return;
		}
		if (goals < 0) {
			this.txtResult.appendText("Inserire un numero maggiore di 0");
			return;
		}

		this.model.creaGrafo(goals);

	}

	@FXML
	void doDreamTeam(ActionEvent event) {
		this.txtResult.clear();

		Integer k;
		try {
			k = Integer.parseInt(this.txtK.getText());
		} catch (NumberFormatException e) {
			this.txtResult.appendText("Inserire un numero valido");
			return;
		}

		if (k < 0) {
			this.txtResult.appendText("Inserire un numero valido");
			return;
		}

		Set<Player> dreamTeam = this.model.generaDreamTeam(k);

		if (dreamTeam == null) {
			this.txtResult.appendText("Creare il grafo");
			return;
		}

		this.txtResult.appendText("Grado do titolarità: " + this.model.getTitolarita());
		this.txtResult.appendText("\n\nGiocatori: \n");

		for (Player p : dreamTeam) {
			this.txtResult.appendText(p + "\n");
		}

	}

	@FXML
	void doTopPlayer(ActionEvent event) {
		this.txtResult.clear();

		Player best = this.model.getTopPlayer();

		if (best == null) {
			this.txtResult.appendText("Creare il grafo");
			return;
		}

		this.txtResult.appendText("Miglior giocatore: " + best + "\n\n");

		for (PlayerMinutes pm : this.model.getBattuti(best)) {
			this.txtResult.appendText(
					String.format("Minuti per cui ha battuto %s : %d\n", pm.getPlayer(), pm.getMinutes().intValue()));
		}

	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
		assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

	}

	public void setModel(Model model) {
		this.model = model;
	}
}
