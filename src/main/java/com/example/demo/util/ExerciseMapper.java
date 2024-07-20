package com.example.demo.util;


import com.example.demo.dto.ExerciseDTO;
import com.example.demo.entity.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExerciseMapper {

    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    ExerciseDTO toDTO(Exercise exercise);

    Exercise toEntity(ExerciseDTO dto);
}
