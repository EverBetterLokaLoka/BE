package com.example.lokaloka.mapper;

import com.example.lokaloka.domain.dto.resdto.ImageResDTO; // DTO cho hình ảnh
import com.example.lokaloka.domain.entity.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResDTO toImageResDTO(Image image);
}