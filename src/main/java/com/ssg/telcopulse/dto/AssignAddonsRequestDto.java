package com.ssg.telcopulse.dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;

public record AssignAddonsRequestDto(@NotEmpty(message = "At least one add-on package ID must be provided")
    List<Long> addonIds) {

}
