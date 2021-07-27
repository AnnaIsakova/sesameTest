package com.sesame.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.util.List;

/**
 * Violation model
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"entityIds"})
@With
public class Violation {

    private String rule;
    private List<String> entityIds;
}
