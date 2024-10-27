package org.example.springjwt.service;

import org.example.springjwt.entity.Users;
import org.example.springjwt.repository.UsersRepo;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepo usersRepo;

    public UsersService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;

    }

    public Users getByUsername(String username)
    {
        return usersRepo.findUsersByUsername(username);
    }

    public Users register(Users user) {

        return usersRepo.save(user);
    }
}
