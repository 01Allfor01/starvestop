package com.allforone.starvestop.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class SliceResponse<T> {
    private final List<T> content;
    private final int size;
    private final boolean hasNext;
    private final boolean first;
    private final boolean last;

    public static <T> SliceResponse<T> from(Slice<T> slice) {
        return new SliceResponse<>(
                slice.getContent(),
                slice.getSize(),
                slice.hasNext(),
                slice.isFirst(),
                slice.isLast()
        );
    }
}
