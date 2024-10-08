package com.libraryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryResponse {
    String libraryUid;
    String name;
    String address;
    String city;
}
