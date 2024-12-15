package Controller;

import Model.Api;
import Model.SearchModel;
import View.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainController {
    private SearchModel model;
    private MainView view;
    private Api api;

    public MainController(SearchModel model, MainView view) {
        this.model = model;
        this.view = view;
        this.api = new Api(); // Create an instance of Api

        // Add listeners for the search and navigation buttons
        this.view.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        this.view.getHomeButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showPage("HomePage");
            }
        });

        this.view.getCollectionButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showPage("MyCollectionPage");
            }
        });
    }
    
    private void handleSearch() {
        String searchText = view.getSearchText(); // Texte dans le champ de recherche
        String searchType = (String) view.getSearchTypeComboBox().getSelectedItem(); // Type de recherche

        if (searchText == null || searchText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please enter a search term.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        model.setSearchText(searchText); // Mettre à jour le modèle avec le texte de recherche

        try {
            String result;
            if ("Character".equals(searchType)) {
                // Recherche par personnage
                result = api.searchComicsByCharacter(searchText,20);
            } else {
                // Recherche par titre
                result = api.searchByTitle(searchText,20);
            }

            // Afficher les résultats dans un dialogue
            JOptionPane.showMessageDialog(view, result, "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Error during API request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    
}
