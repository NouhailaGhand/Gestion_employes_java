
package DAO;
import Model.Employe;
import Model.EmployeModel;
import Model.Post;
import Model.Role;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EmployeDAOimpl implements GenericDAOI<Employe> , DataImportExport<Employe> {

    // function of add Employe :
    @Override
    public void add(Employe e) {
        String sql = "INSERT INTO employe (nom, prenom, email, telephone, salaire, role, poste , solde) VALUES (?, ?, ?, ?, ?, ?, ? , ?)";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, e.getNom()); 
            stmt.setString(2, e.getPrenom());
            stmt.setString(3, e.getEmail());
            stmt.setString(4, e.getTelephone());
            stmt.setDouble(5, e.getSalaire());
            stmt.setString(6, e.getRole().name());
            stmt.setString(7, e.getPost().name());
            stmt.setInt(8, e.getSolde());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of add employe ");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    // function of delete Employe :
    @Override
    public void delete(int id) {
        String sql = "DELETE FROM employe WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1,id);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of delete employe");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    // function of update Employe :
    @Override
    public void update(Employe e) {
        String sql = "UPDATE employe SET nom = ?, prenom = ?, email = ?, telephone = ?, salaire = ?, role = ?, poste = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setString(1, e.getNom());
            stmt.setString(2, e.getPrenom());
            stmt.setString(3, e.getEmail());
            stmt.setString(4, e.getTelephone());
            stmt.setDouble(5, e.getSalaire());
            stmt.setString(6, e.getRole().name());
            stmt.setString(7, e.getPost().name());
            stmt.setInt(8,e.getId());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of update employe");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    // function of display Employe :
    @Override
    public List<Employe> display() {
        String sql = "SELECT * FROM employe";
        List<Employe> Employes = new ArrayList<>();
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                int id = re.getInt("id");
                String nom = re.getString("nom");
                String prenom = re.getString("prenom");
                String email = re.getString("email");
                String telephone = re.getString("telephone");
                double salaire = re.getDouble("salaire");
                String role = re.getString("role");
                String poste = re.getString("poste");
                int solde = re.getInt("solde");
                Employe e = new Employe(id,nom, prenom, email, telephone, salaire, Role.valueOf(role), Post.valueOf(poste),solde);
                Employes.add(e);
            }
            return Employes;
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
            return null;
        } catch (SQLException ex) {
            System.err.println("failed of display employe");
            return null;
        }
    }


    // function of update solde Employe :
    public void updateSolde(int id, int solde) {
        String sql = "UPDATE employe SET solde = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, solde);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of update solde employe");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    // function of import data Employe :
    @Override
    public void importData(String FileName) {
        String sql = "INSERT INTO employe (nom, prenom, email, telephone, salaire, role, poste , solde) VALUES (?, ?, ?, ?, ?, ?, ? , ?)";
        try(BufferedReader reader = new BufferedReader(new FileReader(FileName)) ; 
        PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
            if(Integer.parseInt(data[4])< 0 ){
                System.out.println("Erreur : le salaire doit etre positif.");
                throw new IllegalArgumentException();
            }

            if(data[3].length() != 10){
                System.out.println("Erreur : le telephone doit etre 10 num.");
                throw new IllegalArgumentException();
            }
            if(!data[2].contains("@")){
                System.out.println("Erreur : le mail doit contenir le @.");
                throw new IllegalArgumentException();
            }
            for(Employe e : new EmployeModel(new EmployeDAOimpl()).displayEmploye()){
                if(e.getEmail().equals(data[2])){
                    throw new IllegalArgumentException();
                }
                if(e.getTelephone().equals(data[3])){
                    throw new IllegalArgumentException();
                }
            }
                if(data.length == 8){
                    stmt.setString(1, data[0].trim());
                    stmt.setString(2, data[1].trim());
                    stmt.setString(3, data[2].trim());
                    stmt.setString(4, data[3].trim());
                    stmt.setDouble(5, Double.parseDouble(data[4]));
                    stmt.setString(6, data[5].trim());
                    stmt.setString(7, data[6].trim());
                    stmt.setInt(8, Integer.parseInt(data[7]));
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
            System.out.println("Employees imported successfully !");
        } catch (IOException | SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("failed of import data employe");
        }
    }

    // function of export data Employe :
    @Override
    public void exportData(String FileName, List<Employe> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileName))) {
            writer.write("nom,prenom,email,telephone,salaire,role,poste,solde\n");
            writer.newLine();
            for (Employe e : data) {
               String line = String.format("%s , %s , %s , %s ,%.2f, %s , %s ", 
               e.getNom(), e.getPrenom(), e.getEmail(), e.getTelephone(), e.getSalaire(), e.getRole(), e.getPost());
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Employees exported successfully !");
        } catch (IOException exception) {
            System.err.println("failed of export data employe");
        }
    }
}

