package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.resdto.ActivityResDTO;
import com.example.lokaloka.domain.entity.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    @Mapping(source = "location.id", target = "locationId")
    ActivityResDTO toActivityResDTO(Activity activity);
    default Timestamp map(Timestamp value) {
        return value;
    }
}