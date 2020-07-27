package nl.ebay.creditlimittracker.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import nl.ebay.creditlimittracker.exception.handlers.FileParsingException;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.model.enums.BackendSystem;
import nl.ebay.creditlimittracker.util.DateConverter;
import nl.ebay.creditlimittracker.util.FileUtil;
import nl.ebay.creditlimittracker.util.GlobalConstants;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceCSVImpl implements UserService {
    @Override
    public List<User> getUserData() {
        List<User> usersFromCSV = new ArrayList<>();
        try {
            File file = FileUtil.getFileFromClasspath("/Workbook2.csv");
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (!line[0].equals("Name")) {
                    usersFromCSV.add(User.builder()
                            .name(line[0])
                            .address(line[1])
                            .postCode(line[2])
                            .phone(line[3])
                            .creditLimit(Double.valueOf(line[4]))
                            .dateOfBirth(DateConverter.convertDate(line[5], "dd/MM/yyyy", GlobalConstants.FRONT_END_DATE_FORMAT))
                            .backendSystem(BackendSystem.CSV)
                            .build());
                }
            }
        } catch (IOException | CsvValidationException | ParseException e) {
            throw new FileParsingException("Exception while reading", e);
        }
        return usersFromCSV;
    }
}
