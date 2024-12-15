package View;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/*Classe permettant d'afficher les différents éléments de la page d'accueil 
 * ainsi que la page de collection 
 */

public class MainView extends JFrame {
    // Composants de l'en-tête (Header)
    private JPanel headerPanel;
    private JButton homeButton;
    private JButton collectionButton;
    private JTextField searchField;
    private JButton searchButton;
    private JLabel logoLabel;  

    // Composants du pied de page (Footer)
    private JPanel footerPanel;
    private JLabel footerLabel;

    // Zone centrale et gestion des pages
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Pages
    private JPanel homePage;
    private JPanel collectionPage;
    private JPanel searchResultsPage;
    
    
    private JComboBox<String> searchTypeComboBox; // Déclaration du JComboBox


    public MainView() {
        // Initialisation de la fenêtre principale
        setTitle("Application MVC");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialisation du layout principal
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialiser les pages
        setupHeader();
        setupPages();
        setupFooter();

        // Ajouter les zones à la fenêtre principale
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(headerPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Afficher la fenêtre
        setVisible(true);
    }
    
    
    private void setupHeader() {
        headerPanel = new JPanel(new BorderLayout());

        // Charger et redimensionner l'image tout en conservant ses proportions
        ImageIcon logoIcon = new ImageIcon("src/resources/LOGO.png");
        Image img = logoIcon.getImage();

        // Récupérer les dimensions originales
        int originalWidth = logoIcon.getIconWidth();
        int originalHeight = logoIcon.getIconHeight();

        // Dimensions images
        int targetHeight = 50; // Hauteur cible
        int targetWidth = (int) ((double) targetHeight / originalHeight * originalWidth); 

        // Redimensionner l'image
        Image resizedImage = img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);

        logoLabel = new JLabel(new ImageIcon(resizedImage));
        logoLabel.setPreferredSize(new Dimension(targetWidth, targetHeight)); 

        // Onglets et panneau de recherche (inchangés)
        JPanel tabPanel = new JPanel();
        homeButton = new JButton("Home");
        collectionButton = new JButton("My Collection");
        tabPanel.add(homeButton);
        tabPanel.add(collectionButton);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        String[] searchOptions = {"Character", "Title"};
        searchTypeComboBox = new JComboBox<>(searchOptions);
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(tabPanel, BorderLayout.CENTER);
        headerPanel.add(searchPanel, BorderLayout.EAST);
    }

    
    public JComboBox<String> getSearchTypeComboBox() {
        return searchTypeComboBox;
    }

    // Configuration des pages
    private void setupPages() {
        ///--- Page d'accueil (HomePage) ---///
        JPanel homePage = new JPanel();
        homePage.setLayout(new BoxLayout(homePage, BoxLayout.Y_AXIS)); 

        // Création des sections
        JPanel section1 = createSection("Section 1");
        JPanel section2 = createSection("Section 2");
        JPanel section3 = createSection("Section 3");

        // Espaces entre les sections
        homePage.add(section1);
        homePage.add(Box.createVerticalStrut(20)); // Espace de 20 pixels entre les sections
        homePage.add(section2);
        homePage.add(Box.createVerticalStrut(20)); // Espace de 20 pixels entre les sections
        homePage.add(section3);

        ///--- Page de la collection (CollectionPage) ---///
        collectionPage = new JPanel(new BorderLayout());
        collectionPage.add(new JLabel("Welcome to My Collection Page!"), BorderLayout.CENTER);
        
        ///--- Page des résultats de recherche (SearchResultsPage) ---///
        searchResultsPage = new JPanel(new BorderLayout());
        searchResultsPage.add(new JLabel("Search Results"), BorderLayout.NORTH);

        // Ajouter les pages au panel avec CardLayout
        cardPanel.add(homePage, "HomePage");
        cardPanel.add(collectionPage, "MyCollectionPage");
        cardPanel.add(searchResultsPage, "SearchResultsPage");

        // Afficher la HomePage par défaut
        cardLayout.show(cardPanel, "HomePage");
    }

    // Créer une section (panel avec un label)
    private JPanel createSection(String text) {
        JPanel section = new JPanel();
        section.setBackground(Color.LIGHT_GRAY);  // Couleur de fond pour la section
        section.add(new JLabel(text));  // Ajouter un label pour indiquer la section
        return section;
    }

    // Configuration du pied de page (Footer)
    private void setupFooter() {
        footerPanel = new JPanel();
        footerLabel = new JLabel("@zapcomics");
        footerPanel.add(footerLabel);
    }

    // Getter pour les boutons et autres composants
    public JButton getHomeButton() {
        return homeButton;
    }

    public JButton getCollectionButton() {
        return collectionButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void updateSearchResults(String results) {
        // Supprimer les anciens résultats
        searchResultsPage.removeAll(); 

        // Divise la recherche en lignes distinctes
        String[] resultLines = results.split("\n");

        // Create a panel to hold the results
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        // Add each result line with a button
        for (String line : resultLines) {
            JPanel resultLinePanel = new JPanel(new BorderLayout());
            JLabel resultLabel = new JLabel(line);
            JButton actionButton = new JButton("Action");

            // Add action listener to the button
            actionButton.addActionListener(e -> {
                // Show a message dialog with the line text
                JOptionPane.showMessageDialog(this, "Button clicked for: " + line);
            });

            resultLinePanel.add(resultLabel, BorderLayout.CENTER);
            resultLinePanel.add(actionButton, BorderLayout.EAST);
            resultsPanel.add(resultLinePanel);
        }

        // Ajouter une barre de défilement
        searchResultsPage.add(new JScrollPane(resultsPanel), BorderLayout.CENTER);

        // Rafraîchir l'affichage
        searchResultsPage.revalidate();
        searchResultsPage.repaint();
    }


    // Méthode pour changer de page (vue)
    public void showPage(String pageName) {
        cardLayout.show(cardPanel, pageName);
    }
}
