package taskreminder.service;




import taskreminder.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {

 

  

    public String saveNewUser(User user);
        
    public void saveAdmin(User user);
        

    public void saveUser(User user);

    public List<User> getAll() ;

    public Optional<User> findById(Integer id);

    public void deleteById(Integer id);

    public User findByUsername(String username);
}
