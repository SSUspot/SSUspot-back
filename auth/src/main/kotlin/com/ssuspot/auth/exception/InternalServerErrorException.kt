package com.ssuspot.auth.exception

import com.ssuspot.auth.response.ErrorResponse

class InternalServerErrorException(val error: ErrorResponse) : RuntimeException()