package edu.icet.crm.service.admin;

import edu.icet.crm.entity.User;
import edu.icet.crm.enums.UserRole;
import edu.icet.crm.model.UserDto;
import edu.icet.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole()== UserRole.EMPLOYEE )
                .map(User::getUserDto)
                .collect(Collectors.toList());
    }
}
