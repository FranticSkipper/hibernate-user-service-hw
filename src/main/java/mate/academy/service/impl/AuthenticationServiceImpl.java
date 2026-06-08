package mate.academy.service.impl;

import java.util.Optional;
import javax.naming.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userOptional = this.userService.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new AuthenticationException("Invalid email");
        }

        User user = userOptional.get();
        String hashedPassword = HashUtil.hashPassword(password, user.getSalt());

        if (!hashedPassword.equals(user.getHashedPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        return user;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        try {
            Optional<User> userOptional = this.userService.findByEmail(email);

            if (userOptional.isPresent()) {
                throw new RegistrationException("User already exists");
            }

            User user = new User();
            byte[] salt = HashUtil.getSalt();
            user.setSalt(salt);
            HashUtil.hashPassword(password, user.getSalt());
            user.setEmail(email);

            return this.userService.add(user);
        } catch (Exception e) {
            throw new RegistrationException("Can't register new user. Params: " + email, e);
        }

    }
}
