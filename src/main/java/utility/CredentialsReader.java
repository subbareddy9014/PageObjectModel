package utility;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CredentialsReader {

    @DataProvider(name = "credentials")
    public Object[][] readCredentials() {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(new FileInputStream(new File("src/main/resources/input/credentials.xlsx")));
        } catch (IOException ignore) {
            return null;
        }
        Sheet credentialSheet = workbook.getSheet("credentials");
        int rowCount = credentialSheet.getLastRowNum() - credentialSheet.getFirstRowNum();
        int colCount = 2;
        String[][] credentials = new String[rowCount][colCount];
        for (int i = 1; i < rowCount + 1; i++) {
            if (credentialSheet.getRow(i).getLastCellNum() > 1 && credentialSheet.getRow(i).getCell(0).getStringCellValue()!= null) {
                String userName = credentialSheet.getRow(i).getCell(0).getStringCellValue();
                String password = credentialSheet.getRow(i).getCell(1).getStringCellValue();
                credentials[i-1][0] = userName;
                credentials[i-1][1] = password;
            }
        }
        return credentials;
    }
}
