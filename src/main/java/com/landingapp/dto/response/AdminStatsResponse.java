package com.landingapp.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AdminStatsResponse {
    private Long totalUsers;
    private Long totalLandings;
    private Long publishedLandings;
    private List<AdminLandingResponse> recentLandings;
}
