package com.example.backend.api.utils.exception
enum class ErrorCode(
    val value: Int
){
    NO_CODE(0),
    NULL_BODY_ERROR_CODE(1),
    PARSE_ERROR_CODE(2),
    LOCAL_ERROR_CODE(3),
    NETWORK_ERROR(4),
    SERVER_ERROR(5),
}