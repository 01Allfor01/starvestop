package com.allforone.starvestop.domain.admin;

import com.allforone.starvestop.common.exception.CustomException;
import com.allforone.starvestop.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public GetAdminResponse getAdmin(Long adminId) {
        Admin admin = getById(adminId);
        return GetAdminResponse.from(admin.getId(), admin.getEmail());
    }

    @Transactional
    public UpdateAdminResponse updateAdmin(Long adminId, UpdateAdminRequest request) {
        String password = request.getPassword();

        Admin foundAdmin = getById(adminId);

        foundAdmin.update(password);

        return new UpdateAdminResponse(
                foundAdmin.getEmail()
        );
    }

    @Transactional
    public void deleteAdmin(Long adminId) {
        Admin foundAdmin = getById(adminId);
        foundAdmin.delete();
    }

    public void existsByEmail(String email) {
        if (adminRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public Admin save(String email, String password) {
        Admin admin = Admin.create(email, password);
        return adminRepository.save(admin);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
    }

    public Admin getById(Long id) {
        return adminRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
