package tech.bread.solt.doctornyangserver.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@NoArgsConstructor
public class IdCheckRequestDto {
    private String id;
}
