package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.*;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.Address;
import fr.ndev.insurance.model.Phone;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<?> addPhone(PhoneDTO phoneDTO, Long userId) {
        try {
            User user = getUser(userId);
            Phone phone = phoneDTO.toPhone();
            user.addPhone(phone);
            userRepository.save(user);
            return ResponseEntity.ok(PhoneDTO.of(phone));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deletePhone(int index, Long userId) {
        try {
            User user = getUser(userId);
            index = index - 1;
            int phoneCount = user.getPhones().size();
            if (index < 0 || index >= phoneCount) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Phone number not found"));
            } else if (phoneCount == 1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Cannot delete the only phone number"));
            } else if (user.getPhones().get(index).isMain()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Cannot delete the main phone number"));
            }
            user.deletePhone(index);
            userRepository.save(user);
            return ResponseEntity.ok(new JsonResponse(HttpStatus.OK, "Phone number deleted successfully"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updatePhone(int index, PhoneDTO phoneDTO, Long userId) {
        try {
            User user = getUser(userId);
            Phone phone = phoneDTO.toPhone();
            index = index - 1;
            if (index < 0 || index >= user.getPhones().size()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Phone number not found"));
            }
            user.updatePhone(index, phone);
            userRepository.save(user);
            return ResponseEntity.ok(PhoneDTO.of(phone));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> choosePhoneAsMain(int index, Long userId) {
        try {
            User user = getUser(userId);
            index = index - 1;
            if (user.getPhones().get(index) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Phone number not found"));
            } else if (user.getPhones().get(index).isMain()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Phone number is already the main phone number"));
            }
            user.choosePhoneAsMain(index);
            userRepository.save(user);
            Phone phone = user.getPhones().get(index);
            return ResponseEntity.ok(PhoneDTO.of(phone));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllPhones(Long userId) {
        try {
            User user = getUser(userId);
            return ResponseEntity.ok(user.getPhones().stream()
                    .map(PhoneDTO::of)
                    .toList());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> addAddress(AddressDTO addressDTO, Long userId) {
        try {
            User user = getUser(userId);
            Address address = addressDTO.toAddress();
            user.addAddress(address);
            userRepository.save(user);
            return ResponseEntity.ok(AddressDTO.of(address));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteAddress(int index, Long userId) {
        try {
            User user = getUser(userId);
            index = index - 1;
            int addressCount = user.getAddresses().size();
            if (index < 0 || index >= addressCount) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Address not found"));
            } else if (addressCount == 1) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Cannot delete the only address"));
            } else if (user.getAddresses().get(index).isMain()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Cannot delete the main address"));
            }
            user.deleteAddress(index);
            userRepository.save(user);
            return ResponseEntity.ok(new JsonResponse(HttpStatus.OK, "Address deleted successfully"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateAddress(int index, AddressDTO addressDTO, Long userId) {
        try {
            User user = getUser(userId);
            Address address = addressDTO.toAddress();
            index = index - 1;
            if (index < 0 || index >= user.getAddresses().size()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Address not found"));
            }
            user.updateAddress(index, address);
            userRepository.save(user);
            return ResponseEntity.ok(AddressDTO.of(address));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> chooseAddressAsMain(int index, Long userId) {
        try {
            User user = getUser(userId);
            index = index - 1;
            if (user.getAddresses().get(index) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Address not found"));
            } else if (user.getAddresses().get(index).isMain()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Address is already the main address"));
            }
            user.chooseAddressAsMain(index);
            userRepository.save(user);
            Address address = user.getAddresses().get(index);
            return ResponseEntity.ok(AddressDTO.of(address));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public ResponseEntity<?> getAllAddresses(Long userId) {
        try {
            User user = getUser(userId);
            return ResponseEntity.ok(user.getAddresses().stream()
                    .map(AddressDTO::of)
                    .toList());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateEmail(String email, Long userId) {
        try {
            User user = getUser(userId);
            user.setEmail(email);
            userRepository.save(user);
            return ResponseEntity.ok(UserDTO.of(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updatePassword(PasswordRequest passwordRequest, Long userId) {
        try {
            User user = getUser(userId);
            String newPassword = passwordRequest.getNewPassword();
            String currentPassword = passwordRequest.getCurrentPassword();
            String confirmPassword = passwordRequest.getConfirmPassword();
            if (!confirmPassword.equals(newPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new JsonResponse(HttpStatus.BAD_REQUEST, "Passwords do not match"));
            }
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new JsonResponse(HttpStatus.BAD_REQUEST, "Current password is incorrect"));
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok(UserDTO.of(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateFirstname(String firstname, Long userId) {
        try {
            User user = getUser(userId);
            user.setFirstname(firstname);
            userRepository.save(user);
            return ResponseEntity.ok(UserDTO.of(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateLastname(String lastname, Long userId) {
        try {
            User user = getUser(userId);
            user.setLastname(lastname);
            userRepository.save(user);
            return ResponseEntity.ok(UserDTO.of(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateProfile(ProfileRequest profileRequest, Long userId) {
        try {
            User user = getUser(userId);
            boolean isModified = false;
            if (profileRequest.getEmail() != null && !profileRequest.getEmail().isEmpty()) {
                user.setEmail(profileRequest.getEmail());
                isModified = true;
            }
            if (profileRequest.getPassword() != null && !profileRequest.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
                isModified = true;
            }
            if (profileRequest.getFirstname() != null && !profileRequest.getFirstname().isEmpty()) {
                user.setFirstname(profileRequest.getFirstname());
                isModified = true;
            }
            if (profileRequest.getLastname() != null && !profileRequest.getLastname().isEmpty()) {
                user.setLastname(profileRequest.getLastname());
                isModified = true;
            }
            if (isModified) {
                userRepository.save(user);
                return ResponseEntity.ok(UserDTO.of(user));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse(HttpStatus.BAD_REQUEST, "Blank informations"));
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public ResponseEntity<?> getProfile(Long userId) {
        try {
            User user = getUser(userId);
            return ResponseEntity.ok(UserDTO.of(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> changeRole(Long userId, Role role) {
        try {
            User user = getUser(userId);
            if (role == user.getRole()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Role is already set to " + role));
            }
            user.setRole(role);
            userRepository.save(user);
            return ResponseEntity.ok(UserDTO.of(user));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public List<UserDTOwithId> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTOwithId::of)
                .toList();
    }

    public User getUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(userId == null){
            return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        }
    }

}
