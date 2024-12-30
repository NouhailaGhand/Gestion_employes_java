package Model;
import DAO.EmployeDAOimpl;
import DAO.HolidayDAOimpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.text.View;

public class EmployeModel {
    private EmployeDAOimpl dao;
    public EmployeModel(EmployeDAOimpl dao) {
        this.dao = dao;
    }

    // funtion of add Employe :
    public boolean addEmploye(int id ,String nom, String prenom, String email, String telephone, double salaire, Role role, Post post, int solde) {
        if(salaire < 0 ){
            System.out.println("Erreur : le salaire doit etre positif.");
            return false;
        }

        if(telephone.length() != 10){
            System.out.println("Erreur : le telephone doit etre 10 num.");
            return false;
        }
        if(!email.contains("@")){
            System.out.println("Erreur : le mail doit contenir le @.");
            return false;
        }
        for(Employe e : new EmployeModel(new EmployeDAOimpl()).displayEmploye()){
            if(e.getEmail().equals(email)){
                return false;
            }
            if(e.getTelephone().equals(telephone)){
                return false;
            }
        }

        Employe e = new Employe(id,nom, prenom, email, telephone, salaire, role, post ,solde);
        
        dao.add(e);

        return true;
    }

    // function of delete Employe :
    public  boolean deleteEmploye(int id){
        for(Holiday e : new HolidayModel(new HolidayDAOimpl()).displayHoliday()){
            if(e.getId_employe()== id){
                return false;
            }
        }
        dao.delete(id);
        return true;
    }

    // function of update Employe :
    public boolean updateEmploye(int id, String nom, String prenom, String email, String telephone, double salaire, Role role, Post post , int solde) {

        Employe e = new Employe(id,nom, prenom, email, telephone, salaire, role, post,solde);
        dao.update(e);
        return true;
    }


    //function of display Employe :
    public List<Employe> displayEmploye() {
        List<Employe> Employes = dao.display();
        return Employes;
    }

    //function of update solde Employe :
    public boolean updateSolde(int id, int solde) {
        dao.updateSolde(id, solde);
        return true;
    }

    private boolean checkFileExists(File file){
        if(!file.exists()){
            throw new IllegalArgumentException("le fichier n'existe pas :" + file.getPath());
        }
        return true;
    }

    private boolean checkIsFile(File file){
        if(!file.isFile()){
            throw new IllegalArgumentException("le chemin specifie n'est pas un fichier :" + file.getPath());
        }
        return true;
    }

    private boolean checkIsReadable(File file){
        if(!file.canRead()){
            throw new IllegalArgumentException("le fichier n'est pas lisible :" + file.getPath());
        }
        return true;
    }

    public void importData(String FileName) {
        File file = new File(FileName);
        checkFileExists(file);
        checkIsFile(file);
        checkIsReadable(file);
        dao.importData(FileName);
    }

    public void exportData(String FileName , List<Employe> data) throws IOException {
        File file = new File(FileName);
        checkFileExists(file);
        checkIsFile(file);
        dao.exportData(FileName, data);
    }
}