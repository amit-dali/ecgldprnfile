package nl.ebay.creditlimittracker.service;

import nl.ebay.creditlimittracker.exception.handlers.FileParsingException;
import nl.ebay.creditlimittracker.model.User;
import nl.ebay.creditlimittracker.model.enums.BackendSystem;
import nl.ebay.creditlimittracker.util.DateConverter;
import nl.ebay.creditlimittracker.util.FileUtil;
import nl.ebay.creditlimittracker.util.GlobalConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

@Service
public class UserServicePrnImpl implements UserService {

    private final int[] fixedStartingPositions;

    @Autowired
    public UserServicePrnImpl(@Value("${prn.fixedStartingPositions}") String fixedStartingPositions) {
        this.fixedStartingPositions = Arrays.stream(fixedStartingPositions.split(",")).mapToInt(Integer::parseInt).toArray();
    }


    @Override
    public List<User> getUserData() {
        List<User> usersFromPrn = new ArrayList<>();
        try {
            File file = FileUtil.getFileFromClasspath("/Workbook2.prn");
            String line;
            BufferedReader br = new BufferedReader(new FileReader(file, ISO_8859_1));
            int iteration = 0;
            while ((line = br.readLine()) != null) {
                if (iteration != 0) {
                    usersFromPrn.add(User.builder()
                            .name(getData(line, 0))
                            .address(getData(line, 1))
                            .postCode(getData(line, 2))
                            .phone(getData(line, 3))
                            .creditLimit(Double.valueOf(getData(line, 4)))
                            .dateOfBirth(DateConverter.convertDate(getData(line, 5), "yyyyMMdd", GlobalConstants.FRONT_END_DATE_FORMAT))
                            .backendSystem(BackendSystem.PRN)
                            .build());

                }
                iteration++;
            }

        } catch (IOException | ParseException e) {
            throw new FileParsingException("Exception while reading", e);
        }

        return usersFromPrn;
    }

    private String getData(String line, int position) {
        int beginIndex = position == 0 ? 0 : fixedStartingPositions[position - 1];
        return line.substring(beginIndex, fixedStartingPositions[position]).trim();
    }

}
