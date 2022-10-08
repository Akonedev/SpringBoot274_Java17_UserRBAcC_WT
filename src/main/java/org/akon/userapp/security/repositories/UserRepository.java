package org.akon.userapp.security.repositories;

import org.akon.userapp.security.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

//    @Query(value = "SELECT u FROM AppUser u WHERE u.contact.email = :email")
//    AppUser findByEmail(@Param("email") String email);
    AppUser findByEmail(String email);

    AppUser findByUsername(String username);

    AppUser findOneByEmail(String email);

}
