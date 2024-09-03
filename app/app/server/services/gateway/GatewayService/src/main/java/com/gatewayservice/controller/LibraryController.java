package com.gatewayservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gatewayservice.config.ActionType;
import com.gatewayservice.dto.*;
import com.gatewayservice.jwt.JwtAuthentication;
import com.gatewayservice.producer.StatsProducer;
import com.gatewayservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "LIBRARIES")
@RequestMapping("/libraries")
public class LibraryController {
    /**
     * Контроллер, работающий с библиотеками
     */
    private final LibraryService libraryService;
    private final StatsProducer producer;


    public LibraryController(LibraryService libraryService, StatsProducer producer) {
        this.libraryService = libraryService;
        this.producer = producer;
    }

    /**
     * Получение списка библиотек по городу
     * @param city город, в котором ищем библиотеки
     * @param page параметр пагинации
     * @param size параметр пагинации
     * @return список библиотек в указанном городе
     */
    @Operation(summary = "Получить список библиотек в городе")
    @GetMapping()
    public ResponseEntity<LibraryPaginationResponse> getLibrariesByCity(@RequestParam(value = "city", required = true) String city,
                                                                        @RequestParam(value = "page", required = false) Integer page,
                                                                        @RequestParam(value = "size", required = false) Integer size) {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String token = auth.getToken();
        String username = auth.getUsername();
        ResponseEntity<ArrayList<LibraryResponse>> response = libraryService.getLibrariesByCity(city, token);
        ArrayList<LibraryResponse> allLibs = response.getBody();
        HttpStatus status = response.getStatusCode();

        ResponseEntity<LibraryPaginationResponse> res;
        if (status != HttpStatus.OK)
            return ResponseEntity.status(status).build();

        if (page == null)
            res = ResponseEntity.status(status).body(new LibraryPaginationResponse(1, 1, allLibs.size(), allLibs));

        else {
            int maxPage = allLibs.size() / size + 1;

            if (page > maxPage)
                page = maxPage;

            ArrayList<LibraryResponse> pageLibs =
                    new ArrayList<>(allLibs.subList((page - 1) * size, Integer.min(page * size, allLibs.size())));

            res = ResponseEntity.status(status).body(new LibraryPaginationResponse(page, size, allLibs.size(), pageLibs));
        }

        try {
            Integer finalPage = page;
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.LIBRARIES_BY_CITY,
                            new HashMap<String, String>() {{
                                put("city", city);
                                put("page", String.valueOf(finalPage.intValue()));
                                put("size", String.valueOf(size.intValue()));
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }

    /**
     * Получение списка книг в выбранной библиотеке
     * @param libraryUid UUID библиотеки, в которой хотим получить список книг
     * @param page параметр пагинации
     * @param size параметр пагинации
     * @param showAll true - показывать недоступные для аренды книги
     */
    @Operation(summary = "Получить список книг в библиотеке")
    @GetMapping("/{libraryUid}/books")
    public ResponseEntity<LibraryBookPaginationResponse> getBooksInLibrary(@PathVariable UUID libraryUid,
                                                                      @RequestParam(value = "page", required = false) Integer page,
                                                                      @RequestParam(value = "size", required = false) Integer size,
                                                                      @RequestParam(value = "showAll", required = false, defaultValue = "false") boolean showAll)  {
        LocalDateTime startDate = LocalDateTime.now();
        JwtAuthentication auth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        String token = auth.getToken();
        String username = auth.getUsername();
        ResponseEntity<ArrayList<LibraryBookResponse>> response = libraryService.getBooksByLibrary(libraryUid, showAll, token);
        ArrayList<LibraryBookResponse> books = response.getBody();
        HttpStatus status = response.getStatusCode();
        ResponseEntity<LibraryBookPaginationResponse> res = null;

        if (status != HttpStatus.OK)
            res = ResponseEntity.status(status).build();

        if (page == null)
            res = ResponseEntity.status(status).body(new LibraryBookPaginationResponse(1, 1, books.size(), books));

        int maxPage = books.size() / size + 1;

        if (page > maxPage)
            page = maxPage;

        ArrayList<LibraryBookResponse> pageLibs =
                new ArrayList<>(books.subList((page - 1) * size, Integer.min(page * size, books.size())));

        res = ResponseEntity.status(status).body(new LibraryBookPaginationResponse(page, size, books.size(), pageLibs));

        try {
            Integer finalPage = page;
            producer.sendMessage(
                    new StatMessage(
                            username, startDate, LocalDateTime.now(), ActionType.BOOKS_BY_LIBRARY,
                            new HashMap<String, String>() {{
                                put("uuid", libraryUid.toString());
                                put("page", String.valueOf(finalPage.intValue()));
                                put("size", String.valueOf(size.intValue()));
                                put("show_all", String.valueOf(showAll));
                            }}));
        } catch (JsonProcessingException e) {
            log.error("[GATEWAY] error sending kafka msg, {}", e.getMessage());
        }

        return res;
    }
}
