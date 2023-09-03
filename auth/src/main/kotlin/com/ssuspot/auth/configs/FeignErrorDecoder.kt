package com.ssuspot.auth.configs

import com.fasterxml.jackson.databind.ObjectMapper
import com.ssuspot.auth.exception.InternalServerErrorException
import com.ssuspot.auth.response.ErrorResponse
import feign.Response
import feign.codec.ErrorDecoder
import java.nio.charset.Charset

class FeignErrorDecoder(
        private val objectMapper: ObjectMapper
) : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response?): Exception {
        val errorResponse =
                objectMapper.readValue(response!!.body().asReader(Charset.defaultCharset()), ErrorResponse::class.java)
        throw InternalServerErrorException(errorResponse)
    }
}