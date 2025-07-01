package com.api.myFinancePro.service;

import com.api.myFinancePro.dto.UserCreationDTO;
import com.api.myFinancePro.dto.UserResponseDTO;
import com.api.myFinancePro.exception.EmailAlreadyExistsException;
import com.api.myFinancePro.exception.UserNotFoundException;
import com.api.myFinancePro.exception.UsernameAlreadyExistsException;
import com.api.myFinancePro.model.User;
import com.api.myFinancePro.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author alej0nt
 */
@Service
public class UserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Creates a new user in the system.
     * <p>
     * This method takes a UserCreationDTO containing the necessary information
     * and performs validation, password encryption, and persistence.
     *
     * @param userCreationDTO The DTO containing user information to be saved.
     * @return A UserResponseDTO containing the saved user's data.
     * @throws UsernameAlreadyExistsException If the username already exists in the system.
     * @throws EmailAlreadyExistsException    If the email already exists in the system.
     */
    public UserResponseDTO register(UserCreationDTO userCreationDTO) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        User user = convertToUser(userCreationDTO);
        existByEmailAndUsername(user.getEmail(), user.getUsername());
        setPassword(user);
        return convertToUserResponseDTO(userRepository.save(user));
    }

    /**
     * Deletes a user from the database by their ID.
     * <p>
     * This method takes an ID as input and deletes the corresponding user from
     * the database. It does not return any specific data.
     *
     * @param id The ID of the user to be deleted.
     *
     * @throws UserNotFoundException If the user with the provided ID is not found in the database.
     */

    public void removeUser (Integer id) throws UserNotFoundException {
        findById(id);
        userRepository.deleteById(id);
    }


    /**
     * Converts a User entity to a UserResponseDTO.
     * <p>
     * Maps the relevant fields from the User entity to a UserResponseDTO,
     * which is typically returned to the client with non-sensitive information.
     *
     * @param user The User entity to convert.
     * @return A DTO with selected user details.
     */
    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNames(user.getNames());
        dto.setSurnames(user.getSurnames());
        dto.setGender(user.getGender());
        return dto;
    }

    /**
     * Converts a UserCreationDTO to a User entity.
     * <p>
     * Creates a new User instance using the information provided by the client.
     * The returned entity can be saved in the database after password encryption.
     *
     * @param userCreationDTO The DTO containing the user's registration data.
     * @return A populated User entity.
     */
    private User convertToUser(UserCreationDTO userCreationDTO) {
        User user = new User();
        user.setNames(userCreationDTO.getNames());
        user.setSurnames(userCreationDTO.getSurnames());
        user.setEmail(userCreationDTO.getEmail());
        user.setPassword(userCreationDTO.getPassword());
        user.setUsername(userCreationDTO.getUsername());
        user.setGender(userCreationDTO.getGender());
        return user;
    }

    /**
     * Validates the uniqueness of the user's email and username.
     * <p>
     * Checks the database to ensure that neither the provided email
     * nor the username is already registered. If a conflict is found,
     * the appropriate exception is thrown.
     *
     * @param email    The email to check.
     * @param username The username to check.
     * @throws EmailAlreadyExistsException    If the email is already in use.
     * @throws UsernameAlreadyExistsException If the username is already in use.
     */
    private void existByEmailAndUsername(String email, String username)
            throws EmailAlreadyExistsException, UsernameAlreadyExistsException {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
    }

    /**
     * Encrypts and sets the user's password.
     * <p>
     * This method hashes the plain-text password using the configured
     * PasswordEncoder before storing it in the User entity.
     *
     * @param user The user whose password will be encrypted and set.
     */
    private void setPassword(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
    }

    /**
     * Finds a user by their ID.
     * <p>
     * This method returns the User entity associated with the provided ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User entity if found, or null if not found.
     */

    private void findById(Integer id) throws UserNotFoundException {
        if (userRepository.findById(id).isEmpty()){
            throw new UserNotFoundException();
        }
    }
}
