package com.example.classhubproject.data.community;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PagingDTO<T> {
    List<T> contents;

    // 현재 페이지 번호
    int currentPageNum;
    // 마지막 페이지 번호
    int lastPageNum;
    //페이지네이션 왼쪽 번호
    int leftPageNum;
    //페이지네이션 오른쪽 번호
    int rightPageNum;

    public PagingDTO(List<T> contents, int currentPageNum, int lastPageNum, int leftPageNum, int rightPageNum) {
        this.contents = contents;
        this.currentPageNum = currentPageNum;
        this.lastPageNum = lastPageNum;
        this.leftPageNum = leftPageNum;
        this.rightPageNum = rightPageNum;
    }
}