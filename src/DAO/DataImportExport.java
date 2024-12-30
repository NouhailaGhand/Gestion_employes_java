package DAO;

import java.io.IOException;
import java.util.List;

public interface DataImportExport<T>{
    void importData(String FileName) throws IOException;
    void exportData(String FileName , List<T> data) throws IOException;
}
