    package com.example.sm.minh.eshop.models;

    import jakarta.persistence.*;
    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.annotation.LastModifiedDate;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.Date;

    @Entity(name = "Users")
    @Getter
    @Setter
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;


        @Column(name = "user_name", length = 100, nullable = false)
        private String userName;


        @Column(name = "password", length = 50, nullable = false)
        private String password;

        @Column(name = "first_name", length = 50, nullable = false)
        private String firstName;

        @Column(name = "last_name", length = 50, nullable = false)
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
        private Collection<Role> listRoles = new ArrayList<>();

        public User() {
        }

        public User(String userName, String password, String firstName, String lastName, Date deletedAt, Boolean isActive) {
            this.userName = userName;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.deletedAt = deletedAt;
            this.isActive = isActive;
        }

        public User(String userName, String password, String firstName, String lastName) {
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

        public void addRoles(Role roles) {
            this.listRoles.add(roles);
        }
    }
