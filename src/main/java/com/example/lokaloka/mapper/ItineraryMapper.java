package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.entity.Itinerary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocationMapper.class})
public interface ItineraryMapper {
    ItineraryResDTO toItineraryResDTO(Itinerary itinerary);

}