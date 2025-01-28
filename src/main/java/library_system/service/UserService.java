package library_system.service;

import library_system.dto.UserDTO;
import library_system.dto.UserLoginDTO;
import library_system.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();
    User registerUser(UserDTO userDTO);

}
