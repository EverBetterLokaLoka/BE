package com.example.lokaloka.service.impl;

import com.example.lokaloka.domain.dto.resdto.ItineraryResDTO;
import com.example.lokaloka.domain.dto.resdto.UserResDTO;
import com.example.lokaloka.domain.entity.Itinerary;
import com.example.lokaloka.domain.entity.User;
import com.example.lokaloka.mapper.ItineraryMapper;
import com.example.lokaloka.repository.IItineraryRepository;
import com.example.lokaloka.repository.IUserRepository;
import com.example.lokaloka.service.IItineraryService;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ItineraryService  implements IItineraryService {
    IItineraryRepository itineraryRepository;
    IUserRepository userRepository;
    ItineraryMapper itineraryMapper;

    @Override
    public List<ItineraryResDTO> getAllItineraries() {
        User user = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));

        List<Itinerary> itineraries = itineraryRepository.findByUser(user);
        return itineraries.stream()
                .map(itineraryMapper::toItineraryResDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ItineraryResDTO getItineraryById(Long id) {
        return null;
    }

    @Override
    public ItineraryResDTO createItinerary(ItineraryResDTO itinerary) {
        return null;
    }

    @Override
    public void deleteItineraryById(Long id) {

    }
}
