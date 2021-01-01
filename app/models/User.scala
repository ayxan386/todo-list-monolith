package models

import java.time.LocalDateTime
import java.util.UUID

case class User(id: UUID,
                nickname: String,
                password: String,
                creationDate: Option[LocalDateTime],
                lastLoginDate: Option[LocalDateTime])
