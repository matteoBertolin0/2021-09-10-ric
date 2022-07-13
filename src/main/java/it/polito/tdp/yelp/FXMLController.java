/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	try {
    		txtResult.clear();
    		String str = cmbCitta.getValue();
    		
    		model.creaGrafo(str);
    		
    		txtResult.appendText("Grafo creato con "+model.getGrafo().vertexSet().size()+" vertici e "+model.getGrafo().edgeSet().size()+" archi");
    		
    		cmbB1.getItems().clear();
    		cmbB2.getItems().clear();
    		List<Business> lista = new ArrayList<>();
    		for(Business b :model.getGrafo().vertexSet()) {
    			lista.add(b);
    		}
    		Collections.sort(lista);
    		cmbB1.getItems().addAll(lista);
    		cmbB2.getItems().addAll(lista);
    	}catch(Exception e) {
    		txtResult.setText("Selezionare una città");
    	}
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	try {
    		txtResult.clear();
    		
    		Business b = model.getPiuDistante(cmbB1.getValue());
    		
    		txtResult.appendText("LOCALE PIU' DISTANTE:\n"+ b + " = " + model.getGrafo().getEdgeWeight(model.getGrafo().getEdge(b, cmbB1.getValue())));
    		
    		
    	}catch(Exception e) {
    		txtResult.setText("Selezionare una città");
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	try {
    		txtResult.clear();
    		model.calcolaPercorso(cmbB1.getValue(), cmbB2.getValue(), Integer.parseInt(txtX2.getText()));
    		txtResult.appendText(model.getPercorso().toString());
    		
    		
    	}catch(Exception e) {
    		txtResult.setText("Selezionare una città");
    	}
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	cmbCitta.getItems().clear();
    	
    	List<String> lista = model.getAllCity();
    	
    	Collections.sort(lista);
    	
    	cmbCitta.getItems().addAll(lista);
    }
}
