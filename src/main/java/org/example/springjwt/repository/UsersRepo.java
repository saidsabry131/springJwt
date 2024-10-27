package org.example.springjwt.repository;

import org.example.springjwt.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users,Integer> {

    Users findUsersByUsername(String username);
}
