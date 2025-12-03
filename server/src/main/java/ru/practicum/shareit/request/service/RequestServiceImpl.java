package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptionType.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseDto addNewRequest(Long userId, RequestDto dto) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        Request request = RequestMapper.toEntity(dto, userId);
        request.setCreated(LocalDateTime.now());
        Request saved = requestRepository.save(request);

        return RequestMapper.toDto(saved, List.of());
    }

    @Override
    public ResponseDto getRequestById(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id = " + requestId + " не найден"));

        List<Item> items = itemRepository.findByRequestId(requestId);

        return RequestMapper.toDto(request, items);
    }

    @Override
    public List<ResponseDto> getUserRequests(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        List<Request> requests = requestRepository.findByRequesterIdOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }


        List<Long> requestIds = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList());


        List<Item> allItems = itemRepository.findByRequestIdIn(requestIds);


        Map<Long, List<Item>> itemsByRequestId = allItems.stream()
                .collect(Collectors.groupingBy(Item::getRequestId));

        return requests.stream()
                .map(request -> RequestMapper.toDto(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseDto> getAllRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }

        List<Request> requests = requestRepository.findByRequesterIdNotOrderByCreatedDesc(userId);

        if (requests.isEmpty()) {
            return List.of();
        }


        List<Long> requestIds = requests.stream()
                .map(Request::getId)
                .collect(Collectors.toList());

        List<Item> allItems = itemRepository.findByRequestIdIn(requestIds);

        Map<Long, List<Item>> itemsByRequestId = allItems.stream()
                .collect(Collectors.groupingBy(Item::getRequestId));

        return requests.stream()
                .map(request -> RequestMapper.toDto(
                        request,
                        itemsByRequestId.getOrDefault(request.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }

}
