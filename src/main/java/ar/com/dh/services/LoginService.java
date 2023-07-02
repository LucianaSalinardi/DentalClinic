//package ar.com.dh.services;
//
//import ar.com.dh.dtos.responses.UserResponseDto;
//import ar.com.dh.entities.User;
//import ar.com.dh.exceptions.ApiException;
//import ar.com.dh.repository.UserRepository;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LoginService {
//
//    private final UserRepository userRepository;
//
//    public LoginService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    /**
//     * @param email user email
//     * @param password user password
//     * @return DTO with login information
//     * @throws ApiException
//     */
//    public UserResponseDto login(String email, String password) throws ApiException {
//
//        User user = userRepository.findByEmailAndPassword(email, password);
//
//        if (user != null) {
//
//            UserResponseDto userResponse = new UserResponseDto();
//            userResponse.setUsername(username);
//
//            return session;
//        } else {
//            throw new ApiException("404", "Usuario y/o contraseña incorrecto", 404);
//        }
//
//    }
//}
