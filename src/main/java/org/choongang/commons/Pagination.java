package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Pagination {
    private int page;
    private int total;
    private int ranges;
    private int limit;

    private int firstRangePage; // 구간별 첫 페이지
    private int lastRangePage; // 구간별 마지막 페이지

    private int prevRangePage; // 이전 구간 첫 페이지 번호
    private int nextRangePage; // 다음 구간 첫 페이지 번호

    private int totalPages; // 전체 페이지 갯수
    private String baseURL; // 페이징 쿼리스트링 기본 URL

    public Pagination(int page, int total, int ranges, int limit) {
        this(page, total, ranges, limit, null);
    }

    /**
     *
     * @param page : 현재 페이지
     * @param total : 전체 레코드 갯수
     * @param ranges : 페이지 구간 갯수
     * @param limit : 한 페이지 당 레코드 갯수
     */
    public Pagination(int page, int total, int ranges, int limit, HttpServletRequest request) {

        page = Utils.onlyPositiveNumber(page, 1);
        total = Utils.onlyPositiveNumber(total, 0);
        ranges = Utils.onlyPositiveNumber(ranges, 10);
        limit = Utils.onlyPositiveNumber(limit, 20);

        // 전체 페이지 갯수
        int totalPages = (int) Math.ceil(total / (double) limit);

        // 구간 번호
        int rangeCnt = (page - 1) / ranges;
        int firstRangePage = rangeCnt * ranges + 1;
        int lastRangePage = firstRangePage + ranges - 1;
        lastRangePage = lastRangePage > totalPages ? totalPages : lastRangePage;

        // 이전 구간 첫 페이지
        if (rangeCnt > 0) {
            prevRangePage = firstRangePage - ranges;
        }

        // 다음 구간 첫 페이지
        // 마지막 구간 번호
        int lastRangeCnt = (totalPages -1) / ranges;
        if(rangeCnt < lastRangeCnt) { // 마지막 구간이 아닌 경우 구간 첫 페이지
            nextRangePage = firstRangePage + ranges;
        }
        /**
         * 쿼리스트링 값 유지 처리 - 쿼리스트링 값 중에서 page만 제외하고 다시 조합
         * ex) ?orderStautus=CASH&name=...&page=2 -> ?orderStatus=CASH&name=...
         *      페이지만 있을 때 ?page=2 -> ?로 대체
         *      쿼리스트링 없는 경우 -> ?로 대체
         *
         *      &로 문자열 분리하여 키와 값 형태로 바꾼다 -> page를 제외하기
         *      { "orderStatus=CASH", "name=...", "page=2" }
         */
        String baseURL = "?";
        if(request != null) {
            String queryString = request.getQueryString();
            if(StringUtils.hasText(queryString)) {
                queryString = queryString.replace("?" , ""); // ? 제거

                baseURL += Arrays.stream(queryString.split("&")) // &로 분리
                        .filter(s -> !s.contains("page=")) // page 제외
                        .collect(Collectors.joining("&")); // 문자열로 바꾼 후 &으로 연결하기

                baseURL = baseURL.length() > 1 ? baseURL += "&" : baseURL;
            }

        }

        this.page = page;
        this.total = total;
        this.ranges = ranges;
        this.limit = limit;
        this.firstRangePage = firstRangePage;
        this.lastRangePage = lastRangePage;
        this.totalPages = totalPages;
        this.baseURL = baseURL;

    }
    public List<String[]> getPages() {
        // 0 : 페이지 번호, 1 : 페이지 URL - ?page=페이지번호

        return IntStream.rangeClosed(firstRangePage, lastRangePage)
                .mapToObj(p -> new String[] { String.valueOf(p),
                        baseURL + "page=" + p})
                .toList();

    }

}
