    package com.example.myshopdaknong.Sercurity;
    
    import com.example.myshopdaknong.Entity.Users;
    import com.example.myshopdaknong.Entity.roles;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;
    
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;
    
    public class ShopMeUserDetail implements UserDetails {
        private Users users;
        private static final long serialVersionUID = 8434638013158790457L; // Cập nhật serialVersionUID


        public ShopMeUserDetail(Users user) {
                this.users=user;
            }
    
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            Collection<roles> roles=  users.getListRoles();
            List<SimpleGrantedAuthority> authorities=new ArrayList<>();
            for(roles role : roles)
            {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return authorities;
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
