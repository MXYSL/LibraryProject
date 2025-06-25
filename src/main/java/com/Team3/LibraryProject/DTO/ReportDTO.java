package com.Team3.LibraryProject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private String reportType;
    private String period;
    private List<Object[]> data;
    private BigDecimal totalAmount;
    private Long totalCount;
    private String summary;
}