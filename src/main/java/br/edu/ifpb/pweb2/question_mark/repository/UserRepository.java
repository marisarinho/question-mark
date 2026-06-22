package br.edu.ifpb.pweb2.question_mark.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpb.pweb2.question_mark.model.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
