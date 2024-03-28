    package com.example.myshopdaknong.entity;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotNull;
    import lombok.Getter;
    import lombok.Setter;
    import org.hibernate.validator.constraints.Length;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.Date;

    @Entity(name = "Users")
    @Getter
    @Setter
    public class Users {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotNull
        @NotBlank(message = "User Name Is Required")
        @Length(min = 6,message = "User Name must be at least 6 characters")
        @Column(name = "user_name", length = 255, nullable = false)
        private String userName;

        @NotBlank(message = "Password Is Required")
        @Length(min = 6,max = 128,message = "Password must be at least 6 characters")
        @Column(name = "password", length = 255, nullable = false)
        private String password;

        @NotBlank(message = "First Name Is Required")
        @Getter
        @Column(name = "first_name", length = 255, nullable = false)
        private String firstName;

        @NotBlank(message = "Last Name Is Required")
        @Column(name = "last_name", length = 255, nullable = false)
        private String lastName;

        @Column(name = "created_at", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @CreatedDate
        private Date createdAt;

        @Column(name = "updated_at")
        @Temporal(TemporalType.TIMESTAMP)
        @LastModifiedDate
        private Date updatedAt;

        @Column(name = "deleted_at")
        private Date deletedAt;

        @Column(name = "is_active")
        private Boolean isActive;

        @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
        @JoinTable(name = "users_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "roles_id"))
        private Collection<Roles> listRoles = new ArrayList<>();

        public Users() {
        }

        public Users(String userName, String password, String firstName, String lastName, Date deletedAt, Boolean isActive) {
            this.userName = userName;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.deletedAt = deletedAt;
            this.isActive = isActive;
        }

        public Users(String userName, String password, String firstName, String lastName) {
            this.userName = userName;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.createdAt = new Date();
        }

        @Override
        public String toString() {
            return "Users{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", password='" + password + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", createdAt=" + createdAt +
                    ", updatedAt=" + updatedAt +
                    ", deletedAt=" + deletedAt +
                    ", isActive=" + isActive +
                    ", listRoles=" + listRoles +
                    '}';
        }

        public Boolean getActive() {
            return isActive;
        }

        public String getFullName() {
            if (this.firstName != null && this.lastName != null && !this.firstName.isEmpty() && !this.lastName.isEmpty()) {
                return this.lastName + " " + this.firstName;
            }
            return null;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public void addRoles(Roles roles) {
            this.listRoles.add(roles);
        }
    }
