package com.kkiri_trip.back.global.error.exception;

import com.kkiri_trip.back.global.error.errorcode.DashboardErrorCode;
import com.kkiri_trip.back.global.error.errorcode.PlaceErrorCode;
import lombok.Getter;

@Getter
public class PlaceExceptoin extends RuntimeException {
    private final PlaceErrorCode errorCode;

    public PlaceExceptoin(PlaceErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}