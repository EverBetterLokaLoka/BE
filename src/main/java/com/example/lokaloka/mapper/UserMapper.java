package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.reqdto.ProfileReqDTO;
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

    // ✅ Mapping từ User -> UserResDTO, xử lý danh sách itineraries
    @Mapping(target = "itineraries", source = "itineraries")
    UserResDTO toUserResDTO(User user);

    // ✅ Mapping từ User -> ProfileReqDTO
    @Mapping(target = "full_name", source = "full_name")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "gender", source = "gender")
    @Mapping(target = "dob", source = "dob")
    @Mapping(target = "emergency_number", source = "emergency_numbers")
    @Mapping(target = "updatedAt",source = "updated_at")
    @Mapping(target = "email",source = "email")
    ProfileReqDTO toProfileReqDTO(User user);

    // ✅ Mapping từ ProfileReqDTO -> User, nhưng chỉ cập nhật những field không null
    @Mapping(target = "full_name", expression = "java(dto.getFull_name() != null ? dto.getFull_name() : user.getFull_name())")
    @Mapping(target = "phone", expression = "java(dto.getPhone() != null ? dto.getPhone() : user.getPhone())")
    @Mapping(target = "address", expression = "java(dto.getAddress() != null ? dto.getAddress() : user.getAddress())")
    @Mapping(target = "gender", expression = "java(dto.getGender() != null ? dto.getGender() : user.getGender())")
    @Mapping(target = "dob", expression = "java(dto.getDob() != null ? dto.getDob() : user.getDob())")
    @Mapping(target = "emergency_numbers", expression = "java(dto.getEmergency_number() != null ? dto.getEmergency_number() : user.getEmergency_numbers())")
    void updateUserFromDTO(ProfileReqDTO dto, @MappingTarget User user);

    // ✅ Mapping từ Itinerary -> ItineraryResDTO
    @Mapping(target = "locations", source = "locations")
    ItineraryResDTO toItineraryResDTO(Itinerary itinerary);

    // ✅ Mapping từ Location -> LocationResDTO
    @Mapping(target = "activities", source = "activities")
    LocationResDTO toLocationResDTO(Location location);

    // ✅ Mapping từ Activity -> ActivityResDTO
    ActivityResDTO toActivityResDTO(Activity activity);

    // ✅ Danh sách
    List<ItineraryResDTO> toItineraryResDTOList(List<Itinerary> itineraries);
    List<LocationResDTO> toLocationResDTOList(List<Location> locations);
    List<ActivityResDTO> toActivityResDTOList(List<Activity> activities);
}
