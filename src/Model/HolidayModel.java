package Model;
import Controller.EmployeController;
import DAO.HolidayDAOimpl;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
public class HolidayModel {
    private HolidayDAOimpl dao;

    public HolidayModel(HolidayDAOimpl dao) {
        this.dao = dao;
    }

    public boolean addHoliday(int id, int id_employe, Date startdate, Date enddate, Type_holiday type , Employe targetEmploye) {
        
        if(startdate.after(enddate)) return false;
        if(startdate.equals(enddate)) return false;
        if(startdate.before(new Date(System.currentTimeMillis()))) return false;
        if(enddate.before(new Date(System.currentTimeMillis()))) return false;
        for (Holiday holiday : new HolidayModel(new HolidayDAOimpl()).displayHoliday()) {
                if (holiday.getId_employe() == id_employe) {
                    if(startdate.after(holiday.getStartDate()) && startdate.before(holiday.getEndDate()) && enddate.before(holiday.getEndDate()) && enddate.after(holiday.getStartDate())){
                        System.out.println("le cong s est superpose a un autre cong");
                        return false;
                    }

                    if(startdate.after(holiday.getStartDate()) && startdate.before(holiday.getEndDate()) && enddate.after(holiday.getEndDate())){
                        startdate = holiday.getEndDate();
                        continue;
                    }
                    if(startdate.before(holiday.getStartDate()) && enddate.after(holiday.getStartDate()) && enddate.before(holiday.getEndDate())){
                        enddate = holiday.getStartDate();
                        continue;
                    }
                    break;
                }
        }
        
        long daysBetween = (enddate.toLocalDate().toEpochDay() - startdate.toLocalDate().toEpochDay());
        if(daysBetween > targetEmploye.getSolde()) return false;
        EmployeController.updateSolde(targetEmploye.getId(), targetEmploye.getSolde() - (int) daysBetween);
        Holiday e = new Holiday(id, id_employe, startdate, enddate, type);
        dao.add(e);
        return true;
    }


    public List<Holiday> displayHoliday() {
        List<Holiday> Holidays = dao.display();
        return Holidays;
    }

    public boolean deleteHoliday(int id) {
        dao.delete(id);
        return true;
    }

    public boolean updateHoliday(int id, int id_employe, Date startdate, Date enddate, Type_holiday type , Employe targetEmploye , int olddaysbetween ) {

        long daysBetween = (enddate.toLocalDate().toEpochDay() - startdate.toLocalDate().toEpochDay());

        if(startdate.after(enddate)) return false;
        if(startdate.equals(enddate)) return false;
        if(startdate.before(new Date(System.currentTimeMillis()))) return false;
        if(enddate.before(new Date(System.currentTimeMillis()))) return false;

        if(daysBetween > (targetEmploye.getSolde()+olddaysbetween)) return false;
        for (Holiday holiday : new HolidayModel(new HolidayDAOimpl()).displayHoliday()) {
            if (holiday.getId_employe() == id_employe) {
                if(startdate.after(holiday.getStartDate()) && startdate.before(holiday.getEndDate()) && enddate.before(holiday.getEndDate()) && enddate.after(holiday.getStartDate())){
                    System.out.println("le cong s est superpose a un autre cong");
                    return false;
                }

                if(startdate.after(holiday.getStartDate()) && startdate.before(holiday.getEndDate()) && enddate.after(holiday.getEndDate())){
                    startdate = holiday.getEndDate();
                    continue;
                }
                if(startdate.before(holiday.getStartDate()) && enddate.after(holiday.getStartDate()) && enddate.before(holiday.getEndDate())){
                    enddate = holiday.getStartDate();
                    continue;
                }
                break;
            }
        }
        EmployeController.updateSolde(targetEmploye.getId(), (targetEmploye.getSolde()+olddaysbetween) - (int) daysBetween);

        Holiday e = new Holiday(id, id_employe, startdate, enddate, type);
        dao.update(e);
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

    public void importData(String FileName){
        File file = new File(FileName);
        checkFileExists(file);
        checkIsFile(file);
        checkIsReadable(file);
        dao.importData(FileName);
    }

    public void exportData(String FileName , List<Holiday> data) throws IOException {
        File file = new File(FileName);
        checkFileExists(file);
        checkIsFile(file);
        dao.exportData(FileName, data);
    }

}
