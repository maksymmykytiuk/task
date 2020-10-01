package com.maksym.task.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "'login' cannot be empty")
    @Column(unique = true)
    private String login;

    @NotNull(message = "'fullName' cannot be empty")
    private String fullName;

    @NotNull(message = "'dob' cannot be empty")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;

    @NotNull(message = "'gender' cannot be empty")
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
