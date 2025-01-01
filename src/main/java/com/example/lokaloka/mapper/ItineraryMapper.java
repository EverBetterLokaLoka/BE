package com.example.lokaloka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItineraryMapper {
    ItineraryMapper INSTANCE = Mappers.getMapper(ItineraryMapper.class);
}
