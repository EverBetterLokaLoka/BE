package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.reqdto.UserReqDTO;
import com.example.lokaloka.domain.dto.resdto.ActivityResDTO;
import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.dto.resdto.LocationResDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.domain.entity.Activity;
import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.domain.entity.Location;
import com.example.lokaloka.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "itineraries", source = "itineraries")
    UserResDTO toUserResDTO(User user);

    @Mapping(target = "locations", source = "locations")
    ItineraryResDTO toItineraryResDTO(Itinerary itinerary);

    @Mapping(target = "activities", source = "activities")
    LocationResDTO toLocationResDTO(Location location);

    ActivityResDTO toActivityResDTO(Activity activity);

    List<ItineraryResDTO> toItineraryResDTOList(List<Itinerary> itineraries);

    List<LocationResDTO> toLocationResDTOList(List<Location> locations);

    List<ActivityResDTO> toActivityResDTOList(List<Activity> activities);

    void updateUserFromDTO(UserReqDTO dto, @MappingTarget User user);
}

