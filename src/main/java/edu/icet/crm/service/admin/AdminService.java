package edu.icet.crm.service.admin;

import edu.icet.crm.model.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getUsers();

}
