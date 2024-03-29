    package com.example.myshopdaknong.sercurity;
    
    import com.example.myshopdaknong.entity.User;
    import com.example.myshopdaknong.entity.Role;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
    @Data
    @AllArgsConstructor
    public class ShopMeUserDetail implements UserDetails {
        private User users;
        private static final long serialVersionUID = 8434638013158790457L; // Cập nhật serialVersionUID

//        public ShopMeUserDetail(Users users) {
//            this.users = users;
//        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<Role> roles=  users.getListRoles();
            List<SimpleGrantedAuthority> authorities=new ArrayList<>();
            for(Role role : roles)
            {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return authorities;
        }
        // Thêm method để truy cập ID của Users
        public Integer getUserId() {
            return this.users.getId();
        }
        @Override
        public String getPassword() {
            return this.users.getPassword();
        }
    
        @Override
        public String getUsername() {
            return this.users.getUserName();
        }
    
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
    
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
    
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
    
        @Override
        public boolean isEnabled() {
            return this.users.getActive();
        }
        public String getFullName() {
            return this.users.getLastName()+" "+this.users.getFirstName();
        }
    }
