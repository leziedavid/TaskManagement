package com.mobisoft.taskmanagement.dto;
import java.util.List;
import com.mobisoft.taskmanagement.entity.Gender;
import com.mobisoft.taskmanagement.entity.Role;
import lombok.Data;

@Data
public class UserDepartmentDTO {
    private Long userId;
    private String lastname;
    private String firstname;
    private String phone;
    private String email;
    private String username;
    private String fonction;
    private String token;
    private String profil;
    private int otp;
    private Integer isValid;
    private Gender genre;
    private Role role;  // Ajout du champ role
    private List<DepartmentDTO> departments;
    private List<LeaveDTO> leaves;
    private String usersCreatedAt;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public int getOtp() { return otp; }
    public void setOtp(int otp) { this.otp = otp; }

    public String getProfil() { return profil; }
    public void setProfil(String profil) { this.profil = profil; }

    public Integer getIsValid() { return isValid; }
    public void setIsValid(Integer isValid) { this.isValid = isValid; }

    public Gender getGenre() { return genre; }
    public void setGenre(Gender genre) { this.genre = genre; }

    public Role getRole() { return role; }  // Getter pour role
    public void setRole(Role role) { this.role = role; }  // Setter pour role

    public List<DepartmentDTO> getDepartments() { return departments; }
    public void setDepartments(List<DepartmentDTO> departments) { this.departments = departments; }

    public static class DepartmentDTO {
        private Long departmentId;
        private String departmentName;

        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    }

    // public static class LeaveDTO {
    //     private Long leaveId;
    //     private Long userId; // Identifiant de l'utilisateur
    //     private OffsetDateTime startDate;
    //     private OffsetDateTime endDate;
    //     private String leaveType;
    //     private String status;
    //     private String description;

    //     public Long getLeaveId() {
    //         return leaveId;
    //     }

    //     public void setLeaveId(Long leaveId) {
    //         this.leaveId = leaveId;
    //     }

    //     public Long getUserId() {
    //         return userId;
    //     }

    //     public void setUserId(Long userId) {
    //         this.userId = userId;
    //     }

    //     public OffsetDateTime getStartDate() {
    //         return startDate;
    //     }

    //     public void setStartDate(OffsetDateTime startDate) {
    //         this.startDate = startDate;
    //     }

    //     public OffsetDateTime getEndDate() {
    //         return endDate;
    //     }

    //     public void setEndDate(OffsetDateTime endDate) {
    //         this.endDate = endDate;
    //     }

    //     public String getLeaveType() {
    //         return leaveType;
    //     }

    //     public void setLeaveType(String leaveType) {
    //         this.leaveType = leaveType;
    //     }

    //     public String getStatus() {
    //         return status;
    //     }

    //     public void setStatus(String status) {
    //         this.status = status;
    //     }

    //     public String getDescription() {
    //         return description;
    //     }

    //     public void setDescription(String description) {
    //         this.description = description;
    //     }
    // }


}
