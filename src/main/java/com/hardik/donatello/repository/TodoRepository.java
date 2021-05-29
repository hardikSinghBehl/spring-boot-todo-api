package com.hardik.donatello.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hardik.donatello.entity.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {

}
