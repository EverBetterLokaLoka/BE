package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.resdto.ImageResDTO;
import com.example.lokaloka.domain.dto.resdto.LocationResDTO;
import com.example.lokaloka.domain.entity.Image;
import com.example.lokaloka.domain.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface LocationMapper {

    @Mapping(source = "itinerary.id", target = "itineraryId")
    @Mapping(source = "activities", target = "activities")
    @Mapping(source = "image_url", target = "image")
    LocationResDTO toLocationResDTO(Location location);

    // Đảm bảo có phương thức ánh xạ từ Image nếu cần
//    default List<Image> mapImages(List<Image> images) {
//        return images; // Hoặc thực hiện chuyển đổi nếu cần
//    }
}