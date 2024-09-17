package com.poly.Recruitment.service.serviceImplement;

import com.poly.Recruitment.entity.User;
import com.poly.Recruitment.repository.UserDAO;
import com.poly.Recruitment.service.UserService;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    @Override
    public User save(User user) {
        return userDAO.save(user);
    }

    @Override
    public User update(User user) {
        return userDAO.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userDAO.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }
    
    @Override
    public ByteArrayInputStream exportUsersToExcel() throws IOException {
        List<User> users = userDAO.findAll(); // Lấy danh sách người dùng từ DAO

        String[] columns = {"ID", "Name", "Email", "Phone", "Role", "Address", "Photo"}; // Thêm các cột mới

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Users");

            // Tạo dòng header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // Tạo các dòng dữ liệu
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(user.getUserID());
                row.createCell(1).setCellValue(user.getName());
                row.createCell(2).setCellValue(user.getEmail());
                row.createCell(3).setCellValue(user.getPhone());
                row.createCell(4).setCellValue(user.getRole());
                row.createCell(5).setCellValue(user.getAddress() != null ? user.getAddress() : ""); // Địa chỉ
                row.createCell(6).setCellValue(user.getPhoto() != null ? user.getPhoto() : ""); // Đường dẫn ảnh hoặc thông tin ảnh
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
