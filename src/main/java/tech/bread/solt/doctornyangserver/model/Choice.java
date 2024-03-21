package tech.bread.solt.doctornyangserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Choice implements Serializable {
    private GPTMessage message;
    private Integer index;
    @JsonProperty("finish_reason")
    private String finishReason;
}