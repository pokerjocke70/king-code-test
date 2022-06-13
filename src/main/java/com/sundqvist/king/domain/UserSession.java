package com.sundqvist.king.domain;

import java.time.LocalDateTime;

public record UserSession(String id, LocalDateTime expiration) {
}
