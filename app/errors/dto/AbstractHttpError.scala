package errors.dto

abstract class AbstractHttpError(val message: String, val status: Int)
    extends RuntimeException(message) {}
