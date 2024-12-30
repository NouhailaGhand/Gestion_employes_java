
package DAO;
import Model.Employe;
import Model.EmployeModel;
import Model.Holiday;
import Model.Type_holiday;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HolidayDAOimpl implements GenericDAOI<Holiday> , DataImportExport<Holiday> {

    @Override
    public void add(Holiday e) {
        String sql = "INSERT INTO holiday (id_employe, startdate, enddate, type) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1, e.getId_employe());
            stmt.setDate(2, e.getStartDate());
            stmt.setDate(3, e.getEndDate());
            stmt.setString(4, e.getType().name());

            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of add holiday");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM holiday WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            stmt.setInt(1,id);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of delete holiday");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    @Override
    public void update(Holiday e) {
        String sql = "UPDATE holiday SET id_employe = ?, startdate = ?, enddate = ?, type = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)){
            stmt.setInt(1, e.getId_employe());
            stmt.setDate(2, e.getStartDate());
            stmt.setDate(3, e.getEndDate());
            stmt.setString(4, e.getType().name());
            stmt.setInt(5,e.getId_holiday());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            System.err.println("failed of update holiday");
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
        }
    }

    @Override
    public List<Holiday> display() {
        String sql = "SELECT * FROM holiday";
        List<Holiday> Holidays = new ArrayList<>();
        try (PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                int id = re.getInt("id");
                int id_employe = re.getInt("id_employe");
                Date startdate = re.getDate("startdate");
                Date enddate = re.getDate("enddate");
                String type = re.getString("type");
                Holiday e = new Holiday(id,id_employe,startdate, enddate,Type_holiday.valueOf(type));
                Holidays.add(e);
            }
            return Holidays;
        } catch (ClassNotFoundException ex) {
            System.err.println("failed connexion with data base");
            return null;
        }catch (SQLException ex) {
            System.err.println("failed of display holidays");
            return null;
        }
    }

    @Override
    public void importData(String FileName) {
        String sql = "INSERT INTO holiday (id_employe, startdate, enddate, type) VALUES (?, ?, ?, ?)";
        try(BufferedReader reader = new BufferedReader(new FileReader(FileName)) ; 
        PreparedStatement stmt = DBConnexion.getConnexion().prepareStatement(sql)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if(data.length == 4){
                    for(Employe e : new EmployeModel(new EmployeDAOimpl()).displayEmploye()){
                        if(e.getEmail().equals(data[0].trim())){
                            stmt.setInt(1, e.getId());
                            break;
                        }
                    }
                    stmt.setDate(2, Date.valueOf(data[1].trim()));
                    stmt.setDate(3, Date.valueOf(data[2].trim()));
                    stmt.setString(4, data[3].trim());
                    stmt.addBatch();
                    System.out.println("holidayes imported successfully !");
                }
            }
            stmt.executeBatch();
            
        } catch (IOException | SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
            System.err.println("failed of import data holiday");
        }
    }

    //function of export data Employe :
    @Override
    public void exportData(String FileName, List<Holiday> data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FileName))) {
            writer.write("nom,prenom,startDate,endDate,type\n");
            writer.newLine();
            for (Holiday e : data) {
                for (Employe emp : new EmployeModel(new EmployeDAOimpl()).displayEmploye()) {
                    if (emp.getId() == e.getId_employe()) {
                        String line = String.format("%s,%s,%s,%s,%s",
                                emp.getNom(), emp.getPrenom(), e.getStartDate(), e.getEndDate(), e.getType().name());
                        writer.write(line);
                        writer.newLine();
                        break;
                    }
                }
            }
            System.out.println("Employees exported successfully!");
        } catch (IOException exception) {
            System.err.println("failed to export data employe");
        }
    }

}

