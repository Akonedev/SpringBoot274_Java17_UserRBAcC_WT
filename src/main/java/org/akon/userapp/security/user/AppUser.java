package org.akon.userapp.security.user;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
public class AppUser {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name="id")
    private Long id;

    private String email;

    @Enumerated
    @Column(columnDefinition = "tinyint")
    private Gender gender;

    @Size(min = 2, max = 25, message = "Firstname Entre 2 et 25 caracteres SVP")
    private String lastName;

    @Size(min = 2, max = 25, message = "Lastname Entre 2 et 25 caracteres SVP")
    private String firstName;


    //    @Column(name="username", nullable = false)
    @Size(min = 2, max = 25, message = "Firstname Entre 2 et 25 caracteres SVP")
    private String username;

//    @Column(name="password", nullable = false)
    private String password;


    @Column(name = "birth_date")
    private java.time.LocalDate birthDate;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Contact contact;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Address address;

    @Column(name="enabled")
    private boolean enabled;

    @Column(name="note")
    private String note;


    @Basic
    private java.time.LocalDateTime creationDt;

    @Basic
    private java.time.LocalDateTime updatedDt;

    @Basic
    private java.time.LocalDateTime loginDt;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name="secured")
    private boolean secured;

    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinDate;
    private String role;
    private String[] authorities;
    private boolean isActive;
    private boolean isNotLocked;

    public AppUser() {
    }

    public AppUser(String email) {
        this.email = email;
    }



//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }


//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//

//    @Id
//    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
//    @GenericGenerator(name = "native", strategy = "native")
//    @Column(name="id")
//    private Long id;

//    @Column(name="username", nullable = false)
//    private String username;
//
//    @Column(name="password", nullable = false)
//    private String password;
//
//    @Column(name="name", nullable = false)
//    private String name;

//    @Size(min = 2, max = 25, message = "Firstname Entre 2 et 25 caracteres SVP")
//    private String lastName;


//    @Size(min = 2, max = 25, message = "Lastname Entre 2 et 25 caracteres SVP")
//    private String firstName;

//    @Column(name="surname", nullable = false)
//    private String surname;
//
//
//    //    @Column(name="email", nullable = true)
//    private String email;



//    // Birth date without a time-zone in the ISO-8601 calendar system, such as 2007-12-03
//    @Column(name = "birth_date")
//    private java.time.LocalDate birthDate;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Contact contact;
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//    private Address address;
//
//    @Column(name="enabled")
//    private boolean enabled;
//
//    @Column(name="note")
//    private String note;



//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return false;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return false;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return false;
//    }
}