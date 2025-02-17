package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.resdto.LocationResDTO;
import com.example.lokaloka.domain.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface LocationMapper {
    @Mapping(source = "itinerary.id", target = "itineraryId")
    @Mapping(source = "activities", target = "activities")
    LocationResDTO toLocationResDTO(Location location);
}