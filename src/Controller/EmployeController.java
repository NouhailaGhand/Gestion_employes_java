package Controller;

// Importation des classes nécessaires
import Model.*;
import View.*;

import java.sql.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import DAO.EmployeDAOimpl;
import DAO.HolidayDAOimpl;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class EmployeController {

    private final Employe_HolidayView View;
    public static EmployeModel model_employe ;
    public static int id = 0;
    public static int oldselectedrow = -1;
    public static boolean test = false;
    String nom = "";
    String prenom = "";
    String email = "";
    String telephone = "";
    double salaire = 0;
    Role role = null;
    Post poste = null;
    int solde = 0;
    boolean updatereussi = false;

    public EmployeController(Employe_HolidayView view, EmployeModel model) {
        this.View = view;
        this.model_employe = model;

        displayEmploye();
        // Action Listener :
        View.getaddButton_employe().addActionListener(e -> addEmploye());
        View.getdeleteButton_employe().addActionListener(e -> deleteEmploye());
        View.getupdateButton_employe().addActionListener(e -> updateEmploye());
        View.getdisplayButton_employe().addActionListener(e -> displayEmploye());
        View.getCreateAcconte().addActionListener(e -> createAcconte());
        View.getImportButton().addActionListener(e -> handleImport());
        View.getExportButton().addActionListener(e -> handleExport());

        // Selection Listener :
        Employe_HolidayView.Tableau.getSelectionModel().addListSelectionListener(e -> updateEmployebyselect());
    }


    //function of display Employe :
    public void displayEmploye() {
        List<Employe> Employes = model_employe.displayEmploye();
        if(Employes.isEmpty()){
            View.afficherMessageErreur("Aucun employe.");
        }
        DefaultTableModel tableModel = (DefaultTableModel) Employe_HolidayView.Tableau.getModel();
        tableModel.setRowCount(0);
        for(Employe e : Employes){
            tableModel.addRow(new Object[]{e.getId(), e.getNom(), e.getPrenom(), e.getEmail(), e.getTelephone(), e.getSalaire(), e.getRole(), e.getPost(),e.getSolde()});
        }
        // remplir la liste des employes dans le holiday automatique :
        View.remplaire_les_employes();
    }

    
    // function of add Employe :
    private void addEmploye() {
        String nom = View.getNom();
        String prenom = View.getPrenom();
        String email = View.getEmail();
        String telephone = View.getTelephone();
        double salaire = View.getSalaire();
        Role role = View.getRole();
        Post poste = View.getPoste();

        View.viderChamps_em();
        boolean addreussi = model_employe.addEmploye(0,nom, prenom, email, telephone, salaire, role, poste ,25);

        if(addreussi == true){
            View.afficherMessageSucces("L'employe a bien ete ajoutee.");
            displayEmploye();
        }else{
            View.afficherMessageErreur("L'employe n'a pas ete ajoutee.");
        }
    }



    // function of delete Employe : 
    private void deleteEmploye(){
        int selectedrow = Employe_HolidayView.Tableau.getSelectedRow();
        if(selectedrow == -1){
            View.afficherMessageErreur("Veuillez selectionner une ligne.");
        }else{
            int id = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 0);
            if(model_employe.deleteEmploye(id)){
                View.afficherMessageSucces("L'employe a bien ete supprimer.");
                displayEmploye();
            }else{
                View.afficherMessageErreur("L'employe a une relation avec un holiday.");
            }
        }
    }

    // function of Update :
        // function one of fetch data employe by select and display it in the forme:
    private void updateEmployebyselect(){
        int selectedrow = Employe_HolidayView.Tableau.getSelectedRow();

        if (selectedrow == -1) {
            return;
        }
        try{
            id = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 0);
            nom = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 1);
            prenom = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 2);
            email = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 3);
            telephone = (String) Employe_HolidayView.Tableau.getValueAt(selectedrow, 4);
            salaire = (double) Employe_HolidayView.Tableau.getValueAt(selectedrow, 5);
            role = (Role) Employe_HolidayView.Tableau.getValueAt(selectedrow, 6);
            poste = (Post) Employe_HolidayView.Tableau.getValueAt(selectedrow, 7);
            solde = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 8);
            View.remplaireChamps_em(id, nom, prenom, email, telephone, salaire, role, poste);
            test = true;
        }catch(Exception e){
             View.afficherMessageErreur("Erreur lors de la récupération des données");
        }
    }

        // function two of update Employe by click update button :
    private void updateEmploye(){
        if (!test) {
            View.afficherMessageErreur("Veuillez d'abord sélectionner une ligne à modifier.");
            return;
        }
        try {
            nom = View.getNom();
            prenom = View.getPrenom();
            email = View.getEmail();
            telephone = View.getTelephone();
            salaire = View.getSalaire();
            role = View.getRole();
            poste = View.getPoste();
    
            boolean updateSuccessful = model_employe.updateEmploye(id, nom, prenom, email, telephone, salaire, role, poste , solde);
    
            if (updateSuccessful) {
                test = false; 
                View.afficherMessageSucces("L'employé a été modifié avec succès.");
                displayEmploye();
                View.viderChamps_em();
            } else {
                View.afficherMessageErreur("Erreur lors de la mise à jour de l'employé.");
            }
        } catch (Exception e) {
            
            View.afficherMessageErreur("Erreur lors de la mise à jour");
        }
    }

    // function of update solde :
    public static void updateSolde(int  id , int solde){
       model_employe.updateSolde(id, solde);
    }

    public void createAcconte(){
        int selectedrow = Employe_HolidayView.Tableau.getSelectedRow();
        if(selectedrow == -1){
            View.afficherMessageErreur("Veuillez selectionner une ligne.");
        }else{
            View.CreateAccountView createConteView = new CreateAccountView();
            int id = (int) Employe_HolidayView.Tableau.getValueAt(selectedrow, 0);
        }
    }

    private void handleImport(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers CSV", "txt"));

        if(fileChooser.showOpenDialog(View) == JFileChooser.APPROVE_OPTION){
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                model_employe.importData(filePath);
                displayEmploye();
                View.afficherMessageSucces("Importation réussie.");
            } catch (Exception e) {
                View.afficherMessageErreur("Erreur lors de l'importation :"+e.getMessage());
            }
        }
    }

    private void handleExport(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers CSV", "txt"));

        if(fileChooser.showSaveDialog(View) == JFileChooser.APPROVE_OPTION){
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if(!filePath.toLowerCase().endsWith(".txt")){
                    filePath += ".txt";
                }
                List<Employe> employes = model_employe.displayEmploye();
                model_employe.exportData(filePath , employes);
                View.afficherMessageSucces("Exportation réussie.");
            } catch (Exception e) {
                View.afficherMessageErreur("Erreur lors de l'exportation :"+e.getMessage());
            }
        }
    }

}