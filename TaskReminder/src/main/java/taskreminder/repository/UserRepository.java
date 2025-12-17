package taskreminder.repository;


import taskreminder.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteByUsername(String username);
}
