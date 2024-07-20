package com.example.demo.repo;

import com.example.demo.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepo extends JpaRepository<Exercise,Long> {
    Exercise findBycode(String code);
}
