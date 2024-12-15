package Controller;


//Point d'entrée de l'application

import Model.SearchModel;
import View.MainView;

public class MainApp {
    public static void main(String[] args) {
        // Initialisation du modèle, de la vue et du contrôleur
        SearchModel model = new SearchModel();
        MainView view = new MainView();
        MainController controller = new MainController(model, view);

        // Afficher la fenêtre principale
        view.setVisible(true);
    }
}
